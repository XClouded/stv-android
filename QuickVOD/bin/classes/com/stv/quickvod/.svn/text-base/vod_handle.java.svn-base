package com.stv.quickvod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.stv.tcp_client.TCPClient;

/**   
 * @Filename:    vod_handle.java  
 * @Package:     com.stv.quickvod  
 * @Description: vod_handle是点播主页面后台消息收发处理中心，以及实现登录、发送按键、挂机等操作的封装
 *               需要处理在向服务器发送数据前的所有准备，包括创建socket连接，处理各种发生的异常
 */

public class vod_handle {
	private final int timeover = 10000;        //10s登陆超时
	private final int enterplayTime = 100;     //进入播放页面初始化时间
	
	public static boolean completeInit = false;//播放页面初始化完成的标识
	public static boolean playStatus = false;  //播放状态标识
	public static boolean pauseStatus = false; //暂停状态标识
	public boolean exiting = false;            //退出系统的标识
	
	private String[] messageStrings = new String[2];
	
	private Handler timer ;                    //处理登陆超时的句柄
	private Handler enterplayHandler;          //处理播放到结尾调用回调函数的句柄
	
	private vod_model model_;
	private vod_view view_;
	
	public static TCPClient client = null;
	
	//以下回调函数是在播放界面实现的
	private static AppCallBack msgCallBack_Playend = null;       //播放到尾的回调
	private static PlayCallBack msgCallBack_Currentstatus = null;//当前的播放状态的回调
    
	public vod_handle(vod_view view) {
		model_ = new vod_model();
		this.view_ = view;
		timer = new Handler();
		enterplayHandler = new Handler();
	}
	
	/**
	 * Apply service to RCServer
	 */
	public void login(String phoneNum, String smartcard) {
		model_.setPhoneNum(phoneNum);
		model_.setSmartCard(smartcard);
		client = new TCPClient(handler, model_.getPhoneNum(), model_.getSmartCard());
        client.Call();
		timer.postDelayed(loginTimeover, timeover);
	}
	
	/**
	 * Turn Down
	 */
	public void quit() {
		if(client != null){
			client.Quit();
		}
	}
	
	/**
	 * Send value of key to RCServer
	 */
	public void sendKey(int value){
    	String keyValue = new String();
    	switch (value) {
		case R.id.key_up:
			keyValue = "2";
			break;
		case R.id.key_down:
			keyValue = "8";
			break;
		case R.id.key_left:
			keyValue = "4";
			break;
		case R.id.key_right:
			keyValue = "6";
			break;
		case R.id.key_ok:
			keyValue = "5";
			break;
		case R.id.key_close:
			keyValue = "0";
			break;
		case R.id.key_return:
			keyValue = "*";
			break;
		default:
			break;
		}
    	client.Digit(keyValue);
	}
	
	/**
	 * Close Socket
	 */
	public void exitsys() {
		exiting = true;
		frontpage.closeSocket();
	}
	
	/**
	 * Handle message from RCServer
	 */
    private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg){
    		String file = "";
    		view_.cancleDialog();
    		timer.removeCallbacks(loginTimeover);
    		switch(msg.arg1){
    		case vod_model.Message_Success_EnterVOD:
				file = "VOD页面状态";
				model_.setApplyStatus(true);
				model_.setPlayStatus(false);
				break;
			case vod_model.Message_Success_EnterPlay:
				file = "VOD播放状态";
				Log.d("点播节目成功", String.valueOf(model_.getPlayStatus()));
				enterplayHandler.postDelayed(runnable2, enterplayTime);
				if(!model_.getPlayStatus()) {	
					model_.setApplyStatus(false);
					model_.setPlayStatus(true);
					playStatus = true;
					messageStrings[0] = file;
					messageStrings[1] = String.valueOf(msg.arg2);
					view_.vodPlayActivity(messageStrings);
				} else {
					if (msgCallBack_Currentstatus != null) {
						msgCallBack_Currentstatus.startCallback(true, msg.arg2);//播放状态回调
					}
				}
				break;
			case vod_model.Message_Play_Pause:
				file = "VOD播放状态";
				enterplayHandler.postDelayed(runnable2, enterplayTime);
				if (!model_.getPlayStatus()) {
					model_.setApplyStatus(false);
					model_.setPlayStatus(true);
					pauseStatus = true;
					messageStrings[0] = file;
					messageStrings[1] = String.valueOf(msg.arg2);
					view_.vodPlayActivity(messageStrings);
				} else {
					if (msgCallBack_Currentstatus != null) {
						msgCallBack_Currentstatus.startCallback(false, msg.arg2);//暂停状态回调
					}
				}
				break;
			case vod_model.Message_Fail_NotExitUser:
				file = "服务器验证无此用户！";
				exitsys();
				dialogSysinfo(file);
				break;
			case vod_model.Message_Fail_SystemError:
				file = "VOD系统错误";
				exitsys();
				dialogSysinfo(file);
				break;
			case vod_model.Message_Fail_LackResources:
				file = "VOD系统资源不足";
				exitsys();
				dialogSysinfo(file);
				break;
			case vod_model.Message_Fail_CASwitchFreq:
				file = "VOD系统切换频点失败";
				exitsys();
				dialogSysinfo(file);
				break;
			case vod_model.Message_SystemMaintain:
				file = "VOD系统维护中";
				exitsys();
				dialogSysinfo(file);
				break;
			case vod_model.Message_Play_Stop:
				file = "点播节目已停止";
				model_.setPlayStatus(false);
				break;
			case vod_model.Message_Play_Success:
				file = "您的账号已登录";
				exitsys();
				dialogSysinfo("对不起，您的账号已在别处登录，您被迫下线！");
				break;
			case vod_model.Message_Play_Fail:
				file = "VOD页面状态";
				dialogSysinfo("点播节目失败");
				break;
			case vod_model.Message_ActiveUsers:
				file = "其他用户已经登陆";
				exitsys();
				dialogSysinfo("对不起，由于其他用户已经通过该账号登陆，VOD系统暂时不能提供服务！");
				break;
			case vod_model.Message_SystemError:
				file = "系统错误，请重新登陆";
				exitsys();
				dialogSysinfo(file);
				break;
			case vod_model.Message_TimeOut:
				file = "操作超时";
				exitsys();
				dialogSysinfo("对不起，由于您长时间没有操作，程序已经与服务器断开连接,如果您需要继续操作，请重新启动！");
				break;
			case vod_model.Message_LackBalance:
				file = "VOD页面状态";
				dialogSysinfo("余额不足 ");
				break;
			case vod_model.Message_NotOrder:
				file = "VOD页面状态";
				dialogSysinfo("没有订购该服务");
				break;
			case vod_model.Message_NetworkFail:
				if (!exiting) {
					file = "网络连接断开！";
					dialogSysinfo("网络连接断开！");
				} else {
					file = "正在退出系统...";
				}
				break;
			case vod_model.Message_ExitSuccess:
				ActivityStackControl.finishProgram();
				break;
			default:
				break;
    		}
    		view_.setLogInfoText(file);
    	}
	};
	
	private Runnable loginTimeover = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			dialogLoginTimeout();
			timer.removeCallbacks(loginTimeover);
		}
	};
	
	private Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(completeInit && model_.getApplyStatus()) {
				if (msgCallBack_Playend != null) {
					msgCallBack_Playend.startCallback();
				}
				completeInit = false;
				enterplayHandler.removeCallbacks(runnable2);
			} else {
				enterplayHandler.postDelayed(runnable2, enterplayTime);
			}
		}
	}; 
	
	private void dialogLoginTimeout(){
		view_.cancleDialog();
		if(!model_.getApplyStatus()) {
			new AlertDialog.Builder(view_)
			.setTitle(R.string.tip)
			.setMessage(R.string.loginfail)
			.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ActivityStackControl.finishProgram();
				}
			})
			.setPositiveButton(R.string.relogin, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
		            client.Call();
		            timer.postDelayed(loginTimeover, timeover);
		            view_.mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		            view_.mypDialog.setTitle(R.string.tip);
		            view_.mypDialog.setMessage("正在登录VOD点播服务器，请稍候...");
		            view_.mypDialog.setCancelable(false);
		            view_.mypDialog.show();
				}
			}).show();
		}
	}
	
	private void dialogSysinfo(String info){
		Activity activity = ActivityStackControl.getTop();
		if (activity != null) {
			AlertDialog.Builder tipDialogBuilder = new AlertDialog.Builder(activity);
			tipDialogBuilder.setTitle(R.string.tip);
			tipDialogBuilder.setMessage(info);
			tipDialogBuilder.setCancelable(false);
			
			if (info == "点播节目失败" 
				|| info == "余额不足 "
				|| info == "没有订购该服务") {
				
				tipDialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
			} else {
				tipDialogBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ActivityStackControl.finishProgram();
					}
				});
			}
			tipDialogBuilder.show();
		}
	}
	
	public static void registerCallback(AppCallBack callback, int callBackId) {
		if (callBackId == vod_model.ID_CALL_BACK_PLAYEND) {
			msgCallBack_Playend = callback;
		}
    }

	public static void registerPlayCallback(PlayCallBack callback, int callBackId) {
		if (callBackId == vod_model.ID_CALL_BACK_CURRENTSTATUS) {
			msgCallBack_Currentstatus = callback;
		}
	}
	
    public static void removeCallback(AppCallBack callback) {
    	if(callback == msgCallBack_Playend && msgCallBack_Playend != null) {
			msgCallBack_Playend = null;
		}
    } 
    
    public static void removePlayCallback(PlayCallBack callback) {
    	 if (callback == msgCallBack_Currentstatus && msgCallBack_Currentstatus != null) {
 			msgCallBack_Currentstatus = null;
 		}
    }
    
}
