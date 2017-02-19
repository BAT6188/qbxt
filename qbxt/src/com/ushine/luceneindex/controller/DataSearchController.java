package com.ushine.luceneindex.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tdcq.common.logging.LogFactory;
import com.tdcq.common.logging.LogInfo;
import com.ushine.core.po.Resource;
import com.ushine.core.service.IResourceService;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.StoreIndexQuery;
import com.ushine.luceneindex.vo.MyComparator;
import com.ushine.luceneindex.vo.MyJsonObject;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.model.OrganizStore;
import com.ushine.storesinfo.model.OutsideDocStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.VocationalWorkStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;
import com.ushine.storesinfo.service.IClueStoreService;
import com.ushine.storesinfo.service.ILeadSpeakStoreService;
import com.ushine.storesinfo.service.IOrganizStoreService;
import com.ushine.storesinfo.service.IOutsideDocStoreService;
import com.ushine.storesinfo.service.IPersonStoreService;
import com.ushine.storesinfo.service.IVocationalWorkStoreService;
import com.ushine.storesinfo.service.IWebsiteJournalStoreService;
import com.ushine.util.StringUtil;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 利用Lucene做查询的控制器
 * @author Administrator
 *
 */
@Controller
public class DataSearchController {
	
	@Autowired
	private IBaseDao baseDao;
	@Autowired
	private IResourceService resourceService;
	@Autowired
	private IPersonStoreService personStoreService;
	@Autowired
	private IClueStoreService clueStoreService;
	@Autowired
	private IVocationalWorkStoreService vocationalWorkStoreService;
	@Autowired
	private IOutsideDocStoreService outsideDocStoreService;
	@Autowired
	private ILeadSpeakStoreService leadSpeakStoreService;
	
	private static final Logger logger = LoggerFactory.getLogger(DataSearchController.class);
	
	private String getDatas(String fieldValue,String uid,String oid ,String did,Class clazz,Integer size) {
		logger.info("搜索关键字fieldValue："+fieldValue);
		String datas=new String();
		String classSimpleName=clazz.getSimpleName();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String endTime=format.format(new Date());
		try {
			//开始时间
			String startTime="1900-01-01";
			switch (classSimpleName) {
				//获得10条数据
				case "PersonStore":
					datas=personStoreService.findPersonStore("anyField", fieldValue, startTime, endTime, 1, size, uid, oid, did,null,null);
					break;
				case "ClueStore":
					datas=clueStoreService.findClueStore("anyField", fieldValue, startTime, endTime, 1, size, uid, oid, did,null,null);
					break;
				case "VocationalWorkStore":
					datas=vocationalWorkStoreService.findVocationalWorkStore("anyField", fieldValue, startTime, endTime, 1, size, uid, oid, did,null,null);
					break;
				case "OutsideDocStore":
					datas=outsideDocStoreService.findOutsideDocStore("anyField", fieldValue, startTime, endTime, 1, size, uid, oid, did,null,null);
					break;
				case "LeadSpeakStore":
					datas=leadSpeakStoreService.findLeadSpeakStore("anyField", fieldValue, startTime, endTime, 1, size, uid, oid, did,null,null);
					break;	
			}
		} catch (Exception e) {
			logger.error("查询符合条件的数据失败");
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 获得符合查询条件的数量，返回MyJsonObject对象
	 * @param recourceCode 权限编号
	 * @param fieldValue 关键字
	 * @param oid oid
	 * @param did did
	 * @param uid uid
	 * @param dataType 搜索库的类型
	 * @param clazz 搜索库的Class
	 * @param size 显示数量
	 * @return
	 */
	private MyJsonObject getStoresCount(String recourceCode,String fieldValue,String oid,String did,String uid,
			String dataType,Class clazz,Integer size) {
		MyJsonObject countObject=new MyJsonObject();
		try {
			//获得数据
			if(recourceCode.equals("1x0001")){
				//全部
				countObject.setDataCount(StoreIndexQuery.getCount(fieldValue, null, null, null, clazz));
				countObject.setDatas(getDatas(fieldValue, null, null, null, clazz,size));
			}
			if(recourceCode.equals("1x0010")){
				//组织
				countObject.setDataCount(StoreIndexQuery.getCount(fieldValue, null, oid, null, clazz));
				countObject.setDatas(getDatas(fieldValue, null, oid, null, clazz,size));
			}
			if(recourceCode.equals("1x0011")){
				//部门
				countObject.setDataCount(StoreIndexQuery.getCount(fieldValue, null, null,did, clazz));
				countObject.setDatas(getDatas(fieldValue, null, null, did, clazz,size));
			}
			if(recourceCode.equals("1x0100")){
				//个人
				countObject.setDataCount(StoreIndexQuery.getCount(fieldValue, uid, null, null, clazz));
				countObject.setDatas(getDatas(fieldValue, uid, null, null, clazz,size));
			}
			if(recourceCode.equals("1x0000")){
				//禁止
				countObject.setDataCount(-1);
				countObject.setDatas("");
			}
			countObject.setDataType(dataType);
			countObject.setStoreName(clazz.getSimpleName());
		} catch (Exception e) {
			//异常
			countObject.setDataCount(-1);
			countObject.setDatas("");
			countObject.setDataType("");
			countObject.setStoreName("");
			logger.error("查询索引库失败");
		}
		return countObject;
	}
	/**
	 * 查询七个库的命中数
	 * @param fieldValue
	 * @return
	 */
	@RequestMapping(value="/searchForKeyWord.do",method=RequestMethod.POST)
	@ResponseBody
	public String searchForKeyword(
			@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "size", required = false) Integer size,
			HttpServletRequest request) {
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询命中数");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		JSONArray array=new JSONArray();
		try {
			//汉字关键字搜索
			//post请求不需要转化编码
			//fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			logger.info("输入的关键字fieldValue:"+fieldValue);
			//获取用户的登录信息
			if(!StringUtil.isEmty(fieldValue)){
				UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
				loginfo.setUserName(sessionMgr.getTrueName(request));
				loginfo.setUserCode(sessionMgr.getCode(request));
				String value=sessionMgr.getUserInfoByISon(request);
				//获得json里的各个key的值用MorphDynaBean
				JSONObject jsonObject=JSONObject.fromObject(value);
				MorphDynaBean bean=(MorphDynaBean) jsonObject.toBean(jsonObject);
				//角色id
				String roleId=bean.get("uRole").toString();
				roleId=roleId.substring(1, roleId.length()-1);
				//资源id
				String personStoreResId=getResourceCode("查询人员");
				String clueStoreResId=getResourceCode("查询线索");
				String outsideDocStoreResId=getResourceCode("查询外来文档");
				String vocationalStoreResId=getResourceCode("查询业务文档");
				String leadSpeakStoreResId=getResourceCode("查询领导讲话");
				/////七个库的操作权限代号
				//人员库的
				String personCode=getPermitCode(personStoreResId, roleId);
				//线索库
				String clueCode=getPermitCode(clueStoreResId, roleId);
				//外来文档库
				String outsideDocCode=getPermitCode(outsideDocStoreResId, roleId);
				//业务文档库
				String voStoreCode=getPermitCode(vocationalStoreResId, roleId);
				//领导讲话
				String leadStoreCode=getPermitCode(leadSpeakStoreResId, roleId);
				
				String[] resourceCodes=new String[]{personCode,clueCode,
						outsideDocCode,voStoreCode,leadStoreCode};
				String[] dataTypes=new String[]{"重点人员库","线索库",
						"外来文档库","业务文档库","领导讲话库"};
				
				String[] storeNames=new String[]{"PersonStore",
						"ClueStore","OutsideDocStore","VocationalWorkStore","LeadSpeakStore"};
				
				Class[]classes=new Class[]{PersonStore.class,
						ClueStore.class,OutsideDocStore.class,VocationalWorkStore.class,LeadSpeakStore.class};
				
				MyJsonObject[] myJsonObjects=new MyJsonObject[classes.length];
				for (int i = 0; i < classes.length; i++) {
					//查询七个库符合条件
					myJsonObjects[i]=getStoresCount(resourceCodes[i], fieldValue, bean.get("uOId").toString(), 
							bean.get("uDId").toString(), bean.get("uId").toString(), dataTypes[i], classes[i],size);
				}
				//排序输出：从多到少
				Arrays.sort(myJsonObjects, new MyComparator());
				//加到jsonarray中
				for (MyJsonObject myJsonObject : myJsonObjects) {
					array.add(JSONObject.fromObject(myJsonObject));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("查询出现异常");
		}finally {
			log.log(loginfo);
		}
		logger.info("查询所有库的结果集合：\r\n"+array.toString());
		return array.toString();
	}
	/**
	 * 获取资源的操作权限
	 * @param resouceID
	 * @param roleId
	 * @return
	 */
	private String getPermitCode(String resouceID,String roleId){
		//默认没有操作权限
		String result="1x0000";
		try {
			String sql="SELECT t2.code as code FROM (SELECT operation_id as "
					+ "oper_id FROM T_PERMIT WHERE resource_id='"+resouceID+"'"
					+ " AND role_id='"+roleId+"') t1, T_OPERATION t2 WHERE t1.oper_id=t2.id";
			List<String> list=baseDao.findBySql(sql);
			if(null!=list&&list.size()>0){
				result=list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获得查询人员、组织等对相应的id,用来进一步获得用户的查询权限
	 * @param resouceName 资源名称,比如查询人员，查询组织
	 * @return 资源的id
	 */
	private String getResourceCode(String resouceName){
		String result=null;
		try {
			List<Resource> list=resourceService.findResourcesByName(resouceName);
			for (Resource resource : list) {
				if (null!=list&&list.size()>0) {
					result=list.get(0).getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
