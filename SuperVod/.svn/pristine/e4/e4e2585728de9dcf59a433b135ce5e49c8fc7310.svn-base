package com.stv.supervod.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.stv.supervod.activity.R;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Description: 显示界面操作提示信息
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-12-16 下午3:53:29 zhaojunfeng created
 */
public class TutorialDlgUtil {
	private static HashMap<String, Boolean> appPref = new HashMap<String, Boolean>();
	private final static String appPrefFisrtUseTag = "isFirst";
	private static TutorialDlgUtil inStance = null;
	
	private Dialog tutorial;
		
	private void showDlg(Context con, int subLayoutResId) {	
		tutorial = new Dialog(con, R.style.Tutorial_Dialog);
		LayoutInflater m = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = m.inflate(R.layout.tutorial_dlg, null);
		view.getBackground().setAlpha(160);
		tutorial.setContentView(view);	
		
		FrameLayout subView = (FrameLayout) view.findViewById(R.id.tutorial_fl_view);
		subView.addView(m.inflate(subLayoutResId, null));
		
		ImageView ivClose = (ImageView) view.findViewById(R.id.tutorial_iv_close);
		ivClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tutorial.cancel();
			}
		});
		tutorial.show();
	}
	
	/**
	 * Description: 显示界面提示信息
	 * @Version1.0 2011-12-16 下午4:58:40 zhaojunfeng created
	 * @param con
	 * @param appName
	 * @param subLayoutResId
	 */
	public static void ShowTutorial( Context con, String appName, int subLayoutResId ) {
		if( inStance == null ){
			inStance = new TutorialDlgUtil();
		}
		
		SharedPreferences setting;
		
		if( appPref.containsKey(appName) ){
			if((boolean)appPref.get(appName)){
				// first use app
				setting = con.getSharedPreferences(appName, 0);
				SharedPreferences.Editor edit = setting.edit();
				edit.putBoolean(appPrefFisrtUseTag, false);
				edit.commit();				
				inStance.showDlg( con, subLayoutResId );	
			}
		} else {
			setting = con.getSharedPreferences(appName, 0);
			boolean isFirst = setting.getBoolean(appPrefFisrtUseTag, true);
			if( isFirst ){
				SharedPreferences.Editor edit = setting.edit();
				edit.putBoolean(appPrefFisrtUseTag, false);
				edit.commit();				
				inStance.showDlg( con, subLayoutResId );				
			}
			
			appPref.put(appName, false);
		}	
	}
	
	/**
	 * Description: 强制显示提示信息，maybe just for test
	 * @Version1.0 2011-12-16 下午5:04:43 zhaojunfeng created
	 * @param con
	 * @param appName
	 * @param subLayoutResId
	 */
	public static void ForceShowTutorial(Context con, String appName, int subLayoutResId) {
		SharedPreferences setting = con.getSharedPreferences(appName, 0);
		SharedPreferences.Editor edit = setting.edit();
		edit.putBoolean(appPrefFisrtUseTag, true);
		edit.commit();	
		appPref.put(appName, true);
		ShowTutorial(con,appName,subLayoutResId);
	}
}
