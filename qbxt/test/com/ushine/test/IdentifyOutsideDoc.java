package com.ushine.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class IdentifyOutsideDoc {
	private Future<String> future = null;
	private ExecutorService service;
	public Future<String> getFuture() {
		return future;
	}
	
	public IdentifyOutsideDoc() {
		service = Executors.newSingleThreadExecutor();
	}

	public void setFuture(Future<String> future) {
		this.future = future;
	}

	public ExecutorService getService() {
		return service;
	}

	public void setService(ExecutorService service) {
		this.service = service;
	}
	
	public void startExecutorService(String ip,String destDir){
		identifyAndSave(ip, destDir);
	}
	
	class Submit implements Callable<String>{
		@Override
		public String call() throws Exception {
			try {

				return "success";
			} catch (Exception e) {
				return "exception";
			}
		}
	}
	
	public void identifyAndSave(final String ip, final String destDir) {
		
		try {
			service.submit(new Submit());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			service.shutdown();
		}
	}

	public String getfutureTaskResult() throws Exception {
		return service.isShutdown()? future.get() : "doing";
	}
}
