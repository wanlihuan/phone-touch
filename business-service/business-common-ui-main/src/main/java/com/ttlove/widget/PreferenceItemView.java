package com.ttlove.widget;

import com.android.ttlove.ttlovelibs.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreferenceItemView extends FrameLayout{
	
	private RelativeLayout mItemParentView;
	private ImageView mLeftIconView;
	private TextView mTitleView;
	private TextView mSummaryView;
	private View mDividerBottomView;
	
	public PreferenceItemView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public PreferenceItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PreferenceItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.preference_item_layout, this, true);
		
		mItemParentView = (RelativeLayout) this.findViewById(R.id.ItemParentView);
		
		mLeftIconView = (ImageView) this.findViewById(R.id.left_icon);
		mTitleView = (TextView) this.findViewById(R.id.title);
		mSummaryView = (TextView) this.findViewById(R.id.summary);
		mDividerBottomView = this.findViewById(R.id.divider_bottom);
	}
	
	public void setOnClickListener(OnClickListener l){
		mItemParentView.setOnClickListener(l);
	}
	
	public void setImageDrawableLeftIcon(Drawable drawable){
		mLeftIconView.setImageDrawable(drawable);
	}
	public void setVisibilityLeftIcon(int visibility){
		mDividerBottomView.setVisibility(visibility);
	}
	
	public void setTitleText(CharSequence text){
		mTitleView.setText(text);
	}
	
	public void setVisibilitySummary(int visibility){
		if(visibility == View.GONE)
			mTitleView.setTextSize(20);
		else
			mTitleView.setTextSize(19);
		
		mSummaryView.setVisibility(visibility);
	}
	
	public void setSummaryText(CharSequence text){
		mSummaryView.setText(text);
	}
	
	public void setVisibilityDividerBottom(int visibility){
		mDividerBottomView.setVisibility(visibility);
	}
	
}
