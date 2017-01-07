package com.ushine.storeInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IOutsideDocStoreService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.storeInfo.storeFinal.StoreFinal;

/**
 * 测试OutsideDocStoreServiceImpl
 * @author dh
 *
 */
@Component("OutsideDocStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class OutsideDocStoreServiceImplTest {
	@Autowired
	private IOutsideDocStoreService outsideDocStoreService;
	@Autowired
	private IInfoTypeService infoTypeService;
	@Autowired
	private IPersonStoreService personStoreService;
	//测试新增
	private static String setDate(int startYear, int endYear) {
		// 创办时间
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
	public void save() throws Exception{
		String []ids={"40288aac5480504e0154806bb9550027","40288aac5480504e0154806bd6640028",
				"40288aac5480504e0154806c2e070029"};
		String []secrets={"无","秘密","机密","绝密"};
		
		String []first={"中央","二局","一局","三局","四局","十局","五局","六局","七局","八局","九局","十一局","十二局"};
		
		String []second={"二处","一处","三处","四处","十处","五处","六处","七处","八处","九处"};
		
		String []publicition={"计算机科学","石油化工","中国机械工程","中国粉体技术","现代制造技术与装备","混凝土","财务与会计","国际商务协会","计算机学报",
				"国土资源管理","GIS系统导论","社会科学研究","浙江社会科学","江西社会科学","东南学术","山东社会科学","河南师范学报","湖北大学学吧","暨南学报","世界宗教研究",
				"中国宗教","中国统计","人口研究","人口学刊","人口与发展","人口与经济","管理学报","领导科学","管理工程学报","民族研究","世界民族","回族研究"};
		
		String[] field = { "40288a6255bdaa4b0155bdaadd1e0001", "40288a6255cd90cd0155cd9217db0001",
				"40288a6255cd90cd0155cdfb9c710002", "40288a6255cd90cd0155cdfbcf0e0003" };
		String content=FileUtils.readFileToString(new File("f://11.txt"));
		
		String number = Long.toString(System.currentTimeMillis());
		number=number.substring(number.length() - 4, number.length());
		Long value=Long.parseLong(number);
		for (int i = 0; i <value; i++) {
			OutsideDocStore store=new OutsideDocStore();
			store.setAction("1");
			//来源
			/*store.setSourceUnit(first[(int) (Math.random()*first.length)]+second[(int) (Math.random()*second.length)]);
			//随机生成日期
			//时间
			store.setTime(setDate(1990, 2016));
			store.setCreateDate(setDate(2013, 2016));
			store.setTitle(first[(int) (Math.random()*first.length)]+publicition[(int) (Math.random()*publicition.length)]);
			//涉密
			store.setSecretRank(secrets[(int) (Math.random()*secrets.length)]);*/
			//类别
			store.setInfoType(infoTypeService.findInfoTypeById(ids[(int) (Math.random()*ids.length)]));
			//领域
			store.setInvolvedInTheField(infoTypeService.findInfoTypeById(field[(int)(Math.random()*field.length)]));
			//名称
			store.setName(publicition[(int) (Math.random()*publicition.length)]);
			store.setOid("40288aac54cce4700154cce4787c0002");
			store.setUid("40288aac54cce4700154cce4787c0002");
			store.setDid("40288aac54cce4700154cce4787c0002");
			int index=(int) (Math.random()*1000);
			store.setCentent(content.substring(index, index+2048));
			outsideDocStoreService.saveOutsideDocStore(store);
			System.out.println(i);
			Thread.sleep(5);
		}
	}
	
	@Test
	public void test()throws Exception{
		//随机生成日期
		Random random=new Random();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		calendar.set(1971, 1, 1);
		long start=calendar.getTimeInMillis();
		calendar.set(2016, 7, 12);
		long end=calendar.getTimeInMillis();
		for (int i = 0; i < 10; i++) {
			Date date=new Date((long) (start+random.nextDouble()*(end-start)));
			System.out.println(format.format(date));
		}
	}
	//@Test
	//测试删除
	public void delete() throws Exception{
		String[] outsideDocStoreIds=new String[]{"40288a6254bd1fe70154bd1feeed0001",
				"40288a6254bd1fe70154bd1feef40002"};
		outsideDocStoreService.delOutsideDocStore(outsideDocStoreIds);
	}
	
	//@Test
	//测试查询
	public void find()throws Exception{
		String result=outsideDocStoreService.findOutsideDocStore("infoType", "中央维稳办", "1901-10-10", "2016-05-16", 
				1, 100, null, null, null,null,null);
		System.out.println(result);
	}
	
	@Test
	public void update()throws Exception{
		OutsideDocStore outsideDocStore=outsideDocStoreService.findOutsideDocStoreById("40288a6254bd1fe70154bd1feeed0001");
		/*outsideDocStore.setSourceUnit("上海交通大学");
		outsideDocStore.setTime("2015-01-31");
		outsideDocStore.setTitle("上海交通大学");
		outsideDocStore.setSecretRank("机密");*/
		outsideDocStore.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName(
				"中央维稳办", StoreFinal.OUTSIDEDOC_STORE));
		outsideDocStore.setName("上海交通大学");
		outsideDocStore.setCentent("上海交通大学");
		outsideDocStore.setAction("2");
		outsideDocStoreService.updateOutsideDocStore(outsideDocStore);
	}
	
	
}
