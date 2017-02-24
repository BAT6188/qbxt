package com.ushine.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import com.ushine.abstracttest.AbstractAssert;

public class CollectionUtilsTest extends AbstractAssert {
	
	@Test
	public void testSubtract(){
		String []ids={"1","2","3"};
		List<String> list1=new ArrayList<>();
		List<String> list2=new ArrayList<>();
		CollectionUtils.addAll(list1, ids);
		CollectionUtils.addAll(list2, ids);
		//substract
		List<String> subtract = (List<String>) CollectionUtils.subtract(list1, list2);
		assertEquals(0, subtract.size());
	}
}
