package com.yingt.xdtouch;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.touch.panel.WindowWrapper;
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

        creatPanelWindow();
    }

    /**
     * 创建面板视图
     */
    private void creatPanelWindow() {

        final FloatView floatView = new FloatView(this);
        floatView.setWindowLayout(0, 0, 150, 150);
        floatView.setImageBitmap(null);
        mWindowWrapper.addView(floatView);

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "被点击", Toast.LENGTH_LONG).show();
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