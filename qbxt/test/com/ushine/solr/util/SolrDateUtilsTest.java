package com.ushine.solr.util;
import static org.junit.Assert.*;

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class SolrDateUtilsTest {
	/**
	 * 测试比较日期时间是否在给定的区间范围内
	 * @throws ParseException
	 */
	@Test
	public void testCompareDate() throws ParseException{
		//assertEquals(false, DateUtils.compareDate("2017-02-15", "2017-02-16", "2017-02-16 12:30:30"));
		//assertEquals(false, DateUtils.compareDate("2017-02-16", "2017-02-16", "2017-02-16 12:30:30"));
		//加上时间段
		//startDateStr+=" 00:00:00";
		//endDateStr+=" 23:59:59";
		assertEquals(true, SolrDateUtils.compareDate("2017-02-15", "2017-02-16", "2017-02-16 12:30:30"));
	}
	
	@Test
	public void testGetDate(){
		//System.err.println(SolrDateUtils.getDate(111111111));
		String date="2017-01-25 23:37:10.0";
		String date2="2017-01-25 23:37:10";
		String pattern="yyyy-MM-dd HH:mm:ss";
		assertEquals(date2, StringUtils.substring(date, 0, pattern.length()));
		//assertEquals(SolrDateUtils.getTimeMillis(date), SolrDateUtils.getTimeMillis(date2));
	}
}
