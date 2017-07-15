package com.touch.panel;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by laihuan.wan on 2017/7/15 0015.
 */

public class WindowWrapper {
    private WindowManager mWindowManager;

    public WindowWrapper(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

    }

    public final WindowManager getWindowManager() {
        return mWindowManager;
    }

    public final void addView(IFloatView iFloatView) {
        mWindowManager.addView(iFloatView.getView(), iFloatView.getWindowLayoutParams());
    }

    public final void updateViewLayout(IFloatView view) {
        mWindowManager.updateViewLayout(view.getView(), view.getWindowLayoutParams());
    }

    public final void remove(IFloatView view) {
        mWindowManager.removeView(view.getView());
    }

    /**
     *  GONE 隐藏浮动视图
     * @param view
     */
    public final void goneFloatView(IFloatView view){
        view.getView().setVisibility(View.GONE);
    }

    /**
     *  VISIBLE 显示浮动视图
     * @param view
     */
    public final void visibleFloatView(IFloatView view){
        view.getView().setVisibility(View.VISIBLE);
    }
}
