package com.stv.demo.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stv.R;
import com.stv.demo.vo.PosterInfo;

public class PostTextAdapter  extends BaseAdapter{
	private Context mContext;
    private List<PosterInfo>  mlist;
	

	private int width, height;

	public PostTextAdapter(Context c, List<PosterInfo> list) {
		mContext = c;
		mlist = list;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout line = new LinearLayout(mContext);
		ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//这里要对list数据进行解析判断
		TextView textview = new TextView(mContext);
		int l1 = mlist.size();
		PosterInfo poster = mlist.get(position%mlist.size());
		textview.setBackgroundResource(poster.getId());
		 //textView1.setTypeface(Typeface.createFromAsset(this.getAssets(), "zy.3gp"));
		textview.setTypeface(Typeface.DEFAULT_BOLD);
		textview.setTextColor(Color.WHITE);

		textview.setText(poster.getName());
		textview.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
		//textview.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textview.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 150));
		//textview.setLayoutParams(new Gallery.LayoutParams(200, 100));
		return textview;
	}

	public static List<PosterInfo> getListDemo(){
		 List<PosterInfo> list = new ArrayList<PosterInfo>();
		for(int i=0;i<6;i++){
			PosterInfo p = new PosterInfo();
			if(i/2==0){
				p.setId(R.drawable.poster2);
			}else{
				p.setId(R.drawable.poster1);
			}
			
			p.setName("《失恋三十三天》热播");
			list.add(p);
		}
		return list;
	}
	
	
}
