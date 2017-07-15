package com.assistivetouch.widget;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.widget.TextView;

public class Flashlight {
	Context mContext;
	private static boolean isCameraOpen = false;
	private Camera camera = null;
	
	public Flashlight(Context context){
		mContext = context;
		
	}
	
	TextView mTextView;
	 public void setView(TextView textView){
			mTextView = textView;
	 }
	 
	 public void updateUiShow(){
		 mTextView.setSelected(getState());
	 }
	 
	public static boolean getState(){
		return isCameraOpen;
	}
	
	public static void setState(boolean state){
		isCameraOpen = state;
	}
	
	public void onOrOffCamera(){
		if(!getState()){
			//Toast.makeText(mContext, "您已经打开了手电筒", Toast.LENGTH_SHORT).show();
			try{
				camera = Camera.open();
	            Parameters params = camera.getParameters();
	            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
	            camera.setParameters(params);
	            camera.startPreview(); // 开始亮灯
			}catch(Exception e){
				
			}
		}else{
			//Toast.makeText(mContext, "关闭了手电筒", Toast.LENGTH_SHORT).show();
			try{
				camera.stopPreview(); // 关掉亮灯
				camera.release(); // 关掉照相机
			}catch(Exception e){
				
			}
		}
		setState(!getState());
	}
	
}
