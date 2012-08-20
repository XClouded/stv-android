package com.stv.supervod.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.stv.supervod.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeekListItemAdapter extends BaseAdapter{
	private Context mCon;
	private ArrayList<HashMap<String, Object>> mWeekListItem;
	private int mSeletced;
	
	public WeekListItemAdapter(Context c, ArrayList<HashMap<String, Object>> list, 
			int sel){
		mCon = c;
		mWeekListItem = list;
		mSeletced = sel;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mWeekListItem.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if( convertView == null ){
			LayoutInflater mInflater = (LayoutInflater) mCon.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = mInflater.inflate(R.layout.tvbackepg_week_item, parent, false);
		} else {
		}
		
		TextView week = (TextView) convertView.findViewById(R.id.tvbackepg_week_item);
		if( position == mSeletced ){
			week.setText(mWeekListItem.get(position).get("DateInWeek").toString() + 
					"\n" + mWeekListItem.get(position).get("DateInDate").toString());
			week.setTextColor(mCon.getResources().getColor(R.color.week_focus));
			convertView.setBackgroundResource(R.drawable.btn_tvback_left_focus);
		} else {
			week.setText(mWeekListItem.get(position).get("DateInWeek").toString());
			convertView.setBackgroundResource(R.drawable.btn_tvback_left);
			week.setTextColor(Color.WHITE);
		}
		return convertView;
	}

	public void  updateFocus( int sel ) {
		if( sel < mWeekListItem.size() ){
			mSeletced = sel;
			notifyDataSetChanged();
		}
	}
}
