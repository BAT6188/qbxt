package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.controller.OutsideDocStoreController;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IOutsideDocStoreService;
import com.ushine.storeInfo.storeFinal.StoreFinal;

/**
 * 测试OutsideDocStoreController
 * @author dh
 *
 */
@Component("OutsideDocStoreControlerTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml","classpath*:/controller-servlet.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class OutsideDocStoreServiceControllerTest {
	@Autowired
	private IOutsideDocStoreService outsideDocStoreService;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Test
	public void gettype(){
		OutsideDocStoreController controller=new OutsideDocStoreController();
		String result=controller.getOutsideDocStoreType();
		
	}
}
