package com.yingt.xdtouch;

import android.content.Intent;
import android.os.Bundle;

import com.niunet.assistivetouch.FloatingService;
import com.yingt.uimain.ui.SplashActivity;

/**
 * Created by laihuan.wan on 2017/7/12 0012.
 */

public class InitActivity extends SplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent serIntent = new Intent(this, FloatingService.class);
//        startService(serIntent);
    }

    @Override
    public Class onInitTagMainActivity() {
        return MainActivity.class;
    }

    @Override
    public Class onInitTagTourActivity() {
        return MyProductTourActivity.class;
    }
}
