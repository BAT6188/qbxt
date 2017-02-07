package com.ushine.storeInfo;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.common.utils.SpringUtils;
import com.ushine.dao.IBaseDao;
import com.ushine.store.service.IPersonStoreIndexService;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.service.IPersonStoreService;
import static org.junit.Assert.*;
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
	
	@Test
	public void testFindAll() throws Exception{
		IBaseDao<PersonStore,Serializable> baseDao=(IBaseDao<PersonStore, Serializable>)SpringUtils.getBean("baseDao");
		System.out.println(baseDao.findAll(PersonStore.class).size());
	}
	
	@Test
	public void testFindByPersonName() throws Exception{
		IPersonStoreService service=(IPersonStoreService) SpringUtils.getBean("personStoreServiceImpl");
		assertEquals(true, service.findPersonStoreByPersonName("dongdidi"));
	}
	
}
