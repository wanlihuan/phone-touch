package com.assistivetouch.widget;


import com.android.phone.assistant.util.CommonUtil;
import com.android.phone.assistant.util.YouMi;
import com.assistivetouch.settings.widget.PanelAnimationListView;
import com.assistivetouch.widget.LockScreen;
import com.android.assistivetouch.R;
import com.niunet.assistivetouch.AssistiveTouchActivity;
import com.niunet.assistivetouch.WebActivity;
import com.ttlove.widget.SlipButton;
import com.ttlove.widget.TtloveDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TouchSettingView extends FrameLayout{
	private Context mContext;
	private SlipButton mActivateDevice;
	public static final String TOUCH_PREFERENCES = "touch_preferences";
	SharedPreferences preferences;
	private TextView mAlphaTextView;
	private TextView mDaxiaoTextView;
	
	public TouchSettingView(Context context){
		this(context, null);
    }
	public TouchSettingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public TouchSettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	      
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.layout_fragment_settings, this, true);
		
		preferences = context.getSharedPreferences(TOUCH_PREFERENCES, Context.MODE_WORLD_READABLE);
		
		//开机自启动touch
		final SlipButton mCbAutoOpen = (SlipButton) this.findViewById(R.id.cb_auto_open);
		mCbAutoOpen.setChecked(preferences.getBoolean("AutoOpen", true));
		mCbAutoOpen.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { 
				// TODO Auto-generated method stub
				
				Editor editor = preferences.edit();
				editor.putBoolean("AutoOpen", isChecked);
				editor.commit();
			}
    	});
		
		//开启和关闭
		SlipButton mAssistantTouchEnable = (SlipButton) this.findViewById(R.id.cb_assistant_touch_enable);
		mAssistantTouchEnable.setChecked(preferences.getBoolean("TouchEnable", true));
		mAssistantTouchEnable.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { 
				// TODO Auto-generated method stub
				
				Editor editor = preferences.edit();
				editor.putBoolean("TouchEnable", isChecked);
				editor.commit();
				
				Intent intent = new Intent();
				intent.setAction("com.touch.action.TOUCH_ENABLE");
				mContext.sendBroadcast(intent);
				//更改开机自动启动功能
				mCbAutoOpen.setChecked(isChecked);
			}
    	});
		
		//设备激活
		/*mActivateDevice = (SlipButton) this.findViewById(Rs.id.cb_activate_device);
		LockScreen.creatInstance(context);
		boolean isDevicceAdmin = LockScreen.getInstance().getDevicceAdmin();
		mActivateDevice.setChecked(isDevicceAdmin);
		
		mActivateDevice.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { 
				// TODO Auto-generated method stub
				
				if(isChecked){
					activeDevicceAdmin();
				}else{
					LockScreen.getInstance().removeActiveAdmin();
					Toast.makeText(mContext, "锁屏激活已取消！", Toast.LENGTH_SHORT).show();
					Uri packageURI = Uri.parse("package:"+mContext.getPackageName());
					mContext.startActivity(new Intent(Intent.ACTION_DELETE, packageURI));
				}
			}
    	});*/
		
		//卸载程序
		LinearLayout mUninstallApp = (LinearLayout) this.findViewById(R.id.uninstall_app);
		LockScreen.creatInstance(context);
		boolean isDevicceAdmin = LockScreen.getInstance().getDevicceAdmin();
		mUninstallApp.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
			//	if(LockScreen.getInstance() != null)
				LockScreen.getInstance().removeActiveAdmin(); //先取消锁屏激活
				
				Uri packageURI = Uri.parse("package:"+mContext.getPackageName());
				mContext.startActivity(new Intent(Intent.ACTION_DELETE, packageURI));
			}
		});
		
		//精品推荐
		LinearLayout mJingPinAppDow = (LinearLayout) this.findViewById(R.id.jingPinAppDow);
		mJingPinAppDow.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
			//	YouMi.getInstance().showRecommendWall();//精品推荐
				YouMi.getInstance().showOffersWall();//积分墙
			}
		});
		
		//设置透明度
		mAlphaTextView = (TextView)this.findViewById(R.id.xiaobaidian_alpha_textview);
		SeekBar mTouchAlphaProgress = (SeekBar) this.findViewById(R.id.touch_alpha_progress);
		final int max = FloatView.maxAlpha - FloatView.minAlpha;
		mTouchAlphaProgress.setMax(max);
		int curProgress = preferences.getInt("TouchAlphaProgress", FloatView.normalAlpha - FloatView.minAlpha);
		setXiaobaidianAlphaText(curProgress, max);//设置文本显示
		mTouchAlphaProgress.setProgress(curProgress);
		mTouchAlphaProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
				setXiaobaidianAlphaText(progress, max);
				
				Editor editor = preferences.edit();
				editor.putInt("TouchAlphaProgress", progress);
				editor.commit();
				
				FloatView.normalAlpha = progress + FloatView.minAlpha;
				Intent intent = new Intent("android.touch.action.FLOAT_ALPHA_CHANGED_NOTI");
				mContext.sendBroadcast(intent);
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//设置小白点大小
		mDaxiaoTextView = (TextView)this.findViewById(R.id.xiaobaidian_daxiao_textview);
		SeekBar mTouchDaxiaoProgress = (SeekBar) this.findViewById(R.id.touch_daxiao_progress);
		mTouchDaxiaoProgress.setMax(FloatView.DaxiaoMaxProgress);
		curProgress = preferences.getInt("TouchDaxiaoProgress", 
				(int)((FloatView.DaxiaoNormalRatio - FloatView.DaxiaoMinRatio) / (FloatView.DaxiaoMaxRatio - FloatView.DaxiaoMinRatio)*FloatView.DaxiaoMaxProgress));
		setXiaobaidianDaxiaoText(curProgress, FloatView.DaxiaoMaxProgress);
		mTouchDaxiaoProgress.setProgress(curProgress);
		mTouchDaxiaoProgress.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				Editor editor = preferences.edit();
				editor.putInt("TouchDaxiaoProgress", progress);
				editor.commit();
				
				setXiaobaidianDaxiaoText(progress, FloatView.DaxiaoMaxProgress);
				FloatView.DaxiaoNormalRatio = FloatView.getDaxiaoRatio(progress, FloatView.DaxiaoMaxProgress);
				Intent intent = new Intent("android.touch.action.FLOAT_DAXIAO_CHANGED_NOTI");
				mContext.sendBroadcast(intent);
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//保存上一次面板
		final SlipButton mSaveOldPanel = (SlipButton) this.findViewById(R.id.save_old_panel);
		mSaveOldPanel.setChecked(preferences.getBoolean("saveOldPanel", true));
		mSaveOldPanel.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { 
				// TODO Auto-generated method stub
				
				Editor editor = preferences.edit();
				editor.putBoolean("saveOldPanel", isChecked);
				editor.commit();
			}
    	});
		
		//自定义操纵面板
		LinearLayout  mCustomPanel = (LinearLayout) this.findViewById(R.id.custom_panel);
		mCustomPanel.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(mContext, AssistiveTouchActivity.class);
				intent.putExtra("mode", 1);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		
		//面板特效
		LinearLayout  mPanelAnimation = (LinearLayout) this.findViewById(R.id.panel_animation);
		mPanelAnimation.setOnClickListener(new OnClickListener(){

			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				final TtloveDialog mTtloveDialog = new TtloveDialog(mContext);
				PanelAnimationListView mPanelAnimationListView = new PanelAnimationListView(mContext);
				
				mTtloveDialog.show();
				
				mTtloveDialog.setTitle("面板特效");
				mTtloveDialog.addCenterView(mPanelAnimationListView);
				mTtloveDialog.setOnClickLeftButtonListener(new OnClickListener(){
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mTtloveDialog.dismiss();
					}
				});
				mTtloveDialog.setVisibilityRightButton(View.GONE);
				
			}
		});
		
		//用户反馈
		LinearLayout mYongHuFanKui = (LinearLayout) this.findViewById(R.id.yonghufankui);
		TextView itemTitleView = (TextView) this.findViewById(R.id.item_title);
		if(CommonUtil.mAdType == CommonUtil.YOU_MI){
			 itemTitleView.setText("检查更新");
			 mYongHuFanKui.setOnClickListener(new OnClickListener(){
					public void onClick(View view) {
						// TODO Auto-generated method stub
						YouMi.getInstance().checkAppUpdate(mContext, true);
					}
				});
		 }else if(CommonUtil.mAdType == CommonUtil.WAPS){
			
		 }  
		
		//Root权限说明
		LinearLayout mRootHelp = (LinearLayout) this.findViewById(R.id.root_help);
		mRootHelp.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, WebActivity.class);
				intent.putExtra("url", "file:///android_asset/root_help/root_help.html");
				mContext.startActivity(intent);
			}
		});
		
	}
	
	private void setXiaobaidianAlphaText(int progress, int max){
		mAlphaTextView.setText(String.format(getResources().getString(R.string.xiaobaidian_alpha_text), (int)((float)progress / max * 100))+"%)"); 
	}
	private void setXiaobaidianDaxiaoText(int progress, int max){
		mDaxiaoTextView.setText(String.format(getResources().getString(R.string.xiaobaidian_daxiao_text), (int)((float)progress / max * 100))+"%)"); 
	}
	
	//激活
	private void activeDevicceAdmin(){
		LockScreen.creatInstance(mContext);
		LockScreen.getInstance().activeManage();
	}
	
	public void onResume(){
		if(LockScreen.getInstance() != null){
			boolean isDevicceAdmin = LockScreen.getInstance().getDevicceAdmin();
		//	if(!isDevicceAdmin)
		//		Toast.makeText(mContext, "锁屏功能可能需要重新激活！", Toast.LENGTH_SHORT).show();
			if(mActivateDevice != null)
				mActivateDevice.setChecked(isDevicceAdmin);
		}
	}
	
}
