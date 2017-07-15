package com.yingt.uimain.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yingt.uimain.R;

/**
 * 1. 支持状态栏透明
 * Created by laihuan.wan on 2017/5/11 0011.
 */

public abstract class BaseActivity extends SkinBaseActivity  implements ISubBaseUi{

    private BaseToolbar baseToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 状态栏透明设置
//        Window window = getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.yingt_uimain_base_ui_layout);
        // 布局内容
        int layoutId = getLayoutResId();
        if (layoutId > 0) {
            View contentView = getLayoutInflater().inflate(layoutId, null);
            if (contentView != null)
                ((LinearLayout) findViewById(R.id.yingt_uimain_content)).addView(contentView);
        }

        FrameLayout toolbarRoot = (FrameLayout) findViewById(R.id.yingt_uimain_toolbar_root);
        if (isRemovedToolbar()) {
            toolbarRoot.setVisibility(View.GONE);

        } else {
            toolbarRoot.setVisibility(View.VISIBLE);
            baseToolbar = getLayoutToolbarView();
            if (baseToolbar == null)
                baseToolbar = new BaseToolbar(this);

            toolbarRoot.addView(baseToolbar);
        }

        //  处理事件
        onFindViewById();
        onInitUiData();
        onEventProcessor();
    }

    @Override
    public boolean isRemovedToolbar() {
        return false;
    }

    @Override
    public BaseToolbar getLayoutToolbarView() {
        return null;
    }

    @Override
    public void onFindViewById() {
    }

    @Override
    public void onInitUiData() {

    }

    /**
     * 处理用户事件
     */
    @Override
    public void onEventProcessor(){

    }
}
