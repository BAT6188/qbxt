package com.ushine.storesinfo.controller;

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
import com.ushine.storesinfo.model.OrganizPublicAction;
import com.ushine.storesinfo.service.IOrganizPublicActionService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 组织下属媒体刊物控制器
 * @author wangbailin
 *
 */
@Controller
public class OrganizPublicActionController {
	private static final Logger logger = LoggerFactory
			.getLogger(OrganizPublicActionController.class);
	@Autowired
	private IOrganizPublicActionService organizPublicActionService;
	/**
	 * 查询组织下属媒体刊物
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
	@RequestMapping(value="/findOrganizNetworkBook.do",method=RequestMethod.GET)
	@ResponseBody
	public String findOrganizNetworkBook(@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size, HttpServletRequest request,
			@RequestParam("organizId") String organizId) {
		logger.info("查询下属媒体刊物");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询下属媒体刊物成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"),"UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			PagingObject<OrganizPublicAction> pagingObject = organizPublicActionService.findOrgSubordinatesOrganizPublicActionStore(organizId, field, fieldValue, startTime, endTime, nextPage, size);
			return findOrganizNetworkBookVoToJson(pagingObject);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询下属媒体刊物失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询下属媒体刊物失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 把媒体网站刊物集合转成json
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findOrganizNetworkBookVoToJson(PagingObject<OrganizPublicAction> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizPublicAction store : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", store.getId());
			obj.put("name", store.getWebsiteJournalStore().getName());
			obj.put("websiteURL", store.getWebsiteJournalStore().getWebsiteURL());
			obj.put("serverAddress", store.getWebsiteJournalStore().getServerAddress());
			obj.put("establishAddress", store.getWebsiteJournalStore().getEstablishAddress());
			obj.put("mainWholesaleAddress", store.getWebsiteJournalStore().getMainWholesaleAddress());
			obj.put("establishPerson", store.getWebsiteJournalStore().getEstablishPerson());
			obj.put("establishTime", store.getWebsiteJournalStore().getEstablishTime());
			obj.put("basicCondition", store.getWebsiteJournalStore().getBasicCondition());
			obj.put("infoType", store.getWebsiteJournalStore().getInfoType().getTypeName());
			obj.put("createDate", store.getWebsiteJournalStore().getCreateDate());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 组织关联下属组织
	 * @param request
	 * @param ids
	 * @param organizId
	 * @return
	 */
	@RequestMapping(value="/associatedOrgSuborinatesBook.do",method=RequestMethod.POST)
	@ResponseBody
	public String associatedOrgSuborinatesBook(HttpServletRequest request,
			@RequestParam("ids") String[] ids,
			@RequestParam("organizId") String organizId){
		logger.info("组织关联下属媒体刊物");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("组织关联下属媒体刊物成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizPublicActionService.saveOrganizPublicAction(organizId, ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "关联媒体刊物成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "组织关联下属媒体刊物失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "组织关联下属媒体刊物失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 解除组织下属媒体刊物
	 * @return
	 */
	@RequestMapping(value="/removeOrganizSuborinatesBook.do",method=RequestMethod.POST)
	@ResponseBody
	public String removeOrganizSuborinatesBook(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids){
		logger.info("解除组织下属媒体刊物");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("解除组织下属媒体刊物成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizPublicActionService.delOrganizOrganizPublicActionByids(ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "解除媒体刊物成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "解除组织下属媒体刊物失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "解除组织下属媒体刊物失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	
	
	/**
	 * 根据人员id查询上级组织
	 * @return
	 */
	@RequestMapping(value="/findNetworkAtHigherLevelByPersonStoreId.do",method=RequestMethod.GET)
	@ResponseBody
	public String findNetworkAtHigherLevelByPersonStoreId(
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size, HttpServletRequest request,
			@RequestParam("networkId") String networkId) {
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
			PagingObject<OrganizPublicAction> pagingObject = organizPublicActionService.findNetworkAtHigherLevelBynetworkId(networkId, field, fieldValue, startTime, endTime, nextPage, size);
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
	public String findOrganizSubordinatesByOrgIdVoToJson(PagingObject<OrganizPublicAction> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizPublicAction br : vo.getArray()) {
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
	 * 媒体刊物关联上级组织
	 * @return
	 */
	@RequestMapping(value="/associatedNetworkSubOrganiz.do",method=RequestMethod.POST)
	@ResponseBody
	public String associatedPersonSubOrganiz(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids,
			@RequestParam("networkId") String networkId){
		logger.info("媒体刊物关联上级组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("媒体刊物关联上级组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizPublicActionService.saveNetworkSubOrganiz(networkId, ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "媒体刊物关联上级组织成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "媒体刊物关联上级组织失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "媒体刊物关联上级组织失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 解除媒体上级组织
	 * @return
	 */
	@RequestMapping(value="/removeOrgSubordinatesNetwork.do",method=RequestMethod.POST)
	@ResponseBody
	public String removeOrgSubordinatesNetwork(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids){
		logger.info("解除媒体上级组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("解除媒体上级组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizPublicActionService.delOrganizOrganizPublicActionByids(ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "解除媒体上级组织成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "解除媒体上级组织失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "解除媒体上级组织失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
}
