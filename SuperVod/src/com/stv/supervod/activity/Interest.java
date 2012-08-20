package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stv.supervod.adapter.EditListItemAdapter;
import com.stv.supervod.doc.DB;
import com.stv.supervod.exception.ErrorCodeException;
import com.stv.supervod.service.HttpDownloadImpl;
import com.stv.supervod.service.VideoServiceImpl;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.utils.Constant.PlayerType;
import com.stv.supervod.utils.DpPxUtil;
import com.stv.supervod.utils.ErrorCode;
import com.stv.supervod.utils.Constant.ServiceTypeEnum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


/**
 * @author      Administrator
 * @description 收藏，上一级菜单：更多
 * @authority   激活用户
 * @function    1、向服务器请求收藏的影片（20），并展示
 *              2、用户点击编辑按钮，允许用户删除影片，并给予确认提示
 *              3、用户点击某一部影片，向服务器请求该影片数据，进入影片详细页：Detail
 *              4、收藏和书签共用一套页面
 */
/**
 * Description: 
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-12-6 上午10:39:41 zhaojunfeng created
 */
public class Interest extends BaseActivity{
	private final String TAG = "Interest";
	
	private ListView mListView;
	private List<Map<String, Object>> mListItem = new ArrayList<Map<String, Object>>();
	// 保存删除节目的id，在用户完成编辑后进行数据库同步
	private List<Integer> mDeleteItemIds = new ArrayList<Integer>();
	private Map<String, Object> mServiceInfo = null;
	private Button mBtnBack;
	private Button mBtnEdit;	
	
	private boolean mIsEdit = false;
	private EditListItemAdapter mItemListadapter = null;
	private int mItemSelected = -1;
	private DB mDbInstance = null;
	private processAsyncTask mProcessTask = null;
	private ProgressDialog mProgressDlg;

	private enum MyFavOp {
		error, get_info, play, overdate
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest);
        
		// 初始化
        mListView = (ListView) findViewById(R.id.interest_list);
        mBtnBack = (Button) findViewById(R.id.interest_btn_back);
        mBtnEdit = (Button) findViewById(R.id.interest_btn_edit);
		mProgressDlg = new ProgressDialog(this);
		mProgressDlg.setMessage("数据加载中...");
		mProgressDlg.setIndeterminate(true);
		mProgressDlg.setCancelable(true);
        
        // 绑定数据
        mListView.setOnItemClickListener(mItemListListener);
        mBtnBack.setOnClickListener(mBtnBackListener);
        mBtnEdit.setOnClickListener(mBtnEditListener);

        // 获取数据进行显示
        mDbInstance = DB.getDBInstance(this);
        mProcessTask = new processAsyncTask(this);
        mProcessTask.execute(MyFavOp.get_info);		
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mItemListadapter = null;
		mListItem.clear();
		mListItem = null;
		super.onDestroy();
	}
	
	//[start] 显示
	/**
	 * Description: 显示节目信息
	 * @Version1.0 2012-3-2 下午4:18:28 zhaojunfeng created
	 */
	private void showList(){
		mItemListadapter = new EditListItemAdapter(this, mListItem);
		mListView.setAdapter(mItemListadapter);
		if( mProgressDlg.isShowing() ){
			mProgressDlg.dismiss();
		}
	}
	
	/**
	 * Description: Toast显示信息
	 * @Version1.0 2012-3-8 上午10:57:12 zhaojunfeng created
	 * @param msg
	 */
	private void showMessage( String msg ){
		Toast tt = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		tt.setGravity(Gravity.CENTER, 0, DpPxUtil.dp2px(this, 60));
		tt.show();
	}	
	//[end]

	//[start] 后台操作线程
	private class processAsyncTask extends AsyncTask<MyFavOp, String, MyFavOp>{
		private Context mCon;
		private String mLastError = "";
		
		public processAsyncTask( Context con ){
			mCon = con;
			mProgressDlg.show();
		}
		
		@Override
		protected void onPostExecute(MyFavOp result) {
			// TODO Auto-generated method stub
			switch (result) {
			case get_info:
				showList();
				break;
			case overdate:
				doOverDateItem();
				break;
			case play:
				String offingId = mServiceInfo.get(KeyEnum.offeringId.toString()).toString();
				VideoServiceImpl impl = VideoServiceImpl.getInstance();
				if( impl.getPlayStatus(offingId) ){
					Interest.this.showPlayer( 
							offingId, 
							mListItem.get(mItemSelected).get(KeyEnum.title.toString()).toString(),
							false, PlayerType.lite);					
				} else {
					if( ServiceTypeEnum.MOD.toString().equalsIgnoreCase(
							mServiceInfo.get(KeyEnum.serviceType.toString()).toString()) ){
						// 显示收费提示框
						float price = Integer.parseInt(mServiceInfo.get(KeyEnum.price.toString()).toString()) / 100;
						showServiceDialog( Float.toString(price) );
					} else { // 免费节目，直接播放
						Interest.this.showPlayer( 
								offingId, 
								mListItem.get(mItemSelected).get(KeyEnum.title.toString()).toString(),
								false,PlayerType.common);
					}					
				}
				break;
			case error:
			default:
				showMessage(mLastError);
				break;
			}
			
			if( mProgressDlg.isShowing() ){
				mProgressDlg.dismiss();
			}
		}
		
		@Override
		protected MyFavOp doInBackground(MyFavOp... params) {
			// TODO Auto-generated method stub
			MyFavOp ret = params[0];
			try {
				if( params[0] == MyFavOp.get_info ){
					//[start] 获取节目详细信息
					getDataFromDb();
					//[end]
				} else if( params[0] == MyFavOp.play ){
					//[start] 播放
					HttpDownloadImpl httpDownload = new HttpDownloadImpl();
					Map<String, Object> it = mListItem.get(mItemSelected);
					mServiceInfo = httpDownload.downloadOffingId(
							it.get(KeyEnum.assetId.toString()).toString(),
							it.get(KeyEnum.type.toString()).toString());
					if( mServiceInfo.isEmpty() ){
						mLastError = "没有有效节目信息";
						ret = MyFavOp.error;
					}
					//[end]
				}
			} catch (Exception e) {
				e.printStackTrace();
				mLastError = ErrorCode.getErrorInfo(e);
				if( e instanceof ErrorCodeException && e.getMessage().equalsIgnoreCase("10005")){
					// 资源不存在，节目已过期，删除
					ret = MyFavOp.overdate;
				} else {
					ret = MyFavOp.error;
				}
			}	
			return ret;
		}
	
	}
	//[end]
	
	
	//[start] Listerner
    private OnItemClickListener mItemListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			// TODO Auto-generated method stub
			mItemSelected = position;
			if( mIsEdit ){
				//doDeleteFunc(position);
				showDialog( false );
			} else {
		        mProcessTask = new processAsyncTask(Interest.this);
		        mProcessTask.execute(MyFavOp.play);			
			}
		}
	}; 
	
	private View.OnClickListener mBtnBackListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Framework.switchActivityBack();
		}
	}; 
	
	private View.OnClickListener mBtnEditListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if( !mIsEdit && mListItem.isEmpty() ){
				return;
			}
							
			mIsEdit = !mIsEdit;
			if( mIsEdit ){ // 编辑状态
				mBtnEdit.setText(getString(R.string.further_btn_editok));
				mItemListadapter.enableEdit();
			} else {    // 完成状态
				mBtnEdit.setText(getString(R.string.further_btn_edit));
				mItemListadapter.disableEdit();
				updateDb();
			}
		}
	};
	//[end]
	
	//[start] 对话框
	private void showDialog( boolean isClear )
	{
		AlertDialog.Builder bd = new AlertDialog.Builder(Interest.this);
		bd.setTitle(R.string.further_dlg_title);
		
		if( isClear ){
			bd.setMessage(R.string.further_dlg_clear_content);
			bd.setPositiveButton( getString(R.string.framework_dlg_btn_ok),
					new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					doDeleteFunc();
				}
			});
		} else {
			bd.setMessage(R.string.further_dlg_delete_content);
			bd.setPositiveButton( getString(R.string.framework_dlg_btn_ok)
					, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					doDeleteFunc();	
				}
			});
		}
		
		bd.setNegativeButton(getString(R.string.framework_dlg_btn_cancel)
				, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		bd.setCancelable(false);
		bd.show();
	}
	
	/**
	 * Description: 显示收费对话框 
	 * @Version1.0 2012-3-8 上午10:36:51 zhaojunfeng created
	 */
	private void showServiceDialog( String price ){
		AlertDialog.Builder bd = new AlertDialog.Builder(Interest.this);
		bd.setTitle(R.string.further_dlg_title);
		
		String msg = String.format(getString(R.string.detail_txt_price), price);
		
		bd.setMessage(msg);
		bd.setPositiveButton( getString(R.string.framework_dlg_btn_ok)
				, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Interest.this.showPlayer( 
						mServiceInfo.get(KeyEnum.offeringId.toString()).toString(), 
						mListItem.get(mItemSelected).get(KeyEnum.title.toString()).toString(),
						false, PlayerType.common);
			}
		});
		
		bd.setNegativeButton(getString(R.string.framework_dlg_btn_cancel)
				, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		
		bd.setCancelable(false);
		bd.show();
	}
	//[end]
	
	//[start] 数据库操作
	private void doDeleteFunc()
	{
		if( mItemSelected == -1 ){
			for (Map<String, Object> it : mListItem) {
				mDeleteItemIds.add(	Integer.parseInt(it.get(KeyEnum.id.toString()).toString()));
			}
			mListItem.clear();
		} else {
			mDeleteItemIds.add(	Integer.parseInt(mListItem.get(mItemSelected).get
					(KeyEnum.id.toString()).toString()));
			mListItem.remove(mItemSelected);
		}
		mListView.setVisibility(View.GONE);
		mItemListadapter.notifyDataSetChanged();
		mListView.setVisibility(View.VISIBLE);
		
		// null data, disable edit
		if( mListItem.size() == 0 ){ 
			mIsEdit = false;
			mBtnEdit.setText(getString(R.string.further_btn_edit));
			mItemListadapter.disableEdit();
			updateDb();
		}		
	}
	
	/**
	 * Description: 根据记录的删除节目的id进行数据库删除操作
	 * @Version1.0 2012-4-9 下午4:29:43 zhaojunfeng created
	 */
	private void updateDb(){
		for (Integer it : mDeleteItemIds) {
			mDbInstance.delMyfav(it);
		}
	}
	
	/**
	 * Description: 过期节目，自动删除
	 * @Version1.0 2012-4-9 下午4:56:28 zhaojunfeng created
	 */
	private void doOverDateItem(){
		try {
			int index = Integer.parseInt(mListItem.get(mItemSelected).get
					(KeyEnum.id.toString()).toString());
			mDbInstance.delMyfav(index);
			mListItem.remove(mItemSelected);
			showMessage(getString(R.string.further_txt_overtime_item));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Description: 从数据库回去数据
	 * @Version1.0 2012-4-9 下午4:22:26 zhaojunfeng created
	 */
	private void getDataFromDb()
	{		
		if( mDbInstance == null ){
			return;
		}
		
		Cursor cursor = null;
		try {
			cursor = mDbInstance.getAllMyfavCursor();
			if( cursor != null && cursor.getCount() != 0 && cursor.moveToFirst() ){
				do {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(KeyEnum.id.toString(), cursor.getString(0));
					map.put(KeyEnum.title.toString(), cursor.getString(1));
					map.put(KeyEnum.type.toString(), cursor.getString(2));
					map.put(KeyEnum.categories.toString(), cursor.getString(3));
					map.put(KeyEnum.poster.toString(), cursor.getString(4));
					map.put(KeyEnum.assetId.toString(), cursor.getString(5));
					mListItem.add(map);  			
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if( cursor != null ){
				cursor.close();
				cursor = null;
			}
		}
	}
	//[end]
}
