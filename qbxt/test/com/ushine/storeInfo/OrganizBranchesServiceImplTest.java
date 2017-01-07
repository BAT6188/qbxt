package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.model.OrganizBranches;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.service.IOrganizBranchesService;
import com.ushine.storeInfo.service.IOrganizStoreService;
/**
 * 组织分支机构接口实现测试类
 * @author wangbailin
 *
 */
@Component("OrganizBranchesServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class OrganizBranchesServiceImplTest {
		
	@Autowired
	private IOrganizStoreService organizStoreService;
	@Autowired
	private IOrganizBranchesService organizBranchesService;
	
	//@Test
	public void saveOrganizBranchesTest(){
		try {
			OrganizStore organizStore = organizStoreService.findOrganizStoreById("40288aac5479eee2015479eeeffc0001");
			OrganizStore organizStore1 = organizStoreService.findOrganizStoreById("40288aac547a2a2501547a2a2c330001");
			OrganizStore organizStore2 = organizStoreService.findOrganizStoreById("40288aac547a2c2601547a2c2d760001");
			
			OrganizBranches branches = new OrganizBranches();
			branches.setOrganizStore(organizStore);
			branches.setOrganizBranches(organizStore2);
			organizBranchesService.saveOrganizBranches(branches);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@Test
	public void findOrgBranchesByIdTest(){
		try {
			OrganizBranches branches = organizBranchesService.findOrgBranchesById("40288aac547a2e7501547a2e7c810001");
			System.out.println(branches.getOrganizStore().toString());
			System.out.println(branches.getOrganizBranches().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
