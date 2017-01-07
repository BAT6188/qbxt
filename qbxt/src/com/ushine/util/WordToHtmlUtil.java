package com.ushine.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.w3c.dom.Document;
/**
 * 将word2003或07以上版本转为html文件
 * @author dh
 *
 */
public class WordToHtmlUtil {
	/**
	 * word转html
	 * @param inputPath 源word路径
	 * @param outputPath 目标html路径
	 */
	public static void wordToHtml(String inputPath,String outputPath){
		int index=inputPath.lastIndexOf(".");
		String subffix=inputPath.substring(index+1);
		try {
			if(subffix.equals("doc")){
				//转2003
				convertDocToHtml(inputPath, outputPath);
			}else if(subffix.equals("docx")){
				//转2007以上版本
				convertDocxToHtml(inputPath, outputPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 将word2007以上版本转为html
	 * 效果不够完美
	 * @param fileName
	 * @param outPutFile
	 * @throws Exception
	 */
	private static void convertDocxToHtml(String fileName,String outPutFile)throws Exception{
		
		//long startTime = System.currentTimeMillis();
		InputStream in=null;
		OutputStream out=null;
		try {
			File file=new File(fileName);
			if (file.exists()) {
				in=new FileInputStream(fileName);
				XWPFDocument document = new XWPFDocument(in);
				XHTMLOptions options = XHTMLOptions.getDefault().indent(4)
						.setFragment(true).setOmitHeaderFooterPages(true);
				// Extract image
				/*File imageFolder = new File("D:/vfsroot/1000000/ueditor_upload/images"
						+ fileInName);
				options.setExtractor(new FileImageExtractor(imageFolder));
				// URI resolver
				options.URIResolver(new FileURIResolver(imageFolder));*/

				File outFile = new File(outPutFile);
				outFile.getParentFile().mkdirs();
			    out = new FileOutputStream(outFile);
				XHTMLConverter.getInstance().convert(document, out, options);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭流
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
		/*System.out.println("Generate " + fileOutName + " with "
				+ (System.currentTimeMillis() - startTime) + " ms.");*/

	}
	/**
	 * 将word2003输出为html文件
	 * @param fileName 文件路径
	 * @param outPutFile 目标文件路径
	 * @throws TransformerException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private static void convertDocToHtml(String fileName, String outPutFile)throws Exception {  
		InputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			File file=new File(fileName);
			if (file.exists()) {
				in = new FileInputStream(fileName);
				HWPFDocument wordDocument = new HWPFDocument(in);
				// WordToHtmlUtils.loadDoc(new FileInputStream(inputFile));
				WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
						DocumentBuilderFactory.newInstance().newDocumentBuilder()
								.newDocument());
				//处理图片
				wordToHtmlConverter.setPicturesManager(new PicturesManager() {
					public String savePicture(byte[] content,
							PictureType pictureType, String suggestedName,
							float widthInches, float heightInches) {
						return "test/" + suggestedName;
					}
				});
				wordToHtmlConverter.processDocument(wordDocument);
				// save pictures
				//保存图片
				/*
				 * List pics=wordDocument.getPicturesTable().getAllPictures();
				 * if(pics!=null){ for(int i=0;i<pics.size();i++){ Picture pic =
				 * (Picture)pics.get(i); System.out.println(); try {
				 * pic.writeImageContent(new FileOutputStream("D:/test/" +
				 * pic.suggestFullFileName())); } catch (FileNotFoundException e) {
				 * e.printStackTrace(); } } }
				 */
				Document htmlDocument = wordToHtmlConverter.getDocument();
				out = new ByteArrayOutputStream();
				DOMSource domSource = new DOMSource(htmlDocument);
				StreamResult streamResult = new StreamResult(out);

				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer serializer = tf.newTransformer();
				//这样转码服务器正常，但是本地调试中文乱码
				serializer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
				serializer.setOutputProperty(OutputKeys.INDENT, "yes");
				serializer.setOutputProperty(OutputKeys.METHOD, "html");
				serializer.transform(domSource, streamResult);
				writeFile(new String(out.toByteArray()), outPutFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}

	}

	/**
	 * 输出word内容
	 * @param content 
	 * @param path 文件的路径
	 */
	private static void writeFile(String content, String path) {  
        FileOutputStream fos = null;  
        BufferedWriter bw = null;  
        try {  
            File file = new File(path);  
            fos = new FileOutputStream(file);  
            bw = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));  
            bw.write(content);  
        } catch (FileNotFoundException fnfe) {  
            fnfe.printStackTrace();  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        } finally {  
            try {  
                if (bw != null)  
                    bw.close();  
                if (fos != null)  
                    fos.close();  
            } catch (IOException ie) {  
            }  
        }  
    }  
}
