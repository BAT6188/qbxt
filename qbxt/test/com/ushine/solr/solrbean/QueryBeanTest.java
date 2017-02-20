package com.ushine.solr.solrbean;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;

import com.ushine.storesinfo.model.PersonStore;

public class QueryBeanTest {

	@Test
	public void testGetSolrQuery() {
		assertTrue(ClassUtils.getShortClassName(PersonStore.class).equals("PersonStore"));
		QueryBean bean1 = new QueryBean(null, null, null, null, null, null, null, null, null, "2011-01-01",
				"2011-01-01");
		System.err.println(bean1.initQuery(PersonStore.class));

		QueryBean bean2 = new QueryBean(null, null, null, "infoType", "董昊", null, null, null, null, "2011-01-01",
				"2012-01-01");
		System.err.println(bean2.initQuery(PersonStore.class));
		System.err.println(bean2.getSolrQuery(PersonStore.class));

		QueryBean bean3 = new QueryBean(null, null, null, "infoType", "董昊", "personName", null, null, "infoType",
				"2011-01-01", "2012-01-01");
		System.err.println(bean3.initQuery(PersonStore.class));
		System.err.println(bean3.getSolrQuery(PersonStore.class));
	}

	@Test
	public void testInitQuery() {
		QueryBean bean = new QueryBean(null, null, null, null, null, null, null, null, null, "2011-01-01",
				"2011-01-01");
		bean.initQuery(PersonStore.class);
		QueryBean bean2 = new QueryBean(null, null, null, null, null, null, null, null, null, "2001-01-01",
				"2011-01-01");
		bean2.initQuery(PersonStore.class);
		QueryBean bean3 = new QueryBean(null, null, null, null, null, null, null, null, null, "1970-01-01",
				"2011-01-01");
		bean3.initQuery(PersonStore.class);
	}

	/**
	 * 转义字符
	 */
	@Test
	public void testTransform() {
		System.err.println(transformSolrMetacharactor("*"));
		System.err.println(transformSolrMetacharactor("\\"));
		System.err.println(transformSolrMetacharactor("{"));
		// 效果一样
		System.err.println(ClientUtils.escapeQueryChars("{"));
	}

	/**
	 * + – && || ! ( ) { } [ ] ^ ” ~ * ? : \
	 * 
	 * @param input
	 * @return
	 */
	public static String transformSolrMetacharactor(String input) {
		StringBuffer sb = new StringBuffer();
		String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			matcher.appendReplacement(sb, "\\\\" + matcher.group());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
