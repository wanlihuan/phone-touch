package com.yingt.uimain.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class YtFragmentUtils {

    private FragmentManager mFM = null;
    private FragmentActivity fragmentAct;
    private int rootResId = -1;

    /**
     * Fragment 工具类构造方法
     * @param activity
     */
    public YtFragmentUtils(FragmentActivity activity) {
        fragmentAct = activity;
    }

    /**
     * Fragment 工具类构造方法
     * @param activity
     * @param rootResId 加载 Fragment的主界面容器ID
     */
    public YtFragmentUtils(FragmentActivity activity, int rootResId) {
        fragmentAct = activity;
        this.rootResId = rootResId;

    }

    /**
     * 切换 fragment
     * @param fragment 实例对象
     */
    public void switchV4Fragment(Fragment fragment) {
        Fragment f = fragment;//instanceFragment("com.szkingdom.main.activity.MainFragment");//new MainFragment();
        if (null == mFM)
            mFM = fragmentAct.getSupportFragmentManager();
        FragmentTransaction ft = mFM.beginTransaction();
        if(rootResId < 0)
            ft.replace(android.R.id.content, f);
        else
            ft.replace(rootResId/*R.id.fl_main_root*/, f);
        ft.commit();
    }

    /**
     * 切换 fragment
     * @param fragmentClassName 包名 + 类名
     */
    public void switchV4Fragment(String fragmentClassName) {
        Fragment f = instanceFragment(fragmentClassName);
        if(f == null)
            return;

        if (null == mFM)
            mFM = fragmentAct.getSupportFragmentManager();
        FragmentTransaction ft = mFM.beginTransaction();
        if(rootResId < 0)
            ft.replace(android.R.id.content, f);
        else
            ft.replace(rootResId/*R.id.fl_main_root*/, f);
        ft.commit();
    }

    /**
     * Fragment 实例化
     * @param fragmentClassName
     * @return
     */
    private Fragment instanceFragment(String fragmentClassName){
        Fragment tagFragment = null;
        try {
            tagFragment = (Fragment)
                    Class.forName(fragmentClassName).getConstructor().newInstance();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Fragment.InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (java.lang.InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(tagFragment != null)
            return tagFragment;
        else {
            Toast.makeText(fragmentAct, fragmentClassName + " 页面不存在！", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /**
     * 显示 fragment
     * @param f fragment对象
     */
    public void showFragment(Fragment f){
        if (null == mFM)
            mFM = fragmentAct.getSupportFragmentManager();
        FragmentTransaction transaction = mFM.beginTransaction();
        if (!f.isAdded()) {
            transaction.add(rootResId, f);
        }else{
            transaction.show(f);
        }
        transaction.commit();
    }

    /**
     * 隐藏 fragment
     * @param f fragment对象
     */
    public void hideFragment(Fragment f){
        if (null == mFM)
            mFM = fragmentAct.getSupportFragmentManager();
        FragmentTransaction transaction = mFM.beginTransaction();
        transaction.hide(f);
        transaction.commit();
    }
}
