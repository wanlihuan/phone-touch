package com.yingt.uimain.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.yingt.uimain.R;

/**
 * Created by laihuan.wan on 2017/7/9 0009.
 */

public abstract class BaseExpandAdapter implements RecycleChildAdapter {

    private LayoutInflater inflater;

    private boolean expandState = true;

    /**
     * 修改展开的状态
     *
     * @param isExpand
     */
    public final void setExpandState(boolean isExpand) {
        expandState = isExpand;
    }

    public final boolean isExpandState() {
        return expandState;
    }

    /**
     * 获取可扩展标题栏的布局资源 ID
     *
     * @return
     */
    public abstract int getTitleLayoutResId();

    /**
     * 获取可展开关闭内容区域布局资源 ID
     *
     * @return
     */
    public abstract int getContentLayoutResId();

    /**
     * 获取视图 Holder
     *
     * @return
     */
    public BaseExpandHolder onCreateViewHolder(View view, int position){
        return new BaseExpandHolder(view);
    }

    /**
     * 打开关闭监听器
     *
     * @param isOpenState
     */
    public abstract void onExpandObserver(RecyclerView.ViewHolder viewHolder, int position, boolean isOpenState);

    public void onClickTitle(View view){

    }

    public BaseExpandAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.yingt_uimain_recycle_item_expand_layout, null);

        // 添加 title 视图
        int layoutId = getTitleLayoutResId();
        if (layoutId > 0) {
            View titleView = inflater.inflate(layoutId, null);
            if (titleView != null)
                ((RelativeLayout) view.findViewById(R.id.rl_item_title)).addView(titleView);
        }

        // 添加 内容 视图
        int conLayoutId = getContentLayoutResId();
        if (conLayoutId > 0) {
            View contentView = inflater.inflate(conLayoutId, null);
            if (contentView != null)
                ((ExpandableLinearLayout) view.findViewById(R.id.el_expandableLayout)).addView(contentView);
        }

        return onCreateViewHolder(view, position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof BaseExpandHolder) {
            final BaseExpandHolder holder = (BaseExpandHolder) viewHolder;
            holder.setIsRecyclable(false);
            holder.expandableLayout.setInRecyclerView(true);
            // 实现扩展的动画效果，具体可以参考开源 demo https://github.com/AAkira/ExpandableLayout
            holder.expandableLayout.setInterpolator(
                    Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR));
            holder.expandableLayout.setExpanded(expandState);
            holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    setExpandState(true);
                    onExpandObserver(viewHolder, position, true);
                }

                @Override
                public void onPreClose() {
                    setExpandState(false);
                    onExpandObserver(viewHolder, position, false);
                }
            });
            holder.itemTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTitle(v);
                    holder.expandableLayout.toggle();
                }
            });
        }
    }

    /**
     * 视图扩展 Holder
     */
    protected class BaseExpandHolder extends RecyclerView.ViewHolder {
        public ExpandableLinearLayout expandableLayout;
        public RelativeLayout itemTitleView;

        public BaseExpandHolder(View v) {
            super(v);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.el_expandableLayout);
            itemTitleView = (RelativeLayout) v.findViewById(R.id.rl_item_title);
        }
    }

}
