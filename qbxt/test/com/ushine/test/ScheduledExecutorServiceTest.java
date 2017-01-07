package com.ushine.test;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.io.FileUtils;

import com.ushine.util.SmbFileUtils;	
/**
 * 捕获ScheduledExecutorService异常
 * @author Administrator
 *
 */
public class ScheduledExecutorServiceTest {
	public static void main(String[] args){
		ScheduledExecutorService service=null;
		Future<String> future=null;
		try {
			service=Executors.newSingleThreadScheduledExecutor();
			future=service.submit(new CopySmbFile());
			String result=future.get();
			System.err.println("任务执行结果："+result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			service.shutdown();
		}
		
	}
}
/**
 * 需要采用抛异常的方式
 * @author Administrator
 *
 */
class MyTest{
	public static void copySmbWordToDir(String url) throws Exception{
		long value=System.currentTimeMillis();
		System.err.println("currentTimeMillis："+value);
		if(value%2==0){
			int i=1/0;
		}
	}
}
class CopySmbFile implements Callable<String>{
	@Override
	public String call() throws Exception {
		try {
			String dirUrl="E:\\2011年情报\\公安技侦情报";
			File dir=new File(dirUrl);
			File []files=dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					//拷贝
					System.err.println("正在拷贝文件："+file.getName());
					FileUtils.copyFile(file, new File("F:\\demoUpload\\"+file.getName()));
				}
			}
			//抛出异常的方式进行,就可以捕获方法体异常
			MyTest.copySmbWordToDir(null);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
	}
}
