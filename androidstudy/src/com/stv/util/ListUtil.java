package com.stv.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListUtil {
	public static void setListViewHeightBasedOnChildren(ListView listView,
			Integer titleheight) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		totalHeight = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// 如果列表标题栏有高度，也要计算一下
		if (titleheight != null) {
			totalHeight += titleheight;
		}
		params.height = totalHeight;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public static void setExpandListViewHeightBasedOnChildren(
			ExpandableListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listView.getExpandableListAdapter().getGroupCount(); i++) {
			View listItem1 = listAdapter.getView(i, null, listView);
			// View listItem =(View)
			// listView.getExpandableListAdapter().getChild(i, j);

			listItem1.measure(0, 0); // 计算子项View 的宽高

			totalHeight += listItem1.getMeasuredHeight(); // 统计所有子项的总高度

			for (int j = 0; j < listView.getExpandableListAdapter()
					.getChildrenCount(i); j++) {
				View listItem = listAdapter.getView(i, null, listView);
				// View listItem =(View)
				// listView.getExpandableListAdapter().getChild(i, j);

				listItem.measure(0, 0); // 计算子项View 的宽高

				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			}
		}

		/*
		 * for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
		 * //listAdapter.getCount()返回数据项的数目 View listItem =
		 * listAdapter.getView(i, null, listView); listItem.measure(0, 0);
		 * //计算子项View 的宽高 totalHeight += listItem.getMeasuredHeight();
		 * //统计所有子项的总高度 }
		 */

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

}
