package com.stv.quickvod;

import android.app.Activity;
//import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.TextView;

/**   
 * Filename:    play_view.java  
 * Package:     com.stv.quickvod  
 * Description: play_view是播放页面，这个页面处理播放时候的操作，如快进、快退、暂停、播放，通过回调的方式
 *              前端告知当前处于何状态以及当前倍速是多少，再者处理操作超时、系统错误、连接断开等回调消息
 */

public class play_view extends Activity implements OnGestureListener{

	private ImageView returnImageView;
	private ImageView forwardImageView;
	private ImageView backwardImageView;
	private ImageView playImageView;
	private TextView loginfoTextView;
	private TextView speedTextView;
	private ImageView circleImageView;
	
	private boolean is_play = true;
	
	private int count = 0;
	private long keytime = 0;
	private int keyTimeInterval = 300;//定义两次按键时间间隔
	
	private final int PLAYSPEED_1X = 500;
	private final int PLAYSPEED_2X = 200;
	private final int PLAYSPEED_4X = 100;
	private final int PLAYSPEED_8X = 50;
	private final int PLAYSPEED_16X = 25;
	private final int PLAYSPEED_32X = 12;
	
	private final int PLAYSTATUS_FORWARD = 100;
	private final int PLAYSTATUS_BACKWORD = 101;
	
	private int offsetWidth = 1;
	private int offsetHigth = 1;
	private int PLAYSPEED_TIMER = 0;
	
	private GestureDetector detector;
	private DisplayMetrics dMetrics;
	private Rect rect_backward = new Rect();
	private Rect rect_forward = new Rect();
	private Rect rect_play = new Rect();
	private Rect rect_return = new Rect();
	
	private Bundle myBundle = new Bundle();
	private Handler exitsysHandler;
	//private ActivityManager activityMgr;

	private Integer[] images = {
			R.drawable.circle1, R.drawable.circle2, R.drawable.circle3, R.drawable.circle4,
			R.drawable.circle5, R.drawable.circle6, R.drawable.circle7, R.drawable.circle8,
			R.drawable.circle9, R.drawable.circle10, R.drawable.circle11, R.drawable.circle12,
			R.drawable.circle13, R.drawable.circle14, R.drawable.circle15, R.drawable.circle16
		};

	private Handler handler = new Handler();
	private Runnable forwardRunnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			update(PLAYSTATUS_FORWARD);
			handler.postDelayed(this, PLAYSPEED_TIMER);
		}
	};
	
	private Runnable backwardRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			update(PLAYSTATUS_BACKWORD);
			handler.postDelayed(this, PLAYSPEED_TIMER); 
		}
	};
	
	private void update(int status){
		if (status == PLAYSTATUS_FORWARD) {
			if(++count == 16)
	    		count = 0;
	    	circleImageView.setImageDrawable(getResources().getDrawable(images[count]));
		} else if (status == PLAYSTATUS_BACKWORD) {
			if(--count == -1)
	    		count = 15;
	    	circleImageView.setImageDrawable(getResources().getDrawable(images[count]));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
		ActivityStackControl.add(this);
		
		//activityMgr = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		
		returnImageView = (ImageView)findViewById(R.id.key_return);
		forwardImageView = (ImageView)findViewById(R.id.key_forward);
		backwardImageView = (ImageView)findViewById(R.id.key_backword);
		playImageView = (ImageView)findViewById(R.id.key_play);
		loginfoTextView = (TextView)findViewById(R.id.display);
		speedTextView = (TextView)findViewById(R.id.speed);
		circleImageView = (ImageView)findViewById(R.id.circle);
		
		exitsysHandler = new Handler();
		
		PLAYSPEED_TIMER = PLAYSPEED_1X;
		myBundle = this.getIntent().getExtras();
		String[] strings = myBundle.getStringArray("message");
		loginfoTextView.setText(strings[0]);
		if (vod_handle.playStatus && Integer.parseInt(strings[1]) == 1) {
			playImageView.setImageResource(R.drawable.pause);
			startTimer(Integer.parseInt(strings[1]));
		} else if (vod_handle.playStatus && Integer.parseInt(strings[1]) != 1) {
			playImageView.setImageResource(R.drawable.play);
			speedTextView.setText("X"+strings[1]);
			startTimer(Integer.parseInt(strings[1]));
		} else if (vod_handle.pauseStatus) {
			playImageView.setImageResource(R.drawable.play);
		}
		
		dMetrics = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        
        if (dMetrics.widthPixels == 320 && dMetrics.heightPixels == 480) {
			offsetWidth = 320/480 + 1;
		    offsetHigth = 480/800 + 1;
		} 
		
		vod_handle.registerCallback(processMsg_Playend, vod_model.ID_CALL_BACK_PLAYEND);
		vod_handle.registerPlayCallback(processMsg_Currentstatus, vod_model.ID_CALL_BACK_CURRENTSTATUS);
		detector = new GestureDetector(this);
		vod_handle.completeInit = true;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.i("onTouchEvent", "Activity onTouchEvent!");
		forwardImageView.getGlobalVisibleRect(rect_forward);
		backwardImageView.getGlobalVisibleRect(rect_backward);
		playImageView.getGlobalVisibleRect(rect_play);
		returnImageView.getGlobalVisibleRect(rect_return);
		return this.detector.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		if (e.getRawY() >= (rect_return.top - 60 * offsetHigth) && e.getRawY() <= (rect_return.bottom + 30 * offsetHigth)) {
			if(e.getRawX() >= (rect_return.left - 70 * offsetWidth) && e.getRawX() <= (rect_return.right + 70 * offsetWidth)) { 
				returnImageView.setImageResource(R.drawable.revert_focus);
			}
		} else if (e.getRawY() >= (rect_backward.top - 70 * offsetHigth) && e.getRawY() <= (rect_backward.bottom + 40 * offsetHigth)) {
			if (e.getRawX() >= (rect_backward.left - 70 * offsetWidth) && e.getRawX() <= (rect_backward.right + 30 * offsetWidth)) { 
				backwardImageView.setImageResource(R.drawable.backword_focus);
			} else if(e.getRawX() >= (rect_forward.left - 30 * offsetWidth) && e.getRawX() <= (rect_forward.right + 70 * offsetWidth)) { 
				forwardImageView.setImageResource(R.drawable.forward_focus);
			} else if(e.getRawX() >= (rect_play.left - 90 * offsetWidth) && e.getRawX() <= (rect_play.right + 90 * offsetWidth)) { 
				if(is_play) {
					playImageView.setImageResource(R.drawable.pause_focus);
				} else {
					playImageView.setImageResource(R.drawable.play_focus);
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.d("***当前时间-上次按键时间***", String.valueOf(System.currentTimeMillis()-keytime));
		if (e.getRawY() >= (rect_return.top- 30 * offsetHigth) && e.getRawY() <= (rect_return.bottom + 30 * offsetHigth)) {
			if(e.getRawX() >= (rect_return.left- 70 * offsetWidth) && e.getRawX() <= (rect_return.right + 70 * offsetWidth)) {
				returnImageView.setImageResource(R.drawable.revert);
				if (System.currentTimeMillis() - keytime > keyTimeInterval) {
					keytime = System.currentTimeMillis();
					Log.i("onSingleTapUp", "onSingleTapUp");
				} else {
					keytime = System.currentTimeMillis();
					return true;
				}
				handler.removeCallbacks(forwardRunnable);
				handler.removeCallbacks(backwardRunnable);
				send_key(R.id.key_return);
				ActivityStackControl.remove(this);
				finish();
			}
		} else if (e.getRawY() >= (rect_backward.top - 70 * offsetHigth) && e.getRawY() <= (rect_backward.bottom + 40 * offsetHigth)) {
			if (e.getRawX() >= (rect_backward.left - 70 * offsetWidth) && e.getRawX() <= (rect_backward.right + 30 * offsetWidth)) {
				backwardImageView.setImageResource(R.drawable.backword);
				if (System.currentTimeMillis() - keytime > keyTimeInterval) {
					keytime = System.currentTimeMillis();
					Log.i("onSingleTapUp", "onSingleTapUp");
				} else {
					keytime = System.currentTimeMillis();
					return true;
				}
				send_key(R.id.key_backword);
			} else if(e.getRawX() >= (rect_forward.left - 30 * offsetWidth) && e.getRawX() <= (rect_forward.right + 70 * offsetWidth)) {
				forwardImageView.setImageResource(R.drawable.forward);
				if (System.currentTimeMillis() - keytime > keyTimeInterval) {
					keytime = System.currentTimeMillis();
					Log.i("onSingleTapUp", "onSingleTapUp");
				} else {
					keytime = System.currentTimeMillis();
					return true;
				}
				send_key(R.id.key_forward);
			} else if(e.getRawX() >= (rect_play.left - 90 * offsetWidth) && e.getRawX() <= (rect_play.right + 90 * offsetWidth)) {
				if (is_play) {
					playImageView.setImageResource(R.drawable.pause);
				} else {
					playImageView.setImageResource(R.drawable.play);
				}
				if (System.currentTimeMillis() - keytime > keyTimeInterval) {
					keytime = System.currentTimeMillis();
					Log.i("onSingleTapUp", "onSingleTapUp");
				} else {
					keytime = System.currentTimeMillis();
					return true;
				}
				send_key(R.id.key_play);
			}
		}
		return true;
	}
	
	/**
	 * Monitor sliding gestures
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.i("Fling", "Fling Happened!");
		backwardImageView.setImageResource(R.drawable.backword);
		forwardImageView.setImageResource(R.drawable.forward);
		returnImageView.setImageResource(R.drawable.revert);
		
		double k = (e2.getRawX()-e1.getRawX())/(e2.getRawY()-e1.getRawY());
		//double a = Math.atan(k)*180/Math.PI;
		
		if (e1.getX() - e2.getX() > 50 && (k > 1 || k < -1)) {
			Log.i("From Right to Left", "Slide to the left");
			send_key(R.id.key_backword);
			return true;
		} else if (e1.getX() - e2.getX() < -50 && (k > 1 || k < -1)) {
			Log.i("From Left to Right", "Slide to the right");
			send_key(R.id.key_forward);
			return true;
		}
		
		return true;
	}
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			new AlertDialog.Builder(this)
			.setTitle(R.string.tip)
			.setMessage(R.string.quit_message)
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			})
			.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					handler.removeCallbacks(forwardRunnable);
					handler.removeCallbacks(backwardRunnable);
					loginfoTextView.setText("正在退出系统...");
					vod_view.controller_.exitsys();
					exitsysHandler.postDelayed(exitrunnble, 2000);
				}
			}).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	public void send_key(int value){
    	String keyValue = new String();
    	switch (value) {
		case R.id.key_play:
			keyValue = "5";
			break;
		case R.id.key_forward:
			keyValue = "6";
			break;
		case R.id.key_backword:
			keyValue = "4";
			break;
		case R.id.key_close:
			keyValue = "0";
			break;
		case R.id.key_return:
			keyValue = "*";
			break;
		default:
			break;
		}
    	vod_handle.client.Digit(keyValue);
	}
	
	private Runnable exitrunnble = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			vod_handle.removePlayCallback(processMsg_Currentstatus);
			ActivityStackControl.finishProgram();
		}
	};
	
	private AppCallBack processMsg_Playend = new AppCallBack() {
		
		@Override
		public void startCallback() {
			// TODO Auto-generated method stub
			handler.removeCallbacks(forwardRunnable);
			handler.removeCallbacks(backwardRunnable);
			vod_handle.removeCallback(processMsg_Playend);
			ActivityStackControl.remove(play_view.this);
			finish();
		}
	};
	
	private PlayCallBack processMsg_Currentstatus = new PlayCallBack() {
		
		@Override
		public void startCallback(boolean playStatus, int playSpeed) {
			// TODO Auto-generated method stub
			loginfoTextView.setText("VOD播放状态");
			handler.removeCallbacks(forwardRunnable);
			handler.removeCallbacks(backwardRunnable);
			if (playStatus && playSpeed >= 2) {
				speedTextView.setVisibility(View.VISIBLE);
				speedTextView.setText("X"+playSpeed);
				playImageView.setImageResource(R.drawable.play);
				startTimer(playSpeed);
				is_play = true;
			} else if(playStatus && playSpeed <= -2) {
				speedTextView.setVisibility(View.VISIBLE);
				speedTextView.setText("X"+playSpeed);
				playImageView.setImageResource(R.drawable.play);
				startTimer(playSpeed);
				is_play = true;
			} else if(playStatus && playSpeed == 1) {
				//speedTextView.setText("X1");
				speedTextView.setVisibility(View.INVISIBLE);
				playImageView.setImageResource(R.drawable.pause);
				PLAYSPEED_TIMER = PLAYSPEED_1X;
				handler.postDelayed(forwardRunnable, PLAYSPEED_1X);
				is_play = true;
			} else if(!playStatus) {
				speedTextView.setVisibility(View.INVISIBLE);
				playImageView.setImageResource(R.drawable.play);
				is_play = false;
			}
		}
	};
	
	public void startTimer(int play_speed) {
		switch (play_speed) {
		case 1:
			PLAYSPEED_TIMER = PLAYSPEED_1X;
			handler.postDelayed(forwardRunnable, PLAYSPEED_1X);
			break;
		case 2:
			PLAYSPEED_TIMER = PLAYSPEED_2X;
			handler.postDelayed(forwardRunnable, PLAYSPEED_2X);
			break;
		case 4:
			PLAYSPEED_TIMER = PLAYSPEED_4X;
			handler.postDelayed(forwardRunnable, PLAYSPEED_4X);
			break;
		case 8:
			PLAYSPEED_TIMER = PLAYSPEED_8X;
			handler.postDelayed(forwardRunnable, PLAYSPEED_8X);
			break;
		case 16:
			PLAYSPEED_TIMER = PLAYSPEED_16X;
			handler.postDelayed(forwardRunnable, PLAYSPEED_16X);
			break;
		case 32:
			PLAYSPEED_TIMER = PLAYSPEED_32X;
			handler.postDelayed(forwardRunnable, PLAYSPEED_32X);
			break;
		case -2:
			PLAYSPEED_TIMER = PLAYSPEED_2X;
			handler.postDelayed(backwardRunnable, PLAYSPEED_2X);
			break;
		case -4:
			PLAYSPEED_TIMER = PLAYSPEED_4X;
			handler.postDelayed(backwardRunnable, PLAYSPEED_4X);
			break;
		case -8:
			PLAYSPEED_TIMER = PLAYSPEED_8X;
			handler.postDelayed(backwardRunnable, PLAYSPEED_8X);
			break;
		case -16:
			PLAYSPEED_TIMER = PLAYSPEED_16X;
			handler.postDelayed(backwardRunnable, PLAYSPEED_16X);
			break;
		case -32:
			PLAYSPEED_TIMER = PLAYSPEED_32X;
			handler.postDelayed(backwardRunnable, PLAYSPEED_32X);
			break;
		default:
			break;
		}
	}
}
