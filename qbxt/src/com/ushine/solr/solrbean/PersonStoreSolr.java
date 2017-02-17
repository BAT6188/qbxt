package com.ushine.solr.solrbean;

import org.apache.solr.client.solrj.beans.Field;

/**
 * PersonStore<br>
 * 如果以Bean的形式添加应该在每个属性添加@Field注解，并且要和schema.xml文件中的对应起来<br>
 * setter方法上添加Annotation也是可以的  
 * @author dh
 *
 */
public class PersonStoreSolr {
	/**
	 * personId的值唯一，使用数据库里的主键
	 */
	@Field
	private String personId;
	/**
	 * 姓名，精确查询
	 */
	@Field
	private String personName;
	/**
	 * 所属类别
	 */
	@Field
	private String infoType;
	

	/**
	 * 曾用名，精确查询
	 */
	@Field
	private String nameUsedBefore;
	/**
	 * 英文名，要求模糊查询
	 */
	@Field
	private String englishName;
	/**
	 * 性别，精确查询<br>
	 * 男或者女或者空
	 */
	@Field
	private String sex;
	/**
	 * 出生日期，精确查询<br>
	 * 格式统一为yyyy-MM-dd
	 */
	@Field
	private String bebornTime;
	/**
	 * 户籍地址，精确查询
	 */
	@Field
	private String registerAddress;
	/**
	 * 现住地址，精确查询
	 */
	@Field
	private String presentAddress;
	/**
	 * 工作单位，精确查询
	 */
	@Field
	private String workUnit;
	/**
	 * 履历，暂定精确查询
	 */
	@Field
	private String antecedents;
	/**
	 * 活动情况，暂定精确查询
	 */
	@Field
	private String activityCondition;
	/**
	 * 附件名称，多个附件用,隔开
	 */
	@Field
	private String appendix;
	/**
	 * 附件内容，暂定精确查询
	 */
	@Field
	private String attachContent;
	
	/**
	 * 数据创建时间
	 */
	@Field
	private long createDate;
	/**
	 * uid，人员权限依据，不分词，不索引
	 */
	@Field
	private String uid;
	/**
	 * oid，组织权限依据，不分词，不索引
	 */
	@Field
	private String oid;
	/**
	 * did，部门权限依据，不分词，不索引
	 */
	@Field
	private String did;
	@Field
	private String networkAccountStores;
	@Field
	private String certificatesStores;
	
	public PersonStoreSolr(){
		
	}
	
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getNameUsedBefore() {
		return nameUsedBefore;
	}
	public void setNameUsedBefore(String nameUsedBefore) {
		this.nameUsedBefore = nameUsedBefore;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBebornTime() {
		return bebornTime;
	}
	public void setBebornTime(String bebornTime) {
		this.bebornTime = bebornTime;
	}
	public String getRegisterAddress() {
		return registerAddress;
	}
	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	public String getPresentAddress() {
		return presentAddress;
	}
	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}
	public String getWorkUnit() {
		return workUnit;
	}
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	public String getAntecedents() {
		return antecedents;
	}
	public void setAntecedents(String antecedents) {
		this.antecedents = antecedents;
	}
	public String getActivityCondition() {
		return activityCondition;
	}
	public void setActivityCondition(String activityCondition) {
		this.activityCondition = activityCondition;
	}
	public String getAppendix() {
		return appendix;
	}
	public void setAppendix(String appendix) {
		this.appendix = appendix;
	}
	public String getAttachContent() {
		return attachContent;
	}
	public void setAttachContent(String attachContent) {
		this.attachContent = attachContent;
	}
	public String getNetworkAccountStores() {
		return networkAccountStores;
	}
	public void setNetworkAccountStores(String networkAccountStores) {
		this.networkAccountStores = networkAccountStores;
	}
	public String getCertificatesStores() {
		return certificatesStores;
	}
	public void setCertificatesStores(String certificatesStores) {
		this.certificatesStores = certificatesStores;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
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
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	/**
	 * 有id的构造函数
	 * @param personId
	 * @param personName
	 * @param infoType
	 * @param nameUsedBefore
	 * @param englishName
	 * @param sex
	 * @param bebornTime
	 * @param registerAddress
	 * @param presentAddress
	 * @param workUnit
	 * @param antecedents
	 * @param activityCondition
	 * @param appendix
	 * @param attachContent
	 * @param createDate
	 * @param uid
	 * @param oid
	 * @param did
	 * @param networkAccountStores
	 * @param certificatesStores
	 */
	public PersonStoreSolr(String personId, String personName,String infoType, String nameUsedBefore, String englishName, String sex, String bebornTime, String registerAddress, String presentAddress, String workUnit,
			String antecedents, String activityCondition, String appendix, String attachContent, long createDate, String uid, String oid, String did, String networkAccountStores,
			String certificatesStores) {
		this.personId = personId;
		this.personName = personName;
		this.nameUsedBefore = nameUsedBefore;
		this.englishName = englishName;
		this.infoType=infoType;
		this.sex = sex;
		this.bebornTime = bebornTime;
		this.registerAddress = registerAddress;
		this.presentAddress = presentAddress;
		this.workUnit = workUnit;
		this.antecedents = antecedents;
		this.activityCondition = activityCondition;
		this.appendix = appendix;
		this.attachContent = attachContent;
		this.createDate = createDate;
		this.uid = uid;
		this.oid = oid;
		this.did = did;
		this.networkAccountStores = networkAccountStores;
		this.certificatesStores = certificatesStores;
	}
	/**
	 * 没有id的构造函数
	 * @param personName
	 * @param infoType
	 * @param nameUsedBefore
	 * @param englishName
	 * @param sex
	 * @param bebornTime
	 * @param registerAddress
	 * @param presentAddress
	 * @param workUnit
	 * @param antecedents
	 * @param activityCondition
	 * @param appendix
	 * @param attachContent
	 * @param createDate
	 * @param uid
	 * @param oid
	 * @param did
	 * @param networkAccountStores
	 * @param certificatesStores
	 */
	public PersonStoreSolr(String personName,String infoType, String nameUsedBefore, String englishName, String sex, String bebornTime, String registerAddress, String presentAddress, String workUnit, String antecedents,
			String activityCondition, String appendix, String attachContent, long createDate, String uid, String oid, String did, String networkAccountStores, String certificatesStores) {
		this.personName = personName;
		this.infoType=infoType;
		this.nameUsedBefore = nameUsedBefore;
		this.englishName = englishName;
		this.sex = sex;
		this.bebornTime = bebornTime;
		this.registerAddress = registerAddress;
		this.presentAddress = presentAddress;
		this.workUnit = workUnit;
		this.antecedents = antecedents;
		this.activityCondition = activityCondition;
		this.appendix = appendix;
		this.attachContent = attachContent;
		this.createDate = createDate;
		this.uid = uid;
		this.oid = oid;
		this.did = did;
		this.networkAccountStores = networkAccountStores;
		this.certificatesStores = certificatesStores;
	}

	@Override
	public String toString() {
		return "PersonStore [personId=" + personId + ", infoType=" + infoType + ",personName=" + personName + ", nameUsedBefore=" + nameUsedBefore + ", englishName=" + englishName + ", sex=" + sex + ", bebornTime="
				+ bebornTime + ", registerAddress=" + registerAddress + ", presentAddress=" + presentAddress + ", workUnit=" + workUnit + ", createDate=" + createDate + ", uid=" + uid + ", oid="
				+ oid + ", did=" + did + ", networkAccountStores=" + networkAccountStores + ", certificatesStores=" + certificatesStores + "]";
	}
	
}
