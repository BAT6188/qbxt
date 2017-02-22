package com.ushine.solr.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.ushine.solr.solrbean.OutsideDocStoreSolr;
import com.ushine.solr.vo.OutsideDocStoreVo;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.OutsideDocStore;

public class OutsideDocStoreSolrServiceImplTest2 {
	/**
	 * 测试bean的转换
	 */
	OutsideDocStoreSolrServiceImpl impl;

	@Before
	public void setUp() {
		impl = new OutsideDocStoreSolrServiceImpl();
		assertNotNull(impl);
	}

	@Test
	public void testConvertTo() {
		// 通过
		InfoType infoType = new InfoType();
		infoType.setTypeName("一局");
		OutsideDocStore store = new OutsideDocStore();
		store.setInfoType(infoType);
		store.setId("100");
		store.setAction("0");
		store.setCentent(null);
		store.setDocNumber("200");
		store.setCreateDate("2017-01-11 00:30:00");
		// 转-->>OutsideDocStoreSolr
		OutsideDocStoreSolr solrBean = impl.convertToSolrBean(store);
		assertEquals("一局", solrBean.getInfoType());
		assertEquals("", solrBean.getCentent());
		// 1484065800000 System.out.println(solrBean.getCreateDate());
		// 转-->>vo
		OutsideDocStoreVo vo = impl.convertToOutsideDocStoreVo(solrBean);
		assertEquals("2017-01-11 00:30:00", vo.getCreateDate());
		assertEquals("一局", vo.getInfoType());
	}

	@Test
	public void testInfoTypeNull() {
		OutsideDocStore store = new OutsideDocStore();
		store.setInfoType(null);
		OutsideDocStoreSolr solrBean = impl.convertToSolrBean(store);
		assertEquals("", solrBean.getInfoType());
	}
}
