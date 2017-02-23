package com.ushine.storesinfo;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.service.IPersonStoreService;


public class PersonStoreServiceImplTest extends AbstractSpringJUnitTest<PersonStore> {
	
	private Logger logger=Logger.getLogger(PersonStoreServiceImplTest.class);
	@Autowired IPersonStoreService service;
	
	@Test
	public void testGet() throws Exception{
		logger.warn("详细信息：\r\n"+service.findPersonStore(null, null, "1999-02-19", "2017-02-19", 1, 2, null, null, null, null, null));
		logger.warn("详细信息：\r\n"+service.findPersonStore("infoType", "反颠覆", "2001-02-19", "2017-02-19", 1, 2, null, null, null, null, null));
	}
}
