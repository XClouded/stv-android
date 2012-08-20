package com.stv.supervod.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.stv.supervod.exception.ErrorCodeException;
import com.stv.supervod.utils.Constant;

public class RegisterServiceImpl {

	public enum LoginInfoEnum {
		registerinfo, user, ispsd, psd, mycity, psd_md5
	}

	/**
	 * Description: 验证通过后保存用户名密码，以及是否自动登录等信息
	 * 注意：需要指定为数据的共享模式，系统默认只能一个activity使用数据
	 * 
	 * @Version1.0 2011-12-6 上午10:14:10 mustang created
	 * @param act
	 * @param username
	 * @param password
	 * @param auto_login
	 * @param remember_password
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws NoSuchAlgorithmException 
	 */
	public void login(Activity act, String username, String password, String mycity, String pwtype) throws ClientProtocolException, 
			IOException, ErrorCodeException, NoSuchAlgorithmException {
		SharedPreferences sp = act.getSharedPreferences(LoginInfoEnum.registerinfo.toString(), Activity.MODE_PRIVATE);
		SharedPreferences.Editor se = sp.edit();
		se.putString(LoginInfoEnum.user.toString(), username);
		se.putString(LoginInfoEnum.ispsd.toString(), pwtype);
		se.putString(LoginInfoEnum.mycity.toString(), mycity);
		se.putString(LoginInfoEnum.psd.toString(), password);
		
		if (pwtype.equals("0")) {
			//MD5加密
			password = toMd5(password.getBytes());
			Log.d("------------MD5之后的结果---------------", password);
		}
		se.putString(LoginInfoEnum.psd_md5.toString(), password);
			
		se.commit();
		loginService(username, password, pwtype);
	}

	/**
	 * Description: 获取用户信息
	 * 
	 * @Version1.0 2011-12-7 下午03:54:49 mustang created
	 * @param act
	 * @return
	 */
	public SharedPreferences getLocalUserinfo(Activity act) {
		SharedPreferences sp = act.getSharedPreferences("registerinfo", Activity.MODE_PRIVATE);
		return sp;
	}

	/**
	 * Description: 登录服务器
	 * 
	 * @Version1.0 2011-12-7 下午03:02:58 mustang created
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void loginService(String username, String password, String pswtype) throws ClientProtocolException, 
			IOException, ErrorCodeException {
		Map<String, String> params = new HashMap<String, String>();
		params.put(LoginInfoEnum.user.toString(), username);
		params.put(LoginInfoEnum.ispsd.toString(), pswtype);
		params.put(LoginInfoEnum.psd.toString(), password);
		
		HttpRequestServiceImpl httpRequest = new HttpRequestServiceImpl();
		httpRequest.requestByHttpPost(params, Constant.login_url);
	}

	/**
	 * Description: 退出服务器
	 * 
	 * @Version1.0 2011-12-7 下午03:02:58 mustang created
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean logoutService(Activity act) {
		/*Map<String, String> map = getLocalUserinfo(act);
		String username = (String) map.get("username");
		String password = (String) map.get("username");
		// 注销的同时标识为下线
		SharedPreferences sp = act.getSharedPreferences("registerinfo", Activity.MODE_PRIVATE);
		if (sp != null) {
			sp.edit().putBoolean("online", false).commit();
		}
		if (!ValidateUtil.isBlank(username) && !ValidateUtil.isBlank(password)) {
			// TODO 据说注销这里暂时是不需要发请求的，服务器自己会把流推完。
		}*/
		return true;
	}

	/**
	 * Description: 添加相关信息，去服务器端验证有效性
	 * 
	 * @Version1.0 2011-12-6 下午02:43:29 mustang created
	 * @param username
	 * @param password
	 * @param email
	 * @param phone
	 * @param id_number
	 * @param smartcard
	 * @param service_password
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws ErrorCodeException 
	 */
	public boolean saveRegisterInfo(String username, String password, String phone, String id_number, String smartcard, String service_password)
			throws ClientProtocolException, IOException, ErrorCodeException {
		// String json1 = "{"+ "\"query\": \"Pizza\", "+
		// "\"locations\": [ 94043, 90210 ] "+ "}";

		StringBuilder sj = new StringBuilder();
		sj.append("{");
		sj.append("\"username\"").append(":").append("\"").append(username).append("\",");
		sj.append("\"password\"").append(":").append("\"").append(password).append("\",");
		sj.append("\"phone\"").append(":").append("\"").append(phone).append("\",");
		sj.append("\"id_number\"").append(":").append("\"").append(id_number).append("\",");
		sj.append("\"smartcard\"").append(":").append("\"").append(smartcard).append("\"");
		sj.append("}");
		//HttpRequestServiceImpl httpRequest = new HttpRequestServiceImpl();
		//httpRequest.requestByHttpPost(sj.toString(), "");
		// TODO 对SB返回来的JSON串进行解析，如果正确返回true,否则返回false;
		return true;
	}

	private String toMd5(byte[] bytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            return to32HexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            Log.v("he--------------------------------ji", "toMd5(): " + e);
            throw new RuntimeException(e);
            // 05-20 09:42:13.697: ERROR/hjhjh(256):
            // 5d5c87e61211ab7a4847f7408f48ac
        }
	}
	
	private String to32HexString(byte[] bytes, String separator) {
	    StringBuilder hexString = new StringBuilder();
	    for (byte b : bytes) {
	        String hex = Integer.toHexString(0xFF & b);
	        if(hex.length()==1){
	            hexString.append('0');
	        }
	        hexString.append(hex).append(separator);
	    }
	    return hexString.toString();
	}
	
	private String to16HexString(String str) {
		return str.substring(8,24);
	}
}
