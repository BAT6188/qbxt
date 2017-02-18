package com.ushine.solr.service;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.PersonStore;

public interface IPersonStoreSolrService {
	/**
	 * 添加solr索引
	 * @param server HttpSolrServer
	 * @param daoStore dao层的bean模型
	 * @return 成功0，失败-1
	 */
	int addDocumentByStore(HttpSolrServer server, PersonStore store);
	/**
	 * 添加索引集合
	 * @param server HttpSolrServer
	 * @param daoStore daoStore的List
	 * @return 成功返回list的size，失败-1
	 */
	int addDocumentByStores(HttpSolrServer server, List<PersonStore> stores);
	/**
	 * 创建新的solr索引
	 * @param server HttpSolrServer
	 */
	void createNewIndexByStores(HttpSolrServer server);
	/**
	 * 依据id删除一条索引
	 * @param server HttpSolrServer
	 * @param id 对象id
	 */
	void deleteDocumentById(HttpSolrServer server, String id);
	/**
	 * 根据id数组删除多条索引
	 * @param server HttpSolrServer
	 * @param ids id数组
	 */
	void deleteDocumentByIds(HttpSolrServer server, String[] ids);
	/**
	 * 删除所有索引记录
	 * @param server
	 */
	void deleteAll(HttpSolrServer server);

	void updateDocumentByStore(HttpSolrServer server, String id, PersonStore store);
	
	long getDocumentsCount(HttpSolrServer server,Map<String, String> queryMap,String startDate,String endDate);
	
	List<PersonStoreVo> getDocuementsVO(HttpSolrServer server,Map<String, String> queryMap,String startDate,String endDate,int start,int rows,String sortField);
}
