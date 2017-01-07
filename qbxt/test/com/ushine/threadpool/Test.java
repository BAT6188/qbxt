package com.ushine.threadpool;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class Test extends AbstractJUnit4SpringContextTests {
	private static int produceTaskSleepTime = 10;
	private static int produceTaskMaxNumber = 1;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return threadPoolTaskExecutor;
	}

	public void setThreadPoolTaskExecutor(
			ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;

	}
	@org.junit.Test
	public void testThreadPoolExecutor(){
		for(int i=0;i<produceTaskMaxNumber;i++){
			try {
				Thread.sleep(produceTaskSleepTime);
				new Thread(new StartTaskThread(threadPoolTaskExecutor, i)).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
