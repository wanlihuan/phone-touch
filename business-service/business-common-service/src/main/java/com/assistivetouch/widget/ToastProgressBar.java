package com.assistivetouch.widget;


import com.android.assistivetouch.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastProgressBar{
	private final int maxEnableProgress = 14; 
	
	private LinearLayout mRoot;
	private ImageView[] progressView = new ImageView[maxEnableProgress];
	private Context mContext;
	private Toast mToast;
	
	/**
	 * ���ȵ�λ progressUnit����/����
	 */
	private int showUnit = 1;
	protected int maxProgress = 0;
	private int mCurrentProgress = 0;

	public ToastProgressBar(Context context){
		
	}
	
	public ToastProgressBar(Context context, CharSequence text, int duration) {
		
		mContext = context;
		
		if(text == null || duration <= 0)
			return;
		
		mRoot = new LinearLayout(context);
		mRoot.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mRoot.setOrientation(LinearLayout.VERTICAL);
		mRoot.setGravity(Gravity.CENTER);
		mRoot.setBackgroundResource(R.drawable.ez_volume_panel_bg);

		TextView mTextView = new TextView(context);
        mTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.WHITE);
        mTextView.setText(text);
        mRoot.addView(mTextView);
        
        /*ImageView mImageView = new ImageView(context);
        mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mImageView.setImageDrawable(context.getResources().getDrawable(Rs.drawable.ez_voluem_panel_speaker));
        mChildParent.addView(mImageView);*/
        
        LinearLayout mProgressParent = new LinearLayout(context);
        mProgressParent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mProgressParent.setOrientation(LinearLayout.HORIZONTAL);
        mProgressParent.setGravity(Gravity.CENTER);
        mProgressParent.setPadding(0, 15, 0, 5);
        mRoot.addView(mProgressParent);
        
        for(int i = 0; i < maxEnableProgress; i++){
        	progressView[i] = new ImageView(context);
        	progressView[i].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
 //       	progressView[i].setImageDrawable(context.getResources().getDrawable(Rs.drawable.ez_process1_item_n));
        	mProgressParent.addView(progressView[i]);
        }
        
		mToast = new Toast(context);  
		mToast.setView(mRoot);
		mToast.setDuration(duration);
		mToast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 20);
		
	}
	
	public void setMaxProgress(int maxProgress){//ĿǰĬ������Ϊ���7
		this.maxProgress = maxProgress;
		showUnit = maxEnableProgress / maxProgress;//�����ÿһ��������ʾ���ٸ���
	}
	
	public void updateAndshowProgressBar(int progress) {
		if(progress > maxProgress)
			progress = maxProgress;
		
		if(progress < 0)
			progress = 0;
		
		mCurrentProgress = progress;
		for(int i = 0; i < mCurrentProgress * showUnit; i++){
			if(progressView[i] != null)
				progressView[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.ez_process1_item_h));
		}
		
		for(int i = maxEnableProgress - 1; i >= mCurrentProgress * showUnit; i--){
			if(progressView[i] != null)
				progressView[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.ez_process1_item_n));
		}
		
		if(null != mToast)
			mToast.show();
	}
	
	public int getMaxProgress(){
		return maxProgress;
	}
	public int getCurrentProgress(){
		return mCurrentProgress;
	}
	public void setBackgroundResource(int resid){
		if(null != mRoot)
			mRoot.setBackgroundResource(resid);
	}
	
}
