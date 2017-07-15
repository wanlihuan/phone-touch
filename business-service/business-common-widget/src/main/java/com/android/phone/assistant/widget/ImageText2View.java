package com.android.phone.assistant.widget;

import com.android.phone.assistant.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageText2View extends FrameLayout{
	private LinearLayout mRoot;
	private ImageView mIcon;
	private TextView mTitle;
	private TextView mSummary;
	
	public ImageText2View(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ImageText2View(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	private Context mContext;

	public ImageText2View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.component_list_grid_item, this, true);
		
		mRoot = (LinearLayout) this.findViewById(R.id.root);
		
		mIcon = (ImageView) this.findViewById(R.id.icon);
		mTitle = (TextView) this.findViewById(R.id.title);
		mSummary = (TextView) this.findViewById(R.id.summary);
	}
	
	public void setIcon(Drawable drawable){
		mIcon.setBackgroundDrawable(drawable);
	}
	
	public void setTitle(CharSequence title){
		mTitle.setText(title);
	}
	
	public CharSequence getTitle(){
		return mTitle.getText();
	}
	public void setSummary(CharSequence summary){
		mSummary.setText(summary);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		super.setOnClickListener(l);
		final OnClickListener mOnClickListener = l;
		mRoot.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mOnClickListener.onClick(ImageText2View.this);
			}
		});
	}
}
