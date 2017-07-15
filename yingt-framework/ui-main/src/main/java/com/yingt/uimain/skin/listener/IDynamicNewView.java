package com.yingt.uimain.skin.listener;

import android.view.View;

import com.yingt.uimain.skin.entity.DynamicAttr;

import java.util.List;


public interface IDynamicNewView {
	void dynamicAddView(View view, List<DynamicAttr> pDAttrs);
}
