package com.ushine.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * ThreadFactory
 * @author Administrator
 *
 */
public class ThreadFactoryTest {
	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newCachedThreadPool(new WorkThreadFactory());
		for (int i = 0; i < 5; i++) {
			es.submit(new WorkRunnable());
		}
		es.shutdown();
		// 等待线程执行完毕，不能超过2*60秒，配合shutDown
		es.awaitTermination(2 * 60, TimeUnit.SECONDS);
	}
}

class WorkRunnable implements Runnable {

	@Override
	public void run() {
		System.err.println("complete a task!!!");
	}

}

class WorkThread extends Thread {
	private Runnable target; // 线程执行目标
	private AtomicInteger counter;

	public WorkThread(Runnable target, AtomicInteger counter) {
		this.target = target;
		this.counter = counter;
	}

	@Override
	public void run() {
		try {
			target.run();
		} finally {
			int c = counter.getAndDecrement();
			System.err.println("terminate no " + c + " Threads");
		}
	}
}

class WorkThreadFactory implements ThreadFactory {
	private AtomicInteger atomicInteger = new AtomicInteger(0);

	@Override
	public Thread newThread(Runnable runnable) {
		int c = atomicInteger.incrementAndGet();
		System.err.println("create no " + c + " Threads");
		return new WorkThread(runnable, atomicInteger);// 通过计数器，可以更好的管理线程
	}
}
