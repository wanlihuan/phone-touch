package com.android.phone.assistant;

import java.util.ArrayList;
import java.util.List;

import com.android.file.util.FileSystem;
import com.android.phone.assistant.util.ThemeCache;
import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.ThemeManager;
import com.android.phone.assistant.util.YouMi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PreviewThemeActivity extends Activity{
	private final int LOAD_THEME = 0;
	private final int SHOW_POP_MENU = 1;
	
	
	private RelativeLayout mPreviewParent;
	private ImageView mPreviewPanelView;
	private ImageView mPreviewTouchView;
	private BottomPopMenu mBottomPopMenu = null;
	private String exchangeButtonText;
	private SharedPreferences preferences;
	/**
	 * [0]:�������ƣ�[1]������icon sd��ȫ·����[2]��������ѱ�־
	 */
	private String[] mPreviewThemeInfo;
	/**
	 * ����bitmap
	 */
	private List<Bitmap> bitmapCache = new ArrayList<Bitmap>();
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case LOAD_THEME:
				loadData();
				break;
			case SHOW_POP_MENU:
				popMenu();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		
		Bundle mBundle = this.getIntent().getExtras();
		mPreviewThemeInfo = mBundle.getStringArray("PreviewThemeInfo");
		
		
		preferences = getSharedPreferences("ThemeConfig", Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
		
		setContentView(R.layout.a2);
		
		mPreviewParent = (RelativeLayout) this.findViewById(R.id.preview_parent);
		mPreviewParent.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
				popMenu();
			}
		});
		mPreviewTouchView = (ImageView) this.findViewById(R.id.preview_touch_icon_view);
		mPreviewPanelView = (ImageView) this.findViewById(R.id.preview_panel_icon_view);
		
		mHandler.sendEmptyMessage(LOAD_THEME);
		mHandler.sendEmptyMessageDelayed(SHOW_POP_MENU, 500);
	}

	
	private void loadData(){
		/*Bitmap mWallpaperBitmap = null;
		if(mWallpaperBitmap == null){
			mWallpaperBitmap = CommonUtil.getWallpaperDrawable(this);
			bitmapCache.add(mWallpaperBitmap);
		}*/
		

		mPreviewParent.setBackgroundDrawable(new BitmapDrawable(ThemeCache.bgBitmapCache.get(ThemeCache.KEY_WALLPAPER_SRC)));
		
		/*Bitmap touchBitmap = null;
		if(info.icon != null){
			touchBitmap = (new CommonUtil()).getMatrixBitmap(info.icon, 0.15f);
			bitmapCache.add(touchBitmap);
			mPreviewTouchView.setImageBitmap(touchBitmap);
		}*/
		
		try {
			setPreviewPanelBitmap(mPreviewThemeInfo[1]);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		exchangeButtonText = getPrice() == 0? "Ӧ��" : "�һ���" + getPrice() + "���֣�";
	}
	
	private int getPrice(){
		if(mPreviewThemeInfo[2].equals("Y")){
			return 0;
		}else{
			return 20;
		}
	}
	private void setPreviewPanelBitmap(int drawableId)throws Exception{
		Bitmap panelBitmap = null;
	
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
		if(bitmap != null){
			panelBitmap = (new CommonUtil()).getMatrixBitmap(bitmap, 0.81f);
			
			if(bitmap != null && !bitmap.isRecycled()){
				bitmap.recycle();
				bitmap = null;
			}
		}
		
		bitmapCache.add(panelBitmap);
		mPreviewPanelView.setImageBitmap(panelBitmap);
	}
	
	private void setPreviewPanelBitmap(String pngSDPath){
		Bitmap bitmap = null;
		Bitmap panelBitmap = null;
		
		bitmap = FileSystem.parsePngFile(pngSDPath);//��SD���н�����bitmap
		if(bitmap != null && !bitmap.isRecycled()){
			panelBitmap = (new CommonUtil()).getMatrixBitmap(bitmap, 0.83f);
			
			bitmapCache.add(panelBitmap);
			
			bitmap.recycle();
			bitmap = null;
			mPreviewPanelView.setImageBitmap(panelBitmap);
		}else{
			Toast.makeText(this, "������س����ˣ�", 500).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		recycleBitmapCache();
	}
	
	private void recycleBitmapCache(){
		for(Bitmap bm : bitmapCache){
			if(bm != null && !bm.isRecycled()){
				bm.recycle();
			}
		}
		bitmapCache.clear();
		System.gc();
	}
	
	private void popMenu(){
		
		if(mBottomPopMenu == null)
			mBottomPopMenu = new BottomPopMenu(this);
		
		if(mBottomPopMenu != null){
			mBottomPopMenu.showAtLocation(mPreviewParent, 0, 0);
			mBottomPopMenu.setExchangeButtonText(exchangeButtonText);
		}
	}
	
	private class BottomPopMenu implements OnClickListener{
		PopupWindow popupWindow = null;
		Context mContext;
		RelativeLayout  optionMenuParent;
		Button mBackButton;
		Button mExchangeButton;
		
		public BottomPopMenu(Context context){
			mContext = context;
			
			View view = LayoutInflater.from(context).inflate(R.layout.bottom_pop_menu_layout, null);
			optionMenuParent = (RelativeLayout) view.findViewById(R.id.option_menu);
			optionMenuParent.setOnClickListener(this);
			
			mBackButton = (Button)view.findViewById(R.id.back_button);
			mBackButton.setOnClickListener(this);
			mExchangeButton = (Button)view.findViewById(R.id.exchange_button);
			mExchangeButton.setOnClickListener(this);
			
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setAnimationStyle(R.style.BottomPopAnimationFade);
	        popupWindow.setFocusable(true);
	        popupWindow.setBackgroundDrawable(new BitmapDrawable());//����ʹsetOutsideTouchable��Ч
	        popupWindow.setOutsideTouchable(true);
	        
	       /* optionMenuParent.setOnKeyListener(new OnKeyListener(){
	            public boolean onKey(View v, int keyCode, KeyEvent event)
	            {
	                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
	                	popupWindow.dismiss();
	                	PreviewThemeActivity.this.finish();  
	                }
	                return false;
	            }
	        });*/
		}

		public void setExchangeButtonText(CharSequence text){
			mExchangeButton.setText(text);
		}
		
		public void showAtLocation(View parent, int x, int y) {
			popupWindow.showAtLocation(parent, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, x, y);
			
	        popupWindow.update();
	        
		}
		
		public boolean isShowing(){
			return popupWindow.isShowing();
		}
		public void disMiss() {
			popupWindow.dismiss();
		}
		
		private boolean configFlag = false;
		public void onClick(View v) {
			if(v == mBackButton){
				recycleBitmapCache();
				disMiss();
				PreviewThemeActivity.this.finish();
			}else if(v == mExchangeButton){
				
				if(getPrice() == 0){
					configTouchIcon();
				}else{
					//��������㹻�Ľ��
					CommonUtil.currentPoints = 100;
					if(CommonUtil.currentPoints - getPrice() >= 0){
						if(!configFlag){
							configFlag = true;
							Toast.makeText(mContext, "��"+getPrice()+"���֣��ٰ�һ�ν��������ã�", Toast.LENGTH_SHORT).show();
							return;
						}
						configFlag = false;
						
						//�ۿ�
						 if(CommonUtil.mAdType == CommonUtil.WAPS){
							 
						 }else  if(CommonUtil.mAdType == CommonUtil.YOU_MI){
							 YouMi.getInstance().spendPoints(getPrice());
						 }
						
						//ThemeManager.mAdapterInfoList.put(mThemeId, nInfo);
						
						configTouchIcon();
						
					}else
						Toast.makeText(mContext, "��Ǹ�����ֲ���,��ȥ��������׬����ְɣ�", Toast.LENGTH_LONG).show();
				}
			}
		}
		
		private void configTouchIcon(){
			Editor editor = preferences.edit();
			
			//Log.d("tag", "mPreviewThemeInfo[0] = "+mPreviewThemeInfo[0]+",mPreviewThemeInfo[1]= "+mPreviewThemeInfo[1]+",mPreviewThemeInfo[2]= "+mPreviewThemeInfo[2]+
			//",mPreviewThemeInfo[1].split(themes)[1] = "+mPreviewThemeInfo[1].split("themes")[1]);
			
			
			if(/*ThemeManager.currentThemeTypeFileName.equals(ThemeManager.MYTHEME_TOUCH_CONFIG_FILE_NAME)*/
					mPreviewThemeInfo[1].contains("TTT_")){
				
				editor.putString(ThemeManager.TTT_CURRENT_THEME_PATH, mPreviewThemeInfo[1].split("themes")[1]);
				editor.commit();
				Bitmap bitmap = ThemeCache.currentThemeCache.get("TTT_");
				if(bitmap != null && bitmap.isRecycled() == false){
					bitmap.recycle();
					bitmap = null;
				}
				ThemeCache.currentThemeCache.put("TTT_", 
						FileSystem.parsePngFile(mPreviewThemeInfo[1]));
				Intent intent = new Intent("android.touch.action.TOUCH_ICON_BITMAP");
				intent.putExtra("ShowToast", true);
				mContext.sendBroadcast(intent);
				
			}else if(mPreviewThemeInfo[1].contains("PPP_")/*ThemeManager.currentThemeTypeFileName.equals(ThemeManager.MYTHEME_PANEL_CONFIG_FILE_NAME)*/){
				
				editor.putString(ThemeManager.PPP_CURRENT_THEME_PATH, mPreviewThemeInfo[1].split("themes")[1]);
				editor.commit();
				Bitmap bitmap = ThemeCache.currentThemeCache.get("PPP_");
				if(bitmap != null && bitmap.isRecycled() == false){
					bitmap.recycle();
					bitmap = null;
				}
				ThemeCache.currentThemeCache.put("PPP_", 
						FileSystem.parsePngFile(mPreviewThemeInfo[1]));
				
				Intent intent = new Intent("android.touch.action.TOUCH_PANEL_BITMAP");
				intent.putExtra("ShowToast", true);
				mContext.sendBroadcast(intent);
			}
			
			//ThemeManager.currentThemeIconId = ID;
			
			recycleBitmapCache();
			disMiss();
			PreviewThemeActivity.this.finish();
		}
		
	}
	
	
	
}
