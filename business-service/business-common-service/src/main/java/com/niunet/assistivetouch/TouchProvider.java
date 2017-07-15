package com.niunet.assistivetouch;


import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.assistant.util.TouchWords;

public class TouchProvider extends ContentProvider{
	private final String TAG = "AssistiveTouchProvider";
	
	private TouchDatabaseHelper mTouchDatabaseHelper;
	
	private static final int ITEM = TouchWords.ITEM;
	private static final int ITEMS = TouchWords.ITEMS;
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mTouchDatabaseHelper.getWritableDatabase();
		int count = 0;
		
		switch (TouchWords.matcher.match(uri)) {
		case ITEMS://URI类型
			Log.d("TAG", "case ITEMS:case ITEMS:case ITEMS:case ITEMS:");
	        
			count = db.delete(TouchWords.TABLE_NAME, selection, selectionArgs);
	        
	        break;
			
		case ITEM:
			Log.d("TAG", "case ITEM:case ITEM:case ITEM:case ITEM:");
			String id = uri.getPathSegments().get(1);
			Log.d(TAG, "ididididid="+id);
			System.out.println(String.valueOf(uri.getPathSegments().size()));
			count = db.delete(TouchWords.TABLE_NAME, TouchWords._ID + "=" + id, selectionArgs);

			break;
			
		case TouchWords.THEME_ITEMS://URI类型
			Log.d("TAG", "case ITEMS:case ITEMS:case ITEMS:case ITEMS:");
	        
			count = db.delete(TouchWords.USER_THEME_TABLE_NAME, selection, selectionArgs);
	        
	        break;
			
		case TouchWords.THEME_ITEM:
			Log.d("TAG", "case ITEM:case ITEM:case ITEM:case ITEM:");
			System.out.println(String.valueOf(uri.getPathSegments().size()));
			count = db.delete(TouchWords.USER_THEME_TABLE_NAME, TouchWords._ID + "=" + 
					uri.getPathSegments().get(1), selectionArgs);

			break;
		default:
			throw new IllegalArgumentException("未知Uri:" + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (TouchWords.matcher.match(uri))
		{
			// 如果操作的数据是多项记录
		case TouchWords.THEME_ITEMS:
			case ITEMS:
				return TouchWords.CONTENT_ITEMS;
				// 如果操作的数据是单项记录
			case TouchWords.THEME_ITEM:
			case ITEM:
				return TouchWords.CONTENT_ITEM;
			default:
				throw new IllegalArgumentException("未知Uri:" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mTouchDatabaseHelper.getWritableDatabase();
		switch (TouchWords.matcher.match(uri)) {
		case ITEMS://URI类型
			
			Log.d("TAG", "case ITEMS:case ITEMS:case ITEMS:case ITEMS:");
	        long rowId = db.insert(TouchWords.TABLE_NAME, null, values);
	        if (rowId <= 0) return null;

	        uri = ContentUris.withAppendedId(uri, rowId);
	        Log.d(TAG, "uriuriuriuriuriuriuri"+uri);
	        getContext().getContentResolver().notifyChange(uri, null);

	        break;
			
		case ITEM:
			Log.d("TAG", "case ITEM:case ITEM:case ITEM:case ITEM:");
			break;
			
		case TouchWords.THEME_ITEMS://URI类型
			Log.d("TAG", "case THEME_ITEMS:case THEME_ITEMS:case THEME_ITEMS:case THEME_ITEMS:");
	        rowId = db.insert(TouchWords.USER_THEME_TABLE_NAME, null, values);
	        if (rowId <= 0) return null;

	        uri = ContentUris.withAppendedId(uri, rowId);
	        Log.d(TAG, "uriuriuriuriuriuriuri"+uri);
	        getContext().getContentResolver().notifyChange(uri, null);

	        break;
		default:
			throw new IllegalArgumentException("未知Uri:" + uri);
		}
		
		return uri;
    }

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Log.d("tag", "TouchDatabaseHelper TouchDatabaseHelper TouchDatabaseHelper");
		mTouchDatabaseHelper = new TouchDatabaseHelper(this.getContext(),
				TouchWords.DATABASE_NAME, TouchWords.DATABASE_VERSION);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mTouchDatabaseHelper.getWritableDatabase();
		Cursor cursor = null;

		switch (TouchWords.matcher.match(uri)) {
		case ITEMS://URI类型
			Log.d("TAG", "case ITEMS:case ITEMS:case ITEMS:case ITEMS:");
			
	        cursor = db.query(TouchWords.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	        cursor.setNotificationUri(getContext().getContentResolver(), uri);

	        break;
			
		case ITEM:
			Log.d("TAG", "case ITEM:case ITEM:case ITEM:case ITEM:");
			String id = uri.getPathSegments().get(1);
			cursor = db.query(TouchWords.TABLE_NAME, projection, TouchWords._ID + "=" + id 
						+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
						selectionArgs, null, null, sortOrder);
			break;
			
		case TouchWords.THEME_ITEMS://URI类型
			Log.d("TAG", "case THEME_ITEMS:case THEME_ITEMS:case THEME_ITEMS:case THEME_ITEMS:");
			
	        cursor = db.query(TouchWords.USER_THEME_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
	        cursor.setNotificationUri(getContext().getContentResolver(), uri);

	        break;
			
		case TouchWords.THEME_ITEM:
			Log.d("TAG", "case ITEM:case ITEM:case ITEM:case ITEM:");
			id = uri.getPathSegments().get(1);
			cursor = db.query(TouchWords.USER_THEME_TABLE_NAME, projection, TouchWords._ID + "=" + id 
						+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
						selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("未知Uri:" + uri);
		}
		
        return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mTouchDatabaseHelper.getWritableDatabase();
		int count = 0;
		switch (TouchWords.matcher.match(uri)) {
		case ITEMS://URI类型
			Log.d("TAG", "case ITEMS:case ITEMS:case ITEMS:case ITEMS:");
			
			count = db.update(TouchWords.TABLE_NAME, values, selection, selectionArgs);

	        break;
			
		case ITEM:
			Log.d("TAG", "case ITEM:case ITEM:case ITEM:case ITEM:");
			String id = uri.getPathSegments().get(1);

			count = db.update(TouchWords.TABLE_NAME, values, TouchWords._ID + "=" + id + 
					(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
			break;
			
		case TouchWords.THEME_ITEMS://URI类型
			Log.d("TAG", "case THEME_ITEMS:case THEME_ITEMS:case THEME_ITEMS:case ITEMS:");
			
			count = db.update(TouchWords.USER_THEME_TABLE_NAME, values, selection, selectionArgs);

	        break;
			
		case TouchWords.THEME_ITEM:
			Log.d("TAG", "case ITEM:case ITEM:case ITEM:case ITEM:");
			id = uri.getPathSegments().get(1);

			count = db.update(TouchWords.USER_THEME_TABLE_NAME, values, TouchWords._ID + "=" + id + 
					(!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("未知Uri:" + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}
	
    public class TouchDatabaseHelper extends SQLiteOpenHelper
	{
    	Context mContext;
    	
		final String CREATE_TABLE_SQL =
					"create table " + TouchWords.TABLE_NAME + 
					" ("+TouchWords._ID + " integer primary key autoincrement, " +
					TouchWords.VIEW_ID + " TEXT," +
					TouchWords.PANEL_NUM + " TEXT, " +
					TouchWords.CELL_NUM + " TEXT, " +
					TouchWords.ICON + " BLOB, " +
					TouchWords.LABEL + " TEXT, " +
					TouchWords.INTENT_URI+ " TEXT "+
					" )";
		//我的主题数据库
		final String CREATE_USER_THEME_TABLE_SQL =
			"create table " + TouchWords.USER_THEME_TABLE_NAME + 
			" ("+TouchWords._ID + " integer primary key autoincrement, " +
			TouchWords.THEME_NAME + " TEXT," +
			TouchWords.THEME_ICON_PATH + " TEXT, " +
			TouchWords.THEME_TYPE + " TEXT " +
			" )";
		/**
		 * @param context
		 * @param name
		 * @param version
		 */
		public TouchDatabaseHelper(Context context, String name, int version)
		{
			super(context, name, null, version);
			
			mContext = context;
		}

		@SuppressLint("SdCardPath")
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// 第一次使用数据库时自动建表
			db.execSQL(CREATE_TABLE_SQL);
			db.execSQL(CREATE_USER_THEME_TABLE_SQL);
			initDefaultTheme(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			System.out.println("--------SQLiteDatabase onUpdate Called--------" 
				+"oldVersion = "+ oldVersion + "---> newVersion = " + newVersion);
			
			if(oldVersion < 5){
				db.execSQL(CREATE_USER_THEME_TABLE_SQL);
				initDefaultTheme(db);
				
			}
		}
		
		private void initDefaultTheme(SQLiteDatabase db){
			
			ContentValues values = new ContentValues();
			
			//初始化默认主题
			//初始化touch
			values.put(TouchWords.THEME_NAME, "我的Touch主题");
			values.put(TouchWords.THEME_ICON_PATH, "/mnt/sdcard/Android/toucherCache/com.easyunet.easytouch.tools/SNKKS82KSJHBD982KJSHHS612/TTT_btn_assistivetouch_pressed");
			values.put(TouchWords.THEME_TYPE, "TTT_");
			db.insert(TouchWords.USER_THEME_TABLE_NAME, null, values);
			
			values.put(TouchWords.THEME_NAME, "我的panel主题");
			values.put(TouchWords.THEME_ICON_PATH, "/mnt/sdcard/Android/toucherCache/com.easyunet.easytouch.tools/SNKKS82KSJHBD982KJSHHS612/TTT_btn_assistivetouch_pressed");
			values.put(TouchWords.THEME_TYPE, "PPP_");
			db.insert(TouchWords.USER_THEME_TABLE_NAME, null, values);
			
		}
	}
    
}
