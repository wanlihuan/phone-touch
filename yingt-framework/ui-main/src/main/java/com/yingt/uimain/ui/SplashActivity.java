package com.yingt.uimain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.yingt.uimain.R;
import com.yingt.uimain.base.BaseActivity;
import com.orhanobut.logger.Logger;
import com.yingt.uimain.base.BaseToolbar;

public class SplashActivity extends BaseActivity {

    @Override
    public boolean isRemovedToolbar() {
        return true;
    }

    @Override
    public BaseToolbar getLayoutToolbarView() {
        return null;
    }

    @Override
    public int getLayoutResId() {
        return 0;//R.layout.yingt_uimain_activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 状态栏透明设置
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);

//        hideToolbar();
        final Class tagClass = onInitTagMainActivity();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(tagClass == null) {
                    Intent intent =  new Intent(SplashActivity.this, MainTabActivity.class);
                    intent.putExtra("TourClass", onInitTagTourActivity());
                    startActivity(intent);
                }else {
                    Intent intent =  new Intent(SplashActivity.this, tagClass);
                    intent.putExtra("TourClass", onInitTagTourActivity());
                    startActivity(intent);
                }

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Logger.d("aaaaa--------");
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                        Logger.d("bbbbbb--------");
                    }
                }, 500);
            }
        }, 800);
    }

    /**
     * 主界面的 Activity 页面
     * @return
     */
    public Class onInitTagMainActivity(){
        return null;
    }

    /**
     * 初始化预览界面类
     * @return
     */
    public Class onInitTagTourActivity(){
        return null;
    }
}
