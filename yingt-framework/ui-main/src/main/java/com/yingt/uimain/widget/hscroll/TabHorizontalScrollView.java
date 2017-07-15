package com.yingt.uimain.widget.hscroll;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yingt.uimain.R;

/**
 * Created by laihuan.wan on 2017/6/22 0022.
 *
 * 封装好的左右滑动控件
 *
 *  可继承该基类进行自定义开发界面，该类除了具有左右滑动属性之外只是封装出了容器，分为 4 个区域，说明如下：
 *     封装说明：
 *         将界面分为标题栏固定、标题栏左右滑动、列表固定、列表左右滑动，可分别设置布局视图
 */

public abstract class TabHorizontalScrollView extends FrameLayout {

    private  LayoutInflater inflater;
    private CHScrollManager mCHScrollManager;
    private RecyclerView mRecyclerView;
    private TabHScrollRecyclerAdapter recyclerViewAdapter;

    /**
     * 获取 title 左边固定视图自定义视图布局 id
     * @return
     */
    public abstract int getLayoutTitleLeftChildResId();
    /**
     * 获取 title 右边滚动部分的自定义视图布局 id
     * @return
     */
    public abstract int getLayoutTitleScrollChildResId();

    /**
     * 用来获取添加的 title 布局视图实例对象
     */
    public abstract void onFindTitleViewById();

    /**
     * 获取 列表 右边滚动部分的自定义视图布局 id
     * @return
     */
    public abstract int getLayoutListScrollChildResId();

    /**
     * 获取列表左边固定部分的自定义视图布局 id
     * @return
     */
    public abstract int getLayoutListLeftChildResId();


    public TabHorizontalScrollView(Context context) {
        this(context, null);
    }

    public TabHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        inflater = LayoutInflater.from(context);

        mCHScrollManager = new CHScrollManager();
        //监听水平滚动改变，显示隐藏滑动指示器
        mCHScrollManager.setOnScrollChangedListener(new CHScrollManager.OnScrollChangedListener() {

            @Override
            public void onScrollChanged(int scrollX, int scrollY) {
                // TODO Auto-generated method stub
            }
        });

        inflater.inflate(R.layout.yingt_uimain_hscroll_root_layout, this, true);

        // title 左边固定不变的视图处理
        FrameLayout titleLeftRoot = (FrameLayout) findViewById(R.id.chsv_child_root);
        int titleLeftChildResId = getLayoutTitleLeftChildResId();
        if (titleLeftChildResId > 0) {
            View titleLeftChildView = inflater.inflate(titleLeftChildResId, null);
            if (titleLeftChildView != null)
                titleLeftRoot.addView(titleLeftChildView);
        }

        // title 右边滚动视图添加滚动盒中
        CHScrollView titleChScrollView = (CHScrollView) findViewById(R.id.chsv_scroll);
        titleChScrollView.setHorizontalFadingEdgeEnabled(false);
        mCHScrollManager.addHView(titleChScrollView);// title 右边滚动视图添加滚动盒中

        //  处理标题栏中右边滚动的视图
        FrameLayout titleScrollRoot = (FrameLayout) findViewById(R.id.chsv_scroll_child_root);
        int titleScrollChildResId = getLayoutTitleScrollChildResId();
        if (titleScrollChildResId > 0){
            View titleScrollChildView = inflater.inflate(titleScrollChildResId, null);
            if (titleScrollChildView != null)
                titleScrollRoot.addView(titleScrollChildView);
        }

        onFindTitleViewById();


        // 初始化下拉刷新控件
        TwinklingRefreshLayout twinklingRefreshLayout =
                (TwinklingRefreshLayout) findViewById(R.id.tr_tab_hscroll_refreshlayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_hscroll_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public class TabHScrollRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        public TabHScrollRecyclerAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getItemCount() {
            if(mTabHScrollAadapter != null)
                return mTabHScrollAadapter.getItemCount();
            return 0;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if(mTabHScrollAadapter != null)
                mTabHScrollAadapter.onBindViewHolder(viewHolder,position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {

            View view = inflater.inflate(R.layout.yingt_uimain_hscroll_item_layout, null);

            // 处理列表选项左边的视图
            FrameLayout leftRoot = (FrameLayout) view.findViewById(R.id.chsv_child_root);
            int listLeftChildResId = getLayoutListLeftChildResId();
            if(listLeftChildResId > 0) {
                View leftChildView = inflater.inflate(listLeftChildResId, null);
                if (leftChildView != null)
                    leftRoot.addView(leftChildView);
            }

            // 右边滚动视图添加滚动盒中
            CHScrollView chScrollView = (CHScrollView) view.findViewById(R.id.chsv_scroll);
            chScrollView.setHorizontalFadingEdgeEnabled(false);
            mCHScrollManager.addHView(chScrollView);// 右边滚动视图添加滚动盒中

            //  处理 列表 中右边滚动的视图
            FrameLayout scrollRoot = (FrameLayout) view.findViewById(R.id.chsv_scroll_child_root);
            int listScrollChildResId = getLayoutListScrollChildResId();
            if(listScrollChildResId > 0) {
                View scrollChildView = inflater.inflate(getLayoutListScrollChildResId(), null);
                if (scrollChildView != null)
                    scrollRoot.addView(scrollChildView);
            }

            if(mTabHScrollAadapter != null)
                return mTabHScrollAadapter.onCreateViewHolder(view);

            return null;
        }

    }

    private TabHScrollAadapter mTabHScrollAadapter;
    public void setAadapter(TabHScrollAadapter tabHScrollAadapter){
        mTabHScrollAadapter = tabHScrollAadapter;

        // 设置 RecyclerView 适配器
        if(recyclerViewAdapter == null) {
            recyclerViewAdapter = new TabHScrollRecyclerAdapter(getContext());
            mRecyclerView.setAdapter(recyclerViewAdapter);
        }else{
            notifyDataSetChanged();
        }
    }

    /**
     * 更新数据
     */
    public final void notifyDataSetChanged(){
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.notifyDataSetChanged();
    }
}
