package com.ushine.solr.service;

import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

public interface ISolrService<T> {

	void addDocumentByStore(HttpSolrServer server, T store);

	void addDocumentByStores(HttpSolrServer server, List<T> store);

	void deleteDocumentById(HttpSolrServer server, String id);

	void deleteDocumentByIds(HttpSolrServer server, String[] ids);

	void updateDocumentByStore(HttpSolrServer server, String id, T store);
	
	long getDocumentsCount(HttpSolrServer server,String query,String startDate,String endDate);
	/**
	 	SolrQuery params = new SolrQuery();
		params.setQuery("*:*");
		//降序
		params.setSort(fieldName1, ORDER.desc);
		//升序
		params.addSort(fieldName2, ORDER.asc);
		
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
