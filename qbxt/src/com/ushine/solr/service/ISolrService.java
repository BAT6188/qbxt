package com.ushine.solr.service;

import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

public interface ISolrService<T> {
	/**
	 * 添加solr索引
	 * @param server HttpSolrServer
	 * @param daoStore dao层的bean模型
	 */
	void addDocumentByStore(HttpSolrServer server, T daoStore);

	void addDocumentByStores(HttpSolrServer server, List<T> daoStore);
	
	void createNewIndexByStores(HttpSolrServer server, List<T> daoStore);

	void deleteDocumentById(HttpSolrServer server, String id);

	void deleteDocumentByIds(HttpSolrServer server, String[] ids);
	/**
	 * 删除所有索引记录
	 * @param server
	 */
	void deleteAll(HttpSolrServer server);

	void updateDocumentByStore(HttpSolrServer server, String id, T daoStore);
	
	long getDocumentsCount(HttpSolrServer server,String query,String startDate,String endDate);
	/**
	 * @param server
	 * @param query
	 * @param startDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @param sortField
	 * @return
	 */
	List<T> getDocuementsVO(HttpSolrServer server,String query,String startDate,String endDate,int start,int rows,String sortField);
}
