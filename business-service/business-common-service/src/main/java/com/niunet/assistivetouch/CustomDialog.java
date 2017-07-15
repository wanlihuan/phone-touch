package com.niunet.assistivetouch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.assistivetouch.widget.AppItemView;
import com.assistivetouch.widget.IconUtilities;
import com.android.assistivetouch.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class CustomDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener{
	private Context mContext;
	private TextView mTitle;
	private ProgressBar mProgressBar;
	private GridView mAllAppGridView;
	private TextView mBtnCancel;
	private TextView mBtnOk;
	private DisplayMetrics dm;
	private PackageManager pm;
	public List<ViewInfo> mAppList = new ArrayList<ViewInfo>();
	private Map<String, Bitmap> cacheBitmap = new HashMap<String, Bitmap>();
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				try{
					GridAdapter mGridAdapter = new GridAdapter(mContext);
					mAllAppGridView.setAdapter(mGridAdapter);// ��������
					mGridAdapter.notifyDataSetInvalidated();
					mProgressBar.setVisibility(View.GONE);
					//this.sendEmptyMessageDelayed(1, 500);
				}catch(Exception e){
					
				}
				break;
			case 1:
				//recycledCacheBitmap();
				break;
			}
		}
	};
	
	public CustomDialog(Context context) {
		this(context, R.style.Theme_CustomDialog);
		// TODO Auto-generated constructor stub
		
		
	}

	public CustomDialog(Context context, int theme) {	
		super(context, theme);
		// TODO Auto-generated constructor stub
		mContext = context;
		dm = new DisplayMetrics();	
	    WindowManager winMgr = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);            
	    winMgr.getDefaultDisplay().getMetrics(dm); 
	    pm = context.getPackageManager(); // ���PackageManager����   
		
	}
	
	public void loadingAllApp(){
		mProgressBar.setVisibility(View.VISIBLE);
		mAppList.clear();
		recycleCacheBitmap();
		final IconUtilities iconUtilities = new IconUtilities(mContext);
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
			super.run();
			
		    Intent mainIntent = new Intent(Intent.ACTION_MAIN);  
		    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);  
		    // ͨ����ѯ���������ResolveInfo����.   
		    List<ResolveInfo> resolveInfos = pm  
		            .queryIntentActivities(mainIntent, PackageManager.GET_UNINSTALLED_PACKAGES);  
		    // ����ϵͳ���� �� ����name����   
		    // ���������Ҫ������ֻ����ʾϵͳӦ�ã��������г�������Ӧ�ó���   
		    Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));  
		    
		    Intent launchIntent = new Intent(Intent.ACTION_MAIN);  
	        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        
		    for (ResolveInfo reInfo : resolveInfos) {  
		    	ViewInfo appInfo = new ViewInfo();
		       
		        appInfo.label = (String) reInfo.loadLabel(pm);
		        
		        cacheBitmap.put(reInfo.activityInfo.packageName, UiManager.createIconBitmap(reInfo.loadIcon(pm), mContext));
		        
		        appInfo.icon = iconUtilities.createIconDrawable(new BitmapDrawable(cacheBitmap.get(reInfo.activityInfo.packageName)));
		       
		        launchIntent.setComponent(new ComponentName(reInfo.activityInfo.packageName,  
		        		reInfo.activityInfo.name));  
		       appInfo.intentUri = launchIntent.toURI();
		       
		       mAppList.add(appInfo);
		    }
		    
		    mHandler.sendEmptyMessage(0);
			}
		}.start();
	}
	
	public void setAdapterList(List<ViewInfo> list){
		mProgressBar.setVisibility(View.VISIBLE);
		mAppList.clear();
//		recycleCacheBitmap();
		
		mAppList = list;
		mHandler.sendEmptyMessage(0);
	}
	
	public Bitmap getItemClickedCacheBitmap(String packNameKey){
		
		return cacheBitmap.get(packNameKey);
	}
	
	 
	 public void recycleCacheBitmap(){
			Set<String> keys = cacheBitmap.keySet();
			 for(Iterator<String> it = keys.iterator(); it.hasNext();){
				String key = it.next();
				Bitmap bm = cacheBitmap.get(key);
				if(bm != null && !bm.isRecycled())
					bm.recycle();
			 }
			 cacheBitmap.clear();
			 mAppList.clear();
		}
	 
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.upside_apps_select);
		
		mTitle = (TextView) this.findViewById(R.id.title);
		
		mBtnCancel = (TextView)this.findViewById(R.id.btnCancel);
		mBtnCancel.setOnClickListener(this);
		mBtnOk = (TextView)this.findViewById(R.id.btnOk);
		mBtnOk.setOnClickListener(this);
		
		mAllAppGridView = (GridView) findViewById(R.id.app_content);
		
		mProgressBar = (ProgressBar)this.findViewById(R.id.progress_bar);//new ProgressBar(getContext());
		mProgressBar.setVisibility(View.VISIBLE);
		
		mAllAppGridView.setOnItemClickListener(this);
		mSelectedMap.clear();
	}
	
	
	public void onClick(View view) {
		// TODO Auto-generated method stub
		recycleCacheBitmap();
		if(view == mBtnCancel){
			this.dismiss();
		}else if(view == mBtnOk){
			if(mOnSelectedItemsListener != null)
				mOnSelectedItemsListener.onSelectedItems(mSelectedMap);
			this.dismiss();
		}
	}
	
	public void setTitle(CharSequence text){
		mTitle.setText(text);
	}
	
	Drawable mItemDrawable = null;
   public void setItemBackground(Drawable drawable){
	   mItemDrawable = drawable;
    }
	
	private class GridAdapter extends BaseAdapter {
		
		private Context mContext;

		public GridAdapter(Context ctx) {
			mContext = ctx;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mAppList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mAppList.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			Drawable drawable = (Drawable)((ViewInfo)getItem(position)).icon;
			
			AppItemView mAppItemView;
			if (convertView == null) {
				mAppItemView = new AppItemView(mContext);
				mAppItemView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
			} else {
				mAppItemView = (AppItemView) convertView;
			}
			
			mAppItemView.setItemBackground(mItemDrawable);
			mAppItemView.setIcon(drawable);
			mAppItemView.setLabel(((ViewInfo)getItem(position)).label);
		
			return mAppItemView;
		}
	}

	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {  
		// TODO Auto-generated method stub
		boolean isChecked = ((AppItemView)view).isChecked();
		//if(isChecked == false && mSelectedMap.size() >= 1)//Ŀǰֻ�޶�ѡһ��
		//	return;
		
		/*if(!isChecked){
			mSelectedMap.put(""+position, position);
		}else{
			mSelectedMap.remove(""+position);
		}*/
		if(mOnSelectedItemsListener != null)
			mOnSelectedItemsListener.onSelectedItem(position);
		recycleCacheBitmap();
		((AppItemView)view).setChecked(!isChecked);
		this.dismiss();
		((Activity)mContext).finish();
	}
	
	Map<String,Integer> mSelectedMap = new HashMap<String, Integer>();
	
	OnSelectedItemsListener mOnSelectedItemsListener;
	public void setOnSelectedItemsListener(OnSelectedItemsListener onSelectedItemsListener){  
		mOnSelectedItemsListener = onSelectedItemsListener;
	}
	public interface OnSelectedItemsListener{
		/**
		 * ��ѡ֮��
		 * @param selectedMapPo
		 */
		void onSelectedItems(Map<String, Integer> selectedMapPo);
		/**
		 * ��ѡ
		 * @param position
		 */
		void onSelectedItem(int position);
	}
}
