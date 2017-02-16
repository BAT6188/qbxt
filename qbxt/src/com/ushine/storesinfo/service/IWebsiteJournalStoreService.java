package com.ushine.storesinfo.service;

import com.ushine.common.vo.PagingObject;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;

/**
 * 媒体网站刊物接口
 * @author wangbailin
 *
 */
public interface IWebsiteJournalStoreService {
	/**
	 * 新增媒体网站刊物
	 * @param websiteJournalStore
	 * @return
	 * @throws Exception
	 */
	public boolean saveWebsitejournal(WebsiteJournalStore websiteJournalStore)throws Exception;
	/**
	 * 根据媒体网站刊物id查询
	 * @param websiteJouId
	 * @return
	 * @throws Exception
	 */
	public WebsiteJournalStore findWebsiteJouById(String websiteJouId)throws Exception;
	/**
	 * 条件查询
	 * @param field 属性(字段)
	 * @param fieldValue 属性(字段)值
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param nextPage 起始页
	 * @param size 每页数量
	 * @param uid 用户id
	 * @param oid 组织id
	 * @param did 部门id
	 * @param sortField 排序字段
	 * @param dir 升或降
	 * @return
	 * @throws Exception
	 */
	public String findWebsiteJournalStore(String field, String fieldValue,String startTime, String endTime, int nextPage, int size,
			String uid, String oid, String did,String sortField,String dir)throws Exception;
	/**
	 * 删除媒体网站刊物
	 * 把action属性变为3
	 * @param websiteJournalStoreIds id数组
	 * @return
	 * @throws Exception
	 */
	public void delWebsiteJournalStoreByIds(String []websiteJournalStoreIds)throws Exception;
	/**
	 * 修改媒体网站刊物
	 * @param websiteJournalStore
	 * @throws Exception
	 */
	public void updateWebsiteJournalStore(WebsiteJournalStore websiteJournalStore)throws Exception;
	/**
	 * 根据是否启用来查询启用的数据
	 * @param isEnable
	 * @return
	 * @throws Exception
	 */
	public PagingObject<WebsiteJournalStore> findWebsiteJournalStoreByIsEnable(String field, String fieldValue, String startTime,String endTime,int nextPage, int size)throws Exception;
	/**
	 * 根据是否启用来查询启用的数据
	 * @param isEnable
	 * @return
	 * @throws Exception
	 */
	public PagingObject<WebsiteJournalStore> findWebsiteJournalByIsEnable(int nextPage, int size, String uid, String oid, String did)throws Exception;
	/**
	 * 启用媒体网站刊物
	 * @param ids id数组
	 * @throws Exception
	 */
	public void startWebsiteJournalStore(String []ids)throws Exception;
	/**
	 * 禁用媒体网站刊物
	 * @param ids id数组
	 * @throws Exception
	 */
	public void ceaseWebsiteJournalStore(String []ids)throws Exception;
	/**
	 * 将媒体网站刊物保存为word
	 * @param id id
	 * @param filePath 文件路径
	 */
	public void outputWebsiteJournalStoreToWord(String id,String filePath);
	/**
	 * 根据名称判断是否存在
	 * @param name
	 * @return
	 */
	public boolean hasStoreByName(String name);
}
