package com.ushine.test;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

/**
 * 将中文输出为Unicode编码或将Unicode输出为中文
 * @author Administrator
 *
 */
public class UnicodeTest {

	public static void main(String[] args) {
		String cn = "怀念外婆屋后的柚子树";
		System.out.println(cnToUnicode(cn));
		// 字符串 : \u5f00\u59cb\u4efb\u52a1 ，由于 \ 在java里是转义字符，要写出下面这种形式
		String unicode = "\\u5f00\\u59cb\\u4efb\\u52a1";
		System.out.println(unicodeToCn(unicode));
	}

	private static String unicodeToCn(String unicode) {
		/** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格 */
		String[] strs = unicode.split("\\\\u");
		String returnStr = "";
		// 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
		for (int i = 1; i < strs.length; i++) {
			returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
		}
		return returnStr;
	}

	private static String cnToUnicode(String cn) {
		char[] chars = cn.toCharArray();
		String returnStr = "";
		for (int i = 0; i < chars.length; i++) {
			returnStr += "\\u" + Integer.toString(chars[i], 16);
		}
		return returnStr;
	}
	/**
	 * 使用commons-lang包下的StringEscapeUtils
	 * @throws Exception
	 */
	@Test
	public void test1()throws Exception{
		System.out.println(UnicodeTest.cnToUnicode("董昊"));
		System.out.println(StringEscapeUtils.escapeJava("董昊"));
	}
}