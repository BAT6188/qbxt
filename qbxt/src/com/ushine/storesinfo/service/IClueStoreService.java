package com.ushine.storesinfo.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.ushine.common.vo.PagingObject;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.OrganizStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;

/**
 * 线索库接口
 * @author wangbailin
 *
 */
public interface IClueStoreService {
		/**
		 * 新增线索
		 * @param clueStore
		 * @return
		 * @throws Exception
		 */
		public boolean saveClue(ClueStore clueStore)throws Exception;
		/**
		 * 修改线索
		 * @param clueStore
		 * @return
		 * @throws Exception
		 */
		public boolean updateClue(ClueStore clueStore)throws Exception;
		/**
		 * 根据线索id查询
		 * @param clueId
		 * @return
		 * @throws Exception
		 */
		public ClueStore findClueById(String clueId)throws Exception;
		/**
		 * 查询线索，，，多条件
		 * @param field
		 * @param fieldValue
		 * @param startTime
		 * @param endTime
		 * @param nextPage
		 * @param size
		 * @param uid
		 * @param oid
		 * @param did
		 * @param sortField
		 * @return
		 * @throws Exception
		 */
		public String findClueStore(String field,String fieldValue,String startTime,String endTime,
				int nextPage,int size,String uid,String oid,String did,String sortField,String dir)throws Exception;
		/**
		 * 根据id删除线索
		 * @param ids
		 * @return
		 * @throws Exception
		 */
		public boolean delClueStoreByIds(String[] ids)throws Exception;
		/**
		 * 根据线索的id和人员的参数值查询人员信息
		 * @param id
		 * @param field
		 * @param fieldValue
		 * @param nextPage
		 * @param size
		 * @return
		 * @throws Exception
		 */
		public PagingObject<PersonStore> findCluePersonStore(String clueId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
		/**
		 * 根据线索的id和组织的参数值查询组织信息
		 * @param id
		 * @param field
		 * @param fieldValue
		 * @param nextPage
		 * @param size
		 * @return
		 * @throws Exception
		 */
		public PagingObject<OrganizStore> findClueOrganizStore(String clueId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
		/**
		 * 根据线索的id和媒体网站的参数值查询媒体网站信息
		 * @param id
		 * @param field
		 * @param fieldValue
		 * @param nextPage
		 * @param size
		 * @return
		 * @throws Exception
		 */
		public PagingObject<WebsiteJournalStore> findClueWebsiteJournalStore(String clueId,String field,String fieldValue,String startTime,String endTime,int nextPage,int size)throws Exception;
		/**
		 * 分页和根据权限查询数据
		 * @param nextPage
		 * @param size
		 * @param uid
		 * @param oid
		 * @param did
		 * @return
		 * @throws Exception
		 */
		public PagingObject<ClueStore> findClueStore(int nextPage,int size,String uid,String oid,String did)throws Exception;
		public List<PersonStore> findPersonStoreByClueId(String clueId)throws Exception;
		public List<OrganizStore> findOrganizStoreByClueId(String clueId)throws Exception;
		public List<WebsiteJournalStore> findWebsiteJournalStoreByClueId(String clueId)throws Exception;
		/**
		 * 修改线索库信息为启用
		 * @param orgIds
		 * @return
		 * @throws Exception
		 */
		public boolean updateClueStoreIsEnableStart(String[] clueIds)throws Exception;
		/**
		 * 修改线索库信息为禁用
		 * @param orgIds
		 * @return
		 * @throws Exception
		 */
		public boolean updateClueStoreIsEnableCease(String[] clueIds)throws Exception;
		/**
		 * 输出cluestore信息为word
		 * @param id id
		 * @param filePath 文件路径
		 */
		public void outputClueStoreToWord(String id,String filePath);
		
		/**
		 * 根据线索名判断是否已经存在
		 * @param clueName
		 * @return
		 */
		public boolean hasStoreByClueName(String clueName);
}
