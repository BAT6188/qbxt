package com.ushine.storeInfo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.LeadSpeakStore;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;

public class CallableFutureTest {
	class MyCallable implements Callable<String>{
		private String fieldValue;
		private Class clazz;
		
		public MyCallable(String fieldValue, Class clazz) {
			super();
			this.fieldValue = fieldValue;
			this.clazz = clazz;
		}
		
		public MyCallable() {
			
		}

		@Override
		public String call() throws Exception {
			return clazz.getSimpleName()+"符合要求数量："+StoreIndexQuery.getCount(fieldValue, null, null, null, clazz);
		}
	}
	Class[] classes=new Class[]{PersonStore.class,OrganizStore.class,WebsiteJournalStore.class,
			ClueStore.class,OutsideDocStore.class,VocationalWorkStore.class,LeadSpeakStore.class};
	
	@Test
	public void testMyCallable()throws Exception{
		// 创建一个执行任务的服务
		long start=System.currentTimeMillis();
		System.err.println("=====开始=====");
		ExecutorService es = Executors.newFixedThreadPool(7);
		for (Class clazz : classes) {
			MyCallable task=new MyCallable("董昊", clazz);
			Future future=es.submit(task);
			System.err.println(future.get());
		}
		System.err.println("=====结束=====");
		es.shutdown();
		long end =System.currentTimeMillis();
		//毫秒
		System.err.println(String.format("共用时%s毫秒", Long.toString((end-start))));
	}
}
