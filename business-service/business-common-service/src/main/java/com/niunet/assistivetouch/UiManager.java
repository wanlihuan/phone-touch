package com.niunet.assistivetouch;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.assistivetouch.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.android.phone.assistant.util.AppManger;

public class UiManager extends AppManger{
	Context mContext;
	DisplayMetrics dm;
	static int tarIconWidth;
	static int tarIconHeight;
	
	public static final Map<String, ViewInfo> mViewTagList = new HashMap<String, ViewInfo>();
	
	/**
	 * 用于不需要默认加入面板按钮，
	 */
	public static final String PANEL_TYPE_NO = "99";
	
	
	/**
	 * 第一面板，主面板
	 */
	public static final String PANEL_TYPE_0 = "0";
	/**
	 * 设备开关面板
	 */
	public static final String PANEL_TYPE_1 = "1";
	/**
	 * 常用面板
	 */
	public static final String PANEL_TYPE_2 = "2";
	
	/**
	 * 设备更多面板
	 */
	public static final String PANEL_TYPE_3 = "3";
	
	
	/**
	 * 锁屏事件
	 */
	public static final String EVENT_LOCK_SCREEN = "EVENT_LOCK_SCREEN";
	
	/**
	 * 返回键事件
	 */
	public static final String EVENT_KEYCODE_BACK = "EVENT_KEYCODE_BACK";
	
	/**
	 * 菜单键事件
	 */
	public static final String EVENT_KEYCODE_MENU = "EVENT_KEYCODE_MENU";
	
	
	/**
	 * 飞行模式事件
	 */
	public static final String EVENT_AIR_PLANE  = "EVENT_AIR_PLANE";
	
	/**
	 * 增加屏幕亮度事件
	 */
	public static final String EVENT_SCREEN_BRIGHTNESS_ADD  = "EVENT_SCREEN_BRIGHTNESS_add";
	
	/**
	 * 降低屏幕亮度事件
	 */
	public static final String EVENT_SCREEN_BRIGHTNESS_BELOW  = "EVENT_SCREEN_BRIGHTNESS_below";
	
	
	/**
	 * 手电筒事件
	 */
	public static final String EVENT_FLASH_LIGHT = "EVENT_FLASH_LIGHT";
	
	/**
	 * 自动旋转事件
	 */
	public static final String EVENT_AUTOUTO_ROTATION = "EVENT_AUTOUTO_ROTATION";
	
	/**
	 * 情景模式切换事件
	 */
	public static final String EVENT_SCENE_MODE_SWITCH = "EVENT_SCENE_MODE_SWITCH";
	
	/**
	 * 媒体音量增加调节
	 */
	public static final String EVENT_MUSIC_VOLUME_ADD = "EVENT_MUSIC_VOLUME_ADD";
	
	/**
	 * 媒体音量降低调节
	 */
	public static final String EVENT_MUSIC_VOLUME_BELOW = "EVENT_MUSIC_VOLUME_BELOW";
	
	/**
	 * 铃声音量增加调节
	 */
	public static final String EVENT_RING_VOLUME_ADD = "EVENT_RING_VOLUME_ADD";
	
	/**
	 * 铃声音量降低调节
	 */
	public static final String EVENT_RING_VOLUME_BELOW = "EVENT_RING_VOLUME_BELOW";
	
	/**
	 * 闹钟音量增加调节
	 */
	public static final String EVENT_ALARM_VOLUME_ADD = "EVENT_ALARM_VOLUME_ADD";
	
	/**
	 * 闹钟音量降低调节
	 */
	public static final String EVENT_ALARM_VOLUME_BELOW = "EVENT_ALARM_VOLUME_BELOW";
	
	
	/**
	 * 移动梦网事件
	 */
	public static final String EVENT_MOBILE_NETWORK = "EVENT_MOBILE_NETWORK";
	
	/**
	 * WIFI开关事件
	 */
	public static final String EVENT_WIFI_ON_OFF = "EVENT_WIFI_ON_OFF";
	
	/**
	 * WIFI开关事件
	 */
	public static final String EVENT_BLUETOOTH_ON_OFF = "EVENT_BLUETOOTH_ON_OFF";
	
	
	
	/**
	 * 控制板的跳转
	 */
	public static final String EVENT_PANEL_SWITCH = "EVENT_PANEL_SWITCH";
	
	/**
	 * 绑定某一整个面板
	 */
	BindItemsCallback mBindItemsCallback;
	public void setBindItemsCallbacks(BindItemsCallback bindItemsCallback){
		mBindItemsCallback = bindItemsCallback;
	}
	
	public interface BindItemsCallback {
		/**
		 * @param itemInfo
		 */
        public void bindItems(String panelNum, ArrayList<ViewInfo> itemInfo);
	}
	
	//添加一项数据回调
	AddItemCallback mAddItemCallback;
	public void setAddItemCallbacksCallbacks(AddItemCallback addItemCallback){
		mAddItemCallback = addItemCallback;
	}
	public interface AddItemCallback {
		/**
		 * 
		 * @param viewInfo
		 */
        public void addItem(ViewInfo viewInfo);
	}

	public UiManager(){
		
	}
	
	public UiManager(Context context){
		mContext = context;
		dm = new DisplayMetrics();	
	    WindowManager winMgr = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);            
	    winMgr.getDefaultDisplay().getMetrics(dm);
	    tarIconWidth = Math.min(dm.widthPixels, dm.heightPixels) / 4 -10;
        tarIconHeight = tarIconWidth;
	}
	
	public void initViewTagList(boolean isAll, String panelNum){

		Intent intent = null;
		
	/***********第0屏幕*****************/
		if(isAll || panelNum.equals(PANEL_TYPE_0)){
			ViewInfo tagInfo = new ViewInfo();
			tagInfo.viewId = "lockscreen";
			tagInfo.panelNum = UiManager.PANEL_TYPE_0;
			tagInfo.cellNum = "1";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_LOCK_SCREEN);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "锁屏";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_power_down);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "changyong";
			tagInfo.panelNum = UiManager.PANEL_TYPE_0;
			tagInfo.cellNum = "3";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_PANEL_SWITCH);
			intent.putExtra("panelNum", UiManager.PANEL_TYPE_2);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "常用";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_star);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "shebei";
			tagInfo.panelNum = UiManager.PANEL_TYPE_0;
			tagInfo.cellNum = "5";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_PANEL_SWITCH);
			intent.putExtra("panelNum", UiManager.PANEL_TYPE_1);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "设备";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_phone);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "zhupingmu";
			tagInfo.panelNum = UiManager.PANEL_TYPE_0;
			tagInfo.cellNum = "7";
			intent = new Intent(Intent.ACTION_MAIN)
	        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)
	        .addCategory(Intent.CATEGORY_HOME);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "主屏幕";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_home);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			// 第0屏幕
			tagInfo = new ViewInfo();
			tagInfo.viewId = "ContorCenter";
			tagInfo.panelNum = UiManager.PANEL_TYPE_0;
			tagInfo.cellNum = "4";
			intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(new ComponentName(mContext.getPackageName(), "com.niunet.assistivetouch.TouchLogoActivity"));  
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "控制中心";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_center);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
		
			tagInfo.viewId = "caidanjian";
			tagInfo.panelNum = UiManager.PANEL_TYPE_0;
			tagInfo.cellNum = "6";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_KEYCODE_MENU);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "菜单键";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_menu_key);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "fanhuijian";
			tagInfo.panelNum = UiManager.PANEL_TYPE_0;
			tagInfo.cellNum = "8";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_KEYCODE_BACK);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "返回键";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_back_key);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
		}
		
		/***********第1屏幕*****************/
		if(isAll || panelNum.equals(PANEL_TYPE_1)){
		
			ViewInfo tagInfo = new ViewInfo();
			tagInfo.viewId = "SwitchPanel_1";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "4";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_PANEL_SWITCH);
			intent.putExtra("panelNum", UiManager.PANEL_TYPE_0);
			tagInfo.intentUri = intent.toURI();//跳转事件，跳转到1号面板上
			tagInfo.label = "";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_back);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();//手电筒
			tagInfo.viewId = "ShouDianTong";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "6";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_FLASH_LIGHT);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "手电筒";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_flashlight);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "YiDongMengWang";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "2";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_MOBILE_NETWORK);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "移动网络";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_mobile_network);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();//屏幕亮度
			tagInfo.viewId = "ScreenLightness_add";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "1";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_SCREEN_BRIGHTNESS_ADD);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "屏幕亮度(+)";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_screen_lightness);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();//屏幕亮度
			tagInfo.viewId = "ScreenLightness_low";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "5";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_SCREEN_BRIGHTNESS_BELOW);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "屏幕亮度(-)";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_screen_lightness);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "MeiTiYinLiang_add";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "3";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_MUSIC_VOLUME_ADD);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "媒体音量(+)";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_ringer_normal);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "MeiTiYinLiang_low";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "7";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_MUSIC_VOLUME_BELOW);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "媒体音量(-)";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_ringer_normal);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "wifi";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "0";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_WIFI_ON_OFF);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "wifi";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_wifi);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "More_1";
			tagInfo.panelNum = UiManager.PANEL_TYPE_1;
			tagInfo.cellNum = "8";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_PANEL_SWITCH);
			intent.putExtra("panelNum", UiManager.PANEL_TYPE_3);
			tagInfo.intentUri = intent.toURI();//跳转事件，跳转到1号面板上
			tagInfo.label = "更多...";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_more);
			mViewTagList.put(tagInfo.viewId, tagInfo);

		}
		if(isAll || panelNum.equals(PANEL_TYPE_2)){
			/***********第2屏幕*****************/
			ViewInfo tagInfo = new ViewInfo();
			tagInfo.viewId = "SwitchPanel_2";
			tagInfo.panelNum = UiManager.PANEL_TYPE_2;
			tagInfo.cellNum = "4";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_PANEL_SWITCH);
			intent.putExtra("panelNum", UiManager.PANEL_TYPE_0);
			tagInfo.intentUri = intent.toURI();//跳转事件，跳转到0号面板上
			tagInfo.label = "";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_back);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
		}
		if(isAll || panelNum.equals(PANEL_TYPE_3)){
			
			ViewInfo tagInfo = new ViewInfo();
			tagInfo.viewId = "SwitchPanel_3";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "4";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_PANEL_SWITCH);
			intent.putExtra("panelNum", UiManager.PANEL_TYPE_1);
			tagInfo.intentUri = intent.toURI();//跳转事件，跳转到1号面板上
			tagInfo.label = "";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_back);
			mViewTagList.put(tagInfo.viewId, tagInfo);  
			
			//更多面板，第3面板
			tagInfo = new ViewInfo();
			tagInfo.viewId = "LanYa";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "3";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_BLUETOOTH_ON_OFF);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "蓝牙";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_bluetooth);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "Gps";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "5";
			intent = new Intent();
			ComponentName comp = new ComponentName("com.android.settings",
					"com.android.settings.Settings$LocationSettingsActivity");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.setAction("android.settings.LOCATION_SOURCE_SETTINGS");
			intent.setComponent(comp);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "GPS";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_gps);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "AutoOrientation";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "1";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_AUTOUTO_ROTATION);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "自动旋转";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_auto_orientation);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "Airplane";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "7";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_AIR_PLANE);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "飞行模式";
			tagInfo.icon = getDrawable(R.drawable.action_point_toolbox_airplane);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "RingShengYinLiang_add";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "2";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_RING_VOLUME_ADD);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "铃声音量(+)";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_ringer_normal);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "LingShengYinLiang_low";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "0";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_RING_VOLUME_BELOW);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "铃声音量(-)";  
			tagInfo.icon = getDrawable(R.drawable.selector_ic_ringer_normal);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "NaoZhongYinLiang_add";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "8";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_ALARM_VOLUME_ADD);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "闹钟音量(+)";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_ringer_normal);
			mViewTagList.put(tagInfo.viewId, tagInfo);
			
			tagInfo = new ViewInfo();
			tagInfo.viewId = "NaoZhongYinLiang_low";
			tagInfo.panelNum = UiManager.PANEL_TYPE_3;
			tagInfo.cellNum = "6";
			intent = new Intent();
			intent.setAction(UiManager.EVENT_ALARM_VOLUME_BELOW);
			tagInfo.intentUri = intent.toURI();
			tagInfo.label = "闹钟音量(-)";
			tagInfo.icon = getDrawable(R.drawable.selector_ic_ringer_normal);
			mViewTagList.put(tagInfo.viewId, tagInfo);
		}
	}
	
	public Drawable getDrawable(int drawableId){
		return mContext.getResources().getDrawable(drawableId);
	}
	
	/**
	 * 绑定到panelNum上
	 * @param panelNum
	 */
	public void bindItemsToPanel(BindItemsCallback bindItemsCallback, String panelNum, DatabaseUtil databaseUtil){
		
//		ArrayList<ViewInfo> panelNumItemsList = databaseUtil.getTagInfoList(panelNum);//获取保存在数据库中的常用的快捷方式
		
		/*Map<String, ViewInfo> tagList = getTagList(false, panelNum);
		
		Set<String> keys = tagList.keySet();
		 for(Iterator<String> it = keys.iterator(); it.hasNext();){
			String key = it.next();
			ViewInfo info = tagList.get(key);
			
			if(panelNum.equals(info.panelNum)){
				boolean isExist = false;
				for(ViewInfo panelNumItem : panelNumItemsList){
					if(//panelNumItem.cellNum.equals(info.cellNum) ||  //说明此位置已经存在在数据库中配置的按钮
							panelNumItem.intentUri.equals(info.intentUri)){ //说明某个按钮已经存在在数据库中某个面板的某个位置上
						isExist = true;
						break;
					}
				}
				if(isExist == false)
					panelNumItemsList.add(info);
			}
		 }*/
		
		if(bindItemsCallback != null)
			bindItemsCallback.bindItems(panelNum, databaseUtil.getTagInfoList(panelNum));
		
//		panelNumItemsList.clear();
//		panelNumItemsList = null;
	}
	
	public void addItemToPanel(AddItemCallback addItemCallback, ViewInfo viewInfo, DatabaseUtil databaseUtil){
		
		databaseUtil.insertNewTagInfo(viewInfo);//插入数据库
		if(addItemCallback != null)
			addItemCallback.addItem(viewInfo);
	}
	
	public static byte[] getIconByteArray(Bitmap bitmap){
		if(bitmap == null)
			return null;
		
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);    
		
		return os.toByteArray();
	}
	
	public static Bitmap createIconBitmap(Drawable icon, Context context) {
		
		 if(icon == null)
		   	return null;
		    
         final Bitmap bitmap = Bitmap.createBitmap(tarIconWidth, tarIconHeight,
                 Bitmap.Config.ARGB_8888);
         final Canvas canvas = new Canvas();
         canvas.setBitmap(bitmap);
         /*Drawable bg = context.getResources().getDrawable(Rs.drawable.icon_background);
         bg.setBounds(0, 0, tarIconWidth, tarIconHeight);
         bg.draw(canvas);*/
         icon.setBounds(0, 0, tarIconWidth, tarIconHeight);
         icon.draw(canvas);
         
         return bitmap;
	 }
	 
	/*public static Bitmap convertDrawable2BitmapByCanvas(Drawable drawable) {
		Log.d("tag", "insertNewTagInfo drawable.getIntrinsicWidth()="+drawable.getIntrinsicWidth()+
				",drawable.getIntrinsicHeight()="+drawable.getIntrinsicHeight());
		Bitmap bitmap = Bitmap.createBitmap(
		drawable.getIntrinsicWidth(),
		drawable.getIntrinsicHeight(),
		Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
		drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}*/
	
	// byte[] → Bitmap
	public static Bitmap convertBytes2Bimap(byte[] b) {
		if (b.length == 0) 
			return null;
	
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}
	
}
