package com.ushine.storeInfo;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import com.ushine.common.utils.PathUtils;

public class WordTest {

	public static void main(String[] args) {
		try {
			String path="D:\\Users\\dh\\Desktop\\通知\\通知（20005）号——测试文档021.docx";
			File srcFile=new File(path);
			String basePath=FilenameUtils.getFullPath(path);
			for(int i=0;i<200;i++){
				long time=System.currentTimeMillis();
				String number=Long.toString(time).substring(Long.toString(time).length()-5,Long.toString(time).length());
				int value=(int) (Math.random()*1000);
				String name="通知（"+number+"）号——测试文档"+Integer.toString(value)+".docx";
				File destFile=new File(basePath+name);
				FileUtils.copyFile(srcFile, destFile, true);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		
	@Test
	public void test()throws Exception{
		//System.err.println(DateUtils.parseDate("2009-11-09", new String[]{}));
		//System.err.println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
		//System.err.println(RandomStringUtils.r);
		//‪C:\Users\Administrator\Desktop\新建文本文档.txt
		System.err.println(FileUtils.readFileToString(new File("e://测试.txt")));
		/*System.err.println(FileUtils.readFileToString(new File("e://测试.txt"),
				Charset.forName("utf-8")));*/
	}		
	/**
	 * 复制文件
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		//复制文件  十二局第63750期—十二局文档782.docx
		File srcFile=new File("E:\\2011年情报\\公安技侦情报");
		if(srcFile.isDirectory()){
			File[] files=srcFile.listFiles();
			for (File file : files) {
				if(file.isFile()){
					Integer integer=(int) (Math.random()*10000);
					Integer integer2=(int) (Math.random()*1000);
					//- ———
					String name=String.format("报告(%s)号文档%s.doc", integer.toString(),integer2.toString());
					File destFile=new File("E:\\testupload\\报告\\"+name);
					FileUtils.copyFile(file, destFile);
					System.err.println(destFile.getName());
				}
			}
		}
	}
	
	@Test
	public void test2() throws Exception {
		//复制文件  十二局第63750期—十二局文档782.docx
		//通知（47931）号——测试文档530.docx
		File destFile=new File("E:\\testupload\\函\\");
		File []files=destFile.listFiles();
		for (File file : files) {
			file.renameTo(new File("E:\\testupload\\函\\"+file.getName()+"x"));
		}
	}
	@Test
	public void test3() throws Exception {
		//System.err.println(FilenameUtils.getPathNoEndSeparator(PathUtils.getConfigPath(WordTest.class)));
		
	}
}
