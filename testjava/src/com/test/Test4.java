package com.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class Test4 {
	public static void main(String[] args) throws InterruptedException {
		/*
		 * LinkedBlockingDeque<String> list = new LinkedBlockingDeque<String>();
		 * for(int i=0;i<10;i++){ //list.add(i+""); //list.addFirst(i+"");
		 * list.addLast(i+""); }
		 * System.out.println("==============="+list.size());
		 * while(!list.isEmpty()){ //list.takeLast()
		 * System.out.println(list.takeLast()+""); }
		 */
		//testList();
		//testVector();
		CountDownLatch latch = new CountDownLatch(10);
		for(int i=0;i<20;i++){
			latch.countDown();
			System.out.println(latch.getCount());
		}
	}
	
	
	private static void testList() {
		List<String> tests=new ArrayList<String>();
		tests.add("a");
		tests.add("b");
		tests.add("temp");
		tests.add("c");
		for(String test:tests){
			if(test.equals("b")){
				tests.remove("b");
			}
			System.out.println(test);
			
		}
	/*	for(int i=0;i<tests.size();i++){
			if(tests.get(i).equals("b")){
				tests.remove("temp");
			}
			System.out.println(tests.get(i));
			
		}*/
		
	}
	
	
	private static void testVector() {
		Vector<String> tests=new Vector<String>();
		tests.add("a");
		tests.add("b");
		tests.add("temp");
		tests.add("c");
		for(String test:tests){
			if(test.equals("b")){
				tests.remove("b");
			}
		}
		
	}

}
