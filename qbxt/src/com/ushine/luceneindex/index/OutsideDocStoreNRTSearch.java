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
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.model.OutsideDocStore;
import com.ushine.util.ReadAttachUtil;

/**
 * 外来文档索引读写
 * 
 * @author dh
 *
 */
public class OutsideDocStoreNRTSearch implements IStoreNRTSearch {
	private static NRTManager nrtManager;
	private static OutsideDocStoreNRTSearch outsideDocStoreNRTSearch = null;
	private static IndexWriter indexWriter;
	private static IndexReader reader;
	private static Directory directory;
	private static NRTManagerReopenThread thread;
	private static Logger logger = LoggerFactory.getLogger(OutsideDocStoreNRTSearch.class);
	private static String path = null;

	/**
	 * 构造函数私有
	 */
	private OutsideDocStoreNRTSearch() {

	}

	/**
	 * 获得单例对象
	 * 
	 * @return OutsideDocStoreNRTSearch
	 */
	static{
		/*try {
			outsideDocStoreNRTSearch = new OutsideDocStoreNRTSearch();
			directory = FSDirectory.open(new File(path));
			// IKAnalyzer分词,用短语拼接查询PhraseQuery
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer(true));
			
			indexWriter = new IndexWriter(directory, conf);
			nrtManager = new NRTManager(indexWriter, new SearcherWarmer() {
				public void warm(IndexSearcher indexsearcher) throws IOException {
					// 索引变化时
					logger.info("外来文档库索引信息reopen");
				}
			});
			
			// 线程后台运行
			//NRTManagerReopenThread thread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			thread = new NRTManagerReopenThread(nrtManager, 0.01, 0.001);
			thread.setName("外来文档库索索引线程");
			thread.setDaemon(true);
			// 一定要线程开启
			thread.start();
		} catch (Exception e) {
			logger.info("外来文档库索引创建失败");
			e.printStackTrace();
		}*/
	}
	public static OutsideDocStoreNRTSearch getInstance() {
		return outsideDocStoreNRTSearch;
		
	}

	/**
	 * 创建OutsideDocStore的索引库
	 * 
	 * @param stores
	 *            OutsideDocStore对象集合
	 */
	public void createIndex(List<OutsideDocStore> stores) {
		try {
			List<Document> documents = new ArrayList<Document>();
			// 清空原来的
			nrtManager.deleteAll();
			for (OutsideDocStore store : stores) {
				// 加到集合中
				documents.add(addDocument(store));
			}
			nrtManager.addDocuments(documents);
			// 提交
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("外来文档库创建新的索引库失败");
			e.printStackTrace();
		}
	}

	/**
	 * 添加一条索引
	 * 
	 * @param store
	 *            OutsideDocStore对象
	 */
	public void addIndex(OutsideDocStore store) {
		try {
			Document doc = addDocument(store);
			nrtManager.addDocument(doc);
		} catch (Exception e) {
			logger.info("外来文档库添加新的索引失败");
			e.printStackTrace();
		}
	}

	/**
	 * 添加多条索引
	 * 
	 * @param stores
	 *            OutsideDocStore对象集合
	 */
	public void addIndex(List<OutsideDocStore> stores) {
		List<Document> docs = new ArrayList<Document>();
		try {
			for (OutsideDocStore store : stores) {
				docs.add(addDocument(store));
			}
			// 添加到索引库中
			nrtManager.addDocuments(docs);
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("外来文档库添加新的索引集合失败");
			e.printStackTrace();
		}
	}

	/**
	 * 更新一条索引记录
	 * 
	 * @param id
	 *            OutsideDocStore对象id
	 * @param store
	 *            OutsideDocStore对象
	 */
	public void updateIndex(String id, OutsideDocStore store) {
		try {
			// 先删除原来的
			Term term = new Term("id", id);
			// 添加一条新的
			Document doc = addDocument(store);
			nrtManager.updateDocument(term, doc);
		} catch (Exception e) {
			logger.info("外来文档库索引更新失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除一条索引
	 * 
	 * @param id
	 *            OutsideDocStore对象id
	 */
	public void deleteIndex(String id) {
		try {
			Term term = new Term("id", id);
			nrtManager.deleteDocuments(term);
		} catch (Exception e) {
			logger.info("外来文档库索引删除失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除多个索引
	 * 
	 * @param ids
	 *            OutsideDocStore对象id数组
	 */
	public void deleteIndex(String[] ids) {
		try {
			for (String id : ids) {
				Term term = new Term("id", id);
				nrtManager.deleteDocuments(term);
			}
			commitIndex();
		} catch (Exception e) {
			logger.info("外来文档库索引删除失败");
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
			logger.info("业务文档库索引commit失败");
			e.printStackTrace();
		}
	}
	/**
	 * 将OutsideDocStore转为Document
	 * 
	 * @param store
	 * @return Document
	 */
	private Document addDocument(OutsideDocStore store) {
		Document document = new Document();
		try {
			document.add(new Field("id", store.getId(), Store.YES, Index.NOT_ANALYZED));
			if (null != store.getName()) {
				document.add(new Field("name", store.getName(), Store.YES, Index.ANALYZED));
			}
			if (null != store.getDocNumber()) {
				document.add(new Field("docNumber", store.getDocNumber(), Store.YES, Index.ANALYZED));
			}
			if (null != store.getSecretRank()) {
				document.add(new Field("secretRank", store.getSecretRank(), Store.YES, Index.ANALYZED));
			}
			if (null != store.getSourceUnit()) {
				document.add(new Field("sourceUnit", store.getSourceUnit(), Store.YES, Index.ANALYZED));
			}
			/*
			//附件内容
			if(null!=store.getAttachContent()){
				document.add(new Field("attachContent",store.getAttachContent(),Store.NO,Index.ANALYZED));
			}*/
			if (null != store.getTime()) {
				document.add(new Field("time", store.getTime(), Store.YES, Index.NOT_ANALYZED));
			}
			
			if (null != store.getCentent()) {
				document.add(new Field("centent", store.getCentent(), Store.NO, Index.ANALYZED));
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
			if (null != store.getInfoType()) {
				document.add(new Field("infoType", store.getInfoType().getTypeName(), Store.YES, Index.ANALYZED));
			}
			if (null != store.getInvolvedInTheField()) {
				document.add(new Field("involvedInTheField", store.getInvolvedInTheField().getTypeName(), Store.YES,
						Index.ANALYZED));
			}
			if (null != store.getCreateDate()) {
				String value=store.getCreateDate();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(value);
				document.add(new Field("createDate", format.format(date), Store.YES, Index.NOT_ANALYZED));
				SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//默认排序字段
				document.add(new Field("defalutSort", Long.toString(format2.parse(value).getTime()), Store.NO, Index.NOT_ANALYZED));
			}
			//附件的名称和内容
			String root = System.getProperty("qbtest.root");
			String attaches=store.getAttaches();
			if(null!=attaches){
				String []attachesNames=attaches.split(",");
				StringBuffer buffer=new StringBuffer();
				StringBuffer contentBuffer = new StringBuffer();
				for (String string : attachesNames) {
					int index0=string.indexOf("_");
					int index1=string.lastIndexOf(".");
					if(index1>index0){
						//只取文件名
						buffer.append(string.subSequence(index0+1,index1)+",");
					}
					//内容
					String fileName = root + File.separator + string.trim();
					File file = new File(fileName);
					contentBuffer.append(ReadAttachUtil.readContent(fileName) + ",");
				}
				document.add(new Field("attachContent",contentBuffer.toString(),Store.NO,Index.ANALYZED));
				document.add(new Field("attaches", buffer.toString(), Store.YES, Index.ANALYZED));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}

	/********************************* 查询方法 *************************************/
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
		}finally {
			if(reader!=null){
				try {
					//reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
		// 分页查询
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
	 * 关闭IndexSearcher
	 */
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
	 * 分页查询以及排序
	 */
	@Override
	public List<Document> getDocuments(Query query, Filter filter, String sortFiled,String dir, int nowPage, int pageSize) {
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
			Sort sort=null;
			if(StringUtils.isEmpty(sortFiled)||StringUtils.equals("docNumber", sortFiled)){
				//默认排序字段：期刊号 
				logger.info(String.format("当前排序字段：%s","docNumber"));
				sort=new Sort(new SortField("docNumber", SortField.LONG,reverse));
			}else if (StringUtils.equals("createDate", sortFiled)) {
				//日期排序
				logger.info(String.format("当前排序字段：%s","defalutSort"));
				sort=new Sort(new SortField("defalutSort", SortField.LONG,reverse));
			}else{
				//其他排序字段
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
}
