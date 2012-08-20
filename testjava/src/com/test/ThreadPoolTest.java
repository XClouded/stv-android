package com.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadPoolTest {

	public static void main(String[] strs) throws InterruptedException, ExecutionException, TimeoutException {

		ExecutorService exec = Executors.newSingleThreadExecutor();

		Future<String> fut = exec.submit(new TaskCallable());
		// 无论是否有返回结果，最多阻塞1秒
		String str1 = fut.get(1, TimeUnit.SECONDS);
		// 直到有返回结果，否则一直阻塞
		String str2 = fut.get();
		System.out.println(str1);
		System.out.println(str2);

	}

}

class TaskCallable implements Callable<String> {
	@Override
	public String call() throws Exception {
		return "===========" + System.currentTimeMillis();
	}

}