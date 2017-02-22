package com.ushine.storesinfo.service;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.servlet.http.HttpServletRequest;

import com.ushine.core.verify.session.UserSessionMgr;
import com.ushine.storesinfo.model.VocationalWorkStore;

/**
 * 业务文档库接口
 * @author wangbailin
 *
 */
public interface IVocationalWorkStoreService {
		/**
		 * 新增业务文档库
		 * @param vocationalWorkStore
		 * @return
		 * @throws Exception
		 */
		public boolean saveVocationalWork(VocationalWorkStore vocationalWork) throws Exception;
		/**
		 * 根据文件的全名判断是否已经保存过了
		 * @param fileName
		 * @return 存在返回true
		 * @throws Exception
		 */
		public boolean hasSavedStore(String fileName) throws Exception;
		/**
		 * 新增多个业务文档
		 * @param list VocationalWorkStore集合
		 * @return
		 * @throws Exception
		 */
		public boolean saveVocationalWork(List<VocationalWorkStore> list) throws Exception;
		/**
		 *  新增多个业务文档
		 * @param datas 新增的数据
		 * @param infoType 文档类型
		 * @param request request请求
		 * @param userMgr 登录的用户
		 * @param uploadNumber 上传文档的文件夹号码
		 * @return string类型
		 * @throws Exception
		 */
		public String saveVocationalWork(String datas,String infoType,HttpServletRequest request, 
				UserSessionMgr userMgr, String uploadNumber) throws Exception;
		/**
		 * 上传后新增多个文档
		 * @param datas
		 * @param request
		 * @param userMgr
		 * @return
		 * @throws Exception
		 */
		public String saveVocationalWork(String datas,HttpServletRequest request, UserSessionMgr userMgr)throws Exception;
		/**
		 * 更新业务文档
		 * @param vocationalWorkStore
		 * @return
		 * @throws Exception
		 */
		public boolean updateVocationalWork(VocationalWorkStore vocationalWorkStore)throws Exception;
		/**
		 * 根据id删除业务文档,删除只是把action变为3
		 * @param storeIds id数组
		 * @return 是否成功
		 * @throws Exception
		 */
		public boolean delVocationalWorkStoreByIds(String[] storeIds)throws Exception;
		/**
		 * 根据业务文档id查询业务文档
		 * @param vocationWorkId
		 * @return
		 * @throws Exception
		 */
		public VocationalWorkStore findVocationalWorkById(String vocationWorkId) throws Exception;
		/**
		 * 查询全部业务文档
		 * @return
		 * @throws Exception
		 */
		public List<VocationalWorkStore> findVocationalWorkAll()throws Exception;
		/**
		 * 根据hql语句查询业务文档
		 * @param hql hql语句
		 * @return 集合
		 */
		public List<VocationalWorkStore> findVocationalWorkAll(String hql)throws Exception;
		/**
		 * 
		 * @param field 字段名称
		 * @param fieldValue 字段值
		 * @param startTime 开始时间
		 * @param endTime 结束时间
		 * @param nextPage 
		 * @param size 每页数量
		 * @param uid 个人id
		 * @param oid 组织id
		 * @param did 部门id
		 * @param sortField 需要排序的字段
		 * @param dir 升序或降序
		 * @return json字符串
		 */
		public String findVocationalWorkStore(String field, String fieldValue,
				String startTime, String endTime, int nextPage, int size,
				String uid, String oid, String did,String sortField,String dir)throws Exception;
		/**
		 * 	//识别导入的业务文档
		 * @param directoryPath 文件夹路径
		 * @param docType 业务文档类别
		 */
		public String identifyServiceDoc(String directoryPath,String docType)throws Exception;
		
		/**
		 * 根据docName判断是否已经存在了文档
		 * @param docName 文档名称
		 * @return 存在返回true，反之false
		 */
		public boolean hasStoreByDocName(String docName);
		
}
