package com.ushine.storeInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.model.ClueRelationship;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.service.IClueRelationshipService;
import com.ushine.storeInfo.service.IInfoTypeService;
/**
 * 线索基础库关系接口实现测试类
 * @author wangbailin
 *
 */
@Component("ClueRelationshipServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class ClueRelationshipServiceImplTest {
	
		@Autowired
		private IClueRelationshipService clueRelationshipService;
		@Autowired
		private IInfoTypeService infoTypeService;
		
		
		//@Test
		public void savaClueRelationshipTest(){
			try {
				InfoType infoType = infoTypeService.findInfoTypeById("ff80808154752484015475248b250001");
				ClueRelationship clueRelationship=new ClueRelationship();
				clueRelationship.setClueId("40288aac547929bc01547929c4db0001");
				clueRelationship.setLibraryId("ff808081547589e301547589eb0d0001");
				clueRelationshipService.savaClueRelationship(clueRelationship);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		@Test
		public void findClueRelationshipByIdTest(){
			try {
				ClueRelationship clueRelationship = clueRelationshipService.findClueRelationshipById("40288aac547936e301547936e95b0001");
				System.out.println(clueRelationship.getClueId());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
}
