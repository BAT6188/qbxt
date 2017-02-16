package com.ushine.core.init;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ushine.common.config.Configured;
import com.ushine.common.listener.InitializedService;
import com.ushine.common.utils.SpringUtils;
import com.ushine.core.cache.OperCache;
import com.ushine.core.cache.ResCache;
import com.ushine.core.po.Operation;
import com.ushine.core.po.Resource;
import com.ushine.core.service.ICityService;
import com.ushine.core.service.IOperationService;
import com.ushine.core.service.IResourceService;
import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.PersonStoreNRTSearch;
import com.ushine.storesinfo.model.PersonStore;

/**
 * 基础模块初始化服务实现:
 * 1. 应用程序启动时预先加载系统定义的所有操作到缓存
 * 
 * @author franklin
 *
 */
public class InitCoreService implements InitializedService {
	private static final Logger logger = LoggerFactory.getLogger(InitCoreService.class);
	public static String cityTree="";
	private IOperationService operService;
	private IResourceService resService;
	private ICityService cityService;
	private IBaseDao baseDao;
	
	public InitCoreService() {
		operService = (IOperationService) SpringUtils.getBean("operationServiceImpl");
		resService = (IResourceService) SpringUtils.getBean("resourceServiceImpl");
		cityService = (ICityService) SpringUtils.getBean("cityServiceImpl");
		baseDao=(IBaseDao) SpringUtils.getBean("baseDao");
	}
	
	public void initialized(ServletContextEvent sce) {
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
			cityTree=cityService.findCity();
			logger.info("{缓存加载}成功加载城市树形菜单.");
			//**************
			//创建索引库的文件夹从配置文件中读取
			/*String webRoot=System.getProperty("qbtest.root");
			String dirBaseIndex=webRoot+File.separator+Configured.getInstance().get("baseIndex");
			//不存在创建
			File file=new File(dirBaseIndex);
			if(!file.exists()){
				file.mkdir();
			}
			logger.info("创建索引库根文件夹成功"+dirBaseIndex);*/
			//***********
			//为每个库创建索引文件夹及索引文件
			/*String personStoreIndex=dirBaseIndex+file.separator+Configured.getInstance().get("personStoreIndex");
			File personIndexFile=new File(personStoreIndex);
			if(!file.exists()){
				personIndexFile.mkdir();
			}*/
			
		} catch(Exception e) {
			logger.error("基础模块初始化服务失败，强制关闭应用程序.", e);
			System.exit(-1);
		}
	}

	public static String getCityTree() {
		return cityTree;
	}

	public static void setCityTree(String cityTree) {
		InitCoreService.cityTree = cityTree;
	}
	
}
