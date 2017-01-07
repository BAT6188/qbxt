package com.ushine.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 * 获得IndexReader对象的抽象类
 * @author dh
 *
 */
public abstract class AbstractIndexReaderUtil {
	private IndexReader reader=null;
	
	/**
	 * 设置IndexReader对象的值<br>
	 * 每个类库的IndexReader都不相同
	 * @param reader
	 */
	public void setReader(IndexReader reader) {
		this.reader = reader;
	}

	/**
	 * 抽象方法获得每个索引库IndexReader
	 */
	public abstract IndexReader getIndexReader();
	
	/**
	 * 获得总数
	 * @param query
	 * @return 符合条件的总数 int类型
	 */
	public int getCount(Query query){
		int totalCount=0;
		ScoreDoc []scoreDocs=null;
		IndexSearcher searcher=null;
		try {
			searcher=new IndexSearcher(reader);
			TopDocs topDocs=searcher.search(query, Integer.MAX_VALUE);
			scoreDocs=topDocs.scoreDocs;
			totalCount=scoreDocs.length;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭searcher
			if(searcher!=null){
				try {
					searcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return totalCount;
	}
	/**
	 * 获得Document对象集合
	 * @param query Query对象
	 * @param size 数量
	 * @return
	 */
	public List<Document> getSearcher(Query query,int size){
		//一次性加载到内存中会严重降低效率
		//而且还可能造成内存不足
		//还是使用searchAfter比较合适
		IndexSearcher searcher=null;
		List<Document> list=new ArrayList<Document>();
		ScoreDoc []scoreDocs=null;
		try {
			searcher=new IndexSearcher(reader);
			TopDocs topDocs=searcher.search(query, size);
			scoreDocs=topDocs.scoreDocs;
			if (scoreDocs.length>0) {
				for (ScoreDoc scoreDoc : scoreDocs) {
					Document document=searcher.doc(scoreDoc.doc);
					list.add(document);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭searcher
			if(searcher!=null){
				try {
					searcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**
	 * 分页
	 * @param query
	 * @param nowPage
	 * @param pageSize
	 * @return
	 */
	public List<Document> getSearcher(Query query,int nowPage,int pageSize){
		IndexSearcher searcher=null;
		List<Document> list=new ArrayList<Document>();
		ScoreDoc []scoreDocs=null;
		try {
			list.clear();
			searcher=new IndexSearcher(reader);
			TopDocs topDocs=searcher.search(query, Integer.MAX_VALUE);
			scoreDocs=topDocs.scoreDocs;
			if (scoreDocs.length>0) {
				//共多少页
				int pageCount=(scoreDocs.length+pageSize-1)/pageSize;
				//第一页 每页40条 0-39
				//第二页                    40-79
				for(int i=((nowPage-1)*pageSize);i<(nowPage*pageSize-1);i++){
					if (i==scoreDocs.length) {
						break;
					}
					ScoreDoc scoreDoc=scoreDocs[i];
					Document document=searcher.doc(scoreDoc.doc);
					list.add(document);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭searcher
			if(searcher!=null){
				try {
					searcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**
	 * 高亮字段值
	 * @param document 文档
	 * @param query 查询条件
	 * @param analyzer 分词器
	 * @param filed 字段
	 * @return
	 */
	/*public String highlighterFiled(List<Document> documents,Query query,Analyzer analyzer,String filed){
		//默认是没有高亮
		String result=null;
		//System.err.println(document.get(filed));
		SimpleHTMLFormatter htmlFormatter=new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Highlighter highlighter=new Highlighter(htmlFormatter, new QueryScorer(query));
		try {
			for (Document document : documents) {
				TokenStream tokenStream=analyzer.tokenStream(filed, new StringReader(document.get(filed)));
				if(null!=highlighter.getBestFragment(tokenStream, document.get(filed))){
					//做高亮
					result=highlighter.getBestFragment(tokenStream, document.get(filed));
				}else{
					result=document.get(filed);
				}
			}
		} catch (Exception e) {
			//result="该字段值为空";
			e.printStackTrace();
		}
		return result;
	}*/
	
	/**
	 * 过滤查询
	 * @param query
	 * @param nowPage
	 * @param pageSize
	 * @param filter
	 * @return
	 */
	public List<Document> getSearcher(Query query,int nowPage,int pageSize,Filter filter){
		IndexSearcher searcher=null;
		List<Document> list=new ArrayList<Document>();
		ScoreDoc []scoreDocs=null;
		try {
			list.clear();
			searcher=new IndexSearcher(reader);
			TopDocs topDocs=searcher.search(query,filter, Integer.MAX_VALUE);
			scoreDocs=topDocs.scoreDocs;
			if (scoreDocs.length>0) {
				//共多少页
				int pageCount=(scoreDocs.length+pageSize-1)/pageSize;
				//第一页 每页40条 0-39
				//第二页                    40-79
				for(int i=((nowPage-1)*pageSize);i<(nowPage*pageSize-1);i++){
					if (i==scoreDocs.length) {
						break;
					}
					ScoreDoc scoreDoc=scoreDocs[i];
					Document document=searcher.doc(scoreDoc.doc);
					list.add(document);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭searcher
			if(searcher!=null){
				try {
					searcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	/**
	 * 根据doc的id获得document对象
	 * @param i id
	 * @return Document
	 */
	public Document getDocument(int i){
		IndexSearcher searcher=null;
		Document document=null;
		try {
			searcher=new IndexSearcher(reader);
			document=searcher.doc(i);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭searcher
			if(searcher!=null){
				try {
					searcher.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return document;
	}
}
