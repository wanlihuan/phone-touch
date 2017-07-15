package com.yingt.uimain.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.yingt.uimain.skin.entity.DynamicAttr;
import com.yingt.uimain.skin.listener.IDynamicNewView;
import com.yingt.uimain.skin.listener.ISkinUpdate;
import com.yingt.uimain.skin.loader.SkinInflaterFactory;
import com.yingt.uimain.skin.loader.SkinManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Base Activity for development
 * 
 * <p>NOTICE:<br> 
 * You should extends from this if you what to do skin change
 * 
 * @author fengjun
 */
public class SkinBaseActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {
	
	/**
	 * Whether response to skin changing after create
	 */
	private boolean isResponseOnSkinChanging = true;
	
	private SkinInflaterFactory mSkinInflaterFactory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		try {
//			Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
//			field.setAccessible(true);
//			field.setBoolean(getLayoutInflater(), false);

			LayoutInflater inflater = getLayoutInflater();
			if (getLayoutInflater().getFactory() != null) {
				inflater = inflater.cloneInContext(this);
			}
			mSkinInflaterFactory = new SkinInflaterFactory();
			inflater.setFactory(mSkinInflaterFactory);

//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SkinManager.getInstance().attach(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SkinManager.getInstance().detach(this);
		mSkinInflaterFactory.clean();
	}
	
	/**
	 * dynamic add a skin view 
	 * 
	 * @param view
	 * @param attrName
	 * @param attrValueResId
	 */
	protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId){	
		mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
	}
	
	protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs){
		mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
	}
	
	final protected void enableResponseOnSkinChanging(boolean enable){
		isResponseOnSkinChanging = enable;
	}

	@Override
	public void onThemeUpdate() {
		if(!isResponseOnSkinChanging){
			return;
		}
		mSkinInflaterFactory.applySkin();
	}

	@Override
	public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
		mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
	}
}
