package com.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Exchanger;

public class ExchangerDemo {
	private static final Exchanger ex = new Exchanger();

	class DataProducer implements Runnable {
		private List list = new ArrayList();

		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println("生产了一个数据，耗时1秒");
				list.add(new Date());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			try {

				// 将数据准备用于交换，并返回消费者的数据
				list = (List) ex.exchange(list);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				System.out.println("Producer " + iterator.next());
			}
		}
	}

	class DataConsumer implements Runnable {
		private List list = new ArrayList();

		public void run() {
			for (int i = 0; i < 5; i++) {
				// 消费者产生数据，后面交换的时候给生产者
				list.add("这是一个收条。");
			}
			try {
				// 进行交换数据，返回生产者的数据
				list = (List) ex.exchange(list);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Date d = (Date) iterator.next();
				System.out.println("consumer:" + d);
			}
		}
	}

	public static void main(String args[]) {
		ExchangerDemo et = new ExchangerDemo();
		new Thread(et.new DataProducer()).start();
		new Thread(et.new DataConsumer()).start();
	}

}
