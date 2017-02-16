package com.ushine.storeInfo.service.impl;

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
import org.dom4j.Element;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
import com.ushine.store.index.PersonStoreNRTSearch;
import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.CertificatesStore;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.NetworkAccountStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.storeInfo.storefinal.StoreFinal;
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
	@Autowired
	private IBaseDao<PersonStore, String> baseDao;
	@Autowired
	private IBaseDao iBaseDao;
	
	@Autowired private IBaseDao personNamesDao;
	@Autowired private IInfoTypeService infoTypeService;
	private PersonStoreNRTSearch personStoreNRTSearch=PersonStoreNRTSearch.getInstance();
	
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String field,String fieldValue,String startTime,String endTime,int nextPage, int size, String uid,
			String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PersonStore.class);
		//时间倒序排序
		criteria.addOrder(Order.desc("createDate"));
		//anyField任意字段搜索
		if(!StringUtil.isNull(startTime) && startTime.length() >=10){
			startTime = startTime.substring(0,10)+ " 00:00:00";
			criteria.add(Restrictions.ge("createDate", startTime));
		}
		if(!StringUtil.isNull(endTime) && endTime.length()>=10){
			endTime = endTime.substring(0,10)+" 23:59:59";
			criteria.add(Restrictions.le("createDate", endTime));
		}
		if (!StringUtil.isNull(uid)) {
			criteria.add(Restrictions.eq("uid", uid));
		}
		if (!StringUtil.isNull(oid)) {
			criteria.add(Restrictions.eq("oid", oid));
		}
		if (!StringUtil.isNull(did)) {
			criteria.add(Restrictions.eq("did", did));
		}
		//查询aciton不为3的
		criteria.add(Restrictions.ne("action", "3"));
		//查询已入库的数据
		criteria.add(Restrictions.eq("isToStorage", "1"));
		return criteria;
	}
	
	
	public boolean savePersonStore(PersonStore personStore) throws Exception {
		//新增人员
		baseDao.save(personStore);
		//添加索引
		personStoreNRTSearch.addIndex(personStore);
		//IBaseLuceneDao personStoreDao=new PersonStoreIndexImpl();
		//personStoreDao.saveIndex(personStore);
		return true;
	}
	public PersonStore findPersonStoreById(String personStoreId)
			throws Exception {
		// TODO Auto-generated method stub
		return baseDao.findById(PersonStore.class, personStoreId);
	}
	@SuppressWarnings("unchecked")
	public String findPersonStore(String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size,
			String uid, String oid, String did) throws Exception {
		logger.debug("筛选人员库信息,field:"+field+",fieldValue:"+fieldValue+",startTime:"+startTime+"endTime:"+endTime+"");
		return null;	
	}
	
	@SuppressWarnings("unchecked")
	public String findPersonStore(String field, String fieldValue,
			String startTime, String endTime, int nextPage, int size,
			String uid, String oid, String did,String sortField,String dir) throws Exception {
		logger.debug("筛选人员库信息,field:"+field+",fieldValue:"+fieldValue+",startTime:"+startTime+"endTime:"+endTime+"");
		//查询
		boolean hasValue=false;
		if(!StringUtil.isEmty(fieldValue)){
			hasValue=true;
		}
		PagingObject<PersonStore> vo=StoreIndexQuery.findStore(field, fieldValue, 
				startTime, endTime, nextPage, size, uid, oid, did, sortField,dir,
				PersonStore.class);
		return StoreIndexQuery.personStoreVoToJson(vo, hasValue);		
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
			personStoreNRTSearch.deleteIndex(personStoreIds);
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
			//更新索引
			//personStoreNRTSearch.updateIndex(id, personStore);
			personStoreNRTSearch.deleteIndex(id);
			personStoreNRTSearch.addIndex(personStore);
			return new ViewObject(ViewObject.RET_FAILURE, "更新信息成功").toJSon();
		} catch (Exception e) {
			logger.debug("更新人员信息异常"+e.getMessage());
			e.printStackTrace();
			return new ViewObject(ViewObject.RET_FAILURE, "异常：更新信息失败").toJSon();
		}
	}

	public PagingObject<PersonStore> findPersonStoreByIsEnable(String field, String fieldValue, String startTime,String endTime,int nextPage,
			int size, String uid, String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(PersonStore.class);
		//时间倒序排序
				criteria.addOrder(Order.desc("createDate"));
		criteria.add(Restrictions.eq("isEnable", "2"));
		if (!StringUtil.isNull(did)) {
			criteria.add(Restrictions.eq("did", did));
		}
		if (!StringUtil.isNull(uid)) {
			criteria.add(Restrictions.eq("uid", uid));
		}
		if (!StringUtil.isNull(oid)) {
			criteria.add(Restrictions.eq("oid", oid));
		}
		if(!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue)){
			//人员类别不能直接设置值
			criteria.add(Restrictions.like(field, "%"+fieldValue+"%"));
		}
		if(!StringUtil.isNull(startTime) && startTime.length() >=10){
			startTime = startTime.substring(0,10)+ " 00:00:00";
			criteria.add(Restrictions.ge("createDate", startTime));
		}
		if(!StringUtil.isNull(endTime) && endTime.length()>=10){
			endTime = endTime.substring(0,10)+" 23:59:59";
			criteria.add(Restrictions.le("createDate", endTime));
		}
		int rowCount = baseDao.getRowCount(criteria);
		Paging paging = new Paging(size, nextPage, rowCount);
		logger.debug("分页信息：" + JSONObject.fromObject(paging));

		criteria.setProjection(null);
		criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);

		List<PersonStore> list = baseDao.findPagingByCriteria(criteria, size,
				paging.getStartRecord());
		PagingObject<PersonStore> vo = new PagingObject<PersonStore>();
		vo.setPaging(paging);
		vo.setArray(list);
		return vo;
	}

	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String uid,
			String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(PersonStore.class);
		//时间倒序排序
				criteria.addOrder(Order.desc("createDate"));
		if (!StringUtil.isNull(uid)) {
			criteria.add(Restrictions.eq("uid", uid));
		}
		if (!StringUtil.isNull(oid)) {
			criteria.add(Restrictions.eq("oid", oid));
		}
		if (!StringUtil.isNull(did)) {
			criteria.add(Restrictions.eq("did", did));
		}
		//查询aciton不为3的
		criteria.add(Restrictions.ne("action", "3"));
		criteria.add(Restrictions.eq("isEnable", "2"));
		return criteria;
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
		/*DetachedCriteria criteria = DetachedCriteria.forClass(PersonStore.class);
		criteria.add(Restrictions.eq("personName", personName))
				//没有被删除的
				.add(Restrictions.ne("action", "3"));
		int restus = baseDao.getRowCount(criteria);
		if(restus>0){
			return true;
		}*/
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
			//String sql=" select count(id) from t_info_type where TYPE_NAME='"+infoType+"' and TABLE_TYPE_NAME='"+StoreFinal.PERSON_STORE+"'";
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
			
			/*if (StringUtil.isEmty(sex)) {
				buffer.append("性别为空"+" ");
			}
			if (!StringUtil.isEmty(sex)&&!sex.trim().equals("男")&&!sex.trim().equals("女")) {
				buffer.append("性别"+sex+"不合法"+" ");
			}*/
			String hql="select count(id) from InfoType where typeName='"+infoType+"' and tableTypeName='"+StoreFinal.PERSON_STORE+"'";
			List<Long> list=iBaseDao.findByHql(hql);
			if (list.get(0)<=0) {
				buffer.append("人员类别"+infoType+"不存在"+" ");
			}
			//String sql=" select count(id) from t_info_type where TYPE_NAME='"+infoType+"' and TABLE_TYPE_NAME='"+StoreFinal.PERSON_STORE+"'";
			/*//日期不合法
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
			}*/
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
			personStoreNRTSearch.addIndex(personStores);
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
				
				/*for (int i=0;i<properties.length;i++) {
					//设置内容
			        XWPFParagraph property = document.createParagraph();
			        //property.setSpacingAfter(100);
			        property.setSpacingAfterLines(25);
			        XWPFRun propertyRun=property.createRun();
			        propertyRun.setFontFamily(fontFamily);
			        propertyRun.setFontSize(12);
					String string=properties[i];
					if(string.equals("infoType")){
						//直接利用PropertyUtils获得属性值
						InfoType infoType=(InfoType) PropertyUtils.getSimpleProperty(store, string);
						propertyRun.setText(chinese_properties[i]+"："+infoType.getTypeName());
					}else{
						propertyRun.setText(chinese_properties[i]+"："+PropertyUtils.getSimpleProperty(store, string));
					}
				}
				//证件账号表格
				List<InfoType> list=infoTypeService.findInfoTypeByTypeName("CertificatesStore");
				XWPFParagraph cTitle = document.createParagraph();
				cTitle.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun cTitleRun = cTitle.createRun();
		        cTitleRun.setFontFamily(fontFamily);
		        cTitleRun.setText("证件列表：");
		        cTitleRun.setFontSize(fontSize);
				XWPFTable cTable=document.createTable(list.size()+1,2);//2列
				cTable.setWidth(500);
				cTable.setCellMargins(20, 20, 20, 20);
				// 表格属性
				CTTblPr tablePr = cTable.getCTTbl().addNewTblPr();
				// 表格宽度
				CTTblWidth width = tablePr.addNewTblW();
				width.setW(BigInteger.valueOf(8000));
				//表头
				List<XWPFTableCell> row0Cells = cTable.getRow(0).getTableCells();
				row0Cells.get(0).setText("证件类型");
				row0Cells.get(1).setText("证件号");
				for(int i=0;i<list.size();i++){
					InfoType infoType=list.get(i);
					List<XWPFTableCell> tableCells = cTable.getRow(i+1).getTableCells();
					tableCells.get(0).setText(infoType.getTypeName());
					DetachedCriteria criteria=DetachedCriteria.forClass(CertificatesStore.class);
					//用DetachedCriteria的关联查询就可
					criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", id));
					criteria.createAlias("infoType", "i").add(Restrictions.eq("i.id", infoType.getId()));
					StringBuffer number=new StringBuffer();
					List<CertificatesStore> cList=iBaseDao.findByCriteria(criteria);
					for (CertificatesStore certificatesStore : cList) {
						number.append(certificatesStore.getCertificatesNumber()+",");
					}
					tableCells.get(1).setText(number.toString());
				}
				//网络账号表格
				List<InfoType> list2=infoTypeService.findInfoTypeByTypeName(NetworkAccountStore.class.getSimpleName());
				XWPFParagraph nTitle = document.createParagraph();
				nTitle.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun nTitleRun = nTitle.createRun();
		        nTitleRun.setFontFamily(fontFamily);
		        nTitleRun.setText("账号列表：");
		        nTitleRun.setFontSize(fontSize);
				XWPFTable nTable=document.createTable(list2.size()+1,2);//2列
				nTable.setWidth(500);
				nTable.setCellMargins(20, 20, 20, 20);
				// 表格属性
				CTTblPr tablePr2 = nTable.getCTTbl().addNewTblPr();
				// 表格宽度
				CTTblWidth width2 = tablePr2.addNewTblW();
				width2.setW(BigInteger.valueOf(8000));
				//表头
				List<XWPFTableCell> row1Cells = nTable.getRow(0).getTableCells();
				row1Cells.get(0).setText("账号类型");
				row1Cells.get(1).setText("账号");
				for(int i=0;i<list2.size();i++){
					InfoType infoType=list2.get(i);
					List<XWPFTableCell> tableCells = nTable.getRow(i+1).getTableCells();
					tableCells.get(0).setText(infoType.getTypeName());
					DetachedCriteria criteria=DetachedCriteria.forClass(NetworkAccountStore.class);
					//用DetachedCriteria的关联查询就可
					criteria.createAlias("personStore", "p").add(Restrictions.eq("p.id", id));
					criteria.createAlias("infoType", "i").add(Restrictions.eq("i.id", infoType.getId()));
					StringBuffer number=new StringBuffer();
					List<NetworkAccountStore> cList=iBaseDao.findByCriteria(criteria);
					for (NetworkAccountStore networkAccountStore : cList) {
						number.append(networkAccountStore.getNetworkNumber()+",");
					}
					tableCells.get(1).setText(number.toString());
				}
				//输出的word
				fos = new FileOutputStream(new File(filePath));
				document.write(fos);*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(fos);
		}
	}
}
