package com.ushine.log.utils;

import net.sf.json.JSONArray;

public class JsonToStringUtils {

	public static String jsonToString(Object object) {
		JSONArray array = JSONArray.fromObject(object);
		return array.toString();
	}
}
