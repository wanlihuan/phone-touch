package com.android.phone.demo;

import com.android.phone.assistant.DiySourceWallActivity;
import com.android.phone.assistant.R;
import com.android.phone.assistant.widget.UiComponentList;
import com.android.phone.assistant.widget.UiRecommendApp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class ConfigMainPageActivity extends DiySourceWallActivity{
	UiRecommendApp mRecommendAppUi;
    UiComponentList mComponentListUi;
//    UiTouchSetting mTouchSettingUi;
	String[] titleBarStr = {"����", " ����", "��������"};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
			 /*if(mTouchSettingUi == null){
				 mTouchSettingUi = new UiTouchSetting(this);
		        mTouchSettingUi.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		        		ViewGroup.LayoutParams.MATCH_PARENT)); 
		        addView2Bottom(mTouchSettingUi, selectedItem);
			 }*/
		 }else if(selectedItem == 2){
			 showOffersList();
		 }
		 
		super.onBottomItemSwitch(selectedItem);
	}
	
}
