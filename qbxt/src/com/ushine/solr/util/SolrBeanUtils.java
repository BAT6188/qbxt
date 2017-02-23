package com.ushine.solr.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
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
	/**
	 * 将dao层的bean对象转成solr索引中的对象
	 * 
	 * @param daoBean
	 *            原bean对象
	 * @param targetBean
	 *            转后对象的
	 * @param sourceIdProperty
	 *            原bean的id属性字段名称
	 * @param tartgetIdProperty
	 *            目标bean的id属性字段名称
	 * @return 转换后的对象实例
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static Object convertBeanToAnotherBean(Object sourceBean, Object targetBean, String sourceIdProperty, String tartgetIdProperty) throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, NoSuchFieldException, SecurityException {
		if (null == sourceBean || null == targetBean || null == sourceIdProperty || null == sourceIdProperty) {
			throw new RuntimeException("参数输入了空对象");
		}
		// Object newInstance=targetBeanClass.newInstance();
		// 获得所有的声明的属性
		Field[] fields = sourceBean.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals(sourceIdProperty)) {
				// id属性互转
				String id = getStringValue(PropertyUtils.getSimpleProperty(sourceBean, sourceIdProperty));
				BeanUtils.copyProperty(targetBean, tartgetIdProperty, id);
			} else if (field.getName().equals(QueryBean.CREAT_EDATE)) {
				// 日期转换long型与string互转
				// logger.info("进行createDate的转换");
				setCreateDateProperty(sourceBean, targetBean);
			} else if (field.getName().equals(QueryBean.INFOTYPE)) {
				// 类型单独转换
				// logger.info("进行infoType的转换");
				setInfoTypeProperty(sourceBean, targetBean);
			} else if (!field.getName().equals("serialVersionUID")) {
				// 其他直接拷贝，考虑到序列号
				String value = getStringValue(PropertyUtils.getSimpleProperty(sourceBean, field.getName()));
				BeanUtils.copyProperty(targetBean, field.getName(), value);
			}
		}
		return targetBean;
	}

	/**
	 * 将原对象的infoType属性付给目标对象
	 * 
	 * @param sourceInstance
	 *            源对象
	 * @param targetInstance
	 *            目标对象
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	protected static void setInfoTypeProperty(Object sourceInstance, Object targetInstance) throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		if (null == sourceInstance || null == targetInstance) {
			throw new RuntimeException("输入参数对象为空");
		}
		String property = sourceInstance.getClass().getDeclaredField(QueryBean.INFOTYPE).getType().getSimpleName();
		String propertyValue = null;
		if (property.equals("InfoType")) {
			// 如果是InfoType类型，获得getTypeName属性值，付给目标对象
			InfoType infoType = (InfoType) PropertyUtils.getSimpleProperty(sourceInstance, QueryBean.INFOTYPE);
			//使用ObjectUtils
			InfoType defaultValue=new InfoType();
			defaultValue.setId(System.currentTimeMillis()+"");
			defaultValue.setTypeName("");
			//设置一个默认值
			infoType=(InfoType) ObjectUtils.defaultIfNull(infoType, defaultValue);
			propertyValue = getStringValue(infoType.getTypeName());
		} else if (property.equals("String")) {
			// 如果是string类型，直接付给目标对象
			propertyValue = getStringValue(PropertyUtils.getSimpleProperty(sourceInstance, QueryBean.INFOTYPE));
		}
		//logger.info("转换后的infoType属性值："+propertyValue);
		BeanUtils.copyProperty(targetInstance, QueryBean.INFOTYPE, propertyValue);
	}

	/**
	 * 将源目标的createDate转成目标对象的该属性
	 * 
	 * @param sourceInstance
	 *            源对象
	 * @param targetInstance
	 *            目标对象
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	protected static void setCreateDateProperty(Object sourceInstance, Object targetInstance) throws NoSuchFieldException, SecurityException, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		if (null == sourceInstance || null == targetInstance) {
			throw new RuntimeException("输入参数对象为空");
		}
		String property = sourceInstance.getClass().getDeclaredField(QueryBean.CREAT_EDATE).getType().getSimpleName();
		if (StringUtils.equals(property, "long")) {
			// 转成yyyy-MM-dd HH:MM:ss
			String value = getStringValue(PropertyUtils.getSimpleProperty(sourceInstance, QueryBean.CREAT_EDATE));
			logger.debug(value + "转成yyyy-MM-dd HH:MM:ss");
			long timeMillis = Long.parseLong(value);
			String propertyValue = SolrDateUtils.getDate(timeMillis);
			BeanUtils.copyProperty(targetInstance, QueryBean.CREAT_EDATE, propertyValue);
		} else if (StringUtils.equals(property, "String")) {
			String date = getStringValue(PropertyUtils.getSimpleProperty(sourceInstance, QueryBean.CREAT_EDATE));
			logger.debug(date + "转成long型");
			// 转成long
			BeanUtils.copyProperty(targetInstance, QueryBean.CREAT_EDATE, SolrDateUtils.getTimeMillis(date));
		}
	}

	/**
	 * 返回属性的值
	 * 
	 * @param propertyValue
	 *            属性值，允许为null
	 * @return 如果是null返回空串
	 */
	public static String getStringValue(Object propertyValue) {
		Object defaultValue=ObjectUtils.defaultIfNull(propertyValue, "");
		return defaultValue.toString();
	}

	/**
	 * 手动做高量处理
	 * 
	 * @param object
	 *            vo层对象
	 * @param clazz
	 *            vo层对象的Class
	 * @param searchString
	 *            搜索关键字
	 * @return 高亮后的vo层对象
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static Object highlightVo(Object object, Class clazz, String searchString) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object newInstance = clazz.newInstance();
		// 获得所有声明的属性
		Field[] fields = clazz.getDeclaredFields();
		// 拼接
		String replacement = QueryBean.HIGHLIGHT_PRE + searchString + QueryBean.HIGHLIGHT_POST;
		for (Field field : fields) {
			// id、创建日期不高亮处理
			if (field.getName().equals(QueryBean.ID) || field.getName().equals(QueryBean.CREAT_EDATE)) {
				String value = (String) PropertyUtils.getSimpleProperty(object, field.getName());
				PropertyUtils.setSimpleProperty(newInstance, field.getName(), value);
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
	 *            待高亮集合
	 * @param clazz
	 *            集合中对象的类
	 * @param searchString
	 *            关键字
	 * @return 高亮后的集合
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List highlightVoList(List list, Class clazz, String searchString) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List newList = new ArrayList<>();
		for (Object object : list) {
			newList.add(highlightVo(object, clazz, searchString));
		}
		return newList;
	}

	/**
	 * 不是id、createDate而且必须是String类型的属性
	 * 
	 * @param field
	 *            属性字段
	 * @return 满足以上条件为true
	 */
	protected static boolean getStringTypeProperty(Field field) {
		try {
			if (!field.getName().equals(QueryBean.ID) && !field.getName().equals(QueryBean.CREAT_EDATE) && field.getType().getName() == "java.lang.String") {
				return true;
			}
		} catch (Exception e) {
			logger.info("获得属性类型失败");
		}
		return false;
	}
}
