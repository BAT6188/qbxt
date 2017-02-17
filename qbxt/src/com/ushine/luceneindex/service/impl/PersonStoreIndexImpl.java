package com.ushine.luceneindex.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ushine.luceneindex.base.IBaseIndexService;
import com.ushine.luceneindex.nrtmanager.PersonStoreNRTManager;
import com.ushine.luceneindex.service.IPersonStoreIndexService;
import com.ushine.storesinfo.model.PersonStore;
/**
 * 人员索引操作实现类
 * @author Administrator
 *
 */
@Service("personStoreIndexImpl")
public class PersonStoreIndexImpl implements IPersonStoreIndexService{
	private static Logger logger = Logger.getLogger(PersonStoreIndexImpl.class);
	@Autowired
	private IBaseIndexService baseIndexServiceImpl;
	//PersonStoreNRTManager.getInstance().getNRTManager()
	NRTManager nrtManager=null;
	@Override
	public  Document storeToDocument(PersonStore store) throws Exception {
		
		return null;
	}

	@Override
	public  void createIndex(List<PersonStore> list) throws Exception {
		
	}

	@Override
	public  void saveIndex(PersonStore store) throws Exception {
		//新增一条
		Document document=storeToDocument(store);
		baseIndexServiceImpl.addIndex(nrtManager, document);
		logger.info("新增一条人员索引成功");
	}

	@Override
	public  void saveIndex(List<PersonStore> list) throws Exception {
		List<Document> documents=new ArrayList<>();
		for (PersonStore store : list) {
			Document document=storeToDocument(store);
			if (null!=document){
				documents.add(storeToDocument(store));
			}
		}
		baseIndexServiceImpl.addIndex(nrtManager, documents);
		logger.info("新增人员集合索引成功");
	}

	@Override
	public  void updateIndex(PersonStore store) throws Exception {
		
	}

	@Override
	public  void deleteIndex(String id) throws Exception {
		baseIndexServiceImpl.deleteIndex(nrtManager, id);
	}

	@Override
	public  void deleteIndex(String []ids) throws Exception {
		baseIndexServiceImpl.deleteIndex(nrtManager, ids);
	}

	@Override
	public int getCount(Query query, Filter filter) throws Exception {
		
		return 0;
	}

	@Override
	public List<Document> getDocuments(Query query, Filter filter, String sortField, String dir, int nowPage,
			int pageSize) throws Exception {
		
		return null;
	}
}
