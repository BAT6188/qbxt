package com.ushine.solr.service;

import java.util.List;

import com.ushine.solr.solrbean.LeadSpeakStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.LeadSpeakStoreVo;
import com.ushine.storesinfo.model.LeadSpeakStore;

public interface ILeadSpeakStoreSolrService extends IBaseSolrService<LeadSpeakStore> {
	/**
	 * 查询并分页显示
	 * @param bean
	 * @param start
	 * @param rows
	 * @return
	 */
	List<LeadSpeakStoreVo> getDocuementsVo(QueryBean bean,int start,int rows);
	/**
	 * LeadSpeakStore-->>LeadSpeakStoreSolr
	 * @param list
	 * @return
	 */
	List<LeadSpeakStoreSolr> convertToSolrBeanList(List<LeadSpeakStore> list);
	/**
	 * LeadSpeakStoreSolr-->>LeadSpeakStoreVo
	 * @param list
	 * @return
	 */
	List<LeadSpeakStoreVo> convertToOutsideDocStoreVoList(List<LeadSpeakStoreSolr> list);
}
