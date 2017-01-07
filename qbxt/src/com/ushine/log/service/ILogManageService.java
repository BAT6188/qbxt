package com.ushine.log.service;

import com.hy.log_manageClient.model.PojoViewLogModel;

/**
 * 日志管理
 * 
 * @author leibin
 * 
 */

public interface ILogManageService {

	/**
	 * 查询日志
	 * 
	 * @param str
	 */
	public String queryLog(PojoViewLogModel str) throws Exception;
	
}
