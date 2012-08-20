package com.test;

import java.math.BigInteger;


public class Test7 {

	public static void main(String[] args) {
//		String str=null;
//		str+="world";
//		System.out.println(str);
//		long l=256l*256*256*256*256*256*128*128;
//		System.out.println(l);
//		long ll=1000000000000000000l;
//		System.out.println(ll);
		//System.out.println(Integer.toBinaryString(65534));
		BigInteger a1 = new BigInteger("1000000000000000000011111");
		BigInteger a2 = new BigInteger("1111111111111111111111111");
		BigInteger a3 = a1.add(a2);
		//System.out.println(Float.toHexString(30.5f));
		System.out.println(a3);
	}

}
