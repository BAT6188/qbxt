package com.ushine.storesinfo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.service.IClueRelationshipService;

public class ClueRelationShipServiceImplTest extends AbstractSpringJUnitTest<ClueStore> {

	@Autowired IClueRelationshipService service;
	@Test
	public void testGetStoreId() throws Exception {
		String result=service.findStoreIdByClueId("402884825a55dc51015a55e19ce9000d", IClueRelationshipService.PERSONSTORE_TYPE);
		assertTrue(result.length()>2);
		logger.info("查询结果：\r\n"+result);
	}
}
