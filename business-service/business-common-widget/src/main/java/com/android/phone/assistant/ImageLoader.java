package com.android.phone.assistant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ImageLoader {

//    public static final String TAG = YoumiSDK.YOUMITAG;
//    public static final String savePath = "/Android/data/.youmicache/.C2655DBDD5C7328BA5EF149B2A8261A0/";

    // 回调方法
    public interface ImageLoaderCallback {
        public void imageLoaded(Bitmap bitmap);
    }

    // 使用线程池，来重复利用线程，优化内存
    private static final int DEFAULT_THREAD_POOL_SIZE = 1;

    private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
            .newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);

    /*
     * @Paramter imgUrl格式:
     * http://localhost:8080/fileDownloadAction.action?id=1661
     * 从SD卡上加载图片，如果存在则使用该图片； 如果不存在，则从网络下载保存到SD卡，然后使用该图片。
     * 使用线程池对线程进行管理，优化内存使用和CPU效率
     */
//    public static Bitmap loadImage(final Context mContext, final String url,
//            final ImageLoaderCallback imageDownloaderCallback) {
//
//        // 主线程处理回调函数
//        final Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                imageDownloaderCallback.imageLoaded((Bitmap) msg.obj);
//            }
//        };
//
//        File file = new File(Environment.getExternalStorageDirectory()
//                + File.separator + savePath + Coder_Md5.md5(url));
//        Bitmap bitmap = null;
//        if (file!=null && file.exists()) {
//            bitmap = readBitmap(file);
//        }
//
//        if (bitmap != null) {
//            Message msg = Message.obtain();
//            msg.obj = bitmap;
//
//            handler.sendMessageDelayed(msg, 50);
//            return bitmap;
//        }
//
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bitmap = syncLoadBitmap(mContext, url);
//                Message msg = Message.obtain();
//                msg.obj = bitmap;
//                handler.sendMessage(msg);
//            }
//        });
//        return null;
//    }
//
//    /**
//     * @Title: syncLoadBitmap
//     * @Description:同步加载图片
//     * @param @param context
//     * @param @param url
//     * @param @return 传入参数名字
//     * @return Bitmap 返回类型
//     * @date 2013-3-27 下午4:12:01
//     * @throw
//     */
//    public static Bitmap syncLoadBitmap(Context context, String rawUrl) {
//        try {
//            if (Basic_StringUtil.isNullOrEmpty(rawUrl)) {
//                return null;
//            }
//            String fileName = Coder_Md5.md5(rawUrl);
//
//            File dir = new File(Environment.getExternalStorageDirectory()
//                    + File.separator + savePath);
//            if (!dir.exists()) {
//                if (Debug_SDK.isDebug) {
//                    Debug_SDK.dd(TAG, "文件夹不存在，创建文件夹");
//                }
//                dir.mkdirs();
//            }
//
//            File destFile = new File(Environment.getExternalStorageDirectory()
//                    + File.separator + savePath + fileName);
//            // 从本地读取
//            Bitmap bitmap = readBitmap(destFile);
//            if (bitmap != null) {
//                if (Debug_SDK.isDebug) {
//                    Debug_SDK.dd(TAG, "ImageLoader 从本地缓存读取图片");
//                }
//                return bitmap;
//            }
//
//            // 从网络读取
//            bitmap = loadBitmapFromNetWork(context, rawUrl, destFile);
//
//            return bitmap;
//        } catch (Throwable e) {
//            if (Debug_SDK.isDebug) {
//                Debug_SDK.de(TAG, e);
//            }
//        }
//        return null;
//    }

    public static Bitmap loadBitmapFromNetWork(String picUrl) {
        try {
            URL url = new URL(picUrl);
            return BitmapFactory.decodeStream(url.openStream());

        } catch (Throwable e) {
//            if (Debug_SDK.isDebug) {
//                Debug_AdLog.e(e);
//            }
        }
        return null;
    }


    public static Bitmap readBitmap(File destFile) {
        try {
            if (!destFile.exists()) {
                return null;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(destFile.getAbsolutePath());
            return bitmap;
        } catch (Throwable e) {
//            if (Debug_SDK.isDebug) {
//                Debug_SDK.e(e);
//            }
        }
        return null;
    }


    public static void shutdownThreadPool() {
        executor.shutdown();
    }

//    public static boolean checkFileExist(Context context, String picString) {
//        String fileName = Coder_Md5.md5(picString);
//        File file = new File(Environment.getExternalStorageDirectory()
//                + File.separator + savePath + fileName);
//        if (Debug_SDK.isDebug) {
//            Debug_SDK.dd(TAG, "checkFileExist:"+file.exists());
//        }
//        return file.exists();
//    }
}
