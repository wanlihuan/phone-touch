package com.android.phone.assistant.util;

import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class ThemeCache {

	/**
	 * 	经过处理后的墙纸背景缓存Key
	 */
	public static String KEY_WALLPAPER = "key_wallpaperBitmap";
	/**
	 * 墙纸背景缓存KEY
	 */
	public static String KEY_WALLPAPER_SRC = "key_wallpaperBitmapSrc";

	/**
	 * 经过处理过和未处理的背景图片缓存
	 */
	public static Map<String, Bitmap> bgBitmapCache = new LinkedHashMap<String, Bitmap>();

	/**
	 * 主题列表主题缓存
	 */
	public static Map<String, Bitmap> touchBitmapCache = new LinkedHashMap<String, Bitmap>();

	/**当前被使用的主题缓存*/
	public static Map<String, Bitmap> currentThemeCache = new LinkedHashMap<String, Bitmap>();

}
