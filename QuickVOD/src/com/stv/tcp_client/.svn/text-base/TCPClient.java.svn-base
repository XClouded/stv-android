package com.stv.tcp_client;

import java.io.IOException;
import java.io.OutputStream;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;

import com.stv.quickvod.frontpage;
import com.stv.tcp_monitor.TCPMonitor;
import com.stv.vodprotocol.VodProtocol;

/**   
 * @Filename:    TCPClient.java  
 * @Package:     com.stv.tcp_client  
 * @Description: TCPClient是TCP连接的客户端程序，主要负责向TCP连接的服务器申请服务、传递键值、发送心跳等
 *               以及处理Socket发出的IO异常
 */

public class TCPClient {
	private String phoneNum = "";
	private String smartCard = "";
	
	private SocketChannel channel = frontpage.channel;
	private Selector selector = frontpage.selector;
	
	//定义用来存储发送数据的byte缓冲区
	private ByteBuffer sendbuffer;
	
	private Map<String, String> map = new HashMap<String, String>();
	
	OutputStream outputStream = null; 
	  
	/** 
	 * TCPClient Constructed Function
	 * 
	 * @param handler   message handle
	 * @param phone     11 mobile phone number
	 * @param smartcard 16 smart card number 
	 */  
	public TCPClient(Handler handler, String phone, String smartcard) { 
		super();
		this.phoneNum = phone;
		this.smartCard = smartcard;
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
		//将发送数据的socket传给TCP监听，并启动一个监听线程
		new TCPMonitor(channel, selector, handler);
    }  
	
	/** 
	 * Through the TCP protocol, Apply service to RCServer
	 * 
	 * @param null
	 */  
	public void Call(){
		byte[] buf = VodProtocol.Packed(this.phoneNum, this.smartCard, 
											(byte)0, VodProtocol.PHONE_NUMBER);
		try{  
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(43);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			channel.write(sendbuffer);
		} catch(IOException e){
			System.err.println("Socket IOException："+e);
			try {
				if (!channel.isConnected()) {
					channel.close();
				}
			} catch (IOException e1) {
				// TODO: handle exception
				System.err.println("Socket IOException："+e1);
			}
		}
	}
	
	/** 
	 * Through the TCP protocol, send the value of key to RCServer
	 * 
	 * @param key digit value
	 */ 
	public void Digit(String key){
		byte[] buf = VodProtocol.Packed(this.phoneNum, this.smartCard, 
						Integer.parseInt(map.get(key)), VodProtocol.KEY_VALUE);
		try{   
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(8);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			channel.write(sendbuffer);
		}catch(IOException e){
			System.err.println("Socket IOException："+e);
		}
	}
	
	/** 
	 * Through the TCP protocol, send the value of heartbeat to RCServer
	 * 
	 * @param null
	 */ 
	public void Heartbeat(){
		byte[] buf = VodProtocol.Packed(this.phoneNum, this.smartCard, 
											1, VodProtocol.KEY_VALUE);
		try{
			//将数据put进缓冲区
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			channel.write(sendbuffer);
		}catch(IOException e){
			System.err.println("Socket IOException："+e);
		}
	}
	
	/** 
	 * Through the TCP protocol, send exiting message to RCServer while phone 
	 * exiting the application 
	 * 
	 * @param null
	 */ 
	public void Quit() {
		byte[] buf = VodProtocol.Packed(this.phoneNum, this.smartCard, 
											(byte)0, VodProtocol.HANG_UP);
		try{   
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(6);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			channel.write(sendbuffer);
		}catch(IOException e){
			System.err.println("Socket IOException："+e);
		}
	}
}
