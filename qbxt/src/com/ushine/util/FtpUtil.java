package com.ushine.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ushine.common.config.Configured;


/**
 * ftp上传文件或文件夹的工具类
 *  ftpIp=192.168.10.226
	ftpPort=21
	ftpUser=donghao
	strIp=Configured.getInstance().get("ftpIp");
		intPort=Integer.parseInt(Configured.getInstance().get("ftpPort"));
		user=Configured.getInstance().get("ftpUser");
		password=Configured.getInstance().get("ftpPasswd");
	ftpPasswd=donghao
 * @author dh
 *
 */
public class FtpUtil {
	/**
	 * 初始化
	 */
	private static String strIp=Configured.getInstance().get("ftpIp");;
	private static int intPort=Integer.parseInt(Configured.getInstance().get("ftpPort"));;
	private static String user=Configured.getInstance().get("ftpUser");;
	private static String password=Configured.getInstance().get("ftpPasswd");
	//工作目录
	private static String baseDir=Configured.getInstance().get("ftpBaseDir");
	
	private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
	
	private static FTPClient ftpClient=new FTPClient();

	/**
	 * @return 判断是否登入成功
	 * */
	public static boolean ftpLogin() {
		boolean isLogin = false;
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			//windows
			ftpClient.setControlEncoding("GBK");
		}else{
			//linux
			ftpClient.setControlEncoding("utf-8");
		}
		ftpClient.configure(ftpClientConfig);
		try {
			if (intPort > 0) {
				ftpClient.connect(strIp, intPort);
			} else {
				ftpClient.connect(strIp);
			}
			// FTP服务器连接回答
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				logger.error("登录FTP服务失败！");
				return isLogin;
			}
			ftpClient.login(user, password);
			// 设置传输协议
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setBufferSize(1024 * 2);
			ftpClient.setDataTimeout(30 * 1000);
			ftpClient.changeWorkingDirectory(baseDir);
			logger.info("恭喜" + user + "成功登陆FTP服务器");
			isLogin = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(user + "登录FTP服务失败！" + e.getMessage());
		}
		return isLogin;
	}

	/**
	 * @退出关闭服务器链接
	 * */
	public static void ftpLogOut() {
		if (null != ftpClient && ftpClient.isConnected()) {
			try {
				boolean reuslt = ftpClient.logout();// 退出FTP服务器
				if (reuslt) {
					logger.info("成功退出服务器");
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("退出FTP服务器异常！" + e.getMessage());
			} finally {
				try {
					ftpClient.disconnect();// 关闭FTP服务器的连接
				} catch (IOException e) {
					e.printStackTrace();
					logger.warn("关闭FTP服务器的连接异常！");
				}
			}
		}
	}
	
	/***
	 * 上传Ftp文件
	 * 
	 * @param localFile
	 *            当地文件
	 * @param romotUpLoadePath上传服务器路径
	 *            - 应该以/结束
	 * */
	public static boolean uploadFile(File localFile, String romotUpLoadePath) {
		BufferedInputStream inStream = null;
		boolean success = false;
		try {
			ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
			inStream = new BufferedInputStream(new FileInputStream(localFile));
			logger.info(localFile.getName() + "开始上传.....");
			//获得最后修改时间
			long value=localFile.lastModified();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(value);
			//String time=calendar.getTime().toLocaleString();
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			//日期
			String time=format.format(calendar.getTime());
			String fileName=FilenameUtils.getName(localFile.getAbsolutePath());
			//日期+名称
			fileName=time+fileName;
			//success = ftpClient.storeFile(localFile.getName(), inStream);
			//设置文件的名称
			success = ftpClient.storeFile(fileName, inStream);
			if (success == true) {
				logger.info(localFile.getName() + "上传成功");
				return success;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(localFile + "未找到");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	/***
	 * @上传文件夹
	 * @param localDirectory
	 *            当地文件夹
	 * @param remoteDirectoryPath
	 *            Ftp 服务器路径 以目录"/"结束
	 * */
	public static boolean uploadDirectory(String localDirectory,
			String remoteDirectoryPath) {
		File src = new File(localDirectory);
		
		if(src.exists()){
			try {
				
				remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
				logger.info("远程文件夹地址"+remoteDirectoryPath);
				ftpClient.makeDirectory(remoteDirectoryPath);
				// ftpClient.listDirectories();
			} catch (IOException e) {
				e.printStackTrace();
				logger.info(remoteDirectoryPath + "目录创建失败");
			}
			File[] allFile = src.listFiles();
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (!allFile[currentFile].isDirectory()) {
					String srcName = allFile[currentFile].getPath().toString();
					uploadFile(new File(srcName), remoteDirectoryPath);
				}
			}
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (allFile[currentFile].isDirectory()) {
					// 递归
					uploadDirectory(allFile[currentFile].getPath().toString(),
							remoteDirectoryPath);
				}
			}
			return true;
		}
		return false;
	}
	/**
	 * 上传本地文件夹中的文件到ftp中
	 */
	public static boolean uploadFileOfDirectory(String localDirectory,String remotePath){
		try {
			//通过浏览器上传时无法获得文件列表的！！！！！！！！
			File src=new File(localDirectory);
			if (src.isDirectory()) {
				logger.info("上传的是本地文件夹");
			}else if (src.isFile()) {
				logger.info("上传的是本地文件");
			}else{
				logger.info("无法获得上传的文件列表");
			}
			ftpClient.makeDirectory(remotePath);
			//清空原有文件
			ftpClient.changeWorkingDirectory(remotePath);
			FTPFile []files=ftpClient.listFiles();
			for (FTPFile ftpFile : files) {
				//logger.info("ftpFile.getName()"+ftpFile.getName());
				ftpClient.deleteFile(ftpFile.getName());
			}
			
			File[] allFile = src.listFiles();
			//logger.info("文件夹下文件数量"+allFile.length);
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				//只上传文件
				if (!allFile[currentFile].isDirectory()) {
					String srcName = allFile[currentFile].getPath().toString();
					
					uploadFile(new File(srcName), remotePath);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Test
	public void test() throws Exception{
		FtpUtil.ftpLogin();
		//FtpUtil.uploadFileOfDirectory("c:\\Users\\dh\\Desktop\\测试上传", "donghao");
		//System.err.println(FtpUtil.uploadDirectory("D:\\Users\\dh\\Desktop\\测试上传", "/"));
		FtpUtil.uploadFileOfDirectory("C:\\Users\\Administrator\\Desktop\\我的测试上传", "shared");
		FtpUtil.ftpLogOut();
	}
}
