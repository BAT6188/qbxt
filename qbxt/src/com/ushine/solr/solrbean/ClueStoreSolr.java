package com.ushine.solr.solrbean;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 线索对应的solrbean
 * @author dh
 *
 */
public class ClueStoreSolr {
	@Field
	private String cluestoreId;
	@Field
	private String clueName;//线索名称
	@Field
	private String clueSource;//线索来源
	@Field
	private String findTime;//发现时间
	@Field
	private String clueContent;//线索内容
	@Field
	private String arrangeAndEvolveCondition;//工作部署及进展情况
	@Field
	private String uid; // 创建人
	@Field
	private String oid; // 组织
	@Field
	private String did; // 部门
	//当前数据创建时间
	@Field
	private long createDate;
	//关联的人员id集合
	@Field
	private String personId;
	
	public ClueStoreSolr() {
	}
	public ClueStoreSolr(String cluestoreId, String clueName, String clueSource, String findTime, long createDate) {
		super();
		this.cluestoreId = cluestoreId;
		this.clueName = clueName;
		this.clueSource = clueSource;
		this.findTime = findTime;
		this.createDate = createDate;
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
	public String getClueContent() {
		return clueContent;
	}
	public void setClueContent(String clueContent) {
		this.clueContent = clueContent;
	}
	public String getArrangeAndEvolveCondition() {
		return arrangeAndEvolveCondition;
	}
	public void setArrangeAndEvolveCondition(String arrangeAndEvolveCondition) {
		this.arrangeAndEvolveCondition = arrangeAndEvolveCondition;
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
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
}
