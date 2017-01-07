package com.ushine.storeInfo.service;

import java.util.List;

import com.ushine.storeInfo.model.TempClueData;

/**
 * 线索涉及对象临时数据接口
 * @author wangbailin
 *
 */
public interface ITempClueDataService {
	/**
	 * 新增线索涉及对象临时数据
	 * @param clueData
	 * @return
	 * @throws Exception
	 */
	public boolean saveTempClueData(TempClueData clueData)throws Exception;
	/**
	 * 更新临时线索
	 * @param clueData
	 * @param state 如果state为0代表没关联,此时data_id属性值为空<br>
	 * 需要更新它的state为1,并且data_id关联到线索人员或组织或媒体
	 * @return
	 * @throws Exception
	 */
	public boolean updateTempClueData(TempClueData clueData)throws Exception;
	/**
	 * 根据当前操作随机数查询线索涉及对象临时数据
	 * @return
	 * @throws Exception
	 */
	public List<TempClueData> findTempClueData(String action)throws Exception;
	/**
	 * 删除临时数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean dateTempClueDataById(String id)throws Exception;
	/**
	 * 新增线索涉及对象临时数据，选择其他表已有的数据
	 * @param ids
	 * @param names
	 * @param type
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public boolean saveTempClueDataBySelect(String[] ids,String[] names,String type,String number)throws Exception;
	/**
	 * 根据标识删除数据
	 * @return
	 * @throws Exception
	 */
	public boolean delTempCluDataByAction(String action)throws Exception;
}
