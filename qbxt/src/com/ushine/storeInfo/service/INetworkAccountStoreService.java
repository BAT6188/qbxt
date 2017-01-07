package com.ushine.storeInfo.service;

import com.ushine.storeInfo.model.NetworkAccountStore;

/**
 * 网络账号接口
 * @author wangbailin
 *
 */
public interface INetworkAccountStoreService {
	/**
	 * 新增网络账号
	 * @param networkAccountStore
	 * @return
	 * @throws Exception
	 */
	public boolean saveNetworkAccount(NetworkAccountStore networkAccountStore)throws Exception;
	/**
	 * 根据网络账号id查询
	 * @param networkAccountId
	 * @return
	 * @throws Exception
	 */
	public NetworkAccountStore findNetworkAccountByid(String networkAccountId)throws Exception;
	/**
	 * 根据id数组删除
	 * @param strings
	 */
	public void deleteNetworkAccountByIds(String[] strings);
}
