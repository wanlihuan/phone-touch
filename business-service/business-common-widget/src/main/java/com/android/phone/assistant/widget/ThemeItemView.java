package com.android.phone.assistant.widget;


import java.util.Iterator;
import java.util.Set;

import com.android.file.util.FileSystem;
import com.android.phone.assistant.R;
import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.ThemeCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ThemeItemView extends FrameLayout{
	
	private ImageView mItemBgView;
	private ImageView mImageView;
	private ImageView mPanelImageView;
	private ImageView mCurrentHintView;
	private TextView mItemHead;
	private TextView mItemTail;
	
	public ThemeItemView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public ThemeItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ThemeItemView(Context context, AttributeSet attrs, int defStyle) { 
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.a1, this, true);
		
		mItemBgView = (ImageView) this.findViewById(R.id.image_bg_view);
		
		mImageView = (ImageView) this.findViewById(R.id.image_view);
		mPanelImageView = (ImageView) this.findViewById(R.id.panel_image_view);
		
		mCurrentHintView = (ImageView) this.findViewById(R.id.current_hint_view);
		
		mItemHead = (TextView) this.findViewById(R.id.item_head);
		mItemTail = (TextView) this.findViewById(R.id.item_tail);
		
	}
	
	public void setBackgroundBitmap(Bitmap bitmap){
		Bitmap bgBitmap = null;
		if(ThemeCache.bgBitmapCache.get(ThemeCache.KEY_WALLPAPER) == null){
			int h = bitmap.getHeight();
			int w = bitmap.getWidth();
			Matrix matrix = new Matrix();
			float a = CommonUtil.widthPixels / w / 3.0f;
			float b = 0.35f;//0.33f;//CommonUtil.heightPixels / h / 1.6f;
			matrix.setScale(a, b);
			
			bgBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			ThemeCache.bgBitmapCache.put(ThemeCache.KEY_WALLPAPER, bgBitmap);//处理后的
			ThemeCache.bgBitmapCache.put(ThemeCache.KEY_WALLPAPER_SRC, bitmap);//原始桌面背景存入缓存中
		}else{
			bgBitmap = ThemeCache.bgBitmapCache.get(ThemeCache.KEY_WALLPAPER);
		}
		mItemBgView.setBackgroundDrawable(new BitmapDrawable(bgBitmap));
		
	}
	/**
	 * 
	 * @param themeCacheKey 缓存key
	 * @param bitmapPath SD卡中bitmap路径
	 */
	public void setTouchBitmap(String themeCacheKey, String bitmapPath){
		Bitmap touchBitmap = null;
		Bitmap bitmap = null;
		if(ThemeCache.touchBitmapCache.get(themeCacheKey) == null){
			bitmap = FileSystem.parsePngFile(bitmapPath);//从SD卡中解析出bitmap
			if(bitmap != null && !bitmap.isRecycled()){
		     touchBitmap = (new CommonUtil()).getMatrixBitmap(bitmap, 0.15f);
		     ThemeCache.touchBitmapCache.put(themeCacheKey, touchBitmap);
		     bitmap.recycle();
			}
		     
		}else{
			touchBitmap = ThemeCache.touchBitmapCache.get(themeCacheKey);
		}
		mImageView.setImageBitmap(touchBitmap);
	}
	
	/**
	 * 
	 * @param themeCacheKey 缓存key
	 * @param bitmapPath SD卡中bitmap路径
	 */
	public void setPanelBitmap(String themeCacheKey, String bitmapPath){
		Bitmap touchBitmap = null;
		Bitmap bitmap = null;
		if(ThemeCache.touchBitmapCache.get(themeCacheKey) == null){
			bitmap = FileSystem.parsePngFile(bitmapPath);//从SD卡中解析出bitmap
			if(bitmap != null && !bitmap.isRecycled()){
		     touchBitmap = (new CommonUtil()).getMatrixBitmap(bitmap, 0.27f);
		     ThemeCache.touchBitmapCache.put(themeCacheKey, touchBitmap);
		     bitmap.recycle();
			}
		     
		}else{
			touchBitmap = ThemeCache.touchBitmapCache.get(themeCacheKey);
		}
		mPanelImageView.setImageBitmap(touchBitmap);
	}
	
	
	public void setPanelBitmap(String themeId, int drawableId) throws Exception{
		Bitmap touchBitmap = null;
		
		if(ThemeCache.touchBitmapCache.get(themeId) == null){
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
			if(bitmap != null){
		     touchBitmap = (new CommonUtil()).getMatrixBitmap(bitmap, 0.27f);
		     ThemeCache.touchBitmapCache.put(themeId, touchBitmap);
			}
			
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();
				bitmap = null;
			}
			
		}else{
			touchBitmap = ThemeCache.touchBitmapCache.get(themeId);
		}
		
		mPanelImageView.setImageBitmap(touchBitmap);
	}
	
	/*public void setPanelDrawable(int drawableId)throws Exception{
		mPanelImageView.setBackgroundDrawable(mContext.getResources().getDrawable(drawableId));
	}*/
	
	public static void recycleBitmapCache(){
		Set<String> keys = ThemeCache.bgBitmapCache.keySet();
		 for(Iterator<String> it = keys.iterator(); it.hasNext();){
				String key = it.next();
				Bitmap bm = ThemeCache.bgBitmapCache.get(key);
				if(bm != null && !bm.isRecycled()){  
					bm.recycle();
				}
		 }
		 ThemeCache.bgBitmapCache.clear();
		 
		 Set<String> keys1 = ThemeCache.touchBitmapCache.keySet();
		 for(Iterator<String> it = keys1.iterator(); it.hasNext();){
				String key = it.next();
				Bitmap bm = ThemeCache.touchBitmapCache.get(key);
				if(bm != null && !bm.isRecycled()){  
					bm.recycle();
				}
		 }
		 ThemeCache.touchBitmapCache.clear();
		 
		System.gc();
	}
	
	public void setCurrentHintDrawable(Drawable drawable){
		mCurrentHintView.setBackgroundDrawable(drawable);
	}
	
	public void setCurrentHintVisibility(int visible){
		mCurrentHintView.setVisibility(visible);
	}
	
	public void setTailLeftIIcon(Drawable drawable){
		mItemTail.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
	}
	
	public void setTailText(CharSequence text){
		mItemTail.setText(text);
	}
	
	public void setTailVisibility(int visible){
		mItemTail.setVisibility(visible);
	}
	
	public void setTitle(CharSequence text){
		mItemHead.setText(text);
	}
	
	public CharSequence getTitle(){
		return mItemHead.getText();
	}
}
