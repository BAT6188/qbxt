package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.common.utils.SpringUtils;
import com.ushine.store.service.IPersonStoreIndexService;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IPersonStoreService;

import oracle.net.aso.n;

/**
 * 人员库接口实现测试类
 * @author wangbailin
 *
 */
@Component("PersonStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class PersonStoreServiceImplTest {
	@Autowired
	private IPersonStoreIndexService personStoreIndexService;
	@Test
	public void addPersonStoreIndex() throws Exception {
		personStoreIndexService.saveIndex(new PersonStore());
	}
}
