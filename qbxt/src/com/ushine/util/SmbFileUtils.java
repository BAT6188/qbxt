package com.ushine.util;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jcifs.smb.SmbFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 对网络共享文件夹文件操作的应用类
 * @author dh
 *
 */
public class SmbFileUtils {
	private static final Logger logger = LoggerFactory.getLogger(SmbFileUtils.class); 
	private static final String seperator="/";
	private static final String smb="smb://";
	private List<String> nameList=new ArrayList<>();
	private String ip;
	/**
	 * 构造函数
	 * @param ip ip地址
	 */
	public SmbFileUtils(String ip) {
		this.ip=ip;
	}
	
	public SmbFileUtils() {
		
	}
	/**
	 * 获得根路径下某文件夹所有子文件及子文件夹中的文件名
	 * @param folderName 文件夹名称
	 * @return 文件名的list集合
	 */
	public List<String> getAllFileName(String folderName) {
		try {
			String url=smb+ip+seperator+folderName+seperator;
			nameList=listFilesName(nameList, url);
		} catch (Exception e) {
			logger.error("获得文件夹下所有文件名失败");
		}
		return nameList;
	}
	/**
	 * 复制共享文件夹下所有子文件到本地目标文件夹中
	 * @param sourceFolder 共享文件夹名称
	 * @param targetFolder 本地目标文件夹名称
	 */
	public void copySmbFilesToDir(String sourceFolder,String targetFolder) {
		String url=smb+ip+seperator+sourceFolder+seperator;
		copyAllFilesOfDir(url, targetFolder);
	}
	/**
	 * 只拷贝word
	 * @param url \\192.168.177.129\2011年情报\公安技侦情报\公安技侦情报第99期－公安技侦截获2011年香港区议会选举获提名人员材料
	 * @param targetFolder
	 */
	public void copySmbWordToDir(String url,String targetFolder)throws Exception{
		url="smb:"+url+seperator;
		copyAllFilesOfDir(url, targetFolder);
	}
	
	/**
	 * 获得该ip根路径下的所有可见文件夹名称
	 * @return
	 */
	public String getRootFolderName() {
		String url=smb+ip+seperator;
		JSONArray array=new JSONArray();
		try {
			if (validSmbIp(url)) {
				SmbFile file=new SmbFile(url);
				SmbFile []dirs=file.listFiles();
				for (SmbFile dir : dirs) {
					if (dir.isDirectory()&&!dir.isHidden()) {
						JSONObject json=new JSONObject();
						String value=StringUtils.substring(dir.getName(), 0, dir.getName().length()-1);
						json.put("folderName", value);
						array.add(json);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return array.toString();
	}
	/**
	 * 验证地址是否有效
	 * @param url
	 * @return
	 */
	private boolean validSmbIp(String url) {
		boolean valid=false;
		try {
			SmbFile file=new SmbFile(url);
			file.connect();
			valid=true;
		} catch (Exception e) {
			logger.error("无法获得"+url+"网络共享");
		}
		return valid;
	}
	/**
	 * 设置SmbFile的文件名称
	 * @param file SmbFile
	 * @return 名称为源文件最后修改日期+文件名
	 */
	private String setTargetFileName(SmbFile file) {
		String fileName=file.getName();
		try {
			long lastModified=file.lastModified();
			Calendar calendar=Calendar.getInstance();
			calendar.setTimeInMillis(lastModified);
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			String date=format.format(calendar.getTime());
			fileName=date+fileName;
		} catch (Exception e) {
			logger.error("设置目标文件名称异常");
		}
		return fileName;
	}
	/**
	 * 拷贝url下所有文件到目标文件夹中
	 * @param url
	 * @param dirctoryPath
	 */
	private void copyAllFilesOfDir(String url,String dirctoryPath){
		logger.info(url);
		InputStream inputStream=null;
		try {
			if (validSmbIp(url)) {
				SmbFile file=new SmbFile(url);
				if (file.isDirectory()&&!file.isHidden()) {
					//拷贝文件夹中的文件
					//只拷贝该目录下的文件,不考虑子文件夹下的文件
					SmbFile[] files=file.listFiles();
					int i=0;
					for (SmbFile smbFile : files) {
						//System.err.println(smbFile.getPath());
						if (smbFile.isFile()) {
							i++;
							//递归拷贝文件夹
							//copyAllFilesOfDir(smbFile.getPath(),dirctoryPath);
							//只处理word
							String extention=FilenameUtils.getExtension(smbFile.getName());
							if (extention.equals("doc")||extention.equals("docx")) {
								//拷贝文件
								inputStream=smbFile.getInputStream();
								File targetFile=new File(dirctoryPath+File.separator+setTargetFileName(smbFile));
								logger.info("正在复制第"+i+"文件："+smbFile.getName());
								FileUtils.copyInputStreamToFile(inputStream, targetFile);
							}
						}
					}
				}else{
					//拷贝文件
					inputStream=file.getInputStream();
					File targetFile=new File(dirctoryPath+File.separator+setTargetFileName(file));
					logger.info("正在复制"+file.getName());
					FileUtils.copyInputStreamToFile(inputStream, targetFile);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally {
			try {
				if (null!=inputStream) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 递归获得文件夹下所有子文件及子文件夹中的文件名<br>
	 * 要获得结果将list作为参数传递并返回该list
	 * @param names list集合
	 * @param url 共享地址url,形如smb://192.168.183.128//11/
	 * @return list集合
	 */
	private List<String> listFilesName(List<String>names,String url){
		logger.info(url);
		try {
			SmbFile file=new SmbFile(url);
			if (file.isDirectory()&&!file.isHidden()) {
				//文件夹
				SmbFile[] files=file.listFiles();
				for (SmbFile smbFile : files) {
					if (smbFile.isDirectory()) {
						listFilesName(names,smbFile.getPath());
					}else{
						names.add(smbFile.getName());
					}
				}
			}else{
				//文件
				names.add(file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return names;
	}
}
