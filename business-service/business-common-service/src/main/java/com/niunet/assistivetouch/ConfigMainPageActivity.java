package com.niunet.assistivetouch;

import net.youmi.android.dev.OnlineConfigCallBack;

import com.android.phone.assistant.DiySourceWallActivity;
import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.YouMi;
import com.android.phone.assistant.widget.UiComponentList;
import com.android.phone.assistant.widget.UiRecommendApp;
import com.assistivetouch.widget.TouchSettingView;
import com.android.assistivetouch.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;

public class ConfigMainPageActivity extends DiySourceWallActivity{
	UiRecommendApp mRecommendAppUi;
    UiComponentList mComponentListUi;
    TouchSettingView mTouchSettingUi;
	String[] titleBarStr = {"����", " ����", "��������"};   
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		
		initItems(
        		this,
				3, 
				titleBarStr, 
				new Drawable[]{
						getResources().getDrawable(R.drawable.selector_theme_icon)
						,getResources().getDrawable(R.drawable.selector_control_panel_icon)
						,getResources().getDrawable(R.drawable.eoe_btn_subject)
						},
				0
		);
		
		WindowManager winMgr = (WindowManager) getSystemService(Context.WINDOW_SERVICE);            
		DisplayMetrics dm = new DisplayMetrics();
		winMgr.getDefaultDisplay().getMetrics(dm);  
	   
		CommonUtil.widthPixels = dm.widthPixels;           
		CommonUtil.heightPixels = dm.heightPixels; 
	}

	@Override
	public void onBottomItemSwitch(int selectedItem) {
		// TODO Auto-generated method stub
		
		setTitleText(titleBarStr[selectedItem]);
		 
		 if(selectedItem == 0){
			 if(mComponentListUi == null){  //�����б�����
				 mComponentListUi = new UiComponentList(this); 
			        mComponentListUi.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
			        		LayoutParams.MATCH_PARENT));
			        addView2Bottom(mComponentListUi, selectedItem);
			 }
			 
		 }else if(selectedItem == 1){//Touch����
			 if(mTouchSettingUi == null){
				mTouchSettingUi = new TouchSettingView(this);
		        mTouchSettingUi.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		        		LayoutParams.MATCH_PARENT));
		        addView2Bottom(mTouchSettingUi, selectedItem);
		        
		       /* mTouchSettingUi = new SettingView(this);
		        mTouchSettingUi.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		        		ViewGroup.LayoutParams.MATCH_PARENT)); 
		        addView2Bottom(mTouchSettingUi, selectedItem);*/
			 }
		 }else if(selectedItem == 2){
			 showOffersList();
			 
			// �������ù���,�Ƿ���ʾ�岥���
			YouMi.getInstance().asyncGetOnlineConfig("isShowSporAds", new OnlineConfigCallBack(){

				public void onGetOnlineConfigFailed(String key) {
					// TODO Auto-generated method stub
					Log.d("tag", "asyncGetOnlineConfig key="+key+"value=failed");
				}

				public void onGetOnlineConfigSuccessful(String key, String value) {
					// TODO Auto-generated method stub
					Log.d("tag", "asyncGetOnlineConfig key="+key+",value="+value);
					//��ʾ�岥���
					if("true".equals(value))
						YouMi.getInstance().showSpotAds();
				}
			});
		 }
		 
		super.onBottomItemSwitch(selectedItem);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mTouchSettingUi != null)
			mTouchSettingUi.onResume();
	}
	
}
