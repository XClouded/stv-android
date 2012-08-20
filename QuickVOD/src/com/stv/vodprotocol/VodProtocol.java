package com.stv.vodprotocol;

/**   
 * @Filename:    VodProtocol.java  
 * @Package:     com.stv.vodprotocol  
 * @Description: 定义通信协议，主要有申请服务、发送按键以及退出系统
 */

public class VodProtocol {
	public static final int PHONE_NUMBER = 1;//协议：申请类型
	public static final int KEY_VALUE = 2;   //协议：发送按键
	public static final int HANG_UP = 3;     //协议：退出系统
	public static final int Data_Section_Length = 26;
	public static final int Smartcard_Length = 16;
	
	/**   
	 * Build TCP/UDP protocol
	 * 
	 * @param userID user 11 mobile phone number
	 * @param smartcard user 16 smart card number
	 * @param key_code key value
	 * @param type protocol type
	 * @return byte[] byte code   
	 */ 
	public static byte[] Packed(String userID, String smartCard, int key_code, int type){
		byte[] buffer;
		if(type == PHONE_NUMBER){
			buffer = new byte[Data_Section_Length + 1 + Smartcard_Length];
			buffer[5] = 0x00;//消息ID,0x00代表申请服务
			int j = Data_Section_Length-userID.getBytes().length;
			for (int i = 6; i < j; i++) {
				buffer[i] = 0x46;
			}
			System.arraycopy(userID.getBytes(), 0, buffer, j, userID.getBytes().length);
			System.arraycopy(smartCard.getBytes(), 0, buffer, Data_Section_Length, smartCard.getBytes().length);
			buffer[Data_Section_Length + Smartcard_Length] = 0x05;//手机设备类型：0x04代表iphone;0x05代表android
		}
		else if(type == KEY_VALUE){
			buffer = new byte[8];
			buffer[5] = 0x01;//消息ID,0x01代表发送按键
			buffer[6] = (byte)key_code;//键值码值
			buffer[7] = 0x01;//频度，预留，默认填充1
		}
		else if(type == HANG_UP){
			buffer = new byte[6];
			buffer[5] = 0x02;//消息ID,0x02代表挂机
		}
		else
			return null;
		
		buffer[0] = (byte)0xEF;//字节头，用于保证数据完整性
		buffer[1] = 0x02;//协议
		buffer[2] = 0x01;//版本
		buffer[3] = new Integer((buffer.length-4)&0xff).byteValue();//字节长度
		buffer[4] = 0x06;
		/*int l = buffer.length;
		while(l-- != 0){
			System.out.println("UDP打包的数据："+buffer[l]); 
		}*/
		return buffer;
	} 
	
	/**   
	 * Unpacked, obtain data from buffer
	 * 
	 * @param buf receive buffer
	 * @return byte[] message code and playing speed
	 */ 
	public static byte[] UnPacked(byte[] buf){
		int length = buf.length;
		System.out.println("数据包长度"+length);
		byte[] return_code = new byte[2];
		return_code[0] = 0;
		return_code[1] = 0;
		
		//校验协议类型
		if(buf[1] != 2)
		{
			System.out.println("UDPERROR:协议类型错误"+buf[1]);
			return return_code;	
		}
		//校验版本号
		if(buf[2] != 1)
		{
			System.out.println("UDPERROR：版本号错误"+buf[2]);
			return return_code;	
		}
        
		if (buf[length-3] == 0x02 && buf[length-4] == 0x06) {
			return_code[0] = 0x70;
		} else {
			return_code[0] = buf[length-2];
		}
		
		return_code[1] = buf[length-1];
		return return_code;
	}
}
