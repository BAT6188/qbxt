package com.ushine.test;
/**
 * 基于java线程实现后台定时监控
 * @author Administrator
 *
 */
public abstract class SchedThread {
	protected static final long NEVER=Long.MAX_VALUE;
	//定义一个线程锁,保证当前只有一个在操作
	private final Object lock= new Object();
	//定义一个Thread变量
	private Thread thread;
	//控制线程循环的开关
	private boolean active=true;
	//定义一个毫秒级的时间变量,指示何时执行下一个操作
	private long nextTime;
	/**
	 * 定义一个抽象方法来获取下一个执行操作的时间,可使用NEVER
	 * @return
	 */
	protected abstract long getNextTime();
	/**
	 * 定义一个抽象方法,让子类来定义具体工作过程
	 */
	protected abstract void executeWork();
	
	protected String getName(){
		return getClass().getName();
	}
	/**
	 * 线程启动
	 */
	public void start(){
		thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				runInternal();
			}
		},getName());
		thread.start();
	}
	/**
	 * 强行停止线程,跳出for循环
	 * @throws Exception
	 */
	public void stop()throws Exception{
		synchronized (lock) {
			active=false;
			lock.notify();
		}
		thread.join();
	}
	/**
	 * 此方法可在任何时候激活当前线程,让线程进入工作执行环节
	 * @param time
	 */
	public void workAdded(long time) {
		synchronized (lock) {
			if(time<nextTime){
				//立即激活线程工作继续进行
				lock.notify();
			}
		}
	}
	
	private void runInternal() {
		//无限循环
		for(;;){
			try {
				synchronized (lock) {
					nextTime=getNextTime();
					//获得时间区间,即要等待的时间段
					long interval=nextTime-System.currentTimeMillis();
					if(interval>0){
						try {
							lock.wait(interval);
						} catch (InterruptedException e) {
							//e.printStackTrace();
							//忽略
						}
					}
					//active为false,强制中断
					if(!active){
						break;
					}
				}
				executeWork();
			} catch (Exception e) {
				//忽略
			}
		}
	}
}









