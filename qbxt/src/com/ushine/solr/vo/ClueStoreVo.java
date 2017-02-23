package com.ushine.solr.vo;
/**
 * ClueStore对应的vo层模型
 * @author dh
 *
 */
public class ClueStoreVo {

	private String cluestoreId;
	private String clueName;//线索名称
	private String clueSource;//线索来源
	private String findTime;//发现时间
	private long createDate;
	/**
	 * 带参构造函数
	 * @param cluestoreId
	 * @param clueName
	 * @param clueSource
	 * @param findTime
	 * @param createDate
	 */
	public ClueStoreVo(String cluestoreId, String clueName, String clueSource, String findTime, long createDate) {
		this.cluestoreId = cluestoreId;
		this.clueName = clueName;
		this.clueSource = clueSource;
		this.findTime = findTime;
		this.createDate = createDate;
	}
	public ClueStoreVo() {
		
	}
	public String getCluestoreId() {
		return cluestoreId;
	}
	public void setCluestoreId(String cluestoreId) {
		this.cluestoreId = cluestoreId;
	}
	public String getClueName() {
		return clueName;
	}
	public void setClueName(String clueName) {
		this.clueName = clueName;
	}
	public String getClueSource() {
		return clueSource;
	}
	public void setClueSource(String clueSource) {
		this.clueSource = clueSource;
	}
	public String getFindTime() {
		return findTime;
	}
	public void setFindTime(String findTime) {
		this.findTime = findTime;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	
}
