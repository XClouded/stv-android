package com.stv.supervod.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.stv.supervod.service.RegisterServiceImpl;


/**
 * Description: 登录成功显示用户信息及注销按钮
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-12-6 上午10:43:41 mustang created
 */
public class LoginSuccess extends BaseActivity{

	private TextView username;
	private String  vusername = "";
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success);
        username = (TextView) this.findViewById(R.id.username);
        Bundle bd  = this.getIntent().getExtras();
        vusername = bd.getString("username");
        username.setText(vusername);
    }
	
	/**
	 * Description: 重新载入登录页面
	 * @Version1.0 2011-12-5 下午06:20:26 mustang created
	 * @param view
	 */
	public void logout(View view){
		//TODO 这里还要连接服务器的注销方法
		RegisterServiceImpl regService = new RegisterServiceImpl();
		regService.logoutService(this);
		Intent intent = new Intent();
		intent.setClass(this, Login.class);
		Bundle bd = new Bundle();
		bd.putString("username", vusername);
		intent.putExtras(bd);
		Framework.switchActivity("logout2login", intent);
	}
	
	
	
	
}
