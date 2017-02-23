package com.ushine.solr.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.solrbean.VocationalWorkStoreSolr;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.solr.vo.VocationalWorkStoreVo;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.VocationalWorkStore;

import net.sf.json.JSONArray;

public class SolrBeanUtilsTest {
	
	@Test
	public void testGetStringValue(){
		VocationalWorkStore store = new VocationalWorkStore();
		store.setCreateDate(null);
		assertEquals("", SolrBeanUtils.getStringValue(store.getCreateDate()));
	}
	/**
	 * 测试bean与bean互转
	 * 
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	@Test
	@Ignore
	public void testConvertBeanToAnotherBean() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, NoSuchFieldException, SecurityException {
		VocationalWorkStore store = new VocationalWorkStore();
		String createDate = "2017-02-10 00:00:30";
		store.setCreateDate(createDate);
		InfoType infoType = new InfoType();
		infoType.setTypeName("十二局");
		store.setInfoType(infoType);
		store.setTheOriginal(null);
		store.setAttaches("ddd");
		store.setId("100");
		store.setTime(createDate);
		store.setDocName("docname");
		store.setDocNumber("setDocNumber");
		store.setAction("0");
		VocationalWorkStoreSolr storeSolr = new VocationalWorkStoreSolr();
		storeSolr = (VocationalWorkStoreSolr) SolrBeanUtils.convertBeanToAnotherBean(store, storeSolr, QueryBean.ID, QueryBean.VOCATIONALWORK_ID);
		// 从 store转到 solrbean
		assertEquals("100", storeSolr.getVocationalWorkStoreId());
		assertEquals("docname", storeSolr.getDocName());
		assertEquals("setDocNumber", storeSolr.getDocNumber());
		assertEquals("", storeSolr.getTheOriginal());
		assertEquals("十二局", storeSolr.getInfoType());
		assertEquals(1486656030000L, storeSolr.getCreateDate());
		// 从solrbean转到vo
		VocationalWorkStoreVo storeVo = new VocationalWorkStoreVo();
		storeVo = (VocationalWorkStoreVo) SolrBeanUtils.convertBeanToAnotherBean(storeSolr, storeVo, QueryBean.VOCATIONALWORK_ID, QueryBean.ID);
		assertEquals("100", storeVo.getId());
		assertEquals("十二局", storeVo.getInfoType());
		assertEquals("docname", storeVo.getDocName());
		assertEquals(createDate, storeVo.getCreateDate());
		assertEquals(createDate, storeVo.getTime());
	}

	/**
	 * 测试infoType、createDate能否互转
	 * 
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@Test
	@Ignore
	public void testSetInfoTypeAndCreateDate() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException, SecurityException {
		// (expected=RuntimeException.class)
		// SolrBeanUtils.convertBeanToAnotherBean("", RuntimeException.class,
		// null, null);
		VocationalWorkStore store = new VocationalWorkStore();
		store.setCreateDate("2017-02-10 00:00:30");
		InfoType infoType = new InfoType();
		infoType.setTypeName("十二局");
		store.setInfoType(infoType);
		// com.ushine.storesinfo.model.InfoType
		// System.err.println(VocationalWorkStoreSolr.class.getDeclaredField(QueryBean.CREAT_EDATE).getType().getSimpleName());
		// System.err.println(VocationalWorkStoreVo.class.getDeclaredField(QueryBean.CREAT_EDATE).getType().getSimpleName());
		// System.err.println(Long.class.getSimpleName().toLowerCase());
		// System.err.println(store.getClass().getDeclaredField("id").getType().getSimpleName());

		// store---solr
		VocationalWorkStoreSolr solr = new VocationalWorkStoreSolr();
		SolrBeanUtils.setInfoTypeProperty(store, solr);
		SolrBeanUtils.setCreateDateProperty(store, solr);
		assertEquals(1486656030000L, solr.getCreateDate());
		assertEquals("十二局", solr.getInfoType());

		// solr----vo
		solr.setCreateDate(1486656030000L);
		VocationalWorkStoreVo vo = new VocationalWorkStoreVo();
		SolrBeanUtils.setCreateDateProperty(solr, vo);
		assertEquals("2017-02-10 00:00:30", vo.getCreateDate());

		// SolrBeanUtils.setCreateDateProperty(null, null);
	}

	@Test
	@Ignore
	public void testHighlightVo() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PersonStoreVo vo = new PersonStoreVo();
		String value1 = "网络";
		vo.setInfoType(value1);
		String value2 = "网络董昊网络";
		vo.setPersonName(value2);
		PersonStoreVo highlightVo = (PersonStoreVo) SolrBeanUtils.highlightVo(vo, PersonStoreVo.class, value1);
		System.err.println(highlightVo.getInfoType());
		System.err.println(highlightVo.getPersonName());
		// assertEquals(QueryBean.HIGHLIGHT_PRE+value1+QueryBean.HIGHLIGHT_POST,
		// highlightVo.getInfoType());
	}

	@Test
	@Ignore
	public void test() {
		List<PersonStoreVo> list = new ArrayList<>();
		PersonStoreVo vo1 = new PersonStoreVo("1", "vo1", "vo1", "vo1", "vo1", "vo1", "vo1", "vo1", "vo1", "vo1", "vo1");
		PersonStoreVo vo2 = new PersonStoreVo("2", "vo2", "vo2", "vo2", "vo2", "vo2", "vo2", "vo2", "vo2", "vo2", "vo2");
		list.add(vo1);
		list.add(vo2);
		System.err.println(JSONArray.fromObject(list).toString());
	}
}
