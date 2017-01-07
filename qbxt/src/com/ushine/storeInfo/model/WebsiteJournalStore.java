package com.ushine.storeInfo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 媒体网站刊物库实体
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_WEBSITE_JOURNAL_STORE")
public class WebsiteJournalStore {
		
		
		private String id;//id
		private String name;//名称  y
		private String websiteURL;//域名y
		private String serverAddress;//服务器所在地
		private String establishAddress;//创办地
		private String mainWholesaleAddress;//主要发行地y
		private String establishPerson;//创办人y
		private String establishTime;//创建时间
		private String basicCondition;//基本情况
		private InfoType infoType;//关联信息类别
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
		
		
		/***************get方法**************************/
		@Id
		@GenericGenerator(name = "uId", strategy = "uuid.hex")
		@GeneratedValue(generator = "uId")
		@Column(name = "ID", length = 32)
		public String getId() {
			return id;
		}
		/**
		 * 名称
		 * @return
		 */
		@Column(name = "NAME", length = 60)
		public String getName() {
			return name;
		}
		/**
		 * 域名
		 * @return
		 */
		@Column(name = "WEBSITEURL", length = 60)
		public String getWebsiteURL() {
			return websiteURL;
		}
		/**
		 * 服务器所在地
		 * @return
		 */
		@Column(name = "SERVER_ADDRESS", length = 60)
		public String getServerAddress() {
			return serverAddress;
		}
		/**
		 * 创办地
		 * @return
		 */
		@Column(name = "ESTABLISH_ADDRESS", length = 60)
		public String getEstablishAddress() {
			return establishAddress;
		}
		/**
		 * 主要发行地
		 * @return
		 */
		@Column(name = "MAIN_WHOLESALE_ADDRESS", length = 1000)
		public String getMainWholesaleAddress() {
			return mainWholesaleAddress;
		}
		/**
		 * 创办人
		 * @return
		 */
		@Column(name = "ESTABLISH_PERSON", length = 60)
		public String getEstablishPerson() {
			return establishPerson;
		}
		/**
		 * 创建时间
		 * @return
		 */
		@Column(name="ESTABLISH_TIME",columnDefinition="date")
		public String getEstablishTime() {
			return establishTime;
		}
		/**
		 * 基本情况
		 * @return
		 */
		@Column(name = "BASIC_CONDITION", length = 20000)
		public String getBasicCondition() {
			return basicCondition;
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
		
		
		/**************set方法****************************/
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 名称
		 * @param name
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * 域名
		 * @param websiteURL
		 */
		public void setWebsiteURL(String websiteURL) {
			this.websiteURL = websiteURL;
		}
		/**
		 * 服务器所在地
		 * @param serverAddress
		 */
		public void setServerAddress(String serverAddress) {
			this.serverAddress = serverAddress;
		}
		/**
		 * 创办地
		 * @param establishAddress
		 */
		public void setEstablishAddress(String establishAddress) {
			this.establishAddress = establishAddress;
		}
		/**
		 * 主要发行地
		 * @param mainWholesaleAddress
		 */
		public void setMainWholesaleAddress(String mainWholesaleAddress) {
			this.mainWholesaleAddress = mainWholesaleAddress;
		}
		/**
		 * 创办人
		 * @param establishPerson
		 */
		public void setEstablishPerson(String establishPerson) {
			this.establishPerson = establishPerson;
		}
		/**
		 * 创建时间
		 * @param establishTime
		 */
		public void setEstablishTime(String establishTime) {
			this.establishTime = establishTime;
		}
		/**
		 * 基本情况
		 * @param basicCondition
		 */
		public void setBasicCondition(String basicCondition) {
			this.basicCondition = basicCondition;
		}
		/**
		 * 关联信息类别
		 * @param infoType
		 */
		public void setInfoType(InfoType infoType) {
			this.infoType = infoType;
		}
		
		@Override
		public String toString() {
			return "WebsiteJournalStore [id=" + id + ", name=" + name
					+ ", websiteURL=" + websiteURL + ", serverAddress="
					+ serverAddress + ", establishAddress=" + establishAddress
					+ ", mainWholesaleAddress=" + mainWholesaleAddress
					+ ", establishPerson=" + establishPerson + ", establishTime="
					+ establishTime + ", basicCondition=" + basicCondition
					+ ", infoType=" + infoType + ", uid=" + uid + ", oid=" + oid
					+ ", did=" + did + "]";
		}
}
