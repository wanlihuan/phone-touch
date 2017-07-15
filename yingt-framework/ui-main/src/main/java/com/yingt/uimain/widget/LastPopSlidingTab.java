package com.yingt.uimain.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yingt.uimain.R;

/**
 * Created by laihuan.wan on 2017/6/30 0030.
 */

public class LastPopSlidingTab extends LinearLayout {

    public static final int DEF_VALUE_TAB_TEXT_ALPHA = 150;

    private int mTabCount = 0;
    private int mCurrentPosition = 0;
    private int mStartOffset = 0;
    private int mItemWidth;

    private int mIndicatorColor;
    private int mIndicatorHeight = 2;

    private int mTabTextSize = 14;
    private ColorStateList mTabTextColor = null;

    private boolean mLastItemHasIcon = true;

    private int mIndicatorLinePadding = 0;
    private int mPopwindowLayout;

    private Typeface mTabTextTypeface = null;
    private int mTabTextTypefaceStyle = Typeface.NORMAL;
    private LinearLayout.LayoutParams mTabLayoutParams;

    private LinearLayout mTabsContainer;
    private View mIndicatorView;
    private int itemAnimatorTime = 50;// 毫秒

    private PopupWindow popupWindow;
    private View popView;

    public LastPopSlidingTab(Context context) {
        this(context, null);
    }

    public LastPopSlidingTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LastPopSlidingTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOrientation(LinearLayout.VERTICAL);

        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mTabsContainer = new LinearLayout(context);
        mTabsContainer.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(mTabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mIndicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight, dm);
        mTabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTabTextSize, dm);

        mIndicatorLinePadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorLinePadding, dm);

        String tabTextTypefaceName = "sans-serif";

        // get custom attrs for tabs and container
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        mIndicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, mIndicatorColor);
        mIndicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, mIndicatorHeight);
        mIndicatorLinePadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorLinePadding, mIndicatorLinePadding);
        mTabTextSize = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabTextSize, mTabTextSize);
        mTabTextColor = a.hasValue(R.styleable.PagerSlidingTabStrip_pstsTabTextColor) ?
                a.getColorStateList(R.styleable.PagerSlidingTabStrip_pstsTabTextColor) : null;
        mTabTextTypefaceStyle = a.getInt(R.styleable.PagerSlidingTabStrip_pstsTabTextStyle, mTabTextTypefaceStyle);
        int tabTextAlpha = a.getInt(R.styleable.PagerSlidingTabStrip_pstsTabTextAlpha, DEF_VALUE_TAB_TEXT_ALPHA);
        String fontFamily = a.getString(R.styleable.PagerSlidingTabStrip_pstsTabTextFontFamily);

        mLastItemHasIcon = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsLastItemHasIcon, false);
        mPopwindowLayout = a.getResourceId(
                R.styleable.PagerSlidingTabStrip_pstsPopwindowLayout, -1);

        a.recycle();

        //Tab text color selector
        if (mTabTextColor == null) {
            mTabTextColor = createColorStateList(
                    0xffff0000,
                    Color.argb(tabTextAlpha,
                            Color.red(0xffff0000),
                            Color.green(0xffff0000),
                            Color.blue(0xffff0000)));
        }

        //Tab text typeface and style
        if (fontFamily != null) {
            tabTextTypefaceName = fontFamily;
        }

        mTabTextTypeface = Typeface.create(tabTextTypefaceName, mTabTextTypefaceStyle);
        mTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
    }

    /**
     * 初始化数据
     *
     * @param titles
     */
    public final void initDatas(String[] titles) {
        mTabCount = titles.length;
        for (int i = 0; i < mTabCount; i++) {
            View tabView = LayoutInflater.from(getContext()).inflate(R.layout.yingt_uimain_psts_tab, this, false);
            CharSequence title = titles[i];
            addTab(i, title, tabView);

            if (mLastItemHasIcon && i == mTabCount - 1) {
                FrameLayout frameLayout = (FrameLayout) tabView.findViewById(R.id.yingt_uimain_psts_tab_right);
                ImageView imageView = new ImageView(getContext());
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.yingt_uimain_pst_indica_icon));
                frameLayout.addView(imageView);
            }
        }

        // 初始化 popwindow 视图
        if (mPopwindowLayout > 0)
            popView = LayoutInflater.from(getContext()).inflate(mPopwindowLayout, null);

        updateTabStyles();
    }

    /**
     * 获取指示器的偏移
     *
     * @param position
     * @return
     */
    private int getPositionOffset(int position) {
        return position * mItemWidth + mIndicatorLinePadding - mStartOffset;
    }

    WsLayoutParams lp = new WsLayoutParams();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 第一次创建指示器视图
        if (mIndicatorView == null && mTabCount > 0) {
            int measuredChildHeight = getMeasuredHeight();
            int measuredChildWidth = getMeasuredWidth();

            mItemWidth = measuredChildWidth / mTabCount;
            int indicatorWidth = mItemWidth - 2 * mIndicatorLinePadding;
            mStartOffset = mIndicatorLinePadding;// 起始偏移，不是以 0 点开始，而是 mIndicatorLinePadding 作为起始

            lp.l = mStartOffset;// 初始化永远在第 0 项，并且是以 mIndicatorLinePadding 作为 0 的偏移
            lp.t = measuredChildHeight;
            lp.width = indicatorWidth;
            lp.height = mIndicatorHeight;

            mIndicatorView = new View(getContext());
            mIndicatorView.setBackgroundColor(mIndicatorColor);
            mIndicatorView.setLayoutParams(new ViewGroup.LayoutParams(
                    lp.width, lp.height));
            addView(mIndicatorView);
            setMeasuredDimension(measuredChildWidth, measuredChildHeight + mIndicatorHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mIndicatorView.layout(lp.l, lp.t, lp.l + lp.width, lp.t + lp.height);
    }

    class WsLayoutParams {
        public int l;
        public int t;
        public int width;
        public int height;
    }

    private void addTab(final int position, CharSequence title, View tabView) {
        TextView textView = (TextView) tabView.findViewById(R.id.yingt_uimain_psts_tab_title);
        if (textView != null) {
            if (title != null) textView.setText(title);
            textView.setTextColor(mTabTextColor);
        }

        tabView.setFocusable(true);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // 处理点击最后的选项图片
                if (position == mTabCount - 1 && mLastItemHasIcon)
                    lastIconItemProcess(); // 处理最后一个带图片的选项
                else if (mCurrentPosition != position)
                    slidingTab(position); // 处理指示器的滑动显示
            }
        });

        mTabsContainer.addView(tabView, position, mTabLayoutParams);
    }

    private void updateTabStyles() {
        // 初始化被选中状态
        View tabViewC = mTabsContainer.getChildAt(mCurrentPosition);
        TextView tab_titleC = (TextView) tabViewC.findViewById(R.id.yingt_uimain_psts_tab_title);
        tab_titleC.setSelected(true);

        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            TextView tab_title = (TextView) tabView.findViewById(R.id.yingt_uimain_psts_tab_title);

            if (tab_title != null) {
                tab_title.setTextColor(mTabTextColor);
                tab_title.setTypeface(mTabTextTypeface, mTabTextTypefaceStyle);
                tab_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
            }
        }
    }

    /**
     * 设置最后选项的文本内容
     *
     * @param text
     */
    public final void setLastItemText(String text) {
        if (mTabCount > 0) {
            int lastIndex = mTabCount - 1;
            View tabView = mTabsContainer.getChildAt(lastIndex);
            TextView tab_title = (TextView) tabView.findViewById(R.id.yingt_uimain_psts_tab_title);
            tab_title.setText(text);
        }
    }

    /**
     * 切换到最后的 tab
     */
    public final void slidingToLastTab() {
        slidingTab(mTabCount - 1);
    }

    /**
     * 切换 tab
     * 1. 处理选项文本颜色
     * 2. 处理滑动游标
     *
     * @param toPosition
     */
    public final void slidingTab(int toPosition) {
        // 处理选项文本颜色
        View tabView = mTabsContainer.getChildAt(mCurrentPosition);
        TextView tab_title = (TextView) tabView.findViewById(R.id.yingt_uimain_psts_tab_title);
        tab_title.setSelected(false);
        View tabView1 = mTabsContainer.getChildAt(toPosition);
        TextView tab_title1 = (TextView) tabView1.findViewById(R.id.yingt_uimain_psts_tab_title);
        tab_title1.setSelected(true);

        // 移动指示器游标
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(mIndicatorView, View.TRANSLATION_X,
                        getPositionOffset(mCurrentPosition), getPositionOffset(toPosition));
        int delay = itemAnimatorTime * Math.abs(toPosition - mCurrentPosition);
        delay = delay > 200 ? 200 : delay;
        objectAnimator.setDuration(delay);
        objectAnimator.start();
        mCurrentPosition = toPosition;

        // 回调监听
        if (mOnSwitchTabListener != null)
            mOnSwitchTabListener.onSwitchTab(mCurrentPosition);
    }

    /**
     * 最后的图片选项处理
     */
    private void lastIconItemProcess() {
        final int lastIndex = mTabCount - 1;
        View tabView = mTabsContainer.getChildAt(lastIndex);
        FrameLayout frameLayout = (FrameLayout) tabView.findViewById(R.id.yingt_uimain_psts_tab_right);
        final View view = frameLayout.getChildAt(0);

        if (view != null) {
            view.setSelected(true);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, 0, 180);
            objectAnimator.setDuration(300);
            objectAnimator.start();

            //  处理显示 popwindow
            if (popupWindow == null) {
                if (popView != null) {
                    popupWindow = new PopupWindow(popView, mItemWidth,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            view.setSelected(false);
                            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, 180, 0);
                            objectAnimator.setDuration(300);
                            objectAnimator.start();
                        }
                    });
                }
            }
            showAsDropDown(tabView);
        }
    }

    /**
     * 显示 popwindow
     *
     * @param parent
     */
    public final void showAsDropDown(View parent) {
        if (popupWindow != null) {
            popupWindow.showAsDropDown(parent, 0, mIndicatorHeight);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
        }
    }

    /**
     * 隐藏 popwindow
     */
    public final void dismissPop() {
        popupWindow.dismiss();
    }

    /**
     * 获取 pop 视图中的自视图对象
     *
     * @param id
     * @return
     */
    public final View findViewByIdForPop(int id) {
        if (popView != null)
            return popView.findViewById(id);

        return null;
    }

    private ColorStateList createColorStateList(int color_state_selected, int color_state_default) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected}, // enabled
                        new int[]{} //default
                },
                new int[]{
                        color_state_selected,
                        color_state_default
                }
        );
    }

    private OnSwitchTabListener mOnSwitchTabListener;

    public void setOnSwitchTabListener(OnSwitchTabListener onSwitchTabListener) {
        mOnSwitchTabListener = onSwitchTabListener;
    }

    public interface OnSwitchTabListener {
        void onSwitchTab(int position);
    }
}
