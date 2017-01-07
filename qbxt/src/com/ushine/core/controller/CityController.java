package com.ushine.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ushine.common.vo.ViewObject;
import com.ushine.core.cache.CityCache;
import com.ushine.core.service.ICityService;
/**
 * 
 * @author xyt
 *
 */
@Controller
public class CityController {
	private static final Logger logger = LoggerFactory.getLogger(CityController.class);
	@Autowired
	private ICityService cityService;
	/**
	 * 查询所有城市树形菜单
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/findCitys", method=RequestMethod.GET)
	@ResponseBody
	public String addCompany(
			HttpServletRequest request) {
		logger.debug("查询所有城市");
		try {
			CityCache c=CityCache.getInstance();
			//System.out.println(c.getCitys());
			return c.getCitys();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String msg = "查询所有城市树形菜单失败";
			logger.error(msg + e);
			return new ViewObject(-1, msg).toJSon();
		}
	}
	public ICityService getCityService() {
		return cityService;
	}
	public void setCityService(ICityService cityService) {
		this.cityService = cityService;
	}

}
