package com.ushine.storesinfo.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ushine.common.vo.PagingObject;
import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.PersonStore;

/**
 * 人员库接口类
 * @author wangbailin
 *
 */
public interface IPersonStoreService {
	/**
	 * 新增人员库
	 * @param personStore
	 * @return
	 * @throws Exception
	 */
	public boolean savePersonStore(PersonStore personStore)throws Exception;
	/**
	 * 根据id查询人员库信息
	 * @param personStoreId
	 * @return
	 * @throws Exception
	 */
	public PersonStore findPersonStoreById(String personStoreId) throws Exception;
	/**
	 * 根据更新人员库信息
	 * @param  
	 * @return 更新结果
	 * @throws Exception
	 */
	public String updatePersonStore(PersonStore store)throws Exception;
	/**
	 * 根据人员的id删除人员
	 * @param personStoreIds 人员id的数组
	 * @return 删除结果的json字符串
	 * @throws Exception
	 */
	public String delPersonStoreByIds(String []personStoreIds)throws Exception;
	/**
	 * 根据传入条件和权限分页筛选组织记录
	 * @Description: TODO
	 * @param field 查询的字段名称
	 * @param fieldValue 关键字
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param nextPage 翻页
	 * @param size 每页数量
	 * @param uid 根据uid查询个人数据
	 * @param oid 根据组织id查询数据
	 * @param did 根据部门id查询数据
	 * @param sortField 排序字段
	 * @param dir 升或降
	 * @return
	 * @throws Exception
	 * @author donghao
	 * @date 2016-5-10
	 */
	public String findPersonStore(String field, String fieldValue, String startTime,String endTime,
			int nextPage, int size, String uid, String oid, String did,String sortField,String dir)
			throws Exception;
	/**
	 * 根据是否启用来查询启用的数据
	 * @param isEnable
	 * @return
	 * @throws Exception
	 */
	//public PagingObject<PersonStore> findPersonStoreByIsEnable(String field, String fieldValue, String startTime,String endTime,int nextPage, int size, String uid, String oid, String did)throws Exception;
	/**
	 * 设置启用
	 * @param ids 人员id数组
	 * @throws Exception
	 */
	public void updatePersonStoreIsEnableStart(String []ids)throws Exception;
	/**
	 * 设置成禁用
	 * @param ids 人员id数组
	 * @throws Exception
	 */
	public void updatePersonStoreIsEnableCease(String []ids)throws Exception;
	/**
	 * 根据人员库信息名称查询数据，如果有数据返回true，反之亦然
	 * @param personName
	 * @return
	 * @throws Exception
	 */
	public boolean findPersonStoreByPersonName(String personName)throws Exception;
	/**
	 * 验证信息是否合理
	 * @param personName 姓名不能为空
	 * @param infoType 类别应该存在
	 * @param beBornTime 日期合法
	 * @param sex 性别合法
	 * @return 合理返回空串,不合理则返回说明
	 * @throws Exception
	 */
	public String canBeSaved(String personName,String infoType,String bebornTime,String sex)throws Exception;
	/**
	 * /**
	 * 保存多个人员信息
	 * @param datas 前台传入的gridpanel的数据
	 * @param startRowNumber 表格开始读取数据的行号
	 * @param HttpServletRequest request请求
	 * @return 保存后的相关信息
	 * @throws Exception
	 */
	public String saveMuiltiPersonStore(HttpServletRequest request,Integer startRowNumber,String datas)throws Exception;
	/**
	 * 保存人员信息到Excel中
	 * @param path 保存的路径
	 * @param ids 人员的id数组
	 * @throws Exception
	 */
	public void outputPersonStoreToExcel(String path,String []ids)throws Exception;
	/**
	 * 把人员信息导入word中
	 * @param id 人员的id
	 */
	public File outputPersonStoreToWord(String id);
	
	public void testFindPersonStoreById(int count);
}
