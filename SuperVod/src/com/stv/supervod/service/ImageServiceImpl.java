package com.stv.supervod.service;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.stv.supervod.utils.Constant;
import com.stv.supervod.utils.ValidateUtil;

/**
 * Description:建立、删除图片及索引 Copyright (c) 永新视博 All Rights Reserved.
 * 
 * @version 1.0 2011-12-12 上午10:19:55 mustang created
 */
public class ImageServiceImpl {

	/**
	 * Description: 修改图片时间 文件夹路径格式：有sdcard情况
	 * /mnt/sdcard/com.com.stv.supervod.activity/files
	 * 无sdcard情况是：/date/date/com.com.stv.supervod.activity/files
	 * 
	 * @Version1.0 2011-12-12 上午11:34:53 mustang created
	 * @param act
	 */
	public static void deleteTimeoutImage(Activity act) {
		long now = Calendar.getInstance().getTimeInMillis();
		File dir = new File(Constant.download_dir);
		String[] filesnames = dir.list();
		if (filesnames != null && filesnames.length > 0) {
			for (String name : filesnames) {
				if (!ValidateUtil.isBlank(name)) {
					File file = new File(dir.getAbsoluteFile(), name);
					// 文件已经过期，需要删除
					if (file != null && file.isFile() && now - file.lastModified() > Constant.interval_day) {
						file.delete();
					}
				}
			}
		}

	}

	/**
	 * Description: 根据文件名字取出本地文件，如果返回为null需要把文件的URL单独扔到一个list中，
	 * 对list起一个单独下载的线程,刷新页面中没有海报的数据。
	 * 
	 * @Version1.0 2011-12-12 下午01:36:20 mustang created
	 * @param imgName
	 * @param act
	 * @return
	 */
	public static Drawable getImageDrawableByName(String imgName) {
		String path = Constant.download_dir;
		if (!ValidateUtil.isBlank(imgName)) {
			File file = new File(path, imgName);
			if (file.isFile()) {
				Drawable da = Drawable.createFromPath(path + "/" + imgName);
				if( da != null){
					// 修改文件的最后访问时间
					file.setLastModified(System.currentTimeMillis());
					return da;
				}
			}else{
				return null;
			}
		}
		return null;
	}

	/**
	 * Description: 通过URL获取图片文件
	 * 
	 * @Version1.0 2011-12-23 下午05:29:15 mustang created
	 * @param url
	 * @return
	 */
	public static Drawable getImageDrawableByUrl(String url) {
		String[] urls = null;
		if (!ValidateUtil.isBlank(url)) {
			urls = url.split("/");
		}
		if (urls != null && urls.length > 0) {
			String imgname = urls[urls.length - 1];
			Drawable da = getImageDrawableByName(imgname);
			return da;
		}
		return null;
	}

}
