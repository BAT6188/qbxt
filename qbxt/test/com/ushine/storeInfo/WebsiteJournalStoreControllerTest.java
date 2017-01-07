package com.ushine.storeInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.ushine.core.verify.session.UserSessionMgr;
//import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
//import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import com.ushine.storeInfo.controller.InfoTypeController;
import com.ushine.storeInfo.controller.VocationalWorkStoreController;
import com.ushine.storeInfo.controller.WebsiteJournalStoreController;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.impl.InfoTypeServiceImpl;

/**
 * 对controller进行测试
 * @author dh
 *
 */
@Transactional  
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)  
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath*:/applicationContext.xml","classpath*:/controller-servlet.xml"})  
public class WebsiteJournalStoreControllerTest {
	  private MockHttpServletRequest request;  
	  private MockHttpServletResponse response;  
	  @Autowired
	  private IInfoTypeService infoTypeService;
	  InfoTypeController infoTypeController=new InfoTypeController();
	  WebsiteJournalStoreController controller=new WebsiteJournalStoreController();
	  
	  @Test
	  public void testUsers() throws Exception{
		  request=new MockHttpServletRequest();
		  response=new MockHttpServletResponse();
		 String result=controller.getWebsiteJournalStoreType();
		  System.out.println("================controller.getWebsiteJournalStoreType()=============="+result);
	  }
	  
	  //@Test
	  public void testMockMvc() throws Exception{
		  //需要对待测试的controller进行实例化
		  //否则会出现No mapping found for HTTP request with URI 的这个错误
		  //版本不兼容
		  //MockMvc mockMvc=MockMvcBuilders.standaloneSetup(vocationalWorkStoreController).build();
		  //mockMvc.perform(get("/getvocationalworkstoretype.do", null)).andDo(print());
		 //ResultActions resultActions= mockMvc.perform(get("/user/users", new Object[]{new RedirectAttributesModelMap()})).andDo(print());
		 //String result=resultActions.andReturn().getResponse().getContentAsString();
		 //System.out.println("================================"+result);
		 /*mockMvc.perform((post("/loginTest").param("userName", "admin").param("password", "1"))).andExpect(status().isOk())  
          .andDo(print());  */
	  }
}
