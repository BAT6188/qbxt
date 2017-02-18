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
