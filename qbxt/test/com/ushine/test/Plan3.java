package com.ushine.test;
import java.lang.Thread.*;
import java.util.concurrent.*;
/**
 * 单线程池的捕获异常方式
 * @author Administrator
 *
 */
public class Plan3 {
    private SimpleTask task = new SimpleTask();
    private MyFactory factory = new MyFactory(task);
    public static void main(String[] args) {
        Plan3 plan = new Plan3();
        ExecutorService pool = Executors.newSingleThreadExecutor(plan.factory);
        pool.execute(plan.task);
        pool.shutdown();
    }
    
    class MyFactory implements ThreadFactory{
        private SimpleTask task;
        public MyFactory(SimpleTask task) {
            super();
            this.task = task;
        }
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    ExecutorService pool = Executors.newSingleThreadExecutor(new MyFactory(task));
                    pool.execute(task);
                    pool.shutdown();
                }
            });
            return thread;
        }
    }
    
    class SimpleTask implements Runnable{
        private int task = 10;
        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName+"--"+"启动");
            while(task>0){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(System.currentTimeMillis()%3==0){
                	System.err.println("模拟出现异常情况……");
                    throw new RuntimeException("模拟异常");
                }
                System.out.println(threadName+"--"+"执行task"+task);
                task--;
            }
            System.out.println(threadName+"--"+"正常终止");
        }
    }
}