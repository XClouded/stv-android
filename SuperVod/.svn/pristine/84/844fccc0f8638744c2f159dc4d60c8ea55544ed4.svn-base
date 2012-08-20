package com.stv.supervod.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.stv.supervod.service.Connect2RCServer;
import com.stv.supervod.service.RegisterServiceImpl;
import com.stv.supervod.service.RegisterServiceImpl.LoginInfoEnum;
import com.stv.supervod.utils.AlertUtils;
import com.stv.supervod.utils.ErrorCode;
import com.stv.supervod.utils.ValidateUtil;

/**
 * Description: 1)去服务器验证用户信息有效性 2)如果信息合法保存，登录配置如记住密码，自动登录等.
 * 
 * @version 1.0 2011-12-7 下午02:18:35 mustang created
 */
public class Login extends Activity {
	protected static final String TextView = null;

	private RegisterServiceImpl regService = new RegisterServiceImpl();

	private ProgressDialog logDialog;
	private Activity act = this;
	private EditText userNameText;
	private EditText passwordText;
	private TextView tel;
	private TextView other;
	private Spinner spinner;
	private LinearLayout pwLayout;

	private String pwd_type = 0 + "";
	private String userName;
	private String password;
	private int cityIndex = 0;
	private final int smartcardlength = 16;
	private final int phonenumlength = 6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		spinner = (Spinner) this.findViewById(R.id.citys);
		pwLayout = (LinearLayout) this.findViewById(R.id.pw_type_bg);
		userNameText = (EditText) this.findViewById(R.id.username);
		passwordText = (EditText) this.findViewById(R.id.password);
		// textview不支持 onclick事件方法设置在xml布局文件中？
		tel = (TextView) this.findViewById(R.id.pw_type_tel);
		other = (TextView) this.findViewById(R.id.pw_type_other);
		tel.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				pwLayout.setBackgroundResource(R.drawable.login_password_tel);
				((TextView) v).setTextColor(Color.WHITE);
				other.setTextColor(Color.BLACK);
				pwd_type = 1 + "";
			}
		});
		other.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				pwLayout.setBackgroundResource(R.drawable.login_password_other);
				((TextView) v).setTextColor(Color.WHITE);
				tel.setTextColor(Color.BLACK);
				pwd_type = 0 + "";
			}
		});

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.add("山东青州");
		adapter.add("福建漳州");
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				cityIndex = position;
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		// 初始显示值
		SharedPreferences sp = regService.getLocalUserinfo(this);
		if (sp != null) {
			userNameText.setText(sp.getString(LoginInfoEnum.user.toString(), ""));
			passwordText.setText(sp.getString(LoginInfoEnum.psd.toString(), ""));
			pwd_type = sp.getString(LoginInfoEnum.ispsd.toString(), "1");
			if (pwd_type.equals("1")) {
				pwLayout.setBackgroundResource(R.drawable.login_password_tel);
				tel.setTextColor(Color.WHITE);
				other.setTextColor(Color.BLACK);
			} else {
				pwLayout.setBackgroundResource(R.drawable.login_password_other);
				tel.setTextColor(Color.BLACK);
				other.setTextColor(Color.WHITE);
			}
			String mycity = sp.getString(LoginInfoEnum.mycity.toString(), "");
			int cityIndex = 0;
			if (!ValidateUtil.isBlank(mycity)) {
				int index = adapter.getPosition(mycity);
				if (index > -1) {
					cityIndex = index;
				}
			}
			spinner.setSelection(cityIndex);
		}

	}

	/**
	 * Description: 处理登录的业务逻辑
	 * 
	 * @Version1.0 2011-12-5 下午06:20:26 mustang created, 2012-03-01 ljnalex
	 *             modify
	 * @param view
	 */
	public void login(View view) {
		userNameText = (EditText) this.findViewById(R.id.username);
		passwordText = (EditText) this.findViewById(R.id.password);
		userName = userNameText.getText().toString();
		password = passwordText.getText().toString();
		if (userName.length() < smartcardlength) {
			AlertUtils.showAlertDialog(this, "输入的智能卡号长度错误，请重新输入16位智能卡卡号！");
		} else if (pwd_type.equals("1") && password.length() < phonenumlength) {
			AlertUtils.showAlertDialog(this, "输入的电话号码长度错误，请重新输入！");
		} else if (pwd_type.equals("0") && password.length() == 0) {
			AlertUtils.showAlertDialog(this, "开户密码不能为空！");
		} else {
			logDialog = (ProgressDialog) AlertUtils.createMyDialog(this, "正在为您努力登录中，请耐心等候...");
			logDialog.show();
			new Thread() {
				public void run() {
					Message msg = new Message();
					try {
						RegisterServiceImpl regService = new RegisterServiceImpl();
						String mycity = (String) spinner.getAdapter().getItem(cityIndex);
						regService.login(act, userName, password, mycity, pwd_type);
						msg.obj = "";
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						msg.obj = ErrorCode.getErrorInfo(e);
						handler.sendMessage(msg);
					}
				}
			}.start();
		}
	}

	/**
	 * 关闭滚动条
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			logDialog.dismiss();
			loginResult(msg);
		}
	};

	/**
	 * Description: 如果成功登录进入首页，否则告诉失败原因
	 * 
	 * @Version1.0 2012-1-13 上午09:54:55 mustang created
	 */
	public void loginResult(Message msg) {
		if (msg.obj == "") {
			// 登录成功
			Intent intent = new Intent();
			intent.setClass(this, Framework.class);
			startActivity(intent);
			finish();
		} else {
			AlertUtils.showAlertDialog(this, "很抱歉，" + msg.obj);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Connect2RCServer.getInstance().gcCache();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
