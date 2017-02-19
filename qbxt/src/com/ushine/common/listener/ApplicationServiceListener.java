package com.ushine.common.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.common.utils.PathUtils;
import com.ushine.common.utils.PropertiesUtils;
import com.ushine.common.utils.SpringUtils;
import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.ClueStoreNRTSearch;
import com.ushine.luceneindex.index.LeadSpeakStoreNRTSearch;
import com.ushine.luceneindex.index.OrganizStoreNRTSearch;
import com.ushine.luceneindex.index.OutsideDocStoreNRTSearch;
import com.ushine.luceneindex.index.PersonStoreNRTSearch;
import com.ushine.luceneindex.index.StoreIndexPath;
import com.ushine.luceneindex.index.VocationalWorkStoreNRTSearch;
import com.ushine.luceneindex.index.WebsiteJournalStoreNRTSearch;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IPersonStoreSolrService;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.model.OrganizStore;
import com.ushine.storesinfo.model.OutsideDocStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.VocationalWorkStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;

/**
 * 应用服务监听器, 在程序启动与停止时，执行初始化和注销方法。
 * 
 * @author Franklin
 *
 */
public class ApplicationServiceListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceListener.class);

	/*
	 * 配置文件路径/WEB-INF/config
	 */
	private static final String CONFIG_PATH = PathUtils.getConfigPath(ApplicationServiceListener.class);

	/*
	 * 初始化文件
	 */
	private static final String INIT_FILE = "init.properties";

	/*
	 * 注销文件
	 */
	private static final String DESTROY_FILE = "destroy.properties";

	/*
	 * 需要执行初始化方法的类
	 */
	private static List<String> initClass = null;

	/*
	 * 需要执行注销方法的类
	 */
	private static List<String> destroyClass = null;

	/**
	 * 读出配置文件init.properties, destroy.properties
	 */
	static {
		try {
			logger.debug("加载初始化程序:" + CONFIG_PATH + INIT_FILE);
			PropertiesUtils propInit = new PropertiesUtils(CONFIG_PATH + INIT_FILE);
			initClass = propInit.getValues();

			logger.debug("加载注销程序:" + CONFIG_PATH + DESTROY_FILE);
			PropertiesUtils propDestroy = new PropertiesUtils(CONFIG_PATH + DESTROY_FILE);
			destroyClass = propDestroy.getValues();
		} catch (Exception e) {
			logger.error("加载系统配置文件失败.", e);
			System.exit(-1);
		}
	}
	/**
	 * 实例化各个索引库
	 */

	/**
	 * 应用程序启动时执行
	 */
	// 不用自动装配@Autowired IPersonStoreSolrService personStoreSolrService;
	HttpSolrServer psServer = SolrServerFactory.getPSSolrServerInstance();

	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("应用程序初始化启动.");
		try {
			// 是否为每个库重新建立索引
			if (StoreIndexPath.isCreateIndex("createPersonStoreIndex")) {
				// 人员
				IPersonStoreSolrService personStoreSolrService = (IPersonStoreSolrService) SpringUtils
						.getBean("personStoreSolrServiceImpl");
				personStoreSolrService.createNewIndexByStores(psServer);
				logger.info("人员库索引创建成功");
			}
			if (StoreIndexPath.isCreateIndex("createClueStoreIndex")) {
				// 线索
				logger.info("线索库创建索引成功");
			}
			if (StoreIndexPath.isCreateIndex("createOrganizStoreIndex")) {
				// 组织

				logger.info("组织库创建索引成功");
			}
			if (StoreIndexPath.isCreateIndex("createWebsiteJournalStoreIndex")) {
				// 媒体网站

				logger.info("媒体网站刊物库创建索引成功");
			}
			if (StoreIndexPath.isCreateIndex("createVocationalWorkStoreIndex")) {
				// 业务文档

				logger.info("业务文档库创建索引成功");
			}
			if (StoreIndexPath.isCreateIndex("createOutsideDocStoreIndex")) {
				// 外来文档

				logger.info("外来文档库创建索引成功");
			}
			if (StoreIndexPath.isCreateIndex("createLeadSpeakStoreIndex")) {
				// 领导讲话

				logger.info("领导讲话库创建索引成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建索引库失败");
		}
		/*
		 * // 有初始化程序时，才执行操作 if(initClass.size() > 0) { logger.debug("需要执行"+
		 * initClass.size() +"个初始化程序."); try { for(String className : initClass)
		 * { logger.debug("执行:" + className); InitializedService service =
		 * (InitializedService)Class.forName(className).newInstance();
		 * service.initialized(sce); } } catch(Exception e) {
		 * logger.error("应用程序执行初始化失败.", e); System.exit(-1); } }
		 */
	}

	/**
	 * 应用程序关闭时执行
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("应用程序资源关闭...");
		try {
			// 应用程序关闭时主动关闭索引库
			IPersonStoreSolrService personStoreSolrService = (IPersonStoreSolrService) SpringUtils
					.getBean("personStoreSolrServiceImpl");
			personStoreSolrService.closeServer(psServer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 有注销程序时，才执行操作
		if (destroyClass.size() > 0) {
			logger.debug("需要执行" + destroyClass.size() + "个注销程序.");
			try {
				for (String className : destroyClass) {
					logger.debug("执行:" + className);
					DestroyedService service = (DestroyedService) Class.forName(className).newInstance();
					service.destroy(sce);
				}
			} catch (Exception e) {
				logger.error("应用程序资源关闭失败.", e);
				System.exit(-1);
			}
		}
	}
}
