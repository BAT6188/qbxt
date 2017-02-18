package com.ushine.solr.demo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.ushine.solr.solrbean.PersonStoreSolr;
/**
 * 利用solrj做索引的crud操作<br>
 * 注意：<br>
 * 1、利用DocumentObjectBinder对象将SolrInputDocument和 Bean对象相互转换
 * 2、日期只支持utc格式的：createDate:[2017-02-01T00:00:00Z TO 2017-02-03T23:59:59Z]
 * @author dh
 *
 */
public class SolrDemo {
	
	private Logger logger=Logger.getLogger(SolrDemo.class);
	private Log log=LogFactoryImpl.getLog(SolrDemo.class);
	/**
	 * PersonStore的solr地址
	 * Solr Admin中提供的：http://localhost:8085/personstore/select?q=*%3A*&wt=csv&indent=true
	 */
	private final static String SolrURL = "http://localhost:8085/personstore";
	private static HttpSolrServer solrServer = null;

	/**
	 * solrServer是线程安全的，所以在使用时需要使用单例的模式，减少资源的消耗
	 * 
	 * @return HttpSolrServer
	 */
	public static HttpSolrServer getSolrServer() {
		if (solrServer == null) {
			solrServer = new HttpSolrServer(SolrURL);
		}
		return solrServer;
	}
	/**
	 * 根据查询条件获得SolrDocumentList
	 * @param condition 查询条件
	 * @return SolrDocumentList
	 * @throws SolrServerException
	 */
	protected SolrDocumentList getListByCondition(String condition) throws SolrServerException{
		SolrDocumentList sdList=null;
		SolrQuery query = new SolrQuery();
		query.setQuery(condition);  
        query.setHighlight(true);                //开启高亮  
        query.setHighlightSimplePre("<aa>");    //后缀 
        query.setHighlightSimplePost("</aa>");    //前缀  
        query.addHighlightField("sex");      //高亮字段  
        
		QueryResponse response = solrServer.query(query);
		
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		Map<String, List<String>> map=highlighting.get("4028318158b444400158b49746570001");
		List<String> list=map.get("sex");
		for (String string : list) {
			System.err.println(string);
		}
		
		/*Map<String, List<String>> map=highlighting.get("personId");
		List<String> list = map.get("infoType");
		for (String string : list) {
			System.err.println(string);
		}*/
		sdList = response.getResults();
		for (SolrDocument solrDocument : sdList) {
			System.err.println(solrDocument.toString());
		}
		logger.error(String.format("符合查询条件为[%s]的总数：%s", condition,sdList.getNumFound()));
		//log.info(String.format("符合查询条件为[%s]的总数：%s", condition,sdList.getNumFound()));
		return sdList;
	}
	/**
	 * 根据查询条件获得符合条件的数量
	 * @param condition 查询条件
	 * @return long
	 * @throws SolrServerException
	 */
	protected long getCountByCondition(String condition) throws SolrServerException{
		return getListByCondition(condition).getNumFound();
	}
	/**
	 * 日期范围查询<br>
	 * 日期只支持utc格式的：createDate:[2017-02-01T00:00:00Z TO 2017-02-03T23:59:59Z]
	 * @param field 范围字段
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return PersonStore的List
	 * @throws SolrServerException
	 */
	@Deprecated
	protected List<String> getListByDate(String field, String startDate, String endDate) throws SolrServerException {
		String condition = String.format("%s:[%s TO %s]", field, startDate, endDate);
		SolrDocumentList sdList = getListByCondition(condition);
		List<String> idList = new ArrayList<>();
		for (SolrDocument document : sdList) {
			String personId = (String) document.getFieldValue("personId");
			idList.add(personId);
		}
		return idList;
	}
	
	/**
	 * SolrInputDocument与bean的转换，要求字段类型对应
	 * PersonStore的createDate不对应转换失败
	 * @param sdList
	 * @return
	 */
	protected List<PersonStoreSolr> documentListToBeanList(SolrDocumentList sdList){
		//利用DocumentObjectBinder对象将SolrInputDocument和 PersonStore对象相互转换
		DocumentObjectBinder binder=new DocumentObjectBinder();
		List<PersonStoreSolr> beans = binder.getBeans(PersonStoreSolr.class, sdList);
		return beans;
	}
	
	/**
	 * 根据查询语句输出详细的查询内容
	 * @param condition 查询条件
	 * @throws SolrServerException 
	 */
	protected void getDetailsByCondition(String condition) throws SolrServerException {
		//获得数量
		SolrDocumentList sdList=getListByCondition(condition);
		/*for (SolrDocument solrDocument : sdList) {
			System.err.println(solrDocument.getFieldValue("infoType"));
		}*/
		//遍历结果
		List<PersonStoreSolr> list=documentListToBeanList(sdList);
		for (PersonStoreSolr personStoreSolr : list) {
			System.err.println(personStoreSolr.toString());
		}
	}
	/**
	 * 通过bean的形式添加索引
	 * @param bean
	 * @throws IOException
	 * @throws SolrServerException
	 */
	protected void addDocumentByBean(PersonStoreSolr bean) throws IOException, SolrServerException{
		solrServer.addBean(bean);
		solrServer.commit();
	}
	/**
	 * 通过List的形式添加索引
	 * @param list
	 * @throws IOException
	 * @throws SolrServerException
	 */
	protected void addDocumentByBeans(List<PersonStoreSolr> list) throws IOException, SolrServerException{
		solrServer.addBeans(list);
		solrServer.commit();
	}
	/**
	 * 根据id数组删除
	 * @param ids
	 * @throws SolrServerException
	 * @throws IOException
	 */
	protected void deleteByIds(String []ids) throws SolrServerException, IOException {
		List<String> list=new ArrayList<>();
		for (String id : ids) {
			list.add(id);
		}
		solrServer.deleteById(list);
		//提交
		solrServer.commit();
	}
	/**
	 * 根据id删除
	 * @param id
	 * @throws SolrServerException
	 * @throws IOException
	 */
	protected void deleteDocumentById(String id) throws SolrServerException, IOException {
		solrServer.deleteById(id);
		//提交
		solrServer.commit();
	}
	/**
	 * 修改是依据id进行覆盖的
	 * @param id 待修改的bean的personId
	 * @param bean 要改成的bean
	 * @throws SolrServerException
	 * @throws IOException
	 */
	protected void updateDocumentByBean(String id,PersonStoreSolr bean) throws SolrServerException, IOException {
		bean.setPersonId(id);
		addDocumentByBean(bean);
	}
	
	protected void getListPages(String condition,int startNumber,int rows) {
		
	}

	/**
	 * 清空索引库
	 * 
	 * @throws SolrServerException
	 * @throws IOException
	 */
	protected void deleteAll() throws SolrServerException, IOException {
		solrServer.deleteByQuery("*:*");
		//操作完需要提交
		solrServer.commit();
	}
}