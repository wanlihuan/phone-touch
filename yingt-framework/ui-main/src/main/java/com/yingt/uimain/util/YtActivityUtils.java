package com.yingt.uimain.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.yingt.uimain.ui.MainTabActivity;
import com.yingt.uimain.ui.ProductTourActivity;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class YtActivityUtils {
    // 保存主界面
    private static MainTabActivity mainTabActivity;
    public static void saveMainActivity(MainTabActivity mainActivity){
        mainTabActivity = mainActivity;
    }
    public static MainTabActivity getMainActivity(){
        return mainTabActivity;
    }

    /**
     * 启动用户预览界面
     */
    public static void startTourActivity(Activity currentActivity){
        Class tourClass = (Class)mainTabActivity.getIntent().getExtras().get("TourClass");
        if(tourClass == null)
            currentActivity.startActivity(new Intent(currentActivity, ProductTourActivity.class));
        else
            currentActivity.startActivity(new Intent(currentActivity, tourClass));

        currentActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void switchWindow(Activity currentAct,
                                    Class<? extends Activity> targetActivity, Bundle bundle,
                                    boolean closeCurrent) {
        switchWindow(currentAct, targetActivity, bundle, -1, closeCurrent);
    }

    /**
     * 跳转至界面<BR>
     * 如果目标activity已经存在，则重用，不会生成新的activity
     *
     * @param currentAct
     *            当前 Activity
     * @param targetActivity
     *            目标 Activity
     * @param bundle
     * @param requestCode
     *            请求码，默认值为-1
     * @param closeCurrentActivity
     *            是否关闭当前activity
     */
    public static void switchWindow(Activity currentAct,
                                    Class<? extends Activity> targetActivity, Bundle bundle,
                                    int requestCode, boolean closeCurrentActivity) {
        Intent i = new Intent();
        i.setClass(currentAct, targetActivity);

        if (bundle != null) {
            i.putExtras(bundle);
        }
        if (requestCode > 0) {
            currentAct.startActivityForResult(i, requestCode);
            Logger.d("tag:	", "跳转了1");
        } else {
            Logger.d("tag:	", "跳转了");
            currentAct.startActivity(i);
        }

        if (closeCurrentActivity) {
            currentAct.finish();
        }
    }

    /**
     * 常用于启动从一个模块中启动另一个模块中的activity
     * @param activity
     * @param startActivityPCName
     * @param bundle
     * @param closeCurrentActivity
     */
    public static void switchWindow(Activity activity,
                                    String startActivityPCName, Bundle bundle,
                                    boolean closeCurrentActivity) {
        switchWindow(activity, startActivityPCName, bundle, -1, closeCurrentActivity);
    }


    /**
     * 常用于启动从一个模块中启动另一个模块中的activity
     *
     * @param activity
     * @param startActivityPCName
     *            被启动的Activity的包名+类名
     * @param bundle
     * @param requestCode
     * @param closeCurrentActivity
     */
    public static void switchWindow(Activity activity,
                                    String startActivityPCName, Bundle bundle, int requestCode,
                                    boolean closeCurrentActivity) {
        if (activity == null) {
            return;
        }
        Intent i = new Intent();
        ComponentName componentName = new ComponentName(
                activity.getPackageName(), startActivityPCName);
        i.setComponent(componentName);

        if (bundle != null) {
            i.putExtras(bundle);
        }
        if (requestCode > 0) {
            activity.startActivityForResult(i, requestCode);
        } else {
            activity.startActivity(i);
        }

        if (closeCurrentActivity) {
            activity.finish();
        }
    }

}
