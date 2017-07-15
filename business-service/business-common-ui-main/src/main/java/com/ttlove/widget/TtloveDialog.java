package com.ttlove.widget;

import com.android.ttlove.ttlovelibs.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TtloveDialog extends Dialog{
	
	private LinearLayout mDialogTitle;
	private TextView mTitleText;
	private LinearLayout mDialogCenter;
	private LinearLayout mBottomRootroot;
	private Button mLeftButton;
	private Button mRightButton;
	
	public TtloveDialog(Context context) {
		this(context, R.style.Theme_CustomDialog);
	//	super(context);
		// TODO Auto-generated constructor stub
	}

	public TtloveDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_layout);
		
		mDialogTitle = (LinearLayout) this.findViewById(R.id.dialog_title);
		mTitleText = (TextView)this.findViewById(R.id.title_text);
		
		mDialogCenter = (LinearLayout)this.findViewById(R.id.dialog_center_info);
		mBottomRootroot = (LinearLayout)this.findViewById(R.id.bottom_rootroot);
		
		mLeftButton = (Button) this.findViewById(R.id.LeftButton);
		mRightButton = (Button) this.findViewById(R.id.RightButton);
		
	}
	
	public void setOnClickRightButtonListener(View.OnClickListener l){
		mRightButton.setOnClickListener(l);
	}
	
	public void setOnClickLeftButtonListener(View.OnClickListener l){
		mLeftButton.setOnClickListener(l);
	}
	public void setVisibilityRightButton(int visibility){
		
		mRightButton.setVisibility(visibility);
	}
	
	public void setVisibilityLeftButton(int visibility){
		
		mLeftButton.setVisibility(visibility);
	}
	
	public void setVisibilityTitle(int visibility){
		
		mDialogTitle.setVisibility(visibility);
	}
	
	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		mTitleText.setText(titleId);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		mTitleText.setText(title);
	}

	public TextView getTitleText(){
		
		return mTitleText;
	}
	
	public void addCenterView(View child){
		if(mDialogCenter.getChildCount() > 0)
			mDialogCenter.removeAllViews();
		
		mDialogCenter.addView(child);
	}
	
	/**
	 *  ���öԻ��򱳾�
	 * @param topResid      top���ֱ�����ԴID
	 * @param centerResid 
	 * @param bottomResid
	 */
	public void setBackground(int topResid, int centerResid, int bottomResid){
		if(topResid > 0)
			mDialogTitle.setBackgroundResource(topResid);
		
		if(centerResid > 0)
			mDialogCenter.setBackgroundResource(centerResid);
		
		if(bottomResid > 0)
			mBottomRootroot.setBackgroundResource(bottomResid);
	}
	
	
}
