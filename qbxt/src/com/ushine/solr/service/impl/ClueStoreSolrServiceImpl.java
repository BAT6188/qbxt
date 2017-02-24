package com.ushine.solr.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushine.dao.IBaseDao;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IClueStoreSolrService;
import com.ushine.solr.service.IPersonStoreSolrService;
import com.ushine.solr.solrbean.ClueStoreSolr;
import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.ClueStoreVo;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.service.IClueRelationshipService;

/**
 * 实现线索和人员的检索： 1条线索关联着n个人员；同理，1个人员关联着n个线索 查询时应同时检索线索库本身的关键字、检索人员库的关键字，获得了三个结果集合，
 * 线索库符合的、线索库不符合的、人员库符合的，对这三个集合做运算
 * 每个不符合的线索库的personId字段做集合，与人员符合的id集合做containAny运算，true则把该条线索加进
 * 总线索库-符合线索库=线索库不符合 线索库不符合与人员库符合的再做运算
 * 
 * springmvc的事务控制：
 * 首先，不应该在controller调用多个service而且还要要求一个事务；
 * 其次，service多次嵌套事务，spring不会产生多个事务，简单的说，service层调用service层，两个供用一个事务；
 * 不应该在controller调用多个service，写在一个service 里面 调用多个 dao
 * @author Administrator
 *
 */
@Service(value = "clueStoreSolrServiceImpl")
public class ClueStoreSolrServiceImpl implements IClueStoreSolrService {
	@Autowired
	IClueRelationshipService relationshipService;
	@Autowired
	IPersonStoreSolrService personStoreSolrService;
	@Autowired
	IBaseDao<ClueStore, Serializable> baseDao;
	protected HttpSolrServer server = SolrServerFactory.getCSSolrServerInstance();
	Logger logger = Logger.getLogger(ClueStoreSolrServiceImpl.class);

	@Override
	public void addDocumentByStore(ClueStore daoStore) {
		List<ClueStore> list = new ArrayList<>();
		list.add(daoStore);
		addDocumentByStores(list);
	}

	@Override
	public void addDocumentByStores(List<ClueStore> list) {
		try {
			List<ClueStoreSolr> solrList = convertToSolrBeanList(list);
			server.addBeans(solrList);
			// 提交
			server.commit();
			logger.info(String.format("新增索引集合%s条完成", solrList.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增索引集合异常：" + e.getMessage());
		}
	}

	@Override
	public void createNewIndexByStores() {
		List<ClueStore> daoList = new ArrayList<>();
		try {
			deleteAll();
			// 获得数据库里没被删除的
			daoList = baseDao.findByHql(String.format("from %s where action<>3", "ClueStore"));
			List<ClueStoreSolr> solrList = convertToSolrBeanList(daoList);
			server.addBeans(solrList);
			server.commit();
			logger.info(String.format("创建新的索引集合%s条完成", solrList.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建新索引库异常：" + e.getMessage());
		}
	}

	@Override
	public void deleteDocumentById(String id) {
		deleteDocumentByIds(new String[] { id });
	}

	@Override
	public void deleteDocumentByIds(String[] ids) {
		List<String> list = new ArrayList<>();
		try {
			CollectionUtils.addAll(list, ids);
			server.deleteById(list);
			server.commit();
			logger.info(String.format("删除索引集合%s条完成", list.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除索引异常：" + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		try {
			// 清空
			server.deleteByQuery(QueryBean.CLUESTORE_ID + ":*");
			server.commit();
			logger.info("清空原索引库成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDocumentByStore(String id, ClueStore daoStore) {
		List<ClueStore> daoList = new ArrayList<ClueStore>();
		try {
			daoStore.setId(id);
			daoList.add(daoStore);
			// 转
			server.addBeans(convertToSolrBeanList(daoList));
			server.commit();
			logger.info(String.format("更新id为：%s索引成功", id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("更新id为：%s索引异常：%s", id, e.getMessage()));
		}
	}

	@Override
	public long getDocumentsCount(QueryBean queryBean) {
		return getFinalResultList(queryBean).size();
	}

	@Override
	public void closeServer() {
		try {
			if (null != server) {
				server.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("关闭server异常：" + e.getMessage());
		}
	}

	@Override
	public List<ClueStoreVo> getDocuementsVo(QueryBean bean, int start, int rows) {
		// 分页
		List<ClueStoreSolr> solrList = getFinalResultList(bean);
		// 分页结果
		List<ClueStoreSolr> pageSolrList = new ArrayList<>();
		long totalCount = solrList.size();
		// 从start开始，获得rows条数据
		for (int i = start; i < (start + rows); i++) {
			if (totalCount <= i) {
				break;
			}
			pageSolrList.add(solrList.get(i));
		}
		List<ClueStoreVo> vo = new ArrayList<>();
		vo = convertToClueStoreVoList(pageSolrList);
		return vo;
	}

	@Override
	public List<ClueStoreSolr> convertToSolrBeanList(List<ClueStore> list) {
		List<ClueStoreSolr> solrList = new ArrayList<>();
		try {
			for (ClueStore clueStore : list) {
				ClueStoreSolr targetBean = new ClueStoreSolr();
				targetBean = (ClueStoreSolr) SolrBeanUtils.convertBeanToAnotherBean(clueStore, targetBean, QueryBean.ID,
						QueryBean.CLUESTORE_ID);
				// 获得关联的人员的id
				String personId = relationshipService.findStoreIdByClueId(clueStore.getId(),
						IClueRelationshipService.PERSONSTORE_TYPE);
				logger.error("转到solrBean时personId："+personId);
				// 防止null
				targetBean.setPersonId(ObjectUtils.defaultIfNull(personId, "").toString());
				// 添加集合
				solrList.add(targetBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转ClueStoreSolr异常：" + e.getMessage());
		}
		return solrList;
	}

	@Override
	public List<ClueStoreVo> convertToClueStoreVoList(List<ClueStoreSolr> list) {
		List<ClueStoreVo> voList = new ArrayList<>();
		try {
			for (ClueStoreSolr solrBean : list) {
				ClueStoreVo vo = new ClueStoreVo();
				vo = (ClueStoreVo) SolrBeanUtils.convertBeanToAnotherBean(solrBean, vo, QueryBean.CLUESTORE_ID,
						QueryBean.ID);
				voList.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转LeadSpeakStoreVo异常：" + e.getMessage());
		}
		return voList;
	}

	/**
	 * 获得全部符合查询条件的人员
	 * 
	 * @param bean
	 *            查询条件
	 * @return List<String> 人员的id集合
	 */
	protected List<String> getPersonStoreMatch(QueryBean bean) {
		List<String> personIdList = new ArrayList<>();
		// 查询全部符合的人员
		HttpSolrServer personServer = SolrServerFactory.getPSSolrServerInstance();
		bean.setQueryField(null);
		List<PersonStoreVo> list = personStoreSolrService.getDocuementsVO(personServer, bean, 0, Integer.MAX_VALUE);
		logger.info("人员中符合该查询条件的数量："+list.size());
		for (PersonStoreVo vo : list) {
			personIdList.add(vo.getId());
		}
		return personIdList;
	}

	/**
	 * 获得线索本身符合条件的全部集合
	 * 
	 * @param bean
	 *            查询条件
	 * @return List<ClueStoreSolr> ClueStoreSolr集合
	 */
	protected List<ClueStoreSolr> getClueStoreMatch(QueryBean bean) {
		List<ClueStoreSolr> solrList = new ArrayList<ClueStoreSolr>();
		try {
			solrList = getSolrList(bean);
			logger.info("获得线索本身符合条件的数量："+solrList.size());
		} catch (Exception e) {
			logger.error("查询数量失败："+e.getMessage());
			e.printStackTrace();
		}
		return solrList;
	}

	/**
	 * 获得符合条件的所有ClueStoreSolr
	 * 
	 * @param bean
	 *            查询条件
	 * @return List ClueStoreSolr集合
	 * @throws SolrServerException
	 */
	private List<ClueStoreSolr> getSolrList(QueryBean bean) throws SolrServerException {
		List<ClueStoreSolr> solrList;
		SolrQuery query = bean.getSolrQuery(ClueStore.class);
		// 获得线索本身符合条件的全部集合
		query.setStart(0).setRows(Integer.MAX_VALUE);
		QueryResponse response = server.query(query);
		// 转成solr bean对象ClueStoreSolr
		SolrDocumentList sdList = response.getResults();
		DocumentObjectBinder binder = new DocumentObjectBinder();
		solrList = binder.getBeans(ClueStoreSolr.class, sdList);
		return solrList;
	}

	/**
	 * 获得不符合查询条件的所有的ClueStoreSolr
	 * 
	 * @param bean
	 *            查询语句，只设置日期
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<ClueStoreSolr> getClueStoreNotMatch(QueryBean bean) {
		List<ClueStoreSolr> notMatchList = new ArrayList<ClueStoreSolr>();
		try {
			QueryBean totalQueryBean = new QueryBean();
			// 其他条件为null
			// totalQueryBean.setStartDate("1970-01-01");
			// String endDate = DateFormatUtils.format(new Date(),
			// "yyyy-MM-dd");
			totalQueryBean.setStartDate(bean.getStartDate());
			totalQueryBean.setEndDate(bean.getEndDate());
			// 获得日期内所有的
			List<ClueStoreSolr> totalList = getSolrList(totalQueryBean);
			logger.info("总数："+totalList);
			List<ClueStoreSolr> matchList=getClueStoreMatch(bean);
			// 减法运算
			if (totalList.size()!=matchList.size()) {
				notMatchList = (List<ClueStoreSolr>) CollectionUtils.subtract(totalList, matchList);
			}
			logger.info("获得不符合条件的线索库数量："+notMatchList.size());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获得不符合条件的线索库异常：" + e.getMessage());
		}
		return notMatchList;
	}

	/**
	 * 对线索库不符合条件的集合与人员符合的id集合做运算
	 * 
	 * @param clueStoreSolrList
	 *            不符合的线索库集合
	 * @param personIdList
	 *            符合条件的人员的id集合
	 * @return 包含符合人员的线索库集合
	 */
	protected List<ClueStoreSolr> getContainsAnyList(List<ClueStoreSolr> clueStoreSolrList, List<String> personIdList) {
		List<ClueStoreSolr> tempResultList = new ArrayList<>();
		for (ClueStoreSolr clueStoreSolr : clueStoreSolrList) {
			// 获得每一条集合的人员id集合
			List<String> personIdListFromClue = getPersonIdListFromClueStoreSolr(clueStoreSolr);
			// 两者之间有交集
			if (CollectionUtils.containsAny(personIdListFromClue, personIdList)) {
				tempResultList.add(clueStoreSolr);
			}
		}
		//
		logger.info("获得包含符合人员的线索库数量："+tempResultList.size());
		return tempResultList;
	}

	/**
	 * 从一个ClueStoreSolr获得包含的人员的id集合 一个ClueStoreSolr可能有多个关联的人员
	 * 
	 * @param clueStoreSolr
	 *            ClueStoreSolr对象
	 * @return List<String> 关联的人员id集合
	 */
	protected List<String> getPersonIdListFromClueStoreSolr(ClueStoreSolr clueStoreSolr) {
		String id = clueStoreSolr.getPersonId();
		List<String> personIdList = new ArrayList<>();
		// 用逗号分隔
		if (StringUtils.contains(id, ",")) {
			String[] ids = id.split(",");
			for (String string : ids) {
				personIdList.add(string.trim());
			}
		}
		return personIdList;
	}

	/**
	 * 获得最终符合查询条件的线索
	 * 
	 * @param bean
	 *            查询条件
	 * @return ClueStoreSolr集合
	 */
	@SuppressWarnings("unchecked")
	protected List<ClueStoreSolr> getFinalResultList(QueryBean bean) {
		List<ClueStoreSolr> finalResultList = new ArrayList<>();
		try {
			// 先获得线索本身符合的
			List<ClueStoreSolr> firstList = getClueStoreMatch(bean);
			// 获得人员符合的
			List<String> personMatchIdList = getPersonStoreMatch(bean);
			// 再进行线索、人员运算
			List<ClueStoreSolr> secondList = getContainsAnyList(getClueStoreNotMatch(bean), personMatchIdList);
			finalResultList = ListUtils.sum(firstList, secondList);
			logger.info("最终符合条件线索库数量："+finalResultList.size());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获得最终符合条件的线索结果异常：" + e.getMessage());
		}
		return finalResultList;
	}
}
