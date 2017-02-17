package com.ushine.storesinfo.service;

import com.ushine.common.vo.PagingObject;
import com.ushine.storesinfo.model.OrganizStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;

/**
 * 组织基础库接口
 * @author wangbailin
 *
 */
public interface IOrganizStoreService {
		/**
		 * 新增组织基础库信息
		 * @param organizStore
		 * @return
		 * @throws Exception
		 */
		public boolean saveOrganizStore(OrganizStore organizStore)throws Exception;
		/**
		 * 根据组织库id查询
		 * @param organizStoreId
		 * @return
		 * @throws Exception
		 */
		public OrganizStore findOrganizStoreById(String organizStoreId)throws Exception;
		/**
		 * 根据是否启用来查询启用的数据
		 * @param isEnable
		 * @return
		 * @throws Exception
		 */
		public PagingObject<OrganizStore> findOrganizStoreByIsEnable(String field, String fieldValue, String startTime,String endTime,int nextPage, int size)throws Exception;
		/**
		 * 根据传入条件和权限分页筛选组织记录
		 * @Description: TODO
		 * @param field 查询的字段名称
		 * @param fieldValue 关键字
		 * @param startTime 开始时间
		 * @param endTime 结束时间
		 * @param nextPage 翻页
		 * @param size 每页数量
		 * @param uid 根据uid查询个人数据
		 * @param oid 根据组织id查询数据
		 * @param did 根据部门id查询数据
		 * @param sortField 排序字段
		 * @param dir 升序或降序
		 * @return
		 * @throws Exception
		 * @author donghao
		 * @date 2016-5-10
		 */
		public String findOrganizStore(String field, String fieldValue, String startTime,String endTime,
				int nextPage, int size, String uid, String oid, String did,String sortField,String dir)
				throws Exception;
		/**
		 * 根据是否启用来查询启用的数据
		 * @param isEnable
		 * @return
		 * @throws Exception
		 */
		public PagingObject<OrganizStore> findOrganizStoreByIsEnable(int nextPage, int size, String uid, String oid, String did)throws Exception;
		/**
		 * 根据组织库id修改组织
		 * @param ids
		 * @return
		 * @throws Exception
		 */
		public boolean updateOrganizStoreAction(String[] ids)throws Exception;
		/**
		 * 修改组织库信息
		 * @param organizStore
		 * @return
		 * @throws Exception
		 */
		public boolean updateOrganizStoreByOrgId(OrganizStore organizStore)throws Exception;
		/**
		 * 修改组织库信息为启用
		 * @param orgIds
		 * @return
		 * @throws Exception
		 */
		public boolean updateOrganizStoreIsEnableStart(String[] orgIds)throws Exception;
		/**
		 * 修改组织库信息为禁用
		 * @param orgIds
		 * @return
		 * @throws Exception
		 */
		public boolean updateOrganizStoreIsEnableCease(String[] orgIds)throws Exception;
		/**
		 * 保存组织信息为word 
		 * @param id 组织id
		 * @param filePath 文件路径
		 */
		public void outputOrganizStoreToWord(String id,String filePath);
		/**
		 * 根据组织名称查询组织是否存在
		 * @param organizName
		 * @return
		 */
		public boolean hasStoreByOrganizName(String organizName);
		
}
