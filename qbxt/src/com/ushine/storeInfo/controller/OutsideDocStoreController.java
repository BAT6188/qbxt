package com.ushine.storeInfo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import com.ushine.common.config.Configured;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IOutsideDocStoreService;
import com.ushine.storeInfo.storeFinal.StoreFinal;
import com.ushine.util.IdentifyDocUtils;
import com.ushine.util.ReadAttachUtil;
import com.ushine.util.SmbFileUtils;
import com.ushine.util.StringUtil;
import com.ushine.util.UpLoadUtil;
import com.ushine.util.WordToHtmlUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 外来文档控制器
 * 
 * @author dh
 * 
 */
@Controller("outsideDocStoreController")
public class OutsideDocStoreController {
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IOutsideDocStoreService outsideDocStoreService;

	private Logger logger = LoggerFactory.getLogger(OutsideDocStoreController.class);
	private String outsideDocStore = Configured.getInstance().get("outsideDocStore");

	/**
	 * 获得外来文档的所有类型
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getoutsidedocstoretype.do", method = RequestMethod.GET)
	@ResponseBody
	public String getOutsideDocStoreType() {
		try {
			return infoTypeService.getInfoTypeByTableTypeName(StoreFinal.OUTSIDEDOC_STORE);
		} catch (Exception e) {
			logger.error("异常信息:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询外来文档库信息，多条件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findOutsideDocStore.do", method = RequestMethod.GET)
	@ResponseBody
	public String findOutsideDocStoreByConditions(
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, @RequestParam("page") int nextPage,
			@RequestParam("limit") int size,
			// 排序字段
			@RequestParam(value = "sort", required = false) String sortField,
			// 升序或降序
			@RequestParam(value = "dir", required = false) String dir, HttpServletRequest request) {
		logger.info("查询外来文档库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询外来文档库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			//
			logger.info(String.format("外来文档的排序字段：%s", sortField));
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限查询人员库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {
					// 读取全部
					return outsideDocStoreService.findOutsideDocStore(field, fieldValue, startTime, endTime, nextPage,
							size, null, null, null, sortField, dir);
				} else if ("1x0010".equals(list.get(0))) {
					// 所属组织
					return outsideDocStoreService.findOutsideDocStore(field, fieldValue, startTime, endTime, nextPage,
							size, null, sessionMgr.getUOID(request), null, sortField, dir);
				} else if ("1x0011".equals(list.get(0))) {
					// 所属部门
					return outsideDocStoreService.findOutsideDocStore(field, fieldValue, startTime, endTime, nextPage,
							size, null, null, sessionMgr.getUDID(request), sortField, dir);
				} else if ("1x0100".equals(list.get(0))) {
					// 个人数据
					return outsideDocStoreService.findOutsideDocStore(field, fieldValue, startTime, endTime, nextPage,
							size, sessionMgr.getUID(request), null, null, sortField, dir);
				} else {
					// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询外来文档库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	@RequestMapping(value = "/hasStoreByName.do", method = RequestMethod.POST)
	@ResponseBody
	public String hasStoreByName(@RequestParam("name") String name) {
		// 默认不存在
		logger.info("查询文档：" + name + "是否存在");
		String msg = "not_exist";
		ViewObject viewObject = null;
		try {
			if (outsideDocStoreService.hasStoreByName(name)) {
				// 已经存在文档名称
				msg = "exist";
			}
			viewObject = new ViewObject(ViewObject.RET_SUCCEED, msg);
		} catch (Exception e) {
			viewObject = new ViewObject(ViewObject.RET_ERROR, msg);
			e.printStackTrace();
			logger.error("查询失败");
		}
		return viewObject.toJSon();
	}

	/**
	 * 新增外来文档
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveOutsideDocStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String saveOutsideDocStore(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "docNumber", required = false) String docNumber,
			@RequestParam(value = "infoType", required = false) String infoType,
			@RequestParam(value = "time", required = false) String time,
			// @RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "secretRank", required = false) String secretRank,
			@RequestParam(value = "sourceUnit", required = false) String sourceUnit,
			@RequestParam(value = "centent", required = false) String centent,
			@RequestParam(value = "involvedInTheField", required = false) String involvedInTheField,
			@RequestParam(value = "number", required = false) String number,
			// 允许新增多个文档
			@RequestParam(value = "saveMultiDoc", required = false) String saveMultiDoc,
			// 上传多个文件新增多个业务文档参数
			@RequestParam(value = "uploadAndSaveMultiDoc", required = false) String uploadAndSaveMultiDoc,
			// 多个文档
			@RequestParam(value = "datas", required = false) String datas,
			@RequestParam(value = "uploadNumber", required = false) String uploadNumber, HttpServletResponse response,
			HttpServletRequest request) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("新增外来文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("新增外来文档成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增信息
			String msg = "新增外来文档成功";
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					if (StringUtils.equals(saveMultiDoc, "yes")) {
						// 共享文件夹方式
						msg = saveMultiOutsideDocStore(datas,sourceUnit, 
								secretRank,infoType, request, userMgr, uploadNumber);
					} else if (StringUtils.equals(uploadAndSaveMultiDoc, "yes")) {
						// 上传文件的方式
						msg = saveMultiForeingStore(datas, request, userMgr);
					} else {
						OutsideDocStore store = new OutsideDocStore();
						// 新增为1
						store.setAction("1");
						// 设置权限
						store.setUid(userMgr.getUID(request));
						store.setDid(userMgr.getUDID(request));
						store.setOid(userMgr.getUOID(request));
						// 用户id
						store.setUid(userMgr.getUID(request));
						store.setName(name);
						store.setTime(time);
						store.setDocNumber(docNumber);
						store.setCentent(centent);
						store.setCreateDate(StringUtil.dates());
						store.setInvolvedInTheField(infoTypeService.findInfoTypeById(involvedInTheField));
						// 设置类别
						InfoType type = infoTypeService.findInfoTypeById(infoType);
						store.setInfoType(type);
						// 附件名称
						String attaches = foreignDocStoreFilePath(number, request, response);
						store.setAttaches(attaches);
						// 设置附件内容
						String root = System.getProperty("qbtest.root");
						String[] attachesNames = attaches.split(",");
						StringBuffer buffer = new StringBuffer();
						for (String string : attachesNames) {
							String fileName = root + File.separator + string.trim();
							File file = new File(fileName);
							buffer.append(ReadAttachUtil.readContent(fileName) + " ");
						}
						outsideDocStoreService.saveOutsideDocStore(store);
					}
				}
				return new ViewObject(ViewObject.RET_FAILURE, msg).toJSon();
			}
			return new ViewObject(ViewObject.RET_FAILURE, "新增失败，没有权限").toJSon();
		} catch (Exception e) {
			logger.error("新增外来文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增外来文档失败").toJSon();
		}
	}

	/**
	 * 上传后新增多个文档
	 * 
	 * @param datas
	 * @param request
	 * @param userMgr
	 * @return
	 */
	public String saveMultiForeingStore(@RequestParam("datas") String datas, HttpServletRequest request,
			UserSessionMgr userMgr) {
		logger.info("新增数据datas：" + datas);
		try {
			String result = outsideDocStoreService.saveOutsideDocStore(datas, request, userMgr);
			logger.info("result：" + result);
			return new ViewObject(ViewObject.RET_SUCCEED, result).toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增业务文档失败").toJSon();
		}
	}

	/**
	 * 新增多个外来文档
	 * 
	 * @return
	 */
	public String saveMultiOutsideDocStore(String datas, 
			String sourceUnit, String secretRank,String infoType,
			HttpServletRequest request, UserSessionMgr userMgr, String uploadNumber) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("新增多个外来文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("新增多个外来文档");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			String result = outsideDocStoreService.saveOutsideDocStore(datas,sourceUnit,
					secretRank,infoType, request, userMgr, uploadNumber);
			return new ViewObject(ViewObject.RET_FAILURE, result).toJSon();
		} catch (Exception e) {
			logger.error("新增外来文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增外来文档失败").toJSon();
		} finally {
			// 删掉转成的html文档
			try {
				String htmlPath = request.getServletContext()
						.getRealPath(File.separator + "tempWordToHtml" + uploadNumber);
				File file = new File(htmlPath);
				if (file.isDirectory()) {
					FileUtils.deleteDirectory(file);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 识别文件夹文档
	 * 
	 * @param uploadNumber
	 * @param ip
	 * @param folderName
	 * @param docTypeId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/identifyOutsideDocStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String identifyOutsideDocStore(@RequestParam(value = "uploadNumber", required = false) String uploadNumber,
			@RequestParam(value = "ip", required = false) String ip,
			@RequestParam(value = "folderName", required = false) String folderName,
			@RequestParam(value = "docType", required = false) String docTypeId, HttpServletRequest request) {
		String result = null;
		String targetFolder = null;
		try {
			String typeName = infoTypeService.findInfoTypeById(docTypeId).getTypeName();
			logger.info("外来文档类型:" + typeName);
			logger.info("共享机器ip:" + ip);
			// logger.info("识别文件夹名称:" + folderName);
			String temp = "tempOutsideDocUpload" + uploadNumber;
			targetFolder = request.getServletContext().getRealPath(temp);
			// 复制共享文件到targetFoler下
			logger.info("复制到目标文件夹下:" + targetFolder);
			SmbFileUtils utils = new SmbFileUtils();
			// utils.copySmbFilesToDir(folderName, targetFolder);
			// 替换反斜杠
			ip = ip.replaceAll("\\\\", "/");
			utils.copySmbWordToDir(ip, targetFolder);
			// 识别文档
			String msg = outsideDocStoreService.identifyOutsideDoc(targetFolder, typeName);
			result = new ViewObject(ViewObject.RET_SUCCEED, msg).toJSon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除 修改action为3
	 * 
	 * @param serviceDocStoreIds
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delOutsideDocStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String delOutsideDocStore(@RequestParam("ids") String[] ids, HttpServletRequest request) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("删除外来文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("删除外来文档成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					outsideDocStoreService.delOutsideDocStore(ids);
					logger.debug("删除多个外来文档id：" + Arrays.toString(ids));
					return new ViewObject(ViewObject.RET_FAILURE, "删除外来文档成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "删除失败，没有权限").toJSon();
		} catch (Exception e) {
			logger.error("删除外来文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:删除外来文档失败").toJSon();
		}
	}

	/**
	 * 修改外来文档
	 * 
	 */
	@RequestMapping(value = "/updateOutsideDocStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String updateOutsideDocStore(@RequestParam("id") String id, @RequestParam("name") String name,
			// @RequestParam("sourceUnit") String sourceUnit,
			@RequestParam("infoType") String infoType, @RequestParam("time") String time,
			// @RequestParam("title") String title,
			@RequestParam("docNumber") String docNumber, @RequestParam("centent") String centent,
			// @RequestParam("secretRank") String secretRank,
			@RequestParam(value = "involvedInTheField", required = false) String involvedInTheField,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "attaches", required = false) String attaches, HttpServletResponse response,
			HttpServletRequest request) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("修改外来文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("修改外来文档成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);

		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限修改外来文档信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					// 先获得对象
					OutsideDocStore store = outsideDocStoreService.findOutsideDocStoreById(id);
					// 设置权限
					store.setUid(userMgr.getUID(request));
					store.setDid(userMgr.getUDID(request));
					store.setOid(userMgr.getUOID(request));
					// 修改为2
					store.setAction("2");
					store.setUid(userMgr.getUID(request));
					store.setName(name);
					store.setTime(time);
					store.setCentent(centent);
					store.setDocNumber(docNumber);
					/*
					 * store.setSecretRank(secretRank); store.setTitle(title);
					 * store.setSourceUnit(sourceUnit);
					 */
					store.setInvolvedInTheField(infoTypeService.findInfoTypeByTypeNameAndTableName(involvedInTheField,
							StoreFinal.INVOLVED_IN_THE_FIELD));

					// 设置类别
					InfoType type = infoTypeService.findInfoTypeByTypeNameAndTableName(infoType,
							StoreFinal.OUTSIDEDOC_STORE);

					// InfoType type=infoTypeService.findInfoTypeById(infoType);
					store.setInfoType(type);
					// 重新设置附件
					StringBuffer buffer = new StringBuffer();
					if (StringUtils.isNotEmpty(attaches)) {
						String[] files = attaches.split(",");
						for (String file : files) {
							// 临时上传的
							if (file.contains("tempForeignDocStoreFileUpLoad_")) {
								String leadStoreFile = foreignDocStoreFilePath(number, request, response);
								// 重新上传文件后
								if (leadStoreFile != null && leadStoreFile.length() > 1) {
									buffer.append(leadStoreFile);
									buffer.append(",");
								}
							} else {
								// 这是原来的附件
								String attach_name=outsideDocStore + File.separator + file;
								buffer.append(attach_name);
								buffer.append(",");
							}
						}
					}
					String filePath = buffer.toString().trim();
					//filePath = filePath.replace(" ", "");
					if (filePath != null && filePath.length() > 0) {
						// 去掉,号
						filePath = filePath.substring(0, filePath.length() - 1);
					}
					logger.info("全部附件名称："+filePath);
					store.setAttaches(filePath);
					// 设置附件内容
					String root = System.getProperty("qbtest.root");
					String[] attachesNames = filePath.split(",");
					StringBuffer content = new StringBuffer();
					for (String string : attachesNames) {
						String fileName = root + File.separator + string.trim();
						File file = new File(fileName);
						content.append(ReadAttachUtil.readContent(fileName) + " ");
					}
					// store.setAttachContent(content.toString());
					outsideDocStoreService.updateOutsideDocStore(store);
					return new ViewObject(ViewObject.RET_FAILURE, "修改外来文档成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改外来文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:修改外来文档失败").toJSon();
		}
	}

	/**
	 * 上传外来文档附件
	 * 
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/foreignDocStoreFileUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public String leadSpeakStoreFileUpload(@RequestParam("number") String number, HttpServletRequest request,
			HttpServletResponse response) {
		// tempForeignDocStoreFileUpLoad
		String path = request.getSession().getServletContext()
				.getRealPath(File.separator + "tempForeignDocStoreFileUpLoad" + number);
		// 如果文件夹不存在则创建文件夹
		File fileT = new File(path);
		if (!fileT.exists()) {
			fileT.mkdirs();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String fileName = "";
		try {
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem fileItem : list) {
				// 不是表单标签
				if (!fileItem.isFormField()) {
					fileName = new UpLoadUtil().getTpTime() + "_" + fileItem.getName();
					File file = new File(path + File.separator + fileName);
					fileItem.write(file);
					fileItem.delete();
				}
			}

		} catch (Exception e) {
			String msg = "文件上传失败";
			logger.error(msg + e);
			e.printStackTrace();
			return new ViewObject(-1, msg).toJSon();
		}
		return new ViewObject(ViewObject.RET_SUCCEED, fileName).toJSon();
	}

	/**
	 * 获得附件
	 * 
	 */
	@RequestMapping(value = "/getForeignDocStoreTempFile.do", method = RequestMethod.GET)
	@ResponseBody
	public String findExamineTempFile(HttpServletRequest request, @RequestParam("number") String number,
			@RequestParam("appendix") String appendix, HttpServletResponse response) {
		// 获得正在上传的文件
		List<String> tempList = findExamineTempFile(number, request, response);
		try {
			if (appendix != null && appendix.length() > 1) {
				// 修改附件
				// 先把原来的列举出来
				appendix = URLDecoder.decode(appendix, "utf-8");

				String appendixPath = request.getServletContext().getRealPath(File.separator + outsideDocStore);
				String[] strings = appendix.split(",");
				for (String string : strings) {
					int index = string.lastIndexOf(File.separator);
					string = string.substring(index + 1, string.length());
					String fileName = appendixPath + File.separator + string;
					File file = new File(fileName);
					if (file.exists()) {
						// 存在附件
						tempList.add(string);
					}
				}
			}
			return examineTempvoToJSon(tempList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String examineTempvoToJSon(List<String> examineList) throws Exception {
		JSONObject root = new JSONObject();
		JSONArray array = new JSONArray();
		// 读取配置文件里的属性

		for (String s : examineList) {
			JSONObject obj = new JSONObject();
			obj.put("filePath", s);

			if (s.contains("tempForeignDocStoreFileUpLoad_")) {
				// 代表临时上传的文件
				int index = s.lastIndexOf("_");
				s = s.substring(index + 1, s.length());
			}

			obj.put("fileName", s);
			obj.put("fileTempName", outsideDocStore + File.separator + s);
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}

	/**
	 * 获取当前正在上传的临时文件名称
	 * 
	 * @param number
	 * @param request
	 * @param response
	 */
	public static List<String> findExamineTempFile(String number, HttpServletRequest request,
			HttpServletResponse response) {
		// 返回所有临时文件的名称
		List<String> list = new ArrayList<String>();
		File tempFile[] = null;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		ServletContext serc = request.getSession().getServletContext();
		String tpath = serc.getRealPath(File.separator + "tempForeignDocStoreFileUpLoad" + number);
		try {
			File tempFiles = new File(tpath);// 临时文件夹
			if (tempFiles.exists()) {
				tempFile = tempFiles.listFiles();
				for (File file : tempFile) {
					// 临时文件
					list.add("tempForeignDocStoreFileUpLoad_" + file.getName());
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fi);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(fo);
			IOUtils.closeQuietly(out);
		}
		return null;
	}

	/**
	 * 删除临时上传的文件
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/deleteForeignDocStoreFile.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteExamineFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("fileName") String fileName, @RequestParam("number") String number) {
		// 返回所有临时文件的名称

		File tempFile[] = null;
		// System.out.println("====fileName===="+fileName);
		ServletContext serc = request.getSession().getServletContext();
		String tpath = serc.getRealPath(File.separator + "tempForeignDocStoreFileUpLoad" + number);
		try {
			File tempFiles = new File(tpath);// 临时文件夹
			if (tempFiles.exists()) {
				tempFile = tempFiles.listFiles();
				for (File file : tempFile) {
					if (file.getName().equals(fileName)) {
						file.delete();
						break;
					}
				}
			}

			return new ViewObject(ViewObject.RET_SUCCEED, "删除成功").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到当前上传附件的路径 从临时文件夹复制到最终存放的目录
	 * 
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	private String foreignDocStoreFilePath(String number, HttpServletRequest request, HttpServletResponse response) {
		File tempFile[] = null;
		ServletContext serc = request.getSession().getServletContext();
		// 获得临时上传附件的路径
		String tpath = serc.getRealPath(File.separator + "tempForeignDocStoreFileUpLoad" + number);
		// 附件存放的文件夹名称
		// 读取配置文件里的属性
		String lpath = serc.getRealPath(File.separator + outsideDocStore);
		File fileT = new File(lpath);
		if (!fileT.exists()) {
			// 不存在就创建
			fileT.mkdirs();
		}
		String s = "";
		List<String> list = new ArrayList<String>();
		File tempFiles = new File(tpath);
		try {
			if (tempFiles.exists()) {
				tempFile = tempFiles.listFiles();
				for (File file : tempFile) {
					// 复制文件
					FileUtils.copyFileToDirectory(file, fileT);
					list.add(outsideDocStore + File.separator + file.getName());
				}
				s = list.toString();
				s = s.replace("[", "");
				s = s.replace("]", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 删掉文件夹
				FileUtils.deleteDirectory(new File(tpath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	/**
	 * 删除文件夹和文件夹里面内容
	 * 
	 * @param sPath
	 * @return
	 */
	public static boolean deleteDirectory(String sPath) {
		boolean flag = false;
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 获得外来文档的附件
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getForeignDocAttachRealPath.do", method = RequestMethod.GET)
	@ResponseBody
	public String personAttachRealPath(@RequestParam(value = "id") String id, HttpServletRequest request) {
		try {
			// logger.info("==getPersonPhotoRealPath的id===="+id);
			OutsideDocStore store = outsideDocStoreService.findOutsideDocStoreById(id);
			// 附件
			String attach = store.getAttaches();
			return getAttachRealPath(attach);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据数据库值获得附件的名称
	 * 
	 * @param data
	 * @return
	 */
	public String getAttachRealPath(String data) {
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		if (data != null && data.trim().length() > 0) {
			// ServletContext context=request.getSession().getServletContext();
			String[] strings = data.split(",");
			for (String str : strings) {
				if (str.trim().length() > 1) {
					int index = str.lastIndexOf(File.separator);
					str = str.substring(index + 1, str.length());
					index = str.lastIndexOf("_");
					String name = str.substring(index + 1, str.length());
					// System.out.println("文件路径=="+path);
					object.put("src", str);
					object.put("name", name);
					array.add(object);
				}
			}
		}
		return array.toString();
	}

	/**
	 * 下载附件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downForeignDocAttach.do")
	public String downloadPersonAttach(@RequestParam("src") String src, HttpServletRequest request,
			HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		try {
			response.setContentType("multipart/form-data");
			String fileName = request.getSession().getServletContext()
					.getRealPath(File.separator + outsideDocStore + File.separator + URLDecoder.decode(src, "utf-8"));
			logger.info("下载的附件解码src：" + URLDecoder.decode(src, "utf-8"));
			logger.info("下载的附件fileName：" + fileName);
			File file = new File(fileName);
			if (file.exists()) {
				// int index=src.lastIndexOf("_");
				// 设置文件名
				// 名称直接用src就行
				response.setHeader("Content-Disposition", "attachment;fileName=" + src);
				is = new FileInputStream(new File(fileName));
				os = response.getOutputStream();

				byte[] b = new byte[1024];
				int length;
				while ((length = is.read(b)) > 0) {
					os.write(b, 0, length);
				}
			} else {
				logger.info("下载的附件不存在");
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// 关闭流
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
	}
	
	
	
	/**
	 * 上传单个文档并识别
	 * 
	 * @param number
	 * @param lastModified
	 * @param docTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/uploadForeinDocAndIdentify.do", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFileAndIdentify(@RequestParam("number") String number,
			@RequestParam("lastModified") String lastModified, @RequestParam("docTypeId") String docTypeId,
			HttpServletRequest request, HttpServletResponse response) {
		// 识别文档结果
		ViewObject viewObject = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/");
			// 上传文件的完整路径
			path = path + "tempForeignDocStoreFileUpload" + number;
			// 类型名称
			String docType = infoTypeService.findInfoTypeById(docTypeId).getTypeName();
			// 如果文件夹不存在则创建文件夹
			File fileT = new File(path);
			if (!fileT.exists()) {
				fileT.mkdirs();
			}
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem fileItem : list) {
				// 不是表单标签
				if (!fileItem.isFormField()) {
					// 获得最后修改时间转化为日期格式
					lastModified = IdentifyDocUtils.getLastModified(lastModified);
					logger.info("最后修改时间lastModified："+lastModified);
					String fileName = lastModified + fileItem.getName();
					String filePath = path + File.separator + fileName;
					File file = new File(filePath);
					if (file.exists()) {
						// 上传过了
						viewObject = new ViewObject(-1, "文件已上传");
					} else {
						String result = IdentifyDocUtils.identifyForeignDoc(filePath, docType);
						viewObject = new ViewObject(1, result);
						fileItem.write(file);
					}
					fileItem.delete();
				}
			}
			return viewObject.toJSon();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return new ViewObject(-1, "文件上传失败").toJSon();
		}
	}

	/**
	 * 删除临时上传的文件
	 * 
	 * @param request
	 * @param response
	 * @param filePath
	 *            文件全路径
	 * @return
	 */
	@RequestMapping(value = "/deleteTempForeignDoc.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteTempForeignDoc(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "filePath", required = false) String filePath) {
		try {
			logger.info("删除临时文件路径：" + filePath);
			File tempFile = new File(filePath);
			if (tempFile.exists()) {
				FileUtils.forceDelete(new File(filePath));
			}
			return new ViewObject(ViewObject.RET_SUCCEED, "删除成功").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_ERROR, "删除失败").toJSon();
		}
	}

	/**
	 * 查看文档的原文内容
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getForeignTheOriginal.do", method = RequestMethod.GET)
	@ResponseBody
	public String getTheOriginal(@RequestParam("id") String id, HttpServletRequest request) {
		String theOriginal = new String();
		String htmlName = null;
		try {
			OutsideDocStore store = outsideDocStoreService.findOutsideDocStoreById(id);
			if (null != store) {
				String value = store.getCentent();
				if (!StringUtils.isEmpty(value)) {
					// 原文内容不是空
					theOriginal = new ViewObject(ViewObject.RET_SUCCEED, value).toJSon();
				} else {
					// 读取附件内容
					String[] attaches = store.getAttaches().split(",");
					String fileName = store.getFileName();
					// 程序根路径
					String root = request.getServletContext().getRealPath("/");
					for (String string : attaches) {
						string = string.trim();
						if (string.contains(File.separator)) {
							int index = string.indexOf(File.separator);
							String tempFileName = string.substring(index + 9);
							if (tempFileName.equals(fileName)) {
								String inputPath = root + string;
								logger.info("附件全路径：" + inputPath);
								htmlName = root + System.currentTimeMillis() + ".html";
								// 转html
								WordToHtmlUtil.wordToHtml(inputPath, htmlName);
								logger.info("word转html：" + htmlName);
								StringBuffer buffer = new StringBuffer();
								File htmlFile = new File(htmlName);
								if (htmlFile.exists()) {
									buffer.append(FileUtils.readFileToString(new File(htmlName), Charset.forName("utf-8")));
								}
								theOriginal = new ViewObject(ViewObject.RET_SUCCEED, buffer.toString()).toJSon();
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			theOriginal = new ViewObject(ViewObject.RET_SUCCEED, "读取原文内容失败").toJSon();
			logger.error("读取原文内容失败");
		} finally {
			try {
				FileUtils.forceDelete(new File(htmlName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("原文内容：" + theOriginal);
		return theOriginal;
	}
}
