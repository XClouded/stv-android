package com.stv.supervod.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stv.supervod.activity.R;
import com.stv.supervod.service.ImageDownloader;
import com.stv.supervod.utils.ValidateUtil;

/**
 * Description: 封装首页海报横向列表显示数据 Copyright (c) 永新视博 All Rights Reserved.
 * 
 * @version 1.0 2011-12-5 上午11:30:52 mustang created
 */
public class PosterSimpleAdapter extends BaseAdapter {
	private  int POSTER_LENGTH = 9;
	private ImageDownloader downloader = ImageDownloader.getImageDownloader();
	private Context context;
	public List<? extends Map<String, ?>> list = null;

	public PosterSimpleAdapter(Context context, List<? extends Map<String, ?>> data) {
		if (data.size() > 0) {
			POSTER_LENGTH = data.size();
		}
		list = data;
		this.context = context;
	}

	
	
	public int getDataLength(){
		return POSTER_LENGTH;
	}
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.homepage_poster_item, parent, false);
			holder = new ViewHolder();
			holder.layout = (LinearLayout) convertView.findViewById(R.id.posterlayout);
			holder.textView = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (list != null && !list.isEmpty()) {
			Map dataSet = list.get(position % POSTER_LENGTH);
			String title = "";
			if (dataSet != null) {
				title = (String) dataSet.get("title");
				holder.textView.setText(title);
				String poster = (String) dataSet.get("poster");
				if (!ValidateUtil.isBlank(poster) && holder.layout != null) {
					downloader.download(poster, holder.layout);
				}
			}

		}
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		TextView textView;
		LinearLayout layout;
	}
}
