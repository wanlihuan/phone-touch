package com.touch.panel.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.touch.panel.R;


public class Workspace extends ViewGroup implements View.OnClickListener{
	
	int centerCelllpx;
	int centerCelllpy;
	final int MAX_VERTICAL_COUNT = 3;
	final int MAX_HORIZONTAL_COUNT = 3;
	LinearLayout[] cellParent = new LinearLayout[MAX_VERTICAL_COUNT * MAX_HORIZONTAL_COUNT];
	BitmapView mBackagroundImageView;
	float bgSize = 0.0f;
	
	public Workspace(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
		
	}

	public Workspace(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public Workspace(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		bgSize = context.getResources().getDimensionPixelSize(R.dimen.size_dialog);
		
		mBackagroundImageView = new BitmapView(context);
		mBackagroundImageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT));
		mBackagroundImageView.setWidthHeight(bgSize, bgSize);
		mBackagroundImageView.setTag("bgpanel");

		setBackgroundForPanel(context.getResources().getDrawable(R.drawable.default_panel_p));

		addView(mBackagroundImageView);
		
		for(int i = 0; i < MAX_VERTICAL_COUNT * MAX_HORIZONTAL_COUNT; i++){
			cellParent[i] = new LinearLayout(context);
			cellParent[i].setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT));
			cellParent[i].setOnClickListener(this);
			cellParent[i].setTag(""+i);
			//cellParent[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_ic_favor_null));
			addView(cellParent[i]);
		}
	}
	
	OnClickItemListener mOnClickItemListener;
	public void setOnClickItemListener(OnClickItemListener onClickItemListener){
		mOnClickItemListener = onClickItemListener;
	}
	public interface OnClickItemListener{
		public void onClickItem(ViewGroup parentView, String cellNum);
	}
	
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(mOnClickItemListener != null)
			mOnClickItemListener.onClickItem((ViewGroup)view, (String)view.getTag());
	}
	
	public void setFlashingLightning(boolean openFlag){
		mBackagroundImageView.setFlashingLightning(openFlag);
	}
	
	public void setBackgroundForPanel(Drawable drawable){
			BitmapDrawable bd = (BitmapDrawable) drawable;
			mBackagroundImageView.setBackgroundBitmap(bd.getBitmap());	
	}
	
	public void setBitmapBackgroundForPanel(Bitmap bitmap){
			mBackagroundImageView.setBackgroundBitmap(bitmap);	
	}
	
	public void setBackgroundVisibilityForNullSpace(boolean bgVisibility){
		for(int i = 0; i < MAX_VERTICAL_COUNT * MAX_HORIZONTAL_COUNT; i++){
			if(bgVisibility){
				cellParent[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_ic_favor_null));
			}else{
				cellParent[i].setBackgroundDrawable(null);
			}
		}
	}
	
	public void setBackgroundVisibilityForNullSpace(int cellNum, boolean bgVisibility){
		if(bgVisibility){
			cellParent[cellNum].setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_ic_favor_null));
		}else{
			cellParent[cellNum].setBackgroundDrawable(null);
		}
	}
	public void addChildView(View childView, int cellNum){
		//if(cellParent[cellNum].getChildCount() > 0)
		//	cellParent[cellNum].removeView(childView);
		
		cellParent[cellNum].addView(childView);
		cellParent[cellNum].setBackgroundDrawable(null);
	}
	
	public void clearWorkspace(){
		for(int i = 0; i < MAX_VERTICAL_COUNT * MAX_HORIZONTAL_COUNT; i++)
			cellParent[i].removeAllViews();
	}
	
	/**
	 * �Ƴ�cellNumλ�õ�����View
	 * @param cellNum
	 */
	public void removeChildView(int cellNum){
		if(cellParent[cellNum].getChildCount() > 0)
			cellParent[cellNum].removeAllViews();
	}
	
	/**
	 * �Ƴ�cellNumλ�õ�ָ��view
	 * @param cellNum
	 * @param view
	 */
	public void removeChildView(int cellNum, View view){
		
		for(int i = 0; i < cellParent[cellNum].getChildCount(); i++){
			if(cellParent[cellNum].getChildAt(i).getTag() == view.getTag()){
				cellParent[cellNum].removeView(view);
			}
		}
	}
	
	public interface ExitAnimationCallbacks {
		
        public void exitAnimationEnd();
	}
	public interface EnterAnimationCallbacks {
		
        public void enterAnimationEnd();
	}

	public void startSwitchEnterTranslateAnimation(final EnterAnimationCallbacks enterAnimationCallbacks){
		(new Handler()).post(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub							
				final int count = getChildCount();
				TranslateAnimation exitAnim = null;
		        for (int i = 0; i < count; i++) {
		        	
		        	if(getChildAt(i).getTag().equals("bgpanel"))
		        		continue;
		        	
		        	ViewGroup viewGroup = (ViewGroup) getChildAt(i);
		        	
		        	LayoutParams lp = (LayoutParams) viewGroup.getLayoutParams();
		        	exitAnim = new TranslateAnimation(centerCelllpx - lp.x, 0, centerCelllpy - lp.y, 0);
		        	if(i == count - 1)
		        		exitAnim.setDuration(160);//�����һ������������
		        	else
		        		exitAnim.setDuration(150);
		    		viewGroup.startAnimation(exitAnim);
		        }
		        if(exitAnim != null)
			        exitAnim.setAnimationListener(new AnimationListener(){
			
						public void onAnimationEnd(Animation arg0) {
							// TODO Auto-generated method stub
							if(enterAnimationCallbacks != null)
								enterAnimationCallbacks.enterAnimationEnd();
						}
			
						public void onAnimationRepeat(Animation arg0) {
							// TODO Auto-generated method stub
							
						}
			
						public void onAnimationStart(Animation arg0) {
							// TODO Auto-generated method stub
							
						}
			        	
			        });
			}		
		});
	}
	
	
	public void startSwitchExitAlphaAnimation(){
		(new Handler()).post(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub							
				AlphaAnimation mAlphaAnimation = new AlphaAnimation(1.0f, 0.7f);
				mAlphaAnimation.setDuration(100);
				mAlphaAnimation.setFillAfter(true);
				Workspace.this.startAnimation(mAlphaAnimation);
			}		
		});
	}
	public void startSwitchEnterAlphaAnimation(){
		(new Handler()).post(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub							
				AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.7f, 1.0f);
				mAlphaAnimation.setDuration(100);
				mAlphaAnimation.setFillAfter(true);
				Workspace.this.startAnimation(mAlphaAnimation);
			}		
		});
	}
	
	public void startSwitchExitTranslateAnimation(final ExitAnimationCallbacks exitAnimationCallbacks){
		(new Handler()).post(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub							
				startExitTranslateAnimation(exitAnimationCallbacks);
			}		
		});
	}
	
	private void startExitTranslateAnimation(final ExitAnimationCallbacks exitAnimationCallbacks){
		final int count = getChildCount();
		TranslateAnimation exitAnim = null;
        for (int i = 0; i < count; i++) {
        	if(getChildAt(i).getTag().equals("bgpanel"))
        		continue;
        	
        	ViewGroup viewGroup = (ViewGroup) getChildAt(i);
        	
        	LayoutParams lp = (LayoutParams) viewGroup.getLayoutParams();
        	exitAnim = new TranslateAnimation(0, centerCelllpx - lp.x, 0, centerCelllpy - lp.y);
        	if(i == count - 1)
        		exitAnim.setDuration(85);
        	else
        		exitAnim.setDuration(80);
    		exitAnim.setFillAfter(true);
    		viewGroup.startAnimation(exitAnim);
        }
        if(exitAnim != null)
	        exitAnim.setAnimationListener(new AnimationListener(){
				public void onAnimationEnd(Animation arg0) {
					// TODO Auto-generated method stub
					if(exitAnimationCallbacks != null)
						exitAnimationCallbacks.exitAnimationEnd();
				}
	
				public void onAnimationRepeat(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
				public void onAnimationStart(Animation arg0) {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		final int count = getChildCount();
        for (int i = 0; i < count; i++) {
        	View view = getChildAt(i);
        	LayoutParams lp = (LayoutParams) view.getLayoutParams(); 
            view.layout(lp.x, lp.y, lp.x + lp.width, lp.y + lp.height);
        }
	}

	 @SuppressLint("DrawAllocation")
	@Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        // TODO: currently ignoring padding            
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	    	int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
	        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);            
	        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
	        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);
	        
	        int measuredChildHeight = 0;
	        int measuredChildWidth = 0;
	        
	        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
	                throw new RuntimeException("Dockbar cannot have UNSPECIFIED dimensions");
	        }
	       
	        int cellWidth = widthSpecSize / MAX_HORIZONTAL_COUNT;
        	int cellHeight = heightSpecSize / MAX_VERTICAL_COUNT;
        	
	        final int count = getChildCount();
	        int num = 0;
	        
	        //��ȡ�������߶�
	        for (int i = 0; i < count; i++) {
	        	View childView = getChildAt(i);
	        	childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
	          	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        	
	        	measuredChildWidth = childView.getMeasuredWidth();
	        	measuredChildHeight = childView.getMeasuredHeight();
	        	LayoutParams lp = (LayoutParams) childView.getLayoutParams();
	        	
	        	if(!childView.getTag().equals("bgpanel")){
	        		num++;
	            	int cellx = (num - 1) % MAX_HORIZONTAL_COUNT;
	            	int celly = (num - 1) / MAX_HORIZONTAL_COUNT;
	            	
	            	int cellCenterx = cellWidth/2 + cellx * cellWidth;
	            	int cellCentery = cellHeight/2 + celly * cellHeight;
	            	
	            	
		        	lp.width = Math.min(measuredChildWidth, cellWidth);
	            	lp.height = measuredChildHeight;
	            	
		        	lp.x =  cellCenterx - (measuredChildWidth / 2);
		        	lp.y = cellCentery - (measuredChildHeight / 2);
		        	
		        	if(cellx == 1 && celly == 1){
			        	centerCelllpx = lp.x;//�����Ǹ�view�����Ͻ�����
			        	centerCelllpy = lp.y;
		        	}
	        	}else{
	        		lp.width = (int) bgSize;
	            	lp.height = (int) bgSize;
	            	
		        	lp.x =  0;
		        	lp.y = 0;
	        	}
	        	childView.setLayoutParams(lp);
	        }
	        
	        setMeasuredDimension(widthSpecSize, heightSpecSize); 
	 }
	 
	 public class LayoutParams extends MarginLayoutParams {

		 public LayoutParams(int wrapContent, int wrapContent2) {
	            super(wrapContent, wrapContent2);
	     }

         public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
         }

	        /**
	         * ���ֵ�X����
	         */
	        int x;
	        
	        /**
	         * ���ֵ�Y����
	         */
	        int y;
	        
	        /**
	         * �ڵڼ�ѡ��
	         */
	        int cell;

	 }
	
}
