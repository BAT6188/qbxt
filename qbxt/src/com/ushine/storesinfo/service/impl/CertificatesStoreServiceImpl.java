package com.ushine.storesinfo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.service.ICertificatesStoreService;

/**
 * 证件库接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("certificatesStoreServiceImpl")
public class CertificatesStoreServiceImpl implements ICertificatesStoreService{
	private static final Logger logger = LoggerFactory.getLogger(InfoTypeServiceImpl.class);
	@Autowired
	private IBaseDao<CertificatesStore, String> baseDao;
	public boolean saveCertificates(CertificatesStore certificatesStore)
			throws Exception {
		// TODO Auto-generated method stub
		baseDao.save(certificatesStore);
		return true;
	}

	public CertificatesStore findCertificatesById(
			String certificatesId) throws Exception {
		// TODO Auto-generated method stub
		CertificatesStore certificates =baseDao.findById(CertificatesStore.class, certificatesId);
		return certificates;
	}

	public void deleteCertificatesByIds(String[] ids) {
		// TODO Auto-generated method stub
		//删除
		try {
			baseDao.deleteById(CertificatesStore.class, ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
