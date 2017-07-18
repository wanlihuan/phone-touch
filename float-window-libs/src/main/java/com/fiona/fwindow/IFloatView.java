package com.fiona.fwindow;

import android.view.View;
import android.view.WindowManager;

/**
 * Created by laihuan.wan on 2017/7/15 0015.
 */

public interface IFloatView {
    View getView();
    WindowManager.LayoutParams getWindowLayoutParams();
    void setWindowLayout(int x, int y);
    void setWindowLayout(int x, int y,int width, int height);
}
