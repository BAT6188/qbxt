package com.ushine.solr.factory;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
/**
 * 工厂类，创建每个solr索引Server的单例
 * @author dh
 *
 */
public class SolrServerFactory {
	
	/**
	 * 人员的solr地址
	 */
	private final static String PSSOLRURL = "http://localhost:8085/personstore";
	private static HttpSolrServer psSolrServer=null;
	/**
	 * 业务文档的solr地址
	 */
	private final static String VWSSOLRURL = "http://localhost:8085/vocationalworkstore";
	private static HttpSolrServer vwsSolrServer=null;
	
	/**
	 * 创建人员库的单例SolrServer
	 * @return
	 */
	public static HttpSolrServer getPSSolrServerInstance(){
		if(psSolrServer==null){
			psSolrServer=new HttpSolrServer(PSSOLRURL);
		}
		return psSolrServer;
	}
	/**
	 * 创建业务文档库的单例SolrServer
	 * @return
	 */
	public static HttpSolrServer getVWSSolrServerInstance(){
		if(vwsSolrServer==null){
			vwsSolrServer=new HttpSolrServer(VWSSOLRURL);
		}
		return vwsSolrServer;
	}
}
