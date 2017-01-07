package com.ushine.test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ushine.dao.IBaseDao;
import com.ushine.store.index.PersonStoreNRTSearch;
import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.VocationalWorkStore;
/**
 * ExecutorService测试
 * @author Administrator
 *
 */
public class ExecutorServiceTest {
	public static void main(String[] args) throws Exception{
		test3();
	}
	
	private static void test1()  throws Exception{
		ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext.xml");
		IBaseDao baseDao=(IBaseDao) context.getBean("baseDao");
		//SingleThread.createIndex(baseDao);
		//ExecutorThread.createIndex(baseDao);
		
	}
	
	private static void test2()  throws Exception{
		String path="E:\\android-sdk";
		//SingleThread.copyDirectory(path); //857062毫秒
		//ExecutorThread.copyDirectory(path); //1273749
	}
	
	private static void test3()  throws Exception{
		String fieldValue="北京";
		//SingleThread.indexQuery(fieldValue);
		ExecutorThread.indexQuery(fieldValue);
	}
}

class SingleThread{
	private static final Logger logger = LoggerFactory.getLogger(SingleThread.class);
	
	public static void createIndex(IBaseDao baseDao)throws Exception {
		long start=System.currentTimeMillis();
		PersonStoreNRTSearch nrtSearch=PersonStoreNRTSearch.getInstance();
		nrtSearch.createIndex(baseDao.findAll(PersonStore.class));
		long end=System.currentTimeMillis();
		System.err.println("共用时："+(end-start)+"毫秒");
		
	}
	
	public static void copyDirectory(String path)throws Exception {
		long start=System.currentTimeMillis();
		//
		//E:\\2011年情报
		File dir=new File(path);
		File[] dirs=dir.listFiles();
		for (File file : dirs) {
			if (file.isDirectory()) {
				logger.info("正在拷贝："+file.getName()+"文件夹");
				FileUtils.copyDirectory(file, new File("F:\\demoUpload\\"+file.getName()));
			}
		}
		long end=System.currentTimeMillis();
		System.err.println("共用时："+(end-start)+"毫秒");
	}
	
	public static void indexQuery(String fieldValue)throws Exception {
		long start=System.currentTimeMillis();
		logger.info("人员库符合数量："+StoreIndexQuery.getCount(fieldValue, null, null, null, PersonStore.class));
		logger.info("业务文档库符合数量："+StoreIndexQuery.getCount(fieldValue, null, null, null, VocationalWorkStore.class));
		long end=System.currentTimeMillis();
		System.err.println("共用时："+(end-start)+"毫秒");
	}
}

class ExecutorThread{
	private static final Logger logger = LoggerFactory.getLogger(ExecutorThread.class);
	private static ExecutorService service=Executors.newFixedThreadPool(10);
	public static void createIndex(final IBaseDao baseDao)throws Exception {
		long start=System.currentTimeMillis();
		try {
			service.execute(new Runnable() {
				public void run() {
					PersonStoreNRTSearch nrtSearch=PersonStoreNRTSearch.getInstance();
					try {
						nrtSearch.createIndex(baseDao.findAll(PersonStore.class));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
		} catch (Exception e) {
			
		}finally {
			service.shutdown();
		}
		//每隔100毫秒监测一次是否已经关闭
		/*while (!service.awaitTermination(100, TimeUnit.MILLISECONDS)) {  
		    //System.out.println("线程池没有关闭");  
		}*/ 
		while(!service.isTerminated()){
			
		}
		//输出执行的时间
		long end=System.currentTimeMillis();
		logger.info("共用时："+(end-start)+"毫秒");
	}
	
	public static void copyDirectory(String path)throws Exception {
		long start=System.currentTimeMillis();
		try {
			//E:\\2011年情报
			File dir=new File(path);
			File[] dirs=dir.listFiles();
			for (final File file : dirs) {
				if (file.isDirectory()) {
					service.execute(new Runnable() {
						@Override
						public void run() {
							logger.info("正在拷贝："+file.getName()+"文件夹");
							try {
								FileUtils.copyDirectory(file, new File("F:\\demoUpload\\"+file.getName()));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		} catch (Exception e) {
			
		}finally {
			service.shutdown();
		}
		//每隔100毫秒监测一次是否已经关闭
		/*while (!service.awaitTermination(100, TimeUnit.MILLISECONDS)) {  
		    //System.out.println("线程池没有关闭");  
		}*/ 
		while(!service.isTerminated()){
			
		}
		//输出执行的时间
		long end=System.currentTimeMillis();
		logger.info("共用时："+(end-start)+"毫秒");
	}
	
	public static void indexQuery(final String fieldValue)throws Exception {
		long start=System.currentTimeMillis();
		try {
			Class []classes=new Class[]{VocationalWorkStore.class,PersonStore.class};
			for (final Class clazz : classes) {
				service.execute(new Runnable() {
					@Override
					public void run() {
						try {
							logger.info(clazz.getSimpleName()+"符合要求数量："+StoreIndexQuery.getCount(
									fieldValue, null, null, null, clazz));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (Exception e) {
			
		}finally {
			service.shutdown();
		}
		//每隔100毫秒监测一次是否已经关闭
		/*while (!service.awaitTermination(100, TimeUnit.MILLISECONDS)) {  
		    //System.out.println("线程池没有关闭");  
		}*/ 
		while(!service.isTerminated()){
			
		}
		//输出执行的时间
		long end=System.currentTimeMillis();
		logger.info("共用时："+(end-start)+"毫秒");
	}
}


