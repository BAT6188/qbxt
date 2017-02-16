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
 * 组织刊物
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_ORGANIZ_PUBLIC_ACTION")
public class OrganizPublicAction {
	
		private String id;   //id
		private OrganizStore organizStore;  //组织
		private WebsiteJournalStore websiteJournalStore;//所属刊物，关联媒体网站刊物基础库信息
		
		
		
		
		/*************get方法******************/
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
		 * 刊物
		 * @return
		 */
		@ManyToOne(cascade = { CascadeType.MERGE })
		@JoinColumn(name = "WEBSITE_JOURNAL_STORE_ID")
		public WebsiteJournalStore getWebsiteJournalStore() {
			return websiteJournalStore;
		}
		
		
		
		
		
		
		/****************set方法**************************/
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
		 * 所属刊物，关联媒体网站刊物基础库信息
		 * @param websiteJournalStore
		 */
		public void setWebsiteJournalStore(WebsiteJournalStore websiteJournalStore) {
			this.websiteJournalStore = websiteJournalStore;
		}
		
		
		
}
