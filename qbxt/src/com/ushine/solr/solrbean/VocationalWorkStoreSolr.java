package com.ushine.solr.solrbean;

import org.apache.solr.client.solrj.beans.Field;

/**
 * VocationalWorkStore对应的solr中的对象
 * @author Administrator
 *
 */
public class VocationalWorkStoreSolr {

	@Field
	private String vocationalWorkStoreId;
	@Field
	private String docName;
	@Field
	private String docNumber;
	@Field
	private String time;
	@Field
	private String theOriginal;
	@Field
	private String infoType;
	@Field
	private String uid;
	@Field
	private String oid;
	@Field
	private String did;
	@Field
	private long createDate;
	@Field
	private String attaches;
	/**
	 * 包含id的构造函数
	 * @param vocationalWorkStoreId
	 * @param docName
	 * @param docNumber
	 * @param time
	 * @param theOriginal
	 * @param infoType
	 * @param uid
	 * @param oid
	 * @param did
	 * @param createDate
	 * @param attaches
	 */
	public VocationalWorkStoreSolr(String vocationalWorkStoreId, String docName, String docNumber, String time,
			String theOriginal, String infoType, String uid, String oid, String did, long createDate,
			String attaches) {
		super();
		this.vocationalWorkStoreId = vocationalWorkStoreId;
		this.docName = docName;
		this.docNumber = docNumber;
		this.time = time;
		this.theOriginal = theOriginal;
		this.infoType = infoType;
		this.uid = uid;
		this.oid = oid;
		this.did = did;
		this.createDate = createDate;
		this.attaches = attaches;
	}
	/**
	 * 没有id的构造函数
	 * @param docName
	 * @param docNumber
	 * @param time
	 * @param theOriginal
	 * @param infoType
	 * @param uid
	 * @param oid
	 * @param did
	 * @param createDate
	 * @param attaches
	 */
	public VocationalWorkStoreSolr(String docName, String docNumber, String time, String theOriginal, String infoType,
			String uid, String oid, String did, long createDate, String attaches) {
		super();
		this.docName = docName;
		this.docNumber = docNumber;
		this.time = time;
		this.theOriginal = theOriginal;
		this.infoType = infoType;
		this.uid = uid;
		this.oid = oid;
		this.did = did;
		this.createDate = createDate;
		this.attaches = attaches;
	}
	
	public VocationalWorkStoreSolr() {
		
	}
	public String getVocationalWorkStoreId() {
		return vocationalWorkStoreId;
	}
	public void setVocationalWorkStoreId(String vocationalWorkStoreId) {
		this.vocationalWorkStoreId = vocationalWorkStoreId;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTheOriginal() {
		return theOriginal;
	}
	public void setTheOriginal(String theOriginal) {
		this.theOriginal = theOriginal;
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
	public String getAttaches() {
		return attaches;
	}
	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}
	@Override
	public String toString() {
		return "VocationalWorkStoreSolr [vocationalWorkStoreId=" + vocationalWorkStoreId + ", docName=" + docName
				+ ", docNumber=" + docNumber + ", time=" + time + ", infoType=" + infoType + ", uid=" + uid + ", oid="
				+ oid + ", did=" + did + ", createDate=" + createDate + "]";
	}
	
}
