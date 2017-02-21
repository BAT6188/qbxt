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
 * 业务文档库实体
 * 
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_VOCATIONALWORK_STORE")
public class VocationalWorkStore {

	@Override
	public String toString() {
		return "VocationalWorkStore [id=" + id + ", docName=" + docName + ", docNumber=" + docNumber + ", time=" + time
				+ ", infoType=" + infoType + ", uid=" + uid + ", oid=" + oid + ", did=" + did + ", action=" + action
				+ ", createDate=" + createDate + "]";
	}

	private String id;// id
	private String docName;// 文档名称
	private String docNumber;// 文档编号
	private String time;// 时间
	private String theOriginal;// 原文
	private InfoType infoType;// 关联信息类别
	private String uid; // 创建人
	private String oid; // 组织
	private String did; // 部门
	private String action;// 增量数据操作 1:新增，2:修改，3:删除
	private String createDate;// 当前数据创建时间
	//private InfoType involvedInTheField;// 涉及领域
	private String attaches;//附件路径
	private String fileName;//文件名称，用于判断是否入库了
	
	public VocationalWorkStore() {
		
	}
	
	public VocationalWorkStore(String id, String docName, String docNumber, String time, InfoType infoType, String uid,
			String oid, String did, String createDate) {
		
		init(id, docName, docNumber, time, infoType, createDate);
		this.uid = uid;
		this.oid = oid;
		this.did = did;
	}
	
	public VocationalWorkStore(String id, String docName, String docNumber, String time, InfoType infoType,
			String createDate) {
		
		init(id, docName, docNumber, time, infoType, createDate);
	}
	
	private void init(String id, String docName, String docNumber, String time, InfoType infoType,
			String createDate) {
		this.id = id;
		this.docName = docName;
		this.docNumber = docNumber;
		this.time = time;
		this.infoType = infoType;
		this.createDate = createDate;
	}

	/**
	 * 文件名称，用于判断是否入库了
	 * @return
	 */
	@Column(name="FILENAME",length = 256)
	public String getFileName() {
		return fileName;
	}
	/**
	 * 文件名称，用于判断是否入库了
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "ATTACHES", length = 2000)
	public String getAttaches() {
		return attaches;
	}

	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}

	/**
	 * 当前数据创建时间
	 * 
	 * @return
	 */
	@Column(name = "CREATE_DATE", columnDefinition = "TIMESTAMP")
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * 当前数据创建时间
	 * 
	 * @param createDate
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/*@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "INVOLVED_IN_THE_FIELD")
	public InfoType getInvolvedInTheField() {
		return involvedInTheField;
	}

	public void setInvolvedInTheField(InfoType involvedInTheField) {
		this.involvedInTheField = involvedInTheField;
	}*/

	@Column(name = "ACTION", length = 2)
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 创建人
	 * 
	 * @return the uid 上午10:13:42
	 */
	@Column(name = "UID", length = 60)
	public String getUid() {
		return uid;
	}

	/**
	 * 创建人
	 * 
	 * @param uid
	 *            the uid to set 上午10:13:42
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * 组织
	 * 
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
	 * 部门
	 * 
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

	/************* get方法 **********************/
	@Id
	@GenericGenerator(name = "uId", strategy = "uuid.hex")
	@GeneratedValue(generator = "uId")
	@Column(name = "ID", length = 32)
	public String getId() {
		return id;
	}

	/**
	 * 文档名称
	 * 
	 * @return
	 */
	@Column(name = "DOC_NAME", length = 60)
	public String getDocName() {
		return docName;
	}

	/**
	 * 文档号
	 * 
	 * @return
	 */
	@Column(name = "DOC_NUMBER", length = 60)
	public String getDocNumber() {
		return docNumber;
	}

	/**
	 * 创建时间 不能定义为timedamp,应该为date类型
	 * 
	 * @return
	 */
	@Column(name = "TIME", columnDefinition = "date")
	public String getTime() {
		return time;
	}

	/**
	 * 原文
	 * 
	 * @return
	 */
	@Column(name = "THE_ORIGINAL", length = 20000)
	public String getTheOriginal() {
		return theOriginal;
	}

	/**
	 * 关联信息类别
	 * 
	 * @return
	 */
	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "INFOTYPEID")
	public InfoType getInfoType() {
		return infoType;
	}

	/**************** set方法 **********************/
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 刊物名称
	 * 
	 * @param docName
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}

	/**
	 * 刊物号
	 * 
	 * @param docNumber
	 */
	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	/**
	 * 刊物号
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * 原文
	 * 
	 * @param theOriginal
	 */
	public void setTheOriginal(String theOriginal) {
		this.theOriginal = theOriginal;
	}

	/**
	 * 关联信息类别
	 * 
	 * @param infoType
	 */
	public void setInfoType(InfoType infoType) {
		this.infoType = infoType;
	}
}
