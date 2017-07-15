package com.niunet.assistivetouch;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.android.phone.assistant.util.TouchWords;

public class DatabaseUtil {
    private Context mContext;
    private ContentResolver mContentResolver;
    private Map<String, Bitmap> cacheBitmap = new HashMap<String, Bitmap>();

    public DatabaseUtil(Context context) {
        mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public void insertNewTagInfo(ViewInfo tagInfo) {

        ContentValues values = new ContentValues();

        values.put(TouchWords.VIEW_ID, tagInfo.viewId);
        values.put(TouchWords.PANEL_NUM, tagInfo.panelNum);
        values.put(TouchWords.CELL_NUM, tagInfo.cellNum);
        values.put(TouchWords.INTENT_URI, tagInfo.intentUri);

        Bitmap bm = UiManager.createIconBitmap(tagInfo.icon, mContext);
        values.put(TouchWords.ICON, UiManager.getIconByteArray(bm));

        values.put(TouchWords.LABEL, tagInfo.label);

        mContentResolver.insert(TouchWords.TOUCH_CONTENT_URI, values);

        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
    }


    /**
     * 获取panelNum面板上的数据
     *
     * @param panelNum
     * @return
     */
    @SuppressWarnings("deprecation")
    public ArrayList<ViewInfo> getTagInfoList(String panelNum) {
        ArrayList<ViewInfo> tagInfoList = new ArrayList<ViewInfo>();

        Cursor cursor = mContentResolver.query(TouchWords.TOUCH_CONTENT_URI, null,
                null, null, null);

        if(cursor == null)
            return null;
        recycleCacheBitmap();

        while (cursor.moveToNext()) {
            if (panelNum.equals(cursor.getString(cursor.getColumnIndex(TouchWords.PANEL_NUM)))) {

                ViewInfo tagInfo = new ViewInfo();

                tagInfo.viewId = cursor.getString(cursor.getColumnIndex(TouchWords.VIEW_ID));
                tagInfo.panelNum = cursor.getString(cursor.getColumnIndex(TouchWords.PANEL_NUM));
                tagInfo.cellNum = cursor.getString(cursor.getColumnIndex(TouchWords.CELL_NUM));
                tagInfo.intentUri = cursor.getString(cursor.getColumnIndex(TouchWords.INTENT_URI));
                tagInfo.label = cursor.getString(cursor.getColumnIndex(TouchWords.LABEL));

                if (panelNum.equals(UiManager.PANEL_TYPE_2) &&
                        cursor.getString(cursor.getColumnIndex(TouchWords.VIEW_ID)).contains("shortcut")) {

                    //只查询快捷方式的图片，自定义的图片不保存在数据库中
                    if (cacheBitmap.get(tagInfo.cellNum) == null) {
                        cacheBitmap.put(tagInfo.cellNum,
                                UiManager.convertBytes2Bimap(cursor.getBlob(cursor.getColumnIndex(TouchWords.ICON))));
                    }

                    tagInfo.icon = new BitmapDrawable(cacheBitmap.get(tagInfo.cellNum));

                } else {
                    tagInfo.icon = UiManager.mViewTagList.get(tagInfo.viewId).icon;
                }

                tagInfoList.add(tagInfo);
            }
        }

        cursor.close();

        return tagInfoList;
    }

    public void recycleCacheBitmap() {
        Set<String> keys = cacheBitmap.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = it.next();
            Bitmap bm = cacheBitmap.get(key);
            if (bm != null && !bm.isRecycled())
                bm.recycle();
        }
        cacheBitmap.clear();
    }

    /**
     * 通过TouchWords字段值来查找ID
     * 返回_ID字段
     */
    public String queryDb_ID(String panelNum, String cellNum) {
        String id = null;
        Cursor cursor = mContentResolver.query(TouchWords.TOUCH_CONTENT_URI, null,
                null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(TouchWords.PANEL_NUM)).equals(panelNum) &&
                    cursor.getString(cursor.getColumnIndex(TouchWords.CELL_NUM)).equals(cellNum)) {
                id = cursor.getString(cursor.getColumnIndex(TouchWords._ID));
                break;
            }
        }

        cursor.close();

        return id;
    }

    public String queryDbIDFromPanelAndUri(String panelNum, String intentUri) {
        String id = null;
        Cursor cursor = mContentResolver.query(TouchWords.TOUCH_CONTENT_URI, null,
                null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(TouchWords.PANEL_NUM)).equals(panelNum) &&
                    cursor.getString(cursor.getColumnIndex(TouchWords.INTENT_URI)).equals(intentUri)) {
                id = cursor.getString(cursor.getColumnIndex(TouchWords._ID));
                break;
            }
        }

        cursor.close();

        return id;
    }

    /**
     * 通过某个字段的值来查询ID
     *
     * @param word
     * @param value
     * @return
     */
    public String queryDbIdFromWord(String word, String value) {
        String id = null;
        Cursor cursor = mContentResolver.query(TouchWords.TOUCH_CONTENT_URI, null,
                null, null, null);
if(cursor == null)
    return "";
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(word)).equals(value)) {
                id = cursor.getString(cursor.getColumnIndex(TouchWords._ID));
                break;
            }
        }

        cursor.close();

        return id;
    }

    /**
     * 更新对应数据库_ID的viewId字段
     *
     * @param id 数据库_ID段
     * @param
     */
    public void updataViewId(String id, String viewId) {
        //更新数据库viewId字段
        ContentValues values = new ContentValues();
        values.put(TouchWords.VIEW_ID, viewId);
        mContentResolver.update(TouchWords.TOUCH_CONTENT_URI, values, TouchWords._ID + " = " + id, null);

    }

    public void updataIcon(String id, byte[] iconByte) {
        //更新数据库viewId字段
        ContentValues values = new ContentValues();
        values.put(TouchWords.ICON, iconByte);
        mContentResolver.update(TouchWords.TOUCH_CONTENT_URI, values, TouchWords._ID + " = " + id, null);

    }

    public void deleteDbData(String id) {

        if (id != null)
            mContentResolver.delete(TouchWords.TOUCH_CONTENT_URI, TouchWords._ID + " = " + id, null);
    }


    /**
     * 删除数据库中说有的数据
     */
    public void deleteDbDataForAll() {
        String id = null;
        Cursor cursor = mContentResolver.query(TouchWords.TOUCH_CONTENT_URI, null,
                null, null, null);

        while (cursor != null && cursor.moveToNext()) {

            id = cursor.getString(cursor.getColumnIndex(TouchWords._ID));
            deleteDbData(id);
        }
        if (cursor != null)
            cursor.close();
    }
}
