package com.ushine.solr.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushine.dao.IBaseDao;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IOutsideDocStoreSolrService;
import com.ushine.solr.solrbean.OutsideDocStoreSolr;
import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.OutsideDocStoreVo;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.OutsideDocStore;
import com.ushine.storesinfo.model.PersonStore;

/**
 * 
 * @author dh
 * 
 */
@Service(value = "outsideDocStoreSolrServiceImpl")
public class OutsideDocStoreSolrServiceImpl implements IOutsideDocStoreSolrService {

	protected HttpSolrServer server = SolrServerFactory.getODSSolrServerInstance();
	Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	IBaseDao<OutsideDocStore, Serializable> baseDao;

	@Override
	public void addDocumentByStore(OutsideDocStore daoStore) {
		try {
			OutsideDocStoreSolr bean = convertToSolrBean(daoStore);
			server.addBean(bean);
			server.commit();
			logger.info("添加索引成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加索引异常：" + e.getMessage());
		}
	}

	@Override
	public void addDocumentByStores(List<OutsideDocStore> daoStore) {
		try {
			List<OutsideDocStoreSolr> list=convertToSolrBeanList(daoStore);
			server.addBeans(daoStore);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增索引集合失败："+e.getMessage());
		}
	}

	@Override
	public void createNewIndexByStores() {
		try {
			// 清空
			deleteAll();
			// 查询数据库中没有被删除的
			List<OutsideDocStore> list = baseDao.findByHql(String.format("from %s where action<>3", "OutsideDocStore"));
			List<OutsideDocStoreSolr> solrList = convertToSolrBeanList(list);
			server.addBeans(solrList);
			server.commit();
			logger.info(String.format("添加%s索引成功", list.size()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建新solr索引库异常：" + e.getMessage());
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
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除索引异常：" + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		try {
			server.deleteByQuery(QueryBean.OUTSIDEDOC_ID + ":*");
			server.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDocumentByStore(String id, OutsideDocStore daoStore) {
		try {
			daoStore.setId(id);
			// 转换
			OutsideDocStoreSolr bean = convertToSolrBean(daoStore);
			server.addBean(bean);
			server.commit();
			logger.info(String.format("更新id为：%s索引成功", id));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("更新id为：%s索引异常：%s", id, e.getMessage()));
		}
	}

	@Override
	public long getDocumentsCount(QueryBean queryBean) {
		try {
			QueryResponse response = server.query(queryBean.getSolrQuery(OutsideDocStore.class));
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
	public List<OutsideDocStoreVo> getDocuementsVo(QueryBean bean, int start, int rows) {
		List<OutsideDocStoreVo> psvList=new ArrayList<>();
		try {
			SolrQuery query=bean.getSolrQuery(OutsideDocStore.class);
			//分页
			query.setStart(start).setRows(rows);
			
			QueryResponse response = server.query(query);
			//转成solr bean对象OutsideDocStoreSolr
			SolrDocumentList sdList = response.getResults();
			DocumentObjectBinder binder=new DocumentObjectBinder();
			List<OutsideDocStoreSolr> beans = binder.getBeans(OutsideDocStoreSolr.class, sdList);
			//转成vo层对象
			psvList=convertToOutsideDocStoreVoList(beans);
		} catch (Exception e) {
			logger.error("查询数量失败");
			e.printStackTrace();
		}
		return psvList;
	}

	@Override
	public OutsideDocStoreSolr convertToSolrBean(OutsideDocStore store) {
		OutsideDocStoreSolr solrBean = new OutsideDocStoreSolr();
		try {
			solrBean = (OutsideDocStoreSolr) SolrBeanUtils.convertBeanToAnotherBean(store, solrBean, QueryBean.ID, QueryBean.OUTSIDEDOC_ID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转换为OutsideDocStoreSolr对象异常：" + e.getMessage());
		}
		return solrBean;
	}

	@Override
	public OutsideDocStoreVo convertToOutsideDocStoreVo(OutsideDocStoreSolr solrBean) {
		OutsideDocStoreVo vo = new OutsideDocStoreVo();
		try {
			vo = (OutsideDocStoreVo) SolrBeanUtils.convertBeanToAnotherBean(solrBean, vo, QueryBean.OUTSIDEDOC_ID, QueryBean.ID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("转换为OutsideDocStoreVo对象异常：" + e.getMessage());
		}
		return vo;
	}

	@Override
	public List<OutsideDocStoreSolr> convertToSolrBeanList(List<OutsideDocStore> list) {
		List<OutsideDocStoreSolr> solrList = new ArrayList<>();
		for (OutsideDocStore outsideDocStore : list) {
			OutsideDocStoreSolr bean = convertToSolrBean(outsideDocStore);
			// 加入集合
			solrList.add(bean);
		}
		return solrList;
	}

	@Override
	public List<OutsideDocStoreVo> convertToOutsideDocStoreVoList(List<OutsideDocStoreSolr> list) {
		List<OutsideDocStoreVo> voList = new ArrayList<>();
		for (OutsideDocStoreSolr outsideDocStoreSolr : list) {
			OutsideDocStoreVo vo = convertToOutsideDocStoreVo(outsideDocStoreSolr);
			// 添加
			voList.add(vo);
		}
		return voList;
	}

}
