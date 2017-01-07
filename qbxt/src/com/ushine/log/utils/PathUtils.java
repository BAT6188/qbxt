package com.ushine.log.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * 文件操作类：
 * 
 * @author Franklin
 * @version 1.0 2011-11-4 -> 2011-11-4
 * 
 */
public class PathUtils {

	/**
	 * 获取WEB-INF/Classes路径
	 * 
	 * @return String
	 */
	public static String getClassesPath() {
		String path = PathUtils.class.getClassLoader().getResource("/")
				.toString().replace("%20", " ");
		return path.substring(6);
	}

	public static String getCurrentThreadClassPath() {
		return Thread.currentThread().getContextClassLoader().getResource("")
				.getPath().replace("%20", " ");
	}

	/**
	 * 返回JSon数据
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param vo
	 *            Object
	 */
	public static void printHtml(HttpServletResponse response, Object vo) {
		PrintWriter writer = null;
		try {
			response.setContentType("text/html; charset=UTF-8");
			writer = response.getWriter();
			writer.print(JSONObject.fromObject(vo));
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * 返回文字信息
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param str
	 *            文本信息
	 */
	public static void printHtml(HttpServletResponse response, String str) {
		PrintWriter writer = null;
		try {
			response.setContentType("text/html; charset=UTF-8");
			writer = response.getWriter();
			writer.print(str);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

}
