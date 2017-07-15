package com.assistivetouch.widget;

import com.android.assistivetouch.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppItemView extends FrameLayout implements Checkable {

    private Context mContext;
    private boolean mChecked;//判断该选项是否被选上的标志量
    private TextView mTextView = null;
    private ImageView mSecletView = null;
    RelativeLayout mGridRoot;
    public AppItemView(Context context) {
        this(context, null, 0);
    }

    public AppItemView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);
    }

    public AppItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.grid_item, this);
        mGridRoot = (RelativeLayout) this.findViewById(R.id.grid_root);
        mTextView = (TextView) findViewById(R.id.text_view);
        mSecletView = (ImageView) findViewById(R.id.select);
    }

    public TextView getTextView(){
    	return mTextView;
    }
    
    public void setItemBackground(Drawable drawable){
    	mGridRoot.setBackgroundDrawable(drawable);
    }
    
    public void setChecked(boolean checked) {
        // TODO Auto-generated method stub
        mChecked = checked;
      //  mGridRoot.setBackgroundDrawable(checked ? getResources().getDrawable(
      //          R.drawable.btn_default_normal_disable_focused) : null);
        mSecletView.setVisibility(checked ? View.VISIBLE : View.GONE);//选上了则显示小勾图片
    }

    public boolean isChecked() {
        // TODO Auto-generated method stub
        return mChecked;
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    public void setIcon(Drawable drawable) {
        if (mTextView != null) {
        	mTextView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }
    public void setLabel(CharSequence text) {
        if (mTextView != null) {
        	mTextView.setTextColor(Color.BLACK);
        	mTextView.setText(text);
        }
    }
}
