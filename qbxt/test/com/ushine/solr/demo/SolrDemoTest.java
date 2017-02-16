package com.ushine.solr.demo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ushine.solr.demo.SolrDemo;
import com.ushine.solr.solrbean.PersonStore;

public class SolrDemoTest {
	
	 private static HttpSolrServer solrServer=null;
	 private SolrDemo solrJDemo=null;
	 
	 @Before
	 public void testGetServer(){
		 solrServer=SolrDemo.getSolrServer();
		 solrJDemo=new SolrDemo();
	 }
	 
	 /**
	  * 测试获得SolrServer
	  */
	 @Test
	 public void testServerNotNull(){
		 assertNotNull(solrServer);
		 assertNotNull(solrJDemo);
	 }
	 /**
	  * 测试获得查询的数量
	  * 精确查询语句(加引号)："personstoreAll:\"董迪迪\""
	  * @throws SolrServerException
	  */
	 @Test
	 public void testGetCountByCondition() throws SolrServerException{
		 //需要精确查询才行
		 assertEquals(1, solrJDemo.getCountByCondition("personstoreAll:\"董昊\""));
		 assertEquals(1, solrJDemo.getCountByCondition("personstoreAll:\"上海市浦东区\""));
		 assertEquals(1, solrJDemo.getCountByCondition("personstoreAll:\"车辆网点\""));
		 assertEquals(3, solrJDemo.getCountByCondition("personstoreAll:佳佳"));
		 assertEquals(1, solrJDemo.getCountByCondition("personstoreAll:\"1988-12-09\""));
		 
	 }
	 /**
	  * 测试以Bean的形式添加索引
	  * @throws IOException
	  * @throws SolrServerException
	  */
	 @Test
	 public void testAddByBean() throws IOException, SolrServerException{
		 PersonStore bean=new PersonStore(0001+"", "董昊", "", "lebron james", "男", "1989-11-09", "山东省济宁市", 
				 	"上海市浦东区", "公安部第三研究所", "专业为GIS", "主要从事java、网络运维", 
				 	"附件,图片,doc文档", "****", "2017-02-09T11:30:20Z", null, null, null, null, null);
		solrJDemo.addDocumentByBean(bean);
		assertEquals(2, solrJDemo.getCountByCondition("personstoreAll:\"董昊\""));
	 }
	 /**
	  * 测试通过List形式添加
	  * @throws IOException
	  * @throws SolrServerException
	  */
	 @Test
	 public void testAddByBeans() throws IOException, SolrServerException{
		PersonStore bean1 = new PersonStore("0002", "董昊", "", "lebron james", "男", "1989-11-09", "山东省济宁市", "上海市浦东区",
				"公安部第三研究所", "专业为GIS", "主要从事java、网络运维", "附件,图片,doc文档", "公司人员车辆网店表,佳吉最新",
				"2017-02-01T00:00:000Z", null, null, null, "1002361422,13699414937", "donglebron@sina.com,lerbondong@sina.com");
		PersonStore bean2 = new PersonStore("0003", "董雪", "", "kobe bryant", "男", "1988-12-09", "山东省济宁市", "上海市普陀区",
				"公安部第三研究所", "专业为GIS", "主要从事java、网络运维", "附件,图片,doc文档", "公司车辆网点表,佳吉最新",
				"2017-02-02T00:00:00.000Z", null, null, null, null, null);
		PersonStore bean3 = new PersonStore("0001", "董月", "", "dong yue", "男", "2008-12-28", "山东省济宁市", "上海市静安区",
				"公安部第三研究所", "专业为GIS", "主要从事java、网络运维", "附件,图片,doc文档", "佳佳",
				"2017-02-03T00:00:00.000Z", null, null, null, null, null);
		List<PersonStore> list = new ArrayList<>();
		list.add(bean1);
		list.add(bean2);
		list.add(bean3);
		solrJDemo.addDocumentByBeans(list);
		assertEquals(3, solrJDemo.getCountByCondition("*:*"));
	 }
	 /**
	  * 测试按数组删除
	 * @throws IOException 
	 * @throws SolrServerException 
	  */
	 @Test
	 public void testDeleteByIds() throws SolrServerException, IOException{
		 solrJDemo.deleteByIds(new String[]{"0001","0002"});
		 assertEquals(0, solrJDemo.getCountByCondition("personId:0001"));
		 assertEquals(0, solrJDemo.getCountByCondition("personId:0002"));
		 assertEquals(1, solrJDemo.getCountByCondition("personId:0003"));
	 }
	 /**
	  * 测试根据id删除
	  * @throws SolrServerException
	  * @throws IOException
	  */
	 @Test
	 public void testDeleteById() throws SolrServerException, IOException{
		 assertEquals(1, solrJDemo.getCountByCondition("personId:0003"));
		 solrJDemo.deleteDocumentById("0003");
		 assertEquals(0, solrJDemo.getCountByCondition("personId:0003"));
		 assertEquals(0, solrJDemo.getCountByCondition("personId:*"));
	 }
	 /**
	  * 测试更新
	  * @throws SolrServerException
	  * @throws IOException
	  */
	 @Test
	 public void testUpdateDocumentByBean() throws SolrServerException, IOException{
		 assertEquals(0, solrJDemo.getCountByCondition("personstoreAll:\"董月\""));
		 PersonStore bean=new PersonStore("董盼", "", "lebron james", "男", "1989-11-09", "山东省济宁市", "上海市浦东区", 
				 "公安部第三研究所", "专业为GIS", "主要从事java、网络运维", "附件,图片,doc文档", "公司人员车辆网店表,佳吉最新",
				 "2017-02-01T00:00:00.000Z", null, null, null, "1002361422,13699414937", "donglebron@sina.com,lerbondong@sina.com");
		 solrJDemo.updateDocumentByBean("0001", bean);
		 assertEquals(1, solrJDemo.getCountByCondition("personstoreAll:\"董月\""));
	 }
	 
	 /**
	  * 测试时间范围查询
	  * @throws SolrServerException
	  */
	 @Test
	 public void testGetListByDate() throws SolrServerException{
		 List<String> list = solrJDemo.getListByDate("createDate", "2017-02-01T00:00:00Z", "2017-02-03T23:59:59Z");
		 assertEquals(3, list.size());
	 }
	 /**
	  * 测试查询输出详细信息
	  * @throws SolrServerException
	  */
	 @Test
	 public void testGetDetailsByCondition() throws SolrServerException{
		 solrJDemo.getDetailsByCondition("*:*");
	 }
	 /**
	  * 测试删除所有
	  * @throws SolrServerException
	  * @throws IOException
	  */
	 @Test
	 public void testDeleteAll() throws SolrServerException, IOException{
		 solrJDemo.deleteAll();
		 assertEquals(0, solrJDemo.getCountByCondition("personId:*"));
	 }
	 
	 /**
	  * 关闭HttpSolrServer
	  */
	 @AfterClass
	 public static void tearDown(){
		 solrServer.shutdown();
	 }
}
