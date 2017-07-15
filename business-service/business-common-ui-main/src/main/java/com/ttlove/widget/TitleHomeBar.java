package com.ttlove.widget;


import com.android.ttlove.ttlovelibs.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class TitleHomeBar extends FrameLayout{
	private TextView mTitlePointsText;
	private TextView mTitleText;
	private ImageButton mTitleIcon;
	
	public TitleHomeBar(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public TitleHomeBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public TitleHomeBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.app_home_title, this, true);
		
		mTitleIcon = (ImageButton) this.findViewById(R.id.titleIcon);
		mTitleText = (TextView)this.findViewById(R.id.titleText);
		mTitlePointsText = (TextView) this.findViewById(R.id.titlePointsText);
	}
	
	public void setOnClickTitleIconListener(OnClickListener onClickListener){
		mTitleIcon.setOnClickListener(onClickListener);
	}
	
	public ImageButton getTitleButton(){
		return mTitleIcon;
	}
	public TextView getTitleTextView(){
		return mTitleText;
	}
	public void setTitleIconSrc(int resId){
		mTitleIcon.setImageResource(resId);
	}
	
	public void setTitleText(CharSequence text){
		mTitleText.setText(text);
	}
	
	public String getTitleText(){
		return mTitleText.getText().toString();
	}
	
	public void setTitlePointsText(CharSequence text){
		mTitlePointsText.setText(text);
	}
	
}
