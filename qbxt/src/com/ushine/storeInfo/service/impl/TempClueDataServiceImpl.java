package com.ushine.storeInfo.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.TempClueData;
import com.ushine.storeInfo.service.ITempClueDataService;
/**
 * 线索涉及对象临时数据接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("tempClueDataServiceImpl")
public class TempClueDataServiceImpl implements ITempClueDataService{
	@Autowired
	private IBaseDao<TempClueData, String> dao;
	public boolean saveTempClueData(TempClueData clueData) throws Exception {
		// TODO Auto-generated method stub
		//新增前要根据线索id和基础库id和类别判断是否在数据库中存在，如果存在就不新增，反之亦然
		String sql = "SELECT COUNT(*) FROM TEMP_CLUE_DATA  WHERE ACTION = '"+clueData.getAction()+"'  AND NAME = '"+clueData.getName()+"' AND TYPE = '"+clueData.getType()+"'";
		int count = Integer.parseInt(dao.getRows(sql).toString());
		if(count <= 0){
			dao.save(clueData);
		}
		return true;
	}
	
	
	public boolean updateTempClueData(TempClueData clueData)
			throws Exception {
		String state="0";//没关联
		String hql = "from TempClueData  WHERE action = '"+clueData.getAction()+"'  AND name = '"+clueData.getName()
				+"' AND type = '"+clueData.getType()+"' AND state = '"+state+"'";
		//int count = Integer.parseInt(dao.getRows(sql).toString());
		List<TempClueData> list=dao.findByHql(hql);
		if(list!=null&&list.size()>0){
			//说明这条记录线索没有关联到人员,组织,网站
			//System.out.println(list.get(0).toString());
			TempClueData thisClueData=list.get(0);
			thisClueData.setDataId(clueData.getDataId());
			thisClueData.setState("1");
			dao.update(thisClueData);
			return true;
		}
		//System.out.println(count);
		return false;
	}

	public List<TempClueData> findTempClueData(String action) throws Exception {
		// TODO Auto-generated method stub
		DetachedCriteria criteria = DetachedCriteria.forClass(TempClueData.class);
		criteria.add(Restrictions.eq("action", action));
		criteria.addOrder(Order.asc("type"));
		return dao.findByCriteria(criteria);
	}

	public boolean dateTempClueDataById(String id) throws Exception {
		// TODO Auto-generated method stub
		dao.deleteById(TempClueData.class, id);
		return true;
	}

	public boolean saveTempClueDataBySelect(String[] ids, String[] names,
			String type, String number) throws Exception {
		for (int i = 0;i <ids.length;i++) {
			
			TempClueData clueData = new TempClueData();
			clueData.setAction(number);
			clueData.setDataId(ids[i]);
			clueData.setName(names[i]);
			clueData.setState("1");
			clueData.setType(type);
			//新增前要根据线索id和基础库id和类别判断是否在数据库中存在，如果存在就不新增，反之亦然
			String hql="select id from TempClueData where action='%s' and dataId='%s' and name='%s' and type='%s'";
			hql=String.format(hql, clueData.getAction(),clueData.getDataId(),clueData.getName(),clueData.getType());
			//String sql = "SELECT COUNT(*) FROM TEMP_CLUE_DATA  WHERE ACTION = '"+clueData.getAction()+"' AND DATA_ID = '"+clueData.getDataId()+"' AND NAME = '"+clueData.getName()+"' AND TYPE = '"+clueData.getType()+"'";
			//int count = Integer.parseInt(dao.getRows(sql).toString());
			int count = dao.findByHql(hql).size();
			if (count < 1) {
				dao.save(clueData);
			}
		}
		return true;
	}

	public boolean delTempCluDataByAction(String action) throws Exception {
		// TODO Auto-generated method stub
		dao.deleteByProperty(TempClueData.class, "action", action);
		return true;
	}

	
	
}
