package com.yingt.uimain.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

/**
 * Created by laihuan.wan on 2017/7/3 0003.
 */

public class Res {
    private static Resources res;

    public static void setContext(Context context)
    {
        Res.res = context.getResources();
    }

    public static String getString(int id)
    {
        return res.getString(id);
    }

    public static int getColor(int id)
    {
        return res.getColor(id);
    }

    public static int getDimen(int id)
    {
        return res.getDimensionPixelSize(id);
    }

    public static String[] getStringArray(int id)
    {
        return res.getStringArray(id);
    }

    public static Drawable getDrawable(int id)
    {
        return res.getDrawable(id);
    }

    public static int getInteger(int id)
    {
        return res.getInteger(id);
    }

    public static int[] getIngegerArray(int id)
    {
        return res.getIntArray(id);
    }

    public static boolean getBoolean(int id)
    {
        return res.getBoolean(id);
    }

    /**
     * 获取float类型数值(api不直接支持float, 请传入string类型的Resources id)
     * @param id	string类型的Resources id
     * @return		float类型值
     */
    public static float getFloat(int id) // throws Exception
    {
        return Float.parseFloat(res.getString(id));
    }

    public static boolean[] getBooleanArray(int arrayId){
        TypedArray typedArray = res.obtainTypedArray(arrayId);
        int len = typedArray.length();
       boolean[] booleenAarray = new boolean[len];
        for (int i = 0; i < len; i++) {
            booleenAarray[i] = typedArray.getBoolean(i, false);
        }
        typedArray.recycle();
        return booleenAarray;
    }
}
