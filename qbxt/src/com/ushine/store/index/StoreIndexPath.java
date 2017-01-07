package com.ushine.store.index;

import java.io.File;

import com.ushine.common.config.Configured;

/**
 * 获得各个库的索引位置
 * @author Administrator
 *
 */
public final class StoreIndexPath {
	private StoreIndexPath(){
		//私有
	}
	/**
	 * 获得应用程序的根目录
	 * @return
	 */
	private static String getWebRoot(){
		String value=Configured.getInstance().get("rootIndex");
		if(value.equals("true")){
			//程序根目录
			return System.getProperty("qbtest.root");
		}else{
			return value;
		}
	}
	/**
	 * 索引库的根目录
	 * @return
	 */
	private static String getIndexRoot(){
		return getWebRoot()+File.separator+Configured.getInstance().get("baseIndex");
	}
	/**
	 * 各个库的索引位置
	 * @param storename 库的名称
	 * @return
	 */
	private static String getStoreIndexPath(String storename){
		return getIndexRoot()+File.separator+Configured.getInstance().get(storename);
	}
	/**
	 * PersonStore的索引库目录
	 */
	public static final String PERSONSTORE_INDEXPATH=getStoreIndexPath("personStoreIndex");
	/**
	 * OrganizStore的索引库目录
	 */
	public static final String ORGANIZSTORE_INDEXPATH=getStoreIndexPath("organizStoreIndex");
	/**
	 * WebsiteJournalStore的索引库目录
	 */
	public static final String WEBSITEJOURNALSTORE_INDEXPATH=getStoreIndexPath("websiteJournalStoreIndex");
	/**
	 * ClueStore的索引库目录
	 */
	public static final String CLUESTORE_INDEXPATH=getStoreIndexPath("clueStoreIndex");
	/**
	 * OutsideDocStore的索引库目录
	 */
	public static final String OUTSIDEDOCSTORE_INDEXPATH=getStoreIndexPath("outsideDocStoreIndex");
	/**
	 * LeadSpeakStore的索引库目录
	 */
	public static final String LEADSPEAKSTORE_INDEXPATH=getStoreIndexPath("leadSpeakStoreIndex");
	/**
	 * VocationalWorkStore的索引库目录
	 */
	public static final String VOCATIONALWORKSTORE_INDEXPATH=getStoreIndexPath("vocationalWorkStoreIndex");
	/**
	 * 附件的索引目录
	 */
	public static final String ATTACHES_INDEXPATH=getStoreIndexPath("attachesIndex");
	/**
	 * 是否创建索引<br>
	 * conf.peroperties中createIndex默认为false,不创建 <br>
	 * @return boolean
	 */
	public static boolean isCreateIndex() {
		boolean result=false;
		if("true".equals(Configured.getInstance().get("createIndex"))){
			result=true;
		}
		return result;
	}
	/**
	 * 是否为对应的库创建索引
	 * @param indexStore
	 * @return
	 */
	public static boolean isCreateIndex(String indexStore) {
		boolean result=false;
		if("true".equals(Configured.getInstance().get(indexStore))){
			result=true;
		}
		return result;
	}
}
