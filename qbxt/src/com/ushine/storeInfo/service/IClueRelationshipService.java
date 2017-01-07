package com.ushine.storeInfo.service;

import java.util.List;

import com.ushine.common.vo.PagingObject;
import com.ushine.storeInfo.model.ClueRelationship;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.TempClueData;

/**
 * 线索基础库关系接口
 * @author wangbailin
 *
 */
public interface IClueRelationshipService {
		/**
		 * 根据store的Id查找所属线索的id
		 * @param storeId store的id
		 * @param storeType store的类型,人员或组织或媒体
		 * @return 线索的id
		 * @throws Exception
		 */
		public String findCluesIdByStoreId(String storeId,String storeType)throws Exception;
		/**
		 * 新增线索基础库关系
		 * @param clueRelationship
		 * @return
		 * @throws Exception
		 */
		public boolean savaClueRelationship(ClueRelationship clueRelationship)throws Exception;
		/**
		 * 关键线索基础库关系id查询
		 * @param clueRelationshipId
		 * @return
		 * @throws Exception
		 */
		public ClueRelationship findClueRelationshipById(String clueRelationshipId)throws Exception;
		/**
		 * 新增线索下的线索涉及对象
		 * @param culeId
		 * @param clueDatas
		 * @return
		 * @throws Exception
		 */
		public boolean saveClueRelationship(String clueId,List<TempClueData> clueDatas)throws Exception;
		/**
		 * 根据线索的id删除线索关系
		 * @param ids
		 * @return
		 * @throws Exception
		 */
		public boolean delClueRelationshipByclueId(String[] ids)throws Exception;
		/**
		 * 根据线索的id和基础库id解除线索关系
		 * @param clueId
		 * @param ids
		 * @return
		 * @throws Exception
		 */
		public boolean removeClueObjByClueIdAndIds(String clueId,String ids[])throws Exception;
		/**
		 * 基础库信息转线索
		 * @param dataId
		 * @param clueId
		 * @return
		 * @throws Exception
		 */
		public boolean TurnClueStore(String[] dataId,String[] clueId,String type)throws Exception;
		/**
		 * 根据线索id查询线索关系
		 * @param clueId
		 * @return
		 * @throws Exception
		 */
		public List<ClueRelationship> findRelationshipByClueId(String clueId)throws Exception;
}
