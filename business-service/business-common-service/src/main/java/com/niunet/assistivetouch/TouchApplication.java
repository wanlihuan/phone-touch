package com.niunet.assistivetouch;

import java.util.Iterator;
import java.util.Set;

import com.android.assistivetouch.R;
import com.ttlove.util.ThreadQueueManager;
import com.ttlove.util.ThreadQueueManager.OnThreadRunListener;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.android.phone.assistant.util.TouchWords;
public class TouchApplication extends Application {
	SharedPreferences preferences;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		preferences = getSharedPreferences("touch_preferences", Context.MODE_WORLD_READABLE);
		
		/*++add by wanlaihuan 2013-12-18*/
		UiManager mUiManager = new UiManager(this);
		TouchWords.init(UiManager.appPackageName);
		DatabaseUtil databaseUtil = new DatabaseUtil(this);
		
		mUiManager.initViewTagList(true, null);//初始化所有的按钮
		
		//++后续需要删除 2013-12-18 add
		if(!preferences.getBoolean("isClear1", false)){
			
			//Toast.makeText(this, "菜单键和返回键在自定义面板中，可根据自己的需要进行加入！", Toast.LENGTH_LONG).show();
			databaseUtil.deleteDbDataForAll(); 
			
			Editor editor = preferences.edit();
			editor.putBoolean("isClear1", true).commit();
			//Log.d("TAG", "TouchApplication Clear!");
		}
		//--后续需要删除 2013-12-18 add
		
		saveAllDataToDatabase(databaseUtil);
		/*--add by wanlaihuan 2013-12-18*/
		
		final ThreadQueueManager mThreadQueueManager = new ThreadQueueManager();
		mThreadQueueManager.registerThreadRunListener(new OnThreadRunListener(){

			public void onThreadRun(String thread_ID) throws Exception {
				// TODO Auto-generated method stub
				
				int tiXingCount = preferences.getInt("isTiXingCount1", 0);
				if(tiXingCount < 3)//最多提醒3次
				{
					newThemeNotifaction();
					
					Editor editor = preferences.edit();
					editor.putInt("isTiXingCount1", ++tiXingCount);
					editor.commit();
				}
				
				if(thread_ID.equals("NewThemeNotifaction")){
					mThreadQueueManager.unregisterThreadRunListener();
					
				}
			}
		});
		
		//mThreadQueueManager.addMonitorQueue("NewThemeNotifaction", 15, 15);//新主题提醒通知
		
	}
	
	private void saveAllDataToDatabase(DatabaseUtil databaseUtil){
		String dbId = null;
		
		Set<String> keys = UiManager.mViewTagList.keySet();
		 for(Iterator<String> it = keys.iterator(); it.hasNext();){
			String key = it.next();
			
			//没有默认位置的功能按钮暂时不添加到数据库中，等待用户自定义的时候添加
			if(UiManager.mViewTagList.get(key).panelNum == UiManager.PANEL_TYPE_NO)
				continue;
			
			ViewInfo tagInfo = new ViewInfo();
			tagInfo.viewId = UiManager.mViewTagList.get(key).viewId;
			tagInfo.panelNum = UiManager.mViewTagList.get(key).panelNum;
			tagInfo.cellNum = UiManager.mViewTagList.get(key).cellNum;
			tagInfo.intentUri = UiManager.mViewTagList.get(key).intentUri;
			tagInfo.icon = null;//图片不保存至数据库
			tagInfo.label = UiManager.mViewTagList.get(key).label;
			
			dbId = null;
			dbId = databaseUtil.queryDbIdFromWord(TouchWords.VIEW_ID, tagInfo.viewId);
			if(dbId == null)//说明不存在，是新添加的按钮功能
				databaseUtil.insertNewTagInfo(tagInfo);
		 }
	}
	
	 private void newThemeNotifaction(){
		 	NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	        Intent intent = new Intent();
	        ComponentName componentName= new ComponentName(getPackageName(), "com.niunet.assistivetouch.TouchLogoActivity");
	        intent.setComponent(componentName);
	 		PendingIntent pi = PendingIntent.getActivity(this, 0, intent,0);  
	 		
	 		//创建一个Notification
	 		Notification notify = new Notification();
	 		notify.icon = R.drawable.ic_launcher_notifition;
	 		notify.tickerText = /*getResources().getText(Rs.string.app_name)+*/"IPhone IOS7高清主题";
	 		notify.when = System.currentTimeMillis();
	 		notify.defaults = Notification.DEFAULT_SOUND;//为Notification设置声音
	 		notify.defaults = Notification.DEFAULT_ALL;//为Notification设置默认声音、默认振动、默认闪光灯
	 		notify.flags = Notification.FLAG_AUTO_CANCEL;//点击后自动结束
	 		
//	 		notify.setLatestEventInfo(this, /*getResources().getText(Rs.string.app_name)+*/"IPhone IOS7高清主题",
//	 				"点击进入更换界面!", pi);
	 		
			mNotificationManager.notify(0x990, notify);	//发送通知
	}
}

