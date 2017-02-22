package com.ushine.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ushine.common.utils.HibernateUtils;
import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;

/**
 * HibernateDao方法实现，该实现中不使用Spring管理Session
 * @author franklin
 *
 * @param <T>
 */
public class HibernateDaoImpl<T> implements IBaseDao<T, Serializable> {
	private static final Logger logger = LoggerFactory.getLogger(HibernateDaoImpl.class);
	
	public void save(T entityObject) throws Exception {
		logger.debug("[Hibernate]保存数据到数据库(" + entityObject.getClass() + ").");
		Session session =  null;
		try {
			session =  HibernateUtils.getSession();
			session.beginTransaction();
			
			session.save(entityObject);
			
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			/* 异常时进行事务回滚操作 */
			session.getTransaction().begin();
		} finally {
			/* 最后关闭Hibernate Session */
			HibernateUtils.closeSession();
		}
	}

	public void update(T entityObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void saveOrUpdate(T entityObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void delete(T entityObject) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void deleteById(Class<T> entityClass, Serializable[] ids)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void deleteByProperty(Class<T> entityClass, String propertyName,
			Object value) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void deleteAll(Class<T> entityClass) throws Exception {
		logger.debug("[Hibernate]查询全部数据(" + entityClass + ").");
		Session session =  null;
		try {
			session =  HibernateUtils.getSession();
			session.beginTransaction();
			
			String hqlDelete = "delete from " + entityClass.getName();
			Query query =  session.createQuery(hqlDelete);
			
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			/* 异常时进行事务回滚操作 */
			session.getTransaction().begin();
		} finally {
			/* 最后关闭Hibernate Session */
			HibernateUtils.closeSession();
		}
	}

	public T findById(Class<T> entityClass, Serializable id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> findByProperty(Class<T> entityClass, String propertyName,
			Object value) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> findByCriteria(DetachedCriteria criteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> findAll(Class<T> entityClass) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> findByHql(String hql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	@SuppressWarnings("unchecked")
	public List<T> findPaging(Class<T> entityClass, int sizePage,
			int startRecord) throws Exception {
		logger.debug("[Hibernate]分页查询(" + entityClass + ")，每页(" + sizePage + ")记录，起始记录(" + startRecord + ").");
		
		List<T> datas = new ArrayList<T>();
		Session session =  null;
		
		try {
			session =  HibernateUtils.getSession();
			// session.beginTransaction();
			
			String hqlFind = "from " + entityClass.getName();
			Query query =  session.createQuery(hqlFind);
			query.setFirstResult(startRecord); // 查询的开始位置
			query.setMaxResults(sizePage); // ，每页显示多少条数据
			datas = (List<T>) query.list(); 
			
			// session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			/* 异常时进行事务回滚操作 */
			// session.getTransaction().begin();
		} finally {
			/* 最后关闭Hibernate Session */
			HibernateUtils.closeSession();
		}
		return datas;
	}

	@SuppressWarnings("unchecked")
	public List<T> findPagingByCriteria(DetachedCriteria criteria,
			int sizePage, int startRecord) throws Exception {
		logger.debug("DetachedCriteria分页查询，每页(" + sizePage + ")记录，起始记录(" + startRecord + ").");
		
		List<T> datas = new ArrayList<T>();
		Session session =  null;
		
		try {
			session =  HibernateUtils.getSession();
			
			//****************************************************
			//*DetachedCriteria类使你在一个session范围之外创建
			//*一个查询，并且可以使用任意的 Session来执行它
			//****************************************************
			Criteria query = criteria.getExecutableCriteria(session);
			query.setFirstResult(startRecord);
			query.setMaxResults(sizePage);
			
			datas = (List<T>) query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/* 最后关闭Hibernate Session */
			HibernateUtils.closeSession();
		}
		return datas;
	}

	public List<T> findPagingByHql(String hql, int sizePage, int startRecord)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRowCount(Class<T> entityClass) throws Exception {
		Session session =  null;
		int size = 0;
		
		try {
			session =  HibernateUtils.getSession();
			Criteria criteria = session.createCriteria(entityClass);
			criteria.setProjection(Projections.rowCount());
			
			size = (Integer) criteria.list().get(0);
			logger.debug("对(" + entityClass + ")进行Count操作，共有(" + size + ")记录.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/* 最后关闭Hibernate Session */
			HibernateUtils.closeSession();
		}
		return size;
	}

	public int getRowCount(DetachedCriteria criteria) throws Exception {
		Session session =  null;
		int size = 0;
		
		try {
			session =  HibernateUtils.getSession();
			
			//****************************************************
			//*DetachedCriteria类使你在一个session范围之外创建
			//*一个查询，并且可以使用任意的 Session来执行它
			//****************************************************
			criteria.setProjection(Projections.rowCount());
			Criteria query = criteria.getExecutableCriteria(session);
			
			size = (Integer) query.list().get(0);
			logger.debug("(DetachedCriteria条件)Count操作，共有(" + size + ")记录.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/* 最后关闭Hibernate Session */
			HibernateUtils.closeSession();
		}
		return size;
	}

	public Object executeHql(String hql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object executeSql(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteById(Class<T> entityClass, String id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ushine.dao.IBaseDao#findBySql(java.lang.String)
	 * 下午3:37:20
	 */
	public List findBySql(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getRows(String sql) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List findBySqlAnPersonStore(String sql, Class<PersonStore> class1,
			int sizePage, int startRecord) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*public List findBySqlAnOrganizStore(String sql, Class<OrganizStore> class1,
			int sizePage, int startRecord) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}*/

	public List findBySqlAnWebsiteJournalStore(String sql,
			Class<WebsiteJournalStore> class1, int sizePage, int startRecord)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(List<T> entityObjectList) throws Exception {
		//保存多条数据
		
	}

}
