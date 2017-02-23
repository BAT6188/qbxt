package com.ushine.solr.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.Query;

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
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.ILeadSpeakStoreSolrService;
import com.ushine.solr.solrbean.LeadSpeakStoreSolr;
import com.ushine.solr.solrbean.OutsideDocStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.LeadSpeakStoreVo;
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.model.OutsideDocStore;

@Service(value = "leadSpeakStoreSolrServiceImpl")
public class LeadSpeakStoreSolrServiceImpl implements ILeadSpeakStoreSolrService {
	// 单例
	HttpSolrServer server = SolrServerFactory.getLSSSolrServerInstance();
	Logger logger = Logger.getLogger(LeadSpeakStoreSolrServiceImpl.class);
	@Autowired
	IBaseDao<LeadSpeakStore, Serializable> baseDao;

	@Override
	public void addDocumentByStore(LeadSpeakStore daoStore) {
		List<LeadSpeakStore> daoList = new ArrayList<>();
		try {
			daoList.add(daoStore);
			// 转换
			List<LeadSpeakStoreSolr> solrList = convertToSolrBeanList(daoList);
			server.addBeans(solrList);
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增一条索引异常：" + e.getMessage());
		}
	}

	@Override
	public void addDocumentByStores(List<LeadSpeakStore> list) {
		try {
			List<LeadSpeakStoreSolr> solrList = convertToSolrBeanList(list);
			server.addBeans(solrList);
			// 提交
			server.commit();
			logger.info(String.format("新增索引集合%s条完成", solrList.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增索引集合异常：" + e.getMessage());
		}
	}

	@Override
	public void createNewIndexByStores() {
		List<LeadSpeakStore> daoList = new ArrayList<>();
		try {
			deleteAll();
			// 获得数据库里没被删除的
			daoList = baseDao.findByHql(String.format("from %s where action<>3", "LeadSpeakStore"));
			List<LeadSpeakStoreSolr> solrList = convertToSolrBeanList(daoList);
			server.addBeans(solrList);
			server.commit();
			logger.info(String.format("创建新的索引集合%s条完成", solrList.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建新索引库异常：" + e.getMessage());
		}
	}

	@Override
	public void deleteDocumentById(String id) {
		deleteDocumentByIds(new String[] { id });
	}

	@Override
	public void deleteDocumentByIds(String[] ids) {
		List<String> list = new ArrayList<>();
		try {
			CollectionUtils.addAll(list, ids);
			server.deleteById(list);
			server.commit();
			logger.info(String.format("删除索引集合%s条完成", list.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除索引异常：" + e.getMessage());
		}
	}

	/**
	 * 清空原索引库
	 */
	@Override
	public void deleteAll() {
		try {
			// 清空
			server.deleteByQuery(QueryBean.LEADSPEAKSTORE_ID + ":*");
			server.commit();
			logger.info("清空原索引库成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDocumentByStore(String id, LeadSpeakStore daoStore) {
		List<LeadSpeakStore> daoList = new ArrayList<LeadSpeakStore>();
		try {
			daoStore.setId(id);
			daoList.add(daoStore);
			// 转
			server.addBeans(convertToSolrBeanList(daoList));
			server.commit();
			logger.info(String.format("更新id为：%s索引成功", id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("更新id为：%s索引异常：%s", id, e.getMessage()));
		}
	}

	@Override
	public long getDocumentsCount(QueryBean queryBean) {
		try {
			QueryResponse response = server.query(queryBean.getSolrQuery(LeadSpeakStore.class));
			long result = response.getResults().getNumFound();
			logger.info("查询符合条件的数量为：" + result);
			return result;
		} catch (Exception e) {
			logger.error("查询数量失败");
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void closeServer() {
		try {
			if (null != server) {
				server.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("关闭server异常：" + e.getMessage());
		}
	}

	@Override
	public List<LeadSpeakStoreVo> getDocuementsVo(QueryBean bean, int start, int rows) {
		List<LeadSpeakStoreVo> voList = new ArrayList<LeadSpeakStoreVo>();
		try {
			SolrQuery query = bean.getSolrQuery(OutsideDocStore.class);
			// 分页
			query.setStart(start).setRows(rows);
			QueryResponse response = server.query(query);
			// 转成solr bean对象
			SolrDocumentList sdList = response.getResults();
			DocumentObjectBinder binder = new DocumentObjectBinder();
			List<LeadSpeakStoreSolr> beans = binder.getBeans(LeadSpeakStoreSolr.class, sdList);
			// 转成vo层对象
			voList = convertToLeadSpeakStoreVoList(beans);
		} catch (Exception e) {
			logger.error("查询数量失败");
			e.printStackTrace();
		}
		return voList;
	}

	@Override
	public List<LeadSpeakStoreSolr> convertToSolrBeanList(List<LeadSpeakStore> list) {

		List<LeadSpeakStoreSolr> solrList = new ArrayList<>();
		try {
			for (LeadSpeakStore leadSpeakStore : list) {
				LeadSpeakStoreSolr solrBean = new LeadSpeakStoreSolr();
				solrBean = (LeadSpeakStoreSolr) SolrBeanUtils.convertBeanToAnotherBean(leadSpeakStore, solrBean, QueryBean.ID, QueryBean.LEADSPEAKSTORE_ID);
				// 加入集合
				solrList.add(solrBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转LeadSpeakStoreSolr异常：" + e.getMessage());
		}
		return solrList;
	}

	@Override
	public List<LeadSpeakStoreVo> convertToLeadSpeakStoreVoList(List<LeadSpeakStoreSolr> list) {

		List<LeadSpeakStoreVo> voList = new ArrayList<>();
		try {
			for (LeadSpeakStoreSolr leadSpeakStoreSolr : list) {
				LeadSpeakStoreVo vo = new LeadSpeakStoreVo();
				vo = (LeadSpeakStoreVo) SolrBeanUtils.convertBeanToAnotherBean(leadSpeakStoreSolr, vo, QueryBean.LEADSPEAKSTORE_ID, QueryBean.ID);
				voList.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转LeadSpeakStoreVo异常：" + e.getMessage());
		}
		return voList;
	}

}
