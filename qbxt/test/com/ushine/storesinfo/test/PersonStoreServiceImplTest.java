package com.ushine.storesinfo.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storesinfo.service.IPersonStoreService;

@Component("PersonStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class PersonStoreServiceImplTest {
	
	private Logger logger=Logger.getLogger(PersonStoreServiceImplTest.class);
	@Autowired IPersonStoreService service;
	
	@Test
	public void testGet() throws Exception{
		logger.warn("详细信息：\r\n"+service.findPersonStore(null, null, "1999-02-19", "2017-02-19", 1, 2, null, null, null, null, null));
		logger.warn("详细信息：\r\n"+service.findPersonStore("infoType", "反颠覆", "2001-02-19", "2017-02-19", 1, 2, null, null, null, null, null));
	}
}
