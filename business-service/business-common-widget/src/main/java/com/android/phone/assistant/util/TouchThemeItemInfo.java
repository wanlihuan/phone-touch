package com.android.phone.assistant.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class TouchThemeItemInfo {
	public String id;//主题编号，如：T10001
	public Bitmap bgBitmap;
	public Bitmap icon;
	public String panelDrawableId;//panel背景主题图片Id
	public String title;
	public Drawable tailIcon;//显示多少积分前面的icon
	public String tail;//显示需要多少积分
	public String gold;//如果某主题已经兑换了那么此值也是0,是0不是代表这个主题是免费的，而是用户已经兑换了,就可以直接使用了
	public boolean myThemeFlag;
}
