package com.android.phone.assistant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Random;

import com.android.file.util.FileSystem;

import android.content.Context;
import android.telephony.TelephonyManager;

public class MyTheme {
	Context mContext;
	TelephonyManager tm;

	public MyTheme(Context context){
		mContext = context;
		tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 产生start 到 end之间的不重复随机数，不包括end
	 * @param start
	 * @param end
	 * @return
	 */
	public int[] getRandomInteger(int start, int end){

		Random rand = new Random();
		int len = end - start;
		int arr[] = new int[len];
		boolean[] bool = new boolean[len];

		int num = 0;
		for (int i = 0; i < len; i++)
		{
			do {
				num = rand.nextInt(len);
			} while (bool[num]);

			bool[num] = true;

			arr[i] = num + start;
		}

		return arr;
	}

	public boolean saveConfig(String saveDataFileName, String themeId) {

		String encodeStr = encodedThemeId(themeId);

		try {
			File targetFile = FileSystem.getCacheRootDir(mContext, saveDataFileName);
			RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
			raf.seek(targetFile.length());
			raf.write(encodeStr.getBytes());
			raf.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 编码过程，也即加密过程
	 * @param themeId
	 * @return
	 */
	public String encodedThemeId(String themeId){

		String resStr = themeId+"]"+/*getMacAddressStr()*/getDeviceId()+"wewoquvy";
		int len = resStr.length();
		int[] indexArray = getRandomInteger(0, len);

		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < indexArray.length; i++){
			sb.append(resStr.charAt(indexArray[i]));
		}
		sb.append("_");

		for(int i = 0; i < indexArray.length; i++){

			String c = String.format("%02x", indexArray[i]);
			sb.append(c);
		}
		sb.append(";");

		return sb.toString();
	}
	/**
	 * 解码,也即解密过程
	 * @param fileStr
	 * @return
	 */
	public String decodeThemeId(String fileStr){
		String themeId = null;

		String[] str1 = fileStr.split("_");
		final int offset = 2;
		int count = str1[1].length() / offset;
		char[] decodeCharArr = new char[count];
		for(int i = 0; i < count; i++){
			String hexStr = str1[1].substring(offset * i, offset * (i + 1));
			int index = Integer.parseInt(hexStr, 16);

			decodeCharArr[index] = str1[0].charAt(i);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(decodeCharArr);
		String decodeStr = sb.toString();

		//检查是否是本机
		if((decodeStr.contains(getMacAddressStr()) || decodeStr.contains(getDeviceId())) &&
				decodeStr.contains("wewoquvy")){

			themeId = decodeStr.split("]")[0];
		}

		return themeId;
	}

	/**
	 * 返回主题ID数组
	 * @param fromDataFileName
	 * @return
	 */
	public String[] getConfig(String fromDataFileName) {

		try {
			// 获取指定文件对应的输入流
			FileInputStream fis = new FileInputStream(FileSystem.getCacheRootDir(mContext, fromDataFileName).getPath());
			// 将指定输入流包装成BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(
					fis));
			StringBuilder sb = new StringBuilder("");
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String[] themeStrArray = sb.toString().split(";");
			String[] themeIdArray = new String[themeStrArray.length];

			for(int i = 0; i < themeStrArray.length; i++){//解析出所有的主题Id
				themeIdArray[i] = decodeThemeId(themeStrArray[i]);
			}

			return themeIdArray;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getMacAddressStr(){
		return "abcdefghijk012345678";
 		/*WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
 		WifiInfo info = wifi.getConnectionInfo();
 		String macAddress = info.getMacAddress();

 		if(macAddress == null)
 			return "abcdefg";

 		return macAddress.replace(":", "");//去掉冒号
*/ 	}

	private String getDeviceId(){

		if(tm.getDeviceId() == null || tm.getDeviceId().equals(""))
			return "abcdefg";

		return tm.getDeviceId();
	}
}
