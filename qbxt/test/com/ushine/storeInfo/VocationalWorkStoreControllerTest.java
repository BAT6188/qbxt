package com.ushine.storeInfo;

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

import com.ushine.core.verify.session.UserSessionMgr;
//import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
//import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import com.ushine.storeInfo.controller.InfoTypeController;
import com.ushine.storeInfo.controller.VocationalWorkStoreController;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.storeInfo.service.IVocationalWorkStoreService;

/**
 * 对controller进行测试
 * 
 * @author dh
 * 
 */
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/applicationContext.xml", "classpath*:/controller-servlet.xml" })
public class VocationalWorkStoreControllerTest {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	@Autowired
	private IInfoTypeService infoTypeService;

	@Autowired
	private IPersonStoreService personStoreService;

	@Autowired
	private IVocationalWorkStoreService vocationalWorkStoreService;
	InfoTypeController infoTypeController = new InfoTypeController();
	VocationalWorkStoreController vocationalWorkStoreController = new VocationalWorkStoreController();

	// @Test
	public void testUsers() throws Exception {

		request = new MockHttpServletRequest();
		request.setAttribute(UserSessionMgr.PERMIT_OPER, "1x0001");
		response = new MockHttpServletResponse();
		// String
		// valueString=infoTypeController.findInfoTypeByTypeNameVoToJSon(infoTypeService.findInfoTypeAll());
		// String
		// vwsController=vocationalWorkStoreController.getVocationalWorkStoreType();
		// System.out.println("=============================="+valueString);
		/*
		 * String result=vocationalWorkStoreController.
		 * findVocationalWorkStoreByConditions("docNumber", "100", "1901-01-10",
		 * "2016-01-01", 0, 100, request);
		 */
		String result = vocationalWorkStoreController.delVocationalWorkStore(
				new String[] { "ff80818154ade5610154ae0e28c80003", "ff80818154ae2d9c0154ae2f2ba80001" }, request);
		System.out.println("===============DEL VocationalWorkStoreByConditions===============" + result);
	}

	// @Test
	public void testMockMvc() throws Exception {
		// 需要对待测试的controller进行实例化
		// 否则会出现No mapping found for HTTP request with URI 的这个错误
		// 版本不兼容
		// MockMvc
		// mockMvc=MockMvcBuilders.standaloneSetup(vocationalWorkStoreController).build();
		// mockMvc.perform(get("/getvocationalworkstoretype.do",
		// null)).andDo(print());
		// ResultActions resultActions= mockMvc.perform(get("/user/users", new
		// Object[]{new
		// RedirectAttributesModelMap()})).andDo(print());
		// String
		// result=resultActions.andReturn().getResponse().getContentAsString();
		// System.out.println("================================"+result);
		/*
		 * mockMvc.perform((post("/loginTest").param("userName",
		 * "admin").param("password", "1"))).andExpect(status().isOk())
		 * .andDo(print());
		 */
		// request=new MockHttpServletRequest();
		// System.out.println("=====request.getContextPath()====="+request.getSession().getServletContext().getRealPath("jquery"));
	}
}
