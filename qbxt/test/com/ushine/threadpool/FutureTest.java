package com.ushine.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.springframework.context.*;
import org.springframework.context.support.*;

import com.ushine.storesinfo.service.IPersonStoreService;
/**
 * 测试异步执行查询结果
 * @author octocat
 *
 */
public class FutureTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		final IPersonStoreService service = (IPersonStoreService) context.getBean("personStoreServiceImpl");
		System.out.println("==开始==");
		long start = System.currentTimeMillis();
		FutureTask<String> task = new FutureTask<>(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return service.findPersonStore("anyField", "男", null, null, 1, 10, null, null, null, null, null);
			}
		});
		task.run();
		System.err.println(task.get());
		long end = System.currentTimeMillis();
		System.out.println("==结束==");
		System.out.println("==用时：==" + (end - start));

	}
	
	class MyFutureTask{
		
	}
}
