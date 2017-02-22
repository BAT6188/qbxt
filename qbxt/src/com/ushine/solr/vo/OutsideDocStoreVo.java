package com.ushine.solr.vo;


/**
 * 外来文档vo层模型
 * @author dh
 *
 */
public class OutsideDocStoreVo {
	private String id;
	private String name;
	private String sourceUnit;
	private String secretRank;
	private String time;
	private String docNumber;
	private String infoType;
	private String createDate;
	
	
	public OutsideDocStoreVo() {
	}

	/**
	 * 带参构造函数
	 * @param id
	 * @param name
	 * @param sourceUnit
	 * @param secretRank
	 * @param time
	 * @param docNumber
	 * @param infoType
	 * @param createDate
	 */
	public OutsideDocStoreVo(String id, String name, String sourceUnit, String secretRank, String time, 
			String docNumber, String infoType, String createDate) {
		this.id = id;
		this.name = name;
		this.sourceUnit = sourceUnit;
		this.secretRank = secretRank;
		this.time = time;
		this.docNumber = docNumber;
		this.infoType = infoType;
		this.createDate = createDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSourceUnit() {
		return sourceUnit;
	}

	public void setSourceUnit(String sourceUnit) {
		this.sourceUnit = sourceUnit;
	}

	public String getSecretRank() {
		return secretRank;
	}

	public void setSecretRank(String secretRank) {
		this.secretRank = secretRank;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "OutsideDocStoreVo [id=" + id + ", name=" + name + ", sourceUnit=" + sourceUnit + ", secretRank=" 
				+ secretRank + ", time=" + time + ", docNumber=" + docNumber + ", infoType="
				+ infoType + ", createDate=" + createDate + "]";
	}
}
