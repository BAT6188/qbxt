package com.ushine.solr.service.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.solr.service.IOutsideDocStoreSolrService;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.storesinfo.model.OutsideDocStore;

public class OutsideDocStoreSolrServiceImplTest extends AbstractSpringJUnitTest<OutsideDocStore> {
	@Autowired IOutsideDocStoreSolrService solrService;
	@Test
	@Ignore
	public void testServerNotNull(){
		OutsideDocStoreSolrServiceImpl impl=new OutsideDocStoreSolrServiceImpl();
		//通过
		assertNotNull(impl.server);
	}
	
	@Test
	public void testCreateNew(){
		solrService.createNewIndexByStores();
		QueryBean queryBean=new QueryBean(null, null, null, null, null, null, null, null, null, "1970-01-01", "2017-02-22");
		assertEquals(365, solrService.getDocumentsCount(queryBean));
	}
}
