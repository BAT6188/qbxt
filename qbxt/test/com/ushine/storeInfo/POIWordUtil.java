package com.ushine.storeInfo;

import java.util.*;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

import java.io.*;

public class POIWordUtil {  
	  
    public static void main(String[] args) throws Exception {  
        Map<String, String> replaces = new HashMap<String, String>();  
  
        replaces.put("${username}", "rongzhi_li");  
        replaces.put("${password}", "1123456");  
        replaces.put("${author}", "lee");  
  
        poiWordTableReplace("d://t1.doc", "d://t2.doc", replaces);  
    }  
  
    public static void poiWordTableReplace(String sourceFile, String newFile,  
            Map<String, String> replaces) throws Exception {  
        FileInputStream in = new FileInputStream(sourceFile);  
        HWPFDocument hwpf = new HWPFDocument(in);  
        Range range = hwpf.getRange();// 得到文档的读取范围  
        TableIterator it = new TableIterator(range);  
        // 迭代文档中的表格  
        while (it.hasNext()) {  
            Table tb = (Table) it.next();  
            // 迭代行，默认从0开始  
            for (int i = 0; i < tb.numRows(); i++) {  
                TableRow tr = tb.getRow(i);  
                // 迭代列，默认从0开始  
                for (int j = 0; j < tr.numCells(); j++) {  
                    TableCell td = tr.getCell(j);// 取得单元格  
                    // 取得单元格的内容  
                    for (int k = 0; k < td.numParagraphs(); k++) {  
                        Paragraph para = td.getParagraph(k);  
  
                        String s = para.text();  
                        final String old = s;  
                        for (String key : replaces.keySet()) {  
                            if (s.contains(key)) {  
                                s = s.replace(key, replaces.get(key));  
                            }  
                        }  
                        if (!old.equals(s)) {// 有变化  
                            para.replaceText(old, s);  
                            s = para.text();  
                            System.out.println("old:" + old + "->" + "s:" + s);  
                        }  
  
                    } // end for  
                } // end for  
            } // end for  
        } // end while  
  
        FileOutputStream out = new FileOutputStream(newFile);  
        hwpf.write(out);  
  
        out.flush();  
        out.close();  
  
    }  
}  
