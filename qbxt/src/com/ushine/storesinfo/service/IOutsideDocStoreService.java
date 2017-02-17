package com.ushine.storesinfo.service;

import javax.servlet.http.HttpServletRequest;

import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.storesinfo.model.OutsideDocStore;

/**
 * 外来文档接口
 * @author wangbailin
 *
 */
public interface IOutsideDocStoreService {
	/**
	 * 根据id查询
	 * @param outsideDocId
	 * @return OutsideDocStore对象实例
	 * @throws Exception
	 */
	public OutsideDocStore findOutsideDocStoreById(String outsideDocId)throws Exception;
	/**
	 * 条件查询
	 * @param field 属性
	 * @param fieldValue 属性值
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param nextPage 下一页
	 * @param size 每页数量
	 * @param uid 用户id
	 * @param oid 组织id 
	 * @param did 部门id
	 * @return json字符串
	 * @return sortField 排序字段
	 * @return dir 升或降
	 * @throws Exception
	 */
	public String findOutsideDocStore(String field, String fieldValue,String startTime, String endTime, int nextPage, int size,
			String uid, String oid, String did,String sortField,String dir)throws Exception;
	
	/**
	 * 删除
	 * 更新action值为3,而不是真正的删除
	 * @param outsideDocStoreIds id数组
	 * @throws Exception
	 */
	public void delOutsideDocStore(String[] outsideDocStoreIds)throws Exception;
	/**
	 * 新增
	 * @param outsideDocStore
	 * @throws Exception
	 */
	public void saveOutsideDocStore(OutsideDocStore outsideDocStore)throws Exception;
	/**
	 * 更新
	 * @param outsideDocStore
	 * @throws Exception
	 */
	public void updateOutsideDocStore(OutsideDocStore outsideDocStore)throws Exception;
	
	/**
	 * 依据名称判断是否文档已经存在
	 * @param name 
	 * @return 
	 */
	public boolean hasStoreByName(String name);
	/**
	 * 识别外来文档
	 * @param directoryPath 文档路径
	 * @param docType 文档类别
	 * @return 识别结果
	 * @throws Exception
	 */
	public String identifyOutsideDoc(String directoryPath, String docType)throws Exception ;
	/**
	 * 保存多个文件
	 * @param datas 
	 * @param infoType	类别
	 * @param sourceUnit 来源单位
	 * @param secretRank 密级
	 * @param request
	 * @param userMgr
	 * @param uploadNumber
	 * @return
	 * @throws Exception
	 */
	public String saveOutsideDocStore(String datas,String infoType,
			String sourceUnit,String secretRank,
			HttpServletRequest request, 
			UserSessionMgr userMgr, String uploadNumber) throws Exception;
	
	public String saveOutsideDocStore(String datas,HttpServletRequest request,UserSessionMgr userMgr)throws Exception;
}
