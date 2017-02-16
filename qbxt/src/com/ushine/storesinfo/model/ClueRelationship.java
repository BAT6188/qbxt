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
 * 线索关系实体
 * @author wangbailin
 *
 */
@Entity
@Table(name = "T_CLUE_RELATIONSHIP")
public class ClueRelationship {

		@Override
	public String toString() {
		return "ClueRelationship [id=" + id + ", clueId=" + clueId + ", libraryId=" + libraryId + ", dataType="
				+ dataType + ", getDataType()=" + getDataType() + ", getId()=" + getId() + ", getClueId()="
				+ getClueId() + ", getLibraryId()=" + getLibraryId() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
		private String id;//id
		private String clueId;//线索id
		private String libraryId;//基础库id
		private String dataType;//区分该数据所属哪一种数据的personStore:人员库数据    organizStore：组织库数据     websiteJournalStore：媒体库数据
		
		@Column(name = "DATA_TYPE", length = 32)
		public String getDataType() {
			return dataType;
		}
		public void setDataType(String dataType) {
			this.dataType = dataType;
		}
		/************get方法*******************/
		@Id
		@GenericGenerator(name = "uId", strategy = "uuid.hex")
		@GeneratedValue(generator = "uId")
		@Column(name = "ID", length = 32)
		public String getId() {
			return id;
		}
		/**
		 * 线索id
		 * @return
		 */
		@Column(name = "CLUE_ID", length = 32)
		public String getClueId() {
			return clueId;
		}
		
		/**
		 * 基础库id
		 * @return
		 */
		@Column(name = "LIBRARY_ID", length = 60)
		public String getLibraryId() {
			return libraryId;
		}

		
		
		
		
		/****************set方法********************/
		/**
		 * id
		 * @param id
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * 线索id
		 * @param clueId
		 */
		public void setClueId(String clueId) {
			this.clueId = clueId;
		}
		/**
		 * 基础库id
		 * @param libraryId
		 */
		public void setLibraryId(String libraryId) {
			this.libraryId = libraryId;
		}
		
}
