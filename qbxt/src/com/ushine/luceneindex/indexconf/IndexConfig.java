package com.ushine.luceneindex.indexconf;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.SearcherWarmer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 索引的配置项
 * 
 * @author Administrator
 *
 */
public class IndexConfig {
	private IndexWriter indexWriter;
	private NRTManager nRTManager;
	private static Logger logger = Logger.getLogger(IndexConfig.class);

	/**
	 * 构造函数
	 * 
	 * @param indexPath
	 *            索引路径
	 * @param warmInfo
	 *            索引变化时提示信息
	 * @param threadName
	 *            线程名称
	 */
	public IndexConfig(String indexPath, final String warmInfo, String threadName) {
		try {
			Directory directory = FSDirectory.open(new File(indexPath));
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, new IKAnalyzer(true));
			indexWriter = new IndexWriter(directory, conf);
			nRTManager = new NRTManager(indexWriter, new SearcherWarmer() {
				public void warm(IndexSearcher indexsearcher) throws IOException {
					// 索引变化时
					logger.info(warmInfo);
				}
			});
			// 线程后台运行
			NRTManagerReopenThread thread = new NRTManagerReopenThread(nRTManager, 0.01, 0.001);
			thread.setName(threadName);
			thread.setDaemon(true);
			// 线程开启
			thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得IndexWriter
	 * 
	 * @return IndexWriter
	 */
	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	/**
	 * 获得NRTManager
	 * 
	 * @return NRTManager
	 */
	public NRTManager getNRTManager() {
		return nRTManager;
	}
}
