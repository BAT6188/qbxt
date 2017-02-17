package com.ushine.storesinfo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
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
import com.ushine.luceneindex.index.StoreIndexQuery;
import com.ushine.luceneindex.index.WebsiteJournalStoreNRTSearch;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.IWebsiteJournalStoreService;
import com.ushine.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@Service("websiteJournalStoreServiceImpl")
public class WebsiteJournalStoreServiceImpl implements IWebsiteJournalStoreService {

	private Logger logger = LoggerFactory.getLogger(WebsiteJournalStoreServiceImpl.class);
	@Autowired
	private IBaseDao<WebsiteJournalStore, String> baseDao;
	@Autowired
	private IInfoTypeService infoTypeService;
	//
	WebsiteJournalStoreNRTSearch nrtSearch=WebsiteJournalStoreNRTSearch.getInstance();
	/**
	 * 新增
	 */
	public boolean saveWebsitejournal(WebsiteJournalStore websiteJournalStore) throws Exception {
		//
		baseDao.save(websiteJournalStore);
		// 更新索引库
		nrtSearch.addIndex(websiteJournalStore);
		
		
		return true;
	}

	public WebsiteJournalStore findWebsiteJouById(String websiteJouId) throws Exception {
		// TODO Auto-generated method stub
		return baseDao.findById(WebsiteJournalStore.class, websiteJouId);
	}

	// 多条件查询
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String field, String fieldValue, String startTime, String endTime,
			int nextPage, int size, String uid, String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebsiteJournalStore.class);
		// 时间倒序排序
		criteria.addOrder(Order.desc("createDate"));
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue) && !field.equals("infoType")) {
			// 类别不能直接设置值
			criteria.add(Restrictions.like(field, "%" + fieldValue + "%"));
		}
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue) && field.equals("infoType")) {
			// 如果是类别
			// 使用createAlias来创建属性别名,然后引用别名进行条件查询
			criteria.createAlias("infoType", "i").add(Restrictions.like("i.typeName", "%" + fieldValue + "%"));
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
		// 查询已入库的数据
		criteria.add(Restrictions.eq("isToStorage", "1"));
		// criteria.add(Restrictions.sqlRestriction("action=1 or action=2"));
		// criteria.add(Restrictions.like("action", "1"));
		// criteria.add(Restrictions.like("action", "2"));
		return criteria;
	}

	/**
	 * 把媒体网站刊物集合转成json
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	// , Map<String, String> infoTypeMap
	public String voToJson(PagingObject<WebsiteJournalStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (WebsiteJournalStore store : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", store.getId());
			obj.put("isEnable", store.getIsEnable());
			obj.put("name", store.getName());
			obj.put("websiteURL", store.getWebsiteURL());
			obj.put("serverAddress", store.getServerAddress());

			obj.put("establishAddress", store.getEstablishAddress());
			obj.put("mainWholesaleAddress", store.getMainWholesaleAddress());
			obj.put("establishPerson", store.getEstablishPerson());
			obj.put("establishTime", store.getEstablishTime());
			obj.put("basicCondition", store.getBasicCondition());
			obj.put("createDate", store.getCreateDate());
			// id和类型对应,不去数据库中查询
			// obj.put("infoType", infoTypeMap.get(store.getId()));
			// 类别不为空
			if (store.getInfoType() != null) {
				obj.put("infoType", store.getInfoType().getTypeName());
			}

			array.add(obj);
		}
		root.element("datas", array);
		// logger.info("====WebsiteJournalStore===="+root.toString());
		return root.toString();
	}

	@SuppressWarnings("unchecked")
	public String findWebsiteJournalStore(String field, String fieldValue, String startTime, String endTime,
			int nextPage, int size, String uid, String oid, String did,String sortField,String dir) throws Exception {
		// 利用索引查询
		PagingObject<WebsiteJournalStore> voObject=StoreIndexQuery.findStore(field, fieldValue, startTime, endTime, nextPage, 
				size, uid, oid, did,sortField,dir,WebsiteJournalStore.class);
		boolean hasValue=false;
		if(!StringUtil.isEmty(fieldValue)){
			hasValue=true;
		}
		return StoreIndexQuery.websiteJournalStoreVoToJson(voObject, hasValue);
	}

	public void delWebsiteJournalStoreByIds(String[] websiteJournalStoreIds) throws Exception {
		try {
			// 删除只是把每个对象的ation值改为3
			StringBuffer buffer = new StringBuffer("update WebsiteJournalStore set action='3' where ");
			for (String string : websiteJournalStoreIds) {
				buffer.append(String.format("id='%s' or ", string));
			}
			String hql = buffer.toString().trim();
			// 去掉最后的or
			hql = StringUtils.substring(hql, 0, hql.length() - 2);
			logger.info("删除语句：" + hql);
			// 执行删除
			baseDao.executeHql(hql);
			//更新索引
			nrtSearch.deleteIndex(websiteJournalStoreIds);
			logger.info("==删除媒体网站刊物对象的id==" + Arrays.toString(websiteJournalStoreIds));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除媒体网站刊物失败" + e.getMessage());
		}
	}

	public void updateWebsiteJournalStore(WebsiteJournalStore websiteJournalStore) throws Exception {
		// 修改
		try {
			baseDao.update(websiteJournalStore);
			logger.info("==修改媒体网站刊物对象的==" + websiteJournalStore.toString());
			nrtSearch.updateIndex(websiteJournalStore.getId(), websiteJournalStore);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改媒体网站刊物失败" + e.getMessage());
		}
	}

	public PagingObject<WebsiteJournalStore> findWebsiteJournalStoreByIsEnable(String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(WebsiteJournalStore.class);
		// 时间倒序排序
		criteria.addOrder(Order.desc("createDate"));
		criteria.add(Restrictions.eq("isEnable", "2"));
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)) {
			// 人员类别不能直接设置值
			criteria.add(Restrictions.like(field, "%" + fieldValue + "%"));
		}
		if (!StringUtil.isNull(startTime) && startTime.length() >= 10) {
			startTime = startTime.substring(0, 10) + " 00:00:00";
			criteria.add(Restrictions.ge("createDate", startTime));
		}
		if (!StringUtil.isNull(endTime) && endTime.length() >= 10) {
			endTime = endTime.substring(0, 10) + " 23:59:59";
			criteria.add(Restrictions.le("createDate", endTime));
		}
		int rowCount = baseDao.getRowCount(criteria);

		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<WebsiteJournalStore> list = baseDao.findPagingByCriteria(criteria, size, paging.getStartRecord());
		PagingObject<WebsiteJournalStore> vo = new PagingObject<WebsiteJournalStore>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}

	public PagingObject<WebsiteJournalStore> findWebsiteJournalByIsEnable(int nextPage, int size, String uid,
			String oid, String did) throws Exception {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(WebsiteJournalStore.class);
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

		List<WebsiteJournalStore> list = baseDao.findPagingByCriteria(criteria, size, paging.getStartRecord());
		PagingObject<WebsiteJournalStore> vo = new PagingObject<WebsiteJournalStore>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}

	public void startWebsiteJournalStore(String[] ids) throws Exception {
		// 启用
		for (String id : ids) {
			String hql = "UPDATE WebsiteJournalStore w SET isEnable='2' WHERE w.id='" + id + "'";
			baseDao.executeHql(hql);
		}
	}

	public void ceaseWebsiteJournalStore(String[] ids) throws Exception {
		// 禁用
		for (String id : ids) {
			String hql = "UPDATE WebsiteJournalStore w SET isEnable='1' WHERE w.id='" + id + "'";
			baseDao.executeHql(hql);
		}
	}
	/**
	 * 
	 */
	@Override
	public void outputWebsiteJournalStoreToWord(String id, String filePath) {
		//保存为word
		FileOutputStream fos=null;
		String fontFamily="宋体";
		int fontSize=30;
		String[] properties=new String[]{"name","infoType","websiteURL","serverAddress","establishAddress",
				"mainWholesaleAddress","establishPerson","establishTime","basicCondition"
				};
		String[] chinese_properties=new String[]{"名称","类别","网址","服务器所在地","创办地","主要发行地","创办人",
				"创建时间","基本情况"};
		String temple=PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class)+"temple.doc";
		try {
			WebsiteJournalStore store=findWebsiteJouById(id);
			if(null!=store){
				/*CustomXWPFDocument document=new CustomXWPFDocument();
				//标题
				XWPFParagraph title = document.createParagraph();
		        title.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun titleRun = title.createRun();
		        titleRun.setBold(true);
		        titleRun.setFontFamily(fontFamily);
		        titleRun.setText(store.getName()+"的信息");
		        titleRun.setFontSize(fontSize);*/
				
				POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(temple));
				HWPFDocument doc = new HWPFDocument(fs);

				// centered paragraph with large font size
				Range range = doc.getRange();
				Paragraph titlePar = range.insertAfter(new ParagraphProperties(), 0);
				CharacterRun titleRun = titlePar.insertAfter("");
				titleRun.insertAfter(store.getName()+"详细信息：");
				titleRun.setFontSize(50);
				
		        for(int i=0;i<properties.length;i++){
		        	/*//设置内容
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
					}else{
						propertyRun.setText(chinese_properties[i]+"："+PropertyUtils.getSimpleProperty(store, string));
					}*/
		        	
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
		        //fos=new FileOutputStream(new File(filePath));
		        //document.write(fos);
		        fos=new FileOutputStream(new File(filePath));
		        doc.write(fos);
			}
			
		} catch (Exception e) {
			logger.error("保存word异常");
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(fos);
		}
	}

	@Override
	public boolean hasStoreByName(String name) {
		// 根据文档名称判断是否存在
		boolean result=false;
		try {
			String hql=String.format("select id from WebsiteJournalStore where name='%s'"
					+ "and action<>'3'", name);
			List<WebsiteJournalStore> list=baseDao.findByHql(hql);
			if(list.size()>0){
				result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
