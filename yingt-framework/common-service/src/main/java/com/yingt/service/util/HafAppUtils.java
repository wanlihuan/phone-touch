package com.yingt.service.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class HafAppUtils {

    /**
     * 获取当前 app 的版本代码
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return version;
    }
}
