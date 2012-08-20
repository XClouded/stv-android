package com.stv.demo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.stv.R;

/**
 * Description: 
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-28 下午01:34:48 mustang created
 */
public class HomeActivity extends Activity {
	private static final int DIALOG1_KEY = 0;
	private static final int DIALOG2_KEY = 1;
	private Activity act = this;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_home);
		//requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		//setProgressBarVisibility(false);
		//dialog =(ProgressDialog) onCreateDialog(DIALOG2_KEY);
		//dialog.show();
		
		new Thread() {
			public void run() {
				try {
					//sleep(1000);
					//setProgressBarVisibility(false);
					//dialog.dismiss();
					Intent intent = new Intent();
					intent.setClass(act, ListActivityDemo.class);
					startActivity(intent);
					//dismissDialog(DIALOG2_KEY);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {

				}
			}
		}.start();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle("Indeterminate");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		case DIALOG2_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

}
