package com.ushine.util;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class IndexWriterUtil {
	private Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_35);
	private String indexPath;
	
	
	public IndexWriterUtil(Analyzer analyzer, String indexPath) {
		this.analyzer = analyzer;
		this.indexPath = indexPath;
	}
	
	public IndexWriterUtil() {
		
	}
	//private	Directory directory;
	/*
	 * 设置分词器
	 */
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	/**
	 * 索引的创建路径
	 * @param indexPath
	 */
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	/**
	 * 创建新的索引库
	 * @param documents
	 */
	public void createIndex(List<Document> documents){
		IndexWriter writer=null;
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_35, analyzer);
		try {
			//directory=new RAMDirectory();
			Directory directory=FSDirectory.open(new File(indexPath));
			writer=new IndexWriter(directory, config);
			//清空原来的
			writer.deleteAll();
			for (Document document : documents) {
				//添加document
				writer.addDocument(document);
			}
			//addDirectory();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeWriter(writer);
		}
	}
	/**
	 * 删除索引
	 * @param term Term对象
	 */
	public void deleteIndex(Term term){
		IndexWriter writer=null;
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_35, analyzer);
		try {
			Directory directory=FSDirectory.open(new File(indexPath));
			//directory=new RAMDirectory();
			writer=new IndexWriter(directory, config);
			writer.deleteDocuments(term);
			writer.forceMergeDeletes();
			//提交删除
			writer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeWriter(writer);
		}		
	}
	/**
	 * 添加一条新的索引
	 * @param doc Document对象
	 */
	public void addIndex(Document doc){
		IndexWriter writer=null;
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_35, analyzer);
		try {
			Directory directory=FSDirectory.open(new File(indexPath));
			writer=new IndexWriter(directory, config);
			writer.addDocument(doc);
			//提交
			writer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeWriter(writer);
		}
	}
	/**
	 * 添加索引集合
	 * @param docs
	 */
	public void addIndex(List<Document> docs){
		IndexWriter writer=null;
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_35, analyzer);
		try {
			Directory directory=FSDirectory.open(new File(indexPath));
			writer=new IndexWriter(directory, config);
			for (Document doc : docs) {
				writer.addDocument(doc);
			}
			//提交
			writer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeWriter(writer);
		}
	}
	
	/**
	 * 更新索引
	 * @param term Term对象
	 * @param doc  Document对象
	 */
	public void updateIndex(Term term,Document doc){
		IndexWriter writer=null;
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_35, analyzer);
		try {
			//先删除原来的
			Directory directory=FSDirectory.open(new File(indexPath));
			//directory=new RAMDirectory();
			writer=new IndexWriter(directory, config);
			//添加一条新的
			writer.updateDocument(term, doc);
			//提交
			writer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeWriter(writer);
		}
	}
	/**
	 * 关闭IndexWriter
	 */
	private void closeWriter(IndexWriter writer){
		if(writer!=null){
			try {
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 合并索引库
	 */
	private void addDirectory(){
		IndexWriter writer=null;
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_35, analyzer);
		try {
			Directory fsDirectory=FSDirectory.open(new File(indexPath));
			//writer=new IndexWriter(directory, config);
			//writer.addIndexes(new Directory[]{directory});
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeWriter(writer);
		}
	}
}
