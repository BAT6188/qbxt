package com.ushine.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.dom4j.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ushine.common.utils.PathUtils;
import com.ushine.common.utils.XMLUtils;
import com.ushine.storesinfo.service.impl.VocationalWorkStoreServiceImpl;

import net.sf.json.JSONObject;

/**
 * 识别文档工具类
 * 
 * @author Administrator
 *
 */
public class IdentifyDocUtils {
	
	private static Logger logger = LoggerFactory.getLogger(IdentifyDocUtils.class);
	/**
	 * 获得文档的标题
	 * 
	 * @param string
	 * @return
	 */
	public static String getTitle(String string) {
		String result = new String();
		try {
			// 英文横线
			if (StringUtils.contains(string, "-")) {
				// 包含三个---
				if (StringUtils.contains(string, "---")) {
					result = StringUtils.substringAfter(string, "---");
				}
				// 包含两个--
				else if (StringUtils.contains(string, "--") && !StringUtils.contains(string, "---")) {
					result = StringUtils.substringAfter(string, "--");
				} else {
					result = StringUtils.substringAfter(string, "-");
				}
			}
			// 中文横线
			else if (StringUtils.contains(string, "—")) {
				// 包含三个 ———
				if (StringUtils.contains(string, "———")) {
					result = StringUtils.substringAfter(string, "———");
				}
				// 只包含两个 ——
				else if (StringUtils.contains(string, "——") && !StringUtils.contains(string, "———")) {
					result = StringUtils.substringAfter(string, "——");
				} else {
					result = StringUtils.substringAfter(string, "—");
				}
			}
			// 全脚的横线
			else if (StringUtils.contains(string, "－")) {
				// 包含三个－－－
				if (StringUtils.contains(string, "－－－")) {
					result = StringUtils.substringAfter(string, "－－－");
				}
				// 只包含两个－－
				else if (StringUtils.contains(string, "－－") && !StringUtils.contains(string, "－－－")) {
					result = StringUtils.substringAfter(string, "－－");
				} else {
					result = StringUtils.substringAfter(string, "－");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取xml文件配置项，判断是否能否识别该类型
	 * 
	 * @param nodeName
	 *            节点名称
	 * @param docType
	 *            类型包括技侦情报、十二局信息、通知等
	 * @return
	 */
	private static boolean isCanBeIdentified(String nodeName, String docType) {
		boolean b = false;
		try {
			String xml = PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class) + "vocational-work-store.xml";
			XMLUtils utils = new XMLUtils(xml);
			List<Element> elements = utils.getNode(nodeName).elements();
			for (Element element : elements) {
				String result = StringUtils.trim(element.getText());
				if (docType.contains(result)) {
					// 包含该类别
					b = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	
	private static boolean isForeignDocCanBeIdentified(String nodeName, String docType) {
		boolean b = false;
		try {
			String xml = PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class) + "outside-doc-store.xml";
			XMLUtils utils = new XMLUtils(xml);
			List<Element> elements = utils.getNode(nodeName).elements();
			for (Element element : elements) {
				String result = StringUtils.trim(element.getText());
				if (docType.contains(result)) {
					// 包含该类别
					b = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 识别业务文档的期刊号、标题
	 * @param filePath
	 * @param docType
	 * @return
	 * @throws Exception
	 */
	public static String identifyServiceDoc(String filePath, String docType) throws Exception {
		// 识别导入的业务文档
		File file = new File(filePath);
		// 输出成json
		JSONObject root = new JSONObject();
		StringBuffer unIdentifyDetail = new StringBuffer();
		// 技侦情报、十二局信息文件名格式为 20111109公安技侦情报第00100期—习近平在哈萨克斯坦纳扎尔巴耶夫大学发表重要演讲
		String fileName = file.getName();
		String docNumber = "";
		String title = "";
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		// 文档最后修改时间
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		Date date = format1.parse(fileName.substring(0, 8));
		String time = format2.format(date);
		// 技侦情报、十二局
		if (isCanBeIdentified("first-type", docType) && fileName.contains(docType)) {
			int index0 = fileName.indexOf("第");
			int index1 = fileName.indexOf("期");
			if (index1 > index0 && index0 > 0 && index1 > 0) {
				// 中间的就是期刊号
				docNumber = fileName.substring(index0 + 1, index1);
				
				// 标题
				title = IdentifyDocUtils.getTitle(fileName);
				if (StringUtils.isEmpty(title)) {
					title = StringUtils.substring(fileName, index1 + 1);
				}
			}
		}
		// 请示 报告 通知 函是另外一种形式 类别(数字)号—名称
		if (isCanBeIdentified("second-type", docType) && fileName.contains(docType)) {
			// 期刊号
			if (fileName.contains("(") && fileName.contains(")")) {
				// 英文括号
				int index0 = fileName.indexOf("(");
				int index1 = fileName.indexOf(")");
				// 期刊号
				if (index1 > index0) {
					docNumber = fileName.substring(index0 + 1, index1);
				}
				// 标题
				title = IdentifyDocUtils.getTitle(fileName);
				if (StringUtils.isEmpty(title)) {
					int index2 = fileName.indexOf("号");
					if (index2 > index1) {
						title = StringUtils.substring(fileName, index2 + 1);
					} else {
						title = StringUtils.substring(fileName, index1 + 1);
					}
				}
			} else if (fileName.contains("（") && fileName.contains("）")) {
				// 中文括号
				int index0 = fileName.indexOf("（");
				int index1 = fileName.indexOf("）");
				// 期刊号
				if (index1 > index0) {
					docNumber = fileName.substring(index0 + 1, index1);
				}
				// 标题
				title = IdentifyDocUtils.getTitle(fileName);
				if (StringUtils.isEmpty(title)) {
					int index2 = fileName.indexOf("号");
					if (index2 > index1) {
						title = StringUtils.substring(fileName, index2 + 1);
					} else {
						title = StringUtils.substring(fileName, index1 + 1);
					}
				}
			}
		}

		fileName = StringUtils.substring(fileName, 8);
		root.element("fileName", fileName);
		root.element("docNumber", docNumber);
		root.element("docName", FilenameUtils.getBaseName(title));
		root.element("time", time);
		root.element("infoType", docType);
		root.element("filePath", filePath);
		// 输出成json
		return root.toString();
	}
	
	/**
	 * 识别外来文档的期刊号、标题
	 * @param filePath
	 * @param docType
	 * @return
	 * @throws Exception
	 */
	public static String identifyForeignDoc(String filePath, String docType) throws Exception {
		// 识别导入的外来文档
		File file = new File(filePath);
		// 输出成json
		JSONObject root = new JSONObject();
		StringBuffer unIdentifyDetail = new StringBuffer();
		String fileName = file.getName();
		String docNumber = "";
		String title = "";
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		// 文档最后修改时间
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		Date date = format1.parse(fileName.substring(0, 8));
		String time = format2.format(date);
		//
		if (isForeignDocCanBeIdentified("first-type", docType) && fileName.contains(docType)) {
			int index0 = fileName.indexOf("第");
			int index1 = fileName.indexOf("期");
			if (index1 > index0 && index0 > 0 && index1 > 0) {
				// 中间的就是期刊号
				docNumber = fileName.substring(index0 + 1, index1);
				
				// 标题
				title = IdentifyDocUtils.getTitle(fileName);
				if (StringUtils.isEmpty(title)) {
					title = StringUtils.substring(fileName, index1 + 1);
				}
			}
		}
		

		fileName = StringUtils.substring(fileName, 8);
		root.element("fileName", fileName);
		root.element("docNumber", docNumber);
		root.element("docName", FilenameUtils.getBaseName(title));
		root.element("time", time);
		root.element("infoType", docType);
		root.element("filePath", filePath);
		// 输出成json
		return root.toString();
	}
	/**
	 * 将字符串转成长整形
	 * @param lastModified
	 * @return 成功为true
	 */
	private static boolean convertToLong(String lastModified){
		try{
			Long value=Long.parseLong(lastModified);
			return true;
		}catch(Exception exception){
			return false;
		}
	}
	/**
	 * 将最后修改时间转为yyyyMMdd
	 * @param lastModified
	 * @return
	 */
	public static String getLastModified(String lastModified){
		String pattern="yyyyMMdd";
		try {
			if(convertToLong(lastModified)){
				Long value=Long.parseLong(lastModified);
				return DateFormatUtils.format(value, pattern);
			}else{
				//文本型日期
				//Thu Nov 24 2016 10:49:20 GMT+0800 (中国标准时间)
				Calendar calendar=Calendar.getInstance().getInstance(new Locale(lastModified));
				String result=DateFormatUtils.format(calendar, pattern);
				logger.info("文本型："+lastModified+"转为日期格式："+result);
				return result;
			}
		} catch (Exception e) {
			String today=DateFormatUtils.format(new Date(), pattern);
			logger.info("获得最后修改时间失败，转成今天日期："+lastModified);
			return today;
		}
	}
	//测试识别文档是否通过
	@Test
	public void test1()throws Exception{
		String filePath="‪E:\\apache-tomcat-7.0.65\\webapps\\qbxt\\20110101外来文档第9期－“中国劳工通讯”制定2011年度工作目标图谋继续插手国内“劳工维权”.doc";
		String result=IdentifyDocUtils.identifyForeignDoc(filePath, "外来文档");
		System.err.println(result);
	}
	
	@Test
	public void test2()throws Exception{
		System.err.println(getLastModified("Thu Nov 24 2016 10:49:20 GMT+0800 (中国标准时间)"));
		//System.err.println(getLastModified("1479952301894"));
		//Calendar calendar=Calendar.getInstance().getInstance(new Locale("Thu Nov 24 2016 10:49:20 GMT+0800 (中国标准时间)"));
		//System.err.println(DateFormatUtils.format(calendar, "yyyyMMdd"));
	}
}
