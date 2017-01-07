package com.ushine.store.nrtmanager;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.NRTManager;

import com.ushine.store.index.StoreIndexPath;
import com.ushine.store.indexconf.IndexConfig;
/**
 * 人员库索引的NRTManager单例对象
 * @author Administrator
 *
 */
public class PersonStoreNRTManager {
	private static Logger logger = Logger.getLogger(PersonStoreNRTManager.class);
	
	private static IndexWriter indexWriter;
	private static NRTManager nrtManager;
	private static PersonStoreNRTManager personStoreNRTManager;
	
	private PersonStoreNRTManager() {
		//私有化构造函数
	}
	
	/**
	 * 获得PersonStoreNRTManager单例对象
	 * @return
	 */
	public static PersonStoreNRTManager getInstance(){
		return personStoreNRTManager;
	}
	
	static{
		/*try {
			personStoreNRTManager=new PersonStoreNRTManager();
			//人员索引根路径
			String indexPath = StoreIndexPath.PERSONSTORE_INDEXPATH;
			IndexConfig indexConfig=new IndexConfig(indexPath, "人员索引库更新", "人员索引库线程");
			indexWriter=indexConfig.getIndexWriter();
			nrtManager=indexConfig.getNRTManager();
		} catch (Exception e) {
			logger.error("实例化PersonStoreNRTManager对象失败");
			e.printStackTrace();
		}*/
	}
	/**
	 * 获得NRTManager对象实例
	 * @return NRTManager实例
	 */
	public NRTManager getNRTManager(){
		return nrtManager;
	}
	/**
	 * 获得IndexWriter对象实例
	 * @return IndexWriter实例
	 */
	public IndexWriter getIndexWriter(){
		return indexWriter;
	}
}
