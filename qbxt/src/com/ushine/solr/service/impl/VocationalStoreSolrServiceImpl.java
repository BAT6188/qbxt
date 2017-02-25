package com.ushine.solr.service.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushine.dao.IBaseDao;
import com.ushine.solr.service.IVocationalStoreSolrService;
import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.solrbean.VocationalWorkStoreSolr;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.VocationalWorkStoreVo;
import com.ushine.storesinfo.model.VocationalWorkStore;

@Service(value = "vocationalStoreSolrServiceImpl")
public class VocationalStoreSolrServiceImpl implements IVocationalStoreSolrService {

	Logger logger = Logger.getLogger(VocationalStoreSolrServiceImpl.class);
	@Autowired IBaseDao<VocationalWorkStore, Serializable> baseDao;
	/**
	 * 将dao层对象转成solr里的bean
	 * 
	 * @param store
	 *            VocationalWorkStore对象
	 * @return VocationalWorkStoreSolr对象
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private VocationalWorkStoreSolr convertDaoStoreToSolr(VocationalWorkStore store) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			NoSuchFieldException, SecurityException {
		VocationalWorkStoreSolr solr = new VocationalWorkStoreSolr();
		solr=(VocationalWorkStoreSolr) SolrBeanUtils.convertBeanToAnotherBean(store, solr, QueryBean.ID, QueryBean.VOCATIONALWORK_ID);
		return solr;
	}

	/**
	 * 将solr中的bean转成业务文档vo层的bean
	 * 
	 * @param storeSolr
	 *            VocationalWorkStoreSolr对象
	 * @return VocationalWorkStoreVo对象实例
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private VocationalWorkStoreVo convertStoreSolrToVo(VocationalWorkStoreSolr storeSolr) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			NoSuchFieldException, SecurityException {

		VocationalWorkStoreVo vo = new VocationalWorkStoreVo();
		vo = (VocationalWorkStoreVo) SolrBeanUtils.convertBeanToAnotherBean(storeSolr, vo, QueryBean.VOCATIONALWORK_ID, QueryBean.ID);
		return vo;
	}

	@Override
	public int addDocumentByStore(HttpSolrServer server, VocationalWorkStore daoStore) {
		try {
			VocationalWorkStoreSolr solrBean = convertDaoStoreToSolr(daoStore);
			// daoStore转成solr bean
			server.addBean(solrBean);
			server.commit();
			logger.info("新增业务文档索引成功");
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增业务文档索引失败：" + e.getMessage());
			return -1;
		}
	}

	@Override
	public int addDocumentByStores(HttpSolrServer server, List<VocationalWorkStore> list) {
		try {
			List<VocationalWorkStoreSolr> storeSolrs = new ArrayList<>();
			for (VocationalWorkStore vocationalWorkStore : list) {
				// 转换
				storeSolrs.add(convertDaoStoreToSolr(vocationalWorkStore));
			}
			server.addBeans(storeSolrs);
			// 提交
			server.commit();
			logger.info(String.format("新增%s条业务文档索引成功", list.size()));
			return list.size();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增业务文档索引失败：" + e.getMessage());
			return 0;
		}
	}

	@Override
	public void createNewIndexByStores(HttpSolrServer server) {
		try {
			//清空
			deleteAll(server);
			//数据库中没有删除的
			List<VocationalWorkStore> list = baseDao.findByHql(String.format("from %s where action<>3", "VocationalWorkStore"));
			addDocumentByStores(server, list);
			logger.info(String.format("创建索引库成功，共%s条", list.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建新的业务文档solr索引库失败："+e.getMessage());
		}
	}

	@Override
	public void deleteDocumentById(HttpSolrServer server, String id) {
		// 调用deleteDocumentByIds
		deleteDocumentByIds(server, new String[] { id });
	}

	@Override
	public void deleteDocumentByIds(HttpSolrServer server, String[] ids) {
		List<String> list = new ArrayList<>();
		try {
			CollectionUtils.addAll(list, ids);
			server.deleteById(list);
			// 提交
			server.commit();
			logger.info(String.format("删除%s条索引成功", ids.length));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除业务文档solr索引失败：" + e.getMessage());
		}
	}

	@Override
	public void deleteAll(HttpSolrServer server) {
		try {
			server.deleteByQuery(QueryBean.VOCATIONALWORK_ID + ":*");
			server.commit();
			logger.info("清空业务文档solr索引库完成");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("清空业务文档solr索引失败：" + e.getMessage());
		}
	}

	@Override
	public void updateDocumentByStore(HttpSolrServer server, String id, VocationalWorkStore daoStore) {
		try {
			daoStore.setId(id);
			server.addBean(convertDaoStoreToSolr(daoStore));
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(String.format("更新id为：%s的索引失败："+e.getMessage(),id));
		}
	}

	@Override
	public long getDocumentsCount(HttpSolrServer server, QueryBean queryBean) {
		try {
			QueryResponse response=server.query(queryBean.getSolrQuery(VocationalWorkStore.class));
			long result=response.getResults().getNumFound();
			logger.info(String.format("查询符合条件的业务文档总数有：%s条", result));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询符合条件的总数失败："+e.getMessage());
		}
		return -1;
	}

	@Override
	public void closeServer(HttpSolrServer server) {
		if (null != server) {
			server.shutdown();
		}
	}

	@Override
	public List<VocationalWorkStoreVo> getDocuementsVO(HttpSolrServer server, QueryBean bean, int start, int rows) {
		List<VocationalWorkStoreVo> list=new ArrayList<>();
		try {
			SolrQuery query=bean.getSolrQuery(VocationalWorkStore.class);
			//分页
			query.setStart(start).setRows(rows);
			QueryResponse response = server.query(query);
			//先将solr中的bean转成VocationalWorkStore的bean
			SolrDocumentList sdList = response.getResults();
			DocumentObjectBinder binder=new DocumentObjectBinder();
			List<VocationalWorkStoreSolr> beans = binder.getBeans(VocationalWorkStoreSolr.class, sdList);
			for (VocationalWorkStoreSolr solrBean : beans) {
				//转成vo对象
				VocationalWorkStoreVo vo = convertStoreSolrToVo(solrBean);
				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询业务文档数据异常："+e.getMessage());
		}
		return list;
	}

}
