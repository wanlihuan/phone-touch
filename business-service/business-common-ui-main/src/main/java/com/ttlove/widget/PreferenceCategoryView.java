package com.ttlove.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreferenceCategoryView extends FrameLayout{
	private Context mContext;
	private TextView mTitleView;
	private View mDividerView;
	
	public PreferenceCategoryView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public PreferenceCategoryView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PreferenceCategoryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		LinearLayout mRoot = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mRoot.setLayoutParams(lp);
		mRoot.setGravity(Gravity.CENTER_VERTICAL);
		mRoot.setOrientation(LinearLayout.VERTICAL);
		this.addView(mRoot);
		
		mTitleView = new TextView(context); 
		mTitleView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		mTitleView.setPadding(10, 0, -1, 5);
		mTitleView.setTextColor(Color.rgb(0xff, 0x8c, 0x00));
		mTitleView.setText("mode title test");
		
		mDividerView = new View(context);
		mDividerView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
		mDividerView.setBackgroundColor(Color.rgb(0xff, 0x8c, 0x00));
		mRoot.addView(mTitleView);
		mRoot.addView(mDividerView);
	}
	
	public void setTitleText(String str){
		mTitleView.setText(str);
	}
	
	public void setTitleColor(int color){
		mTitleView.setTextColor(color);
	}
	
	public void setDividerColor(int color){
		mDividerView.setBackgroundColor(color);
	}
}
