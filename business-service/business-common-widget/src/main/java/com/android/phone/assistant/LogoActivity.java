package com.android.phone.assistant;

import java.io.File;

import com.android.file.util.FileSystem;
import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.ThemeCache;
import com.android.phone.assistant.util.ThemeManager;
import com.android.phone.assistant.util.YouMi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LogoActivity extends Activity{
//	public static String mainPageClassName = null;
	private final int START_MAIN_ACTIVITY   = 0;
	private ImageView mLogoBg;
	private TextView mLogoTitle;
	
	String[] assertThemePath = {"themes/JH8N32B3JJD6872N552BX5BWSJ",
			"themes/KSK3IKS87DMNS7NHWSN6MJF",
			"themes/SNKKS82KSJHBD982KJSHHS612"
			,"themes/3HGV3JJ32J2BJ3B4"
			,"themes/b3j2kbbhjv5bv2b43"
			,"themes/BSDKEWJH63BN"
			,"themes/DBASKS7N3BVSJBSY"
			,"themes/DBDJKJED63BDNTW"
			,"themes/DBDKKDGDEJ"
			,"themes/DBDKS0DB48DBD"
			,"themes/DBDMJDJHD73BDUU"
			,"themes/DBSJDKL93BDG"
			,"themes/DBSKLBDCV7NDBE",
			"themes/DBSKLSDNSDHESNN"
			,"themes/DBVWMD0Y3BDND"
			,"themes/DGJDJDBMD9"
			,"themes/DNBLSODDM9DDG"
			,"themes/DNBMJS63WBDJTYSD"
			,"themes/DSGKLDSJKGEWKBS7"
			,"themes/DSHJKSDJHHDS87S6"
			,"themes/fgdsgg32hkjh4"
			,"themes/GSAG2JGBJH4BV2"
			,"themes/GVDKJHS6NEN3BDI"
			,"themes/HSLHIWGDF7BSK3"
			,"themes/HYRNWLD6MW3NE7U"
			,"themes/JSGJDKD8SSSE"
			,"themes/SBAJS7N3BSJH23"
			,"themes/SBGDKIENDNDNSMS7"
			,"themes/SBJAS654SNB3WV"
			
			,"themes/SBKSD73VKST"
			,"themes/SBS73NSNBSJU3NS"
			,"themes/SBSJSKAN8SNGBW"
			,"themes/SBSKLSNSSD86SS"
			,"themes/SDF72VDKSNU3BVS"
			,"themes/VBSJMS7B23NBDYT3"
			,"themes/Y6UWB3BDSKI3BEI"
			
			
			};
	
	 final Handler mHandler = new Handler() {
			@Override 
			public void handleMessage(Message msg)  
			{						
				switch (msg.what) {		
					case START_MAIN_ACTIVITY:
					 	Intent intent = new Intent();
					 	try{
						 	ComponentName componentName = new ComponentName(getPackageName(),//app的包名，而不是本类的包名
						 			regMainActivityClassName());
						 	intent.setComponent(componentName);
		                    startActivity(intent);
					 	}catch(Exception e){
					 		Toast.makeText(LogoActivity.this, "启动Logo界面时未指定需启动的主界面！", Toast.LENGTH_SHORT).show();
					 	}
	                    LogoActivity.this.finish();      
	                    
					break;  
				}
			}
	    };
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        
		setContentView(R.layout.main_logo_layout);
		mLogoBg = (ImageView) this.findViewById(R.id.logo_bg);
		mLogoTitle = (TextView) this.findViewById(R.id.logo_title);
		
		mLogoBg.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_logo));  
		
		WindowManager winMgr = (WindowManager) getSystemService(Context.WINDOW_SERVICE);            
		DisplayMetrics dm = new DisplayMetrics();
		winMgr.getDefaultDisplay().getMetrics(dm);  
	   
		CommonUtil.widthPixels = dm.widthPixels;           
		CommonUtil.heightPixels = dm.heightPixels; 
		
		//初始化主题
		init();
		
		//初始化有米
        if(CommonUtil.mAdType == CommonUtil.YOU_MI){
        	if(YouMi.getInstance() == null){
	        	YouMi.creatYoumiInstance(this);
	        	YouMi.getInstance().initYoumi();
        	}
        }
	}

	private void init(){
		
		//初始化默认的主题配置
		final SharedPreferences preferences = getSharedPreferences("ThemeConfig", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		final String defaultPPPThemeDir = getResources().getString(R.string.PPP_default_theme_path);
		final String defaultTTTThemeDir = getResources().getString(R.string.TTT_default_theme_path);
		//第一次运行时才设置默认主题目录配置
		boolean isFirst = false;
		if(preferences.getString(ThemeManager.PPP_CURRENT_THEME_PATH, "").equals("") ||
				preferences.getString(ThemeManager.TTT_CURRENT_THEME_PATH, "").equals("")){
			isFirst = true;
			Editor mEditor = preferences.edit();
			mEditor.putString(ThemeManager.PPP_CURRENT_THEME_PATH, defaultPPPThemeDir);
			mEditor.putString(ThemeManager.TTT_CURRENT_THEME_PATH, defaultTTTThemeDir);
			mEditor.commit();
		}
		
		final File themeSDCacheDir = FileSystem.getCacheRootDir(this, ThemeManager.THEME_PATH_MAINDIR);
		final boolean isFirst1 = isFirst;
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				
				//将Assert目录下的主题文件复制到SDCard中并解压出来
				for(int i = 0; i < assertThemePath.length; i++){
					File file = new File(themeSDCacheDir.getAbsolutePath()+assertThemePath[i].split("themes")[1]);
					if(!file.exists()){
						FileSystem.copyFileFromAssetsAndDecodeBinZip(LogoActivity.this, assertThemePath[i], 
							themeSDCacheDir.getAbsolutePath());
					}
				}
				//可能service已经先启动了，
				ThemeManager.addThemeToCache(LogoActivity.this);
				Intent intent = new Intent("android.touch.action.TOUCH_PANEL_BITMAP");
				LogoActivity.this.sendBroadcast(intent);
				//Intent intent1 = new Intent("android.touch.action.TOUCH_ICON_BITMAP");
				//LogoActivity.this.sendBroadcast(intent1);
				
				
				mHandler.sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 1000);
			}
		}.start();
		  
	}
	
	
	private void logTitleTranslateAnimation(){
		
		    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, CommonUtil.heightPixels / 2, 0);
		    translateAnimation.setDuration(500);
		    mLogoTitle.startAnimation(translateAnimation);
		    translateAnimation.setAnimationListener(new AnimationListener(){

				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					   
				}

				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}

				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
		    	
		    });
	}
	/**
	 * 
	 * @return 需要启动的类全名
	 */
	public String regMainActivityClassName(){
		
		return null;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	
}