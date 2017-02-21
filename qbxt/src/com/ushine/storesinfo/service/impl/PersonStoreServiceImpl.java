package com.ushine.storesinfo.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.ParagraphProperties;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.dom4j.Element;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.common.config.Configured;
import com.ushine.common.utils.PathUtils;
import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.dao.IBaseDao;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IPersonStoreSolrService;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.NetworkAccountStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.IPersonStoreService;
import com.ushine.storesinfo.storefinal.StoreFinal;
import com.ushine.util.CustomXWPFDocument;
import com.ushine.util.StringUtil;
import com.ushine.util.XmlUtils;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
/**
 * 人员库接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service(value="personStoreServiceImpl")
public class PersonStoreServiceImpl implements IPersonStoreService{
	private static final Logger logger = LoggerFactory.getLogger(PersonStoreServiceImpl.class);
	@Autowired private IBaseDao<PersonStore, String> baseDao;
	@Autowired private IBaseDao iBaseDao;
	@Autowired private IBaseDao personNamesDao;
	@Autowired private IInfoTypeService infoTypeService;
	@Autowired private IPersonStoreSolrService solrService;
	@Autowired IPersonStoreSolrService personStoreSolrService;
	// 单例的solr server
	private HttpSolrServer server = SolrServerFactory.getPSSolrServerInstance();
	//private PersonStoreNRTSearch personStoreNRTSearch=PersonStoreNRTSearch.getInstance();
	public boolean savePersonStore(PersonStore personStore) throws Exception {
		//新增人员
		baseDao.save(personStore);
		//solr添加索引
		solrService.addDocumentByStore(server, personStore);
		return true;
	}
	public PersonStore findPersonStoreById(String personStoreId)
			throws Exception {
		return baseDao.findById(PersonStore.class, personStoreId);
	}
	
	@SuppressWarnings("unchecked")
	public String findPersonStore(String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size,
			String uid, String oid, String did,String sortField,String dir) throws Exception {
		logger.info("筛选人员库信息,field:"+field+",fieldValue:"+fieldValue+",startTime:"+startTime+"endTime:"+startTime+"");
		//查询
		if (StringUtils.equals(field, "anyField")) {
			//任意字段查询
			field=QueryBean.PERSONSTOREALL;
		}
		QueryBean queryBean=new QueryBean(uid, oid, did, field, fieldValue, null, null, sortField,dir, startTime, endTime);
		//查询总数
		long totalRecord = personStoreSolrService.getDocumentsCount(server, queryBean);
		Paging paging = new Paging(size, nextPage, totalRecord);
		PagingObject<PersonStoreVo> vo = new PagingObject<>();
		vo.setPaging(paging);
		// 集合
		// nextPage从1开始
		List<PersonStoreVo> array = personStoreSolrService.getDocuementsVO(server, queryBean, (nextPage - 1) * size, size);
		if (StringUtils.isNotBlank(fieldValue)) {
			// 有关键字要高亮
			List<PersonStoreVo> highlightArray = SolrBeanUtils.highlightVoList(array, PersonStoreVo.class, fieldValue);
			vo.setArray(highlightArray);
		} else {
			vo.setArray(array);
		}
		return convertPersonStoreVoToJson(vo);
	}
	
	private String convertPersonStoreVoToJson(PagingObject<PersonStoreVo> vo){
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		//转json
		String datas=JSONArray.fromObject(vo.getArray()).toString();
		root.element("datas", datas);
		//logger.info("datas:"+datas);
		return root.toString();
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String voToJson(PagingObject<PersonStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (PersonStore PersonStore : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", PersonStore.getId());
			/*if(PersonStore.getIsEnable().trim().equals("2")){
				obj.put("isEnable", "是");
			}
			if(PersonStore.getIsEnable().trim().equals("1")){
				obj.put("isEnable", "否");
			}*/
			obj.put("isEnable",PersonStore.getIsEnable());
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
			obj.put("createDate", PersonStore.getCreateDate());
			//为空的情况
			if(PersonStore.getInfoType()!=null){
				//类别可能被删除
				//人员关联的类别就为空了
				obj.put("infoType", PersonStore.getInfoType().getTypeName());
			}
			//附件
			obj.put("appendix", PersonStore.getAppendix());
			//照片
			obj.put("photo", PersonStore.getPhotofraphWay());
			//证件
			Set<CertificatesStore> set1=PersonStore.getCertificatesStores();
			JSONArray cjArray=new JSONArray();
			for (CertificatesStore c : set1) {
				JSONObject cObject=new JSONObject();
				//为空
				if(c.getInfoType()!=null&& c.getInfoType().getTypeName().trim().length()>0){
					cObject.put("certificatesType", c.getInfoType().getTypeName());
					cObject.put("certificatesTypeNumber", c.getCertificatesNumber());
					//需要放到array中
					cjArray.add(cObject);
					obj.put("certificates",cjArray.toString());
				}
				//cObject.put("certificatesType", c.getInfoType().getTypeName());
				//cObject.put("certificatesTypeNumber", c.getCertificatesNumber());
			}
			//网络账号
			Set<NetworkAccountStore> set=PersonStore.getNetworkAccountStores();
			JSONArray nArray=new JSONArray();
			for (NetworkAccountStore networkAccountStore : set) {
				JSONObject networkObject=new JSONObject();
				if(networkAccountStore.getInfoType()!=null&& networkAccountStore.getInfoType().getTypeName().trim().length()>0){
					networkObject.put("networkAccountType", networkAccountStore.getInfoType().getTypeName());
					networkObject.put("networkAccountTypeNumber", networkAccountStore.getNetworkNumber());
					//networkObject.put("networkAccountType", networkAccountStore.getInfoType().getTypeName());
					//networkObject.put("networkAccountTypeNumber", networkAccountStore.getNetworkNumber());
					nArray.add(networkObject);
					obj.put("networkaccount",nArray.toString());
				}
			}
			//obj.put("networkaccout", JSONArray.fromObject(PersonStore.getNetworkAccountStores()).toString());
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}

	public String delPersonStoreByIds(String[] personStoreIds) {
		//删除多个人员
		try {
			StringBuffer buffer = new StringBuffer("update PersonStore set action='3' where ");
			for (String string : personStoreIds) {
				buffer.append(String.format("id='%s' or ", string));
			}
			String hql = buffer.toString().trim();
			// 去掉最后的or
			hql = StringUtils.substring(hql, 0, hql.length() - 2);
			logger.info("删除语句：" + hql);
			// 执行删除
			baseDao.executeHql(hql);
			// 删除索引
			solrService.deleteDocumentByIds(server, personStoreIds);
			return new ViewObject(ViewObject.RET_SUCCEED, "删除人员信息成功").toJSon();
		} catch (Exception e) {
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_FAILURE, "异常：删除信息失败").toJSon();
		}
	}

	public String updatePersonStore(PersonStore personStore){
		// 修改人员信息
		try {
			String id=personStore.getId();
			baseDao.update(personStore);
			//更新solr索引
			solrService.updateDocumentByStore(server, id, personStore);
			return new ViewObject(ViewObject.RET_SUCCEED, "更新信息成功").toJSon();
		} catch (Exception e) {
			logger.debug("更新人员信息异常"+e.getMessage());
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_FAILURE, "异常：更新信息失败").toJSon();
		}
	}
	
	public void updatePersonStoreIsEnableStart(String[] ids) throws Exception {
		// 设置成启用
		for (String id : ids) {
			String hql="UPDATE PersonStore p SET isEnable='2' WHERE p.id='"+id+"'";
			baseDao.executeHql(hql);
		}
	}

	public void updatePersonStoreIsEnableCease(String[] ids) throws Exception {
		// 设置成禁用
		for (String id : ids) {
			String hql="UPDATE PersonStore p SET isEnable='1' WHERE p.id='"+id+"'";
			baseDao.executeHql(hql);
		}
	}

	public boolean findPersonStoreByPersonName(String personName)
			throws Exception {
		//姓名已经存在
		String hql=String.format("select id from PersonStore where personName='%s' and action <>'3'", personName);
		List<PersonStore> list=baseDao.findByHql(hql);
		if (list.size()>0) {
			logger.info(personName+"已经存在于数据库中");
			return true;
		}
		return false;
	}
	
	@Override
	public String canBeSaved(String personName, String infoType, String bebornTime, String sex) throws Exception {
		//验证人员信息是否合理
		StringBuffer buffer=new StringBuffer();
		try {
			if (StringUtil.isEmty(personName)) {
				buffer.append("姓名为空"+" ");
			}
			
			boolean restus=findPersonStoreByPersonName(personName);
			if (restus) {
				buffer.append("姓名"+personName+"已经存在"+" ");
			}
			
			if (StringUtil.isEmty(sex)) {
				buffer.append("性别为空"+" ");
			}
			if (!StringUtil.isEmty(sex)&&!sex.trim().equals("男")&&!sex.trim().equals("女")) {
				buffer.append("性别"+sex+"不合法"+" ");
			}
			String hql="select count(id) from InfoType where typeName='"+infoType+"' and tableTypeName='"+StoreFinal.PERSON_STORE+"'";
			List<Long> list=iBaseDao.findByHql(hql);
			if (list.get(0)<=0) {
				buffer.append("人员类别"+infoType+"不存在"+" ");
			}
			//日期不合法
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(bebornTime);
				long realValue=date.getTime();
				long todayValue=new Date().getTime();
				//时间大于今天
				if (realValue>=todayValue) {
					buffer.append("出生日期不合法"+" ");
				}
			} catch (Exception e) {
				buffer.append("出生日期不合法"+" ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	/**
	 * 判断是否合理
	 * @param personName 人名
	 * @param personNames 人名数组
	 * @param infoType 类别
	 * @return
	 * @throws Exception
	 */
	private String canBeSaved(String personName, Object[]personNames, String infoType) throws Exception {
		//验证人员信息是否合理
		StringBuffer buffer=new StringBuffer();
		try {
			if (StringUtil.isEmty(personName)) {
				buffer.append("姓名为空"+" ");
			}
			//用数组判断人员是否存在
			//boolean restus=findPersonStoreByPersonName(personName);
			boolean restus=ArrayUtils.contains(personNames, personName);
			if (restus) {
				buffer.append("姓名"+personName+"已经存在"+" ");
			}
			String hql="select count(id) from InfoType where typeName='"+infoType+"' and tableTypeName='"+StoreFinal.PERSON_STORE+"'";
			List<Long> list=iBaseDao.findByHql(hql);
			if (list.get(0)<=0) {
				buffer.append("人员类别"+infoType+"不存在"+" ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
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

	@Override
	public String saveMuiltiPersonStore(HttpServletRequest request,Integer startRowNumber,String datas) throws Exception {
		//保存多个人员
		String msg="新增成功";
		StringBuffer failure=new StringBuffer();
		//新增成功的人员名称
		StringBuffer savePersonName=new StringBuffer();
		int  saveCount=0;
		JSONObject jsonResultObject=new JSONObject();
		List<PersonStore> personStores=new ArrayList<>();
		try {
			//json数据转成java对象
			JSONArray jsonArray=(JSONArray) JSONSerializer.toJSON(datas);
			List datasList=(List) JSONSerializer.toJava(jsonArray);
			int row=0;
			//拼接hql语句一次性查询是否存在相同人名
			Object []personNames=null;
			String hql="select personName from PersonStore where";
			for (Object object : datasList){
				JSONObject jsonObject = JSONObject.fromObject(object);
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				String personName=(String) bean.get("personName");
				hql+=" (personName='"+personName+"' and action<>'3') or";
			}
			hql=hql.substring(0, hql.length()-2);
			//logger.info("查询存在的hql语句："+hql);
			List<String> nameList=personNamesDao.findByHql(hql);
			//转数组
			personNames=(Object[]) nameList.toArray();
			//logger.info("已经存在的人员姓名："+Arrays.toString(personNames));
			
			for (Object object : datasList) {
				row++;
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 获得实例
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				String infoType=(String) bean.get("infoType");
				String personName=(String) bean.get("personName");
				String nameUsedBefore=(String) bean.get("nameUsedBefore");
				String englishName=(String) bean.get("englishName");
				String registerAddress=(String) bean.get("registerAddress");
				String presentAddress=(String) bean.get("presentAddress");
				String workUnit=(String) bean.get("workUnit");
				String sex=(String) bean.get("sex");
				String bebornTime=(String) bean.get("bebornTime");
				//人员信息是否合理
				String result=canBeSaved(personName, personNames, infoType);
				if (result.length()>0) {
					//不合理无法入库
					failure.append("Excel表格第"+(row+startRowNumber+1)+"行原因:"+result+"<br>");
					logger.info("Excel表格第"+(row+startRowNumber+1)+"行结果:"+result);
				}else{
					//
					PersonStore store=new PersonStore();
					store.setAction("1");
					store.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName(infoType, StoreFinal.PERSON_STORE));
					store.setNameUsedBefore(nameUsedBefore);
					store.setEnglishName(englishName);
					store.setRegisterAddress(registerAddress);
					store.setPresentAddress(presentAddress);
					//性别
					if(StringUtils.equals(sex, "男")||StringUtils.equals(sex, "女")){
						store.setSex(sex);
					}else{
						store.setSex(null);
					}
					//日期可为空
					store.setBebornTime(setPersonBerBornTime(bebornTime));
					store.setPersonName(personName);
					store.setWorkUnit(workUnit);
					store.setCreateDate(StringUtil.dates());
					
					store.setPhotofraphWay("");
					store.setAppendix("");
					store.setActivityCondition("");
					store.setAntecedents("");
					//uid did oid
					UserSessionMgr mgr=UserSessionMgr.getInstance();
					store.setUid(mgr.getUID(request));
					store.setOid(mgr.getUOID(request));
					store.setDid(mgr.getUDID(request));
					//证件
					Set<CertificatesStore> certificatesStores=new HashSet<>();
					Map<String, String> cMap=getCountsByNode("certificates");
					//证件类型名称
					String[] cHeader=getCountsTypeByNode("certificates");
					for (String string : cHeader) {
						//证件集合
						String number=(String) bean.get(string);
						if (!StringUtil.isEmty(number)) {
							CertificatesStore certificatesStore=new CertificatesStore();
							certificatesStore.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName(cMap.get(string), 
									StoreFinal.CERTIFICATES_STORE));
							certificatesStore.setCertificatesNumber(number);
							certificatesStore.setPersonStore(store);
							certificatesStores.add(certificatesStore);
						}
					}
					store.setCertificatesStores(certificatesStores);
					//网络账号
					Set<NetworkAccountStore> networkAccountStores=new HashSet<>();
					Map<String, String> nMap=getCountsByNode("networkaccount");
					//账号类型名称
					String[] nHeader=getCountsTypeByNode("networkaccount");
					for (String string : nHeader) {
						String number=(String)bean.get(string);
						if (!StringUtil.isEmty(number)) {
							NetworkAccountStore networkAccountStore=new NetworkAccountStore();
							//添加账号
							networkAccountStore.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName(nMap.get(string), 
									StoreFinal.NETWORK_ACCOUNT_STORE));
							networkAccountStore.setNetworkNumber(number);
							networkAccountStore.setPersonStore(store);
							networkAccountStores.add(networkAccountStore);
						}
					}
					store.setNetworkAccountStores(networkAccountStores);
					personStores.add(store);
					//入库的人员
					savePersonName.append("Excel表格第"+(row+startRowNumber+1)+"行，姓名:"+personName+"<br>");
					saveCount++;
				}
			}
			//保存多条数据
			baseDao.save(personStores);
			//保存索引
			solrService.addDocumentByStores(server, personStores);
		} catch (Exception e) {
			msg="新增人员异常";
			e.printStackTrace();
		}
		jsonResultObject.put("saveCount", saveCount);//成功入库条数
		jsonResultObject.put("msg", msg);
		jsonResultObject.put("savePersonName", savePersonName.toString());
		jsonResultObject.put("failure", failure.toString());
		return jsonResultObject.toString();
	}
	/**
	 * 返回账号对应的map对象
	 * @param nodeName 账号节点
	 * @return
	 */
	private Map getCountsByNode(String nodeName) {
		Map<String, String> countsMap=new LinkedHashMap<>();
		String filePath=PathUtils.getConfigPath(PersonStoreServiceImpl.class)+"person-store-excel.xml";
		XmlUtils xmlUtils=new XmlUtils(filePath);
		//获得单个节点
		Element element=xmlUtils.getNode(nodeName);
		List<Element> elements=element.elements();
		for (Element element2 : elements) {
			countsMap.put(xmlUtils.getNodeAttrVal(element2, "name"),element2.getText());
		}
		return countsMap;
	}
	/**
	 * 获得账号的类型名称如email、qq等
	 * @param nodeName
	 * @return
	 */
	private String[] getCountsTypeByNode(String nodeName) {
		String filePath=PathUtils.getConfigPath(PersonStoreServiceImpl.class)+"person-store-excel.xml";
		XmlUtils xmlUtils=new XmlUtils(filePath);
		//获得单个节点
		Element element=xmlUtils.getNode(nodeName);
		List<Element> elements=element.elements();
		//账号类型
		String []result=new String[elements.size()];
		for (int i=0;i<elements.size();i++) {
			//获得账号名称
			result[i]=xmlUtils.getNodeAttrVal(elements.get(i), "name");
		}
		return result;
	}


	@Override
	public void outputPersonStoreToExcel(String path, String[] personIds) throws Exception {
		// 保存到Excel中
		// 创建文件流
		OutputStream stream = null;
		try {
			// 创建工作文档对象
			Workbook wb =null;
			if(FilenameUtils.getExtension(path).equals("xls")){
				wb= new HSSFWorkbook();
			}
			if(FilenameUtils.getExtension(path).equals("xlsx")){
				wb= new XSSFWorkbook();
			}
			// 创建sheet对象
			Sheet sheet1 = (Sheet) wb.createSheet("人员信息");
			// 表头
			Map<String, String> hashMap = new LinkedHashMap<>();
			hashMap.put("infoType", "类别");
			hashMap.put("personName", "姓名");
			hashMap.put("nameUsedBefore", "曾用名");
			hashMap.put("englishName", "英文名");
			hashMap.put("registerAddress", "户籍地址");
			hashMap.put("presentAddress", "现住地址");
			hashMap.put("workUnit", "工作单位");
			hashMap.put("sex", "性别");
			hashMap.put("bebornTime", "出生日期");
			// 网络账号
			List<InfoType> networkAccountStoreInfoTypeList = infoTypeService
					.findInfoTypeByTypeName("NetworkAccountStore");
			for (InfoType infoType : networkAccountStoreInfoTypeList) {
				hashMap.put(infoType.getId(), infoType.getTypeName());
			}
			// 身份账号
			List<InfoType> certificatesStoreInfoTypeList = infoTypeService.findInfoTypeByTypeName("CertificatesStore");
			for (InfoType infoType : certificatesStoreInfoTypeList) {
				hashMap.put(infoType.getId(), infoType.getTypeName());
			}
			Set<String> set = hashMap.keySet();
			Row headerRow = (Row) sheet1.createRow(0);
			int i = 0;
			for (String string : set) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(hashMap.get(string));
				i++;
			}
			//保存数据//
			for (int j = 0; j < personIds.length; j++) {
				logger.info("正在查询第" + (j + 1) + "个人员的信息");
				int k = 0;
				PersonStore personStore = findPersonStoreById(personIds[j]);
				Row row = (Row) sheet1.createRow(j + 1);
				Map<String, String> valueMap = new LinkedHashMap<>();
				valueMap.put("infoType", personStore.getInfoType().getTypeName());
				valueMap.put("personName", personStore.getPersonName());
				valueMap.put("nameUsedBefore", personStore.getNameUsedBefore());
				valueMap.put("englishName", personStore.getEnglishName());
				valueMap.put("registerAddress", personStore.getRegisterAddress());
				valueMap.put("presentAddress", personStore.getPresentAddress());
				valueMap.put("workUnit", personStore.getWorkUnit());
				valueMap.put("sex", personStore.getSex());
				valueMap.put("bebornTime", personStore.getBebornTime());
				// 获得身份账号
				for (InfoType infoType : certificatesStoreInfoTypeList) {
					DetachedCriteria criteria=DetachedCriteria.forClass(CertificatesStore.class);
					//用DetachedCriteria的关联查询就可
					criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", personIds[j]));
					criteria.createAlias("infoType", "i").add(Restrictions.eq("i.id", infoType.getId()));
					StringBuffer number=new StringBuffer();
					List<CertificatesStore> list=iBaseDao.findByCriteria(criteria);
					for (CertificatesStore certificatesStore : list) {
						number.append(certificatesStore.getCertificatesNumber()+",");
					}
					//一个账号类型可能有多个账号
					valueMap.put(infoType.getId(),(number.toString().length()>1?
							number.toString().substring(0,number.toString().length()-1):number.toString()));
				}
				// 获得网络账号
				for (InfoType infoType : networkAccountStoreInfoTypeList) {
					DetachedCriteria criteria=DetachedCriteria.forClass(NetworkAccountStore.class);
					//用DetachedCriteria的关联查询就可
					criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", personIds[j]));
					criteria.createAlias("infoType", "i").add(Restrictions.eq("i.id", infoType.getId()));
					StringBuffer number=new StringBuffer();
					List<NetworkAccountStore> list=iBaseDao.findByCriteria(criteria);
					for (NetworkAccountStore networkAccountStore : list) {
						number.append(networkAccountStore.getNetworkNumber()+",");
					}
					//一个账号类型可能有多个账号
					valueMap.put(infoType.getId(),(number.toString().length()>1?
							number.toString().substring(0,number.toString().length()-1):number.toString()));
				}
				for (String string : set) {
					Cell cell = row.createCell(k);
					cell.setCellValue(valueMap.get(string));
					k++;
				}
				
			}

			stream = new FileOutputStream(path);
			// 写入数据
			wb.write(stream);

		} catch (Exception e) {
			logger.error("导出人员到Excel中失败");
			e.printStackTrace();
		} finally {
			// 关闭文件流
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public void outputPersonStoreToWord(String id,String filePath) {
		FileOutputStream fos=null;
		String fontFamily="宋体";
		int fontSize=30;
		try {
			PersonStore store=findPersonStoreById(id);
			CustomXWPFDocument document=new CustomXWPFDocument();
			//word2003
			String temple=PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class)+"temple.doc";
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(temple));
			HWPFDocument doc = new HWPFDocument(fs);
			if (null!=store) {
				/*//标题
				 XWPFParagraph title = document.createParagraph();
		        title.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun titleRun = title.createRun();
		        titleRun.setBold(true);
		        titleRun.setFontFamily(fontFamily);
		        titleRun.setText(store.getPersonName()+"的信息");
		        titleRun.setFontSize(fontSize);*/
		        //利用反射给属性赋值
		        String []properties=new String[]{"infoType","personName","nameUsedBefore","englishName","sex",
						"bebornTime","registerAddress","presentAddress","workUnit","antecedents","activityCondition",
						};
		        String []chinese_properties=new String[]{"人员类别","姓名","曾用名","英文名","性别","出生日期","户籍地址","现住地址"
		        		,"工作单位","履历","活动情况"};
		        Range range = doc.getRange();
				Paragraph titlePar = range.insertAfter(new ParagraphProperties(), 0);
				CharacterRun titleRun = titlePar.insertAfter("");
				titleRun.insertAfter(store.getPersonName()+"详细信息：");
				titleRun.setFontSize(50);
				
				for(int i=0;i<properties.length;i++){
		        	//设置内容
					Paragraph paragraph = range.insertAfter(new ParagraphProperties(), 0);
					String string=properties[i];
					CharacterRun run1 = paragraph.insertAfter("");
					run1.setFontSize(fontSize);
					if(string.equals("infoType")){
						//直接利用PropertyUtils获得属性值
						InfoType infoType=(InfoType) PropertyUtils.getSimpleProperty(store, string);
						if(null!=infoType){
							run1.insertAfter(chinese_properties[i]+"："+infoType.getTypeName());
						}else{
							run1.insertAfter(chinese_properties[i]+"：");
						}
					}else{
						run1.insertAfter(chinese_properties[i]+"："+PropertyUtils.getSimpleProperty(store, string));
					}
		        }
				//插入表格
				List<InfoType> list=infoTypeService.findInfoTypeByTypeName("CertificatesStore");
				Paragraph cParagraph = range.insertAfter(new ParagraphProperties(), 0);
				CharacterRun cRun = cParagraph.insertAfter("");
				cRun.insertAfter("证件信息：");
				cRun.setFontSize(36);
				
				for(int i=0;i<list.size();i++){
					InfoType infoType=list.get(i);
					Paragraph c = range.insertAfter(new ParagraphProperties(), 0);
					
					CharacterRun run1 = c.insertAfter("");
					DetachedCriteria criteria=DetachedCriteria.forClass(CertificatesStore.class);
					//用DetachedCriteria的关联查询就可
					criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", id));
					criteria.createAlias("infoType", "i").add(Restrictions.eq("i.id", infoType.getId()));
					StringBuffer number=new StringBuffer();
					List<CertificatesStore> cList=iBaseDao.findByCriteria(criteria);
					for (CertificatesStore certificatesStore : cList) {
						number.append(certificatesStore.getCertificatesNumber()+",");
					}
					run1.insertAfter(infoType.getTypeName()+"："+number.toString());
				}
				
				List<InfoType> list2=infoTypeService.findInfoTypeByTypeName("NetworkAccountStore");
				Paragraph tParagraph = range.insertAfter(new ParagraphProperties(), 0);
				CharacterRun tRun = tParagraph.insertAfter("");
				tRun.insertAfter("联系方式信息：");
				tRun.setFontSize(36);
				
				for(int i=0;i<list2.size();i++){
					InfoType infoType=list2.get(i);
					Paragraph c = range.insertAfter(new ParagraphProperties(), 0);
					
					CharacterRun run1 = c.insertAfter("");
					DetachedCriteria criteria=DetachedCriteria.forClass(NetworkAccountStore.class);
					//用DetachedCriteria的关联查询就可
					criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", id));
					criteria.createAlias("infoType", "i").add(Restrictions.eq("i.id", infoType.getId()));
					StringBuffer number=new StringBuffer();
					List<NetworkAccountStore> clist2=iBaseDao.findByCriteria(criteria);
					for (NetworkAccountStore networkAccountStore : clist2) {
						number.append(networkAccountStore.getNetworkNumber()+",");
					}
					run1.insertAfter(infoType.getTypeName()+"："+number.toString());
				}
				
				 fos=new FileOutputStream(new File(filePath));
				 doc.write(fos);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(fos);
		}
	}
}
