package com.ushine.storesinfo.service;

import com.ushine.common.vo.PagingObject;
import com.ushine.storesinfo.model.OrganizBranches;
import com.ushine.storesinfo.model.WebsiteJournalStore;

/**
 * 组织分支机构接口
 * @author wangbailin
 *
 */
public interface IOrganizBranchesService {
    /**
     * 新增组织分支机构
     * @param organizBranches
     * @return
     * @throws Exceptin
     */
	public boolean saveOrganizBranches(OrganizBranches organizBranches)throws Exception;
	/**
	 * 根据分支机构id查询
	 * @param orgBranchesId
	 * @return
	 * @throws Exception
	 */
	public OrganizBranches findOrgBranchesById(String orgBranchesId)throws Exception;
	/**
	 * 根据组织库id查询下属组织库信息
	 * @param id
	 * @param field
	 * @param fieldValue
	 * @param nextPage
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public PagingObject<OrganizBranches> findOrgSubordinatesOrgStore(String organizId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
	/**
	 * 组织关联下属组织id
	 * @param organizId
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean saveOrganizBranches(String organizId,String[] ids)throws Exception;
	
	/**
	 * 删除下属组织
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean delOrganizBranchesByids(String[] ids) throws Exception;
}
