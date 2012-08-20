package com.stv.tcp_monitor;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.stv.vodprotocol.VodProtocol;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**   
 * @Filename:    TCPMonitor.java  
 * @Package:     com.stv.tcp_monitor 
 * @Description: TCPMonitor是TCP连接的监听程序，主要负责监听从RCServer返回的消息响应
 */

public class TCPMonitor {
	private static Thread tcpMonitorThread = null;
	
	/** 
     * TCPMonitor Constructed Function
     * 
     * @param socket TCP　connection channel
     * @param　handler message handler
     */ 
	public TCPMonitor(SocketChannel channel, Selector selector, Handler handler){
		try{
			tcpMonitorThread = new TCPMonitorThread(channel, selector, handler);//启动一个线程
			tcpMonitorThread.start();
		}catch(IOException e){
			System.out.println("发生异常:"+e);
		}
	}
	
	/** 
     * Stop TCPMonitor Thread Function
     * 
     * @param null
     */
	public static void stpoTcpMonitorThread() {
		if (tcpMonitorThread != null) {
			tcpMonitorThread.stop();
		}
	}
	
	/** 
     * Start TCPMonitor Thread Function
     * 
     * @param socket TCP　connection channel
     * @param　handler message handler
     */
	public static void startTcpMonitorThread(SocketChannel channel, Selector selector, Handler handler) {
		try{
			tcpMonitorThread = new TCPMonitorThread(channel, selector, handler);//启动一个线程
			tcpMonitorThread.start();
		}catch(IOException e){
			System.out.println("*****启动TCP监听线程发生异常:******"+e);
		}
	}
}

class TCPMonitorThread extends Thread implements Runnable{
	 
	private Handler handler = null;
	private SocketChannel channel;
	private Selector selector;
	
	//定义用于接收服务器返回的数据的缓冲区
	ByteBuffer readBuffer= ByteBuffer.allocate(8);
	
	private boolean flag = true;//标志变量，是否继续操作
	
	public TCPMonitorThread(SocketChannel channel, Selector selector, Handler handler) throws IOException{
		//重载了带字符串参数的构造函数
		this("TCPMonitorThread", channel, selector, handler);
	}
	
	public TCPMonitorThread(String name, SocketChannel channel, Selector selector, Handler handler) throws IOException{
		//调用父类的构造函数
		super(name);
		this.handler = handler;
	    this.channel = channel;
	    this.selector = selector;
	}
	
	@Override
	public void run(){
		while(flag){
            //利用循环来读取服务器发回的数据
			Message message = new Message();
			try{
				//如果客户端连接没有打开就退出循环
                if(!channel.isOpen()) break;
                //此方法为查询是否有事件发生如果没有就阻塞,有的话返回事件数量
                int shijian=selector.select(0);
                //如果没有事件返回循环
                if(shijian==0)
                {
                    continue;
                }
                //定义一个临时的客户端socket对象
                SocketChannel sc;
                //遍例所有的事件
                for (SelectionKey key : selector.selectedKeys())
                {
                    //删除本次事件
                    selector.selectedKeys().remove(key);
                    //如果本事件的类型为read时,表示服务器向本客户端发送了数据
                    if (key.isReadable())
                    {
                        //将临时客户端对象实例为本事件的socket对象
                        sc=(SocketChannel) key.channel();
                        //定义一个用于存储所有服务器发送过来的数据
                        ByteArrayOutputStream bos= new ByteArrayOutputStream();
                        //将缓冲区清空以备下次读取
                        readBuffer.clear();
                        //此循环从本事件的客户端对象读取服务器发送来的数据到缓冲区中
                        while(sc.read(readBuffer) > 0)
                        {
                            //将本次读取的数据存到byte流中
                            bos.write(readBuffer.array());   
                            //将缓冲区清空以备下次读取
                            readBuffer.clear();
                        }
                        //如果byte流中存有数据
                        if(bos.size()>0)
                        {
                            //建立一个普通字节数组存取缓冲区的数据
                            byte[] msg_code = VodProtocol.UnPacked(bos.toByteArray());
                            Log.d("发送回的消息码：", Integer.toString(msg_code[0]));
        					message.arg1 = msg_code[0];
        					message.arg2 = msg_code[1];
        					handler.sendMessage(message);
                        }
                    }
                }
			} catch (SocketException e) {
				// TODO: handle exception
				e.printStackTrace();
				//网络连接失败消息码
				message.arg1 = 1001;
				flag = false;
				handler.sendMessage(message);
			} catch(IOException e){
				//打印错误栈
				e.printStackTrace();
				//网络连接失败消息码
				message.arg1 = 1001;
				flag = false;
				handler.sendMessage(message);
			} 
		}
	}
}

