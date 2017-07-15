package com.android.phone.assistant.widget;

import com.android.phone.assistant.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * 网络状态显示控件
 * @author wanlh
 *
 */
public class NetStatusHintView extends FrameLayout {
	
	public NetStatusHintView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public NetStatusHintView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public NetStatusHintView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.net_status_hint_layout, this, true);
		
	}
}
