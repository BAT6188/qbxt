package com.ushine.store.vo;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
/**
 * 自定义的查询条件类
 * @author dh
 *
 */
public class MyQueryCondition {
	public MyQueryCondition() {
		
	}
	/**
	 * 带参数的构造函数
	 * @param booleanQuery BooleanQuery查询条件
	 * @param filter 过滤条件 
	 * @param sortField 排序字段
	 * @param dir 升序或降序
	 */
	public MyQueryCondition(BooleanQuery booleanQuery, Filter filter, String sortField, String dir) {
		this.booleanQuery = booleanQuery;
		this.filter = filter;
		this.sortField = sortField;
		this.dir = dir;
	}
	/**
	 * BooleanQuery查询条件
	 * @return
	 */
	public BooleanQuery getBooleanQuery() {
		return booleanQuery;
	}
	/**
	 * BooleanQuery查询条件
	 * @param booleanQuery
	 */
	public void setBooleanQuery(BooleanQuery booleanQuery) {
		this.booleanQuery = booleanQuery;
	}
	/**
	 * 过滤条件
	 * @return
	 */
	public Filter getFilter() {
		return filter;
	}
	/**
	 * 过滤条件
	 * @param filter
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	/**
	 * 排序字段
	 * @return
	 */
	public String getSortField() {
		return sortField;
	}
	/**
	 * 排序字段
	 * @param sortField
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	/**
	 * 升序或降序
	 * @return
	 */
	public String getDir() {
		return dir;
	}
	/**
	 * 升序或降序
	 * @param dir
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	private BooleanQuery booleanQuery;
	private Filter filter;
	private String sortField;
	private String dir;
	
}
