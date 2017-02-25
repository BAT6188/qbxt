package com.ushine.utils;

import org.junit.Test;

import com.ushine.abstracttest.AbstractAssert;
import com.ushine.solr.util.SolrIndexUtils;

public class SolrIndexUtilsTest extends AbstractAssert {

	@Test
	public void testCreateIndex(){
		assertEquals(true, SolrIndexUtils.createNewIndex("createPersonStoreIndex"));
	}
}
