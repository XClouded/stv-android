package com.stv.supervod.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.stv.supervod.exception.ErrorCodeException;
import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.utils.Constant;
import com.stv.supervod.utils.Constant.MyVodStateEnum;
import com.stv.supervod.utils.ValidateUtil;

/**
 * Description: 
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2012-2-24 下午4:04:39 zhaojunfeng created
 */
public class MyVodServiceImpl {	
	/**
	 * myvod 添加节目返回结果
	 */
	public static enum MyVodAddResult{
		success, count_overflow, duration_overflow, channel_exsit, none
	};
	
	/**
	 * Description: 通知BO添加频道
	 * @Version1.0 2012-2-27 下午5:08:59 zhaojunfeng created
	 * @param channelId
	 * @param assetId
	 * @param type
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	public MyVodAddResult add( String channelId, String assetId, String type ) 
			throws ClientProtocolException, IOException, JSONException, ErrorCodeException 
	{ 
		if( ValidateUtil.isBlank(channelId) || ValidateUtil.isBlank(assetId) || ValidateUtil.isBlank(type) )
		{
			throw new ErrorCodeException( "20301" );
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(KeyEnum.channelId.toString(), channelId);
		params.put(KeyEnum.assetId.toString(), assetId);
		params.put(KeyEnum.type.toString(), type);
		HttpRequestServiceImpl request = new HttpRequestServiceImpl();
		StringBuilder sb = request.requestByHttpPostMyVod(params, Constant.supervod_myvod_add_url);	
	
		return parseAddJson(channelId,sb);
	}
		
	/**
	 * Description: 
	 * @Version1.0 2012-2-27 下午5:08:52 zhaojunfeng created
	 * @param sb
	 * @return
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	@SuppressWarnings("unused")
	private MyVodAddResult parseAddJson(String channelId,StringBuilder sb) 
			throws JSONException, ErrorCodeException
	{
		JSONObject root = new JSONObject(sb.toString());
		if(root == null){
			throw new ErrorCodeException("20305");
		}
		
		String result = root.getString(KeyEnum.result.toString());
		String state = root.getString(KeyEnum.state.toString());
		if( result.equals("02")){
			return MyVodAddResult.count_overflow;
		} else if (result.equals("03")){
			return MyVodAddResult.duration_overflow;
		} else if( result.equals("04") ){
			return MyVodAddResult.channel_exsit;
		}
		
		if( !result.equals("01") ){
			throw new ErrorCodeException("20305"); 
		}
		
		if( state.equalsIgnoreCase(MyVodStateEnum.play.toString()) ){
			// inform rcserver
		}
		return MyVodAddResult.success;
	}
	
	/**
	 * Description: Asset是否存在于我的频道
	 * @Version1.0 2012-3-16 下午3:29:35 zhaojunfeng created
	 * @param channelId
	 * @param assetId
	 * @param type
	 * @return true ：存在于我的频道，false：不存在
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	public Boolean isMyVod(String channelId, String assetId, String type) 
			throws ClientProtocolException, IOException, JSONException, ErrorCodeException 
	{
		if( ValidateUtil.isBlank(channelId) || ValidateUtil.isBlank(assetId) || ValidateUtil.isBlank(type) )
		{
			throw new ErrorCodeException( "20301" );
		}		
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(KeyEnum.assetId.toString(), assetId);
		params.put(KeyEnum.type.toString(), type);
		params.put(KeyEnum.channelId.toString(), channelId);
		HttpRequestServiceImpl request = new HttpRequestServiceImpl();
		StringBuilder sb = request.requestByHttpGet(params, Constant.supervod_myvod_ismyvod_url);	
	
		return parseIsMyvodJson(sb);
	}
	
	/**
	 * Description: 解析是否存在assetid
	 * @Version1.0 2012-3-16 下午3:29:19 zhaojunfeng created
	 * @param sb
	 * @return
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	@SuppressWarnings("unused")
	private Boolean parseIsMyvodJson(StringBuilder sb) 
			throws JSONException, ErrorCodeException{
		JSONObject root = new JSONObject(sb.toString());
		if(root == null){
			throw new ErrorCodeException("20305");
		}
		
		String result = root.getString(KeyEnum.is.toString());
		return result.equalsIgnoreCase("0");
	}
	
	/**
	 * Description: 从BO获取我的频道
	 * @Version1.0 2012-2-29 下午5:03:35 zhaojunfeng created
	 * @param channelId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	public Map<String, Object> channels( String channelId ) 
			throws ClientProtocolException, IOException, JSONException, ErrorCodeException{
		Map<String, String> params = new HashMap<String, String>();
		params.put(KeyEnum.channelId.toString(), channelId );
		
		HttpRequestServiceImpl httpRequest = new HttpRequestServiceImpl();
		StringBuilder sb = httpRequest.requestByHttpGet(params, Constant.supervod_myvod_channels_url);
		return parseChannelsListJson(sb);
	}
	
	/**
	 * Description: 解析我的频道数据进行显示，添加频道状态为正常
	 * @Version1.0 2012-2-29 下午5:03:45 zhaojunfeng created
	 * @param sb
	 * @return
	 * @throws JSONException
	 */
	private Map<String, Object>  parseChannelsListJson(StringBuilder sb) throws JSONException, ErrorCodeException {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		// 通过根名字获得整个JSON对象
		JSONArray listArray = null;
		JSONObject root = new JSONObject(sb.toString());
		
		if (root != null) {
			listArray = root.getJSONArray("programs");
			result.put(KeyEnum.channelId.toString(), root.getString(KeyEnum.channelId.toString()));
			result.put(KeyEnum.state.toString(), root.getString(KeyEnum.state.toString()));
			result.put(KeyEnum.offeringId.toString(), root.getString(KeyEnum.offeringId.toString()));
			result.put(KeyEnum.assetId.toString(), root.getString(KeyEnum.assetId.toString()));
		}
		
		String key = "";
		if (listArray != null && listArray.length() > 0) {
			for (int j = 0; j < listArray.length(); j++) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject obj = (JSONObject) listArray.opt(j);
				Iterator it = obj.keys();
				while (it.hasNext()) {
					key = it.next().toString();
					map.put(key, obj.get(key));
				}	
				map.put(KeyEnum.state.toString(), MyVodStateEnum.normal.toString());
				list.add(map);
			}
		}
		result.put(KeyEnum.list.toString(), list);
		return result;
	}
	
	/**
	 * Description: 通知BO更新我的频道
	 * @Version1.0 2012-2-29 下午5:25:13 zhaojunfeng created
	 * @param channelId
	 * @param offingIds
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	public void updateChannelList( String channelId, List<String> offingIds ) 
			throws ClientProtocolException, IOException, JSONException, ErrorCodeException {
		if( ValidateUtil.isBlank(channelId) )
		{
			throw new ErrorCodeException( "20301" );
		}
		
		String offlist = "";
		for (String string : offingIds) {
			offlist += string + ",";
		}
		if( offlist != "" ){
			offlist = offlist.substring(0, offlist.length() - 1);
		} else {
			offlist = ","; // 清空
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(KeyEnum.channelId.toString(), channelId);
		params.put("offeringIds", offlist);
		HttpRequestServiceImpl request = new HttpRequestServiceImpl();
		request.requestByHttpPostMyVod(params, Constant.supervod_myvod_update_url);	
	}
	
	/**
	 * Description: 从BO获取状态，可以直接使用query更准确
	 * @Version1.0 2012-2-29 下午5:24:45 zhaojunfeng created
	 * @param channelId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	public String state( String channelId )
			throws ClientProtocolException, IOException, JSONException, ErrorCodeException{
		if( ValidateUtil.isBlank(channelId) )
		{
			throw new ErrorCodeException( "20301" );
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(KeyEnum.channelId.toString(), channelId );
		
		HttpRequestServiceImpl httpRequest = new HttpRequestServiceImpl();
		StringBuilder sb = httpRequest.requestByHttpGet(params, Constant.supervod_myvod_state_url);
		return parseStateJson(sb);
	}
	
	/**
	 * Description: 解析状态
	 * @Version1.0 2012-2-29 下午5:25:00 zhaojunfeng created
	 * @param sb
	 * @return
	 * @throws JSONException
	 * @throws ErrorCodeException
	 */
	private String parseStateJson(StringBuilder sb) 
			throws JSONException, ErrorCodeException{
		// 通过根名字获得整个JSON对象
		JSONObject root = new JSONObject(sb.toString());
		return root.getString(KeyEnum.state.toString());
	}
	
}
