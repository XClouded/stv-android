package com.test;


public class CyclicBarrierDemo {

	public static void main(String[] args) {
		int[] vv = new int[] { 5, 4, 1, 2, 6, 3 };
	/*	for (int i = 0; i < vv.length - 1; i++) {
			for (int j = i + 1; j < vv.length; j++) {
				if (vv[i] < vv[j]) {
					int tmp = vv[i];
					vv[i] = vv[j];
					vv[j] = tmp;
				}
			}

		}*/
		
		for(int i=0;i<vv.length;i++){
			System.out.println(vv[i]);
		}
		

	}

}
