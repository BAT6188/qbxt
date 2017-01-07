package com.ushine.store.service;

import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;

import com.ushine.storeInfo.model.PersonStore;
/**
 * 操作Lucene索引的基础接口<br>
 * 包括增、删、改 、查
 * @author Administrator
 *
 */
public interface IPersonStoreIndexService {
	/**
	 * 将对象转为document对象
	 * @param store 对象实例
	 * @return document对象
	 */
	public  Document storeToDocument(PersonStore store)throws Exception;

	/**
	 * 为集合创建新的索引库
	 * @param list list集合
	 */
	public  void createIndex(List<PersonStore> list)throws Exception;
	
	/**
	 * 添加一条新的索引
	 * 
	 * @param object
	 *            类对象
	 */
	public  void saveIndex(PersonStore store)throws Exception;

	/**
	 * 添加多条索引
	 * 
	 * @param list
	 *            类对象集合
	 */
	public  void saveIndex(List<PersonStore> list)throws Exception;

	/**
	 * 更新一条索引
	 * 
	 * @param object
	 *            类对象
	 */
	public  void updateIndex(PersonStore store)throws Exception;

	/**
	 * 删除一条索引
	 * @param object 对象实例
	 * @throws Exception
	 */
	public  void deleteIndex(String id)throws Exception;

	/**
	 * 删除多个索引对象
	 * @param list list集合
	 * @throws Exception
	 */
	public  void deleteIndex(String []ids)throws Exception;

	/**
	 * 根据查询、过滤条件获得数量
	 * 
	 * @param query 查询条件
	 * @param filter 过滤条件，允许为null
	 * @return 符合条件的总数
	 */
	public int getCount(Query query, Filter filter)throws Exception;
	
	/**
	 * 分页查询并排序
	 * @param query 查询条件
	 * @param filter 过滤条件，允许为null
	 * @param sortField 需要排序的字段
	 * @param dir 排序的方向
	 * @param nowPage 当前页码
	 * @param pageSize 每页显示数量
	 * @return Document集合
	 */
	public List<Document> getDocuments(Query query,Filter filter,
			String sortField,String dir,int nowPage, int pageSize)throws Exception;
}
