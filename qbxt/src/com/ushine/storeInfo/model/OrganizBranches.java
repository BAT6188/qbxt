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
 * 组织分支机构
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_ORGANIZ_BRANCHES")
public class OrganizBranches {
		
		private String id;
		private OrganizStore organizStore;//组织
		private OrganizStore organizBranches;//所属分支
		
		
		/*************get方法****************/
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
		@JoinColumn(name = "ORGANIZ_ID")
		public OrganizStore getOrganizStore() {
			return organizStore;
		}
		/**
		 * 所属分支
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "ORGANIZ_BRANCHES_ID")
		public OrganizStore getOrganizBranches() {
			return organizBranches;
		}
		
		
		
		/**************set方法*************/
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
		 * 所属分支
		 * @param organizBranches
		 */
		public void setOrganizBranches(OrganizStore organizBranches) {
			this.organizBranches = organizBranches;
		}
		
}
