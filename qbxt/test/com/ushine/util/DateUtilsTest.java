package com.ushine.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;

public class DateUtilsTest {
	@Test
	public void test1()throws Exception{
		//1295341092000
		String result=DateFormatUtils.format(Long.parseLong("1295341092000"), "yyyyMMdd");
		System.err.println(result);
	}
}
