package com.ttlove.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class ThreadQueueManager {

	private ScheduledExecutorService mThreadPool;
	
	public ThreadQueueManager(){
		 mThreadPool = Executors.newSingleThreadScheduledExecutor(); 
	}
	
	
	/**
	 * 
	 * @param thread_ID  线程队列中相应线程唯一ID
	 * @param initialDelay  从initialDelay之后才开始运行监控线程  单位为：秒
	 * @param period  运行监控线程的时间间隔 单位为：秒
	 */
	public void addMonitorQueue(String thread_ID, long initialDelay, long period){
		
		NotifiThread notifiThread = new NotifiThread();
		notifiThread.setName(thread_ID);
		
		mThreadPool.scheduleAtFixedRate(notifiThread, initialDelay, period, TimeUnit.SECONDS);
	}
	
	
	OnThreadRunListener mOnThreadRunListener;
	public void registerThreadRunListener(OnThreadRunListener onThreadRunListener){
		mOnThreadRunListener = onThreadRunListener;
	}
	
	public void unregisterThreadRunListener(){
		mOnThreadRunListener = null;
	}
	
	public interface OnThreadRunListener{
		
		void onThreadRun(String thread_ID) throws Exception;
	};
	
	private class NotifiThread extends Thread {
	    @Override
	    public void run() {
	    	String thread_ID = getName();
	    	
//	    	Log.d("TAG", "ThreadQueueManager thread_ID="+thread_ID);
	    	
	    	if(mOnThreadRunListener != null){
				try {
					mOnThreadRunListener.onThreadRun(thread_ID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}else{
	    		Log.w("ThreadQueueManager", "ThreadQueueManager thread_ID = "+thread_ID+" Exception");
	    		mOnThreadRunListener.equals("");//产生异常是为了退出线程队列池
	    	}
	    	
	    }
	}
	
}
