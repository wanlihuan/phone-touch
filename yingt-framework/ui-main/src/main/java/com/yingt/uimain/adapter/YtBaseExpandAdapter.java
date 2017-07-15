package com.yingt.uimain.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.Utils;
import com.yingt.uimain.R;

/**
 * Created by laihuan.wan on 2017/7/11 0011.
 */

public class YtBaseExpandAdapter extends BaseExpandAdapter{

    private String title;

    public YtBaseExpandAdapter(Context context){
        super(context);
    }

    @Override
    public int getTitleLayoutResId() {
        return R.layout.yingt_uimain_base_expand_title;
    }

    @Override
    public int getContentLayoutResId() {
        return 0;
    }

    @Override
    public YtBaseExpandHolder onCreateViewHolder(View view, int position) {
        super.onCreateViewHolder(view, position);
        setTitleText("盈通 Expand"); // 默认标题栏文本
        setExpandState(false);// 默认关闭状态，因为本来就没数据
        return new YtBaseExpandHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final YtBaseExpandHolder holder = (YtBaseExpandHolder) viewHolder;
        holder.textView.setText(title); // 动态设置标题栏，用于上层的设置
        holder.buttonLayout.setRotation(isExpandState() ? 0f : -90f);// 初始化指示器默认的方向
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public void onExpandObserver(RecyclerView.ViewHolder viewHolder, int position, boolean isOpenState) {
        YtBaseExpandHolder holder = (YtBaseExpandHolder) viewHolder;
        if (isOpenState) {
            createRotateAnimator(holder.buttonLayout, -90f, 0f).start();
        } else {
            createRotateAnimator(holder.buttonLayout, 0f, -90f).start();
        }
        if(onExpandListener != null)
            onExpandListener.onExpand(isOpenState, position);
    }

    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public void onClickTitle(View view) {
        super.onClickTitle(view);
    }

    public void setTitleText(String text) {
        this.title = text;
    }

    protected class YtBaseExpandHolder extends BaseExpandAdapter.BaseExpandHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;

        public YtBaseExpandHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);

        }
    }

    private OnExpandListener onExpandListener;
    public void setOnExpandListener(OnExpandListener onExpandListener){
        this.onExpandListener = onExpandListener;
    }
    public interface OnExpandListener{
        void onExpand(boolean isExpand, int position);
    }
}
