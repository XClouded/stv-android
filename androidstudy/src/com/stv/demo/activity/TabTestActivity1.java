package com.stv.demo.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.stv.R;
import com.stv.demo.vo.ActivityTabInfo;

/**
 * Description: 
 * 
 * 注意事项：
 * 1、如果定义listview的复杂页面就不要继承listactivity
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-15 下午05:39:24 mustang created
 */
public class TabTestActivity1 extends TabActivity {

	private static TabHost tabHost;
	private static TabActivity act;
	private static TabWidget tabWidget;
	private static TabSpec tab0;
	private static TabSpec tab1;
	private static TabSpec tab2;
	private static TabSpec tab3;
	private static TabSpec tab4;
	private static Intent  Intent1;
	private static List<TabSpec> tablist;
	public static List<ActivityTabInfo> actList = new ArrayList<ActivityTabInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = this;
		setContentView(R.layout.study_tab);
		//tabHost = (TabHost) this.findViewById(R.id.tabhost);
		tabHost = getTabHost();
		tabHost.getTabContentView();
		tab0 = tabHost.newTabSpec("tab0");
		tab0.setIndicator("首页", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, ListActivityDemo.class));
		tabHost.addTab(tab0);

		tab1 = tabHost.newTabSpec("tab1");
		Intent1 = new Intent(this, ChannelActivity.class);
		tab1.setIndicator("频道", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(Intent1);
		tabHost.addTab(tab1);

		tab2 = tabHost.newTabSpec("tab2");
		tab2.setIndicator("搜索", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, SearchActivity.class));
		tabHost.addTab(tab2);

		tab3 = tabHost.newTabSpec("tab3");
		tab3.setIndicator("历史", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, ListActivityDemo1.class));
		tabHost.addTab(tab3);

		tab4 = tabHost.newTabSpec("tab4");
		tab4.setIndicator("更多", getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(this, MoreActivity.class));
		tabHost.addTab(tab4);

		/*tab6 = tabHost.newTabSpec("tab6");
		tab6.setIndicator("隐藏我");
		tab6.setContent(new Intent(act, ListActivityDemo.class));
		tabHost.addTab(tab6);*/

		//add by mustang，这段代码让TAB在屏幕下边显示。
		/*tabWidget = tabHost.getTabWidget();
		LinearLayout lLayout = (LinearLayout) tabHost.getChildAt(0);
		lLayout.removeViewAt(0);
		//tabWidget.setStripEnabled(false);
		lLayout.addView(tabWidget);
		*/
		ActivityTabInfo ai = new ActivityTabInfo(ListActivityDemo.class, 0);
		actList.add(ai);
		//tabWidget.getChildAt(5).setVisibility(View.GONE);

	}

	
	
	
	/*public static  void showActivity() {
		tabHost.addTab(tabHost.newTabSpec("tab6").setIndicator("更多", act.getResources().getDrawable(R.drawable.icon48x48_1))
				.setContent(new Intent(act, ListActivityDemo1.class)));
		tabWidget.getChildAt(5).setVisibility(View.GONE);
		tabHost.setCurrentTabByTag("tab6");
		//tab.setContent(new Intent(this, ListActivityDemo1.class));
	}*/
	public static void showActivity(ActivityTabInfo ai) {
		//ActivityTabInfo Class<?> cls, Map<String, Object> map, int tabindex
		Class<?> cls = ai.getActivity();
		int tabindex = ai.getTabIndex();
		switch (tabindex) {
		case 0:
			forwardIntent(tab0, cls, tabindex);
			break;
		case 1:
			//forwardIntent(tab1, cls, tabindex);
			//tab1.setContent(new Intent(act, ListActivityDemo1.class));
			Intent1 = new Intent(act, ListActivityDemo1.class);
			
			tab1.setContent(Intent1);
			//tabHost.setCurrentTabByTag("tab" + tabindex);
			tabHost.setCurrentTab(tabindex);
			break;
		case 2:
			forwardIntent(tab2, cls, tabindex);
			break;
		case 3:
			forwardIntent(tab3, cls, tabindex);
			break;
		case 4:
			forwardIntent(tab4, cls, tabindex);
			break;
		}
		//tab6.setContent(new Intent(act, ListActivityDemo1.class));
		//tabHost.setCurrentTabByTag("tab6");
		//activityManager.startActivity("6", new Intent(act, ListActivityDemo1.class));
	}

	private static void forwardIntent(TabSpec tab, Class<?> cls, int tabindex) {
		tab.setContent(new Intent(act, cls));
		tabHost.setCurrentTabByTag("tab" + tabindex);
		/*tabHost.clearAllTabs();
		tabHost.addTab(tab0);
		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);
		tabHost.addTab(tab4);*/
		//tabHost.setCurrentTab(tabindex);
	}

	private static void reView() {
		tab0 = tabHost.newTabSpec("tab0");
		tab0.setIndicator("首页", act.getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(act, ListActivityDemo.class));
		tabHost.addTab(tab0);

		tab1 = tabHost.newTabSpec("tab1");
		tab1.setIndicator("频道", act.getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(act, ChannelActivity.class));
		tabHost.addTab(tab1);

		tab2 = tabHost.newTabSpec("tab2");
		tab2.setIndicator("搜索", act.getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(act, SearchActivity.class));
		tabHost.addTab(tab2);

		tab3 = tabHost.newTabSpec("tab3");
		tab3.setIndicator("历史", act.getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(act, ListActivityDemo1.class));
		tabHost.addTab(tab3);

		tab4 = tabHost.newTabSpec("tab4");
		tab4.setIndicator("更多", act.getResources().getDrawable(R.drawable.icon48x48_1)).setContent(new Intent(act, ListActivityDemo.class));
		tabHost.addTab(tab4);
	}

	
	
}
