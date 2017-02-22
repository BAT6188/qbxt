package com.ushine.solr.vo;
/**
 * 领导讲话vo模型
 * @author Administrator
 *
 */
public class LeadSpeakStoreVo {
	
	public LeadSpeakStoreVo() {
		
	}

	@Override
	public String toString() {
		return "LeadSpeakStoreVo [id=" + id + ", title=" + title + ", time=" + time + ", meetingName=" + meetingName
				+ ", secretRank=" + secretRank + ", infoType=" + infoType + ", createDate=" + createDate + "]";
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	private String id;//id
	private String title;//标题
	private String time;//时间
	private String meetingName;//会议名称
	private String secretRank;//密级
	private String infoType;//关联信息类别
	private String createDate;//当前数据创建时间
}
