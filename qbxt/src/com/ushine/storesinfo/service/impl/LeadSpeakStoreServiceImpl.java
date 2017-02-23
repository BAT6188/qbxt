package com.ushine.storesinfo.service.impl;

import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.LeadSpeakStoreNRTSearch;
import com.ushine.luceneindex.index.StoreIndexQuery;
import com.ushine.solr.service.ILeadSpeakStoreSolrService;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.MyJSonUtils;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.LeadSpeakStoreVo;
import com.ushine.solr.vo.OutsideDocStoreVo;
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.service.ILeadSpeakStoreService;
import com.ushine.util.StringUtil;

/**
 * 领导讲话接口类
 * 
 * @author wangbailin
 * 
 */
@Transactional
@Service("leadSpeakStoreServiceImpl")
public class LeadSpeakStoreServiceImpl implements ILeadSpeakStoreService {
	private static final Logger logger = LoggerFactory.getLogger(LeadSpeakStoreServiceImpl.class);
	@Autowired
	private IBaseDao<LeadSpeakStore, String> baseDao;
	@Autowired
	ILeadSpeakStoreSolrService solrService;

	public void saveLeadSpeakStore(LeadSpeakStore leadSpeakStore) throws Exception {
		// 新增
		baseDao.save(leadSpeakStore);
		// 添加索引
		solrService.addDocumentByStore(leadSpeakStore);
	}

	public LeadSpeakStore findLeadSpeakStoreById(String leadSpeakStoreId) throws Exception {
		// 查询
		try {
			LeadSpeakStore leadSpeakStore = baseDao.findById(LeadSpeakStore.class, leadSpeakStoreId);
			logger.info("查询LeadSpeakStore对象:" + leadSpeakStore.toString());
			return leadSpeakStore;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常信息:" + e.getMessage());
			return null;
		}
	}

	public void updateLeadSpeakStore(LeadSpeakStore leadSpeakStore) throws Exception {
		baseDao.update(leadSpeakStore);
		logger.info("更新LeadSpeakStore对象:" + leadSpeakStore.toString());
		// 更新
		solrService.updateDocumentByStore(leadSpeakStore.getId(), leadSpeakStore);
	}

	public void delLeadSpeakStore(String[] leadSpeakStoreIds) throws Exception {
		// 删除
		StringBuffer buffer = new StringBuffer("update LeadSpeakStore set action='3' where ");
		for (String string : leadSpeakStoreIds) {
			buffer.append(String.format("id='%s' or ", string));
		}
		String hql = buffer.toString().trim();
		// 去掉最后的or
		hql = StringUtils.substring(hql, 0, hql.length() - 2);
		logger.info("删除语句：" + hql);
		// 执行删除
		baseDao.executeHql(hql);
		solrService.deleteDocumentByIds(leadSpeakStoreIds);
		// 删除多条
		// logger.info("删除LeadSpeakStore对象id:" +
		// Arrays.toString(leadSpeakStoreIds));
	}

	@SuppressWarnings("unchecked")
	public String findLeadSpeakStore(String field, String fieldValue, String startTime, String endTime, int nextPage, int size, String uid, String oid, String did) throws Exception {
		logger.debug("筛选信息,field:" + field + ",fieldValue:" + fieldValue + ",startTime:" + startTime + "endTime:" + endTime + "");
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String findLeadSpeakStore(String field, String fieldValue, String startTime, String endTime, int nextPage, int size, String uid, String oid, String did, String sortField, String dir)
			throws Exception {
		// 利用索引查询
		if (StringUtils.equals(field, "anyField")) {
			//任意字段查询
			field=QueryBean.LEADSPEAKSTOREALL;
		}
		QueryBean queryBean=new QueryBean(uid, oid, did, field, fieldValue, null, null, sortField,dir, startTime, endTime);
		//查询总数
		long totalRecord = solrService.getDocumentsCount(queryBean);
		Paging paging = new Paging(size, nextPage, totalRecord);
		PagingObject<LeadSpeakStoreVo> vo = new PagingObject<>();
		vo.setPaging(paging);
		// 集合
		// nextPage从1开始
		List<LeadSpeakStoreVo> array = solrService.getDocuementsVo(queryBean, (nextPage - 1) * size, size);
		if (StringUtils.isNotBlank(fieldValue)) {
			// 有关键字要高亮
			List<LeadSpeakStoreVo> highlightArray = SolrBeanUtils.highlightVoList(array, LeadSpeakStoreVo.class, fieldValue);
			vo.setArray(highlightArray);
		} else {
			vo.setArray(array);
		}
		return MyJSonUtils.toJson(vo);
	}

	/**
	 * 领导讲话集合转成json
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String voToJson(PagingObject<LeadSpeakStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (LeadSpeakStore store : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", store.getId());
			obj.put("title", store.getTitle());
			obj.put("time", store.getTime());
			obj.put("meetingName", store.getMeetingName());
			obj.put("secretRank", store.getSecretRank());
			obj.put("centent", store.getCentent());
			obj.put("createDate", store.getCreateDate());
			if (store.getInvolvedInTheField() != null) {
				// 不为空
				obj.put("involvedInTheField", store.getInvolvedInTheField().getTypeName());
			}
			// 类别不为空
			if (store.getInfoType() != null) {
				obj.put("infoType", store.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}

	@Override
	public boolean hasStoreByMeetingName(String meetingName) {
		// 判断是否存在
		try {
			String hql = String.format("select id from LeadSpeakStore where " + "meetingName='%s' and action<>'3'", meetingName);
			if (baseDao.findByHql(hql).size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
