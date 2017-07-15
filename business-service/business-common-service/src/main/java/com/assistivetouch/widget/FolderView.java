package com.assistivetouch.widget;

import com.android.assistivetouch.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FolderView extends FrameLayout{
	private Context mContext = null;
	private ImageView topBodyView;
	private ImageView bottomBodyView;
	private FolderTaskView mFolderTaskView;
	private int mTopBodyHeight = 0;
	private int mBottomBodyHeight = 0;
	private TranslateAnimation  mTopBodyViewOpenAnim;
	private TranslateAnimation mTopBodyViewCloseAnim;
	private TranslateAnimation mBottomBodyViewOpenAnim;
	private  TranslateAnimation mBottomBodyViewCloseAnim;
	private int mTopBodyAnimOffset = 180;
	private int mBottomBodyAnimOffset = 180;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			  mTopBodyAnimOffset = mFolderTaskView.getViewHeight()/2;
		      mBottomBodyAnimOffset = mTopBodyAnimOffset;
		      
			  initAnimationRes(mTopBodyAnimOffset, mBottomBodyAnimOffset); 
			  
			  if(topBodyView != null && mTopBodyViewOpenAnim != null)
				  topBodyView.startAnimation(mTopBodyViewOpenAnim);
			  if(bottomBodyView != null && mBottomBodyViewOpenAnim != null)
				  bottomBodyView.startAnimation(mBottomBodyViewOpenAnim);
		}
	};
	
	public FolderView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public FolderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public FolderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.folder_layout, this, true);
		
		mFolderTaskView = (FolderTaskView)findViewById(R.id.FolderTaskView);
		topBodyView = (ImageView)findViewById(R.id.TopBodyView);
		bottomBodyView = (ImageView)findViewById(R.id.BottomBodyView);
		
	}

	public void initAnimationRes(int topBodyAnimOff, int bottomBodyAnimOff){
		 if(topBodyAnimOff > 0){	
			 mTopBodyViewOpenAnim = new TranslateAnimation(0, 0, 0, -topBodyAnimOff);
			 mTopBodyViewOpenAnim.setFillAfter(true);
			 mTopBodyViewOpenAnim.setDuration(300);
		   	 
			 mTopBodyViewCloseAnim = new TranslateAnimation(0, 0, -topBodyAnimOff, 0);
			 mTopBodyViewCloseAnim.setDuration(320);//稍微比其他动画时间长些
		 }
	   	 
		 if(bottomBodyAnimOff > 0){	
		   	 mBottomBodyViewOpenAnim = new TranslateAnimation(0, 0, 0, bottomBodyAnimOff);
		   	 mBottomBodyViewOpenAnim.setFillAfter(true);
		     mBottomBodyViewOpenAnim.setDuration(300);
		   	 
		   	 mBottomBodyViewCloseAnim = new TranslateAnimation(0, 0, bottomBodyAnimOff, 0);
		     mBottomBodyViewCloseAnim.setDuration(300);
		 }
   }

  public void openFilder(Bitmap topBodyBitmap, Bitmap bottomBodyBitmap){
	  //mTopBodyHeight = topBodyBitmap.getHeight();
	  //mBottomBodyHeight = bottomBodyBitmap.getHeight();
	  
	  //创建并加载近期任务
      (new RecentTaskUtil(mContext)).loadRecentTask(mFolderTaskView); 
      
	  topBodyView.setBackgroundDrawable(alphaColorFilter(new BitmapDrawable(topBodyBitmap)));
	  bottomBodyView.setBackgroundDrawable(alphaColorFilter(new BitmapDrawable(bottomBodyBitmap)));
	  
	  mHandler.sendEmptyMessageDelayed(0, 50);
	 
  }
  
  public void closeFolder(AnimationListener animationListener){
	  
	  if(topBodyView != null && mTopBodyViewCloseAnim != null){
		  topBodyView.startAnimation(mTopBodyViewCloseAnim);
		  mTopBodyViewCloseAnim.setAnimationListener(animationListener);
	  }
	  
	  if(bottomBodyView != null && mBottomBodyViewCloseAnim != null)
		  bottomBodyView.startAnimation(mBottomBodyViewCloseAnim);
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		//final int x = (int) event.getX();
		final int y = (int) event.getY();
		if(y > mTopBodyHeight - mTopBodyAnimOffset && y < mTopBodyHeight + mBottomBodyAnimOffset)
			return true;
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mContext.sendBroadcast(new Intent("android.touch.action.CLOSE_RECENT_TASK"));
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
		//	isTouchDown = false;						
			break;
		}
	
	return true;
  }
  
  /**
   * 设置半透明和灰色图片
   * @param drawable
   * @return
   */
  private Drawable alphaColorFilter(Drawable drawable){
	 
		//drawable.setAlpha(250);
		/*ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0); 
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix); 
		drawable.setColorFilter(colorMatrixFilter);*/
		
		return drawable;
  }
}
