/**
 * 
 */
package com.stv.supervod.service;

import android.os.Bundle;

import com.stv.supervod.service.HttpDownloadImpl.KeyEnum;
import com.stv.supervod.utils.ConvertUtils;
import com.stv.supervod.utils.VideoConstant;
import com.stv.supervod.utils.VodModel;

/**
 * @author Administrator
 * @Description: 定义通信协议，主要有申请服务、发送按键以及退出系统
 */
public class TCP_Protocol {
		
	/**
	 * 
	 * @param model
	 * @param type
	 * @return byte[]
	 */
	public static byte[] packed4play(VodModel model,int type){
		byte[] buffer;
		
		if(type == VideoConstant.APPLY_SERVICE){
			if(model.getIsPassword()==0){//0:表示密码登陆 1：表示电话号码登陆
				buffer=new byte[40];
				byte[] md5 = ConvertUtils.HexString2Bytes(model.getPassword());
				System.arraycopy(md5, 0, buffer, 24, md5.length); //密码
			}else{
				buffer=new byte[35];
				byte[] pas=model.getPassword().getBytes();
				System.arraycopy(model.getPassword().getBytes(), 0, buffer, 24, model.getPassword().length());//电话号码
			}
			
			buffer[4]=0x00; //点播请求			
			System.arraycopy(model.getSmartCardId().getBytes(), 0, buffer, 6, model.getSmartCardId().length());
			buffer[22]=0x05;//android系统
			buffer[23]=model.getIsPassword();//0表示密码，1：非密码
		}
		else if(type == VideoConstant.MTV_BATCH_PLAY){
			buffer=new byte[14];
			buffer[4]=0x01;
			buffer[6]=model.getAction();
			buffer[7]=model.getPlayMode();
			buffer[8]=new Integer(model.getPlayCount()).byteValue();
			buffer[9]=model.getChannelId();
			int offering=new Integer(model.getOfferingId());			
			buffer[10]=(byte)((offering>>24)&0xff);
			buffer[11]=(byte)(offering>>16&0xff);
			buffer[12]=(byte)(offering>>8&0xff);
			buffer[13]=(byte)(offering&0xff);						
			
		}
		else if(type == VideoConstant.VIDEO_BATCH_PLAY){
			buffer=new byte[13];
			buffer[4]=0x02;
			buffer[6]=model.getAction();
			byte channelId=model.getChannelId();
			buffer[7]= (byte)channelId;
			int offering=new Integer(model.getOfferingId());
			buffer[8]=(byte)((offering>>24)&0xff);
			buffer[9]=(byte)(offering>>16&0xff);
			buffer[10]=(byte)(offering>>8&0xff);
			buffer[11]=(byte)(offering&0xff);	
			buffer[12]=model.getIsBookmark();
		}
		else if(type == VideoConstant.SINGLE_PLAY){
			buffer=new byte[11];
			buffer[4]=0x03;
			int offering=new Integer(model.getOfferingId());
			buffer[6]=(byte)((offering>>24)&0xff);
			buffer[7]=(byte)(offering>>16&0xff);
			buffer[8]=(byte)(offering>>8&0xff);
			buffer[9]=(byte)(offering&0xff);	
			buffer[10]=model.getIsBookmark();
		}
		else if(type == VideoConstant.PLAY_CONTROL){
			buffer=new byte[9];
			buffer[4]=0x04;
			buffer[6]=model.getOperation();//播放控制：快进、快退、播放、暂停
			buffer[7]=(byte)(model.getNpt_begin()>>8);
			buffer[8]=(byte)model.getNpt_begin();
		}
		else if(type == VideoConstant.HEART_BEAT){
			buffer=new byte[6];
			buffer[4]=0x05;
		}
		else if(type == VideoConstant.STOP_SERVICE){
			buffer=new byte[6];
			buffer[4]=0x06;
		}
		else if(type == VideoConstant.QUERY_STATUS){
			buffer=new byte[6];
			buffer[4]=0x08;			
		}
		else if(type == VideoConstant.NETWORK_CHECK){
			buffer = new byte[6];
			buffer[4]=0x09;
		}
		else{
			return null;
		}
		
		buffer[0]=(byte)0xFE;//字节头，用于保证数据完整性
		buffer[1]=0x02; //协议
		buffer[2]=0x01; //版本
		buffer[3]=0x05; 
		buffer[5]=new Integer((buffer.length-6)).byteValue();//字节长度
		int l = buffer.length;
		while(l-- != 0){
			System.out.println("TCP打包的数据："+buffer[l]); 
		}
		return buffer;
		
	}
	
	public static Bundle UnPacked(byte[] buf){	
		int l = buf.length;
		while(l-- != 0){
			System.out.println("发送回的消息码："+buf[l]); 
		}
		System.out.println("================分界线==========================================");
		Bundle bundle =new Bundle();
		
		//校验协议类型
		if(buf[1] != 2)
		{
			System.out.println("TCP protocal ERROR:协议类型错误"+buf[1]);
			bundle.putByte("type", (byte)0x10);
			return bundle;	
		}
		//校验版本号
		if(buf[2] != 1)
		{   bundle.putByte("type", (byte)0x11);
			System.out.println("tcp protocal ERROR：版本号错误"+buf[2]);
			return bundle;	
		}
		if(buf[3]==5&&buf[4]==0){//申请服务	
			bundle.putByte("type", buf[4]);
			bundle.putByte("operationCode", buf[6]);
			bundle.putByte("scale", buf[7]);
			bundle.putByte("channelId", buf[8]);
			bundle.putByte(KeyEnum.play_mode.toString(), buf[9]);
			bundle.putByte(KeyEnum.play_count.toString(), buf[10]);
			bundle.putByte(KeyEnum.playTimes.toString(), buf[11]);
			int offeringId=0;
			offeringId=(buf[12]&0xff)<<24+(buf[13]&0xff)<<16+(buf[14]&0xff)<<8+(buf[15]&0xff);
			bundle.putInt("offeringId", offeringId);
			
			bundle.putShort("nptBegin", (short)(buf[16]<<8+buf[17]));
			bundle.putShort("nptTotal", (short)(((buf[18]&0xff)<<8)+(buf[19]&0xff)));	
			bundle.putByte("count", buf[20]);
		}
		else if((buf[3]==5&&buf[4]==1)||(buf[3]==5&&buf[4]==2)||(buf[3]==5&&buf[4]==3)){//播放
			bundle.putByte("type",buf[4]);
			bundle.putByte("operationCode", buf[6]);
			bundle.putShort("nptBegin", (short)(((buf[7]&0xff)<<8)+(buf[8]&0xff)));
			bundle.putShort("nptTotal", (short)(((buf[9]&0xff)<<8)+(buf[10]&0xff)));	
		}
		
		else if(buf[3]==5&&buf[4]==4){//点播控制返回
			bundle.putByte("type",buf[4]);
			bundle.putByte("operationCode", buf[6]);
			bundle.putByte("scale", buf[7]);
			bundle.putShort("nptBegin", (short)(((buf[8]&0xff)<<8)+(buf[9]&0xff)));
									
		}
		else if((buf[3]==5&&buf[4]==5)||(buf[3]==5&&buf[4]==6)){//心跳\停止服务
			bundle.putByte("type",buf[4]);
			bundle.putByte("operationCode", buf[6]);
			
		}
		else if(buf[3]==5&&buf[4]==7){//系统消息
			bundle.putByte("type",buf[4]);
			bundle.putByte(KeyEnum.operationCode.toString(), buf[6]);
			bundle.putByte(KeyEnum.channelId.toString(), buf[7]);
			bundle.putByte(KeyEnum.play_mode.toString(), buf[8]);
			bundle.putByte(KeyEnum.play_count.toString(), buf[9]);
			bundle.putByte(KeyEnum.playTimes.toString(), buf[10]);
			bundle.putInt(KeyEnum.offeringId.toString(), (int)(((buf[11]&0xff)<<24)+((buf[12]&0xff)<<16)+((buf[13]&0xff)<<8)+(buf[14]&0xff)));
			byte count=(byte)(buf[15]&0xff);
			bundle.putShort("nptTotal", (short)(((buf[14+count]&0xff)<<8)+(buf[15+count]&0xff)));
		}
		else if(buf[3]==5&&buf[4]==9){
			bundle.putByte("type",buf[4]);
			
		}
		else if(buf[3]==5&&buf[4]==8){
			
			bundle.putByte("type",buf[4]);
			bundle.putByte(KeyEnum.operationCode.toString(), buf[6]);
			bundle.putByte(KeyEnum.scale.toString(), buf[7]);
			bundle.putByte(KeyEnum.channelId.toString(), buf[8]);
			bundle.putByte(KeyEnum.play_mode.toString(), buf[9]);
			bundle.putByte(KeyEnum.play_count.toString(), buf[10]);
			bundle.putByte(KeyEnum.playTimes.toString(), buf[11]);
			bundle.putString(KeyEnum.offeringId.toString(), new Integer((((buf[12]&0xff)<<24)+((buf[13]&0xff)<<16)+((buf[14]&0xff)<<8)+(buf[15]&0xff))).toString());			
			bundle.putShort(KeyEnum.startTime.toString(), (short)(((buf[16]&0xff)<<8)+(buf[17]&0xff)));
			bundle.putShort(KeyEnum.runTime.toString(), (short)(((buf[18]&0xff)<<8)+(buf[19]&0xff)));
			byte count=(byte)(buf[20]&0xff);
			bundle.putByte("count",count );
			String s="";
			for(int i=21;i<21+count;i++){
				s+=(char)buf[i];
			}
			bundle.putString(KeyEnum.assetId.toString(), s);
		}
		else
			bundle.putByte("type", (byte)0x12);
		return bundle;
		
	}

	

}
