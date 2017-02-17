package com.ushine.solr.vo;
/**
 * PersonStoreVo对象
 * @author dh
 *
 */
public class PersonStoreVo {
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	private String id;
	private String personName;
	private String nameUsedBefore;
	private String englishName;
	private String sex;
	private String bebornTime;
	private String presentAddress;
	private String workUnit;
	private String createDate;
	private String registerAddress;
	
	public PersonStoreVo(){
		
	}
	public PersonStoreVo(String id, String personName, String nameUsedBefore, String englishName, String sex, String bebornTime, String presentAddress, String registerAddress,String workUnit,String createDate) {
		super();
		this.id = id;
		this.personName = personName;
		this.nameUsedBefore = nameUsedBefore;
		this.englishName = englishName;
		this.sex = sex;
		this.bebornTime = bebornTime;
		this.presentAddress = presentAddress;
		this.workUnit = workUnit;
		this.createDate=createDate;
		this.registerAddress=registerAddress;
	}
	public String getRegisterAddress() {
		return registerAddress;
	}
	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "PersonStoreVo [id=" + id + ", personName=" + personName + ", nameUsedBefore=" + nameUsedBefore + ", englishName=" + englishName + ", sex=" + sex + ", bebornTime=" + bebornTime
				+ ", presentAddress=" + presentAddress + ", workUnit=" + workUnit + "]";
	}
	
}
