package com.ushine.storeInfo.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ushine.common.vo.PagingObject;
import com.ushine.common.vo.ViewObject;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.TempClueData;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.IOrganizStoreService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.storeInfo.service.ITempClueDataService;
import com.ushine.storeInfo.service.IWebsiteJournalStoreService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 线索涉及对象临时数据控制器
 * @author wangbailin
 *
 */
@Controller
public class TempClueDataController {
	@Autowired
	private ITempClueDataService clueDataService;
	@Autowired
	private IPersonStoreService personStoreService;
	@Autowired
	private IOrganizStoreService organizStoreService;
	@Autowired
	private IWebsiteJournalStoreService websiteJournalStoreService;
	/**
	 * 新增线索涉及对象临时数据，，，，，只有名称
	 * tempClueDataName,tempClueDataType,sjnum
	 * @return
	 */
	@RequestMapping(value="/saveTempClueDataName.do",method=RequestMethod.POST)
	@ResponseBody
   public String saveTempClueDataName(
		   HttpServletRequest request,
		   @RequestParam("tempClueDataName") String tempClueDataName,
		   @RequestParam("tempClueDataType") String tempClueDataType,
		   @RequestParam("sjnum") String sjnum){
	   //产生对象
	   TempClueData clueData = new TempClueData();
	   clueData.setName(tempClueDataName);
	   clueData.setAction(sjnum);
	   clueData.setType(tempClueDataType);
	   clueData.setState("0");
	   try {
		clueDataService.saveTempClueData(clueData);
		return new ViewObject(ViewObject.RET_SUCCEED, "新增成功").toJSon();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new ViewObject(ViewObject.RET_ERROR, "新增失败").toJSon();
	}
   }
	/**
	 * 根据操作标识随机数查询涉及对象
	 * @param request
	 * @param number
	 * @return
	 * http://localhost:8080/majorInfo/findTempClueData.do?number=923101&_dc=1463468253467&page=1&start=0&limit=50 404 (Not Found) 
	 */
	
	@RequestMapping(value="/findTempClueData.do",method=RequestMethod.GET)
	@ResponseBody
	public String findTempClueData(
			HttpServletRequest request,
			@RequestParam("number") String number){
		try {
			List<TempClueData> clueDatas = clueDataService.findTempClueData(number);
			return findTempClueDataVoToJSon(clueDatas);
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
	public String findTempClueDataVoToJSon(List<TempClueData> list) {
		JSONObject root = new JSONObject();
		JSONArray array = new JSONArray();
		for (TempClueData t : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", t.getId());
			obj.put("name", t.getName());
			obj.put("type", t.getType());
			obj.put("state", t.getState());
			obj.put("dataId", t.getDataId());
			obj.put("action", t.getAction());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 删除临时数据ById
	 * @return
	 */
	@RequestMapping(value="/dateTempClueDataById.do",method=RequestMethod.POST)
	@ResponseBody
	public String dateTempClueDataById(
			HttpServletRequest request,
			@RequestParam("id") String id){
		try {
			clueDataService.dateTempClueDataById(id);
			return new ViewObject(ViewObject.RET_SUCCEED, "删除成功！").toJSon();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_ERROR, "删除失败！").toJSon();
		}
	}
	/**
	 * 根据数据类型选择已有的数据
	 * @return
	 */
	@RequestMapping(value="/selectExistingDataByType.do",method=RequestMethod.GET)
	@ResponseBody
	public String selectExistingDataByType(
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size,
			HttpServletRequest request,
			@RequestParam("field") String field,
			@RequestParam("fieldValue") String fieldValue,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime,
			@RequestParam("type") String type){
		try {
			// 汉字关键字搜索
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			if("personStore".equals(type)){   //人员库
				//System.out.println("===type==="+type);
				PagingObject<PersonStore> pagingObject = personStoreService.findPersonStoreByIsEnable(field,fieldValue,startTime,endTime,nextPage, size,null,null,null);
			   return personStoreVoToJSon(pagingObject);
			}else if("organizStore".equals(type)){   //组织库
				PagingObject<OrganizStore> pagingObject = organizStoreService.findOrganizStoreByIsEnable(field,fieldValue,startTime,endTime,nextPage, size);
				return organizStoreVoToJSon(pagingObject);
			}else if("websiteJournalStore".equals(type)){  //媒体网站库
				PagingObject<WebsiteJournalStore> pagingObject = websiteJournalStoreService.findWebsiteJournalStoreByIsEnable(field,fieldValue,startTime,endTime,nextPage, size);
				return WebsiteJournalStoreVoToJSon(pagingObject);
			}
		} catch (Exception e) {
			// TODO: handle exception
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
	public String personStoreVoToJSon(PagingObject<PersonStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (PersonStore PersonStore : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", PersonStore.getId());
			obj.put("name", PersonStore.getPersonName());
			if(PersonStore.getInfoType()!=null){
				//类别不能为空
				obj.put("type", PersonStore.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 将特定的数据转换成json格式
	 * @param list
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String organizStoreVoToJSon(PagingObject<OrganizStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizStore o : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", o.getId());
			obj.put("name", o.getOrganizName());
			if(o.getInfoType()!=null){
				//类别不能为空
				obj.put("type", o.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 将特定的数据转换成json格式
	 * @param list
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String WebsiteJournalStoreVoToJSon(PagingObject<WebsiteJournalStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (WebsiteJournalStore w : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", w.getId());
			obj.put("name", w.getName());
			if(w.getInfoType()!=null){
				//类别不能为空
				obj.put("type", w.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	
	
	
	/**
	 * 新增线索涉及对象临时数据   选择已有的
	 * tempClueDataName,tempClueDataType,sjnum
	 * @return
	 */
	@RequestMapping(value="/saveTempClueDataBySelect.do",method=RequestMethod.POST)
	@ResponseBody
   public String saveTempClueDataBySelect(
		   HttpServletRequest request,
		   @RequestParam("ids") String ids[],
		   @RequestParam("names") String names[],
		   @RequestParam("type") String type,
		   @RequestParam("number")String number){
		
	   try {
		clueDataService.saveTempClueDataBySelect(ids, names, type, number);
		return new ViewObject(ViewObject.RET_SUCCEED, "选择成功").toJSon();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return new ViewObject(ViewObject.RET_ERROR, "选择失败").toJSon();
	}
   }
	
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="/announcementUploadFile.do",method=RequestMethod.POST)
	@ResponseBody
	public void announcementUploadFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("dir") String dirName) {
		try {
			String path = request.getSession().getServletContext().getRealPath(
					File.separator + "tempAnnouncementImageFileUpload");
			// 如果文件夹不存在则创建文件夹
			File fileT = new File(path);
			if (!fileT.exists()) {
				fileT.mkdirs();
			}
			String savePathTemp = path;
			
			//文件保存目录URL
			String saveUrl  = "announcementPreviewImage.do?path=";
			
			//定义允许上传的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");
			//extMap.put("flash", "swf,flv");
			//extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
			
			//最大文件大小
			long maxSize = 1000000;

			response.setContentType("text/html; charset=UTF-8");

			if(!ServletFileUpload.isMultipartContent(request)){
				response.getWriter().write(getError("请选择文件。"));
				return;
			}
			//检查目录
			File uploadDir = new File(path);
			if(!uploadDir.isDirectory()){
				response.getWriter().write(getError("上传目录不存在。"));
				return;
			}
			//检查目录写权限
			if(!uploadDir.canWrite()){
				response.getWriter().write(getError("上传目录没有写权限。"));
				return;
			}

			if (dirName == null) {
				dirName = "image";
			}
			if(!extMap.containsKey(dirName)){
				response.getWriter().write(getError("目录名不正确。"));
				return;
			}
			
			//创建文件夹
			savePathTemp += dirName + "/";
			saveUrl += dirName + "/";
			File saveDirFile = new File(savePathTemp);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			savePathTemp += ymd + "/";
			saveUrl += ymd + "/";
			File dirFile = new File(savePathTemp);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			List items = upload.parseRequest(request);
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				String fileName = item.getName();
				long fileSize = item.getSize();
				if (!item.isFormField()) {
					//检查文件大小
					if(fileSize > maxSize){
						response.getWriter().write(getError("上传文件大小超过限制。"));
						return;
					}
					//检查扩展名
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
						response.getWriter().write(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
						return;
					}
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
					try{
						File uploadedFile = new File(savePathTemp, newFileName);
						item.write(uploadedFile);
					}catch(Exception e){
						response.getWriter().write(getError("上传文件失败。"));
						return;
					}

					JSONObject obj = new JSONObject();
					obj.put("error", 0);
					obj.put("url", saveUrl + newFileName);
					response.getWriter().write(obj.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getError(String message){
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toString();
	}
}
