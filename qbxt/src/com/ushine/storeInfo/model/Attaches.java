package com.ushine.storeInfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 附件库
 * @author Administrator
 *
 */
@Entity
@Table(name = "T_ATTACHES")
public class Attaches implements Serializable{
	//主键
	private String id;
	//文档库名称
	private String storeType;
	//对应的store的id
	private String storeId;
	//附件名
	private String attachName;
	//附件里面的文字内容
	private String attachContent;
	
	@Id
	@GenericGenerator(name = "uId", strategy = "uuid.hex")
	@GeneratedValue(generator = "uId")
	@Column(name = "ID", length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 对应的库名称
	 * @return
	 */
	@Column(name = "STORETYPE", length = 32)
	public String getStoreType() {
		return storeType;
	}
	/**
	 * 对应的库名称
	 * @param storeType
	 */
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	/**
	 * 附件对应的store的ID
	 * @return
	 */
	@Column(name = "STOREID", length = 32)
	public String getStoreId() {
		return storeId;
	}
	/**
	 * 附件对应的store的ID
	 * @param storeId
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	/**
	 * 附件名称
	 * @return
	 */
	@Column(name = "ATTACHNAME", length = 2000)
	public String getAttachName() {
		return attachName;
	}
	/**
	 * 附件名称
	 * @param attachName
	 */
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	/**
	 * 附件内容
	 * @return
	 */
	@Column(name="ATTACHCONTENT",columnDefinition="LONGTEXT")
	public String getAttachContent() {
		return attachContent;
	}
	/**
	 * 附件内容
	 * @param attachContent
	 */
	public void setAttachContent(String attachContent) {
		this.attachContent = attachContent;
	}
}
