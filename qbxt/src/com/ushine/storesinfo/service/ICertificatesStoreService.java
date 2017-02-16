package com.ushine.storesinfo.service;

import com.ushine.storesinfo.model.CertificatesStore;

/**
 * 证件库接口
 * @author wangbailin
 *
 */
public interface ICertificatesStoreService {
	/**
	 * 新增证件库
	 * @param certificatesStore
	 * @return
	 * @throws Exception
	 */
	public boolean saveCertificates(CertificatesStore certificatesStore)throws Exception;
	/**
	 * 根据证件id查询
	 * @param certificatesStore
	 * @return
	 * @throws Exception
	 */
	public CertificatesStore findCertificatesById(String certificatesId)throws Exception;
	/**
	 * 根据id删除账号
	 * @param strings
	 */
	public void deleteCertificatesByIds(String[] strings);
}
