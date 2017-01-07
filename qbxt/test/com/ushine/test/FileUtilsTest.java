package com.ushine.test;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class FileUtilsTest {
	@Test
	public void test1()throws Exception{
		//E:\2011测试导入文档数据\公安技侦情报
		//E:\2011测试导入文档数据\外来文档
		File srcDir=new File("E:/2011测试导入文档数据/外来文档");
		File []files=srcDir.listFiles();
		String replace="外来文档"; 
		String newHeader="中央维稳办";
		for (File file : files) {
			if(file.isFile()&&file.getName().contains(replace)){
				String temp=StringUtils.substring(file.getName(), replace.length());
				String newName=newHeader+temp;
				File destFile=new File("E:/2011测试导入文档数据/中央维稳办/"+newName);
				destFile.setLastModified(file.lastModified());
				FileUtils.copyFile(file, destFile);
				System.err.println(destFile.getName());
			}
		}
	}
	
	@Test
	public void test2()throws Exception{
		String filename="公安技侦情报第9期－“中国劳工通讯”制定2011年度工作目标图谋继续插手国内“劳工维权”.doc";
		String replace="公安技侦情报";
		String newHeader="外来文档";
		if(FilenameUtils.getName(filename).contains("公安技侦情报")){
			int index=FilenameUtils.getName(filename).indexOf("");
			//System.err.println(replace.length()+1);
			String temp=StringUtils.substring(filename, replace.length());
			System.err.println(newHeader+temp);
		}
	}
	
	@Test
	public void test3()throws Exception{
		String path="E:/apache-tomcat-7.0.65/webapps/qbxt/vocationalWorkStoreAttachment/donghao.doc";
		String result=FilenameUtils.getFullPath(path);
		System.err.println(result);
	}
}
