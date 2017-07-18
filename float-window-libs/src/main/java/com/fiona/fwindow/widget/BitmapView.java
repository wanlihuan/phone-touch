package com.fiona.fwindow.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class BitmapView extends View{
	private final int DELAY_MILLIS = 35;
	
    private final int WHAT_SHAPE_ALPHA = 0;
    private boolean shapeAlphaFlag = false;
	private int maxCount;
	
   private float mWidth;
   private float mHeight;
   private Bitmap mBitmap;
   private int mAlpha = 255;
   boolean mOpenFlag = false;
   Paint paint;
   PaintFlagsDrawFilter mDrawFilter;
   
   private Handler mHandler = new Handler(){
   	final int offset = 7;
   	int fromAlpha = 0;
   	int alphaOffset = 0;
   	int count = maxCount;
   	
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch(msg.what){
			
			case WHAT_SHAPE_ALPHA:
				if(!mOpenFlag){
					BitmapView.this.setAlphaBitmap(255);
					break;
				}
				if(shapeAlphaFlag == false){
					fromAlpha = msg.arg1;
					alphaOffset = msg.arg2;
					shapeAlphaFlag = true;
				}
				
				count--;
				fromAlpha += alphaOffset;
				
				if(fromAlpha > 255)
					fromAlpha = 255;
				else if(fromAlpha < 0)
					fromAlpha = 0;
				
				BitmapView.this.setAlphaBitmap(fromAlpha);
				
				if(count < 0){
					count = maxCount;
					alphaOffset *= -1;
					mHandler.sendEmptyMessageDelayed(WHAT_SHAPE_ALPHA, 500);
				}else
					mHandler.sendEmptyMessageDelayed(WHAT_SHAPE_ALPHA, DELAY_MILLIS);
				
				break;
			
			}
		}
   	
   };
   
   public BitmapView(Context context){
     super(context);
     setBackgroundColor(Color.TRANSPARENT);
     paint = new Paint();
     paint.setFilterBitmap(true);
     paint.setAntiAlias(true);
     mDrawFilter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
     
   }

   @Override
   protected void onDraw(Canvas canvas)
   {
       
       paint.setAlpha(mAlpha);
       
       canvas.setDrawFilter(mDrawFilter);
       
       Matrix matrix = new Matrix();
       matrix.setScale(mWidth / mBitmap.getWidth(), mHeight / mBitmap.getHeight());
       
       canvas.drawBitmap(mBitmap, matrix, paint);
   }
   
   public void setWidthHeight(float width, float height){
	   mWidth = width;
	   mHeight = height;
   }
   
   public void setBackgroundBitmap(Bitmap bitmap){
	   mBitmap = bitmap;
	   invalidate();
   }
   
   public void setAlphaBitmap(int alpha){
	   mAlpha = alpha;
	   invalidate();
   }
   
   /**
	 *  渐进方式显示touch
	 * @param fromAlpha
	 * @param toAlpha
	 * @param Duration
	 */
	private void setShapeAlpha(int fromAlpha, int toAlpha, int Duration){
		
		setAlphaBitmap(fromAlpha);
		maxCount = Duration / DELAY_MILLIS;
		int offset = (toAlpha - fromAlpha) / maxCount;
		
		Message msg = new Message();
		msg.what = WHAT_SHAPE_ALPHA;
		msg.arg1 = fromAlpha;
		msg.arg2 = offset;
		
		mHandler.removeMessages(WHAT_SHAPE_ALPHA);
		shapeAlphaFlag = false;
		mHandler.sendMessage(msg);
	}
	
	/**
	 * 呼吸灯特效
	 */
	public void setFlashingLightning(boolean openFlag){
		if(openFlag == false)
			mOpenFlag = false;
		else{
			if(mOpenFlag == false){
				mOpenFlag = openFlag;
				
				setShapeAlpha(255, 175, 800);
			}
		}
	}
	
}
