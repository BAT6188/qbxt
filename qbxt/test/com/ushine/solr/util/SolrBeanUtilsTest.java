package com.ushine.solr.util;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.PersonStore;

public class SolrBeanUtilsTest {
	/**
	 * 测试将PersonStore转成对应的SolrBean
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException 
	 * @throws ParseException 
	 */
	@Test
	public void testConvertPersonStoreToSolrBean() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException{
		PersonStore ps=new PersonStore();
		ps.setId("10000");
		ps.setPersonName("董昊");
		ps.setNameUsedBefore(null);
		InfoType type=new InfoType();
		type.setTypeName("网络工程师");
		ps.setInfoType(type);
		//创建日期
		String date="2017-02-15 12:30:32";
		ps.setCreateDate(date);
		//账号
		Set<CertificatesStore> stores=new HashSet<>();
		CertificatesStore cs1=new CertificatesStore();
		cs1.setCertificatesNumber("10001");
		CertificatesStore cs2=new CertificatesStore();
		cs2.setCertificatesNumber("10002");
		stores.add(cs1);
		stores.add(cs2);
		ps.setCertificatesStores(stores);
		
		PersonStoreSolr psSolr = SolrBeanUtils.convertPersonStoreToSolrBean(ps);
		assertEquals("10000", psSolr.getPersonId());
		assertEquals("董昊", psSolr.getPersonName());
		assertEquals("", psSolr.getNameUsedBefore());
		assertEquals("网络工程师", psSolr.getInfoType());
		long value=SolrDateUtils.getTimeMillis(date);
		assertEquals(value, psSolr.getCreateDate());
		System.err.println(psSolr.getCertificatesStores());
		//assertEquals("10001,10002,", psSolr.getCertificatesStores());
		String [] cstores=psSolr.getCertificatesStores().split(",");
		assertEquals(true, ArrayUtils.contains(cstores, "10001"));
		assertEquals(true, ArrayUtils.contains(cstores, "10002"));
	}
	
	/**
	 * 测试将solr中的PersonStore转成vo对象
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Test
	public void testConvertPersonStoreSolrToVo() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		PersonStoreVo vo=null;
		PersonStoreSolr psSolr=new PersonStoreSolr();
		psSolr.setCreateDate(10000);
		psSolr.setBebornTime("1901-01-01");
		psSolr.setNameUsedBefore(null);
		vo=SolrBeanUtils.convertPersonStoreSolrToVo(psSolr);
		assertEquals("1901-01-01", vo.getBebornTime());
		assertEquals("", vo.getNameUsedBefore());
	}
}
