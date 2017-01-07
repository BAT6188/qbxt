package com.ushine.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XML文档工具类
 * 
 * @author Franklin
 *
 */
public class XMLUtils {
	private static final Logger logger = LoggerFactory.getLogger(XMLUtils.class);

	private Document doc = null;

	private String fileName = null;

	private Element root = null;

	/**
	 * 加载XML要操作的文件
	 * 
	 * @param fileName
	 *            String XML文件名
	 */
	public XMLUtils(String _fileName) throws Exception {
		try {
			SAXReader reader = new SAXReader();
			doc = reader.read(new File(_fileName));
			root = doc.getRootElement();
			fileName = _fileName;
			logger.debug("成功加载XML文件: " + _fileName + ".");
		} catch (Exception e) {
			throw new Exception("加载XML文件失败:" + _fileName + ",关闭应用程序.", e);
		}
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	/**
	 * 取得根节点下的单个子节点
	 * 
	 * @param nodeName
	 *            String 节点名称
	 * @return Element 节点
	 */
	public Element getNode(String nodeName) {
		return getNode(null, nodeName);
	}

	/**
	 * 取得某节点的单个子节点
	 * 
	 * @param parent
	 *            Element 父节点，为空查询根节点下的子节点
	 * @param nodeName
	 *            String 节点名称
	 * @return Element 节点
	 */
	public Element getNode(Element parent, String nodeName) {
		if (parent == null) {
			return root.element(nodeName);
		} else {
			return parent.element(nodeName);
		}
	}

	/**
	 * 取得根节点下指定的子节点集合
	 * 
	 * @param nodeName
	 *            String 节点名称
	 * @return List<Element> 节点集合
	 */
	public List<Element> getNodes(String nodeName) {
		return getNodes(null, nodeName);
	}

	/**
	 * 取得某节点的子节点集合
	 * 
	 * @param parent
	 *            Element 父节点，为空查询根节点下的子节点
	 * @param nodeName
	 *            String 节点名称
	 * @return List<Element> 节点集合
	 */
	@SuppressWarnings("unchecked")
	public List<Element> getNodes(Element parent, String nodeName) {
		if (parent == null) {
			parent = root;
		}
		return (List<Element>) parent.elements(nodeName);
	}

	/**
	 * 获取指定指定的值
	 * 
	 * @param element
	 *            Element 节点
	 * @return String 节点值
	 */
	public String getNodeVal(Element element) {
		return element.getText();
	}

	/**
	 * 获取指定节点的属性值
	 * 
	 * @param element
	 *            Element 节点
	 * @param attrName
	 *            String 属性名称
	 * @return String 属性值
	 */
	public String getNodeAttrVal(Element element, String attrName) {
		return element.attributeValue(attrName);
	}

	/**
	 * 在一个父节点下面添加一个子节点
	 * 
	 * @param parentNodeName
	 *            父节点名称
	 * @param addElementName
	 *            待添加的节点名称
	 * @param text
	 *            子节点的文本
	 * @return true或false
	 */
	public boolean addElement(String parentNodeName, String addElementName, String text) {
		try {
			// 获得父节点
			Element parentEle = getNode(parentNodeName);
			// 被添加的节点 element为节点的名称
			Element addEle = DocumentHelper.createElement(addElementName);
			addEle.addText(new String(text.getBytes(), "utf-8"));
			parentEle.add(addEle);
			writeDocument();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存Xml文档 <br>
	 * 保存修改的文档 部署在tomcat中出现中文乱码现象<br>
	 */
	private void writeDocument() {
		XMLWriter writer = null;
		try {
			// 格式化输出
			OutputFormat outFormat = OutputFormat.createPrettyPrint();
			outFormat.setEncoding("utf-8");
			writer = new XMLWriter(new FileWriter(fileName), outFormat);
			writer.write(doc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeXmlWriter(writer);
		}
	}
	/**
	 * 关闭XMLWriter
	 * @param writer XMLWriter实例
	 */
	private void closeXmlWriter(XMLWriter writer){
		try {
			if (null != writer) {
				writer.close();
			}
		} catch (Exception e) {
			logger.error("关闭XMLWriter失败");
			e.printStackTrace();
		}
	}
}
