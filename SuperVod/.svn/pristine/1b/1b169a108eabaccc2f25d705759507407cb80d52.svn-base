package com.stv.supervod.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import android.util.Log;


public class Connect2RCServer {
	private final String RcServerIp = "192.168.14.61";
	private final String RcServerPort = "5002";

	private final int timeout = 100; //100ms延时处理
	private volatile boolean flag = false;
	
	public Selector selector = null;
	public SocketChannel channel = null;
	public TCPClient client = null;
	
    private static Connect2RCServer c2r = new Connect2RCServer();
    
	private Connect2RCServer(){	
	}
	
	public synchronized static Connect2RCServer getInstance() {	
//		if (c2r == null) {
//			c2r = new Connect2RCServer();
//		}
		return c2r;
	}
	
	public void connet2rcserver() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				try {
					//定义异步客户端
					channel = SocketChannel.open();
					//定义一个记录套接字通道事件的对象
					selector = Selector.open();
					//将客户端设定为异步
					channel.configureBlocking (false);  
					//建立连接，此方法是异步
					long connetCastTime = System.currentTimeMillis();
					channel.connect(new InetSocketAddress(
							RcServerIp, Integer.parseInt(RcServerPort)));
					int date = (int) ((System.currentTimeMillis() - connetCastTime )/1000);
					Log.d("-----afdsafdsf------", String.valueOf(date));
					//在轮讯对象中注册此客户端的连接事件
					channel.register(selector, SelectionKey.OP_CONNECT);
					Log.d("-----channel.register------", String.valueOf(flag));
					flag = true;
					Log.d("-----赋值，flag:------", String.valueOf(flag));
				} catch (Exception e) {
					// TODO: handle exception
					flag=false;
					e.printStackTrace();
					try{
						throw new IOException("Connection Failure");
					}catch(Exception ee){
						ee.printStackTrace();
					}
				}	
			}
		}.start();
	}	
	
	public boolean getConnectionStatus(){
		return flag;
	}
	
	public void gcCache() {
		try {
			if (c2r.channel != null && c2r.channel.isOpen()) {
				c2r.channel.socket().close();
				c2r.channel.close();
				c2r.channel = null;
			}
			if (c2r.selector != null && c2r.selector.isOpen()) {
				c2r.selector.close();
				c2r.selector = null;
			}
			
			flag = false;
			VideoServiceImpl.getInstance().removeHeardBeat();//停止心跳
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//close socket
	public  void closeSocket(){
		if(channel!=null){
			if(channel.isConnected()){
				try {
					channel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
	}	
}
