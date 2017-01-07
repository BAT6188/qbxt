package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.OrganizPerson;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IOrganizPersonService;
import com.ushine.storeInfo.service.IOrganizStoreService;
import com.ushine.storeInfo.service.IPersonStoreService;

/**
 * 组织下属成员接口实现测试类
 * @author wangbailin
 *
 */
@Component("OrganizPersonServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class OrganizPersonServiceImplTest {
	@Autowired
	private IOrganizStoreService organizStoreService;
	@Autowired
	private IOrganizPersonService organizPersonService;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IPersonStoreService personStoreService;
	
	
	//@Test
	public void saveOrganizPersonTest(){
		try {
			OrganizStore organizStore = organizStoreService.findOrganizStoreById("40288aac5479eee2015479eeeffc0001");
			PersonStore personStore = personStoreService.findPersonStoreById("ff808081547589e301547589eb0d0001");
			OrganizPerson organizPerson = new OrganizPerson();
			organizPerson.setOrganizStore(organizStore);
			organizPerson.setPersonStore(personStore);
			organizPersonService.saveOrganizPerson(organizPerson);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Test
	public void findOrgPersonByIdTest(){
		try {
			OrganizPerson organizPerson = organizPersonService.findOrgPersonById("40288aac547a1e4301547a1e49dd0001");
			System.out.println(organizPerson.getOrganizStore().toString());
			System.out.println(organizPerson.getPersonStore().toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
