package com.ushine.luceneindex.index;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

public interface IStoreNRTSearch {
	/**
	 * 根据查询、过滤条件获得数量
	 * @param query 查询条件
	 * @param filter 过滤条件
	 * @return
	 */
	public int getCount(Query query,Filter filter);
	/**
	 * 获得Document对象集合         
	 * @param query Query对象
	 * @param filter 过滤条件
	 * @param size 需要获得的数量
	 * @return Document集合
	 */
	/**
	 * 分页查询
	 * @param query 查询条件
	 * @param filter 过滤条件
	 * @param nowPage 当前页码
	 * @param pageSize  每页显示数量
	 * @return Document集合
	 */
	public List<Document> getDocuments(Query query,Filter filter,int nowPage, int pageSize);
	/**
	 * 分页查询并排序
	 * @param query 查询条件
	 * @param filter 过滤条件
	 * @param sortField 需要排序的字段
	 * @param dir 排序的方向
	 * @param nowPage 当前页码
	 * @param pageSize 每页显示数量
	 * @return Document集合
	 */
	public List<Document> getDocuments(Query query,Filter filter,String sortField,String dir,int nowPage, int pageSize);
}
