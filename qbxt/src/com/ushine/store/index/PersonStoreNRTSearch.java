package com.ushine.store.index;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ushine.common.config.Configured;
import com.ushine.common.utils.SpringUtils;
import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.CertificatesStore;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.NetworkAccountStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.service.IClueRelationshipService;
import com.ushine.util.ReadAttachUtil;
import com.ushine.util.StringUtil;

import oracle.net.aso.k;

/**
 * 对人员库信息索引库的读写操作
 * @author dh
 *
 */
public class PersonStoreNRTSearch implements IStoreNRTSearch{
	private static IndexWriter indexWriter;
	private static NRTManager nrtManager;
	private static Directory directory ;
	//先关闭IndexWriter
	//后关闭Directory
	private static PersonStoreNRTSearch personStoreNRTSearch = null;
	private PersonStoreNRTSearch() {

	}
	private static Logger logger = Logger.getLogger(PersonStoreNRTSearch.class);
	//获得人员库索引根路径
	private static String path = StoreIndexPath.PERSONSTORE_INDEXPATH;
	
	private	static NRTManagerReopenThread thread;
	/**
	 * 初始化加载
	 */
	static{
		try {
			personStoreNRTSearch = new PersonStoreNRTSearch();
			directory = FSDirectory.open(new File(path));
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer(true));
			indexWriter = new IndexWriter(directory, conf);
			
			nrtManager = new NRTManager(indexWriter, new SearcherWarmer() {
				public void warm(IndexSearcher indexsearcher) throws IOException {
					// 索引变化时
					logger.info("人员库索引信息reopen");
				}
			});
			// 线程后台运行
			//NRTManagerReopenThread thread = new NRTManagerReopenThread(nrtManager, 5.0, 0.025);
			thread = new NRTManagerReopenThread(nrtManager, 0.01, 0.001);
			thread.setName("人员库索引线程");
			thread.setDaemon(true);
			// 线程开启
			thread.start();
		} catch (Exception e) {
			logger.info("人员库创建索引异常！！！");
			e.printStackTrace();
		}
	}
	/**
	 * 判断值是否为空
	 * @param fieldValue
	 * @return 空或空串返回空串
	 */
	private String addField(String fieldValue){
		String value="";
		//字段值不为空
		if(!StringUtil.isEmty(fieldValue)){
			value=fieldValue;
		}
		return value;
	}
	/**
	 * 人员信息转Document
	 * 
	 * @param personStore
	 *            人员信息
	 * @return
	 */
	private Document addDoc(PersonStore personStore) {
		try {
			Document document = new Document();
			document.add(new Field("id", personStore.getId(), Store.YES, Index.NOT_ANALYZED));

			if (null != personStore.getPersonName()) {
				document.add(new Field("personName", addField(personStore.getPersonName()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getNameUsedBefore()) {
				document.add(new Field("nameUsedBefore", addField(personStore.getNameUsedBefore()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getEnglishName()) {
				document.add(new Field("englishName", addField(personStore.getEnglishName()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getSex()) {
				document.add(new Field("sex", addField(personStore.getSex()), Store.YES, Index.ANALYZED));
			}

			if (null != personStore.getBebornTime()) {
				document.add(new Field("bebornTime", addField(personStore.getBebornTime()), Store.YES, Index.NOT_ANALYZED));
			}
			if (null != personStore.getRegisterAddress()) {
				document.add(new Field("registerAddress",addField( personStore.getRegisterAddress()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getPresentAddress()) {
				document.add(new Field("presentAddress", addField(personStore.getPresentAddress()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getWorkUnit()) {
				document.add(new Field("workUnit", addField(personStore.getWorkUnit()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getAntecedents()) {
				document.add(new Field("antecedents", addField(personStore.getAntecedents()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getActivityCondition()) {
				document.add(new Field("activityCondition", addField(personStore.getActivityCondition()), Store.YES, Index.ANALYZED));
			}
			if (null != personStore.getInfoType()) {
				document.add(new Field("infoType", addField(personStore.getInfoType().getTypeName()), Store.YES, Index.ANALYZED));
			}
			//附件名称、内容索引
			if (null != personStore.getAppendix()) {
				StringBuffer buffer=new StringBuffer();
				StringBuffer attachContentBuffer=new StringBuffer();
				String []attachesNames=personStore.getAppendix().split(",");
				String root=Configured.getInstance().get("rootIndex");
				for (String string : attachesNames) {
					int index0=string.indexOf("_");
					int index1=string.lastIndexOf(".");
					if(index1>index0){
						//只取文件名
						buffer.append(string.subSequence(index0+1,index1)+",");
					}
					String filepath=root+File.separator+string.trim();
					//附件存在
					File file=new File(filepath);
					if (file.exists()) {
						attachContentBuffer.append(ReadAttachUtil.readContent(filepath)+" ");
					}
				}
				String result=buffer.toString();
				if (result.length()>1) {
					document.add(new Field("appendix", result.substring(0, result.length()-1), Store.YES, Index.ANALYZED));
				}
				document.add(new Field("attachContent", attachContentBuffer.toString(), Store.NO, Index.ANALYZED));
			}
			
			IBaseDao baseDao=(IBaseDao) SpringUtils.getBean("baseDao");
			//账号不分词,通配符查询就行
			//网络账号
			DetachedCriteria criteria=DetachedCriteria.forClass(NetworkAccountStore.class);
			//用DetachedCriteria的关联查询就可
			criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", personStore.getId()));
			StringBuffer number=new StringBuffer();
			List<NetworkAccountStore> list=baseDao.findByCriteria(criteria);
			for (NetworkAccountStore networkAccountStore : list) {
				number.append(networkAccountStore.getNetworkNumber()+",");
			}
			document.add(new Field("networkAccountStores", number.toString(), Store.YES, Index.NOT_ANALYZED));
			
			//证件
			DetachedCriteria criteria2=DetachedCriteria.forClass(CertificatesStore.class);
			//用DetachedCriteria的关联查询就可
			criteria2.createAlias("personStore", "p").add(Restrictions.eq("p.id", personStore.getId()));
			StringBuffer number2=new StringBuffer();
			List<CertificatesStore> list2=baseDao.findByCriteria(criteria2);
			for (CertificatesStore certificatesStore : list2) {
				number2.append(certificatesStore.getCertificatesNumber()+",");
			}
			document.add(new Field("certificatesStores", number2.toString(), Store.YES, Index.NOT_ANALYZED));
			// 创建日期
			if (null != personStore.getCreateDate()) {
				//SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				//Date date=format.parse(personStore.getCreateDate());
				//document.add(new Field("createDate", addField(format.format(date)), Store.YES, Index.NOT_ANALYZED));
				String value=personStore.getCreateDate();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				Date date=format.parse(value);
				document.add(new Field("createDate", format.format(date), Store.YES, Index.NOT_ANALYZED));
				SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//默认排序字段
				document.add(new Field("defalutSort", Long.toString(format2.parse(value).getTime()), Store.NO, Index.NOT_ANALYZED));
			}
			if (null != personStore.getUid()) {
				document.add(new Field("uid", personStore.getUid(), Store.YES, Index.NOT_ANALYZED));
			}
			if (null != personStore.getOid()) {
				document.add(new Field("oid", personStore.getOid(), Store.YES, Index.NOT_ANALYZED));
			}
			if (null != personStore.getDid()) {
				document.add(new Field("did", personStore.getDid(), Store.YES, Index.NOT_ANALYZED));
			}
			return document;
		} catch (Exception e) {
			logger.info("人员库转索引失败");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建新的索引库
	 * 
	 * @param personStores
	 *            人员库对象集合
	 */
	public void createIndex(List<PersonStore> personStores) {
		try {
			List<Document> documents = new ArrayList<Document>();
			//indexWriter.deleteAll();
			// 清空原来的
			nrtManager.deleteAll();
			for (PersonStore store : personStores) {
				// 加到集合中
				documents.add(addDoc(store));
			}
			nrtManager.addDocuments(documents);
			commitIndex();
		} catch (Exception e) {
			logger.info("人员库创建新的索引库失败");
			e.printStackTrace();
		}
	}

	/**
	 * 添加一条新的索引
	 * 
	 * @param personStore 人员对象
	 */
	public void addIndex(PersonStore personStore) {
		try {
			Document doc = addDoc(personStore);
			nrtManager.addDocument(doc);
			//不需要commitIndex();
		} catch (Exception e) {
			logger.info("人员库添加新的索引失败");
			e.printStackTrace();
		}
	}

	/**
	 * 添加索引集合
	 * 
	 * @param stores 人员对象集合
	 */
	public void addIndex(List<PersonStore> stores) {
		List<Document> docs = new ArrayList<Document>();
		try {
			for (PersonStore personStore : stores) {
				docs.add(addDoc(personStore));
			}
			// 添加到索引库中
			nrtManager.addDocuments(docs);
			//不需要
			commitIndex();
		} catch (Exception e) {
			logger.info("人员库添加新的索引集合失败");
			e.printStackTrace();
		}
	}

	/**
	 * 更新索引
	 * 
	 * @param id  人员id
	 * @param personStore  人员对象
	 */
	public void updateIndex(String id, PersonStore personStore) {
		try {
			// 先删除原来的
			Term term = new Term("id", id);
			// 添加一条新的
			Document doc = addDoc(personStore);
			nrtManager.updateDocument(term, doc);
			// 提交
			//不需要commitIndex();
		} catch (Exception e) {
			logger.info("人员库索引更新失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param id 人员id
	 */
	public void deleteIndex(String id) {
		try {
			Term term = new Term("id", id);
			nrtManager.deleteDocuments(term);
			//不需要
			commitIndex();
		} catch (Exception e) {
			logger.info("人员库索引删除失败");
			e.printStackTrace();
		}
	}

	/**
	 * 删除多个索引
	 * 
	 * @param ids 人员id数组
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
			logger.info("人员库索引删除失败");
			e.printStackTrace();
		}
	}

	/**
	 * 索引的提交
	 */
	private static void commitIndex() {
		try {
			indexWriter.commit();
		} catch (Exception e) {
			logger.info("人员库索引commit失败");
			e.printStackTrace();
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

	/************************** 查询的功能 ******************************/
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
	 * 获得索引库索引总数
	 * @return 总数
	 */
	public int getTotalCount(){
		int count=0;
		/*try {
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
		}*/
		return count;
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
	public IndexSearcher getSeacher(){
		IndexSearcher searcher=null;
		try {
			searcher = nrtManager.getSearcherManager(true).acquire();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searcher;
		
	}
	
	public void closeSeacher(IndexSearcher searcher){
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
	private final void closeSearcher(IndexSearcher searcher) {
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
	 * 分页排序
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
			//排序方向
			boolean reverse=true;
			if(StringUtils.isNotEmpty(dir)&&dir.equals("ASC")){
				reverse=false;
				logger.info(String.format("排序方向：%s", "降序"));
			}else{
				logger.info(String.format("排序方向：%s", "升序"));
			}
			Sort sort=null;
			if(StringUtils.isEmpty(sortFiled)||StringUtils.equals("createDate", sortFiled)){
				//默认排序字段：创建时间
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
			//分页
			for (int i = ((nowPage - 1) * pageSize); i < (nowPage * pageSize); i++) {
				if (i == scoreDocs.length) {
					break;
				}
				ScoreDoc scoreDoc = scoreDocs[i];
				Document document = searcher.doc(scoreDoc.doc);
				list.add(document);
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
	 * 获得单例对象
	 * @return
	 */
	public static PersonStoreNRTSearch getInstance() {
		return personStoreNRTSearch;
	}
}
