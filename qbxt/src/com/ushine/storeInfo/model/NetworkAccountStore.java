package com.ushine.storeInfo.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

/**
 * 网络账号实体
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_NETWORKACCOUNT_STORE")
public class NetworkAccountStore {
		
		
		private String id;//id
		private String networkNumber;//网络号码
		private InfoType infoType;//关联信息类别
		private PersonStore personStore;//所属人员
		
		
		
		/*************get方法******************/
		@Id
		@GenericGenerator(name = "uId", strategy = "uuid.hex")
		@GeneratedValue(generator = "uId")
		@Column(name = "ID", length = 32)
		public String getId() {
			return id;
		}
		@Column(name = "NETWORK_NUMBER", length = 60)
		public String getNetworkNumber() {
			return networkNumber;
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
		 * 关联信息类别
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE})
		@JoinColumn(name = "PERSON_STORE_ID")
		public PersonStore getPersonStore() {
			return personStore;
		}
		
		
		
		
		/****************set方法**********************/
		/**
		 * id
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 网络号码
		 * @param networkNumber
		 */
		public void setNetworkNumber(String networkNumber) {
			this.networkNumber = networkNumber;
		}
		/**
		 * 关联信息类别
		 * @param infoType
		 */
		public void setInfoType(InfoType infoType) {
			this.infoType = infoType;
		}
		/**
		 * 所属人员
		 * @param personStore
		 */
		public void setPersonStore(PersonStore personStore) {
			this.personStore = personStore;
		}
		
}
