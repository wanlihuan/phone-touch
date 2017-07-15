package com.android.file.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileSystem {
	
	public FileSystem() {
		
	}
	
	public static File getCacheRootDir(Context context, String dirOrFile){
		File cacheRootDir = null;
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {	
			
			cacheRootDir = createChildsDir(Environment.getExternalStorageDirectory(), 
					"Android/toucherCache/"+context.getPackageName() + dirOrFile == null? "" : ("/"+dirOrFile));
				//	"Android/data/"+context.getPackageName()+"/cache");
			
			/*++wanlaihuan 2013-12-09 ����ɰ汾���ݵ��ƶ�����*/
			File oldCacheFile = createChildsDir(Environment.getExternalStorageDirectory(), 
					"Android/data/"+context.getPackageName());
			File tFile = new File(oldCacheFile.getPath()+"/cache/touch");//�����ļ�
			File pFile = new File(oldCacheFile.getPath()+"/cache/panel");//�����ļ�
			if(tFile.exists())
				cutGeneralFile(tFile.getPath(), cacheRootDir.getPath());//����
			if(pFile.exists())
				cutGeneralFile(pFile.getPath(), cacheRootDir.getPath());//����
			if(oldCacheFile.exists())
				deleteDirectory(oldCacheFile.getPath());//ɾ��Ŀ¼
			/*--wanlaihuan 2013-12-09 */
			
		}else {
			cacheRootDir = context.getCacheDir();// ��Ŀ¼"/data/data/" + mPackName+"/cache"
			//Toast.makeText(context, "�������SD�洢��������Ӱ�����������", Toast.LENGTH_SHORT).show();
		}
		
		return cacheRootDir;
	}
	
	/**
     * ��Ҫ��Ȩ�ޣ�<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	 * @param context
	 *            �����Ļ���
	 * @param assetsFilePath AssetsĿ¼�µ��ļ���+��׺ �磺apk/..../androidApp.apk ����߲���Ҫ��/��
	 * @param saveFilePath
	 *           �����·�� �磺/data/data/����/cache/�ļ���
	 * @param    // ��ȡϵͳ�������·�� ��ȡ/data/data/����/cache
			File cacheDir = mContext.getCacheDir();
			final String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
	 * @return
	 */
	public static boolean copyFileFromAssets(Context context, String assetsFilePath, String saveFilePath) {
		boolean bRet = false;
		
		//���ӣ�
		//���û��sd��ʱ��ͨ�����з�����װ
    	// ��ȡϵͳ�������·�� ��ȡ/data/data//cacheĿ¼
		//File cacheDir = ApkAutoInstallActivity.this.getCacheDir();
		//String cachePath = cacheDir.getAbsolutePath() + "/temp.apk";
		//copyApkFromAssets(ApkAutoInstallActivity.this, "ecg.apk", cachePath);
		
		creatRootDir(saveFilePath);
		
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
	 * ��AssetsĿ¼�и��Ƽ��ܳ�zip�����Ƶ��ļ�
	 * ����ӵ�Ȩ�ޣ�<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	 * @param context
	 * @param assetsFilePath AssetsĿ¼�µ��ļ���+��׺ �磺apk/..../androidApp.apk ����߲���Ҫ��/
	 * @param saveFileDir ���ձ�����ļ�Ŀ¼
	 * @return �������ձ�����ļ�Ŀ¼
	 */
	public static String copyFileFromAssetsAndDecodeBinZip(Context context, String assetsFilePath, String saveFileDir){
		//��assetsĿ¼ticonĿ¼�µ��ļ���������
		
		/*Bitmap bm = checkPngFile(cachePath);
		if(null != bm){
			return bm;
		}*/
		
		String zipBinFilePath = getCacheRootDir(context, "tempZipBinFile").getPath();
		File zipBinFile = new File(zipBinFilePath);
		deleteFile(zipBinFile);// �����һ��Ԥ���Ķ����ƻ����ļ�
		//if(!(zipBinFile).exists())
		copyFileFromAssets(context, assetsFilePath, zipBinFilePath);
		
		//��ȡ�������ļ���������buffer��
		String base64Str = FileToBase64.fileToBase64(zipBinFilePath);
		byte[] buffer = FileToBase64.base64StringToByteArray(base64Str);
		
		/*try {
			Thread.sleep(50);
		} catch (InterruptedException e1) { 
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		//��buffer�еĶ������ļ������zip�ļ�
		File tempZipFile = getCacheRootDir(context, "temp.zip");
		deleteFile(tempZipFile);// �����һ��Ԥ������ʱѹ���ļ�
		deleteFile(zipBinFile);// ɾ�������ƻ����ļ�
		
		saveDataToFile(buffer, tempZipFile);
		try {
			//��zip�ļ���ѹ����png�ļ�
			ZipUtils.upZipFile(tempZipFile, saveFileDir);
			deleteFile(tempZipFile);// ɾ����ʱѹ���ļ�
			
			return saveFileDir;
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*Bitmap bm1 = checkPngFile(cachePath);
		if(null != bm1){
			return bm1;
		}else{
			decodeCacheIcon(fileSystem, fileToBase64, cacheDir, cacheChildDirName, cacheFileName);
		}*/
		
		return null;
	}
	/**
	 * 
	 * @param packName context.getPackageName()��ȡ�����̵İ���
	 * @return
	 */
	public File getMemoryStorageDirectory(String packName){
		String path = "/data/data/" + packName;
		
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);
		}
		
		return file;
	}
	
	/**
	 * ��ȡ���ߴ�����Ŀ¼
	 * @param parentDir ��Ŀ¼
	 * @param childDirName ��Ŀ¼����
	 * @return
	 */
	public File createChildDir(File parentDir, String childDirName){
		
		File file = null;
		if(parentDir.isDirectory()){
			String childDirPath = parentDir.getPath() + "/"+childDirName;
			
			file = new File(childDirPath);
			if(!file.exists()) {//�����Ѿ�����ͬ�����ֵ��ļ�����Ŀ¼��
				file.mkdir();
				file.setExecutable(true, false);
				file.setReadable(true, false);
				file.setWritable(true, false);
			}
		}
		return file;
	}
	
	public static File createChildsDir(File parentDir, String childPath){
		
		File file = null;
		if(parentDir.isDirectory()){
			String childDirPath = parentDir.getPath();
			String[] path = childPath.split("/");
			for(int i = 0; i < path.length;i++){
				
				childDirPath += "/" + path[i];
				
				file = new File(childDirPath);
				if(!file.exists()) {//�����Ѿ�����ͬ�����ֵ��ļ�����Ŀ¼��
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
	 * 
	 * @param dirFilePath ȫ�ļ�·������ֻ��������ļ��ĸ�Ŀ¼
	 * @return
	 */
	public static File creatRootDir(String dirFilePath){
		
		File file = null;
			String childDirPath = "";//parentDir.getPath();
			String[] path = dirFilePath.split("/");
			for(int i = 0; i < path.length-1;i++){
				
				childDirPath += "/" + path[i];
				
				file = new File(childDirPath);
				if(!file.exists()) {//�����Ѿ�����ͬ�����ֵ��ļ�����Ŀ¼��
					file.mkdir();
					file.setExecutable(true, false);
					file.setReadable(true, false);
					file.setWritable(true, false);
				}
			}
		return file;
	}
	
	/********************************************����洢����*******************************/
	/**
	 * ������ļ��Ķ�д�����Ҵ�0��ʼ
	 * @param file ��Ҫ�򿪵��ļ�
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
	 * @param file ��Ҫ����ȡ���ݵ��ļ�
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
	 * ��byteArray���ݱ������ļ�destFile��
	 * @param byteArray
	 * @param destFile ��Ҫ���������ݵ��ļ�
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
/******************************************** END ����洢����*******************************/
	 /**
     * �����ļ�
     * 
     * @param srcPath
     *            Դ�ļ�����·��
     * @param destDir
     *                 Ŀ���ļ�����Ŀ¼
     * @return boolean
     */
    private static boolean copyFile(String srcPath, String destDir) {
        boolean flag = false;
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) { // Դ�ļ�������
            System.out.println("Դ�ļ�������");
            return false;
        }
        //��ȡ�������ļ����ļ���
        String fileName = srcPath.substring(srcPath.lastIndexOf(File.separator));
        String destPath = destDir + fileName;
        if (destPath.equals(srcPath)) { // Դ�ļ�·����Ŀ���ļ�·���ظ�
            System.out.println("Դ�ļ�·����Ŀ���ļ�·���ظ�!");
            return false;
        }
        File destFile = new File(destPath);
        if(destFile.exists() && destFile.isFile()) {    //��·�����Ѿ���һ��ͬ���ļ�
            System.out.println("Ŀ��Ŀ¼������ͬ���ļ�!");
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
            System.out.println("�����ļ��ɹ�!");
        }
        return flag;
    }
    
    /**
     * ��ȡĿ¼�ļ������б�
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
     *��ȡ�ļ����ļ��У����б�
     * @param nameSaveList ��Ҫ������ļ�ȫ·�����б�
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
     *��ȡ�ļ����ļ��У�������nameContains���б�
     * @param parentDir �ļ���·��
     * @param nameSaveList ��Ҫ������ļ�ȫ·�����б�
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
     * ��ȡĿ¼�е��ļ���ȫ·�������ļ�������nameContains��Ϊ����
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
     * @param srcPath    Դ�ļ���·��
     * @return
     */
    private static boolean copyDirectory(String srcPath, String destDir) {
        System.out.println("�����ļ��п�ʼ!");
        boolean flag = false;
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) { // Դ�ļ��в�����
            System.out.println("Դ�ļ��в�����");
            return false;
        }
        //��ô����Ƶ��ļ��е����֣���������Ƶ��ļ���Ϊ"E://dir"���ȡ������Ϊ"dir"
        String dirName = getDirName(srcPath);
        //Ŀ���ļ��е�����·��
        String destPath = destDir + File.separator + dirName;
//        System.out.println("Ŀ���ļ��е�����·��Ϊ��" + destPath);
        
        if(destPath.equals(srcPath)) {
            System.out.println("Ŀ���ļ�����Դ�ļ����ظ�");
            return false;
        }
        /*File destDirFile = new File(destPath);
        if(destDirFile.exists()) {    //Ŀ��λ����һ��ͬ���ļ���
            System.out.println("Ŀ��λ������ͬ���ļ���!");
            return false;
        }
        destDirFile.mkdirs();    //����Ŀ¼
*/        
        File[] fileList = srcFile.listFiles();    //��ȡԴ�ļ����µ����ļ������ļ���
        if(fileList.length==0) {    //���Դ�ļ���Ϊ��Ŀ¼��ֱ������flagΪtrue����һ���ǳ����Σ�debug�˺ܾ�
            flag = true;
        }
        else {
            for(File temp: fileList) {
                if(temp.isFile()) {    //�ļ�
                    flag = copyFile(temp.getAbsolutePath(), destPath);
                }
                else if(temp.isDirectory()) {    //�ļ���
                    flag = copyDirectory(temp.getAbsolutePath(), destPath);
                }
                if(!flag) {
                    break;
                }
            }
        }
        
        if(flag) {
            System.out.println("�����ļ��гɹ�!");
        }
        
        return flag;
    }
    
	/**
     * �����ļ����ļ���
     * @param srcPath
     * @param destDir Ŀ���ļ����ڵ�Ŀ¼
     * @return
     */
    public static boolean copyGeneralFile(String srcPath, String destDir) {
        boolean flag = false;
        File file = new File(srcPath);
        if(!file.exists()) {
            System.out.println("Դ�ļ���Դ�ļ��в�����!");
            return false;
        }
        if(file.isFile()) {    //Դ�ļ�
            System.out.println("��������ļ�����!");
            flag = copyFile(srcPath, destDir);
        }
        else if(file.isDirectory()) {
            System.out.println("��������ļ��и���!");
            flag = copyDirectory(srcPath, destDir);
        }
        
        return flag;
    }
    
    /**
     * ��ȡ�������ļ��е��ļ�����
     * @param dir
     * @return String
     */
    private static String getDirName(String dir) {
        if(dir.endsWith(File.separator)) {    //����ļ���·����"//"��β������ȥ��ĩβ��"//"
            dir = dir.substring(0, dir.lastIndexOf(File.separator));
        }
        return dir.substring(dir.lastIndexOf(File.separator)+1);
    }
    
    /**
     * ɾ���ļ����ļ���
     * 
     * @param path
     *            ��ɾ�����ļ��ľ���·��
     * @return boolean
     */
    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) { // �ļ�������
            System.out.println("Ҫɾ�����ļ������ڣ�");
        }
        if (file.isDirectory()) { // �����Ŀ¼���򵥶�����
            flag = deleteDirectory(file.getAbsolutePath());
        } else if (file.isFile()) {
            flag = file.delete();
        }
        if (flag) {
            System.out.println("ɾ���ļ����ļ��гɹ�!");
        }
        return flag;
    }
    
    /**
     * ɾ��Ŀ¼����������������ļ������ļ��У�ע��һ��Ŀ¼��������������ļ����ļ���
     * ��ֱ�ӵ���delete�����ǲ��еģ�����������ļ������ļ�����ȫɾ���˲��ܹ�����delete
     * @param path
     *            pathΪ��Ŀ¼��·��
     */
    public static boolean deleteDirectory(String path) {
        boolean flag = true;
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {
            return flag;
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { // ɾ�����ļ����µ��ļ����ļ���
            // Delete file.
            if (file.isFile()) {
                flag = file.delete();
            } else if (file.isDirectory()) {// Delete folder
                flag = deleteDirectory(file.getAbsolutePath());
            }
            if (!flag) { // ֻҪ��һ��ʧ�ܾ����̲��ټ���
                break;
            }
        }
        flag = dirFile.delete(); // ɾ����Ŀ¼
        return flag;
    }
    
	/**
     * �����淽����������з���������+ɾ��
     * @param  destDir ͬ��
     */
    public static boolean cutGeneralFile(String srcPath, String destDir) {
        if(!copyGeneralFile(srcPath, destDir)) {
            System.out.println("����ʧ�ܵ��¼���ʧ��!");
            return false;
        }
        if(!deleteGeneralFile(srcPath)) {
            System.out.println("ɾ��Դ�ļ�(�ļ���)ʧ�ܵ��¼���ʧ��!");
            return false;
        }
        
        System.out.println("���гɹ�!");
        return true;
    }
    
	/**
	 * ɾ���ļ�
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
	 * ���Ŀ¼�Ƿ�Ϊ�ղ�ɾ��
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
	 * ��ĳ·���µ� png �ļ�ת���� bitmap ע��bitmap����֮����Ҫ�ֶ�����
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
			//��SD���е�ͼƬ�ļ�ת����bitmap
			try {
				FileInputStream fis = new FileInputStream(pngFilePath);
				bitmap = BitmapFactory.decodeStream(fis);
			//	fileSystem.deleteFile(new File(cacheDir.getAbsolutePath() + "/"+cacheChildDirName + "/"+name));//ɾ������ѹ������ͼƬ�ļ�
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	/**
	 * ɾ��Ŀ¼�����ļ��ĺ�׺
	 */
	public static void removeHouZhuiDirFile(File parentDir){
		//�г�Ŀ¼�����е��ļ�
		File[] iconFileList = parentDir.listFiles();
		
		//System.out.println("����   "+iconFileList.length+" ��   ͼƬ�ļ�");
		
		for(int i = 0; i < iconFileList.length; i++){
			
			if(!iconFileList[i].isDirectory()){
				try{
					String fileName = iconFileList[i].getName();//����׺��
					String newFileName = fileName.substring(0, fileName.lastIndexOf("."));//ȥ����׺��
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
				// ����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// ��ȡSD����Ŀ¼
					//File sdCardDir = Environment.getExternalStorageDirectory();
					File targetFile = new File(filePath);
					// ��ָ���ļ����� RandomAccessFile����
					RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
					// ���ļ���¼ָ���ƶ������
					raf.seek(targetFile.length());
					// ����ļ�����
					raf.write(fileContent.getBytes("utf-8"));
					raf.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	 
	/**
	 * ��ȡ�ļ��е�����
	 * @param filePath
	 * @return
	 */
	public static String readFileStream(String filePath) {
		try {
			// ����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// ��ȡSD����Ӧ�Ĵ洢Ŀ¼
				File sdCardDir = Environment.getExternalStorageDirectory();
				// ��ȡָ���ļ���Ӧ��������
				FileInputStream fis = new FileInputStream(filePath);
				// ��ָ����������װ��BufferedReader
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

}
