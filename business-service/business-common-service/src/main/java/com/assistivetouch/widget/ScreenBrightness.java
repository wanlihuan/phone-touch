package com.assistivetouch.widget;


import com.niunet.assistivetouch.TempActivity;
import com.niunet.assistivetouch.TouchLogoActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class ScreenBrightness  extends Activity{
//	private Context mContext;
	private int[] mBrightnessArray = {20, 50, 80, 110, 140, 170, 210, 250};
	private int mCurrentBrightnessLevel = 0;
	private ToastProgressBar mToastProgressBar;
	public static boolean isActivity = false;
	private Intent starIntent;
	
	private BroadcastReceiver mScreenBrightnessReceiver = new BroadcastReceiver() { 
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if(action.equals("android.touch.action.SCREEN_BRIGHTNESS_ADD")){
				addScreenBrightness();
				mToastProgressBar.updateAndshowProgressBar(currentBrightness2Level());
            }else if(action.equals("android.touch.action.SCREEN_BRIGHTNESS_BELOW")){
            	
            	belowScreenBrightness();
            	mToastProgressBar.updateAndshowProgressBar(currentBrightness2Level());
            	
            }else if(action.equals("android.touch.action.TOUCH_PANEL_CLOSE")){
            	finish();
            }
		}
	};
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			case 0:
				ScreenBrightness.this.finish();
				break;
			}
			
		}
	};
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		starIntent = getIntent();
		
		mToastProgressBar = new ToastProgressBar(this, "屏幕亮度", 200);
		
		mToastProgressBar.setMaxProgress(starIntent.getIntExtra("maxProgress", 7));
		
	    IntentFilter filter = new IntentFilter();
        filter.addAction("android.touch.action.SCREEN_BRIGHTNESS_ADD");
        filter.addAction("android.touch.action.SCREEN_BRIGHTNESS_BELOW");
        filter.addAction("android.touch.action.TOUCH_PANEL_CLOSE");
        registerReceiver(mScreenBrightnessReceiver, filter);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		if(starIntent.getBooleanExtra("AdjustAction", true)){//调高 
			addScreenBrightness();
		}else{
			belowScreenBrightness();
		}
		mToastProgressBar.updateAndshowProgressBar(currentBrightness2Level());
		
		isActivity = true;
		
//		mHandler.removeMessages(0);
//		mHandler.sendEmptyMessageDelayed(0, 2000);//结束
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mScreenBrightnessReceiver);
		
		isActivity = false;
	}

	/*@Override
	public void updateAndshowProgressBar(int progress) {
		// TODO Auto-generated method stub
		
//		progress = currentBrightness2Level();
		super.updateAndshowProgressBar(progress);
		
	}*/

	
	public int getMaxLevelCount(){
		return mBrightnessArray.length;
	}
	
	
	public int currentBrightness2Level(){
		int level = 0;
		int currentBrightness = getCurrentBrightness();
		Log.d("tag", "wanlaihaun currentBrightness =" + currentBrightness);
		int maxLevel = getMaxLevelCount();
		for(int i = 0; i < maxLevel; i++){
			if(currentBrightness <= mBrightnessArray[i]){
				level = i;
				return level;
			}
		}
		
		return level;
	}
	
	public int getCurrentBrightness(){
		int currentBrightness = 0;
		try {
			currentBrightness = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentBrightness; 
	}
	
	public void addScreenBrightness(){		
	
			mCurrentBrightnessLevel = currentBrightness2Level();
			Log.d("tag", "wanlaihaun addScreenBrightness mCurrentBrightnessLevel ="+ mCurrentBrightnessLevel);
			
			if(mCurrentBrightnessLevel == getMaxLevelCount() - 1)
				return;
			
			//设置当前Activity的亮度
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.screenBrightness = mBrightnessArray[mCurrentBrightnessLevel + 1] / 255f;
			getWindow().setAttributes(lp);
			
			//设置整个手机的亮度
			Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, mBrightnessArray[mCurrentBrightnessLevel + 1]);
	}
	
	public void belowScreenBrightness(){		
		
			mCurrentBrightnessLevel = currentBrightness2Level();
			Log.d("tag", "wanlaihaun depressScreenBrightness mCurrentBrightnessLevel ="+ mCurrentBrightnessLevel);
			
			if(mCurrentBrightnessLevel == 0)
				return;
			
			//设置当前Activity的亮度
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.screenBrightness = mBrightnessArray[mCurrentBrightnessLevel - 1] / 255f;
			getWindow().setAttributes(lp);
			
			
			//设置整个手机的亮度
			Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, mBrightnessArray[mCurrentBrightnessLevel - 1]);
	}
	
}
