package com.stv.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.stv.R;
import com.stv.demo.service.PostTextAdapter;
import com.stv.util.ListUtil;

/**
 * Description: 
 * 
 * 注意事项：
 * 1、如果定义listview的复杂页面就不要继承listactivity
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-15 下午05:39:24 mustang created
 */
public class ListActivityDemo1 extends Activity {

	private ExpandableListView study_expand_list;
	private static int index = 6000;

	private ExpandableListAdapter mAdapter;
	private Gallery ge;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_index1);
		/*TabHostUtil tu = new TabHostUtil(this);
		tu.showTabHost();*/

		GalleryAdapter adapter1 = new GalleryAdapter(this, getData(), R.layout.study_horizontal_item1, new String[] { "name" }, new int[] { R.id.name });

		PostTextAdapter adp = new PostTextAdapter(this, PostTextAdapter.getListDemo());
		ge = (Gallery) this.findViewById(R.id.gallery1);
		//ge.setAdapter(adapter1);
		ge.setAdapter(adp);
		ge.setSelection(index);

		ge.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		study_expand_list = (ExpandableListView) this.findViewById(R.id.study_expand_list);
		mAdapter = new ExtendListViewDemo();
		study_expand_list.setAdapter(mAdapter);
		ListUtil.setExpandListViewHeightBasedOnChildren(study_expand_list);
	}

	public class ExtendListViewDemo extends BaseExpandableListAdapter {

		private String[] groups = //初始化一些数据用于显示分组的标题，这个例子不是为了说明数据如何存取，所以这里用固定数据，使例子更突出重点。   
		{ "测试组1", "测试组2", "测试组3", };
		private String[][] children = //初始化一些数据用于显示每个分组下的数据项，这个例子不是为了说明数据如何存取，所以这里用固定数据，使例子更突出重点。   
		{ { "天龙八部1", "天龙八部2", "天龙八部3", "天龙八部4" }, { "天龙八部1", "天龙八部2", "天龙八部3", "天龙八部4" }, { "天龙八部1", "天龙八部2", "天龙八部3", "天龙八部4" }, };

		@Override
		public int getGroupCount() {
			return groups.length;

		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length; // 
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups[groupPosition];

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition]; //获取数据，这里不重要，为了让例子完整，还是写上吧   

		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;

		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

			//这里的处理方法和getChildView()里的类似，不再重复说了   
			System.out.println("====groupPosition========"+groupPosition);

			LayoutInflater inflate = LayoutInflater.from(ListActivityDemo1.this);
			View view = inflate.inflate(R.layout.study_vertical_group, null); //用grouplayout这个layout作为条目的视图   
			TextView textview = (TextView) view.findViewById(R.id.widget28);

			textview.setText(groups[groupPosition]);
			System.out.println(groupPosition);

			return view;

		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			//重点在这里   
			System.out.println("====childPosition========"+childPosition);
			LayoutInflater inflate = LayoutInflater.from(ListActivityDemo1.this);
			View view = inflate.inflate(R.layout.study_vertical_item, null); //用childlayout这个layout作为条目的视图   

			ImageView contactIcon = (ImageView) view.findViewById(R.id.img); //childlayout有一个图标，   
			contactIcon.setImageResource(R.drawable.icon); //指定它的图片内容，就是示例图中的企鹅了   

			TextView name = (TextView) view.findViewById(R.id.name); //childlayout有一个用于显示名字的视图   
			name.setText(getChild(groupPosition, childPosition) + ""); //给这个视图数据   
			TextView description = (TextView) view.findViewById(R.id.score); //childlayout有一个用于显示描述的视图，在name视图的下面，   
			description.setTextKeepState("8分"); //这里只是简单的把它的数据设为description   

			TextView type = (TextView) view.findViewById(R.id.type); //childlayout有一个用于显示名字的视图   
			type.setText("武侠..."); //给这个视图数据   
			// ImageView mycursor=(ImageView)view.findViewById(R.id.myCursor);//childlayout还有一个小图标，在右侧，你可以给它一个单击事件，以弹出对当前条目的菜单。   

			return view;
		}

	}

	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", i);
			map.put("name", "笑傲江湖");
			map.put("type", "武侠、动作");
			map.put("score", "8.0");
			map.put("img", R.drawable.icon);
			list.add(map);
		}
		return list;
	}

	public class GalleryAdapter extends SimpleAdapter {

		public GalleryAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return super.getView(position % 6, convertView, parent);
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

	}

}
