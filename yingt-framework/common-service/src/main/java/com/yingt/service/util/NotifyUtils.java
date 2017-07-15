package com.yingt.service.util;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by guodong.deng on 2017/7/5.
 */

public class NotifyUtils {
    /**
     * 打印相关提示信息函数
     */
    public static void showNotifyMessage(String message) {
        System.out.println(" showNotifyMessage --- " + message);
    }

    /**
     * Toast弹出工具
     *
     * @param activity
     * @param message
     */
    public static void showNotifyToast(final Activity activity, final String message) {

        if ("Main".equals(Thread.currentThread().getName()))
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        else if (null != activity)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
    }

}
