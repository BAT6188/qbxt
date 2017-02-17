package com.ushine.util;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ushine.dao.IBaseDao;
import com.ushine.storesinfo.model.InfoType;
import com.ushine.storesinfo.model.PersonStore;
import com.ushine.storesinfo.service.IInfoTypeService;
import com.ushine.storesinfo.service.IPersonStoreService;

/**
 * 写入Excel的操作类
 * 
 * @author Administrator
 *
 */
public class ExcelUtil {
	public static final String HEADERINFO = "headInfo";
	public static final String DATAINFON = "dataInfo";
	
	private static Logger logger=LoggerFactory.getLogger(ExcelUtil.class);
	// @Autowired public IPersonStoreService personStoreService;
	// @Autowired public IInfoTypeService infoTypeService;
	// @Autowired public IBaseDao baseDao;

	public static void writeExcel(String pathname, Map<String, Object> map, Workbook wb) throws IOException {
		if (null != map && null != pathname) {
			// 表头
			@SuppressWarnings("unchecked")
			List<Object> headList = (List<Object>) map.get(ExcelUtil.HEADERINFO);
			// 数据
			@SuppressWarnings("unchecked")
			List<TreeMap<String, Object>> dataList = (List<TreeMap<String, Object>>) map.get(ExcelUtil.DATAINFON);
			CellStyle style = getCellStyle(wb);
			Sheet sheet = wb.createSheet();// 在文档对象中创建一个表单..默认是表单名字是Sheet0、Sheet1....

			/**
			 * 设置Excel表的第一行即表头
			 */
			Row row = sheet.createRow(0);
			for (int i = 0; i < headList.size(); i++) {
				Cell headCell = row.createCell(i);
				headCell.setCellType(Cell.CELL_TYPE_STRING);// 设置这个单元格的数据的类型,是文本类型还是数字类型
				headCell.setCellStyle(style);// 设置表头样式
				headCell.setCellValue(String.valueOf(headList.get(i)));// 给这个单元格设置值
			}

			for (int i = 0; i < dataList.size(); i++) {
				Row rowdata = sheet.createRow(i + 1);// 创建数据行
				TreeMap<String, Object> mapdata = dataList.get(i);
				Iterator it = mapdata.keySet().iterator();
				int j = 0;
				while (it.hasNext()) {
					String strdata = String.valueOf(mapdata.get(it.next()));
					Cell celldata = rowdata.createCell(j);// 在一行中创建某列..
					celldata.setCellType(Cell.CELL_TYPE_STRING);
					celldata.setCellValue(strdata);
					j++;
				}
			}

			// 文件流
			File file = new File(pathname);
			OutputStream os = new FileOutputStream(file);
			os.flush();
			wb.write(os);
			os.close();
		}
	}

	/**
	 * 设置表头样式
	 * 
	 * @param wb
	 * @return
	 */
	public static CellStyle getCellStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 12);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		style.setFillForegroundColor(HSSFColor.LIME.index);// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.SOLID_FOREGROUND);// 让单元格居中
		// style.setWrapText(true);//设置自动换行
		style.setFont(font);
		return style;
	}

	/**
	 * 输出excel
	 * 
	 * @param path
	 * @param personIds
	 * @param personStoreService
	 */
	public static void outputExcel(String path, String[] personIds, IInfoTypeService infoTypeService,
			IPersonStoreService personStoreService, IBaseDao baseDao) {
		try {
			// 创建工作文档对象
			Workbook wb = null;
			wb = new XSSFWorkbook();
			// 创建sheet对象
			Sheet sheet1 = (Sheet) wb.createSheet("人员信息");
			// 表头
			Map<String, String> hashMap = new LinkedHashMap<>();
			hashMap.put("infoType", "类别");
			hashMap.put("personName", "姓名");
			hashMap.put("nameUsedBefore", "曾用名");
			hashMap.put("englishName", "英文名");
			hashMap.put("registerAddress", "户籍地址");
			hashMap.put("presentAddress", "现住地址");
			hashMap.put("workUnit", "工作单位");
			hashMap.put("sex", "性别");
			hashMap.put("bebornTime", "出生日期");
			// 网络账号
			List<InfoType> networkAccountStoreInfoTypeList = infoTypeService
					.findInfoTypeByTypeName("NetworkAccountStore");
			for (InfoType infoType : networkAccountStoreInfoTypeList) {
				hashMap.put(infoType.getId(), infoType.getTypeName());
			}
			// 身份账号
			List<InfoType> certificatesStoreInfoTypeList = infoTypeService.findInfoTypeByTypeName("CertificatesStore");
			for (InfoType infoType : certificatesStoreInfoTypeList) {
				hashMap.put(infoType.getId(), infoType.getTypeName());
			}
			Set<String> set = hashMap.keySet();
			Row headerRow = (Row) sheet1.createRow(0);
			int i = 0;
			for (String string : set) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(hashMap.get(string));
				i++;
			}
			//
			for (int j = 0; j < personIds.length; j++) {
				int k = 0;
				PersonStore personStore = personStoreService.findPersonStoreById(personIds[j]);

				Row row = (Row) sheet1.createRow(j + 1);

				Map<String, String> valueMap = new LinkedHashMap<>();
				valueMap.put("infoType", personStore.getInfoType().getTypeName());
				valueMap.put("personName", personStore.getPersonName());
				valueMap.put("nameUsedBefore", personStore.getNameUsedBefore());
				valueMap.put("englishName", personStore.getEnglishName());
				valueMap.put("registerAddress", personStore.getRegisterAddress());
				valueMap.put("presentAddress", personStore.getPresentAddress());
				valueMap.put("workUnit", personStore.getWorkUnit());
				valueMap.put("sex", personStore.getSex());
				valueMap.put("bebornTime", personStore.getBebornTime());
				// 获得身份账号
				for (InfoType infoType : certificatesStoreInfoTypeList) {
					String sql = "SELECT CERTIFICATES_NUMBER from t_certificates_store where PERSON_STORE_ID='"
							+ personIds[j] + "' and " + "INFOTYPEID='" + infoType.getId() + "'";
					List<String> list = baseDao.findBySql(sql);
					valueMap.put(infoType.getId(), list.toString().substring(1, list.toString().length() - 1));
				}
				// 获得网络账号
				for (InfoType infoType : networkAccountStoreInfoTypeList) {
					String sql = "SELECT NETWORK_NUMBER from t_networkaccount_store where PERSON_STORE_ID='"
							+ personIds[j] + "' and " + "INFOTYPEID='" + infoType.getId() + "'";
					List<String> list = baseDao.findBySql(sql);
					
					valueMap.put(infoType.getId(), list.toString().substring(1, list.toString().length() - 1));
				}
				for (String string : set) {
					Cell cell = row.createCell(k);
					cell.setCellValue(valueMap.get(string));
					k++;
				}
				logger.info("正在查询第"+(j+1)+"个人员的信息");
			}
			// 创建文件流
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(path);
				// 写入数据
				wb.write(stream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 关闭文件流
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
