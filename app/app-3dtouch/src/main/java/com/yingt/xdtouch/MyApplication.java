package com.yingt.xdtouch;

import com.niunet.assistivetouch.TouchApplication;
import com.niunet.assistivetouch.UiManager;
import com.yingt.uimain.UiMainInit;


/**
 * Created by laihuan.wan on 2017/5/11 0011.
 */
public class MyApplication extends TouchApplication {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        UiManager.appPackageName = this.getPackageName();
        UiManager.projectName = this.getString(R.string.app_name);
        UiManager.youmiID = "ed59d7ac84e221a8";
        UiManager.youmiMiYao = "e835a3be0e40c985";

        // 初始化 UI MAIN
        UiMainInit.init(getApplicationContext());
        super.onCreate();
    }
}
