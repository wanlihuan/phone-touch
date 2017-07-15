package com.niunet.assistivetouch;

import com.assistivetouch.widget.TouchSettingView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartBroadcastReceiver extends BroadcastReceiver{
	
	
	public void onReceive(Context context, Intent intent) {
		SharedPreferences preferences = context.getSharedPreferences(
				TouchSettingView.TOUCH_PREFERENCES, Context.MODE_WORLD_READABLE);
		String action = intent.getAction();
		
		if (action.equals("android.intent.action.BOOT_COMPLETED")){
			boolean isChecked = preferences.getBoolean("TouchEnable", true);
        	if(isChecked){
				Intent myintent= new Intent(Intent.ACTION_RUN);
				myintent.setClass(context, FloatingService.class);
				context.startService(myintent);
        	}
				
		}else if(action.equals("com.touch.action.TOUCH_ENABLE")){
			
			boolean isChecked = preferences.getBoolean("TouchEnable", true);
			
        	Intent serIntent = new Intent(context, FloatingService.class);
        	
        	if(isChecked){
        		context.startService(serIntent);
        	}else{
        		context.stopService(serIntent);
        	}
        }
	}
}



