package com.ushine.solr.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import com.ushine.solr.service.IVocationalStoreSolrService;
import com.ushine.solr.solrbean.QueryBean;
import com.ushine.solr.vo.VocationalWorkStoreVo;
import com.ushine.storesinfo.model.VocationalWorkStore;

public class VocationalStoreSolrServiceImpl implements IVocationalStoreSolrService {

	Logger logger=Logger.getLogger(VocationalStoreSolrServiceImpl.class);
	@Override
	public int addDocumentByStore(HttpSolrServer server, VocationalWorkStore daoStore) {
		
		return 0;
	}

	@Override
	public int addDocumentByStores(HttpSolrServer server, List<VocationalWorkStore> daoStore) {
		
		return 0;
	}

	@Override
	public void createNewIndexByStores(HttpSolrServer server) {
		
	}

	@Override
	public void deleteDocumentById(HttpSolrServer server, String id) {
		
	}

	@Override
	public void deleteDocumentByIds(HttpSolrServer server, String[] ids) {
		
	}

	@Override
	public void deleteAll(HttpSolrServer server) {
		
	}

	@Override
	public void updateDocumentByStore(HttpSolrServer server, String id, VocationalWorkStore daoStore) {
		
	}

	@Override
	public long getDocumentsCount(HttpSolrServer server, QueryBean queryBean) {
		
		return 0;
	}

	@Override
	public void closeServer(HttpSolrServer server) {
		if (null!=server) {
			server.shutdown();
		}
	}

	@Override
	public List<VocationalWorkStoreVo> getDocuementsVO(HttpSolrServer server, QueryBean bean, int start, int rows) {
		// TODO Auto-generated method stub
		return null;
	}

}
