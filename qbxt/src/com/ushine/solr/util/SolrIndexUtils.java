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
		try {
			boolean result=Boolean.getBoolean(Configured.getInstance().get(createIndex));
			if (result) {
				return true;
			}
		} catch (Exception e) {
			logger.error("获得配置文件信息异常，返回false");
			e.printStackTrace();
		}
		return false;
	}
}
