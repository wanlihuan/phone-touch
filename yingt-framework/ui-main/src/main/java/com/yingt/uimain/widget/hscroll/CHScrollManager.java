package com.yingt.uimain.widget.hscroll;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

public class CHScrollManager {

	protected List<CHScrollView> mHScrollViews =new ArrayList<CHScrollView>();
	private HorizontalScrollView mCurrentTouchView;
	
	private boolean isScroll = true;
	public void setScrollFlag(boolean flag){
		isScroll = flag;
	}
	public boolean isScroll(){
		return isScroll;
	}
	public void setTouchView(HorizontalScrollView touchView){
		mCurrentTouchView = touchView;
	}
	
	/**
	 * 获取当前触摸的的视图
	 * @return
	 */
	public HorizontalScrollView getTouchView(){
		return mCurrentTouchView;
	}
	
	/**
	 * 添加需要滚动的视图到滚动视图盒中
	 */
	public void addHView(CHScrollView scrollView){
		scrollView.setManager(this);
		mHScrollViews.add(scrollView);
	}
	
	/**
	 * 可用来添加listview中的滚动视图
	 * @param hScrollView
	 */
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	@SuppressLint("NewApi")
	public void addHViews(final CHScrollView hScrollView) {
		if(!getScrollHolder().isEmpty()) {
			int size = getScrollHolder().size();
			CHScrollView scrollView = get(size - 1);
			final int scrollX = scrollView.getScrollX();
			//第一次满屏后，向下滑动，有一条数据在开始时未加入
			if(scrollX != 0) {
				scrollView.post(new Runnable() {
					@SuppressLint("NewApi")
					@Override
					public void run() {
						//当listView刷新完成之后，把该条移动到最终位置
						hScrollView.scrollTo(scrollX, 0);
					}
				});
			}
		}
		hScrollView.setManager(this);
		mHScrollViews.add(hScrollView);
	}
	
	public List<CHScrollView> getScrollHolder(){
		return mHScrollViews;
	}
	
	/**
	 * 获取滚动盒中的滚动视图
	 * @param location
	 * @return
	 */
	public CHScrollView get(int location){
		return mHScrollViews.get(location);
	}
	
	@SuppressLint("NewApi")
	public void onScrollChanged(int l, int t, int oldl, int oldt){
		for(CHScrollView scrollView : mHScrollViews) {
			//防止重复滑动
			if(mCurrentTouchView != scrollView)
				scrollView.smoothScrollTo(l, t);
		}
	}
	
	/**
	 * 用来监听滚动的事件
	 * @param l
	 * @param t
	 */
	public void onScrollEventListener(int l, int t){
		if(mOnScrollChangedListener != null)
			mOnScrollChangedListener.onScrollChanged(l, t);
	}
	
	private OnScrollChangedListener mOnScrollChangedListener = null;
	public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
		mOnScrollChangedListener = onScrollChangedListener;	
	}
	
	/**
	 * 监听滚动状态
	 * @author wanlh
	 *
	 */
	public interface OnScrollChangedListener { 

		/**
		 * 滚动变化监听器,滚动过程中的监听
		 * @param scrollX 水平滚动的距离
		 * @param scrollY 垂直滚动的距离
		 */
	     void onScrollChanged(int scrollX, int scrollY);
	}

}
