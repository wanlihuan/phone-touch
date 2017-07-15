package com.android.phone.assistant.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.file.util.FileSystem;
import com.android.phone.assistant.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class ThemeManager {
	Context mContext;
	/**主题主目录名*/
	public static final String THEME_PATH_MAINDIR = "themes";

	/**存储默认的面板主题preference 键*/
	public static String PPP_CURRENT_THEME_PATH = "PPP_current_path";
	/**存储默认的touch主题preference 键*/
	public static String TTT_CURRENT_THEME_PATH = "TTT_current_path";

	public static final String MYTHEME_TOUCH_CONFIG_FILE_NAME = "touch";
	public static final String MYTHEME_PANEL_CONFIG_FILE_NAME = "panel";

	public static String currentThemeIconId = "default";
	public static String currentThemeTypeFileName = MYTHEME_TOUCH_CONFIG_FILE_NAME;

	//public static Map<String, TouchThemeItemInfo> mAdapterInfoList = new LinkedHashMap<String, TouchThemeItemInfo>();

	public static List<ThemeInfo> mThemeInfoList = new ArrayList<ThemeInfo>();
	public static List<ThemeInfo> mMyThemeInfoList = new ArrayList<ThemeInfo>();
	public static String themePrice  = "20";

	/**
	 *
	 * @param cachePath
	 * 				app的缓存路径名，如："/data/data/"+PackName+"/cache/
	 */
	public ThemeManager(Context context){
		mContext = context;
	}

	/**
	 *
	 * @param cacheDir
	 * @param cacheChildDirName  ..../cache/cacheChildDirName 如：touch 图片为ticon
	 * @param cacheFileName ..../cache/cacheChildDirName/cacheFileName缓存文件名
	 * @return
	 */
	public Bitmap decodeCacheIcon(File cacheDir, String cacheChildDirName, String cacheFileName){
		//将assets目录ticon目录下的文件缓存起来

		Bitmap bm1 = FileSystem.parsePngFile(cacheDir+"/"+cacheChildDirName+"/"+cacheFileName);
		if(null != bm1){
			return bm1;
		}

		bm1 = FileSystem.parsePngFile(
				FileSystem.copyFileFromAssetsAndDecodeBinZip(mContext, cacheChildDirName +"/"+ cacheFileName,
						cacheDir+"/"+cacheChildDirName)+"/"+cacheFileName);

		if(null != bm1){
			return bm1;
		}else{
			decodeCacheIcon(cacheDir, cacheChildDirName, cacheFileName);
		}

		return null;
	}

	public static void addThemeToCache(Context context){
		//可能service已经先启动了，
		/*Bitmap bitmap = ThemeCache.currentThemeCache.get("TTT_");
		if(bitmap != null && bitmap.isRecycled() == false){
			bitmap.recycle();
			bitmap = null;
		}
		bitmap = ThemeCache.currentThemeCache.get("PPP_");
		if(bitmap != null && bitmap.isRecycled() == false){
			bitmap.recycle();
			bitmap = null;
		}*/

		SharedPreferences preferences = context.getSharedPreferences("ThemeConfig", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		String defaultPPPThemeDir = context.getResources().getString(R.string.PPP_default_theme_path);
		String defaultTTTThemeDir = context.getResources().getString(R.string.TTT_default_theme_path);
		if(ThemeCache.currentThemeCache.get("PPP_") == null)
			ThemeCache.currentThemeCache.put("PPP_",
					FileSystem.parsePngFile(
							FileSystem.getCacheRootDir(context,
									ThemeManager.THEME_PATH_MAINDIR).getAbsolutePath()+
									preferences.getString(ThemeManager.PPP_CURRENT_THEME_PATH, defaultPPPThemeDir)));
		if(ThemeCache.currentThemeCache.get("TTT_") == null)
			ThemeCache.currentThemeCache.put("TTT_",
					FileSystem.parsePngFile(
							FileSystem.getCacheRootDir(context,
									ThemeManager.THEME_PATH_MAINDIR).getAbsolutePath()+
									preferences.getString(ThemeManager.TTT_CURRENT_THEME_PATH, defaultTTTThemeDir)));
	}
}
