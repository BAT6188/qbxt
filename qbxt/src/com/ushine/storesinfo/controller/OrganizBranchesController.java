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
import com.ushine.storesinfo.model.OrganizBranches;
import com.ushine.storesinfo.service.IOrganizBranchesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 组织下属组织控制器
 * @author wangbailin
 *
 */
@Controller
public class OrganizBranchesController {
	private static final Logger logger = LoggerFactory
			.getLogger(OrganizBranchesController.class);
	@Autowired
	private IOrganizBranchesService branchesService;
	/**
	 * 根据组织id查询下属组织
	 * @return
	 */
	@RequestMapping(value="/findOrganizSubordinatesByOrgId.do",method=RequestMethod.GET)
	@ResponseBody
	public String findOrganizSubordinatesByOrgId(
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size, HttpServletRequest request,
			@RequestParam("organizId") String organizId) {
		logger.info("查询下属组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询下属组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"),"UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			PagingObject<OrganizBranches> pagingObject = branchesService.findOrgSubordinatesOrgStore(organizId, field, fieldValue, startTime, endTime, nextPage, size);
			return findOrganizSubordinatesByOrgIdVoToJson(pagingObject);
		
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询下属组织失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询下属组织失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findOrganizSubordinatesByOrgIdVoToJson(PagingObject<OrganizBranches> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizBranches br : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", br.getId());
			obj.put("organizName", br.getOrganizBranches().getOrganizName());
			obj.put("orgHeadOfName", br.getOrganizBranches().getOrgHeadOfName());
			//不为空
			if(br.getOrganizBranches().getInfoType()!=null){
				//
				obj.put("infoType",br.getOrganizBranches().getInfoType().getTypeName());
			}
			obj.put("websiteURL", br.getOrganizBranches().getWebsiteURL());
			obj.put("organizPublicActionNames", br.getOrganizBranches().getOrganizPublicActionNames());
			obj.put("organizPersonNames",br.getOrganizBranches().getOrganizPersonNames());
			obj.put("organizBranchesNames", br.getOrganizBranches().getOrganizBranchesNames());
			obj.put("foundTime", br.getOrganizBranches().getFoundTime());
			obj.put("degreeOfLatitude", br.getOrganizBranches().getDegreeOfLatitude());
			obj.put("basicCondition",br.getOrganizBranches().getBasicCondition());
			obj.put("activityCondition",br.getOrganizBranches().getActivityCondition());
			obj.put("createDate", br.getOrganizBranches().getCreateDate());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 组织关联下属组织
	 * @return
	 */
	@RequestMapping(value="/associatedOrganizSubOrganiz.do",method=RequestMethod.POST)
	@ResponseBody
	public String associatedOrganizSubOrganiz(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids,
			@RequestParam("organizId") String organizId){
		logger.info("组织关联下属组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("组织关联下属组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			branchesService.saveOrganizBranches(organizId, ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "关联组织成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "组织关联下属组织失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "组织关联下属组织失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	
	/**
	 * 解除下属组织
	 * @return
	 */
	@RequestMapping(value="removeOrganizSubOrganiz",method=RequestMethod.POST)
	@ResponseBody
	public String removeOrganizSubOrganiz(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids){
		logger.info("解除下属组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("解除下属组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			branchesService.delOrganizBranchesByids(ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "解除组织成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "解除下属组织失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "解除下属组织失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	
}
