package com.ushine.solr.service;

import java.util.List;
import com.ushine.solr.solrbean.QueryBean;

/**
 * 索引库操作的基接口<br>
 * 该接口替代了ISolrService接口，把参数HttpSolrServer去掉，放到了具体的实现类内部了
 * @author dh
 *
 */
public interface IBaseSolrService<T>{

	void addDocumentByStore(T daoStore);
	
	void addDocumentByStores(List<T> daoStore);
	
	void createNewIndexByStores();
	
	void deleteDocumentById(String id);
	
	void deleteDocumentByIds(String[] ids);
	
	void deleteAll();
	
	void updateDocumentByStore(String id, T daoStore);
	
	long getDocumentsCount(QueryBean queryBean);
	
	void closeServer();
}
