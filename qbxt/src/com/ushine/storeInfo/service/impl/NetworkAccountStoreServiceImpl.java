package com.ushine.storeInfo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.NetworkAccountStore;
import com.ushine.storeInfo.service.INetworkAccountStoreService;

/**
 * 网络账号接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("networkAccountStoreServiceImpl")
public class NetworkAccountStoreServiceImpl implements INetworkAccountStoreService{
	private static final Logger logger = LoggerFactory.getLogger(InfoTypeServiceImpl.class);
	@Autowired
	private IBaseDao<NetworkAccountStore, String> baseDao;
	public boolean saveNetworkAccount(NetworkAccountStore networkAccountStore)
			throws Exception {
		// TODO Auto-generated method stub
		baseDao.save(networkAccountStore);
		return true;
	}
	public NetworkAccountStore findNetworkAccountByid(String networkAccountId)
			throws Exception {
		// TODO Auto-generated method stub
		return baseDao.findById(NetworkAccountStore.class, networkAccountId);
	}
	public void deleteNetworkAccountByIds(String[] ids) {
		// TODO Auto-generated method stub
		//删除
		try {
			baseDao.deleteById(NetworkAccountStore.class, ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
