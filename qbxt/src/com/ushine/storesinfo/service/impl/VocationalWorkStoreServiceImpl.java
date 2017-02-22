package com.ushine.storesinfo.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.servlet.http.HttpServletRequest;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import com.ushine.common.utils.XMLUtils;
import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.common.vo.ViewObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.StoreIndexQuery;
import com.ushine.luceneindex.index.VocationalWorkStoreNRTSearch;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IVocationalStoreSolrService;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.util.JSonUtils;
import com.ushine.solr.util.SolrBeanUtils;
import com.ushine.solr.vo.PersonStoreVo;
import com.ushine.solr.vo.VocationalWorkStoreVo;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.VocationalWorkStore;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.IVocationalWorkStoreService;
import com.ushine.storesinfo.storefinal.StoreFinal;
import com.ushine.util.IdentifyDocUtils;
import com.ushine.util.SmbFileUtils;
import com.ushine.util.StringUtil;

/**
 * 业务文档接口实现类
 * 
 * @author wangbailin
 *
 */
@Transactional
@Service("vocationalWorkStoreServiceImpl")
public class VocationalWorkStoreServiceImpl implements IVocationalWorkStoreService {
	private static final Logger logger = LoggerFactory.getLogger(VocationalWorkStoreServiceImpl.class);
	@Autowired
	private IBaseDao<VocationalWorkStore, String> baseDao;
	@Autowired
	private IBaseDao fileNamesDao;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired IVocationalStoreSolrService solrService;
	
	HttpSolrServer server=SolrServerFactory.getVWSSolrServerInstance();
	public boolean saveVocationalWork(VocationalWorkStore vocationalWork) throws Exception {
		// 新增
		baseDao.save(vocationalWork);
		solrService.addDocumentByStore(server, vocationalWork);
		return true;
	}

	@Override
	public boolean saveVocationalWork(List<VocationalWorkStore> list) throws Exception {
		baseDao.save(list);
		// 更新多个索引
		logger.info("正在添加" + list.size() + "条索引");
		solrService.addDocumentByStores(server, list);
		logger.info("添加" + list.size() + "条索引完成");
		return false;
	}

	@Override
	public String saveVocationalWork(String datas, String infoType, HttpServletRequest request, UserSessionMgr userMgr,
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
		List<VocationalWorkStore> list = new ArrayList<>();
		//删除临时上传的文件夹
		String tempFolderPath=null;
		try {
			// 开始
			long start = System.currentTimeMillis();
			// 设置默认的涉及领域
			InfoType involvedInTheField = infoTypeService
					.findInfoTypeById(Configured.getInstance().get("involvedInTheField"));
			// 所属类别
			InfoType docInfoType = infoTypeService.findInfoTypeById(infoType);
			// logger.info("文档类型"+docInfoType.toString());
			String vocationalWorkStoreAttachment = Configured.getInstance().get("vocationalWorkStore");
			// 拷贝到这个文件夹中
			String directory = request.getServletContext().getRealPath("/") + vocationalWorkStoreAttachment;

			// 拼接查询
			Object[] fileNames = null;
			String hql = "select fileName from VocationalWorkStore where";
			for (Object object : li) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				MorphDynaBean bean = (MorphDynaBean) JSONObject.toBean(jsonObject);
				String filePath = (String) bean.get("filePath");
				//获得路径
				tempFolderPath=FilenameUtils.getFullPath(filePath);
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
				// 保存
				VocationalWorkStore store = new VocationalWorkStore();
				store.setAction("1");
				store.setCreateDate(StringUtil.dates());
				// 设置权限
				store.setUid(userMgr.getUID(request));
				store.setDid(userMgr.getUDID(request));
				store.setOid(userMgr.getUOID(request));
				// 去掉后缀名
				docName = FilenameUtils.getBaseName(docName);
				store.setDocName(docName);
				store.setDocNumber(docNumber);
				store.setTime(time);
				// 设置类别
				store.setInfoType(docInfoType);
				// 设置涉及领域
				//store.setInvolvedInTheField(involvedInTheField);
				// copy文件到vocationalWorkStoreAttachment文件夹中
				FileUtils.copyFileToDirectory(new File(filePath), new File(directory));
				String attaches = vocationalWorkStoreAttachment + File.separator + FilenameUtils.getName(filePath);
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
				/*
				 * else if (hasSavedStore(fileName)) { //已存在
				 * unSaveDetail.append(String.format("文档已经存在：%s",
				 * fileName)).append("<br>"); }
				 */
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
			solrService.addDocumentByStores(server, list);
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
		}finally {
			logger.info("删除临时上传的文件夹路径："+tempFolderPath);
			File file=new File(tempFolderPath);
			if(file.exists()){
				FileUtils.deleteDirectory(file);
			}
		}
		return resultObject.toString();
	}

	@Override
	public String saveVocationalWork(String datas, HttpServletRequest request, UserSessionMgr userMgr)
			throws Exception {
		//临时文件夹路径
		String folderPath=null;
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
		List<VocationalWorkStore> list = new ArrayList<>();
		try {
			// 开始
			long start = System.currentTimeMillis();
			// 设置默认的涉及领域
			InfoType involvedInTheField = infoTypeService.findInfoTypeById(Configured.getInstance().get("involvedInTheField"));
			
			// logger.info("文档类型"+docInfoType.toString());
			String vocationalWorkStoreAttachment = Configured.getInstance().get("vocationalWorkStore");
			// 拷贝到这个文件夹中
			String directory = request.getServletContext().getRealPath("/") + vocationalWorkStoreAttachment;
			
			// 拼接查询
			Object[] fileNames = null;
			String hql = "select fileName from VocationalWorkStore where";
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
				//文件夹路径
				folderPath=FilenameUtils.getFullPath(filePath);
				String infoType= (String) bean.get("infoType");
				
				/*logger.info("docName："+docName);
				logger.info("docNumber："+docNumber);
				logger.info("time："+time);
				logger.info("filePath："+filePath);
				logger.info("infoType："+infoType);*/
				// 所属类别
				InfoType docInfoType = infoTypeService.findInfoTypeByTypeNameAndTableName(infoType, 
						StoreFinal.VOCATIONAL_WORK_STORE);
				// 保存
				VocationalWorkStore store = new VocationalWorkStore();
				store.setAction("1");
				store.setCreateDate(StringUtil.dates());
				// 设置权限
				store.setUid(userMgr.getUID(request));
				store.setDid(userMgr.getUDID(request));
				store.setOid(userMgr.getUOID(request));
				// 去掉后缀名
				docName = FilenameUtils.getBaseName(docName);
				store.setDocName(docName);
				store.setDocNumber(docNumber);
				store.setTime(time);
				// 设置类别
				store.setInfoType(docInfoType);
				// 设置涉及领域
				//store.setInvolvedInTheField(involvedInTheField);
				// copy文件到vocationalWorkStoreAttachment文件夹中
				FileUtils.copyFileToDirectory(new File(filePath), new File(directory));
				String attaches = vocationalWorkStoreAttachment + File.separator + FilenameUtils.getName(filePath);
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
			solrService.addDocumentByStores(server, list);
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
		}finally {
			//删除临时上传的文件夹
			logger.info("删除临时文件夹路径folderPath："+folderPath);
			FileUtils.deleteDirectory(new File(folderPath));
		}
		return resultObject.toString();
	}

	public VocationalWorkStore findVocationalWorkById(String vocationWorkId) throws Exception {
		VocationalWorkStore vocationalWorkStore = baseDao.findById(VocationalWorkStore.class, vocationWorkId);
		return vocationalWorkStore;
	}

	public List<VocationalWorkStore> findVocationalWorkAll() throws Exception {
		List<VocationalWorkStore> list = baseDao.findAll(VocationalWorkStore.class);
		return list;
	}

	public List<VocationalWorkStore> findVocationalWorkAll(String hql) throws Exception {
		// 查询时根据action不为3的;
		// 3为删除掉的
		List<VocationalWorkStore> list = baseDao.findByHql(hql);
		return list;
	}

	@SuppressWarnings("unchecked")
	public String findVocationalWorkStore(String field, String fieldValue, String startTime, String endTime,
			int nextPage, int size, String uid, String oid, String did) throws Exception {
		logger.debug("筛选业务文档信息,field:" + field + ",fieldValue:" + fieldValue + ",startTime:" + startTime + "endTime:"
				+ endTime + "");
		return null;
	}

	/**
	 * 含有排序的查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findVocationalWorkStore(String field, String fieldValue, String startTime, String endTime,
			int nextPage, int size, String uid, String oid, String did, String sortField, String dir) throws Exception {
		// 根据条件查询业务文档
		//查询
		if (StringUtils.equals(field, "anyField")) {
			//任意字段查询
			field=QueryBean.VOCATIONALWORKSTOREALL;
		}
		QueryBean queryBean=new QueryBean(uid, oid, did, field, fieldValue, null, null, sortField,dir, startTime, endTime);
		//查询总数
		long totalRecord = solrService.getDocumentsCount(server, queryBean);
		Paging paging = new Paging(size, nextPage, totalRecord);
		PagingObject<VocationalWorkStoreVo> vo = new PagingObject<>();
		vo.setPaging(paging);
		
		// 集合
		// nextPage从1开始 
		List<VocationalWorkStoreVo> array = solrService.getDocuementsVO(server, queryBean, (nextPage - 1) * size, size);
		if (StringUtils.isNotBlank(fieldValue)) {
			// 有关键字要高亮
			List<VocationalWorkStoreVo> highlightArray = SolrBeanUtils.highlightVoList(array, VocationalWorkStoreVo.class, fieldValue);
			vo.setArray(highlightArray);
		} else {
			vo.setArray(array);
		}
		return JSonUtils.toJson(vo);
	}
	
	/**
	 * 字符串可否转日期
	 * 
	 * @param string
	 * @return
	 */
	private static boolean stringToDate(String string) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 能转成date
			Date date = sdf.parse(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 把业务文档集合转成json
	 * 
	 * @param vo
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	// ,Map<String, String> infoTypeMap,Map<String, String> fieldMap
	public String voToJson(PagingObject<VocationalWorkStore> vo) {
		JSONObject root = new JSONObject();
		root.element("paging", vo.getPaging());
		JSONArray array = new JSONArray();
		for (VocationalWorkStore store : vo.getArray()) {
			JSONObject obj = new JSONObject();
			obj.put("id", store.getId());
			obj.put("docName", store.getDocName());
			obj.put("docNumber", store.getDocNumber());
			obj.put("time", store.getTime());
			obj.put("theOriginal", store.getTheOriginal());
			obj.put("createDate", store.getCreateDate());
			// 类别
			// obj.put("infoType", infoTypeMap.get(store.getId()));
			// 涉及领域
			// obj.put("involvedInTheField", fieldMap.get(store.getId()));
			/*if (store.getInvolvedInTheField() != null) {
				// 不为空
				obj.put("involvedInTheField", store.getInvolvedInTheField().getTypeName());
			}*/
			// 类别不为空
			if (store.getInfoType() != null) {
				obj.put("infoType", store.getInfoType().getTypeName());
			}
			array.add(obj);
		}
		root.element("datas", array);
		return root.toString();
	}

	public boolean delVocationalWorkStoreByIds(String[] storeIds) throws Exception {
		// 把action的值变为3
		try {
			StringBuffer buffer = new StringBuffer("update VocationalWorkStore set action='3' where ");
			for (String string : storeIds) {
				buffer.append(String.format("id='%s' or ", string));
			}
			String hql = buffer.toString().trim();
			// 去掉最后的or
			hql = StringUtils.substring(hql, 0, hql.length() - 2);
			logger.info("删除语句：" + hql);
			// 执行删除
			baseDao.executeHql(hql);
			// 删除索引
			solrService.deleteDocumentByIds(server, storeIds);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateVocationalWork(VocationalWorkStore vocationalWorkStore) throws Exception {
		String id = vocationalWorkStore.getId();
		// 更新业务文档
		baseDao.update(vocationalWorkStore);
		solrService.updateDocumentByStore(server, id, vocationalWorkStore);
		return false;
	}

	/**
	 * 读取xml文件配置项，判断是否能否识别该类型
	 * 
	 * @param nodeName
	 *            节点名称
	 * @param docType
	 *            类型包括技侦情报、十二局信息、通知等
	 * @return
	 */
	private boolean isCanBeIdentified(String nodeName, String docType) {
		boolean b = false;
		try {
			String xml = PathUtils.getConfigPath(VocationalWorkStoreServiceImpl.class) + "vocational-work-store.xml";
			XMLUtils utils = new XMLUtils(xml);
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
	public boolean hasSavedStore(String fileName) throws Exception {
		boolean result = false;
		try {
			String hql = String.format("select id from VocationalWorkStore where fileName='%s' and action <>'3'",
					fileName);
			List<VocationalWorkStore> list = baseDao.findByHql(hql);
			if (list.size() > 0) {
				result = true;
				logger.info(fileName + "已经存在于数据库中");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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

	/**
	 * 识别文件夹中的业务文档
	 */
	public String identifyServiceDoc(String directoryPath, String docType) throws Exception {
		// 识别导入的文件夹中的业务文档
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
				// 技侦情报、十二局信息文件名格式为 公安技侦情报第00100期—习近平在哈萨克斯坦纳扎尔巴耶夫大学发表重要演讲
				String fileName = file.getName();
				String docNumber = null;
				String title = null;
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
				// 默认的时间是今天
				String time = format2.format(new Date());
				// 技侦情报、十二局
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
				// 请示 报告 通知 函是另外一种形式 类别(数字)号—名称
				if (isCanBeIdentified("second-type", docType) && fileName.contains(docType)) {
					// 文档最后修改时间
					SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
					Date date = format1.parse(fileName.substring(0, 8));
					time = format2.format(date);
					// 期刊号
					if (fileName.contains("(") && fileName.contains(")")) {
						// 英文括号
						int index0 = fileName.indexOf("(");
						int index1 = fileName.indexOf(")");
						// 期刊号
						if (index1 > index0) {
							docNumber = fileName.substring(index0 + 1, index1);
						}
						// 标题
						title = IdentifyDocUtils.getTitle(fileName);
						if (StringUtils.isEmpty(title)) {
							int index2 = fileName.indexOf("号");
							if (index2 > index1) {
								title = StringUtils.substring(fileName, index2 + 1);
							} else {
								title = StringUtils.substring(fileName, index1 + 1);
							}
						}
					} else if (fileName.contains("（") && fileName.contains("）")) {
						// 中文括号
						int index0 = fileName.indexOf("（");
						int index1 = fileName.indexOf("）");
						// 期刊号
						if (index1 > index0) {
							docNumber = fileName.substring(index0 + 1, index1);
						}
						// 标题
						title = IdentifyDocUtils.getTitle(fileName);
						if (StringUtils.isEmpty(title)) {
							int index2 = fileName.indexOf("号");
							if (index2 > index1) {
								title = StringUtils.substring(fileName, index2 + 1);
							} else {
								title = StringUtils.substring(fileName, index1 + 1);
							}
						}
					}
					logger.info("docNumber:" + docNumber + ",time:" + time + ",title:" + title);
				}
				// 维护网上政治安全专项工作机制---侦控工作第1期（2015年10月1日至2015年10月30日）
				if (isCanBeIdentified("third-type", docType) && fileName.contains(docType)) {

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

	@Override
	public boolean hasStoreByDocName(String docName) {
		// 根据文档名称判断是否存在
		boolean result = false;
		try {
			String hql = String.format("select id from VocationalWorkStore where docName='%s'" + "and action<>'3'",
					docName);
			List<VocationalWorkStore> list = baseDao.findByHql(hql);
			if (list.size() > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}

