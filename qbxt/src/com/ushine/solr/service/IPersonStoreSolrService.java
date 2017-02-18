package com.ushine.solr.service;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.PersonStore;

public interface IPersonStoreSolrService extends ISolrService<PersonStore>{
	/**
	 * 查询返回vo层的PersonStoreVo对象
	 * @param server
	 * @param queryMap
	 * @param startDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @param sortField
	 * @return
	 */
	List<PersonStoreVo> getDocuementsVO(HttpSolrServer server,Map<String, String> queryMap,String startDate,String endDate,int start,int rows,String sortField);
}
