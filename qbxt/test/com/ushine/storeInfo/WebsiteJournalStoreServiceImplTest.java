package com.ushine.storeInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.dao.IBaseDao;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IVocationalWorkStoreService;
import com.ushine.storeInfo.service.IWebsiteJournalStoreService;
import com.ushine.storeInfo.storeFinal.StoreFinal;
import com.ushine.util.StringUtil;
/**
 * 媒体网站刊物接口实现测试类
 * @author wangbailin
 *
 */
@Component("WebsiteJournalStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:/applicationContext.xml","classpath*:/controller-servlet.xml"})  
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class WebsiteJournalStoreServiceImplTest {
	@Autowired
	private IWebsiteJournalStoreService websiteJournalStoreService;
	@Autowired
	private IVocationalWorkStoreService vocationalWorkStoreService;
	@Autowired
	private IInfoTypeService infoTypeService;
	
	@Autowired
	public IBaseDao baseDao2;
	
	@Test
	public void saveWebsitejournalTest(){
		try {
			ApplicationContext context=new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
			IBaseDao baseDao=(IBaseDao) context.getBean("baseDao");
			
			//System.err.println(baseDao.toString());
			String[] infoArr = {
					"40288aac5480504e01548068a791001a",
					"40288aac5480504e01548068c326001b",
					"40288aac5480504e01548068df7f001c",
					"40288aac5480504e01548068ffdc001d"
			};
			String basicCondition=FileUtils.readFileToString(new File("f://水浒传.txt"));
			List<String> addressList=FileUtils.readLines(new File("f://发行地.txt"));
			List<String> names=FileUtils.readLines(new File("f://人名.txt"));
			List<String> publish=FileUtils.readLines(new File("f://期刊.txt"));
			List<String> serverAddress=FileUtils.readLines(new File("f://address.dic"));
			List<String> urList=FileUtils.readLines(new File("f://url.txt"));
			String number = Long.toString(System.currentTimeMillis());
			number=number.substring(number.length() - 4, number.length());
			int value=(int)Long.parseLong(number);
			for (int i = 0; i < value; i++) {
				//InfoType infoType = infoTypeService.findInfoTypeById(infoArr[(int)(Math.random()*infoArr.length)]);
				InfoType  infoType = (InfoType) baseDao.findById(InfoType.class, infoArr[(int)(Math.random()*infoArr.length)]);
				WebsiteJournalStore store = new WebsiteJournalStore();
				//基本情况
				int random=(int) (Math.random()*500);
				store.setBasicCondition(basicCondition.substring(value+random, value+1024+random));
				store.setDid("40288aac552833c701552833cfd90001");
				//发行地
				if (random%3==0) {
					store.setEstablishAddress(addressList.get((int) (Math.random()*addressList.size())));
				}else{
					store.setEstablishAddress(addressList.get((int) (Math.random()*addressList.size()))+","
							+addressList.get((int) (Math.random()*addressList.size())));
				}
				//创办人
				if (random%7==0) {
					store.setEstablishPerson(names.get((int) (Math.random()*names.size()))+","
							+names.get((int) (Math.random()*names.size())));
				}else if(random%5==0){
					store.setEstablishPerson(names.get((int) (Math.random()*names.size()))+","
							+names.get((int) (Math.random()*names.size()))+","
									+names.get((int) (Math.random()*names.size()))+","
											+names.get((int) (Math.random()*names.size())));
				}else{
					store.setEstablishPerson(names.get((int) (Math.random()*names.size())));
				}
				//创办时间
				//随机生成日期
				Random randomDate=new Random();
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar=Calendar.getInstance();
				calendar.set(2009, 1, 1);
				long start=calendar.getTimeInMillis();
				calendar.set(2016, 7, 25);
				long end=calendar.getTimeInMillis();
				Date date=new Date((long) (start+randomDate.nextDouble()*(end-start)));
				store.setEstablishTime(format.format(date));
				store.setCreateDate(format.format(date));
				//类型
				store.setInfoType(infoType);
				//主要发行地
				if (random%3==0) {
					store.setMainWholesaleAddress(addressList.get((int) (Math.random()*addressList.size())));
				}else if(random%11==0){
					store.setMainWholesaleAddress(addressList.get((int) (Math.random()*addressList.size()))+","
							+addressList.get((int) (Math.random()*addressList.size())));
				}else {
					store.setMainWholesaleAddress(addressList.get((int) (Math.random()*addressList.size()))+","
							+addressList.get((int) (Math.random()*addressList.size()))+","
							+addressList.get((int) (Math.random()*addressList.size())));
				}
				//名称
				store.setName(publish.get((int) (Math.random()*publish.size())));
				store.setOid("40288aac552833c701552833cfd90001");
				//服务器
				store.setServerAddress(serverAddress.get((int) (Math.random()*serverAddress.size())));
				store.setUid("40288aac552833c701552833cfd90001");
				store.setAction("1");
				//域名
				if (random%19==0) {
					store.setWebsiteURL("");
				}else{
					store.setWebsiteURL(urList.get((int) (Math.random()*urList.size())));
				}
				
				baseDao.save(store);
				//websiteJournalStoreService.saveWebsitejournal(store);
				Thread.sleep(1);
				System.out.println(i);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	public String readText(int i){
		BufferedReader br = null;
		StringBuffer buffer = new StringBuffer();
		String str ="";
		try {
			String htmlName = "F:\\10万条数据\\"+i+".txt";
			br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlName), Charset.forName("utf-8")));
			int count = 0;
			char[] datas = new char[10240];
			while ((count = br.read(datas)) > 0) {
				str = String.valueOf(datas, 0, count);
				//System.out.println("=====读取每个字节的内容=====" + str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭读流
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return str;
	}

	@Test
	public void testInsert() throws Exception {
		long start = System.currentTimeMillis();
		String[] content = new String[10];
		String []docName={"毛泽东思想","邓小平理论","习主席讲话","十二局技侦信息报告","十三五计划","刑侦大队通知",
				"奥巴马访华演讲","蔡英文就职宣誓","十一届三中全会","参观延安根据地讲话"};
		
		String []time={"2016-07-01","2016-07-02","2016-07-03","2016-07-04","2016-07-05","2016-07-06",
				"2015-07-01","2015-07-02","2015-06-01","2014-07-01"};
		
		String []type={"通知","函","公安技侦情报","十二局","研判会商材料","互联网情报","请示","报告","公安技侦情报","通知"};
		
		String []field={"民族领域","无","网络领域","邪教组织领域","集会结社","无",
				"网络领域","民族领域","集会结社","邪教组织领域"};
		//10万条 000
		for(int i=0;i<content.length;i++){
			for(int j=0;j<1000;j++){
				int a1=(int) (Math.random()*10);
				int a2=(int) (Math.random()*10);
				int a3=(int) (Math.random()*10);
				int a4=(int) (Math.random()*10);
				int a5=(int) (Math.random()*10);
				int a6=(int) (Math.random()*10);
				int a7=(int) (Math.random()*10);
				VocationalWorkStore store=new VocationalWorkStore();
				store.setUid("40288aac552833c701552833cff30002");
				store.setDid("40288aac552833c701552833cff30002");
				store.setOid("40288aac552833c701552833cff30002");
				store.setAction("1");
				store.setDocName(docName[a1]);
				store.setTime(time[a2]);
				//store.setCreateDate(new Date().toString());
				store.setDocNumber(a3+"000"+a4);
				store.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName(type[a7], StoreFinal.VOCATIONAL_WORK_STORE));
				store.setInvolvedInTheField(infoTypeService.findInfoTypeByTypeNameAndTableName(field[a6], StoreFinal.INVOLVED_IN_THE_FIELD));
				store.setTheOriginal(readText(a5));
				vocationalWorkStoreService.saveVocationalWork(store);
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("====使用时间共多少毫秒====" + (end - start));
		System.out.println("====使用时间共多少秒====" + (end - start) / 1000);
	}
	
	//@Test
	public void findWebsiteJouByIdTest(){
		WebsiteJournalStore store = null;
		try {
			store = websiteJournalStoreService.findWebsiteJouById("40288aac547a43a501547a43ac550001");
			System.out.println(store.getBasicCondition());
			System.out.println(store.getInfoType().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test 
	//条件查询
	public void findByConditions() throws Exception{
		String result=websiteJournalStoreService.findWebsiteJournalStore("websiteURL", "百度", "2001-01-01", 
				"2015-12-12", 0, 100, null, null, null, null, null);
		System.out.println(result);
	}
	//@Test
	public void delete() throws Exception{
			String[] websiteJournalStoreIds=new String[]{"ff80818154b40e890154b4118d9d0002","ff80818154b40e890154b413510c0003"};
			websiteJournalStoreService.delWebsiteJournalStoreByIds(websiteJournalStoreIds);
	}
	//@Test
	public void test() throws Exception{
		//保存
		VocationalWorkStore store=new VocationalWorkStore();
		store.setAction("1");
		store.setCreateDate(StringUtil.dates());
		store.setDocName("测试");
		store.setDocNumber("10000");
		store.setTime("2012-01-02");
		//设置类别
		store.setInfoType(infoTypeService.findInfoTypeByTypeNameAndTableName("十二局", StoreFinal.VOCATIONALWORK_STORE));
		store.setTheOriginal("&#20064;&#36817;&#24179;&#22312;&#21704;&#33832;&#20811;&#26031");
		vocationalWorkStoreService.saveVocationalWork(store);
		//vocationalWorkStoreService.identifyServiceDoc("D:\\Users\\dh\\Desktop\\jqueryprint", "十二局");
		//>&#20064;&#36817;&#24179;&#22312;&#21704;&#33832;&#20811;&#26031;
	}
}
