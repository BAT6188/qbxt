package com.ushine.luceneindex.index;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ushine.common.utils.SpringUtils;
import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.ClueRelationship;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.OrganizStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;
import com.ushine.storesinfo.service.IClueRelationshipService;

/**
 * 单例的线索库索引操作
 * 
 * @author dh
 *
 */
public class ClueStoreNRTSearch implements IStoreNRTSearch {
	private static IndexWriter indexWriter;
	private static IndexReader reader;
	private static NRTManager nrtManager;
	private static Directory directory;
	private static ClueStoreNRTSearch clueStoreNRTSearch = null;
	private static Logger logger = Logger.getLogger(ClueStoreNRTSearch.class);
	private static String path = null;
	private static NRTManagerReopenThread thread;

	private ClueStoreNRTSearch() {
		// 私有构造函数
	}

	/**
	 * 创建新的ClueStore索引库
	 * 
	 * @param stores
	 *            ClueStore集合
	 */
	public void createIndex(List<ClueStore> stores) {
		try {
			List<Document> documents = new ArrayList<Document>();
			// 清空原来的
			nrtManager.deleteAll();
			for (ClueStore store : stores) {
				// 加到集合中
				documents.add(addDocument(store));
			}
			nrtManager.addDocuments(documents);
			// 创建完之后务必提交
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("线索库创建新的索引库失败");
			e.printStackTrace();
		}
	}

	/**
	 * 添加一条新索引
	 * 
	 * @param store
	 *            ClueStore对象
	 */
	public void addIndex(ClueStore store) {
		try {
			Document doc = addDocument(store);
			nrtManager.addDocument(doc);
		} catch (Exception e) {
			logger.info("线索库添加新的索引失败");
			e.printStackTrace();
		}
	}

	/**
	 * 添加多条索引
	 * 
	 * @param stores
	 *            ClueStore对象集合
	 */
	public void addIndex(List<ClueStore> stores) {
		List<Document> docs = new ArrayList<Document>();
		try {
			for (ClueStore personStore : stores) {
				docs.add(addDocument(personStore));
			}
			// 添加到索引库中
			nrtManager.addDocuments(docs);
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("线索库添加新的索引集合失败");
			e.printStackTrace();
		}
	}

	/**
	 * 更新一条索引
	 * 
	 * @param id
	 *            ClueStore的id
	 * @param store
	 *            ClueStore对象
	 */
	public void updateIndex(String id, ClueStore store) {
		try {
			// 先删除原来的
			Term term = new Term("id", id);
			// 添加一条新的
			Document doc = addDocument(store);
			nrtManager.updateDocument(term, doc);
		} catch (Exception e) {
			logger.info("线索库索引更新失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除一条索引
	 * 
	 * @param id
	 *            ClueStore的id
	 */
	public void deleteIndex(String id) {
		try {
			Term term = new Term("id", id);
			nrtManager.deleteDocuments(term);
		} catch (Exception e) {
			logger.info("线索库索引删除失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除多条索引
	 * 
	 * @param ids
	 *            ClueStore的id数组
	 */
	public void deleteIndex(String[] ids) {
		try {
			for (String id : ids) {
				Term term = new Term("id", id);
				nrtManager.deleteDocuments(term);
			}
		} catch (Exception e) {
			logger.info("线索库索引删除失败");
			e.printStackTrace();
		}
	}

	/**
	 * 将ClueStore转成Document对象
	 * 
	 * @param store
	 *            ClueStore对象
	 */
	@SuppressWarnings("unchecked")
	private Document addDocument(ClueStore store) {
		Document document = new Document();
		try {
			IBaseDao baseDao = (IBaseDao) SpringUtils.getBean("baseDao");
			document.add(new Field("id", store.getId(), Store.YES, Index.NOT_ANALYZED));
			// **************************线索涉及的人员、组织、媒体网站也加入索引中***************************/
			String hql = "from ClueRelationship where clueId='" + store.getId() + "'";
			List<ClueRelationship> list = baseDao.findByHql(hql);
			// 人员
			List<PersonStore> personStores = new ArrayList<PersonStore>();
			// 组织
			List<OrganizStore> organizStores = new ArrayList<OrganizStore>();
			// 媒体网站
			List<WebsiteJournalStore> websiteJournalStores = new ArrayList<WebsiteJournalStore>();
			for (ClueRelationship relationship : list) {
				if ("personStore".equals(relationship.getDataType())) {
					personStores.add((PersonStore) baseDao.findById(PersonStore.class, relationship.getLibraryId()));
				}
				if ("organizStore".equals(relationship.getDataType())) {
					organizStores.add((OrganizStore) baseDao.findById(OrganizStore.class, relationship.getLibraryId()));
				}
				if ("websiteJournalStore".equals(relationship.getDataType())) {
					websiteJournalStores.add((WebsiteJournalStore) baseDao.findById(WebsiteJournalStore.class,
							relationship.getLibraryId()));
				}
			}
			// 把人员基本信息加入索引中
			StringBuffer personName = new StringBuffer();// 姓名
			StringBuffer nameusedbefore = new StringBuffer();// 曾用名
			StringBuffer englishName = new StringBuffer();// 英文名
			StringBuffer registerAddress = new StringBuffer();// 户籍地址
			StringBuffer presentAddress = new StringBuffer();// 现住地址
			StringBuffer workUnit = new StringBuffer();// 工作单位
			StringBuffer personInfoType = new StringBuffer();// 涉及类别
			StringBuffer antecedents = new StringBuffer();// 履历
			StringBuffer activityCondition = new StringBuffer();// 主要活动情况
			for (PersonStore personStore : personStores) {
				personName.append(getValue(personStore.getPersonName()));
				nameusedbefore.append(getValue(personStore.getNameUsedBefore()));
				englishName.append(getValue(personStore.getEnglishName()));
				registerAddress.append(getValue(personStore.getRegisterAddress()));
				presentAddress.append(getValue(personStore.getPresentAddress()));
				workUnit.append(getValue(personStore.getWorkUnit()));
				if (null != personStore.getInfoType()) {
					// 未入库时InfoType为null
					personInfoType.append(getValue(personStore.getInfoType().getTypeName()));
				}
				antecedents.append(getValue(personStore.getAntecedents()));
				activityCondition.append(getValue(personStore.getActivityCondition()));
			}
			document.add(new Field("personName", bufferToString(personName), Store.YES, Index.ANALYZED));
			document.add(new Field("nameUsedBefore", bufferToString(nameusedbefore), Store.YES, Index.ANALYZED));
			document.add(new Field("englishName", bufferToString(englishName), Store.YES, Index.ANALYZED));
			document.add(new Field("registerAddress", bufferToString(registerAddress), Store.YES, Index.ANALYZED));
			document.add(new Field("presentAddress", bufferToString(presentAddress), Store.YES, Index.ANALYZED));
			document.add(new Field("workUnit", bufferToString(workUnit), Store.YES, Index.ANALYZED));
			document.add(new Field("personInfoType", bufferToString(personInfoType), Store.YES, Index.ANALYZED));
			document.add(new Field("antecedents", bufferToString(antecedents), Store.NO, Index.ANALYZED));
			document.add(new Field("activityCondition", bufferToString(activityCondition), Store.NO, Index.ANALYZED));
			// 把组织信息加入索引中
			StringBuffer organizName = new StringBuffer();
			StringBuffer orgHeadOfName = new StringBuffer();
			StringBuffer degreeOfLatitude = new StringBuffer();
			StringBuffer organizStoreInfoType = new StringBuffer();
			StringBuffer organizBranchesNames = new StringBuffer();
			StringBuffer organizPersonNames = new StringBuffer();
			StringBuffer organizPublicActionNames = new StringBuffer();
			StringBuffer websiteURL = new StringBuffer();
			StringBuffer basicCondition = new StringBuffer();
			for (OrganizStore organizStore : organizStores) {
				// 名称、负责人、活动范围、类型、分支、成员、刊物、网址、基本情况和活动情况
				organizName.append(getValue(organizStore.getOrganizName()));
				orgHeadOfName.append(getValue(organizStore.getOrgHeadOfName()));
				degreeOfLatitude.append(getValue(organizStore.getDegreeOfLatitude()));
				if (null != organizStore.getInfoType()) {
					// 未入库时为null
					organizStoreInfoType.append(getValue(organizStore.getInfoType().getTypeName()));
				}
				organizBranchesNames.append(getValue(organizStore.getOrganizBranchesNames()));
				organizPersonNames.append(getValue(organizStore.getOrganizPersonNames()));
				organizPublicActionNames.append(getValue(organizStore.getOrganizPublicActionNames()));
				websiteURL.append(getValue(organizStore.getWebsiteURL()));
				basicCondition.append(getValue(organizStore.getBasicCondition()));
				activityCondition.append(getValue(organizStore.getActivityCondition()));
			}
			document.add(new Field("organizName", bufferToString(organizName), Store.YES, Index.ANALYZED));
			document.add(new Field("orgHeadOfName", bufferToString(orgHeadOfName), Store.YES, Index.ANALYZED));
			document.add(new Field("degreeOfLatitude", bufferToString(degreeOfLatitude), Store.YES, Index.ANALYZED));
			document.add(
					new Field("organizStoreInfoType", bufferToString(organizStoreInfoType), Store.YES, Index.ANALYZED));
			document.add(
					new Field("organizBranchesNames", bufferToString(organizBranchesNames), Store.YES, Index.ANALYZED));
			document.add(
					new Field("organizPersonNames", bufferToString(organizPersonNames), Store.YES, Index.ANALYZED));
			document.add(new Field("organizPublicActionNames", bufferToString(organizPublicActionNames), Store.YES,
					Index.ANALYZED));
			document.add(new Field("websiteURL", bufferToString(websiteURL), Store.YES, Index.ANALYZED));
			document.add(new Field("activityCondition", bufferToString(activityCondition), Store.NO, Index.ANALYZED));
			// 把媒体网站信息加入索引中 name
			StringBuffer name = new StringBuffer();
			StringBuffer serverAddress = new StringBuffer();
			StringBuffer establishAddress = new StringBuffer();
			StringBuffer mainWholesaleAddress = new StringBuffer();
			StringBuffer establishPerson = new StringBuffer();
			StringBuffer websiteJournalStoreInfoType = new StringBuffer();
			for (WebsiteJournalStore websiteJournalStore : websiteJournalStores) {
				// 名称、服务器所在地、发行地、创建人、类型、基本情况
				name.append(getValue(websiteJournalStore.getName()));
				serverAddress.append(getValue(websiteJournalStore.getServerAddress()));
				establishAddress.append(getValue(websiteJournalStore.getEstablishAddress()));
				mainWholesaleAddress.append(getValue(websiteJournalStore.getMainWholesaleAddress()));
				establishPerson.append(getValue(websiteJournalStore.getEstablishPerson()));
				if (null != websiteJournalStore.getInfoType()) {
					// 未入库为空
					websiteJournalStoreInfoType.append(getValue(websiteJournalStore.getInfoType().getTypeName()));
				}
				basicCondition.append(getValue(websiteJournalStore.getBasicCondition()));
			}
			document.add(new Field("name", bufferToString(name), Store.YES, Index.ANALYZED));
			document.add(new Field("serverAddress", bufferToString(serverAddress), Store.YES, Index.ANALYZED));
			document.add(new Field("establishAddress", bufferToString(establishAddress), Store.YES, Index.ANALYZED));
			document.add(
					new Field("mainWholesaleAddress", bufferToString(mainWholesaleAddress), Store.YES, Index.ANALYZED));
			document.add(new Field("establishPerson", bufferToString(establishPerson), Store.YES, Index.ANALYZED));
			document.add(new Field("websiteJournalStoreInfoType", bufferToString(websiteJournalStoreInfoType),
					Store.YES, Index.ANALYZED));
			document.add(new Field("basicCondition", bufferToString(basicCondition), Store.NO, Index.ANALYZED));

			/************* 线索本身的信息 **************/
			if (null != store.getClueSource()) {
				document.add(new Field("clueSource", store.getClueSource(), Store.YES, Index.ANALYZED));
			}
			if (null != store.getClueName()) {
				document.add(new Field("clueName", store.getClueName(), Store.YES, Index.ANALYZED));
			}
			if (null != store.getFindTime()) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(store.getFindTime());
				document.add(new Field("findTime", format.format(date), Store.YES, Index.NOT_ANALYZED));
			}
			// 工作部署及进展情况
			if (null != store.getArrangeAndEvolveCondition()) {
				document.add(new Field("arrangeAndEvolveCondition", store.getArrangeAndEvolveCondition(), Store.YES,
						Index.ANALYZED));
			}
			// 内容
			if (null != store.getClueContent()) {
				document.add(new Field("clueContent", store.getClueContent(), Store.YES, Index.ANALYZED));
			}
			// 涉及对象
			if (null != store.getInvolvingObjName()) {
				document.add(new Field("involvingObjName", store.getInvolvingObjName(), Store.YES, Index.ANALYZED));
			}
			// 创建日期
			if (null != store.getCreateDate()) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(store.getCreateDate());
				document.add(
						new Field("createDate", store.getCreateDate().substring(0, 10), Store.YES, Index.NOT_ANALYZED));
			}
			if (null != store.getUid()) {
				document.add(new Field("uid", store.getUid(), Store.YES, Index.NOT_ANALYZED));
			}
			if (null != store.getOid()) {
				document.add(new Field("oid", store.getOid(), Store.YES, Index.NOT_ANALYZED));
			}
			if (null != store.getDid()) {
				document.add(new Field("did", store.getDid(), Store.YES, Index.NOT_ANALYZED));
			}
		} catch (Exception e) {
			logger.info("线索库转Document失败");
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 字符串拼接,先判断是否为空
	 * 
	 * @param value
	 * @return value为null返回空串,防止异常出现
	 */
	private String getValue(String value) {
		String result = "";
		if (null != value) {
			result = value + ",";
		}
		return result;
	}

	/**
	 * StringBuffer转到String
	 * 
	 * @param buffer
	 *            StringBuffer对象
	 * @return String
	 */
	private String bufferToString(StringBuffer buffer) {
		String value = "";
		if (buffer.toString().length() > 1) {
			value = buffer.toString().substring(0, buffer.toString().length() - 1);
		}
		return value;
	}

	/**
	 * 单例的ClueStoreNRTSearch
	 * 
	 * @return ClueStoreNRTSearch
	 */
	static{
		/*try {
			clueStoreNRTSearch = new ClueStoreNRTSearch();
			directory = FSDirectory.open(new File(path));
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer(true));
			indexWriter = new IndexWriter(directory, conf);
			
			nrtManager = new NRTManager(indexWriter, new SearcherWarmer() {
				public void warm(IndexSearcher indexsearcher) throws IOException {
					// 索引变化时
					logger.info("线索库索引信息reopen");
				}
			});

			// 线程后台运行
			// NRTManagerReopenThread thread = new
			// NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			thread = new NRTManagerReopenThread(nrtManager, 0.01, 0.001);
			thread.setName("线索库索引线程");
			thread.setDaemon(true);
			thread.start();
		} catch (Exception e) {
			logger.info("线索库创建索引异常");
			e.printStackTrace();
		}*/
	}
	public static ClueStoreNRTSearch getInstance() {
		return clueStoreNRTSearch;
	}

	/*********************** 查询接口 *********************/
	/**
	 * 获得索引库索引总数
	 * 
	 * @return 总数
	 */
	public int getTotalCount() {
		int count = 0;
		try {
			// 这种不包括被删除的
			count = reader.maxDoc();
			// 包括删除的是 IndexSearcher.maxDoc()这个方法
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					// reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}

	@Override
	public int getCount(Query query, Filter filter) {
		// 查询符合条件的线索数量
		int totalCount = 0;
		ScoreDoc[] scoreDocs = null;
		IndexSearcher searcher = null;
		try {
			// 获得searcher对象
			TopDocs topDocs = null;
			searcher = nrtManager.getSearcherManager(true).acquire();
			if (null != filter) {
				topDocs = searcher.search(query, filter, Integer.MAX_VALUE);
			} else {
				topDocs = searcher.search(query, Integer.MAX_VALUE);
			}
			scoreDocs = topDocs.scoreDocs;
			totalCount = scoreDocs.length;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSearcher(searcher);
		}
		return totalCount;
	}

	@Override
	public List<Document> getDocuments(Query query, Filter filter, int nowPage, int pageSize) {
		// 查询符合条件的线索
		IndexSearcher searcher = null;
		List<Document> list = new ArrayList<Document>();
		ScoreDoc[] scoreDocs = null;
		try {
			searcher = nrtManager.getSearcherManager(true).acquire();
			TopDocs topDocs = null;
			// 过滤条件
			if (null != filter) {
				topDocs = searcher.search(query, filter, Integer.MAX_VALUE);
			} else {
				topDocs = searcher.search(query, Integer.MAX_VALUE);
			}
			scoreDocs = topDocs.scoreDocs;
			if (scoreDocs.length > 0) {
				// 无需-1
				for (int i = ((nowPage - 1) * pageSize); i < (nowPage * pageSize); i++) {
					if (i == scoreDocs.length) {
						break;
					}
					ScoreDoc scoreDoc = scoreDocs[i];
					Document document = searcher.doc(scoreDoc.doc);
					list.add(document);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭searcher
			closeSearcher(searcher);
		}
		return list;
	}

	/**
	 * 关闭IndexSearcher
	 */
	private final void closeSearcher(IndexSearcher searcher) {
		// 关闭searcher
		if (searcher != null) {
			try {
				// 关闭资源
				nrtManager.getSearcherManager(true).release(searcher);
				// 不需要自己关闭
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 在程序关闭时调用,关闭索引
	 */
	public void closeManager() {
		try {
			thread.interrupt();
			nrtManager.close();
			indexWriter.commit();
			indexWriter.close();
		} catch (Exception e) {
			logger.info("关闭索引库异常");
		}
	}

	/**
	 * 分页并排序
	 */
	@Override
	public List<Document> getDocuments(Query query, Filter filter, String sortFiled, String dir, int nowPage,
			int pageSize) {
		IndexSearcher searcher = null;
		List<Document> list = new ArrayList<Document>();
		ScoreDoc[] scoreDocs = null;
		try {
			searcher = nrtManager.getSearcherManager(true).acquire();
			TopDocs topDocs = null;
			// 排序方向
			boolean reverse = true;
			if (StringUtils.isNotEmpty(dir) && dir.equals("ASC")) {
				reverse = false;
				logger.info(String.format("排序方向：%s", "降序"));
			} else {
				logger.info(String.format("排序方向：%s", "升序"));
			}
			// 默认排序字段：创建时间
			Sort sort = new Sort(new SortField("createDate", SortField.STRING, reverse));
			if (!StringUtils.isEmpty(sortFiled)) {
				// 排序字段 &&!StringUtils.equals(sortFiled, "null")
				logger.info(String.format("当前排序字段：%s", sortFiled));
				sort = new Sort(new SortField(sortFiled, SortField.STRING, reverse));
			}
			if (null != filter) {
				topDocs = searcher.search(query, filter, Integer.MAX_VALUE, sort);
			} else {
				topDocs = searcher.search(query, Integer.MAX_VALUE, sort);
			}
			scoreDocs = topDocs.scoreDocs;
			if (scoreDocs.length > 0) {
				// 分页
				for (int i = ((nowPage - 1) * pageSize); i < (nowPage * pageSize); i++) {
					if (i == scoreDocs.length) {
						break;
					}
					ScoreDoc scoreDoc = scoreDocs[i];
					Document document = searcher.doc(scoreDoc.doc);
					list.add(document);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭searcher
			closeSearcher(searcher);
		}
		return list;
	}
}
