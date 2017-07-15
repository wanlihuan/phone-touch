package com.yingt.uimain.skin.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 1. 自定义控件自定义属性工厂类
 * Created by laihuan.wan on 2017/6/21 0021.
 */

public class CustomAttrFactory {
    private static CustomAttrFactory instance;
    private static Object synchronizedLock = new Object();

    private Class<? extends SkinAttr> skinAttrBuilder;
    // 自定义属性相关
    private Map<String, SkinAttr> supportAattrNamesMap = new HashMap<String, SkinAttr>();

    public static CustomAttrFactory getInstance() {
        if (instance == null) {
            synchronized (synchronizedLock) {
                if (instance == null) {
                    instance = new CustomAttrFactory();
                }
            }
        }
        return instance;
    }

    public CustomAttrFactory builder(Class<? extends SkinAttr> clas){
        this.skinAttrBuilder = clas;
		return this;
	}

    /**
     * 添加被支持的自定义属性
     * @param attrName
     */
    public CustomAttrFactory addAttr(String attrName){
        try {
            if(skinAttrBuilder != null)
                supportAattrNamesMap.put(attrName, skinAttrBuilder.newInstance());
        }catch(Exception e){
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 获取被支持的皮肤属性
     * @param attrName
     * @return
     */
    public SkinAttr getSupportedSkinAttr(String attrName){
        return supportAattrNamesMap.get(attrName);
    }
}
