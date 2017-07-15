package com.yingt.uimain.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yingt.uimain.R;

/**
 * 1. 默认只有左上角返回按钮、中间 title 部分
 * Created by laihuan.wan on 2017/6/15 0015.
 */
public class BaseToolbar extends Toolbar{

    private LayoutInflater inflater;
    private LinearLayout menuRoot;
    private RelativeLayout backBtn;

    public BaseToolbar(Context context) {
        this(context, null);
    }

    public BaseToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setContentInsetStartWithNavigation(0);
        this.setContentInsetEndWithActions(0);

        inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.yingt_uimain_base_toolbar_root, this, true);

        // 左边的返回按钮
        RelativeLayout leftRoot = (RelativeLayout) findViewById(R.id.yingt_toolbar_focus_back_content);
        backBtn = leftRoot;
        int layoutIdL = getLayoutLeftResId();
        if (layoutIdL > 0) {
            View contentView = inflater.inflate(layoutIdL, null);
            if (contentView != null)
                leftRoot.addView(contentView);
        }
        if(backBtn != null) {
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        onBackBtnPressed();
                }
            });
        }


        // 中间的 title 部分
        RelativeLayout centerRoot = (RelativeLayout) findViewById(R.id.yingt_toolbar_center_root);
        int layoutId = getLayoutTitleResId();
        if (layoutId > 0) {
            View contentView = inflater.inflate(layoutId, null);
            if (contentView != null)
                centerRoot.addView(contentView);
        }

        // 右边菜单
        menuRoot = (LinearLayout) findViewById(R.id.yingt_uimain_toolbar_menu_root);
        int layoutId1 = getLayoutRightMenuResId();
        if (layoutId1 > 0) {
            View contentView = inflater.inflate(layoutId1, null);
            if (contentView != null)
                menuRoot.addView(contentView);
        }

        onFindViewById();
        onInitUiData();
        onEventProcessor();
    }

    public void onBackBtnPressed(){
        ((Activity)getContext()).finish();
    }

    public int getLayoutLeftResId(){
        return R.layout.yingt_uimain_toolbar_left_default;
    }
    public int getLayoutTitleResId(){
        return R.layout.yingt_uimain_toolbar_center_default;
    }

    public int getLayoutRightMenuResId(){
        return -1;
    }

    /**
     * 重置右边菜单的布局
     * @param layoutId
     */
    public void resetRightMenuResId(int layoutId){
        menuRoot.removeAllViews();
        if (layoutId > 0) {
            View contentView = inflater.inflate(layoutId, null);
            if (contentView != null)
                menuRoot.addView(contentView);
        }

        onFindViewById();
        onInitUiData();
        onEventProcessor();
    }

    /**
     * 恢复右边菜单视图
     */
    public void recoverRightMenu(){
        resetRightMenuResId(getLayoutRightMenuResId());
    }

    /**
     * 隐藏左边的视图
     */
    public void hideLeftView(){
        if(backBtn != null)
            backBtn.setVisibility(View.GONE);
    }

    /**
     * 显示左边的视图
     */
    public void showLeftView(){
        if(backBtn != null)
            backBtn.setVisibility(View.VISIBLE);
    }

    /** 用来获取视图实例对象 **/
    public void onFindViewById(){

    }

    /** 初始化界面 **/
    public void onInitUiData(){

    }

    /** 事件处理器 **/
    public void onEventProcessor(){

    }

}
