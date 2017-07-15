package com.android.file.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class FileToBase64 {
	
	public FileToBase64(){
		
	}
	/**
	 * 
	 * @param fulFilelName 如 "/sdcard/XDTdata/123.zip" 
	 * @return
	 */
	public static String fileToBase64(String fulFilelName) {
		
		try 
		{
			FileInputStream fis = new FileInputStream(fulFilelName);  
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
 
			byte[] buffer = new byte[16];  
			int count = 0;  
			while ((count = fis.read(buffer, 0, 16)) >= 0)  
			{  
			    baos.write(buffer, 0, count);   
			}  
  
			String imageBase64 = new String(Base64.encodeBase64(baos.toByteArray()));  
			
			fis.close();
			
			return imageBase64; 
		
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return "";
		} catch (IOException e) 
		{
			e.printStackTrace();
			return "";
		}		
		
	}	
	
	public static byte[] base64StringToByteArray(String base64String) {
		
		return Base64.decodeBase64(base64String.getBytes());
	}
	
}
