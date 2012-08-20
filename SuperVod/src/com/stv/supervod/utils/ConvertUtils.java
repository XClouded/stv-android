package com.stv.supervod.utils;

/**
 * Description: 进制之间互相转换  16进制字符串转byte[]
 * Copyright (c) 永新视博
 * All Rights Reserved.
 * @version 1.0  2012-04-13 ljn_alex created
 */
public class ConvertUtils {
	
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[16];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < 16; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	} 
	 
	 public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
}
