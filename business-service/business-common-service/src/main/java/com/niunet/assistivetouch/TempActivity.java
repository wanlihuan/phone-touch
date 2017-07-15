package com.niunet.assistivetouch;

import java.util.Map;

import com.android.assistivetouch.R;
import com.niunet.assistivetouch.CustomDialog.OnSelectedItemsListener;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class TempActivity extends Activity{

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent = getIntent();
		final DatabaseUtil mDatabaseUtil = new DatabaseUtil(this);  
		final UiManager mUiManager = new UiManager(this);
		
		CustomDialog mCustomDialog = new CustomDialog(this);
        final CustomDialog customDialog = mCustomDialog;
        mCustomDialog = null;
        
        final ViewInfo viewInfo = new ViewInfo();
		viewInfo.panelNum = intent.getStringExtra("panelNum");
		viewInfo.cellNum = intent.getStringExtra("cellNum");
		viewInfo.viewId = "shortcut" + viewInfo.cellNum;
		
        customDialog.show();
        customDialog.setOnDismissListener(new OnDismissListener(){

			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				
				TempActivity.this.finish();
			}
        });
        
		customDialog.setItemBackground(getResources().getDrawable(R.drawable.btn_default_normal_disable_focused));
		
		customDialog.loadingAllApp();
		
		customDialog.setOnSelectedItemsListener(new OnSelectedItemsListener(){
			public void onSelectedItems(Map<String,Integer> selectedMapPo) {
				// TODO Auto-generated method stub
				
			}
			public void onSelectedItem(int position) {
				// TODO Auto-generated method stub
				
				viewInfo.intentUri = customDialog.mAppList.get(position).intentUri;
				
				viewInfo.icon = customDialog.mAppList.get(position).icon;
				viewInfo.label = customDialog.mAppList.get(position).label;
				
				mUiManager.addItemToPanel(null
				,viewInfo
				,mDatabaseUtil);
			}
		});
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
		
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, 100);
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				//打开常用面板广播
				Intent openIntent = new Intent("android.touch.action.TOUCH_PANEL_OPEN");
				openIntent.putExtra("PanelNum", UiManager.PANEL_TYPE_2);
				sendBroadcast(openIntent);
				break;
			}
		}
	};
}
