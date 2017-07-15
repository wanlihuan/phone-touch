package com.ttlove.widget;

import com.android.ttlove.ttlovelibs.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreferenceRadioView extends FrameLayout{
	private RelativeLayout mItemParentView;
	private ImageView mLeftIconView;
	private TextView mTitleView;
	private TextView mSummaryView;
	private View mDividerBottomView;
	private TextView mHintTextView;
	private RadioButton mRadioIconView;
	
	public PreferenceRadioView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public PreferenceRadioView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PreferenceRadioView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.preference_item_radio_layout, this, true);
		
		mItemParentView = (RelativeLayout) this.findViewById(R.id.ItemParentView);
		
		mLeftIconView = (ImageView) this.findViewById(R.id.left_icon);
		mTitleView = (TextView) this.findViewById(R.id.title);
		mSummaryView = (TextView) this.findViewById(R.id.summary);
		mDividerBottomView = this.findViewById(R.id.divider_bottom);
		mHintTextView = (TextView) this.findViewById(R.id.HintTextView);
		mRadioIconView = (RadioButton) this.findViewById(R.id.radio_icon_view);
		
		mRadioIconView.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
				// TODO Auto-generated method stub
				if(mOnRadioCheckedChangedListener != null && checked == true)
					mOnRadioCheckedChangedListener.onRadioCheckedChanged(compoundButton, checked);
			}
		});
		
		setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
				mRadioIconView.setChecked(true);
			}
		});
	}
	
	public RadioButton getRadioButton(){
		
		return mRadioIconView;
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
	
	public void setSummaryText(CharSequence text){
		mSummaryView.setText(text);
	}
	
	public void setVisibilitySummary(int visibility){
		if(visibility == View.GONE)
			mTitleView.setTextSize(20);
		else
			mTitleView.setTextSize(19);
		
		mSummaryView.setVisibility(visibility);
	}
	
	public void setVisibilityDividerBottom(int visibility){
		mDividerBottomView.setVisibility(visibility);
	}

	
	public void setHintText(CharSequence text){
		mHintTextView.setText(text);
	}
	
	/**
	 * 设置单选左边的textView
	 * @param visibility
	 */
	public void setVisibilityHintTextView(int visibility){
		mHintTextView.setVisibility(visibility);
	}
	
	OnRadioCheckedChangedListener mOnRadioCheckedChangedListener = null;
	public void setOnRadioCheckedChangedListener(OnRadioCheckedChangedListener onRadioCheckedListener){
		mOnRadioCheckedChangedListener = onRadioCheckedListener;
	}
	public interface OnRadioCheckedChangedListener{
		void onRadioCheckedChanged(CompoundButton compoundButton, boolean checked);
	}
	
}
