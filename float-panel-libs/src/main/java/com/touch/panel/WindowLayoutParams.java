package com.touch.panel;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import com.touch.panel.util.DeviceInfoUtil;
import com.touch.panel.util.SmartFloatUtil;

/**
 * Created by laihuab.wan on 2017/7/15 0015.
 */

public class WindowLayoutParams {


    public static WindowManager.LayoutParams getLayoutParamsFullScreen(Context context) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        wmParams.type = SmartFloatUtil.askType(context);
        wmParams.format = PixelFormat.RGBA_8888;   //设置图片格式，效果为背景透明

        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
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

        wmParams.width = DeviceInfoUtil.getScreenShortSize(context);
        wmParams.height = DeviceInfoUtil.getScreenLongSize(context);

        return wmParams;
    }
}
