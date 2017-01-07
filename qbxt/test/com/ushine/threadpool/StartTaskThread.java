package com.ushine.threadpool;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 模拟客户端提交的线程
 * 
 * @author Administrator
 *
 */
public class StartTaskThread implements Runnable {
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private int i;

	public StartTaskThread(ThreadPoolTaskExecutor _threadPoolTaskExecutor, int _i) {
		this.threadPoolTaskExecutor = _threadPoolTaskExecutor;
		this.i = _i;
	}

	@Override
	public synchronized void run() {
		String task = "task@" + i;
		System.err.println("创建任务并提交到线程池中：" + task);
		FutureTask<String> futureTask = new FutureTask<>(new ThreadPoolTask(task));
		threadPoolTaskExecutor.execute(futureTask);
		String result = "doing";
		try {
			// 获得结果，同时设置超时时间为1秒
			// 1000, TimeUnit.MILLISECONDS
			result = futureTask.get(1000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			futureTask.cancel(true);
			System.err.println("task@" + i + ":result=" + result);
		}
	}

}
