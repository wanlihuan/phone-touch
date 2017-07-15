package com.niunet.assistivetouch;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.assistivetouch.widget.AirPlane;
import com.assistivetouch.widget.AutoRotationStateTracker;
import com.assistivetouch.widget.BluetoothStateTracker;
import com.assistivetouch.widget.Flashlight;
import com.assistivetouch.widget.MobileNetwork;
import com.assistivetouch.widget.SceneModeSwitch;
import com.assistivetouch.widget.ScreenBrightness;
import com.assistivetouch.widget.SystemKeyEvent;
import com.assistivetouch.widget.VolumeAdjust;
import com.assistivetouch.widget.WifiStateTracker;
import com.assistivetouch.widget.Workspace;
import com.assistivetouch.widget.Workspace.OnClickItemListener;
import com.android.assistivetouch.R;
import com.niunet.assistivetouch.DatabaseUtil;
import com.niunet.assistivetouch.CustomDialog.OnSelectedItemsListener;
import com.niunet.assistivetouch.UiManager.AddItemCallback;
import com.niunet.assistivetouch.UiManager.BindItemsCallback;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TouchPanelView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener{
	private final int maxProgress = 7;
	private final int duration = 300;
	
	public static final int MODE_NORMAL = 0;
	/**
	 * 点击设置中的自定义操纵面板会置此标志
	 */
	public static final int MODE_EDIT = 1;
	private int activityMode = MODE_NORMAL;
	
	private Workspace mWorkspace;
	private String mCurrentPanelNum = null;//"0" "1" "2"
	private LayoutInflater mInflater;
	private DatabaseUtil mDatabaseUtil;
	private UiManager mUiManager;
	
	//系统按键事件
	private SystemKeyEvent mSystemKeyEvent;
	
	//屏幕亮度
//	private ScreenBrightness mScreenBrightness;
	//手电筒
	private Flashlight mFlashlight;
	
	//媒体音量调节
	private VolumeAdjust mVolumeMusicAdjust;
	
	//铃声音量调节
	private VolumeAdjust mVolumeRingAdjust;
	
	//闹钟音量调节
	private VolumeAdjust  mVolumeAlarmAdjust;
	
	//情景模式
	private SceneModeSwitch mSceneModeSwitch;
	
	//移动梦网
	private MobileNetwork mMobileNetwork;
	
	//wifi
	private WifiStateTracker mWifiStateTracker;
	
	 //蓝牙
	private BluetoothStateTracker mBluetoothStateTracker;
	
	//自动旋转
	private AutoRotationStateTracker mAutoRotationStateTracker = null;
	
	//飞行模式开关
	private AirPlane mAirPlane = null;
	
	private final int HANDLER_CHANG_YONG = 0;
	private final int HANDLER_LOCK_SCREEN = 1;
	private final int HANDLER_CLOSE_PANEL = 2;
	
	private Context mContext;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case HANDLER_CHANG_YONG:
					String cellNum = ""+msg.arg1;
					Intent starintent = new Intent(mContext, TempActivity.class);
					starintent.putExtra("panelNum", mCurrentPanelNum);
					starintent.putExtra("cellNum", cellNum);
					starintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(starintent);
					
				break;
				
				case HANDLER_LOCK_SCREEN:
					Intent acintent = new Intent(mContext, LockScreenActivity.class);
					acintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(acintent);
					break;
				case HANDLER_CLOSE_PANEL:
					Intent closeIntent = new Intent("android.touch.action.TOUCH_PANEL_CLOSE");
					mContext.sendBroadcast(closeIntent);
					break;
			}
		}
	};
	
	public TouchPanelView(Context context, int mode) {
		this(context, null, mode);
		// TODO Auto-generated constructor stub
		 
	}
	
	public TouchPanelView(Context context) {
		this(context, 0);
		// TODO Auto-generated constructor stub
		
	}

	public TouchPanelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0, 0);
		// TODO Auto-generated constructor stub
	}
	public TouchPanelView(Context context, AttributeSet attrs, int mode) {
		this(context, attrs, 0, mode);
		// TODO Auto-generated constructor stub
	}

	public TouchPanelView(Context context, AttributeSet attrs, int defStyle, int mode) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mInflater.inflate(R.layout.a3, this, true);
		
		activityMode = mode;
        mDatabaseUtil = new DatabaseUtil(mContext);  
        mUiManager = new UiManager(mContext);
        
        mWorkspace = (Workspace) findViewById(R.id.workspace);
        
    	/*if(!updatePanelIcon())*/
        //	setPanelBackgroundDrawable(getResources().getDrawable(R.drawable.default_panel_p));
    		
    	CustomDialog mCustomDialog = null;
    	if(activityMode == MODE_EDIT)
	        mCustomDialog = new CustomDialog(mContext);
	        
    	 final CustomDialog customDialog = mCustomDialog;
    	 mCustomDialog = null;
    	 
        mWorkspace.setOnClickItemListener(new OnClickItemListener(){
			public void onClickItem(ViewGroup parentView, String cellNum) {
				// TODO Auto-generated method stub
				final ViewInfo viewInfo = new ViewInfo();
				
				viewInfo.panelNum = mCurrentPanelNum;
				viewInfo.cellNum = cellNum;
				
				if(mCurrentPanelNum.equals(UiManager.PANEL_TYPE_2)){
					
					if(activityMode == MODE_EDIT){
						
						viewInfo.viewId = "shortcut"+cellNum;
						customDialog.show();
						customDialog.setItemBackground(getResources().getDrawable(R.drawable.btn_default_normal_disable_focused));
						
						customDialog.loadingAllApp();
						
						customDialog.setOnSelectedItemsListener(new OnSelectedItemsListener(){
							public void onSelectedItems(Map<String,Integer> selectedMapPo) {
								// TODO Auto-generated method stub
								
							}
							@SuppressWarnings("deprecation")
							public void onSelectedItem(int position) {
								// TODO Auto-generated method stub
								
								viewInfo.intentUri = customDialog.mAppList.get(position).intentUri;
								
								viewInfo.icon = customDialog.mAppList.get(position).icon;
								viewInfo.label = customDialog.mAppList.get(position).label;
								
								mUiManager.addItemToPanel(new AddItemCallback(){
									public void addItem(ViewInfo viewInfo) {
										// TODO Auto-generated method stub
										
										creatShortcut(viewInfo);
									}
								}
								,viewInfo
								,mDatabaseUtil);
							}
						});
				     }else{
				    	 mContext.sendBroadcast(new Intent("android.touch.action.TOUCH_PANEL_CLOSE"));
				    	
				    	 Message msg = new Message();
				    	 msg.arg1 = Integer.parseInt(cellNum);
						 mHandler.removeMessages(HANDLER_CHANG_YONG);
						 mHandler.sendMessageDelayed(msg, 400);
				     }
					
				}else{
					if(activityMode == MODE_EDIT){
						customDialog.show();
						customDialog.setTitle("选择功能按钮");
						customDialog.setItemBackground(null);
						ArrayList<ViewInfo> adapterList = new ArrayList<ViewInfo>(); 
						
						Set<String> keys = UiManager.mViewTagList.keySet();
						 for(Iterator<String> it = keys.iterator(); it.hasNext();){
							String key = it.next();
							
							ViewInfo info = UiManager.mViewTagList.get(key);
							try {
								Intent intent = Intent.parseUri(info.intentUri, 0);
								if(!intent.getAction().equals(UiManager.EVENT_PANEL_SWITCH))  
									adapterList.add(info);
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						 }
						
						customDialog.setAdapterList(adapterList);
						
						customDialog.setOnSelectedItemsListener(new OnSelectedItemsListener(){
							public void onSelectedItems(Map<String,Integer> selectedMapPo) {
								// TODO Auto-generated method stub
								
							}
							public void onSelectedItem(int position) {
								// TODO Auto-generated method stub
								
								viewInfo.viewId = customDialog.mAppList.get(position).viewId;
								viewInfo.intentUri = customDialog.mAppList.get(position).intentUri;
								
								viewInfo.icon = null;//不保存功能按钮图片到数据库中
								viewInfo.label = customDialog.mAppList.get(position).label;
								
								mUiManager.addItemToPanel(new AddItemCallback(){
									public void addItem(ViewInfo viewInfo) {
										// TODO Auto-generated method stub
										//使用res中的图片资源
										viewInfo.icon = UiManager.mViewTagList.get(viewInfo.viewId).icon;
										creatShortcut(viewInfo);
									}
								}
								,viewInfo
								,mDatabaseUtil);
							}
						});
				   }
			  }
			}
        });
        
      //初始绑定的面板
        bindItemsToPanel(UiManager.PANEL_TYPE_0);
	}

	public void setPanelBackgroundDrawable(Drawable drawable){
		mWorkspace.setBackgroundForPanel(drawable);
		
	}
	
	public void setPanelBackgroundBitmap(Bitmap bitmap){
		if(bitmap == null)
			mWorkspace.setBackgroundForPanel(mContext.getResources().getDrawable(R.drawable.default_panel_p));
		else
			mWorkspace.setBitmapBackgroundForPanel(bitmap);
		
	}

	public void setPanelBackgroundBitmap1(Bitmap bitmap){
		if(bitmap == null)
			mWorkspace.setBackgroundForPanel(mContext.getResources().getDrawable(R.drawable.btn_assistivetouch));
		else
			mWorkspace.setBitmapBackgroundForPanel(bitmap);

	}

	public void startPanelAnimation(Animation animation){
		
		mWorkspace.startAnimation(animation);
	}
	
	public interface AnimationCallbacks {
		
        public void animationEnd();
	}

	public void startPanelEnterAnimation(final AnimationCallbacks animationCallbacks, final int animResId){
		Animation mAnimation = AnimationUtils.loadAnimation(mContext, animResId);
		mWorkspace.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener(){

			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				if(animationCallbacks != null)
					animationCallbacks.animationEnd();
			}

			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		/*mWorkspace.startSwitchExitTranslateAnimation(new ExitAnimationCallbacks(){//开始icon切换结束动画
			public void exitAnimationEnd() {
				// TODO Auto-generated method stub
				
			}
		});*/
	}
	
	public void startPanelExitAnimation(final AnimationCallbacks animationCallbacks, final int animResId){
		
		Animation mAnimation = AnimationUtils.loadAnimation(mContext, animResId);
		mAnimation.setFillAfter(false);
		mWorkspace.startAnimation(mAnimation);
		mAnimation.setAnimationListener(new AnimationListener(){

			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				if(animationCallbacks != null)
					animationCallbacks.animationEnd();
			}

			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		/*mWorkspace.startSwitchExitTranslateAnimation(new ExitAnimationCallbacks(){//开始icon切换结束动画
			public void exitAnimationEnd() {
				// TODO Auto-generated method stub
				
				Animation mAnimation = AnimationUtils.loadAnimation(mContext, animResId);
				mAnimation.setFillAfter(false);
				mWorkspace.startAnimation(mAnimation);
				mAnimation.setAnimationListener(new AnimationListener(){

					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						if(animationCallbacks != null)
							animationCallbacks.animationEnd();
					}

					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}

					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});*/
		
	}
	
	/*public void startPanelAnimation(Animation animation){
		
		mWorkspace.startAnimation(animation);
	}*/

	/**
	 * 开关呼吸灯模式
	 * @param openFlag
	 */
	public void setFlashingLightning(boolean openFlag){
		mWorkspace.setFlashingLightning(openFlag);
	}
	
	public boolean onLongClick(View view) {
		// TODO Auto-generated method stub
		Object tag = view.getTag();
		Intent intent = null;
		try {
			intent = Intent.parseUri(((ViewInfo) tag).intentUri, 0);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tag instanceof ViewInfo){
			if((activityMode == MODE_EDIT || ((ViewInfo) tag).panelNum.equals(UiManager.PANEL_TYPE_2)) && 
					!intent.getAction().equals(UiManager.EVENT_PANEL_SWITCH)){//添加快捷方式的面板
				
				mWorkspace.removeChildView(Integer.parseInt(((ViewInfo) tag).cellNum), view);
				mWorkspace.setBackgroundVisibilityForNullSpace(Integer.parseInt(((ViewInfo) tag).cellNum), true);
				String deletedData_ID = mDatabaseUtil.queryDb_ID(((ViewInfo) tag).panelNum, ((ViewInfo) tag).cellNum);
				mDatabaseUtil.deleteDbData(deletedData_ID);
				
			}else{
				if(activityMode == MODE_EDIT)
					Toast.makeText(mContext, "跳转功能类按钮不可删除！", Toast.LENGTH_LONG).show();
				
				if(intent.getFlags() == (Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP)){//长安HOME键，显示近期任务
					Log.d("tag", "onLongClick 显示近期任务 ");
					//mHandler.removeMessages(HANDLER_CLOSE_PANEL);
					//mHandler.sendEmptyMessageDelayed(HANDLER_CLOSE_PANEL, 300);
					
					mContext.sendBroadcast(new Intent("android.touch.action.OPEN_RECENT_TASK"));
					
					//Toast.makeText(mContext, "近期开放！", Toast.LENGTH_SHORT).show();  
				}
			}
		}
		return true;//禁止下传事件
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Object tag = v.getTag();
		if(tag instanceof ViewInfo){
			startEventAndUpdateUi(tag);
		}
	}
	
	/**
	 * 设置模式，主要是自定义编辑模式、正常操作模式
	 * @param mode
	 */
	public void setMode(int mode){
		activityMode = mode;
	}
	
	/*private boolean updatePanelIcon(){
    	boolean isOk = false;
    	SharedPreferences preferences = mContext.getSharedPreferences("ThemeConfig", Context.MODE_WORLD_READABLE);
    	final String pIconId = preferences.getString("PIconId", "default");
    	if(pIconId.equals("default"))
    		return isOk;
    	
    	final ThemeManager themeManager = new ThemeManager(mContext);
    	MyTheme mMyTheme = new MyTheme(mContext);
		final String[] myThemeIdArray = mMyTheme.getConfig(ThemeManager.MYTHEME_PANEL_CONFIG_FILE_NAME);
		if(null != myThemeIdArray){
			for(int j = 0; j < myThemeIdArray.length; j++){//查询我的主题配置信息
				if(myThemeIdArray[j].equals(pIconId)){
					 mWorkspace.setBackgroundDrawable(getResources().getDrawable(themeManager.getPanelDrawableId(pIconId)));
					 
					isOk = true;
					break;
				}
			}
		}
		
		return isOk;
    }*/
	
	protected void onDestroy() {
		
		try{
			mDatabaseUtil.recycleCacheBitmap();
			mWifiStateTracker.unregisterReceiver();
			mAutoRotationStateTracker.unregisterContentObserver();
			mBluetoothStateTracker.unregisterReceiver();
			mAirPlane.unregisterReceiver();
		}catch(Exception e){
			
		}
		
		mWorkspace.removeAllViews();
		mWorkspace = null;
	}
	
	private Intent startIntent;
	/**
	 * 启动事件
	 * @param intent
	 */
	private void startEventAndUpdateUi(Object tag){
		
		Intent intent = null;
		try {
			intent = Intent.parseUri(((ViewInfo) tag).intentUri, 0); 
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(intent.getAction().equals(UiManager.EVENT_AIR_PLANE)){//飞行模式
			mAirPlane.AirPlaneOnAndOff();
			
		}else if(intent.getAction().equals(UiManager.EVENT_LOCK_SCREEN)){//锁屏
			Intent closeIntent = new Intent("android.touch.action.TOUCH_PANEL_CLOSE");
			mContext.sendBroadcast(closeIntent);
			
			//锁屏
			mHandler.removeMessages(HANDLER_LOCK_SCREEN);
			mHandler.sendEmptyMessageDelayed(HANDLER_LOCK_SCREEN, 300);
		}else if(intent.getAction().equals(UiManager.EVENT_KEYCODE_MENU)){//菜单
			mHandler.removeMessages(HANDLER_CLOSE_PANEL);
			mHandler.sendEmptyMessageDelayed(HANDLER_CLOSE_PANEL, 800);
			
			mSystemKeyEvent.sendKey(KeyEvent.KEYCODE_MENU);
			
		}else if(intent.getAction().equals(UiManager.EVENT_KEYCODE_BACK)){//返回
		//	Intent closeIntent = new Intent("android.touch.action.TOUCH_PANEL_CLOSE");
		//	mContext.sendBroadcast(closeIntent);
			
			mSystemKeyEvent.sendKey(KeyEvent.KEYCODE_BACK);
			
			
		}else if(intent.getAction().equals(UiManager.EVENT_FLASH_LIGHT)){//手电筒
			
			mFlashlight.onOrOffCamera();
			mFlashlight.updateUiShow();//更新UI
			
		}else if(intent.getAction().equals(UiManager.EVENT_WIFI_ON_OFF)){//wifi
		    mWifiStateTracker.toggleState(mContext);
		    mWifiStateTracker.setImageViewResources(mContext);
		    
		}else if(intent.getAction().equals(UiManager.EVENT_BLUETOOTH_ON_OFF)){//蓝牙
			mBluetoothStateTracker.toggleState(mContext);
			mBluetoothStateTracker.setImageViewResources(mContext);
		}
		else if(intent.getAction().equals(UiManager.EVENT_MOBILE_NETWORK)){//移动网
			
			mMobileNetwork.openCloseNetWork();
			mMobileNetwork.updateUiShow();//更新UI
			
		}else if(intent.getAction().equals(UiManager.EVENT_SCENE_MODE_SWITCH)){//情景模式
			//更新UI
			mSceneModeSwitch.switchProfile();
			mSceneModeSwitch.updateUiShow();
			
		}else if(intent.getAction().equals(UiManager.EVENT_MUSIC_VOLUME_ADD) ||
				intent.getAction().equals(UiManager.EVENT_MUSIC_VOLUME_BELOW)){//媒体音量增加/减少
			
			int volumeAdjustProgress = 0;
			if(intent.getAction().equals(UiManager.EVENT_MUSIC_VOLUME_ADD))
				volumeAdjustProgress = 1;
			else if(intent.getAction().equals(UiManager.EVENT_MUSIC_VOLUME_BELOW))
				volumeAdjustProgress = -1;
			
			if(mVolumeMusicAdjust == null){
				mVolumeMusicAdjust = new VolumeAdjust(mContext, "多媒体", duration, AudioManager.STREAM_MUSIC);
				mVolumeMusicAdjust.setMaxProgress(maxProgress);
				
				int currentProgress = mVolumeMusicAdjust.volume2progress(mVolumeMusicAdjust.getCurrentStreamVolume());
				mVolumeMusicAdjust.updateAndshowProgressBar(currentProgress + volumeAdjustProgress);
			}else{
				
				mVolumeMusicAdjust.updateAndshowProgressBar(mVolumeMusicAdjust.getCurrentProgress() + volumeAdjustProgress);
				int currentValue = mVolumeMusicAdjust.progress2volume(mVolumeMusicAdjust.getCurrentProgress());
				mVolumeMusicAdjust.updateVolume(currentValue);
			}
			
		}else if(intent.getAction().equals(UiManager.EVENT_RING_VOLUME_ADD) ||
				intent.getAction().equals(UiManager.EVENT_RING_VOLUME_BELOW)){//铃声音量加/减少
			
			int volumeAdjustProgress = 0;
			if(intent.getAction().equals(UiManager.EVENT_RING_VOLUME_ADD))
				volumeAdjustProgress = 1;
			else if(intent.getAction().equals(UiManager.EVENT_RING_VOLUME_BELOW))
				volumeAdjustProgress = -1;
			
			if(mVolumeRingAdjust == null){
				mVolumeRingAdjust = new VolumeAdjust(mContext, "铃声", duration, AudioManager.STREAM_RING);
				mVolumeRingAdjust.setMaxProgress(maxProgress);
				
				int currentProgress = mVolumeRingAdjust.volume2progress(mVolumeRingAdjust.getCurrentStreamVolume());
				mVolumeRingAdjust.updateAndshowProgressBar(currentProgress + volumeAdjustProgress);
			}else{
				
				mVolumeRingAdjust.updateAndshowProgressBar(mVolumeRingAdjust.getCurrentProgress() + volumeAdjustProgress);
				int currentValue = mVolumeRingAdjust.progress2volume(mVolumeRingAdjust.getCurrentProgress());
				mVolumeRingAdjust.updateVolume(currentValue);
			}
			
		}else if(intent.getAction().equals(UiManager.EVENT_ALARM_VOLUME_ADD) ||
				intent.getAction().equals(UiManager.EVENT_ALARM_VOLUME_BELOW)){    //闹钟音量加 / 减少调节
			
			int volumeAdjustProgress = 0;
			if(intent.getAction().equals(UiManager.EVENT_ALARM_VOLUME_ADD))
				volumeAdjustProgress = 1;
			else if(intent.getAction().equals(UiManager.EVENT_ALARM_VOLUME_BELOW))
				volumeAdjustProgress = -1;
			
			if(mVolumeAlarmAdjust == null){
				mVolumeAlarmAdjust = new VolumeAdjust(mContext, "闹钟", duration, AudioManager.STREAM_ALARM);
				mVolumeAlarmAdjust.setMaxProgress(maxProgress);
				
				int currentProgress = mVolumeAlarmAdjust.volume2progress(mVolumeAlarmAdjust.getCurrentStreamVolume());
				mVolumeAlarmAdjust.updateAndshowProgressBar(currentProgress + volumeAdjustProgress);
			}else{
				
				mVolumeAlarmAdjust.updateAndshowProgressBar(mVolumeAlarmAdjust.getCurrentProgress() + volumeAdjustProgress);
				int currentValue = mVolumeAlarmAdjust.progress2volume(mVolumeAlarmAdjust.getCurrentProgress());
				mVolumeAlarmAdjust.updateVolume(currentValue);
			}
			
		} else if(intent.getAction().equals(UiManager.EVENT_SCREEN_BRIGHTNESS_ADD)){//增加屏幕亮度
			/*if(mScreenBrightness == null){
				mScreenBrightness = new ScreenBrightness(mContext, "屏幕亮度", duration);
				mScreenBrightness.setMaxProgress(maxProgress);
			}
			mScreenBrightness.addScreenBrightness();
			mScreenBrightness.updateAndshowProgressBar(mScreenBrightness.currentBrightness2Level());*/
			if(ScreenBrightness.isActivity == false){
				Intent starintent = new Intent(mContext, ScreenBrightness.class);
				starintent.putExtra("maxProgress", maxProgress);
				starintent.putExtra("AdjustAction", true);
				starintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(starintent);
			}else{
				Intent adjestIntnet = new Intent("android.touch.action.SCREEN_BRIGHTNESS_ADD");
				mContext.sendBroadcast(adjestIntnet);
			}
		}else if(intent.getAction().equals(UiManager.EVENT_SCREEN_BRIGHTNESS_BELOW)){//降低屏幕亮度
			/*if(mScreenBrightness == null){
				mScreenBrightness = new ScreenBrightness(mContext, "屏幕亮度", duration);
				mScreenBrightness.setMaxProgress(maxProgress);
			}
			mScreenBrightness.belowScreenBrightness();
			mScreenBrightness.updateAndshowProgressBar(mScreenBrightness.currentBrightness2Level());*/
			if(ScreenBrightness.isActivity == false){
				Intent starintent = new Intent(mContext, ScreenBrightness.class);
				starintent.putExtra("maxProgress", maxProgress);
				starintent.putExtra("AdjustAction", false);
				starintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(starintent);
			}else{
				Intent adjestIntnet = new Intent("android.touch.action.SCREEN_BRIGHTNESS_BELOW");
				mContext.sendBroadcast(adjestIntnet);
			}
			
		} else if(intent.getAction().equals(UiManager.EVENT_AUTOUTO_ROTATION)){    //自动旋转
			mAutoRotationStateTracker.toggleState(mContext);
			
		} else if(intent.getAction().equals(UiManager.EVENT_PANEL_SWITCH)){     //面板的跳转,跳转到intent.getStringExtra("panelNum")版面中
				final String bindPanelNum =  intent.getStringExtra("panelNum");
			//	mWorkspace.startSwitchExitAlphaAnimation();
				/*mWorkspace.startSwitchExitTranslateAnimation(new ExitAnimationCallbacks(){//开始切换结束动画
					public void exitAnimationEnd() {
						// TODO Auto-generated method stub
						
						bindItemsToPanel(bindPanelNum);
						
					//	mWorkspace.startSwitchEnterAlphaAnimation();//开始显示动画
						mWorkspace.startSwitchEnterTranslateAnimation(null); 
					}
				});*/
				(new Handler()).post(new Runnable(){
					public void run() {
						// TODO Auto-generated method stub
						bindItemsToPanel(bindPanelNum);
					}
				});
				mWorkspace.startSwitchEnterTranslateAnimation(null);
		}else if(intent.getAction().equals(Intent.ACTION_MAIN) ||//启动快捷方式
				intent.getAction().equals("android.settings.LOCATION_SOURCE_SETTINGS")){//回到主屏幕
			
			startIntent = intent;
			
			if(startActivitySafely(startIntent)){
			
				mHandler.removeMessages(HANDLER_CLOSE_PANEL);
				mHandler.sendEmptyMessageDelayed(HANDLER_CLOSE_PANEL, 500);
			}
		}
	}
	
	private boolean startActivitySafely(Intent intent) {
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			mContext.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mContext, R.string.activity_not_found,
					Toast.LENGTH_SHORT).show();
			return false;
		} catch (SecurityException e) {
			Toast.makeText(mContext, R.string.activity_not_found,
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	public String getCurrentPanelNum(){
		return mCurrentPanelNum;
	}
	/**
	 * 创建快捷方式并绑定到指定的面板上绑定
	 * @param panelNum 被指定绑定的面板
	 */
	public void bindItemsToPanel(final String panelNum){
		mCurrentPanelNum = panelNum;
		mUiManager.bindItemsToPanel(new BindItemsCallback (){
			public void bindItems(String panelNum, ArrayList<ViewInfo> itemInfo) {  
				// TODO Auto-generated method stub
				if(activityMode == MODE_EDIT){//编辑模式
					mWorkspace.setBackgroundVisibilityForNullSpace(true);//显示方格子
					
				}else{
					if(panelNum.equals(UiManager.PANEL_TYPE_2))//常用面板空时显示方格子背景
						mWorkspace.setBackgroundVisibilityForNullSpace(true);
					else
						mWorkspace.setBackgroundVisibilityForNullSpace(false);
				}
				creatShortcut(itemInfo);
			}
        }, panelNum, mDatabaseUtil);
	}
	
	/**
	 * 显示时创建一面板快捷方式
	 * @param itemInfoList
	 */
	private void creatShortcut(ArrayList<ViewInfo> itemInfoList){
		mWorkspace.clearWorkspace();
		if(itemInfoList == null || itemInfoList.size() == 0){
			return;
		}
		
		for(int i = 0; i < itemInfoList.size();i++){
			
			ViewInfo itemInfo = itemInfoList.get(i);
			
			mWorkspace.addChildView(getShortcut(itemInfo), Integer.parseInt(itemInfo.cellNum)); 
			itemInfo = null;
		}
		
		mWorkspace.requestLayout();
	}
	
	/**
	 * 创建单个的快捷方式
	 * @param itemInfo
	 */
	private void creatShortcut(ViewInfo itemInfo){
		
		mWorkspace.addChildView(getShortcut(itemInfo), Integer.parseInt(itemInfo.cellNum));
		mWorkspace.requestLayout();
	}
	
	private TextView getShortcut(ViewInfo viewInfo){
		
		TextView textView = (TextView) mInflater.inflate(R.layout.item_text_view, null);
		
		textView.setTag(viewInfo);
		textView.setOnClickListener(this);
		textView.setOnLongClickListener(this);
		
		if(viewInfo.label.equals("")){
			
			textView.setCompoundDrawablesWithIntrinsicBounds(viewInfo.icon, null, null, null);
			textView.setText(null);
			
		}else{
			
			textView.setTextSize(12.0f);
			textView.setText(viewInfo.label);
			textView.setCompoundDrawablesWithIntrinsicBounds(null, viewInfo.icon, null, null);
			
			//初始化显示
			try {
				initUiShow(textView, Intent.parseUri(viewInfo.intentUri, 0));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return textView;
	}
	
	
	/**
	 * 初始化界面显示
	 * @param view
	 */
	private void initUiShow(View view, Intent intent){
	//	Object tag = view.getTag();
        
		if(intent.getAction().equals(UiManager.EVENT_SCREEN_BRIGHTNESS_ADD) || 
				intent.getAction().equals(UiManager.EVENT_SCREEN_BRIGHTNESS_BELOW)){//屏幕亮度
			/*if(mScreenBrightness == null)
				mScreenBrightness = new ScreenBrightness(this);*/
			
		}else if(intent.getAction().equals(UiManager.EVENT_KEYCODE_MENU) ||
				intent.getAction().equals(UiManager.EVENT_KEYCODE_BACK)){
			
			if(mSystemKeyEvent == null)
				mSystemKeyEvent = new SystemKeyEvent(mContext);
			
	   }else if(intent.getAction().equals(UiManager.EVENT_FLASH_LIGHT)){
			//手电筒
			if(mFlashlight == null)
	        mFlashlight = new Flashlight(mContext);
			mFlashlight.setView((TextView)view);
			mFlashlight.updateUiShow();
			
		}else if(intent.getAction().equals(UiManager.EVENT_AUTOUTO_ROTATION)){
			//自动旋转
			if(mAutoRotationStateTracker == null)
	        mAutoRotationStateTracker = new AutoRotationStateTracker(mContext);
			mAutoRotationStateTracker.setView((TextView)view);
			mAutoRotationStateTracker.updateUiShow();
			
		}else if(intent.getAction().equals(UiManager.EVENT_SCENE_MODE_SWITCH)){//情景模式
			 
			if(mSceneModeSwitch == null)
				mSceneModeSwitch = new SceneModeSwitch(mContext);
			mSceneModeSwitch.setView((TextView)view);
			mSceneModeSwitch.updateUiShow();
			  
		}else if(intent.getAction().equals(UiManager.EVENT_MUSIC_VOLUME_ADD) || 
				intent.getAction().equals(UiManager.EVENT_MUSIC_VOLUME_BELOW)){//媒体音量加/减调节
			/*if(mVolumeMusicAdjust == null){
				mVolumeMusicAdjust = new VolumeAdjust(this, "多媒体", 500);
				mVolumeMusicAdjust.setVolumeType(AudioManager.STREAM_MUSIC);
			}*/
			
		}else if(intent.getAction().equals(UiManager.EVENT_RING_VOLUME_ADD) || 
				intent.getAction().equals(UiManager.EVENT_RING_VOLUME_BELOW)){//铃声音量加/减调节
			
			/*if(mVolumeRingAdjust == null){
				mVolumeRingAdjust = new VolumeAdjust(this, "铃声", 500);
				mVolumeRingAdjust.setMaxProgress(7);
				mVolumeRingAdjust.setVolumeType(AudioManager.STREAM_RING);
			}*/
			
		}else if(intent.getAction().equals(UiManager.EVENT_ALARM_VOLUME_ADD) || 
				intent.getAction().equals(UiManager.EVENT_ALARM_VOLUME_BELOW)){//闹钟音量加/减调节
			
			/*if(mVolumeAlarmAdjust == null){
				mVolumeAlarmAdjust = new VolumeAdjust(this, "闹钟", 500);
				mVolumeRingAdjust.setMaxProgress(7);
				mVolumeAlarmAdjust.setVolumeType(AudioManager.STREAM_ALARM);
			}*/
			
		}else if(intent.getAction().equals(UiManager.EVENT_MOBILE_NETWORK)){
			//移动梦网
			if(mMobileNetwork == null)
	        mMobileNetwork = new MobileNetwork(mContext);
			mMobileNetwork.setView((TextView)view);
			mMobileNetwork.initEnable();
			mMobileNetwork.updateUiShow();
			
		}else if(intent.getAction().equals(UiManager.EVENT_WIFI_ON_OFF)){
			//wifi
			if(mWifiStateTracker == null)
	        mWifiStateTracker = new WifiStateTracker(mContext, null);
			mWifiStateTracker.setView((TextView)view);
			mWifiStateTracker.setImageViewResources(mContext);
			
		}else if(intent.getAction().equals(UiManager.EVENT_BLUETOOTH_ON_OFF)){
			 //蓝牙
			if(mBluetoothStateTracker == null)
	        mBluetoothStateTracker = new BluetoothStateTracker(mContext, null);
			mBluetoothStateTracker.setView((TextView)view);
			mBluetoothStateTracker.setImageViewResources(mContext);
		}else if(intent.getAction().equals(UiManager.EVENT_AIR_PLANE)){
			//飞行模式开关
			if(mAirPlane == null)
				mAirPlane = new AirPlane(mContext);
		}
	}
}
