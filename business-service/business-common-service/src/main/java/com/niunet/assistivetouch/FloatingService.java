package com.niunet.assistivetouch;

import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.ThemeCache;
import com.android.phone.assistant.util.ThemeManager;
import com.assistivetouch.widget.FolderView;
import com.assistivetouch.widget.FloatView;
import com.assistivetouch.widget.FloatView.OnShapeAlphaListener;
import com.assistivetouch.widget.TouchSettingView;
import com.assistivetouch.widget.FloatView.OnMoveListener;
import com.android.assistivetouch.R;
import com.niunet.assistivetouch.TouchPanelView.AnimationCallbacks;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 悬浮窗Service 该服务会在后台一直运行一个悬浮的透明的窗体。
 */
public class FloatingService extends Service{
	private NotificationManager mNotificationManager;
	private SharedPreferences preferences;
	private WindowManager mWindowManager = null;
	private WindowManager.LayoutParams wmFloatParams = null;
	private WindowManager.LayoutParams wmPanelParams = null;
	private WindowManager.LayoutParams wmRecentTaskParams = null;
	private FloatView mFloatView = null;
	private LayoutInflater mInflater;
	private TouchPanelView mTouchPanelView = null;
	private LinearLayout mIOS7TopNotificationView = null;
	private FolderView mRecentTaskViewGroup = null;

	private int statusBarHeight = 0;
	/**
	 * 保存小白点的位置坐标位置与屏幕的比例系数，用于在小白点的位置打开面板
	 */
	private float floatXRatio = 0.0f;
	private float floatYRatio = 0.0f;
	/**
	 * 保存当前
	 */
	private int currentFloatViewX = 0;
	private int currentFloatViewY = 0;
	int screenWidth;
	int screenHeight;
	int screenMinSection;
	int screenMaxSection;
	int currentOrientation = 0;
	Bitmap topBodyBitmap;
	Bitmap bottomBodyBitmap;

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(topBodyBitmap != null && !topBodyBitmap.isRecycled()){
				topBodyBitmap.recycle();
				topBodyBitmap = null;
			}

			if(bottomBodyBitmap != null && !bottomBodyBitmap.isRecycled()){
				bottomBodyBitmap.recycle();
				bottomBodyBitmap = null;
			}
		}
	};

	private BroadcastReceiver mFloatViewReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if(action.equals("android.touch.action.OPEN_RECENT_TASK")){//打开最近任务
				if(mRecentTaskViewGroup == null)
					return;

				CommonUtil commonUtil = new CommonUtil();
				Bitmap mWallpaperBitmap = commonUtil.getWallpaperDrawable(context, statusBarHeight, screenWidth);
				mTouchPanelView.setBackgroundDrawable(new BitmapDrawable(mWallpaperBitmap));

				mTouchPanelView.setDrawingCacheEnabled(true);
				mTouchPanelView.buildDrawingCache();
				Bitmap srcBitmap = mTouchPanelView.getDrawingCache(); //带桌面墙纸的截图 。不可以回收，不然下一次会报异常

				//创建上下部分的截图
				topBodyBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight() / 2);
				bottomBodyBitmap = Bitmap.createBitmap(srcBitmap, 0, srcBitmap.getHeight() / 2,
						srcBitmap.getWidth(), srcBitmap.getHeight() / 2);

		       /* if (bitmap != null) {
		            System.out.println("bitmap got!");
		            try {
		              FileOutputStream out = new FileOutputStream("/sdcard/EasyTouch.png");
		              bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		              System.out.println("file " + "/sdcard/EasyTouch.png" + "output done.");
		            } catch (Exception e) {
		              e.printStackTrace();
		            }
		          } else {
		            System.out.println("bitmap is NULL!");
		          }*/

				//回收临时使用的墙纸
				if(mWallpaperBitmap != null && !mWallpaperBitmap.isRecycled()){
					mTouchPanelView.setBackgroundDrawable(null);
					mWallpaperBitmap.recycle();
					mWallpaperBitmap = null;
				}

				updateRecentTaskLocation(statusBarHeight);
				mRecentTaskViewGroup.setVisibility(View.VISIBLE);
				mRecentTaskViewGroup.openFilder(topBodyBitmap, bottomBodyBitmap);

			}else if(action.equals("android.touch.action.CLOSE_RECENT_TASK")){	//关闭最近任务

				mRecentTaskViewGroup.closeFolder(new AnimationListener(){

					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub

						mRecentTaskViewGroup.setVisibility(View.GONE);
						mHandler.sendEmptyMessageDelayed(0, 200);
						/*if(topBodyBitmap != null && !topBodyBitmap.isRecycled()){
							topBodyBitmap.recycle();
							topBodyBitmap = null;
						}

						if(bottomBodyBitmap != null && !bottomBodyBitmap.isRecycled()){
							bottomBodyBitmap.recycle();
							bottomBodyBitmap = null;
						}*/
					}

					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

				});
			}
			else if(action.equals("android.touch.action.FLOAT_ALPHA_CHANGED_NOTI")){//小白点透明度改变
				setTouchAlpha(FloatView.normalAlpha);

			}if(action.equals("android.touch.action.FLOAT_DAXIAO_CHANGED_NOTI")){//小白点大小改变
				setTouchSize(FloatView.DaxiaoNormalRatio);

			}else if(action.equals("android.touch.action.FLOAT_VIEW_VISIBILITY")){//显示小白点
				mNotificationManager.cancel(0x989);

				mFloatView.setVisibility(View.VISIBLE);
				mFloatView.setShapeAlpha(null, FloatView.maxAlpha, FloatView.normalAlpha, FloatView.DURATION);

			}else if(action.equals("android.touch.action.TOUCH_PANEL_OPEN")){//打开面板

				mTouchPanelView.bindItemsToPanel(intent.getStringExtra("PanelNum"));
				//打开面板动画
				panelEnterAnimation(preferences.getInt("AnimationType", 0));

			}else if(action.equals("android.touch.action.TOUCH_PANEL_CLOSE")){//关闭面板

				//关闭面板动画
				panelExitAnimation(preferences.getInt("AnimationType", 0));

			}else if(action.equals("android.touch.action.TOUCH_PANEL_BITMAP") || //配置面板icon
					action.equals("android.touch.action.TOUCH_ICON_BITMAP")){ //配置小白点icon
				//从缓存中获取主题进行设置
				mTouchPanelView.setPanelBackgroundBitmap(ThemeCache.currentThemeCache.get("PPP_"));
				mFloatView.setImageBitmap(ThemeCache.currentThemeCache.get("TTT_"));

            	/*if(!updateFloatIcon())
            		Toast.makeText(FloatingService.this, "配置失败，再试一次！", Toast.LENGTH_SHORT).show();
            	else*/
				if(intent.getBooleanExtra("ShowToast", false))
					Toast.makeText(FloatingService.this, "配置成功！", Toast.LENGTH_SHORT).show();

			}
		}
	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

		Log.d("tag", "currentOrientation="+currentOrientation+",newConfig.orientation="+newConfig.orientation);

		if(currentOrientation == 0){
			currentOrientation = -1;
			return;
		}

		updateScreenWidthHeight();
		statusBarHeight = mFloatView.getStatusBarHeight();
		updatePanelLocation(statusBarHeight);

		if(newConfig.orientation == 1){	//代表切换从横屏切换到到竖屏
			updateFloatTouchLocation(screenWidth - currentFloatViewY, currentFloatViewX);

		}else if(newConfig.orientation == 2){	//代表切换从竖屏切换到到横屏

			updateFloatTouchLocation(currentFloatViewY, screenHeight - currentFloatViewX);
		}

		currentOrientation = newConfig.orientation;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		pandingToForegroundProcess();//绑定广播，使之成为前台进程而不被杀死

		mWindowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		preferences = getSharedPreferences(TouchSettingView.TOUCH_PREFERENCES, Context.MODE_WORLD_READABLE);
		mInflater = LayoutInflater.from(this);

		updateScreenWidthHeight();//更新有关屏幕信息，特别是横竖屏幕切换后需要改变

		//初始化图片到缓存中
		ThemeManager.addThemeToCache(this);

		creatPanelWindow();//面板层 、系统按键层

		//近期任务层、文件夹层等
		creatRecentTaskWindow();

		//   	creatIOS7NoticationWindow();//通知栏层

		creatFloatWindow();//小白点层

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.touch.action.TOUCH_ICON_BITMAP");
		filter.addAction("android.touch.action.TOUCH_PANEL_BITMAP");
		filter.addAction("android.touch.action.TOUCH_PANEL_OPEN");
		filter.addAction("android.touch.action.TOUCH_PANEL_CLOSE");
		filter.addAction("android.touch.action.FLOAT_VIEW_VISIBILITY");
		filter.addAction("android.touch.action.FLOAT_ALPHA_CHANGED_NOTI");
		filter.addAction("android.touch.action.FLOAT_DAXIAO_CHANGED_NOTI");
		filter.addAction("android.touch.action.OPEN_RECENT_TASK");
		filter.addAction("android.touch.action.CLOSE_RECENT_TASK");
		registerReceiver(mFloatViewReceiver, filter);
	}

	/**
	 * 创建Touch视图
	 */
	private void creatFloatWindow(){

		if(mFloatView == null)
			mFloatView = new FloatView(this);

		//设置过透明度后，开机初始化透明度
		FloatView.normalAlpha = preferences.getInt("TouchAlphaProgress", FloatView.normalAlpha - FloatView.minAlpha) + FloatView.minAlpha;

		mFloatView.setShapeAlpha(null, FloatView.maxAlpha, FloatView.normalAlpha, FloatView.DURATION);

		//设置LayoutParams(全局变量）相关参数
		wmFloatParams = new WindowManager.LayoutParams();

		wmFloatParams.type=LayoutParams.TYPE_TOAST;   //设置window type
		wmFloatParams.format=PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明

		wmFloatParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
        */
		wmFloatParams.gravity = Gravity.LEFT|Gravity.TOP;   //调整悬浮窗口至左上角，便于调整坐标
		//以屏幕左下角为原点，设置x、y初始值
		wmFloatParams.x = screenWidth;
		wmFloatParams.y = screenHeight / 2;
		setFloatXYRatio(wmFloatParams.x, wmFloatParams.y);

		//设置过大小后，开机初始化大小
		FloatView.DaxiaoNormalRatio = FloatView.getDaxiaoRatio(preferences.getInt("TouchDaxiaoProgress",
				(int)((FloatView.DaxiaoNormalRatio - FloatView.DaxiaoMinRatio) / (FloatView.DaxiaoMaxRatio - FloatView.DaxiaoMinRatio)*
						FloatView.DaxiaoMaxProgress)), FloatView.DaxiaoMaxProgress);

		wmFloatParams.height = wmFloatParams.width = (int) (screenMinSection * FloatView.DaxiaoNormalRatio);

		try{
			mWindowManager.addView(mFloatView, wmFloatParams);
		}catch(Exception e){

		}

		//if(!updateFloatIcon())
		//	mFloatView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.btn_assistivetouch));

		//从缓存中获取主题进行设置
		mFloatView.setImageBitmap(ThemeCache.currentThemeCache.get("TTT_"));

		mFloatView.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//解决刚刚启动第一次显示没有减掉状态栏的情况
				//if(statusBarHeight == 0)
				{
					if(currentOrientation == 0)
						currentOrientation = -1;

					statusBarHeight = mFloatView.getStatusBarHeight();
					updatePanelLocation(statusBarHeight);
				}

				//展开动画
				panelEnterAnimation(preferences.getInt("AnimationType", 0));

				//保存上一次面板的显示
				if(!preferences.getBoolean("saveOldPanel", true))
					mTouchPanelView.bindItemsToPanel(UiManager.PANEL_TYPE_0);
				else
					mTouchPanelView.bindItemsToPanel(mTouchPanelView.getCurrentPanelNum());

				//是否开启呼吸灯效果
				mTouchPanelView.setFlashingLightning(preferences.getInt("PanelShowStyle", 0) == 0? false : true);
			}
		});

		mFloatView.setOnLongClickListener(new OnLongClickListener(){

			@SuppressLint({ "NewApi", "NewApi" })
			public boolean onLongClick(View view) {
				// TODO Auto-generated method stub
				if(mFloatView.getIsMovingFlag()){
					mFloatView.clearMovingFlag();
					return true;
				}

				mFloatView.setShapeAlpha(new OnShapeAlphaListener(){
					public void shapeAlphaEnd() {
						// TODO Auto-generated method stub
						mFloatView.setVisibility(View.GONE);
						notifyToStatuBar();

					}
				}, FloatView.maxAlpha, 0, FloatView.DURATION);

				return true;
			}
		});

		mFloatView.setOnMoveListener(new OnMoveListener(){
			public void onMove(int x, int y) {
				// TODO Auto-generated method stub

				updateFloatTouchLocation(x, y);
			}
		});
	}

	/**
	 * 创建面板视图
	 */
	private void creatPanelWindow(){
		if(mTouchPanelView == null)
			mTouchPanelView = new TouchPanelView(this);

		mTouchPanelView.setVisibility(View.GONE);
		//是否开启呼吸灯效果
		mTouchPanelView.setFlashingLightning(preferences.getInt("PanelShowStyle", 0) == 0? false : true);

		wmPanelParams = getPanelParams();
		try{
			mWindowManager.addView(mTouchPanelView, wmPanelParams);
		}catch(Exception e){
			e.printStackTrace();
		}

		//点击空白或者外面面板消失
		mTouchPanelView.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {

				//关闭面板广播
				FloatingService.this.sendBroadcast(new Intent("android.touch.action.TOUCH_PANEL_CLOSE"));

			}
		});

		//每次启动时会加载主题到缓存中
		//从缓存中获取主题进行设置
		mTouchPanelView.setPanelBackgroundBitmap(ThemeCache.currentThemeCache.get("PPP_"));
	}

	private void creatIOS7NoticationWindow(){
		if(mIOS7TopNotificationView == null)
			mIOS7TopNotificationView = (LinearLayout) mInflater.inflate(R.layout.pop_windown_root_layout,null);

		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

		wmParams.type = LayoutParams.TYPE_PHONE;   //设置window type
		wmParams.format = PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明

		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;

		wmParams.gravity = Gravity.CENTER|Gravity.TOP;   //调整悬浮窗口至左上角，便于调整坐标
		wmParams.x = 0;
		wmParams.y = 0;

		wmParams.height = screenHeight / 8;
		wmParams.width = screenWidth;
		try{
			mWindowManager.addView(mIOS7TopNotificationView, wmParams);
		}catch(Exception e){

		}
		mIOS7TopNotificationView.setVisibility(View.GONE);
	}

	private void creatRecentTaskWindow(){
		if(mRecentTaskViewGroup == null)
			mRecentTaskViewGroup = new FolderView(this);

		wmRecentTaskParams = new WindowManager.LayoutParams();

		wmRecentTaskParams.type = LayoutParams.TYPE_PHONE;   //设置window type
		wmRecentTaskParams.format = PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明

		wmRecentTaskParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;

		wmRecentTaskParams.gravity = Gravity.CENTER|Gravity.TOP;   //调整悬浮窗口至左上角，便于调整坐标
		wmRecentTaskParams.x = 0;
		wmRecentTaskParams.y = 0;

		wmRecentTaskParams.height = screenHeight - statusBarHeight;
		wmRecentTaskParams.width = screenWidth;

		try{
			mWindowManager.addView(mRecentTaskViewGroup, wmRecentTaskParams);
		}catch(Exception e){

		}

		mRecentTaskViewGroup.setVisibility(View.GONE);
	}

	/**
	 * 面板打开动画
	 * @param animationType 动画类型
	 */
	private void panelEnterAnimation(int animationType){

		mFloatView.setVisibility(View.GONE);
		mTouchPanelView.setVisibility(View.VISIBLE);

		if(animationType == 0){//iphone式动画
			ScaleAnimation  enterScaleAnim = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
					Animation.RELATIVE_TO_SELF, floatXRatio, Animation.RELATIVE_TO_SELF, floatYRatio);
			enterScaleAnim.setDuration(200);

			AnimationSet mAnimationSet = new AnimationSet(true);
			mAnimationSet.addAnimation(enterScaleAnim);
			mTouchPanelView.startPanelAnimation(mAnimationSet);

		}else if(animationType == 1){//弹出框式动画

			mTouchPanelView.startPanelEnterAnimation(null, R.anim.zoom_enter);

		}
	}


	/**
	 * 面板关闭动画
	 * @param animationType 动画类型
	 */
	private void panelExitAnimation(int animationType){

		if(animationType == 0){ //iphone式动画
			ScaleAnimation  enterScaleAnim = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f,
					Animation.RELATIVE_TO_SELF, floatXRatio, Animation.RELATIVE_TO_SELF, floatYRatio);
			enterScaleAnim.setDuration(200);

			AnimationSet mAnimationSet = new AnimationSet(true);
			mAnimationSet.addAnimation(enterScaleAnim);
			mTouchPanelView.startPanelAnimation(mAnimationSet);
			mAnimationSet.setAnimationListener(new AnimationListener(){

				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					mFloatView.setVisibility(View.VISIBLE);
					mFloatView.setShapeAlpha(null, FloatView.maxAlpha, FloatView.normalAlpha, FloatView.DURATION);
					mTouchPanelView.setVisibility(View.GONE);
					if(mTouchPanelView.getBackground() != null)
						mTouchPanelView.setBackgroundDrawable(null);
					/*if(mWallpaperBitmap != null && !mWallpaperBitmap.isRecycled()){
						mWallpaperBitmap.recycle();
						mWallpaperBitmap = null;
					}*/
				}

				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub

				}

				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub

				}
			});
		}else if(animationType == 1){//弹出框式动画

			mTouchPanelView.startPanelExitAnimation(new AnimationCallbacks(){
														public void animationEnd() {
															// TODO Auto-generated method stub
															mFloatView.setVisibility(View.VISIBLE);
															mFloatView.setShapeAlpha(null, FloatView.maxAlpha, FloatView.normalAlpha, FloatView.DURATION);
															mTouchPanelView.setVisibility(View.GONE);
														}
													}
					,R.anim.zoom_exit);
		}
	}

	/**
	 * 绑定广播，使之成为前台进程而不被杀死
	 */
	private void pandingToForegroundProcess(){

		//创建一个Notification
		Notification notify = new Notification();
		startForeground(0x987, notify);   // notification ID: 0x1982, you can name it as you will.
	}

	private void notifyToStatuBar(){

		Intent intent = new Intent("android.touch.action.FLOAT_VIEW_VISIBILITY");
		PendingIntent pi = PendingIntent.getBroadcast(FloatingService.this, 0, intent , 0);

		//创建一个Notification
		Notification notify = new Notification();
		notify.icon = R.drawable.ic_launcher_notifition;
		notify.tickerText = getResources().getText(R.string.app_name)+"在这里";
		notify.when = System.currentTimeMillis();
		//	notify.defaults = Notification.DEFAULT_SOUND;//为Notification设置声音
		//	notify.defaults = Notification.DEFAULT_ALL;//为Notification设置默认声音、默认振动、默认闪光灯
		notify.flags = Notification.FLAG_NO_CLEAR;

//		notify.setLatestEventInfo(FloatingService.this, getResources().getText(R.string.app_name),
//				"点击悬浮到主屏幕", pi);

		mNotificationManager.notify(0x989, notify);	//发送通知
	}

	/**
	 * 更新有关屏幕信息，特别是横竖屏幕切换后需要改变
	 */
	private void updateScreenWidthHeight(){
		DisplayMetrics dm = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight =  dm.heightPixels;
		screenMinSection = Math.min(dm.widthPixels, dm.heightPixels);
		screenMaxSection = Math.max(dm.widthPixels, dm.heightPixels);

	}

	private WindowManager.LayoutParams getPanelParams(){

		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

		wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;   //设置window type
		wmParams.format = PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明

		wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
        */
		wmParams.gravity = Gravity.TOP|Gravity.CENTER;
		wmParams.x = 0;
		wmParams.y = 0;

		wmParams.width = screenWidth;
		wmParams.height = screenHeight;

		return wmParams;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d("tag", "FloatingService onDestroy");
		//mFloatView.recycleIcon();
		removeWindowView();
		unregisterReceiver(mFloatViewReceiver);
	}

	private void removeWindowView(){
		try{
			mWindowManager.removeView(mFloatView);
			mWindowManager.removeView(mTouchPanelView);
			mWindowManager.removeView(mIOS7TopNotificationView);
		}catch(Exception e){

		}
	}

	private void setFloatXYRatio(int x, int y){

		currentFloatViewX = x;
		currentFloatViewY = y;

		floatYRatio = y / (float)screenHeight;
		floatXRatio = x / (float)screenWidth;

		if(floatYRatio > 0.0f && floatYRatio < 0.25f){
			floatYRatio = 0.0f;
			if(x > 0)
				floatXRatio = x / (float)screenWidth - 0.05f;
			else
				floatXRatio = x / (float)screenWidth + 0.05f;

		}else if(floatYRatio > 0.6f && floatYRatio < 1.0f){
			floatYRatio = 1.0f;
			if(x > 0)
				floatXRatio = x / (float)screenWidth - 0.05f;
			else
				floatXRatio = x / (float)screenWidth + 0.05f;
		}
	}
	/*-- add by wanlaihuan 2013-10-14*/

	/**
	 * 处理进入横屏的情况
	 */
	private void updatePanelLocation(int statusBarHeight){

		wmPanelParams.height = screenHeight - statusBarHeight;
		wmPanelParams.width = screenWidth;
		try{
			mWindowManager.updateViewLayout(mTouchPanelView, wmPanelParams);
		}catch(Exception e){

		}
	}

	private void updateRecentTaskLocation(int statusBarHeight){

		wmRecentTaskParams.height = screenHeight - statusBarHeight;
		wmRecentTaskParams.width = screenWidth;
		try{
			mWindowManager.updateViewLayout(mRecentTaskViewGroup, wmRecentTaskParams);
		}catch(Exception e){

		}
	}

	/**
	 * 更新浮动窗口位置参数
	 * @param x
	 * @param y
	 */
	private void updateFloatTouchLocation(int x, int y){

		setFloatXYRatio(x, y);
		wmFloatParams.x = x;
		wmFloatParams.y = y;

		try{
			mWindowManager.updateViewLayout(mFloatView, wmFloatParams);
		}catch(Exception e){

		}
	}

	/**
	 * 更改大小
	 * @param ratio 0.0f ~ 1.0f
	 */
	private void setTouchSize(float ratio){

		wmFloatParams.height = wmFloatParams.width = (int) (screenMinSection * ratio);
		mWindowManager.updateViewLayout(mFloatView, wmFloatParams);
	}

	/**
	 * 改变透明度 0 ~ 255
	 * @param alpha
	 */
	private void setTouchAlpha(int alpha){

		mFloatView.setAlpha(alpha);
	}


	/**
	 *
	 * @param normalBitmap 正常Bitmap
	 */
	public void setTouchBitmap(Bitmap normalBitmap){
		mFloatView.setImageBitmap(normalBitmap);
		mFloatView.invalidate();
	}
}


