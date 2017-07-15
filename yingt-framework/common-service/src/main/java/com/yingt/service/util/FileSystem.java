package com.yingt.service.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileSystem {

	public FileSystem() {
		
	}

	/**
	 * 获取外部缓存文件
	 * @param context
	 * @param cacheFile
	 * @return
	 */
	public static File getSDCardCacheFile(Context context, String cacheFile){
		File absoluteCacheFile = null;

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

			String filePath = Environment.getExternalStorageDirectory() + "/kds"+"/"+
					(cacheFile == null? "" : cacheFile);
			absoluteCacheFile = creatAbsoluteDirOrFile(filePath, true);
		}else {

			String childPath = (cacheFile == null? "" : cacheFile);

			absoluteCacheFile = creatAbsoluteDirOrFile(context.getCacheDir()+childPath, true);
		}

		return absoluteCacheFile;
	}

	/**
     * 需要加权限：uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
	 * @param context
	 *            上下文环境
	 * @param assetsFilePath Assets目录下的文件名+后缀 如：apk/..../androidApp.apk 最左边不需要‘/’
	 * @param saveFilePath
	 *           保存的路径 如：/data/data/包名/cache/文件名
	 *     // 获取系统缓冲绝对路径 获取/data/data/包名/cache
	 *		File cacheDir = mContext.getCacheDir();
	 *	final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
	 */
	public static boolean copyFileFromAssets(Context context, String assetsFilePath, String saveFilePath) {
		boolean bRet = false;
		
		//例子：
		//如果没有sd卡时刻通过下列方法安装
    	// 获取系统缓冲绝对路径 获取/data/data//cache目录
		//File cacheDir = ApkAutoInstallActivity.this.getCacheDir();
		//String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
		//copyApkFromAssets(ApkAutoInstallActivity.this, "ecg.apk", cachePath);

		creatAbsoluteDirOrFile(saveFilePath, true);
		
		try {
			InputStream is = context.getAssets().open(assetsFilePath);
			
			File file = new File(saveFilePath);
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);

			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}

			fos.close();
			is.close();

			bRet = true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return bRet;
	}

	/**
	 *
	 * @param absolutePath 创建的绝对路径
	 * @param isCreatedFile 是否创建文件，否则就是路径
	 * @return
	 */
	public static File creatAbsoluteDirOrFile(String absolutePath, boolean isCreatedFile){

		File file = null;
		String createdAbsoluteFile = "";
		String[] path = absolutePath.split("/");
		for(int i = 0; i < path.length;i++){

			if(TextUtils.isEmpty(path[i]))
				continue;

			createdAbsoluteFile += "/" + path[i];

			file = new File(createdAbsoluteFile);

			// 创建文件
			if(isCreatedFile && i == path.length - 1){

				if(file.exists() && file.isDirectory())
					file.delete();

				try {
					file.createNewFile();
					file.setExecutable(true, false);
					file.setReadable(true, false);
					file.setWritable(true, false);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else {
				if (file.exists() && file.isDirectory()) {
					continue;
				} else {
					if (file.isFile())
						file.delete();

					file.mkdir();
					file.setExecutable(true, false);
					file.setReadable(true, false);
					file.setWritable(true, false);
				}
			}
		}
		return file;
	}

	/**
     * 读文件
     * 
     * @param context
     * @param filePath 需要读取的文件名，包括路径+文件名+后缀
     * @return
     */
    public static String readFromFile(Context context, String filePath) {
        try {
         // 获取指定文件对应的输入流
            FileInputStream fis = new FileInputStream(filePath);         
            String content = null;
            if (fis != null) {
                byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
        
                // And make a string
                content = new String(buffer, "utf-8");//EncodingUtils.getString(buffer, "UTF-8");
                buffer = null;
                fis.close();
            }
            return content;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    
	/********************************************随机存储操作*******************************/
	
	 public static String getFromAssets(Context context, String fileName){ 
         try { 

          // 获取指定文件对应的输入流
             InputStream is = context.getResources().getAssets().open(fileName);
             
             String content = null;
             if (is != null) {
                 byte[] buffer = new byte[is.available()];
                     is.read(buffer);
         
                 // And make a string
                 content = new String(buffer, "utf-8");//EncodingUtils.getString(buffer, "UTF-8");
                 buffer = null;
                 is.close();
             }
             return content;
         } catch (Exception e) { 
             Logger.e("FileSystem", e.getMessage());
         }
         
         return null;
 } 
	 
	/**
	 * 打开随机文件的读写，并且从0开始
	 * @param file 将要打开的文件
	 * @return
	 */
	public static RandomAccessFile openRandomFileRw(File file){
		RandomAccessFile ras = null;
		try {
			ras = new RandomAccessFile(file, "rw");
			ras.seek(0);
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ras;
	}
	
	/**
	 * @param file 将要被读取数据的文件
	 * @return
	 */
	public String readDataToBuffer(File file){
		byte[] buffer = new byte[32];
		int size = -1;
		String str = "";
		
		RandomAccessFile randomAccessFile = openRandomFileRw(file);
		
		while(randomAccessFile != null){
			try {
				size = randomAccessFile.read(buffer, 0, buffer.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(size <= 0){
				try {
					randomAccessFile.close();
					randomAccessFile = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else
				str += new String(buffer);
		}
		return str;
	}
	
	/**
	 * 将byteArray数据保存至文件destFile中
	 * @param byteArray
	 * @param destFile 将要被保存数据的文件
	 */
	public static void saveDataToFile(byte[] byteArray, File destFile){
		
		RandomAccessFile destRas = openRandomFileRw(destFile);
		
		try {
			destRas.write(byteArray, 0, byteArray.length);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				destRas.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
/******************************************** END 随机存储操作*******************************/
	 /**
     * 复制文件
     * 
     * @param srcPath
     *            源文件绝对路径
     * @param destDir
     *                 目标文件所在目录
     * @return boolean
     */
    private static boolean copyFile(String srcPath, String destDir) {
        boolean flag = false;
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) { // 源文件不存在
            System.out.println("源文件不存在");
            return false;
        }
        //获取待复制文件的文件名
        String fileName = srcPath.substring(srcPath.lastIndexOf(File.separator));
        String destPath = destDir + fileName;
        if (destPath.equals(srcPath)) { // 源文件路径和目标文件路径重复
            System.out.println("源文件路径和目标文件路径重复!");
            return false;
        }
        File destFile = new File(destPath);
        if(destFile.exists() && destFile.isFile()) {    //该路径下已经有一个同名文件
            System.out.println("目标目录下已有同名文件!");
            return false;
        }
        
        File destFileDir = new File(destDir);
        destFileDir.mkdirs();
        try {
            FileInputStream fis = new FileInputStream(srcPath);
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int c;
            while ((c = fis.read(buf)) != -1) {
                fos.write(buf, 0, c);
            }
            fis.close();
            fos.close();
            flag = true;
        } catch (IOException e) {
            //
        }
        
        if(flag) {
            System.out.println("复制文件成功!");
        }
        return flag;
    }
    
    /**
     * 获取目录文件夹名列表
     * @param parentDir
     * @param nameSaveList
     */
    public static void getFolderNameList(String parentDir, List<String> nameSaveList) {
    	
        File dirFile = new File(parentDir);
        if (dirFile.isDirectory()) {
            File[] folder = dirFile.listFiles();
            for (File file : folder) { 
            	if(file.isDirectory())
            		nameSaveList.add(file.getAbsolutePath());
            }
        }
    }

    /**
     *获取文件（文件夹）名列表
     * @param parentDir 文件夹路径
     * 返回 parentDir目录下所有的目录名称（文件名不算的）
     */
    public static List<String> getFolderList(String parentDir) {
    	
    	List<String> folderSaveList = new ArrayList<String>();
    	
        File dirFile = new File(parentDir);
        if (!dirFile.isDirectory()) {
        	return folderSaveList;
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { 
            if (file.isDirectory()) {// Delete folder
            	folderSaveList.add(file.getAbsolutePath());
            }
        }
        
        return folderSaveList;
    }
    
    /**
     *获取文件（文件夹）名列表
     * @param parentDir 文件夹路径
     * @param nameSaveList 需要保存的文件全路径名列表
     */
    public static void getFileNameList(String parentDir, List<String> nameSaveList) {
    	
        File dirFile = new File(parentDir);
        if (!dirFile.isDirectory()) {
        	nameSaveList.add(dirFile.getAbsolutePath());
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { 
            if (file.isFile()) {
            	nameSaveList.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {// Delete folder
            	getFileNameList(file.getAbsolutePath(), nameSaveList);
            }
        }
    }
    /**
     *获取文件（文件夹）名包含nameContains的列表
     * @param parentDir 文件夹路径
     * @param nameSaveList 需要保存的文件全路径名列表
     * @param nameContains
     */
    public static void getFileNameList(String parentDir, List<String> nameSaveList, String nameContains) {
    	
        File dirFile = new File(parentDir);
        if (!dirFile.isDirectory()) {
        	String curName = dirFile.getName();
        	if(curName.contains(nameContains))
        		nameSaveList.add(dirFile.getAbsolutePath());
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { 
            if (file.isFile()) {
            	String curName = file.getName();
            	if(curName.contains(nameContains))
            		nameSaveList.add(file.getAbsolutePath());
            	
            } else if (file.isDirectory()) {// Delete folder
            	getFileNameList(file.getAbsolutePath(), nameSaveList, nameContains);
            }
        }
    }
    
    /**
     * 获取目录中的文件名全路径，该文件名包含nameContains作为条件
     * @param parentDir
     * @param nameContains
     * @return
     */
    public static String getDirFileNamePath(String parentDir, String nameContains) {
    	
        File dirFile = new File(parentDir);
        if (!dirFile.isDirectory())
        	return null;
        
        File[] files = dirFile.listFiles();
        for (File file : files) { 
            if (file.isFile()) {
            	String curName = file.getName();
            	if(curName.contains(nameContains))
            		return file.getAbsolutePath();
            }
        }
		return null;
    }
    /**
     * 
     * @param srcPath    源文件夹路径
     * @param destDir    目标文件夹所在目录
     * @return
     */
    private static boolean copyDirectory(String srcPath, String destDir) {
        System.out.println("复制文件夹开始!");
        boolean flag = false;
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) { // 源文件夹不存在
            System.out.println("源文件夹不存在");
            return false;
        }
        //获得待复制的文件夹的名字，比如待复制的文件夹为"E://dir"则获取的名字为"dir"
        String dirName = getDirName(srcPath);
        //目标文件夹的完整路径
        String destPath = destDir + File.separator + dirName;
//        System.out.println("目标文件夹的完整路径为：" + destPath);
        
        if(destPath.equals(srcPath)) {
            System.out.println("目标文件夹与源文件夹重复");
            return false;
        }
        /*File destDirFile = new File(destPath);
        if(destDirFile.exists()) {    //目标位置有一个同名文件夹
            System.out.println("目标位置已有同名文件夹!");
            return false;
        }
        destDirFile.mkdirs();    //生成目录
*/        
        File[] fileList = srcFile.listFiles();    //获取源文件夹下的子文件和子文件夹
        if(fileList.length==0) {    //如果源文件夹为空目录则直接设置flag为true，这一步非常隐蔽，debug了很久
            flag = true;
        }
        else {
            for(File temp: fileList) {
                if(temp.isFile()) {    //文件
                    flag = copyFile(temp.getAbsolutePath(), destPath);
                }
                else if(temp.isDirectory()) {    //文件夹
                    flag = copyDirectory(temp.getAbsolutePath(), destPath);
                }
                if(!flag) {
                    break;
                }
            }
        }
        
        if(flag) {
            System.out.println("复制文件夹成功!");
        }
        
        return flag;
    }
    
	/**
     * 复制文件或文件夹
     * @param srcPath
     * @param destDir 目标文件所在的目录
     * @return
     */
    public static boolean copyGeneralFile(String srcPath, String destDir) {
        boolean flag = false;
        File file = new File(srcPath);
        if(!file.exists()) {
            System.out.println("源文件或源文件夹不存在!");
            return false;
        }
        if(file.isFile()) {    //源文件
            System.out.println("下面进行文件复制!");
            flag = copyFile(srcPath, destDir);
        }
        else if(file.isDirectory()) {
            System.out.println("下面进行文件夹复制!");
            flag = copyDirectory(srcPath, destDir);
        }
        
        return flag;
    }
    
    /**
     * 获取待复制文件夹的文件夹名
     * @param dir
     * @return String
     */
    private static String getDirName(String dir) {
        if(dir.endsWith(File.separator)) {    //如果文件夹路径以"//"结尾，则先去除末尾的"//"
            dir = dir.substring(0, dir.lastIndexOf(File.separator));
        }
        return dir.substring(dir.lastIndexOf(File.separator)+1);
    }
    
    /**
     * 删除文件或文件夹
     * 
     * @param path
     *            待删除的文件的绝对路径
     * @return boolean
     */
    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) { // 文件不存在
            System.out.println("要删除的文件不存在！");
        }
        if (file.isDirectory()) { // 如果是目录，则单独处理
            flag = deleteDirectory(file.getAbsolutePath());
        } else if (file.isFile()) {
            flag = file.delete();
        }
        if (flag) {
            System.out.println("删除文件或文件夹成功!");
        }
        return flag;
    }
    
    /**
     * 删除目录及其下面的所有子文件和子文件夹，注意一个目录下如果还有其他文件或文件夹
     * 则直接调用delete方法是不行的，必须待其子文件和子文件夹完全删除了才能够调用delete
     * @param path
     *            path为该目录的路径
     */
    public static boolean deleteDirectory(String path) {
        boolean flag = true;
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {
            return flag;
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { // 删除该文件夹下的文件和文件夹
            // Delete file.
            if (file.isFile()) {
                flag = file.delete();
            } else if (file.isDirectory()) {// Delete folder
                flag = deleteDirectory(file.getAbsolutePath());
            }
            if (!flag) { // 只要有一个失败就立刻不再继续
                break;
            }
        }
        flag = dirFile.delete(); // 删除空目录
        return flag;
    }
    
	/**
     * 由上面方法延伸出剪切方法：复制+删除
     * @param  destDir 同上
     */
    public static boolean cutGeneralFile(String srcPath, String destDir) {
        if(!copyGeneralFile(srcPath, destDir)) {
            System.out.println("复制失败导致剪切失败!");
            return false;
        }
        if(!deleteGeneralFile(srcPath)) {
            System.out.println("删除源文件(文件夹)失败导致剪切失败!");
            return false;
        }
        
        System.out.println("剪切成功!");
        return true;
    }
    
	/**
	 * 删除文件
	 * @param file
	 */
	public static void  deleteFile(File file)
    {
      if (file.exists())
      {
       if (file.isFile()){
    	   file.delete();
    	   
       }else if (file.isDirectory()){
	        
	        File files[] = file.listFiles();
	        for (int i = 0; i < files.length; i++)
	        { 
	         deleteFile(files[i]);
	        }
	        file.delete();
       }
      }
    }
	
	/**
	 * 检测目录是否为空并删除
	 * @param file
	 */
	public void checkEmptyDirAndDelete(File file){
		if (file.isDirectory()){
	        
	        File files[] = file.listFiles();
	        if(files.length == 0)
	        	file.delete();
       }
	}
	
	/**
	 * 将某路径下的 png 文件转化成 bitmap 注：bitmap用完之后需要手动回收
	 * @param pngFilePath
	 * @return
	 */
	public static Bitmap parsePngFile(String pngFilePath){
		Bitmap bitmap = null;
		File pngFile = new File(pngFilePath);
		if(pngFile.exists()){
			if(pngFile.length() <= 1){
				pngFile.delete();
				return null;
			}
			//将SD卡中的图片文件转化成bitmap
			try {
				FileInputStream fis = new FileInputStream(pngFilePath);
				bitmap = BitmapFactory.decodeStream(fis);
			//	fileSystem.deleteFile(new File(cacheDir.getAbsolutePath() + "/"+cacheChildDirName + "/"+name));//删除被解压出来的图片文件
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	/**
	 * 删除目录所有文件的后缀
	 * @param parentDir 文件的目录路径
	 */
	public static void removeHouZhuiDirFile(File parentDir){
		//列出目录下所有的文件
		File[] iconFileList = parentDir.listFiles();
		
		//System.out.println("共有   "+iconFileList.length+" 张   图片文件");
		
		for(int i = 0; i < iconFileList.length; i++){
			
			if(!iconFileList[i].isDirectory()){
				try{
					String fileName = iconFileList[i].getName();//带后缀的
					String newFileName = fileName.substring(0, fileName.lastIndexOf("."));//去掉后缀的
					iconFileList[i].renameTo(new File(parentDir.getAbsoluteFile()+"\\"+newFileName));
				}catch(Exception e){
					
				}
			}else{
				removeHouZhuiDirFile(iconFileList[i].getAbsoluteFile());
			}
		}
	}
	
	
	 public void writeFileStream(String filePath, String fileContent) {
			try {
				// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获取SD卡的目录
					//File sdCardDir = Environment.getExternalStorageDirectory();
					File targetFile = new File(filePath);
					// 以指定文件创建 RandomAccessFile对象
					RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
					// 将文件记录指针移动到最后
					raf.seek(targetFile.length());
					// 输出文件内容
					raf.write(fileContent.getBytes("utf-8"));
					raf.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	 
	/**
	 * 读取文件中的内容
	 * @param filePath
	 * @return
	 */
	public static String readFileStream(String filePath) {
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获取SD卡对应的存储目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				// 获取指定文件对应的输入流
				FileInputStream fis = new FileInputStream(filePath);
				// 将指定输入流包装成BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 复制单个文件
	 * @param oldFile	原文件
	 * @param newFile	目标位置文件
	 * @param isCover	是否覆盖;
	 * @return boolean
	 */
	public static boolean copyFile(File oldFile, File newFile, boolean isCover) {
		boolean isok = true;
		try {
			int byteread = 0;
			if (oldFile.exists()) {					// 复制文件存在时
				if(newFile.exists()){
					if(!isCover){
						Logger.i("FileSystem", "复制至"+newFile.getAbsolutePath()+"文件已存在!");
						return false;
					}

					newFile.delete();
				}
				
				InputStream inStream = new FileInputStream(oldFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newFile);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			} else {
				isok = false;
			}
		} catch (Exception e) {
			Logger.i("FileSystem", "复制"+oldFile.getAbsolutePath()+"文件操作出错: "+e.getMessage());
			// e.printStackTrace();
			isok = false;
		}
		return isok;
	}
	
	/**
	 * 字符串文本保存到某目录下的文件中
	 * @param saveStr
	 * @param saveFilePath 保存的文件全路径
	 */
	public static String saveTextToFile(String saveStr, String saveFilePath) {
		try {
			File e = new File(saveFilePath);
			if(!e.exists()) {
				File outStream = new File(e.getParent());
				outStream.mkdirs();
				e.createNewFile();
			}

			FileOutputStream outStream1 = new FileOutputStream(e);
			outStream1.write(saveStr.getBytes());
			outStream1.close();
			return saveFilePath;
		} catch (Exception var4) {
			var4.printStackTrace();
			return null;
		}
	}

	/**
	 * 拷贝Asset指定文件夹到手机内部存储指定目录位置;
	 * @param context		上下文对象
	 * @param assetDir		Asset指定文件夹路径
	 * @param dir			手机内部存储指定目录路径
	 */
	public static void copyAssets(Context context, String assetDir, String dir) {
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(dir);
		// if this directory does not exists, make one.
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {

			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						copyAssets(context, fileName, dir + fileName + "/");
					} else {
						copyAssets(context, assetDir + "/" + fileName, dir+ fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
