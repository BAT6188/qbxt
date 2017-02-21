package com.ushine.solr.service.impl;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IVocationalStoreSolrService;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.VocationalWorkStoreVo;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.VocationalWorkStore;

public class VocationalStoreSolrServiceImplTest extends AbstractSpringJUnitTest<VocationalWorkStore> {
	
	@Autowired IVocationalStoreSolrService solrService;
	private HttpSolrServer getServer(){
		server=SolrServerFactory.getVWSSolrServerInstance();
		return server;
	}
	
	public VocationalWorkStore getStore(String id){
		VocationalWorkStore store=new VocationalWorkStore();
		store.setAction("0");
		store.setId(id);
		store.setDocName("测试");
		store.setDocNumber("第100期");
		store.setCreateDate("2017-01-11 00:00:30");
		store.setAttaches("git权威指南，ssh讲义，hadoop实战");
		InfoType infoType=new InfoType();
		infoType.setTypeName("十二局");		
		store.setInfoType(infoType);
		store.setTheOriginal("近期与境外机构联系密切组织规模不断壮大");
		return store;
	}
	
	public List<VocationalWorkStore> getList(){
		List<VocationalWorkStore> list=new ArrayList<>();
		list.add(getStore("100"));
		list.add(getStore("101"));
		list.add(getStore("102"));
		return list;
	}
	
	QueryBean queryBean=new QueryBean(null, null, null, null, null, null, null, null, null, "1970-10-11", "2017-02-21");
	
	@Test
	public void testServerNotNull(){
		assertNotNull(getServer());
		assertNotNull(solrService);
	}
	/**
	 * 创建新索引
	 */
	@Test
	public void testCreateNewIndex(){
		solrService.createNewIndexByStores(getServer());
		
		assertEquals(23, solrService.getDocumentsCount(server, queryBean));
		solrService.deleteAll(getServer());
		assertEquals(0, solrService.getDocumentsCount(server, queryBean));
	}
	
	@Test
	public void testAdd(){
		solrService.deleteAll(getServer());
		solrService.addDocumentByStore(getServer(), getStore("100"));
		assertEquals(1, solrService.getDocumentsCount(getServer(), queryBean));
	}
	
	@Test
	public void testAddStores(){
		solrService.deleteAll(getServer());
		solrService.addDocumentByStores(getServer(), getList());
		assertEquals(3, solrService.getDocumentsCount(getServer(), queryBean));
	}
	
	@Test
	public void testDelete(){
		testAddStores();
		solrService.deleteDocumentById(getServer(), "100");
		assertEquals(2, solrService.getDocumentsCount(getServer(), queryBean));
		solrService.deleteDocumentByIds(getServer(), new String[]{"101","102"});
		assertEquals(0, solrService.getDocumentsCount(getServer(), queryBean));
	}
	
	@Test
	public void testUpdate(){
		testAddStores();
		VocationalWorkStore daoStore=new VocationalWorkStore();
		InfoType infoType=new InfoType();
		infoType.setTypeName("公安部");	
		daoStore.setInfoType(infoType);
		daoStore.setDocNumber("00001");
		QueryBean bean2=new QueryBean(null, null, null,null, "公安部", null, null, null, null, "1970-01-01", "2017-02-21");
		QueryBean bean1=new QueryBean(null, null, null,"docNumber", "00001", null, null, null, null, "1970-01-01", "2017-02-21");
		assertEquals(0, solrService.getDocumentsCount(getServer(), bean2));
		assertEquals(0, solrService.getDocumentsCount(getServer(), bean1));
		//修改后
		solrService.updateDocumentByStore(getServer(), "100", daoStore);	
		assertEquals(1, solrService.getDocumentsCount(getServer(), bean2));
		assertEquals(1, solrService.getDocumentsCount(getServer(), bean1));
	}
	
	@Test
	public void testGetCount(){
		testAddStores();
		//以下关键字都通过测试："测试" "十二局" "git" "hadoop" "境外机构" 
		QueryBean bean2=new QueryBean(null, null, null,"docName", "测试", null, null, null, null, "1970-01-01", "2017-02-21");
		assertEquals(3, solrService.getDocumentsCount(getServer(), bean2));
		
		QueryBean bean3=new QueryBean(null, null, null, null, "境外机构", null, null, null, null, "1970-01-01", "2017-02-21");
		assertEquals(3, solrService.getDocumentsCount(getServer(), bean3));
		
		QueryBean bean1=new QueryBean(null, null, null,"infoType", "十二局", null, null, null, null, "1970-01-01", "2017-02-21");
		logger.info("查询数量：\r\n"+solrService.getDocumentsCount(getServer(), bean1));
		//assertEquals(3, solrService.getDocumentsCount(getServer(), bean1));
	}
	/**
	 * 创建时间的范围、权限、再查询搜索
	 */
	@Test
	public void testTimeRange(){
		solrService.deleteAll(getServer());
		InfoType type=new InfoType();
		type.setTypeName("十二局");
		VocationalWorkStore store1=new VocationalWorkStore("100", "测试文档1", "0001", "2017-01-01", type, "2017-01-01 00:00:00");
		VocationalWorkStore store2=new VocationalWorkStore("200", "测试文档2", "0002", "2017-01-02", type, "2017-01-02 01:00:00");
		VocationalWorkStore store3=new VocationalWorkStore("300", "测试文档2", "0002", "2017-01-03", type, "2017-01-03 02:00:00");
		VocationalWorkStore store4=new VocationalWorkStore("400", "测试文档4", "0002", "2017-01-04", type, "2017-01-04 03:00:00");
		VocationalWorkStore store5=new VocationalWorkStore("500", "测试文档5", "0005", "2017-01-05", type, "2017-01-05 04:00:00");
		//, , , , , ""
		VocationalWorkStore store6=new VocationalWorkStore("600", "测试文档5", "0005", "2017-01-05", type, "101", null, null, "2016-01-05 04:00:00");
		List<VocationalWorkStore> list=new ArrayList();
		list.add(store1);
		list.add(store2);
		list.add(store3);
		list.add(store4);
		list.add(store5);
		list.add(store6);
		solrService.addDocumentByStores(getServer(), list);
		QueryBean bean1=new QueryBean(null, null, null, null, null, null, null, null, null, "2017-01-01", "2017-01-01");
		assertEquals(1, solrService.getDocumentsCount(getServer(), bean1));
		QueryBean bean2=new QueryBean(null, null, null, null, null, null, null, null, null, "2017-01-02", "2017-01-05");
		assertEquals(4, solrService.getDocumentsCount(getServer(), bean2));
		//uid权限
		QueryBean bean3=new QueryBean("101", null, null, null, null, null, null, null, null, "2015-01-02", "2017-01-05");
		assertEquals(1, solrService.getDocumentsCount(getServer(), bean3));
		//再查询
		QueryBean bean4=new QueryBean(null, null, null, "docNumber", "0002", null, null, null, null, "2015-01-02", "2017-01-05");
		assertEquals(3, solrService.getDocumentsCount(getServer(), bean4));
		
		QueryBean bean5=new QueryBean(null, null, null,"docNumber", "0002", "docName", "测试文档2", null, null, "2015-01-02", "2017-01-05");
		assertEquals(2, solrService.getDocumentsCount(getServer(), bean5));
		
	}
	
	@Test
	public void testToVo(){
		//testAddStores();
		testTimeRange();
		List<VocationalWorkStoreVo> voList=solrService.getDocuementsVO(getServer(), queryBean, 1, 2);
		for (VocationalWorkStoreVo vocationalWorkStoreVo : voList) {
			logger.info(vocationalWorkStoreVo.toString());
		}
	}
}
