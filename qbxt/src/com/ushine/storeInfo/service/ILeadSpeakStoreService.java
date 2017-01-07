package com.ushine.storeInfo.service;

import com.ushine.storeInfo.model.LeadSpeakStore;

/**
 * 领导讲话接口实现类
 * @author wangbailin
 *
 */
public interface ILeadSpeakStoreService {
	/**
	 * 新增领导讲话
	 * @param leadSpeakStore
	 * @throws Exception
	 */
	void saveLeadSpeakStore(LeadSpeakStore leadSpeakStore)throws Exception;
	/**
	 * 根据id查询到LeadSpeakStore实例
	 * @param leadSpeakStoreId
	 * @return
	 * @throws Exception
	 */
	LeadSpeakStore findLeadSpeakStoreById(String leadSpeakStoreId)throws Exception;
	/**
	 * 更新
	 * @param leadSpeakStore
	 * @throws Exception
	 */
	void updateLeadSpeakStore(LeadSpeakStore leadSpeakStore)throws Exception;
	/**
	 * 根据id数组删除多个LeadSpeakStore对象
	 * 
	 * @param leadSpeakStoreIds
	 * @throws Exception
	 */
	void delLeadSpeakStore(String []leadSpeakStoreIds)throws Exception;
	/**
	 * 多条件查询
	 * @param field 属性
	 * @param fieldValue 属性值
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param nextPage 下一页
	 * @param size 每页数量
	 * @param uid 用户id
	 * @param oid 组织id
	 * @param did 部门id
	 * @param sortField 排序字段
	 * @param dir 降序还是升序
	 * @return
	 * @throws Exception
	 */
	public String findLeadSpeakStore(String field, String fieldValue,String startTime, String endTime, 
			int nextPage, int size,String uid, String oid, String did,String sortField,String dir)throws Exception;
	/**
	 * 根据会议名称判断是否存在
	 * @param title
	 * @return
	 */
	public boolean hasStoreByMeetingName(String meetingName);
}
