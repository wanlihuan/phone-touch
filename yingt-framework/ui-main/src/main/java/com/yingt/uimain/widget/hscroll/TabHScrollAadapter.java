package com.yingt.uimain.widget.hscroll;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by laihuan.wan on 2017/6/22 0022.
 */

public abstract class TabHScrollAadapter {

    public abstract void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);
    public abstract RecyclerView.ViewHolder onCreateViewHolder(View view);
    public abstract int getItemCount();
}
