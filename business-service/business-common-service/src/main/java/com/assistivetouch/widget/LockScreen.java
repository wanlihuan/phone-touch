package com.assistivetouch.widget;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class LockScreen {
	Context mContext;  
	private DevicePolicyManager policyManager;    
	private ComponentName componentName;
	private static LockScreen mLockScreen = null;
	
	public static void creatInstance(Context context){
		if(mLockScreen == null)
			mLockScreen = new LockScreen(context);
	}
	
	public static LockScreen getInstance(){
		
		return mLockScreen;
	}
	
	private LockScreen(Context context){
		mContext = context;
	}
	
	public boolean getDevicceAdmin(){
		//获取设备管理服务    
		if(null == policyManager)
	      policyManager = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);    
		if(null == componentName)
	      componentName = new ComponentName(mContext, AdminReceiver.class);  
		
		return policyManager.isAdminActive(componentName);
	}
	
	public void removeActiveAdmin(){
		policyManager.removeActiveAdmin(componentName);
	}
	 
	public void startLockScreen(boolean devicceAdmin){
		
      if(!devicceAdmin){//若无权限    
          activeManage();//去获得权限    
          try{
      		policyManager.lockNow();//直接锁屏    
      	  }catch(Exception e){
      		//activeManage();//去获得权限    
            //  policyManager.lockNow();//并锁屏    
      	  }   
      }    
      if (devicceAdmin) {    
      	try{
      		policyManager.lockNow();//直接锁屏    
      	}catch(Exception e){
      		activeManage();//去获得权限    
              policyManager.lockNow();//并锁屏    
      	}
      }    
	}
	public void activeManage() {    
       // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器    
       Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);    	            
       //权限列表    
       intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);    
  
       //描述(additional explanation)    
       intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "若需要卸载此应用，请先取消激活！");    
       mContext.startActivity(intent);    
   }   
}
