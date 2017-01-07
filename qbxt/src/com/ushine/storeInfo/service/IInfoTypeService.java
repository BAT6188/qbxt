package com.ushine.storeInfo.service;


import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.opensymphony.xwork2.util.finder.ClassFinder.Info;
import com.ushine.storeInfo.model.InfoType;

/**
 * 信息类别接口
 * @author wangbailin
 *
 */
public interface IInfoTypeService {
	/**
	 * 新增信息类别
	 * @param infoType
	 * @return
	 * @throws Exception
	 */
	public boolean saveInfoType(InfoType infoType)throws Exception;
	/**
	 * 修改信息类别
	 * @param infoType
	 * @return
	 * @throws Exception
	 */
	public boolean updateInfoType(InfoType infoType)throws Exception;
	/**
	 * 修改类别时更新索引库
	 * @param srcInfoType 原来
	 * @param destInfoType 目标
	 * @throws Exception
	 */
	public void updateInfoTypeIndex(InfoType srcInfoType,InfoType destInfoType)throws Exception;
	/**
	 * 删除信息类别
	 * 可一次删除多个
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public boolean delInfoType(String[] ids)throws Exception;
	/**
	 * 根据信息类别id查询信息类别
	 * @param infoTypeId
	 * @return
	 * @throws Exception
	 */
	public InfoType findInfoTypeById(String infoTypeId)throws Exception;
	/**
	 * 查询全部信息类别
	 * @return
	 * @throws Exception
	 */
	public List<InfoType> findInfoTypeAll()throws Exception;
	/**
	 * 解析xml
	 * @param inputStream
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> readXml(InputStream inputStream,String nodeName)throws Exception;
	/**
	 * 根据基础类型名称来查询类型
	 * @param tableTypeName
	 * @return
	 * @throws Exception
	 */
	public List<InfoType> findInfoTypeByTypeName(String tableTypeName)throws Exception;
	/**
	 * 根据表名称查询信息类别
	 * @return
	 * @throws Exception
	 */
	public String getInfoTypeByTableTypeName(String tableTypeName)throws Exception;
	
	/**
	 * 根据类型名称和表名称获得类型<br>
	 * 因为不同的表之间类型值可能会重复
	 * @param typeName 类型名称
	 * @param tableTypeName 表名
	 * @return
	 * @throws Exception
	 */
	public InfoType findInfoTypeByTypeNameAndTableName(String typeName,String tableTypeName)throws Exception;
	/**
	 * 统计每个类别分类统计数据长度
	 * @param tableTypeName
	 * @return
	 * @throws Exception
	 */
	public String infoTypeDataCount(String tableTypeName)throws Exception;
}
