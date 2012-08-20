package com.stv.supervod.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stv.supervod.adapter.EditListItemAdapter;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.service.HttpDownloadImpl.TypeEnum;
import com.stv.supervod.service.MyVodServiceImpl;
import com.stv.supervod.service.VideoServiceImpl;
import com.stv.supervod.utils.Constant.MyVodPlayModeEnum;
import com.stv.supervod.utils.Constant.PlayerType;
import com.stv.supervod.utils.DpPxUtil;
import com.stv.supervod.utils.ErrorCode;
import com.stv.supervod.utils.VideoConstant;
import com.stv.supervod.utils.Constant.MyVodStateEnum;
import com.stv.supervod.utils.ValidateUtil;
import com.stv.supervod.widget_extended.DraggableListView;
import com.stv.supervod.widget_extended.DraggableListView.DeleteListener;

import android.R.integer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyMtv extends BaseActivity{	
	private final String TAG = "MyMtv";
	private final String mChannelId = "1";
	
	private DraggableListView mListView;
	private List<Map<String, Object>> mListItem = new ArrayList<Map<String, Object>>();
	private Map<String, Object> mChannelsInfo = new HashMap<String, Object>();
	private Button mBtnBack;
	private Button mBtnEdit;
	private Button mBtnPlayMode;
	private FrameLayout mSettingBar;
	private Spinner mSpinner;
	private String[] mRepeatLists = { "1", "2", "3", "4", "5","6","7","8","9","10" };

	private boolean mIsEdit = false;
	private EditListItemAdapter mItemListadapter = null;
	private int mItemSelected = -1;
	
	private processAsyncTask mProcessTask = null;
	private Boolean mIsDoSubmit = false;
	private VideoServiceImpl mVideoServiceImpl = VideoServiceImpl.getInstance();
	private ProgressDialog mProgressDlg;
	private Toast mToast;
	
	private MyVodPlayModeEnum mPlayMode = MyVodPlayModeEnum.in_order; 
	private int mPlayCount = 0;
	// BO返回的书签信息
	private String mItemState = "";
	private String mItemAssetId = "";
	private String mItemOfferingId = "";
	
	private enum myvodOperation{
		error, none,channels,update, query
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymtv);
		
		// 初始化
		mListView = (DraggableListView) findViewById(R.id.mymtv_list);
        mBtnBack = (Button) findViewById(R.id.mymtv_btn_back);
        mBtnEdit = (Button) findViewById(R.id.mymtv_btn_edit);
        
        mSettingBar = (FrameLayout) findViewById(R.id.mymtv_ll_bar);
        mSettingBar.setVisibility(View.GONE);
        mBtnPlayMode = (Button) findViewById(R.id.mymtv_btn_play_mode);
        mSpinner = (Spinner) findViewById(R.id.mymtv_spinner_repeat_count);
        mSpinner.setPromptId(R.string.mymtv_repeat_title);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
        		R.layout.mymtv_spinner_item, mRepeatLists);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        adapter.setDropDownViewResource(R.layout.mymtv_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection( mPlayCount,true);
        
		mProgressDlg = new ProgressDialog(this);
		mProgressDlg.setMessage("数据加载中...");
		mProgressDlg.setIndeterminate(true);
		mProgressDlg.setCancelable(true);
		
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, DpPxUtil.dp2px(this, 60));
        
        // 绑定数据
        mListView.setOnItemClickListener(mItemListListener);
        mBtnBack.setOnClickListener(mBtnBackListener);
        mBtnEdit.setOnClickListener(mBtnEditListener);
        mSpinner.setOnItemSelectedListener(mSpinerListener);
        mBtnPlayMode.setOnClickListener(mPlayModeListener);
        
        // 获取数据进行显示
        mProcessTask = new processAsyncTask(this);
        mProcessTask.execute(myvodOperation.channels);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mToast.cancel();
		unregisterReceiver(mVideoServiceReceiver);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initService();
		super.onResume();
	}
	
	//[start] service
	private void initService(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(VideoConstant.ERROR);
		filter.addAction(VideoConstant.NEXT);
		filter.addAction(VideoConstant.QUERY_MSG);
		registerReceiver(mVideoServiceReceiver, filter);
	}
	//[end]
	
	//[start] 显示
	/**
	 * Description: 启动播放器
	 * @Version1.0 2012-3-9 上午10:34:01 zhaojunfeng created
	 * @param type
	 * @return
	 */
	public Boolean showPlayerInner( String type ) {
		try {
			Bundle extras = new Bundle();
			extras.putString(KeyEnum.type.toString(), type);
			extras.putString(KeyEnum.name.toString(), getString(R.string.mymtv_title));
			extras.putString(KeyEnum.offeringId.toString(), mItemOfferingId);
			extras.putString(KeyEnum.channelId.toString(), mChannelId);
			extras.putString(KeyEnum.play_mode.toString(), mPlayMode.toString());
			extras.putInt(KeyEnum.play_count.toString(), Integer.parseInt(mRepeatLists[mPlayCount]));
			Intent intent = new Intent();
			intent.setClass(MyMtv.this, Play.class);
			intent.putExtras(extras);
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			//AlertUtils.displayToast(this, e.getMessage());
			Log.e(TAG, e.getMessage());
			return false;
		}		
		return true;
	}
	
	/**
	 * Description: 
	 * @Version1.0 2012-3-28 上午10:58:54 zhaojunfeng created
	 */
	private void showMessage( String msg ){
		if( mProgressDlg.isShowing() ){
			mProgressDlg.dismiss();
		}
		mToast.cancel();
		mToast.setText(msg);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}	
	
	/**
	 * Description: 显示节目信息
	 * @Version1.0 2012-3-2 下午4:18:28 zhaojunfeng created
	 */
	private void showList(){
		if( mItemListadapter == null ){
			mItemListadapter = new EditListItemAdapter(this, mListItem);
			mListView.setAdapter(mItemListadapter);
			mListView.setSelection(mItemSelected);
		} else {
			mListView.setDropListener(mIsEdit?mDropListener:null);
			mListView.setDeleteListener(mIsEdit?mDeleteListener:null);	
			mListView.getAdapter();
		}		
	}	
	
	
	/**
	 * Description: 从新设置节目状态
	 * @Version1.0 2012-3-8 下午6:15:57 zhaojunfeng created
	 */
	private void reBuildItemState(){
		// 遍历设置状态
		for (int i = 0; i < mListItem.size(); i++) {
			Map<String, Object> it = mListItem.get(i);
			if( !ValidateUtil.isBlank(mItemAssetId) && !ValidateUtil.isBlank(mItemOfferingId)
				 &&	mItemAssetId.equals(it.get(KeyEnum.assetId.toString()).toString()) 
				 &&	mItemOfferingId.equals(it.get(KeyEnum.offeringId.toString()).toString())){
				it.put(KeyEnum.state.toString(), mItemState);
				mItemSelected = i;
			} else {
				it.put(KeyEnum.state.toString(), MyVodStateEnum.normal.toString());
			}
		}
	}
	//[end]
	
	//[start] 对话框
	/**
	 * Description: 显示删除对话框
	 * @Version1.0 2012-3-6 下午5:12:03 zhaojunfeng created
	 */
	private void showDialog(){
		AlertDialog.Builder bd = new AlertDialog.Builder(MyMtv.this);
		bd.setTitle(R.string.further_dlg_title);
		
		bd.setMessage(R.string.further_dlg_delete_content);
		bd.setPositiveButton( getString(R.string.framework_dlg_btn_ok)
				, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if( mItemSelected == -1 ){
					mListItem.clear();
				} else {
					mListItem.remove(mItemSelected);
				}
				mIsDoSubmit = true;
				mItemListadapter.notifyDataSetChanged();
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
	
	//[start] 后台操作线程
	private class processAsyncTask extends AsyncTask<myvodOperation, String, myvodOperation>{
		private Context mCon;
		private String mLastError = "";
		private MyVodServiceImpl myvodSer = new MyVodServiceImpl();
		
		public processAsyncTask( Context con ){
			mCon = con;
			mProgressDlg.show();
		}
		
		@Override
		protected void onPostExecute(myvodOperation result) {
			// TODO Auto-generated method stub
			switch (result) {
			case update:
			case channels:
				mProgressDlg.dismiss();
				showList();
				break;
			case query: break;
			case none: break;
			case error:
			default:
				showMessage(mLastError);
				break;
			}
		}
		
		@Override
		protected myvodOperation doInBackground(myvodOperation... params) {
			// TODO Auto-generated method stub
			myvodOperation ret = params[0];
			try {
				if( params[0] == myvodOperation.channels ){
					//[start] 获取频道列表
					mChannelsInfo = myvodSer.channels(mChannelId);
					mListItem = (List<Map<String, Object>>) mChannelsInfo.get(KeyEnum.list.toString());
					
					mItemState = mChannelsInfo.get(KeyEnum.state.toString()).toString();
					
					if( mItemState.equalsIgnoreCase(MyVodStateEnum.play.toString()) ){
						// 从RCServer获取状态,并在回调中重建状态显示
						mVideoServiceImpl.myvodState();
						ret = myvodOperation.none;
					} //[end]
				} else if( params[0] == myvodOperation.update ){
					//[start] 更新频道 
					List<String> offingIds = new ArrayList<String>();
					for (int i = 0; i < mListItem.size(); i++) {
						offingIds.add(mListItem.get(i).get(KeyEnum.offeringId.toString()).toString());
					}
					// 通知BO更新频道
					myvodSer.updateChannelList(mChannelId, offingIds);
					
					// 如果没有全部删除完毕，则查询状态
					if( mListItem.size() > 0 ){
						// 查询是否播放，决定是否通知RCServer更新
						String state = myvodSer.state(mChannelId);
						if( state.equalsIgnoreCase(MyVodStateEnum.play.toString()) ){
							// 通知RCServer更新，并在回调中查询状态
							mVideoServiceImpl.myvodChannelUpdate(TypeEnum.KTV.toString(), mChannelId);
							ret = myvodOperation.none;
						} 
					}//[end]
				} else if( params[0] == myvodOperation.query ){
					//[start] 查询更新状态，并在回调中重建更新状态
					mVideoServiceImpl.myvodState();
					//[end]
				} 
			} catch (Exception e) {
				e.printStackTrace();
				mLastError = ErrorCode.getErrorInfo(e);
				ret = myvodOperation.error;
			}	
			return ret;
		}	
	}
	//[end]
	
	//[start] Listerner
	private DraggableListView.DeleteListener mDeleteListener = 
			new DeleteListener() {
				
				@Override
				public void delete(int which) {
					// TODO Auto-generated method stub
					mItemSelected = which;
					showDialog();
				}
			};
			
	private DraggableListView.DropListener mDropListener =  new DraggableListView.DropListener() {  
		        public void drop(int from, int to) {  
		        	HashMap<String, Object> item = (HashMap<String, Object>) mListItem.get(from);  
		        	mListItem.remove(item);//.remove(from);  
		        	mListItem.add(to, item);  
		        	mItemListadapter.notifyDataSetChanged();
		        	mIsDoSubmit = true;
		        }  
	    }; 
	
    private DraggableListView.OnItemClickListener mItemListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
			// TODO Auto-generated method stub
			mItemSelected = position;
			if( mIsEdit ){
				// 在draglistview中进行
				//showDialog();
			} else {
				Map<String, Object> item = mListItem.get(mItemSelected);
				String state = item.get(KeyEnum.state.toString()).toString();
				
				if( state.equalsIgnoreCase(MyVodStateEnum.pause.toString()) 
					|| state.equalsIgnoreCase(MyVodStateEnum.play.toString()) ){
					// 在播放或暂停状态，启动播放器
					mItemOfferingId = item.get(KeyEnum.offeringId.toString()).toString();
					showPlayerInner( PlayerType.lite.toString() );
				} else {
					// comm play
					mItemOfferingId = item.get(KeyEnum.offeringId.toString()).toString();
					showPlayerInner( PlayerType.my_mtv.toString() );
				}
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
			if( mIsEdit ){
				mBtnEdit.setText(getString(R.string.further_btn_editok));
				mItemListadapter.enableEdit();
				mSettingBar.setVisibility(View.VISIBLE);
				showList();
			} else {
				mBtnEdit.setText(getString(R.string.further_btn_edit));
				mItemListadapter.disableEdit();
				mSettingBar.setVisibility(View.GONE);
				// 检测是否数据有更改
				if( mIsDoSubmit ){
					mProcessTask = new processAsyncTask(MyMtv.this);
					mProcessTask.execute(myvodOperation.update);
					mIsDoSubmit = false;
				}
			}
		}
	};
		
	
	private View.OnClickListener mPlayModeListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if( mListItem.isEmpty() && !mIsEdit ){
				return;
			}
			
			// 没有逆序操作
			if( mPlayMode.equals(MyVodPlayModeEnum.in_order) ){
				mPlayMode = MyVodPlayModeEnum.random;
				showMessage(getString(R.string.mymtv_playmod_random));
				mBtnPlayMode.setBackgroundResource(R.drawable.myvod_random);
			} /* else if( mPlayMode.equals(MyVodPlayModeEnum.re_order) ){
				mPlayMode = MyVodPlayModeEnum.random;
			} */else if( mPlayMode.equals(MyVodPlayModeEnum.random) ){
				mPlayMode = MyVodPlayModeEnum.in_order;
				showMessage(getString(R.string.mymtv_playmod_inorder));
				mBtnPlayMode.setBackgroundResource(R.drawable.myvod_repeat);
			}
			//Log.d(TAG, "--->mPlayMode=" + mPlayMode.toString());
		}
	};
	
	private OnItemSelectedListener mSpinerListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			mPlayCount = position;
			mSpinner.setSelection(position);
			Log.d(TAG, "--->mPlayCount=" + mPlayCount);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * 设置RCServer回调函数
	 */
	protected BroadcastReceiver mVideoServiceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, intent.getAction());
			
			if( VideoConstant.QUERY_MSG.equalsIgnoreCase(intent.getAction()) ||
					VideoConstant.NEXT.equalsIgnoreCase(intent.getAction()) ){ // 查询消息
				if( mProgressDlg.isShowing() ){
					mProgressDlg.dismiss();
				}
				try {
					Bundle bundle = intent.getExtras();
					mItemState = bundle.getString(KeyEnum.state.toString());
					mItemAssetId = bundle.getString(KeyEnum.assetId.toString());
					mItemOfferingId = bundle.getString(KeyEnum.offeringId.toString());
					reBuildItemState();
					showList();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else if(VideoConstant.ERROR.equalsIgnoreCase(intent.getAction())){ // 系统消息
				showMessage(intent.getExtras().getString("error"));
			}
		}
		
	};
	
	
//	private RCServerListener mRcServerListener = new RCServerListener() {
//		
//		@Override
//		public void getVodState(Bundle bundle) {
//			// TODO Auto-generated method stub
//			if( mProgressDlg.isShowing() ){
//				mProgressDlg.dismiss();
//			}
//			try {
//				mItemState = bundle.getString(KeyEnum.state.toString());
//				mItemAssetId = bundle.getString(KeyEnum.assetId.toString());
//				mItemOfferingId = bundle.getString(KeyEnum.offeringId.toString());
//				reBuildItemState();
//				showList();
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//		
//		@Override
//		public void getVodMtvPlayResult(Bundle bundle) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void getVodMoviePlayResult(Bundle bundle) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void getCommPlayResult(Bundle bundle) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void getChannelUpdateResult(Bundle bundle) {
//			// TODO Auto-generated method stub
//			try {
//				mProcessTask = new processAsyncTask(MyMtv.this);
//				mProcessTask.execute(myvodOperation.query);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//	};

	
	//[end]    
	    
	//[start] test data
	private void getTestChannels()
	{
		for (int i = 0; i < 22; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>(); 

			map.put(KeyEnum.title.toString(), String.format("音乐--%d", i));
			map.put(KeyEnum.actor.toString(), "刘德华");
			map.put(KeyEnum.poster.toString(), "http://image1.webscache.com/baike/haibao/ipad/2008-08/202573033-321-2008-08-29%2015-19-47.jpg");
			map.put(KeyEnum.assetId.toString(), i);
			map.put(KeyEnum.offeringId.toString(), i);
			map.put(KeyEnum.categories.toString(), "流行");
			map.put(KeyEnum.bandwidth.toString(), "SD");
			map.put(KeyEnum.runTime.toString(), "7200");
			
			map.put(KeyEnum.type.toString(), TypeEnum.KTV);
			
			if( i == 2 ){
				map.put(KeyEnum.state.toString(), MyVodStateEnum.play.toString());
				map.put(KeyEnum.title.toString(), String.format("东方时空asdas啥打算sas"));
			}
			
			mListItem.add(map);  
		}
	}	
	//[end]	
}
