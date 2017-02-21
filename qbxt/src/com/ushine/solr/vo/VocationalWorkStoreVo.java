package com.ushine.solr.vo;
/**
 * 业务文档vo层对象
 * @author dh
 *
 */
public class VocationalWorkStoreVo {

	@Override
	public String toString() {
		return "VocationalWorkStoreVo [id=" + id + ", docName=" + docName + ", docNumber=" + docNumber + ", time="
				+ time + ", infoType=" + infoType + ", createDate=" + createDate + "]";
	}
	private String id;
	private String docName;
	private String docNumber;
	private String time;
	private String infoType;
	private String createDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
}
