package com.stv.supervod.utils;

import android.content.Context;

/**
 * Description: DP和PX之间的单位转换 Copyright (c) 永新视博 All Rights Reserved.
 * 
 * @version 1.0 2011-12-5 下午03:41:36 mustang created
 */
public class DpPxUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale);
	}

}
