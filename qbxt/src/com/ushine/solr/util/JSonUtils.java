package com.ushine.solr.util;

import com.ushine.common.vo.PagingObject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSonUtils {
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
}
