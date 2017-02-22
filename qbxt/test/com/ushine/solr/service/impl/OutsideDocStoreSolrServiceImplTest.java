package com.ushine.solr.service.impl;

import org.junit.Ignore;
import org.junit.Test;

import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.storesinfo.model.OutsideDocStore;

public class OutsideDocStoreSolrServiceImplTest extends AbstractSpringJUnitTest<OutsideDocStore> {

	@Test
	@Ignore
	public void testServerNotNull(){
		OutsideDocStoreSolrServiceImpl impl=new OutsideDocStoreSolrServiceImpl();
		//通过
		assertNotNull(impl.server);
	}
}
