package com.stv.demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.stv.R;

public class MoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateDialog().show();
		//onCreateDialog().dismiss();

	}

	/*@Override
	protected Dialog onCreateDialog(int id) {
		 // This example shows how to add a custom layout to an AlertDialog
	    LayoutInflater factory = LayoutInflater.from(this);
	    final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
	    return new AlertDialog.Builder(MoreActivity.this)
	       // .setIcon(R.drawable.alert_dialog_icon)
	       // .setTitle(R.string.alert_dialog_text_entry)
	        .setView(textEntryView)
	        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {

	                 User clicked OK so do some stuff 
	            }
	        })
	        .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {

	                 User clicked cancel so do some stuff 
	            }
	        })
	        .create();
	}*/

	protected Dialog onCreateDialog() {
		// This example shows how to add a custom layout to an AlertDialog
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
		return new AlertDialog.Builder(this).setView(textEntryView)
		.create();
	}

}
