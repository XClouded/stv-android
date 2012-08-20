package com.stv.supervod.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.stv.supervod.service.Connect2RCServer;
import com.stv.supervod.service.HttpDownloadImpl;
import com.stv.supervod.service.ImageServiceImpl;
import com.stv.supervod.utils.AlertUtils;
import com.stv.supervod.utils.CacheUtils;
import com.stv.supervod.utils.Constant;
import com.stv.supervod.utils.ErrorCode;

/**
 * @author Administrator
 * @description 欢迎页面
 * @authority 所有用户
 * @function 1、建立网络连接，选择服务器 2、用户份判断，记住登录信息用户自动登录到服务器
 *           3、遍历存放图片文件夹，删除最后访问时间大于本地时间3天的图片，同步数据库
 */
public class Welcome extends Activity {

	private HttpDownloadImpl downloadImpl = new HttpDownloadImpl();
	private CacheUtils instance = CacheUtils.getInstance();

	private Activity act = this;
	private ProgressBar progressBar;
	private int num = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		ErrorCode.openErrorCode(this);
		File dir = null;
		// 初始文件下载路径
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + "/files");
			if (!dir.exists()) {
				dir.mkdirs();
			}
		} else {
			dir = this.getFilesDir();
		}
		Constant.download_dir = dir.getAbsolutePath();
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

		if (isNetworkAvailable(this)) {
			new Thread() {
				public void run() {
					try {
						ImageServiceImpl.deleteTimeoutImage(act);
						instance.keywordsList = downloadImpl.downloadKeysList(num);
						instance.categoryfilmstylelist = downloadImpl.downloadCategorysList(Constant.treeindex_film_style);
						instance.categoryfilmarealist = downloadImpl.downloadCategorysList(Constant.treeindex_film_area);
						instance.categorytvarealist = downloadImpl.downloadCategorysList(Constant.treeindex_tv_area);
						instance.categorytvstylelist = downloadImpl.downloadCategorysList(Constant.treeindex_tv_style);
						handler.sendEmptyMessage(0);
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.obj = ErrorCode.getErrorInfo(e);
						handler.sendMessage(msg);
					}
				}
			}.start();
			Connect2RCServer.getInstance().connet2rcserver();
		} else {
			new AlertDialog.Builder(this).setTitle(R.string.tip).setMessage("您当前没有连接任何网络，请检查wifi或移动网络是否打开!")
					.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Description: 判断网络是否打开
	 * 
	 * @Version1.0 2012-2-29 下午05:02:24 ljnalex created
	 * @param activity
	 */
	public boolean isNetworkAvailable(Activity mActivity) {
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Description: 登录页
	 * 
	 * @Version1.0 2012-1-11 下午05:02:24 mustang created
	 * @param cityname
	 */
	public void goLoginPage() {
		Intent intent = new Intent();
		intent.setClass(this, Login.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 关闭滚动条,所有和视图相关的操作都要放在这里做，新启的线程是不能操作视图控件的。
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				progressBar.setVisibility(View.GONE);
				goLoginPage();

			} else {
				AlertUtils.displayToast(act, (String) msg.obj);
			}
		}

	};
}
