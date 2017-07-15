package com.yingt.uimain.widget.hscroll;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Add by laihuan.wan on 2017/06/22
 * 1. 可左右滑动
 *
 */
public class CHScrollView extends HorizontalScrollView {
	
	private CHScrollManager mCHScrollManager;
	public CHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	@SuppressLint("NewApi")
	public CHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}

	public CHScrollView(Context context) {
		super(context);
		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
	}
	
	public void setManager(CHScrollManager chScrollManager){
		mCHScrollManager = chScrollManager;
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//进行触摸赋值
		if(mCHScrollManager != null){
			if(!mCHScrollManager.isScroll())
				return false;
			
			mCHScrollManager.setTouchView(this);
		}
		return super.onTouchEvent(ev);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		
		if(mCHScrollManager != null)
			mCHScrollManager.onScrollEventListener(l, t);
		
		//当当前的CHSCrollView被触摸时，滑动其它
		if(mCHScrollManager != null && mCHScrollManager.getTouchView() == this) {
			mCHScrollManager.onScrollChanged(l, t, oldl, oldt);
		}else{
			super.onScrollChanged(l, t, oldl, oldt);
		}
	}
}
