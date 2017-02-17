package com.ushine.solr.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import oracle.net.aso.s;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.NetworkAccountStore;
import com.ushine.storesinfo.model.PersonStore;

/**
 * dao层的bean与solr中的bean、vo层中的bean之间的相互转换
 * 
 * @author dh
 * 
 */
public class SolrBeanUtils {
	private static Logger logger = Logger.getLogger(SolrBeanUtils.class);
	private static final String INFOTYPE = "infoType";
	private static final String CREATEDATE = "createDate";

	/**
	 * 将dao层的PersonStore的bean转换为Solr中Bean
	 * 
	 * @param ps
	 *            PersonStore
	 * @return PersonStoreSolr
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static PersonStoreSolr convertPersonStoreToSolrBean(PersonStore ps) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PersonStoreSolr psSolr = new PersonStoreSolr();
		// 复制id属性
		BeanUtils.copyProperty(psSolr, "personId", ps.getId());
		// 属性集合
		String[] properties = { "personName", "nameUsedBefore", "englishName", "sex", "bebornTime", "registerAddress", "presentAddress", "workUnit", "antecedents", "appendix", "activityCondition",
				"uid", "oid", "did", };
		// PropertyUtils反射获得属性值，复制其他属性
		for (String property : properties) {
			String propertyValue = getPropertyValue(PropertyUtils.getSimpleProperty(ps, property));
			BeanUtils.copyProperty(psSolr, property, propertyValue);
		}
		// 复制infoType属性
		InfoType infoType = (InfoType) PropertyUtils.getSimpleProperty(ps, INFOTYPE);
		if (null != infoType) {
			String value = getPropertyValue(infoType.getTypeName());
			BeanUtils.copyProperty(psSolr, INFOTYPE, value);
			logger.info("拷贝" + INFOTYPE + "属性值：" + value);
		} else {
			BeanUtils.copyProperty(psSolr, INFOTYPE, "");
		}
		// 复制createDate属性，转成long型
		logger.info("拷贝" + CREATEDATE + "属性值：" + ps.getCreateDate());
		BeanUtils.copyProperty(psSolr, CREATEDATE, SolrDateUtils.getTimeMillis(ps.getCreateDate()));
		// 复制身份账号集合
		Set<CertificatesStore> certificatesStores = ps.getCertificatesStores();
		StringBuffer csBuffer = new StringBuffer();
		if (null != certificatesStores && certificatesStores.size() > 0) {
			for (CertificatesStore store : certificatesStores) {
				csBuffer.append(getPropertyValue(store.getCertificatesNumber()) + ",");
			}
		}
		BeanUtils.copyProperty(psSolr, "certificatesStores", csBuffer.toString());
		// 复制网络账号集合
		Set<NetworkAccountStore> networkAccountStores = ps.getNetworkAccountStores();
		StringBuffer nasBuffer = new StringBuffer();
		if (null != networkAccountStores && networkAccountStores.size() > 0) {
			for (NetworkAccountStore store : networkAccountStores) {
				nasBuffer.append(getPropertyValue(store.getNetworkNumber()) + ",");
			}
		}
		BeanUtils.copyProperty(psSolr, "networkAccountStores", nasBuffer.toString());
		return psSolr;
	}

	/**
	 * private String createDate;
	 * 
	 * @param psSolr
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static PersonStoreVo convertPersonStoreSolrToVo(PersonStoreSolr psSolr) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PersonStoreVo vo = new PersonStoreVo();
		// 复制id属性
		BeanUtils.copyProperty(vo, "id", psSolr.getPersonId());
		String[] properties = {"personName", "nameUsedBefore", "englishName", "sex", "bebornTime", "presentAddress", "workUnit", "registerAddress"};
		for (String string : properties) {
			String value = getPropertyValue(PropertyUtils.getSimpleProperty(psSolr, string));
			BeanUtils.copyProperty(vo, string, value);
		}
		// createDate要将long转成日期格式

		return vo;
	}

	/**
	 * 返回属性的值，如果是null返回空串
	 * 
	 * @param propertyValue
	 *            属性值，允许为null
	 * @return
	 */
	public static String getPropertyValue(Object propertyValue) {
		StringBuilder buffer = new StringBuilder();
		// 防止出现null
		if (null != propertyValue && StringUtils.isNotEmpty(propertyValue.toString())) {
			buffer.append(propertyValue);
		}
		return buffer.toString();
	}
}
