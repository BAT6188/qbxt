package com.ushine.storesinfo.util;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.PersonStore;

/**
 * 人员账号Set集合输出的应用类 //证件
 * for (CertificatesStore certificatesStore : list2) {
 * number2.append(certificatesStore.getCertificatesNumber()+","); }
 * 
 * @author dh
 * 
 */
public class AccountsUtils {
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
