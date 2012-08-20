package com.stv.supervod.utils;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;

import com.stv.supervod.activity.R;
import com.stv.supervod.exception.ErrorCodeException;

/**
 * Description: 
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2012-1-4 下午3:20:39 zhaojunfeng created
 */
public class ErrorCode {
	private static Context mCon;
	private static ErrorCode mInstatce = null;
	
	private ErrorCode(){}
	
	public static void openErrorCode( Context c ) {
		if( mInstatce == null ){
			mInstatce = new ErrorCode();
		}
		mCon = c;
	}
	
	public static final void closeErrorCode() {
		mCon = null;
		mInstatce = null;
	}
	
	/**
	 * Description: 
	 * @Version1.0 2012-1-4 下午3:20:32 zhaojunfeng created
	 * @param c
	 * @param error_code
	 * @return
	 */
	private final static String getErrorCodeString( String error_code ) {
		if( mInstatce == null ){
			return "null,must init";
		}
		
		int error_id = 0;
		Context c = mCon;
		try {
			error_id = Integer.parseInt(error_code);
		} catch (Exception e) {
			// TODO: handle exception
			error_id = 0;
		}
		
		switch (error_id) {
		/*
		 * System error 
		 */
		case 10001:	return c.getResources().getString(R.string.system_err_10001 );
		case 10002:	return c.getResources().getString(R.string.system_err_10002 );
		case 10003:	return c.getResources().getString(R.string.system_err_10003 );
		case 10004:	return c.getResources().getString(R.string.system_err_10004 );
		case 10005:	return c.getResources().getString(R.string.system_err_10005 );
		
		/*
		 * Services error 
		 */		
		// register error
		case 20001:	return c.getResources().getString(R.string.server_err_20001 );
		case 20002:	return c.getResources().getString(R.string.server_err_20002 );
		case 20003:	return c.getResources().getString(R.string.server_err_20003 );
		case 20004:	return c.getResources().getString(R.string.server_err_20004 );
		
		// loading and quit
		case 20101:	return c.getResources().getString(R.string.server_err_20101 );
		case 20102:	return c.getResources().getString(R.string.server_err_20102 );
		case 20103:	return c.getResources().getString(R.string.server_err_20103 );
		
		// get data
		case 20201:	return c.getResources().getString(R.string.server_err_20201 );
		case 20202:	return c.getResources().getString(R.string.server_err_20202 );
		case 20203:	return c.getResources().getString(R.string.server_err_20203 );
		
		// my epg
		case 20301:	return c.getResources().getString(R.string.server_err_20301 );
		case 20302:	return c.getResources().getString(R.string.server_err_20302 );
		case 20303:	return c.getResources().getString(R.string.server_err_20303 );
		case 20304:	return c.getResources().getString(R.string.server_err_20304 );
		
		default:	return c.getResources().getString(R.string.system_err_id )+error_code;
		}
	}
	
	/**
	 * Description: 公共的错误信息
	 * @Version1.0 2012-1-18 下午03:16:33 mustang created
	 * @return
	 */
	
	public static String getErrorInfo(Exception excetpion){
		if(excetpion instanceof ErrorCodeException){
			return getErrorCodeString(excetpion.getMessage());
		}else if(excetpion instanceof ClientProtocolException&&mCon!=null){
			return mCon.getResources().getString(R.string.server_err_30001 );
		}else if(excetpion instanceof IOException&&mCon!=null){
			return mCon.getResources().getString(R.string.server_err_30002 );
		}else if(excetpion instanceof JSONException&&mCon!=null){
			return mCon.getResources().getString(R.string.server_err_30003 );
		}else{
			//return mCon.getResources().getString(R.string.server_err_30004 );
			return "发生未知错误";
		}
	}
	
	
	
}
