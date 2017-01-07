package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.model.CertificatesStore;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.service.ICertificatesStoreService;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IPersonStoreService;
/**
 * 证件接口实现测试类
 * @author wangbailin
 *
 */
@Component("CertificatesStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class CertificatesStoreServiceImplTest {
	@Autowired
	private ICertificatesStoreService certificatesStoreService;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IPersonStoreService personStoreService;
	//@Test
	public void saveCertificatesTest(){
		InfoType infoType = null;
		PersonStore personStore = null;
		try {
			infoType = infoTypeService.findInfoTypeById("ff80808154752484015475248b250001");
			personStore = personStoreService.findPersonStoreById("ff808081547589e301547589eb0d0001");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CertificatesStore store = new CertificatesStore();
		store.setCertificatesNumber("123456");
		store.setInfoType(infoType);
		store.setPersonStore(personStore);
		try {
			certificatesStoreService.saveCertificates(store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void findCertificatesByIdTest(){
		try {
			CertificatesStore certificatesStore = certificatesStoreService.findCertificatesById("ff808081547597bf01547597c5a20001");
			System.out.println(certificatesStore.getCertificatesNumber());
			System.out.println(certificatesStore.getInfoType().toString());
			System.out.println(certificatesStore.getPersonStore().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
