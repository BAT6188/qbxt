package com.ushine.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.ushine.util.IdentifyDocUtils;

public class DateTest {
	@Test
	public void test1()throws Exception{
		Long value=1479952301894L;
		Date date=new Date(value);
		//Date date=new Date();
		Date date2=new Date("Thu Nov 24 2016 10:49:20 GMT+0800 (中国标准时间)");
		//System.err.println(date2);
		//System.err.println(date);
		//DateFormatUtils.format(new , pattern)
		//String string="Thu Nov 24 2016 10:49:20";
		//System.err.println(DateUtils.parseDate(string,new String[]{ "yyyy-MM-dd"}));
		System.err.println(DateFormatUtils.format(date, "yyyyMMdd"));
	}
	
	@Test
	public void test2()throws Exception{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.err.println(format.parse("2016-11-24 12:30:00").getTime());
		System.err.println(format.parse("2016-11-24 12:30:20").getTime());
		/*System.err.println(format.parse("2016-11-24 12:00:00"));
		System.err.println(format.parse("2016-11-24 13:00:00"));*/
		/*Calendar calendar1=Calendar.getInstance().getInstance(new Locale("2016-11-24 12:00:00"));
		Calendar calendar2=Calendar.getInstance().getInstance(new Locale("2016-11-24 13:00:00"));
		long l1=calendar1.getTimeInMillis();
		long l2=calendar2.getTimeInMillis();
		System.err.println(l1);
		System.err.println(l2);*/
	}
}
