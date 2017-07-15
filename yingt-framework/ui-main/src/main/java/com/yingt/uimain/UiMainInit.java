package com.yingt.uimain;

import android.content.Context;

import com.yingt.uimain.skin.loader.SkinManager;
import com.yingt.uimain.util.Res;

/**
 * Created by laihuan.wan on 2017/6/1 0001.
 */
public class UiMainInit {

    public static void init(Context context){
        // Must call init first

        // 初始化获取资源类
        Res.setContext(context);

        // 初始化换肤框架
        SkinManager.getInstance().init(context);
        SkinManager.getInstance().load();
    }
}
