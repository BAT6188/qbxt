package com.ushine.storesinfo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import net.sf.json.JSONObject;

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
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.common.utils.PathUtils;
import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.ClueStoreNRTSearch;
import com.ushine.luceneindex.index.StoreIndexQuery;
import com.ushine.solr.service.IClueStoreSolrService;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;
import com.ushine.storesinfo.service.IClueStoreService;
import com.ushine.util.StringUtil;

/**
 * 线索库接口实现类
 * 
 * @author wangbailin
 * 
 */
@Transactional
@Service("clueStoreServiceImpl")
public class ClueStoreServiceImpl implements IClueStoreService {
	private static final Logger logger = LoggerFactory.getLogger(InfoTypeServiceImpl.class);
	@Autowired
	private IBaseDao<ClueStore, String> baseDao;
	@Autowired
	private IBaseDao<PersonStore, String> dao;
	@Autowired
	IClueStoreSolrService solrService;

	public boolean saveClue(ClueStore clueStore) throws Exception {
		baseDao.save(clueStore);
		// 添加索引
		solrService.addDocumentByStore(clueStore);
		return true;
	}

	public boolean updateClue(ClueStore clueStore) throws Exception {
		baseDao.update(clueStore);
		// 更新索引
		solrService.updateDocumentByStore(clueStore.getId(), clueStore);
		return true;
	}

	public ClueStore findClueById(String clueId) throws Exception {
		// TODO Auto-generated method stub
		return (ClueStore) baseDao.findById(ClueStore.class, clueId);
	}

	/**
	 * 包含排序的查询
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String findClueStore(String field, String fieldValue, String startTime, String endTime, int nextPage, int size, String uid, String oid, String did, String sortField, String dir)
			throws Exception {
		// 按索引查询
		boolean hasValue = false;
		if (!StringUtil.isEmty(fieldValue)) {
			hasValue = true;
		}
		PagingObject<ClueStore> vo = StoreIndexQuery.findStore(field, fieldValue, startTime, endTime, nextPage, size, uid, oid, did, sortField, dir, ClueStore.class);
		return StoreIndexQuery.clueStoreVoToJson(vo, hasValue);
	}

	public boolean delClueStoreByIds(String[] ids) throws Exception {
		// 删除
		StringBuffer buffer = new StringBuffer("update ClueStore set action='3' where ");
		for (String string : ids) {
			buffer.append(String.format("id='%s' or ", string));
		}
		String hql = buffer.toString().trim();
		// 去掉最后的or
		hql = StringUtils.substring(hql, 0, hql.length() - 2);
		logger.info("删除语句：" + hql);
		// 执行删除
		baseDao.executeHql(hql);
		// 删除多条索引
		solrService.deleteDocumentByIds(ids);
		return true;
	}

	@Transactional(readOnly = true)
	public List findById(String id) throws Exception {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM T_PERSON_STORE  AS P LEFT JOIN T_CLUE_RELATIONSHIP  R ON (R.`LIBRARY_ID`=P.`ID`) LEFT JOIN T_CLUE_STORE  C ON(C.`ID`=R.`CLUE_ID`) WHERE C.`ID` = '" + id + "' ";
		List list = baseDao.findBySqlAnPersonStore(sql, PersonStore.class, 0, 2);
		// Object list = baseDao.getRows(sql);
		// int a = Integer.parseInt(l);
		// System.out.println(list);
		return list;
	}

	/**
	 * 根据clueid查询相关联的人员
	 */
	public PagingObject<PersonStore> findCluePersonStore(String clueId, String field, String fieldValue, String startTime, String endTime, int nextPage, int size) throws Exception {
		String checkArr = personCheckArr(field, fieldValue, startTime, endTime);
		// 拼接sql语句 分页查询数据
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" SELECT * FROM T_PERSON_STORE  AS P  ");
		sbSql.append(" LEFT JOIN T_CLUE_RELATIONSHIP  R ON (R.`LIBRARY_ID`=P.`ID`) ");
		sbSql.append(" LEFT JOIN T_CLUE_STORE  C ON(C.`ID`=R.`CLUE_ID`) ");
		sbSql.append(" WHERE C.`ID` = '" + clueId + "' ");
		sbSql.append(checkArr);
		// 拼接sql语句 ,统计总条数
		StringBuffer sbCount = new StringBuffer();
		sbCount.append(" SELECT COUNT(*) FROM T_PERSON_STORE AS P  ");
		sbCount.append(" LEFT JOIN T_CLUE_RELATIONSHIP R ON (R.`LIBRARY_ID`=P.`ID`) ");
		sbCount.append(" LEFT JOIN T_CLUE_STORE C ON(C.`ID`=R.`CLUE_ID`) ");
		sbCount.append(" WHERE C.`ID` = '" + clueId + "' ");
		sbSql.append(checkArr);
		int rowCount = Integer.parseInt(baseDao.getRows(sbCount.toString()).toString());
		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));
		@SuppressWarnings("unchecked")
		List<PersonStore> list = baseDao.findBySqlAnPersonStore(sbSql.toString(), PersonStore.class, size, paging.getStartRecord());
		PagingObject<PersonStore> vo = new PagingObject<PersonStore>();
		vo.setArray(list);
		vo.setPaging(paging);
		return vo;
	}

	public PagingObject<WebsiteJournalStore> findClueWebsiteJournalStore(String clueId, String field, String fieldValue, String startTime, String endTime, int nextPage, int size) throws Exception {
		String checkArr = websiteJournalCheckArr(field, fieldValue, startTime, endTime);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" SELECT * FROM T_WEBSITE_JOURNAL_STORE  AS W ");
		sbSql.append(" LEFT JOIN T_CLUE_RELATIONSHIP AS R ON(W.`ID`=R.`LIBRARY_ID`) ");
		sbSql.append(" LEFT JOIN T_CLUE_STORE AS C ON (C.`ID`=R.`CLUE_ID`) ");
		sbSql.append(" WHERE C.`ID` = '" + clueId + "'");
		sbSql.append(checkArr);

		StringBuffer sbCount = new StringBuffer();
		sbCount.append(" SELECT COUNT(*) FROM T_WEBSITE_JOURNAL_STORE AS W ");
		sbCount.append(" LEFT JOIN T_CLUE_RELATIONSHIP AS R ON(W.`ID`=R.`LIBRARY_ID`) ");
		sbCount.append(" LEFT JOIN T_CLUE_STORE AS C ON (C.`ID`=R.`CLUE_ID`) ");
		sbCount.append(" WHERE C.`ID` = '" + clueId + "'");
		sbCount.append(checkArr);
		int rowCount = Integer.parseInt(baseDao.getRows(sbCount.toString()).toString());
		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));
		@SuppressWarnings("unchecked")
		List<WebsiteJournalStore> list = baseDao.findBySqlAnWebsiteJournalStore(sbSql.toString(), WebsiteJournalStore.class, size, paging.getStartRecord());
		PagingObject<WebsiteJournalStore> vo = new PagingObject<WebsiteJournalStore>();
		vo.setArray(list);
		vo.setPaging(paging);
		return vo;
	}

	// 根据需要查询的字段名称返回数据库对应的字段名称
	private String personCheckArr(String field, String fieldValue, String startTime, String endTime) {
		StringBuffer sb = new StringBuffer();
		// workUnit,personName,nameUsedBefore,englishName,bebornTime
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)) {
			if ("anyField".equals(field)) {
				sb.append("   AND P.`WORK_UNIT` like '%" + fieldValue + "%'  ").append("   OR P.`PERSON_NAME` like '%" + fieldValue + "%'  ")
						.append("   OR P.`NAME_USED_BEFORE` like '%" + fieldValue + "%'  ").append("   OR P.`ENGLISH_NAME` like '%" + fieldValue + "%'  ")
						.append("   OR P.`PRESENT_ADDRESS` like '%" + fieldValue + "%'  ").append("   OR P.`REGISTER_ADDRESS` like '%" + fieldValue + "%'  ")
						// .append("   OR P.`BEBORN_TIME` like '%"+fieldValue+"%'  ")
						.append("   OR P.`SEX` like '%" + fieldValue + "%'  ");
			}
			/*
			 * if("workUnit".equals(field)){
			 * sb.append("   AND P.`WORK_UNIT` like '%"+fieldValue+"%'  ");
			 * }else if("personName".equals(field)){
			 * sb.append("   AND P.`PERSON_NAME` like '%"+fieldValue+"%'  ");
			 * }else if("nameUsedBefore".equals(field)){
			 * sb.append("   AND P.`NAME_USED_BEFORE` like '%"
			 * +fieldValue+"%'  "); }else if("englishName".equals(field)){
			 * sb.append("   AND P.`ENGLISH_NAME` like '%"+fieldValue+"%'  ");
			 * }else if("presentAddress".equals(field)){
			 * sb.append("   AND P.`PRESENT_ADDRESS` like '%"
			 * +fieldValue+"%'  "); }else if("registerAddress".equals(field)){
			 * sb
			 * .append("   AND P.`REGISTER_ADDRESS` like '%"+fieldValue+"%'  ");
			 * }else if("bebornTime".equals(field)){
			 * sb.append("   AND P.`BEBORN_TIME` like '%"+fieldValue+"%'  "); }
			 */
		}
		if (!StringUtil.isNull(startTime) && !StringUtil.isNull(endTime)) {
			startTime = startTime.substring(0, 10);
			startTime += " 00:00:00";
			endTime = endTime.substring(0, 10);
			endTime += " 23:59:59";
			sb.append(" AND P.`CREATE_DATE` >= '" + startTime + "' AND P.`CREATE_DATE` <= '" + endTime + "'");
		}
		return sb.toString();
	}

	// 根据需要查询的字段名称返回数据库对应的字段名称
	private String organizCheckArr(String field, String fieldValue, String startTime, String endTime) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		// organizName，foundTime，degreeOfLatitude，websiteURL，orgHeadOfName
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)) {
			if ("anyField".equals(field)) {
				sb.append("   AND O.`ORGANIZ_NAME` like '%" + fieldValue + "%'  ").append("   OR O.`DEGREE_OF_LATITUDE` like '%" + fieldValue + "%'  ")
						.append("   OR O.`WEBSITE_URL` like '%" + fieldValue + "%'  ").append("   OR O.`ORG_HEADOF_NAME` like '%" + fieldValue + "%'  ");
			}
			/*
			 * if("organizName".equals(field)){
			 * sb.append("   AND O.`ORGANIZ_NAME` like '%"+fieldValue+"%'  ");
			 * }else if("degreeOfLatitude".equals(field)){
			 * sb.append("   AND O.`DEGREE_OF_LATITUDE` like '%"
			 * +fieldValue+"%'  "); }else if("websiteURL".equals(field)){
			 * sb.append("   AND O.`WEBSITE_URL` like '%"+fieldValue+"%'  ");
			 * }else if("orgHeadOfName".equals(field)){
			 * sb.append("   AND O.`ORG_HEADOF_NAME` like '%"
			 * +fieldValue+"%'  "); }else if("foundTime".equals(field)){
			 * sb.append("   AND O.`FOUND_TIME` like '%"+fieldValue+"%'  "); }
			 */
		}
		if (!StringUtil.isNull(startTime) && !StringUtil.isNull(endTime)) {
			startTime = startTime.substring(0, 10);
			startTime += " 00:00:00";
			endTime = endTime.substring(0, 10);
			endTime += " 23:59:59";
			sb.append(" AND O.`CREATE_DATE` >= '" + startTime + "' AND O.`CREATE_DATE` <= '" + endTime + "'");
		}
		return sb.toString();
	}

	// 根据需要查询的字段名称返回数据库对应的字段名称
	private String websiteJournalCheckArr(String field, String fieldValue, String startTime, String endTime) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)) {
			if ("anyField".equals(field)) {
				sb.append("   AND W.`NAME` like '%" + fieldValue + "%'  ").append("   OR W.`WEBSITEURL` like '%" + fieldValue + "%'  ")
						.append("   OR W.`MAIN_WHOLESALE_ADDRESS` like '%" + fieldValue + "%'  ").append("   OR W.`ESTABLISH_PERSON` like '%" + fieldValue + "%'  ")
						.append("   OR W.`BASIC_CONDITION` like '%" + fieldValue + "%'  ").append("   OR W.`ESTABLISH_ADDRESS` like '%" + fieldValue + "%'  ")
						.append("   OR W.`SERVER_ADDRESS` like '%" + fieldValue + "%'  ");
			}
		}
		if (!StringUtil.isNull(startTime) && !StringUtil.isNull(endTime)) {
			startTime = startTime.substring(0, 10);
			startTime += " 00:00:00";
			endTime = endTime.substring(0, 10);
			endTime += " 23:59:59";
			sb.append(" AND W.`CREATE_DATE` >= '" + startTime + "' AND W.`CREATE_DATE` <= '" + endTime + "'");
		}
		return sb.toString();
	}

	public PagingObject<ClueStore> findClueStore(int nextPage, int size, String uid, String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(ClueStore.class);
		// 必须是没有删除的数据
		criteria.add(Restrictions.or(Restrictions.eq("action", "1"), Restrictions.eq("action", "2")));
		// 必须是启用的数据
		criteria.add(Restrictions.eq("isEnable", "2"));
		if (!StringUtil.isNull(uid)) {
			criteria.add(Restrictions.eq("uid", uid));
		}
		if (!StringUtil.isNull(oid)) {
			criteria.add(Restrictions.eq("oid", oid));
		}
		if (!StringUtil.isNull(did)) {
			criteria.add(Restrictions.eq("did", did));
		}
		int rowCount = baseDao.getRowCount(criteria);
		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));
		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		List<ClueStore> list = baseDao.findPagingByCriteria(criteria, size, paging.getStartRecord());
		PagingObject<ClueStore> vo = new PagingObject<ClueStore>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}

	public List<PersonStore> findPersonStoreByClueId(String clueId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM T_PERSON_STORE  P ");
		sb.append(" LEFT JOIN T_CLUE_RELATIONSHIP  R ON (R.`LIBRARY_ID`=P.`ID`) ");
		sb.append(" LEFT JOIN T_CLUE_STORE  C ON (C.`ID`=R.`CLUE_ID`) ");
		sb.append(" WHERE C.`ID`= '" + clueId + "' ");
		List<PersonStore> list = baseDao.findBySqlAnPersonStore(sb.toString(), PersonStore.class, 1000, 0);
		return list;
	}

	public List<WebsiteJournalStore> findWebsiteJournalStoreByClueId(String clueId) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM T_WEBSITE_JOURNAL_STORE P ");
		sb.append(" LEFT JOIN T_CLUE_RELATIONSHIP  R ON (R.`LIBRARY_ID`=P.`ID`) ");
		sb.append(" LEFT JOIN T_CLUE_STORE  C ON (C.`ID`=R.`CLUE_ID`) ");
		sb.append(" WHERE C.`ID`= '" + clueId + "' ");
		List<WebsiteJournalStore> list = baseDao.findBySqlAnWebsiteJournalStore(sb.toString(), WebsiteJournalStore.class, 1000, 0);
		return list;
	}

	public boolean updateClueStoreIsEnableStart(String[] clueIds) throws Exception {
		for (String id : clueIds) {
			String sql = "UPDATE T_CLUE_STORE   SET IS_ENABLE = '2' WHERE ID = '" + id + "'";
			baseDao.executeSql(sql);
		}
		return true;
	}

	public boolean updateClueStoreIsEnableCease(String[] clueIds) throws Exception {
		for (String id : clueIds) {
			String sql = "UPDATE T_CLUE_STORE SET IS_ENABLE = '1' WHERE ID = '" + id + "'";
			baseDao.executeSql(sql);
		}
		return true;
	}

	/**
		 * 
		 */
	@Override
	public void outputClueStoreToWord(String id, String filePath) {
		// 保存为word
		String temple = PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class) + "temple.doc";
		FileOutputStream fos = null;
		String fontFamily = "宋体";
		int fontSize = 30;
		String[] properties = new String[] { "clueName", "clueSource", "findTime", "clueContent", "arrangeAndEvolveCondition" };
		String[] chinese_properties = new String[] { "线索名称", "线索来源", "发现时间", "线索内容", "工作部署及进展情况" };
		try {
			ClueStore store = findClueById(id);
			if (null != store) {
				POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(temple));
				HWPFDocument doc = new HWPFDocument(fs);

				// centered paragraph with large font size
				Range range = doc.getRange();
				Paragraph titlePar = range.insertAfter(new ParagraphProperties(), 0);
				CharacterRun titleRun = titlePar.insertAfter("");
				titleRun.insertAfter(store.getClueName() + "详细信息：");
				titleRun.setFontSize(50);

				/*
				 * CustomXWPFDocument document=new CustomXWPFDocument(); //标题
				 * XWPFParagraph title = document.createParagraph();
				 * title.setAlignment(ParagraphAlignment.CENTER); XWPFRun
				 * titleRun = title.createRun(); titleRun.setBold(true);
				 * titleRun.setFontFamily(fontFamily);
				 * titleRun.setText(store.getClueName()+"的信息");
				 * titleRun.setFontSize(fontSize);
				 */
				for (int i = 0; i < properties.length; i++) {
					/*
					 * //设置内容 XWPFParagraph property =
					 * document.createParagraph();
					 * property.setSpacingAfterLines(25); XWPFRun
					 * propertyRun=property.createRun();
					 * propertyRun.setFontFamily(fontFamily);
					 * propertyRun.setFontSize(12); String string=properties[i];
					 * propertyRun
					 * .setText(chinese_properties[i]+"："+PropertyUtils
					 * .getSimpleProperty(store, string));
					 */
					// 设置内容
					Paragraph paragraph = range.insertAfter(new ParagraphProperties(), 0);
					String string = properties[i];
					CharacterRun run1 = paragraph.insertAfter("");
					run1.setFontSize(fontSize);
					if (string.equals("infoType")) {
						// 直接利用PropertyUtils获得属性值
						InfoType infoType = (InfoType) PropertyUtils.getSimpleProperty(store, string);
						if (null != infoType) {
							run1.insertAfter(chinese_properties[i] + "：" + infoType.getTypeName());
						} else {
							run1.insertAfter(chinese_properties[i] + "：");
						}
					} else if (string.equals("personStore")) {
						PersonStore personStore = (PersonStore) PropertyUtils.getSimpleProperty(store, string);
						if (null != personStore) {
							run1.insertAfter(chinese_properties[i] + "：" + personStore.getPersonName());
						} else {
							run1.insertAfter(chinese_properties[i] + "：");
						}
					} else {
						run1.insertAfter(chinese_properties[i] + "：" + PropertyUtils.getSimpleProperty(store, string));
					}
				}
				/*
				 * fos=new FileOutputStream(new File(filePath));
				 * document.write(fos);
				 */
				fos = new FileOutputStream(new File(filePath));
				doc.write(fos);
			}

		} catch (Exception e) {
			logger.error("保存word异常");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	@Override
	public boolean hasStoreByClueName(String clueName) {
		// 根据线索名称判断是否存在
		boolean result = false;
		try {
			String hql = String.format("select id from ClueStore where clueName='%s'" + "and action<>'3'", clueName);
			List<ClueStore> list = baseDao.findByHql(hql);
			if (list.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
