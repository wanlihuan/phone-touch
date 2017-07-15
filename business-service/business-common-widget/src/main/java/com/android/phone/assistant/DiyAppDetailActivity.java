package com.android.phone.assistant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.android.phone.assistant.util.YouMi;
import com.android.phone.assistant.widget.AppInfo;
import com.android.phone.assistant.widget.AppScreenShotScrollView;
import com.ttlove.widget.TitleBar;

public class DiyAppDetailActivity extends Activity {

    Context context;
    ImageView appIcon;
    TextView appName;
    TextView appSize;
    TextView version;
    Button downloadBtn;
    TextView appDesc;
    AppScreenShotScrollView appSSView;
    TextView loading;

    ArrayList<String> appScreenShots;

    Bitmap[] bitmaps;
    int adId;
    MyHandler myHandler = new MyHandler();

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        adId = getIntent().getExtras().getInt("adId");
        if(adId == -1){
            finish();
        }

        final AppInfo mAppInfo = YouMi.appInfoList.get(adId);
        appScreenShots = mAppInfo.appScreenShots;

        setContentView(R.layout.activity_diy_detail);
        
        TitleBar mTitleHomeBar = (TitleBar) this.findViewById(R.id.home_title_view);
        mTitleHomeBar.setTitleText(mAppInfo.appName);
        mTitleHomeBar.getTitleButton().setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DiyAppDetailActivity.this.finish();
			}
        });
        
        appIcon = (ImageView) findViewById(R.id.appIcon);
        appName = (TextView) findViewById(R.id.appName);
        appSize = (TextView) findViewById(R.id.appSize);
        version = (TextView) findViewById(R.id.version);
        downloadBtn = (Button) findViewById(R.id.downloadBtn);
        appDesc = (TextView) findViewById(R.id.appDesc);
        appSSView = (AppScreenShotScrollView) findViewById(R.id.appSSView);
        loading = (TextView) findViewById(R.id.loading);
        
        appIcon.setImageBitmap(mAppInfo.icon);
        appName.setText(mAppInfo.appName);
        appSize.setText(mAppInfo.size);
        version.setText("�汾��"+mAppInfo.versionName);
        appDesc.setText(mAppInfo.description);

        

        downloadBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                //DiyManager.downloadAd(context, mAppInfo.adId);
            }
        });


        Thread thread = new Thread(new Runnable() {
            public void run() {
                try{
                    if(appScreenShots!=null && appScreenShots.size()>0){
                        bitmaps = new Bitmap[appScreenShots.size()];

                        for(int i=0; i<appScreenShots.size(); i++){
                            bitmaps[i] = ImageLoader.loadBitmapFromNetWork(appScreenShots.get(i));
                        }

                        myHandler.sendEmptyMessageAtTime(-1, 0);

                    }
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            appSSView.initView();
            appSSView.setImages(bitmaps);
            loading.setVisibility(View.INVISIBLE);
        }
    }


}
