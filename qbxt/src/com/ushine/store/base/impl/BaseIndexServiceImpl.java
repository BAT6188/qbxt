package com.ushine.store.base.impl;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.NRTManager;
import org.springframework.stereotype.Service;

import com.ushine.store.base.IBaseIndexService;

@Service("baseIndexServiceImpl")
public class BaseIndexServiceImpl implements IBaseIndexService {
	
	@Override
	public void addIndex(NRTManager nrtManager, Document document)throws Exception {
		if(null!=document){
			nrtManager.addDocument(document);
		}
	}

	@Override
	public void addIndex(NRTManager nrtManager, List<Document> documents)throws Exception {
		if (null!=documents) {
			nrtManager.addDocuments(documents);
		}
	}

	@Override
	public void deleteIndex(NRTManager nrtManager, String id) throws Exception {
		if(null!=id){
			nrtManager.deleteDocuments(new Term(id));
		}
	}
	
	@Override
	public void deleteIndex(NRTManager nrtManager, String[]ids) throws Exception {
		Term []terms=new Term[ids.length];
		for (int j = 0; j < ids.length; j++) {
			terms[j]=new Term(ids[j]);
		}
		nrtManager.deleteDocuments(terms);
	}

}
