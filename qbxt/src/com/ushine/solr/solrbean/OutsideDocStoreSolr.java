package com.ushine.solr.solrbean;

import org.apache.solr.client.solrj.beans.Field;

/**
 * OutsideDocStore对应的solrBean
 * 	
 * @author dh
 *
 */
public class OutsideDocStoreSolr {
	@Field
	private String outsideDocId;
	@Field
	private String sourceUnit;
	@Field
	private String secretRank;
	@Field
	private String name;//文档名称
	@Field
	private String time;//时间
	@Field
	private String docNumber;// 文档编号
	@Field
	private String attaches;//附件名称
	@Field
	private String centent;//内容
	@Field
	private String infoType;//关联信息类别
	@Field
	private String uid; // 创建人
	@Field
	private String oid; // 组织
	@Field
	private String did; // 部门
	@Field
	private long createDate;//当前数据创建时间
	/**
	 * 带参构造方法
	 * @param outsideDocId id唯一值
	 * @param sourceUnit 来源单位
	 * @param secretRank 密级
	 * @param name 文档名
	 * @param time 时间
	 * @param docNumber 文档编号
	 * @param infoType 所属类别
	 * @param createDate 入库时间
	 */
	public OutsideDocStoreSolr(String outsideDocId, String sourceUnit, String secretRank, String name, 
			String time, String docNumber, String infoType, long createDate) {
		this.outsideDocId = outsideDocId;
		this.sourceUnit = sourceUnit;
		this.secretRank = secretRank;
		this.name = name;
		this.time = time;
		this.docNumber = docNumber;
		this.infoType = infoType;
		this.createDate = createDate;
	}

	public OutsideDocStoreSolr() {
		
	}

	public String getOutsideDocId() {
		return outsideDocId;
	}

	public void setOutsideDocId(String outsideDocId) {
		this.outsideDocId = outsideDocId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAttaches() {
		return attaches;
	}

	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}

	public String getCentent() {
		return centent;
	}

	public void setCentent(String centent) {
		this.centent = centent;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "OutsideDocStoreSolr [outsideDocId=" + outsideDocId + ", sourceUnit=" + sourceUnit + ", secretRank=" + secretRank + ", name=" + name + ", time=" + time + ", docNumber=" + docNumber
				+ ", infoType=" + infoType + ", createDate=" + createDate + "]";
	}
	
}
