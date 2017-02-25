package com.ushine.storesinfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.ClueRelationship;
import com.ushine.storesinfo.model.ClueStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.TempClueData;
import com.ushine.storesinfo.service.IClueRelationshipService;
import com.ushine.storesinfo.service.IClueStoreService;
import com.ushine.storesinfo.service.IPersonStoreService;
/**
 * 线索基础库关系接口实现类
 * @author wangbailin
 *
 */
@Transactional
@Service("clueRelationshipServiceImpl")
public class ClueRelationshipServiceImpl implements IClueRelationshipService{
	private static final Logger logger = LoggerFactory.getLogger(InfoTypeServiceImpl.class);
	@Autowired
	private IBaseDao<ClueRelationship, String> baseDao;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private IBaseDao storeIdDao;
	
	@Autowired
	private IPersonStoreService personStoreService;
	@Autowired
	private IClueStoreService clueStoreService;
	public boolean savaClueRelationship(ClueRelationship clueRelationship)
			throws Exception {
		//关联
		//新增前要根据线索id和基础库id和类别判断是否在数据库中存在，如果存在就不新增，反之亦然
		String hql=String.format("select id from ClueRelationship where clueId='%s' and libraryId='%s' and dataType='%s'", 
				clueRelationship.getClueId(),clueRelationship.getLibraryId(),clueRelationship.getDataType());
		int count =baseDao.findByHql(hql).size();
		//String sql = "SELECT COUNT(*) FROM T_CLUE_RELATIONSHIP  WHERE CLUE_ID = '"++"' AND LIBRARY_ID = '"+clueRelationship.getLibraryId()+"' AND DATA_TYPE= '"+clueRelationship.getDataType()+"'";
		//int count = Integer.parseInt(baseDao.getRows(sql).toString());
		if(count <= 0){
			baseDao.save(clueRelationship);
			//nrtSearch.deleteIndex(clueRelationship.getClueId());
			//添加新的线索索引
			//nrtSearch.addIndex(clueStoreService.findClueById(clueRelationship.getClueId()));
		}
		return true;
	}
	public ClueRelationship findClueRelationshipById(String clueRelationshipId)
			throws Exception {
		// TODO Auto-generated method stub
		return (ClueRelationship) baseDao.findById(ClueRelationship.class, clueRelationshipId);
	}
	public boolean saveClueRelationship(String clueId,
			List<TempClueData> clueDatas) throws Exception {
		// TODO Auto-generated method stub
		for (TempClueData tempClueData : clueDatas) {
			if("1".equals(tempClueData.getState())){//新增线索关系时只新增已关联到其他库的数据，
				ClueRelationship relationship = new ClueRelationship();
				relationship.setClueId(clueId);
				relationship.setDataType(tempClueData.getType());
				relationship.setLibraryId(tempClueData.getDataId());
				baseDao.save(relationship);
			}else if("0".equals(tempClueData.getState())){  //没有入库的数据，先进行入库在进行关联关系
				//判断当前数据是否属于哪一个基础库的
				if("personStore".equals(tempClueData.getType())){
					PersonStore store = new PersonStore();
					store.setPersonName(tempClueData.getName());
					store.setIsToStorage("2");
					personStoreService.savePersonStore(store);
					//新增线索关系
					ClueRelationship relationship = new ClueRelationship();
					relationship.setClueId(clueId);
					relationship.setDataType(tempClueData.getType());
					relationship.setLibraryId(store.getId());
					baseDao.save(relationship);
				}/*else if("organizStore".equals(tempClueData.getType())){
					OrganizStore store = new OrganizStore();
					store.setOrganizName(tempClueData.getName());
					store.setIsToStorage("2");
					organizStoreService.saveOrganizStore(store);
					//新增线索关系
					ClueRelationship relationship = new ClueRelationship();
					relationship.setClueId(clueId);
					relationship.setDataType(tempClueData.getType());
					relationship.setLibraryId(store.getId());
					baseDao.save(relationship);
				}else if("websiteJournalStore".equals(tempClueData.getType())){
					WebsiteJournalStore store = new WebsiteJournalStore();
					store.setName(tempClueData.getName());
					store.setIsToStorage("2");
					websiteJournalStoreService.saveWebsitejournal(store);
					//新增线索关系
					ClueRelationship relationship = new ClueRelationship();
					relationship.setClueId(clueId);
					relationship.setDataType(tempClueData.getType());
					relationship.setLibraryId(store.getId());
					baseDao.save(relationship);
				}*/
			}
		}
		return true;
	}
	public boolean delClueRelationshipByclueId(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		for (int i = 0; i < ids.length; i++) {
			baseDao.deleteByProperty(ClueRelationship.class, "clueId", ids[i]);
		}
		return true;
	}
	public boolean removeClueObjByClueIdAndIds(String clueId, String ids[])
			throws Exception {
		//解除人员
		for (String s : ids) {
			String hql=String.format("delete from ClueRelationship where clueId='%s' and libraryId='%s'", clueId,s);
			//String sql = "DELETE FROM T_CLUE_RELATIONSHIP  WHERE CLUE_ID = '"+clueId+"' AND LIBRARY_ID = '"+s+"'";
			//baseDao.executeSql(sql);
			baseDao.executeHql(hql);
			//nrtSearch.updateIndex(clueId, clueStoreService.findClueById(clueId));
		}
		return true;
	}
	public boolean TurnClueStore(String[] dataId, String[] clueId,String type)
			throws Exception {
		//线索关系集合
		List<ClueRelationship> clueRelationships=new ArrayList<>();
		List<ClueStore> clueStores=new ArrayList<>();
		for (int i = 0; i < clueId.length; i++) {
			for (int j = 0; j < dataId.length; j++) {
				//检查是否存在线索与基础的关系
				//String sqlCount = "SELECT COUNT(*) FROM T_CLUE_RELATIONSHIP  WHERE CLUE_ID = '"+clueId[i]+"' AND LIBRARY_ID = '"+dataId[j]+"' AND DATA_TYPE = '"+type+"'";
				//int count = Integer.parseInt(baseDao.getRows(sqlCount).toString());
				String hql=String.format("select id from ClueRelationship where clueId='%s' and libraryId='%s' and dataType='%s'", 
						clueId[i],dataId[j],type);
				int count =baseDao.findByHql(hql).size();
				if(count<1){
					ClueRelationship clueRelationship=new ClueRelationship();
					clueRelationship.setClueId(clueId[i]);
					clueRelationship.setLibraryId(dataId[j]);
					clueRelationship.setDataType(type);
					clueRelationships.add(clueRelationship);
				}
			}
			//获得线索对象
			ClueStore clueStore=clueStoreService.findClueById(clueId[i]);
			clueStores.add(clueStore);
		}
		//保存数据
		baseDao.save(clueRelationships);
		//删除原来的索引
		//nrtSearch.deleteIndex(clueId);
		//添加新关联的
		//nrtSearch.addIndex(clueStores);
		return true;
	}
	public List<ClueRelationship> findRelationshipByClueId(String clueId) throws Exception {
		List<ClueRelationship> list = baseDao.findByProperty(ClueRelationship.class, "clueId", clueId);
		return list;
	}
	@Override
	public String findCluesIdByStoreId(String storeId,String storeType) throws Exception {
		StringBuffer buffer=new StringBuffer();
		String hql="from ClueRelationship where libraryId='"+storeId+"' and dataType='"+storeType+"'";
		List<ClueRelationship> list=baseDao.findByHql(hql);
		for (ClueRelationship clueRelationship : list) {
			buffer.append(clueRelationship.getClueId()+",");
		}
		return buffer.toString();
	}
	@Override
	public String findStoreIdByClueId(String clueId, String storeType) throws Exception {
		String defalut="[]";
		try {
			List<String> list = storeIdDao.findByHql(String.format("select libraryId from ClueRelationship where clueId='%s' and dataType='%s'", clueId,storeType));
			defalut=list.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询出现异常："+e.getMessage());
		}
		return defalut.substring(1, defalut.length()-1);
	}
}
