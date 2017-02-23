package com.ushine.solr.factory;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.ushine.common.config.Configured;
/**
 * 工厂类，获得每个solr索引Server的单例
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
	/**
	 * 外来文档的solr地址
	 */
	private final static String ODSSOLRURL = Configured.getInstance().get("outsideDocStoreUrl");
	private static HttpSolrServer odsSolrServer=null;
	/**
	 * 领导讲话的solr地址
	 */
	private final static String LSSSOLRURL=Configured.getInstance().get("leadSpeakStoreUrl");
	private static HttpSolrServer lssSolrServer=null;
	/**
	 * 线索的solr地址
	 */
	private static final String CSSOLRURL=Configured.getInstance().get("clueStoreUrl");
	private static HttpSolrServer csSolrServer=null;
	/**
	 * 静态代码块，类初始化时加载，只加载一次且线程安全
	 */
	static{
		psSolrServer=new HttpSolrServer(PSSOLRURL);
		vwsSolrServer=new HttpSolrServer(VWSSOLRURL);
		odsSolrServer=new HttpSolrServer(ODSSOLRURL);
		lssSolrServer=new HttpSolrServer(LSSSOLRURL);
		csSolrServer=new HttpSolrServer(CSSOLRURL);
	}
	
	/**
	 * 获得人员库的单例SolrServer
	 * @return HttpSolrServer
	 */
	public static HttpSolrServer getPSSolrServerInstance(){
		return psSolrServer;
	}
	/**
	 * 获得业务文档库的单例SolrServer
	 * @return HttpSolrServer
	 */
	public static HttpSolrServer getVWSSolrServerInstance(){
		return vwsSolrServer;
	}
	/**
	 * 获得外来文档的单例SolrServer
	 * @return HttpSolrServer
	 */
	public static HttpSolrServer getODSSolrServerInstance(){
		return odsSolrServer;
	}
	/**
	 * 获得领导讲话的单例SolrServer
	 * @return HttpSolrServer
	 */
	public static HttpSolrServer getLSSSolrServerInstance(){
		return lssSolrServer;
	}
	/**
	 * 获得线索的单例SolrServer
	 * @return HttpSolrServer
	 */
	public static HttpSolrServer getCSSolrServerInstance(){
		return csSolrServer;
	}
}
