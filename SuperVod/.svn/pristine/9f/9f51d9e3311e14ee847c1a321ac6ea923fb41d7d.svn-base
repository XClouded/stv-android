package com.stv.supervod.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stv.supervod.activity.R;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;

/**
 * Description: 
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2012-3-9 下午2:24:02 zhaojunfeng created
 */
public class EpisodeDetailListAdapter extends BaseAdapter{
	private Context mCon;
	private int mTotalCount;
	private Boolean mHaveMore = false;
	private Boolean mIsShowAll = false;
	private Boolean mIsSortOrder = true; // true 顺序， false 逆序
	private List<Map<String,Object>> mListItem;
	private int mMaxShowCount = 10;
	
	private int mSelectIndex = 0;   // 界面选中项的index
	
	
	public EpisodeDetailListAdapter( Context c, List<Map<String,Object>> list ){
		mCon = c;
		mListItem = list;
		mTotalCount = mListItem.size();
		mHaveMore = (mTotalCount > mMaxShowCount);
		// 如果有更多，默认显示部分，否则全部显示
		mIsShowAll = mHaveMore ? false : true; 
	}

	/**
	 * Description: 获取当前选中的集数的信息
	 * @Version1.0 2012-3-9 下午3:29:11 zhaojunfeng created
	 * @return
	 */
	public Map<String,Object> getSelectItem(){
		return mListItem.get((mIsSortOrder)?mSelectIndex:(mTotalCount - mSelectIndex - 1));
	}
	
	/**
	 * Description: 设置当前选择的序号
	 * @Version1.0 2012-3-9 下午3:13:48 zhaojunfeng created
	 * @param select
	 */
	public void setSelectIndex(int select){
		if( select < 0 || select >= mTotalCount ){
			return;
		}
		mSelectIndex = select;
	}
	
	/**
	 * Description: 获得当前显示顺序
	 * @Version1.0 2012-3-9 下午3:10:25 zhaojunfeng created
	 * @return
	 */
	public Boolean getIsSortOrder() {
		return mIsSortOrder;
	}
	
	/**
	 * Description: 设置显示顺序
	 * @Version1.0 2012-3-9 下午3:10:39 zhaojunfeng created
	 * @param order
	 */
	public void setIsSortOrder( Boolean order ) {
		if( !order.equals(mIsSortOrder) ){
			mIsSortOrder = order;
			notifyDataSetChanged();
		}
	}
	
	/**
	 * Description: 当前是否显示了全部项
	 * @Version1.0 2012-3-9 下午2:25:48 zhaojunfeng created
	 * @return
	 */
	public Boolean getIsShowAll() {
		return mIsShowAll;
	}
	
	/**
	 * Description: 设置是否显示全部项
	 * @Version1.0 2012-3-9 下午2:25:23 zhaojunfeng created
	 * @param show
	 */
	public void setIsShowAll( Boolean show ) {
		if( !show.equals(mIsShowAll) && mHaveMore ){
			mIsShowAll = show;
			notifyDataSetChanged();
		}
	}
	
	/**
	 * Description:  是否有更多项可以隐藏
	 * @Version1.0 2012-3-9 下午2:24:44 zhaojunfeng created
	 * @return
	 */
	public Boolean getIsHaveMore() {
		return mHaveMore;
	}
	
	//[start] 显示
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if( mHaveMore ){
			return mIsShowAll?mTotalCount:mMaxShowCount;
		} else {
			return mTotalCount;
		}
	}
	
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	} 

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder v;
		int select = (mIsSortOrder)?position:(mTotalCount - position - 1);
		
		if( convertView == null ){
			LayoutInflater mInflater = (LayoutInflater) mCon.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.detail_episode_item, arg2, false);
			
			v = new ViewHolder();
			v.mSubItemBg = (LinearLayout) convertView.findViewById(R.id.detail_subitem_ll);
			v.mSubItemTv = (TextView) convertView.findViewById(R.id.detail_subitem_tv_number);
			convertView.setTag(v);
		} else {
			v = (ViewHolder) convertView.getTag();
		}			
		try {				
			String number = mListItem.get(select).get(KeyEnum.episodeId.toString()).toString();
			v.mSubItemTv.setText(String.format("%1$02d", Integer.parseInt(number)));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return convertView;
		}			
		if( mSelectIndex == position ){
			v.mSubItemBg.setBackgroundResource(R.drawable.poster_detail_press);
			v.mSubItemTv.setTextColor(Color.BLACK);
		} else {
			v.mSubItemBg.setBackgroundDrawable(null);
			v.mSubItemTv.setTextColor(Color.WHITE);
		}
		return convertView;			
	}
	private class ViewHolder{
		LinearLayout mSubItemBg;
		TextView mSubItemTv;
	}
	//[end]
}