package com.ushine.storesinfo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 导入任务对象domain
 * @author Administrator
 *
 */
@Entity
@Table(name = "T_IMPORTTASKDOMAIN")
public class ImportTaskDomain implements Serializable{
	private String id;
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
	 * 任务执行状态
	 * 0:代表执行中
	 * 1:执行成功
	 * -1:执行异常
	 */
	@Column(name = "TASK_STATUS", length = 2)
	public Integer getStatus() {
		return status;
	}
	/**
	 * 任务执行状态
	 * 0:代表执行中
	 * 1:执行成功
	 * -1:执行异常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 任务名称
	 */
	@Column(name = "TASK_NAME", length = 256)
	public String getTaskName() {
		return taskName;
	}
	/**
	 * 任务名称
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	/**
	 * 任务执行成功后详细信息
	 */
	@Column(name = "TASK_RESULTDETAIL", length = 20000)
	public String getTaskResultDetail() {
		return taskResultDetail;
	}
	/**
	 * 任务执行成功后详细信息
	 */
	public void setTaskResultDetail(String taskResultDetail) {
		this.taskResultDetail = taskResultDetail;
	}
	/**
	 * 任务执行状态
	 * 0:代表执行中
	 * 1:执行成功
	 * -1:执行异常
	 */
	private Integer status;
	/**
	 * 任务名称
	 */
	private String taskName;
	/**
	 * 任务执行成功后详细信息
	 */
	private String taskResultDetail;
}
