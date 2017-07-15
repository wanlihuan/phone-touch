package com.android.file.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 压缩文件夹为zip
 * @author Administrator
 *
 */
public class IconToZipBase64 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(true){
			upzipFolder();
			return;
		}
		
		File iconDirFile = new File("D:\\workspace\\IconToZipBase64\\iconFile");//图片目录
		File zipDirFile = new File("D:\\workspace\\IconToZipBase64\\zipTempFile");//zip文件目录
		File baseDir = new File("D:\\workspace\\IconToZipBase64\\zipBase64File");//存储最后的二进制文件目录,
		
		//先清空相关的存放目录
		deleteDirectory(zipDirFile.getPath());
		deleteDirectory(baseDir.getPath());
		
		File[] iconFileList = iconDirFile.listFiles();
		System.out.println("共有   "+iconFileList.length+" 张   图片文件");
		for(int i = 0; i < iconFileList.length; i++){
			if(!iconFileList[i].isDirectory()){
				//int count = i + 1;
				//System.out.println("处理第"+ count +"张 图片   "+iconFileList[i].getName());
				String fileName = iconFileList[i].getName();//带后缀的
				String newFileName = fileName.substring(0, fileName.lastIndexOf("."));//不带后缀的
				
				File zipFile = new File(zipDirFile.getPath()+"\\"+newFileName+".zip");
				
				try {
					ZipUtils.zipFiles(new File(iconDirFile.getPath() + "\\"+iconFileList[i].getName()), 
							zipFile, "");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(!zipFile.exists()){
					System.out.println(newFileName+".zip文件生成失败！处理结束！");
					return;
				}
				String zipBase64Str = FileToBase64.fileToBase64(zipFile.getPath());
				byte[] baseArray = FileToBase64.base64StringToByteArray(zipBase64Str);

				String mSimplingSavePath = baseDir.getPath() + "\\"+newFileName;//最终的文件
				if(zipBase64Str != null)
					saveDataToFile(baseArray, mSimplingSavePath);
			}
		}
		
		File[] baseFileList = baseDir.listFiles();
		if(baseFileList.length == iconFileList.length)
			System.out.println(baseFileList.length+"张文件全部处理完毕！");
	}

	
	/**
	 * 将byteArray数据保存至文件pathName中
	 * @param byteArray
	 * @param pathName 文件全路径名+文件名后缀
	 */
	private static void saveDataToFile(byte[] byteArray, String savePathName){
		RandomAccessFile diagSheetRas = openRandomFileRw(savePathName);
		
		try {
			diagSheetRas.write(byteArray, 0, byteArray.length);
			System.out.println("处理完毕！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				diagSheetRas.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @param pathName 文件全路径名+文件名后缀
	 * @return
	 */
	public static RandomAccessFile openRandomFileRw(String pathName){
		RandomAccessFile ras = null;
		File file = new File(pathName);
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
	
	private static boolean deleteDirectory(String path) {
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
       // flag = dirFile.delete(); // 删除空目录
        return flag;
    }
	
	/**
	 * 压缩文件夹
	 * */
	private static void upzipFolder(){
		File iconDirFile = new File("D:\\workspace\\IconToZipBase64\\iconFile");//原生图片目录
		File zipDirFile = new File("D:\\workspace\\IconToZipBase64\\zipTempFile");//压缩后的zip文件目录
		File baseDir = new File("D:\\workspace\\IconToZipBase64\\zipBase64File");//将zip文件存储最后的二进制文件目录
		
		
		//先清空相关的存放目录
		deleteDirectory(zipDirFile.getPath());
		deleteDirectory(baseDir.getPath());
		
		//移除所有文件的后缀
		removeHouZhuiDirFile(iconDirFile);
		
		//对主目录中的文件夹中的图片全部处理
		File[] fileArr = iconDirFile.listFiles();
		for(int i = 0; i < fileArr.length; i++){
			
			String mainDir = fileArr[i].getName();
			File zipFile = new File(zipDirFile.getPath()+"\\"+mainDir+".zip");
			
			try {
				ZipUtils.zipFiles(new File(iconDirFile.getPath()+"\\"+mainDir), 
						zipFile, "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String zipBase64Str = FileToBase64.fileToBase64(zipFile.getPath());
			byte[] baseArray = FileToBase64.base64StringToByteArray(zipBase64Str);
	
			String mSimplingSavePath = baseDir.getPath() + "\\"+mainDir;//最终的文件
			if(zipBase64Str != null){
				saveDataToFile(baseArray, mSimplingSavePath);
				
			}
		}
	}
	
	/**
	 * 删除目录所有文件的后缀
	 * @param iconFileDir 文件的目录路径
	 */
	public static void removeHouZhuiDirFile(File iconFileDir){
		//列出目录下所有的文件
		File[] iconFileList = iconFileDir.listFiles();
		
		System.out.println("共有   "+iconFileList.length+" 张   图片文件");
		
		for(int i = 0; i < iconFileList.length; i++){
			
			if(!iconFileList[i].isDirectory()){
				try{
					String fileName = iconFileList[i].getName();//带后缀的
					String newFileName = fileName.substring(0, fileName.lastIndexOf("."));//去掉后缀的
					iconFileList[i].renameTo(new File(iconFileDir.getAbsoluteFile()+"\\"+newFileName));
				}catch(Exception e){
					
				}
			}else{
				removeHouZhuiDirFile(iconFileList[i].getAbsoluteFile());
			}
		}
	}
}
