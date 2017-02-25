package com.ushine.utils;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.junit.Test;

import com.ushine.abstracttest.AbstractAssert;
import com.ushine.solr.util.MyStringUtils;

public class MyStringUtilsTest extends AbstractAssert {

	@Test
	public void testSplit(){
		String fieldValue="董昊&&第三";
		Map map=MyStringUtils.getSplitMap(fieldValue);
		assertEquals(2, map.size());
		logger.info(MapUtils.getString(map, MyStringUtils.QUERYVALUE));
		logger.info(MapUtils.getString(map, MyStringUtils.AGAINQUERYVALUE));
		//
		String value2="董昊";
		Map map2=MyStringUtils.getSplitMap(value2);
		assertEquals(2, map2.size());
		logger.info(MapUtils.getString(map2, MyStringUtils.QUERYVALUE));
		logger.info(MapUtils.getString(map2, MyStringUtils.AGAINQUERYVALUE));
	}
}
