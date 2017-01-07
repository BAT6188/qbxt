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
import com.ushine.storeInfo.model.NetworkAccountStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.INetworkAccountStoreService;
import com.ushine.storeInfo.service.IPersonStoreService;
/**
 * 网络账号接口实现测试类
 * @author wangbailin
 *
 */
@Component("NetworkAccountStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class NetworkAccountStoreServiceImplTest {
	@Autowired
	private INetworkAccountStoreService networkAccountStoreService;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IPersonStoreService personStoreService;
	
	
	//@Test
	public void saveNetworkAccountTest(){
		InfoType infoType = null;
		PersonStore personStore = null;
		try {
			 infoType =infoTypeService.findInfoTypeById("ff80808154752484015475248b250001");
			 personStore = personStoreService.findPersonStoreById("ff808081547589e301547589eb0d0001");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NetworkAccountStore store = new NetworkAccountStore();
		store.setNetworkNumber("987654321");
		store.setInfoType(infoType);
		store.setPersonStore(personStore);
		try {
			networkAccountStoreService.saveNetworkAccount(store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void findNetworkAccountByidTest(){
		try {
			NetworkAccountStore store = networkAccountStoreService.findNetworkAccountByid("ff8080815475a53e015475a544a80001");
			System.out.println(store.getNetworkNumber());
			System.out.println(store.getInfoType().toString());
			System.out.println(store.getPersonStore().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
