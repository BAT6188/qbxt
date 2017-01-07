package com.ushine.threadpool;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;

public class ThreadPoolTask implements Callable<String>, Serializable {

	private static final long serialVersionUID = 1L;
	private Object threadPoolTaskData;
	private static int consumeTaskSleepTime = 2000;

	public ThreadPoolTask(Object tasks) {
		this.threadPoolTaskData = tasks;
	}

	@Override
	public synchronized String call() throws Exception {
		// 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
		String result = "";
		System.err.println("开始执行任务：" + threadPoolTaskData);
		try {
			File srcDir=new File("E:\\2011测试导入业务文档数据");
			File destDir=new File("E:\\testcopy");
			FileUtils.copyDirectory(srcDir, destDir);
			result = "OK";
		} catch (Exception e) {
			e.printStackTrace();
			result = "ERROR";
		}
		threadPoolTaskData=null;
		return result;
	}

}
