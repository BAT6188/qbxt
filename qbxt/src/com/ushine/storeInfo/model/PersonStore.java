package com.ushine.storeInfo.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 人员库
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_PERSON_STORE")
public class PersonStore implements Serializable{
		
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String id;//id
		private String personName;//人员名称
		private String nameUsedBefore;//曾用名
		private String englishName;//英文名
		private String photofraphWay;//照片路径
		private String sex;//性别
		private String bebornTime;//出生日期
		private String registerAddress;//户籍地址
		private String presentAddress;//现住地址 
		private String workUnit;//工作单位
		private String antecedents;//履历
		private String appendix;//附件
		private String activityCondition;//主要活动情况
		private InfoType infoType;//关联信息类别
		private Set<CertificatesStore> certificatesStores;//人员证件
		private Set<NetworkAccountStore> networkAccountStores;//人员网络账号
		private String uid; // 创建人
		private String oid; // 组织
		private String did; // 部门
		private String action;//增量数据操作   1:新增，2:修改，3:删除
		private String isEnable = "1";//是否启用      1：否2：是，默认1
		private String createDate;//当前数据创建时间
		private String isToStorage = "1";//是否入库1：已入库，2:未入库
		
		/**
		 * 是否入库
		 * @return
		 */
		@Column(name = "IS_TO_STOREAGE", length = 2)
		public String getIsToStorage() {
			return isToStorage;
		}
		/**
		 * 是否入库
		 * @param isToStorage
		 */
		public void setIsToStorage(String isToStorage) {
			this.isToStorage = isToStorage;
		}
		/**
		 * 当前数据创建时间
		 * @return
		 */
		@Column(name="CREATE_DATE",columnDefinition="TIMESTAMP")
		public String getCreateDate() {
			return createDate;
		}
		/**
		 * 当前数据创建时间
		 * @param createDate
		 */
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		@Column(name = "IS_ENABLE", length = 2)
		public String getIsEnable() {
			return isEnable;
		}

		public void setIsEnable(String isEnable) {
			this.isEnable = isEnable;
		}

		@Column(name = "ACTION", length = 2)
		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		/**
		 * 创建人
		 * @return the uid 上午10:13:42 
		 */
		@Column(name = "UID", length = 60)
		public String getUid() {
			return uid;
		}

		/**
		 * 创建人
		 * @param uid
		 *            the uid to set 上午10:13:42 
		 */
		public void setUid(String uid) {
			this.uid = uid;
		}

		/**
		 *  组织
		 * @return the oid 上午10:13:42
		 */
		@Column(name = "OID", length = 60)
		public String getOid() {
			return oid;
		}

		/**
		 * 组织
		 * 
		 * @param oid
		 *            the oid to set 上午10:13:42
		 */
		public void setOid(String oid) {
			this.oid = oid;
		}

		/**
		 *  部门
		 * @return the did 上午10:13:42
		 * 
		 */
		@Column(name = "DID", length = 60)
		public String getDid() {
			return did;
		}

		/**
		 * 部门
		 * 
		 * @param did
		 *            the did to set 上午10:13:42
		 */
		public void setDid(String did) {
			this.did = did;
		}
		
		
		
	

		/***************get方法*****************/
		/**
		 * id
		 * @return
		 */
		@Id
		@GenericGenerator(name = "uId", strategy = "uuid.hex")
		@GeneratedValue(generator = "uId")
		@Column(name = "ID", length = 32)
		public String getId() {
			return id;
		}
		/**
		 * 人员名称
		 * @return
		 */
		@Column(name = "PERSON_NAME", length = 60)
		public String getPersonName() {
			return personName;
		}
		/**
		 * 曾用名
		 * @return
		 */
		@Column(name = "NAME_USED_BEFORE", length = 60)
		public String getNameUsedBefore() {
			return nameUsedBefore;
		}
		/**
		 * 英文名
		 * @return
		 */
		@Column(name = "ENGLISH_NAME", length = 60)
		public String getEnglishName() {
			return englishName;
		}
		/**
		 * 照片路径
		 * @return
		 */
		@Column(name = "PHOTOFRAPH_WAY", length = 20000)
		public String getPhotofraphWay() {
			return photofraphWay;
		}
		/**
		 * 性别
		 * @return
		 */
		@Column(name = "SEX", length = 60)
		public String getSex() {
			return sex;
		}
		/**
		 * 出生日期
		 * @return
		 */
		@Column(name="BEBORN_TIME",columnDefinition="TIMESTAMP")
		public String getBebornTime() {
			return bebornTime;
		}
		/**
		 * 户籍地址
		 * @return
		 */
		@Column(name = "REGISTER_ADDRESS", length = 100)
		public String getRegisterAddress() {
			return registerAddress;
		}
		/**
		 * 现住地址
		 * @return
		 */
		@Column(name = "PRESENT_ADDRESS", length = 60)
		public String getPresentAddress() {
			return presentAddress;
		}
		/**
		 * 工作单位
		 * @return
		 */
		@Column(name = "WORK_UNIT", length = 60)
		public String getWorkUnit() {
			return workUnit;
		}
		/**
		 * 履历
		 * @return
		 */
		@Column(name = "ANTECEDENTS", length = 1000)
		public String getAntecedents() {
			return antecedents;
		}
		/**
		 * 附件
		 * @return
		 */
		@Column(name = "APPENDIX", length = 20000)
		public String getAppendix() {
			return appendix;
		}
		/**
		 * 主要活动情况
		 * @return
		 */
		@Column(name = "ACTIVITY_CONDITION", length = 20000)
		public String getActivityCondition() {
			return activityCondition;
		}
		/**
		 * 关联信息类别
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "INFOTYPEID")
		public InfoType getInfoType() {
			return infoType;
		}
		/**
		 * 证件集合
		 * fetch = FetchType.EAGER
		 * @return
		 */
		@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "personStore")
		public Set<CertificatesStore> getCertificatesStores() {
			return certificatesStores;
		}
		/**
		 * 网络账号集合
		 * @return
		 */
		@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "personStore")
		public Set<NetworkAccountStore> getNetworkAccountStores() {
			return networkAccountStores;
		}
		
		
		
		
		/*****************set方法*****************************/
		/**
		 * id
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 人员名称
		 * @param personName
		 */
		public void setPersonName(String personName) {
			this.personName = personName;
		}
		/**
		 * 曾用名
		 * @param nameUsedBefore
		 */
		public void setNameUsedBefore(String nameUsedBefore) {
			this.nameUsedBefore = nameUsedBefore;
		}
		/**
		 * 英文名
		 * @param englishName
		 */
		public void setEnglishName(String englishName) {
			this.englishName = englishName;
		}
		/**
		 * 照片路径
		 * @param photofraphWay
		 */
		public void setPhotofraphWay(String photofraphWay) {
			this.photofraphWay = photofraphWay;
		}
		/**
		 * 性别
		 * @param sex
		 */
		public void setSex(String sex) {
			this.sex = sex;
		}
		/**
		 * 出生日期
		 * @param bebornTime
		 */
		public void setBebornTime(String bebornTime) {
			this.bebornTime = bebornTime;
		}
		/**
		 * 户籍地址
		 * @param registerAddress
		 */
		public void setRegisterAddress(String registerAddress) {
			this.registerAddress = registerAddress;
		}
		/**
		 * 现住地址
		 * @param presentAddress
		 */
		public void setPresentAddress(String presentAddress) {
			this.presentAddress = presentAddress;
		}
		/**
		 * 工作单位
		 * @param workUnit
		 */
		public void setWorkUnit(String workUnit) {
			this.workUnit = workUnit;
		}
		/**
		 * 履历
		 * @param antecedents
		 */
		public void setAntecedents(String antecedents) {
			this.antecedents = antecedents;
		}
		/**
		 * 附件
		 * @param appendix
		 */
		public void setAppendix(String appendix) {
			this.appendix = appendix;
		}
		/**
		 * 主要活动情况
		 * @param activityCondition
		 */
		public void setActivityCondition(String activityCondition) {
			this.activityCondition = activityCondition;
		}
		/**
		 * 人员类别
		 * @param infoType
		 */
		public void setInfoType(InfoType infoType) {
			this.infoType = infoType;
		}
		/**
		 * 人员证件
		 * @param certificatesStores
		 */
		public void setCertificatesStores(Set<CertificatesStore> certificatesStores) {
			this.certificatesStores = certificatesStores;
		}
		/**
		 * 人员网络账号
		 * @param networkAccountStores
		 */
		public void setNetworkAccountStores(
				Set<NetworkAccountStore> networkAccountStores) {
			this.networkAccountStores = networkAccountStores;
		}
		@Override
		public String toString() {
			return "PersonStore [id=" + id + ", personName=" + personName
					+ ", nameUsedBefore=" + nameUsedBefore + ", englishName="
					+ englishName + ", photofraphWay=" + photofraphWay
					+ ", sex=" + sex + ", bebornTime=" + bebornTime
					+ ", registerAddress=" + registerAddress
					+ ", presentAddress=" + presentAddress + ", workUnit="
					+ workUnit + ", antecedents=" + antecedents + ", appendix="
					+ appendix + ", activityCondition=" + activityCondition
					+ ", infoType=" + infoType + ", certificatesStores="
					+ certificatesStores + ", networkAccountStores="
					+ networkAccountStores + ", uid=" + uid + ", oid=" + oid
					+ ", did=" + did + "]";
		}
}
