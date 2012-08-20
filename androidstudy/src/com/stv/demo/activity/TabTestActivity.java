package com.stv.demo.activity;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.stv.R;


/**
 * Description: TAB 测试
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-12-14 下午02:16:41 mustang created
 */
public class TabTestActivity extends TabActivity {

	private static TabHost tabHost;
	
	private static LocalActivityManager mLocalActivityManager = null;
	private static View currentview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocalActivityManager = getLocalActivityManager();
		tabHost = getTabHost();
		TabSpec tab0 = tabHost.newTabSpec("tab0");
		tab0.setIndicator("首页", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, ListActivityDemo.class));
		tabHost.addTab(tab0);

		TabSpec tab1 = tabHost.newTabSpec("tab1");
		
		tab1.setIndicator("频道", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, ChannelActivity.class));
		tabHost.addTab(tab1);

		TabSpec tab2 = tabHost.newTabSpec("tab2");
		tab2.setIndicator("搜索", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, SearchActivity.class));
		tabHost.addTab(tab2);

		TabSpec tab3 = tabHost.newTabSpec("tab3");
		tab3.setIndicator("历史", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, ListActivityDemo1.class));
		tabHost.addTab(tab3);

		TabSpec tab4 = tabHost.newTabSpec("tab4");
		tab4.setIndicator("更多", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, MoreActivity.class));
		tabHost.addTab(tab4);
		// add by mustang，这段代码让TAB在屏幕下边显示
		TabWidget tabWidget = tabHost.getTabWidget();
		LinearLayout lLayout = (LinearLayout) tabHost.getChildAt(0);
		lLayout.removeViewAt(0);
		//去掉tab下边那条白线,1.6 sdk貌似不支持
		// tabWidget.setStripEnabled(false);
		lLayout.addView(tabWidget);
		tabHost.setOnTabChangedListener(new OnTabChangeListenerImple());

	}
	class OnTabChangeListenerImple implements OnTabChangeListener {
		@Override
		public void onTabChanged(String tabId) {
			if (currentview != null) {
				tabHost.getTabContentView().removeView(currentview);
				tabHost.setCurrentTabByTag(tabId);
			}
		}
	}

	public static void showActivity(Intent newintent) {
		tabHost.getTabContentView().removeView(tabHost.getCurrentView());
		currentview = getContentView(newintent);
		tabHost.getTabContentView().addView(currentview);
	}

	public static View getContentView(Intent mIntent) {
		if (mLocalActivityManager == null) {
			throw new IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityGroup)'?");
		}
		final Window w = mLocalActivityManager.startActivity(System.currentTimeMillis() + "", mIntent);
		final View wd = w != null ? w.getDecorView() : null;
		return wd;
	}

}
