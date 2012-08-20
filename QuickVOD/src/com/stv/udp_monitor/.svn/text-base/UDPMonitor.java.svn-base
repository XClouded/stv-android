package com.stv.udp_monitor;

import java.io.*;
import java.net.*;

import com.stv.vodprotocol.VodProtocol;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UDPMonitor {
	public UDPMonitor(DatagramSocket socket, Handler handler){//, MsgSource source){
		try{
			new UDPMonitorThread(socket, handler).start();//启动一个线程
		}catch(IOException e){
			System.out.println("发生异常:"+e);
		}
	}
}

class UDPMonitorThread extends Thread implements Runnable{
	protected DatagramSocket socket = null;//记录和本对象相关联的DatagramSocket对象
	protected boolean flag = true;//标志变量，是否继续操作
	protected Handler handler = null;
	
	public UDPMonitorThread(DatagramSocket socket, Handler handler) throws IOException{
		//无参数的构造函数
		this("UDPMonitorThread", socket, handler);//重载了带字符串参数的构造函数
	}
	
	public UDPMonitorThread(String name, DatagramSocket socket, Handler handler) throws IOException{
		super(name);//调用父类的构造函数
		this.socket = socket;
		//this.source = source;
		this.handler = handler;
	}
	
	public void run(){
		while(flag){
			System.out.println("run()");
			try{
				byte[] buf = new byte[VodProtocol.Data_Section_Length+1];//创建缓冲区
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);//接收数据报
				Log.d("收到数据", "接收到数据，正在解析...");
				byte[] msg_code = VodProtocol.UnPacked(buf);//解析数据报
				Log.d("发送回的消息码：", Integer.toString(msg_code[0]));
				//source.fireWorkspaceOpened(msg_code);//触发事件发生，产生开门动作
				Message message = new Message();
				message.what = msg_code[0];
				handler.sendMessage(message);
			}catch(IOException e){
				e.printStackTrace();//打印错误栈
				flag = false;//结束循环
				socket.close();
			}
		}
	}
}
