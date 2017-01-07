package com.ushine.storeInfo;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Element;
import org.junit.Test;

import com.ushine.common.config.Configured;
import com.ushine.common.utils.PathUtils;
import com.ushine.storeInfo.service.impl.VocationalWorkStoreServiceImpl;
import com.ushine.util.SmbFileUtils;
import com.ushine.util.MyXmlUtilsTest;

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
	
	/*public static boolean test4(String nodeName,String docType){
		boolean b=false;
		try {
			String xml=PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class)+"vocational-work-store.xml";
			XmlUtilsTest utils=new XmlUtilsTest(xml);
			List<Element> elements=utils.getNode(nodeName).elements();
			for (Element element : elements) {
				String result=StringUtils.trim(element.getText());
				if (docType.contains(result)) {
					b=true;
					break;
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return b;
	}*/
	
	public static void test5() {
		SmbFileUtils utils=new SmbFileUtils("192.168.183.128");
		List<String> names=utils.getAllFileName("11");
		System.err.println("------------");
		for (String string : names) {
			System.err.println(string);
		}
	}
	
	@Test
	public void test6() throws Exception {
		//SmbFileUtils utils=new SmbFileUtils("192.168.177.129");
		//utils.copySmbFilesToDir("十二局信息", "e://testcopy");
		SmbFileUtils utils=new SmbFileUtils();
		utils.copySmbWordToDir("\\\\192.168.177.129\\十二局信息", "e://testcopy");
	}
	public static void test7() throws Exception {
		//SmbFileUtils utils=new SmbFileUtils("192.168.183.128");
		//System.err.println(utils.getRootFolderName());
		/*for (int i = 0; i < 100; i++) {
			String name=Long.toString(System.currentTimeMillis());
			File file=new File("c://testcopy"+File.separator+name);
			System.err.println("c://testcopy"+File.separator+name);
			FileUtils.forceMkdir(file);
		}*/
		//String identifyInfo=String.format("识别出%s,共%s个文档", "100","11");
		//System.err.println(identifyInfo);
		System.err.println(StringUtils.equals(null, null));
	}
	
	public static void test8()  throws Exception {
		//复制文件  十二局第63750期—十二局文档782.docx
		//通知（47931）号——测试文档530.docx
		File srcFile=new File("c://通知.doc");
		for(int i=0;i<100;i++){
			Integer integer=(int) (Math.random()*10000);
			Integer integer2=(int) (Math.random()*1000);
			String name=String.format("通知（%s）号———测试文档%s.doc", integer.toString(),integer2.toString());
			File destFile=new File("c://testcopy//"+name);
			FileUtils.copyFile(srcFile, destFile);
			System.err.println(destFile.getName());
		}
	}
	
	public static void test9()  throws Exception {
		String string="vocationalWorkStoreAttachment\\20160806公安技侦情报第43442期——情报文档585.doc";
		System.err.println(string);
		if (string.contains(File.separator)) {
			int index=string.indexOf(File.separator);
			String tempFileName=string.substring(index+9);
			System.err.println(tempFileName);
		}else{
			System.err.println("null");
		}
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
	
	/**
	 * 主机名获得ip地址
	 * @throws Exception
	 */
	@Test
	public void test13()throws Exception {
		InetAddress address = null;

		try {
			address = InetAddress.getByName("192.168.177.129");
		} catch (Exception e) {
			System.out.println("not found");
			System.exit(2);
		}
		System.out.println(address.getHostName() + "=" + address.getHostAddress());
	}
}
