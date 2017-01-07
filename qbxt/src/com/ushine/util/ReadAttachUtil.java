package com.ushine.util;

import java.io.*;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

/**
 * 读取附件的内容
 * @author Administrator
 *
 */
public class ReadAttachUtil {
	/**
	 * 判断是否是2003版本
	 * @param filePath
	 * @return
	 */
	public static boolean isWord2003(String filePath){
		WordExtractor ex = null;
		FileInputStream is =null;
		boolean result=false;
		try {
			is = FileUtils.openInputStream(new File(filePath));
			ex = new WordExtractor(is);
			if(ex!=null){
				result=true;
			}
		} catch (Exception e) {			
		}finally {
			IOUtils.closeQuietly(is);
		}
		return result;
	}
	/**
	 * 判断是否是2007版本
	 * @param filePath
	 * @return
	 */
	public static boolean isWord2007(String filePath){
		OPCPackage opcPackage =null;
		boolean result=false;
		try {
			opcPackage = POIXMLDocument.openPackage(filePath);
			if (null!=opcPackage) {
				result=true;
			}
		} catch (Exception e) {			
		}finally {
			if(null!=opcPackage){
				try {
					opcPackage.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	/**
	 * 读取内容,支持excel、word、文本
	 * @param filePath
	 * @return
	 */
	public static String readContent(String filePath) {
		String content=new String();
		InputStream is=null;
		OPCPackage opcPackage =null;
		try {
			//后缀
			String extention=FilenameUtils.getExtension(filePath);
			File file=new File(filePath);
			if(file.exists()){
				if ("xls".equals(extention)||"xlsx".equals(extention)) {
					//Excel2003 2007
					POIReadExcelToHtml poiReadExcelToHtml=new POIReadExcelToHtml();
					//带有html标签
					content=poiReadExcelToHtml.readExcelToHtml(filePath, false);
					if (StringUtils.isNotEmpty(content)) {
						//去除标签
						content=content.replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", " ").replaceAll
								("</[a-zA-Z]+[1-9]?>", " ").replaceAll("&nbsp;", " ");
					}
				}
				//不同版本的word需要使用不同的方法
				else if (isWord2003(filePath)) {
					//word2003
					is = FileUtils.openInputStream(new File(filePath));
					WordExtractor ex = new WordExtractor(is);
					content = ex.getText();
				}
				else if(isWord2007(filePath)){
					opcPackage = POIXMLDocument.openPackage(filePath);
					POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
					content = extractor.getText();
				}
				else if("txt".equals(extention)||"html".equals(extention)){
					content=FileUtils.readFileToString(new File(filePath),Charset.forName("utf-8"));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				IOUtils.closeQuietly(is);
				if(null!=opcPackage){
					opcPackage.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}
}
