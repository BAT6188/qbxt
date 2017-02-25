package com.ushine.solr.util;

import org.apache.commons.lang.ObjectUtils;

import com.ushine.common.vo.PagingObject;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MyJSonUtils {
	/**
	 * PagingObject对象转json格式
	 * @param vo PagingObject实例
	 * @return 格式形如：{paging:{"currentPage":1,"next":false,"previous":false,
	 	"sizePage":50,"startRecord":0,"totalPage":1,"totalRecord":4},datas:[{},{}]}
	 */
	public static String toJson(@SuppressWarnings("rawtypes") PagingObject vo){
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		//集合转json
		String datas=JSONArray.fromObject(vo.getArray()).toString();
		root.element("datas", datas);
		return root.toString(); 
	}
	/**
	 * 从json格式字符串中获得值
	 * @param jsonString json格式的字符串
	 * @param key json中的key
	 * @return String
	 */
	public static String getValueFromJson(String jsonString,String key){
		JSONObject jsonObject=JSONObject.fromObject(jsonString);
		//获得json里的各个key的值用MorphDynaBean
		MorphDynaBean bean=(MorphDynaBean) JSONObject.toBean(jsonObject);
		//获得值
		Object object=ObjectUtils.defaultIfNull(bean.get(key), "");
		return object.toString();
	}
}
