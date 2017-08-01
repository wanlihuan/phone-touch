package com.fiona.fwindow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.fiona.fwindow.util.DeviceInfoUtil;
import com.fiona.fwindow.util.SmartFloatUtil;

/**
 * Created by laihuab.wan on 2017/7/15 0015.
 */

public class WindowLayoutParams {

    public static int getWidth(Context context){
        return DeviceInfoUtil.getScreenShortSize(context);
    }

    public static int getHeight(Context context){
        return DeviceInfoUtil.getScreenLongSize(context) -
                DeviceInfoUtil.getStatusBarHeight(context);
    }

    public static WindowManager.LayoutParams getLayoutParamsFullScreen(Context context) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        wmParams.type = SmartFloatUtil.askType(context);
        wmParams.format = PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明

        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
//        |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
         wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
        */
        wmParams.gravity = Gravity.TOP | Gravity.LEFT;
        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = getWidth(context);
        wmParams.height = getHeight(context);

        return wmParams;
    }
}
