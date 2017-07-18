package com.fiona.fwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by laihuan.wan on 2017/7/15 0015.
 *
 * 模板类文件，该文件是为了能快速的创建面板视图而添加的，开发新的面板视图可直接 copy 这里的全部代码
 */

public class TemplatePanelView extends View implements IFloatView{

    public TemplatePanelView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub

    }

    public TemplatePanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public TemplatePanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }


    @Override
    public View getView() {
        return this;
    }

    private WindowManager.LayoutParams layoutParams;

    @Override
    public WindowManager.LayoutParams getWindowLayoutParams() {
        if (layoutParams == null)
            layoutParams = WindowLayoutParams.getLayoutParamsFullScreen(this.getContext());
        return layoutParams;
    }

    @Override
    public void setWindowLayout(int x, int y) {
        setWindowLayout(x, y, -1, -1);
    }

    @Override
    public final void setWindowLayout(int x, int y, int width, int height) {
        if (layoutParams == null)
            layoutParams = WindowLayoutParams.getLayoutParamsFullScreen(this.getContext());

        if (layoutParams != null) {
            layoutParams.x = x;
            layoutParams.y = y;
            if (width >= 0)
                layoutParams.width = width;
            if (height >= 0)
                layoutParams.height = height;
        }
    }
}
