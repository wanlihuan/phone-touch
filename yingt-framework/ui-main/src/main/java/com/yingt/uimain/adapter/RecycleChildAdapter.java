package com.yingt.uimain.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by laihuan.wan on 2017/6/27 0027.
 */

public interface RecycleChildAdapter {

    void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position);
}
