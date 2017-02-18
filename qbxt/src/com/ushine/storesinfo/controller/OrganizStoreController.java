package com.ushine.storesinfo.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
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
import com.ushine.storesinfo.model.OrganizPerson;
import com.ushine.storesinfo.model.OrganizPublicAction;
import com.ushine.storesinfo.model.OrganizStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.TempClueData;
import com.ushine.storesinfo.model.WebsiteJournalStore;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.IOrganizBranchesService;
import com.ushine.storesinfo.service.IOrganizPersonService;
import com.ushine.storesinfo.service.IOrganizPublicActionService;
import com.ushine.storesinfo.service.IOrganizStoreService;
import com.ushine.storesinfo.service.IPersonStoreService;
import com.ushine.storesinfo.service.ITempClueDataService;
import com.ushine.storesinfo.service.IWebsiteJournalStoreService;
import com.ushine.storesinfo.storefinal.StoreFinal;
import com.ushine.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 组织库业务控制器
 * @author wangbailin
 *
 */
@Controller
public class OrganizStoreController {
	private static final Logger logger = LoggerFactory
			.getLogger(OrganizStoreController.class);
	private ServletContext servletContext;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IOrganizStoreService organizStoreService;
	@Autowired
	IPersonStoreService personStoreService;
	@Autowired
	private IWebsiteJournalStoreService websiteJournalStoreService;
	@Autowired
	private IOrganizBranchesService organizBranchesService;//组织分支机构
	@Autowired
	private IOrganizPersonService organizPersonService;//组织下属人员
	@Autowired
	private IOrganizPublicActionService organizPublicActionService;  //组织下属媒体刊物
	@Autowired
	private ITempClueDataService tempClueDataService;
	/**
	 * 查询组织库信息，多条件
	 */
	@RequestMapping(value="/findOrganizStoreByConditions.do",method=RequestMethod.GET)
	@ResponseBody
	public String findOrganizStoreByConditions(
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
		logger.info("查询组织库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询组织库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			fieldValue = new String(fieldValue.getBytes("ISO-8859-1"), "UTF-8");
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限查询组织库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {// 读取全部
					return organizStoreService.findOrganizStore(field,
							fieldValue, startTime, endTime, nextPage, size,
							null, null, null,sortField,dir);
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					return organizStoreService.findOrganizStore(field,
							fieldValue, startTime, endTime, nextPage, size,
							null, sessionMgr.getUOID(request), null,sortField,dir);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					return organizStoreService.findOrganizStore(field,
							fieldValue, startTime, endTime, nextPage, size,
							null, null, sessionMgr.getUDID(request),sortField,dir);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					return  organizStoreService.findOrganizStore(field,
								fieldValue, startTime, endTime, nextPage, size,
							sessionMgr.getUID(request), null, null,sortField,dir);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取")
							.toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询组织库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	@RequestMapping(value="/hasStoreByOrganizName.do",method=RequestMethod.POST)
	@ResponseBody
	public String hasStoreByOrganizName(@RequestParam("organizName") String organizName){
		//默认不存在
		logger.info("查询组织："+organizName+"是否存在");
		String msg="not_exist";
		ViewObject viewObject=null;
		try {
			if(organizStoreService.hasStoreByOrganizName(organizName)){
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
	 * 新增组织库信息
	 */
	@RequestMapping(value="/saveOrganizStore.do",method=RequestMethod.POST)
	@ResponseBody
	public String saveOrganizStore(
			HttpServletRequest request,
			@RequestParam("organizName") String organizName,
			@RequestParam("foundTime") String foundTime,
			@RequestParam("organizeType") String organizeType,
			@RequestParam("degreeOfLatitude") String degreeOfLatitude,
			@RequestParam("orgHeadOfName") String orgHeadOfName,//组织负责人手动输入名称
			@RequestParam("personStore") String personStore,//选择组织负责人名称集合
			@RequestParam("personStoreIds") String personStoreIds,//选择组织负责人id集合
			@RequestParam("organizPersonNames") String organizPersonNames,//主要成员手动输入名称
			@RequestParam("organizPersons") String organizPersons,//选择下属成员名称集合
			@RequestParam("organizPersonIds") String organizPersonIds,//选择下属成员id集合
			@RequestParam("organizPublicActionNames") String organizPublicActionNames,//组织下属刊物手动输入名称
			@RequestParam("organizPublicActions") String organizPublicActions,//选择下属刊物名称集合
			@RequestParam("organizPublicActionIds") String organizPublicActionIds,//选择下属刊物id集合
			@RequestParam("organizBranchesNames") String organizBranchesNames,//下属组织手动输入名称
			@RequestParam("organizBranches") String organizBranches,//选择下属组织名称集合
			@RequestParam("organizBrancheIds") String organizBrancheIds,//选择下属组织id集合
			@RequestParam("websiteURL") String websiteURL,//网站
			@RequestParam("basicCondition") String basicCondition,//基本情况
			@RequestParam("activityCondition") String activityCondition,//活动情况
			@RequestParam("typeId") String typeId,
			//是否为线索组织
			@RequestParam(value="isClue",required=false) String isClue,
			//线索组织的num
			@RequestParam(value="clueOrganizNum",required=false) String clueOrganizNum,
			//线索组织的state没关联就是0
			@RequestParam(value="state",required=false) String state
			){
		logger.info("新增组织库信息");
		logger.info("====线索关联组织===="+state);
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("新增组织库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限新增组织库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {// qi
					OrganizStore store = new OrganizStore();
					store.setActivityCondition(activityCondition);
					store.setBasicCondition(basicCondition);
					store.setDegreeOfLatitude(degreeOfLatitude);
					store.setFoundTime(foundTime);
					store.setInfoType(infoTypeService.findInfoTypeById(typeId));
					store.setOrganizBranchesNames(organizBranchesNames);
					store.setOrganizName(organizName);
					store.setOrganizPersonNames(organizPersonNames);
					store.setOrganizPublicActionNames(organizPublicActionNames);
					//去除开头和末尾的逗号
					orgHeadOfName=orgHeadOfName+","+personStore;
					if(orgHeadOfName.startsWith(",")){
						orgHeadOfName=orgHeadOfName.substring(1,orgHeadOfName.length());
					}
					if(StringUtil.isNull(orgHeadOfName)){
						store.setOrgHeadOfName(personStore);
					}else if(StringUtil.isNull(personStore)){
						store.setOrgHeadOfName(orgHeadOfName);
					}else{
						store.setOrgHeadOfName(orgHeadOfName+","+personStore);
					}
					if(orgHeadOfName.endsWith(",")){
						orgHeadOfName=orgHeadOfName.substring(0,orgHeadOfName.length()-1);
					}
					
					store.setOrgHeadOfName(orgHeadOfName);
					
					//logger.info("====orgHeadOfName+personStore===="+orgHeadOfName+personStore);
					//store.setPersonStore(personStore);
					store.setWebsiteURL(websiteURL);
					store.setOid(sessionMgr.getUOID(request));
					store.setUid(sessionMgr.getUID(request));
					store.setDid(sessionMgr.getUDID(request));
					store.setCreateDate(StringUtil.dates());
					store.setAction("1");
					store.setIsEnable("2");
					//先新增
					organizStoreService.saveOrganizStore(store);
					if(isClue.equals("isClue")){
						//如果是组织线索
						//然后添加到临时线索表temp_clue_data里面
						TempClueData clueData=new TempClueData();
						//正在操作的组织
						clueData.setAction(clueOrganizNum);
						clueData.setDataId(store.getId());
						clueData.setName(store.getOrganizName());
						clueData.setState("1");
						clueData.setType("organizStore");
						//添加到临时线索表中
						if(state!=null&&state.equals("0")){
							//这代表没有关联组织需要更新
							tempClueDataService.updateTempClueData(clueData);
						}else{
							//需要保存
							tempClueDataService.saveTempClueData(clueData);
						}
					}
					
					//获得组织下属:人员，组织，刊物的set对象
					String [] organizIds = null;
					String [] websiteIds = null;
					String [] personIds = null;
					if(!StringUtil.isNull(organizBrancheIds) && organizBrancheIds.length()>=1){
						organizIds = organizBrancheIds.split(",");
					}
					if(!StringUtil.isNull(organizPublicActionIds) && organizPublicActionIds.length()>=1){
						websiteIds = organizPublicActionIds.split(",");
					}
					if(!StringUtil.isNull(organizPersonIds) && organizPersonIds.length()>=1){
						personIds = organizPersonIds.split(",");
					}
					//创建组织下属分支机构
					if(organizIds!=null){
						for (int i = 0; i < organizIds.length; i++) {
							OrganizStore organizStore = organizStoreService.findOrganizStoreById(organizIds[i]);
							OrganizBranches branches = new OrganizBranches();
							branches.setOrganizStore(store);
							branches.setOrganizBranches(organizStore);
							organizBranchesService.saveOrganizBranches(branches);
						}
					}
					//创建组织下属媒体刊物
					if(websiteIds!=null){
						for (int i = 0; i < websiteIds.length; i++) {
							WebsiteJournalStore journalStore = websiteJournalStoreService.findWebsiteJouById(websiteIds[i]);
							OrganizPublicAction publicAction = new OrganizPublicAction();
							publicAction.setOrganizStore(store);
							publicAction.setWebsiteJournalStore(journalStore);
							organizPublicActionService.saveOrgPublicAction(publicAction);
						}
					}
					//创建组织下属人员
					if(personIds!=null){
						for (int i = 0; i < personIds.length; i++) {
							PersonStore personStore2 = personStoreService.findPersonStoreById(personIds[i]);
							OrganizPerson organizPerson = new OrganizPerson();
							organizPerson.setOrganizStore(store);
							organizPerson.setPersonStore(personStore2);
							organizPersonService.saveOrganizPerson(organizPerson);
						}
					}
					return new ViewObject(ViewObject.RET_SUCCEED, "新增成功!").toJSon();
				} 
			}
			return new ViewObject(ViewObject.RET_FAILURE, "新增组织库信息信息失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "新增组织库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE,"新增组织库信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 组织入库操作
	 */
	@RequestMapping(value="/dataStorageOrganizStoreById.do",method=RequestMethod.POST)
	@ResponseBody
	public String dataStorageOrganizStoreById(
			HttpServletRequest request,
			@RequestParam("organizName") String organizName,
			@RequestParam("foundTime") String foundTime,
			@RequestParam("organizeType") String organizeType,
			@RequestParam("degreeOfLatitude") String degreeOfLatitude,
			@RequestParam("orgHeadOfName") String orgHeadOfName,//组织负责人手动输入名称
			@RequestParam("personStore") String personStore,//选择组织负责人名称集合
			@RequestParam("personStoreIds") String personStoreIds,//选择组织负责人id集合
			@RequestParam("organizPersonNames") String organizPersonNames,//主要成员手动输入名称
			@RequestParam("organizPersons") String organizPersons,//选择下属成员名称集合
			@RequestParam("organizPersonIds") String organizPersonIds,//选择下属成员id集合
			@RequestParam("organizPublicActionNames") String organizPublicActionNames,//组织下属刊物手动输入名称
			@RequestParam("organizPublicActions") String organizPublicActions,//选择下属刊物名称集合
			@RequestParam("organizPublicActionIds") String organizPublicActionIds,//选择下属刊物id集合
			@RequestParam("organizBranchesNames") String organizBranchesNames,//下属组织手动输入名称
			@RequestParam("organizBranches") String organizBranches,//选择下属组织名称集合
			@RequestParam("organizBrancheIds") String organizBrancheIds,//选择下属组织id集合
			@RequestParam("websiteURL") String websiteURL,//网站
			@RequestParam("basicCondition") String basicCondition,//基本情况
			@RequestParam("activityCondition") String activityCondition,//活动情况
			@RequestParam("typeId") String typeId,
			@RequestParam("id") String id
			){
		logger.info("组织入库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("组织入库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
					OrganizStore store = new OrganizStore();
					store.setId(id);
					store.setActivityCondition(activityCondition);
					store.setBasicCondition(basicCondition);
					store.setDegreeOfLatitude(degreeOfLatitude);
					store.setFoundTime(foundTime);
					store.setInfoType(infoTypeService.findInfoTypeById(typeId));
					store.setOrganizBranchesNames(organizBranchesNames);
					store.setOrganizName(organizName);
					store.setOrganizPersonNames(organizPersonNames);
					store.setOrganizPublicActionNames(organizPublicActionNames);
					store.setOrgHeadOfName(orgHeadOfName+","+personStore);
					//logger.info("====orgHeadOfName+personStore===="+orgHeadOfName+personStore);
					//store.setPersonStore(personStore);
					store.setWebsiteURL(websiteURL);
					store.setOid(sessionMgr.getUOID(request));
					store.setUid(sessionMgr.getUID(request));
					store.setDid(sessionMgr.getUDID(request));
					store.setCreateDate(StringUtil.dates());
					store.setAction("1");
					store.setIsEnable("2");
					store.setIsToStorage("1");
					//先新增
					organizStoreService.updateOrganizStoreByOrgId(store);
					//获得组织下属:人员，组织，刊物的set对象
					String [] organizIds = null;
					String [] websiteIds = null;
					String [] personIds = null;
					if(!StringUtil.isNull(organizBrancheIds) && organizBrancheIds.length()>=1){
						organizIds = organizBrancheIds.split(",");
					}
					if(!StringUtil.isNull(organizPublicActionIds) && organizPublicActionIds.length()>=1){
						websiteIds = organizPublicActionIds.split(",");
					}
					if(!StringUtil.isNull(organizPersonIds) && organizPersonIds.length()>=1){
						personIds = organizPersonIds.split(",");
					}
					//创建组织下属分支机构
					if(organizIds!=null){
						for (int i = 0; i < organizIds.length; i++) {
							OrganizStore organizStore = organizStoreService.findOrganizStoreById(organizIds[i]);
							OrganizBranches branches = new OrganizBranches();
							branches.setOrganizStore(store);
							branches.setOrganizBranches(organizStore);
							organizBranchesService.saveOrganizBranches(branches);
						}
					}
					//创建组织下属媒体刊物
					if(websiteIds!=null){
						for (int i = 0; i < websiteIds.length; i++) {
							WebsiteJournalStore journalStore = websiteJournalStoreService.findWebsiteJouById(websiteIds[i]);
							OrganizPublicAction publicAction = new OrganizPublicAction();
							publicAction.setOrganizStore(store);
							publicAction.setWebsiteJournalStore(journalStore);
							organizPublicActionService.saveOrgPublicAction(publicAction);
						}
					}
					//创建组织下属人员
					if(personIds!=null){
						for (int i = 0; i < personIds.length; i++) {
							PersonStore personStore2 = personStoreService.findPersonStoreById(personIds[i]);
							OrganizPerson organizPerson = new OrganizPerson();
							organizPerson.setOrganizStore(store);
							organizPerson.setPersonStore(personStore2);
							organizPersonService.saveOrganizPerson(organizPerson);
						}
					}
					return new ViewObject(ViewObject.RET_SUCCEED, "入库成功!").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "入库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE,"入库信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 修改组织库信息，
	 * @return
	 */
	@RequestMapping(value="/updateOrganizStoreById.do",method=RequestMethod.POST)
	@ResponseBody
	public String updateOrganizStoreById(HttpServletRequest request,
			@RequestParam("organizName") String organizName,
			@RequestParam("foundTime") String foundTime,
			@RequestParam("organizeType") String organizeType,
			@RequestParam("degreeOfLatitude") String degreeOfLatitude,
			@RequestParam("orgHeadOfName") String orgHeadOfName,//组织负责人手动输入名称
			@RequestParam("personStore") String personStore,//选择组织负责人名称集合
			@RequestParam("personStoreIds") String personStoreIds,//选择组织负责人id集合
			@RequestParam("organizPersonNames") String organizPersonNames,//主要成员手动输入名称
			@RequestParam("organizPublicActionNames") String organizPublicActionNames,//组织下属刊物手动输入名称
			@RequestParam("organizBranchesNames") String organizBranchesNames,//下属组织手动输入名称
			@RequestParam("websiteURL") String websiteURL,//网站
			@RequestParam("basicCondition") String basicCondition,//基本情况
			@RequestParam("activityCondition") String activityCondition,//活动情况
			@RequestParam("typeId") String typeId,
			@RequestParam("id") String id
			){
		logger.info("修改组织库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("修改组织库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限修改组织库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {// qi
					OrganizStore organizStore = organizStoreService.findOrganizStoreById(id);
					OrganizStore store = new OrganizStore();
					store.setId(id);
					store.setActivityCondition(activityCondition);
					store.setBasicCondition(basicCondition);
					store.setDegreeOfLatitude(degreeOfLatitude);
					store.setFoundTime(foundTime);
					store.setInfoType(infoTypeService.findInfoTypeById(typeId));
					store.setOrganizBranchesNames(organizBranchesNames);
					store.setOrganizName(organizName);
					store.setOrganizPersonNames(organizPersonNames);
					store.setOrganizPublicActionNames(organizPublicActionNames);
					/*if(orgHeadOfName!=null&&orgHeadOfName.trim().length()>0){
						//逗号隔开
						store.setOrgHeadOfName(orgHeadOfName+","+personStore);
						logger.info("====orgHeadOfName+personStore===="+orgHeadOfName+personStore);
					}else{
						store.setOrgHeadOfName(personStore);
						logger.info("====setOrgHeadOfName===="+personStore);
					}*/
					//去除开头和末尾的逗号
					orgHeadOfName=orgHeadOfName+","+personStore;
					if(orgHeadOfName.startsWith(",")){
						orgHeadOfName=orgHeadOfName.substring(1,orgHeadOfName.length());
					}
					if(orgHeadOfName.endsWith(",")){
						orgHeadOfName=orgHeadOfName.substring(0,orgHeadOfName.length()-1);
					}
					store.setOrgHeadOfName(orgHeadOfName);
					store.setWebsiteURL(websiteURL);
					store.setOid(sessionMgr.getUOID(request));
					store.setUid(sessionMgr.getUID(request));
					store.setDid(sessionMgr.getUDID(request));
					store.setAction("2");
					store.setIsEnable(organizStore.getIsEnable());
					store.setCreateDate(organizStore.getCreateDate());
					organizStoreService.updateOrganizStoreByOrgId(store);
					return new ViewObject(ViewObject.RET_SUCCEED, "修改成功!").toJSon();
				} 
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改组织库信息信息失败，没有权限")
					.toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "修改组织库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE,
					"修改组织库信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);

		}
	}
	/**
	 * 删除组织库信息
	 * @return
	 */
	@RequestMapping(value="/delOrganizStoreById.do",method=RequestMethod.POST)
	@ResponseBody
	public String delOrganizStoreById(
			HttpServletRequest request,
			@RequestParam("ids") String[] ids){
		logger.info("删除组织库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("删除组织库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限删除组织库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {// 
					organizStoreService.updateOrganizStoreAction(ids);
					return new ViewObject(ViewObject.RET_SUCCEED, "删除成功!").toJSon();
				} 
			}
			return new ViewObject(ViewObject.RET_FAILURE, "删除组织库信息信息失败，没有权限")
					.toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "删除组织库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE,
					"删除组织库信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);

		}
	}
	/**
	 * 根据人员库名称查询类别控制器
	 * @return
	 */
	@RequestMapping(value="/getInfoTypeByOrganizStore.do",method=RequestMethod.GET)
	@ResponseBody
	public String getInfoTypeByPersonStore(){
		try {
			//根据人员库名称查询类别
			return infoTypeService.getInfoTypeByTableTypeName(StoreFinal.ORGANIZ_STORE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 选择负责人
	 * @return
	 */
	@RequestMapping(value = "/selectPersonData.do", method = RequestMethod.GET)
	@ResponseBody
	public String selectPersonData(HttpServletRequest request,
			@RequestParam("page") int nextPage, @RequestParam("limit") int size) {
		logger.info("组织选择人员库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("组织选择人员库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限查询组织库信息
			if (list != null && list.size() > 0) {
				/*if ("1x0001".equals(list.get(0))) {// 读取全部
					PagingObject<PersonStore> pagingObject = personStoreService
							.findPersonStoreByIsEnable(null,null,null,null,nextPage, size, null,
									null, null);
					return selectPersonDataVoToJson(pagingObject);
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					PagingObject<PersonStore> pagingObject = personStoreService
							.findPersonStoreByIsEnable(null,null,null,null,nextPage, size, null,
									sessionMgr.getUOID(request), null);
					return selectPersonDataVoToJson(pagingObject);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					PagingObject<PersonStore> pagingObject = personStoreService
							.findPersonStoreByIsEnable(null,null,null,null,nextPage, size, null,
									null, sessionMgr.getUDID(request));
					return selectPersonDataVoToJson(pagingObject);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					PagingObject<PersonStore> pagingObject = personStoreService
							.findPersonStoreByIsEnable(null,null,null,null,nextPage, size,
									sessionMgr.getUID(request), null, null);
					return selectPersonDataVoToJson(pagingObject);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取")
							.toJSon();
				}*/
			}
			return new ViewObject(ViewObject.RET_FAILURE, "组织选择人员库信息失败，没有权限")
					.toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "组织选择人员库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE,
					"组织选择人员库信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);

		}
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String selectPersonDataVoToJson(PagingObject<PersonStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (PersonStore PersonStore : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", PersonStore.getId());
			obj.put("personName", PersonStore.getPersonName());
			obj.put("nameUsedBefore", PersonStore.getNameUsedBefore());
			obj.put("englishName", PersonStore.getEnglishName());
			obj.put("bebornTime", PersonStore.getBebornTime());
			obj.put("presentAddress", PersonStore.getPresentAddress());
			obj.put("workUnit", PersonStore.getWorkUnit());
			obj.put("registerAddress", PersonStore.getRegisterAddress());
			obj.put("antecedents", PersonStore.getAntecedents());
			obj.put("activityCondition", PersonStore.getActivityCondition());
			obj.put("sex", PersonStore.getSex());
			obj.put("infoType", PersonStore.getInfoType().getTypeName());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 选择刊物
	 * @return
	 */
	@RequestMapping(value="/selectWetsiteJournalData.do",method=RequestMethod.GET)
	@ResponseBody
	public String selectWetsiteJournalData(HttpServletRequest request,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size){
		logger.info("组织选择媒体网站库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("组织选择媒体网站库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限查询组织库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {// 读取全部
					PagingObject<WebsiteJournalStore> pagingObject = websiteJournalStoreService.findWebsiteJournalByIsEnable(nextPage, size, null, null, null);
					return selectWetsiteJournalDataVoToJson(pagingObject);
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					
					PagingObject<WebsiteJournalStore> pagingObject = websiteJournalStoreService.findWebsiteJournalByIsEnable(nextPage, size, null, sessionMgr.getUOID(request), null);
					return selectWetsiteJournalDataVoToJson(pagingObject);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					
					PagingObject<WebsiteJournalStore> pagingObject = websiteJournalStoreService.findWebsiteJournalByIsEnable(nextPage, size, null, null, sessionMgr.getUDID(request));
					return selectWetsiteJournalDataVoToJson(pagingObject);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					
					PagingObject<WebsiteJournalStore> pagingObject = websiteJournalStoreService.findWebsiteJournalByIsEnable(nextPage, size, sessionMgr.getUOID(request), null, null);
					return selectWetsiteJournalDataVoToJson(pagingObject);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取")
							.toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "组织选择媒体网站库信息失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "组织选择媒体网站库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "组织选择媒体网站库信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		
	}
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String selectWetsiteJournalDataVoToJson(PagingObject<WebsiteJournalStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (WebsiteJournalStore wb : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", wb.getId());
			obj.put("name", wb.getName());
			obj.put("websiteURL", wb.getWebsiteURL());
			obj.put("mainWholesaleAddress", wb.getMainWholesaleAddress());
			obj.put("establishPerson", wb.getEstablishPerson());
			obj.put("establishTime", wb.getEstablishTime());
			if(wb.getInfoType()!=null){				
				//类别不为空
				obj.put("type", wb.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 选择组织数据
	 * @return
	 */
	@RequestMapping(value="/selectOrganizData.do",method=RequestMethod.GET)
	@ResponseBody
	public String selectOrganizData(HttpServletRequest request,
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size){
		logger.info("组织选择下属组织库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("组织选择下属组织库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//
			// 判断当前用户是否有权限查询组织库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {// 读取全部
					PagingObject<OrganizStore> pagingObject = organizStoreService.findOrganizStoreByIsEnable(nextPage, size, null, null, null);
					return selectOrganizDataVoToJson(pagingObject);
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					PagingObject<OrganizStore> pagingObject = organizStoreService.findOrganizStoreByIsEnable(nextPage, size, null, sessionMgr.getUOID(request), null);
					return selectOrganizDataVoToJson(pagingObject);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					PagingObject<OrganizStore> pagingObject = organizStoreService.findOrganizStoreByIsEnable(nextPage, size, null, null, sessionMgr.getUDID(request));
					return selectOrganizDataVoToJson(pagingObject);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					PagingObject<OrganizStore> pagingObject = organizStoreService.findOrganizStoreByIsEnable(nextPage, size, sessionMgr.getUID(request), null, null);
					return selectOrganizDataVoToJson(pagingObject);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取")
							.toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "组织选择下属组织库信息失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "组织选择下属组织库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "组织选择下属组织库信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		
	}
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String selectOrganizDataVoToJson(PagingObject<OrganizStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OrganizStore or : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", or.getId());
			obj.put("organizName", or.getOrganizName());
			obj.put("websiteURL", or.getWebsiteURL());
			obj.put("foundTime", or.getFoundTime());
			if(or.getInfoType()!=null){
				
				obj.put("type", or.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}
	/**
	 * 下载组织信息的PDF文件
	 * @param request
	 * @param name
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadOrganizPDFFile.do")
	@ResponseBody
	public String dwonExportData(HttpServletRequest request,
			@RequestParam("orgId") String orgId,
			HttpServletResponse response) {
		ServletOutputStream out = null;
		String root = request.getServletContext().getRealPath("/");
		//文件名
		String path = root + orgId + ".doc";
		try {
			// 获取服务器地址
			//String path = servletContext.getRealPath("/") + "PDFFilePath/";
			//生成word
			organizStoreService.outputOrganizStoreToWord(orgId, path);
			// 下载附件
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头
			response.setHeader("Content-Disposition", "attachment;fileName=" + orgId + ".doc");
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
	/**
	 * 启用组织
	 * @param request
	 * @param orgIds
	 * @return
	 */
	@RequestMapping(value="/startOrganiz.do",method=RequestMethod.POST)
	@ResponseBody
	public String startOrganiz(
			HttpServletRequest request,
			@RequestParam("orgIds") String[] orgIds){
		logger.info("启用组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("启用组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizStoreService.updateOrganizStoreIsEnableStart(orgIds);
			return new ViewObject(ViewObject.RET_FAILURE, "启用组织信息成功！").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "启用组织信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "启用组织信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 禁用组织
	 * @param request
	 * @param orgIds
	 * @return
	 */
	@RequestMapping(value="/ceaseOrganiz.do",method=RequestMethod.POST)
	@ResponseBody
	public String ceaseOrganiz(
			HttpServletRequest request,
			@RequestParam("orgIds") String[] orgIds){
		logger.info("禁用组织");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("禁用组织成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 汉字关键字搜索
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			organizStoreService.updateOrganizStoreIsEnableCease(orgIds);
			return new ViewObject(ViewObject.RET_FAILURE, "禁用组织信息成功！").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "禁用组织信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "禁用组织信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		
	}
	}
	
}
