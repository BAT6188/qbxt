package com.ushine.storeInfo.service.impl;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import com.ushine.common.vo.PagingObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.dao.IBaseDao;
import com.ushine.store.index.OutsideDocStoreNRTSearch;
import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.Attaches;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IOutsideDocStoreService;
import com.ushine.storeInfo.storeFinal.StoreFinal;
import com.ushine.util.IdentifyDocUtils;
import com.ushine.util.StringUtil;
import com.ushine.util.XmlUtils;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * 外来文档接口实现类
 * 
 * @author wangbailin
 *
 */
@Transactional
@Service("outsideDocStoreServiceImpl")
public class OutsideDocStoreServiceImpl implements IOutsideDocStoreService {
	private Logger logger = LoggerFactory.getLogger(OutsideDocStoreServiceImpl.class);
	@Autowired
	private IBaseDao<OutsideDocStore, Serializable> baseDao;
	@Autowired
	private IBaseDao<Attaches, Serializable> baseDaoAttaches;
	@Autowired
	private IInfoTypeService infoTypeService;
	//
	private OutsideDocStoreNRTSearch nrtSearch = OutsideDocStoreNRTSearch.getInstance();
	@Autowired
	private IBaseDao fileNamesDao;

	// 多条件查询
	@Transactional(readOnly = true)
	private DetachedCriteria getCondition(String field, String fieldValue, String startTime, String endTime,
			int nextPage, int size, String uid, String oid, String did) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OutsideDocStore.class);
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue) && !field.equals("infoType")) {
			// 类别不能直接设置值
			criteria.add(Restrictions.like(field, "%" + fieldValue + "%"));
		}
		if (!StringUtil.isNull(field) && !StringUtil.isNull(fieldValue) && field.equals("infoType")) {
			// 如果是类别
			// 使用createAlias来创建属性别名,然后引用别名进行条件查询
			criteria.createAlias("infoType", "i").add(Restrictions.like("i.typeName", "%" + fieldValue + "%"));
		}
		if (!StringUtil.isNull(startTime) && startTime.length() >= 10) {
			startTime = startTime.substring(0, 10) + " 00:00:00";
			criteria.add(Restrictions.ge("createDate", startTime));
		}
		if (!StringUtil.isNull(endTime) && endTime.length() >= 10) {
			endTime = endTime.substring(0, 10) + " 23:59:59";
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
		// 不等于3,not equals
		criteria.add(Restrictions.ne("action", "3"));
		return criteria;
	}

	/**
	 * 把外来文档集合转成json
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String voToJson(PagingObject<OutsideDocStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (OutsideDocStore store : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", store.getId());
			obj.put("name", store.getName());
			// obj.put("sourceUnit", store.getSourceUnit());
			// obj.put("title", store.getTitle());
			obj.put("time", store.getTime());
			// obj.put("secretRank", store.getSecretRank());
			obj.put("centent", store.getCentent());
			obj.put("createDate", store.getCreateDate());
			if (store.getInvolvedInTheField() != null) {
				// 不为空
				obj.put("involvedInTheField", store.getInvolvedInTheField().getTypeName());
			}
			// 类别不为空
			if (store.getInfoType() != null) {
				obj.put("infoType", store.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}

	@SuppressWarnings("unchecked")
	public String findOutsideDocStore(String field, String fieldValue, String startTime, String endTime, int nextPage,
			int size, String uid, String oid, String did) throws Exception {
		// 根据条件查询外来文档
		logger.debug("筛选外来文档信息,field:" + field + ",fieldValue:" + fieldValue + ",startTime:" + startTime + "endTime:"
				+ endTime + "");
		/*
		 * DetachedCriteria criteria =
		 * getCondition(field,fieldValue,startTime,endTime,nextPage, size, uid,
		 * oid, did);
		 * 
		 * int rowCount = baseDao.getRowCount(criteria);
		 * 
		 * Paging paging = new Paging(size, nextPage, rowCount);
		 * logger.debug("分页信息：" + JSONObject.fromObject(paging));
		 * 
		 * criteria.setProjection(null);
		 * criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		 * 
		 * List<OutsideDocStore> list = baseDao.findPagingByCriteria(criteria,
		 * size, paging.getStartRecord()); PagingObject<OutsideDocStore> vo =
		 * new PagingObject<OutsideDocStore>(); vo.setPaging(paging);
		 * vo.setArray(list);
		 */
		return null;
	}

	/**
	 * 包含排序的查询
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String findOutsideDocStore(String field, String fieldValue, String startTime, String endTime, int nextPage,
			int size, String uid, String oid, String did, String sortField, String dir) throws Exception {
		// 利用索引查询
		boolean hasValue = false;
		// 输入了值后高亮
		if (!StringUtil.isEmty(fieldValue)) {
			hasValue = true;
		}
		PagingObject<OutsideDocStore> vo = StoreIndexQuery.findStore(field, fieldValue, startTime, endTime, nextPage,
				size, uid, oid, did, sortField, dir, OutsideDocStore.class);
		return StoreIndexQuery.outsideDocStoreVoToJson(vo, hasValue);
	}

	public void delOutsideDocStore(String[] outsideDocStoreIds) throws Exception {
		// 删除更新action为3
		try {
			logger.info("删除OutsideDocStore对象id为:" + Arrays.toString(outsideDocStoreIds));
			StringBuffer buffer = new StringBuffer("update OutsideDocStore set action='3' where ");
			for (String string : outsideDocStoreIds) {
				buffer.append(String.format("id='%s' or ", string));
			}
			String hql = buffer.toString().trim();
			// 去掉最后的or
			hql = StringUtils.substring(hql, 0, hql.length() - 2);
			logger.info("删除语句：" + hql);
			// 执行删除
			baseDao.executeHql(hql);
			// 更新索引
			nrtSearch.deleteIndex(outsideDocStoreIds);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存异常信息" + e.getMessage());
		}
	}

	public void saveOutsideDocStore(OutsideDocStore outsideDocStore) throws Exception {
		try {
			// 保存
			baseDao.save(outsideDocStore);
			nrtSearch.addIndex(outsideDocStore);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存异常信息" + e.getMessage());
		}
	}

	public void updateOutsideDocStore(OutsideDocStore outsideDocStore) throws Exception {
		// 更新
		try {
			logger.info("更新OutsideDocStore对象为:" + outsideDocStore.toString());
			baseDao.update(outsideDocStore);
			// 索引更新
			nrtSearch.updateIndex(outsideDocStore.getId(), outsideDocStore);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新异常信息" + e.getMessage());
		}
	}

	public OutsideDocStore findOutsideDocStoreById(String outsideDocId) throws Exception {
		// 根据id查找
		try {
			logger.info("查找OutsideDocStore对象id为:" + outsideDocId);
			return baseDao.findById(OutsideDocStore.class, outsideDocId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新异常信息" + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean hasStoreByName(String name) {
		// 根据文档名称判断是否存在
		boolean result = false;
		try {
			String hql = String.format("select id from OutsideDocStore where name='%s'" + "and action<>'3'", name);
			List<OutsideDocStore> list = baseDao.findByHql(hql);
			if (list.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取xml文件配置项，判断是否能否识别该类型
	 * 
	 * @param nodeName
	 *            节点名称
	 * @param docType
	 *            外来文档的类别
	 * @return
	 */
	private boolean isCanBeIdentified(String nodeName, String docType) {
		boolean b = false;
		try {
			String xml = PathUtils.getConfigPath(OutsideDocStoreServiceImpl.class) + "outside-doc-store.xml";
			XmlUtils utils = new XmlUtils(xml);
			List<Element> elements = utils.getNode(nodeName).elements();
			for (Element element : elements) {
				String result = StringUtils.trim(element.getText());
				if (docType.contains(result)) {
					// 包含该类别
					b = true;
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return b;
	}

	@Override
	public String identifyOutsideDoc(String directoryPath, String docType) throws Exception {
		// 识别导入的文件夹中的外来文档
		File dir = new File(directoryPath);
		// 总共文件数
		int totalCount = 0;
		// 识别出的数量
		int identifyCount = 0;
		// 输出成json
		JSONObject root = new JSONObject();
		JSONArray array = new JSONArray();
		StringBuffer unIdentifyDetail = new StringBuffer();
		if (dir.isDirectory()) {
			// 文件夹里面的文件
			totalCount = dir.listFiles().length;
			for (File file : dir.listFiles()) {
				String fileName = file.getName();
				String docNumber = null;
				String title = null;
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
				// 默认的时间是今天
				String time = format2.format(new Date());
				//
				if (isCanBeIdentified("first-type", docType) && fileName.contains(docType)) {
					int index0 = fileName.indexOf("第");
					int index1 = fileName.indexOf("期");
					if (index1 > index0 && index0 > 0 && index1 > 0) {
						// 中间的就是期刊号
						docNumber = fileName.substring(index0 + 1, index1);
						// 文档最后修改时间
						SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
						Date date = format1.parse(fileName.substring(0, 8));
						time = format2.format(date);
						// 标题
						title = IdentifyDocUtils.getTitle(fileName);
						if (StringUtils.isEmpty(title)) {
							title = StringUtils.substring(fileName, index1 + 1);
						}
					}
					logger.info("docNumber:" + docNumber + ",time:" + time + ",title:" + title);
				}

				fileName = StringUtils.substring(fileName, 8);
				if (docNumber != null && title != null) {
					identifyCount++;
					JSONObject obj = new JSONObject();
					obj.put("fileName", fileName);
					obj.put("docNumber", docNumber);
					obj.put("docName", FilenameUtils.getBaseName(title));
					obj.put("time", time);
					// 文档路径
					obj.put("filePath", file.getPath());
					// 读取原文内容
					// obj.put("theOriginal", readWord(file.getPath()));
					array.add(obj);
					logger.info("识别结果=类型：" + docType + ",期刊号：" + docNumber + ",标题：" + title + ",时间：" + time);
				} else {
					// 未识别出
					unIdentifyDetail.append("文件名：" + fileName + "<br>");
				}
			}
			// 输出成json
			// root.element("totalCount", totalCount);
			String identifyInfo = String.format("共%s个文档,识别%s", totalCount, identifyCount);
			root.element("identifyInfo", identifyInfo);
			root.element("unIdentifyDetail", unIdentifyDetail.toString());
			root.element("datas", array.toString());
			logger.info(root.toString());
		}
		return root.toString();
	}

	/**
	 * 期刊号以及标题长度是否为空
	 * 
	 * @param title
	 * @param docNumber
	 * @return 如果不为空返回空串
	 */
	private String isValidDoc(String title, String docNumber) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isEmpty(title)) {
			result.append("无法获得标题");
		}
		if (StringUtils.isEmpty(docNumber)) {
			result.append("无法获得期刊号");
		}
		if (StringUtils.length(result.toString()) > 0) {
			logger.info("无法入库：" + result.toString());
		}
		return result.toString();
	}

	@Override
	public String saveOutsideDocStore(String datas, String sourceUnit,String secretRank,
			String infoType, HttpServletRequest request, UserSessionMgr userMgr,
			String uploadNumber) throws Exception {
		// logger.info("多个业务文档" + datas);
		// 把json转成对象
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(datas);
		List li = (List) JSONSerializer.toJava(jsonArray);
		// 总数量
		int totalCount = li.size();
		// 未保存的文件名称
		StringBuffer unSaveDetail = new StringBuffer();
		// 成功保存的文件名
		StringBuffer saveDetail = new StringBuffer();
		// 返回结果
		JSONObject resultObject = new JSONObject();
		List<OutsideDocStore> list = new ArrayList<>();
		// 删除临时上传的文件夹
		String tempFolderPath = null;
		try {
			// 开始
			long start = System.currentTimeMillis();
			// 设置默认的涉及领域
			InfoType involvedInTheField = infoTypeService
					.findInfoTypeById(Configured.getInstance().get("involvedInTheField"));
			// 所属类别
			InfoType docInfoType = infoTypeService.findInfoTypeById(infoType);
			// logger.info("文档类型"+docInfoType.toString());
			String outsideDocStoreAttachment = Configured.getInstance().get("outsideDocStore");
			// 拷贝到这个文件夹中
			String directory = request.getServletContext().getRealPath("/") + outsideDocStoreAttachment;

			// 拼接查询
			Object[] fileNames = null;
			String hql = "select fileName from OutsideDocStore where";
			for (Object object : li) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				String filePath = (String) bean.get("filePath");
				String fileName = FilenameUtils.getName(filePath).substring(8);
				hql += " (fileName='" + fileName + "' and action<>'3') or";
			}
			hql = hql.substring(0, hql.length() - 2);
			List<String> nameList = fileNamesDao.findByHql(hql);
			// 转数组
			fileNames = (Object[]) nameList.toArray();
			logger.info("已经存在的文档名称：" + Arrays.toString(fileNames));

			for (Object object : li) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 获得实例
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				String docName = (String) bean.get("docName");
				String docNumber = (String) bean.get("docNumber");
				String time = (String) bean.get("time");
				String filePath = (String) bean.get("filePath");
				// 获得路径
				tempFolderPath = FilenameUtils.getFullPath(filePath);
				// 保存
				OutsideDocStore store = new OutsideDocStore();
				store.setAction("1");
				store.setCreateDate(StringUtil.dates());
				// 设置权限
				store.setUid(userMgr.getUID(request));
				store.setDid(userMgr.getUDID(request));
				store.setOid(userMgr.getUOID(request));
				// 去掉后缀名
				docName = FilenameUtils.getBaseName(docName);
				store.setName(docName);
				store.setDocNumber(docNumber);
				store.setTime(time);
				// 设置类别
				store.setInfoType(docInfoType);
				// 设置密级
				store.setSecretRank(secretRank);
				// 来源单位
				store.setSourceUnit(sourceUnit);
				// 设置涉及领域
				store.setInvolvedInTheField(involvedInTheField);
				// copy文件到文件夹中
				FileUtils.copyFileToDirectory(new File(filePath), new File(directory));
				String attaches = outsideDocStoreAttachment + File.separator + FilenameUtils.getName(filePath);
				// 附件
				store.setAttaches(attaches);
				// 是否已经保存过了
				String fileName = FilenameUtils.getName(filePath).substring(8);
				store.setFileName(fileName);
				String error = isValidDoc(docName, docNumber).trim();
				if (StringUtils.length(error) > 0) {
					// 没有标题或期刊号
					unSaveDetail.append(String.format("文档入库失败：%s,%s", fileName, error)).append("<br>");
				}
				// 数组中包含文件名
				else if (ArrayUtils.contains(fileNames, fileName)) {
					// 已存在
					unSaveDetail.append(String.format("文档已经存在：%s", fileName)).append("<br>");
				} else {
					list.add(store);
					saveDetail.append(String.format("文档成功保存：%s", store.getFileName())).append("<br>");
				}
			}
			// 新增多个
			baseDao.save(list);
			// 更新多个索引
			logger.info("正在添加" + list.size() + "条索引");
			nrtSearch.addIndex(list);
			logger.info("添加" + list.size() + "条索引完成");
			// 结束
			long end = System.currentTimeMillis();
			// 结果
			// 共%s个文件,totalCount
			String saveInfo = String.format("成功%s个,用时%s秒", list.size(), (end - start) / 1000);
			resultObject.element("saveInfo", saveInfo);
			resultObject.element("unSaveDetail", unSaveDetail.toString());
			resultObject.element("saveDetail", saveDetail.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			// 删除临时的文件夹
			logger.info("删除临时上传的文件夹路径：" + tempFolderPath);
			File file = new File(tempFolderPath);
			if (file.exists()) {
				FileUtils.deleteDirectory(file);
			}
		}
		return resultObject.toString();
	}

	@Override
	public String saveOutsideDocStore(String datas, HttpServletRequest request, UserSessionMgr userMgr)
			throws Exception {
		// 临时文件夹路径
		String folderPath = null;
		// 把json转成对象
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON(datas);
		List li = (List) JSONSerializer.toJava(jsonArray);
		// 总数量
		int totalCount = li.size();
		// 未保存的文件名称
		StringBuffer unSaveDetail = new StringBuffer();
		// 成功保存的文件名
		StringBuffer saveDetail = new StringBuffer();
		// 返回结果
		JSONObject resultObject = new JSONObject();
		List<OutsideDocStore> list = new ArrayList<>();
		try {
			// 开始
			long start = System.currentTimeMillis();
			// 设置默认的涉及领域
			InfoType involvedInTheField = infoTypeService
					.findInfoTypeById(Configured.getInstance().get("involvedInTheField"));

			// logger.info("文档类型"+docInfoType.toString());
			String outsideDocStoreAttachment = Configured.getInstance().get("outsideDocStore");
			// 拷贝到这个文件夹中
			String directory = request.getServletContext().getRealPath("/") + outsideDocStoreAttachment;

			// 拼接查询
			Object[] fileNames = null;
			String hql = "select fileName from OutsideDocStore where";
			for (Object object : li) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				String filePath = (String) bean.get("filePath");
				String fileName = FilenameUtils.getName(filePath).substring(8);
				hql += " (fileName='" + fileName + "' and action<>'3') or";
			}
			hql = hql.substring(0, hql.length() - 2);
			// logger.info("查询存在的hql语句："+hql);
			List<String> nameList = fileNamesDao.findByHql(hql);
			// 转数组
			fileNames = (Object[]) nameList.toArray();
			logger.info("已经存在的文档名称：" + Arrays.toString(fileNames));

			for (Object object : li) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				// 获得实例
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				String docName = (String) bean.get("docName");
				String docNumber = (String) bean.get("docNumber");
				String time = (String) bean.get("time");
				String filePath = (String) bean.get("filePath");
				// 文件夹路径
				folderPath = FilenameUtils.getFullPath(filePath);
				String infoType = (String) bean.get("infoType");

				// 所属类别
				InfoType docInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(infoType,
						StoreFinal.OUTSIDEDOC_STORE);
				// 保存
				OutsideDocStore store = new OutsideDocStore();
				store.setAction("1");
				store.setCreateDate(StringUtil.dates());
				// 设置权限
				store.setUid(userMgr.getUID(request));
				store.setDid(userMgr.getUDID(request));
				store.setOid(userMgr.getUOID(request));
				// 去掉后缀名
				docName = FilenameUtils.getBaseName(docName);
				store.setName(docName);
				store.setDocNumber(docNumber);
				store.setTime(time);
				// 设置类别
				store.setInfoType(docInfoType);
				// 设置涉及领域
				store.setInvolvedInTheField(involvedInTheField);
				// copy文件到vocationalWorkStoreAttachment文件夹中
				FileUtils.copyFileToDirectory(new File(filePath), new File(directory));
				String attaches = outsideDocStoreAttachment + File.separator + FilenameUtils.getName(filePath);
				// 附件
				store.setAttaches(attaches);
				// 是否已经保存过了
				String fileName = FilenameUtils.getName(filePath).substring(8);
				store.setFileName(fileName);
				String error = isValidDoc(docName, docNumber).trim();
				if (StringUtils.length(error) > 0) {
					// 没有标题或期刊号
					unSaveDetail.append(String.format("文档入库失败：%s,%s", fileName, error)).append("<br>");
				} else if (ArrayUtils.contains(fileNames, fileName)) {
					// 已存在
					unSaveDetail.append(String.format("文档已经存在：%s", fileName)).append("<br>");
				} else {
					list.add(store);
					saveDetail.append(String.format("文档成功保存：%s", store.getFileName())).append("<br>");
				}
			}
			// 新增多个
			baseDao.save(list);
			// 更新多个索引
			logger.info("正在添加" + list.size() + "条索引");
			nrtSearch.addIndex(list);
			logger.info("添加" + list.size() + "条索引完成");
			// 结束
			long end = System.currentTimeMillis();
			// 结果
			// 共%s个文件,totalCount
			String saveInfo = String.format("成功%s个,用时%s秒", list.size(), (end - start) / 1000);
			resultObject.element("saveInfo", saveInfo);
			resultObject.element("unSaveDetail", unSaveDetail.toString());
			resultObject.element("saveDetail", saveDetail.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			// 删除临时上传的文件夹
			logger.info("删除临时文件夹路径folderPath：" + folderPath);
			FileUtils.deleteDirectory(new File(folderPath));
		}
		return resultObject.toString();
	}
}
