package com.ushine.storeInfo.service.impl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ushine.common.vo.PagingObject;
import com.ushine.dao.IBaseDao;
import com.ushine.store.index.LeadSpeakStoreNRTSearch;
import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.Attaches;
import com.ushine.storeInfo.model.LeadSpeakStore;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.service.ILeadSpeakStoreService;
import com.ushine.util.ReadAttachUtil;
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
	private static final Logger logger = LoggerFactory
			.getLogger(LeadSpeakStoreServiceImpl.class);
	@Autowired
	private IBaseDao<LeadSpeakStore, String> baseDao;
	@Autowired
	private IBaseDao<Attaches, String> baseDaoAttaches;
	//
	private LeadSpeakStoreNRTSearch leadSpeakStoreNRTSearch=LeadSpeakStoreNRTSearch.getInstance();
	public void saveLeadSpeakStore(LeadSpeakStore leadSpeakStore)
			throws Exception {
		// 新增
		try {
			//logger.info("新增LeadSpeakStore对象:" + leadSpeakStore.toString());
			baseDao.save(leadSpeakStore);
			//添加索引
			leadSpeakStoreNRTSearch.addIndex(leadSpeakStore);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增异常信息:" + e.getMessage());
		}
	}
	public LeadSpeakStore findLeadSpeakStoreById(String leadSpeakStoreId)
			throws Exception {
		// 查询
		try {
			LeadSpeakStore leadSpeakStore = baseDao.findById(
					LeadSpeakStore.class, leadSpeakStoreId);
			logger.info("查询LeadSpeakStore对象:" + leadSpeakStore.toString());
			return leadSpeakStore;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询异常信息:" + e.getMessage());
			return null;
		}
	}
	public void updateLeadSpeakStore(LeadSpeakStore leadSpeakStore)
			throws Exception {
		// 更新
		try {
			baseDao.update(leadSpeakStore);
			logger.info("更新LeadSpeakStore对象:" + leadSpeakStore.toString());
			leadSpeakStoreNRTSearch.updateIndex(leadSpeakStore.getId(), leadSpeakStore);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新异常信息:" + e.getMessage());
		}
	}
	public void delLeadSpeakStore(String[] leadSpeakStoreIds) throws Exception {
		// 删除
		try {
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
			//删除多条
			leadSpeakStoreNRTSearch.deleteIndex(leadSpeakStoreIds);
			logger.info("删除LeadSpeakStore对象id:"+ Arrays.toString(leadSpeakStoreIds));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除异常信息:" + e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public String findLeadSpeakStore(String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size,
			String uid, String oid, String did) throws Exception {
		logger.debug("筛选信息,field:" + field + ",fieldValue:" + fieldValue
				+ ",startTime:" + startTime + "endTime:" + endTime + "");
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public String findLeadSpeakStore(String field, String fieldValue, String startTime, String endTime, int nextPage,
			int size, String uid, String oid, String did, String sortField,String dir) throws Exception {
		//查询数据并排序
		PagingObject<LeadSpeakStore> vo=StoreIndexQuery.findStore(field, fieldValue, 
				startTime, endTime, nextPage, size, 
				uid, oid, did,sortField, dir,LeadSpeakStore.class);
		boolean hasValue=false;
		if (StringUtil.isEmty(fieldValue)){
			hasValue=false;
		}else{
			//输入了值才高亮
			hasValue=true;
		}
		return StoreIndexQuery.leadSpeakStoreVoToJson(vo,hasValue);
	}
	// 多条件查询
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size,
			String uid, String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(LeadSpeakStore.class);
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)
				&& !field.equals("infoType")) {
			// 类别不能直接设置值
			criteria.add(Restrictions.like(field, "%" + fieldValue + "%"));
		}
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)
				&& field.equals("infoType")) {
			// 如果是类别
			// 使用createAlias来创建属性别名,然后引用别名进行条件查询
			criteria.createAlias("infoType", "i").add(
					Restrictions.like("i.typeName", "%" + fieldValue + "%"));
		}
		if (!StringUtil.isNull(startTime) && startTime.length() >= 10) {
			startTime = startTime.substring(0, 10) + " 00:00:00";
			criteria.add(Restrictions.ge("createDate", startTime));
		}
		if (!StringUtil.isNull(endTime) && endTime.length() >= 10) {
			endTime = endTime.substring(0, 10) + " 23:59:59";
			criteria.add(Restrictions.le("createDate", endTime));
		}
		if (!StringUtil.isNull(uid)) {
			criteria.add(Restrictions.eq("uid", uid));
		}
		if (!StringUtil.isNull(oid)) {
			criteria.add(Restrictions.eq("oid", oid));
		}
		if (!StringUtil.isNull(did)) {
			criteria.add(Restrictions.eq("did", did));
		}
		// 查询时ACTION为1或者为2
		// 不等于3,not equals
		criteria.add(Restrictions.ne("action", "3"));
		return criteria;
	}
	/**
	 * 领导讲话集合转成json
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
			if(store.getInvolvedInTheField()!=null){
				//不为空
				obj.put("involvedInTheField", store.getInvolvedInTheField().getTypeName());
			}
			//类别不为空
			if(store.getInfoType()!=null){
				obj.put("infoType", store.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	@Override
	public boolean hasStoreByMeetingName(String meetingName) {
		//判断是否存在
		try {
			String hql=String.format("select id from LeadSpeakStore where "
					+ "meetingName='%s' and action<>'3'", meetingName);
			if(baseDao.findByHql(hql).size()>0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
