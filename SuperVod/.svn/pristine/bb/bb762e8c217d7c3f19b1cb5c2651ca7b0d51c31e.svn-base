package com.stv.supervod.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stv.supervod.exception.ErrorCodeException;
import com.stv.supervod.service.HttpDownloadImpl;
import com.stv.supervod.service.ImageDownloader;
import com.stv.supervod.service.MyVodServiceImpl;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.service.HttpDownloadImpl.TypeEnum;
import com.stv.supervod.service.MyVodServiceImpl.MyVodAddResult;
import com.stv.supervod.service.VideoServiceImpl;
import com.stv.supervod.utils.Constant;
import com.stv.supervod.utils.Constant.PlayerType;
import com.stv.supervod.utils.Constant.ServiceTypeEnum;
import com.stv.supervod.utils.DpPxUtil;
import com.stv.supervod.utils.ErrorCode;
import com.stv.supervod.utils.ValidateUtil;

/**
 * @author      Administrator
 * @description 影片详细页
 * @authority   登录或激活业务用户
 * @function    1、向用户展示固定影片详细信息
 *              2、用户点击评分按钮，判断用户是否已登录，未登录则进入登录页面：Login
 *              3、用户点击收藏按钮，判断用户是否已激活，未激活则进入激活页面：Register，
 *                已激活则提交数据到服务器，并提示用户收藏成功
 *              4、用户点击播放按钮，判断用户是否已激活，未激活则进入激活页面：Register，
 *                已激活则进入播放页面：Play
 */
public class CommDetail extends BaseActivity{
	/* 常量 */
	private final String TAG = "CommDetail";
	private final String mChannelId = "2";
	
	/* 全局变量 */
	private Map<String, Object> mProgram = null;
	private Map<String, Object> mServiceInfo = null;
	private String mAssetId = "";
	private Boolean mInfoVaild = false;
	private MyVodAddResult mMyVodAddResult = MyVodAddResult.none;
	private Boolean mIsInMyvod = false;
	
	/* 控件 */
	private Button mBtnPlay = null;
	private Button mBtnAddMyFav = null;
	private Button mBtnAddMyVod = null;
	private Button mBtnBack = null;
	private ImageView mIvPoster = null;
	private TextView mTvTitle;
	private TextView mTvInfoRow1;
	private TextView mTvInfoRow2;
	private TextView mTvInfoRow3;
	private TextView mTvInfoDesc;
	private TextView mTvInfoScore1;
	private TextView mTvInfoScore2;
	private RatingBar mRateBar;

	private processAsyncTask mProcessTask = null;
	private ProgressDialog mProgressDlg = null;
	private Toast mToast;
	
	private enum CommDetailOp{
		error, get_info, add_myvod, play
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_detail);
        
		// 初始化
		mBtnBack = (Button) findViewById(R.id.comm_detail_btn_back);
		mBtnAddMyFav = (Button) findViewById(R.id.comm_detail_btn_fav);
		mBtnAddMyVod = (Button) findViewById(R.id.comm_detail_btn_epg);
		mBtnPlay = (Button) findViewById(R.id.comm_detail_btn_play);
		
		mIvPoster = (ImageView) findViewById(R.id.comm_detail_iv_poster);
		mTvTitle = (TextView) findViewById(R.id.comm_detail_tv_title);
		mTvInfoRow1 = (TextView) findViewById(R.id.comm_detail_tv_row1);
		mTvInfoRow2 = (TextView) findViewById(R.id.comm_detail_tv_row2);
		mTvInfoRow3 = (TextView) findViewById(R.id.comm_detail_tv_row3);
		mTvInfoDesc = (TextView) findViewById(R.id.comm_detail_tv_desc);
		mTvInfoScore1 = (TextView) findViewById(R.id.ratingScore1);
		mTvInfoScore2 = (TextView) findViewById(R.id.ratingScore2);		
		mRateBar = (RatingBar) findViewById(R.id.comm_detail_ratingBar);
		
		mProgressDlg = new ProgressDialog(this);
		mProgressDlg.setMessage("数据加载中...");
		mProgressDlg.setIndeterminate(true);
		mProgressDlg.setCancelable(true);
		        
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, DpPxUtil.dp2px(this, 60));
		
        // 绑定数据
        mBtnBack.setOnClickListener(mBtnBackListener);
        mBtnAddMyFav.setOnClickListener(mBtnAddMyFavListener);
        mBtnAddMyVod.setOnClickListener(mBtnAddMyVodListener);
        mBtnPlay.setOnClickListener(mBtnPlayListener);

        // 获取数据进行显示
        mAssetId = getIntent().getStringExtra(KeyEnum.assetId.toString());
        
        mProcessTask = new processAsyncTask(this);
        mProcessTask.execute(CommDetailOp.get_info);
        
        Log.d(TAG, "mAssetId="+mAssetId);
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mToast.cancel();
		super.onDestroy();
	}
	
	//[start] 后台操作线程
	private class processAsyncTask extends AsyncTask<CommDetailOp, String, CommDetailOp>{
		private Context mCon;
		private String mLastError = "";
		
		public processAsyncTask( Context con ){
			mCon = con;
			mProgressDlg.show();
		}
		
		@Override
		protected void onPostExecute(CommDetailOp result) {
			// TODO Auto-generated method stub
			switch (result) {
			case get_info:
				showInfo();
				break;
			case add_myvod:
				showAddMyVodResult();
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
		protected CommDetailOp doInBackground(CommDetailOp... params) {
			// TODO Auto-generated method stub
			CommDetailOp ret = params[0];
			try {
				if( params[0] == CommDetailOp.get_info ){
					//[start] 获取节目详细信息
					HttpDownloadImpl httpDownload = new HttpDownloadImpl();
					mProgram = httpDownload.downloadMovieDetail(mAssetId);
					mServiceInfo = httpDownload.downloadOffingId(mAssetId, TypeEnum.MOVIE.toString());
					
					MyVodServiceImpl myvodSer = new MyVodServiceImpl();
					mIsInMyvod = myvodSer.isMyVod("1", mAssetId, TypeEnum.MOVIE.toString());
					if( mProgram.isEmpty() || mServiceInfo.isEmpty() ){
						mLastError = "没有有效节目信息";
						ret = CommDetailOp.error;
					} else if( !mServiceInfo.containsKey(KeyEnum.serviceType.toString()) 
							|| !mServiceInfo.containsKey(KeyEnum.offeringId.toString())  ){
						mLastError = "没有有效节目信息";
						ret = CommDetailOp.error;
					}
					//[end]
				} else if( params[0] == CommDetailOp.add_myvod ){
					//[start] 加入我的频道
					MyVodServiceImpl myvodSer = new MyVodServiceImpl();
					mMyVodAddResult = myvodSer.add( mChannelId, mAssetId, TypeEnum.MOVIE.toString());
					//[end]
				} /*else if( params[0] == CommDetailOp.play ){
					//[start] 播放
					
					//[end]
				}*/
			} catch (Exception e) {
				e.printStackTrace();
				mLastError = ErrorCode.getErrorInfo(e);
				ret = CommDetailOp.error;
			}	
			return ret;
		}

	}
	//[end]
	
	//[start] 显示
	private void showInfo()
	{
		try {
			String row2 = getString(R.string.detail_txt_director)+ 
					mProgram.get(KeyEnum.director.toString()).toString();
			String row3 = getString(R.string.detail_txt_actor) + 
					mProgram.get(KeyEnum.actor.toString()).toString();
			String row1 = getString(R.string.detail_txt_type) + 
					mProgram.get(KeyEnum.categories.toString()).toString();
			String desc = mProgram.get(KeyEnum.descrip.toString()).toString();			

			mTvTitle.setText(mProgram.get(KeyEnum.title.toString()).toString());
			mTvInfoRow1.setText(row1 );
			mTvInfoRow2.setText( row2 );
			mTvInfoRow3.setText( row3 );
			
			// 解析评分
			String ratingBar = mProgram.get(KeyEnum.recommendedRank.toString()).toString();
			if (!ValidateUtil.isBlank(ratingBar)) {
				float vr = Float.parseFloat(ratingBar);
				if (vr > -1) {
					vr = vr / 10;
					mRateBar.setRating(vr);
				}
				String[] rs = (vr + "").split("\\.");
				if (rs != null && rs.length > 1) {
					mTvInfoScore1.setText(rs[0] + ".");
					mTvInfoScore2.setText(rs[1]);
				}
			} else {
				mRateBar.setRating(5);
				mTvInfoScore1.setText("5.");
				mTvInfoScore2.setText("0");
			}

			mTvInfoDesc.setText(desc);
			
	        String poster = mProgram.get(KeyEnum.poster.toString()).toString();
			if (!ValidateUtil.isBlank(poster)) {
		        ImageDownloader downloader =ImageDownloader.getImageDownloader();
		        downloader.download(poster, mIvPoster);
			}
			
			mInfoVaild = true;
		} catch (Exception e) {
			// TODO: handle exception
			showMessage( e.getMessage() );
		}
		if( mProgressDlg.isShowing() ){
			mProgressDlg.dismiss();
		}
	}
	
	/**
	 * Description: 显示加入我的频道结果
	 * @Version1.0 2012-3-8 上午11:10:55 zhaojunfeng created
	 */
	private void showAddMyVodResult(){
		String msg = "";
		switch (mMyVodAddResult) {
		case channel_exsit:
			msg += getResources().getString(R.string.server_err_20304 );
			break;
		case count_overflow:
			msg += getResources().getString(R.string.server_err_20302 );
			break;
		case duration_overflow:
			msg += getResources().getString(R.string.server_err_20303 );
			break;
		case success:
			msg += getResources().getString(R.string.detail_txt_addvod_ok );
			break;
		case none:
		default:
			msg += getResources().getString(R.string.detail_txt_addvod_error );
			break;
		}
		showMessage(msg);
	}
	
	/**
	 * Description: Toast显示信息
	 * @Version1.0 2012-3-8 上午10:57:12 zhaojunfeng created
	 * @param msg
	 */
	private void showMessage( String msg ){
		mToast.cancel();
		mToast.setText(msg);
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}	
	//[end]
	
	
	//[start] Listerner
	private View.OnClickListener mBtnBackListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Framework.switchActivityBack();
		}
	}; 
	
	private View.OnClickListener mBtnPlayListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if( mInfoVaild ){
				if( mServiceInfo != null ){
					String offingId = mServiceInfo.get(KeyEnum.offeringId.toString()).toString();
					VideoServiceImpl impl = VideoServiceImpl.getInstance();
					if( impl.getPlayStatus(offingId) ){
						// 已经在播放了，直接启动播放器
						CommDetail.this.showPlayer( 
								offingId, 
								mProgram.get(KeyEnum.title.toString()).toString(),
								false, PlayerType.lite);
					} else {
						// 播放新节目
						if( ServiceTypeEnum.MOD.toString().equalsIgnoreCase(
								mServiceInfo.get(KeyEnum.serviceType.toString()).toString()) ){
							// 显示收费提示框
							float price = Integer.parseInt(mServiceInfo.get(KeyEnum.price.toString()).toString()) / 100;
							showServiceDialog( Float.toString(price), CommDetailOp.play );
						} else { // 免费节目，直接播放
							CommDetail.this.showPlayer( 
									offingId, 
									mProgram.get(KeyEnum.title.toString()).toString(),
									false,PlayerType.common);
						}
					}
				}
			}			
		}
	};

	private View.OnClickListener mBtnAddMyFavListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if( mInfoVaild ){
				String title = mProgram.get(KeyEnum.title.toString()).toString();
				String type = TypeEnum.MOVIE.toString();
				String category = mProgram.get(KeyEnum.categories.toString()).toString();
				String poster = mProgram.get(KeyEnum.poster.toString()).toString();
				boolean ret = addMyfav(title, type, category, poster, mAssetId); 
				if( ret ){
					showMessage( getString(R.string.detail_txt_addfav_ok) );				
				} else {
					showMessage(getString(R.string.detail_txt_addfav_error));			
				}			
			}
		}
	};
	
	private View.OnClickListener mBtnAddMyVodListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if( mInfoVaild ){
				if( mServiceInfo != null ){
					if( mIsInMyvod ){
						// 已经加入我的频道了
						showMessage( getString(R.string.myvod_program_exist) );
					} else if( ServiceTypeEnum.MOD.toString().equalsIgnoreCase(
							mServiceInfo.get(KeyEnum.serviceType.toString()).toString()) ){
						// 显示收费提示框
						float price = Integer.parseInt(mServiceInfo.get(KeyEnum.price.toString()).toString()) / 100;
						showServiceDialog( Float.toString(price), CommDetailOp.add_myvod );
					} else { // 免费节目，直接加入
				        mProcessTask = new processAsyncTask(CommDetail.this);
				        mProcessTask.execute(CommDetailOp.add_myvod);
					}
				}
			}
		}
	};	
	//[end]    
	    
	//[start] 对话框	
	/**
	 * Description: 显示收费对话框 
	 * @Version1.0 2012-3-8 上午10:36:51 zhaojunfeng created
	 */
	private void showServiceDialog( String price, final CommDetailOp op ){
		AlertDialog.Builder bd = new AlertDialog.Builder(CommDetail.this);
		bd.setTitle(R.string.further_dlg_title);
		
		String msg = String.format(getString(R.string.detail_txt_price), price);
		
		bd.setMessage(msg);
		bd.setPositiveButton( getString(R.string.framework_dlg_btn_ok)
				, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				if( op == CommDetailOp.play ){
					CommDetail.this.showPlayer( 
							mServiceInfo.get(KeyEnum.offeringId.toString()).toString(), 
							mProgram.get(KeyEnum.title.toString()).toString(),
							false,PlayerType.common);
				} else {
			        mProcessTask = new processAsyncTask(CommDetail.this);
			        mProcessTask.execute(op);				
				}
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

	
	//[start] test data
	private void getTestInfo(){
		// ... get data
		mProgram = new HashMap<String, Object>();
		mProgram.put(KeyEnum.title.toString(), "龙门飞甲");
		mProgram.put(KeyEnum.poster.toString(), "http://www.baidu.com/img/baidu_jgylogo3.gif");
		mProgram.put(KeyEnum.categories.toString(), "惊悚、3D");
		mProgram.put(KeyEnum.recommendedRank.toString(), "85");
		mProgram.put(KeyEnum.director.toString(), "徐克");
		mProgram.put(KeyEnum.actor.toString(), "李连杰");
		mProgram.put(KeyEnum.runTime.toString(), "2000");
		mProgram.put(KeyEnum.bandwidth.toString(), "SD");
		mProgram.put(KeyEnum.descrip.toString(), "费拉了拉了上拉力赛拉链上两大lsd拉萨的");
		mProgram.put(KeyEnum.assetId.toString(), "2000");
	}
	//[end]
}
