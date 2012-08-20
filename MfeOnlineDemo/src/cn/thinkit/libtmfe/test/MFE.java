package cn.thinkit.libtmfe.test;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import set.*;
import log.*;
import msg.*;

public class MFE extends Activity {
	/** Called when the activity is first created. */

	//�ؼ�
	private ImageButton imageVoiceSearch;
	private ImageButton imageSearch;
	private ProgressBar progressSearch;
	private EditText editSearch;
	private WebView htmlView;
	private ImageView logoView;

	//menu item
	public final int ITEM_SET = Menu.FIRST;
	public final int ITEM_CLEARLOG = Menu.FIRST + 1;
	public final int ITEM_PROCESSTRACE = Menu.FIRST + 2;
	
	//�ؼ���
	private String mKeyWords = "";
	
	//����
	public static Config mCfg;

	//ǰ������
	private JNI mVREngine = new JNI();

	static {
		System.loadLibrary("tmfe30");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		//ǰ�������ʼ��
		JNI j = mVREngine;
		j.mfeSetLogLevel(7);
		int nOffset = j.mfeGetParam(JNI.PARAM_OFFSET);
		int ret = j.mfeSetParam(JNI.PARAM_OFFSET, nOffset+1);
		
		//�Ƿ�ط���Ч
		j.mfeEnableNoiseDetection(true);
		
		j.mfeInit();
		if (ret != JNI.MFE_SUCCESS) {
			System.out.println("MFE Engine Init failed. Error code is " + ret);
		} else {
			ret = j.mfeOpen();
			if (ret != JNI.MFE_SUCCESS)
				System.out.println("MFE Engine Open failed. Error code is "
						+ ret);
		}		 

		//�ؼ���ʼ��
		editSearch = (EditText) findViewById(R.id.EditTextSearch);
		editSearch.setText("");

		imageVoiceSearch = (ImageButton) findViewById(R.id.ImageButtonVoice);
		imageVoiceSearch.setClickable(true);
		imageVoiceSearch.setOnClickListener(imageVoiceSearch_handle);

		imageSearch = (ImageButton) findViewById(R.id.ImageButtonSearch);
		imageSearch.setClickable(true);
		imageSearch.setOnClickListener(imageSearch_handle);

		progressSearch = (ProgressBar) findViewById(R.id.ProgressBarSearch);
		progressSearch.setVisibility(View.INVISIBLE);

		htmlView = (WebView) findViewById(R.id.WebViewResult);
		WebSettings webSettings = htmlView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		htmlView.setVisibility(View.INVISIBLE);

		logoView = (ImageView) findViewById(R.id.ImageViewLogo);
		logoView.setVisibility(View.VISIBLE);
		
		//��ȡ�����ļ�
		mCfg = new Config(this);
		mCfg.read();
		
		// ����Ƿ�logĿ¼�Ƿ����
		if (!mCfg.logDirExists()) {
			String text = "û��sd�������ܱ�����־";
			Toast.makeText(MFE.this, text, Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void onDestroy() {
		
		//�˳�ǰ������
		int ret = 0;
		ret = mVREngine.mfeClose();
		if(ret != JNI.MFE_SUCCESS)
			System.out.println("MFE Engine close failed. Error code is " + ret);
		ret = mVREngine.mfeExit();
		if(ret != JNI.MFE_SUCCESS)
			System.out.println("MFE Engine exit failed. Error code is " + ret);
		
		super.onDestroy();
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.addSubMenu(0, ITEM_SET, 0, getText(R.string.menu_set));
		menu.addSubMenu(0, ITEM_CLEARLOG, 1, getText(R.string.menu_clear));
		menu.addSubMenu(0, ITEM_PROCESSTRACE, 2, getText(R.string.menu_processtrace));

		MenuItem item = menu.findItem(ITEM_SET);
		item.setIcon(R.drawable.ic_menu_manage);

		item = menu.findItem(ITEM_CLEARLOG);
		item.setIcon(android.R.drawable.ic_menu_delete);

		item = menu.findItem(ITEM_PROCESSTRACE);
		item.setIcon(android.R.drawable.ic_menu_info_details);

		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {

		mCfg.read();
		
		switch (item.getItemId()) {
		case ITEM_SET:
			start_SetActivity();
			break;

		case ITEM_CLEARLOG:
			clear_log();
			break;

		case ITEM_PROCESSTRACE:
			start_LogActivity();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// ���ý���
	void start_SetActivity() {

		SetActivity.msgHandle = mHandler;
		SetActivity.mCfg = MFE.mCfg;

		Intent intent = new Intent();
		intent.setClass(MFE.this, SetActivity.class);
		int res = 0;
		startActivityForResult(intent, res);
	}

	//���log�� �����ļ�(¼���ͷ��ͱ��������)
	void clear_log(){
		if (!MFE.mCfg.logDirExists()) {
			String text = "��־�ļ������ڣ�����sd��";
			Toast.makeText(MFE.this, text, Toast.LENGTH_SHORT).show();
		} else {
			String logDir = MFE.mCfg.getLogDir();
			File folder = new File(logDir);
			if (folder.exists()) {
				File root = new File(logDir);
				File[] fs = root.listFiles();
				for (int i = 0; i < fs.length; i++) {
					if (fs[i].isDirectory()) {
						;
					} else{
						if (fs[i].exists())
							fs[i].delete();
					}
				}

				String text = "�Ѿ�ɾ��������־�������ļ�";
				Toast.makeText(MFE.this, text, Toast.LENGTH_SHORT).show();
			}
		}
	}

	//log����
	void start_LogActivity() {
		if (mCfg.getIfLogKeep()) {
			// ����ʱ��ͳ�ƽ���
			Intent intent = new Intent();
			intent.setClass(MFE.this, LogActivity.class);
			int res = 0;
			startActivityForResult(intent, res);
		} else {
			String nomsg = "���Ƚ�������---����log";
			Toast.makeText(MFE.this, nomsg, Toast.LENGTH_SHORT).show();
		}
	}

	//�õ��ؼ��ʺ�ʼ������������
	private void start_search() {

		new Thread() {
			@Override
			public void run() {
				if (mKeyWords.length() > 0) {
					getHtmlContent("http://wap.baidu.com/s?word="
							+ URLEncoder.encode(mKeyWords));
				} else {
					getHtmlContent("http://wap.baidu.com");
				}
			}
		}.start();
	}

	//��ʼ��������������������������
	private void start_RecordActivity() {

		RecordActivity.mfeHandle = mHandler;
		RecordActivity.mVREngine = mVREngine;

		Long runTime = SystemClock.elapsedRealtime();
		RecordActivity.mSessionId = runTime;

		Intent intent = new Intent();
		intent.setClass(MFE.this, RecordActivity.class);

		int res = 0;
		startActivityForResult(intent, res);
	}
	
	//�ֶ�����ͼ��
	private View.OnClickListener imageSearch_handle = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			mKeyWords = editSearch.getText().toString();
			if (mKeyWords.length() > 0) {
				imageVoiceSearch.setClickable(false);
				imageSearch.setClickable(false);
				
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(editSearch.getWindowToken(), 0); 
				
				progressSearch.setVisibility(View.VISIBLE);
				//logoView.setVisibility(View.INVISIBLE);

				start_search();
			}
		}
	};

	//��������ͼ��
	private View.OnClickListener imageVoiceSearch_handle = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			imageVoiceSearch.setClickable(false);
			imageSearch.setClickable(false);
			
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(editSearch.getWindowToken(), 0); 
			
			progressSearch.setVisibility(View.INVISIBLE);
			start_RecordActivity();
		}

	};

	//��Ϣ����
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgDefinition.SPD_MSG:
				break;
				
			case MsgDefinition.EPD_MSG:
				break;
				
			case MsgDefinition.RES_MSG:
				mKeyWords = (String) msg.obj;
				if (mKeyWords.length() > 0) {
					editSearch.setText(mKeyWords);
					progressSearch.setVisibility(View.VISIBLE);

					start_search();
				}
				break;

			case MsgDefinition.VOICE_QUIT_MSG:
				imageVoiceSearch.setClickable(true);
				imageSearch.setClickable(true);				
				break;

			case MsgDefinition.HTML_MSG:
				imageVoiceSearch.setClickable(true);
				imageSearch.setClickable(true);
				
				progressSearch.setVisibility(View.INVISIBLE);
				logoView.setVisibility(View.INVISIBLE);
				htmlView.setVisibility(View.VISIBLE);

				htmlView.loadDataWithBaseURL("file:///android_asset/",
						(String) msg.obj, "text/html", "UTF-8", "");
				break;

			case MsgDefinition.CONNECT_FAILED_MSG:
				imageVoiceSearch.setClickable(true);
				imageSearch.setClickable(true);
				
				progressSearch.setVisibility(View.INVISIBLE);
				logoView.setVisibility(View.VISIBLE);
				htmlView.setVisibility(View.INVISIBLE);
				String text= "��������ʧ�ܣ���������";
				Toast.makeText(MFE.this, text, Toast.LENGTH_SHORT).show();	
				break;
				
			default:
				super.handleMessage(msg);
				break;
			}
		}
	};

	private void getHtmlContent(String httpUrl) {
		// TODO Auto-generated method stub
		HttpGet request = new HttpGet(httpUrl);

		HttpParams httpParameters;
		int timeoutConnection = 3000;
		int timeoutSocket = 5000;

		httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		
		String proxyHost = android.net.Proxy.getDefaultHost();
		if (proxyHost != null) {// �����wap��ʽ��Ҫ������
			httpClient.getParams().setParameter(
					ConnRoutePNames.DEFAULT_PROXY,
					new HttpHost(android.net.Proxy.getDefaultHost(),
							android.net.Proxy.getDefaultPort()));
		}

		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = EntityUtils.toString(response.getEntity());
				Message msg = mHandler.obtainMessage(MsgDefinition.HTML_MSG, str);
				mHandler.sendMessage(msg);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
			String str = e.getMessage();
			Message msg = mHandler.obtainMessage(MsgDefinition.CONNECT_FAILED_MSG, str);
			mHandler.sendMessage(msg);
			
		} catch (IOException e) {
			e.printStackTrace();
			
			String str = e.getMessage();
			Message msg = mHandler.obtainMessage(MsgDefinition.CONNECT_FAILED_MSG, str);
			mHandler.sendMessage(msg);
		}
	};

}
