package com.ushine.storeInfo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
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
import com.ushine.storeInfo.model.LeadSpeakStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.ILeadSpeakStoreService;
import com.ushine.storeInfo.storefinal.StoreFinal;
import com.ushine.util.ReadAttachUtil;
import com.ushine.util.StringUtil;
import com.ushine.util.UpLoadUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 领导讲话控制器
 * @author wangbailin
 *
 */
@Controller
public class LeadSpeakStoreController {
	private Logger logger = LoggerFactory
			.getLogger(LeadSpeakStoreController.class);
	
	@Autowired private IInfoTypeService infoTypeService;
	@Autowired private ILeadSpeakStoreService leadSpeakStoreService;
	/**
	 * 获得领导讲话类型
	 * @return
	 */
	@RequestMapping(value="/getleadspeakstoretype.do",method=RequestMethod.GET)
	@ResponseBody
	public String getLeadSpeakStoreType(){
		try {
			return infoTypeService
					.getInfoTypeByTableTypeName(StoreFinal.LEADSPEAK_STORE);
		} catch (Exception e) {
			logger.error("异常信息:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询领导讲话信息，多条件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findLeadSpeakStore.do", method = RequestMethod.GET)
	@ResponseBody
	public String findLeadSpeakStoreByConditions(
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size, 
			//排序字段
			@RequestParam(value ="sort", required = false) String sortField,
			@RequestParam(value ="dir", required = false) String dir,
			HttpServletRequest request) {
		logger.info("查询领导讲话信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询领导讲话信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//ASC DESC
			logger.info(String.format("领导讲话库的排序字段：%s，排序方向：%s",sortField,dir));
			// 判断当前用户是否有权限查询人员库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {
					// 读取全部
					return leadSpeakStoreService.findLeadSpeakStore(field,
							fieldValue, startTime, endTime, nextPage, size,
							null, null, null,sortField,dir);
				} else if ("1x0010".equals(list.get(0))) {
					// 所属组织
					return leadSpeakStoreService.findLeadSpeakStore(field,
							fieldValue, startTime, endTime, nextPage, size,
							null, sessionMgr.getUOID(request), null,sortField,dir);
				} else if ("1x0011".equals(list.get(0))) {
					// 所属部门
					return leadSpeakStoreService.findLeadSpeakStore(field,
							fieldValue, startTime, endTime, nextPage, size,
							null, null, sessionMgr.getUDID(request),sortField,dir);
				} else if ("1x0100".equals(list.get(0))) {
					// 个人数据
					return leadSpeakStoreService.findLeadSpeakStore(field,
							fieldValue, startTime, endTime, nextPage, size,
							sessionMgr.getUID(request), null, null,sortField,dir);
				} else {
					// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取")
							.toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询领导讲话信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 依据会议名称判断是否存在文档
	 * @param title
	 * @return
	 */
	@RequestMapping(value = "/hasStoreByMeetingName.do", method = RequestMethod.POST)
	@ResponseBody
	public String hasStoreByMeetingName(@RequestParam("meetingName") String meetingName){
		logger.info("查询文档："+meetingName+"是否已经存在");
		String msg="not_exist";
		ViewObject viewObject=null;
		try {
			if(leadSpeakStoreService.hasStoreByMeetingName(meetingName)){
				//已经存在文档名称
				msg="exist";
			}
			viewObject=new ViewObject(ViewObject.RET_SUCCEED, msg);
		} catch (Exception e) {
			viewObject=new ViewObject(ViewObject.RET_ERROR, msg);
			e.printStackTrace();
		}
		return viewObject.toJSon();
	}
	/**
	 * 新增领导讲话
	 * @return
	 */
	@RequestMapping(value = "/saveLeadSpeakStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String saveVocationalWorkStore(
			@RequestParam(value = "meetingName", required = false) String meetingName,
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "infoType") String infoType,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "secretRank", required = false) String secretRank,
			@RequestParam(value = "centent", required = false) String centent,
			@RequestParam(value = "involvedInTheField", required = false) String involvedInTheField,
			@RequestParam(value = "number", required = false) String number,
			HttpServletRequest request,HttpServletResponse response
			) {
		//
		
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("新增领导讲话");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("新增领导讲话成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {// 启用
					LeadSpeakStore store = new LeadSpeakStore();
					//设置权限
					store.setUid(userMgr.getUID(request));
					store.setDid(userMgr.getUDID(request));
					store.setOid(userMgr.getUOID(request));
					// 新增为1
					store.setAction("1");
					// 用户id
					store.setUid(userMgr.getUID(request));
					store.setCentent(centent);
					store.setMeetingName(meetingName);
					store.setTime(time);
					store.setSecretRank(secretRank);
					store.setTitle(title);
					store.setInvolvedInTheField(infoTypeService.findInfoTypeById(involvedInTheField));
					store.setCreateDate(StringUtil.dates());
					// 设置类别
					InfoType type = infoTypeService.findInfoTypeById(infoType);
					store.setInfoType(type);
					//设置附件路径
					String attaches=leadSpeakStoreFilePath(number, request, response);
					store.setAttaches(attaches);
					//设置附件内容
					String root=Configured.getInstance().get("rootIndex");
					String []attachesNames=attaches.split(",");
					StringBuffer buffer=new StringBuffer();
					for (String string : attachesNames) {
						String fileName=root+File.separator+string.trim();
						File file=new File(fileName);
						buffer.append(ReadAttachUtil.readContent(fileName)+" ");
					}
					store.setAttachContent(buffer.toString());
					leadSpeakStoreService.saveLeadSpeakStore(store);
					return new ViewObject(ViewObject.RET_FAILURE, "新增领导讲话成功")
							.toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "新增失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增领导讲话失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:新增领导讲话失败")
					.toJSon();
		}
	}
	
	/**
	 * 删除
	 * 修改action为3
	 * @param ids
	 * @param request 
	 * @return
	 */
	@RequestMapping(value = "/delLeadSpeakStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String delOutsideDocStore(
			@RequestParam("ids") String[] ids,
			HttpServletRequest request) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo logInfo = new LogInfo();
		logInfo.setApplication("删除领导讲话");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("删除领导讲话成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			UserSessionMgr userMgr = UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			//是否有权限
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					leadSpeakStoreService.delLeadSpeakStore(ids);
					logger.debug("删除领导讲话id：" + Arrays.toString(ids));
					return new ViewObject(ViewObject.RET_FAILURE, "删除领导讲话成功")
							.toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "删除失败，没有权限").toJSon();
		} catch (Exception e) {
			logger.error("删除领导讲话失败" + e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:删除领导讲话失败")
					.toJSon();
		}
	}
	
	/**
	 * 修改领导讲话
	 * 
	 */
	@RequestMapping(value="/updateLeadSpeakStore.do",method=RequestMethod.POST)
	@ResponseBody
	public String updateOutsideDocStore(
			@RequestParam("id")String id,
			@RequestParam(value = "meetingName", required = false) String meetingName,
			@RequestParam(value = "time", required = false) String time,
			@RequestParam(value = "infoType") String infoType,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "secretRank", required = false) String secretRank,
			@RequestParam(value = "centent", required = false) String centent,
			@RequestParam(value = "involvedInTheField", required = false) String involvedInTheField,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "attaches", required = false) String attaches,
			HttpServletResponse response,
			HttpServletRequest request){
		com.tdcq.common.logging.Logger log=LogFactory.getLogger();
		LogInfo logInfo=new LogInfo();
		logInfo.setApplication("修改领导讲话");
		logInfo.setUri(request.getRequestURI());
		logInfo.setClientIP(request.getRemoteAddr());
		logInfo.setLogTime(new Date());
		logInfo.setResult("修改领导讲话成功");
		logInfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		/*System.err.println("=========="+meetingName);
		System.err.println("=========="+time);
		System.err.println("=========="+infoType);
		System.err.println("=========="+title);
		System.err.println("=========="+secretRank);
		System.err.println("=========="+centent);
		System.err.println("=========="+involvedInTheField);
		System.err.println("=========="+number);
		System.err.println("=========="+attaches);*/
		try {
			UserSessionMgr userMgr=UserSessionMgr.getInstance();
			logInfo.setUserName(userMgr.getTrueName(request));
			logInfo.setUserCode(userMgr.getCode(request));
			List<String> list = userMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限修改领导讲话信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					//先获得对象
					LeadSpeakStore store=leadSpeakStoreService.findLeadSpeakStoreById(id);
					//设置权限
					store.setUid(userMgr.getUID(request));
					store.setDid(userMgr.getUDID(request));
					store.setOid(userMgr.getUOID(request));
					//修改为2
					store.setAction("2");
					store.setCentent(centent);
					store.setMeetingName(meetingName);
					store.setSecretRank(secretRank);
					store.setTime(time);
					store.setTitle(title);
					store.setInvolvedInTheField(infoTypeService.findInfoTypeByTypeNameAndTableName(involvedInTheField, 
							StoreFinal.INVOLVED_IN_THE_FIELD));
					//设置类别
					InfoType type=infoTypeService.findInfoTypeByTypeNameAndTableName(infoType, 
							StoreFinal.LEADSPEAK_STORE);
					
					store.setInfoType(type);
					//重新设置附件
					StringBuffer buffer=new StringBuffer();
					String leadSpeakStore=Configured.getInstance().get("leadSpeakStore");
					if(attaches!=null&&attaches.trim().length()>1){
						String []files=attaches.split(",");
						for (String file : files) {
							//System.err.println("==========file=========="+file);
							//临时上传的
							if(file.contains("tempLeadSpeakStoreFileUpload_")){
								String leadStoreFile=leadSpeakStoreFilePath(number, request, response);
								//重新上传文件后
								if(leadStoreFile!=null&&leadStoreFile.length()>1){
									buffer.append(leadStoreFile);
									buffer.append(",");
								}
							}else{
								//这是原来的附件
								buffer.append(leadSpeakStore+File.separator+file);
								buffer.append(",");
							}
						}
					}
					String filePath=buffer.toString().trim();
					filePath=filePath.replace(" ", "");
					if(filePath!=null&&filePath.length()>0){
						//去掉,号
						filePath=filePath.substring(0,filePath.length()-1);
					}
					store.setAttaches(filePath);
					
					//设置附件内容
					String root=Configured.getInstance().get("rootIndex");
					String []attachesNames=filePath.split(",");
					StringBuffer content=new StringBuffer();
					for (String string : attachesNames) {
						String fileName=root+File.separator+string.trim();
						File file=new File(fileName);
						content.append(ReadAttachUtil.readContent(fileName)+" ");
					}
					store.setAttachContent(content.toString());
					leadSpeakStoreService.updateLeadSpeakStore(store);
					return new ViewObject(ViewObject.RET_FAILURE, "修改领导讲话成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改领导讲话失败"+e.getMessage());
			return new ViewObject(ViewObject.RET_FAILURE, "异常:修改领导讲话失败").toJSon();
		}
	}
	
	/**
	 * 下载附件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downloadLeadSpeakAttach.do")
	public String downloadPersonAttach(@RequestParam("src") String src,
			HttpServletRequest request, HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		try {
			response.setContentType("multipart/form-data");
			String leadSpeakStoreFile=Configured.getInstance().get("leadSpeakStore");
			String fileName = request.getSession().getServletContext().getRealPath(
				File.separator +leadSpeakStoreFile+File.separator+ URLDecoder.decode(src, "utf-8"));
   		 	logger.info("========下载的附件解码src========="+URLDecoder.decode(src,"utf-8"));
			logger.info("========下载的附件fileName=========" + fileName);
			File file=new File(fileName);
			if(file.exists()){
				//int index=src.lastIndexOf("_");
				//设置文件名
				//名称直接用src就行
				response.setHeader("Content-Disposition", "attachment;fileName="+ src);
				is = new FileInputStream(new File(fileName));
				os = response.getOutputStream();

				byte[] b = new byte[1024];
				int length;
				while ((length = is.read(b)) > 0) {
					os.write(b, 0, length);
				}
			}else{
				logger.info("========下载的附件不存在=========");
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * 获得领导讲话的附件
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLeadSpeakAttachRealPath.do", method = RequestMethod.GET)
	@ResponseBody
	public String personAttachRealPath(@RequestParam(value = "id") String id,
			HttpServletRequest request) {
		try {
			// logger.info("==========getPersonPhotoRealPath的id============"+id);
			LeadSpeakStore store = leadSpeakStoreService.findLeadSpeakStoreById(id);
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
		if (data!=null&&data.trim().length() > 0) {
			// ServletContext context=request.getSession().getServletContext();
			String[] strings = data.split(",");
			for (String str : strings) {
				if(str.trim().length()>1){
					int index = str.lastIndexOf(File.separator);
					str = str.substring(index + 1, str.length());
					index = str.lastIndexOf("_");
					String name = str.substring(index + 1, str.length());
					// System.out.println("========文件路径=========="+path);
					object.put("src", str);
					object.put("name", name);
					array.add(object);
				}
			}
		}
		return array.toString();
	}
	
	/**
	 * 上传领导讲话的附件
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/leadSpeakStoreFileUpload.do",method=RequestMethod.POST)
	@ResponseBody
	public String leadSpeakStoreFileUpload(@RequestParam("number")String number,
			HttpServletRequest request,HttpServletResponse response){
		//tempLeadSpeakStoreFileUpload
		String path = request.getSession().getServletContext().getRealPath(
				File.separator + "tempLeadSpeakStoreFileUpload" + number);
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
				//不是表单标签
				if (!fileItem.isFormField()) {
					fileName = new UpLoadUtil().getTpTime() + "_"+ fileItem.getName();
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
	 * 获得领导讲话的附件
	 * 
	 */
	@RequestMapping(value = "/getLeadSpeakStoreTempFile.do", method = RequestMethod.GET)
	@ResponseBody
	public String findExamineTempFile(HttpServletRequest request,
			@RequestParam("number") String number, 
			@RequestParam("appendix") String appendix, 
			HttpServletResponse response) {
		// 获得正在上传的文件
		List<String> tempList = findExamineTempFile(number, request, response);
		try {
			if (appendix!=null&&appendix.length()>1) {
				//修改附件
				//先把原来的列举出来
				appendix=URLDecoder.decode(appendix, "utf-8");
				String leadSpeakStore=Configured.getInstance().get("leadSpeakStore");
				String appendixPath=request.getServletContext().getRealPath(File.separator+leadSpeakStore);
				String []strings=appendix.split(",");
				for (String string : strings) {
					int index=string.lastIndexOf(File.separator);
					string=string.substring(index+1,string.length());
					String fileName=appendixPath+File.separator+string;
					//System.out.println("====先把原来的列举出来fileName===="+fileName);
					//System.out.println("====先把原来的列举出来appendix===="+appendix);
					File file=new File(fileName);
					if(file.exists()){
						//存在附件
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
	public String examineTempvoToJSon(List<String> examineList)
			throws Exception {
		JSONObject root = new JSONObject();
		JSONArray array = new JSONArray();
		//读取配置文件里的属性
		String leadSpeakStore=Configured.getInstance().get("leadSpeakStore");
		for (String s : examineList) {
			JSONObject obj = new JSONObject();
			obj.put("filePath", s);
			
			if(s.contains("tempLeadSpeakStoreFileUpload_")){
				//代表临时上传的文件
				int index=s.lastIndexOf("_");
				s=s.substring(index+1, s.length());
			}
			
			obj.put("fileName", s);
			obj.put("fileTempName", leadSpeakStore+File.separator+s);
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
	public static List<String> findExamineTempFile(String number,
			HttpServletRequest request, HttpServletResponse response) {
		// 返回所有临时文件的名称
		List<String> list = new ArrayList<String>();
		File tempFile[] = null;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		ServletContext serc = request.getSession().getServletContext();
		String tpath = serc.getRealPath(File.separator
				+ "tempLeadSpeakStoreFileUpload" + number);
		try {
			File tempFiles = new File(tpath);// 临时文件夹
			if (tempFiles.exists()) {
				tempFile = tempFiles.listFiles();
				for (File file : tempFile) {
					//临时文件
					list.add("tempLeadSpeakStoreFileUpload_"+file.getName());
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fi != null) {
					fi.close();
					if (in != null) {
						in.close();
						if (fo != null) {
							fo.close();
							if (out != null) {
								out.close();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 删除临时上传的文件
	 * @param request
	 * @param response
	 * @param fileName
	 * @param number
	 * @return
	 */
	@RequestMapping(value = "/deleteLeadSpeakStoreFile.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteExamineFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("fileName") String fileName,
			@RequestParam("number") String number) {
		// 返回所有临时文件的名称
		
		File tempFile[] = null;
		//System.out.println("====fileName===="+fileName);
		ServletContext serc = request.getSession().getServletContext();
		String tpath = serc.getRealPath(File.separator
				+ "tempLeadSpeakStoreFileUpload" + number);
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
	 * 得到当前上传附件的路径
	 * 从临时文件夹复制到最终存放的目录
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	public static String leadSpeakStoreFilePath(String number,
			HttpServletRequest request, HttpServletResponse response) {
		File tempFile[] = null;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		ServletContext serc = request.getSession().getServletContext();
		// 获得临时上传附件的路径
		String tpath = serc.getRealPath(File.separator+ "tempLeadSpeakStoreFileUpload" + number);
		// 人员库附件存放的文件夹名称
		//读取配置文件里的属性
		String personStoreFile=Configured.getInstance().get("leadSpeakStore");
		String lpath = serc.getRealPath(File.separator + personStoreFile);
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
					
					/*fi = new FileInputStream(file);
					fo = new FileOutputStream(lpath + File.separator+ file.getName());
					in = fi.getChannel();
					out = fo.getChannel();
					// 连接两个通道,从in中读取,然后写入out
					in.transferTo(0, in.size(), out);*/
					//直接利用FileUtils复制文件
					FileUtils.copyFileToDirectory(file, fileT);
					list.add(personStoreFile+File.separator + file.getName());
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
				if (fi != null) {
					fi.close();
				}if (in != null) {
					in.close();
				}
				if (fo != null) {
					fo.close();
				}
				if (out != null) {
					out.close();
				}
				while (new File(tpath).exists()) {
					//deleteDirectory(tpath);
					//先删除文件
					File []files=new File(tpath).listFiles();
					for (File file : files) {
						FileUtils.forceDelete(file);
					}
					//删掉文件夹
					FileUtils.deleteDirectory(new File(tpath));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/******************不需调用该方法*******************/
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
}
