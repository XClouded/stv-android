package com.stv.demo.vo;

public class ActivityTabInfo {

	public ActivityTabInfo(Class act, int index) {
		activity = act;
		tabIndex = index;
	}

	private Class activity;

	public Class getActivity() {
		return activity;
	}

	public void setActivity(Class activity) {
		this.activity = activity;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	private int tabIndex;

}
