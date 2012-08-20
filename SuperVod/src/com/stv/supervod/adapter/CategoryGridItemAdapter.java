/**
 * 
 */
package com.stv.supervod.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.stv.supervod.activity.CategoryItem;
import com.stv.supervod.activity.R;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class CategoryGridItemAdapter extends BaseAdapter{
	private Context mCon;
	private ArrayList<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
	private int currFocusId = 0;
	
	private class ViewHolder{
		TextView  sort_text;
	}
	
	public CategoryGridItemAdapter(Context c, ArrayList<Map<String, Object>> data){
		mCon = c;
		items = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if( convertView == null ){
			LayoutInflater mInflater = (LayoutInflater) mCon.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = mInflater.inflate(R.layout.category_sort_grid, parent, false);
			
			holder = new ViewHolder();
			holder.sort_text = (TextView) convertView.findViewById(R.id.sort_item);
			convertView.setTag(holder);				
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.sort_text.setText((String)items.get(position).get(KeyEnum.name.toString()));
		if( position == currFocusId ){
			convertView.setBackgroundResource(R.drawable.btn_bg_blue);
			holder.sort_text.setTextColor(Color.WHITE);
		} else {
			convertView.setBackgroundDrawable(null);
			holder.sort_text.setTextColor(Color.BLACK);
		}
		
		return convertView;
	}
	
	public void setPostion(int pos) {
		currFocusId = pos;
	}
	
	public int getPostion() {
		return currFocusId;
	}
}
