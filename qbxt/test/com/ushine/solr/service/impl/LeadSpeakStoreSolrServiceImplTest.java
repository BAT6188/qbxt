package com.ushine.solr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

//import com.ushine.abstracttest.AbstractAssert;
import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.solr.service.ILeadSpeakStoreSolrService;
import com.ushine.solr.solrbean.LeadSpeakStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.LeadSpeakStoreVo;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.LeadSpeakStore;

public class LeadSpeakStoreSolrServiceImplTest extends AbstractSpringJUnitTest<LeadSpeakStore> {
	@Autowired ILeadSpeakStoreSolrService solrService;
	
	@Test
	@Ignore
	public void testConvert(){
		LeadSpeakStoreSolrServiceImpl impl=new LeadSpeakStoreSolrServiceImpl();
		LeadSpeakStore store1=new LeadSpeakStore();
		store1.setInfoType(null);
		//1487779271000
		store1.setCreateDate("2017-02-23 00:01:11");
		store1.setAction("1");
		store1.setMeetingName("会议1");
		//
		LeadSpeakStore store2=new LeadSpeakStore();
		InfoType infoType=new InfoType();
		infoType.setTypeName("局领导");
		store2.setInfoType(infoType);
		//转solr
		List<LeadSpeakStore> list=new ArrayList<LeadSpeakStore>();
		list.add(store1);
		list.add(store2);
		List<LeadSpeakStoreSolr> solrList = impl.convertToSolrBeanList(list);
		assertEquals("", solrList.get(0).getInfoType());
		assertEquals("局领导", solrList.get(1).getInfoType());
		//转LeadSpeakStoreVo
		List<LeadSpeakStoreVo> voList=impl.convertToLeadSpeakStoreVoList(solrList);
		assertEquals("2017-02-23 00:01:11", voList.get(0).getCreateDate());
	}
	
	@Test
	@Ignore
	public void testAdd(){
		solrService.deleteAll();
		QueryBean queryBean=new QueryBean();
		queryBean.setStartDate("1971-01-01");
		queryBean.setEndDate("2017-02-23");
		assertEquals(0, solrService.getDocumentsCount(queryBean));
		//新增
		LeadSpeakStore store1=new LeadSpeakStore();
		store1.setInfoType(null);
		//1487779271000
		store1.setCreateDate("2017-02-23 00:01:11");
		store1.setAction("1");
		store1.setMeetingName("会议1");
		solrService.addDocumentByStore(store1);
		//新增后
		assertEquals(1, solrService.getDocumentsCount(queryBean));
		queryBean.setQueryField(null);
		queryBean.setQueryFieldValue("会议1");
		assertEquals(1, solrService.getDocumentsCount(queryBean));
		//meetingName
		queryBean.setQueryField("meetingName");
		queryBean.setQueryFieldValue("会议1");
		assertEquals(1, solrService.getDocumentsCount(queryBean));
		
	}
	/**
	 * id不能为null，也不能重复
	 */
	@Test
	public void testGet(){
		//清空
		solrService.deleteAll();
		LeadSpeakStore store1=new LeadSpeakStore();
		store1.setInfoType(null);
		store1.setId("100");
		//加第一条
		store1.setCreateDate("2017-02-23 00:01:11");
		solrService.addDocumentByStore(store1);
		//加第二条
		LeadSpeakStore store2=new LeadSpeakStore();
		store2.setCreateDate("2017-02-23 00:01:11");
		store2.setId("101");
		store2.setAttaches("附件1");
		store2.setAttachContent("git权威指南");
		solrService.addDocumentByStore(store2);
		//应该有2条
		QueryBean queryBean2=new QueryBean();
		queryBean2.setStartDate("1971-01-01");
		queryBean2.setEndDate("2017-02-23");
		assertEquals(2, solrService.getDocumentsCount(queryBean2));
		//查附件
		QueryBean queryBean3=new QueryBean();
		queryBean3.setQueryFieldValue("附件1");
		queryBean3.setStartDate("1971-01-01");
		queryBean3.setEndDate("2017-02-23");
		assertEquals(1, solrService.getDocumentsCount(queryBean3));
		//查附件内容
		QueryBean queryBean4=new QueryBean();
		queryBean4.setQueryFieldValue("权威指南");
		queryBean4.setStartDate("1971-01-01");
		queryBean4.setEndDate("2017-02-23");
		assertEquals(1, solrService.getDocumentsCount(queryBean4));
	}
}
