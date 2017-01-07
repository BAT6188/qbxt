package com.ushine.core.init;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.common.init.ISystemInitService;
import com.ushine.core.cache.CityCache;
import com.ushine.core.cache.OperCache;
import com.ushine.core.cache.ResCache;
import com.ushine.core.po.Operation;
import com.ushine.core.po.Resource;
import com.ushine.core.service.ICityService;
import com.ushine.core.service.IOperationService;
import com.ushine.core.service.IResourceService;

public class InitCoreServiceImpl implements ISystemInitService {
	private static final Logger logger = LoggerFactory.getLogger(InitCoreServiceImpl.class);
	@Autowired
	private IOperationService operService;
	@Autowired
	private IResourceService resService;
	@Autowired
	private ICityService cityService;

	public String name() {
		return "基础模块初始化服务实现";
	}

	public ISystemInitService load() throws Exception {
		return this;
	}

	public void init() throws Exception {
		try {
			//****************************************
			//* 添加操作到缓存
			//****************************************
			OperCache operMgr = OperCache.getInstance();
			List<Operation> opers = operService.findOperations();
			for(Operation oper : opers) {
				operMgr.pup(oper);
			}
			logger.info("{缓存加载}成功加载系统操作信息.");
			
			//****************************************
			//* 添加资源到缓存
			//****************************************
			ResCache resCache = ResCache.getInstance();
			List<Resource> ress = resService.findResources();
			for(Resource res : ress) {
				resCache.pup(res);
			}
			logger.info("{缓存加载}成功加载系统资源信息.");
			
			//****************************************
			//* 添加城市到缓存
			//****************************************
			CityCache c=CityCache.getInstance();
			c.setCitys(cityService.findCity());
			logger.info("{缓存加载}成功加载城市树形菜单.");
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("基础模块初始化服务失败，强制关闭应用程序.", e);
			System.exit(-1);
		}
	}

	public void destroy() {
		
	}

}
