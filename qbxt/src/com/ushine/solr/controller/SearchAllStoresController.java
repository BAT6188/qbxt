package com.ushine.solr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ushine.core.po.Resource;
import com.ushine.core.service.IPermitService;
import com.ushine.core.service.IResourceService;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.solr.futuretask.MyFutureTask;
import com.ushine.solr.solrbean.MyJsonObject;
import com.ushine.solr.util.MyJSonUtils;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.model.OutsideDocStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.VocationalWorkStore;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 依据关键字查询所有库中符合的数据
 * @author octocat
 *
 */
@Controller(value="searchAllStoresController")
public class SearchAllStoresController {
	@Autowired
	private IResourceService resourceService;
	@Autowired
	private IPermitService permitService;
	
	Logger logger = Logger.getLogger(SearchAllStoresController.class);

	private static final String OID = "oid";
	private static final String UID = "uid";
	private static final String DID = "did";

	@RequestMapping(value = "/searchForKeyWord.do", method = RequestMethod.POST)
	@ResponseBody
	public String searchForKeyWord(@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "size", required = false) Integer size, HttpServletRequest request) {
		JSONArray array = new JSONArray();
		try {
			if (StringUtils.isNotBlank(fieldValue)) {
				UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
				String value = sessionMgr.getUserInfoByISon(request);
				logger.info("用户信息：" + value);
				String roleId = MyJSonUtils.getValueFromJson(value, "uRole");
				roleId = roleId.substring(1, roleId.length() - 1);
				logger.info("角色roleId：" + roleId);
				// 资源id
				String personStoreResId = getResourceCode("查询人员");
				String clueStoreResId = getResourceCode("查询线索");
				String outsideDocStoreResId = getResourceCode("查询外来文档");
				String vocationalStoreResId = getResourceCode("查询业务文档");
				String leadSpeakStoreResId = getResourceCode("查询领导讲话");
				// 进一步获得操作权限
				// 人员库的
				String personCode = getPermitCode(personStoreResId, roleId);
				logger.info("人员库的操作权限：" + personCode);
				// 线索库
				String clueCode = getPermitCode(clueStoreResId, roleId);
				logger.info("线索库的操作权限：" + clueCode);
				// 外来文档库
				String outsideDocCode = getPermitCode(outsideDocStoreResId, roleId);
				logger.info("外来文档库的操作权限：" + outsideDocCode);
				// 业务文档库
				String voStoreCode = getPermitCode(vocationalStoreResId, roleId);
				logger.info("业务文档库的操作权限：" + voStoreCode);
				// 领导讲话
				String leadStoreCode = getPermitCode(leadSpeakStoreResId, roleId);
				logger.info("领导讲话库的操作权限：" + leadStoreCode);
				// 查询数据并封装
				String[] resourceCodes = new String[] { personCode, clueCode, outsideDocCode, voStoreCode,
						leadStoreCode };

				MyJsonObject[] myJsonObjects = new MyJsonObject[resourceCodes.length];

				// 人员
				Map<String, String> personMap = getUODId(value, personCode);
				MyFutureTask personStoreTask = new MyFutureTask(fieldValue, personMap.get(OID), personMap.get(DID),
						personMap.get(UID), "重点人员库", size, PersonStore.class);
				myJsonObjects[0] = personStoreTask.getSearchJsonObjectResult();
				// 线索
				Map<String, String> clueMap = getUODId(value, clueCode);
				MyFutureTask clueStoreTask = new MyFutureTask(fieldValue, clueMap.get(OID), clueMap.get(DID),
						clueMap.get(UID), "线索库", size, ClueStore.class);
				myJsonObjects[1] = clueStoreTask.getSearchJsonObjectResult();
				
				// 业务文档
				Map<String, String> vocationalMap = getUODId(value, voStoreCode);
				MyFutureTask vocationalTask = new MyFutureTask(fieldValue, vocationalMap.get(OID),
						vocationalMap.get(UID), vocationalMap.get(UID), "外来文档库", size, VocationalWorkStore.class);
				myJsonObjects[2] = vocationalTask.getSearchJsonObjectResult();
				
				// 外来文档
				Map<String, String> outMap = getUODId(value, outsideDocCode);
				MyFutureTask outsideTask = new MyFutureTask(fieldValue, outMap.get(OID), outMap.get(DID),
						outMap.get(UID), "业务文档库", size, OutsideDocStore.class);
				myJsonObjects[3] = outsideTask.getSearchJsonObjectResult();
				
				// 领导讲话
				Map<String, String> leadMap = getUODId(value, leadStoreCode);
				MyFutureTask leadTask = new MyFutureTask(fieldValue, leadMap.get(OID), leadMap.get(DID),
						leadMap.get(UID), "领导讲话库", size, LeadSpeakStore.class);
				myJsonObjects[4] = leadTask.getSearchJsonObjectResult();
				
				// 加入到JSONArray中
				for (MyJsonObject myJsonObject : myJsonObjects) {
					array.add(JSONObject.fromObject(myJsonObject));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询失败：" + e.getMessage());
		}

		return array.toString();
	}

	/**
	 * 获得uid did oid
	 * 
	 * @param value
	 *            用户权限信息，json格式的字符串
	 * @param recourceCode
	 *            权限代码
	 * @return Map对象
	 */
	private Map<String, String> getUODId(String value, String recourceCode) {
		Map<String, String> map = new HashMap<>();
		//默认全部
		map.put(OID, null);
		map.put(UID, null);
		map.put(DID, null);
		
		if (recourceCode.equals("1x0010")) {
			// 组织
			map.put(OID, MyJSonUtils.getValueFromJson(value, "uOId"));
		}
		if (recourceCode.equals("1x0011")) {
			// 部门
			map.put(UID, MyJSonUtils.getValueFromJson(value, "uDId"));
		}
		if (recourceCode.equals("1x0100")) {
			// 个人
			map.put(DID, MyJSonUtils.getValueFromJson(value, "uDId"));
		}
		if (recourceCode.equals("1x0000")) {
			// 禁止
			map.put(OID, "-1");
			map.put(UID, "-1");
			map.put(DID, "-1");
		}
		return map;
	}

	/**
	 * 获得查询人员、组织等对相应的id,用来进一步获得用户的查询权限
	 * 
	 * @param resouceName
	 *            资源名称,比如查询人员，查询组织
	 * @return 资源的id
	 */
	private String getResourceCode(String resouceName) {
		String result = null;
		try {
			List<Resource> list = resourceService.findResourcesByName(resouceName);
			for (Resource resource : list) {
				result = resource.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取资源的操作权限
	 * 
	 * @param resouceID
	 * @param roleId
	 * @return
	 */
	private String getPermitCode(String resouceID, String roleId) {
		// 默认没有操作权限
		String result = null;
		try {
			List<String> list = new ArrayList<>();
			list.add(roleId);
			List<String> rolePermitCode = permitService.getRolePermitCode(list, resouceID);
			result = rolePermitCode.get(0);
		} catch (Exception e) {
			logger.info("获得操作权限异常：" + e.getMessage());
			e.printStackTrace();
		}
		// 出现异常返回默认值没有操作权限
		return ObjectUtils.defaultIfNull(result, "1x0000").toString();
	}
}
