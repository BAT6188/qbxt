package com.ushine.storeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class ArryayTest {
	/**
	 * 将json数组转成bean
	 * 
	 */
	@Test
	public void test1(){
		try {
			String map="[{name:'donghao',age:'26'},{name:'dongxue',age:'27'}]";
			JSONArray array=JSONArray.fromObject(map);
			List li = (List) JSONSerializer.toJava(array);
			for (Object object : li) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 获得实例
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				System.err.println(bean.get("name")+"---"+bean.get("age"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 数组中是否包含某个
	 * @throws Exception
	 */
	@Test
	public void test2()throws Exception{
		 String[] strings = { "Red", "Orange", "Blue", "Brown", "Red" };
		 System.err.println(ArrayUtils.contains(strings, "Blue"));
	}
	
	
}
