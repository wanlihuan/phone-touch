package com.yingt.uimain.skin.entity;


public class AttrFactory {
	
	public static final String BACKGROUND = "background";
	public static final String TEXT_COLOR = "textColor";
	public static final String LIST_SELECTOR = "listSelector";
	public static final String DIVIDER = "divider";

	public SkinAttr get(String attrName, int attrValueRefId, String attrValueRefName, String typeName){

		// 先获取自定义支持的属性 add by laihuan.wan on 2017/06/21
		SkinAttr mSkinAttr = CustomAttrFactory.getInstance().getSupportedSkinAttr(attrName);

		if(mSkinAttr == null) { //  如果判断不是自定义的属性，则...
			if (BACKGROUND.equals(attrName)) {
				mSkinAttr = new BackgroundAttr();
			} else if (TEXT_COLOR.equals(attrName)) {
				mSkinAttr = new TextColorAttr();
			} else if (LIST_SELECTOR.equals(attrName)) {
				mSkinAttr = new ListSelectorAttr();
			} else if (DIVIDER.equals(attrName)) {
				mSkinAttr = new DividerAttr();
			} else {
				return null;
			}
		}
		
		mSkinAttr.attrName = attrName;
		mSkinAttr.attrValueRefId = attrValueRefId;
		mSkinAttr.attrValueRefName = attrValueRefName;
		mSkinAttr.attrValueTypeName = typeName;
		return mSkinAttr;
	}
	
	/**
	 * Check whether the attribute is supported
	 * @param attrName
	 * @return true : supported <br>
	 * 		   false: not supported
	 */
	public boolean isSupportedAttr(String attrName){

		// 先获取自定义支持的属性 add by laihuan.wan 2017/06/21
		SkinAttr mSkinAttr = CustomAttrFactory.getInstance().getSupportedSkinAttr(attrName);

		if(mSkinAttr != null)
			return true;

		if(BACKGROUND.equals(attrName)){ 
			return true;
		}
		if(TEXT_COLOR.equals(attrName)){ 
			return true;
		}
		if(LIST_SELECTOR.equals(attrName)){ 
			return true;
		}
		if(DIVIDER.equals(attrName)){ 
			return true;
		}

		return false;
	}
}
