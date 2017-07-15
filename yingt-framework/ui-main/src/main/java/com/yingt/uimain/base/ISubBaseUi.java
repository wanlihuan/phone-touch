package com.yingt.uimain.base;

import android.view.View;

/**
 * Created by laihuan.wan on 2017/6/15 0015.
 */

public interface ISubBaseUi {

    /**
     * 是否移除 toolbar
     * @return
     */
    boolean isRemovedToolbar();

    /**
     * 获取 toolbar
     * @return
     */
    BaseToolbar getLayoutToolbarView();

    /**
     * 获取页面布局资源 ID
     */
    int getLayoutResId();

    /** 用来获取视图实例对象 **/
    void onFindViewById();

    /** 初始化界面 **/
    void onInitUiData();

    /** 事件处理器 **/
    void onEventProcessor();

}
