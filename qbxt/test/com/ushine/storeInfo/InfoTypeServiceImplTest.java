package com.ushine.storeInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ushine.storeInfo.controller.PersonStoreController;
import com.ushine.storeInfo.model.InfoType;
import com.ushine.storeInfo.service.IInfoTypeService;
import com.ushine.storeInfo.service.IPersonStoreService;
import com.ushine.storeInfo.storefinal.StoreFinal;
import com.ushine.util.StringUtil;
/**
 * 信息类别实现测试类
 * @author wangbailin
 *
 */
@Component("InfoTypeServiceImplTest")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)  
@Transactional
public class InfoTypeServiceImplTest {
	@Autowired
	private IInfoTypeService iInfoTypeService;
	@Autowired
	private IPersonStoreService personStoreService;
	//@Test
	public void saveInfoTypeTest(){
		InfoType infoType = new InfoType();
		infoType.setSaveTime(StringUtil.dates());
		infoType.setTableTypeName("VocationalWorkStore");
		infoType.setTypeName("报告2");
		try {
			iInfoTypeService.saveInfoType(infoType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@Test
	public void findInfoTypeByIdTest(){
		try {
			InfoType infoType = iInfoTypeService.findInfoTypeById("40288aac5474828b0154748290f30001");
			System.out.println(infoType.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//@Test
	public void findInfoTypeAllTest(){
		try {
			List<InfoType> infoTypes = iInfoTypeService.findInfoTypeAll();
			for (InfoType infoType : infoTypes) {
				System.out.println(infoType.getId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void findInfoTypeByTypeNameAndTableName() throws Exception{
		InfoType infoType=iInfoTypeService.findInfoTypeByTypeNameAndTableName("身份证", StoreFinal.CERTIFICATES_STORE);
		System.out.println(infoType.toString());
	}
	//@Test
	public void findInfoTypeByTypeNameTest(){
		try {
			List<InfoType> list = iInfoTypeService.findInfoTypeByTypeName("");
			System.out.println(list.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void findInfoTypeByTableName(){
		try {
			InfoType infoType=iInfoTypeService.findInfoTypeByTypeNameAndTableName("测试", StoreFinal.PERSON_STORE);
			System.out.println("=========================="+infoType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCanBeSaved(){
		try {
			String result=canBeSaved("董昊3昊", "", "2016-12-23", "女");
			System.out.println("=========================="+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date=format.parse("20130-12-203");
			System.err.println("=========合法========="+format.format(date));
		} catch (Exception e) {
			System.err.println("=========日期不合法=========");
			e.printStackTrace();
		}
	}
	private String canBeSaved(String personName,String infoType,String bebornTime,String sex){
		StringBuffer buffer=new StringBuffer();
		try {
			if (StringUtil.isEmty(personName)) {
				buffer.append("姓名为空"+",");
			}
			if (personStoreService.findPersonStoreByPersonName(personName)) {
				buffer.append("姓名已经存在"+",");
			}
			if (StringUtil.isEmty(sex)) {
				buffer.append("性别为空"+",");
			}
			if (!StringUtil.isEmty(sex)&&!sex.trim().equals("男")&&!sex.trim().equals("女")) {
				buffer.append("性别不合法"+",");
			}
			InfoType type=iInfoTypeService.findInfoTypeByTypeNameAndTableName(infoType, StoreFinal.PERSON_STORE);
			if (null==type) {
				buffer.append("人员类别不存在"+",");
			}
			//日期不合法
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date=format.parse(bebornTime);
				long realValue=date.getTime();
				long todayValue=new Date().getTime();
				//时间大于今天
				if (realValue>=todayValue) {
					buffer.append("出生日期不合法"+",");
				}
			} catch (Exception e) {
				buffer.append("出生日期不合法"+",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
