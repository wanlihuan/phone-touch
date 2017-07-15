package com.niunet.assistivetouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.phone.assistant.LogoActivity;
import com.assistivetouch.widget.TouchSettingView;

public class TouchLogoActivity extends LogoActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences preferences = getSharedPreferences(TouchSettingView.TOUCH_PREFERENCES, Context.MODE_WORLD_READABLE);
		boolean isChecked = preferences.getBoolean("TouchEnable", true);
    	Intent serIntent = new Intent(this, FloatingService.class);
    	if(isChecked)
    		startService(serIntent);
    	
    	//直接启动
    	/*Intent intent = new Intent();
	 	try{
		 	ComponentName componentName = new ComponentName(getPackageName(),//app的包名，而不是本类的包名
		 			regMainActivityClassName());
		 	intent.setComponent(componentName);
            startActivity(intent);
            
	 	}catch(Exception e){
	 		Toast.makeText(this, "启动Logo界面时未指定需启动的主界面！", Toast.LENGTH_SHORT).show();
	 	}
        this.finish();*/   
	}

	@Override
	public String regMainActivityClassName() {  
		// TODO Auto-generated method stub
		
		return "com.niunet.assistivetouch.ConfigMainPageActivity";
	}
}
