package com.ushine.luceneindex.index;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.LeadSpeakStore;

/**
 * 领导讲话库索引读写
 * @author Administrator
 *
 */
public class LeadSpeakStoreNRTSearch implements IStoreNRTSearch {
	private static IndexWriter indexWriter;
	private static Directory directory;
	private static NRTManager nrtManager;
	private static LeadSpeakStoreNRTSearch leadSpeakStoreNRTSearch;
	private static NRTManagerReopenThread thread;
	@Autowired
	private IBaseDao<LeadSpeakStore, Serializable> baseDao;
	/**
	 * 构造函数私有化
	 */
	private LeadSpeakStoreNRTSearch(){
	}
	private static Logger logger = Logger.getLogger(LeadSpeakStoreNRTSearch.class);
	private static String path =  StoreIndexPath.LEADSPEAKSTORE_INDEXPATH;
	
	/**
	 * 返回LeadSpeakStoreNRTSearch实例,单例
	 * @return
	 */
	static{
		/*try {
			leadSpeakStoreNRTSearch = new LeadSpeakStoreNRTSearch();
			directory = FSDirectory.open(new File(path));
			//为了达到精确匹配,不分词,用通配符,这种方式高亮效果不好
			//IKAnalyzer分词,用短语拼接查询PhraseQuery最合适
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer(true));
			
			indexWriter = new IndexWriter(directory, conf);
			nrtManager = new NRTManager(indexWriter, new SearcherWarmer() {
				public void warm(IndexSearcher indexsearcher) throws IOException {
					// 索引变化时
					logger.info("领导讲话库索引信息reopen");
				}
			});
			// 线程后台运行
			//NRTManagerReopenThread thread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			NRTManagerReopenThread thread = new NRTManagerReopenThread(nrtManager, 0.01, 0.001);
			thread.setName("领导讲话库索引线程");
			thread.setDaemon(true);
			//一定要线程开启
			thread.start();
		} catch (Exception e) {
			logger.error("领导讲话库索引创建异常");
			e.printStackTrace();
		}*/
	}
	public static LeadSpeakStoreNRTSearch getInstance() {
		return leadSpeakStoreNRTSearch;
	}
	/**
	 * 领导讲话库转Document
	 * @param store 领导讲话库对象
	 * @return
	 */
	private Document addDoc(LeadSpeakStore store){
		Document document=new Document();
		try {
			document.add(new Field("id", store.getId(), Store.YES, Index.NOT_ANALYZED));
			if(null!=store.getInvolvedInTheField()){
				document.add(new Field("involvedInTheField", store.getInvolvedInTheField().getTypeName(), Store.YES, Index.ANALYZED));
			}
			if(null!=store.getInfoType()){
				document.add(new Field("infoType", store.getInfoType().getTypeName(), Store.YES, Index.ANALYZED));
			}
			if(null!=store.getTitle()){
				document.add(new Field("title", store.getTitle(), Store.YES, Index.ANALYZED));
			}
			if(null!=store.getTime()){
				document.add(new Field("time", store.getTime(), Store.YES, Index.NOT_ANALYZED));
			}
			if(null!=store.getMeetingName()){
				document.add(new Field("meetingName", store.getMeetingName(), Store.YES, Index.ANALYZED));
			}
			if(null!=store.getSecretRank()){
				document.add(new Field("secretRank", store.getSecretRank(), Store.YES, Index.ANALYZED));
			}
			if(null!=store.getCentent()){
				document.add(new Field("centent", store.getCentent(), Store.YES, Index.ANALYZED));
			}
			if(null!=store.getUid()){
				document.add(new Field("uid", store.getUid(), Store.YES, Index.NOT_ANALYZED));
			}
			if(null!=store.getOid()){
				document.add(new Field("oid", store.getOid(), Store.YES, Index.NOT_ANALYZED));
			}
			if(null!=store.getDid()){
				document.add(new Field("did", store.getDid(), Store.YES, Index.NOT_ANALYZED));
			}
			if(null!=store.getAction()){
				document.add(new Field("action", store.getAction(), Store.YES, Index.NOT_ANALYZED));
			}
			if(null!=store.getCreateDate()){
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(store.getCreateDate());
				document.add(new Field("createDate", store.getCreateDate().substring(0, 10), Store.YES, Index.NOT_ANALYZED));
			}
			//附件
			String attaches=store.getAttaches();
			if(null!=attaches){
				String []attachesNames=attaches.split(",");
				StringBuffer buffer=new StringBuffer();
				for (String string : attachesNames) {
					int index0=string.indexOf("_");
					int index1=string.lastIndexOf(".");
					if(index1>index0){
						//只取文件名
						buffer.append(string.subSequence(index0+1,index1)+",");
					}
				}
				String result=buffer.toString();
				if (result.length()>1) {
					document.add(new Field("attaches", result.substring(0, result.length()-1), Store.YES, Index.ANALYZED));
				}
			}
			//附件内容
			if(null!=store.getAttachContent()){
				document.add(new Field("attachContent",store.getAttachContent(),Store.NO,Index.ANALYZED));
			}
		} catch (Exception e) {
			logger.info("领导讲话库转document异常");
			e.printStackTrace();
		}
		return document;
	}
	/**
	 * 创建领导讲话的索引库
	 * @param leadSpeakStores 领导讲话的store集合
	 */
	public void createIndex(List<LeadSpeakStore> leadSpeakStores) {
		try {
			// 清空原来的
			nrtManager.deleteAll();
			List<Document> documents = new ArrayList<Document>();
			for (LeadSpeakStore store : leadSpeakStores) {
				// 加到集合中
				documents.add(addDoc(store));
			}
			nrtManager.addDocuments(documents);
			commitIndex();
		} catch (Exception e) {
			logger.info("领导讲话库创建新的索引库失败");
			e.printStackTrace();
		} 
	}
	/**
	 * 添加一条索引
	 * @param leadSpeakStore 领导讲话的store
	 */
	public void addIndex(LeadSpeakStore leadSpeakStore) {
		try {
			Document doc = addDoc(leadSpeakStore);
			nrtManager.addDocument(doc);
			//不需要commitIndex();
		} catch (Exception e) {
			logger.info("领导讲话库添加新的索引失败");
			e.printStackTrace();
		}
	}
	/**
	 * 添加多条索引
	 * @param stores 领导讲话的store集合
	 */
	public void addIndex(List<LeadSpeakStore> stores) {
		List<Document> docs = new ArrayList<Document>();
		try {
			for (LeadSpeakStore store : stores) {
				docs.add(addDoc(store));
			}
			// 添加到索引库中
			nrtManager.addDocuments(docs);
			////
			commitIndex();
		} catch (Exception e) {
			logger.info("领导讲话库添加新的索引集合失败");
			e.printStackTrace();
		}
	}
	/**
	 * 更新索引
	 * @param id LeadSpeakStore的id
	 * @param leadSpeakStore 修改后的LeadSpeakStore对象
	 */
	public void updateIndex(String id, LeadSpeakStore leadSpeakStore) {
		try {
			// 先删除原来的
			Term term = new Term("id", id);
			// 添加一条新的
			Document doc = addDoc(leadSpeakStore);
			nrtManager.updateDocument(term, doc);
			// 提交
			//不需要commitIndex();
		} catch (Exception e) {
			logger.info("领导讲话库索引更新失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除索引
	 * @param id LeadSpeakStore的id
	 */
	public void deleteIndex(String id) {
		try {
			Term term = new Term("id", id);
			nrtManager.deleteDocuments(term);
			//不需要commitIndex();
		} catch (Exception e) {
			logger.info("领导讲话库索引删除失败");
			e.printStackTrace();
		}
	}
	/**
	 * 删除多条索引
	 * @param ids LeadSpeakStore的id数组
	 */
	public void deleteIndex(String[] ids) {
		try {
			for (String id : ids) {
				Term term = new Term("id", id);
				nrtManager.deleteDocuments(term);
			}
			//不需要
			commitIndex();
		} catch (Exception e) {
			logger.info("领导讲话库索引删除失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 索引的提交
	 */
	private void commitIndex() {
		try {
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("领导讲话库索引commit失败");
			e.printStackTrace();
		}
	}
	
	/************************** 查询的功能 ******************************/
	/**
	 * 获得索引库索引总数
	 * @return 总数
	 */
	public int getTotalCount(){
		int count=0;
		IndexSearcher searcher = null;
		try {
			//这种不包括被删除的
			//count=reader.maxDoc();
			//searcher = nrtManager.getSearcherManager(true).acquire();
			count= baseDao.getRowCount(LeadSpeakStore.class);
			//包括删除的是 IndexSearcher.maxDoc()这个方法
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeSearcher(searcher);
		}
		return count;
	}
	
	/**
	 * 获得总数
	 * 
	 * @param query
	 *            查询条件
	 * @return 符合条件的总数
	 */
	public int getCount(Query query,Filter filter) {
		int totalCount = 0;
		ScoreDoc[] scoreDocs = null;
		IndexSearcher searcher = null;
		try {
			// 获得searcher对象
			TopDocs topDocs =null; 
			searcher = nrtManager.getSearcherManager(true).acquire();
			if(null!=filter){				
				topDocs = searcher.search(query,filter, Integer.MAX_VALUE);
			}else{
				topDocs = searcher.search(query,Integer.MAX_VALUE);
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

	/**
	 * 获得Document对象集合
	 * 
	 * @param query
	 *            Query对象
	 * @param size
	 *            需要获得的数量
	 * @return
	 */
	public List<Document> getSearcher(Query query,Filter filter,int size) {
		// 一次性加载到内存中会严重降低效率
		// 而且还可能造成内存不足
		// 还是使用searchAfter比较合适
		IndexSearcher searcher = null;
		List<Document> list = new ArrayList<Document>();
		/*ScoreDoc[] scoreDocs = null;
		try {
			searcher = nrtManager.getSearcherManager(true).acquire();
			TopDocs topDocs = null;
			if(null!=filter){				
				topDocs = searcher.search(query,filter, Integer.MAX_VALUE);
			}else{
				topDocs = searcher.search(query,Integer.MAX_VALUE);
			}
			scoreDocs = topDocs.scoreDocs;
			if (scoreDocs.length > 0) {
				for (ScoreDoc scoreDoc : scoreDocs) {
					Document document = searcher.doc(scoreDoc.doc);
					list.add(document);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭searcher
			closeSearcher(searcher);
		}*/
		return list;
	}
	/**
	 * 进行分页查询并进行排序
	 */
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
				//排序字段 &&!StringUtils.equals(sortFiled, "null")
				logger.info(String.format("当前排序字段：%s",sortFiled));
				sort=new Sort(new SortField(sortFiled, SortField.STRING,reverse));
			}
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
	/**
	 * 分页
	 * 
	 * @param query
	 *            查询条件
	 * @param nowPage
	 *            当前页码
	 * @param pageSize
	 *            每页显示数量
	 * @return
	 */
	public List<Document> getDocuments(Query query,Filter filter,int nowPage, int pageSize) {
		IndexSearcher searcher = null;
		List<Document> list = new ArrayList<Document>();
		ScoreDoc[] scoreDocs = null;
		try {
			searcher = nrtManager.getSearcherManager(true).acquire();
			TopDocs topDocs = null;
			if(null!=filter){				
				topDocs = searcher.search(query,filter, Integer.MAX_VALUE);
			}else{
				topDocs = searcher.search(query,Integer.MAX_VALUE);
			}
			scoreDocs = topDocs.scoreDocs;
			if (scoreDocs.length > 0) {
				// 共多少页
				// int pageCount=(scoreDocs.length+pageSize-1)/pageSize;
				// 第一页 每页40条 0-39
				// 第二页 40-79
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
	private void closeSearcher(IndexSearcher searcher) {
		// 关闭searcher
		if (searcher != null) {
			try {
				// 关闭资源
				nrtManager.getSearcherManager(true).release(searcher);
				// 不需要自己关闭
				// searcher.close();
			} catch (IOException e) {
				//logger.info("人员库索引searcher关闭失败");
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
	
}
