package com.ushine.storesinfo.model;

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
 * 外来文档库实体
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_OUTSIDEDOC_STORE")
public class OutsideDocStore {
		
		private String sourceUnit;//来源单位
		private String secretRank;//密级
		//private String attachContent;//附件里面的文字内容
		//private String title;//标题
	
		private String id;//id
		private String name;//文档名称
		private String time;//时间
		private String docNumber;// 文档编号
		private String centent;//内容
		private InfoType infoType;//关联信息类别
		private String uid; // 创建人
		private String oid; // 组织
		private String did; // 部门
		private String action;//增量数据操作   1:新增，2:修改，3:删除
		private String createDate;//当前数据创建时间
		private InfoType involvedInTheField;//涉及领域
		private String attaches;//附件名称
		private String fileName;//文件名称，用于判断是否入库了
		
		@Column(name = "DOC_NUMBER", length = 60)
		public String getDocNumber() {
			return docNumber;
		}
		public void setDocNumber(String docNumber) {
			this.docNumber = docNumber;
		}
		@Column(name = "FILE_NAME", length = 256)
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		/**
		 * 附件内容
		 * @return
		 */
		/*@Column(name="ATTACHCONTENT",columnDefinition="LONGTEXT")
		public String getAttachContent() {
			return attachContent;
		}*/
		/**
		 * 附件内容
		 * @param attachContent
		 */
		/*public void setAttachContent(String attachContent) {
			this.attachContent = attachContent;
		}*/

		
		@Column(name = "ATTACHES", length = 20000)
		public String getAttaches() {
			return attaches;
		}
		public void setAttaches(String attaches) {
			this.attaches = attaches;
		}
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "INVOLVED_IN_THE_FIELD")
		public InfoType getInvolvedInTheField() {
			return involvedInTheField;
		}
		public void setInvolvedInTheField(InfoType involvedInTheField) {
			this.involvedInTheField = involvedInTheField;
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
		
		
		
		
		/**************get方法***********************/
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
		@Column(name = "name", length = 60)
		public String getName() {
			return name;
		}
		/**
		 * 来源单位
		 * @return
		 */
		@Column(name = "SOURCE_UNIT", length = 60)
		public String getSourceUnit() {
			return sourceUnit;
		}
		/**
		 * 标题
		 * @return
		 */
		/*@Column(name = "TITLE", length = 60)
		public String getTitle() {
			return title;
		}*/
		/**
		 * 时间
		 * @return
		 */
		@Column(name="TIME",columnDefinition="date")
		public String getTime() {
			return time;
		}
		/**
		 * 密级
		 * @return
		 */
		@Column(name = "SECRET_RANK", length = 60)
		public String getSecretRank() {
			return secretRank;
		}
		/**
		 * 内容
		 * @return
		 */
		@Column(name = "CENTENT", length = 20000)
		public String getCentent() {
			return centent;
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
		
		
		
		/****************set方法********************/
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
		 * 来源单位
		 * @param sourceUnit
		 */
		public void setSourceUnit(String sourceUnit) {
			this.sourceUnit = sourceUnit;
		}
		/**
		 * 标题
		 * @param title
		 */
		/*public void setTitle(String title) {
			this.title = title;
		}*/
		/**
		 * 时间
		 * @param time
		 */
		public void setTime(String time) {
			this.time = time;
		}
		/**
		 * 密级
		 * @param secretRank
		 */
		public void setSecretRank(String secretRank) {
			this.secretRank = secretRank;
		}
		/**
		 * 内容
		 * @param centent
		 */
		public void setCentent(String centent) {
			this.centent = centent;
		}
		/**
		 * 关联信息类别
		 * @param infoType
		 */
		public void setInfoType(InfoType infoType) {
			this.infoType = infoType;
		}
		
		
}
