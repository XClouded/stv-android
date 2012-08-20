package com.test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Test2 {
	private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
	private static int count = 0;

	private static void addmap(String key, String value) {
		synchronized (map) {
			count++;
			map.put(key, key);
		}
	}

	private static  int i = 0;

	private static int getIndex() {
		return i++;
	}

	private static int t1 = 0;
	private static int t2 = 0;

	public static void main(String[] args) {
		new Thread() {
			@Override
			public void run() {
				int t = 0;
				while (i < 500) {
					i++;
					t1++;
					addmap(i + "", i + "");
				}
			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				int t = 0;
				while (i < 500) {
					i++;
					t2++;
					addmap(i + "", i + "");
				}
			}
		}.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("=======t1==t2==" + t1 + "==" + t2 + "====== map.size()======" + map.size());

		System.out.println("=====count============" + count);
	}

}
