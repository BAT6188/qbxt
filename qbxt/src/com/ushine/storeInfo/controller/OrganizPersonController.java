package com.ushine.storeInfo.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import com.ushine.storeInfo.model.OrganizPerson;
import com.ushine.storeInfo.service.IOrganizPersonService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 组织下属人员控制器
 * @author wangbailin
 *
 */
@Controller
public class OrganizPersonController {
	private static final Logger logger = LoggerFactory
			.getLogger(OrganizPersonController.class);
	@Autowired
	private IOrganizPersonService organizPersonService;
	/**
	 * 根据组织id查询下属人员
	 * @param fieldValue
	 * @param field
	 * @param startTime
	 * @param endTime
	 * @param nextPage
	 * @param size
	 * @param request
	 * @param organizId
	 * @return
	 */
	@RequestMapping(value="/findOrganizSubPersonStore.do",method=RequestMethod.GET)
	@ResponseBody
	public String findOrganizSubPersonStore(@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size, HttpServletRequest request,
			@RequestParam("organizId") String organizId) {
		logger.info("查询下属人员");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询下属人员成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"),"UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			PagingObject<OrganizPerson> pagingObject = organizPersonService.findOrgSubordinatesPersonStore(organizId, field, fieldValue, startTime, endTime, nextPage, size);
			return findOrganizSubPersonStoreVoToJson(pagingObject);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询下属人员失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询下属人员失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findOrganizSubPersonStoreVoToJson(PagingObject<OrganizPerson> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizPerson op : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", op.getId());
			obj.put("personName", op.getPersonStore().getPersonName());
			obj.put("nameUsedBefore", op.getPersonStore().getNameUsedBefore());
			obj.put("englishName", op.getPersonStore().getEnglishName());
			obj.put("bebornTime", op.getPersonStore().getBebornTime());
			obj.put("presentAddress", op.getPersonStore().getPresentAddress());
			obj.put("workUnit", op.getPersonStore().getWorkUnit());
			obj.put("registerAddress", op.getPersonStore().getRegisterAddress());
			obj.put("antecedents", op.getPersonStore().getAntecedents());
			obj.put("activityCondition", op.getPersonStore().getActivityCondition());
			obj.put("sex", op.getPersonStore().getSex());
			obj.put("infoType", op.getPersonStore().getInfoType().getTypeName());
			obj.put("createDate", op.getPersonStore().getCreateDate());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	
	
	/**
	 * 组织关联人员
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/associatedOrgSubPerson.do",method=RequestMethod.POST)
	@ResponseBody
	public String associatedOrgSubPerson(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids,
			@RequestParam("organizId") String organizId){
		logger.info("组织关联人员");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("组织关联人员成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizPersonService.saveOrganizPersons(organizId, ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "关联人员成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "组织关联人员失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "组织关联人员失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 解除组织下属人员
	 * @return
	 */
	@RequestMapping(value="/removeOrgSubordinatesPeson.do",method=RequestMethod.POST)
	@ResponseBody
	public String removeOrgSubordinatesPeson(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids){
		logger.info("解除组织下属人员");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("解除组织下属人员成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizPersonService.delOrganizPersonByIds(ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "解除人员成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "解除组织下属人员失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "解除组织下属人员失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	
	

	
	/**
	 * 根据人员id查询上级组织
	 * @return
	 */
	@RequestMapping(value="/findPersonAtHigherLevelByPersonStoreId.do",method=RequestMethod.GET)
	@ResponseBody
	public String findPersonAtHigherLevelByPersonStoreId(
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size, HttpServletRequest request,
			@RequestParam("personStoreId") String personStoreId) {
		logger.info("查询人员上级组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询人员上级组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"),"UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			PagingObject<OrganizPerson> pagingObject = organizPersonService.findPersonAtHigherLevelByPersonStoreId(personStoreId, field, fieldValue, startTime, endTime, nextPage, size);
			return findOrganizSubordinatesByOrgIdVoToJson(pagingObject);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询人员上级组织失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询人员上级组织失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findOrganizSubordinatesByOrgIdVoToJson(PagingObject<OrganizPerson> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizPerson br : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("subId", br.getId());
			obj.put("id", br.getOrganizStore().getId());
			obj.put("organizName", br.getOrganizStore().getOrganizName());
			obj.put("orgHeadOfName", br.getOrganizStore().getOrgHeadOfName());
			//不为空
			if(br.getOrganizStore().getInfoType()!=null){
				//
				obj.put("infoType",br.getOrganizStore().getInfoType().getTypeName());
			}
			obj.put("websiteURL", br.getOrganizStore().getWebsiteURL());
			obj.put("organizPublicActionNames", br.getOrganizStore().getOrganizPublicActionNames());
			obj.put("organizPersonNames",br.getOrganizStore().getOrganizPersonNames());
			obj.put("organizBranchesNames", br.getOrganizStore().getOrganizBranchesNames());
			obj.put("foundTime", br.getOrganizStore().getFoundTime());
			obj.put("degreeOfLatitude", br.getOrganizStore().getDegreeOfLatitude());
			obj.put("basicCondition",br.getOrganizStore().getBasicCondition());
			obj.put("activityCondition",br.getOrganizStore().getActivityCondition());
			obj.put("createDate", br.getOrganizStore().getCreateDate());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	
	
	/**
	 * 人员关联上级组织
	 * @return
	 */
	@RequestMapping(value="/associatedPersonSubOrganiz.do",method=RequestMethod.POST)
	@ResponseBody
	public String associatedPersonSubOrganiz(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids,
			@RequestParam("personStoreId") String personStoreId){
		logger.info("人员关联上级组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("人员关联上级组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizPersonService.savePersonSubOrganiz(personStoreId, ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "人员关联上级组织成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "人员关联上级组织失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "人员关联上级组织失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
}
