package com.ushine.test;
/**
 * 交替打印
 * @author Administrator
 *
 */
public class PrintThread implements Runnable {
	private Object prev;
	private Object self;
	private String name;
	
	public PrintThread(Object prev, Object self,String name) {
		this.prev = prev;
		this.self = self;
		this.name = name;
	}
	
	public PrintThread() {
	
	}

	@Override
	public void run() {
		int count=10;
		while(count>0){
			synchronized (prev) {
				try {
					synchronized (self) {
						count--;
						System.err.println(name);
						//System.err.println(name+"打印第"+(i+1)+"次");
						self.notify();
					}
					prev.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args)throws Exception {
		Object a=new Object();
		Object b=new Object();
		Object c=new Object();
		
		Thread t1=new Thread(new PrintThread(c, a,"A"));
		Thread t2=new Thread(new PrintThread(a, b,"B"));
		Thread t3=new Thread(new PrintThread(b, c,"C"));
		t2.start();
		t1.start();
		t3.start();
		
	}
}
