package com.ushine.solr.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ctc.wstx.util.StringUtil;
import com.ushine.solr.solrbean.PersonStoreSolr;
import com.ushine.solr.solrbean.QueryBean;
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
	public static PersonStoreSolr convertPersonStoreToSolrBean(PersonStore ps)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PersonStoreSolr psSolr = new PersonStoreSolr();
		// 复制id属性
		BeanUtils.copyProperty(psSolr, "personId", getStringValue(ps.getId()));
		// 属性集合
		String[] properties = { "personName", "nameUsedBefore", "englishName", "sex", "bebornTime", "registerAddress",
				"presentAddress", "workUnit", "antecedents", "appendix", "activityCondition", "uid", "oid", "did" };
		// PropertyUtils反射获得属性值，复制其他属性
		for (String property : properties) {
			String propertyValue = getStringValue(PropertyUtils.getSimpleProperty(ps, property));
			BeanUtils.copyProperty(psSolr, property, propertyValue);
		}
		// 复制infoType属性
		InfoType infoType = (InfoType) PropertyUtils.getSimpleProperty(ps, INFOTYPE);
		if (null != infoType) {
			String value = getStringValue(infoType.getTypeName());
			BeanUtils.copyProperty(psSolr, INFOTYPE, value);
			// logger.info("拷贝" + INFOTYPE + "属性值：" + value);
		} else {
			BeanUtils.copyProperty(psSolr, INFOTYPE, "");
		}
		// 复制createDate属性，转成long型
		// logger.info("拷贝" + CREATEDATE + "属性值：" + ps.getCreateDate());
		BeanUtils.copyProperty(psSolr, CREATEDATE, SolrDateUtils.getTimeMillis(ps.getCreateDate()));
		// 复制身份账号集合
		Set<CertificatesStore> certificatesStores = ps.getCertificatesStores();
		StringBuffer csBuffer = new StringBuffer();
		if (null != certificatesStores && certificatesStores.size() > 0) {
			for (CertificatesStore store : certificatesStores) {
				csBuffer.append(getStringValue(store.getCertificatesNumber()) + ",");
			}
		}
		BeanUtils.copyProperty(psSolr, "certificatesStores", csBuffer.toString());
		// 复制网络账号集合
		Set<NetworkAccountStore> networkAccountStores = ps.getNetworkAccountStores();
		StringBuffer nasBuffer = new StringBuffer();
		if (null != networkAccountStores && networkAccountStores.size() > 0) {
			for (NetworkAccountStore store : networkAccountStores) {
				nasBuffer.append(getStringValue(store.getNetworkNumber()) + ",");
			}
		}
		BeanUtils.copyProperty(psSolr, "networkAccountStores", nasBuffer.toString());
		return psSolr;
	}

	/**
	 * 将PersonStoreSolr对象转换成vo层的PersonStoreVo对象
	 * 
	 * @param psSolr
	 *            PersonStoreSolr实例
	 * @return PersonStoreVo对象
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static PersonStoreVo convertPersonStoreSolrToVo(PersonStoreSolr psSolr)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		PersonStoreVo vo = new PersonStoreVo();
		// 复制id属性
		// logger.info("psSolr.getPersonId()："+getStringValue(psSolr.getPersonId()));
		BeanUtils.copyProperty(vo, "id", getStringValue(psSolr.getPersonId()));
		String[] properties = { "personName", "nameUsedBefore", "infoType", "englishName", "sex", "bebornTime",
				"presentAddress", "workUnit", "registerAddress" };
		for (String string : properties) {
			String value = getStringValue(PropertyUtils.getSimpleProperty(psSolr, string));
			BeanUtils.copyProperty(vo, string, value);
		}
		// createDate要将long转成日期格式
		String createDate = SolrDateUtils.getDate(psSolr.getCreateDate());
		BeanUtils.copyProperty(vo, CREATEDATE, createDate);
		return vo;
	}

	/**
	 * 将dao层的bean对象转成solr索引中的对象
	 * @param daoBean 原bean对象
	 * @param solrBeanClass 转后对象的Class
	 * @param idPropertyMap 指定将原id属性转成待转的id属性字段；<br>
	 * key是原来的bean的id，value是转换的bean对应的id属性字段<br>
	 * @return 转换后的对象实例，即solrBeanClass对象实例
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	protected static Object convertDaoBeanToSolrBean(Object daoBean,Class solrBeanClass,Map<String, String> idPropertyMap) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		Object newInstance=solrBeanClass.newInstance();
		//获得所有的声明的属性
		Field[] fields = daoBean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("id")) {
				//id
				
			}else if (field.getName().equals(QueryBean.CREAT_EDATE)) {
				//日期转换
				
			}else if(field.getName().equals(QueryBean.INFOTYPE)){
				//类型单独转换
				
			}
			//其他直接拷贝
			String value=getStringValue(PropertyUtils.getSimpleProperty(daoBean, field.getName()));
			BeanUtils.copyProperty(newInstance, field.getName(), value);
		}
		return newInstance;
	}

	/**
	 * 返回属性的值，如果是null返回空串
	 * 
	 * @param propertyValue
	 *            属性值，允许为null
	 * @return
	 */
	public static String getStringValue(Object propertyValue) {
		StringBuilder buffer = new StringBuilder();
		// 防止出现null
		if (null != propertyValue && StringUtils.isNotEmpty(propertyValue.toString())) {
			buffer.append(propertyValue);
		}
		return buffer.toString();
	}

	/**
	 * 手动做高量处理
	 * 
	 * @param object
	 *            vo层对象
	 * @param clazz
	 *            Class
	 * @param searchString
	 *            搜索关键字
	 * @return 高亮后的vo层对象
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object highlightVo(Object object, Class clazz, String searchString)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object newInstance = clazz.newInstance();
		// 获得所有声明的属性
		Field[] fields = clazz.getDeclaredFields();
		// 拼接
		String replacement = QueryBean.HIGHLIGHT_PRE + searchString + QueryBean.HIGHLIGHT_POST;
		for (Field field : fields) {
			// id不高亮处理
			if (field.getName().equals("id")) {
				String id = (String) PropertyUtils.getSimpleProperty(object, field.getName());
				PropertyUtils.setSimpleProperty(newInstance, field.getName(), id);
			}
			// 创建日期也不高亮
			if (field.getName().equals(QueryBean.CREAT_EDATE)) {
				String createDate = (String) PropertyUtils.getSimpleProperty(object, field.getName());
				PropertyUtils.setSimpleProperty(newInstance, field.getName(), createDate);
			}
			if (getStringTypeProperty(field)) {
				// 替换
				String value = getStringValue(PropertyUtils.getSimpleProperty(object, field.getName()));
				value = StringUtils.replace(value, searchString, replacement, -1);
				PropertyUtils.setSimpleProperty(newInstance, field.getName(), value);
			}
		}
		return newInstance;
	}

	/**
	 * 高亮集合
	 * 
	 * @param list
	 * @param clazz
	 * @param searchString
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static List highlightVoList(List list, Class clazz, String searchString)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List newList = new ArrayList<>();
		for (Object object : list) {
			newList.add(highlightVo(object, clazz, searchString));
		}
		return newList;
	}

	/**
	 * 不是id、createDate而且必须是String类型的属性
	 * @param field
	 * @return 满足以上条件为true
	 */
	protected static boolean getStringTypeProperty(Field field) {
		try {
			if (!field.getName().equals("id") && !field.getName().equals(QueryBean.CREAT_EDATE)
					&& field.getType().getName() == "java.lang.String") {
				return true;
			}
		} catch (Exception e) {
			logger.info("获得属性类型失败");
		}
		return false;
	}
}
