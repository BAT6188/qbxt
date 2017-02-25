package com.ushine.solr.util;

import org.apache.log4j.Logger;

import com.ushine.common.config.Configured;

public class SolrIndexUtils {
	static Logger logger=Logger.getLogger(SolrIndexUtils.class);
	/**
	 * 根据配置文件判断是否需要创建新的索引集合
	 * 配置项中的值为true、false
	 * @return true创建，false不创建新的
	 */
	public static boolean createNewIndex(String createIndex){
		
		return Boolean.parseBoolean(Configured.getInstance().get(createIndex));
	}
}
