package com.ushine.log.utils;

/**
 * String工具集
 * 
 * @author Franklin
 * 
 */
public class StringUtils {

	/**
	 * 判断是否为空
	 * 
	 * @param s
	 *            String
	 * @return boolean True=值为空/null,False=非空
	 */
	public static boolean isNull(String s) {
		if (s == null || "".equals(s)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串数组是否为空
	 * 
	 * @param s
	 *            String[]
	 * @return boolean True=值为空/null,False=非空
	 */
	public static boolean isNull(String[] s) {
		if (s == null || s.length <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * String转换成int
	 * 
	 * @param s
	 *            String
	 * @return int
	 */
	public static int toInt(String s) {
		int retVal = 0;
		if (s != null && !"".equals(s)) {
			retVal = Integer.parseInt(s);
		}
		return retVal;
	}

	/**
	 * String[]装换成int[]
	 * 
	 * @param array
	 *            String[]
	 * @return int[]
	 */
	public static int[] toInt(String[] array) {
		int[] retVal = null;
		if (array != null && array.length > 0) {
			retVal = new int[array.length];
		}
		for (int i = 0; i < array.length; i++) {
			if (isNull(array[i])) {
				retVal[i] = 0;
			} else {
				retVal[i] = Integer.parseInt(array[i]);
			}
		}
		return retVal;
	}

	/**
	 * 字符串转换为Boolean
	 * 
	 * @param s
	 *            String “true” 或 “false”
	 * @return boolean 输入错误返回false
	 */
	public static boolean toBoolean(String s) {
		if (s.equals("true") || s.equals("false")) {
			return Boolean.parseBoolean(s);
		}
		return false;
	}

}
