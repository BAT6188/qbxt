package com.ushine.storeInfo;

import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.PersonStore;

public class ThreadTest {
	
	public static void main(String[] args) throws Exception{
		long start=System.currentTimeMillis();
		//线程1
		Thread thread1=new Thread(new MyThread());
		thread1.start();
		thread1.join();
		//线程2
		//Thread thread2=new Thread(new MyThread());
		//thread2.start();
		//thread2.join();
		long end=System.currentTimeMillis();
		System.err.println("时长："+(end-start));
	}
}

class MyThread implements Runnable{
	@Override
	public void run() {
		try {
			System.err.println("线程开始");
			StoreIndexQuery.findStore("anyField", "1989", "1900-01-01", "2016-10-01", 
					1, 1000, null, null, null, null, null, PersonStore.class);
			System.err.println("线程结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}