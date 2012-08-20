package com.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class Test5 {
public static void main(String[] args){
	float a=9.6f;
	String[] aa = (a+"").split("\\.");
	for(int i=0;i<aa.length;i++){
		System.out.println(aa[i]);
	}
	String[] b = "10.0".split(".");
	for(int i=0;i<b.length;i++){
		System.out.println(b[i]);
	}

}

}
