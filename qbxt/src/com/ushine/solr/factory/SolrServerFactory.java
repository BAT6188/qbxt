package com.ushine.solr.factory;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.ushine.common.config.Configured;
/**
 * 工厂类，创建每个solr索引Server的单例
 * @author dh
 *
 */
public class SolrServerFactory {
	
	/**
	 * 人员的solr地址
	 */
	private final static String PSSOLRURL = Configured.getInstance().get("personStoreUrl");
	private static HttpSolrServer psSolrServer=null;
	/**
	 * 业务文档的solr地址
	 */
	private final static String VWSSOLRURL = Configured.getInstance().get("vocationalWorkStoreUrl");
	private static HttpSolrServer vwsSolrServer=null;
	
	static{
		psSolrServer=new HttpSolrServer(PSSOLRURL);
		vwsSolrServer=new HttpSolrServer(VWSSOLRURL);
	}
	
	/**
	 * 创建人员库的单例SolrServer
	 * @return
	 */
	public static HttpSolrServer getPSSolrServerInstance(){
		return psSolrServer;
	}
	/**
	 * 创建业务文档库的单例SolrServer
	 * @return
	 */
	public static HttpSolrServer getVWSSolrServerInstance(){
		return vwsSolrServer;
	}
}
