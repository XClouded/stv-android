package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Test3 {
	private static int num = 1000;

	public static synchronized int outTickts() {
		num--;
		return num;
	}

	private static int t1 = 0;
	private static int t2 = 0;
	private static int t3 = 0;
	private static int t4 = 0;

	public static void main(String[] args) throws InterruptedException {
		/*new Thread() {
			@Override
			public void run() {
				int vn = outTickts();
				while (vn > -1) {
					System.out.println("线程一当前售出票数" + vn);
					t1++;
					vn = outTickts();
				}

			}
		}.start();

		new Thread() {
			@Override
			public void run() {
				int vn = outTickts();
				while (vn > -1) {
					System.out.println("线程二当前售出票数" + vn);
					vn = outTickts();
					t2++;
				}

			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				int vn = outTickts();
				while (vn > -1) {
					System.out.println("线程三当前售出票数" + vn);
					vn = outTickts();
					t3++;
				}

			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				int vn = outTickts();
				while (vn > -1) {
					 System.out.println("线程四当前售出票数"+vn);
					vn = outTickts();
					t4++;
				}

			}
		}.start();

		Thread.sleep(3000);
		System.out.println("====================================线程1共买票" + t1 + "张");
		System.out.println("====================================线程2共买票" + t2 + "张");
		System.out.println("====================================线程3共买票" + t3 + "张");
		System.out.println("====================================线程4共买票" + t4 + "张");*/
		//testValue();
		//testMap();
		/*try {
			testException();
		} catch (ErrorCodeException e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}*/
		//LinkedBlockingQueue<E>
		//LinkedBlockingDeque<E>
		//CopyOnWriteArrayList<E>
		testreplace();
	}

	
	public static void testreplace(){
		//sj.append("{");
		//sj.append("\"username\"").append(":").append("\"").append(username).append("\",");
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"error\"").append(":").append("\"").append(20101).append("\"");
		sb.append("}");
		System.out.println(sb.toString());
		String[] strs = sb.toString().split(":");
		
		
		if(strs.length>1&&strs[1].length()>0){
			String code = strs[1].substring(1, strs[1].length()-2);
			System.out.println(code);
		}
		
		
		//{“error”:“20101”}
	}
	
	public static void testException() throws ErrorCodeException{
		throw new ErrorCodeException("哈哈哈");
	}
	
	public static void testMap(){
		Map<String, String> map = new HashMap<String,String>();
		map.put("name1", "name1");
		map.put("name2", "name2");
		map.put("name3", "name3");
		Iterator it = map.keySet().iterator();
		String str= "";
		while (it.hasNext()){
			String key = (String) it.next();
			String kv = "&"+key+"="+map.get(key);
			str+=kv;
		}
		str ="?"+str.substring(1);
		System.out.println(str);
	}
	
	public static void testSellTickts() {

	}

	public static void testValue() {
		User u = new User();
		String str = "test1";
		testAddres(u, str);

		System.out.println(u.getName());
		System.out.println(str);
	}

	public static void testAddres(User user, String tempStr) {
		//user = new User();
		user.setName("name");
		tempStr = "test2";
	}

	public static void testMulThreads() throws InterruptedException {
		long t1 = System.currentTimeMillis();
		final List<Map> list = new ArrayList<Map>();
		new Thread() {
			public void run() {
				int i = 0;
				// while (stop == false) {
				while (list.size() < 10) {
					i++;
					System.out.println(i);
				}
			};
		}.start();
		// backThread.start();
		TimeUnit.MILLISECONDS.sleep(50);
		long t2 = System.currentTimeMillis();
		System.out.println("==================" + (t2 - t1));
		// stop = true;
		// u.setUserName(null);
		for (int i = 0; i < 10; i++) {
			list.add(new HashMap());
		}
	}

}
