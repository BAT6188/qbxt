package com.ushine.luceneindex.index;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ushine.common.utils.SpringUtils;
import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.ClueRelationship;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.model.NetworkAccountStore;
import com.ushine.storesinfo.model.OutsideDocStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.VocationalWorkStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;
import com.ushine.storesinfo.service.IClueRelationshipService;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.util.StringUtil;

/**
 * 七个store全文检索查询接口
 * 
 * @author dh
 *
 */
public class StoreIndexQuery {

	private static Logger logger = LoggerFactory.getLogger(StoreIndexQuery.class);

	private static IStoreNRTSearch nrtSearch = null;

	private static List storeList = null;
	
	private static Map<String, Document> documentHashMap;
	
	private static Map<String, Query> queryHashMap;
	/**
	 * 再次查询的分隔符
	 */
	private static final String SPLITSTRING="&&";
	
	/**
	 * 利用检索查询的实现方法,七个store对象都适用
	 * 
	 * @param field
	 *            字段
	 * @param fieldValue
	 *            字段值
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param nextPage
	 *            当前页
	 * @param size
	 *            每页数量
	 * @param uid
	 * @param oid
	 * @param did
	 * @param sortField 用于排序的字段
	 * @param dir 升序或降序
	 * @param clazz
	 * @return PagingObject集合
	 */
	public static PagingObject findStore(String field, String fieldValue, String startTime, String endTime,
			int nextPage, int size, String uid, String oid, String did,String sortField, String dir,Class clazz) {

		List<Document> documents = null;
		PagingObject vo = null;
		Paging paging = null;
		String storeName = clazz.getSimpleName();// 哪一种库
		documentHashMap=new HashMap<>();
		queryHashMap=new HashMap<>();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = format.parse(startTime.substring(0, 10));
			Date endDate = format.parse(endTime.substring(0, 10));
			// 日期范围
			String lowerTerm = DateTools.dateToString(startDate, Resolution.DAY);
			String upperTerm = DateTools.dateToString(endDate, Resolution.DAY);
			// 过滤条件
			BooleanQuery filterQuery = new BooleanQuery();
			// 输入的值查询
			BooleanQuery anyFieldQuery = new BooleanQuery();
			// 时间范围
			TermRangeQuery query = null;
			//查询之后再查询的query
			BooleanQuery dataSearchQuery=new BooleanQuery();
			String againValue="";
			if(fieldValue.contains(SPLITSTRING)){
				//拆开成数组 &&!values[1].equals("undefined")
				String []values=fieldValue.split(SPLITSTRING);
				fieldValue=values[0];
				if(values.length>1){
					againValue=values[1];
				}
			}
			switch (storeName) {
			// 人员
			case "PersonStore":
				nrtSearch = PersonStoreNRTSearch.getInstance();
				storeList = new ArrayList<PersonStore>();
				// 任意字段
				String[]personStoreFields={"personName","nameUsedBefore","englishName","sex","registerAddress",
						"presentAddress","workUnit","antecedents","activityCondition","infoType",
						"appendix","attachContent"
						};
				for (String string : personStoreFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				anyFieldQuery.add(new WildcardQuery(new Term("bebornTime", "*" + fieldValue + "*")), Occur.SHOULD);
				//id查询完全匹配
				anyFieldQuery.add(new TermQuery(new Term("id", fieldValue)), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("networkAccountStores", "*" + fieldValue + "*")), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("certificatesStores", "*" + fieldValue + "*")), Occur.SHOULD);
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("bebornTime", "*" + againValue + "*")), Occur.SHOULD);
					dataSearchQuery.add(new WildcardQuery(new Term("networkAccountStores", "*" + againValue + "*")), Occur.SHOULD);
					dataSearchQuery.add(new WildcardQuery(new Term("certificatesStores", "*" + againValue + "*")), Occur.SHOULD);
				}
				//创建的时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				vo = new PagingObject<PersonStore>();
				break;
			// 领导讲话
			case "LeadSpeakStore":
				nrtSearch = LeadSpeakStoreNRTSearch.getInstance();
				storeList = new ArrayList<LeadSpeakStore>();
				vo = new PagingObject<LeadSpeakStore>();
				// 任意字段
				String[]leadSpeakStoreFields={"infoType","title","meetingName","secretRank",
						"centent","involvedInTheField","attaches","attachContent"};
				for (String string : leadSpeakStoreFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				anyFieldQuery.add(new WildcardQuery(new Term("time", "*" + fieldValue + "*")), Occur.SHOULD);
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				// 时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				//再查询
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("time", "*" + againValue + "*")), Occur.SHOULD);
				}
				
				break;
			// 业务文档
			case "VocationalWorkStore":
				nrtSearch = VocationalWorkStoreNRTSearch.getInstance();
				storeList = new ArrayList<VocationalWorkStore>();
				vo = new PagingObject<VocationalWorkStore>();
				// 任意字段
				String[]vwsFields={"infoType","docNumber","theOriginal","docName","involvedInTheField",
						"attaches","attachContent"};
				for (String string : vwsFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("time", "*" + fieldValue + "*")), Occur.SHOULD);
				// 时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				//再查询
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("time", "*" + againValue + "*")), Occur.SHOULD);
				}
				break;
			// 外来文档
			case "OutsideDocStore":
				nrtSearch = OutsideDocStoreNRTSearch.getInstance();
				storeList = new ArrayList<OutsideDocStore>();
				vo = new PagingObject<OutsideDocStore>();
				//
				String[]odsFields={"involvedInTheField","infoType","docNumber", "name",
						 "centent","attaches","attachContent","sourceUnit","secretRank"};
				for (String  string: odsFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("time", "*" + fieldValue + "*")), Occur.SHOULD);
				// 时间
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				//再查询
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("time", "*" + againValue + "*")), Occur.SHOULD);
				}
				break;
			// 线索库
			case "ClueStore":
				nrtSearch = ClueStoreNRTSearch.getInstance();
				storeList = new ArrayList<ClueStore>();
				vo = new PagingObject<ClueStore>();
				// 线索里需要搜索的字段
				String[] fields = { "personName", "nameUsedBefore", "englishName", "registerAddress", "presentAddress",
						"workUnit", "personInfoType", "organizName", "orgHeadOfName", "degreeOfLatitude",
						"organizStoreInfoType", "organizBranchesNames", "organizPersonNames",
						"organizPublicActionNames", "name", "serverAddress", "establishAddress", "mainWholesaleAddress",
						"establishPerson", "websiteJournalStoreInfoType", "clueSource", "clueName",
						"arrangeAndEvolveCondition", "clueContent", "involvingObjName", "antecedents",
						"activityCondition", "websiteURL", "basicCondition" };
				for (String string : fields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				// 再加时间
				anyFieldQuery.add(new WildcardQuery(new Term("findTime", "*" + fieldValue + "*")), Occur.SHOULD);
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				if(againValue.length()>0){
					dataSearchQuery.add(new WildcardQuery(new Term("findTime", "*" + fieldValue + "*")), Occur.SHOULD);
					//dataSearchQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				}
				// 时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				break;
			}
			// 过滤条件
			filterQuery.add(query, Occur.MUST);
			//再一次查询
			BooleanQuery againQuery=new BooleanQuery();
			if (null == uid && null == oid && null != did) {
				// 读取所属部门
				filterQuery.add(new TermQuery(new Term("did", did)), Occur.MUST);
			}
			if (null == did && null == uid && null != oid) {
				// 读取所属组织
				filterQuery.add(new TermQuery(new Term("oid", oid)), Occur.MUST);
			}
			if (null == oid && null == did && null != uid) {
				// 读取个人数据
				filterQuery.add(new TermQuery(new Term("uid", uid)), Occur.MUST);
			}
			Filter filter = new QueryWrapperFilter(filterQuery);
			
			////依据关键字查询索引库////
			if (StringUtil.isEmty(fieldValue)) {
				// 没输入关键字
				logger.info(storeName + "只依据日期进行全文检索查询,排序字段："+sortField);
				paging = new Paging(size, nextPage, nrtSearch.getCount(query, filter));
				// 设置页数以及排序
				documents=nrtSearch.getDocuments(query, filter, sortField,dir, nextPage, size);
			} else if (!StringUtil.isNull(fieldValue) && field.equals("anyField")) {
				// 任意字段查询
				logger.info(storeName + "依据日期还有输入的值进行全文检索查询,排序字段："+sortField);
				// 输入的值通配符查询,用时间做过滤条件
				if(againValue.length()>0){
					//再一次查询
					againQuery.add(anyFieldQuery,Occur.MUST);
					againQuery.add(dataSearchQuery,Occur.MUST);
					paging = new Paging(size, nextPage, nrtSearch.getCount(againQuery, filter));
					documents = nrtSearch.getDocuments(againQuery, filter, sortField,dir,nextPage, size);
					//第一次查询的filter
					/*************查询之后再查询利用CachingWrapperFilter就可以实现**************/
					//CachingWrapperFilter newFilter=new CachingWrapperFilter(new QueryWrapperFilter(anyFieldQuery));
					//在查询的filter
					//paging = new Paging(size, nextPage, nrtSearch.getCount(dataSearchQuery, newFilter));
					//documents = nrtSearch.getSearcher(dataSearchQuery, newFilter, nextPage, size);
				}else{
					paging = new Paging(size, nextPage, nrtSearch.getCount(anyFieldQuery, filter));
					documents = nrtSearch.getDocuments(anyFieldQuery, filter,sortField,dir, nextPage, size);
				}
			} else if (!StringUtil.isEmty(fieldValue) && !field.equals("anyField")) {
				// 单一字段全文检索查询
				logger.info(storeName + "依据日期还有单一字段全文检索查询,排序字段："+sortField);
				BooleanQuery oneFieldQuery = new BooleanQuery();
				oneFieldQuery.add(getPhraseQuery(field, fieldValue), Occur.MUST);
				oneFieldQuery.add(query, Occur.MUST);
				// 过滤
				paging = new Paging(size, nextPage, nrtSearch.getCount(oneFieldQuery, filter));
				//排序
				documents = nrtSearch.getDocuments(oneFieldQuery, filter,sortField, dir,nextPage, size);
			}
			IBaseDao baseDao = (IBaseDao) SpringUtils.getBean("baseDao");
			IInfoTypeService infoTypeService = (IInfoTypeService) SpringUtils.getBean("infoTypeServiceImpl");
			// 获得store实例
			documentHashMap.clear();
			queryHashMap.clear();
			
			for (Document document : documents) {
				//做高亮
				storeList.add(baseDao.findById(clazz, document.get("id")));
				documentHashMap.put(document.get("id"), document);
				//高亮的query
				if (againValue.length()>0) {
					queryHashMap.put(document.get("id"), againQuery);
				}else{
					queryHashMap.put(document.get("id"), anyFieldQuery);
				}
			}
			
		} catch (Exception e) {
			logger.info("依据全文检索查询异常");
			e.printStackTrace();
		}
		vo.setPaging(paging);
		vo.setArray(storeList);
		return vo;
	}
	/***************************************查询符合条件的数量******************************************/
	/**
	 * 获得命中数
	 * @param fieldValue 关键字
	 * @param uid 
	 * @param oid
	 * @param did
	 * @param clazz 库名称
	 * @return
	 */
	public static Integer getCount(String fieldValue, String uid, String oid,String did, Class clazz) {
		String storeName = clazz.getSimpleName();// 哪一种库
		int totalCount=0;
		try {
			// 日期范围
			String lowerTerm = DateTools.dateToString(DateUtils.parseDate("1900-01-01", new String[]{"yyyy-MM-dd"}), Resolution.DAY);
			String upperTerm = DateTools.dateToString(new Date(), Resolution.DAY);
			// 过滤条件
			BooleanQuery filterQuery = new BooleanQuery();
			// 输入的值查询
			BooleanQuery anyFieldQuery = new BooleanQuery();
			// 时间范围
			TermRangeQuery query = null;
			//查询之后再查询的query
			BooleanQuery dataSearchQuery=new BooleanQuery();
			String againValue="";
			if(fieldValue.contains(SPLITSTRING)){
				//拆开成数组 &&!values[1].equals("undefined")
				String []values=fieldValue.split(SPLITSTRING);
				fieldValue=values[0];
				if(values.length>1){
					againValue=values[1];
				}
			}
			switch (storeName) {
			// 人员
			case "PersonStore":
				nrtSearch = PersonStoreNRTSearch.getInstance();
				storeList = new ArrayList<PersonStore>();
				// 任意字段
				String[]personStoreFields={"personName","nameUsedBefore","englishName","sex","registerAddress",
						"presentAddress","workUnit","antecedents","activityCondition","infoType","appendix","attachContent"
						};
				for (String string : personStoreFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				anyFieldQuery.add(new WildcardQuery(new Term("bebornTime", "*" + fieldValue + "*")), Occur.SHOULD);
				//id查询完全匹配
				anyFieldQuery.add(new TermQuery(new Term("id", fieldValue)), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("networkAccountStores", "*" + fieldValue + "*")), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("certificatesStores", "*" + fieldValue + "*")), Occur.SHOULD);
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("bebornTime", "*" + againValue + "*")), Occur.SHOULD);
					dataSearchQuery.add(new WildcardQuery(new Term("networkAccountStores", "*" + againValue + "*")), Occur.SHOULD);
					dataSearchQuery.add(new WildcardQuery(new Term("certificatesStores", "*" + againValue + "*")), Occur.SHOULD);
				}
				//创建的时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				break;
			// 领导讲话
			case "LeadSpeakStore":
				nrtSearch = LeadSpeakStoreNRTSearch.getInstance();
				storeList = new ArrayList<LeadSpeakStore>();
				// 任意字段
				String[]leadSpeakStoreFields={"infoType","title","meetingName","secretRank",
						"centent","involvedInTheField","attaches","attachContent"};
				for (String string : leadSpeakStoreFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				anyFieldQuery.add(new WildcardQuery(new Term("time", "*" + fieldValue + "*")), Occur.SHOULD);
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				// 时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				//再查询
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("time", "*" + againValue + "*")), Occur.SHOULD);
				}
				
				break;
			// 业务文档
			case "VocationalWorkStore":
				nrtSearch = VocationalWorkStoreNRTSearch.getInstance();
				storeList = new ArrayList<VocationalWorkStore>();
				// 任意字段
				String[]vwsFields={"infoType","docNumber","theOriginal","docName","involvedInTheField",
						"attaches","attachContent"};
				for (String string : vwsFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("time", "*" + fieldValue + "*")), Occur.SHOULD);
				// 时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				//再查询
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("time", "*" + againValue + "*")), Occur.SHOULD);
				}
				break;
			// 外来文档
			case "OutsideDocStore":
				nrtSearch = OutsideDocStoreNRTSearch.getInstance();
				storeList = new ArrayList<OutsideDocStore>();
				String[]odsFields={"involvedInTheField","infoType", "name","docNumber",
					"centent","attaches","attachContent","sourceUnit","secretRank"};
				for (String  string: odsFields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				anyFieldQuery.add(new WildcardQuery(new Term("time", "*" + fieldValue + "*")), Occur.SHOULD);
				// 时间
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				//再查询
				if (againValue.length()>0) {
					dataSearchQuery.add(new WildcardQuery(new Term("time", "*" + againValue + "*")), Occur.SHOULD);
				}
				break;
				
			// 线索库
			case "ClueStore":
				nrtSearch = ClueStoreNRTSearch.getInstance();
				storeList = new ArrayList<ClueStore>();
				// 线索里需要搜索的字段
				String[] fields = { "personName", "nameUsedBefore", "englishName", "registerAddress", "presentAddress",
						"workUnit", "personInfoType", "organizName", "orgHeadOfName", "degreeOfLatitude",
						"organizStoreInfoType", "organizBranchesNames", "organizPersonNames",
						"organizPublicActionNames", "name", "serverAddress", "establishAddress", "mainWholesaleAddress",
						"establishPerson", "websiteJournalStoreInfoType", "clueSource", "clueName",
						"arrangeAndEvolveCondition", "clueContent", "involvingObjName", "antecedents",
						"activityCondition", "websiteURL", "basicCondition" };
				for (String string : fields) {
					anyFieldQuery.add(getPhraseQuery(string, fieldValue), Occur.SHOULD);
					//再查询
					if (againValue.length()>0) {
						dataSearchQuery.add(getPhraseQuery(string, againValue), Occur.SHOULD);
					}
				}
				// 再加时间
				anyFieldQuery.add(new WildcardQuery(new Term("findTime", "*" + fieldValue + "*")), Occur.SHOULD);
				//anyFieldQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				if(againValue.length()>0){
					dataSearchQuery.add(new WildcardQuery(new Term("findTime", "*" + fieldValue + "*")), Occur.SHOULD);
					//dataSearchQuery.add(new WildcardQuery(new Term("createDate", "*" + fieldValue + "*")), Occur.SHOULD);
				}
				// 时间范围
				query = new TermRangeQuery("createDate", lowerTerm, upperTerm, true, true);
				break;
			}
			// 过滤条件
			filterQuery.add(query, Occur.MUST);
			//再一次查询
			BooleanQuery againQuery=new BooleanQuery();
			if (null == uid && null == oid && null != did) {
				// 读取所属部门
				filterQuery.add(new TermQuery(new Term("did", did)), Occur.MUST);
			}
			if (null == did && null == uid && null != oid) {
				// 读取所属组织
				filterQuery.add(new TermQuery(new Term("oid", oid)), Occur.MUST);
			}
			if (null == oid && null == did && null != uid) {
				// 读取个人数据
				filterQuery.add(new TermQuery(new Term("uid", uid)), Occur.MUST);
			}
			
			Filter filter = new QueryWrapperFilter(filterQuery);
			if (!StringUtil.isNull(fieldValue)) {
				// 任意字段查询
				totalCount=nrtSearch.getCount(anyFieldQuery, filter);
			}
			
		} catch (Exception e) {
			logger.info("依据全文检索查询异常");
			e.printStackTrace();
		}
		return totalCount;
	}
	/**
	 * 命中数转json输出
	 * @param fieldValue
	 * @param uid
	 * @param oid
	 * @param did
	 * @return
	 */
	public static String countToJson(String fieldValue,String uid,String oid,String did) {
		//七个库
		Class[] classes={PersonStore.class,WebsiteJournalStore.class,
				VocationalWorkStore.class,OutsideDocStore.class,LeadSpeakStore.class,ClueStore.class};
		JSONArray array=new JSONArray();
		for (Class clazz : classes) {
			JSONObject jsonObject=new JSONObject();
			String dataType=new String();
			String storeName=clazz.getSimpleName();
			//信息库中文名称
			if (storeName.equals("PersonStore")) {
				//权限
				dataType="重点人员库";
			}
			if (storeName.equals("OrganizStore")) {
				dataType="重点组织库";
			}
			if (storeName.equals("WebsiteJournalStore")) {
				dataType="媒体网站刊物库";
			}
			if (storeName.equals("VocationalWorkStore")) {
				dataType="业务文档库";
			}
			if (storeName.equals("ClueStore")) {
				dataType="线索库";
			}
			if (storeName.equals("OutsideDocStore")) {
				dataType="外来文档库";
			}
			if (storeName.equals("LeadSpeakStore")) {
				dataType="领导讲话库";
			}
			int count=getCount(fieldValue, uid, oid, did, clazz);
			//命中数大于0
			if (count>0) {
				jsonObject.put("storeName",storeName);
				jsonObject.put("dataType",dataType);
				jsonObject.put("dataCount", count);
				array.add(jsonObject);
			}
		}
		return array.toString();
	}
	/**
	 * 高亮ClueStore
	 * @param vo PagingObject封装的ClueStore集合
	 * @param hasValue
	 * @return json
	 * @throws Exception 
	 */
	public static String clueStoreVoToJson(PagingObject<ClueStore> vo,boolean hasValue) throws Exception {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		IClueRelationshipService clueRelationshipService=(IClueRelationshipService) SpringUtils.getBean("clueRelationshipServiceImpl");
		for (ClueStore clueStore : vo.getArray()) {
			if(null!=clueStore){
				//根据当前遍历的线索分别提取出当前遍历线索是否有人员，组织以及媒体刊物
				List<ClueRelationship> list = clueRelationshipService.findRelationshipByClueId(clueStore.getId());
				//声明是否存在涉及对象,默认为false
				String persionStore = "false";
				String organizStore = "false";
				String websiteJournalStore = "false";
				for (ClueRelationship r : list) {
					if("websiteJournalStore".equals(r.getDataType())){
						websiteJournalStore = "true";
						break;
					}
				}
				for (ClueRelationship r : list) {
					if("organizStore".equals(r.getDataType())){
						organizStore = "true";
						break;
					}
				}
				for (ClueRelationship r : list) {
					if("personStore".equals(r.getDataType())){
						persionStore = "true";
						break;
					}
				}
				String icon = "persionStore:"+persionStore+",organizStore:"+organizStore+",websiteJournalStore:"+websiteJournalStore+"";
				JSONObject obj = new JSONObject();
				obj.put("id", clueStore.getId());
				obj.put("createDate", clueStore.getCreateDate());
				obj.put("icon", icon);
				obj.put("isEnable", clueStore.getIsEnable());
				Document document=documentHashMap.get(clueStore.getId());
				Query query=queryHashMap.get(clueStore.getId());
				if(hasValue){
					obj.put("clueName", highlighterFiled(document, query, "clueName"));
					obj.put("clueSource", highlighterFiled(document, query, "clueSource"));
					obj.put("findTime", highlighterFiled(document, query, "findTime"));
					obj.put("clueContent", highlighterFiled(document, query, "clueContent"));
					obj.put("arrangeAndEvolveCondition",highlighterFiled(document, query, "arrangeAndEvolveCondition"));
				}else{
					obj.put("clueName", clueStore.getClueName());
					obj.put("clueSource", clueStore.getClueSource());
					obj.put("findTime", clueStore.getFindTime());
					obj.put("clueContent", clueStore.getClueContent());
					obj.put("arrangeAndEvolveCondition",clueStore.getArrangeAndEvolveCondition());
				}
				array.add(obj);
			}
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 高亮PersonStore
	 * @param vo PagingObject封装的PersonStore集合
	 * @param hasValue 
	 * @return
	 */
	public static String personStoreVoToJson(PagingObject<PersonStore> vo,boolean hasValue) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (PersonStore PersonStore : vo.getArray()) {
			if (null!=PersonStore) {
				JSONObject obj = new JSONObject();
				obj.put("id", PersonStore.getId());
				obj.put("isEnable",PersonStore.getIsEnable());
				obj.put("createDate", PersonStore.getCreateDate());
				//附件
				obj.put("appendix", PersonStore.getAppendix());
				//照片
				obj.put("photo", PersonStore.getPhotofraphWay());
				Document document=documentHashMap.get(PersonStore.getId());
				Query query=queryHashMap.get(PersonStore.getId());
				if(hasValue){
					obj.put("personName", highlighterFiled(document, query, "personName"));
					obj.put("nameUsedBefore", highlighterFiled(document, query, "nameUsedBefore"));
					obj.put("infoType", highlighterFiled(document, query, "infoType"));
					obj.put("englishName", highlighterFiled(document, query, "englishName"));
					obj.put("bebornTime", highlighterFiled(document, query, "bebornTime"));
					obj.put("presentAddress",highlighterFiled(document, query, "presentAddress"));
					obj.put("workUnit", highlighterFiled(document, query, "workUnit"));
					obj.put("registerAddress", highlighterFiled(document, query, "registerAddress"));
					obj.put("antecedents", highlighterFiled(document, query, "antecedents"));
					obj.put("activityCondition", highlighterFiled(document, query, "activityCondition"));
					obj.put("sex", highlighterFiled(document, query, "sex"));
				}else{
					obj.put("personName", PersonStore.getPersonName());
					obj.put("nameUsedBefore", PersonStore.getNameUsedBefore());
					obj.put("englishName", PersonStore.getEnglishName());
					String bebornTime=PersonStore.getBebornTime();
					if(StringUtils.length(bebornTime)>=10){
						obj.put("bebornTime", PersonStore.getBebornTime().substring(0, 10));
					}else{
						obj.put("bebornTime", PersonStore.getBebornTime());
					}
					obj.put("presentAddress", PersonStore.getPresentAddress());
					obj.put("workUnit", PersonStore.getWorkUnit());
					obj.put("registerAddress", PersonStore.getRegisterAddress());
					obj.put("antecedents", PersonStore.getAntecedents());
					obj.put("activityCondition", PersonStore.getActivityCondition());
					obj.put("sex", PersonStore.getSex());
					//为空的情况
					if(PersonStore.getInfoType()!=null){
						//类别可能被删除
						//人员关联的类别就为空了
						obj.put("infoType", PersonStore.getInfoType().getTypeName());
					}
				}
				//证件
				Set<CertificatesStore> set1=PersonStore.getCertificatesStores();
				JSONArray cjArray=new JSONArray();
				for (CertificatesStore c : set1) {
					JSONObject cObject=new JSONObject();
					//为空
					if(c.getInfoType()!=null&& c.getInfoType().getTypeName().trim().length()>0){
						cObject.put("certificatesType", c.getInfoType().getTypeName());
						cObject.put("certificatesTypeNumber", c.getCertificatesNumber());
						//需要放到array中
						cjArray.add(cObject);
						obj.put("certificates",cjArray.toString());
					}
				}
				//网络账号
				Set<NetworkAccountStore> set=PersonStore.getNetworkAccountStores();
				JSONArray nArray=new JSONArray();
				for (NetworkAccountStore networkAccountStore : set) {
					JSONObject networkObject=new JSONObject();
					if(networkAccountStore.getInfoType()!=null&& networkAccountStore.getInfoType().getTypeName().trim().length()>0){
						networkObject.put("networkAccountType", networkAccountStore.getInfoType().getTypeName());
						networkObject.put("networkAccountTypeNumber", networkAccountStore.getNetworkNumber());
						nArray.add(networkObject);
						obj.put("networkaccount",nArray.toString());
					}
				}
				array.add(obj);
			}
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 高亮显示OrganizStore
	 * @param vo PagingObject封装的OrganizStore集合
	 * @param hasValue
	 * @return json 
	 */
	/*public static String organizStoreVoToJson(PagingObject<OrganizStore> vo,boolean hasValue){
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizStore or : vo.getArray()) {
			if (null!=or) {
				JSONObject obj = new JSONObject();
				obj.put("id", or.getId());
				if (null!=or.getIsEnable()) {
					obj.put("isEnable",or.getIsEnable());
				}else{
					obj.put("isEnable","1");
				}
				obj.put("createDate", or.getCreateDate());
				Document document=documentHashMap.get(or.getId());
				Query query=queryHashMap.get(or.getId());
				if(hasValue){
					//高亮
					obj.put("organizName", highlighterFiled(document, query, "organizName"));
					obj.put("orgHeadOfName", highlighterFiled(document, query, "orgHeadOfName"));
					//为空的条件
					if(or.getInfoType()!=null){
						
						obj.put("infoType", highlighterFiled(document, query, "infoType"));
						obj.put("infoTypeId", or.getInfoType().getId());
					}
					obj.put("websiteURL", highlighterFiled(document, query, "websiteURL"));
					obj.put("organizPublicActionNames", highlighterFiled(document, query, "organizPublicActionNames"));
					obj.put("organizPersonNames",highlighterFiled(document, query, "organizPersonNames"));
					obj.put("organizBranchesNames", highlighterFiled(document, query, "organizBranchesNames"));
					obj.put("foundTime", highlighterFiled(document, query, "foundTime"));
					obj.put("degreeOfLatitude",highlighterFiled(document, query, "degreeOfLatitude"));
					obj.put("basicCondition",highlighterFiled(document, query, "basicCondition"));
					obj.put("activityCondition",highlighterFiled(document, query, "activityCondition"));
				}else{
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
				}
				array.add(obj);
			}
		}
		root.element("datas", array);
		return root.toString();
	}*/
	/**
	 * 高亮显示WebsiteJournalStore
	 * @param vo PagingObject封装的WebsiteJournalStore集合
	 * @param hasValue 有无关键字
	 * @return json串
	 */
	public static String websiteJournalStoreVoToJson(PagingObject<WebsiteJournalStore> vo,boolean hasValue){
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (WebsiteJournalStore store : vo.getArray()) {
			if(null!=store){
				JSONObject obj = new JSONObject();
				obj.put("id", store.getId());
				obj.put("isEnable", store.getIsEnable());
				obj.put("createDate", store.getCreateDate());
				Document document=documentHashMap.get(store.getId());
				Query query=queryHashMap.get(store.getId());
				if (hasValue) {
					obj.put("name", highlighterFiled(document, query, "name"));
					obj.put("websiteURL",  highlighterFiled(document, query, "websiteURL"));
					obj.put("serverAddress",  highlighterFiled(document, query, "serverAddress"));
					obj.put("establishAddress",  highlighterFiled(document, query, "establishAddress"));
					obj.put("mainWholesaleAddress", highlighterFiled(document, query, "mainWholesaleAddress"));
					obj.put("establishPerson",  highlighterFiled(document, query, "establishPerson"));
					obj.put("establishTime", highlighterFiled(document, query, "establishTime"));
					obj.put("basicCondition", highlighterFiled(document, query, "basicCondition"));
					// 类别不为空
					if (store.getInfoType() != null) {
						obj.put("infoType",  highlighterFiled(document, query, "infoType"));
					}
				}else{
					obj.put("name", store.getName());
					obj.put("websiteURL", store.getWebsiteURL());
					obj.put("serverAddress", store.getServerAddress());
					obj.put("establishAddress", store.getEstablishAddress());
					obj.put("mainWholesaleAddress", store.getMainWholesaleAddress());
					obj.put("establishPerson", store.getEstablishPerson());
					obj.put("establishTime", store.getEstablishTime());
					obj.put("basicCondition", store.getBasicCondition());
					// 类别不为空
					if (store.getInfoType() != null) {
						obj.put("infoType", store.getInfoType().getTypeName());
					}
				}
				array.add(obj);
			}
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 高亮输出LeadSpeakStore
	 * @param vo PagingObject封装的LeadSpeakStore集合对象
	 * @param hasValue 是否输入了值
	 * @return 转JSON后输出
	 */
	public static String leadSpeakStoreVoToJson(PagingObject<LeadSpeakStore> vo,boolean hasValue) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (LeadSpeakStore store : vo.getArray()) {
			if (null!=store) {
				JSONObject obj = new JSONObject();
				obj.put("id", store.getId());
				obj.put("createDate", store.getCreateDate());
				obj.put("attaches", store.getAttaches());
				obj.put("centent", store.getCentent());
				Document document=documentHashMap.get(store.getId());
				Query query=queryHashMap.get(store.getId());
				if (hasValue) {
					//输入了值才高亮
					obj.put("title", highlighterFiled(document,query,"title"));
					obj.put("time", highlighterFiled(document,query,"time"));
					obj.put("meetingName", highlighterFiled(document,query,"meetingName"));
					obj.put("secretRank", highlighterFiled(document,query,"secretRank"));
					//obj.put("centent", highlighterFiled(document,query,"centent"));
					if(store.getInvolvedInTheField()!=null){
						//不为空
						obj.put("involvedInTheField", highlighterFiled(document,query,"involvedInTheField"));
					}
					//类别不为空
					if(store.getInfoType()!=null){
						obj.put("infoType", highlighterFiled(document,query,"infoType"));
					}
				}else{
					obj.put("title", store.getTitle());
					obj.put("time", store.getTime());
					obj.put("meetingName", store.getMeetingName());
					obj.put("secretRank",store.getSecretRank());
					
					//obj.put("createDate",  highlighterFiled("createDate"));
					if(store.getInvolvedInTheField()!=null){
						//不为空
						obj.put("involvedInTheField",store.getInvolvedInTheField().getTypeName());
					}
					//类别不为空
					if(store.getInfoType()!=null){
						obj.put("infoType", store.getInfoType().getTypeName());
					}
				}
				array.add(obj);
			}
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 高亮输出OutsideDocStore
	 * @param vo PagingObject封装的OutsideDocStore集合对象
	 * @param hasValue 是否输入了关键字
	 * @return 转JSON后输出
	 */
	public static String outsideDocStoreVoToJson(PagingObject<OutsideDocStore> vo,boolean hasValue) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OutsideDocStore store : vo.getArray()) {
			if(null!=store){
				JSONObject obj = new JSONObject();
				Document document=documentHashMap.get(store.getId());
				Query query=queryHashMap.get(store.getId());
				obj.put("id", store.getId());
				obj.put("createDate", store.getCreateDate());
				obj.put("attaches", store.getAttaches());
				obj.put("centent", store.getCentent());
				if (hasValue) {
					obj.put("name", highlighterFiled(document,query,"name"));
					obj.put("sourceUnit", highlighterFiled(document,query,"sourceUnit"));
					obj.put("title", highlighterFiled(document,query,"title"));
					obj.put("time", highlighterFiled(document,query,"time"));
					obj.put("secretRank", highlighterFiled(document,query,"secretRank"));
					obj.put("docNumber",  highlighterFiled(document,query,"docNumber"));
					if (store.getInvolvedInTheField() != null) {
						// 不为空
						obj.put("involvedInTheField", highlighterFiled(document,query,"involvedInTheField"));
					}
					if (store.getInfoType() != null) {
						// 类别不为空
						obj.put("infoType", highlighterFiled(document,query,"infoType"));
					}
				}else{
					obj.put("name", store.getName());
					obj.put("time", store.getTime());
					obj.put("docNumber", store.getDocNumber());
					obj.put("sourceUnit", store.getSourceUnit());
					obj.put("secretRank", store.getSecretRank());
					/*
					obj.put("title", store.getTitle());
					*/
					if (store.getInvolvedInTheField() != null) {
						// 不为空
						obj.put("involvedInTheField", store.getInvolvedInTheField().getTypeName());
					}
					// 类别不为空
					if (store.getInfoType() != null) {

						obj.put("infoType", store.getInfoType().getTypeName());
					}
				}
				array.add(obj);
			}
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 高亮输出VocationalWorkStore
	 * @param vo PagingObject封装的VocationalWorkStore集合对象
	 * @param hasValue 是否输入关键词
	 * @return json字符串
	 */
	public static String vocationalWorkStoreVoToJson(PagingObject<VocationalWorkStore> vo,boolean hasValue) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (VocationalWorkStore store : vo.getArray()) {
			if(null!=store){
				JSONObject obj = new JSONObject();
				obj.put("id", store.getId());
				Document document=documentHashMap.get(store.getId());
				Query query=queryHashMap.get(store.getId());
				obj.put("createDate", store.getCreateDate());
				obj.put("attaches", store.getAttaches());
				//原文内容
				String theOriginal=store.getTheOriginal();
				/*if (StringUtils.isEmpty(theOriginal)) {
					//读取附件内容
					theOriginal=readWord(store.getAttaches());
				}*/
				obj.put("theOriginal", theOriginal);
				if (hasValue) {
					obj.put("docName", highlighterFiled(document,query,"docName"));
					obj.put("docNumber", highlighterFiled(document,query,"docNumber"));
					obj.put("time", highlighterFiled(document,query,"time"));
					//obj.put("theOriginal", highlighterFiled(document,query,"theOriginal"));
					/*if (store.getInvolvedInTheField() != null) {
						// 不为空
						obj.put("involvedInTheField", highlighterFiled(document,query,"involvedInTheField"));
					}*/
					// 类别不为空
					if (store.getInfoType() != null) {
						obj.put("infoType", highlighterFiled(document,query,"infoType"));
					}
					array.add(obj);
				}else{
					obj.put("docName", store.getDocName());
					obj.put("docNumber", store.getDocNumber());
					obj.put("time", store.getTime());
					
					/*if (store.getInvolvedInTheField() != null) {
						// 不为空
						obj.put("involvedInTheField", store.getInvolvedInTheField().getTypeName());
					}*/
					// 类别不为空
					if (store.getInfoType() != null) {
						obj.put("infoType", store.getInfoType().getTypeName());
					}
					array.add(obj);
				}
			}
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 将分词后的短语拼接,进行短语查询,这样能达到精确匹配的效果
	 * 
	 * @param filed
	 *            查询字段
	 * @param fieldValue
	 *            关键词
	 * @return PhraseQuery
	 */
	public static PhraseQuery getPhraseQuery(String filed, String fieldValue) {
		StringReader reader = null;
		PhraseQuery phraseQuery = new PhraseQuery();
		try {
			phraseQuery.setSlop(1);
			reader = new StringReader(fieldValue);
			// 获得具体的分词
			IKSegmentation ikSegmentation = new IKSegmentation(reader, true);
			Lexeme lexeme = null;
			while ((lexeme = ikSegmentation.next()) != null) {
				// 把分词信息拼接短语查询
				phraseQuery.add(new Term(filed, lexeme.getLexemeText()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				// 关闭流
				reader.close();
			}
		}
		return phraseQuery;
	}
	/**
	 * 给字段做高亮显示
	 * @param filed 字段名称
	 * @return 高亮后的结果
	 */
	private static String highlighterFiled(Document document,Query query,String filed) {
		Analyzer analyzer = new IKAnalyzer(true);
		// 默认是没有高亮
		String result = document.get(filed);
		SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style='background-color:#ffd73a'>","</span>");
		// 哪个query条件做高亮
		Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));
		TokenStream tokenStream = null;
		StringReader reader = null;
		try {
			if(null!=result){
				reader = new StringReader(result);
				tokenStream = analyzer.tokenStream(filed, reader);
				/***********原来这中写法导致stream close,弄了很长时间***********/
				/*if (null != highlighter.getBestFragment(tokenStream, document.get(filed))) {
					
					result = highlighter.getBestFragment(tokenStream, document.get(filed));
				}*/
				String highlighterValue= highlighter.getBestFragment(tokenStream, document.get(filed));
				if(null!=highlighterValue){
					// 做高亮
					result =highlighterValue;
				}
			}
			
		} catch (Exception e) {
			//result="";
			e.printStackTrace();
		} finally {
			try {
				// 关闭TokenStream
				if (null != tokenStream) {
					tokenStream.close();
				}
				//关闭reader
				if (null != reader) {
					reader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
