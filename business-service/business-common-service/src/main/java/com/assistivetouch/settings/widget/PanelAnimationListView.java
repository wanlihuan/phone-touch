package com.assistivetouch.settings.widget;

import com.assistivetouch.widget.TouchSettingView;
import com.android.assistivetouch.R;
import com.ttlove.widget.PreferenceCategoryView;
import com.ttlove.widget.PreferenceRadioView;
import com.ttlove.widget.RadioGroup;
import com.ttlove.widget.RadioGroup.OnItemCheckedChangedListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class PanelAnimationListView extends FrameLayout{
	private Context mContext;
	private final int DEFAULT_ANIMATION_TYPE = 0;
	SharedPreferences preferences;
	RadioGroup mRadioGroup;
	RadioGroup mRadioGroup_1;
	
	public PanelAnimationListView(Context context){
		this(context, null);
    }
	
	public PanelAnimationListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public PanelAnimationListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	      
		mContext = context;
		preferences = context.getSharedPreferences(TouchSettingView.TOUCH_PREFERENCES, Context.MODE_WORLD_READABLE);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.a4, this, true);
		
		PreferenceCategoryView mAnimationCategoryView = (PreferenceCategoryView) this.findViewById(R.id.AnimationCategoryView);
		mAnimationCategoryView.setTitleText("进出动画风格");
		
		mRadioGroup = (RadioGroup) this.findViewById(R.id.RadioGroup);
		mRadioGroup.setOnItemCheckedChangedListener(new OnItemCheckedChangedListener(){

			public void onItemCheckedChanged(int item, boolean checked) {
				// TODO Auto-generated method stub
				
				Editor editor = preferences.edit();
				editor.putInt("AnimationType", item);
				editor.commit();
			}
		});
		
		PreferenceRadioView mAnimation1 = (PreferenceRadioView) this.findViewById(R.id.animation1);
		mAnimation1.setTitleText("AssistiveTouch风格（默认）");
		mAnimation1.setVisibilitySummary(View.GONE);
		
		PreferenceRadioView mAnimation2 = (PreferenceRadioView) this.findViewById(R.id.animation2);
		mAnimation2.setTitleText("PopWin风格动画");
		mAnimation2.setVisibilitySummary(View.GONE);
		
		mRadioGroup.initCheckedItem(preferences.getInt("AnimationType", DEFAULT_ANIMATION_TYPE), true);
		
		
		//显示风格
		PreferenceCategoryView mXianshiXiaoguoCategoryView = (PreferenceCategoryView) this.findViewById(R.id.XianshiXiaoguoCategoryView);
		mXianshiXiaoguoCategoryView.setTitleText("显示风格");
		
		mRadioGroup_1 = (RadioGroup) this.findViewById(R.id.RadioGroup_1);
		
		mRadioGroup_1.setOnItemCheckedChangedListener(new OnItemCheckedChangedListener(){

			public void onItemCheckedChanged(int item, boolean checked) {
				// TODO Auto-generated method stub
				
				Editor editor = preferences.edit();
				editor.putInt("PanelShowStyle", item);
				editor.commit();
			}
		});  
		
		PreferenceRadioView mXianshiXiaoguo_1 = (PreferenceRadioView) this.findViewById(R.id.XianshiXiaoguo_1);
		mXianshiXiaoguo_1.setTitleText("静态显示风格（默认）");
		mXianshiXiaoguo_1.setVisibilitySummary(View.GONE);
		
		PreferenceRadioView mXianshiXiaoguo_2 = (PreferenceRadioView) this.findViewById(R.id.XianshiXiaoguo_2);
		mXianshiXiaoguo_2.setTitleText("呼吸灯特效风格");
		mXianshiXiaoguo_2.setVisibilitySummary(View.GONE);
		mXianshiXiaoguo_2.setVisibilityDividerBottom(View.GONE);
		
		mRadioGroup_1.initCheckedItem(preferences.getInt("PanelShowStyle", 0), true);  
		
	}

}
