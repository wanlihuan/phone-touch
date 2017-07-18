package com.yingt.xdtouch;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.phone.assistant.util.ThemeCache;
import com.touch.panel.WindowWrapper;
import com.touch.panel.util.DeviceInfoUtil;
import com.touch.panel.widget.CenterPanelView;
import com.touch.panel.widget.ControlCenterPanel;
import com.touch.panel.widget.FloatView;
import com.yingt.uimain.ui.MainTabActivity;

/**
 * Created by laihuan.wan on 2017/7/12 0012.
 */

public class MainActivity extends MainTabActivity {

    private WindowWrapper mWindowWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWindowWrapper = new WindowWrapper(this);

        ControlCenterPanel controlCenterPanel = new ControlCenterPanel(this);
        controlCenterPanel.setBackgroundColor(0x5f5f5f5f);
        mWindowWrapper.addView(controlCenterPanel);

        CenterPanelView centerPanelView = new CenterPanelView(this);
//        centerPanelView.setBackgroundColor(Color.RED);
        mWindowWrapper.addView(centerPanelView);
        mWindowWrapper.goneView(centerPanelView);

        creatFloatWindow();
    }

    /**
     * 创建面板视图
     */
    private void creatFloatWindow() {

        final FloatView floatView = new FloatView(this);
        floatView.setWindowLayout(0, 0, 100, 100);
        floatView.setImageBitmap(null);
        mWindowWrapper.addView(floatView);

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "被点击", Toast.LENGTH_LONG).show();
                int w = DeviceInfoUtil.getScreenShortSize(getApplicationContext());
                int h = DeviceInfoUtil.getScreenLongSize(getApplicationContext()) - floatView.getStatusBarHeight();
                int newWh = 450;
                floatView.setWindowLayout((w - newWh) / 2, (h - newWh) / 2, newWh, newWh);
                mWindowWrapper.updateViewLayout(floatView);
            }
        });

        floatView.setOnMoveListener(new FloatView.OnMoveListener() {
            public void onMove(int x, int y) {
                // TODO Auto-generated method stub
                floatView.setWindowLayout(x, y);
                mWindowWrapper.updateViewLayout(floatView);
            }
        });
    }
}