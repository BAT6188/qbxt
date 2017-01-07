package com.ushine.storeInfo;

import java.io.*;

import org.apache.poi.hpsf.CustomProperties;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class CreateWordDoc {

	public static void main(String[] args) throws Exception {
		// POI apparently can't create a document from scratch,
		// so we need an existing empty dummy document
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("d://donghao.doc"));
		HWPFDocument doc = new HWPFDocument(fs);
		Range range = doc.getRange();
		
	
		
		// centered paragraph with large font size
	
		
		
		Paragraph par1 = range.insertAfter(new ParagraphProperties(), 0);
		par1.setSpacingAfter(200);
		par1.setJustification((byte) 1);
		
		Table table=par1.insertTableBefore((short) 4, 4);
		
		table.getRow(0).getCell(0).getParagraph(0).getCharacterRun(0).replaceText("donghao", false);
		
		
		// justification: 0=left, 1=center, 2=right, 3=left and right

		CharacterRun run1 = par1.insertAfter("one");
		run1.setFontSize(2 * 18);
		// font size: twice the point size
		
		// paragraph with bold typeface
		Paragraph par2 = run1.insertAfter(new ParagraphProperties(), 0);
		par2.setSpacingAfter(200);
		CharacterRun run2 = par2.insertAfter("two two two two two two two two two two two two two");
		run2.setBold(true);

		// paragraph with italic typeface and a line indent in the first line
		Paragraph par3 = run2.insertAfter(new ParagraphProperties(), 0);
		par3.setFirstLineIndent(200);
		par3.setSpacingAfter(200);
		CharacterRun run3 = par3.insertAfter("three three three three three three three three three "
				+ "three three three three three three three three three three three three three three "
				+ "three three three three three three three three three three three three three three");
		run3.setItalic(true);

		/*// add a custom document property (needs POI 3.5; POI 3.2 doesn't save
		// custom properties)
		DocumentSummaryInformation dsi = doc.getDocumentSummaryInformation();
		CustomProperties cp = dsi.getCustomProperties();
		if (cp == null)
			cp = new CustomProperties();
		cp.put("myProperty", "foo bar baz");
		dsi.setCustomProperties(cp);
		//
*/		
		
		//table.getRow(0).getCell(0).getParagraph(0).getCharacterRun(0).insertBefore("插入0行0列的内容" );
		
		doc.write(new FileOutputStream("d://"+System.currentTimeMillis()+".doc"));
		System.err.println("success");
	}
}