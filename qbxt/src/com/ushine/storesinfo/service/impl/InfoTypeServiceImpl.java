package com.ushine.storesinfo.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.dao.IBaseDao;
import com.ushine.luceneindex.index.LeadSpeakStoreNRTSearch;
import com.ushine.luceneindex.index.OutsideDocStoreNRTSearch;
import com.ushine.luceneindex.index.PersonStoreNRTSearch;
import com.ushine.luceneindex.index.VocationalWorkStoreNRTSearch;
import com.ushine.luceneindex.index.WebsiteJournalStoreNRTSearch;
import com.ushine.storesinfo.model.CertificatesStore;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.LeadSpeakStore;
import com.ushine.storesinfo.model.NetworkAccountStore;
import com.ushine.storesinfo.model.OutsideDocStore;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.model.VocationalWorkStore;
import com.ushine.storesinfo.model.WebsiteJournalStore;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.ILeadSpeakStoreService;
import com.ushine.util.XMLHandler;

/**
 * 信息类别接口实现
 * 
 * @author wangbailin
 *
 */
@Transactional
@Service("infoTypeServiceImpl")
public class InfoTypeServiceImpl implements IInfoTypeService {
	private static final Logger logger = LoggerFactory.getLogger(InfoTypeServiceImpl.class);
	@Autowired
	private IBaseDao baseDao;
	@Autowired
	private ILeadSpeakStoreService leadSpeakStoreService;
	/**
	 * 关联的类别变化后需要重新建立索引库
	 * 
	 * @param storeName
	 */
	private void reCreateIndex(InfoType srcInfoType, InfoType destInfoType) {
		logger.info("正在更新索引库");
		String storeName = srcInfoType.getTableTypeName();
		try {
			if ("PersonStore".equals(storeName)) {
				DetachedCriteria criteria = DetachedCriteria.forClass(PersonStore.class);
				// 查询aciton不为3的
				criteria.add(Restrictions.ne("action", "3"));
				criteria.createAlias("infoType", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
				// 创建索引
				PersonStoreNRTSearch personStoreNRTSearch = PersonStoreNRTSearch.getInstance();
				List<PersonStore> list=baseDao.findByCriteria(criteria);
				System.err.println("PersonStore所属类别数量"+list.size());
				String []ids=new String[list.size()];
				//修改后的
				List<PersonStore> newList=new ArrayList<>();
				int i=0;
				for (PersonStore store : list) {
					ids[i]=store.getId();
					i++;
					store.setInfoType(destInfoType);
					newList.add(store);
				}
				//删除
				personStoreNRTSearch.deleteIndex(ids);
				//新加
				personStoreNRTSearch.addIndex(newList);
			}
			/*if ("OrganizStore".equals(storeName)) {
				OrganizStoreNRTSearch organizStoreNRTSearch = OrganizStoreNRTSearch.getInstance();
				// 查询aciton不为3的
				DetachedCriteria criteria = DetachedCriteria.forClass(OrganizStore.class);
				criteria.add(Restrictions.ne("action", "3"));
				criteria.createAlias("infoType", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
				List<OrganizStore> list=baseDao.findByCriteria(criteria);
				System.err.println("OrganizStore所属类别数量"+list.size());
				String []ids=new String[list.size()];
				//修改后的
				List<OrganizStore> newList=new ArrayList<>();
				int i=0;
				for (OrganizStore store : list) {
					ids[i]=store.getId();
					i++;
					store.setInfoType(destInfoType);
					newList.add(store);
				}
				//删除原来的索引
				organizStoreNRTSearch.deleteIndex(ids);
				//添加新索引
				organizStoreNRTSearch.addIndex(newList);
			}*/
			if ("WebsiteJournalStore".equals(storeName)) {
				WebsiteJournalStoreNRTSearch websiteJournalStoreNRTSearch=WebsiteJournalStoreNRTSearch.getInstance();
				DetachedCriteria criteria = DetachedCriteria.forClass(WebsiteJournalStore.class);
				// 查询aciton不为3的
				criteria.add(Restrictions.ne("action", "3"));
				criteria.createAlias("infoType", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
				List<WebsiteJournalStore> list=baseDao.findByCriteria(criteria);
				System.err.println("WebsiteJournalStore所属类别数量"+list.size());
				String []ids=new String[list.size()];
				//修改后的
				List<WebsiteJournalStore> newList=new ArrayList<>();
				int i=0;
				for (WebsiteJournalStore store : list) {
					ids[i]=store.getId();
					i++;
					store.setInfoType(destInfoType);
					newList.add(store);
				}
				//删除原来的索引
				websiteJournalStoreNRTSearch.deleteIndex(ids);
				//添加新索引
				websiteJournalStoreNRTSearch.addIndex(newList);
			}
			if ("VocationalWorkStore".equals(storeName)) {
				VocationalWorkStoreNRTSearch vocationalWorkStoreNRTSearch = VocationalWorkStoreNRTSearch.getInstance();
				DetachedCriteria criteria = DetachedCriteria.forClass(VocationalWorkStore.class);
				// 查询aciton不为3的
				criteria.add(Restrictions.ne("action", "3"));
				criteria.createAlias("infoType", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
				List<VocationalWorkStore> list=baseDao.findByCriteria(criteria);
				System.err.println("VocationalWorkStore所属类别数量"+list.size());
				String []ids=new String[list.size()];
				//修改后的
				List<VocationalWorkStore> newList=new ArrayList<>();
				int i=0;
				for (VocationalWorkStore store : list) {
					ids[i]=store.getId();
					i++;
					store.setInfoType(destInfoType);
					newList.add(store);
				}
				//删除原来的索引
				vocationalWorkStoreNRTSearch.deleteIndex(ids);
				//添加新索引
				vocationalWorkStoreNRTSearch.addIndex(newList);
			}
			if ("OutsideDocStore".equals(storeName)) {
				OutsideDocStoreNRTSearch outsideDocStoreNRTSearch=OutsideDocStoreNRTSearch.getInstance();
				DetachedCriteria criteria = DetachedCriteria.forClass(OutsideDocStore.class);
				// 查询aciton不为3的
				criteria.add(Restrictions.ne("action", "3"));
				criteria.createAlias("infoType", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
				List<OutsideDocStore> list=baseDao.findByCriteria(criteria);
				System.err.println("OutsideDocStore所属类别数量"+list.size());
				String []ids=new String[list.size()];
				//修改后的
				List<OutsideDocStore> newList=new ArrayList<>();
				int i=0;
				for (OutsideDocStore store : list) {
					ids[i]=store.getId();
					i++;
					store.setInfoType(destInfoType);
					newList.add(store);
				}
				//删除原来的索引
				outsideDocStoreNRTSearch.deleteIndex(ids);
				//添加新索引
				outsideDocStoreNRTSearch.addIndex(newList);
			}
			if ("LeadSpeakStore".equals(storeName)) {
				DetachedCriteria criteria = DetachedCriteria.forClass(LeadSpeakStore.class);
				LeadSpeakStoreNRTSearch leadSpeakStoreNRTSearch = LeadSpeakStoreNRTSearch.getInstance();
				// 查询aciton不为3的
				criteria.add(Restrictions.ne("action", "3"));
				criteria.createAlias("infoType", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
				List<LeadSpeakStore> list=baseDao.findByCriteria(criteria);
				System.err.println("LeadSpeakStore所属类别数量"+list.size());
				String []ids=new String[list.size()];
				//修改后的
				List<LeadSpeakStore> newList=new ArrayList<>();
				int i=0;
				for (LeadSpeakStore store : list) {
					ids[i]=store.getId();
					i++;
					store.setInfoType(destInfoType);
					newList.add(store);
				}
				//删除原来的索引
				leadSpeakStoreNRTSearch.deleteIndex(ids);
				//添加新索引
				leadSpeakStoreNRTSearch.addIndex(newList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("更新索引库完成");
	}

	public boolean saveInfoType(InfoType infoType) throws Exception {
		// 新增
		baseDao.save(infoType);
		return true;
	}
	/**
	 * 更新类别需要应该更新索引库
	 */
	@Override
	public void updateInfoTypeIndex(InfoType srcInfoType, InfoType destInfoType) throws Exception {
		//logger.info("srcInfoType"+srcInfoType.toString());
		//logger.info("destInfoType"+destInfoType.toString());
		String storeName = srcInfoType.getTableTypeName();
		//更新类别
		reCreateIndex(srcInfoType, destInfoType);
		//更新涉及领域
		if ("InvolvedInTheField".equals(storeName)) {
			LeadSpeakStoreNRTSearch leadSpeakStoreNRTSearch=LeadSpeakStoreNRTSearch.getInstance();
			OutsideDocStoreNRTSearch outsideDocStoreNRTSearch=OutsideDocStoreNRTSearch.getInstance();
			VocationalWorkStoreNRTSearch vocationalWorkStoreNRTSearch=VocationalWorkStoreNRTSearch.getInstance();
			//领导讲话
			DetachedCriteria criteria = DetachedCriteria.forClass(LeadSpeakStore.class);
			// 查询aciton不为3的
			criteria.add(Restrictions.ne("action", "3"));
			criteria.createAlias("involvedInTheField", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
			List<LeadSpeakStore> list=baseDao.findByCriteria(criteria);
			System.err.println("LeadSpeakStore所属领域数量"+list.size());
			String []ids=new String[list.size()];
			//修改后的
			List<LeadSpeakStore> newList=new ArrayList<>();
			int i=0;
			for (LeadSpeakStore leadSpeakStore : list) {
				ids[i]=leadSpeakStore.getId();
				i++;
				leadSpeakStore.setInvolvedInTheField(destInfoType);
				newList.add(leadSpeakStore);
			}
			//删除
			leadSpeakStoreNRTSearch.deleteIndex(ids);
			//新加
			leadSpeakStoreNRTSearch.addIndex(newList);
			//外来文档
			DetachedCriteria odsCriteria = DetachedCriteria.forClass(OutsideDocStore.class);
			// 查询aciton不为3的
			odsCriteria.add(Restrictions.ne("action", "3"));
			odsCriteria.createAlias("involvedInTheField", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
			List<OutsideDocStore> odsList=baseDao.findByCriteria(odsCriteria);
			System.err.println("OutsideDocStore所属领域数量"+odsList.size());
			String []odsids=new String[odsList.size()];
			//修改后的
			List<OutsideDocStore> odsNewList=new ArrayList<>();
			int j=0;
			for (OutsideDocStore outsideDocStore : odsList) {
				odsids[j]=outsideDocStore.getId();
				j++;
				outsideDocStore.setInvolvedInTheField(destInfoType);
				odsNewList.add(outsideDocStore);
			}
			//删除
			outsideDocStoreNRTSearch.deleteIndex(odsids);
			//新加
			outsideDocStoreNRTSearch.addIndex(odsNewList);
			//业务
			DetachedCriteria vwsCriteria = DetachedCriteria.forClass(VocationalWorkStore.class);
			// 查询aciton不为3的
			vwsCriteria.add(Restrictions.ne("action", "3"));
			vwsCriteria.createAlias("involvedInTheField", "i").add(Restrictions.eq("i.typeName", srcInfoType.getTypeName()));
			List<VocationalWorkStore> vwsList=baseDao.findByCriteria(vwsCriteria);
			System.err.println("VocationalWorkStore所属领域数量"+vwsList.size());
			String []vwsids=new String[vwsList.size()];
			//修改后的
			List<VocationalWorkStore> vwsNewList=new ArrayList<>();
			int k=0;
			for (VocationalWorkStore vocationalWorkStore : vwsList) {
				vwsids[k]=vocationalWorkStore.getId();
				k++;
				//vocationalWorkStore.setInvolvedInTheField(destInfoType);
				vwsNewList.add(vocationalWorkStore);
			}
			//删除
			vocationalWorkStoreNRTSearch.deleteIndex(vwsids);
			//新加
			vocationalWorkStoreNRTSearch.addIndex(vwsNewList);
		}
	}
	
	public boolean updateInfoType(InfoType infoType) throws Exception {
		baseDao.update(infoType);
		return true;
	}

	public InfoType findInfoTypeById(String infoTypeId) throws Exception {
		// TODO Auto-generated method stub
		InfoType infoType = (InfoType) baseDao.findById(InfoType.class, infoTypeId);
		return infoType;
	}

	public List<InfoType> findInfoTypeAll() throws Exception {
		// TODO Auto-generated method stub
		List<InfoType> infoTypes = baseDao.findAll(InfoType.class);
		return infoTypes;
	}

	public List<HashMap<String, String>> readXml(InputStream inputStream, String nodeName) throws Exception {

		// 创建一个解析xml的工厂对象
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser parser = spf.newSAXParser();// 解析xml
		XMLHandler handler = new XMLHandler(nodeName);
		parser.parse(inputStream, handler);
		inputStream.close();

		List<HashMap<String, String>> listMap = new ArrayList<HashMap<String, String>>();
		for (HashMap<String, String> hashMap : handler.getList()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(hashMap.get("basedLibraryType"), hashMap.get("basedLibraryName"));
			listMap.add(map);
		}
		return handler.getList();
	}

	public List<InfoType> findInfoTypeByTypeName(String tableTypeName) throws Exception {
		List<InfoType> list = baseDao.findByHql("from " + InfoType.class.getName() +
				" where tableTypeName = '" + tableTypeName + "'");
		return list;
	}

	public boolean delInfoType(String[] ids) throws Exception {
		//删除
		for (String string : ids) {
			InfoType infoType = (InfoType) baseDao.findById(InfoType.class, string);
		}
		baseDao.deleteById(InfoType.class, ids);
		return true;
	}

	public String getInfoTypeByTableTypeName(String tableTypeName) throws Exception {
		List<InfoType> list = baseDao.findByHql("from " + InfoType.class.getName() + " where tableTypeName = '" + tableTypeName + "'");
		return getInfoTypeByTableTypeNameVoToJSon(list);
	}

	/**
	 * 将特定的数据转换成json格式
	 * @param list
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public String getInfoTypeByTableTypeNameVoToJSon(List<InfoType> list) {
		JSONArray array = new JSONArray();
		for (InfoType infoType : list) {
			JSONObject obj = new JSONObject();
			//System.out.println(infoType.getId());
			obj.put("value", infoType.getId());
			obj.put("text", infoType.getTypeName());
			array.add(obj);
		}
		return array.toString();
	}

	// @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public InfoType findInfoTypeByTypeNameAndTableName(String typeName, String tableTypeName) throws Exception {
		InfoType infoType=null;
		try {
			infoType = (InfoType) baseDao.findByHql("from " + InfoType.class.getName() + " where tableTypeName= '"
					+ tableTypeName + "' and " + "typeName='" + typeName + "'").get(0);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return infoType;
	}

	public String infoTypeDataCount(String tableTypeName) throws Exception {
		// TODO Auto-generated method stub
		if ("PersonStore".equals(tableTypeName)) {
			return personStoreCount(tableTypeName);
		} /*else if ("OrganizStore".equals(tableTypeName)) {
			return organizStoreCount(tableTypeName);
		}*/ else if ("WebsiteJournalStore".equals(tableTypeName)) {
			return websiteJournalStoreCount(tableTypeName);
		} else if ("VocationalWorkStore".equals(tableTypeName)) {
			return vocationalWorkStoreCount(tableTypeName);
		} else if ("OutsideDocStore".equals(tableTypeName)) {
			return outsideDocStoreCount(tableTypeName);
		} else if ("LeadSpeakStore".equals(tableTypeName)) {
			return leadSpeakStoreCount(tableTypeName);
		} else if ("CertificatesStore".equals(tableTypeName)) {
			return certificatesStoreCount(tableTypeName);
		} else if ("NetworkAccountStore".equals(tableTypeName)) {
			return networkAccountStoreCount(tableTypeName);
		} else if ("InvolvedInTheField".equals(tableTypeName)) {
			return involvedInTheFieldCount(tableTypeName);
		}

		return null;
	}

	// 人员类别统计
	public String personStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(PersonStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}

	// 组织类别统计
	/*public String organizStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(OrganizStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}*/

	// 媒体刊物类别统计
	public String websiteJournalStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(WebsiteJournalStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}

	// 业务文档类别统计
	public String vocationalWorkStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(VocationalWorkStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}

	// 外来文档类别统计
	public String outsideDocStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(OutsideDocStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}

	// 领导讲话类别统计
	public String leadSpeakStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(LeadSpeakStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}

	// 证件类别统计
	public String certificatesStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(CertificatesStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}

	// 网络类别统计
	public String networkAccountStoreCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			DetachedCriteria criteria = DetachedCriteria.forClass(NetworkAccountStore.class);
			criteria.add(Restrictions.eq("infoType.id", infoType.getId()));
			int count = baseDao.getRowCount(criteria);
			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", count);
			array.add(obj);

		}
		return array.toString();
	}

	// 涉及领域类别统计
	public String involvedInTheFieldCount(String tableTypeName) throws Exception {
		// 得到指定类别下的名称集合
		List<InfoType> infoTypes = baseDao.findByProperty(InfoType.class, "tableTypeName", tableTypeName);
		JSONArray array = new JSONArray();
		// 循环查询每个类别的数据总数
		for (InfoType infoType : infoTypes) {
			// 设置查询条件
			// 领导讲话
			DetachedCriteria leadSpeakStorecriteria = DetachedCriteria.forClass(LeadSpeakStore.class);
			leadSpeakStorecriteria.add(Restrictions.eq("involvedInTheField.id", infoType.getId()));
			int leadSpeakStorecount = baseDao.getRowCount(leadSpeakStorecriteria);

			// 业务文档
			DetachedCriteria vocationalWorkStorecriteria = DetachedCriteria.forClass(VocationalWorkStore.class);
			vocationalWorkStorecriteria.add(Restrictions.eq("involvedInTheField.id", infoType.getId()));
			int vocationalWorkStorecount = baseDao.getRowCount(vocationalWorkStorecriteria);

			// 外来文档
			DetachedCriteria outsideDocStorecriteria = DetachedCriteria.forClass(OutsideDocStore.class);
			outsideDocStorecriteria.add(Restrictions.eq("involvedInTheField.id", infoType.getId()));
			int outsideDocStorecriteriacount = baseDao.getRowCount(outsideDocStorecriteria);

			JSONObject obj = new JSONObject();
			obj.put("name", infoType.getTypeName());
			obj.put("data", leadSpeakStorecount + vocationalWorkStorecount + outsideDocStorecriteriacount);
			array.add(obj);

		}
		return array.toString();
	}

	
}
