package com.stv.util;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.stv.R;
import com.stv.demo.activity.ChannelActivity;
import com.stv.demo.activity.ListActivityDemo;
import com.stv.demo.activity.ListActivityDemo1;
import com.stv.demo.activity.SearchActivity;

public class TabHostUtil {
	private static TabActivity act ;

	//private Activity activity;

	public TabHostUtil(TabActivity activity) {
		act = activity;
	}

	public  void showTabHost() {
		final TabHost tabHost = act.getTabHost();

		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("首页", act.getResources().getDrawable(R.drawable.icon48x48_1))
				.setContent(new Intent(act, ListActivityDemo.class)));

		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("频道", act.getResources().getDrawable(R.drawable.icon48x48_1))
				.setContent(new Intent(act, ChannelActivity.class)));

		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("搜索", act.getResources().getDrawable(R.drawable.icon48x48_1))
				.setContent(new Intent(act, SearchActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("历史", act.getResources().getDrawable(R.drawable.icon48x48_1))
				.setContent(new Intent(act, ListActivityDemo1.class)));
		tabHost.addTab(tabHost.newTabSpec("tab5").setIndicator("更多", act.getResources().getDrawable(R.drawable.icon48x48_1))
				.setContent(new Intent(act, ListActivityDemo.class)));

		//add by mustang，这段代码让TAB在屏幕下边显示。
		TabWidget tabWidget = tabHost.getTabWidget();
		LinearLayout lLayout = (LinearLayout) tabHost.getChildAt(0);
		lLayout.removeViewAt(0);
		//tabWidget.setStripEnabled(false);

		lLayout.addView(tabWidget);

	}

}
