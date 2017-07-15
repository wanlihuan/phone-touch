package com.niunet.assistivetouch;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


public class AssistiveTouchActivity extends Activity{
	TouchPanelView mTouchPanelView;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        int mode = getIntent().getIntExtra("mode", TouchPanelView.MODE_NORMAL);
        mTouchPanelView = new TouchPanelView(this, mode);
        
        setContentView(mTouchPanelView);
        
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();  
		mTouchPanelView.onDestroy();
	}

}
