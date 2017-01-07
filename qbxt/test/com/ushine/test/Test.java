package com.ushine.test;

import java.io.File;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.CachingWrapperFilter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ushine.common.utils.SpringUtils;
import com.ushine.common.vo.Paging;
import com.ushine.common.vo.PagingObject;
import com.ushine.core.po.Resource;
import com.ushine.core.service.IPersonService;
import com.ushine.core.service.IResourceService;
import com.ushine.dao.IBaseDao;
import com.ushine.store.index.StoreIndexPath;
import com.ushine.store.index.StoreIndexQuery;
import com.ushine.storeInfo.model.Attaches;
import com.ushine.storeInfo.model.ClueRelationship;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.LeadSpeakStore;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.IClueRelationshipService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.util.ReadAttachUtil;
import com.ushine.util.WordToHtmlUtil;

import fr.opensagres.xdocreport.core.utils.StringEscapeUtils;

@Component("mytest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class Test {

	@Autowired
	IPersonService personSevice;
	@Autowired
	IResourceService resourceService;
	@Autowired IBaseDao baseDao;
	@Autowired
	IClueRelationshipService clueRelationshipService;
	public static void main(String[] args) throws Exception {
		/*String result=new String();
		result=ReadAttachUtil.readContent("f://天龙八部.txt");
		System.err.println(result);*/
		//System.err.println(StoreIndexPath.ATTACHES_INDEXPATH);
		//AttachesNRTSearch attachesNRTSearch=AttachesNRTSearch.getInstance();
		WordToHtmlUtil.wordToHtml("f://讲话一.doc", "f://讲话.html");
		String result=FileUtils.readFileToString(new File("f://讲话.html"));
		System.err.println(result);
		System.err.println("============反转义============");
		StringEscapeUtils.Entities entities=new StringEscapeUtils.Entities();
		String result2=entities.unescape(result);
		System.err.println(result2);
		//System.err.println("==========AttachesNRTSearch=========="+(attachesNRTSearch.getTotalCount()));
		/*Query query=MultiFieldQueryParser.parse(Version.LUCENE_35, new String[]{"福州新闻"}, 
				new String[]{"attachContent"}, new IKAnalyzer(true));
		System.err.println(attachesNRTSearch.getCount(query, null));*/
		//System.err.println(ReadAttachUtil.readContent("f://组织.txt"));
	}
	
	@org.junit.Test
	public void test()throws Exception{
		System.err.println(clueRelationshipService.findCluesIdByStoreId("40288a625624b632015624b6466e0004", "personStore"));
		//AttachesNRTSearch attachesNRTSearch=AttachesNRTSearch.getInstance();
		//System.err.println("==========AttachesNRTSearch=========="+(attachesNRTSearch.getTotalCount()));
	}
}
