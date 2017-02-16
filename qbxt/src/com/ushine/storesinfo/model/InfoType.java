package com.ushine.storesinfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 信息类型
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_INFO_TYPE")
public class InfoType implements Serializable{
	
	
	private String id;  //id
	private String typeName;//类型名称
	private String tableTypeName;//所属库类型名称
	private String saveTime;//创建时间
	
	
	/********get方法**********/
	@Id
	@GenericGenerator(name = "uId", strategy = "uuid.hex")
	@GeneratedValue(generator = "uId")
	@Column(name = "ID", length = 32)
	public String getId() {
		return id;
	}
	/**
	 * 类型名称
	 * @return
	 */
	@Column(name = "TYPE_NAME", length = 60)
	public String getTypeName() {
		return typeName;
	}
	/**
	 * 所属库类型名称
	 * @return
	 */
	@Column(name = "TABLE_TYPE_NAME", length = 60)
	public String getTableTypeName() {
		return tableTypeName;
	}
	/**
	 * 创建时间
	 * @return
	 */
	@Column(name="SAVE_TIME",columnDefinition="TIMESTAMP")
	public String getSaveTime() {
		return saveTime;
	}
	
	
	
	/**************set方法****************/
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 类型名称
	 * @param typeName
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	/**
	 * 所属库类型名称
	 * @param tableTypeName
	 */
	public void setTableTypeName(String tableTypeName) {
		this.tableTypeName = tableTypeName;
	}
	/**
	 * 创建时间
	 * @param saveTime
	 */
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	
	@Override
	public String toString() {
		return "InfoType [id=" + id + ", typeName=" + typeName
				+ ", tableTypeName=" + tableTypeName + ", saveTime=" + saveTime
				+ "]";
	}
	

}
