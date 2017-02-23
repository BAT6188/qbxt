package com.ushine.solr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.solr.service.IClueStoreSolrService;
import com.ushine.solr.solrbean.ClueStoreSolr;
import com.ushine.solr.vo.ClueStoreVo;
import com.ushine.storesinfo.model.ClueStore;

public class ClueStoreSolrServiceImplTest extends AbstractSpringJUnitTest<ClueStore> {

	@Autowired IClueStoreSolrService solrService;
	
	@Test
	public void testServer(){
		ClueStoreSolrServiceImpl impl=new ClueStoreSolrServiceImpl();
		assertNotNull(impl.server);
	}
	
	@Test
	public void testConvert(){
		String id="402884825a55dc51015a55e19ce9000d";
		ClueStore store=new ClueStore();
		store.setId(id);
		store.setCreateDate("2017-02-23 00:30:10");
		store.setClueName("枪支弹药");
		List<ClueStore> list=new ArrayList<>();
		list.add(store);
		List<ClueStoreSolr> solrList = solrService.convertToSolrBeanList(list);
		//取得
		assertEquals("枪支弹药", solrList.get(0).getClueName());
		assertEquals(id, solrList.get(0).getCluestoreId());
		//取得人员id
		System.out.println(solrList.get(0).getPersonId());
		//时间
		System.out.println(solrList.get(0).getCreateDate());
		
		//再转到vo层
		List<ClueStoreVo> voList = solrService.convertToClueStoreVoList(solrList);
		assertEquals("2017-02-23 00:30:10", voList.get(0).getCreateDate());
		logger.info("详细信息：\r\n"+voList.get(0).toString());
	}
}
