package com.ushine.solr.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushine.dao.IBaseDao;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IClueStoreSolrService;
import com.ushine.solr.solrbean.ClueStoreSolr;
import com.ushine.solr.solrbean.LeadSpeakStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.ClueStoreVo;
import com.ushine.solr.vo.LeadSpeakStoreVo;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.service.IClueRelationshipService;

/**
 * 实现线索和人员的检索： 1条线索关联着n个人员；同理，1个人员关联着n个线索 查询时应同时检索线索库本身的关键字、检索人员库的关键字，获得了三个结果集合，
 * 线索库符合的、线索库不符合的、人员库符合的，对这三个集合做运算
 * 每个不符合的线索库的personId字段做集合，与人员符合的id集合做containAny运算，true则把该条线索加进
 * 总线索库-符合线索库=线索库不符合
 * 线索库不符合与人员库符合的再做运算
 * @author Administrator
 *
 */
@Service(value="clueStoreSolrServiceImpl")
public class ClueStoreSolrServiceImpl implements IClueStoreSolrService {
	@Autowired
	IClueRelationshipService relationshipService;
	@Autowired
	IBaseDao<ClueStore, Serializable> baseDao;
	protected HttpSolrServer server = SolrServerFactory.getCSSolrServerInstance();
	Logger logger = Logger.getLogger(ClueStoreSolrServiceImpl.class);

	@Override
	public void addDocumentByStore(ClueStore daoStore) {
		List<ClueStore> list=new ArrayList<>();
		list.add(daoStore);
		addDocumentByStores(list);
	}

	@Override
	public void addDocumentByStores(List<ClueStore> list) {
		try {
			List<ClueStoreSolr> solrList = convertToSolrBeanList(list);
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
		List<ClueStore> daoList = new ArrayList<>();
		try {
			deleteAll();
			// 获得数据库里没被删除的
			daoList = baseDao.findByHql(String.format("from %s where action<>3", "ClueStore"));
			List<ClueStoreSolr> solrList = convertToSolrBeanList(daoList);
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

	@Override
	public void deleteAll() {
		try {
			// 清空
			server.deleteByQuery(QueryBean.CLUESTORE_ID + ":*");
			server.commit();
			logger.info("清空原索引库成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDocumentByStore(String id, ClueStore daoStore) {
		List<ClueStore> daoList = new ArrayList<ClueStore>();
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
		// TODO Auto-generated method stub
		return 0;
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
	public List<ClueStoreVo> getDocuementsVo(QueryBean bean, int start, int rows) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClueStoreSolr> convertToSolrBeanList(List<ClueStore> list) {
		List<ClueStoreSolr> solrList = new ArrayList<>();
		try {
			for (ClueStore clueStore : list) {
				ClueStoreSolr targetBean = new ClueStoreSolr();
				targetBean=(ClueStoreSolr) SolrBeanUtils.convertBeanToAnotherBean(clueStore, targetBean, QueryBean.ID, QueryBean.CLUESTORE_ID);
				//获得关联的人员的id
				String personId=relationshipService.findStoreIdByClueId(clueStore.getId(), IClueRelationshipService.PERSONSTORE_TYPE);
				targetBean.setPersonId(personId);
				//添加集合
				solrList.add(targetBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转ClueStoreSolr异常：" + e.getMessage());
		}
		return solrList;
	}

	@Override
	public List<ClueStoreVo> convertToClueStoreVoList(List<ClueStoreSolr> list) {
		List<ClueStoreVo> voList = new ArrayList<>();
		try {
			for (ClueStoreSolr solrBean : list) {
				ClueStoreVo vo = new ClueStoreVo();
				vo=(ClueStoreVo) SolrBeanUtils.convertBeanToAnotherBean(solrBean, vo, QueryBean.CLUESTORE_ID, QueryBean.ID);
				voList.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转LeadSpeakStoreVo异常：" + e.getMessage());
		}
		return voList;
	}

}
