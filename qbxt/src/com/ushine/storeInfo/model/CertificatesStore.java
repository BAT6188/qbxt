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

import org.hibernate.annotations.GenericGenerator;

/**
 * 证件库实体
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_CERTIFICATES_STORE")
public class CertificatesStore {
		
		private String id;//id
		private String certificatesNumber;//证件号码
		private InfoType infoType;//关联信息类别
		private PersonStore personStore;//所属人员
		
		/************get方法****************/
		@Id
		@GenericGenerator(name = "uId", strategy = "uuid.hex")
		@GeneratedValue(generator = "uId")
		@Column(name = "ID", length = 32)
		public String getId() {
			return id;
		}
		/**
		 * 证件号码
		 * @return
		 */
		@Column(name = "CERTIFICATES_NUMBER", length = 60)
		public String getCertificatesNumber() {
			return certificatesNumber;
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
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "PERSON_STORE_ID")
		public PersonStore getPersonStore() {
			return personStore;
		}
		/**
		 * ID
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 证件号码
		 * @param certificatesNumber
		 */
		public void setCertificatesNumber(String certificatesNumber) {
			this.certificatesNumber = certificatesNumber;
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
