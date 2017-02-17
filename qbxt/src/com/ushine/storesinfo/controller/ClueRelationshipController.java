package com.ushine.storesinfo.controller;

import java.util.Date;
import java.util.List;

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
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.storesinfo.model.ClueRelationship;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.service.IClueRelationshipService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 线索关系控制器
 * @author wangbailin
 *
 */
@Controller
public class ClueRelationshipController {
	private static final Logger logger = LoggerFactory
			.getLogger(ClueStoreController.class);
	@Autowired
	private IClueRelationshipService clueRelationshipService;
	/**
	 * 关联线索人员
	 * @return
	 */
	@RequestMapping(value="/associatedClueStoreByClueId.do",method=RequestMethod.POST)
	@ResponseBody
	public String associatedClueStoreByClueId(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids,
			@RequestParam("clueId") String clueId,
			@RequestParam("store") String store){
		logger.info("关联线索人员");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("关联线索人员成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			for (String s : ids) {
				ClueRelationship relationship = new ClueRelationship();
				relationship.setClueId(clueId);
				relationship.setDataType(store);
				relationship.setLibraryId(s);
				clueRelationshipService.savaClueRelationship(relationship);
			}
			
			
			return new ViewObject(ViewObject.RET_SUCCEED, "关联成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "关联线索失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "关联线索失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	
	
	/**
	 * 解除线索人员
	 * @return
	 */
	@RequestMapping(value="/removeClueByClueId.do",method=RequestMethod.POST)
	@ResponseBody
	public String removeClueByClueId(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids,
			@RequestParam("clueId") String clueId){
		logger.info("解除线索人员");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("解除线索人员成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			clueRelationshipService.removeClueObjByClueIdAndIds(clueId, ids);
			return new ViewObject(ViewObject.RET_SUCCEED, "解除成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "解除线索人员失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "解除线索人员失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 基础信息转线索库
	 * @param request
	 * @param clueIds
	 * @param orgIds
	 * @param store
	 * @return
	 */
	@RequestMapping(value="/basisTurnClueStore.do",method=RequestMethod.POST)
	@ResponseBody
	public String basisTurnClueStore(
			HttpServletRequest request,
			@RequestParam("clueIds") String[] clueIds,
			@RequestParam("dataId") String[] dataId,
			@RequestParam("store") String store){
		logger.info("基础信息转线索");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("基础信息转线索成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			clueRelationshipService.TurnClueStore(dataId, clueIds, store);
			return new ViewObject(ViewObject.RET_SUCCEED, "转线索成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "转线索失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "转线索失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 将特定的数据转换成json格式
	 * @param list
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String findInfoTypeByTypeNameVoToJSon(List<InfoType> list) {
		JSONObject root = new JSONObject();
		JSONArray array = new JSONArray();
		for (InfoType infoType : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", infoType.getId());
			obj.put("typeName", infoType.getTypeName());
			obj.put("tableTypeName", infoType.getTableTypeName());
			obj.put("saveTime", infoType.getSaveTime());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
}
