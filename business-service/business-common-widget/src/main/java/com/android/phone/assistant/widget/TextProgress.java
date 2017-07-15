package com.android.phone.assistant.widget;

import com.android.phone.assistant.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class TextProgress extends FrameLayout {
	
	public TextProgress(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public TextProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public TextProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.text_progress_layout, this, true);
		
	}
}
