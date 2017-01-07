package com.ushine.test;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ushine.core.service.impl.OrganizServiceImpl;
import com.ushine.dao.IBaseDao;
import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.LeadSpeakStore;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;
/**
 * 线程池管理
 * @author Administrator
 *
 */
public class ThreadPoolTest {
	private static int produceTaskSleepTime = 200;
	private static int produceTaskMaxNumber = 10;

	public static void main(String[] args)throws Exception {
		// 构造一个线程池
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(3), 
				new ThreadPoolExecutor.DiscardOldestPolicy());

		/*for (int i = 1; i <= produceTaskMaxNumber; i++) {
			try {
				// 产生一个任务，并将其加入到线程池
				String task = "task@ " + i;
				System.out.println("put " + task);
				threadPool.execute(new ThreadPoolTask(task));
				// 便于观察，等待一段时间
				Thread.sleep(produceTaskSleepTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
		IBaseDao baseDao=(IBaseDao) context.getBean("baseDao");
		Class []classes=new Class[]{PersonStore.class,VocationalWorkStore.class,OrganizStore.class
				,ClueStore.class,WebsiteJournalStore.class,LeadSpeakStore.class,OutsideDocStore.class};
		for (Class clazz : classes) {
			threadPool.execute(new MyThreadPoolTask(clazz,baseDao));
		}
		
		threadPool.shutdown();
	}
}

/**
 * 线程池执行的任务
 */
class ThreadPoolTask implements Runnable, Serializable {
	private static final long serialVersionUID = 0;
	private static int consumeTaskSleepTime = 2000;
	// 保存任务所需要的数据
	private Object threadPoolTaskData;

	ThreadPoolTask(Object tasks) {
		this.threadPoolTaskData = tasks;
	}

	public void run() {
		// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
		System.out.println(Thread.currentThread().getName());
		System.out.println("start .." + threadPoolTaskData);

		try {
			// 便于观察，等待一段时间
			Thread.sleep(consumeTaskSleepTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("end .." + threadPoolTaskData);
		threadPoolTaskData = null;

	}

	public Object getTask() {
		return this.threadPoolTaskData;
	}
}
/**
 * 我的线程池执行任务
 * @author Administrator
 *
 */
class MyThreadPoolTask implements Runnable{
	private static final Logger logger = LoggerFactory.getLogger(MyThreadPoolTask.class);
	private Class clazz;
	private IBaseDao baseDao;
	public MyThreadPoolTask(Class clazz,IBaseDao baseDao) {
		this.clazz=clazz;
		this.baseDao=baseDao;
	}
	@Override
	public void run() {
		try {
			int count= baseDao.findAll(clazz).size();
			logger.info(clazz.getSimpleName()+"总数量："+count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
