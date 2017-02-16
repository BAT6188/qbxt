package com.ushine.storeInfo.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.ushine.core.po.Organiz;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.storefinal.StoreFinal;
import com.ushine.util.PathUtils;
import com.ushine.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 信息类别管理控制器
 * @author wangbailin
 *
 */
@Controller
public class InfoTypeController {
	private static final Logger logger = LoggerFactory
			.getLogger(InfoTypeController.class);
	@Autowired
	private IInfoTypeService infoTypeService;
	
	@Autowired
	private IBaseDao baseDao;
	/**
	 * 查询信息类别并生成树形菜单
	 * @param node 上级id
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getInfoTypeTree", method=RequestMethod.GET)
	@ResponseBody
	public String findByTree(@RequestParam("node") String node, 
			HttpServletRequest request) {
		try {
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			List<String> codes = sessionMgr.getPermitResOperCode(request);
			ViewObject vo = new ViewObject(ViewObject.RET_FAILURE, "您没有对该资源操作的权限.");
			List<Organiz> orgs = new ArrayList<Organiz>();
			JSONArray array=new JSONArray();
			logger.debug("该权限操作编码:" + codes + ".");
			String path= PathUtils.getWebInfPath(InfoType.class)+"config/info-type-config.xml";
			InputStream inputStream = new FileInputStream(path);
			List<HashMap<String, String>> listMap = infoTypeService.readXml(inputStream, "type");	
					// 全部数据
					if(node.equals("root")){
						for (HashMap<String, String> hashMap : listMap) {
							JSONObject obj=new JSONObject();
							obj.put("text",hashMap.get(StoreFinal.BASED_LIBRARY_NAME) );
							obj.put("id", hashMap.get(StoreFinal.BASED_LIBRARY_TYPE));
							obj.put("type", "0");  //0：表示组织1：部门
							obj.put("leaf", false);
							obj.put("expanded", true);
							array.add(obj);
						}
					}
					return array.toString();
				
			
		} catch (Exception e) {
			String msg = "请求获取组织信息操作失败.";
			logger.error(msg , e);
			return new ViewObject(-1, msg).toJSon();
		}
	}
	/**
	 * 根据表名称字段查询类别信息
	 * @param request
	 * @param tableTypeName
	 * @param nextPage
	 * @param size
	 * @return
	 */
	@RequestMapping(value="/findInfoTypeByTypeName",method=RequestMethod.GET)
	@ResponseBody
	public String findInfoTypeByTypeName(
			HttpServletRequest request,
			@RequestParam("tableTypeName") String tableTypeName,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size){
		try {
			List<InfoType> list = infoTypeService.findInfoTypeByTypeName(tableTypeName);
			return findInfoTypeByTypeNameVoToJSon(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	
	
	/**
	 * 新增信息类别控制器
	 * @return
	 */
	@RequestMapping(value="/saveInfoType",method=RequestMethod.POST)
	@ResponseBody
	public String saveInfoType(
			HttpServletRequest request,
			@RequestParam("tableTypeName") String tableTypeName,
			@RequestParam("typeName") String typeName
			){
		logger.info("新增信息类别");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("新增信息类别成功");
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
					InfoType infoType = new InfoType();
					infoType.setSaveTime(StringUtil.dates());
					infoType.setTableTypeName(tableTypeName);
					infoType.setTypeName(typeName);
					infoTypeService.saveInfoType(infoType);
					/*if(tableTypeName.equals("OutsideDocStore")){
						//添加一个导入文档的节点
						String xml = com.ushine.common.utils.PathUtils.getConfigPath(OutsideDocStoreServiceImpl.class) + "outside-doc-store.xml";
						XMLUtils utils=new XMLUtils(xml);
						boolean result=utils.addElement("first-type","element", typeName);
						logger.info("添加一个导入文档的节点结果："+result);
					}*/
					return new ViewObject(ViewObject.RET_SUCCEED, "新增类别成功!").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "新增失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "新增信息类别失败";
			logger.error(msg, e);
			loginfo.setResult("新增信息类别失败：" + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "新增失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	
	
	/**
	 * 修改信息类别控制器
	 * @return
	 */
	@RequestMapping(value="/updateInfoType",method=RequestMethod.POST)
	@ResponseBody
	public String updateInfoType(
			HttpServletRequest request,
			@RequestParam("infoTypeId") String infoTypeId,
			@RequestParam("typeName") String typeName
			){
		logger.info("修改信息类别");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("修改信息类别成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限修改信息类别
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					InfoType infoType = infoTypeService.findInfoTypeById(infoTypeId);
					infoType.setTypeName(typeName);
					
					infoTypeService.updateInfoTypeIndex(infoTypeService.findInfoTypeById(infoTypeId),infoType);
					//更新类别
					infoTypeService.updateInfoType(infoType);
					return new ViewObject(ViewObject.RET_SUCCEED, "修改类别成功!").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "修改信息类别失败";
			logger.error(msg, e);
			loginfo.setResult("修改信息类别失败：" + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "修改失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 删除信息类别控制器
	 * @return
	 */
	@RequestMapping(value="/delInfoType",method=RequestMethod.POST)
	@ResponseBody
	public String delInfoType(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids
			){
		logger.info("删除信息类别");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("删除信息类别成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限删除信息类别
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					infoTypeService.delInfoType(ids);
					return new ViewObject(ViewObject.RET_SUCCEED, "删除类别成功!").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "删除失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "删除信息类别失败";
			logger.error(msg, e);
			loginfo.setResult("删除信息类别失败：" + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "类别已经关联数据,无法删除");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 根据类型分别统计每个类别的数据数量
	 * @return
	 */
	@RequestMapping(value="/getInfoTypeDataCount.do",method=RequestMethod.GET)
	@ResponseBody
	public String getInfoTypeDataCount(
			@RequestParam("tableTypeName") String tableTypeName){
		try {
			return infoTypeService.infoTypeDataCount(tableTypeName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(83512%5);
	}
}
