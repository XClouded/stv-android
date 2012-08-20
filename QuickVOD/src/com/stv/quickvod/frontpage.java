package com.stv.quickvod;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/**   
 * Filename:    frontpage.java  
 * Package:     com.stv.quickvod  
 * Description: front page是用户 登陆的页面，在这个页面用户需要输入11位手机号和16位智能卡号，同时这个页面
 *              需要处理在向服务器发送数据前的所有准备，包括创建socket连接，处理各种发生的异常
 */ 

public class frontpage extends Activity {
	
	private TextView checkNetTextView;
	private ProgressBar progressBar;
	private EditText phonenumEditText;
	private EditText smartcardEditText;
	private Button okButton;
	
	private Handler timeoutHandler = new Handler();
	private static SharedPreferences mPreferences;
	private final int timeout = 100; //100ms延时处理
    
	public static Selector selector;
	public static SocketChannel channel;
    
	private final String ipforChinaunicom = "192.168.14.206";//"211.143.204.245";//"222.173.184.84"
	private final String portforChinaunicom = "5008";//5007
	private final String ipforChinatelecom = "111.11.11.1";
	private final String portforChinatelecom = "5008";//5007
    
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		ActivityStackControl.add(this);
		
		checkNetTextView = (TextView)findViewById(R.id.homepage_info);
		progressBar = (ProgressBar)findViewById(R.id.homepage_progress);
		phonenumEditText = (EditText)findViewById(R.id.PhoneNum);
		smartcardEditText = (EditText)findViewById(R.id.SmartCard);
		okButton = (Button)findViewById(R.id.login_button);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		phonenumEditText.setText(mPreferences.getString("phonenum", ""));
		smartcardEditText.setText(mPreferences.getString("smartcard", ""));
		
		okButton.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
				if (phonenumEditText.getText().toString().length() < 11) {
					new AlertDialog.Builder(frontpage.this)
					.setTitle(R.string.tip)
					.setMessage("输入的电话号码长度错误，请重新输入！")
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					}).show();
				} else if (smartcardEditText.getText().toString().length() < 16) {
					new AlertDialog.Builder(frontpage.this)
					.setTitle(R.string.tip)
					.setMessage("输入的智能卡号长度错误，请重新输入16位智能卡号！")
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}
					}).show();
				} else if (phonenumEditText.getText().toString().length() == 11 &&
						smartcardEditText.getText().toString().length() == 16) {
					checkNetTextView.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.VISIBLE);
					SharedPreferences.Editor mEditor = mPreferences.edit();
			        mEditor.putString("phonenum", phonenumEditText.getText().toString());
			        mEditor.putString("smartcard", smartcardEditText.getText().toString());
			        //mEditor.putString("ipaddress", "192.168.14.206");//"222.173.184.84"
			        //mEditor.putString("port", "5008");//5007
			        mEditor.commit(); 
					if (isNetworkAvailable(frontpage.this)) {
						timeoutHandler.postDelayed(runnable, timeout);
					}
					else {
						new AlertDialog.Builder(frontpage.this)
						.setTitle(R.string.tip)
						.setMessage("您当前没有连接任何网络，请检查wifi或移动网络是否打开!")
						.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								ActivityStackControl.remove(frontpage.this);
								finish();
							}
						}).show();
					}
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}
	
	public boolean isNetworkAvailable( Activity mActivity ) { 
	    Context context = mActivity.getApplicationContext();
	    ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity == null) {
	      return false;
	    } else {  
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();
	        if (info != null) {        
	            for (int i = 0; i < info.length; i++) {           
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                    return true; 
	                }        
	            }     
	        } 
	    }   
	    return false;
	}
	
	private String compareNetwork(String ip1, String ip2) {
		return ping(ip1) < ping(ip2) ? ip1 : ip2;
	}
	
	private Float ping(String ip) {
		try {
			Process p = Runtime.getRuntime().exec("ping -c 10 -w 30 " + ip);
			byte[] msg = new byte[128];

			int len;
			while((len=p.getInputStream().read(msg)) > 0) {
				String string = new String(msg, 0, len);
				Log.d("***ping返回***", string);
				if (string.contains("/")) {
					String[] strs = string.split("/", 0);
					for (String str : strs) {
						Log.d("***解析字符串***", str);
					}
					Log.d("***字符串数组倒数第三位***", strs[strs.length-3]);
					if (!strs[strs.length-3].equals("avg")) {
						return Float.valueOf(strs[strs.length-3]);
					}
				}
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		return Float.POSITIVE_INFINITY;
	}
	
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				//定义一个记录套接字通道事件的对象
				selector = Selector.open();
				//定义异步客户端
				channel = SocketChannel.open(); 
				//将客户端设定为异步
				channel.configureBlocking (false);  
				//建立连接，此方法是异步
				channel.connect(new InetSocketAddress(
						ipforChinaunicom, Integer.parseInt(portforChinaunicom)));
				//在轮讯对象中注册此客户端的读取事件
				channel.register(selector, SelectionKey.OP_READ);
				//我们通过持续调用finishConnect()方法来“轮询”连接状态，该方法在连接成功建立之前一直返回false
				long firsttime = System.currentTimeMillis();
				while(!channel.finishConnect()) {
					if (System.currentTimeMillis() - firsttime >= 2000) {
						throw new IOException("Connection Failure");
					}
				};
				
				checkNetTextView.setText("网络连接成功");
				progressBar.setVisibility(View.INVISIBLE);
				vodActivity();
			} catch (IOException e) {
				// TODO: handle exception
				Log.d("IOException", e.toString());
				checkNetTextView.setText("网络连接失败");
				new AlertDialog.Builder(frontpage.this)
				.setTitle(R.string.tip)
				.setMessage("网络连接失败，找不到VOD服务器地址!")
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {
							channel.close();
							ActivityStackControl.remove(frontpage.this);
							finish();
						} catch (IOException e2) {
							// TODO: handle exception
							e2.printStackTrace();
						}
					}
				}).show();
			}
		}
	};
	
	public static void closeSocket() {
		if (channel.isConnected()) {
			try {
				channel.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public void vodActivity() {
		Intent intent = new Intent();
		Bundle myBundle = new Bundle();
		myBundle.putStringArray("userinfo", new String[]{phonenumEditText.getText().toString(), smartcardEditText.getText().toString()});
		intent.putExtras(myBundle);
		intent.setClass(this, vod_view.class);
		startActivity(intent);
	}
}
