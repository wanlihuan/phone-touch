package com.android.phone.assistant.util;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtil {
	public static final int SVN_NUNBER = 78;
	public static int widthPixels;
	public static int heightPixels;

	/******************广告平台配置，目前只有有米 和 万普平台***********************/
	public final static int YOU_MI = 0;
	public final static int WAPS = 1;
	public static int mAdType = YOU_MI;
	/******************  END  **************************************************/
	/**
	 * 当前积分ID
	 */
	public static final int CURRENT_POINTS = 0;

	/**
	 * 当前金币
	 */
	public static int currentPoints = 0;

	/**
	 * 将bitmap缩小或放大scale的系数倍数
	 * @param bitmap
	 * @param scale
	 * @return
	 */
	public Bitmap getMatrixBitmap(Bitmap bitmap, float scale){

		int w = bitmap.getWidth();
		int minPix = Math.min(widthPixels, heightPixels);
		Matrix matrix = new Matrix();
		matrix.setScale(minPix * scale / w, minPix * scale / w);

		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

	}

	/**
	 * 检查网络是否可用
	 *
	 * @return
	 */
	public boolean getNetworkIsAvailable(Context context)
	{
		NetworkInfo info =
				((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

		if (info == null || !info.isConnected())
		{
			return false;
		}
		if (info.isRoaming())
		{
			return true;
		}
		return true;
	}

	/**
	 * 获取当前墙纸
	 * @return
	 */
	public static Bitmap getWallpaperDrawable(Activity activity) {
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
		Drawable wallpaperDrawable = wallpaperManager.getDrawable();
		Bitmap bitmap1 = ((BitmapDrawable) wallpaperDrawable).getBitmap();

		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		int statusBarHeight = rect.top;

		int bitmapWidth = bitmap1.getWidth();
		int bitmapHeight = bitmap1.getHeight();
		int width = activity.getWindow().getDecorView().getWidth();
		int height = activity.getWindow().getDecorView().getHeight();
		int shotHeight = bitmapHeight - statusBarHeight;

		int x = (bitmapWidth - width) / 2;
		Bitmap bitmap = Bitmap.createBitmap(bitmap1,
				x, statusBarHeight,
				width, shotHeight);

		return bitmap;

	}

	/**
	 * 获取当前墙纸
	 * @return
	 */
	public Bitmap getWallpaperDrawable(Context context, int statusBarHeight, int screenWidth) {
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
		Drawable wallpaperDrawable = wallpaperManager.getDrawable();
		Bitmap bitmap1 = ((BitmapDrawable) wallpaperDrawable).getBitmap();

		int bitmapWidth = bitmap1.getWidth();
		int bitmapHeight = bitmap1.getHeight();
		int shotHeight = bitmapHeight - statusBarHeight;

		int x = (bitmapWidth - screenWidth) / 2;
		Bitmap bitmap = Bitmap.createBitmap(bitmap1,
				x, statusBarHeight,
				screenWidth, shotHeight);

		return bitmap;
	}
}
