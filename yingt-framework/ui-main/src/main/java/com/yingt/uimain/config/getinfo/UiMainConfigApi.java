package com.yingt.uimain.config.getinfo;

import android.content.Context;

import com.google.gson.Gson;
import com.yingt.service.util.FileSystem;
import com.yingt.uimain.config.bean.TabHostInfo;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class UiMainConfigApi {

    /**
     * 获取主界面底部 TAB 控件数据
     * @param context
     * @return
     */
    public static TabHostInfo getBottomTabInfo(Context context){
        String jsonConfig = FileSystem.getFromAssets(context, "uimainConfig/bottom_tab_config");

        Gson gson = new Gson();
        TabHostInfo info = gson.fromJson(jsonConfig, TabHostInfo.class);
        return info;
    }
}
