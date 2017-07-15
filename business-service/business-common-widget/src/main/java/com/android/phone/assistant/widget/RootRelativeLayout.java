package com.android.phone.assistant.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class RootRelativeLayout extends RelativeLayout{
	LinearLayout mBottomView;
	int animationXT = 0;
	int animationYT = 0;
	
	public static final int ROOT_STATUS_NORMAL = 0;
	public static final int ROOT_STATUS_OPEN = 1;
	public static int rootStatus = ROOT_STATUS_NORMAL;
	
 	public RootRelativeLayout(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public RootRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public RootRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mBottomView = new LinearLayout(context);
		mBottomView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
	   			ViewGroup.LayoutParams.WRAP_CONTENT));
		mBottomView.setBackgroundColor(Color.BLACK);
	//	mBottomView.setVisibility(View.GONE);
		addView(mBottomView);
		
	}

	/**
	 * 左边打开还是关闭的切换
	 * @param durationMillis 完全显示需要的时间
	 */
	public void statusSwitch(long durationMillis){
		
		final int count = getChildCount();
		View childView_2 = null;
		
		if(rootStatus == ROOT_STATUS_NORMAL){
			rootStatus = ROOT_STATUS_OPEN;
			for (int i = 0; i < count; i++) {
	        	View child = getChildAt(i);
	        	LayoutParams lp = (LayoutParams) child.getLayoutParams();
	        	if(child.getVisibility() != View.GONE){
		        	if(i == 0){
		        		animationXT = lp.width;
		        		mLayoutXy[0].lx = 0;
		        		mLayoutXy[0].rx = lp.width;
		        		mLayoutXy[0].ty = 0;
		        		mLayoutXy[0].by = lp.height;
		        		
		        	}else{
		        		childView_2 = child;
		        		mLayoutXy[1].lx = animationXT;
		        		mLayoutXy[1].rx = lp.width + animationXT;
		        		mLayoutXy[1].ty = 0;
		        		mLayoutXy[1].by = lp.height;
		        	}
		        //	child.setLayoutParams(lp);
	        	}
	        }
			
		}else{
			rootStatus = ROOT_STATUS_NORMAL;
			
			for (int i = 0; i < count; i++) {
	        	View child = getChildAt(i);
	        	LayoutParams lp = (LayoutParams) child.getLayoutParams();
	        	if(child.getVisibility() != View.GONE){
		        	if(i == 0){
		        		animationXT = -lp.width;
		        		mLayoutXy[0].lx = -lp.width;
		        		mLayoutXy[0].rx = 0;
		        		mLayoutXy[0].ty = 0;
		        		mLayoutXy[0].by = lp.height;
		        		
		        	}else{
		        		childView_2 = child;
		        		mLayoutXy[1].lx = 0;
		        		mLayoutXy[1].rx = lp.width;
		        		mLayoutXy[1].ty = 0;
		        		mLayoutXy[1].by = lp.height;
		        	}
		        	//child.setLayoutParams(lp);
	        	}
	        }
		}
		
		TranslateAnimation bottomAnim = new TranslateAnimation(0, animationXT, 0, animationYT);
		bottomAnim.setDuration(durationMillis);
		mBottomView.startAnimation(bottomAnim);
		childView_2.startAnimation(bottomAnim);
		
		bottomAnim.setAnimationListener(new AnimationListener(){

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
				RootRelativeLayout.this.requestLayout();
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
	//	super.onLayout(changed, l, t, r, b);
		 final int count = getChildCount();
	        
	        //设置布局参数
	        for (int i = 0; i < count; i++) {
	        	View childView = getChildAt(i);
	        	//LayoutParams lp = (LayoutParams) childView.getLayoutParams(); 
	        	/*if(i == 0){
	        		childView.layout(-lp.width + animationXT, 0 + animationYT, 0 + animationXT, lp.height + animationYT);
	        	}else{
	        		childView.layout(l + animationXT, t, r + animationXT, b);
	        	}*/
	        	if(childView.getVisibility() != View.GONE){
		        //	if(i == 0){
		        		childView.layout(mLayoutXy[i].lx, mLayoutXy[i].ty, mLayoutXy[i].rx, mLayoutXy[i].by);
		        	/*}else{
		        		childView.layout(l + animationXT, t, r + animationXT, b);
		        	}*/
	        	}
	        }
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO: currently ignoring padding            
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    	int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize =  MeasureSpec.getSize(widthMeasureSpec);            
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize =  MeasureSpec.getSize(heightMeasureSpec);
        
        if (widthSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.UNSPECIFIED) {
                throw new RuntimeException("Dockbar cannot have UNSPECIFIED dimensions");
        }
        
        final int count = getChildCount();
        
        //设置布局参数
        for (int i = 0; i < count; i++) {
        	View childView = getChildAt(i);
        	LayoutParams lp = (LayoutParams) childView.getLayoutParams();
        	
        	if(childView.getVisibility() != View.GONE){
	        	if(i == 0){
	        		lp.width = (int) (widthSpecSize * 0.33f);
	                lp.height = heightSpecSize;
	                mLayoutXy[0].lx += -lp.width;
	                mLayoutXy[0].rx += 0;
	                mLayoutXy[0].ty += 0;
	                mLayoutXy[0].by += lp.height;
	                
	        	}else{
	        		lp.width = widthSpecSize;
	                lp.height = heightSpecSize;
	                mLayoutXy[1].lx += 0;
	                mLayoutXy[1].rx += lp.width;
	                mLayoutXy[1].ty += 0;
	                mLayoutXy[1].by += lp.height;
	        	}
        	}
        	childView.setLayoutParams(lp);
        } 

        setMeasuredDimension(widthSpecSize, heightSpecSize); 
   }
	
	LayoutXy[] mLayoutXy = {new LayoutXy(), new LayoutXy()};
	 public class LayoutXy {

        int lx;
        int rx;
        int ty;
        int by;
	       
	 }
}
