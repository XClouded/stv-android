package com.stv.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.stv.R;

/**
 * Description: 
 * 
 * 注意事项：
 * 1、如果定义listview的复杂页面就不要继承listactivity
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-15 下午05:39:24 mustang created
 */
public class MediaListActivity extends Activity {
	
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_index);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.study_vertical_item, new String[] { "name", "type", "score", "img" }, new int[] { R.id.name,
				R.id.type, R.id.score, R.id.img });
		listview = (ListView) this.findViewById(R.id.study_vertical_list);
		listview.setAdapter(adapter);
		//setListAdapter(adapter);

	}

	public List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "笑傲江湖");
		map.put("type", "武侠、动作");
		map.put("score", "8.0");
		map.put("img", R.drawable.icon);

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("name", "天龙八部");
		map1.put("type", "武侠、动作");
		map1.put("score", "8.0");
		map1.put("img", R.drawable.icon);
		list.add(map);
		list.add(map1);
		return list;
	}

}
