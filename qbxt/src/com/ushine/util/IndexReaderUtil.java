package com.ushine.util;


import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
/**
 *
 * 获得IndexReader对象<br>
 * 
 * 创建成单例的模式
 */
public class IndexReaderUtil {
	
	private static IndexReader reader=null;
	/**
	 * 私有
	 */
	private IndexReaderUtil(){
		
	}
	/**
	 * 创建IndexReader,实时搜索<br>
	 * @param directoryPath 索引库的路径
	 * @return IndexReader
	 */
	public synchronized static void getIndexReader(String directoryPath){
		try {
			if(reader==null){
				reader=IndexReader.open(FSDirectory.open(new File(directoryPath)));
			}else{
				IndexReader indexReader=IndexReader.openIfChanged(reader);
				if(indexReader!=null){
					//关闭原来的
					reader.close();
					reader=indexReader;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获得Document对象集合
	 * @param query Query对象
	 * @param size 数量
	 * @return
	 */
	public static List<Document> getSearcher(Query query,int size){
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
	public static List<Document> getSearcher(Query query,Filter filter,int nowPage,int pageSize){
		IndexSearcher searcher=null;
		List<Document> list=new ArrayList<Document>();
		ScoreDoc []scoreDocs=null;
		try {
			list.clear();
			searcher=new IndexSearcher(reader);
			TopDocs topDocs=null;
			if (null!=filter) {
				//过滤
				topDocs=searcher.search(query,filter,Integer.MAX_VALUE);
			}else{
				topDocs=searcher.search(query,Integer.MAX_VALUE);
			}
			scoreDocs=topDocs.scoreDocs;
			if (scoreDocs.length>0) {
				//共多少页
				int pageCount=(scoreDocs.length+pageSize-1)/pageSize;
				System.out.println("=====pageCount:"+(pageCount)+"=====");
				System.out.println("=====scoreDocs.length:"+(scoreDocs.length)+"=====");
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
	public static String highlighterFiled(Document document,Query query,Analyzer analyzer,String filed){
		//默认是没有高亮
		String result=document.get(filed);
		//System.err.println(document.get(filed));
		
		SimpleHTMLFormatter htmlFormatter=new SimpleHTMLFormatter("<font color='red'/>", "</font>");
		Highlighter highlighter=new Highlighter(htmlFormatter, new QueryScorer(query));
		try {
			TokenStream tokenStream=analyzer.tokenStream(filed, new StringReader(document.get(filed)));
			if(null!=highlighter.getBestFragment(tokenStream, document.get(filed))){
				//做高亮
				result=highlighter.getBestFragment(tokenStream, document.get(filed));
			}
		} catch (Exception e) {
			//result="该字段值为空";
			e.printStackTrace();
		}
		return result;
	}
	
	public static Document getDocument(int i){
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
