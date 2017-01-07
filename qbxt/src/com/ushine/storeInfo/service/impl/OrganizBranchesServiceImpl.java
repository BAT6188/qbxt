package com.ushine.storeInfo.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.OrganizBranches;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.IOrganizBranchesService;
import com.ushine.util.StringUtil;

/**
 * 组织分支机构接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("organizBranchesServiceImpl")
public class OrganizBranchesServiceImpl implements IOrganizBranchesService{
	private static final Logger logger = LoggerFactory.getLogger(OrganizBranchesServiceImpl.class);
	@Autowired
	private IBaseDao<OrganizBranches, String> baseDao;
	
	
	public boolean saveOrganizBranches(OrganizBranches organizBranches)
			throws Exception {
		// TODO Auto-generated method stub
		baseDao.save(organizBranches);
		return true;
	}

	public OrganizBranches findOrgBranchesById(String orgBranchesId)
			throws Exception {
		// TODO Auto-generated method stub
		
		return baseDao.findById(OrganizBranches.class, orgBranchesId);
	}

	public PagingObject<OrganizBranches> findOrgSubordinatesOrgStore(
			String organizId, String field, String fieldValue, String startTime,
			String endTime, int nextPage, int size) throws Exception {
		DetachedCriteria criteria = getCondition(organizId,field, fieldValue, startTime, endTime, nextPage, size);
		int rowCount = baseDao.getRowCount(criteria);

		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<OrganizBranches> list = baseDao.findPagingByCriteria(criteria, size,
				paging.getStartRecord());
		PagingObject<OrganizBranches> vo = new PagingObject<OrganizBranches>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}


	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String organizId,String field,String fieldValue,String startTime,String endTime,int nextPage, int size) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrganizBranches.class);

		
		criteria.createAlias("organizStore", "o");
		criteria.createAlias("organizBranches", "b");
		//查询已入库的数据
		criteria.add(Restrictions.eq("b.isToStorage", "1"));
		criteria.add(Restrictions.eq("o.id", organizId));
		if(!StringUtil.isNull(startTime) && startTime.length() >=10){
			startTime = startTime.substring(0,10)+ " 00:00:00";
			criteria.add(Restrictions.ge("b.createDate", startTime));
		}
		if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)){
			criteria.add(Restrictions.like("b."+field, "%"+fieldValue+"%"));
		}
		if(!StringUtil.isNull(endTime) && endTime.length()>=10){
			endTime = endTime.substring(0,10)+" 23:59:59";
			criteria.add(Restrictions.le("b.createDate", endTime));
		}
		return criteria;
	}

	public boolean saveOrganizBranches(String organizId, String[] ids)
			throws Exception {
		// TODO Auto-generated method stub
		for (String s : ids) {
			String sql = "SELECT COUNT(*) FROM T_ORGANIZ_BRANCHES WHERE ORGANIZ_ID  = '"+organizId+"' AND ORGANIZ_BRANCHES_ID = '"+s+"'";
			int count = Integer.parseInt(baseDao.getRows(sql).toString());
			if(count == 0){
				StringBuffer sb = new StringBuffer();
				sb.append("INSERT INTO T_ORGANIZ_BRANCHES  (ID,ORGANIZ_ID,ORGANIZ_BRANCHES_ID) VALUES ");
				sb.append(" ('"+StringUtil.uuidUpperCase()+"','"+organizId+"','"+s+"')");
				baseDao.executeSql(sb.toString());
			}
		}
		return true;
	}

	public boolean delOrganizBranchesByids(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		baseDao.deleteById(OrganizBranches.class, ids);
		return true;
	}

}
