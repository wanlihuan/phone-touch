package com.android.phone.assistant.util;

import android.content.UriMatcher;
import android.net.Uri;

public class TouchWords {
	//public static String packageName = "com.niunet.assistivetouch";

	// 定义数据库的名字
	public static final String DATABASE_NAME = "Touch.db";
	// 定义数据库的版本
	public static final int DATABASE_VERSION = 6;

	// 表的名字
	public static final String TABLE_NAME = "touchinfo";

	// 定义数据库的字段
	public static final String _ID = "_id";
	public static final String VIEW_ID = "viewId";
	public static final String PANEL_NUM = "panelNum";
	public static final String CELL_NUM = "cellNum";
	public static final String ICON = "icon";
	public static final String LABEL = "label";
	public static final String INTENT_URI = "intentUri";



	// 我的主题表名字
	public static final String USER_THEME_TABLE_NAME = "UserThemeTable";
	//我的主题字段
	public static final String THEME_NAME = "theme_name";//主题名称
	public static final String THEME_ICON_PATH = "panelNum";//主题图片全路径
	public static final String THEME_TYPE = "theme_type";//主题类型：touch主题还是panel主题


	// 定义访问的类型
	public static final int ITEMS = 1;
	public static final int ITEM = 2;

	// 定义访问的类型（我的主题）
	public static final int THEME_ITEMS = 3;
	public static final int THEME_ITEM = 4;

	// 定义MIME类型，访问单个记录
	public static String CONTENT_ITEM = "vnd.android.cursor.item/" + AppManger.appPackageName;
	// 访问数据集
	public static String CONTENT_ITEMS = "vnd.android.cursor.dir/" + AppManger.appPackageName;
	// 定义访问ContentProvider权限
	public static String AUTHORITY = AppManger.appPackageName;
	// 定义URI
	public static Uri TOUCH_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");
	public static Uri WORD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");

	// 定义URI(我的主题)
	public static Uri USER_THEME_TOUCH_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/themeitems");
	public static Uri USER_THEME_WORD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/themeitem");

	public static UriMatcher matcher;

	public static void init(String packageName){
		CONTENT_ITEM = "vnd.android.cursor.item/" + packageName;
		CONTENT_ITEMS = "vnd.android.cursor.dir/" + packageName;
		// 定义访问ContentProvider权限
		AUTHORITY = packageName;
		// 定义URI
		TOUCH_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");
		WORD_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");

		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 为UriMatcher注册两个Uri
		matcher.addURI(TouchWords.AUTHORITY, "items", ITEMS);
		matcher.addURI(TouchWords.AUTHORITY, "item/#", ITEM);

		// 为UriMatcher注册两个Uri（我的主题）
		matcher.addURI(TouchWords.AUTHORITY, "themeitems", THEME_ITEMS);
		matcher.addURI(TouchWords.AUTHORITY, "themeitem/#", THEME_ITEM);
	}

}
