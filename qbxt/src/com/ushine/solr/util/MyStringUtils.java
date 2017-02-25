package com.ushine.solr.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 自定义的处理String的应用类
 * @author octocat
 *
 */
public class MyStringUtils {

	/**
	 * 第一次查询
	 */
	public static final String QUERYVALUE="queryValue";
	/**
	 * 第二次查询
	 */
	public static final String AGAINQUERYVALUE="againQueryValue";
	/**
	 * 利用&&分割
	 */
	private static final String SPLIT="&&";
	/**
	 * 分割字符串
	 * @param fieldValue 输入的关键字
	 * @return Map对象，一个key是第一次搜索的关键字；另一个key是第二次搜索的关键字
	 */
	public static Map<String, String> getSplitMap(String fieldValue) {
		Map<String, String> splitMap=new HashMap<>();
		String[]strings=StringUtils.split(fieldValue, SPLIT);
		//分成两个
		int length=ArrayUtils.getLength(strings);
		if (length==2) {
			splitMap.put(QUERYVALUE, strings[0]);
			splitMap.put(AGAINQUERYVALUE, strings[1]);
		}else if (length==1) {
			splitMap.put(QUERYVALUE, strings[0]);
			splitMap.put(AGAINQUERYVALUE, null);
		}
		return splitMap;
	}
}
