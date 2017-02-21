package com.ushine.solr.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushine.dao.IBaseDao;
import com.ushine.solr.service.IPersonStoreSolrService;
import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.NetworkAccountStore;
import com.ushine.storesinfo.model.PersonStore;
/**
 * 
 * @author Administrator
 *
 */
@Service(value = "personStoreSolrServiceImpl")
public class PersonStoreSolrServiceImpl implements IPersonStoreSolrService {

	private Logger logger = Logger.getLogger(PersonStoreSolrServiceImpl.class);
	@Autowired private IBaseDao<PersonStore, Serializable> baseDao;
	@Autowired private IBaseDao<CertificatesStore, Serializable> csDao;
	@Autowired private IBaseDao<NetworkAccountStore, Serializable> nasDao;
	
	/**
	 * solr对象转成vo
	 * @param psSolr
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	protected PersonStoreVo convertPersonStoreSolrToVo(PersonStoreSolr psSolr) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, NoSuchFieldException, SecurityException {
		PersonStoreVo vo = new PersonStoreVo();
		vo=(PersonStoreVo) SolrBeanUtils.convertBeanToAnotherBean(psSolr, vo, QueryBean.PERSON_ID, QueryBean.ID);
		return vo;
	}
	
	
	/**
	 * 将dao层的PersonStore的bean转换为Solr中Bean
	 * @param ps PersonStore dao层的PersonStore的bean
	 * @return PersonStoreSolr Solr中Bean
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	protected PersonStoreSolr convertPersonStoreToSolrBean(PersonStore ps) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException,
			SecurityException, InstantiationException {
		PersonStoreSolr psSolr = new PersonStoreSolr();
		psSolr=(PersonStoreSolr) SolrBeanUtils.convertBeanToAnotherBean(ps, psSolr, QueryBean.ID, QueryBean.PERSON_ID);
		// 复制身份账号集合
		Set<CertificatesStore> certificatesStores = ps.getCertificatesStores();
		StringBuffer csBuffer = new StringBuffer();
		if (null != certificatesStores && certificatesStores.size() > 0) {
			for (CertificatesStore store : certificatesStores) {
				csBuffer.append(SolrBeanUtils.getStringValue(store.getCertificatesNumber()) + ",");
			}
		}
		BeanUtils.copyProperty(psSolr, "certificatesStores", csBuffer.toString());
		// 复制网络账号集合
		Set<NetworkAccountStore> networkAccountStores = ps.getNetworkAccountStores();
		StringBuffer nasBuffer = new StringBuffer();
		if (null != networkAccountStores && networkAccountStores.size() > 0) {
			for (NetworkAccountStore store : networkAccountStores) {
				nasBuffer.append(SolrBeanUtils.getStringValue(store.getNetworkNumber()) + ",");
			}
		}
		BeanUtils.copyProperty(psSolr, "networkAccountStores", nasBuffer.toString());
		return psSolr;
	}
	/**
	 * 添加证件结合
	 * @param personStore
	 */
	private void setCertificatesStore(PersonStore personStore) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CertificatesStore.class);
		// 用DetachedCriteria的关联查询就可
		criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", personStore.getId()));
		List<CertificatesStore> list;
		try {
			list = csDao.findByCriteria(criteria);
			Set<CertificatesStore> cs = new HashSet<>();
			for (CertificatesStore certificatesStore : list) {
				cs.add(certificatesStore);
			}
			personStore.setCertificatesStores(cs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 设置网络账号集合
	 * @param personStore
	 */
	private void setNetworkAccountStore(PersonStore personStore){
		DetachedCriteria criteria = DetachedCriteria.forClass(NetworkAccountStore.class);
		// 用DetachedCriteria的关联查询就可
		criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", personStore.getId()));
		List<NetworkAccountStore> list;
		try {
			list = nasDao.findByCriteria(criteria);
			Set<NetworkAccountStore> cs = new HashSet<>();
			for (NetworkAccountStore networkAccountStore : list) {
				cs.add(networkAccountStore);
			}
			personStore.setNetworkAccountStores(cs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 给store添加集合的属性，防止懒加载的出现
	 * @param personStore
	 */
	private void addSetProperty(PersonStore personStore){
		this.setCertificatesStore(personStore);
		this.setNetworkAccountStore(personStore);
	}

	@Override
	public int addDocumentByStore(HttpSolrServer server, PersonStore daoStore) {
		try {
			addSetProperty(daoStore);
			PersonStoreSolr bean = convertPersonStoreToSolrBean(daoStore);
			server.addBean(bean);
			server.commit();
			logger.info("添加solr索引成功");
			return 0;
		} catch (Exception e) {
			logger.error("添加solr索引失败");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int addDocumentByStores(HttpSolrServer server, List<PersonStore> daoStore) {
		//出现了懒加载
		List<PersonStoreSolr> pStoreSolrs = new ArrayList<>();
		try {
			for (PersonStore personStore : daoStore) {
				addSetProperty(personStore);
				PersonStoreSolr solrBean = convertPersonStoreToSolrBean(personStore);
				// 添加到集合中
				pStoreSolrs.add(solrBean);
			}
			// 添加solr索引
			server.addBeans(pStoreSolrs);
			server.commit();
			logger.info("添加" + daoStore.size() + "条solr索引成功");
			return daoStore.size();
		} catch (Exception e) {
			logger.error("添加solr索引失败");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public void createNewIndexByStores(HttpSolrServer server) {
		try {
			// 清空原库
			deleteAll(server);
			// 添加数据库里没有被删除的对象
			List<PersonStore> list = baseDao.findByHql(String.format("from %s where action<>3", "PersonStore"));
			logger.info("正在添加" + list.size() + "条索引");
			addDocumentByStores(server, list);
			logger.info("创建索引库完成");
		} catch (Exception e) {
			logger.error("创建新的solr索引库失败");
			e.printStackTrace();
		}
	}

	@Override
	public void deleteDocumentById(HttpSolrServer server, String id) {
		deleteDocumentByIds(server, new String[] { id });
	}

	@Override
	public void deleteDocumentByIds(HttpSolrServer server, String[] ids) {
		List<String> list = new ArrayList<>();
		for (String id : ids) {
			list.add(id);
		}
		try {
			server.deleteById(list);
			server.commit();
			logger.info(String.format("删除 %s 条索引成功", ids.length));
		} catch (SolrServerException | IOException e) {
			logger.info(String.format("删除 %s 条索引失败", ids.length));
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAll(HttpSolrServer server) {
		try {
			server.deleteByQuery(QueryBean.PERSON_ID + ":*");
			server.commit();
			logger.info("清空solr索引库成功");
		} catch (SolrServerException | IOException e) {
			logger.error("清空solr索引库失败");
			e.printStackTrace();
		}
	}

	@Override
	public void updateDocumentByStore(HttpSolrServer server, String id, PersonStore daoStore) {
		try {
			daoStore.setId(id);
			this.addSetProperty(daoStore);
			PersonStoreSolr solrBean = convertPersonStoreToSolrBean(daoStore);
			server.addBean(solrBean);
			server.commit();
			logger.info(String.format("更新id为：%s的索引成功", id));
		} catch (Exception e) {
			logger.error(String.format("更新id为：%s的索引失败", id));
			e.printStackTrace();
		}
	}

	@Override
	public long getDocumentsCount(HttpSolrServer server, QueryBean queryBean) {
		try {
			QueryResponse response = server.query(queryBean.getSolrQuery(PersonStore.class));
			long result=response.getResults().getNumFound();
			logger.info("查询符合条件的数量为："+result);
			return result;
		} catch (Exception e) {
			logger.error("查询数量失败");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public List<PersonStoreVo> getDocuementsVO(HttpSolrServer server,QueryBean bean, int start, int rows) {
		List<PersonStoreVo> psvList=new ArrayList<>();
		try {
			SolrQuery query=bean.getSolrQuery(PersonStore.class);
			//分页
			query.setStart(start).setRows(rows);
			/*//高亮
			query.setHighlight(true);
			// 高亮字段  
			query.addHighlightField(QueryBean.PERSONSTOREALL);
			//前缀 后缀
			query.setHighlightSimplePre(QueryBean.HIGHLIGHT_PRE);
			query.setHighlightSimplePost(QueryBean.HIGHLIGHT_POST);*/
			
			QueryResponse response = server.query(query);
			//转成solr bean对象PersonStoreSolr
			SolrDocumentList sdList = response.getResults();
			DocumentObjectBinder binder=new DocumentObjectBinder();
			List<PersonStoreSolr> beans = binder.getBeans(PersonStoreSolr.class, sdList);
			//转成vo层对象
			for (PersonStoreSolr personStoreSolr : beans) {
				PersonStoreVo vo = convertPersonStoreSolrToVo(personStoreSolr);
				psvList.add(vo);
			}
		} catch (Exception e) {
			logger.error("查询数量失败");
			e.printStackTrace();
		}
		return psvList;
	}

	@Override
	public void closeServer(HttpSolrServer server) {
		//在程序关闭时执行
		if (null!=server) {
			server.shutdown();
		}
	}
}
