package com.android.phone.assistant.widget;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class AppInfo{
	
	/**
	 * 服务器中的ID，而非list ID号
	 */
	public int adId;
	/**
	 * 简单介绍
	 */
	public String adText;
	public String appName;
	public String author;
	/**
	 * 类别 如，[游戏]角色扮演
	 */
	public String category;//类别
	/**
	 * 详情描述
	 */
	public String description;
	public String iconUrl;
	public Bitmap icon;
	public String size;
	public String packageName;
	public String versionName;
	public int versionCode;
	public ArrayList<String> appScreenShots;
	
	
	public Bitmap frameIcon;
	/**
	 * 等级
	 */
	public Bitmap levelIcon;
	/**
	 * 下载量
	 */
	public String downloadCount;
	
	/**
	 * 目前只有一种状态：只显示下载
	 */
	public String status;
}
