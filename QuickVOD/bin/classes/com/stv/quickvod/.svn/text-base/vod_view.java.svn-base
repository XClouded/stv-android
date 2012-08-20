package com.stv.quickvod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.stv.quickvod.util.SensorUtil;

/**   
 * Filename:    vod_view.java  
 * Package:     com.stv.quickvod  
 * Description: vod_view是点播主页面展示窗口，处理用户按下键后的反应
 */

public class vod_view extends Activity implements OnGestureListener {
	private ImageView returnImageView;
	private ImageView upImageView;
	private ImageView downImageView;
	private ImageView leftImageView;
	private ImageView rightImageView;
	private ImageView okImageView;
	private TextView loginfoTextView;
	
	public ProgressDialog mypDialog;
	private Handler exitsysHandler;
	
	public static vod_handle controller_;
	private Bundle myBundle = new Bundle();

	//private TelephonyManager tManager;
	private DisplayMetrics dMetrics;
	
	private GestureDetector detector;
	private Rect rect_up = new Rect();
	private Rect rect_down = new Rect();
	private Rect rect_left = new Rect();
	private Rect rect_right = new Rect();
	private Rect rect_ok = new Rect();
	private Rect rect_return = new Rect();
	
	private int offsetWidth = 1;
	private int offsetHigth = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		Log.d("onCreate", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page);
		controller_ = new vod_handle(this);
		ActivityStackControl.add(this);//将每一个启动的Activity加入到管理栈当中，在onDestory中移除
		exitsysHandler = new Handler();
		
		returnImageView = (ImageView)findViewById(R.id.key_return);
		upImageView = (ImageView)findViewById(R.id.key_up);
		downImageView = (ImageView)findViewById(R.id.key_down);
		leftImageView = (ImageView)findViewById(R.id.key_left);
		rightImageView = (ImageView)findViewById(R.id.key_right);
		okImageView = (ImageView)findViewById(R.id.key_ok);
		loginfoTextView = (TextView)findViewById(R.id.loginfo);
		
		dMetrics = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        
        if (dMetrics.widthPixels == 320 && dMetrics.heightPixels == 480) {
			offsetWidth = 320/480 + 1;
		    offsetHigth = 480/800 + 1;
		} 
        
		mypDialog = new ProgressDialog(this);
		mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mypDialog.setTitle(R.string.tip);
		mypDialog.setMessage("正在登录VOD点播服务器，请稍候...");
		mypDialog.setCancelable(true);
		mypDialog.show();
		
		myBundle = this.getIntent().getExtras();
		String[] strings = myBundle.getStringArray("userinfo");
		
 		detector = new GestureDetector(this);
		//tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		//showPhoneNum(tManager.getLine1Number());
		controller_.login(strings[0], strings[1]);
		SensorUtil.initSensor(this);
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy(); 
		//System.exit(0);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.i("onTouchEvent", "Activity onTouchEvent!");
		upImageView.getGlobalVisibleRect(rect_up);
		downImageView.getGlobalVisibleRect(rect_down);
		leftImageView.getGlobalVisibleRect(rect_left);
		rightImageView.getGlobalVisibleRect(rect_right);
		okImageView.getGlobalVisibleRect(rect_ok);
		returnImageView.getGlobalVisibleRect(rect_return);
		return this.detector.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		//Log.i("getRawX()", String.valueOf(e.getRawX()));
		//Log.i("getRawY()", String.valueOf(e.getRawY()));
		
		if (e.getRawY() >= (rect_up.top - 60 * offsetHigth) && e.getRawY() <= (rect_up.bottom + 30 * offsetHigth)
				&& e.getRawX() >= (rect_up.left - 30 * offsetWidth) && e.getRawX() <= (rect_up.right + 30 * offsetWidth)) { //向上button按下动作
			upImageView.setImageResource(R.drawable.up_focus);
		} else if (e.getRawY() >= (rect_down.top - 30 * offsetHigth) && e.getRawY() <= (rect_down.bottom + 30 * offsetHigth)
				&& e.getRawX() >= (rect_down.left - 30 * offsetWidth) && e.getRawX() <= (rect_down.right + 30 * offsetWidth)) {//向下button按下动作
			downImageView.setImageResource(R.drawable.down_focus);
		} else if (e.getRawY() >= (rect_return.top - 60 * offsetHigth) && e.getRawY() <= (rect_return.bottom + 30 * offsetHigth)) {
			if(e.getRawX() >= (rect_return.left - 70 * offsetWidth) && e.getRawX() <= (rect_return.right + 70 * offsetWidth)) { //返回button按下动作
				returnImageView.setImageResource(R.drawable.revert_focus);
			}
		} else if (e.getRawY() >= (rect_left.top - 70 * offsetHigth) && e.getRawY() <= (rect_left.bottom + 40 * offsetHigth)) {
			if (e.getRawX() >= (rect_left.left - 70 * offsetWidth) && e.getRawX() <= (rect_left.right + 30 * offsetWidth)) { //向左button按下动作
				leftImageView.setImageResource(R.drawable.left_focus);
			} else if(e.getRawX() >= (rect_right.left - 30 * offsetWidth) && e.getRawX() <= (rect_right.right + 70 * offsetWidth)) { //向右button按下动作
				rightImageView.setImageResource(R.drawable.right_focus);
			} else if(e.getRawX() >= (rect_ok.left - 90 * offsetWidth) && e.getRawX() <= (rect_ok.right + 90 * offsetWidth)) { //确定button按下动作
				okImageView.setImageResource(R.drawable.ok_focus);
			}
		}
		return true;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("onSingleTapUp", "抬起动作");
		if (e.getRawY() >= (rect_up.top - 60 * offsetHigth) && e.getRawY() <= (rect_up.bottom + 30 * offsetHigth)
				&& e.getRawX() >= (rect_up.left - 30 * offsetWidth) && e.getRawX() <= (rect_up.right + 30 * offsetWidth)) {
			upImageView.setImageResource(R.drawable.up);
			controller_.sendKey(R.id.key_up);
		} else if (e.getRawY() >= (rect_down.top - 30 * offsetHigth) && e.getRawY() <= (rect_down.bottom + 30 * offsetHigth)
				&& e.getRawX() >= (rect_down.left - 30 * offsetWidth) && e.getRawX() <= (rect_down.right + 30 * offsetWidth)) {
			downImageView.setImageResource(R.drawable.down);
			controller_.sendKey(R.id.key_down);
		} else if (e.getRawY() >= (rect_return.top- 60 * offsetHigth) && e.getRawY() <= (rect_return.bottom + 30 * offsetHigth)) {
			if(e.getRawX() >= (rect_return.left- 70 * offsetWidth) && e.getRawX() <= (rect_return.right + 70 * offsetWidth)) { //返回button按下动作
				returnImageView.setImageResource(R.drawable.revert);
				controller_.sendKey(R.id.key_return);
			}
		} else if (e.getRawY() >= (rect_left.top - 70 * offsetHigth) && e.getRawY() <= (rect_left.bottom + 40 * offsetHigth)) {
			if (e.getRawX() >= (rect_left.left - 70 * offsetWidth) && e.getRawX() <= (rect_left.right + 30 * offsetWidth)) { //向左button按下动作
				leftImageView.setImageResource(R.drawable.left);
				controller_.sendKey(R.id.key_left);
			} else if(e.getRawX() >= (rect_right.left - 30 * offsetWidth) && e.getRawX() <= (rect_right.right + 70 * offsetWidth)) { //向右button按下动作
				rightImageView.setImageResource(R.drawable.right);
				controller_.sendKey(R.id.key_right);
			} else if(e.getRawX() >= (rect_ok.left - 90 * offsetWidth) && e.getRawX() <= (rect_ok.right + 90 * offsetWidth)) { //确定button按下动作
				okImageView.setImageResource(R.drawable.ok);
				controller_.sendKey(R.id.key_ok);
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
		upImageView.setImageResource(R.drawable.up);
		downImageView.setImageResource(R.drawable.down);
		returnImageView.setImageResource(R.drawable.revert);
		leftImageView.setImageResource(R.drawable.left);
		rightImageView.setImageResource(R.drawable.right);
		okImageView.setImageResource(R.drawable.ok);
		double k = (e2.getRawX()-e1.getRawX())/(e2.getRawY()-e1.getRawY());
		//double a = Math.atan(k)*180/Math.PI;

		Log.i("Fling", "Fling Happened!");
		if (e1.getX() - e2.getX() > 50 && (k > 1 || k < -1)) {
			Log.i("From Right to Left", "Slide to the left");
			controller_.sendKey(R.id.key_left);
			return true;
		} else if (e1.getX() - e2.getX() < -50 && (k > 1 || k < -1)) {
			Log.i("From Left to Right", "Slide to the right");
			controller_.sendKey(R.id.key_right);
			return true;
		} else if (e1.getY() - e2.getY() > 50 && (k > -1 && k < 1)) {
			Log.i("From Down to Up", "Slide to the up");
			controller_.sendKey(R.id.key_up);
			return true;
		} else if (e1.getY() - e2.getY() < -50 && (k > -1 && k < 1)) {
			Log.i("From Up to Down", "Slide to the down");
			controller_.sendKey(R.id.key_down);
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
		return true;
	}
	
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Sniffing keyboard keys
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			mypDialog.cancel();
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
						loginfoTextView.setText("正在退出系统...");
						controller_.exitsys();
						exitsysHandler.postDelayed(exitrunnble, 1000);
					}
				}).show();
			return true;
		}
		else{
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private Runnable exitrunnble = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
	        ActivityStackControl.finishProgram();
		}
	};
	
	public void setLogInfoText(String text) {
		loginfoTextView.setText(text);
	}
	
	public void cancleDialog() {
		mypDialog.cancel();
	}
	
	public void vodPlayActivity(String[] msg){
		Intent intent = new Intent();
		Bundle myBundle = new Bundle();
		//myBundle.putString("message", msg);
		myBundle.putStringArray("message", msg);
		intent.putExtras(myBundle);
		intent.setClass(this, play_view.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);	
	}
}