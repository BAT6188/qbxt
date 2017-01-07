package com.ushine.storeInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.ushine.common.vo.PagingObject;
import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.ClueRelationship;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.TempClueData;
import com.ushine.storeInfo.service.IClueRelationshipService;
import com.ushine.storeInfo.service.IClueStoreService;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.ITempClueDataService;
import com.ushine.util.StringUtil;

/**
 * 线索接口实现测试类
 * 
 * @author wangbailin
 *
 */
@Component("ClueStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class ClueStoreServiceImplTest {

	@Autowired
	private IClueStoreService clueStoreService;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IBaseDao dao;
	@Autowired
	private ITempClueDataService tempClueDataService;
	@Autowired
	private IClueRelationshipService relationshipService;

	/**
	 * private String involvingObjName; //涉及对象名称 private String isInvolvedObj =
	 * "否";//是否涉及对象
	 */
	@Test
	public void saveClueTest() {
		String number = Long.toString(System.currentTimeMillis());
		number = number.substring(number.length() - 4, number.length());
		int value = (int) Long.parseLong(number);
		String[] names = { "走私案","信用卡套现","被杀案","销售假冒产品", "假身份证", "仿制枪", "杀人", "偷卖国宝", 
				"贩卖假药", "打架斗殴", "蓄意伤人案", "抢劫", "金融诈骗" };
		try {
			List<String> personnames = FileUtils.readLines(new File("f://人名.txt"));
			String content = FileUtils.readFileToString(new File("f://11.txt"));
			for (int i = 0; i < value; i++) {
				ClueStore clueStore = new ClueStore();
				String clue=names[(int) (Math.random() * names.length)];
				clueStore.setArrangeAndEvolveCondition(content.substring(2 * i, i * 2 + 1024));
				clueStore.setClueContent(content.substring(3 * i, i * 3 + 512));
				clueStore.setClueName(personnames.get((int) (Math.random() * personnames.size()))+","+
						personnames.get((int) (Math.random() * personnames.size()))+clue);
				clueStore.setDid("40288aac54cce4700154cce4787c0002");
				clueStore.setUid("40288aac54cce4700154cce4787c0002");
				clueStore.setOid("40288aac54cce4700154cce4787c0002");
				clueStore.setFindTime(setDate(1999, 2016));
				clueStore.setCreateDate(setDate(2010, 2016));
				clueStore.setAction("1");
				clueStore.setIsEnable("1");
				clueStore.setClueSource(clue);
				clueStoreService.saveClue(clueStore);
				System.err.println(i);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		StringBuffer buffer=new StringBuffer();
		buffer.append(",,");
		System.err.println(buffer.toString().substring(0, buffer.length()-1));
		/*String value;
		
		buffer.append(value+",");
		System.err.println(buffer.toString());*/
	}

	@Test
	public void findClueRelationShip() {
		try {
			List<ClueRelationship> list=relationshipService.findRelationshipByClueId("40288a625630246501563024766d0004");
			System.err.println(list);
			for (ClueRelationship clueRelationship : list) {
				System.err.println(clueRelationship.getDataType()+"----"+clueRelationship.getLibraryId());
			}
			//ClueStore clueStore = clueStoreService.findClueById("40288aac547929bc01547929c4db0001");
			//System.out.println(clueStore.getArrangeAndEvolveCondition());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

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

	// @Test
	public void findByid() {
		try {
			PagingObject<PersonStore> list = clueStoreService.findCluePersonStore("40288aac54c1ce400154c1d4f864000f",
					"workUnit", "1232", "2016-05-17 15:12:20", "2016-05-21 15:12:20", 0, 2);
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");

			for (PersonStore p : list.getArray()) {
				System.out.println(p.getPersonName());
				System.err.println(p.getInfoType().getTypeName());
			}
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
			System.out.println("----------------------------------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test() throws Exception {
		/*
		 * TempClueData clueData=new TempClueData();
		 * clueData.setAction("246276");
		 * clueData.setDataId("40288aac552eb97c01552edbc30a002f");
		 * clueData.setName("donghao"); //已关联 clueData.setState("1"); //类型
		 * clueData.setType("personStore");
		 * 
		 * String sql = "SELECT COUNT(*) FROM TEMP_CLUE_DATA  WHERE ACTION = '"
		 * +clueData.getAction()+"'  AND NAME = '"+clueData.getName()+
		 * "' AND TYPE = '"+clueData.getType()+"'"; //int count =
		 * Integer.parseInt(dao.getRows(sql).toString()); String result=null;
		 * result=dao.getRows(sql).toString(); if(result!=null){
		 * System.out.println(result); }else{ System.out.println(
		 * "result is null"); }
		 */

		// System.out.println(count);
		TempClueData tempClueData = new TempClueData();
		// 正在操作的人员
		tempClueData.setAction("246276");
		// 关联刚刚新增的这个人员
		tempClueData.setDataId("40288aac552eb97c01552edbc30a002f");
		// System.out.println("====tempClueData.setDataId====="+tempClueData.getDataId());
		// 名称
		tempClueData.setName("donghao");
		// 已关联
		tempClueData.setState("1");
		// 类型
		tempClueData.setType("personStore");
		System.out.println("====是否成功关联对象临时线索人员====" + tempClueDataService.saveTempClueData(tempClueData));
		// 新增临时线索里面
		/*
		 * boolean tempClueResult=false; String sql =
		 * "SELECT COUNT(*) FROM TEMP_CLUE_DATA  WHERE ACTION = '"
		 * +tempClueData.getAction()+ "'  AND NAME = '"+tempClueData.getName()+
		 * "' AND TYPE = '"+tempClueData.getType()+"'" + "AND STATE='"
		 * +tempClueData.getState()+"'"; int count =
		 * Integer.parseInt(dao.getRows(sql).toString());
		 * System.out.println("====临时线索人员新增成功count===="+count);
		 * System.out.println("====临时线索人员新增成功tempClueResult===="+tempClueResult)
		 * ;
		 */

	}
}
