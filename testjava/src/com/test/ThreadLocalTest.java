package com.test;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalTest {

	public static void main(String[] args) {
		final ThreadLocal<Object> tvar = new ThreadLocal<Object>();
		
		
		for (int i = 0; i < 10; i++) {
			Thread t = new Thread() {
				@Override
				public void run() {
					tvar.set(System.nanoTime());

					System.out.println("============" + tvar.get());
				}
			};
			
			  tvar.set("a"); tvar.set("b"); 
			  //tvar.remove();
			  System.out.println("*****"+tvar.get());
			 
			t.start();
			
		}
		/*
		 * Map<Object,Object> map = new HashMap<Object,Object>(); map.put(new
		 * User(), new Integer(1)); map.put(new User(), new Integer(2));
		 * System.out.println(map.size());
		 */

		// ThreadLocalTest t1 = new ThreadLocalTest();
		// t1.testThis(t1);
		// testFor();

	}

	public ThreadLocalTest() {
		System.out.println(this.toString());
	}

	public void testThis(ThreadLocalTest t) {
		System.out.println(t.toString());
	}

	public static void testFor() {
		for (int i = 0; i < 5; i++) {
			System.out.println("======i======" + i);
		}
		int i = 0;
		while (i < 5) {

			System.out.println("============" + i);
			i++;
		}
	}

}
