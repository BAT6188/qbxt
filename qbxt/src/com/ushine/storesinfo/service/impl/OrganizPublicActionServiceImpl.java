package com.ushine.storesinfo.service.impl;

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
import com.ushine.storesinfo.model.OrganizBranches;
import com.ushine.storesinfo.model.OrganizPerson;
import com.ushine.storesinfo.model.OrganizPublicAction;
import com.ushine.storesinfo.service.IOrganizPublicActionService;
import com.ushine.util.StringUtil;
/**
 * 组织下属刊物接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("organizPublicActionServiceImpl")
public class OrganizPublicActionServiceImpl implements IOrganizPublicActionService{
	private static final Logger logger = LoggerFactory.getLogger(OrganizPersonServiceImpl.class);
	@Autowired
	private IBaseDao<OrganizPublicAction, String> baseDao;
	public boolean saveOrgPublicAction(OrganizPublicAction organizPublicAction)
			throws Exception {
		// TODO Auto-generated method stub
		baseDao.save(organizPublicAction);
		return true;
	}

	public OrganizPublicAction findOrgPublicActionById(String orgPublicActionId)
			throws Exception {
		// TODO Auto-generated method stub
		return baseDao.findById(OrganizPublicAction.class, orgPublicActionId);
	}
	public PagingObject<OrganizPublicAction> findOrgSubordinatesOrganizPublicActionStore(
			String organizId, String field, String fieldValue, String startTime,
			String endTime, int nextPage, int size) throws Exception {
		DetachedCriteria criteria = getCondition(organizId,field, fieldValue, startTime, endTime, nextPage, size);
		int rowCount = baseDao.getRowCount(criteria);

		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<OrganizPublicAction> list = baseDao.findPagingByCriteria(criteria, size,
				paging.getStartRecord());
		PagingObject<OrganizPublicAction> vo = new PagingObject<OrganizPublicAction>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}


	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String organizId,String field,String fieldValue,String startTime,String endTime,int nextPage, int size) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrganizPublicAction.class);

		
		criteria.createAlias("organizStore", "o");
		criteria.createAlias("websiteJournalStore", "w");
		//查询已入库的数据
		criteria.add(Restrictions.eq("w.isToStorage", "1"));
		criteria.add(Restrictions.eq("o.id", organizId));
		if(!StringUtil.isNull(startTime) && startTime.length() >=10){
			startTime = startTime.substring(0,10)+ " 00:00:00";
			criteria.add(Restrictions.ge("w.createDate", startTime));
		}
		if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)){
			criteria.add(Restrictions.like("w."+field, "%"+fieldValue+"%"));
		}
		if(!StringUtil.isNull(endTime) && endTime.length()>=10){
			endTime = endTime.substring(0,10)+" 23:59:59";
			criteria.add(Restrictions.le("w.createDate", endTime));
		}
		return criteria;
	}

	public boolean saveOrganizPublicAction(String organizId, String[] ids)
			throws Exception {
		// TODO Auto-generated method stub
		for (String s : ids) {
			String sql = "SELECT COUNT(*) FROM T_ORGANIZ_PUBLIC_ACTION WHERE ORGANIZ_STORE_ID = '"+organizId+"' AND WEBSITE_JOURNAL_STORE_ID = '"+s+"'";
			int count = Integer.parseInt(baseDao.getRows(sql).toString());
			if(count == 0){
				StringBuffer sb = new StringBuffer();
				sb.append("INSERT INTO T_ORGANIZ_PUBLIC_ACTION (ID,ORGANIZ_STORE_ID,WEBSITE_JOURNAL_STORE_ID) VALUES ");
				sb.append(" ('"+StringUtil.uuidUpperCase()+"','"+organizId+"','"+s+"')");
				baseDao.executeSql(sb.toString());
			}
		}
		return true;
	}

	public boolean delOrganizOrganizPublicActionByids(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		baseDao.deleteById(OrganizPublicAction.class, ids);
		return true;
	}

	public PagingObject<OrganizPublicAction> findNetworkAtHigherLevelBynetworkId(
			String networkId, String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size)
			throws Exception {
		DetachedCriteria criteria = getCondition1(networkId,field, fieldValue, startTime, endTime, nextPage, size);
		int rowCount = baseDao.getRowCount(criteria);

		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<OrganizPublicAction> list = baseDao.findPagingByCriteria(criteria, size,
				paging.getStartRecord());
		PagingObject<OrganizPublicAction> vo = new PagingObject<OrganizPublicAction>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition1(String networkId,String field,String fieldValue,String startTime,String endTime,int nextPage, int size) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrganizPublicAction.class);

		
		criteria.createAlias("organizStore", "o");
		criteria.createAlias("websiteJournalStore", "w");
		criteria.add(Restrictions.eq("w.id", networkId));
		//查询已入库的数据
		criteria.add(Restrictions.eq("o.isToStorage", "1"));
		if(!StringUtil.isNull(startTime) && startTime.length() >=10){
			startTime = startTime.substring(0,10)+ " 00:00:00";
			criteria.add(Restrictions.ge("o.createDate", startTime));
		}
		if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)){
			criteria.add(Restrictions.like("o."+field, "%"+fieldValue+"%"));
		}
		if(!StringUtil.isNull(endTime) && endTime.length()>=10){
			endTime = endTime.substring(0,10)+" 23:59:59";
			criteria.add(Restrictions.le("o.createDate", endTime));
		}
		return criteria;
	}

	public boolean saveNetworkSubOrganiz(String networkId, String[] ids)
			throws Exception {
		for (String s : ids) {
			String sql = "SELECT COUNT(*) FROM T_ORGANIZ_PUBLIC_ACTION WHERE ORGANIZ_STORE_ID = '"+s+"' AND WEBSITE_JOURNAL_STORE_ID = '"+networkId+"'";
			int count = Integer.parseInt(baseDao.getRows(sql).toString());
			if(count == 0){
				StringBuffer sb = new StringBuffer();
				sb.append("INSERT INTO T_ORGANIZ_PUBLIC_ACTION (ID,ORGANIZ_STORE_ID,WEBSITE_JOURNAL_STORE_ID) VALUES ");
				sb.append(" ('"+StringUtil.uuidUpperCase()+"','"+s+"','"+networkId+"')");
				baseDao.executeSql(sb.toString());
			}
		}
		return true;
	}
}
