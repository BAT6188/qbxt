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
 * 线索库
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_CLUE_STORE")
public class ClueStore {
		
		private String id;//id
		private String clueName;//线索名称
		private String clueSource;//线索来源
		private String findTime;//发现时间
		private String clueContent;//线索内容
		private String arrangeAndEvolveCondition;//工作部署及进展情况
		private String involvingObjName;   //涉及对象名称
		private String uid; // 创建人
		private String oid; // 组织
		private String did; // 部门
		private String action = "1";//增量数据操作   1:新增，2:修改，3:删除
		private String isEnable = "1";//是否启用      1：否2：是，默认1
		private String isInvolvedObj = "否";//是否涉及对象
		private String createDate;//当前数据创建时间
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
		/**
		 * 是否涉及对象
		 * @return
		 */
		@Column(name = "IS_INVOLVED_OBJ", length = 2)
		public String getIsInvolvedObj() {
			return isInvolvedObj;
		}
		/**
		 * 是否涉及对象
		 * @param isInvolvedObj
		 */
		public void setIsInvolvedObj(String isInvolvedObj) {
			this.isInvolvedObj = isInvolvedObj;
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
		 * 涉及对象名称
		 * @return
		 */
		@Column(name = "INVOLVING_OBJ_NAME", length = 20000)
		public String getInvolvingObjName() {
			return involvingObjName;
		}
		/**
		 * 涉及对象名称
		 * @param involvingObjName
		 */
		public void setInvolvingObjName(String involvingObjName) {
			this.involvingObjName = involvingObjName;
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
		
		
		/************get方法*****************/
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
		 * 线索名称
		 * @return
		 */
		@Column(name = "CLUE_NAME", length = 60)
		public String getClueName() {
			return clueName;
		}
		/**
		 * 线索来源
		 * @return
		 */
		@Column(name = "CLUE_ROURCE", length = 60)
		public String getClueSource() {
			return clueSource;
		}
		/**
		 * 发现时间
		 * @return
		 */
		@Column(name="FIND_TIME",columnDefinition="TIMESTAMP")
		public String getFindTime() {
			return findTime;
		}
		/**
		 * 线索内容
		 * @return
		 */
		@Column(name = "CLUE_CONTENT", length = 20000)
		public String getClueContent() {
			return clueContent;
		}
		/**
		 * 工作部署及进展情况
		 * @return
		 */
		@Column(name = "ARRANGE_AND_EVOLUE_CONDITION", length = 20000)
		public String getArrangeAndEvolveCondition() {
			return arrangeAndEvolveCondition;
		}
		
		
		
		
		/******************set方法*********************/
		/**
		 * id
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 线索名称
		 * @param clueName
		 */
		public void setClueName(String clueName) {
			this.clueName = clueName;
		}
		/**
		 * 线索来源
		 * @param clueSource
		 */
		public void setClueSource(String clueSource) {
			this.clueSource = clueSource;
		}
		/**
		 * 发现时间
		 * @param findTime
		 */
		public void setFindTime(String findTime) {
			this.findTime = findTime;
		}
		/**
		 * 线索内容
		 * @param clueContent
		 */
		public void setClueContent(String clueContent) {
			this.clueContent = clueContent;
		}
		/**
		 * 工作部署及进展情况
		 * @param arrangeAndEvolveCondition
		 */
		public void setArrangeAndEvolveCondition(String arrangeAndEvolveCondition) {
			this.arrangeAndEvolveCondition = arrangeAndEvolveCondition;
		}
		
}
