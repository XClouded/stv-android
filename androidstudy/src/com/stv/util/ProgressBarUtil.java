package com.stv.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

import com.stv.R;

/**
 * Description: 
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-16 下午04:55:58 mustang created
 */
public class ProgressBarUtil {

	public static Dialog moreDialog = null;
	
	public static void showDialogMore(Activity act) {
		ProgressBarUtil util = new ProgressBarUtil();
		util.onCreateDialog(act).show();
	}

	protected Dialog onCreateDialog(Activity act) {
		
		LayoutInflater factory = LayoutInflater.from(act);
		final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
		return new AlertDialog.Builder(act).setView(textEntryView).create();
	}

}
