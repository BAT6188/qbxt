package com.ushine.solr.util;

import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * Query的应用类
 * @author Administrator
 *
 */
public class SolrQueryUtils {
	
	/**
	 * 常量personId
	 */
	public static final String PERSON_ID = "personId";
	/**
	 * 查询字段常量
	 */
	public static final String PERSONSTOREALL = "personstoreAll";
	
	/**
	 * 常量字段createDate
	 */
	public static final String CREATEDATE = "createDate";
	/**
	 * 高亮前缀
	 */
	public static String HIGHLIGHT_PRE="<span style='background-color:#ffd73a'>";
	/**
	 * 高亮后缀
	 */
	public static String HIGHLIGHT_POST="</span>";
	/**
	 * 再查询
	 */
	public static String QUERY_AGAIN="queryAgain";
	/**
	 * 返回SolrQuery
	 * @param condition 基本的查询条件
	 * @param idMap map中包含uid did oid 以及查询的关键字
	 * @return
	 */
	public static SolrQuery	getQuery(String condition,Map<String, String> idMap) {
		SolrQuery query = new SolrQuery(condition);
		String uid=idMap.get("uid");
		String oid=idMap.get("oid");
		String did=idMap.get("did");
		//设置过滤条件，针对权限
		if (null == uid && null == oid && null != did) {
			// 读取所属部门
			query.setFilterQueries("did:\""+did+"\"");
		}
		if (null == did && null == uid && null != oid) {
			// 读取所属组织
			query.setFilterQueries("oid:\""+oid+"\"");
		}
		if (null == oid && null == did && null != uid) {
			// 读取个人数据
			query.setFilterQueries("uid:\""+uid+"\"");
		}
		return query;
	}
}
