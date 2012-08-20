package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.stv.supervod.service.HttpDownloadImpl;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.service.ImageDownloader;
import com.stv.supervod.utils.AlertUtils;
import com.stv.supervod.utils.ErrorCode;
import com.stv.supervod.utils.ValidateUtil;

/**
 * @author      Administrator
 * @description 电视回看，上一级菜单：更多
 * @authority   激活用户
 * @function    1、显示从今天开始往前的一周列表
 *              2、用户点击具体日期，向服务器请求具体日期的所有回看频道，以列表形式展现
 *              3、用户点击某个频道，向服务器请求该频道的回看数据，以列表形式展现
 *              4、用户点击加入我的频道按钮，判断我的频道节目数量是否达到上限，是则提示用户，否则加入
 *              5、用户点击具体的回看节目，进入播放页面：Play
 */
public class TvBack extends BaseActivity{
	private final String TAG = "TvBack";
	
	private final int MSG_GET_DATA_FINISH = 1; 
	private final int MSG_GET_DATA_ERROR = 2; 
	
	// 由于回看频道较少，所以一次就全部获取完毕
	private final int mNumPerPage  =  10; // 500
	private final int mPageIndex  =  1;
	
	private GridView mGridView;
	private List<Map<String, Object>> mListItem =  new ArrayList<Map<String, Object>>();
	private Button mBtnBack;
	private ProgressDialog mProgressDlg;
	private TvBackChannelAdapter tvbackAdapter = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvback);
       
        initGui();
        
        getDataFromServer();
        
        bindAction();
    }
	
	private void getDataFromServer()
	{
		mProgressDlg.show();
		
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					//just for test
//					getTestData();
//					sleep(1500);
					
					HttpDownloadImpl httpDownload = new HttpDownloadImpl();
					Map<String, Object> map = httpDownload.
							downloadChannelList(mNumPerPage, mPageIndex);
					if( map != null && map.containsKey("list") ){
						mListItem = (List<Map<String, Object>>) map.get("list");
					}	
					mHandler.sendEmptyMessage( MSG_GET_DATA_FINISH );
				} catch (Exception e){
					e.printStackTrace();
					Message msg = new Message();
					msg.what = MSG_GET_DATA_ERROR;
					msg.obj=ErrorCode.getErrorInfo(e);
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}
	
	
	/**
	 * 关闭滚动条
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();
			
			switch (msg.what) {
			case MSG_GET_DATA_FINISH:
				showList();
				break;
			case MSG_GET_DATA_ERROR:
				AlertUtils.displayToast(TvBack.this, (String) msg.obj);
				break;
			default:
				break;
			}
		}
	};
	
	private void bindAction()
	{
        // listview click
        mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				String name = mListItem.get(position).get(KeyEnum.name.toString()).toString();
				String cid = mListItem.get(position).get(KeyEnum.channelId.toString()).toString();
				
				Bundle extras = new Bundle();
				extras.putString(KeyEnum.name.toString(), name);
				extras.putString(KeyEnum.channelId.toString(), cid);
				// start tvbackepg intent
				Intent intent = new Intent();
				intent.setClass(TvBack.this, TvBackEpg.class);
				intent.putExtras(extras);
				Framework.switchActivity("TvBackEpg", intent);
			}
		});
		
		
        // btn back
        mBtnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Framework.switchActivityBack();
			}
		});
   
	}
	
	private void showList()
	{		
		if( tvbackAdapter == null ){
			tvbackAdapter = new TvBackChannelAdapter(this);
			mGridView.setAdapter(tvbackAdapter);
		} else {
			tvbackAdapter.notifyDataSetChanged();
		}
	}
		
	private void initGui()
	{
		mGridView = (GridView) findViewById(R.id.tvback_list);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setVerticalSpacing( 20 );// 垂直间隔
		
        mBtnBack = (Button) findViewById(R.id.tvback_btn_back);
        
        mProgressDlg = (ProgressDialog) AlertUtils.createDialog(AlertUtils.NO_TITLE, this);
	}
	
	private void getTestData()
	{
		for(int i=0;i<10;i++)  
        {  
			Map<String, Object> map = new HashMap<String, Object>();  
          	map.put(KeyEnum.name.toString(), String.format("CCTV%d", i));
			map.put(KeyEnum.poster.toString(), "http://image1.webscache.com/baike/haibao/ipad/2008-08/202573033-321-2008-08-29%2015-19-47.jpg");
			map.put(KeyEnum.categories.toString(), "tvback");
			map.put(KeyEnum.recommendedRank.toString(), 8.5f);
			map.put(KeyEnum.channelId.toString(), 100015500);
			mListItem.add(map);
        }
	}
	
	private class TvBackChannelAdapter extends BaseAdapter{
		private Context mCon;
		private ImageDownloader downloader =ImageDownloader.getImageDownloader();
		
		private class ViewHolder{
			ImageView iv;
			TextView  tv;
		}
		
		public TvBackChannelAdapter( Context c ) {
			mCon = c;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListItem.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if( convertView == null ){
				LayoutInflater mInflater = (LayoutInflater) mCon.getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				
				convertView = mInflater.inflate(R.layout.tvback_item, parent, false);
				
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.tvback_item_image);
				holder.tv = (TextView) convertView.findViewById(R.id.tvback_item_name);
				convertView.setTag(holder);				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			Object obj = mListItem.get(position).get(KeyEnum.poster.toString());
			if( obj != null ){
				String poster = obj.toString();
				if (!ValidateUtil.isBlank(poster) && holder.iv != null) {
					downloader.download(poster, holder.iv);
				}			
			} else {
				Log.w(TAG, "no poster idex=" + position);
			}
			
			obj = mListItem.get(position).get(KeyEnum.name.toString());
			if( obj != null ){
				holder.tv.setText(obj.toString());		
			} else {
				holder.tv.setText("");
				Log.w(TAG, "no name idex=" + position);
			}
			
			return convertView;
		}
		
	}
	
}
