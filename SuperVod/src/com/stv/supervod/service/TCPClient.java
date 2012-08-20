package com.stv.supervod.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.stv.supervod.activity.Play;
import com.stv.supervod.exception.ErrorCodeException;
import com.stv.supervod.utils.VideoConstant;
import com.stv.supervod.utils.VodModel;

/**   
 * @Filename:    TCPClient.java  
 * @Package:     com.stv.tcp_client  
 * @Description: TCPClient是TCP连接的客户端程序，主要负责向TCP连接的服务器申请服务、传递键值、发送心跳等
 *               以及处理Socket发出的IO异常
 */

public class TCPClient {
		
	private SocketChannel channel;
	
	//定义用来存储发送数据的byte缓冲区
	private ByteBuffer sendbuffer;	
	OutputStream outputStream = null; 
	  
	/** 
	 * TCPClient Constructed Function
	 * 
	 * @param handler   message handle
	 * @param phone     11 mobile phone number
	 * @param smartcard 16 smart card number 
	 */  
	public TCPClient(Handler handler) { 
		super();
		//将发送数据的socket传给TCP监听，并启动一个监听线程
		new TCPMonitor(handler);
    }  
	
	/** 
	 * Through the TCP protocol, Apply service to RCServer
	 * 申请服务
	 * @param vodModel
	 */  
	public void applyServcie(VodModel vodModel){
		byte[] buf = TCP_Protocol.packed4play(vodModel,VideoConstant.APPLY_SERVICE ); 
		try{
			long time = System.currentTimeMillis();
			Connect2RCServer.getInstance().selector.select(0);
			time = System.currentTimeMillis() - time;
			Log.d("---------selector time----------", time+"ms");
			Iterator<SelectionKey> keyIterator = Connect2RCServer.getInstance().selector.selectedKeys().iterator();
			while(keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				keyIterator.remove();
				//连接事件
				if(key.isConnectable()) {
					SocketChannel socketChannel = (SocketChannel) key.channel();
					if(socketChannel.isConnectionPending()) {
						socketChannel.finishConnect();
					}
					//将数据put进缓冲区
					sendbuffer = ByteBuffer.allocate(43);
					sendbuffer.put(buf);
					//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
					sendbuffer.flip();
					this.channel = socketChannel;
					//向服务器发送数据
					channel.write(sendbuffer);
					channel.register(Connect2RCServer.getInstance().selector, SelectionKey.OP_READ);
					
				}
			}
		} catch (IOException e) {
			System.err.println("Socket IOException："+e);
			try {
				if (channel != null && !channel.isConnected()) {
					channel.close();
				}
			} catch (IOException e1) {
				// TODO: handle exception
				System.err.println("Socket IOException："+e1);
			}
		}
	}
	
	/** 
	 * Through the TCP protocol, send play request to RCServer
	 * 单片播放
	 * @param vodModel
	 */  
	public void play(VodModel vodModel){
		byte[] buf = TCP_Protocol.packed4play(vodModel,VideoConstant.SINGLE_PLAY );
		try{  
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(11);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
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
	 * Through the TCP protocol, send play request to RCServer
	 * MTV批量播放
	 * @param vodModel
	 */  
	public void mtvBatchPlay(VodModel vodModel){
		byte[] buf = TCP_Protocol.packed4play(vodModel,VideoConstant.MTV_BATCH_PLAY );
		try{  
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(14);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
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
	 * Through the TCP protocol, send play request to RCServer
	 * 视频批量播放
	 * @param vodModel
	 */  
	public void videoBatchPlay(VodModel vodModel){
		byte[] buf = TCP_Protocol.packed4play(vodModel,VideoConstant.VIDEO_BATCH_PLAY );
		try{  
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(13);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
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
	 * 播放控制
	 * @param key digit value
	 */ 
	public void playControl(VodModel vodModel){		
		byte[] buf = TCP_Protocol.packed4play(vodModel, VideoConstant.PLAY_CONTROL);
		try{   
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(9);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
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
		VodModel model=new VodModel();
		byte[] buf = TCP_Protocol.packed4play(model, VideoConstant.HEART_BEAT);
		try{
			sendbuffer = ByteBuffer.allocate(6);
			//将数据put进缓冲区
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
		}catch(IOException e){
			System.err.println("Socket IOException："+e);
		}
	}
	
	/** 
	 * Through the TCP protocol, send exiting message to RCServer while phone 
	 * exiting the application 
	 * 停止服务
	 * @param null
	 */ 
	public void Quit() {
		VodModel model=new VodModel();
		byte[] buf = TCP_Protocol.packed4play(model, VideoConstant.STOP_SERVICE);
		try{   
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(6);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
		}catch(IOException e){
			System.err.println("Socket IOException："+e);
		}
	}
	/**
	 * 查询状态
	 */
	public void queryStatus(VodModel model) {		
		byte[] buf = TCP_Protocol.packed4play(model, VideoConstant.QUERY_STATUS);
		try{   
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(6);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
		}catch(IOException e){
			System.err.println("Socket IOException："+e);
		}
	}
	/**
	 * 网络监测
	 */
	public void checkNetwork() {
		VodModel model=new VodModel();
		byte[] buf = TCP_Protocol.packed4play(model, VideoConstant.NETWORK_CHECK);
		try{   
			//将数据put进缓冲区
			sendbuffer = ByteBuffer.allocate(6);
			sendbuffer.put(buf);
			//将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
			sendbuffer.flip();
			//向服务器发送数据
			if (channel != null) {
				channel.write(sendbuffer);
			}
		}catch(IOException e){
			System.err.println("Socket IOException："+e);
		}
	}
	
}
