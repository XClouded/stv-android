package com.test;

public class DemoThread implements Runnable {
	public DemoThread() {
		TestThread testthread1 = new TestThread(this, "1");
		testthread1.start();
		TestThread testthread2 = new TestThread(this, "2");
		testthread2.start();
		
	}

	public static void main(String[] args) {
		DemoThread demoThread1 = new DemoThread();
	}

	public void run() {
		TestThread t = (TestThread) Thread.currentThread();
		try {
			if (t.getName().equalsIgnoreCase("2")) {
				
				synchronized (this) {
					System.out.println("@time in thread" + t.getName() + "=wait" + t.increaseTime());
					wait();
					System.out.println("@time in thread" + t.getName() + "=wait" + t.increaseTime());
				}
			}
			while (true) {
				System.out.println("@time in thread" + t.getName() + "=" + t.increaseTime());
				if (t.getTime() % 10 == 0) {
					synchronized (this) {
						System.out.println("****************************************");
						notify();
						if (t.getTime() == 10) {
							break;
						}
						wait();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class TestThread extends Thread {
	private int time = 0;

	public TestThread(Runnable r, String name) {
		super(r, name);
	}

	public int getTime() {
		return time;
	}

	public int increaseTime() {
		return ++time;
	}
}
