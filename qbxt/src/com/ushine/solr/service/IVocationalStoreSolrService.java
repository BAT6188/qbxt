package com.ushine.solr.service;

import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.VocationalWorkStoreVo;
import com.ushine.storesinfo.model.VocationalWorkStore;

public interface IVocationalStoreSolrService extends ISolrService<VocationalWorkStore> {
	/**
	 * 获得业务文档的vo集合
	 * @param server HttpSolrServer单例
	 * @param bean 查询条件的bean
	 * @param start 开始行
	 * @param rows 行数
	 * @return VocationalWorkStoreVo的List
	 */
	List<VocationalWorkStoreVo> getDocuementsVO(HttpSolrServer server,QueryBean bean,int start,int rows);
}
