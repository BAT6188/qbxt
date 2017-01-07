package com.ushine.storeInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.TempClueData;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IOrganizStoreService;

/**
 * 组织库接口实现测试类
 * 
 * @author wangbailin
 *
 */
@Component("OrganizStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class OrganizStoreServiceImplTest {

	@Autowired
	private IOrganizStoreService organizStoreService;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IBaseDao baseDao;
	private static String setDate(int startYear, int endYear) {
		// 随机生成日期
		Random randomDate = new Random();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(startYear, 1, 1);
		long start = calendar.getTimeInMillis();
		calendar.set(endYear, 7, 21);
		long end = calendar.getTimeInMillis();
		Date date = new Date((long) (start + randomDate.nextDouble() * (end - start)));
		return format.format(date);
	}
	@Test
	public void saveOrganizStoreTest() {
		try {
			String[] infoArr = { "40288aac5480504e015480675d960013", "40288aac5480504e0154806791dd0014",
					"40288aac5480504e01548067b28d0015", "40288aac5480504e01548067d79e0016",
					"40288aac5480504e01548067fe050017", "40288aac5480504e0154806846530018" };
			
			String number = Long.toString(System.currentTimeMillis());
			number=number.substring(number.length() - 4, number.length());
			int value=(int) Long.parseLong(number);
			String content=FileUtils.readFileToString(new File("f://11.txt"));
			List<String> names=FileUtils.readLines(new File("f://组织.txt"));
			List<String> url=FileUtils.readLines(new File("f://url.txt"));
			List<String> personnames=FileUtils.readLines(new File("f://人名.txt"));
			List<String> publish=FileUtils.readLines(new File("f://期刊.txt"));
			List<String> address=FileUtils.readLines(new File("f:/address.dic"));
			for (int i = 0; i < value; i++) {
				InfoType infoType = infoTypeService.findInfoTypeById(infoArr[(int) (Math.random() * infoArr.length)]);
				OrganizStore store = new OrganizStore();
				int a=(int) (Math.random()*100);
				int b=(int) (Math.random()*100);
				store.setActivityCondition(content.substring(value+a, value+a+2014));
				store.setBasicCondition(content.substring(value+b, value+b+1024));
				
				store.setDegreeOfLatitude(address.get((int) (Math.random()*address.size())));
				store.setFoundTime(setDate(1980, 2016));
				store.setCreateDate(setDate(2015, 2016));
				store.setInfoType(infoType);
				if(a>50){
					store.setWebsiteURL(url.get((int) (Math.random()*url.size())));
					store.setOrganizBranchesNames(names.get((int) (Math.random()*names.size())));
					store.setOrganizPersonNames(personnames.get((int) (Math.random()*personnames.size()))+","
							+personnames.get((int) (Math.random()*personnames.size())));
				}else{
					store.setWebsiteURL("");
					store.setOrganizBranchesNames(names.get((int) (Math.random()*names.size()))+","
							+names.get((int) (Math.random()*names.size())));
				}
				if(b>50){
					store.setOrganizPublicActionNames(publish.get((int) (Math.random()*publish.size())));
					store.setOrganizPersonNames(personnames.get((int) (Math.random()*personnames.size())));
				}else{
					store.setOrganizPublicActionNames("");
				}
				store.setOrgHeadOfName(personnames.get((int) (Math.random()*personnames.size()))+","
						+personnames.get((int) (Math.random()*personnames.size())));
				
				store.setAction("1");
				store.setOrganizName(names.get((int) (Math.random()*names.size())));
				store.setUid("40288aac54cce4700154cce4787c0002");
				store.setDid("40288aac54cce4700154cce4787c0002");
				store.setOid("40288aac54cce4700154cce4787c0002");
				organizStoreService.saveOrganizStore(store);
				System.out.println(i);
				Thread.sleep(5);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// @Test
	public void findOrganizStoreByIdTest() {
		try {
			/*
			 * OrganizStore organizStore =
			 * organizStoreService.findOrganizStoreById(
			 * "40288aac54ccf40a0154ccfc073f0001");
			 * //System.out.println(organizStore.toString());
			 * System.out.println(organizStore.getActivityCondition());
			 * System.out.println(organizStore.getInfoType().toString());
			 * System.out.println("-----------组织分支机构-------------");
			 * System.out.println(organizStore.getOrganizBranches().size());
			 * System.out.println("-----------组织下属刊物-------------");
			 * System.out.println(organizStore.getOrganizPublicActions().size())
			 * ; System.out.println("-----------下属成员-------------");
			 * System.out.println(organizStore.getOrganizPersons().size());
			 */
			// PagingObject<OrganizStore> pagingObject =
			// organizStoreService.findOrganizStoreByIsEnable(0, 100);
			// JSONObject root = new JSONObject();
			// root.element("paging", pagingObject.getPaging());
			// JSONArray array = new JSONArray();
			// for (OrganizStore o : pagingObject.getArray()) {
			// JSONObject obj = new JSONObject();
			// obj.put("id", o.getId());
			// obj.put("name", o.getOrganizName());
			// obj.put("type", o.getInfoType().getTypeName());
			// array.add(obj);
			// }
			// root.element("datas", array);
			// System.out.println(root.toString());
			// return organizStoreVoToJSon(pagingObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() throws Exception {
		TempClueData clueData = new TempClueData();
		/// List<TempClueData> list=baseDao.findAll(TempClueData.class);
		// System.out.println(list.size());
		/*
		 * clueData.setAction("91872");
		 * clueData.setDataId("40288a625529686d01552983d0a50005");
		 * clueData.setName("法轮功会"); clueData.setState("0");
		 * clueData.setType("organizStore");
		 * //新增前要根据线索id和基础库id和类别判断是否在数据库中存在，如果存在就不新增，反之亦然 String sql =
		 * "SELECT COUNT(*) FROM TEMP_CLUE_DATA  WHERE ACTION = '"
		 * +clueData.getAction()+"' AND DATA_ID = '"+clueData.getDataId()+
		 * "' AND NAME = '"+clueData.getName()+"' AND TYPE = '"
		 * +clueData.getType()+"'"; int count =
		 * Integer.parseInt(baseDao.getRows(sql).toString());
		 * System.out.println(count); if(count <= 0){ baseDao.save(clueData); }
		 */
		organizStoreService.findOrganizStore("", "", "1900-01-01", "2016-07-15", 1, 50, null, null, null,null,null);
	}
	
	
}
