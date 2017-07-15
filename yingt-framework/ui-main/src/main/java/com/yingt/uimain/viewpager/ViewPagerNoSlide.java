package com.yingt.uimain.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 1. 可以控制是否左右滑动
 * Created by laihuan.wan on 2017/6/12 0012.
 */
public class ViewPagerNoSlide  extends ViewPager {

    private boolean isCanScroll = false;

    public ViewPagerNoSlide(Context context) {
        super(context);
    }

    public ViewPagerNoSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOffscreenPageLimit(10);// 设置最多可以不销毁的页面个数
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }

    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }
}
