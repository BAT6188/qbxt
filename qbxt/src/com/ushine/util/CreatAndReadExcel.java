package com.ushine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ushine.common.config.Configured;
import com.ushine.common.utils.PathUtils;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.service.impl.LeadSpeakStoreServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 可以从http://poi.apache.org/ 这里下载到POI的jar包 POI 创建和读取2003-2007版本Excel文件
 * 
 */

public class CreatAndReadExcel {
	private static Logger logger=LoggerFactory.getLogger(CreatAndReadExcel.class);
	
	//反射调用
	@Test
	public void testReflect(){
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("infoType", null);
			map.put("personName", "董昊");
			map.put("nameUsedBefore", "");
			map.put("englishName", "lebron dong");
			map.put("sex", "男");
			map.put("bebornTime", "1989-11-09");
			map.put("registerAddress", "山东省济宁市兖州区");
			map.put("presentAddress", "上海市浦东区");
			map.put("workUnit", "三所");
			PersonStore store=getPersonStoreByProperty(map);
			System.err.println(store.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//读取人员表
	@Test
	public void testReadExcel(){
		String path = "D:\\Users\\dh\\Desktop\\人员excel\\20160830130802.xlsx";
		//String path = "D:\\Users\\dh\\Desktop\\20160824150805-版本2.xlsx";
		//String path = "D:\\Users\\dh\\Desktop\\20160824150805-2003版本.xls";
		File file = new File(path);
		System.err.println(readPersonInfoExcel(file));
	}
	
	/**
	 * 通过反射的方式为设置每个属性的值
		private Set<CertificatesStore> certificatesStores;//人员证件
		private Set<NetworkAccountStore> networkAccountStores;//人员网络账号
	 * @param map
	 * @return
	 */
	public static PersonStore getPersonStoreByProperty(Map<String, String> map){
		PersonStore store=new PersonStore();
		String []properties=new String[]{"infoType","personName","nameUsedBefore","englishName","sex",
				"bebornTime","registerAddress","presentAddress","workUnit"};
		try {
			Class clazz=Class.forName(PersonStore.class.getName());
			Class[] carg=new Class[1];
			for (String string : properties) {
				//set+属性首字母大写
				Character first=string.charAt(0);
				string=first.toString().toUpperCase()+string.substring(1);
				String propertyMethod="set"+string;
				String value=map.get(string);
				if (string.equals("infoType")) {
					carg[0]=InfoType.class;
				}else{
					carg[0]=String.class;
				}
				//第二个参数为数组
				Method method=clazz.getDeclaredMethod(propertyMethod, carg);
				//第二个参数为数组
				method.invoke(store, value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return store;
	}

	/**
	 * 创建2007版Excel文件
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void creat2007Excel() throws FileNotFoundException, IOException {
		// HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
		sheet.setColumnWidth(1, 10000);// 设置第二列的宽度为
		XSSFRow row = sheet.createRow(1);// 创建一个行对象
		row.setHeightInPoints(23);// 设置行高23像素
		XSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
		// 设置字体
		XSSFFont font = workBook.createFont();// 创建字体对象
		font.setFontHeightInPoints((short) 15);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		font.setFontName("黑体");// 设置为黑体字
		style.setFont(font);// 将字体加入到样式对象
		// 设置对齐方式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 设置边框
		style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
		style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
		style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
		// 格式化日期
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		XSSFCell cell = row.createCell(1);// 创建单元格
		cell.setCellValue(new Date());// 写入当前日期
		cell.setCellStyle(style);// 应用样式对象
		// 文件输出流
		FileOutputStream os = new FileOutputStream("style_2007.xlsx");
		workBook.write(os);// 将文档对象写入文件输出流
		os.close();// 关闭文件输出流
		System.out.println("创建成功 office 2007 excel");
	}

	/**
	 * 创建2003版本的Excel文件
	 */
	private static void creat2003Excel() throws FileNotFoundException, IOException {
		HSSFWorkbook workBook = new HSSFWorkbook();// 创建 一个excel文档对象
		HSSFSheet sheet = workBook.createSheet();// 创建一个工作薄对象
		sheet.setColumnWidth(1, 10000);// 设置第二列的宽度为
		HSSFRow row = sheet.createRow(1);// 创建一个行对象
		row.setHeightInPoints(23);// 设置行高23像素
		HSSFCellStyle style = workBook.createCellStyle();// 创建样式对象
		// 设置字体
		HSSFFont font = workBook.createFont();// 创建字体对象
		font.setFontHeightInPoints((short) 15);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 设置粗体
		font.setFontName("黑体");// 设置为黑体字
		style.setFont(font);// 将字体加入到样式对象
		// 设置对齐方式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		// 设置边框
		style.setBorderTop(HSSFCellStyle.BORDER_THICK);// 顶部边框粗线
		style.setTopBorderColor(HSSFColor.RED.index);// 设置为红色
		style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);// 底部边框双线
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);// 左边边框
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);// 右边边框
		// 格式化日期
		style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		HSSFCell cell = row.createCell(1);// 创建单元格
		cell.setCellValue(new Date());// 写入当前日期
		cell.setCellStyle(style);// 应用样式对象
		// 文件输出流
		FileOutputStream os = new FileOutputStream("style_2003.xls");
		workBook.write(os);// 将文档对象写入文件输出流
		os.close();// 关闭文件输出流
		System.out.println("创建成功 office 2003 excel");
	}

	/**
	 * 对外提供读取excel 的方法
	 */
	public static List<List<Object>> readExcel(File file) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(file);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(file);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}
	/**
	 * 获得Xml文件中的人员Excel的表头
	 * @return
	 */
	private static String[] getHeaders() {
		String filePath=PathUtils.getConfigPath(CreatAndReadExcel.class)+"person-store-excel.xml";
		XmlUtils xmlUtils=new XmlUtils(filePath);
		//获得单个节点
		Element element=xmlUtils.getNode("header");
		List<Element> elements=element.elements();
		String []headers=new String[elements.size()];
		for (int i=0;i<elements.size();i++) {
			headers[i]=elements.get(i).getText();
		}
		return headers;
	}
	/**
	 * 返回表头和对应属性的map对象
	 * @return
	 */
	private static Map getPropertiesByHeader() {
		Map<String, String> personHeaderMap=new LinkedHashMap<>();
		String filePath=PathUtils.getConfigPath(LeadSpeakStoreServiceImpl.class)+"person-store-excel.xml";
		//MyXmlUtilsTest xmlUtils=new MyXmlUtilsTest(filePath);
		XmlUtils xmlUtils=new XmlUtils(filePath);
		//获得单个节点
		Element element=xmlUtils.getNode("header");
		List<Element> elements=element.elements();
		for (Element element2 : elements) {
			//key中文-value英文属性名称
			personHeaderMap.put(element2.getText(),xmlUtils.getNodeAttrVal(element2, "name"));
		}
		return personHeaderMap;
	}
	
	private static Map<Integer, String> headerMap;
	/**
	 * 存放与表头对应的字段属性
	 */
	private static Map<String, String> personHeaderMap;
	/**
	 * 用于存放表头字段
	 */
	private static String []headers;
	/**
	 * 判断Sheet是否符合要求
	 * @param sheet
	 */
	private static Map isStandardPersonSheet(Sheet sheet) {
		int k=0;//用于计数
		Row row=null;
		Cell cell=null;
		headerMap=new HashMap<>();
		personHeaderMap=new HashMap<>();
		headers=getHeaders();
		boolean isStandardExcell=false;
		Integer startRowNumber=-1;
		Map<String, Object> resultMap=new HashMap<>();
		logger.info("标准表头信息："+Arrays.toString(headers));
		for(int i=sheet.getFirstRowNum();i<sheet.getPhysicalNumberOfRows();i++){
			row=sheet.getRow(i);
			if (null!=row) {
				//读取单元格值,判断是否是规定的格式
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++){
					cell=row.getCell(j);
					if (null!=cell) {
						for (String string : headers) {
							if(cell.getCellType()==cell.CELL_TYPE_STRING){
								String cellValue=cell.getStringCellValue().trim();
								if (string.equals(cellValue)) {
									headerMap.put(j, string);
									k++;
									if (k==headers.length) {
										isStandardExcell=true;
										startRowNumber=i;
										personHeaderMap=getPropertiesByHeader();
										logger.info("符合标准表格,表头第"+startRowNumber+"行");
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		resultMap.put("isStandardExcell", isStandardExcell);
		resultMap.put("startRowNumber", startRowNumber);
		return resultMap;
	}
	/**
	 * 读取每个sheet表中每行每单元格的值
	 * @param isStandardExcell
	 * @param sheet
	 * @param row
	 * @param cell
	 * @param startRowNumber
	 */
	private static String readSheetData(boolean isStandardExcell,Sheet sheet,Row row,Cell cell,Integer startRowNumber) {
		//读取符合的数据
		JSONArray array=new JSONArray();
		JSONObject root=new JSONObject();
		int realRowCount=0;
		if (isStandardExcell) {
			//从表头下一行读取内容
			int cellCount=sheet.getRow(startRowNumber).getLastCellNum();
			for (int o = startRowNumber+1; o < sheet.getPhysicalNumberOfRows(); o++) {
				row=sheet.getRow(o);
				if (null!=row) {
					realRowCount++;
					JSONObject jsonObject=new JSONObject();
					for (int n = row.getFirstCellNum(); n <cellCount; n++){
						cell=row.getCell(n);
						//属性
						String value=headerMap.get(n);
						//获得对应属性
						String property=personHeaderMap.get(value);
						if (null!=property) {
							if (null!=cell) {
								// 格式化 number String
								DecimalFormat df = new DecimalFormat("0");
								// 字符
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化日期字符串
								DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
								switch (cell.getCellType()) {
									case XSSFCell.CELL_TYPE_STRING:
										jsonObject.put(property, cell.getStringCellValue());
										break;
									case XSSFCell.CELL_TYPE_NUMERIC:
										String numericValue=null;
										if ("@".equals(cell.getCellStyle().getDataFormatString())) {
											numericValue = df.format(cell.getNumericCellValue());
					
										} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
											numericValue = nf.format(cell.getNumericCellValue());
										} else {
											numericValue = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
										}
										jsonObject.put(property, numericValue);
										break;
									default:
										jsonObject.put(property,"");
										break;
									}
							}else{
								jsonObject.put(property,"");
							}
						}
					}
					array.add(jsonObject);
				}
			}
			root.element("startRowNumber", startRowNumber);
			root.element("realRowCount", realRowCount);
			root.element("array", array.toString());
		}
		return root.toString();
	}
	/**
	 * 读取人员Excel文件
	 * @param file
	 * @return
	 */
	public static String readPersonInfoExcel(File file){
		String result=null;
		try {
			/**标准表头,顺序任意:人员类别,姓名,曾用名,英文名,户籍地址,现住地址,工作单位,性别,出生日期,
			 * 电子邮箱,手机号码,固定电话,QQ号码,微信号,身份证,港澳通行证,护照**/
			Map<String, Object> resultMap=new HashMap<>();
			int k=0;
			String fileName=file.getName();
			//扩展名
			String extention=FilenameUtils.getExtension(fileName);
			boolean isStandardExcell=false;
			if ("xls".equals(extention)) {
				//2003版本
				HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
				HSSFRow row = null;
				HSSFCell cell = null;
				//所有的表
				int count=hwb.getNumberOfSheets();
				for (int m=0;m<count;m++){
					HSSFSheet sheet = hwb.getSheetAt(m);
					for(int i=sheet.getFirstRowNum();i<sheet.getPhysicalNumberOfRows();i++){
						resultMap=isStandardPersonSheet(sheet);
						isStandardExcell=(boolean) resultMap.get("isStandardExcell");
						if (isStandardExcell) {
							break;
						}
					}
					//读取符合的数据
					result=readSheetData(isStandardExcell, sheet, row, cell, (Integer)resultMap.get("startRowNumber"));
				}
			} else if ("xlsx".equals(extention)) {
				//2007版本以上
				XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
				XSSFRow row = null;
				XSSFCell cell = null;
				int count=xwb.getNumberOfSheets();
				for(int m=0;m<count;m++){
					// 读取第m个表格内容
					XSSFSheet sheet = xwb.getSheetAt(m);
					for(int i=sheet.getFirstRowNum();i<sheet.getPhysicalNumberOfRows();i++){
						resultMap=isStandardPersonSheet(sheet);
						isStandardExcell=(boolean) resultMap.get("isStandardExcell");
						if (isStandardExcell) {
							break;
						}
					}
					//读取符合的数据
					result=readSheetData(isStandardExcell, sheet, row, cell, (Integer)resultMap.get("startRowNumber"));
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<List<Object>> read2003Excel(File file) throws IOException {
		List<List<Object>> list = new LinkedList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
		//所有的表
		int count=hwb.getNumberOfSheets();
		for (int m=0;m<count;m++) {
			HSSFSheet sheet = hwb.getSheetAt(m);
			Object value = null;
			HSSFRow row = null;
			HSSFCell cell = null;
			System.out.println("读取office 2003 excel内容如下：");
			for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					// 格式化 number String
					DecimalFormat df = new DecimalFormat("0");
					// 字符
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
					DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
					switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_STRING:
							// System.out.println(i + "行" + j + " 列 is String type");
							value = cell.getStringCellValue();
							System.out.print("  " + value + "  ");
							break;
						case XSSFCell.CELL_TYPE_NUMERIC:
							// System.out.println(i + "行" + j
							// + " 列 is Number type ; DateFormt:"
							// + cell.getCellStyle().getDataFormatString());
							if ("@".equals(cell.getCellStyle().getDataFormatString())) {
								value = df.format(cell.getNumericCellValue());
		
							} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
								value = nf.format(cell.getNumericCellValue());
							} else {
								value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
							}
							System.out.print("  " + value + "  ");
							break;
						case XSSFCell.CELL_TYPE_BOOLEAN:
							// System.out.println(i + "行" + j + " 列 is Boolean type");
							value = cell.getBooleanCellValue();
							System.out.print("  " + value + "  ");
							break;
						case XSSFCell.CELL_TYPE_BLANK:
							// System.out.println(i + "行" + j + " 列 is Blank type");
							value = "";
							System.out.print("  " + value + "  ");
							break;
						default:
							// System.out.println(i + "行" + j + " 列 is default type");
							value = cell.toString();
							System.out.print("  " + value + "  ");
						}
					if (value == null || "".equals(value)) {
						continue;
					}
					linked.add(value);
				}
				System.out.println("");
				list.add(linked);
			}
		}
		return list;
	}

	/**
	 * 读取Office 2007 excel
	 */

	private static List<List<Object>> read2007Excel(File file) throws IOException {

		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		System.out.println("读取office 2007 excel内容如下：");
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
				// 字符
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字

				switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						// System.out.println(i + "行" + j + " 列 is String type");
						value = cell.getStringCellValue();
						//"," + 
						System.out.print(value + ",");
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						// System.out.println(i + "行" + j
						// + " 列 is Number type ; DateFormt:"
						// + cell.getCellStyle().getDataFormatString());
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
	
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						System.out.print(value + ",");
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						// System.out.println(i + "行" + j + " 列 is Boolean type");
						value = cell.getBooleanCellValue();
						System.out.print(value + ",");
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						// System.out.println(i + "行" + j + " 列 is Blank type");
						value = ",";
						// System.out.println(value);
						break;
					default:
						// System.out.println(i + "行" + j + " 列 is default type");
						value = cell.toString();
						System.out.print(value + ",");
					}
				if (value == null || "".equals(value)) {
					continue;
				}
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}
}