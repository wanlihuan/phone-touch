package com.fiona.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.fiona.fwindow.IFloatView;
import com.fiona.fwindow.WindowLayoutParams;
import com.fiona.panel.widget.Workspace;

/**
 * Created by laihuan.wan on 2017/7/15 0015.
 */

public class CenterPanelView extends RelativeLayout implements IFloatView {

    public CenterPanelView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub

    }

    public CenterPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    private Workspace mWorkspace;

    public CenterPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        LayoutInflater.from(context).inflate(R.layout.fp_center_panel_layout, this, true);
        mWorkspace = (Workspace) findViewById(R.id.workspace);
        for (int i = 0; i < 9; i++) {
            View btnView = LayoutInflater.from(context).inflate(R.layout.fp_canter_cell_item_layout, null);
            mWorkspace.addChildView(btnView, i);
        }
//        mWorkspace.requestLayout();
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

    public Workspace getWorkspace() {
        return mWorkspace;
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
