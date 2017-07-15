package com.android.phone.assistant.widget;

import com.android.phone.assistant.R;
import com.android.phone.assistant.ThemeListActivity;
import com.android.phone.assistant.widget.ImageText2View;
import com.ttlove.widget.PreferenceCategoryView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class UiComponentList extends FrameLayout implements View.OnClickListener{
	public UiComponentList(Context context){
		this(context, null);
    }
	public UiComponentList(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	private Context mContext;

	public UiComponentList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mContext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.component_list, this, true);
		
		PreferenceCategoryView mPreferenceCategoryView1 = (PreferenceCategoryView) this.findViewById(R.id.PreferenceCategoryView1);
		mPreferenceCategoryView1.setTitleText("精选主题模块");
		
		ImageText2View mTouch = (ImageText2View)this.findViewById(R.id.component_touch); 
		mTouch.setIcon(context.getResources().getDrawable(R.drawable.icon_component_touch));
		mTouch.setTag("touch");
		mTouch.setTitle("Touch主题");
		mTouch.setSummary("默认");
		mTouch.setOnClickListener(this);
		
		ImageText2View mPanel = (ImageText2View)this.findViewById(R.id.component_panel);
		mPanel.setTag("panel");
		mPanel.setIcon(context.getResources().getDrawable(R.drawable.icon_component_boot_animation));
		mPanel.setTitle("面板主题");
		mPanel.setSummary("默认");
		mPanel.setOnClickListener(this);
		
		ImageText2View mLauncherWallpaper = (ImageText2View)this.findViewById(R.id.launcher_wallpaper);
		mLauncherWallpaper.setTag("more");
		mLauncherWallpaper.setIcon(context.getResources().getDrawable(R.drawable.icon_component_wallpaper));
		mLauncherWallpaper.setTitle("更多功能");
		mLauncherWallpaper.setSummary("更加强大");
		mLauncherWallpaper.setOnClickListener(this);
		
		/*
		 * 在线配置功能，目前不开放
		 * YouMi.getInstance().asyncGetOnlineConfig("NewTheme", new OnlineConfigCallBack(){

			public void onGetOnlineConfigFailed(String key) {
				// TODO Auto-generated method stub
				Log.d("tag", "UiComponentList key="+key);
			}

			public void onGetOnlineConfigSuccessful(String key, String value) {
				// TODO Auto-generated method stub
				Log.d("tag", "UiComponentList key="+key+",value="+value);
			}
		});*/
	}
	
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if("more".equals((String)view.getTag())){
			Toast.makeText(mContext, "以后新版本中会强大更多的实用功能，敬请期待吧！谢谢亲们的支持！", Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent = new Intent();
		intent.putExtra("TitleText", ((ImageText2View)view).getTitle().toString());//标题
		intent.putExtra("ThemeType", (String)view.getTag());
        intent.setClass(mContext, ThemeListActivity.class);
        mContext.startActivity(intent);
        
	}
}
