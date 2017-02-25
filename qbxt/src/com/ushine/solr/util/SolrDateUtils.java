package com.ushine.solr.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.ushine.solr.solrbean.QueryBean;

/**
 * 处理timedamp的应用类
 * 
 * @author dh
 * 
 */
public class SolrDateUtils {
	private static Logger logger = Logger.getLogger(SolrDateUtils.class);
	/**
	 * 日期格式常量
	 */
	private static final String DATE_PATTERNS = "yyyy-MM-dd HH:mm:ss";

	private static final String YMD_PATTERNS = "yyyy-MM-dd";

	/**
	 * 开始时间
	 */
	public static String START_TIME = " 00:00:00";
	/**
	 * 结束时间
	 */
	public static String END_TIME = " 23:59:59";

	/**
	 * 将给定的日期按规定格式转成long型
	 * 
	 * @param date
	 *            日期
	 * @return 失败或异常都返回0，其他返回实际的long
	 */
	public static long getTimeMillis(String date) {
		try {
			// 先截取成标准的格式
			date = StringUtils.substring(date, 0, DATE_PATTERNS.length());
			Date parseDate = DateUtils.parseDate(date, new String[] { DATE_PATTERNS });
			return com.ushine.common.utils.DateUtils.getMillis(parseDate);
		} catch (Exception e) {
			//logger.error("日期" + (date) + "转成long型失败");
			return 0;
		}
	}

	public static long getTimeMillis(String date, String time) {
		try {
			// 先截取成标准的格式
			date = StringUtils.substring(date + time, 0, DATE_PATTERNS.length());
			Date parseDate = DateUtils.parseDate(date, new String[] { DATE_PATTERNS });
			return com.ushine.common.utils.DateUtils.getMillis(parseDate);
		} catch (Exception e) {
			//logger.error("日期" + (date + time) + "转成long型失败");
			return 0;
		}
	}

	/**
	 * 获得开始时间的long
	 * 
	 * @param startDate
	 *            开始日期
	 * @return
	 */
	public static long getStartTimeMillis(String startDate) {
		// 先截取成标准的格式
		startDate = StringUtils.substring(startDate, 0, YMD_PATTERNS.length());
		//null值返回0
		return getTimeMillis(startDate, START_TIME);
	}

	/**
	 * 获得结束时间的long
	 * 
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static long getEndTimeMillis(String endDate) {
		String defalutValue=DateFormatUtils.format(new Date(), YMD_PATTERNS);
		//为null返回当天
		endDate=(String) ObjectUtils.defaultIfNull(endDate, defalutValue);
		// 先截取成标准的格式
		endDate = StringUtils.substring(endDate, 0, YMD_PATTERNS.length());
		return getTimeMillis(endDate, END_TIME);
	}

	public static String getDate(long timeMillis) {
		String date = new String();
		try {
			date = DateFormatUtils.format(timeMillis, DATE_PATTERNS);
		} catch (Exception e) {
			// 异常统一处理
			logger.error("日期" + timeMillis + "转成" + DATE_PATTERNS + "型失败");
		}
		return date;
	}

	/**
	 * 比较输入的日期时间字符串和开始、结束的日期时间比较
	 * 
	 * @param startDateStr
	 * @param endDateStr
	 * @param compareDateStr
	 * @return
	 * @throws ParseException
	 */
	public static boolean compareDate(String startDateStr, String endDateStr, String compareDateStr)
			throws ParseException {

		Date parseDate = org.apache.commons.lang.time.DateUtils.parseDate(compareDateStr,
				new String[] { DATE_PATTERNS });
		long dateLong = com.ushine.common.utils.DateUtils.getMillis(parseDate);
		startDateStr += " 00:00:00";
		endDateStr += " 23:59:59";
		Date startDate = org.apache.commons.lang.time.DateUtils.parseDate(startDateStr, new String[] { DATE_PATTERNS });
		// 转成long
		long startDateLong = com.ushine.common.utils.DateUtils.getMillis(startDate);
		Date endDate = org.apache.commons.lang.time.DateUtils.parseDate(endDateStr, new String[] { DATE_PATTERNS });
		// 转成long
		long endDateLong = com.ushine.common.utils.DateUtils.getMillis(endDate);
		if (dateLong >= startDateLong && dateLong < endDateLong) {
			return true;
		}
		return false;
	}
	
}
