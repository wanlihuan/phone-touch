package com.android.phone.assistant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.file.util.FileSystem;
import com.android.phone.assistant.util.ThemeDbUtil;
import com.android.phone.assistant.util.ThemeInfo;
import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.ThemeManager;
import com.android.phone.assistant.widget.ThemeItemView;
import com.ttlove.widget.HorizontalCellScrollView;
import com.ttlove.widget.TitleBar;
import com.ttlove.widget.TopSelectBar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.AbsListView.LayoutParams;
import android.widget.GridView;
import android.widget.ProgressBar;

public class ThemeListActivity extends Activity implements TopSelectBar.OnClickItemSwitchListener,
								HorizontalCellScrollView.OnScrollToScreenCallback,
								 OnScrollListener 
{
	private final int ITEM_NUMBER = 2;
	private final int DEFAULT_ITEM_NUM = 1;
	/**
	 * ����0������Ϊ�ҵ�������ʾ
	 */
	private final int MY_THEME_ITEM_NUMBER = 0;
	/**
	 * ����1������Ϊ��ʾ��������
	 */
	private final int THEME_CENTER_ITEM_NUMBER = 1;
	
	/**
	 * ��ǰ���ҵ����⻹�������
	 */
	private int currentThemeListNumber = THEME_CENTER_ITEM_NUMBER;
	
	private HorizontalCellScrollView mHorizontalCellScrollView;
	private TopSelectBar mTopSelectBar;
	private ThemeGridView[] mThemeGridView;
	private Bitmap mWallpaperBitmap;
	/**��touch���� �����������*/
	private String currentThemeType = null;
	
	
	private File mCacheDir;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case THEME_CENTER_ITEM_NUMBER:
				
				if(mWallpaperBitmap == null)
					mWallpaperBitmap = CommonUtil.getWallpaperDrawable(ThemeListActivity.this);
				
				if(currentThemeType.equals("touch")){
					getSDThemeList("TTT_");
				}else if(currentThemeType.equals("panel")){  
					getSDThemeList("PPP_");
				}
				
				updateList(1);
			break;
			
			case MY_THEME_ITEM_NUMBER:
				if(mWallpaperBitmap == null)
					mWallpaperBitmap = CommonUtil.getWallpaperDrawable(ThemeListActivity.this);
				
				if(currentThemeType.equals("touch")){
					getDbMyThemeList("TTT_");
					
				}else if(currentThemeType.equals("panel")){  
					getDbMyThemeList("PPP_");
				}
				updateList(0);
				break;
			}
		}
	};
	
	/**
	 * ��ȡtouch���⻹��panel���� 
	 * @param themeType
	 */
	private void getSDThemeList(String themeType){
		
		mCacheDir = FileSystem.getCacheRootDir(ThemeListActivity.this, ThemeManager.THEME_PATH_MAINDIR);
		
		/**�洢�����������Ŀ¼·��*/
	     List<String> themeFolderNameList = new ArrayList<String>();
		FileSystem.getFolderNameList(mCacheDir.getAbsolutePath(), themeFolderNameList);
		
			for(String themeFolderName : themeFolderNameList){
				
				//��ȡtouch���iconȫ·��
				String iconPath = FileSystem.getDirFileNamePath(themeFolderName, themeType);
					
				if(iconPath != null){//������iconʱ
					
					ThemeInfo themeInfo = new ThemeInfo();
					themeInfo.themeIconPath = iconPath;
					
					//��ȡ������Ϣ�ļ���·��
					String configPath = FileSystem.getDirFileNamePath(themeFolderName, "config");
					//��ȡ����������Ϣ�ļ�
					String configInfo = FileSystem.readFileStream(configPath);
					
					try{
						String[] infoArray = configInfo.split(":");
						themeInfo.title = infoArray[0];
						themeInfo.freeFlag = infoArray[1];
					}catch(Exception e){
						themeInfo.title = null;
						themeInfo.freeFlag = null;
					}
					
					ThemeManager.mThemeInfoList.add(themeInfo);
				}
				
			}
			
			themeFolderNameList.clear();
			themeFolderNameList = null;
	}
	
	/**
	 * ��ȡ���ݿ��ҵ�����
	 * @param themeType
	 */
	private void getDbMyThemeList(String themeType){
		ThemeManager.mMyThemeInfoList = (new ThemeDbUtil(this)).getMyThemeList(themeType);
	}
	
	private void updateList(int selectedItem){
		mThemeGridView[selectedItem].notifyDataSetInvalidated(selectedItem);
	}
	
	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ThemeManager.mThemeInfoList.clear();
		
		Intent intent = this.getIntent();
		currentThemeType  = intent.getStringExtra("ThemeType");
		
		setContentView(R.layout.theme_list_layout);
		
		//mAnimation = AnimationUtils.loadAnimation(this, R.anim.preview_theme_zoom_enter);

		mHorizontalCellScrollView = (HorizontalCellScrollView)findViewById(R.id.horizontal_cell_scroll_view);
		mHorizontalCellScrollView.setOnScrollToScreenCallback(this);
		
		TitleBar mTitleHomeBar = (TitleBar) this.findViewById(R.id.home_title_view);
		mTitleHomeBar.setTitleText(intent.getStringExtra("TitleText"));
		mTitleHomeBar.setOnClickTitleIconListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ThemeListActivity.this.finish();
			}
		});
		
		mThemeGridView = new ThemeGridView[ITEM_NUMBER];
		for(int i = 0; i < ITEM_NUMBER; i++){
			mThemeGridView[i] = new ThemeGridView(this);
			mThemeGridView[i].setOnScrollListener(this);
			mHorizontalCellScrollView.addView(mThemeGridView[i]);
		}
		
		mTopSelectBar = (TopSelectBar) this.findViewById(R.id.topselectbar);
		
		mTopSelectBar.setInitItems(ThemeListActivity.this 
				,ITEM_NUMBER 
				,new String[] {"�ҵ�����","��������"} 
				,DEFAULT_ITEM_NUM);
		mHorizontalCellScrollView.setCurrentScreen(DEFAULT_ITEM_NUM);
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			mThemeGridView[currentThemeListNumber].notifyDataSetChanged();
		}catch(Exception e){
			
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		ThemeManager.mThemeInfoList.clear();
		ThemeManager.mMyThemeInfoList.clear();
		
		ThemeItemView.recycleBitmapCache();
		
		System.gc();
	}
	
	public void onClickItemSwitch(int selectedItem) {
		// TODO Auto-generated method stub
		
		mHorizontalCellScrollView.snapToScreen(selectedItem);
	}
	
	@Override
	public void onScrollFinish(int currentIndex) {
		// TODO Auto-generated method stub
		Log.d("tag", "onScrollFinish currentIndex="+currentIndex);
		mTopSelectBar.updateUiShow(currentIndex);
		
		//׼����ص�����
			if(currentIndex == 1 && ThemeManager.mThemeInfoList.size() == 0){//ȥ����SD���е�����
				mHandler.removeMessages(THEME_CENTER_ITEM_NUMBER);
				mHandler.sendEmptyMessage(THEME_CENTER_ITEM_NUMBER);
				
			}else if(currentIndex == 0 && ThemeManager.mMyThemeInfoList.size() == 0){//�����ҵ��������ݿ��е�����
				
				mHandler.removeMessages(MY_THEME_ITEM_NUMBER);
				mHandler.sendEmptyMessage(MY_THEME_ITEM_NUMBER);
			}else
				updateList(currentIndex);
	}
	
	private class ThemeGridView extends FrameLayout{
		private ProgressBar mProgressBar;
		private GridView mGridView;
		private GridAdapter mGridAdapter;
		private Context mContext;
		public ThemeGridView(Context context) {
			this(context, null);
			// TODO Auto-generated constructor stub
			
		}

		public ThemeGridView(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
			// TODO Auto-generated constructor stub
		}

		public ThemeGridView(Context context, AttributeSet attrs, int defStyle) { 
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			mContext = context;
			LayoutInflater inflater = LayoutInflater.from(context);
			inflater.inflate(R.layout.theme_grid_item, this, true);
			
			mProgressBar = (ProgressBar)this.findViewById(R.id.progress_bar);
			mProgressBar.setVisibility(View.VISIBLE);
			mGridView = (GridView) this.findViewById(R.id.theme_content);
			mGridView.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> gridView, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
				//	ThemeItemView themeItemView = (ThemeItemView) view;
				//	String tag = (String) themeItemView.getTag();
					ThemeInfo themeInfo = (ThemeInfo)view.getTag();
					String[] infoArray = new String[]{themeInfo.title, themeInfo.themeIconPath, themeInfo.freeFlag};
					
					Bundle bundle = new Bundle();
					bundle.putStringArray("PreviewThemeInfo", infoArray);
					
					Intent intent = new Intent();
					intent.putExtras(bundle);
			        intent.setClass(mContext, PreviewThemeActivity.class);
			        mContext.startActivity(intent);
					
				}
			});
			mGridAdapter = new GridAdapter(mContext);
			mGridView.setAdapter(mGridAdapter);
		}
		
		/**
		 * themeNum ������ҵ����⻹�������
		 */
		public void notifyDataSetInvalidated(int themeNum){
			
			mGridAdapter.setThemeNum(themeNum);
			mProgressBar.setVisibility(View.GONE);
			mGridAdapter.notifyDataSetChanged();
		}
		
		public void notifyDataSetChanged(){
			mGridAdapter.notifyDataSetChanged();
		}
		
		public int getAdapterCount(){
			return mGridAdapter.getCount();
		}
		
		public void setOnScrollListener(OnScrollListener onScrollListener){
			mGridView.setOnScrollListener(onScrollListener);
		}
	}
	 
//	Animation mAnimation;
	int adapterCount = 0;
	
	private class GridAdapter extends BaseAdapter {

		private Context mContext;
		private int mThemeNum;
	//	private Animation mAnimation;
		
		public GridAdapter(Context context) {
			mContext = context;

		}

		public void setThemeNum(int themeNum){
			mThemeNum = themeNum;
		}
		public int getCount() {
			// TODO Auto-generated method stub
			int size = 0;
			if(mThemeNum == MY_THEME_ITEM_NUMBER)
				size = ThemeManager.mMyThemeInfoList.size();
			
			else if(mThemeNum == THEME_CENTER_ITEM_NUMBER)
				
				size = ThemeManager.mThemeInfoList.size();
			
			return size;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ThemeItemView themeItemView = null;
			if (convertView == null) {
				themeItemView = new ThemeItemView(mContext);
				themeItemView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			} else {
				themeItemView = (ThemeItemView) convertView;
			}
			
			ThemeInfo themeInfo = null;
			themeItemView.setBackgroundBitmap(mWallpaperBitmap);
			
			if(mThemeNum == MY_THEME_ITEM_NUMBER){
				themeItemView.setTailVisibility(View.GONE);
				themeInfo = ThemeManager.mMyThemeInfoList.get(position);
				
			}else if(mThemeNum == THEME_CENTER_ITEM_NUMBER){
				themeItemView.setTailVisibility(View.VISIBLE);
				themeInfo = ThemeManager.mThemeInfoList.get(position);
				if(themeInfo.freeFlag.equals("Y"))
					themeItemView.setTailText("���");
				else
					themeItemView.setTailText("20����");
			}
			if(themeInfo != null){
				themeItemView.setTag(themeInfo);
				
				themeItemView.setTitle(themeInfo.title);
				//������ʾ��icon
				if(currentThemeType.equals("touch")){//Сicon
					themeItemView.setTouchBitmap(themeInfo.themeIconPath, themeInfo.themeIconPath);
					
				}else if(currentThemeType.equals("panel")){//���
					
					themeItemView.setPanelBitmap(themeInfo.themeIconPath, themeInfo.themeIconPath);
				}
			}
			
			//����ǰ����ʹ�õ����⣬���Ϲ���
			/*if(ThemeManager.currentThemeIconId.equals(ThemeManager.mAdapterInfoList.get(adapterInfoKeyList.get(position)).id)){
				themeItemView.setCurrentHintVisibility(View.VISIBLE);
				themeItemView.setCurrentHintDrawable(mContext.getResources().getDrawable(R.drawable.flag_using));
			}else{
				themeItemView.setCurrentHintVisibility(View.GONE);
			}*/
			
			
			//themeItemView.setTailLeftIIcon(ThemeManager.mAdapterInfoList.get(adapterInfoKeyList.get(position)).tailIcon);
			
			
			
			return themeItemView;
		}
	}
//	private List<Integer> animationIndex = new ArrayList<Integer>();
	@Override
	public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		/*int index = firstVisibleItem;
		for(int i = 0; i < visibleItemCount - firstVisibleItem; i++){
			((ThemeItemView)absListView.getChildAt(i)).setBackgroundBitmap(
				ThemeManager.mAdapterInfoList.get(adapterInfoKeyList.get(index+i)).bgBitmap);
		}*/
		
		/*boolean isExist = false;

		for(int i = firstVisibleItem; i < totalItemCount; i++){
			isExist = false;
			for(Integer index : animationIndex){
				if(i == index){
					isExist = true;
					break;
				}
			}
			if(isExist == false){
				animationIndex.add(i);
				if(i - firstVisibleItem < absListView.getChildCount()){
					View view = absListView.getChildAt(i - firstVisibleItem);
					view.startAnimation(mAnimation);  
				}
				animationIndex.add(i);
			}
		}*/
		
		
		
		Log.d("TAG", "onScrollStateChanged view.getChildCount()="+absListView.getChildCount());
		/*for(int i = 0; i < absListView.getChildCount();i++){
			View view = absListView.getChildAt(i);
			view.startAnimation(mAnimation);
		}*/
	}

	@Override
	public void onScrollStateChanged(AbsListView absListView, int scrollState) {
		// TODO Auto-generated method stub
		/*if(scrollState == 1)
		for(int i = 0; i < absListView.getChildCount();i++){
			View view = absListView.getChildAt(i);
			view.startAnimation(mAnimation);
		}*/
	}

}
