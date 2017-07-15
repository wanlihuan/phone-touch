package com.ttlove.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

public class RadioGroup extends LinearLayout implements PreferenceRadioView.OnRadioCheckedChangedListener{

	List<PreferenceRadioView> mPreferenceRadioView;
	boolean isInitChecked = false;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			case 0:
				boolean checked = (Boolean) msg.obj;
				int item = msg.arg1;
				
				mPreferenceRadioView.get(item).getRadioButton().setChecked(checked);
				
				break;
			}
		}
	};
	
	public RadioGroup(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public RadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		 mPreferenceRadioView = new ArrayList<PreferenceRadioView>();
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int count = getChildCount();
		mPreferenceRadioView.clear();
		
		View view;
		for(int i = 0; i < count; i++){
			view = getChildAt(i);
			if(view instanceof PreferenceRadioView){
				mPreferenceRadioView.add((PreferenceRadioView)view);  
				((PreferenceRadioView)view).setOnRadioCheckedChangedListener(this);
			}
		}
	}

	public int getChildRadioCount(){
		int count = getChildCount();
		int radioViewCount = 0;
		View view;
		for(int i = 0; i < count; i++){
			view = getChildAt(i);
			if(view instanceof PreferenceRadioView){
				radioViewCount++;
			}
		}
		
		return radioViewCount;
	}
	
	public void initCheckedItem(int item, boolean checked){
		isInitChecked = true;
		Message msg = new Message();
		msg.arg1 = item;
		msg.obj = checked;
		msg.what = 0;
		mHandler.removeMessages(0);
		mHandler.sendMessageDelayed(msg, 60);
		
	}
	
	public void onRadioCheckedChanged(CompoundButton compoundButton, boolean checked) {
		// TODO Auto-generated method stub
		int checkedItem = -1;
		Log.d("tag", "onRadioCheckedChanged onRadioCheckedChanged isInitChecked="+isInitChecked);
		PreferenceRadioView preferenceRadioView = null;
		for(int i = 0; i < mPreferenceRadioView.size(); i++){
			preferenceRadioView = mPreferenceRadioView.get(i);
			if(compoundButton == preferenceRadioView.getRadioButton()){
				checkedItem = i;
				preferenceRadioView.getRadioButton().setChecked(true);
			}else
				preferenceRadioView.getRadioButton().setChecked(false);
		}
		
		if(mOnItemCheckedChangedListener != null && !isInitChecked)
			mOnItemCheckedChangedListener.onItemCheckedChanged(checkedItem, mPreferenceRadioView.get(checkedItem).getRadioButton().isChecked());
		
		isInitChecked = false;
	}

	OnItemCheckedChangedListener mOnItemCheckedChangedListener = null;
	public void setOnItemCheckedChangedListener(OnItemCheckedChangedListener onItemCheckedChangedListener){
		mOnItemCheckedChangedListener = onItemCheckedChangedListener;
	}
	
	public interface OnItemCheckedChangedListener{
		void onItemCheckedChanged(int item, boolean checked);
	}
	
}
