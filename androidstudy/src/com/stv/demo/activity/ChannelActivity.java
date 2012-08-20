package com.stv.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.stv.R;

public class ChannelActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_channel_table);
	}
	
	public void testAction(View view){
		Intent Intent = new Intent(this, ListActivityDemo1.class);
		TabTestActivity.showActivity(Intent);
	}

	

}
