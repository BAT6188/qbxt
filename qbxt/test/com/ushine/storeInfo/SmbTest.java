package com.ushine.storeInfo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.ushine.common.config.Configured;
import com.ushine.util.SmbFileUtils;

import jcifs.smb.SmbFile;

/**
 * 访问共享文件夹内容
 * @author dh
 *
 */
public class SmbTest {
	/**
	 * 访问某一文件夹里的文件
	 */
	@Test
	public void test1(){
		InputStream inputStream=null;
		try {
			String url="smb://127.0.0.1/test_upload/";
			SmbFile smbFile=new SmbFile(url);
			smbFile.connect();
			SmbFile []smbFiles=smbFile.listFiles();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			for (SmbFile file : smbFiles) {
				long lastModified=file.lastModified();
				Calendar calendar=Calendar.getInstance();
				calendar.setTimeInMillis(lastModified);
				//复制到本地
				inputStream=file.getInputStream();
				File arg1=new File("e:/tempFiles/"+file.getName());
				System.err.println("正在复制,名称:"+file.getName()+",最后修改日期:"+format.format(calendar.getTime()));
				FileUtils.copyInputStreamToFile(inputStream, arg1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (null!=inputStream) {
					inputStream.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 共享机器下面的所有文件夹
	 */
	public static void test2() {
		try {
			//Dh-pc
			/**
			 * access denied 原因在于用户权限不足
			 * 把用户所属组改为administrator就行
			 * 维护网上政治安全专项机制---侦控工作报告 (192.168.183.128)/
			 */
			String url="smb://127.0.0.1/";
			//String url="smb:"+File.separator+"192.168.183.128"+File.separator;
			//System.err.println(url);
			SmbFile smbFile=new SmbFile(url);
			//System.err.println(smbFile.getAllowUserInteraction());
			if (smbFile.exists()) {
				SmbFile []smbDirs=smbFile.listFiles();
				for (SmbFile directory : smbDirs) {
					System.err.println(directory.getName());
					//System.err.println("----------");
					if (!directory.isHidden()&&directory.isDirectory()) {
						//+"是否隐藏："+file.isHidden()
						System.err.println("文件夹名称："+directory.getName()+",directory"+directory.getPath());
						/*SmbFile []smbFiles=directory.listFiles();
						for (SmbFile file : smbFiles){
							if (file.isFile()&&!file.isHidden()) {
								System.err.println("文件名称："+file.getName());
							}
						}*/
					}
					//System.err.println("----------");
					/*if (file.isDirectory()) {
						SmbFile []folderFiles=file.listFiles();
						for (SmbFile folderFile : folderFiles) {
							System.err.println("子目录,名称:"+folderFile.getName()+"是否是文件夹:"+folderFile.isDirectory());
						}
					}*/
				}
			}else{
				System.err.println("无法访问共享文件夹");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test6() throws Exception {
		//SmbFileUtils utils=new SmbFileUtils("192.168.177.129");
		//utils.copySmbFilesToDir("十二局信息", "e://testcopy");
		SmbFileUtils utils=new SmbFileUtils();
		utils.copySmbWordToDir("\\\\192.168.177.129\\十二局信息", "e://testcopy");
	}
	
	@Test
	public void test10()throws Exception {
		SmbFileUtils utils=new SmbFileUtils();
		//直接就可以访问，无需转成ip地址
		String url="\\\\Donghao-47b5bbd\\通知";
		url = url.replaceAll("\\\\", "/");
		System.err.println("url："+url);
		utils.copySmbWordToDir(url,"e://testupload");
		
	}
	/**
	 * 192.168.177.129
	 * @throws Exception
	 */
	@Test
	public void test15()throws Exception {
		String url="\\\\Donghao-47b5bbd\\通知";
		url = url.replaceAll("\\\\", "/");
		SmbFile smbFile=new SmbFile("smb:"+url);
		SmbFile []files=smbFile.listFiles();
		for (SmbFile file : files) {
			System.err.println(file.getName());
		}
	}
	
	/**
	 * 格式化日期
	 * @throws Exception
	 */
	@Test
	public void test12()throws Exception {
		String string="1991.9.04";
		String []parsePatterns=Configured.getInstance().get("parsePatterns").split(",");
		Date date=DateUtils.parseDate(string, parsePatterns);
		assertEquals(DateFormatUtils.format(date, "yyyy-MM-dd"), "1991-09-04");
	}
}
