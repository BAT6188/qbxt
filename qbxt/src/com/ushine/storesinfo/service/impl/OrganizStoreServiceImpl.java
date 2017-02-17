package com.ushine.storesinfo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.ParagraphProperties;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.common.utils.PathUtils;
import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.OrganizStoreNRTSearch;
import com.ushine.luceneindex.index.StoreIndexQuery;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.OrganizStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.IOrganizStoreService;
import com.ushine.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 组织基础库接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("organizStoreServiceImpl")
public class OrganizStoreServiceImpl implements IOrganizStoreService{
	private static final Logger logger = LoggerFactory.getLogger(InfoTypeServiceImpl.class);
	@Autowired
	private IBaseDao<OrganizStore, String> baseDao;
	@Autowired
	private IInfoTypeService infoTypeService;
	private OrganizStoreNRTSearch nrtSearch=OrganizStoreNRTSearch.getInstance();
	public boolean saveOrganizStore(OrganizStore organizStore) throws Exception {
		//新增组织
		baseDao.save(organizStore);
		//添加索引信息
		nrtSearch.addIndex(organizStore);
		return true;
	}
	
	public OrganizStore findOrganizStoreById(String organizStoreId) throws Exception {
		// TODO Auto-generated method stub
		return baseDao.findById(OrganizStore.class, organizStoreId);
	}
	public PagingObject<OrganizStore> findOrganizStoreByIsEnable(String field, String fieldValue, String startTime,String endTime,int nextPage,
			int size) throws Exception {
			DetachedCriteria criteria = DetachedCriteria.forClass(OrganizStore.class);
			//时间倒序排序
			criteria.addOrder(Order.desc("createDate"));
			criteria.add(Restrictions.eq("isEnable", "2"));
			if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)){
				//人员类别不能直接设置值
				criteria.add(Restrictions.like(field, "%"+fieldValue+"%"));
			}
			if(!StringUtil.isNull(startTime) && startTime.length() >=10){
				startTime = startTime.substring(0,10)+ " 00:00:00";
				criteria.add(Restrictions.ge("createDate", startTime));
			}
			if(!StringUtil.isNull(endTime) && endTime.length()>=10){
				endTime = endTime.substring(0,10)+" 23:59:59";
				criteria.add(Restrictions.le("createDate", endTime));
			}
			int rowCount = baseDao.getRowCount(criteria);

			Paging paging = new Paging(size, nextPage, rowCount);
			logger.debug("分页信息：" + JSONObject.fromObject(paging));

			criteria.setProjection(null);
			criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

			List<OrganizStore> list = baseDao.findPagingByCriteria(criteria, size,
					paging.getStartRecord());
			PagingObject<OrganizStore> vo = new PagingObject<OrganizStore>();
			vo.setPaging(paging);
			vo.setArray(list);
			return vo;
		}
	@SuppressWarnings("unchecked")
	public String findOrganizStore(String field, String fieldValue,
			String startTime, String endTime, int nowPage, int size,
			String uid, String oid, String did) throws Exception {
		return null;
	}
	/**
	 * 带有排序的
	 */
	@SuppressWarnings("unchecked")
	public String findOrganizStore(String field, String fieldValue,
			String startTime, String endTime, int nowPage, int size,
			String uid, String oid, String did,String sortField,String dir) throws Exception {
			boolean hasValue=false;
			//有输入值才高亮
			if(!StringUtil.isEmty(fieldValue)){
				hasValue=true;
			}
			//检索
			PagingObject<OrganizStore> vo=StoreIndexQuery.findStore(field, fieldValue, startTime, endTime, 
					nowPage, size, uid, oid, did, sortField,dir,OrganizStore.class);
			return StoreIndexQuery.organizStoreVoToJson(vo, hasValue);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findOrganizStoreByConditionsVoToJson(PagingObject<OrganizStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizStore or : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", or.getId());
			if (null!=or.getIsEnable()) {
				obj.put("isEnable",or.getIsEnable());
			}else{
				obj.put("isEnable","1");
			}
			obj.put("organizName", or.getOrganizName());
			obj.put("orgHeadOfName", or.getOrgHeadOfName());
			//为空的条件
			if(or.getInfoType()!=null){
				
				obj.put("infoType", or.getInfoType().getTypeName());
				obj.put("infoTypeId", or.getInfoType().getId());
			}
			obj.put("websiteURL", or.getWebsiteURL());
			obj.put("organizPublicActionNames", or.getOrganizPublicActionNames());
			obj.put("organizPersonNames",or.getOrganizPersonNames());
			obj.put("organizBranchesNames", or.getOrganizBranchesNames());
			obj.put("foundTime", or.getFoundTime());
			obj.put("degreeOfLatitude", or.getDegreeOfLatitude());
			obj.put("basicCondition",or.getBasicCondition());
			obj.put("activityCondition",or.getActivityCondition());
			obj.put("createDate", or.getCreateDate());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String field,String fieldValue,String startTime,String endTime,
			int nextPage, int size, String uid,
			String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OrganizStore.class);
		//时间倒序排序
		criteria.addOrder(Order.desc("createDate"));
		if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)&&!field.equals("infoType")){
			//人员类别不能直接设置值
			criteria.add(Restrictions.like(field, "%"+fieldValue+"%"));
		}
		if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)&&field.equals("infoType")){
			//如果是人员类别 
			//使用createAlias来创建属性别名,然后引用别名进行条件查询
			criteria.createAlias("infoType", "i").add(Restrictions.like("i.typeName", "%"+fieldValue+"%"));
		}
		//查询不是删除的数据
		criteria.add(Restrictions.or(Restrictions.eq("action", "1"), Restrictions.eq("action", "2")));
		//查询已入库的数据
		criteria.add(Restrictions.eq("isToStorage", "1"));
		if(!StringUtil.isNull(startTime) && startTime.length() >=10){
			startTime = startTime.substring(0,10)+ " 00:00:00";
			criteria.add(Restrictions.ge("createDate", startTime));
		}
		if(!StringUtil.isNull(endTime) && endTime.length()>=10){
			endTime = endTime.substring(0,10)+" 23:59:59";
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
		return criteria;
	}

	public PagingObject<OrganizStore> findOrganizStoreByIsEnable(int nextPage,
			int size, String uid, String oid, String did) throws Exception {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria
				.forClass(OrganizStore.class);
		//时间倒序排序
				criteria.addOrder(Order.desc("createDate"));
		criteria.add(Restrictions.eq("isEnable", "2"));
		criteria.add(Restrictions.ne("action", "3"));
		if (!StringUtil.isNull(did)) {
			criteria.add(Restrictions.eq("did", did));
		}
		if (!StringUtil.isNull(uid)) {
			criteria.add(Restrictions.eq("uid", uid));
		}
		if (!StringUtil.isNull(oid)) {
			criteria.add(Restrictions.eq("oid", oid));
		}
		int rowCount = baseDao.getRowCount(criteria);
		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<OrganizStore> list = baseDao.findPagingByCriteria(criteria, size,
				paging.getStartRecord());
		PagingObject<OrganizStore> vo = new PagingObject<OrganizStore>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}
	public boolean updateOrganizStoreAction(String[] ids) throws Exception {
		//删除组织
		StringBuffer buffer = new StringBuffer("update OrganizStore set action='3' where ");
		for (String string : ids) {
			buffer.append(String.format("id='%s' or ", string));
		}
		String hql = buffer.toString().trim();
		// 去掉最后的or
		hql = StringUtils.substring(hql, 0, hql.length() - 2);
		logger.info("删除语句：" + hql);
		// 执行删除
		baseDao.executeHql(hql);
		//更新库
		nrtSearch.deleteIndex(ids);
		return true;
	}
	public boolean updateOrganizStoreByOrgId(OrganizStore organizStore)
			throws Exception {
		//更新组织
		String id=organizStore.getId();
		baseDao.update(organizStore);
		//更新索引库
		nrtSearch.updateIndex(organizStore.getId(), organizStore);
		return true;
	}
	public boolean updateOrganizStoreIsEnableStart(String[] orgIds)
			throws Exception {
		for (String id : orgIds) {
			String sql  = "UPDATE T_ORGANIZ_STORE SET IS_ENABLE = '2' WHERE ID = '"+id+"'";
			baseDao.executeSql(sql);
		}
		return true;
	}
	public boolean updateOrganizStoreIsEnableCease(String[] orgIds)
			throws Exception {
		for (String id : orgIds) {
			String sql  = "UPDATE T_ORGANIZ_STORE SET IS_ENABLE = '1' WHERE ID = '"+id+"'";
			baseDao.executeSql(sql);
		}
		return true;
	}
	/**
	 * 
	 */
	@Override
	public void outputOrganizStoreToWord(String id, String filePath) {
		//将组织信息保存为word
		FileOutputStream fos=null;
		String fontFamily="宋体";
		int fontSize=36;
		String temple=PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class)+"temple.doc";
		String[] properties=new String[]{"organizName","foundTime","degreeOfLatitude","websiteURL",
				"orgHeadOfName","personStore","infoType","organizBranchesNames","organizPersonNames"
				,"organizPublicActionNames","basicCondition","activityCondition"};
		String[] chinese_properties=new String[]{"组织名称","成立时间","活动范围","网址","组织负责人名称","组织负责人","组织类别",
				"下属组织名称","下属组织成员","下属组织刊物","基本情况","主要活动情况"};
		try {
			OrganizStore store=findOrganizStoreById(id);
			if(null!=store){
				
				POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(temple));
				HWPFDocument doc = new HWPFDocument(fs);

				// centered paragraph with large font size
				Range range = doc.getRange();
				Paragraph titlePar = range.insertAfter(new ParagraphProperties(), 0);
				CharacterRun titleRun = titlePar.insertAfter("");
				titleRun.insertAfter(store.getOrganizName()+"详细信息：");
				titleRun.setFontSize(50);
				
				for(int i=0;i<properties.length;i++){
		        	//设置内容
					Paragraph paragraph = range.insertAfter(new ParagraphProperties(), 0);
					String string=properties[i];
					CharacterRun run1 = paragraph.insertAfter("");
					run1.setFontSize(fontSize);
					if(string.equals("infoType")){
						//直接利用PropertyUtils获得属性值
						InfoType infoType=(InfoType) PropertyUtils.getSimpleProperty(store, string);
						if(null!=infoType){
							run1.insertAfter(chinese_properties[i]+"："+infoType.getTypeName());
						}else{
							run1.insertAfter(chinese_properties[i]+"：");
						}
					}else if(string.equals("personStore")){
						PersonStore personStore=(PersonStore) PropertyUtils.getSimpleProperty(store, string);
						if (null!=personStore) {
							run1.insertAfter(chinese_properties[i]+"："+personStore.getPersonName());
						}else{
							run1.insertAfter(chinese_properties[i]+"：");
						}
					}else{
						run1.insertAfter(chinese_properties[i]+"："+PropertyUtils.getSimpleProperty(store, string));
					}
		        }
		
				 fos=new FileOutputStream(new File(filePath));
			        doc.write(fos);
								
				/*CustomXWPFDocument document=new CustomXWPFDocument();
				//标题
				XWPFParagraph title = document.createParagraph();
		        title.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun titleRun = title.createRun();
		        titleRun.setBold(true);
		        titleRun.setFontFamily(fontFamily);
		        titleRun.setText(store.getOrganizName()+"的信息");
		        titleRun.setFontSize(fontSize);
		        for(int i=0;i<properties.length;i++){
		        	//设置内容
			        XWPFParagraph property = document.createParagraph();
			        property.setSpacingAfterLines(25);
			        XWPFRun propertyRun=property.createRun();
			        propertyRun.setFontFamily(fontFamily);
			        propertyRun.setFontSize(12);
					String string=properties[i];
					if(string.equals("infoType")){
						//直接利用PropertyUtils获得属性值
						InfoType infoType=(InfoType) PropertyUtils.getSimpleProperty(store, string);
						if(null!=infoType){
							propertyRun.setText(chinese_properties[i]+"："+infoType.getTypeName());
						}else{
							propertyRun.setText(chinese_properties[i]+"：");
						}
					}else if(string.equals("personStore")){
						PersonStore personStore=(PersonStore) PropertyUtils.getSimpleProperty(store, string);
						if (null!=personStore) {
							propertyRun.setText(chinese_properties[i]+"："+personStore.getPersonName());
						}else{
							propertyRun.setText(chinese_properties[i]+"：");
						}
					}else{
						propertyRun.setText(chinese_properties[i]+"："+PropertyUtils.getSimpleProperty(store, string));
					}
		        }
		        fos=new FileOutputStream(new File(filePath));
		        document.write(fos);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("");
		}finally {
			IOUtils.closeQuietly(fos);
		}
	}

	@Override
	public boolean hasStoreByOrganizName(String organizName) {
		// 根据文档名称判断是否存在
		boolean result=false;
		
		try {
			String hql=String.format("select id from OrganizStore where organizName='%s'"
					+ "and action<>'3'", organizName);
			List<OrganizStore> list=baseDao.findByHql(hql);
			if(list.size()>0){
				result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
