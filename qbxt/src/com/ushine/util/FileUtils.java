package com.ushine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.util.ArrayList;

import org.junit.Test;

import net.sf.json.JSONArray;

/**
 * 文件工具类,用于对文件的处理
 * 
 * @author 蔡平
 * 
 */

public class FileUtils {
	/**
	 * 将前台的csv文件读取出来 return ArrayList<String[]>
	 */
	public static ArrayList<String[]> readeCsv(Reader re) {
		ArrayList<String[]> csvList = new ArrayList<String[]>(); // 用来保存数据
		try {
			BufferedReader bfd = new BufferedReader(re);
			Character c = ' ';
			String str = "";
			int length=0;
			while ((str = bfd.readLine()) != null) {
				if ((length=str.indexOf(",")) != -1) {
					c = ',';
				} else if ((length=str.indexOf("|")) != -1) {
					c = '|';
				}
				if((length+1)==str.length()){
					str+=" ";
				}
				String[] lins = str.split("\\" + c + "");
				csvList.add(lins);
			}
			System.out.println(JSONArray.fromObject(csvList).toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return csvList;
	}
	//删除文件
	public static boolean deleteFile(String sPath) {  
		boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}
	
	@Test
	public void test()throws Exception{
		
	}
}
