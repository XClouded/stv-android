package com.stv.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.stv.R;
import com.stv.demo.service.PostTextSimpleAdapter;
import com.stv.util.Constants;
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
public class ListActivityDemo extends Activity {
	private static final int DIALOG1_KEY = 0;
	private static final int DIALOG2_KEY = 1;
	public static boolean flag = false;
	public static boolean flag1 = false;
	public static boolean shownow = false;
	public Activity act = this;

	private ListView listview;
	private ListView listview1;
	private Gallery ge;
	ProgressDialog dialog;
	private TextView nowname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Constants.act = this;

		setContentView(R.layout.study_index);
		dialog = (ProgressDialog) onCreateDialog(DIALOG2_KEY);
		dialog.show();
		new Thread() {
			public void run() {
				try {
					sleep(1000);
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					//dismissDialog(DIALOG2_KEY); // 为什么放这里不行呢？
				}
			}
		}.start();
		PostTextSimpleAdapter adp1 = new PostTextSimpleAdapter(act, PostTextSimpleAdapter.getData(), R.layout.study_horizontal_item1, new String[] { "name" },
				new int[] { R.id.name });

		//PostTextAdapter adp = new PostTextAdapter(this,PostTextAdapter.getListDemo());
		ge = (Gallery) act.findViewById(R.id.gallery);
		ge.setAdapter(adp1);
		ge.setSelection(6000);
		ge.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		/*SimpleAdapter adapter1 = new SimpleAdapter(this, getData(), R.layout.study_horizontal_item, new String[] { "img", "name" }, new int[] { R.id.img,
				R.id.name });
		ge = (Gallery) this.findViewById(R.id.gallery);
		ge.setAdapter(adapter1);
		ge.setSelection(0);*/

	}

	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", i);
			map.put("name", "test" + i);
			map.put("type", "武侠、动作");
			map.put("score", "8.0");
			map.put("img", R.drawable.icon);
			list.add(map);
		}
		return list;
	}

	/* (non-Javadoc)继承Activity就不能应用此方法
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	public void showOneInfo(AdapterView<?> parent, View v, int position, long id) {
		Bundle info = new Bundle();
		info.putString("name", "天龙八部");
		info.putInt("img", R.drawable.icon48x48_1);
		Intent intent = new Intent();
		Bundle myBundle = new Bundle();
		myBundle.putBundle("mediainfo", info);
		intent.putExtras(myBundle);
		intent.setClass(this, MediaInfoActivity.class);
		startActivity(intent);

	}

	/**
	 * Description: 测试按钮控制事件
	 * @Version1.0 2011-11-21 下午05:00:32 mustang created
	 * @param view
	 */
	public void showtype(View view) {
		listview = (ListView) this.findViewById(R.id.study_vertical_list);
		ImageView iv = (ImageView) this.findViewById(R.id.today_1);

		if (flag == false) {
			flag = true;
			listview.setVisibility(View.VISIBLE);
			iv.setImageResource(R.drawable.today_2);
			SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.study_vertical_item, new String[] { "name", "type", "score", "img" },
					new int[] { R.id.name, R.id.type, R.id.score, R.id.img });
			listview.setAdapter(adapter);
			ListUtil.setListViewHeightBasedOnChildren(listview,null);
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//showOneInfo(parent, view, position, id);
					shownow(view);
				}
			});
		} else {
			flag = false;
			listview.setVisibility(View.GONE);
			iv.setImageResource(R.drawable.today_1);
		}
	}

	public void showtype2(View view) {
		listview1 = (ListView) this.findViewById(R.id.study_vertical_list1);
		ImageView iv = (ImageView) this.findViewById(R.id.today_2);
		if (flag1 == false) {
			flag1 = true;
			listview1.setVisibility(View.VISIBLE);
			iv.setImageResource(R.drawable.today_2);
			SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.study_vertical_item, new String[] { "name", "type", "score", "img" },
					new int[] { R.id.name, R.id.type, R.id.score, R.id.img });
			listview1.setAdapter(adapter);
			ListUtil.setListViewHeightBasedOnChildren(listview1,null);
			listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//showOneInfo(parent, view, position, id);
					shownow(view);
				}
			});
		} else {
			flag1 = false;
			listview1.setVisibility(View.GONE);
			iv.setImageResource(R.drawable.today_1);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle("Indeterminate");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		case DIALOG2_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("数据加载中...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	/**
	 * 关闭滚动条，需要用这个得
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
		}
	};

	/**
	 * Description: 控制当前页面是现实主内容还是播放内容
	 * @Version1.0 2011-11-28 下午05:09:21 mustang created
	 * @param view
	 */
	public void shownow(View view) {

		ViewFlipper mFlipper = ((ViewFlipper) this.findViewById(R.id.flipper));
		mFlipper.startFlipping();

		nowname = (TextView) this.findViewById(R.id.nowname);
		if (shownow == false) {
			this.findViewById(R.id.widget29).setVisibility(View.GONE);
			this.findViewById(R.id.shownowinfo).setVisibility(View.VISIBLE);
			shownow = true;
		} else {
			this.findViewById(R.id.widget29).setVisibility(View.VISIBLE);
			this.findViewById(R.id.shownowinfo).setVisibility(View.GONE);
			nowname.setText("天龙八部");
			//mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.decelerate_interpolator));
			//mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.decelerate_interpolator));
			mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
			mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
			shownow = false;
		}

	}

	/*动态生成tablerow时不能指定任何布局参数
	 * public void setRows() {
	row1 = (TableRow) this.findViewById(R.id.row1);
	row2 = (TableRow) this.findViewById(R.id.row2);
	table = (TableLayout) this.findViewById(R.id.tableid);
	for (int i = 0; i < 20; i++) {

		ImageView img = new ImageView(this);
		img.setImageResource(R.drawable.icon);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showOneInfo(null, v, 1, 1);

			}

		});
		row1.addView(img);
		TextView tv = new TextView(this);
		tv.setText("测试" + i);
		row2.addView(tv);

	}

	}*/

}
