package com.assistivetouch.widget;

import com.android.phone.assistant.util.YouMi;
import com.assistivetouch.settings.widget.PanelAnimationListView;
import com.android.assistivetouch.R;
import com.niunet.assistivetouch.AssistiveTouchActivity;
import com.ttlove.widget.PreferenceCategoryView;
import com.ttlove.widget.PreferenceCheckBoxView;
import com.ttlove.widget.PreferenceItemView;
import com.ttlove.widget.TtloveDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingView extends FrameLayout{
	private Context mContext;
	private PreferenceCheckBoxView mJiHuoLockScreenView;
	public static final String TOUCH_PREFERENCES = "touch_preferences";
	SharedPreferences preferences;
	
	public SettingView(Context context){
		this(context, null);
    }
	public SettingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	      
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.settings_layout, this, true);
		
		preferences = context.getSharedPreferences(TOUCH_PREFERENCES, Context.MODE_WORLD_READABLE);
		
		PreferenceCategoryView mJiBenSettingCategory = (PreferenceCategoryView) this.findViewById(R.id.JiBenSettingCategory);
		mJiBenSettingCategory.setTitleText("基本设置");
		
		//开机自启动touch
		final PreferenceCheckBoxView mKaiJiZiQiDongView = (PreferenceCheckBoxView) this.findViewById(R.id.KaiJiZiQiDongView);
		mKaiJiZiQiDongView.setTitleText("开机自启动");
//		mKaiJiZiQiDongView.setSummaryText("还未设定词库");
		mKaiJiZiQiDongView.setVisibilitySummary(View.GONE);
		mKaiJiZiQiDongView.setCheckedSlipButton(preferences.getBoolean("AutoOpen", true));
		mKaiJiZiQiDongView.setOnCheckedSlipButtonChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { 
				// TODO Auto-generated method stub
				
				Editor editor = preferences.edit();
				editor.putBoolean("AutoOpen", isChecked);
				editor.commit();
			}
    	});
		
		//开启和关闭
		PreferenceCheckBoxView mKaiQiXiaoBaiDianView = (PreferenceCheckBoxView) this.findViewById(R.id.KaiQiXiaoBaiDianView);
		mKaiQiXiaoBaiDianView.setTitleText("开启触控点");
		mKaiQiXiaoBaiDianView.setSummaryText("还未设定词库");
		mKaiQiXiaoBaiDianView.setCheckedSlipButton(preferences.getBoolean("TouchEnable", true));
		mKaiQiXiaoBaiDianView.setOnCheckedSlipButtonChangeListener(new OnCheckedChangeListener(){
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
				mKaiJiZiQiDongView.setCheckedSlipButton(isChecked);
			}
    	});
		
		//设备激活
		mJiHuoLockScreenView = (PreferenceCheckBoxView) this.findViewById(R.id.JiHuoLockScreenView);
		mJiHuoLockScreenView.setTitleText("激活一键锁屏");
		mJiHuoLockScreenView.setSummaryText("卸载touch时必须先取消此激活");
		LockScreen.creatInstance(context);
		boolean isDevicceAdmin = LockScreen.getInstance().getDevicceAdmin();
		mJiHuoLockScreenView.setCheckedSlipButton(isDevicceAdmin);
		
		mJiHuoLockScreenView.setOnCheckedSlipButtonChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { 
				// TODO Auto-generated method stub
				
				if(isChecked){
					activeDevicceAdmin();
				}else{
					LockScreen.getInstance().removeActiveAdmin();
					Toast.makeText(mContext, "锁屏激活已取消！", Toast.LENGTH_SHORT).show();
				}
			}
    	});
		//精品推荐
		PreferenceItemView mJingPinAppView = (PreferenceItemView) this.findViewById(R.id.JingPinAppView);
		mJingPinAppView.setTitleText("精品应用下载");
		mJingPinAppView.setSummaryText("有积分奖励");
		mJingPinAppView.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
//				YouMi.getInstance().showRecommendWall();//精品推荐
				YouMi.getInstance().showOffersWall();//积分墙
			}
		});
		PreferenceCategoryView mXianShiSettingCategoryView = (PreferenceCategoryView) this.findViewById(R.id.XianShiSettingCategoryView);
		mXianShiSettingCategoryView.setTitleText("显示设置");
		
		//自定义操纵面板
		PreferenceItemView mZiDingYiPanelView = (PreferenceItemView) this.findViewById(R.id.ZiDingYiPanelView);
		mZiDingYiPanelView.setTitleText("自定义面板");
		mZiDingYiPanelView.setSummaryText("长安删除按钮,点击虚线框添加功能按钮");
		mZiDingYiPanelView.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, AssistiveTouchActivity.class);
				intent.putExtra("mode", 1);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		
		//面板特效
		PreferenceItemView mPanelDongHuaTeXiaoView = (PreferenceItemView) this.findViewById(R.id.PanelDongHuaTeXiaoView);
		mPanelDongHuaTeXiaoView.setTitleText("面板特效");
		mPanelDongHuaTeXiaoView.setSummaryText("长安删除按钮,点击虚线框添加功能按钮");
		mPanelDongHuaTeXiaoView.setOnClickListener(new OnClickListener(){
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
		
		PreferenceCategoryView mMoreCategoryView = (PreferenceCategoryView) this.findViewById(R.id.MoreCategoryView);
		mMoreCategoryView.setTitleText("更多");
		
		//用户反馈
		PreferenceItemView mJianChaGengXinView = (PreferenceItemView) this.findViewById(R.id.JianChaGengXinView);
		mJianChaGengXinView.setTitleText("检查更新");
		mJianChaGengXinView.setSummaryText("长安删除按钮,点击虚线框添加功能按钮");
		mJianChaGengXinView.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
				YouMi.getInstance().checkAppUpdate(mContext, true);
			}
		});
		
		//关于
		PreferenceItemView mGuanYuView = (PreferenceItemView) this.findViewById(R.id.GuanYuView);
		mGuanYuView.setTitleText("关于");
		mGuanYuView.setSummaryText("长安删除按钮,点击虚线框添加功能按钮");
		mGuanYuView.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	//激活
	private void activeDevicceAdmin(){
		LockScreen.creatInstance(mContext);
		LockScreen.getInstance().activeManage();
	}
	
	public void onResume(){
		if(LockScreen.getInstance() != null){
			boolean isDevicceAdmin = LockScreen.getInstance().getDevicceAdmin();
			mJiHuoLockScreenView.setCheckedSlipButton(isDevicceAdmin);
		}
	}
}
