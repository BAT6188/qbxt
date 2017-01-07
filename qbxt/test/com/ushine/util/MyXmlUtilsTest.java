package com.ushine.util;

import java.io.File;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.ushine.common.utils.PathUtils;
import com.ushine.common.utils.XMLUtils;
import com.ushine.storeInfo.service.impl.OutsideDocStoreServiceImpl;

public class MyXmlUtilsTest {
	//测试添加节点
	@Test
	public void test1() throws Exception {
		String xml = PathUtils.getConfigPath(OutsideDocStoreServiceImpl.class) + "outside-doc-store.xml";
		//System.err.println(xml);
		XMLUtils utils=new XMLUtils(xml);
		//添加一个节点
		//System.err.println(utils.addElement("first-type","element", "军队文档"));
		List<String> list=FileUtils.readLines(new File(xml), Charsets.UTF_8);
		for (String string : list) {
			if (StringUtils.contains(string, "\u8463\u660a")) {
				//String result=StringUtils.replace(string, "<element>\u8463\u660a</element>", "<element>董昊</element>");
				System.out.println("contains");
			}
			//System.out.println(string);
		}
		//保存文件
		//FileUtils.write
	}
}
