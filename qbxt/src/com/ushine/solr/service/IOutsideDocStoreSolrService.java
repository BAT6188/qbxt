package com.ushine.solr.service;

import java.util.List;

import com.ushine.solr.solrbean.OutsideDocStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.OutsideDocStoreVo;
import com.ushine.storesinfo.model.OutsideDocStore;

public interface IOutsideDocStoreSolrService extends IBaseSolrService<OutsideDocStore> {
	/**
	 * 分页，获得符合查询条件的OutsideDocStoreVo对象集合
	 * @param bean QueryBean查询条件
	 * @param start 第几行开始
	 * @param rows 获得几行
	 * @return OutsideDocStoreVo的List集合
	 */
	List<OutsideDocStoreVo> getDocuementsVo(QueryBean bean,int start,int rows);
	/**
	 * 将OutsideDocStore转成solr中的bean
	 * @param store OutsideDocStore实例
	 * @return OutsideDocStoreSolr实例
	 */
	OutsideDocStoreSolr convertToSolrBean(OutsideDocStore store);
	/**
	 * 转OutsideDocStore到solr中的bean集合
	 * @param list OutsideDocStore集合
	 * @return OutsideDocStoreSolr对象集合
	 */
	List<OutsideDocStoreSolr> convertToSolrBeanList(List<OutsideDocStore> list);
	/**
	 * 将OutsideDocStoreSolr转成OutsideDocStoreVo的bean
	 * @param solrBean OutsideDocStoreSolr实例对象
	 * @return OutsideDocStoreVo实例对象
	 */
	OutsideDocStoreVo convertToOutsideDocStoreVo(OutsideDocStoreSolr solrBean);
	/**
	 * 转OutsideDocStoreSolr集合到OutsideDocStoreVo集合
	 * @param list OutsideDocStoreSolr的List
	 * @return OutsideDocStoreVo的List
	 */
	List<OutsideDocStoreVo> convertToOutsideDocStoreVoList(List<OutsideDocStoreSolr> list);
}
