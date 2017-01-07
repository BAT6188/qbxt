package com.ushine.storeInfo.controller;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tdcq.common.logging.LogFactory;
import com.tdcq.common.logging.LogInfo;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.TempClueData;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.ITempClueDataService;
import com.ushine.storeInfo.service.IWebsiteJournalStoreService;
import com.ushine.storeInfo.storeFinal.StoreFinal;
import com.ushine.util.StringUtil;

@Controller
public class WebsiteJournalStoreController {
	private Logger logger=org.slf4j.LoggerFactory.getLogger(WebsiteJournalStoreController.class);
	private ServletContext servletContext;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IWebsiteJournalStoreService websiteJournalStoreService;
	@Autowired
	private ITempClueDataService tempClueDataService;
	/**
	 * 获得所有媒体文档的类型
	 * @return
	 */
	@RequestMapping(value="/getwebsitejournalstoretype.do",method=RequestMethod.GET)
	@ResponseBody
	public String getWebsiteJournalStoreType(){
		try {
			String infoType=infoTypeService.getInfoTypeByTableTypeName(StoreFinal.WEBSITEJOURNAL_STORE);
			logger.debug("媒体文档的类型包括:"+infoType);
			return infoType; 
		} catch (Exception e) {
			logger.error("异常信息:"+e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value="/hasWebsiteStoreByName.do",method=RequestMethod.POST)
	@ResponseBody
	public String hasWebsiteStoreByName(@RequestParam("name") String name){
		//默认不存在
		logger.info("查询媒体网站刊物："+name+"是否存在");
		String msg="not_exist";
		ViewObject viewObject=null;
		try {
			if(websiteJournalStoreService.hasStoreByName(name)){
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
	 * 新增媒体文档
		private String uid; // 创建人
		private String oid; // 组织
		private String did; // 部门
		private String action;//增量数据操作   1:新增，2:修改，3:删除
	 * @return
	 */
	@RequestMapping(value="/saveWebsiteJournalStore.do",method=RequestMethod.POST)
	@ResponseBody
	public String saveVocationalWorkStore(
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="websiteURL",required=false) String websiteURL,
			@RequestParam(value="serverAddress",required=false) String serverAddress,
			@RequestParam(value="establishAddress",required=false) String establishAddress,
			@RequestParam(value="mainWholesaleAddress",required=false) String mainWholesaleAddress,
			@RequestParam(value="establishPerson",required=false) String establishPerson,
			@RequestParam(value="establishTime",required=false) String establishTime,
			@RequestParam(value="basicCondition",required=false) String basicCondition,
			@RequestParam(value="infoType",required=false) String infoType,
			@RequestParam(value="isClue",required=false) String isClue,
			@RequestParam(value="clueNum",required=false) String clueNum,
			//线索关联媒体文档状态
			//没关联就是为0
			@RequestParam(value="state",required=false) String state,
			HttpServletRequest request){
		com.tdcq.common.logging.Logger log=LogFactory.getLogger();
		LogInfo logInfo=new LogInfo();
		logInfo.setApplication("新增媒体网站刊物");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("新增媒体网站刊物成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr=UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增媒体网站库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					//启用
					WebsiteJournalStore store=new WebsiteJournalStore();
					//设置权限
					store.setUid(userMgr.getUID(request));
					store.setDid(userMgr.getUDID(request));
					store.setOid(userMgr.getUOID(request));
					//新增为1
					store.setAction("1");
					//不要启用刊物
					store.setIsEnable("2");
					store.setBasicCondition(basicCondition);
					store.setEstablishAddress(establishAddress);
					store.setEstablishPerson(establishPerson);
					store.setEstablishTime(establishTime);
					store.setMainWholesaleAddress(mainWholesaleAddress);
					store.setName(name);
					store.setCreateDate(StringUtil.dates());
					store.setWebsiteURL(websiteURL);
					//设置infoType
					/*store.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName
							(infoType, StoreFinal.WEBSITEJOURNAL_STORE));*/
					store.setInfoType(infoTypeService.findInfoTypeById(infoType));
					store.setServerAddress(serverAddress);
					//设置操做人的id
					store.setUid(userMgr.getUID(request));
					//logger.info("====WebsiteJournalStore====="+store.toString());
					//logger.info("====clueNum====="+clueNum.toString());
					//首先添加
					websiteJournalStoreService.saveWebsitejournal(store);
					if (isClue.equals("isClue")) {
						
						//然后放入临时线索表里面
						TempClueData tempClueData=new TempClueData();
						tempClueData.setAction(clueNum);
						tempClueData.setDataId(store.getId());
						tempClueData.setName(store.getName());
						tempClueData.setType("websiteJournalStore");
						tempClueData.setState("1");
						//添加到临时线索库
						if(state!=null&&state.equals("0")){
							//这代表没有关联组织需要更新
							tempClueDataService.updateTempClueData(tempClueData);
						}else{
							//需要保存
							tempClueDataService.saveTempClueData(tempClueData);
						}
					}
					return new ViewObject(ViewObject.RET_FAILURE, "新增媒体网站刊物成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "新增失败，没有权限").toJSon();
		} catch (Exception e) {
			logger.error("新增媒体网站刊物失败"+e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增媒体网站刊物失败").toJSon();
		}finally {
			log.log(logInfo);
		}
	}
	/**
	 * 入库媒体文档
		private String uid; // 创建人
		private String oid; // 组织
		private String did; // 部门
		private String action;//增量数据操作   1:新增，2:修改，3:删除
	 * @return
	 */
	@RequestMapping(value="/dataStorageWebsiteJounalById.do",method=RequestMethod.POST)
	@ResponseBody
	public String dataStorageWebsiteJounalById(
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="websiteURL",required=false) String websiteURL,
			@RequestParam(value="serverAddress",required=false) String serverAddress,
			@RequestParam(value="establishAddress",required=false) String establishAddress,
			@RequestParam(value="mainWholesaleAddress",required=false) String mainWholesaleAddress,
			@RequestParam(value="establishPerson",required=false) String establishPerson,
			@RequestParam(value="establishTime",required=false) String establishTime,
			@RequestParam(value="basicCondition",required=false) String basicCondition,
			@RequestParam(value="infoType",required=false) String infoType,
			@RequestParam(value="clueNum",required=false) String clueNum,
			@RequestParam("typeId") String typeId,
			@RequestParam("id") String id,
			HttpServletRequest request){
		com.tdcq.common.logging.Logger log=LogFactory.getLogger();
		LogInfo logInfo=new LogInfo();
		logInfo.setApplication("入库媒体网站刊物");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("入库媒体网站刊物成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr=UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
					//启用
					WebsiteJournalStore store=new WebsiteJournalStore();
					store.setId(id);
					//设置权限
					store.setUid(userMgr.getUID(request));
					store.setDid(userMgr.getUDID(request));
					store.setOid(userMgr.getUOID(request));
					//新增为1
					store.setAction("1");
					//不要启用刊物
					store.setIsEnable("2");
					store.setBasicCondition(basicCondition);
					store.setEstablishAddress(establishAddress);
					store.setEstablishPerson(establishPerson);
					store.setEstablishTime(establishTime);
					store.setMainWholesaleAddress(mainWholesaleAddress);
					store.setName(name);
					store.setCreateDate(StringUtil.dates());
					store.setWebsiteURL(websiteURL);
					store.setInfoType(infoTypeService.findInfoTypeById(typeId));
					store.setServerAddress(serverAddress);
					store.setIsToStorage("1");
					//设置操做人的id
					store.setUid(userMgr.getUID(request));
					websiteJournalStoreService.updateWebsiteJournalStore(store);
					return new ViewObject(ViewObject.RET_FAILURE, "入库媒体网站刊物成功").toJSon();
		} catch (Exception e) {
			logger.error("入库媒体网站刊物失败"+e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:入库媒体网站刊物失败").toJSon();
		}finally {
			log.log(logInfo);
		}
	}
	/**
	 * 查询媒体网站刊物库信息，多条件
	 * @return
	 */
	@RequestMapping(value="/findWebsiteJournalStore.do",method=RequestMethod.GET)
	@ResponseBody
	public String findVocationalWorkStoreByConditions(
			@RequestParam(value="fieldValue",required=false)String fieldValue,
			@RequestParam(value="field",required=false)String field,
			@RequestParam(value="startTime",required=false) String startTime,
			@RequestParam(value="endTime",required=false) String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size,
			//排序字段
			@RequestParam(value ="sort", required = false) String sortField,
			@RequestParam(value ="dir", required = false) String dir,
			HttpServletRequest request){
		logger.info("查询媒体网站刊物库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询媒体网站刊物库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			//汉字关键字搜索
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限查询
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {
					//读取全部
					return websiteJournalStoreService.findWebsiteJournalStore(field, fieldValue, startTime, endTime, nextPage,size,
							null, null, null,sortField,dir);
				}else if("1x0010".equals(list.get(0))){  //所属组织
					return websiteJournalStoreService.findWebsiteJournalStore(field, fieldValue, startTime, endTime, nextPage, size, 
							null, sessionMgr.getUOID(request), null,sortField,dir);
				}else if("1x0011".equals(list.get(0))){  //所属部门
					return websiteJournalStoreService.findWebsiteJournalStore(field, fieldValue, startTime, endTime, nextPage, size, 
							null,null, sessionMgr.getUDID(request),sortField,dir);
				}else if("1x0100".equals(list.get(0))){  //个人数据
					return websiteJournalStoreService.findWebsiteJournalStore(field, fieldValue, startTime, endTime, nextPage, size, 
							sessionMgr.getUID(request),null, null,sortField,dir);
				}else{//禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询媒体网站刊物库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 删除媒体网站刊物
	 * @param ids 刊物的id数组
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/delWebsiteJournalStore.do",method=RequestMethod.POST)
	@ResponseBody
	public String delWebsiteJournalStore(
			@RequestParam("ids")String []ids,
			HttpServletRequest request){
		logger.info("删除媒体网站刊物库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("删除媒体网站刊物库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限删除
			if (list != null && list.size() > 0) {
					if ("0x0001".equals(list.get(0))) {
						//
						websiteJournalStoreService.delWebsiteJournalStoreByIds(ids);
						return new ViewObject(ViewObject.RET_FAILURE, "删除媒体网站刊物成功").toJSon();
					}
				}
			return new ViewObject(ViewObject.RET_FAILURE, "删除失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "删除媒体网站刊物库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "删除失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 修改
	 * @param id
	 * @param name
	 * @param websiteURL
	 * @param serverAddress
	 * @param establishAddress
	 * @param mainWholesaleAddress
	 * @param establishPerson
	 * @param establishTime
	 * @param basicCondition
	 * @param infoType
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updateWebsiteJournalStore.do",method=RequestMethod.POST)
	@ResponseBody
	public String updateWebsiteJournalStore(
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="websiteURL",required=false) String websiteURL,
			@RequestParam(value="serverAddress",required=false) String serverAddress,
			@RequestParam(value="establishAddress",required=false) String establishAddress,
			@RequestParam(value="mainWholesaleAddress",required=false) String mainWholesaleAddress,
			@RequestParam(value="establishPerson",required=false) String establishPerson,
			@RequestParam(value="establishTime",required=false) String establishTime,
			@RequestParam(value="basicCondition",required=false) String basicCondition,
			@RequestParam(value="infoType",required=false) String infoType,
			HttpServletRequest request){
		com.tdcq.common.logging.Logger log=LogFactory.getLogger();
		LogInfo logInfo=new LogInfo();
		logInfo.setApplication("修改媒体网站刊物");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("修改媒体网站刊物成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr=UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限修改媒体网站库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					//启用
					//先获得对象
					WebsiteJournalStore store=websiteJournalStoreService.findWebsiteJouById(id);
					logger.debug("==修改的媒体网站刊物对象=="+store);
					//设置权限
					store.setUid(userMgr.getUID(request));
					store.setDid(userMgr.getUDID(request));
					store.setOid(userMgr.getUOID(request));
					
					store.setName(name);
					store.setAction("2");
					store.setWebsiteURL(websiteURL);
					store.setEstablishAddress(establishAddress);
					store.setEstablishPerson(establishPerson);
					store.setBasicCondition(basicCondition);
					store.setEstablishTime(establishTime);
					store.setServerAddress(serverAddress);
					store.setMainWholesaleAddress(mainWholesaleAddress);
					//获得类别
					//InfoType type=infoTypeService.findInfoTypeById(infoType);
					InfoType type=infoTypeService.findInfoTypeByTypeNameAndTableName(infoType, 
							StoreFinal.WEBSITEJOURNAL_STORE);
					store.setInfoType(type);
					//更新
					websiteJournalStoreService.updateWebsiteJournalStore(store);
					return new ViewObject(ViewObject.RET_FAILURE, "修改媒体网站刊物成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改失败，没有权限").toJSon();
		} catch (Exception e) {
			logger.error("修改媒体网站刊物失败"+e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:修改媒体网站刊物失败").toJSon();
		}finally {
			log.log(logInfo);
		}
	}
	/**
	 * 启用或禁用媒体
	 * @param ids id数组
	 * @param type 类型是启用还是禁用
	 * @param request
	 * @return
	 */
	@RequestMapping("/startOrCeaseWebsiteJournalStore.do")
	@ResponseBody
	public String startOrCeaseWebsiteJournalStore(
			@RequestParam("ids")String []ids,
			@RequestParam("type")String type,
			HttpServletRequest request
			){
		
		logger.info(type+"媒体");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult(type+"媒体成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			logger.info("====禁用或启用媒体===="+type+"媒体");
			logger.info("====id数组===="+Arrays.toString(ids));
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			if(type.equals("启用")){
				websiteJournalStoreService.startWebsiteJournalStore(ids);
			}else if(type.equals("禁用")){
				websiteJournalStoreService.ceaseWebsiteJournalStore(ids);
			}
			return new ViewObject(ViewObject.RET_FAILURE, type+"媒体信息成功！").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = type+"媒体信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, type+"媒体信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 下载媒体刊物网站信息的PDF文件
	 * @param request
	 * @param name
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadMediaNetworkPDFFile.do")
	@ResponseBody
	public String dwonExportData(HttpServletRequest request,
			@RequestParam("networkId") String networkId,
			HttpServletResponse response) {
		ServletOutputStream out = null;
		String root = request.getServletContext().getRealPath("/");
		//文件名
		String path = root + networkId + ".doc";
		try {
			// 获取服务器地址
			//String path = servletContext.getRealPath("/") + "PDFFilePath/";
			//生成word
			websiteJournalStoreService.outputWebsiteJournalStoreToWord(networkId, path);
			// 下载附件
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头
			response.setHeader("Content-Disposition", "attachment;fileName=" + networkId + ".doc");
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
}
