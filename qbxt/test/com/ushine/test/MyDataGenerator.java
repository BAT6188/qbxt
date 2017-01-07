package com.ushine.test;

public class MyDataGenerator extends SchedThread {

	@Override
	protected long getNextTime() {
		return System.currentTimeMillis()+1000;
	}

	@Override
	protected void executeWork() {
		System.err.println("Execute work……");
	}
	public static void main(String[] args) {
		MyDataGenerator test=new MyDataGenerator();
		test.start();
	}
}
