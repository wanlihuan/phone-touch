package com.yingt.uimain.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.yingt.service.util.HafNameToClass;
import com.yingt.uimain.R;
import com.yingt.uimain.base.BaseFragmentActivity;
import com.yingt.uimain.config.bean.TabHostInfo;
import com.yingt.uimain.config.getinfo.UiMainConfigApi;
import com.yingt.uimain.util.YtActivityUtils;

import java.util.ArrayList;
import java.util.List;

public class MainTabActivity extends BaseFragmentActivity {

    private FragmentTabHost mTabHost;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        YtActivityUtils.saveMainActivity(this);
        checkShowTutorial();

        setContentView(R.layout.yingt_uimain_activity_main);

        initView();
        initTab();
        // 初始化事件
        initEvent();
    }

    private void checkShowTutorial() {
//        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
//        int currentVersionCode = HafAppUtils.getAppVersionCode(this);
//        if(currentVersionCode>oldVersionCode){
        // 启动用户预览
        YtActivityUtils.startTourActivity(MainTabActivity.this);
//            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
//        }
    }

    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
    }

    private View getTabView(String titleText, String imageUrl) {
        View view = LayoutInflater.from(this).inflate(R.layout.yingt_uimain_tab_item, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.yingt_uimain_tab_counter_gray)
                .into(image);
        title.setText(titleText);

        return view;
    }

    private void initEvent() {

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(mTabHost.getCurrentTab());
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTab() {
        mFragmentList = new ArrayList<Fragment>();
        TabHostInfo info = UiMainConfigApi.getBottomTabInfo(this);
        for (int i = 0; i < info.getTabList().size(); i++) {

            TabHostInfo.TabListBean tabInfo = info.getTabList().get(i);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabInfo.getTitle()).
                    setIndicator(getTabView(tabInfo.getTitle(), tabInfo.getNorImage()));
            String fragmentName = tabInfo.getFragmentName();

            Logger.d("Bottom Item Name: "+tabInfo.getTitle()+"\n Ui Fragment Name: " + fragmentName);

            Fragment fragment = (Fragment) HafNameToClass.newInstanceClass(fragmentName);
            if (fragment == null)
                fragment = new Fragment();

            mTabHost.addTab(tabSpec, fragment.getClass(), null);
            mFragmentList.add(fragment);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(
                    Color.parseColor(info.getBackgroundColor()));
        }
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
    }
}
