package com.ushine.core;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.core.controller.LoginController;

@SuppressWarnings("rawtypes")
public class LoginControllerTest extends AbstractSpringJUnitTest {
	Logger logger = Logger.getLogger(LoginControllerTest.class);
	LoginController loginController=null;
	@Test
	public void testNotNull(){
		//LoginController loginController=(LoginController) SpringUtils.getBean("loginController");
		//assertNotNull(loginController);
		
	}

	@Test
	public void testIsLogin() throws UnsupportedEncodingException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setCharacterEncoding("UTF-8");
		loginController=new LoginController();
		
		loginController.isLogion("donghao", "123456", request, null, response);
		logger.error("response返回信息：\r\n" + response.getContentAsString());
	}
}
