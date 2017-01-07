package com.ushine.storeInfo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tdcq.common.logging.LogFactory;
import com.tdcq.common.logging.LogInfo;
import com.ushine.common.config.Configured;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IVocationalWorkStoreService;
import com.ushine.storeInfo.storeFinal.StoreFinal;
import com.ushine.util.IdentifyDocUtils;
import com.ushine.util.SmbFileUtils;
import com.ushine.util.StringUtil;
import com.ushine.util.UpLoadUtil;
import com.ushine.util.WordToHtmlUtil;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 业务文档的控制器
 * 
 * @author dh
 *
 */
@Controller
public class VocationalWorkStoreController {
	private static final Logger logger = LoggerFactory.getLogger(VocationalWorkStoreController.class);
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IVocationalWorkStoreService vocationalWorkStoreService;

	/**
	 * 获得所有业务文档的类型
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getvocationalworkstoretype.do", method = RequestMethod.GET)
	@ResponseBody
	public String getVocationalWorkStoreType() {
		try {
			return infoTypeService.getInfoTypeByTableTypeName(StoreFinal.VOCATIONALWORK_STORE);
		} catch (Exception e) {
			logger.error("异常信息:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得所有涉及领域信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getInvolvedInTheFieldType.do", method = RequestMethod.GET)
	@ResponseBody
	public String getInvolvedInTheFieldType() {
		try {
			return infoTypeService.getInfoTypeByTableTypeName(StoreFinal.INVOLVED_IN_THE_FIELD);
		} catch (Exception e) {
			logger.error("异常信息:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询业务文档库信息，多条件
	 * 
	 * @return
	 */
	/// findVocationalWorkStoreByConditions.do
	@RequestMapping(value = "/findVocationalWorkStoreByConditions.do", method = RequestMethod.GET)
	@ResponseBody
	public String findVocationalWorkStoreByConditions(
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, @RequestParam("page") int nextPage,
			@RequestParam("limit") int size,
			// 排序字段
			@RequestParam(value = "sort", required = false) String sortField,
			// 升序或降序
			@RequestParam(value = "dir", required = false) String dir, HttpServletRequest request) {
		logger.info("查询业务文档库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询业务文档库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			logger.info(String.format("业务文档的排序字段：%s", sortField));
			// 汉字关键字搜索
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
					return vocationalWorkStoreService.findVocationalWorkStore(field, fieldValue, startTime, endTime,
							nextPage, size, null, null, null, sortField, dir);
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					return vocationalWorkStoreService.findVocationalWorkStore(field, fieldValue, startTime, endTime,
							nextPage, size, null, sessionMgr.getUOID(request), null, sortField, dir);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					return vocationalWorkStoreService.findVocationalWorkStore(field, fieldValue, startTime, endTime,
							nextPage, size, null, null, sessionMgr.getUDID(request), sortField, dir);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					return vocationalWorkStoreService.findVocationalWorkStore(field, fieldValue, startTime, endTime,
							nextPage, size, sessionMgr.getUID(request), null, null, sortField, dir);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询业务文档库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 根据传来的id数组删除文档 只是把action属性值设为3
	 * 
	 * @param serviceDocStoreIds
	 * @return
	 */
	@RequestMapping(value = "/delVocationalWorkStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String delVocationalWorkStore(@RequestParam("serviceDocStoreIds") String[] serviceDocStoreIds,
			HttpServletRequest request) {
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("删除业务文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("删除业务文档成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增人员库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					boolean result = vocationalWorkStoreService.delVocationalWorkStoreByIds(serviceDocStoreIds);
					logger.debug("删除多个业务文档结果：" + result);
					return new ViewObject(ViewObject.RET_FAILURE, "删除业务文档成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "删除失败，没有权限").toJSon();
		} catch (Exception e) {
			logger.error("删除业务文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:删除业务文档失败").toJSon();
		}
	}

	/**
	 * springmvc关于上传多个文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/uploadmuiltifiles.do", method = RequestMethod.POST)
	@ResponseBody
	public String uploadMuiltiFiles(@RequestParam(value = "data", required = false) String data,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "map", required = false) String map,
			// lastModified,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			// 转换成多部分request
			if (multipartResolver.isMultipart(request)) {
				// 传来的参数
				JSONArray array = JSONArray.fromObject(map);
				List li = (List) JSONSerializer.toJava(array);
				for (Object object : li) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 获得实例
					MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
					String date = DateFormatUtils.format(Long.parseLong(bean.get("lastModified").toString()),
							"yyyy-MM-dd");
					System.err.println(bean.get("name") + "---" + date);
				}

				MultipartHttpServletRequest multiRequest = multipartResolver.resolveMultipart(request);
				List<MultipartFile> attachs = multiRequest.getFiles("muiltifileupload");
				for (MultipartFile file : attachs) {
					System.err.println(file.getOriginalFilename());
					// 定义上传路径
					String path = "f:/demoUpload/" + file.getOriginalFilename();
					File localFile = new File(path);
					file.transferTo(localFile);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ViewObject(ViewObject.RET_FAILURE, "异常:删除业务文档失败").toJSon();
	}

	/**
	 * 判断是否存在文档
	 * 
	 * @param docName
	 * @return
	 */
	@RequestMapping(value = "/hasVocationalStoreByDocName.do", method = RequestMethod.POST)
	@ResponseBody
	public String hasVocationalStoreByDocName(@RequestParam("docName") String docName) {
		// 默认不存在
		logger.info("查询文档：" + docName + "是否存在");
		String msg = "not_exist";
		ViewObject viewObject = null;
		try {
			if (vocationalWorkStoreService.hasStoreByDocName(docName)) {
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
	 * 新增业务文档<br>
	 * 可以新增多个文档
	 * 
	 * @return
	 */
	@RequestMapping(value = "/saveVocationalWorkStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String saveVocationalWorkStore(@RequestParam(value = "docName", required = false) String docName,
			@RequestParam(value = "docNumber", required = false) String docNumber,
			@RequestParam(value = "infoType", required = false) String infoType,
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "theOriginal", required = false) String theOriginal,
			@RequestParam(value = "involvedInTheField", required = false) String involvedInTheField,
			@RequestParam(value = "number", required = false) String number,
			// 共享文件夹新增多个业务文档参数
			@RequestParam(value = "saveMultiDoc", required = false) String saveMultiDoc,
			// 上传多个文件新增多个业务文档参数
			@RequestParam(value = "uploadAndSaveMultiDoc", required = false) String uploadAndSaveMultiDoc,
			// 多个文档
			@RequestParam(value = "datas", required = false) String datas,
			@RequestParam(value = "uploadNumber", required = false) String uploadNumber, HttpServletRequest request,
			HttpServletResponse response) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("新增业务文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("新增业务文档成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);

		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增信息
			if (list != null && list.size() > 0) {
				// 启用
				String msg = "新增业务文档成功";
				if ("0x0001".equals(list.get(0))) {
					if (StringUtils.equals(saveMultiDoc, "yes")) {
						// 代表根据共享文件夹路径新增多个
						msg = saveMultiVocationalWorkStore(datas, infoType, request, userMgr, uploadNumber);
					} else if (StringUtils.equals(uploadAndSaveMultiDoc, "yes")) {
						// 代表上传多个文档后新增
						msg = saveMultiVocationalWorkStore(datas, request, userMgr);
					} else {
						VocationalWorkStore store = new VocationalWorkStore();
						// 设置权限
						store.setUid(userMgr.getUID(request));
						store.setDid(userMgr.getUDID(request));
						store.setOid(userMgr.getUOID(request));
						// 新增为1
						store.setAction("1");
						store.setUid(userMgr.getUID(request));
						store.setDocName(docName);
						store.setDocNumber(docNumber);
						store.setTime(time);
						store.setTheOriginal(theOriginal);
						store.setCreateDate(StringUtil.dates());
						store.setInvolvedInTheField(infoTypeService.findInfoTypeById(involvedInTheField));
						// 设置类别
						InfoType type = infoTypeService.findInfoTypeById(infoType);
						store.setInfoType(type);
						// 设置附件路径
						String attaches = serviceDocStoreFilePath(number, request, response);
						store.setAttaches(attaches);

						vocationalWorkStoreService.saveVocationalWork(store);
						// logger.info("store" + store);
					}
				}
				return new ViewObject(ViewObject.RET_FAILURE, msg).toJSon();
			}
			return new ViewObject(ViewObject.RET_FAILURE, "新增失败，没有权限").toJSon();
		} catch (Exception e) {
			logger.error("新增业务文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增业务文档失败").toJSon();
		}
	}

	/**
	 * 保存未入库的详细信息到txt中
	 * 
	 * @param result
	 * @param date
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/saveVocationalStoreDetail.do", method = RequestMethod.POST)
	@ResponseBody
	public String downloadSaveVocationalStoreDetail(@RequestParam("result") String result,
			@RequestParam("date") String date, HttpServletRequest request, HttpServletResponse response) {
		String root = request.getServletContext().getRealPath("/");
		// 文件名
		String path = root + date + ".txt";
		try {
			// 写入信息
			String[] results = result.split("<br>");
			StringBuffer buffer = new StringBuffer();
			for (String string : results) {
				buffer.append(StringUtils.substringBefore(string, "<br>")).append("\r\n");
			}
			FileUtils.writeStringToFile(new File(path), buffer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ViewObject(ViewObject.RET_SUCCEED, "success").toJSon();
	}

	/**
	 * 下载未保存的信息
	 * 
	 * @param date
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadSaveVocationalStoreDetail.do", method = RequestMethod.GET)
	@ResponseBody
	public String downloadSaveVocationalStoreDetail(@RequestParam("date") String date, HttpServletRequest request,
			HttpServletResponse response) {
		ServletOutputStream out = null;
		String root = request.getServletContext().getRealPath("/");
		// 文件名
		String path = root + date + ".txt";
		try {
			// 下载附件
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头
			response.setHeader("Content-Disposition", "attachment;fileName=" + date + ".txt");
			// 要下载的文件地址
			out = response.getOutputStream();
			// 使用IOUtils
			File file = new File(path);
			IOUtils.write(FileUtils.readFileToByteArray(file), out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
			try {
				// 删除
				FileUtils.forceDelete(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ViewObject(ViewObject.RET_FAILURE, "下载失败,请联系管理员").toJSon();
	}

	/**
	 * 修改业务文档
	 * 
	 * @param docName
	 * @param docNumber
	 * @param infoType
	 * @param time
	 * @param theOriginal
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateVocationalWorkStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String updateVocationalWorkStore(@RequestParam("id") String id, @RequestParam("docName") String docName,
			@RequestParam("docNumber") String docNumber, @RequestParam("infoType") String infoType,
			@RequestParam("time") String time, @RequestParam("theOriginal") String theOriginal,
			@RequestParam("involvedInTheField") String involvedInTheField,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "attaches", required = false) String attaches, HttpServletResponse response,
			HttpServletRequest request) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("修改业务文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("修改业务文档成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);

		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限修改
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					// 先获得对象
					VocationalWorkStore store = vocationalWorkStoreService.findVocationalWorkById(id);
					// 设置权限
					store.setUid(userMgr.getUID(request));
					store.setDid(userMgr.getUDID(request));
					store.setOid(userMgr.getUOID(request));
					// 修改为2
					store.setAction("2");
					store.setUid(userMgr.getUID(request));
					store.setDocName(docName);
					store.setDocNumber(docNumber);
					store.setTime(time);
					store.setTheOriginal(theOriginal);
					store.setInvolvedInTheField(infoTypeService.findInfoTypeByTypeNameAndTableName(involvedInTheField,
							StoreFinal.INVOLVED_IN_THE_FIELD));
					// 设置类别
					InfoType type = infoTypeService.findInfoTypeByTypeNameAndTableName(infoType,
							StoreFinal.VOCATIONALWORK_STORE);

					// InfoType type=infoTypeService.findInfoTypeById(infoType);
					store.setInfoType(type);
					// 重新设置附件
					StringBuffer buffer = new StringBuffer();
					String seviceDocStore = Configured.getInstance().get("vocationalWorkStore");
					if (attaches != null && attaches.trim().length() > 1) {
						String[] files = attaches.split(",");
						for (String file : files) {
							// 临时上传的
							if (file.contains("tempServiceDocStoreFileUpload_")) {
								String leadStoreFile = serviceDocStoreFilePath(number, request, response);
								// 重新上传文件后
								if (leadStoreFile != null && leadStoreFile.length() > 1) {
									buffer.append(leadStoreFile);
									buffer.append(",");
								}
							} else {
								// 这是原来的附件
								buffer.append(seviceDocStore + File.separator + file);
								buffer.append(",");
							}
						}
					}
					String filePath = buffer.toString().trim();
					// filePath = filePath.replace(" ", "");
					if (filePath != null && filePath.length() > 0) {
						// 去掉,号
						filePath = filePath.substring(0, filePath.length() - 1);
					}
					store.setAttaches(filePath);

					vocationalWorkStoreService.updateVocationalWork(store);
					return new ViewObject(ViewObject.RET_FAILURE, "修改业务文档成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改业务文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:修改业务文档失败").toJSon();
		}
	}

	/**
	 * 新增多个业务文档
	 * 
	 * @return
	 */
	public String saveMultiVocationalWorkStore(@RequestParam("datas") String datas, String infoType,
			HttpServletRequest request, UserSessionMgr userMgr, String uploadNumber) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("新增多个业务文档");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("新增多个业务文档");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			String result = vocationalWorkStoreService.saveVocationalWork(datas, infoType, request, userMgr,
					uploadNumber);
			return new ViewObject(ViewObject.RET_FAILURE, result).toJSon();
		} catch (Exception e) {
			logger.error("新增业务文档失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增业务文档失败").toJSon();
		} finally {
			// 删掉转成的html文档
			try {
				String htmlPath = request.getServletContext()
						.getRealPath(File.separator + "tempWordToHtml" + uploadNumber);
				File file = new File(htmlPath);
				if (file.isDirectory()) {
					FileUtils.deleteDirectory(file);
				}
				// 删除上传的文件夹
				// String temp = "tempVocationalWorkStoreUpload" + uploadNumber;
				// String
				// targetFolder=request.getServletContext().getRealPath(temp);
				// FileUtils.deleteDirectory(new File(targetFolder));
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	public String saveMultiVocationalWorkStore(@RequestParam("datas") String datas, HttpServletRequest request,
			UserSessionMgr userMgr) {
		// System.err.println("datas："+datas);
		try {
			String result = vocationalWorkStoreService.saveVocationalWork(datas, request, userMgr);
			logger.info("result：" + result);
			return new ViewObject(ViewObject.RET_SUCCEED, result).toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增业务文档失败").toJSon();
		}
	}

	@RequestMapping(value = "/getrootfoldername.do", method = RequestMethod.GET)
	@ResponseBody
	public String getRootFolderName(@RequestParam("ip") String ip) {
		String result = new String();
		try {
			SmbFileUtils utils = new SmbFileUtils(ip);
			result = utils.getRootFolderName();
		} catch (Exception e) {
			logger.error("获取文件夹名称失败");
		}
		return result;
	}

	/**
	 * 导入文档然后识别并保存到数据库并做lucene索引类
	 * 
	 * @author Administrator
	 *
	 */
	private class ImportServiceDoc implements Callable<String> {
		private String url;
		private ExecutorService importService;
		private String targetFolder;
		private String typeName;

		/**
		 * 构造方法
		 * 
		 * @param url
		 *            共享文件路径
		 * @param importService
		 *            ExecutorService实例
		 * @param targetFolder
		 *            临时文件夹
		 * @param typeName
		 *            文档类别
		 */
		public ImportServiceDoc(String url, ExecutorService importService, String targetFolder, String typeName) {
			this.url = url;
			this.importService = importService;
			this.targetFolder = targetFolder;
			this.typeName = typeName;
		}

		@Override
		public String call() throws Exception {
			return vocationalWorkStoreService.identifyAndSaveServiceDoc(url, importService, targetFolder, typeName);
		}
	}

	private ExecutorService service = null;
	private Future<String> future = null;

	/**
	 * 将远程文档拷贝到本地
	 * 
	 * @param ip
	 * @return
	 */
	@RequestMapping(value = "/importDoc.do", method = RequestMethod.POST)
	@ResponseBody
	public String importDoc(@RequestParam("ip") String ip, @RequestParam("uploadNumber") String uploadNumber,
			@RequestParam("docType") String docTypeId, HttpServletRequest request) {
		// 替换反斜杠
		String url = ip.replaceAll("\\\\", "/");
		try {
			if (null != service && !service.isShutdown()) {
				// 只允许执行一个
				return new ViewObject(ViewObject.RET_SUCCEED, "isdoing").toJSon();
			} else {
				service = Executors.newSingleThreadExecutor();
				String temp = "tempVocationalWorkStoreUpload" + uploadNumber;
				// 临时文件夹
				String targetFolder = request.getServletContext().getRealPath(temp);
				// 导入的类别
				String typeName = infoTypeService.findInfoTypeById(docTypeId).getTypeName();
				// 带有返回值
				future = service.submit(new ImportServiceDoc(url, service, targetFolder, typeName));
				return new ViewObject(ViewObject.RET_SUCCEED, "success").toJSon();
			}

		} catch (Exception e) {
			logger.error("后台导入异常");
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_ERROR, "exception").toJSon();
		}
	}

	/**
	 * 监测是否完成导入任务
	 * 
	 * @return
	 */
	@RequestMapping(value = "/monitorService.do", method = RequestMethod.POST)
	@ResponseBody
	public String monitorService() {
		try {
			if (null != service) {
				String result = new ViewObject(ViewObject.RET_DOING, "doing").toJSon();
				// 任务是否已停止
				return service.isShutdown() ? future.get() : result;
			}
		} catch (Exception e) {
			logger.error("后台导入异常");
			e.printStackTrace();
		}
		return new ViewObject(ViewObject.RET_ERROR, "exception").toJSon();
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
	@RequestMapping(value = "/identifyVocationalWorkStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String identifyVocationalWorkStore(@RequestParam("uploadNumber") String uploadNumber,
			@RequestParam("ip") String ip, @RequestParam(value = "folderName", required = false) String folderName,
			@RequestParam("docType") String docTypeId, HttpServletRequest request) {
		String result = null;
		String targetFolder = null;
		try {
			String typeName = infoTypeService.findInfoTypeById(docTypeId).getTypeName();
			logger.info("业务文档类型:" + typeName);
			logger.info("共享机器ip:" + ip);
			// logger.info("识别文件夹名称:" + folderName);
			String temp = "tempVocationalWorkStoreUpload" + uploadNumber;
			targetFolder = request.getServletContext().getRealPath(temp);
			// 复制共享文件到targetFoler下
			logger.info("复制到目标文件夹下:" + targetFolder);
			SmbFileUtils utils = new SmbFileUtils();
			// utils.copySmbFilesToDir(folderName, targetFolder);
			// 替换反斜杠
			ip = ip.replaceAll("\\\\", "/");
			utils.copySmbWordToDir(ip, targetFolder);
			// 识别文档
			String msg = vocationalWorkStoreService.identifyServiceDoc(targetFolder, typeName);
			result = new ViewObject(ViewObject.RET_SUCCEED, msg).toJSon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 查看文档的原文内容
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTheOriginal.do", method = RequestMethod.GET)
	@ResponseBody
	public String getTheOriginal(@RequestParam("id") String id, HttpServletRequest request) {
		String theOriginal = new String();
		BufferedReader br = null;
		String htmlName = null;
		try {
			VocationalWorkStore store = vocationalWorkStoreService.findVocationalWorkById(id);
			if (null != store) {
				String value = store.getTheOriginal();
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
									buffer.append(FileUtils.readFileToString(new File(htmlName), 
											Charset.forName("utf-8")));
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
			logger.error("读取原文内容失败");
		} finally {
			try {
				// 删除html
				FileUtils.forceDelete(new File(htmlName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("原文内容：" + theOriginal);
		return theOriginal;
	}

	/**
	 * 读取word里面的内容<br>
	 * 先将word转成html，然后读取html内容
	 * 
	 * @param filePath
	 * @return
	 */
	@RequestMapping(value = "/readWord.do", method = RequestMethod.POST)
	@ResponseBody
	public String readWord(@RequestParam("filePath") String filePath, String uploadNumber, HttpServletRequest request) {
		String content = new ViewObject(ViewObject.RET_FAILURE, "读取内容失败").toJSon();
		// 读
		BufferedReader br = null;
		try {
			String htmlPath = request.getServletContext().getRealPath(File.separator + "tempWordToHtml" + uploadNumber);
			String htmlName = "";
			StringBuilder buffer = new StringBuilder();
			File file = new File(htmlPath);
			// 创建临时存放的html的文件夹
			if (!file.exists()) {
				file.mkdir();
			}
			int index = -1;
			index = filePath.lastIndexOf(file.separator);
			if (index > 0) {
				// 文件名
				htmlName = filePath.substring(index + 1);
				// word转html
				htmlName = htmlPath + file.separator + htmlName + ".html";
				WordToHtmlUtil.wordToHtml(filePath, htmlName);
				logger.info("word转成的html文件" + htmlName);
				// 读取html里面内容
				/**** 指定读取的编码为utf-8 ****/
				br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlName), Charset.forName("utf-8")));
				int count = 0;
				char[] datas = new char[1024];
				while ((count = br.read(datas)) > 0) {
					/**** 不要用readLine ****/
					// buffer.append(br.readLine());
					String str = String.valueOf(datas, 0, count);
					buffer.append(str);
				}
				// logger.info("读取的html文件内容"+buffer.toString());
				content = new ViewObject(ViewObject.RET_SUCCEED, buffer.toString()).toJSon();
			}
			// System.out.println(htmlPath);
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			// 关闭读流
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	/**
	 * 上传业务文档附件
	 * 
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/serviceDocStoreFileUpload.do", method = RequestMethod.POST)
	@ResponseBody
	public String serviceDocFileUpload(@RequestParam("number") String number, HttpServletRequest request,
			HttpServletResponse response) {
		// tempLeadSpeakStoreFileUpload
		String path = request.getSession().getServletContext()
				.getRealPath(File.separator + "tempSerivceDocStoreFileUpload" + number);
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
	 * 上传单个文档并识别
	 * 
	 * @param number
	 * @param lastModified
	 * @param docTypeId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/uploadFileAndIdentify.do", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFileAndIdentify(@RequestParam("number") String number,
			@RequestParam("lastModified") String lastModified, @RequestParam("docTypeId") String docTypeId,
			HttpServletRequest request, HttpServletResponse response) {
		// 识别文档结果
		ViewObject viewObject = null;
		try {
			String path = request.getSession().getServletContext().getRealPath("/");
			// 上传文件的完整路径
			path = path + "tempSerivceDocStoreFileUpload" + number;
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
					logger.info("最后修改时间lastModified：" + lastModified);
					String fileName = lastModified + fileItem.getName();
					String filePath = path + File.separator + fileName;
					File file = new File(filePath);
					if (file.exists()) {
						// 上传过了
						viewObject = new ViewObject(-1, "文件已上传");
					} else {
						String result = IdentifyDocUtils.identifyServiceDoc(filePath, docType);
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

	// 读取配置资源
	String serviceDocStore = Configured.getInstance().get("vocationalWorkStore");

	/**
	 * 获得业务文档附件路径
	 * 
	 * @param request
	 * @param number
	 * @param appendix
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getSericeDocStoreTempFile.do", method = RequestMethod.GET)
	@ResponseBody
	public String findExamineTempFile(HttpServletRequest request, @RequestParam("number") String number,
			@RequestParam("appendix") String appendix, HttpServletResponse response) {
		// 获得正在上传的文件
		List<String> tempList = findExamineTempFile(number, request, response);
		try {
			// appendix != null && appendix.length() > 1
			if (StringUtils.length(appendix) > 0) {
				// 修改附件
				// 先把原来的列举出来
				appendix = URLDecoder.decode(appendix, "utf-8");

				String appendixPath = request.getServletContext().getRealPath(File.separator + serviceDocStore);
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

			if (s.contains("tempServiceDocStoreFileUpload_")) {
				// 代表临时上传的文件
				int index = s.lastIndexOf("_");
				s = s.substring(index + 1, s.length());
			}

			obj.put("fileName", s);
			obj.put("fileTempName", serviceDocStore + File.separator + s);
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
		ServletContext serc = request.getSession().getServletContext();
		String tpath = serc.getRealPath(File.separator + "tempSerivceDocStoreFileUpload" + number);

		try {
			File tempFiles = new File(tpath);// 临时文件夹
			if (tempFiles.exists()) {
				tempFile = tempFiles.listFiles();
				for (File file : tempFile) {
					// 临时文件
					list.add("tempServiceDocStoreFileUpload_" + file.getName());
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
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
	@RequestMapping(value = "/deleteServiceDocStoreFile.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteExamineFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("fileName") String fileName, @RequestParam("number") String number) {
		// 返回所有临时文件的名称
		File tempFile[] = null;
		ServletContext serc = request.getSession().getServletContext();
		String tpath = serc.getRealPath(File.separator + "tempSerivceDocStoreFileUpload" + number);
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
			return new ViewObject(ViewObject.RET_ERROR, "删除失败").toJSon();
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
	@RequestMapping(value = "/deleteTempServiceDoc.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteExamineFile(HttpServletRequest request, HttpServletResponse response,
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
	 * 得到当前上传附件的路径 从临时文件夹复制到最终存放的目录
	 * 
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	public String serviceDocStoreFilePath(String number, HttpServletRequest request, HttpServletResponse response) {
		File tempFile[] = null;
		ServletContext serc = request.getSession().getServletContext();
		// 获得临时上传附件的路径
		String tpath = serc.getRealPath(File.separator + "tempSerivceDocStoreFileUpload" + number);
		String lpath = serc.getRealPath(File.separator + serviceDocStore);
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
					/*
					 * fi = new FileInputStream(file); fo = new
					 * FileOutputStream(lpath + File.separator+ file.getName());
					 * in = fi.getChannel(); out = fo.getChannel(); //
					 * 连接两个通道,从in中读取,然后写入out in.transferTo(0, in.size(), out);
					 */
					FileUtils.copyFileToDirectory(file, fileT);
					list.add(serviceDocStore + File.separator + file.getName());
				}
				s = list.toString();
				s = s.replace("[", "");
				s = s.replace("]", "");
			}
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流操作
				FileUtils.deleteDirectory(new File(tpath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获得附件
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSerciceDocAttachRealPath.do", method = RequestMethod.GET)
	@ResponseBody
	public String personAttachRealPath(@RequestParam(value = "id") String id, HttpServletRequest request) {
		try {
			VocationalWorkStore store = vocationalWorkStoreService.findVocationalWorkById(id);
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
	@RequestMapping("/downloadServiceDocAttach.do")
	public String downloadPersonAttach(@RequestParam("src") String src, HttpServletRequest request,
			HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		try {
			response.setContentType("multipart/form-data");
			String fileName = request.getSession().getServletContext()
					.getRealPath(File.separator + serviceDocStore + File.separator + URLDecoder.decode(src, "utf-8"));
			logger.info("下载的附件解码src=" + URLDecoder.decode(src, "utf-8"));
			logger.info("下载的附件fileName=" + fileName);
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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
		return null;
	}

	@RequestMapping(value = "/identifyandsave.do", method = RequestMethod.POST)
	@ResponseBody
	public String identifyAndSave(@RequestParam("uploadNumber") String uploadNumber, @RequestParam("ip") String ip,
			@RequestParam(value = "folderName", required = false) String folderName,
			@RequestParam("docType") String docTypeId, HttpServletRequest request) {
		String result = null;
		String targetFolder = null;
		try {
			// ip = ip.replaceAll("\\\\", "/");
			String typeName = infoTypeService.findInfoTypeById(docTypeId).getTypeName();
			logger.info("业务文档类型:" + typeName);
			logger.info("共享机器ip:" + ip);
			// logger.info("识别文件夹名称:" + folderName);
			String temp = "tempVocationalWorkStoreUpload" + uploadNumber;
			targetFolder = request.getServletContext().getRealPath(temp);
			vocationalWorkStoreService.identifyAndSave(ip, targetFolder, typeName);

			result = new ViewObject(ViewObject.RET_SUCCEED, "success").toJSon();
		} catch (Exception e) {
			result = new ViewObject(ViewObject.RET_ERROR, "exception").toJSon();
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "/monitortask.do", method = RequestMethod.GET)
	@ResponseBody
	public String monitorTask() throws Exception {
		return vocationalWorkStoreService.monitorExecutorService();
	}
}
