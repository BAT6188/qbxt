package com.ushine.storeInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.LeadSpeakStore;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;
/**
 * 并发编程测试
 * @author dh
 *
 */
public class CurrentTest {
	private static int executorCount = 0;
	private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private static int i=0;
	Class[] classes=new Class[]{PersonStore.class,OrganizStore.class,WebsiteJournalStore.class,
			ClueStore.class,OutsideDocStore.class,VocationalWorkStore.class,LeadSpeakStore.class};
	
	@Test
	public void testExecutors() throws Exception{
		ExecutorService service = Executors. newFixedThreadPool(3);  
        for ( int i = 0; i < 10; i++) {  
            System. out.println( "创建线程" + i);  
            Runnable run = new Runnable() {  
                @Override  
                public void run() {
                	try {
                		Thread.sleep(1000);
						System. out.println("启动线程");  
					} catch (Exception e) {
						e.printStackTrace();
					}
                }  
            };  
            // 在未来某个时间执行给定的命令  
            service.execute(run);  
        }  
        // 关闭启动线程  
        service.shutdown();  
        // 每隔1秒监测一次ExecutorService的关闭情况.  
        service.awaitTermination(1, TimeUnit. SECONDS);  
        System. out.println( "all thread complete");
        if(service.isTerminated()){
        	System. out.println(service.isTerminated());  
        }
	}
	/**
//	 * 1秒后开始执行，执行一次
	 */
	public static void test1(){
		executorService.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					System.err.println("开始执行该任务");
					Thread.sleep(10000);
					System.err.println("执行该任务结束");
					executorService.shutdown();
					System.err.println(executorService.isShutdown());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 1, TimeUnit.SECONDS);
	}
	
	
	public static void main(String[] args) {
		test1();
	}
	
	/**
	 * 可处理异常
	 */
	public static void test2() {
		executorService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				// 通过这种方式可以在出现异常之后代码继续执行
				// 必须加上try_catch捕获异常之后才会在异常后继续执行
				int j=0;
				try {
					test(j);
					executorCount++;
					if (executorCount == 1) {
						// 停止执行
						executorService.shutdown();
					}
				} catch (Exception e) {
					j=i;
					e.printStackTrace();
				}
				
			}//出现异常之后2秒后接着执行
		}, 1, 2, TimeUnit.SECONDS);
	}
	
	public static void test(int j)throws Exception{
		for (j=i; j <10; j++) {
			System.out.println("---"+i+"---");
			i++;
			Thread.sleep(1000);
			if(i==5){
				i=i/0;
			}
		}
	}
}
