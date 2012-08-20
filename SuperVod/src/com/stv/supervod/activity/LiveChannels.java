package com.stv.supervod.activity;

import android.os.Bundle;

/**
 * @author      Administrator
 * @description 电视频道
 * @authority   激活用户
 * @function    1、显示从今天开始的一周日期列表
 *              2、用户点击具体日期，向服务器请求具体日期的频道数据
 */
public class LiveChannels extends BaseActivity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livechannels);
    }
}
