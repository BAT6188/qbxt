package com.ushine.storesinfo.controller;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Element;
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
import com.ushine.common.utils.PathUtils;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.dao.IBaseDao;
//import com.ushine.storeInfo.exportPDFInfo.personStore.PersonStoreExportPdf;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.NetworkAccountStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.TempClueData;
import com.ushine.storesinfo.service.ICertificatesStoreService;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.INetworkAccountStoreService;
import com.ushine.storesinfo.service.IPersonStoreService;
import com.ushine.storesinfo.service.ITempClueDataService;
import com.ushine.storesinfo.storefinal.StoreFinal;
import com.ushine.util.CreatAndReadExcel;
import com.ushine.util.StringUtil;
import com.ushine.util.UpLoadUtil;
import com.ushine.util.XmlUtils;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 人员库业务控制器
 * 
 * @author wangbailin
 * 
 */
@Controller(value="personStoreController")
public class PersonStoreController {
	private static final Logger logger = LoggerFactory.getLogger(PersonStoreController.class);
	private ServletContext servletContext;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IPersonStoreService personStoreService;
	@Autowired
	private IBaseDao baseDao;
	@Autowired
	private ICertificatesStoreService certificatesStoreService;
	@Autowired
	private INetworkAccountStoreService networkAccountStoreService;

	@Autowired
	private ITempClueDataService tempClueDataService;

	/**
	 * 查询人员库信息，多条件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findPersonStoreByConditions.do", method = RequestMethod.GET)
	@ResponseBody
	public String findPersonStoreByConditions(@RequestParam(value = "fieldValue", required = false) String fieldValue,
			@RequestParam(value = "field", required = false) String field,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime, 
			@RequestParam("page") int nextPage,
			@RequestParam("limit") int size, 
			//排序字段
			@RequestParam(value ="sort", required = false) String sortField,
			@RequestParam(value ="dir", required = false) String dir,
			HttpServletRequest request) {
		logger.info("查询人员库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("查询人员库信息成功");
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
			// 判断当前用户是否有权限查询人员库信息
			if (list != null && list.size() > 0) {
				if ("1x0001".equals(list.get(0))) {
					// 读取全部
					String result=personStoreService.findPersonStore(field, fieldValue, startTime, endTime, nextPage, size,
							null, null, null,sortField,dir);
					//logger.info("=====查询到的符合条件的人员=====："+result);
					return result;
				} else if ("1x0010".equals(list.get(0))) { // 所属组织
					return personStoreService.findPersonStore(field, fieldValue, startTime, endTime, nextPage, size,
							null, sessionMgr.getUOID(request), null,sortField,dir);
				} else if ("1x0011".equals(list.get(0))) { // 所属部门
					return personStoreService.findPersonStore(field, fieldValue, startTime, endTime, nextPage, size,
							null, null, sessionMgr.getUDID(request),sortField,dir);
				} else if ("1x0100".equals(list.get(0))) { // 个人数据
					return personStoreService.findPersonStore(field, fieldValue, startTime, endTime, nextPage, size,
							sessionMgr.getUID(request), null, null,sortField,dir);
				} else {// 禁止读取
					return new ViewObject(ViewObject.RET_FAILURE, "没有权限读取").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "查询失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "查询人员库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "查询失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	/**
	 * 判断是否有相同的人名
	 * @param personName
	 * @return
	 */
	@RequestMapping(value = "/findPersonStoreByPersonName.do", method = RequestMethod.POST)
	@ResponseBody
	public String findPersonStoreByPersonName(@RequestParam(value = "personName", required = false) String personName){
		ViewObject viewObject=null;
		try {
			if (personStoreService.findPersonStoreByPersonName(personName)) {
				viewObject=new ViewObject(ViewObject.RET_SUCCEED, "exist");
			}else{
				viewObject=new ViewObject(ViewObject.RET_SUCCEED, "not_exist");
			}
		} catch (Exception e) {
			viewObject=new ViewObject(ViewObject.RET_ERROR, "后台出现异常");
			e.printStackTrace();
			logger.error("获得人员失败");
		}
		return viewObject.toJSon();
	}

	/**
	 * 将出生日期转成标准格式yyyy-MM-dd
	 * @param bebornTime
	 * @return
	 */
	private String setPersonBerBornTime(String bebornTime){
		try {
			//读取配置文件中日期的格式
			String []parsePatterns=Configured.getInstance().get("parsePatterns").split(",");
			Date date=DateUtils.parseDate(bebornTime, parsePatterns);
			return DateFormatUtils.format(date, "yyyy-MM-dd");
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 新增人员库信息控制器 isClue为后添加的,主要是判断是否是添加线索里的
	 * 
	 * @return
	 */
	@RequestMapping(value = "/savePersonStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String savePersonStore(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "personName", required = false) String personName,
			@RequestParam(value = "nameUsedBefore", required = false) String nameUsedBefore,
			@RequestParam(value = "englishName", required = false) String englishName,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "bebornTime", required = false) String bebornTime,
			@RequestParam(value = "registerAddress", required = false) String registerAddress,
			@RequestParam(value = "presentAddress", required = false) String presentAddress,
			@RequestParam(value = "workUnit", required = false) String workUnit,
			@RequestParam(value = "antecedents", required = false) String antecedents,
			@RequestParam(value = "activityCondition", required = false) String activityCondition,
			@RequestParam(value = "infoType", required = false) String infoType,
			@RequestParam(value = "certificatestype", required = false) String certificatestype,
			@RequestParam(value = "networkaccounttype", required = false) String networkaccounttype,
			@RequestParam(value = "isClue", required = false) String isClue,
			// 没关联人员为0
			@RequestParam(value = "state", required = false) String state,
			// 线索人员的随机号
			@RequestParam(value = "cluePersonNum", required = false) String cluePersonNum,
			//是否是新增多个
			@RequestParam(value = "multiple", required = false) String multiple,
			//传过来的多条数据
			@RequestParam(value = "datas", required = false) String datas,
			//开始的行号
			@RequestParam(value = "startRowNumber", required = false) Integer startRowNumber) {
		logger.info("新增人员库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("新增人员库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			//把可以入库的人员添加到list中
			List<PersonStore> personStoreList=new ArrayList<>();
			// 判断当前用户是否有权限新增人员库信息
			if (list != null && list.size() > 0) {
				// 启用
				if ("0x0001".equals(list.get(0))) {
					if (null!=multiple&&multiple.equals("multiple")) {
						
						//导入Excel的方式新增多条数据
						String result=personStoreService.saveMuiltiPersonStore(request,startRowNumber,datas);
						return new ViewObject(ViewObject.RET_SUCCEED, result).toJSon();
						/*
						 * 不要再controller中进行循环查询
						 * 最后导致hibernate假死,合理的方法是在service层中调用
						 * for (int i = 0; i < 2000; i++) {
							System.err.println("第"+i+"条canBeSaved："+personStoreService.canBeSaved("董昊", Double.toString(Math.random()*2016)
									,  "1989-11-09", "男"));
						}*/
						
					}else{
						// 判断当前新增人员库数据的人员名称是否存在数据库中，如果存在提示用户是否继续新增，如果不存在直接新增
						PersonStore store = new PersonStore();
						// 设置action,新增为1
						store.setAction("1");
						// 默认启用为1
						store.setIsEnable("2");
						// 得到上传照片的路径
						String personStoreImagePath = personStoreImagePath(number, request, response);
						store.setPhotofraphWay(personStoreImagePath);
						// 得到上传附件的路径
						String personStoreFilePath = personStoreFilePath(number, request, response);
						store.setAppendix(personStoreFilePath);

						store.setActivityCondition(activityCondition);// 活动情况
						store.setAntecedents(antecedents);// 履历
						// 生日
						store.setBebornTime(setPersonBerBornTime(bebornTime));
						store.setEnglishName(englishName);// 英文名
						//性别
						if(StringUtils.equals(sex, "男")||StringUtils.equals(sex, "女")){
							store.setSex(sex);
						}else{
							store.setSex(null);
						}
						store.setPersonName(personName);// 名字
						store.setNameUsedBefore(nameUsedBefore);// 曾用名
						store.setDid(sessionMgr.getUDID(request));
						store.setUid(sessionMgr.getUID(request));
						store.setOid(sessionMgr.getUOID(request));
						store.setRegisterAddress(registerAddress);// 户籍地址
						store.setPresentAddress(presentAddress);// 现住地址
						store.setCreateDate(StringUtil.dates());
						store.setWorkUnit(workUnit);// 工作单位
						// 人员类型
						store.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName(infoType, StoreFinal.PERSON_STORE));
						// 多个证件
						Set<CertificatesStore> certificatesStores = new HashSet<CertificatesStore>();
						// 把json转成对象
						JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(certificatestype);
						List li = (List) JSONSerializer.toJava(jsonArray);
						for (Object object : li) {
							JSONObject jsonObject = JSONObject.fromObject(object);
							// 获得实例
							// 这个bean不是CertificatesStore
							MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
							CertificatesStore c = new CertificatesStore();
							c.setCertificatesNumber((String) bean.get("certificatesTypeNumber"));
							// 获得证件类型
							InfoType cInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(
									(String) bean.get("certificatesType"), StoreFinal.CERTIFICATES_STORE);
							c.setInfoType(cInfoType);
							// 设置关联人员
							c.setPersonStore(store);
							certificatesStores.add(c);
						}
						store.setCertificatesStores(certificatesStores);
						// 多个网络账号
						Set<NetworkAccountStore> networkAccountStores = new HashSet<NetworkAccountStore>();
						// 从json转到对象
						JSONArray networkaJsonArray = (JSONArray) JSONSerializer.toJSON(networkaccounttype);
						List li2 = (List) JSONSerializer.toJava(networkaJsonArray);
						for (Object object : li2) {
							JSONObject jsonObject = JSONObject.fromObject(object);
							MorphDynaBean bean = (MorphDynaBean) jsonObject.toBean(jsonObject);
							NetworkAccountStore n = new NetworkAccountStore();
							n.setNetworkNumber((String) bean.get("networkAccountTypeNumber"));
							// 获得网络账号类型
							InfoType cInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(
									(String) bean.get("networkAccountType"), StoreFinal.NETWORK_ACCOUNT_STORE);
							n.setInfoType(cInfoType);
							// 关联的人员
							n.setPersonStore(store);
							networkAccountStores.add(n);
						}
						store.setNetworkAccountStores(networkAccountStores);
						// 增加人员
						// 首先添加到人员库里面
						boolean result = personStoreService.savePersonStore(store);
						logger.info("是否为线索人员isClue:" + isClue);
						if (isClue.equals("isClue")) {
							// 新增线索人员
							// 然后添加到临时线索表temp_clue_data里面
							TempClueData tempClueData = new TempClueData();
							// 正在操作的人员
							tempClueData.setAction(cluePersonNum);
							// 关联刚刚新增的这个人员
							tempClueData.setDataId(store.getId());
							// 名称
							tempClueData.setName(store.getPersonName());
							// 已关联
							tempClueData.setState("1");
							// 类型
							tempClueData.setType("personStore");
							// 新增临时线索里面
							if (state != null && state.equals("0")) {
								// 这代表没有关联人员需要更新
								tempClueDataService.updateTempClueData(tempClueData);
							} else {
								tempClueDataService.saveTempClueData(tempClueData);
							}
							logger.info("临时线索人员tempClueData" + tempClueData.toString());
						}
						logger.info("personstore:" + store.toString());
						// 成功或失败
						if (result) {
							return new ViewObject(ViewObject.RET_SUCCEED, "新增人员成功").toJSon();
						} else {
							return new ViewObject(ViewObject.RET_FAILURE, "新增人员失败").toJSon();
						}
					}
				}
			}
			return new ViewObject(ViewObject.RET_ERROR, "新增失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "新增人员库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_ERROR, "异常信息,新增失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}
	
	/**
	 * 保存未入库的详细信息到txt中
	 * @param result
	 * @param date
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/savePersonStoreDetail.do",method=RequestMethod.POST)
	@ResponseBody
	public String savePersonStoreDetail(
			@RequestParam("result") String result,
			@RequestParam("date") String date,
			HttpServletRequest request,
			HttpServletResponse response) {
		String root = request.getServletContext().getRealPath("/");
		//文件名
		String path = root + date+".txt";
		try {
			// 写入信息
			String []results=result.split("<br>");
			StringBuffer buffer=new StringBuffer();
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
	 * @param date
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadPersonStoreDetail.do",method=RequestMethod.GET)
	@ResponseBody
	public String downloadPersonStoreDetail(
			@RequestParam("date") String date,
			HttpServletRequest request,
			HttpServletResponse response) {
		ServletOutputStream out = null;
		String root = request.getServletContext().getRealPath("/");
		//文件名
		String path = root + date+".txt";
		try {
			// 下载附件
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头
			response.setHeader("Content-Disposition", "attachment;fileName=" + date+".txt");
			// 要下载的文件地址
			out = response.getOutputStream();
			//使用IOUtils
			File file=new File(path);
			IOUtils.write(FileUtils.readFileToByteArray(file), out);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(out);
			try {
				//删除
				FileUtils.forceDelete(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ViewObject(ViewObject.RET_FAILURE, "下载失败,请联系管理员").toJSon();
	}

	/**
	 * 人员入库
	 * 
	 * @return
	 */
	@RequestMapping(value = "/dataStoragePersonStoreById.do", method = RequestMethod.POST)
	@ResponseBody
	public String dataStoragePersonStoreById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "personName", required = false) String personName,
			@RequestParam(value = "nameUsedBefore", required = false) String nameUsedBefore,
			@RequestParam(value = "englishName", required = false) String englishName,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "bebornTime", required = false) String bebornTime,
			@RequestParam(value = "registerAddress", required = false) String registerAddress,
			@RequestParam(value = "presentAddress", required = false) String presentAddress,
			@RequestParam(value = "workUnit", required = false) String workUnit,
			@RequestParam(value = "antecedents", required = false) String antecedents,
			@RequestParam(value = "activityCondition", required = false) String activityCondition,
			@RequestParam(value = "infoType", required = false) String infoType,
			@RequestParam(value = "certificatestype", required = false) String certificatestype,
			@RequestParam(value = "networkaccounttype", required = false) String networkaccounttype,
			@RequestParam("typeId") String typeId, @RequestParam("id") String id) {
		logger.info("人员入库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("人员入库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {

			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			PersonStore store = new PersonStore();
			store.setId(id);
			// 设置action,新增为1
			store.setAction("1");
			// 默认启用为1
			store.setIsEnable("2");
			// 得到上传照片的路径
			String personStoreImagePath = personStoreImagePath(number, request, response);
			store.setPhotofraphWay(personStoreImagePath);
			// 得到上传附件的路径
			String personStoreFilePath = personStoreFilePath(number, request, response);
			store.setAppendix(personStoreFilePath);

			store.setActivityCondition(activityCondition);// 活动情况
			store.setAntecedents(antecedents);// 履历
			store.setBebornTime(setPersonBerBornTime(bebornTime));
			store.setEnglishName(englishName);// 英文名
			//性别
			if(StringUtils.equals(sex, "男")||StringUtils.equals(sex, "女")){
				store.setSex(sex);
			}else{
				store.setSex(null);
			}
			store.setPersonName(personName);// 名字
			store.setNameUsedBefore(nameUsedBefore);// 曾用名
			store.setDid(sessionMgr.getUDID(request));
			store.setUid(sessionMgr.getUID(request));
			store.setOid(sessionMgr.getUOID(request));
			store.setRegisterAddress(registerAddress);// 户籍地址
			store.setPresentAddress(presentAddress);// 现住地址
			store.setCreateDate(StringUtil.dates());
			store.setWorkUnit(workUnit);// 工作单位
			// 人员类型
			store.setInfoType(infoTypeService.findInfoTypeById(typeId));
			store.setIsToStorage("1");
			// 多个证件
			Set<CertificatesStore> certificatesStores = new HashSet<CertificatesStore>();
			// 把json转成对象
			JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(certificatestype);
			List li = (List) JSONSerializer.toJava(jsonArray);
			for (Object object : li) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 获得实例
				// 这个bean不是CertificatesStore
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				CertificatesStore c = new CertificatesStore();
				c.setCertificatesNumber((String) bean.get("certificatesTypeNumber"));
				// 获得证件类型
				InfoType cInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(
						(String) bean.get("certificatesType"), StoreFinal.CERTIFICATES_STORE);
				c.setInfoType(cInfoType);
				// 设置关联人员
				c.setPersonStore(store);
				certificatesStores.add(c);
			}
			store.setCertificatesStores(certificatesStores);
			// 多个网络账号
			Set<NetworkAccountStore> networkAccountStores = new HashSet<NetworkAccountStore>();
			// 从json转到对象
			JSONArray networkaJsonArray = (JSONArray) JSONSerializer.toJSON(networkaccounttype);
			List li2 = (List) JSONSerializer.toJava(networkaJsonArray);
			for (Object object : li2) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				MorphDynaBean bean = (MorphDynaBean) jsonObject.toBean(jsonObject);
				NetworkAccountStore n = new NetworkAccountStore();
				n.setNetworkNumber((String) bean.get("networkAccountTypeNumber"));
				// 获得网络账号类型
				InfoType cInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(
						(String) bean.get("networkAccountType"), StoreFinal.NETWORK_ACCOUNT_STORE);
				n.setInfoType(cInfoType);
				// 关联的人员
				n.setPersonStore(store);
				networkAccountStores.add(n);
			}
			store.setNetworkAccountStores(networkAccountStores);
			// 增加人员
			// 首先添加到人员库里面
			personStoreService.updatePersonStore(store);
			// 成功或失败
			return new ViewObject(ViewObject.RET_SUCCEED, "人员入库成功").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "人员入库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "异常信息,人员入库失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 修改人员库信息控制器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updatePersonStoreById.do", method = RequestMethod.POST)
	@ResponseBody
	public String updatePersonStoreById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "personName", required = false) String personName,
			@RequestParam(value = "nameUsedBefore", required = false) String nameUsedBefore,
			@RequestParam(value = "englishName", required = false) String englishName,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "bebornTime", required = false) String bebornTime,
			@RequestParam(value = "registerAddress", required = false) String registerAddress,
			@RequestParam(value = "presentAddress", required = false) String presentAddress,
			@RequestParam(value = "workUnit", required = false) String workUnit,
			@RequestParam(value = "antecedents", required = false) String antecedents,
			@RequestParam(value = "activityCondition", required = false) String activityCondition,
			@RequestParam(value = "infoType", required = false) String infoType,
			@RequestParam(value = "certificatestype", required = false) String certificatestype,
			@RequestParam(value = "networkaccounttype", required = false) String networkaccounttype,
			@RequestParam(value = "appendix", required = false) String appendix,
			@RequestParam(value = "photo", required = false) String photo) {
		logger.info("修改人员库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("修改人员库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限修改人员库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {// 启用
					// 获得这个人员
					PersonStore store = personStoreService.findPersonStoreById(id);
					// 增加权限
					store.setUid(sessionMgr.getUID(request));
					store.setDid(sessionMgr.getUDID(request));
					store.setOid(sessionMgr.getUOID(request));
					// 修改
					store.setAction("2");
					store.setActivityCondition(activityCondition);// 活动情况
					store.setAntecedents(antecedents);// 履历
					//生日可为空
					store.setBebornTime(setPersonBerBornTime(bebornTime));
					store.setEnglishName(englishName);// 英文名
					//性别
					if(StringUtils.equals(sex, "男")||StringUtils.equals(sex, "女")){
						store.setSex(sex);
					}else{
						store.setSex(null);
					}
					store.setPersonName(personName);// 名字
					store.setNameUsedBefore(nameUsedBefore);// 曾用名
					store.setRegisterAddress(registerAddress);// 户籍地址
					store.setPresentAddress(presentAddress);// 现住地址
					store.setWorkUnit(workUnit);// 工作单位
					// 人员类型
					store.setInfoType(
							infoTypeService.findInfoTypeByTypeNameAndTableName(infoType, StoreFinal.PERSON_STORE));
					// 多个证件
					for (CertificatesStore certificatesStore : store.getCertificatesStores()) {
						// 删除原来的
						certificatesStoreService.deleteCertificatesByIds(new String[] { certificatesStore.getId() });
					}
					Set<CertificatesStore> certificatesStores = new HashSet<CertificatesStore>();
					// 把json转成对象
					JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(certificatestype);
					List li = (List) JSONSerializer.toJava(jsonArray);
					for (Object object : li) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						// 获得实例
						// 这个bean不是CertificatesStore
						MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
						CertificatesStore c = new CertificatesStore();
						c.setCertificatesNumber((String) bean.get("certificatesTypeNumber"));
						// 获得证件类型
						InfoType cInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(
								(String) bean.get("certificatesType"), StoreFinal.CERTIFICATES_STORE);
						c.setInfoType(cInfoType);
						// 设置关联人员
						c.setPersonStore(store);
						certificatesStores.add(c);
					}
					store.setCertificatesStores(certificatesStores);
					// 多个网络账号
					for (NetworkAccountStore networkAccountStore : store.getNetworkAccountStores()) {
						// 删除原来的
						networkAccountStoreService
								.deleteNetworkAccountByIds(new String[] { networkAccountStore.getId() });
					}
					Set<NetworkAccountStore> networkAccountStores = new HashSet<NetworkAccountStore>();
					// 从json转到对象
					JSONArray networkaJsonArray = (JSONArray) JSONSerializer.toJSON(networkaccounttype);
					List li2 = (List) JSONSerializer.toJava(networkaJsonArray);
					for (Object object : li2) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						MorphDynaBean bean = (MorphDynaBean) jsonObject.toBean(jsonObject);
						NetworkAccountStore n = new NetworkAccountStore();
						n.setNetworkNumber((String) bean.get("networkAccountTypeNumber"));
						// 获得网络账号类型
						InfoType cInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(
								(String) bean.get("networkAccountType"), StoreFinal.NETWORK_ACCOUNT_STORE);
						n.setInfoType(cInfoType);
						// 关联的人员
						n.setPersonStore(store);
						networkAccountStores.add(n);
					}
					store.setNetworkAccountStores(networkAccountStores);
					// 更新照片
					StringBuffer buffer = new StringBuffer();
					// buffer.append(store.getPhotofraphWay());
					if (photo != null && photo.trim().length() > 0) {
						// 有原来的还有刚刚上传的
						String[] photos = photo.split(",");
						for (String string : photos) {
							// 包含tempPersonStorePhotoUploadImages
							if (string.contains("tempPersonStorePhotoUploadImages")) {
								String personImage = personStoreImagePath(number, request, response);
								// 重新上传图片后
								if (personImage != null && personImage.length() > 1) {
									buffer.append(personImage.trim());
									buffer.append(",");
								}
							} else {
								// 原来就有的
								buffer.append(string.trim());
								buffer.append(",");
							}
						}
					}
					String photofraphWay = buffer.toString().trim();
					photofraphWay = photofraphWay.replace(" ", "");
					if (photofraphWay != null && photofraphWay.length() > 0) {
						// 去掉,号
						photofraphWay = photofraphWay.substring(0, photofraphWay.length() - 1);
					}
					logger.info("照片名称buffer:" + photofraphWay);
					store.setPhotofraphWay(photofraphWay);
					// 更新附件tempPersonStoreFileUpload_
					StringBuffer fileBuffer = new StringBuffer();
					// 文件夹
					String personStoreFile = Configured.getInstance().get("personStoreFile");
					if (appendix != null && appendix.trim().length() > 1) {
						String[] files = appendix.split(",");
						for (String file : files) {
							// 临时上传的
							if (file.contains("tempPersonStoreFileUpload_")) {
								String personFile = personStoreFilePath(number, request, response);
								// 重新上传文件后
								if (personFile != null && personFile.length() > 1) {
									fileBuffer.append(personFile);
									fileBuffer.append(",");
								}
							} else {
								// 这是原来的附件
								fileBuffer.append(personStoreFile + File.separator + file);
								fileBuffer.append(",");
							}
						}
					}
					String filePath = fileBuffer.toString().trim();
					filePath = filePath.replace(" ", "");
					if (filePath != null && filePath.length() > 0) {
						// 去掉,号
						filePath = filePath.substring(0, filePath.length() - 1);
					}
					store.setAppendix(filePath.trim());
					// 更新
					personStoreService.updatePersonStore(store);
					return new ViewObject(ViewObject.RET_FAILURE, "修改人员信息成功").toJSon();
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "修改失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "修改人员库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "异常信息,修改失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 删除人员库信息控制器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delPersonStoreById.do", method = RequestMethod.POST)
	@ResponseBody
	public String delPersonStoreById(@RequestParam("personStoreIds") String[] personStoreIds,
			HttpServletRequest request) {
		logger.info("删除人员库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("删除人员库信息成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			List<String> list = sessionMgr.getPermitResOperCode(request);
			// 判断当前用户是否有权限新增人员库信息
			if (list != null && list.size() > 0) {
				if ("0x0001".equals(list.get(0))) {
					// 启用
					return personStoreService.delPersonStoreByIds(personStoreIds);
				}
			}
			return new ViewObject(ViewObject.RET_FAILURE, "删除失败，没有权限").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "删除人员库信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "异常信息,删除失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 启用人员
	 * 
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/startPersonStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String startPesonStore(HttpServletRequest request, @RequestParam("ids") String[] ids) {
		logger.info("启用人员");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("启用人员成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			personStoreService.updatePersonStoreIsEnableStart(ids);
			return new ViewObject(ViewObject.RET_FAILURE, "启用人员信息成功！").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "启用人员信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "启用人员信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 禁用人员
	 * 
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/ceasePersonStore.do", method = RequestMethod.POST)
	@ResponseBody
	public String ceasePesonStore(HttpServletRequest request, @RequestParam("ids") String[] ids) {
		logger.info("禁用人员");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("禁用人员成功");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			personStoreService.updatePersonStoreIsEnableCease(ids);
			return new ViewObject(ViewObject.RET_FAILURE, "禁用人员信息成功！").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = "禁用人员信息失败";
			logger.error(msg, e);
			loginfo.setResult(msg + e.getMessage());
			ViewObject object = new ViewObject(ViewObject.RET_FAILURE, "禁用人员信息失败");
			return object.toJSon();
		} finally {
			log.log(loginfo);
		}
	}

	/**
	 * 根据人员库名称查询类别控制器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getInfoTypeByPersonStore.do", method = RequestMethod.GET)
	@ResponseBody
	public String getInfoTypeByPersonStore() {
		try {
			// 根据人员库名称查询类别
			return infoTypeService.getInfoTypeByTableTypeName(StoreFinal.PERSON_STORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据网络库名称查询类别控制器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getInfoTypeByNetworkAccountStore.do", method = RequestMethod.GET)
	@ResponseBody
	public String getInfoTypeByNetworkAccountStore() {
		try {
			// 根据人员库名称查询类别
			return infoTypeService.getInfoTypeByTableTypeName(StoreFinal.NETWORK_ACCOUNT_STORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据证件库名称查询类别控制器
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getInfoTypeByCertificatesStore.do", method = RequestMethod.GET)
	@ResponseBody
	public String getInfoTypeByCertificatesStore() {
		try {
			// 根据人员库名称查询类别
			return infoTypeService.getInfoTypeByTableTypeName(StoreFinal.CERTIFICATES_STORE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传审核任务附件
	 * 
	 * @param request
	 * @param response
	 * @return 返回文件名字
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/personStoreFileUpload.do", method = RequestMethod.POST)
	public @ResponseBody String personStoreFileUpload(@RequestParam("number") String number, HttpServletRequest request,
			HttpServletResponse response) {
		String path = request.getSession().getServletContext()
				.getRealPath(File.separator + "tempPersonStoreFileUpload" + number);
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
				if (fileItem.getName() != null && fileItem.getName().length() > 0) {
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
	 * 临时上传的附件
	 * 
	 * @return 2015-12-21
	 */
	@RequestMapping(value = "/findPersonStoreTempFile.do", method = RequestMethod.GET)
	@ResponseBody
	public String findExamineTempFile(HttpServletRequest request, @RequestParam("number") String number,
			@RequestParam("appendix") String appendix, HttpServletResponse response) {
		// 获得正在上传的文件
		List<String> tempList = findExamineTempFile(number, request, response);
		try {
			if (appendix != null && appendix.length() > 1) {
				// 修改人员的附件
				// 先把原来的列举出来
				appendix = URLDecoder.decode(appendix, "utf-8");
				String personStoreFile = Configured.getInstance().get("personStoreFile");
				String appendixPath = request.getServletContext().getRealPath(File.separator + personStoreFile);
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
		String personStoreFile = Configured.getInstance().get("personStoreFile");
		for (String s : examineList) {
			JSONObject obj = new JSONObject();
			obj.put("filePath", s);

			if (s.contains("tempPersonStoreFileUpload_")) {
				// 代表临时上传的文件
				int index = s.lastIndexOf("_");
				s = s.substring(index + 1, s.length());
			}

			obj.put("fileName", s);
			obj.put("fileTempName", personStoreFile + File.separator + s);
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
	 * @return 2015-10-28
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
		String tpath = serc.getRealPath(File.separator + "tempPersonStoreFileUpload" + number);
		try {
			File tempFiles = new File(tpath);// 临时文件夹
			if (tempFiles.exists()) {
				tempFile = tempFiles.listFiles();
				for (File file : tempFile) {
					// 临时文件
					list.add("tempPersonStoreFileUpload_" + file.getName());
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
	 * 删除上传的临时文件
	 * 
	 * @param request
	 * @param response
	 * @param fileName
	 * @param number
	 * @return 2015-12-21
	 */
	@RequestMapping(value = "/deletePersonStoreFile.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteExamineFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("fileName") String fileName, @RequestParam("number") String number) {
		// 返回所有临时文件的名称
		File tempFile[] = null;
		ServletContext serc = request.getSession().getServletContext();
		String tpath = serc.getRealPath(File.separator + "tempPersonStoreFileUpload" + number);
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
	 * 删除临时上传的照片
	 * 
	 * @param request
	 * @param response
	 * @param src
	 *            资源路径
	 *            例如tempPersonStorePhotoUploadImages70/2016052016165732691.png
	 * @return
	 */
	@RequestMapping(value = "/deletePersonStorePhoto.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteExaminePhoto(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("src") String src) {
		try {
			// 照片所在文件夹
			ServletContext context = request.getServletContext();
			int index = -1;
			index = src.lastIndexOf("/");
			String tempFolder = null;
			String tempPhoto = null;
			if (index > -1) {
				tempFolder = src.substring(0, index);
				tempPhoto = src.substring(index + 1, src.length());
			} else {
				index = src.lastIndexOf("\\");
				if (index > -1) {
					tempFolder = src.substring(0, index);
					tempPhoto = src.substring(index + 1, src.length());
				}
			}
			// 临时上传的照片
			if (src.contains("tempPersonStorePhotoUploadImages")) {
				File photoDir = new File(context.getRealPath(File.separator + tempFolder));
				if (photoDir.exists()) {
					for (File photo : photoDir.listFiles()) {
						if (photo.getName().equals(tempPhoto)) {
							// 删掉
							photo.delete();
							break;
						}
					}
				}
			}

			return new ViewObject(ViewObject.RET_SUCCEED, "删除成功").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_SUCCEED, "删除失败").toJSon();
		}
	}

	/**
	 * 增加上传临时图片
	 * 
	 * @param request
	 * @param response
	 * @return 返回文件名字
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/personStorePhototUpload.do", method = RequestMethod.POST)
	public @ResponseBody String personStorePhototUpload(@RequestParam("number") String number,
			HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext()
				.getRealPath(File.separator + "tempPersonStorePhotoUploadImages" + number);
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
				// fileItem.getName()!=null&&fileItem.getName().length()>0
				// (!"null".equals(fileItem.getName())||null!=fileItem.getName())
				if (fileItem.getName() != null && fileItem.getName().length() > 0) {
					int index = fileItem.getName().lastIndexOf(".");
					String postfix = fileItem.getName().substring(index);
					fileName = new UpLoadUtil().getTpTime() + postfix;
					File file = new File(path + File.separator + fileName);
					fileItem.write(file);
					fileItem.delete();
				}
			}

		} catch (Exception e) {
			String msg = "文件上传出错";
			logger.error(msg + e);
			return new ViewObject(-1, msg).toJSon();
		}
		return new ViewObject(ViewObject.RET_SUCCEED, fileName).toJSon();
	}

	/**
	 * 得到当前上传附件的路径
	 * 
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	public static String personStoreFilePath(String number, HttpServletRequest request, HttpServletResponse response) {
		File tempFile[] = null;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		ServletContext serc = request.getSession().getServletContext();
		// 获得临时上传附件的路径
		String tpath = serc.getRealPath(File.separator + "tempPersonStoreFileUpload" + number);
		// 人员库附件存放的文件夹名称
		// 读取配置文件里的属性
		String personStoreFile = Configured.getInstance().get("personStoreFile");
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
					/*
					 * fi = new FileInputStream(file); fo = new
					 * FileOutputStream(lpath + File.separator+ file.getName());
					 * in = fi.getChannel(); out = fo.getChannel(); //
					 * 连接两个通道,从in中读取,然后写入out in.transferTo(0, in.size(), out);
					 */
					org.apache.commons.io.FileUtils.copyFileToDirectory(file, fileT);
					list.add(personStoreFile + File.separator + file.getName());
				}
				s = list.toString();
				s = s.replace("[", "");
				s = s.replace("]", "");
			}
			return s;
		} catch (Exception e) {
			logger.info("异常信息,获得上传附加路径失败:" + e.getMessage());
		} finally {
			IOUtils.closeQuietly(fi);
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(fo);
			IOUtils.closeQuietly(out);
		}
		return null;
	}

	/**
	 * 得到当前上传的图片的路径
	 * @param number
	 * @param request
	 * @param response
	 * @return 
	 */
	public static String personStoreImagePath(String number, HttpServletRequest request, HttpServletResponse response) {
		File tempFile[] = null;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		ServletContext serc = request.getSession().getServletContext();
		// 获得临时上传照片的路径
		String tpath = serc.getRealPath(File.separator + "tempPersonStorePhotoUploadImages" + number);
		// 人员库照片存放文件夹名称路径
		// 通过配置文件获得
		String personStoreImg = Configured.getInstance().get("personStoreImg");
		String lpath = serc.getRealPath(File.separator + personStoreImg);
		File fileT = new File(lpath);
		if (!fileT.exists()) {
			fileT.mkdirs();
		}
		try {
			String s = "";
			List<String> lsit = new ArrayList<String>();
			File tempFiles = new File(tpath);// 临时文件夹
			if (tempFiles.exists()) {
				tempFile = tempFiles.listFiles();
				for (File file : tempFile) {
					fi = new FileInputStream(file);
					fo = new FileOutputStream(lpath + File.separator + file.getName());
					in = fi.getChannel();// 得到对应的文件通道
					out = fo.getChannel();// 得到对应的文件通道
					in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
					lsit.add(personStoreImg + File.separator + file.getName());
				}
				s = lsit.toString();
				s = s.replace("[", "");
				s = s.replace("]", "");
				// logger.info("==临时照片=="+s);
			}
			return s.trim();
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
	 * 获得人员的照片路径 绝对路径
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPersonPhotoRealPath.do", method = RequestMethod.GET)
	@ResponseBody
	public String personStorePhotoRealPath(@RequestParam(value = "id") String id, HttpServletRequest request) {
		try {
			PersonStore store = personStoreService.findPersonStoreById(id);
			// 照片
			String photofgraph_way = store.getPhotofraphWay();
			// json字符串
			return getPhotoRealPath(photofgraph_way, request);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得人员的附件
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPersonAttachRealPath.do", method = RequestMethod.GET)
	@ResponseBody
	public String personAttachRealPath(@RequestParam(value = "id") String id, HttpServletRequest request) {
		try {
			PersonStore store = personStoreService.findPersonStoreById(id);
			// 附件
			String attach = store.getAppendix();
			return getAttachRealPath(attach);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 下载附件
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downloadPersonAttach.do")
	public String downloadPersonAttach(@RequestParam("src") String src, HttpServletRequest request,
			HttpServletResponse response) {
		InputStream is = null;
		OutputStream os = null;
		try {
			response.setContentType("multipart/form-data");
			String personStoreFile = Configured.getInstance().get("personStoreFile");
			String fileName = request.getSession().getServletContext()
					.getRealPath(File.separator + personStoreFile + File.separator + URLDecoder.decode(src, "utf-8"));
			logger.info("下载的附件解码src:" + URLDecoder.decode(src, "utf-8"));
			logger.info("下载的附件fileName:" + fileName);
			File file = new File(fileName);
			if (file.exists()) {
				// 设置文件名
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

	/**
	 * 根据数据库里的路径获得真实路径
	 * 
	 * @param data
	 *            数据库字段里的值
	 * @return 转成json字符串
	 */
	public String getPhotoRealPath(String data, HttpServletRequest request) {
		JSONObject object = new JSONObject();
		JSONArray array = new JSONArray();
		if (data != null && data.trim().length() > 0) {
			// ServletContext context=request.getSession().getServletContext();
			String[] strings = data.split(",");
			for (String str : strings) {
				if (str.trim().length() > 1) {
					ServletContext context = request.getServletContext();
					// 得到照片真实路径
					String filePath = context.getRealPath(File.separator + str.trim());
					File file = new File(filePath);
					if (file.exists()) {
						// 照片存在
						// 直接拿数据库里的值就行
						object.put("src", str);
						array.add(object);
					}
				}
			}
		}
		return array.toString();
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
			String[] strings = data.split(",");
			for (String str : strings) {
				if (str.trim().length() > 1) {
					int index = str.lastIndexOf(File.separator);
					str = str.substring(index + 1, str.length());
					index = str.lastIndexOf("_");
					String name = str.substring(index + 1, str.length());
					object.put("src", str);
					object.put("name", name);
					array.add(object);
				}
			}
		}
		return array.toString();
	}

	/**
	 * 下载人员信息的PDF文件
	 * 
	 * @param request
	 * @param name
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadPersonPDFFile.do")
	@ResponseBody
	public String dwonExportData(HttpServletRequest request, 
			@RequestParam("personId") String personId,
			HttpServletResponse response) {
		ServletOutputStream out = null;
		String root = request.getServletContext().getRealPath("/");
		//文件名
		String path = root + personId + ".doc";
		try {
			// 获取服务器地址
			//String path = servletContext.getRealPath("/") + "PDFFilePath/";
			//生成word
			personStoreService.outputPersonStoreToWord(personId, path);
			// 下载附件
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头
			response.setHeader("Content-Disposition", "attachment;fileName=" + personId + ".doc");
			// 要下载的文件地址
			out = response.getOutputStream();
			//使用IOUtils
			File file=new File(path);
			IOUtils.write(FileUtils.readFileToByteArray(file), out);
			
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

	// 数据导出到Excel中去
	@RequestMapping(value="/personToExcel.do",method=RequestMethod.POST)
	@ResponseBody
	public String personToExcel(@RequestParam("personStoreIds") String personStoreIds,
			@RequestParam("date") String date, 
			HttpServletRequest request,
			HttpServletResponse response) {
		// personStoreService.
		logger.info("导出人员库信息");
		com.tdcq.common.logging.Logger log = LogFactory.getLogger();
		LogInfo loginfo = new LogInfo();
		loginfo.setApplication("test");
		loginfo.setUri(request.getRequestURI());
		loginfo.setClientIP(request.getRemoteAddr());
		loginfo.setLogTime(new Date());
		loginfo.setResult("导出人员库信息");
		loginfo.setOperationType(com.tdcq.common.logging.Logger.LOG_OPERATION_TYPE_UPDATE);
		String msg="导出人员信息成功,请点击下载";
		int status=ViewObject.RET_SUCCEED;
		try {
			// 获取用户的登录信息
			UserSessionMgr sessionMgr = UserSessionMgr.getInstance();
			loginfo.setUserName(sessionMgr.getTrueName(request));
			loginfo.setUserCode(sessionMgr.getCode(request));
			// 获得人员的信息写入到excel
			String []ids=personStoreIds.split(",");
			logger.info("personStoreIds:"+Arrays.toString(ids));
			String root = request.getServletContext().getRealPath("/");
			//文件名
			String path = root + date + ".xls";
			File file=new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			//输出到excel中
			personStoreService.outputPersonStoreToExcel(path, ids);
		} catch (Exception e) {
			e.printStackTrace();
			msg="导出人员信息失败";
			status=ViewObject.RET_FAILURE;
			loginfo.setResult("导出人员信息失败" + e.getMessage());
		} 
		return  new ViewObject(status, msg).toJSon();
	}
	/**
	 * 下载人员的excel
	 * @param date
	 * @param request
	 * @param response
	 */
	@RequestMapping("/downLoadPersonExcel.do")
	public void downLoadPersonExcel(@RequestParam("date") String date,
			HttpServletRequest request,
			HttpServletResponse response){
		ServletOutputStream out = null;
		String root = request.getServletContext().getRealPath("/");
		//文件名
		String path = root + date + ".xls";
		try {
			// 下载附件
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头
			response.setHeader("Content-Disposition", "attachment;fileName=" + date + ".xls");
			// 要下载的文件地址
			out = response.getOutputStream();
			//使用IOUtils
			File file=new File(path);
			IOUtils.write(FileUtils.readFileToByteArray(file), out);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				//关闭流
				IOUtils.closeQuietly(out);
				//删掉该文件
				FileUtils.forceDelete(new File(path));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 上传并识别人员的Excel
	 * @param number
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/identifyPersonInfoExcel.do", method = RequestMethod.POST)
	public @ResponseBody String identifyPersonInfoExcel(
			@RequestParam("number") String number,
			HttpServletRequest request, HttpServletResponse response) {
		String msg= "读取文件内容失败";
		int status=ViewObject.RET_ERROR;//-1
		String path = request.getSession().getServletContext().getRealPath(File.separator + "tempPersonInfoExcel" + number);
		// 如果文件夹不存在则创建文件夹
		File fileT = new File(path);
		if (!fileT.exists()) {
			fileT.mkdirs();
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		File file=null;
		try {
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem fileItem : list) {
				//非表单标签
				if (!fileItem.isFormField()) {
					//后缀
					String postfix = FilenameUtils.getExtension(fileItem.getName());
					String fileName = new UpLoadUtil().getTpTime() + "."+postfix;
					file = new File(path + File.separator + fileName);
					fileItem.write(file);
					fileItem.delete();
					
				}
			}
			//读取该Excel文件的内容并返回
			String result=CreatAndReadExcel.readPersonInfoExcel(file);
			//result内容不能只为{}
			if (null!=result&&result.length()>2) {
				msg=result;
				status=ViewObject.RET_SUCCEED;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(msg + e);
		}finally{
			try {
				//删除临时上传的文件夹
				File tempDir=new File(path);
				FileUtils.deleteDirectory(tempDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ViewObject(status, msg).toJSon();
	}
	/**
	 * 用于获取配置文件中的导入人员表格的标准格式
	 * @return
	 */
	@RequestMapping(value="/getPersonStoreExcelHeader.do",method=RequestMethod.POST)
	@ResponseBody
	public String getPersonStoreExcelHeader(){
		JSONArray headers=new JSONArray();
		try {
			String url=PathUtils.getConfigPath(this.getClass())+"person-store-excel.xml";
			XmlUtils utils=new XmlUtils(url);
			Element element=utils.getNode("header");
			List<Element> elements=element.elements();
			for (Element node : elements) {
				JSONObject header=new JSONObject();
				header.put("text", node.getText());
				header.put("dataIndex", utils.getNodeAttrVal(node, "name"));
				headers.add(header);
			}
			logger.info(headers.toString());
		} catch (Exception e) {
			
			logger.error("获得标准人员表格的表头失败");
		}
		return headers.toString();
	}
	/********************************
	 * OpenSessionInView导致的一直创建新的session，不关闭最终导致session不足；
	 * 解决方式是不要在request里面进行循环读取，放到service层里
	 * *****************************/
	//@RequestMapping(value="/findPersonTest.do",method=RequestMethod.GET)
	//@ResponseBody
	public String findPersonTest(@RequestParam(value="count")int count) {
		String result="success";
		try {
			for (int i = 0; i < count; i++) {
				personStoreService.findPersonStoreById("40288a625624b632015624b647300005");
				System.err.println("第"+i+":"+personStoreService.findPersonStoreById("40288a625624b632015624b647300005").getPersonName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
