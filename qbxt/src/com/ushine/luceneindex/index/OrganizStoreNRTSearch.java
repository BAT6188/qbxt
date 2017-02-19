package com.ushine.luceneindex.index;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ushine.common.utils.SpringUtils;
import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.OrganizStore;

/**
 * 组织库的索引读写操作
 * 
 * @author dh
 *
 */
public class OrganizStoreNRTSearch implements IStoreNRTSearch {
	private static NRTManager nrtManager;
	private static Directory directory;
	private static OrganizStoreNRTSearch organizStoreNRTSearch = null;
	private static IndexWriter indexWriter;
	private static IndexReader reader;
	// 构造私有化
	private OrganizStoreNRTSearch() {

	}
	private static String path = null;
	private static Logger logger = LoggerFactory.getLogger(OrganizStoreNRTSearch.class);
	private static NRTManagerReopenThread thread ;

	
	static{
		/*try {
			organizStoreNRTSearch = new OrganizStoreNRTSearch();
			directory = FSDirectory.open(new File(path));
			// IKAnalyzer分词
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer(true));
			indexWriter = new IndexWriter(directory, conf);
			nrtManager = new NRTManager(indexWriter, new SearcherWarmer() {
				public void warm(IndexSearcher indexsearcher) throws IOException {
					// 索引变化时
					logger.info("组织库索引信息reopen");
				}
			});
			
			// 线程后台运行
			//NRTManagerReopenThread thread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			thread= new NRTManagerReopenThread(nrtManager, 0.01, 0.001);
			thread.setName("组织库索引线程");
			thread.setDaemon(true);
			// 线程开启
			thread.start();
		} catch (Exception e) {
			logger.info("组织库索引创建失败");
			e.printStackTrace();
		}*/
	}
	/**
	 * 获得单例对象
	 * @return OrganizStoreNRTSearch
	 */
	public static OrganizStoreNRTSearch getInstance() {
		return organizStoreNRTSearch;
	}

	/*************************** 索引操作 ***************************/
	/**
	 * 创建OrganizStore的索引库
	 * 
	 * @param stores
	 *            OrganizStore对象集合
	 */
	public void createIndex(List<OrganizStore> stores) {
		try {
			List<Document> documents = new ArrayList<Document>();
			// 清空原来的
			nrtManager.deleteAll();
			for (OrganizStore store : stores) {
				// 加到集合中
				documents.add(addDocument(store));
			}
			nrtManager.addDocuments(documents);
			// 提交
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("组织库创建新的索引库失败");
			e.printStackTrace();
		}
	}

	/**
	 * 新增一条索引
	 * 
	 * @param store
	 *            OrganizStore对象
	 */
	public void addIndex(OrganizStore store) {
		try {
			Document doc = addDocument(store);
			nrtManager.addDocument(doc);
		} catch (Exception e) {
			logger.info("组织库添加新的索引失败");
			e.printStackTrace();
		}
	}

	/**
	 * 新增多条索引
	 * 
	 * @param stores
	 *            OrganizStore对象集合
	 */
	public void addIndex(List<OrganizStore> stores) {
		List<Document> docs = new ArrayList<Document>();
		try {
			for (OrganizStore store : stores) {
				docs.add(addDocument(store));
			}
			// 添加到索引库中
			nrtManager.addDocuments(docs);
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("组织库添加新的索引集合失败");
			e.printStackTrace();
		}
	}

	/**
	 * 更新一条索引
	 * 
	 * @param id
	 *            OrganizStore的id
	 * @param store
	 *            OrganizStore对象
	 */
	public void updateIndex(String id, OrganizStore store) {
		try {
			// 先删除原来的
			Term term = new Term("id", id);
			// 添加一条新的
			Document doc = addDocument(store);
			nrtManager.updateDocument(term, doc);
		} catch (Exception e) {
			logger.info("组织库索引更新失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除一条索引
	 * 
	 * @param id
	 *            OrganizStore对象id
	 */
	public void deleteIndex(String id) {
		try {
			Term term = new Term("id", id);
			nrtManager.deleteDocuments(term);
		} catch (Exception e) {
			logger.info("组织库索引删除失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除多条索引
	 * 
	 * @param ids
	 *            OrganizStore对象id数组
	 */
	public void deleteIndex(String[] ids) {
		try {
			for (String id : ids) {
				Term term = new Term("id", id);
				nrtManager.deleteDocuments(term);
			}
		} catch (Exception e) {
			logger.info("组织库索引删除失败");
			e.printStackTrace();
		}
	}

	/**
	 * 组织库转document
	 * 
	 * @param store
	 * @return
	 */
	private Document addDocument(OrganizStore store) {
		Document document = new Document();
		try {
			//不是Index.NO,而是不分词
			document.add(new Field("id", store.getId(), Store.YES, Index.NOT_ANALYZED));
			if (null != store.getOrganizName()) {
				document.add(new Field("organizName", store.getOrganizName(), Store.YES, Index.ANALYZED));
			}
			//时间不分词
			if (null != store.getFoundTime()) {
				document.add(new Field("foundTime", store.getFoundTime(), Store.YES, Index.NOT_ANALYZED));
			}
			// 网站
			if (null != store.getWebsiteURL()) {
				document.add(new Field("websiteURL", store.getWebsiteURL(), Store.YES, Index.ANALYZED));
			}
			// 活动范围
			if (null != store.getDegreeOfLatitude()) {
				document.add(new Field("degreeOfLatitude", store.getDegreeOfLatitude(), Store.YES, Index.ANALYZED));
			}
			// 基本情况
			if (null != store.getBasicCondition()) {
				document.add(new Field("basicCondition", store.getBasicCondition(), Store.YES, Index.ANALYZED));
			}
			// 活动
			if (null != store.getActivityCondition()) {
				document.add(new Field("activityCondition", store.getActivityCondition(), Store.YES, Index.ANALYZED));
			}
			// 负责人
			if (null != store.getOrgHeadOfName()) {
				document.add(new Field("orgHeadOfName", store.getOrgHeadOfName(), Store.YES, Index.ANALYZED));
			}
			// 类型
			if (null != store.getInfoType()) {
				document.add(new Field("infoType", store.getInfoType().getTypeName(), Store.YES, Index.ANALYZED));
			}
			// 下属组织
			if (null != store.getOrganizBranchesNames()) {
				document.add(new Field("organizBranchesNames", store.getOrganizBranchesNames(), Store.YES, Index.ANALYZED));
			}
			// 下属人员
			if (null != store.getOrganizPersonNames()) {
				document.add(new Field("organizPersonNames", store.getOrganizPersonNames(), Store.YES, Index.ANALYZED));
			}
			// 刊物
			if (null != store.getOrganizPublicActionNames()) {
				document.add(new Field("organizPublicActionNames", store.getOrganizPublicActionNames(), Store.YES,Index.ANALYZED));
			}
			// uid
			if (null != store.getUid()) {
				document.add(new Field("uid", store.getUid(), Store.YES, Index.NOT_ANALYZED));
			}
			// oid
			if (null != store.getOid()) {
				document.add(new Field("oid", store.getOid(), Store.YES, Index.NOT_ANALYZED));
			}
			// did
			if (null != store.getDid()) {
				document.add(new Field("did", store.getDid(), Store.YES, Index.NOT_ANALYZED));
			}
			// 创建时间
			if (null != store.getCreateDate()) {
				//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				//Date date = format.parse(store.getCreateDate());
				document.add(new Field("createDate", store.getCreateDate().substring(0, 10), Store.YES, Index.NOT_ANALYZED));
			}
		} catch (Exception e) {
			logger.info("组织库转document失败");
			e.printStackTrace();
		}
		return document;
	}

	/*************************** 通用的两个查询接口 ***************************/
	/**
	 * 获得索引库索引总数
	 * @return 总数
	 */
	public int getTotalCount(){
		int count=0;
		try {
			//这种不包括被删除的
			count=reader.maxDoc();
			//包括删除的是 IndexSearcher.maxDoc()这个方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public int getCount(Query query, Filter filter) {
		// 获得数量
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
		// 分页
		IndexSearcher searcher = null;
		List<Document> list = new ArrayList<Document>();
		ScoreDoc[] scoreDocs = null;
		try {
			searcher = nrtManager.getSearcherManager(true).acquire();
			TopDocs topDocs = null;
			if (null != filter) {
				topDocs = searcher.search(query, filter, Integer.MAX_VALUE);
			} else {
				topDocs = searcher.search(query, Integer.MAX_VALUE);
			}
			scoreDocs = topDocs.scoreDocs;
			if (scoreDocs.length > 0) {
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

	private void closeSearcher(IndexSearcher searcher) {
		// 关闭searcher
		if (searcher != null) {
			try {
				// 关闭资源
				nrtManager.getSearcherManager(true).release(searcher);
				// 不需要自己关闭
				searcher = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 分页以及排序
	 */
	@Override
	public List<Document> getDocuments(Query query, Filter filter, String sortFiled, String dir,int nowPage, int pageSize) {
		IndexSearcher searcher = null;
		List<Document> list = new ArrayList<Document>();
		ScoreDoc[] scoreDocs = null;
		try {
			searcher = nrtManager.getSearcherManager(true).acquire();
			TopDocs topDocs = null;
			//排序方向
			boolean reverse=true;
			if(StringUtils.isNotEmpty(dir)&&dir.equals("ASC")){
				reverse=false;
				logger.info(String.format("排序方向：%s", "降序"));
			}else{
				logger.info(String.format("排序方向：%s", "升序"));
			}
			//默认排序字段：创建时间
			Sort sort=new Sort(new SortField("createDate", SortField.STRING,reverse));
			if(!StringUtils.isEmpty(sortFiled)){
				logger.info(String.format("当前排序字段：%s",sortFiled));
				sort=new Sort(new SortField(sortFiled, SortField.STRING,reverse));
			}
			//获得数据
			if(null!=filter){
				topDocs =searcher.search(query, filter, Integer.MAX_VALUE,sort);
			}else{
				topDocs = searcher.search(query,Integer.MAX_VALUE,sort);
			}
			scoreDocs = topDocs.scoreDocs;
			if (scoreDocs.length > 0) {
				//分页
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
