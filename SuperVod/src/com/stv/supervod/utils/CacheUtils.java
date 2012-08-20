package com.stv.supervod.utils;

import java.util.List;
import java.util.Map;

public class CacheUtils {

	private CacheUtils(){
	}
	private  static CacheUtils cu = new CacheUtils();
	public static CacheUtils getInstance(){
		return cu;
	}
	
	/**
	 * 为了避免首页activity每次切换时把数据释放，把下载后的数据保存在这里
	 */
	public List<Map<String, Object>> homePageList = null;
	public List<Map<String, Object>> homePagePosters = null;
	
	/**
	 * 每次应用程序启动，在欢迎页面下载关键词、分类等信息
	 */
	public List<Map<String, Object>> keywordsList = null;
	public List<Map<String, Object>> categoryfilmstylelist = null;
	public List<Map<String, Object>> categoryfilmarealist = null;
	public List<Map<String, Object>> categorytvstylelist = null;
	public List<Map<String, Object>> categorytvarealist = null;
	/**
	 * Description: 退出时调用一下，把内存释放掉
	 * @Version1.0 2012-2-20 下午03:54:52 mustang created
	 */
	public static void gcCache(){
		cu.homePageList = null;
		cu.homePagePosters=null;
		cu.keywordsList = null;
		cu.categoryfilmarealist = null;
		cu.categoryfilmstylelist = null;
		cu.categorytvarealist = null;
		cu.categorytvstylelist = null;
		//cu=null;
	}
}
