package com.ushine.solr.solrbean;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 领导讲话的solr bean
 * @author Administrator
 *
 */
public class LeadSpeakStoreSolr {
	@Field
	private String leadspeakstoreId;//id
	@Field
	private String title;//标题
	@Field
	private String time;//时间
	@Field
	private String meetingName;//会议名称
	@Field
	private String secretRank;//密级
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
	@Field
	private String attaches;//附件
	@Field
	private String attachContent;//附件里面的文字内容
	
	public String getAttachContent() {
		return attachContent;
	}
	public void setAttachContent(String attachContent) {
		this.attachContent = attachContent;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMeetingName() {
		return meetingName;
	}
	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	public String getSecretRank() {
		return secretRank;
	}
	public void setSecretRank(String secretRank) {
		this.secretRank = secretRank;
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
	public String getAttaches() {
		return attaches;
	}
	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}
	
	public LeadSpeakStoreSolr() {
		
	}
	
	public LeadSpeakStoreSolr(String leadspeakstoreId, String title, String time, String meetingName, String secretRank,
			String infoType, long createDate) {
		
		this.leadspeakstoreId = leadspeakstoreId;
		this.title = title;
		this.time = time;
		this.meetingName = meetingName;
		this.secretRank = secretRank;
		this.infoType = infoType;
		this.createDate = createDate;
	}
	public String getLeadspeakstoreId() {
		return leadspeakstoreId;
	}
	public void setLeadspeakstoreId(String leadspeakstoreId) {
		this.leadspeakstoreId = leadspeakstoreId;
	}
	
	
}
