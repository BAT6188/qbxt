package com.ushine.solr.futuretask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

import com.ushine.common.utils.SpringUtils;
import com.ushine.solr.factory.SolrServerFactory;
import com.ushine.solr.service.IClueStoreSolrService;
import com.ushine.solr.service.ILeadSpeakStoreSolrService;
import com.ushine.solr.service.IOutsideDocStoreSolrService;
import com.ushine.solr.service.IPersonStoreSolrService;
import com.ushine.solr.service.IVocationalStoreSolrService;
import com.ushine.solr.solrbean.MyJsonObject;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.storesinfo.service.IClueStoreService;
import com.ushine.storesinfo.service.ILeadSpeakStoreService;
import com.ushine.storesinfo.service.IOutsideDocStoreService;
import com.ushine.storesinfo.service.IPersonStoreService;
import com.ushine.storesinfo.service.IVocationalWorkStoreService;

/**
 * 异步执行查询获得结果 
 * 
 * @author Administrator
 *
 */
public class MyFutureTask {

	Logger logger = Logger.getLogger(MyFutureTask.class);

	public MyFutureTask(String fieldValue, String oid, String did, String uid, String dataType, Integer size,
			Class clazz) {
		this.fieldValue = fieldValue;
		this.oid = oid;
		this.did = did;
		this.uid = uid;
		this.dataType = dataType;
		this.size = size;
		this.clazz = clazz;
	}

	// 搜索关键字
	private String fieldValue;
	// 组织id
	private String oid;
	// 部门id
	private String did;
	// 个人id
	private String uid;
	// 数据类型
	// "重点人员库","线索库","外来文档库","业务文档库","领导讲话库"
	private String dataType;
	// 显示数量
	private Integer size;
	// PersonStore.class,ClueStore.class,OutsideDocStore.class,
	// VocationalWorkStore.class,LeadSpeakStore.class
	private Class clazz;
	// 各个库的service

	public MyFutureTask() {
	}

	public MyJsonObject getSearchJsonObjectResult() throws InterruptedException, ExecutionException {
		FutureTask<MyJsonObject> task = new FutureTask<>(new Callable<MyJsonObject>() {

			@Override
			public MyJsonObject call() throws Exception {
				MyJsonObject jsonObject = new MyJsonObject();
				jsonObject.setDataCount((int) getSearchCount());
				jsonObject.setDatas(getSearchDatas());
				jsonObject.setDataType(dataType);
				jsonObject.setStoreName(clazz.getSimpleName());
				return jsonObject;
			}
		});
		task.run();
		return task.get();
	}

	private long getSearchCount() {
		String classSimpleName = clazz.getSimpleName();
		QueryBean queryBean = new QueryBean();
		queryBean.setOid(oid);
		queryBean.setDid(did);
		queryBean.setUid(uid);
		queryBean.setQueryFieldValue(fieldValue);
		long searchCount = 0;
		try {
			switch (classSimpleName) {
			case "PersonStore":
				IPersonStoreSolrService personStoreSolrService = (IPersonStoreSolrService) SpringUtils
						.getBean("personStoreSolrServiceImpl");
				searchCount = personStoreSolrService.getDocumentsCount(SolrServerFactory.getPSSolrServerInstance(),
						queryBean);
				break;
			case "ClueStore":
				IClueStoreSolrService clueStoreSolrService = (IClueStoreSolrService) SpringUtils
						.getBean("clueStoreSolrServiceImpl");
				searchCount = clueStoreSolrService.getDocumentsCount(queryBean);
				break;
			case "VocationalWorkStore":
				IVocationalStoreSolrService vocationalStoreSolrService = (IVocationalStoreSolrService) SpringUtils
						.getBean("vocationalStoreSolrServiceImpl");
				searchCount = vocationalStoreSolrService.getDocumentsCount(SolrServerFactory.getVWSSolrServerInstance(),
						queryBean);
				break;
			case "OutsideDocStore":
				IOutsideDocStoreSolrService outsideDocStoreSolrService = (IOutsideDocStoreSolrService) SpringUtils
						.getBean("outsideDocStoreSolrServiceImpl");
				searchCount = outsideDocStoreSolrService.getDocumentsCount(queryBean);
				break;
			case "LeadSpeakStore":
				ILeadSpeakStoreSolrService leadSpeakStoreSolrService = (ILeadSpeakStoreSolrService) SpringUtils
						.getBean("leadSpeakStoreSolrServiceImpl");
				searchCount = leadSpeakStoreSolrService.getDocumentsCount(queryBean);
				break;
			}

		} catch (Exception e) {
			logger.error("查询数量异常：" + e.getMessage());
			e.printStackTrace();
		}
		return searchCount;
	}

	private String getSearchDatas() {
		String datas = new String();
		String classSimpleName = clazz.getSimpleName();
		try {
			switch (classSimpleName) {
			// 获得10条数据
			case "PersonStore":
				IPersonStoreService personStoreService = (IPersonStoreService) SpringUtils
						.getBean("personStoreServiceImpl");
				datas = personStoreService.findPersonStore("anyField", fieldValue, null, null, 1, size, uid, oid, did,
						null, null);
				break;
			case "ClueStore":
				IClueStoreService clueStoreService = (IClueStoreService) SpringUtils.getBean("clueStoreServiceImpl");
				datas = clueStoreService.findClueStore("anyField", fieldValue, null, null, 1, size, uid, oid, did, null,
						null);
				break;
			case "VocationalWorkStore":
				IVocationalWorkStoreService vocationalWorkStoreService = (IVocationalWorkStoreService) SpringUtils
						.getBean("vocationalWorkStoreServiceImpl");
				datas = vocationalWorkStoreService.findVocationalWorkStore("anyField", fieldValue, null, null, 1, size,
						uid, oid, did, null, null);
				break;
			case "OutsideDocStore":
				IOutsideDocStoreService outsideDocStoreService = (IOutsideDocStoreService) SpringUtils
						.getBean("outsideDocStoreServiceImpl");
				datas = outsideDocStoreService.findOutsideDocStore("anyField", fieldValue, null, null, 1, size, uid,
						oid, did, null, null);
				break;
			case "LeadSpeakStore":
				ILeadSpeakStoreService leadSpeakStoreService = (ILeadSpeakStoreService) SpringUtils
						.getBean("leadSpeakStoreServiceImpl");
				datas = leadSpeakStoreService.findLeadSpeakStore("anyField", fieldValue, null, null, 1, size, uid, oid,
						did, null, null);
				break;
			}
		} catch (Exception e) {
			logger.error("查询符合条件的结果异常：" + e.getMessage());
			e.printStackTrace();
		}
		return datas;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
