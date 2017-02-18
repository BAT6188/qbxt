package com.ushine.solr.service;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.PersonStore;

public interface IPersonStoreSolrService extends ISolrService<PersonStore>{
	/**
	 * 查询返回vo层的PersonStoreVo对象
	 * @param server HttpSolrServer实例
	 * @param QueryBean 查询语句的bean
	 * @param start 开始行
	 * @param rows 行数
	 * @return
	 */
	List<PersonStoreVo> getDocuementsVO(HttpSolrServer server,QueryBean bean,int start,int rows);
}
