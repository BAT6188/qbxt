package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IVocationalWorkStoreService;
import com.ushine.util.StringUtil;

//@Component("IVocationalWorkStoreService")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class IVocationalWorkStoreServiceTest {
	@Autowired
	private IVocationalWorkStoreService iVocationalWorkStoreService;
	@Autowired
	private IInfoTypeService iInfoTypeService;
	//@Test
	public void saveVocationalWorkTest(){
		VocationalWorkStore store = new VocationalWorkStore();
		store.setDocName("时代杂志");
		store.setDocNumber("12345678990");
		

		store.setTheOriginal("sdfsdfasfasdfsdgsdgdgdg");
		store.setTime(StringUtil.dates());
		InfoType infoType = null;
		try {
			infoType = iInfoTypeService.findInfoTypeById("40288aac5474828b0154748290f30001");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		store.setInfoType(infoType);
		try {
			iVocationalWorkStoreService.saveVocationalWork(store);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@Test
	public void findVocationalWorkByIdTest(){
		try {
			VocationalWorkStore store = iVocationalWorkStoreService.findVocationalWorkById("40288aac54748d900154748d95b40001");

		    System.out.println(store.getDocName());
		    System.out.println(store.getInfoType().toString());

		    //System.out.println(store.getJournalName());
		    //System.out.println(store.getInfoType().toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void findVocationalStoreByCondations() throws Exception{
		//多条件查询测试
		String string=iVocationalWorkStoreService.findVocationalWorkStore("infoType", null, 
				"2001-01-01", "2016-05-10", 0, 100, null, null, null,null,null);
		System.out.println("===============findVocationalStoreByCondations================="+string);
	}
	
	@Test
	public void del() throws Exception{
		boolean result=iVocationalWorkStoreService.delVocationalWorkStoreByIds(new String[]{"ff80818154ade5610154ae11a6ed0004",
				"ff80818154ae2d9c0154ae2f2ba80001"});
		System.out.println(result);
	}
}
