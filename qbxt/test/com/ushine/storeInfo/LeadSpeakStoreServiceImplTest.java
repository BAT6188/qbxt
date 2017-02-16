package com.ushine.storeInfo;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ushine.dao.IBaseDao;
import com.ushine.store.index.ClueStoreNRTSearch;
import com.ushine.store.index.IStoreNRTSearch;
import com.ushine.store.index.LeadSpeakStoreNRTSearch;
import com.ushine.store.index.PersonStoreNRTSearch;
import com.ushine.store.index.StoreIndexQuery;
import com.ushine.store.vo.MyComparator;
import com.ushine.store.vo.MyJsonObject;
import com.ushine.storeInfo.model.CertificatesStore;
import com.ushine.storeInfo.model.ClueStore;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.model.LeadSpeakStore;
import com.ushine.storeInfo.model.NetworkAccountStore;
import com.ushine.storeInfo.model.OrganizStore;
import com.ushine.storeInfo.model.OutsideDocStore;
import com.ushine.storeInfo.model.PersonStore;
import com.ushine.storeInfo.model.VocationalWorkStore;
import com.ushine.storeInfo.model.WebsiteJournalStore;
import com.ushine.storeInfo.service.ICertificatesStoreService;
import com.ushine.storeInfo.service.IClueStoreService;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.ILeadSpeakStoreService;
import com.ushine.storeInfo.service.INetworkAccountStoreService;
import com.ushine.storeInfo.service.IOrganizStoreService;
import com.ushine.storeInfo.service.IOutsideDocStoreService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.storeInfo.service.IVocationalWorkStoreService;
import com.ushine.storeInfo.service.IWebsiteJournalStoreService;
import com.ushine.storeInfo.storefinal.StoreFinal;
import com.ushine.util.CustomXWPFDocument;
import com.ushine.util.ReadAttachUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

@Component("LeadSpeakStoreServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class LeadSpeakStoreServiceImplTest {
	@Autowired private IInfoTypeService infoTypeService;
	@Autowired private ILeadSpeakStoreService leadSpeakStoreService;
	@Autowired private IVocationalWorkStoreService vocationalWorkStoreService;
	@Autowired private IOutsideDocStoreService outsideDocStoreService;
	@Autowired private IWebsiteJournalStoreService websiteJournalStoreService;
	@Autowired private IOrganizStoreService organizStoreService;
	@Autowired private IClueStoreService clueStoreService;
	@Autowired private IPersonStoreService personStoreService;
	@Autowired private ICertificatesStoreService certificatesStoreService;
	@Autowired private INetworkAccountStoreService networkAccountStoreService;
	
	@Autowired private IBaseDao baseDao;
	
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
	public  void testInsertVocationalWorkStore() {
		try {
			//ApplicationContext context = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
			//IBaseDao baseDao = (IBaseDao) context.getBean("baseDao");

			List<String> nameList = FileUtils.readLines(new File("f://业务文档.txt"));
			String content = FileUtils.readFileToString(new File("f://射雕英雄传.txt"));
			String[] fileds = { "40288a6255bdaa4b0155bdaadd1e0001", "40288a6255cd90cd0155cd9217db0001",
					"40288a6255cd90cd0155cdfb9c710002", "40288a6255cd90cd0155cdfbcf0e0003" };
			String[] infoTypes = { "40288aac5480504e01548069476e001e", "40288aac5480504e015480696398001f",
					"40288aac5480504e015480697e600020", "40288aac5480504e01548069994e0021",
					"40288aac5480504e0154806a6d2f0022", "40288aac5480504e0154806a995f0023",
					"40288aac5480504e0154806acb8f0024", "40288aac5480504e0154806b0dc80025",
					"40288aac5480504e0154806b78bc0026" };
			String[] chars={"a","b","e","g","s","h"};
			// 添加业务文档
			for (int i = 0; i < 10000; i++) {
				VocationalWorkStore store = new VocationalWorkStore();
				store.setAction("1");
				// 名称
				store.setDocName(nameList.get((int) (Math.random() * nameList.size())));
				// 涉及领域
				store.setInvolvedInTheField(infoTypeService.findInfoTypeById(fileds[(int) (Math.random() * fileds.length)]));
				// 类型
				store.setInfoType(infoTypeService.findInfoTypeById(infoTypes[(int) (Math.random() * infoTypes.length)]));
				String docNumber = Long.toString(System.currentTimeMillis());
				docNumber = chars[(int) (Math.random()*chars.length)].toUpperCase()+docNumber.substring(docNumber.length() - 4, docNumber.length());
				// 原文
				int number = (int) (Math.random() * 1000);
				store.setTheOriginal(content.substring(number, number + 2048));
				// 刊物号
				store.setDocNumber(docNumber);
				store.setTime(setDate(1980, 2016));
				// 数据创建日期
				store.setCreateDate(setDate(2012, 2016));
				store.setDid("40288aac54cce4700154cce4787c0002");
				store.setUid("40288aac54cce4700154cce4787c0002");
				store.setOid("40288aac54cce4700154cce4787c0002");
				vocationalWorkStoreService.saveVocationalWork(store);
				 //baseDao.save(store);
				 System.err.println(i);
				Thread.sleep(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//测试新增
	@Test
	public void save() throws Exception{
		String[] infoArr = {
				"40288aac5480504e0154806d73a5002f",
				"40288aac5480504e0154806d940d0030",
				"40288aac5480504e0154806db2ae0031"};
		String []secrets={"无","秘密","机密","绝密"};
		String []field={"40288a6255cd90cd0155cdfbfeed0004",
				"40288a6255cd90cd0155cd9217db0001",
		"40288a6255cd90cd0155cdfb9c710002","40288a6255cd90cd0155cdfbcf0e0003"};
		
		String []numbers={"1","2","3","4","5","6","7","8","9","10","11","12","13"};
		
		String []first={"中央","二局","一局","三局","四局","十局","五局","六局","七局","八局","九局","十一局","十二局"};
		
		String []type={"讲话","会议","报告","通知","公告"};
		
		List<String> nameList = FileUtils.readLines(new File("f://业务文档.txt"));
		
		String content=FileUtils.readFileToString(new File("f://11.txt"));
		//1200000-1040002
		for (int i = 0; i < (2000); i++) {
			//随机生成日期
			Random random=new Random();
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar=Calendar.getInstance();
			calendar.set(1971, 1, 1);
			long start=calendar.getTimeInMillis();
			calendar.set(2016, 7, 12);
			long end=calendar.getTimeInMillis();
			Date date=new Date((long) (start+random.nextDouble()*(end-start)));
			
			LeadSpeakStore leadSpeakStore=new LeadSpeakStore();
			leadSpeakStore.setAction("1");
			int index=(int) (Math.random()*100);
			leadSpeakStore.setCentent(content.substring(index+i, index+i+2048));
			leadSpeakStore.setInfoType(infoTypeService.findInfoTypeById(infoArr[(int)(Math.random()*infoArr.length)]));
			leadSpeakStore.setTime(format.format(date));
			leadSpeakStore.setMeetingName(numbers[(int) (Math.random()*numbers.length)]
					+"·"+numbers[(int) (Math.random()*numbers.length)]+type[(int) (Math.random()*type.length)]);
			leadSpeakStore.setTitle(nameList.get((int) (Math.random()*nameList.size())));
			leadSpeakStore.setSecretRank(secrets[(int) (Math.random()*secrets.length)]);
			leadSpeakStore.setUid("40288aac54cce4700154cce4787c0002");
			leadSpeakStore.setDid("40288aac54cce4700154cce4787c0002");
			leadSpeakStore.setOid("40288aac54cce4700154cce4787c0002");
			leadSpeakStore.setCreateDate(setDate(2010, 2015));
			leadSpeakStore.setInvolvedInTheField(infoTypeService.findInfoTypeById(field[(int)(Math.random()*field.length)]));
			leadSpeakStoreService.saveLeadSpeakStore(leadSpeakStore);
			Thread.sleep(5);
			System.out.println(i);
		}
	}
	//@Test
	//删除
	public void del() throws Exception{
		String [] ids=new String[]{"40288a6254c1962c0154c19634d10001",
				"40288a6254c197020154c1970a6f0001"};
		leadSpeakStoreService.delLeadSpeakStore(ids);
	}
	
	@Test
	public void createAllStoreIndex()throws Exception{
		
		
		PersonStoreNRTSearch personStoreNRTSearch=PersonStoreNRTSearch.getInstance();
		personStoreNRTSearch.createIndex(baseDao.findAll(PersonStore.class));
		/*
		VocationalWorkStoreNRTSearch vocationalWorkStoreNRTSearch=VocationalWorkStoreNRTSearch.getInstance();
		vocationalWorkStoreNRTSearch.createIndex(baseDao.findAll(VocationalWorkStore.class));
		OrganizStoreNRTSearch organizStoreNRTSearch=OrganizStoreNRTSearch.getInstance();
		organizStoreNRTSearch.createIndex(baseDao.findAll(OrganizStore.class));
		WebsiteJournalStoreNRTSearch websiteJournalStoreNRTSearch=WebsiteJournalStoreNRTSearch.getInstance();
		websiteJournalStoreNRTSearch.createIndex(baseDao.findAll(WebsiteJournalStore.class));
		ClueStoreNRTSearch clueStoreNRTSearch=ClueStoreNRTSearch.getInstance();
		clueStoreNRTSearch.createIndex(baseDao.findAll(ClueStore.class));
		
		OutsideDocStoreNRTSearch outsideDocStoreNRTSearch=OutsideDocStoreNRTSearch.getInstance();
		outsideDocStoreNRTSearch.createIndex(baseDao.findAll(OutsideDocStore.class));
		LeadSpeakStoreNRTSearch leadSpeakStoreNRTSearch=LeadSpeakStoreNRTSearch.getInstance();
		leadSpeakStoreNRTSearch.createIndex(baseDao.findAll(LeadSpeakStore.class));*/
	}
	
	@Test
	public void find()throws Exception{
		/*LeadSpeakStoreNRTSearch leadSpeakStoreNRTSearch=LeadSpeakStoreNRTSearch.getInstance();
		PhraseQuery query=StoreIndexQuery.getPhraseQuery("involvedInTheField", "其他领域");
		//Query query=MultiFieldQueryParser.parse(Version.LUCENE_35, new String[]{"机密"}, new String[]{"secretRank"}, new IKAnalyzer(true));
		System.err.println(documents.size());
		List<Document> documents=leadSpeakStoreNRTSearch.getSearcher(query, null, 1, Integer.MAX_VALUE);*/
		DetachedCriteria criteria = DetachedCriteria.forClass(LeadSpeakStore.class);
		// 查询aciton不为3的
		criteria.add(Restrictions.ne("action", "3"));
		criteria.createAlias("involvedInTheField", "i").add(Restrictions.eq("i.typeName", "其他领域"));
		List<LeadSpeakStore> list=baseDao.findByCriteria(criteria);
		System.err.println("=====所属领域数量====="+list.size());
		String []ids=new String[list.size()];
		List<LeadSpeakStore> newList=new ArrayList<>();
		int i=0;
		for (LeadSpeakStore leadSpeakStore : list) {
			ids[i]=leadSpeakStore.getId();
			i++;
			leadSpeakStore.setInvolvedInTheField(null);
			newList.add(leadSpeakStore);
		}
	}
	
	
	//查询账号
	@Test
	public void testPersonStoreCounts()throws Exception{
		/*IBaseDao baseDao=(IBaseDao) SpringUtils.getBean("baseDao");
		
		String sql="SELECT CERTIFICATES_NUMBER from "
				+ "t_certificates_store where PERSON_STORE_ID ='40288a62568c994b01568c9e765b0005'";
		List<String> list=baseDao.findBySql(sql);
		System.err.println(list.toString());*/
		//左外连接查询
		PersonStore personStore=(PersonStore) baseDao.findById(PersonStore.class, "40288a62568ca24c01568caf35450001");
		String hql="select c from PersonStore p left join  p.certificatesStores c where p.id='40288a625624b632015624b643a10001'";
		hql="select n from PersonStore p left join  p.networkAccountStores n where p.id='40288a62568ca24c01568caf35450001'";
		List<NetworkAccountStore> list=baseDao.findByHql(hql);
		//list肯定都不为空,NetworkAccountStore可能为空
		for (NetworkAccountStore networkAccountStore : list) {
			if (null!=networkAccountStore) {
				System.err.println(networkAccountStore.getNetworkNumber());
			}
		}	
		//hql="from CertificatesStore c left join c.personStore p where c"
		/*List<PersonStore> list=baseDao.findByHql(hql);
		for (PersonStore store : list) {
			Set<CertificatesStore> set=store.getCertificatesStores();
			for (CertificatesStore certificatesStore : set) {
				System.err.println(certificatesStore.getCertificatesNumber());
			}
		}*/
	}
	@Test
	public void testCreateIndex()throws Exception{
		ClueStoreNRTSearch clueStoreNRTSearch=ClueStoreNRTSearch.getInstance();
		DetachedCriteria criteria=DetachedCriteria.forClass(ClueStore.class).add(Restrictions.ne("action", "3"));
		int totalCount=baseDao.findByCriteria(criteria).size();
		System.err.println("=====数据总量====="+totalCount);
		int pageCount=100;
		int totalPage=(totalCount+pageCount-1)/pageCount;
		System.err.println("=====数据总页数====="+totalPage);
		for(int i=0;i<totalPage;i++){
			System.err.println("=====创建第"+(i+1)+"页索引=====");
			@SuppressWarnings("unchecked")
			List<ClueStore> list=baseDao.findPagingByCriteria(criteria, pageCount, i*pageCount);
			if (i==0) {
				clueStoreNRTSearch.createIndex(list);
			}else{
				clueStoreNRTSearch.addIndex(list);
			}
		}
	}
	//替换所有的html标签元素
	@Test
	public void testReadExcel()throws Exception{
		//value=string.replace(/<[^>]+>/g,"");
		System.err.println(ReadAttachUtil.readContent("E:\\测试.xlsx").replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", " ")
				.replaceAll("</[a-zA-Z]+[1-9]?>", " ").replaceAll("&nbsp;", " "));
	}
	
	@Test
	public void testHavePerson()throws Exception{
		
		/*String hql="select count(id) from PersonStore where personName='' and action <>'3'";
		hql="select count(id) from InfoType where typeName='donghao' and tableTypeName='"+StoreFinal.PERSON_STORE+"'";
		List<Integer> list=baseDao.findByHql(hql);
		System.err.println(list.get(0));*/
		
		Map<String, String> map=new HashMap<>();
		for (int i = 0; i < 2000; i++) {
			/*String sql=" select count(id) from t_info_type where TYPE_NAME='邪教33' and TABLE_TYPE_NAME='PersonStore'";
			System.err.println(baseDao.getRows(sql));*/
			/*
			System.err.println("第"+i+"条canBeSaved:"+personStoreService.canBeSaved("董昊", Double.toString(Math.random()*2016)
					,  "1989-11-09", "男"));*/
			/*String hql="select count(id) from PersonStore where personName='董昊' and action <>'3'";
			Object object=baseDao.getRows(hql);
			System.err.println(Integer.parseInt(object.toString()));*/
			
			System.err.println("第"+i+"条canBeSaved:"+personStoreService.canBeSaved("董昊", Double.toString(Math.random()*2016)
					,  "1989-11-09", "男"));
		}
	}
	//账号
	@Test
	public void testCountStore()throws Exception{
		CertificatesStore store=new CertificatesStore();
		InfoType infoType=infoTypeService.findInfoTypeByTypeNameAndTableName("电子邮箱", StoreFinal.NETWORK_ACCOUNT_STORE);
		System.err.println(infoType.getTypeName());
	}
	
	@Test
	public void testXmlReader()throws Exception{
		//String result=CreatAndReadExcel.readPersonInfoExcel(new File("C:\\Users\\dh\\Desktop\\人员excel\\20160830130802.xlsx"));
		//System.err.println(result);
		//String filePath=PathUtils.getConfigPath(LeadSpeakStoreServiceImplTest.class)+"person-store-excel.xml";
		//XmlUtils xmlUtils=new XmlUtils(filePath);
		//Element element=xmlUtils.getNode("person-store-excel");
		//System.err.println(element.getName());
		//获得单个节点
		/*Element element=xmlUtils.getNode("counts");
		List<Element> elements=element.elements();
		for (Element element2 : elements) {
			System.err.println(xmlUtils.getNodeAttrVal(element2, "name")+"===="+element2.getText());
		}*/
		
		/*Map<String, String> countsMap=getPropertiesByHeader("networkaccount");
		System.err.println(infoTypeService.findInfoTypeByTypeNameAndTableName(countsMap.get("email"), 
				StoreFinal.NETWORK_ACCOUNT_STORE).toString());*/
	}
	@Test
	public void testLucene()throws Exception{
		PersonStoreNRTSearch search=PersonStoreNRTSearch.getInstance();
		Query query=MultiFieldQueryParser.parse(Version.LUCENE_35, new String[]{"董昊"}, new String[]{"personName"}, new IKAnalyzer(true));
		System.err.println(search.getCount(query, null));
	}
	/*private Map getPropertiesByHeader(String nodeName) {
		Map<String, String> countsMap=new LinkedHashMap<>();
		String filePath=PathUtils.getConfigPath(LeadSpeakStoreServiceImplTest.class)+"person-store-excel.xml";
		XmlUtilsTest xmlUtils=new XmlUtilsTest(filePath);
		//获得单个节点
		Element element=xmlUtils.getNode(nodeName);
		List<Element> elements=element.elements();
		for (Element element2 : elements) {
			countsMap.put(xmlUtils.getNodeAttrVal(element2, "name"),element2.getText());
		}
		return countsMap;
	}*/
	
	@Test
	public void testLeftJoin() {
		try {
			int count=(int) (Math.random()*10000);
			for (int i = 0; i < 10000; i++) {
				//使用左外连接查询获得懒加载的数据
				String hql="select n from PersonStore p left join  p.networkAccountStores n where p.id='40288a9d56fe45980156fe466e190006'";
				List<NetworkAccountStore> list=baseDao.findByHql(hql);
				for (NetworkAccountStore networkAccountStore : list) {
					//list肯定都不为空,NetworkAccountStore可能为空
					if (null!=networkAccountStore) {
						System.err.println(networkAccountStore.getNetworkNumber());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testIdentify()throws Exception{
		//SmbFileUtils utils=new SmbFileUtils("192.168.183.128");
		//utils.copySmbFilesToDir("技侦情报", "c://testcopy");
		//vocationalWorkStoreService.identifyServiceDoc("c://testcopy", "技侦情报");
		String sql=String.format("SELECT COUNT(ID) FROM t_vocationalwork_store WHERE FILENAME='%s' AND ACTION<>'3'", "dongxu");
		Integer count=Integer.parseInt(baseDao.getRows(sql).toString());
		System.err.println(count);
	}
	
	//是否保存了文档
	@Test
	public void hasSaved()throws Exception{
		//String result="c://testcopy//20160912donghao";
		//
		//System.err.println(FilenameUtils.getName(result).substring(8));
		File dir=new File("C:\\Users\\dh\\Desktop\\测试上传\\十二局信息 (192.168.183.128)");
		File[] files=dir.listFiles();
		int i=0;
		for (File file : files) {
			if(vocationalWorkStoreService.hasSavedStore(FilenameUtils.getBaseName(file.getName()))){
				i++;
				System.err.println(file.getName()+"文档已经保存");
			}
		}
		System.err.println("文档保存数量："+i);
	}
	
	
	@Autowired
	private IBaseDao<CertificatesStore, Serializable> cBaseDao;
	
	//测试获得人员账号
	@Test
	public void testCount()throws Exception{
		personStoreService.outputPersonStoreToExcel("g://输出.xls", new String[]{"40288a625624b632015624b643a10001",
				"40288a625624b632015624b644cb0002","40288a625624b632015624b6459b0003"});
	}
	//测试lucene排序
	@Test
	public void testSort()throws Exception{
		IStoreNRTSearch nrtSearch=LeadSpeakStoreNRTSearch.getInstance();
		Query query=MultiFieldQueryParser.parse(Version.LUCENE_35,  new String[]{"绝密"}, new String[]{"secretRank"}, new IKAnalyzer(true));
		List<Document> list=nrtSearch.getDocuments(query, null, "donghao",null, 1, 50);
		for (Document document : list) {
			System.err.println("createDate："+document.get("createDate")+"，time："+document.get("time"));
		}
	}
	
	
	//倒序输出
	@SuppressWarnings("unchecked")
	@Test
	public void testJsonSort()throws Exception{
		int[] count={10,6,1,0,7,0,3};
		
		String[] storeNames=new String[]{"PersonStore","OrganizStore","WebsiteJournalStore",
				"ClueStore","OutsideDocStore","VocationalWorkStore","LeadSpeakStore"};
		
		MyJsonObject[] myJsonObjects=new MyJsonObject[count.length];
		for (int i=0;i<myJsonObjects.length;i++) {
			MyJsonObject myJsonObject=new MyJsonObject();
			myJsonObject.setDataCount(count[i]);
			myJsonObject.setDatas(Long.toString(System.currentTimeMillis()));
			myJsonObject.setDataType(storeNames[i]);
			myJsonObject.setStoreName(storeNames[i]);
			myJsonObjects[i]=myJsonObject;
		}
		JSONArray array=new JSONArray();
		Arrays.sort(myJsonObjects, new MyComparator());
		for (MyJsonObject myJsonObject : myJsonObjects) {
			array.add(JSONObject.fromObject(myJsonObject));
		}
		System.err.println(array.toString());
	}
	
	@Test
	//数据导入到word
	public void testToWord()throws Exception{
		//System.err.println(StoreIndexQuery.getCount("北京", null, null, null, OrganizStore.class));
		//System.err.println(DateUtils.parseDate("1990-11-01", new String[]{DateFormatUtils.ISO_DATE_FORMAT.getPattern()}));
		CustomXWPFDocument document = new CustomXWPFDocument();
		
		try {
			String picId = document.addPictureData(new FileInputStream("D:/test.jpg"), XWPFDocument.PICTURE_TYPE_PNG);
			document.createPicture(picId, document.getNextPicNameNumber(XWPFDocument.PICTURE_TYPE_PNG), 200, 150);

			FileOutputStream fos = new FileOutputStream(new File("D:\\donghao.doc"));
			document.write(fos);
			fos.close();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//利用反射获得属性的值
	@Test
	public void testReflect(){
		String []properties=new String[]{"infoType","personName","nameUsedBefore","englishName","sex",
				"bebornTime","registerAddress","presentAddress","workUnit","antecedents","activityCondition",
				"networkAccountStores","certificatesStores"};
		try {
			PersonStore store=(PersonStore) baseDao.findById(PersonStore.class, "40288a625624b632015624b643a10001");
			Class clazz=Class.forName(PersonStore.class.getName());
			Class[] carg=new Class[1];
			for (String string : properties) {
				//set+属性首字母大写
				Character first=string.charAt(0);
				string=first.toString().toUpperCase()+string.substring(1);
				String propertyMethod="get"+string;
				//第二个参数为数组
				Method method=clazz.getDeclaredMethod(propertyMethod);
				//第二个参数为数组
				if(string.equals("InfoType")){
					InfoType infoType=(InfoType) method.invoke(store);
					System.err.println(string+"："+infoType.getTypeName());
				}else if (string.equals("NetworkAccountStores")) {
					//获得账号集合
					Set<NetworkAccountStore> set=(Set<NetworkAccountStore>) method.invoke(store);
					System.err.println(string+"："+set.size());
				}else if (string.equals("CertificatesStores")) {
					Set<CertificatesStore> set=(Set<CertificatesStore>) method.invoke(store);
					System.err.println(string+"："+set.size());
				}else{
					System.err.println(string+"："+method.invoke(store));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStoreToWord()throws Exception{
		//人员
		personStoreService.outputPersonStoreToWord("40283181576bc2ed01576bc4c8430003", "D:\\donghao.doc");
		//WordToHtmlUtil.wordToHtml("D:\\donghao.docx", "d://donghao.html");
		
		//组织
		//organizStoreService.outputOrganizStoreToWord("40283181576fb47c01576fb6a0f00001", "D:\\donghao.doc");
		//媒体刊物
		//websiteJournalStoreService.outputWebsiteJournalStoreToWord("40288a62562befab01562befbf9c0004", );
		//线索 
		//clueStoreService.outputClueStoreToWord("40288a625630246501563024766f0008", "D:\\donghao.doc");
	}
	
	@Test
	public void testSetPropery()throws Exception{
		//利用PropertyUtils给属性设置值
		PersonStore store=new PersonStore();
		PropertyUtils.setSimpleProperty(store, "personName", "董昊");
		System.err.println(store.getPersonName());
		PropertyUtils.getSimpleProperty(store, "personName");
	}
	
	@Test
	public void testGetPropery()throws Exception{
		//利用PropertyUtils取属性值
		PersonStore store=new PersonStore();
		store.setPersonName("donghao");
		System.err.println(PropertyUtils.getSimpleProperty(store, "personName").toString());
	}
	
	Class[] classes=new Class[]{PersonStore.class,OrganizStore.class,WebsiteJournalStore.class,
			ClueStore.class,OutsideDocStore.class,VocationalWorkStore.class,LeadSpeakStore.class};
	
	@Test
	public void testSigleThread()throws Exception{
		long start=System.currentTimeMillis();
		System.err.println("=====开始=====");
		for (Class clazz : classes) {
			System.err.println(clazz.getSimpleName()+"总数量："+baseDao.getRowCount(clazz));
		}
		System.err.println("=====结束=====");
		long end =System.currentTimeMillis();
		//480毫秒
		System.err.println(String.format("共用时%s毫秒", Long.toString((end-start))));
	}
	
	@Test
	public void testHasPersonStore()throws Exception{
		/**
		 * 尽量使用hql语句进行操作
		 * Write operations are not allowed in read-only mode (FlushMode.MANUAL):
		 * Turn your Session into FlushMode.COMMIT/AUTO or remove 'readOnly' marker from transaction definition.
		 */
		//System.err.println(personStoreService.findPersonStoreByPersonName("毛晓双"));
	}
	
	@Test
	public void testDeleleIds()throws Exception{
		//依据id批量删除
		String []ids=new String[]{"40288a9d57548791015754c2453f2641","40288a9d57548791015754c245832642","40288a9d57548791015754c245832643"};
		StringBuffer buffer=new StringBuffer("update VocationalWorkStore set action='3' where ");
		for (String string : ids) {
			buffer.append(String.format("id='%s' or ", string));
		}
		String hql=buffer.toString().trim();
		//去掉最后的or
		hql=StringUtils.substring(hql, 0, hql.length()-2);
		System.err.println(hql);
		baseDao.executeHql(hql);
	}
	
	//根据名称查询是否有数据了
	@Test
	public void testHasStoreByName()throws Exception{
		//业务文档
		//System.err.println(vocationalWorkStoreService.hasStoreByDocName("情报文档585"));
		//外来文档
		//System.err.println(outsideDocStoreService.hasStoreByName("新增测试文档1"));
		//assertEquals(false, outsideDocStoreService.hasStoreByName("新增测试文档1"));
		//新增测试刊物
		//assertEquals(true, websiteJournalStoreService.hasStoreByName("新增测试刊物"));
		assertEquals(false, clueStoreService.hasStoreByClueName("新增测试线索"));
	}
	/**
	 * 批量hql插入语句
	 * @throws Exception
	 */
	@Test
	public void test1()throws Exception{
		
		/*String hql="insert into VocationalWorkStore(id,docName,docNumber,time,theOriginal,createDate) values('222','测试','1001',"
				+ "'2010-09-01','暂无','2016-09-27')";
		baseDao.executeHql(hql);*/
	}
	/**
	 * 批量的hql查询
	 * @throws Exception
	 */
	@Test
	public void test2()throws Exception{
		String hql="select fileName from VocationalWorkStore where action<>'3' and ";
		List<VocationalWorkStore> vocationalWorkStores=baseDao.findAll(VocationalWorkStore.class);
		for (VocationalWorkStore store : vocationalWorkStores) {
			hql+=" fileName='"+store.getFileName()+"' or";
		}
		hql=hql.substring(0, hql.length()-2);
		//System.err.println(hql);
		List<String> list=baseDao.findByHql(hql);
		//转数组
		Object []names=(Object[]) list.toArray();
		for (Object string : names) {
			if(ArrayUtils.contains(names, "测试文档1")){
				System.err.println("包含");
				break;
			}
		}
	}
	/**
	 * 拼接hql，按id查询
	 * @throws Exception
	 */
	@Test
	public void test3()throws Exception{
		String[]ids=new String[]{"40283181577172e70157717b92bb025e","40283181577172e70157717b92bd025f",
				"40283181577172e70157717b92bd0260"};
		String hql="from VocationalWorkStore where";
		for (String string : ids) {
			hql+=" id='"+string+"' or";
		}
		hql=hql.substring(0, hql.length()-2);
		
		List<VocationalWorkStore> list=baseDao.findByHql(hql);
		for (VocationalWorkStore vocationalWorkStore : list) {
			System.err.println(vocationalWorkStore.toString());
		}
	}
	IBaseDao<LeadSpeakStore, Serializable> lssBaseDao;
	@Test
	public void test4()throws Exception{
		//
		String hql="from LeadSpeakStore whe";
		List<LeadSpeakStore> tempList=lssBaseDao.findByHql(hql);
		for (LeadSpeakStore store : tempList) {
			System.err.println(store.getMeetingName());
		}
	}
	
	@Test
	public void test5()throws Exception{
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s1=vocationalWorkStoreService.findVocationalWorkById("40283181589137f80158913b8b3d005e").getCreateDate();
		String s2=vocationalWorkStoreService.findVocationalWorkById("40283181589137f80158913eb7430119").getCreateDate();
		System.err.println(format.parse(s1).getTime());
		System.err.println(format.parse(s2).getTime());
	}
	/**
	 * 利用junit测试多线程
	 * @throws Throwable
	 */
	@Test  
    public void testThreadJunit() throws Throwable {   
        //Runner数组，想当于并发多少个。 
        TestRunnable[] trs = new TestRunnable [2];  
        for(int i=0;i<trs.length;i++){  
            trs[i]=new ThreadA();  
        }  
        // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入 
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);  
        // 开发并发执行数组里定义的内容 
        mttr.runTestRunnables();  
    }  
  
	private class ThreadA extends TestRunnable {
		@Override
		public void runTest() throws Throwable {
			System.out.println("开始查询");
			StoreIndexQuery.findStore("anyField", "1989", "1900-01-01", 
					"2016-10-01", 1, 1000, null, null, null, null,
					null, PersonStore.class);
			System.out.println("结束查询");
		}
	}
    
}













