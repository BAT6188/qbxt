package com.ushine.storeInfo.service;

import com.ushine.common.vo.PagingObject;
import com.ushine.storeInfo.model.OrganizBranches;
import com.ushine.storeInfo.model.OrganizPerson;

/**
 * 组织下属成员接口
 * @author wangbailin
 *
 */
public interface IOrganizPersonService {
	/**
	 * 新增组织下属成员
	 * @param organizPerson
	 * @return
	 * @throws Exception
	 */
	public boolean saveOrganizPerson(OrganizPerson organizPerson)throws Exception;
	/**
	 * 根据下属成员id查询
	 * @param orgPersonId
	 * @return
	 * @throws Exception
	 */
	public OrganizPerson findOrgPersonById(String orgPersonId)throws Exception;
	/**
	 * 根据组织库id查询下属人员库信息
	 * @param id
	 * @param field
	 * @param fieldValue
	 * @param nextPage
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public PagingObject<OrganizPerson> findOrgSubordinatesPersonStore(String organizId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
	/**
	 * 组织关联下属人员
	 * @param organizId
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean saveOrganizPersons(String organizId,String[] ids)throws Exception;
	/**
	 * 删除下属人员
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean delOrganizPersonByIds(String[] ids) throws Exception;
	
	/**
	 * 根据人员id查询人员上级组织
	 * @param personStoreId
	 * @param field
	 * @param fieldValue
	 * @param startTime
	 * @param endTime
	 * @param nextPage
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public PagingObject<OrganizPerson> findPersonAtHigherLevelByPersonStoreId(String personStoreId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
	/**
	 * 人员关联上级组织
	 * @param organizId
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean savePersonSubOrganiz(String personStoreId,String[] ids)throws Exception;
}
