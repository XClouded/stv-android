package com.stv.util;

import android.util.Log;

/**
 * Description: 日志工具类,正式打包前赋值logOFF=true
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2011-11-11 下午01:15:20 mustang created
 */
public class LogUtil {
	public static boolean logOFF = false;
	public static final int DEBUG = 1;
	public static final int ERROR = 2;
	public static final int INFO = 3;
	public static final int VERBOSE = 4;
	public static final int WARE = 5;

	public static void showLog(String tag, String msg, int type) {
		if (logOFF == false) {
			switch (type) {
			case DEBUG:
				Log.d(tag, msg);
				break;
			case ERROR:
				Log.e(tag, msg);
				break;
			case INFO:
				Log.i(tag, msg);
				break;
			case VERBOSE:
				Log.v(tag, msg);
				break;
			case WARE:
				Log.w(tag, msg);
				break;
			default:
				break;

			}
		}
	}
}
