package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.model.OrganizPublicAction;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.IOrganizPublicActionService;
import com.ushine.storeInfo.service.IOrganizStoreService;
import com.ushine.storeInfo.service.IWebsiteJournalStoreService;
/**
 * 组织下属刊物接口实现测试类
 * @author wangbailin
 *
 */
@Component("OrganizPublicActionServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class OrganizPublicActionServiceImplTest {
	@Autowired
	private IOrganizStoreService organizStoreService;
	@Autowired
	private IOrganizPublicActionService organizPublicActionService;
	@Autowired
	private IWebsiteJournalStoreService websiteJournalStoreService;
	
	//@Test
	public void saveOrgPublicActionTest(){
		try {
			OrganizStore organizStore = organizStoreService.findOrganizStoreById("40288aac5479eee2015479eeeffc0001");
			WebsiteJournalStore websiteJournalStore = websiteJournalStoreService.findWebsiteJouById("40288aac547a43a501547a43ac550001");
			OrganizPublicAction action = new OrganizPublicAction();
			action.setOrganizStore(organizStore);
			action.setWebsiteJournalStore(websiteJournalStore);
			organizPublicActionService.saveOrgPublicAction(action);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void findOrgPublicActionByIdTest(){
		try {
			OrganizPublicAction action = organizPublicActionService.findOrgPublicActionById("40288aac547a468601547a468de00001");
			System.out.println(action.getOrganizStore().toString());
			System.out.println(action.getWebsiteJournalStore().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
