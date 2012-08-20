package com.stv.supervod.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stv.supervod.activity.R;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.service.HttpDownloadImpl.TypeEnum;
import com.stv.supervod.service.ImageDownloader;
import com.stv.supervod.utils.Constant.MyVodStateEnum;
import com.stv.supervod.utils.ValidateUtil;

public class EditListItemAdapter extends BaseAdapter{
	private Context mCon;
	private List<Map<String, Object>> mListItem;
	private boolean mIsEdit;
	
	public EditListItemAdapter( Context c, List<Map<String,Object>> mListItem2 ){
		mCon = c;
		mListItem = mListItem2;
		mIsEdit = false;
	}
	
	public void enableEdit(){
		mIsEdit = true;
		notifyDataSetChanged();
	}
	
	public void disableEdit(){
		mIsEdit = false;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListItem.size();
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
		ViewHolder holder = null;
		Map<String, Object> item = mListItem.get(position);
		
		if( convertView == null ){
			LayoutInflater mInflater = (LayoutInflater) mCon.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = mInflater.inflate(R.layout.item_list_edit_2, parent, false);
			
			holder = new ViewHolder();
			holder.itemImg = (ImageView) convertView.findViewById(R.id.item_img);
			holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
			holder.itemType = (TextView) convertView.findViewById(R.id.item_type);
			holder.itemStateImge = (ImageView) convertView.findViewById(R.id.item_iv_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Object obj = null;
		String title = item.get(KeyEnum.title.toString()).toString();
		
		// 预设置默认图片和标题
		obj = item.get(KeyEnum.type.toString());
		if( (obj != null) && (obj.toString().compareToIgnoreCase(TypeEnum.KTV.toString())==0)) {
			holder.itemImg.setBackgroundResource(R.drawable.mtv_cover_default);
			
			obj = item.get(KeyEnum.actor.toString());
			if( obj != null ){
				holder.itemName.setText(title + "-" + obj.toString());
			} else {
				holder.itemName.setText(title);
			}
			// update img
			updatePoster( item.get(KeyEnum.poster.toString()), holder.itemImg );			
		} else if( (obj != null) && (obj.toString().compareToIgnoreCase(TypeEnum.TSTV.toString())==0) ){
			holder.itemImg.setBackgroundResource(R.drawable.tvback_cover_default);
			holder.itemName.setText(title);
		} else {
			holder.itemImg.setBackgroundResource(R.drawable.video_cover_default);
			holder.itemName.setText(title);
			// update img
			updatePoster( item.get(KeyEnum.poster.toString()), holder.itemImg );
		}
		
		holder.itemType.setText(item.get(KeyEnum.categories.toString()).toString());
				
		if( mIsEdit ){
			holder.itemStateImge.setBackgroundResource(R.drawable.btn_delete_bg);
			holder.itemStateImge.setVisibility(View.VISIBLE);
		} else {
			holder.itemStateImge.setVisibility(View.VISIBLE);
			obj = item.get(KeyEnum.state.toString());
			if( obj != null ){
				if( obj.toString().equalsIgnoreCase(MyVodStateEnum.play.toString()) ){
					holder.itemStateImge.setBackgroundResource(R.drawable.myvod_play);
				} else if(obj.toString().equalsIgnoreCase(MyVodStateEnum.pause.toString())){
					holder.itemStateImge.setBackgroundResource(R.drawable.myvod_pause);
				} else if(obj.toString().equalsIgnoreCase(MyVodStateEnum.bookmark.toString())){
					holder.itemStateImge.setBackgroundResource(R.drawable.btn_stop_bg);
				} else {
					holder.itemStateImge.setVisibility(View.GONE);
				}
			} else {
				holder.itemStateImge.setVisibility(View.GONE);
			}
		}
		
        //隔行换色
        if( position%2 == 0) {
        	convertView.setBackgroundResource(R.drawable.list_bg);
        } else {
        	convertView.setBackgroundResource(R.drawable.list_bg_focus);
		}
        
        return convertView;
	}
	
	private void updatePoster( Object poster, View view ){
		if( poster != null ){
			if (!ValidateUtil.isBlank(poster.toString())) {
		        ImageDownloader downloader =ImageDownloader.getImageDownloader();
		        downloader.download(poster.toString(), view);
			}			
		}		
	}
	
	private class ViewHolder{
        ImageView itemImg;
        TextView itemName;
        TextView itemType;
        ImageView itemStateImge;
	}

}
