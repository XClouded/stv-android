package com.stv.supervod.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Help extends BaseActivity{
	private final String TAG = "Help";
	
	private Button mBtnBack;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		
		mBtnBack = (Button) findViewById(R.id.help_btn_back);
		
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Framework.switchActivityBack();			
				}
		});
	}
}
