package com.yingt.uimain.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yingt.uimain.R;

/**
 * Created by laihuan.wan on 2017/6/3 0003.
 */

public abstract class BaseFragment extends SkinBaseFragment implements ISubBaseUi{

    private View root;
    private BaseToolbar baseToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.yingt_uimain_base_ui_layout, container, false);
        // 布局内容
        int layoutId = getLayoutResId();
        if (layoutId > 0) {
            View contentView = inflater.inflate(layoutId, null);
            if (contentView != null)
                ((LinearLayout) root.findViewById(R.id.yingt_uimain_content)).addView(contentView);
        }

        FrameLayout toolbarRoot = (FrameLayout) root.findViewById(R.id.yingt_uimain_toolbar_root);
        if (isRemovedToolbar()) {
            toolbarRoot.setVisibility(View.GONE);

        } else {
            toolbarRoot.setVisibility(View.VISIBLE);
            baseToolbar = getLayoutToolbarView();
            if (baseToolbar == null)
                baseToolbar = new BaseToolbar(getContext());

            toolbarRoot.addView(baseToolbar);
        }

        //  处理事件
        onFindViewById();
        onEventProcessor();

        return root;
    }

    public View findViewById(int id){
        return  root.findViewById(id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onInitUiData();
    }

    @Override
    public boolean isRemovedToolbar() {
        return true;
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

    @Override
    public void onEventProcessor(){

    }
}
