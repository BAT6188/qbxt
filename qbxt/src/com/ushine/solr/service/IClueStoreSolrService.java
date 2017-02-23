package com.ushine.solr.service;

import java.util.List;

import com.ushine.solr.solrbean.ClueStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.ClueStoreVo;
import com.ushine.storesinfo.model.ClueStore;
/**
 * 线索的solr操作接口
 * @author Administrator
 *
 */
public interface IClueStoreSolrService extends IBaseSolrService<ClueStore> {
	/**
	 * 分页查询
	 * @param bean 查询条件
	 * @param start 第几行开始
	 * @param rows 查询多少条
	 * @return 视图模型ClueStoreVo
	 */
	List<ClueStoreVo> getDocuementsVo(QueryBean bean,int start,int rows);
	/**
	 * 将ClueStore集合转ClueStoreSolr集合
	 * @param list
	 * @return ClueStoreSolr集合
	 */
	List<ClueStoreSolr> convertToSolrBeanList(List<ClueStore> list);
	/**
	 * 将ClueStoreSolr集合转为ClueStoreVo视图层的集合
	 * @param list ClueStoreSolr集合
	 * @return ClueStoreVo视图层显示的集合
	 */
	List<ClueStoreVo> convertToClueStoreVoList(List<ClueStoreSolr> list);
}
