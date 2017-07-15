package com.assistivetouch.widget;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.TextView;

public class MobileNetwork {
	
	Context mContext;
	ConnectivityManager mConnectivityManager; 
	boolean isMobileDataEnable;
	
	 public MobileNetwork(Context context){
		 mContext = context;
		 mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	 }
	 
	 TextView mTextView;
	 public void setView(TextView textView){
			mTextView = textView;
	 }
	 
	 public void updateUiShow(){
		 mTextView.setSelected(getGprsDataEnable());
	 }
	 
	 public void openCloseNetWork()
	 {
	    Object[] arg = null;
	    try {
	      boolean enable = (Boolean) invokeMethod("getMobileDataEnabled", arg);
	      isMobileDataEnable = !enable;
	   //   boolean enable = getGprsDataEnable();
	      invokeBooleanArgMethod("setMobileDataEnabled", !enable);
	      
	    } catch (Exception e) {
	     // TODO Auto-generated catch block
	     e.printStackTrace();
	    }

	 }
	    
	    public void initEnable(){
	    	Object[] arg = null;
	    	try {
				isMobileDataEnable = (Boolean) invokeMethod("getMobileDataEnabled", arg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    public boolean getGprsDataEnable(){
	    	
	    	return isMobileDataEnable;
	    }
	    
	    @SuppressWarnings("rawtypes")
	    public Object invokeMethod(String methodName, Object[] arg) throws Exception
	    {
		    Class ownerClass = mConnectivityManager.getClass();
		    Class[] argsClass = null;
		    if (arg != null)
		    {
		    	argsClass = new Class[1];
		    	argsClass[0] = arg.getClass();
		    }
		    Method method = ownerClass.getMethod(methodName, argsClass);
	    return method.invoke(mConnectivityManager, arg);
	    }

	    @SuppressWarnings("rawtypes")
	    public Object invokeBooleanArgMethod(String methodName, boolean value) throws Exception
	    {
		    Class ownerClass = mConnectivityManager.getClass();
		    Class[] argsClass = new Class[1];
		    argsClass[0] = boolean.class;
		    Method method = ownerClass.getMethod(methodName, argsClass);
		    return method.invoke(mConnectivityManager, value);
	    }
}
