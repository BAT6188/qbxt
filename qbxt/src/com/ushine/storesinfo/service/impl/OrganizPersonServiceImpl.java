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
import com.ushine.storesinfo.service.IOrganizPersonService;
import com.ushine.util.StringUtil;
/**
 * 组织下属成员接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("organizPersonServiceImpl")
public class OrganizPersonServiceImpl implements IOrganizPersonService{
	private static final Logger logger = LoggerFactory.getLogger(OrganizPersonServiceImpl.class);
	@Autowired
	private IBaseDao<OrganizPerson, String> baseDao;
	public boolean saveOrganizPerson(OrganizPerson organizPerson)
			throws Exception {
		// TODO Auto-generated method stub
		baseDao.save(organizPerson);
		return true;
	}

	public OrganizPerson findOrgPersonById(String orgPersonId) throws Exception {
		// TODO Auto-generated method stub
		
		return baseDao.findById(OrganizPerson.class, orgPersonId);
	}

	public PagingObject<OrganizPerson> findOrgSubordinatesPersonStore(
			String organizId, String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size)
			throws Exception {
		DetachedCriteria criteria = getCondition(organizId,field, fieldValue, startTime, endTime, nextPage, size);
		int rowCount = baseDao.getRowCount(criteria);

		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<OrganizPerson> list = baseDao.findPagingByCriteria(criteria, size,
				paging.getStartRecord());
		PagingObject<OrganizPerson> vo = new PagingObject<OrganizPerson>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String organizId,String field,String fieldValue,String startTime,String endTime,int nextPage, int size) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrganizPerson.class);

		
		criteria.createAlias("organizStore", "o");
		criteria.createAlias("personStore", "p");
		criteria.add(Restrictions.eq("o.id", organizId));
		//查询已入库的数据
		criteria.add(Restrictions.eq("p.isToStorage", "1"));
		if(!StringUtil.isNull(startTime) && startTime.length() >=10){
			startTime = startTime.substring(0,10)+ " 00:00:00";
			criteria.add(Restrictions.ge("p.createDate", startTime));
		}
		if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)){
			criteria.add(Restrictions.like("p."+field, "%"+fieldValue+"%"));
		}
		if(!StringUtil.isNull(endTime) && endTime.length()>=10){
			endTime = endTime.substring(0,10)+" 23:59:59";
			criteria.add(Restrictions.le("p.createDate", endTime));
		}
		return criteria;
	}

	public boolean saveOrganizPersons(String organizId, String[] ids)
			throws Exception {
		for (String s : ids) {
			String sql = "SELECT COUNT(*) FROM T_ORGANIZ_PERSON WHERE ORGANIZ_STORE_ID = '"+organizId+"' AND PERSON_STORE_ID = '"+s+"'";
			int count = Integer.parseInt(baseDao.getRows(sql).toString());
			if(count == 0){
				StringBuffer sb = new StringBuffer();
				sb.append("INSERT INTO T_ORGANIZ_PERSON  (ID,ORGANIZ_STORE_ID,PERSON_STORE_ID) VALUES ");
				sb.append(" ('"+StringUtil.uuidUpperCase()+"','"+organizId+"','"+s+"')");
				baseDao.executeSql(sb.toString());
			}
		}
		return true;
	}

	public boolean delOrganizPersonByIds(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		baseDao.deleteById(OrganizPerson.class, ids);
		return true;
	}

	public PagingObject<OrganizPerson> findPersonAtHigherLevelByPersonStoreId(
			String personStoreId, String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size)
			throws Exception {
		DetachedCriteria criteria = getCondition1(personStoreId,field, fieldValue, startTime, endTime, nextPage, size);
		int rowCount = baseDao.getRowCount(criteria);

		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<OrganizPerson> list = baseDao.findPagingByCriteria(criteria, size,
				paging.getStartRecord());
		PagingObject<OrganizPerson> vo = new PagingObject<OrganizPerson>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}
	
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition1(String organizId,String field,String fieldValue,String startTime,String endTime,int nextPage, int size) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrganizPerson.class);

		
		criteria.createAlias("organizStore", "o");
		criteria.createAlias("personStore", "p");
		criteria.add(Restrictions.eq("p.id", organizId));
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
		public boolean savePersonSubOrganiz(String personStoreId, String[] ids)
				throws Exception {
			// TODO Auto-generated method stub
			for (String s : ids) {
				String sql = "SELECT COUNT(*) FROM T_ORGANIZ_PERSON WHERE ORGANIZ_STORE_ID = '"+s+"' AND PERSON_STORE_ID = '"+personStoreId+"'";
				int count = Integer.parseInt(baseDao.getRows(sql).toString());
				if(count == 0){
					StringBuffer sb = new StringBuffer();
					sb.append("INSERT INTO T_ORGANIZ_PERSON  (ID,ORGANIZ_STORE_ID,PERSON_STORE_ID) VALUES ");
					sb.append(" ('"+StringUtil.uuidUpperCase()+"','"+s+"','"+personStoreId+"')");
					baseDao.executeSql(sb.toString());
				}
			}
			return true;
		
	}
}
