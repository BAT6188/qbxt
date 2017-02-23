package com.ushine.solr.vo;
/**
 * ClueStore对应的vo层模型
 * @author dh
 *
 */
public class ClueStoreVo {

	private String id;
	private String clueName;//线索名称
	private String clueSource;//线索来源
	private String findTime;//发现时间
	private String createDate;
	/**
	 * 带参构造函数
	 * @param cluestoreId
	 * @param clueName
	 * @param clueSource
	 * @param findTime
	 * @param createDate
	 */
	public ClueStoreVo(String id, String clueName, String clueSource, String findTime, String createDate) {
		this.id = id;
		this.clueName = clueName;
		this.clueSource = clueSource;
		this.findTime = findTime;
		this.createDate = createDate;
	}
	public ClueStoreVo() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCreateDate() {
		return createDate;
	}
	@Override
	public String toString() {
		return "ClueStoreVo [id=" + id + ", clueName=" + clueName + ", clueSource=" + clueSource
				+ ", findTime=" + findTime + ", createDate=" + createDate + "]";
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
}
