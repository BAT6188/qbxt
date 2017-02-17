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
 * 组织成员
 * 这种体现在数据库里是多对多的关系
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_ORGANIZ_PERSON")
public class OrganizPerson {
			
		private String id;//id
		private OrganizStore organizStore;//组织
		private PersonStore personStore;//成员，关联人员基础库信息
		
		/***************get方法*******************/
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
		 * 组织
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "ORGANIZ_STORE_ID")
		public OrganizStore getOrganizStore() {
			return organizStore;
		}
		/**
		 * 成员，关联人员基础库信息
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "PERSON_STORE_ID")
		public PersonStore getPersonStore() {
			return personStore;
		}
		
		
		
		
		
		/*******************set方法**********************/
		/**
		 * id
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 组织
		 * @param organizStore
		 */
		public void setOrganizStore(OrganizStore organizStore) {
			this.organizStore = organizStore;
		}
		/**
		 * 成员，关联人员基础库信息
		 * @param personStore
		 */
		public void setPersonStore(PersonStore personStore) {
			this.personStore = personStore;
		}
}
