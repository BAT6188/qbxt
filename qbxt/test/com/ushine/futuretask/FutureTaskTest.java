package com.ushine.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
	public static void main(String[] args) throws Exception {
		FutureTask<String> futureTask=new FutureTask<>(new Callable<String>() {

			@Override
			public String call() throws Exception {
				Thread.sleep(1000*5);
				return "success";
			}
		});
		ExecutorService service=Executors.newSingleThreadExecutor();
		service.execute(futureTask);
		System.err.println("继续执行新的代码");
		while(!futureTask.isDone()){
			
		}
		System.err.println(futureTask.get());
		service.shutdown();
	}
}
