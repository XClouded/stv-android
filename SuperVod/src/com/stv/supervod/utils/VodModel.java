package com.stv.supervod.utils;

public class VodModel {
	private String smartCardId;//
	private byte channelId; //
	private String offeringId;
	private byte isBookmark;//0表示从头开始播放，1表示从书签开始播放
	private byte isPassword;//0表示密码，1：非密码
	private String password; //isPassword为0时表示密码登陆 ； 为1时表示电话号码登陆
	//for batch play
	private byte action;//0:点播，1：更新频道数据
	private byte playMode;//0:顺序，1：逆序，2：随机
	private int playCount;//播放次数
	//for play control
	private byte operation;//操作码，快进、快退、播放、暂停
	private short npt_begin;//开始播放的时间点
	//申请状态
	private boolean applyStatus = false;
	
	public boolean isApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(boolean applyStatus) {
		this.applyStatus = applyStatus;
	}
	public byte getOperation() {
		return operation;
	}
	public void setOperation(byte operation) {
		this.operation = operation;
	}
	public short getNpt_begin() {
		return npt_begin;
	}
	public void setNpt_begin(short npt_begin) {
		this.npt_begin = npt_begin;
	}
	public byte getAction() {
		return action;
	}
	public void setAction(byte action) {
		this.action = action;
	}
	public byte getPlayMode() {
		return playMode;
	}
	public void setPlayMode(byte playMode) {
		this.playMode = playMode;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
		
	public String getSmartCardId() {
		return smartCardId;
	}
	public void setSmartCardId(String smartCardId) {
		this.smartCardId = smartCardId;
	}
	public byte getChannelId() {
		return channelId;
	}
	public void setChannelId(byte channelId) {
		this.channelId = channelId;
	}
	public String getOfferingId() {
		return offeringId;
	}
	public void setOfferingId(String offeringId) {
		this.offeringId = offeringId;
	}
	public byte getIsBookmark() {
		return isBookmark;
	}
	public void setIsBookmark(byte isBookmark) {
		this.isBookmark = isBookmark;
	}
	public byte getIsPassword() {
		return isPassword;
	}
	public void setIsPassword(byte isPassword) {
		this.isPassword = isPassword;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
