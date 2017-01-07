package com.ushine.store.base;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.NRTManager;

public interface IBaseIndexService {
	/**
	 * 新增一条索引
	 * @param nrtManager NRTManager
	 * @param document	document对象
	 * @throws Exception
	 */
	void addIndex(NRTManager nrtManager,Document document)throws Exception;
	/**
	 * 新增集合索引
	 * @param nrtManager
	 * @param documents	document的集合
	 * @throws Exception
	 */
	void addIndex(NRTManager nrtManager,List<Document> documents)throws Exception;
	
	void deleteIndex(NRTManager nrtManager,String id)throws Exception;
	
	void deleteIndex(NRTManager nrtManager,String[]ids)throws Exception;
}
