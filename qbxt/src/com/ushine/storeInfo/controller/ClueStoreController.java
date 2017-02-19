package com.ushine.storeInfo.controller;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tdcq.common.logging.LogFactory;
import com.tdcq.common.logging.LogInfo;
import com.ushine.common.vo.PagingObject;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.store.index.ClueStoreNRTSearch;
import com.ushine.storeInfo.model.CertificatesStore;
import com.ushine.storeInfo.model.ClueRelationship;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.NetworkAccountStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.TempClueData;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.IClueRelationshipService;
import com.ushine.storeInfo.service.IClueStoreService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.storeInfo.service.ITempClueDataService;
import com.ushine.storeInfo.service.IWebsiteJournalStoreService;
import com.ushine.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 线索控制器
 * 
 * @author wangbailin
 * 
 */
@Controller
public class ClueStoreController {
	private static final Logger logger = LoggerFactory
			.getLogger(ClueStoreController.class);
	private ServletContext servletContext;
	@Autowired
	private IClueStoreService clueStoreService;
	@Autowired
	private ITempClueDataService tempClueDataService;
	@Autowired
	private IClueRelationshipService clueRelationshipService;
	@Autowired
	private IPersonStoreService personStoreService;
	@Autowired
	private IWebsiteJournalStoreService websiteJournalStoreService;
	/**
	 * 查询线索名称是否存在
	 * @param clueName
	 * @return
	 */
	@RequestMapping(value = "/hasStoreByClueName.do", method = RequestMethod.POST)
	@ResponseBody
	public String hasStoreByClueName(@RequestParam("clueName") String clueName){
		//默认不存在
		logger.info("查询线索："+clueName+"是否存在");
		String msg="not_exist";
		ViewObject viewObject=null;
		try {
			if(clueStoreService.hasStoreByClueName(clueName)){
				//已经存在名称
				msg="exist";
			}
			viewObject=new ViewObject(ViewObject.RET_SUCCEED, msg);
		} catch (Exception e) {
			viewObject=new ViewObject(ViewObject.RET_ERROR, msg);
			e.printStackTrace();
			logger.error("查询失败");
		}
		return viewObject.toJSon();
	}
	/**
	 * 新增线索
	 * 
	 * @param request
	 * @param clueName
	 * @param clueSource
	 * @param findTime
	 * @param clueContent
	 * @param arrangeAndEvolveCondition
	 * @param number
	 * @param tempClueDataType
	 * @param tempClueDataName
	 * @return
	 */
	@RequestMapping(value = "/saveClueInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public String saveClueInfo(
			HttpServletRequest request,
			@RequestParam("clueName") String clueName,
			@RequestParam("clueSource") String clueSource,
			@RequestParam("findTime") String findTime,
			@RequestParam("clueContent") String clueContent,
			@RequestParam("arrangeAndEvolveCondition") String arrangeAndEvolveCondition,
			@RequestParam("number") String number,
			@RequestParam("tempClueDataType") String tempClueDataType,
			@RequestParam("tempClueDataName") String tempClueDataName) {
		logger.info("新增线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("新增线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增类别信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					// 获得临时的涉及对象数据
					List<TempClueData> clueDatas = tempClueDataService
							.findTempClueData(number);

					ClueStore clue = new ClueStore();
					clue.setArrangeAndEvolveCondition(arrangeAndEvolveCondition);
					clue.setClueContent(clueContent);
					clue.setClueName(clueName);
					clue.setClueSource(clueSource);
					clue.setFindTime(findTime);

					clue.setOid(sessionMgr.getUOID(request));
					clue.setUid(sessionMgr.getUID(request));
					clue.setDid(sessionMgr.getUDID(request));
					clue.setCreateDate(StringUtil.dates());
					clue.setIsEnable("2");
					clue.setAction("1");
					// 新增线索
					clueStoreService.saveClue(clue);
					// 新增线索关系
					clueRelationshipService.saveClueRelationship(clue.getId(),
							clueDatas);
					// 新增成功后删除临时数据
					tempClueDataService.delTempCluDataByAction(number);
					//索引添加条记录
					ClueStoreNRTSearch nrtSearch=ClueStoreNRTSearch.getInstance();
					nrtSearch.addIndex(clue);
					return new ViewObject(ViewObject.RET_SUCCEED, "新增线索成功!").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "新增失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "新增线索失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "新增失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 修改线索
	 * 
	 * @param request
	 * @param clueName
	 * @param clueSource
	 * @param findTime
	 * @param clueContent
	 * @param arrangeAndEvolveCondition
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/updateClueStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String updateClueStore(
			HttpServletRequest request,
			@RequestParam("clueName") String clueName,
			@RequestParam("clueSource") String clueSource,
			@RequestParam("findTime") String findTime,
			@RequestParam("clueContent") String clueContent,
			@RequestParam("arrangeAndEvolveCondition") String arrangeAndEvolveCondition,
			@RequestParam("id") String id) {
		logger.info("修改线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("修改线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增类别信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					ClueStore store = clueStoreService.findClueById(id);

					ClueStore clue = new ClueStore();
					clue.setId(id);
					clue.setArrangeAndEvolveCondition(arrangeAndEvolveCondition);
					clue.setClueContent(clueContent);
					clue.setClueName(clueName);
					clue.setClueSource(clueSource);
					clue.setFindTime(findTime);
					clue.setInvolvingObjName(store.getInvolvingObjName());
					clue.setCreateDate(store.getCreateDate());
					clue.setOid(sessionMgr.getUOID(request));
					clue.setUid(sessionMgr.getUID(request));
					clue.setDid(sessionMgr.getUDID(request));
					clue.setAction("1");
					// 修改
					clueStoreService.updateClue(clue);
					return new ViewObject(ViewObject.RET_SUCCEED, "修改线索成功!")
							.toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "修改线索失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "修改线索失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 查询线索信息多条件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findClueStore.do", method = RequestMethod.GET)
	@ResponseBody
	public String findClueStore(HttpServletRequest request,
			@RequestParam("fieldValue") String fieldValue,
			@RequestParam("field") String field,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam("page") int nextPage, 
			@RequestParam("limit") int size,
			//排序字段
			@RequestParam(value ="sort", required = false) String sortField,
			//升序或降序
			@RequestParam(value ="dir", required = false) String dir) {
		logger.info("查询线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 汉字关键字搜索
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			// 判断当前用户是否有权限查询人员库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {// 读取全部
					return clueStoreService.findClueStore(field, fieldValue, startTime,
									endTime, nextPage, size, null, null, null,sortField,dir);
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					return clueStoreService.findClueStore(field, fieldValue, startTime,
									endTime, nextPage, size, null,
									sessionMgr.getUOID(request), null,sortField,dir);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					return clueStoreService.findClueStore(field, fieldValue, startTime,
									endTime, nextPage, size, null, null,
									sessionMgr.getUDID(request),sortField,dir);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					return clueStoreService.findClueStore(field, fieldValue, startTime,
									endTime, nextPage, size,
									sessionMgr.getUID(request), null, null,sortField,dir);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取")
							.toJSon();
				}

			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询线索失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findClueStoreVoToJson(PagingObject<ClueStore> vo) throws Exception {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (ClueStore clueStore : vo.getArray()) {
			//根据当前遍历的线索分别提取出当前遍历线索是否有人员，组织以及媒体刊物
			List<ClueRelationship> list = clueRelationshipService.findRelationshipByClueId(clueStore.getId());
			//声明是否存在涉及对象,默认为false
			String persionStore = "false";
			String organizStore = "false";
			String websiteJournalStore = "false";
			for (ClueRelationship r : list) {
				if("websiteJournalStore".equals(r.getDataType())){
					websiteJournalStore = "true";
					break;
				}
			}
			for (ClueRelationship r : list) {
				if("organizStore".equals(r.getDataType())){
					organizStore = "true";
					break;
				}
			}
			for (ClueRelationship r : list) {
				if("personStore".equals(r.getDataType())){
					persionStore = "true";
					break;
				}
			}
			String icon = "persionStore:"+persionStore+",organizStore:"+organizStore+",websiteJournalStore:"+websiteJournalStore+"";
			JSONObject obj = new JSONObject();
			obj.put("id", clueStore.getId());
			obj.put("clueName", clueStore.getClueName());
			obj.put("clueSource", clueStore.getClueSource());
			obj.put("findTime", clueStore.getFindTime());
			obj.put("isEnable", clueStore.getIsEnable());
			obj.put("clueContent", clueStore.getClueContent());
			obj.put("createDate", clueStore.getCreateDate());
			obj.put("arrangeAndEvolveCondition",
					clueStore.getArrangeAndEvolveCondition());
			obj.put("icon", icon);
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 删除线索信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delClueStoreByIds.do", method = RequestMethod.POST)
	@ResponseBody
	public String delClueStoreByIds(HttpServletRequest request,
			@RequestParam("ids") String[] ids) {
		logger.info("删除线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("删除线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限删除线索库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					// 删除线索
					clueStoreService.delClueStoreByIds(ids);
					return new ViewObject(ViewObject.RET_SUCCEED, "删除线索成功!")
							.toJSon();
				}

			}
			return new ViewObject(ViewObject.RET_FAILURE, "删除失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "删除线索失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "删除失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 根据线索id和人员条件查询人员信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findCluePersonStore.do", method = RequestMethod.GET)
	@ResponseBody
	public String findCluePersonStore(HttpServletRequest request,
			@RequestParam("fieldValue") String fieldValue,
			@RequestParam("field") String field,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size,
			@RequestParam("clueId") String clueId) {
		try {
			// 汉字关键字搜索
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			PagingObject<PersonStore> pagingObject = clueStoreService.findCluePersonStore(clueId, field, fieldValue, startTime,
							endTime, nextPage, size);
			ClueStore clueStore = clueStoreService.findClueById(clueId);
			return findCluePersonStoreVoToJson(pagingObject,
					clueStore.getClueName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findCluePersonStoreVoToJson(PagingObject<PersonStore> vo,
			String clueName) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (PersonStore PersonStore : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", PersonStore.getId());
			obj.put("personName", PersonStore.getPersonName());
			obj.put("nameUsedBefore", PersonStore.getNameUsedBefore());
			obj.put("englishName", PersonStore.getEnglishName());
			obj.put("bebornTime", PersonStore.getBebornTime());
			obj.put("presentAddress", PersonStore.getPresentAddress());
			obj.put("workUnit", PersonStore.getWorkUnit());
			obj.put("registerAddress", PersonStore.getRegisterAddress());
			obj.put("antecedents", PersonStore.getAntecedents());
			obj.put("activityCondition", PersonStore.getActivityCondition());
			obj.put("sex", PersonStore.getSex());
			obj.put("clueName", clueName);
			obj.put("createDate", PersonStore.getCreateDate());
			obj.put("isToStorage", PersonStore.getIsToStorage());
			if(PersonStore.getInfoType()!=null){
				//关联类别不为空
				obj.put("infoType", PersonStore.getInfoType().getTypeName());
			}
			//证件
			Set<CertificatesStore> set1=PersonStore.getCertificatesStores();
			JSONArray cjArray=new JSONArray();
			for (CertificatesStore c : set1) {
				JSONObject cObject=new JSONObject();
				//为空
				if(c.getInfoType()!=null&& c.getInfoType().getTypeName().trim().length()>0){
					cObject.put("certificatesType", c.getInfoType().getTypeName());
					cObject.put("certificatesTypeNumber", c.getCertificatesNumber());
					//需要放到array中
					cjArray.add(cObject);
					obj.put("certificates",cjArray.toString());
				}
			}
			//网络账号
			Set<NetworkAccountStore> set=PersonStore.getNetworkAccountStores();
			JSONArray nArray=new JSONArray();
			for (NetworkAccountStore networkAccountStore : set) {
				JSONObject networkObject=new JSONObject();
				if(networkAccountStore.getInfoType()!=null&& networkAccountStore.getInfoType().getTypeName().trim().length()>0){
					networkObject.put("networkAccountType", networkAccountStore.getInfoType().getTypeName());
					networkObject.put("networkAccountTypeNumber", networkAccountStore.getNetworkNumber());
					//networkObject.put("networkAccountType", networkAccountStore.getInfoType().getTypeName());
					//networkObject.put("networkAccountTypeNumber", networkAccountStore.getNetworkNumber());
					nArray.add(networkObject);
					obj.put("networkaccount",nArray.toString());
				}
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}

	/**
	 * 根据线索id和媒体刊物条件查询媒体刊物信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findClueWebsiteJournal.do", method = RequestMethod.GET)
	@ResponseBody
	public String findClueWebsiteJournal(HttpServletRequest request,
			@RequestParam("fieldValue") String fieldValue,
			@RequestParam("field") String field,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size,
			@RequestParam("clueId") String clueId) {
		try {
			// 汉字关键字搜索
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			PagingObject<WebsiteJournalStore> pagingObject = clueStoreService
					.findClueWebsiteJournalStore(clueId, field, fieldValue,
							startTime, endTime, nextPage, size);
			ClueStore clueStore = clueStoreService.findClueById(clueId);
			return findClueWebsiteJournalVoToJson(pagingObject,
					clueStore.getClueName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把媒体网站刊物集合转成json
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findClueWebsiteJournalVoToJson(
			PagingObject<WebsiteJournalStore> vo, String clueName) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (WebsiteJournalStore store : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", store.getId());
			obj.put("name", store.getName());
			obj.put("websiteURL", store.getWebsiteURL());
			obj.put("serverAddress", store.getServerAddress());
			obj.put("establishAddress", store.getEstablishAddress());
			obj.put("mainWholesaleAddress", store.getMainWholesaleAddress());
			obj.put("establishPerson", store.getEstablishPerson());
			obj.put("establishTime", store.getEstablishTime());
			obj.put("basicCondition", store.getBasicCondition());
			obj.put("createDate", store.getCreateDate());
			obj.put("clueName", clueName);
			obj.put("isToStorage", store.getIsToStorage());
			if(store.getInfoType()!=null){
				//关联类别不为空
				obj.put("infoType", store.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}

	/**
	 * 根据线索id和组织条件查询组织信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findClueOrganiz.do", method = RequestMethod.GET)
	@ResponseBody
	public String findClueOrganiz(HttpServletRequest request,
			@RequestParam("fieldValue") String fieldValue,
			@RequestParam("field") String field,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size,
			@RequestParam("clueId") String clueId) {
		/*try {
			// 汉字关键字搜索
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			PagingObject<OrganizStore> pagingObject = clueStoreService
					.findClueOrganizStore(clueId, field, fieldValue, startTime,
							endTime, nextPage, size);
			ClueStore clueStore = clueStoreService.findClueById(clueId);
			return findClueOrganizVoToJson(pagingObject,
					clueStore.getClueName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return null;
	}

	

	/**
	 * 选择线索数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/selectClueData.do", method = RequestMethod.GET)
	@ResponseBody
	public String selectClueData(HttpServletRequest request,
			@RequestParam("page") int nextPage, @RequestParam("limit") int size) {
		logger.info("查询线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);

			// 判断当前用户是否有权限查询人员库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {// 读取全部
					PagingObject<ClueStore> pagingObject = clueStoreService
							.findClueStore(nextPage, size, null, null, null);
					return findClueStoreVoToJson(pagingObject);
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					PagingObject<ClueStore> pagingObject = clueStoreService
							.findClueStore(nextPage, size, null,
									sessionMgr.getUOID(request), null);
					return findClueStoreVoToJson(pagingObject);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					PagingObject<ClueStore> pagingObject = clueStoreService
							.findClueStore(nextPage, size, null, null,
									sessionMgr.getUDID(request));
					return findClueStoreVoToJson(pagingObject);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					PagingObject<ClueStore> pagingObject = clueStoreService
							.findClueStore(nextPage, size,
									sessionMgr.getUID(request), null, null);
					return findClueStoreVoToJson(pagingObject);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取")
							.toJSon();
				}

			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询线索失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 下载线索信息的PDF文件
	 * 
	 * @param request
	 * @param name
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadCluePDFFile.do")
	@ResponseBody
	public String dwonExportData(HttpServletRequest request,
			@RequestParam("clueId") String clueId, HttpServletResponse response) {

		ServletOutputStream out = null;
		String root = request.getServletContext().getRealPath("/");
		//文件名
		String path = root + clueId + ".doc";
		try {
			// 获取服务器地址
			//String path = servletContext.getRealPath("/") + "PDFFilePath/";
			//生成word
			clueStoreService.outputClueStoreToWord(clueId, path);
			// 下载附件
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头
			response.setHeader("Content-Disposition", "attachment;fileName=" + clueId + ".doc");
			// 要下载的文件地址
			out = response.getOutputStream();
			//使用IOUtils
			File file=new File(path);
			IOUtils.write(org.apache.commons.io.FileUtils.readFileToByteArray(file), out);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(out);
		}
		return new ViewObject(ViewObject.RET_FAILURE, "下载失败,请联系管理员").toJSon();
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 启用线索
	 * 
	 * @param request
	 * @param orgIds
	 * @return
	 */
	@RequestMapping(value = "/startClue.do", method = RequestMethod.POST)
	@ResponseBody
	public String startOrganiz(HttpServletRequest request,
			@RequestParam("clueIds") String[] clueIds) {
		logger.info("启用线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("启用线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			clueStoreService.updateClueStoreIsEnableStart(clueIds);
			return new ViewObject(ViewObject.RET_FAILURE, "启用线索信息成功！").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "启用线索信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE,
					"启用线索信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);

		}
	}

	/**
	 * 禁用线索
	 * 
	 * @param request
	 * @param orgIds
	 * @return
	 */
	@RequestMapping(value = "/ceaseClue.do", method = RequestMethod.POST)
	@ResponseBody
	public String ceaseOrganiz(HttpServletRequest request,
			@RequestParam("clueIds") String[] clueIds) {
		logger.info("禁用线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("禁用线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			clueStoreService.updateClueStoreIsEnableCease(clueIds);

			return new ViewObject(ViewObject.RET_FAILURE, "禁用线索信息成功！").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "禁用线索信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE,
					"禁用线索信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);

		}
	}
}
