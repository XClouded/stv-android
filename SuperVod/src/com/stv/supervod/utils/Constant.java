package com.stv.supervod.utils;

/**
 * Description: 各种常量 Copyright (c) 永新视博 All Rights Reserved.
 * 
 * @version 1.0 2011-12-12 下午04:11:08 mustang created
 */
/**
 * Description: Copyright (c) 永新视博 All Rights Reserved.
 * 
 * @version 1.0 2011-12-21 下午03:32:41 mustang created
 */
public class Constant {

	/**
	 * 用户登录ID
	 */
	public  static String http_session_id;
	/**
	 * 文件下载保存路径
	 */
	public  static String download_dir = "";
	/**
	 * 图片过期时间 【3天】
	 */
	public static long interval_day = 3 * 24 * 60 * 60 * 1000;
	/**
	 * 各种URL常量
	 */
//	public static final String PUB_URL = "http://supervod.com/rs";
//	public static final String PUB_URL = "http://192.168.7.143/rs";
	public static final String PUB_URL = "http://192.168.14.61/rs";
	/**
	 * 登录
	 */
	public static final String login_url = PUB_URL + "/user/login";
	/**
	 * 退出(注销)
	 */
	public static final String logout_url = PUB_URL + "/logout.json";

	/**
	 * 用户信息注册
	 */
	public static final String register_url = PUB_URL + "/account/register.json";

	/**
	 * 首页不同分类列表的URL
	 */
	public static final String homepage_data_url = PUB_URL + "/home/assets";
	
	/**
	 * 分类-电影列表的URL
	 */
	public static final String category_film_url = PUB_URL + "/asset/films";
	
	/**
	 * 分类-电视剧列表的URL
	 */
	public static final String category_tv_url = PUB_URL + "/asset/tvs";
	
	/**
	 * 分类-高清列表的URL
	 */
	public static final String category_hd_url = PUB_URL + "/asset/hds";
	
	/**
	 * 分类-MTV列表的URL
	 */
	public static final String category_mtv_url = PUB_URL + "/ktv/singers";
	
	
	/**
	 * 获得电影详细页信息
	 */
	public static final String movie_detail_url = PUB_URL + "/asset/film/";

	/**
	 * 获得电视剧详细页信息
	 */
	public static final String tv_detail_url = PUB_URL + "/asset/tv/";
	
	/**
	 * 获取歌手歌曲列表
	 */
	public static final String songs_url = PUB_URL + "/ktv/songs";
	
	/**
	 * 回看频道URL
	 */
	public static final String supervod_channel_url = PUB_URL + "/tstv/channels";
	
	/**
	 * 搜索关键字URL
	 */
	public static final String search_keyword_url = PUB_URL + "/search/keywords";
	
	/**
	 * 搜索节目URL
	 */
	public static final String search_program_url = PUB_URL + "/search/assets";
	
	/**
	 * 影片分类URL
	 */
	public static final String categories_url = PUB_URL + "/asset/categories";
	
	/*
	 * 回看节目URL
	 */
	public static final String supervod_tvback_epg_url = PUB_URL + "/tstv/programs";
	
	/*
	 * 书签节目URL
	 */
	public static final String supervod_bookmark_url = PUB_URL + "/asset/bookmarks";
	
	/*
	 * 获取offingId
	 */
	public static final String supervod_offingid_url = PUB_URL + "/asset/getofferingid";
	
	/*
	 * 我的MTV中播放顺序
	 */
	public static enum MyVodPlayModeEnum{
		in_order, re_order, random 
	}
	
	/*
	 * 我的频道中的节目播放状态
	 */
	public static enum MyVodStateEnum{
		normal, play, pause, bookmark
	}
	
	public static enum ServiceTypeEnum{
		FVOD, SVOD, MOD
	}
	
	/**
	 * Description: 播放器类型，常用、我的电影、我的歌曲、简化（不调用播放接口）
	 * Copyright (c) 永新视博
	 * All Rights Reserved.
	 * @version 1.0  2012-3-8 下午5:05:53 zhaojunfeng created
	 */
	public static enum PlayerType{
		common, my_movie, my_mtv, lite
	};
	
	/**
	 * 添加我的频道 URL
	 */
	public static final String supervod_myvod_add_url = PUB_URL + "/myvod/add";
	
	/**
	 * 获取我的频道 URL
	 */
	public static final String supervod_myvod_channels_url = PUB_URL + "/myvod/channel";
	
	/**
	 * 更新我的频道 URL
	 */
	public static final String supervod_myvod_update_url = PUB_URL + "/myvod/update";
	
	/**
	 * 状态我的频道 URL
	 */
	public static final String supervod_myvod_state_url = PUB_URL + "/myvod/state";
	
	/**
	 * Asset是否存在于我的频道中
	 */
	public static final String supervod_myvod_ismyvod_url = PUB_URL + "/myvod/ismyvod";
	
	/*
	 * 推荐分值比例
	 */
	public static final float    RECOMMEND_RANK_SCALE	= 0.1f;
	
	/*
	 * treeIndex定义：电影风格、区域
	 */
	public static final String treeindex_film_style	= "0001-0001-0001";
	public static final String treeindex_film_area	= "0001-0001-0002";
	
	/*
	 * treeIndex定义：电视剧风格、区域
	 */
	public static final String treeindex_tv_style	= "0001-0002-0001";
	public static final String treeindex_tv_area	= "0001-0002-0002";
	
	/*
	 * treeIndex定义：KTV歌手、曲种(预留)
	 */
	public static final String treeindex_ktv_singer	= "0001-0003-0001";
	public static final String treeindex_ktv_sort	= "0001-0003-0002";
}
