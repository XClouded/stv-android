package com.stv.demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.stv.R;
import com.stv.util.Constants;

/**
 * Description: 
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-28 下午01:34:48 mustang created
 */
public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//onCreateDialog().show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	protected Dialog onCreateDialog() {
		LayoutInflater factory = LayoutInflater.from(Constants.act);
		final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
		return new AlertDialog.Builder(Constants.act).setView(textEntryView)
		.create();
	}

}
