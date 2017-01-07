package com.ushine.storeInfo.model;

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
 * 组织库实体
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_ORGANIZ_STORE")
public class OrganizStore {
			
		private String id;//id
		private String organizName;//组织名称
		private String foundTime;//成立时间
		private String degreeOfLatitude;//活动范围
		private String websiteURL;//网址
		private String basicCondition;//基本情况
		private String activityCondition;//主要活动情况
		private String orgHeadOfName;//组织负责人名称
		private PersonStore personStore;//组织负责人
		private InfoType infoType;//关联信息类别
		private String organizBranchesNames;//下属组织名称
		private Set<OrganizBranches> organizBranches;//下属组织
		private String organizPersonNames;//下属组织成员名称
		private Set<OrganizPerson> organizPersons;//下属成员
		private String organizPublicActionNames;//下属组织刊物名称
		private Set<OrganizPublicAction> organizPublicActions;//下属刊物
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
		
		
		
		
		/********************get方法**************************************/
		@Id
		@GenericGenerator(name = "uId", strategy = "uuid.hex")
		@GeneratedValue(generator = "uId")
		@Column(name = "ID", length = 32)
		public String getId() {
			return id;
		}
		/**
		 * 组织名称
		 * @return
		 */
		@Column(name = "ORGANIZ_NAME", length = 60)
		public String getOrganizName() {
			return organizName;
		}
		/**
		 * 成立时间
		 * @return
		 */
		@Column(name="FOUND_TIME",columnDefinition="date")
		public String getFoundTime() {
			return foundTime;
		}
		/**
		 * 活动范围
		 * @return
		 */
		@Column(name = "DEGREE_OF_LATITUDE", length = 20000)
		public String getDegreeOfLatitude() {
			return degreeOfLatitude;
		}
		/**
		 * 网址
		 * @return
		 */
		@Column(name = "WEBSITE_URL", length = 100)
		public String getWebsiteURL() {
			return websiteURL;
		}
		/**
		 * 基础情况
		 * @return
		 */
		@Column(name = "BASIC_CONDITION", length = 20000)
		public String getBasicCondition() {
			return basicCondition;
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
		 * 组织负责人名称
		 * @return
		 */
		@Column(name = "ORG_HEADOF_NAME", length = 20000)
		public String getOrgHeadOfName() {
			return orgHeadOfName;
		}
		/**
		 * 组织负责人
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "PERSON_STORE_ID")
		public PersonStore getPersonStore() {
			return personStore;
		}
		/**
		 * 关联信息类别
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "INFOTYPE_ID")
		public InfoType getInfoType() {
			return infoType;
		}
		/**
		 * 组织分支机构名称
		 * @return
		 */
		@Column(name = "ORGANIZ_BRANCHES_NAMES", length = 20000)
		public String getOrganizBranchesNames() {
			return organizBranchesNames;
		}
		/**
		 * 组织分支机构
		 * @return
		 */
		@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "organizStore")
		public Set<OrganizBranches> getOrganizBranches() {
			return organizBranches;
		}
		/**
		 * 组织成员名称
		 * @return
		 */
		@Column(name = "ORGANIZ_PERSON_NAMES", length = 20000)
		public String getOrganizPersonNames() {
			return organizPersonNames;
		}
		/**
		 * 下属成员
		 * @return
		 */
		@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "organizStore")
		public Set<OrganizPerson> getOrganizPersons() {
			return organizPersons;
		}
		

		/**
		 * 组织所属刊物名称
		 * @return
		 */
		@Column(name = "ORGANIZ_PUBLIC_ACTION_NAMES", length = 20000)
		public String getOrganizPublicActionNames() {
			return organizPublicActionNames;
		}
		/**
		 * 组织所属刊物
		 * @return
		 */
		@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "organizStore")
		public Set<OrganizPublicAction> getOrganizPublicActions() {
			return organizPublicActions;
		}
		
		
		
		/***********************set方法******************************
		/**
		 * id
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 组织名称
		 * @param organizName
		 */
		public void setOrganizName(String organizName) {
			this.organizName = organizName;
		}
		/**
		 * 成立时间
		 * @param foundTime
		 */
		public void setFoundTime(String foundTime) {
			this.foundTime = foundTime;
		}
		/**
		 * 活动范围
		 * @param degreeOfLatitude
		 */
		public void setDegreeOfLatitude(String degreeOfLatitude) {
			this.degreeOfLatitude = degreeOfLatitude;
		}
		/**
		 * 网址
		 * @param websiteURL
		 */
		public void setWebsiteURL(String websiteURL) {
			this.websiteURL = websiteURL;
		}
		/**
		 * 基本情况
		 * @param basicCondition
		 */
		public void setBasicCondition(String basicCondition) {
			this.basicCondition = basicCondition;
		}
		/**
		 * 主要活动情况
		 * @param activityCondition
		 */
		public void setActivityCondition(String activityCondition) {
			this.activityCondition = activityCondition;
		}
		/**
		 * 组织负责人
		 * @param orgHeadOfName
		 */
		public void setOrgHeadOfName(String orgHeadOfName) {
			this.orgHeadOfName = orgHeadOfName;
		}
		/**
		 * 关联，组织负责人
		 * @param personStore
		 */
		public void setPersonStore(PersonStore personStore) {
			this.personStore = personStore;
		}
		/**
		 * 关联信息类别
		 * @param infoType
		 */
		public void setInfoType(InfoType infoType) {
			this.infoType = infoType;
		}
		/**
		 * 下属组织分支机构名称
		 * @param organizBranchesNames
		 */
		public void setOrganizBranchesNames(String organizBranchesNames) {
			this.organizBranchesNames = organizBranchesNames;
		}
		/**
		 * 下属组织分支机构集合
		 * @param organizBranches
		 */
		public void setOrganizBranches(Set<OrganizBranches> organizBranches) {
			this.organizBranches = organizBranches;
		}
		/**
		 * 下属成员名称
		 * @param organizPersonNames
		 */
		public void setOrganizPersonNames(String organizPersonNames) {
			this.organizPersonNames = organizPersonNames;
		}
		/**
		 * 下属成员集合
		 * @param organizPersons
		 */
		public void setOrganizPersons(Set<OrganizPerson> organizPersons) {
			this.organizPersons = organizPersons;
		}
		/**
		 * 下属媒体刊物名称
		 * @param organizPublicActionNames
		 */
		public void setOrganizPublicActionNames(String organizPublicActionNames) {
			this.organizPublicActionNames = organizPublicActionNames;
		}
		/**
		 * 下属媒体刊物集合
		 * @param organizPublicActions
		 */
		public void setOrganizPublicActions(
				Set<OrganizPublicAction> organizPublicActions) {
			this.organizPublicActions = organizPublicActions;
		}
		
		
		@Override
		public String toString() {
			return "OrganizStore [id=" + id + ", organizName=" + organizName
					+ ", foundTime=" + foundTime + ", degreeOfLatitude="
					+ degreeOfLatitude + ", websiteURL=" + websiteURL
					+ ", basicCondition=" + basicCondition
					+ ", activityCondition=" + activityCondition
					+ ", orgHeadOfName=" + orgHeadOfName + ", personStore="
					+ personStore + ", infoType=" + infoType
					+ ", organizBranchesNames=" + organizBranchesNames
					+ ", organizBranches=" + organizBranches
					+ ", organizPersonNames=" + organizPersonNames
					+ ", organizPersons=" + organizPersons
					+ ", organizPublicActionNames=" + organizPublicActionNames
					+ ", organizPublicActions=" + organizPublicActions
					+ ", uid=" + uid + ", oid=" + oid + ", did=" + did + "]";
		}
}
