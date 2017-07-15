package com.niunet.assistivetouch;

import com.assistivetouch.widget.LockScreen;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LockScreenActivity extends Activity{
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
	    LockScreen.creatInstance(this);
		LockScreen.getInstance().startLockScreen(LockScreen.getInstance().getDevicceAdmin());
	
        finish();
    }

}
