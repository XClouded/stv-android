package com.stv.quickvod;

/**   
 * Filename:    vod_model.java  
 * Package:     com.stv.quickvod  
 * Description: vod_model是点播主页面后台数据中心，包含各种消息码、回调ID,以及存储手机号码、智能卡号等相关
 *              信息
 */

public class vod_model {
	public static final int Message_Success_EnterVOD = 96;   //申请服务成功：VOD界面状态
	public static final int Message_Success_EnterPlay = 97;  //申请服务成功：VOD播放状态
	public static final int Message_Fail_NotExitUser = 98;   //申请服务失败：无此用户
	public static final int Message_Fail_SystemError = 99;   //申请服务失败：VOD系统错误
	public static final int Message_Fail_LackResources = 100;//申请服务失败：VOD资源不足
	public static final int Message_Fail_CASwitchFreq = 101; //申请服务失败：CA系统切换频点失败
	public static final int Message_Play_Pause = 111;        //申请服务成功：VOD暂停状态
	public static final int Message_Play_Stop = 102;         //点播节目已停止
	public static final int Message_Play_Success = 103;      //点播节目成功
	public static final int Message_Play_Fail = 104;         //点播节目失败（进入点播页面）
	public static final int Message_SystemError = 105;       //系统错误，请重新连接
	public static final int Message_TimeOut = 106;           //操作超时
	public static final int Message_ActiveUsers = 107;       //申请服务失败：多个活跃用户
	public static final int Message_LackBalance = 108;       //余额不足 （进入点播页面）
	public static final int Message_NotOrder = 109;          //没有订购该服务 （进入点播页面）
	public static final int Message_SystemMaintain = 110;    //申请服务失败，系统维护中
	public static final int Message_ExitSuccess = 112;       //退出系统成功
	public static final int Message_NetworkFail = 1001;      //网络连接失败
	
	public static final int ID_CALL_BACK_TIMEOUT = 2001;
	public static final int ID_CALL_BACK_PLAYEND = 2002;
	public static final int ID_CALL_BACK_CONNETBREAK = 2003;
	public static final int ID_CALL_BACK_SYSTEMERROR = 2004;
	public static final int ID_CALL_BACK_CURRENTSTATUS = 2005;
	public static final int ID_CALL_BACK_OTHERUSERLOGIN = 2006;
	
    private String phonenum;
    private String smartcard;

    private boolean applyStatus = false;
    private boolean playStatus = false;
    
    
	public vod_model() { }
	public String getPhoneNum() { return phonenum; }
	public void setPhoneNum(String phone) { phonenum = phone; }
	public String getSmartCard() { return smartcard; }
	public void setSmartCard(String card) { smartcard = card;}
	
	public void setApplyStatus(boolean status) { applyStatus = status; }
	public boolean getApplyStatus() { return applyStatus; }
	public void setPlayStatus(boolean status) { playStatus = status; }
	public boolean getPlayStatus() { return playStatus; }
}
