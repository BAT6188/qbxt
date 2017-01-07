package com.ushine.storeInfo.service;

import com.ushine.common.vo.PagingObject;
import com.ushine.storeInfo.model.OrganizBranches;
import com.ushine.storeInfo.model.OrganizPerson;
import com.ushine.storeInfo.model.OrganizPublicAction;

/**
 * 组织下属刊物接口类
 * @author wangbailin
 *
 */
public interface IOrganizPublicActionService {
	/**
	 * 新增组织下属刊物信息
	 * @param organizPublicAction
	 * @return
	 * @throws Exception
	 */
	public boolean saveOrgPublicAction(OrganizPublicAction organizPublicAction)throws Exception;
	/**
	 * 根据组织下属刊物id查询
	 * @param orgPublicActionId
	 * @return
	 * @throws Exception
	 */
	public OrganizPublicAction findOrgPublicActionById(String orgPublicActionId)throws Exception;
	/**
	 * 根据组织库id查询下属媒体刊物库信息
	 * @param id
	 * @param field
	 * @param fieldValue
	 * @param nextPage
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public PagingObject<OrganizPublicAction> findOrgSubordinatesOrganizPublicActionStore(String organizId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
	/**
	 * 组织关联下属媒体刊物id
	 * @param organizId
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean saveOrganizPublicAction(String organizId,String[] ids)throws Exception;
	/**
	 * 删除媒体刊物
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean delOrganizOrganizPublicActionByids(String[] ids) throws Exception;
	/**
	 * 根据媒体刊物id查询组织
	 * @param networkId
	 * @param field
	 * @param fieldValue
	 * @param startTime
	 * @param endTime
	 * @param nextPage
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public PagingObject<OrganizPublicAction> findNetworkAtHigherLevelBynetworkId(String networkId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
	/**
	 * 媒体刊物关联上级组织
	 * @param organizId
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean saveNetworkSubOrganiz(String networkId,String[] ids)throws Exception;

}
