package com.ushine.log.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * @ClassName: TimeChange
 * @Description: TODO(sql.Date 和 util.Date 之间相互转换)
 * @date 2012-11-20 上午10:03:25
 * @version:1.0
 * 
 */
public class TimeChangeUtils {
	/**
	 * 
	 * @Title: utilToSql
	 * @Description: TODO(将util.Date转换成sql.Date格式)
	 * @param util
	 *            .Date
	 * @return sql.Date
	 * @throws
	 */
	public static Timestamp utilToSql(java.util.Date utilDate){
		Timestamp sqlDate = null;
		if (utilDate != null) {
			sqlDate = new Timestamp(utilDate.getTime());
		}
		return sqlDate;
	}

	/**
	 * 
	 * @Title: sqlToUtil
	 * @Description: TODO(将sql.Date转换成util.Date格式)
	 * @param sql
	 *            .Date
	 * @return java.util.Date
	 * @throws
	 */
	public static java.util.Date sqlToUtil(Date sqlDate) {
		java.util.Date utilDate = null;
		if (sqlDate != null) {
			utilDate = new java.util.Date(sqlDate.getTime());
		}
		return utilDate;
	}

	/**
	 * 
	 * @Title: stringToUtil
	 * @Description: TODO(将字符串转化成Util时间类型)
	 * @param @param dateStr
	 * @param @return 设定文件
	 * @return java.util.Date 返回类型
	 * @throws
	 */
	public static java.util.Date stringToUtil(String dateStr) {
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = null;
		try {
			date = sim.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 
	 * @Title: stringToSql
	 * @Description: TODO(将字符串转换成sql时间类型)
	 * @param @param dateStr
	 * @param @return 设定文件
	 * @return Date 返回类型
	 * @throws
	 */
	public static Timestamp stringToSql(String dateStr) {
		return TimeChangeUtils.utilToSql(TimeChangeUtils.stringToUtil(dateStr));
	}

	/**
	 * 
	 * @Title: utilToString
	 * @Description: TODO(将util时间转换成字符串)
	 * @param @param date
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String utilToString(java.util.Date date) {
		String dateShow = null;
		if (date != null) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			dateShow = format.format(date);
		}
		return dateShow;
	}

	/**
	 * 
	 * @Title: sqlToString
	 * @Description: TODO(将sql时间转换成字符串)
	 * @param @param date
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String sqlToString(Timestamp date) {
		return TimeChangeUtils.utilToString(TimeChangeUtils.utilToSql(date));
	}

}
