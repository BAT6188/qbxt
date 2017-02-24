package com.ushine.solr.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ushine.abstracttest.AbstractAssert;
import com.ushine.abstracttest.AbstractSpringJUnitTest;
import com.ushine.solr.service.IClueStoreSolrService;
import com.ushine.solr.solrbean.ClueStoreSolr;
import com.ushine.solr.vo.ClueStoreVo;
import com.ushine.storesinfo.model.ClueStore;
//extends AbstractSpringJUnitTest<ClueStore>
public class ClueStoreSolrServiceImplTest extends AbstractAssert {

	@Autowired IClueStoreSolrService solrService;
	
	@Test
	@Ignore
	public void testServer(){
		ClueStoreSolrServiceImpl impl=new ClueStoreSolrServiceImpl();
		assertNotNull(impl.server);
	}
	
	@Test
	@Ignore
	public void testConvert(){
		String id="402884825a55dc51015a55e19ce9000d";
		ClueStore store=new ClueStore();
		store.setId(id);
		store.setCreateDate("2017-02-23 00:30:10");
		store.setClueName("枪支弹药");
		List<ClueStore> list=new ArrayList<>();
		list.add(store);
		List<ClueStoreSolr> solrList = solrService.convertToSolrBeanList(list);
		//取得
		assertEquals("枪支弹药", solrList.get(0).getClueName());
		assertEquals(id, solrList.get(0).getCluestoreId());
		//取得人员id
		System.out.println(solrList.get(0).getPersonId());
		//时间
		System.out.println(solrList.get(0).getCreateDate());
		
		//再转到vo层
		List<ClueStoreVo> voList = solrService.convertToClueStoreVoList(solrList);
		assertEquals("2017-02-23 00:30:10", voList.get(0).getCreateDate());
		//logger.info("详细信息：\r\n"+voList.get(0).toString());
	}
	
	@Test
	public void testContainsAny(){
		ClueStoreSolr solrFirst=new ClueStoreSolr();
		solrFirst.setCluestoreId("0001");
		solrFirst.setPersonId("4028318158b444400158b49746570001, 40283181597819b90159781a51e60001, "
				+ "40283181597819b90159781b57140002, 40283181597819b90159781df7780003, "
				+ "4028318159782b5d0159782bd9210001, 4028318159782d9d0159782edeb00001, "
				+ "402831815978307e01597830ccc20001, 402831815978323d015978329aef0001, "
				+ "40283181597833a20159783417380001, 402831815978538f01597853edba0001, "
				+ "4028318159790c3f0159790f149f0001, 402884825a54f35b015a54f481430001");
		ClueStoreSolrServiceImpl impl=new ClueStoreSolrServiceImpl();
		//获得人员id集合
		//测试getPersonIdListFromClueStoreSolr方法
		List<String> personIdListFromClue=impl.getPersonIdListFromClueStoreSolr(solrFirst);
		assertEquals(12, personIdListFromClue.size());
		List<String> personIdList=new ArrayList<>();
		personIdList.add("4028318158b444400158b49746570001");
		List<ClueStoreSolr> clueList=new ArrayList<>();
		clueList.add(solrFirst);
		//测试getContainsAnyList方法
		List<ClueStoreSolr> firstTempResultList=impl.getContainsAnyList(clueList, personIdList);
		assertEquals("0001", firstTempResultList.get(0).getCluestoreId());
		//第二组测试
		ClueStoreSolr solrSecond=new ClueStoreSolr();
		solrSecond.setCluestoreId("0002");
		//设置为null
		solrSecond.setPersonId(null);
		clueList.removeAll(clueList);
		assertEquals(0, clueList.size());
		clueList.add(solrSecond);
		assertEquals(1, clueList.size());
		if (clueList.get(0).getCluestoreId()!="0002") {
			fail("错误，实际id为："+clueList.get(0).getCluestoreId());
		}
		List<ClueStoreSolr> secondTempResultList=impl.getContainsAnyList(clueList, personIdList);
		assertTrue(secondTempResultList.size()==0);
	}
	
	@Test
	public void testPage(){
		List<String> list=new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			list.add(i+"");
		}
		//分页
		int start=10; int rows=3;
		List<String> resultList=new ArrayList<>();
		for (int i = start; i < (start + rows); i++) {
			if (list.size() <= i) {
				break;
			}
			resultList.add(list.get(i));
		}
		assertEquals(0, resultList.size());
		for (String string : resultList) {
			System.out.println(string);
		}
	}
}
