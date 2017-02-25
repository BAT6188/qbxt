package com.ushine.solr.solrbean;
/**
 * 一键搜索返回的数据类型
 * @author dh
 *
 */
public class MyJsonObject {
	private int dataCount;
	private String datas;
	private String dataType;
	private String storeName;
	/**
	 *
	 * @return
	 */
	public int getDataCount() {
		return dataCount;
	}
	/**
	 * 数据量
	 * @param dataCount
	 */
	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
	@Override
	public String toString() {
		return "MyJsonObject [dataCount=" + dataCount + ", datas=" + datas + ", dataType=" + dataType + ", storeName="
				+ storeName + "]";
	}
	/**
	 * 数据
	 * @return
	 */
	public String getDatas() {
		return datas;
	}
	/**
	 * 数据
	 * @param datas
	 */
	public void setDatas(String datas) {
		this.datas = datas;
	}
	/**
	 * 类中文名
	 * @return
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * 类中文名
	 * @param dataType
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * 类名ClassSimpleName
	 * @return
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * 类名ClassSimpleName
	 * @param storeName
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	
}
