package com.ushine.storesinfo.util;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.NetworkAccountStore;
import com.ushine.storesinfo.model.PersonStore;

/**
 * @author dh
 * 
 */
public class AccountsUtils {
	@Autowired  IBaseDao<CertificatesStore, Serializable> csDao;
	@Autowired  IBaseDao<NetworkAccountStore, Serializable> nasDao;
	
	@Deprecated
	public List<Class> getAccountsList(PersonStore personStore, IBaseDao baseDao, Class clazz) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
		// 用DetachedCriteria的关联查询就可
		criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", personStore.getId()));
		StringBuffer buffer = new StringBuffer();
		List<Class> list = baseDao.findByCriteria(criteria);
		
		return list;
	}
	
}
