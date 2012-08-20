package cn.thinkit.libtmfe.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import waveform.*;
import log.*;
import msg.*;

class CallbackData {
	int idx;
	int flag;
	byte[] buffer;
	int len;
}

public class RecordActivity extends Activity {

	private ImageView view_volume;
	private TextView view_prompt;
	private Button cancel_button;
	private View nospeachButtons;
	private Button nospeach_cancel_button;
	private Button nospeach_retry_button;
	private ProgressBar waitParse;
	private WaveFormView waveView;

	static Handler mfeHandle; // 主窗口消息句柄，用来返回选中的文件路径
	static JNI mVREngine;

	static List<CallbackData> backDataList = new ArrayList<CallbackData>();
	public static HtmLog log = null;
	static waveform.DataSource waveData = new waveform.DataSource();


	RecordThread recorderInstance = null;
	Thread recorderTh;

	PostThread posterInstance = null;
	Thread posterTh;

	static int iFileCnt = 0;
	static String iFileTag = "";

	static long mSessionId = 12345678;

	// 对应各个声音的volume图片
	private final int[] ID_VOLUME = { R.drawable.mic_level0,
			R.drawable.mic_level1, R.drawable.mic_level2,
			R.drawable.mic_level3, R.drawable.mic_level4,
			R.drawable.mic_level5, R.drawable.mic_level6 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record);
		view_volume = (ImageView) findViewById(R.id.ImageViewVolume);
		view_volume.setImageResource(ID_VOLUME[0]);
		view_volume.setVisibility(View.VISIBLE);

		view_prompt = (TextView) findViewById(R.id.TextViewPrompt);
		view_prompt.setText(getString(R.string.text_start));

		waitParse = (ProgressBar) findViewById(R.id.ProgressBarParse);
		waitParse.setVisibility(View.INVISIBLE);
		
		waveView = (WaveFormView) findViewById(R.id.SurfaceViewWaveForm);
		waveView.setVisibility(View.INVISIBLE);	    

		cancel_button = (Button) findViewById(R.id.btn_stop); // start button
		cancel_button.setOnClickListener(cancel_button_handle);
		cancel_button.setEnabled(true);

		nospeachButtons = findViewById(R.id.btn_stop_and_retry);
		nospeach_cancel_button = (Button) findViewById(R.id.btn_nospeech_stop); // start
																				// button
		nospeach_cancel_button.setOnClickListener(cancel_button_handle);
		nospeach_cancel_button.setEnabled(true);
		nospeach_retry_button = (Button) findViewById(R.id.btn_nospeech_retry); // start
																				// button
		nospeach_retry_button.setOnClickListener(retry_button_handle);
		nospeach_retry_button.setEnabled(true);

		backDataList.clear();
	}

	protected void onStart() {
		super.onStart();

		if ((recorderInstance != null || posterInstance != null)) {

		} else {
			view_prompt.setText(getString(R.string.text_run));
			start();
		}
	}

	/* Stop Button Handler */
	private OnClickListener cancel_button_handle = new OnClickListener() {

		@Override
		public void onClick(View v) {
			stop();

			Message message = mfeHandle.obtainMessage(MsgDefinition.VOICE_QUIT_MSG,
					null);
			mfeHandle.sendMessage(message);
			finish();

		}
	};

	/* Retry Button Handler */
	private OnClickListener retry_button_handle = new OnClickListener() {

		@Override
		public void onClick(View v) {
			view_prompt.setText(getString(R.string.text_nospeach));

			cancel_button.setVisibility(View.VISIBLE);
			nospeachButtons.setVisibility(View.GONE);
			nospeach_cancel_button.setVisibility(View.GONE);
			nospeach_retry_button.setVisibility(View.GONE);

			view_prompt.setText(getString(R.string.text_run));
			start();
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Message message;
			switch (msg.what) {
			case MsgDefinition.VOLUME_NOTIFY_MSG:
				recognise_volume_callback();
				break;

			case MsgDefinition.SPD_MSG:
				view_prompt.setText(getString(R.string.text_run));
				break;

			case MsgDefinition.EPD_MSG:
				view_prompt.setText(getString(R.string.text_end));
				view_volume.setVisibility(View.INVISIBLE);
				waitParse.setVisibility(View.VISIBLE);
				
				waveView.clear();
				waveView.addBuffer(RecordActivity.waveData);	
				int height = waveView.getHeight();
				float rateY = (float)(100/(float)(height/2-1));
				waveView.initRate(6, rateY);
				waveView.initBitmap(320,240,0xcccccc);
				waveView.setVisibility(View.VISIBLE);
				break;

			case MsgDefinition.RES_MSG:
				// RES_MSG消息是post线程发来的，接收到此消息说明post线程已经完成任务
				// 结束语音识别
				stop();

				String str = msg.obj.toString();
				waitParse.setVisibility(View.INVISIBLE);
				waveView.setVisibility(View.INVISIBLE);
				
				message = mfeHandle.obtainMessage(msg.what, str);
				mfeHandle.sendMessage(message);
				finish();
				break;

			case MsgDefinition.NOSPEACH_MSG:
				// 结束语音识别
				stop();
				view_prompt.setText(msg.obj.toString());				

				view_volume.setImageResource(ID_VOLUME[0]);
				view_volume.setVisibility(View.VISIBLE);
				waitParse.setVisibility(View.INVISIBLE);
				
				//没有语音 清除waveView
				waveView.clear();
				waveView.setVisibility(View.INVISIBLE);				

				cancel_button.setVisibility(View.GONE);
				nospeachButtons.setVisibility(View.VISIBLE);
				nospeach_cancel_button.setVisibility(View.VISIBLE);
				nospeach_retry_button.setVisibility(View.VISIBLE);
				break;

			case MsgDefinition.CONNECT_FAILED_MSG:
				// 结束语音识别
				stop();

				view_prompt.setText(getString(R.string.text_wrong_connect));

				view_volume.setImageResource(ID_VOLUME[0]);
				view_volume.setVisibility(View.VISIBLE);
				waitParse.setVisibility(View.INVISIBLE);
				
				//网络问题  清除waveView
				waveView.clear();
				waveView.setVisibility(View.INVISIBLE);
				
				cancel_button.setVisibility(View.GONE);
				nospeachButtons.setVisibility(View.VISIBLE);
				nospeach_cancel_button.setVisibility(View.VISIBLE);
				nospeach_retry_button.setVisibility(View.VISIBLE);
				break;

			default:
				super.handleMessage(msg);
				break;
			}
		};

	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (recorderInstance != null && recorderInstance.isRecording())
				return true;
			else {
				finish();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	};

	private void recognise_volume_callback() {
		int volume = 0;
		if (recorderInstance != null && recorderInstance.isRecording())
			volume = recorderInstance.mVolume;
		if (volume > 6)
			volume = 6;
		if (volume < 0)
			volume = 0;
		view_volume.setImageResource(ID_VOLUME[volume]);
	}

	// 开始语音识别
	private void start() {
		Calendar cal = Calendar.getInstance();
		long timeNow = cal.getTimeInMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("MMdd-HHmmss");
		iFileTag = formatter.format(timeNow);
		
		start_trace();
		start_post();
		start_scan();
	}

	// 结束语音识别
	private void stop() {
		stop_post();
		stop_scan();
		stop_trace();
	}

	// 开启日志
	private void start_trace() {


		String logFile = MFE.mCfg.getLogDir()+ "/trace.htm";

		MFE.mCfg.setLogFile(logFile);
		
		log = new HtmLog(logFile);
		
		if (MFE.mCfg.logDirExists() && MFE.mCfg.getIfDataKeep()) {
			String title = "声音文件路径：" + MFE.mCfg.getLogDir() + "/"
					+ iFileTag + ".pcm";
			log.start(title);
		} else
			log.start("没有保存声音文件");

	}

	// 开始采集数据线程
	private void start_scan() {
		
		//清除数据
		waveData.values = new short[1024*2];
		waveData.len = 0;

		recorderInstance = new RecordThread(this, mHandler, mVREngine);
		recorderInstance.setRecording(true);
		recorderTh = new Thread(recorderInstance);

		String strPcmFileName = MFE.mCfg.getLogDir()+ "/"+ iFileTag + ".pcm";
		recorderInstance.setFileName(strPcmFileName);
		
		recorderTh.start();
		System.out.println("start recorder thread");

		// 显示音量
		// RecordActivity每隔100毫秒向自己发送一次消息，收到消息主动去读recorderInstance的音量大小
		new Thread() {

			@Override
			public void run() {
				System.out.println("recordvolume start");

				while (recorderInstance != null
						&& recorderInstance.isRecording()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Message msg = mHandler.obtainMessage(
							MsgDefinition.VOLUME_NOTIFY_MSG, 0);
					mHandler.sendMessage(msg);
				}
			}
		}.start();

	}
	
	// 开始发送数据线程
	private void start_post() {
		// 清除数据
		RecordActivity.backDataList.clear();

		// 新建发送数据线程
		if (posterInstance != null && posterTh != null) {
			if (posterInstance.isAlived)
				posterInstance.isAlived = false;
		}

		posterInstance = new PostThread(mHandler, RecordActivity.this);
		posterTh = new Thread(posterInstance);

		String strDatFileName = MFE.mCfg.getLogDir()+ "/"+ iFileTag + ".dat";
		posterInstance.setFileName(strDatFileName);

		posterTh.start();
		System.out.println("start poster thread");
	}

	private void stop_post() {
		if (posterInstance.isAlived) {
			if (posterInstance.sendStatus == 1) {
				posterInstance.sendStatus = 3;
			}
			posterInstance.isAlived = false;
		}
	}

	private void stop_scan() {
		recorderInstance.setRecording(false);
	}

	private void stop_trace() {
		log.stop();
	}
}
