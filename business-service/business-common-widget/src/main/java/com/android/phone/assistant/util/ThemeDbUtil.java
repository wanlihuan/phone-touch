package com.android.phone.assistant.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

/**
 * 我的主题数据库操作
 * @author wanlh
 *
 */
public class ThemeDbUtil {

	private Context mContext;
	private ContentResolver mContentResolver;


	public ThemeDbUtil(Context context){
		mContext = context;
		mContentResolver = context.getContentResolver();
	}


	/******************************  我的主题数据可以部分  ***************************************/

	/**
	 * 获取某主题类型的主题列表
	 */
	public List<ThemeInfo> getMyThemeList(String themeType){

		List<ThemeInfo> myThemeInfoList = new ArrayList<ThemeInfo>();

		Cursor cursor = mContentResolver.query(TouchWords.USER_THEME_TOUCH_CONTENT_URI, null,
				null, null, null);


		while(cursor.moveToNext()){
			if(themeType.equals(cursor.getString(cursor.getColumnIndex(TouchWords.THEME_TYPE)))){

				ThemeInfo themeInfo = new ThemeInfo();
				themeInfo.title = cursor.getString(cursor.getColumnIndex(TouchWords.THEME_NAME));
				themeInfo.themeIconPath = cursor.getString(cursor.getColumnIndex(TouchWords.THEME_ICON_PATH));
				themeInfo.freeFlag = "Y";//我的主题已经算是免费的了

				File file = new File(themeInfo.themeIconPath);
				if(file.isFile()&& file.length() > 0)
					myThemeInfoList.add(themeInfo);
			}
		}

		cursor.close();

		return myThemeInfoList;
	}
}
