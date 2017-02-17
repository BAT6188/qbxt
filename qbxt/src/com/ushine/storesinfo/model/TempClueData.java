package com.ushine.storesinfo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 线索涉及对象临时数据
 * @author wangbailin
 *
 */
@Entity
@Table(name="TEMP_CLUE_DATA")
public class TempClueData {
	@Override
	public String toString() {
		return "TempClueData [id=" + id + ", dataId=" + dataId + ", name="
				+ name + ", type=" + type + ", state=" + state + ", action="
				+ action + "]";
	}
	private String id;   //id
	private String dataId; //所关联对象数据的id，如果没有关联为空
	private String name;   //数据的名称
	private String type;   //数据的类型，人员库数据:personStore,组织库数据:organizStore,媒体库数据:websiteJournalStore
	private String state;  //状态,0:没有关联到其他数据表的数据，1:已关联到其他数据表的数据
	private String action; //标识，将从页面参数六位随机数来表示数据属于正在操作的人员
	@Column(name = "ACTION", length = 60)
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@Id
	@GenericGenerator(name = "uId", strategy = "uuid.hex")
	@GeneratedValue(generator = "uId")
	@Column(name = "ID", length = 32)
	public String getId() {
		return id;
	}
	@Column(name = "DATA_ID", length = 60)
	public String getDataId() {
		return dataId;
	}
	@Column(name = "NAME", length = 20000)
	public String getName() {
		return name;
	}
	@Column(name = "TYPE", length = 20)
	public String getType() {
		return type;
	}
	@Column(name = "STATE", length = 2)
	public String getState() {
		return state;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setState(String state) {
		this.state = state;
	}
}
