package com.stv.udp_client;

import java.io.IOException;  
import java.io.Serializable;
import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
import java.net.InetAddress;  
import java.util.HashMap;
import java.util.Map;

import com.stv.udp_monitor.UDPMonitor;
import com.stv.vodprotocol.VodProtocol;

import android.os.Handler;
import android.util.Log;

/** 
 * @author Alex.LiuJN date: 2011-07-25 UDP客户端类 
 * @param 
 */ 
public class UDPClient implements Serializable {
	
	private static final long serialVersionUID = -6919461967497580385L;
	
	private int sERVER_PORT = 5000;  
	private InetAddress sERVER_IP = null;
	private String phoneNum = "";
	private String smartCard = "";
	
	public static final int PHONE_NUMBER = 1;
	public static final int KEY_VALUE = 2;
	public static final int HANG_UP = 3;
	
	protected DatagramSocket socket = null;
	private Map<String, String> map = new HashMap<String, String>();
  
    /** 
     * @param null 
     */  
    public UDPClient(DatagramSocket socket, Handler handler, InetAddress ipaddress, int port, String phone, String smartcard) { 
        super();
        this.socket = socket;
        this.sERVER_PORT = port;
        this.phoneNum = phone;
        this.smartCard = smartcard;
        this.sERVER_IP = ipaddress;
		Log.d("---UDPClient---", "---UDPClient---");
		Log.d("手机电话号码：", phoneNum);
		Log.d("IP地址：", sERVER_IP.toString());
		Log.d("端口号：", Integer.toString(sERVER_PORT));
		//将发送数据的socket传给UDP监听，并启动一个监听线程
		map.put("0", "177");
		map.put("1", "49");
		map.put("2", "38");
		map.put("3", "51");
		map.put("4", "37");
		map.put("5", "13");
		map.put("6", "39");
		map.put("7", "55");
		map.put("8", "40");
		map.put("9", "57");
		map.put("*", "27");
		map.put("#", "35");
		new UDPMonitor(socket, handler);
    }  
	
	/**   
	 * 向Phone Server申请服务，通过UDP协议的方式   
	 */ 
	public void Call(){
		Log.d("UDPClient-Call", "申请服务");
		byte[] buf = VodProtocol.Packed(this.phoneNum, this.smartCard, (byte)0, PHONE_NUMBER);
		try{
			//创建数据报
			DatagramPacket packet = new DatagramPacket(buf, buf.length, sERVER_IP, sERVER_PORT);//创建DatagramPacket对象
			//发送数据报
			socket.send(packet);
			Log.d("UDPClient-Call", "发送数据");
		}catch(IOException e){
			System.err.println("Socket 发生异常："+e);
		}
	}
	
	/**   
	 * 向Phone Server发送键值，通过UDP协议的方式  
	 * @param key String 
	 */
	public void Digit(String key){
		Log.d("UDPClient-Digit", "发送按键");
		try{
			byte[] buf = VodProtocol.Packed(this.phoneNum, this.smartCard, Integer.parseInt(map.get(key)), KEY_VALUE);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, sERVER_IP, sERVER_PORT);
			//发送数据报
			socket.send(packet);
		}catch(IOException e){
			System.err.println("Socket 发生异常："+e);
		}
	}
	
	/**   
	 * 智能机退出应用程序，发送停止服务消息
	 */
	public void Hangup(){
		Log.d("UDPClient-Digit", "挂机");
		try{
			byte[] buf = VodProtocol.Packed(this.phoneNum, this.smartCard, (byte)0, HANG_UP);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, sERVER_IP, sERVER_PORT);
			//发送数据报
			socket.send(packet);
		}catch(IOException e){
			System.err.println("Socket 发生异常："+e);
		}
	}
}
