package context.healthinformatics.Writer;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import context.healthinformatics.Parser.Column;

/**
 * Writes Document classes into a xml file.
 */
public class XMLWriter {
	
	private ArrayList<XMLDocument> docs;
	private Document doc;
	private Transformer transformer;

	/**
	 * Constructor needs xml documents to write.
	 * @param documents the documents that must be written.
	 */
	public XMLWriter(ArrayList<XMLDocument> documents) {
		docs = documents;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			transformer = transformerFactory.newTransformer();
		} catch (ParserConfigurationException e) {
			e.printStackTrace(); // TODO exception
		} catch (TransformerConfigurationException e) {
			e.printStackTrace(); // TODO exception
		}
	}

	/**
	 * Writes the data to a XML location.
	 * 
	 * @param fileDir
	 *            the directory where the xml is written to.
	 */
	public void writeXML(String fileDir) {
		for (int i = 0; i < docs.size(); i++) {
			writeDocument(docs.get(i));
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileDir));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace(); // TODO exception
		}
	}

	/**
	 * Write one single document to xml format.
	 * @param xmlDocument the specific document.
	 */
	private void writeDocument(XMLDocument xmlDocument) {
		Element rootElement = doc.createElement("document");
		rootElement = setAttribute(rootElement, "docname", xmlDocument.getDocName());
		doc.appendChild(rootElement);
		
		appendElement(rootElement, "doctype", xmlDocument.getDocType());
		appendElement(rootElement, "path", xmlDocument.getPath());
		appendElement(rootElement, "start", xmlDocument.getStartLine() + "");
		appendIfElement(rootElement, "sheet", xmlDocument.getSheet() + "", 
				xmlDocument.getDocType(), "excel");
		appendElement(rootElement, "delimiter", xmlDocument.getDelimiter());
		appendColumns(rootElement, xmlDocument.getColumns());
	}

	/**
	 * Append the columns to the root element.
	 * @param parent the root element where will be appended to.
	 * @param columns the columns that must be written in xml format.
	 */
	private void appendColumns(Element parent, ArrayList<Column> columns) {
		for (int i = 0; i < columns.size(); i++) {
			Element column = doc.createElement("column");
			Column current = columns.get(i);
			column = setAttribute(column, "id", current.getColumnNumber() + "");
			parent.appendChild(column);
			appendElement(column, "name", current.getColumnName());
			appendElement(column, "type", current.getColumnType());
			appendIfElement(column, "dateFormat", current.getDateType(), 
					current.getColumnType(), "date");
		}
	}

	/**
	 * Set the attribute to a specific element.
	 * @param parent the root element where will be appended to.
	 * @param id the name of the attribute.
	 * @param data the data of the attribute.
	 * @return element with attribute.
	 */
	private Element setAttribute(Element element, String id, String data) {
		Attr attr = doc.createAttribute(id);
		attr.setValue(data);
		element.setAttributeNode(attr);
		return element;		
	}
	
	/**
	 * Appends an element only if the statement is true, else do nothing.
	 * @param parent the root element where will be appended to.
	 * @param id the name of the element.
	 * @param data the data of the element.
	 * @param ifStat first part of the if-statement.
	 * @param equalsTo where the if must be equals to.
	 */
	private void appendIfElement(Element parent, String id, String data,
			String ifStat, String equalsTo) {
		if (ifStat.toLowerCase().equals(equalsTo)) {
			appendElement(parent, id, data);
		}
		
	}

	/**
	 * 
	 * @param parent the root element where will be appended to.
	 * @param id the name of the element.
	 * @param data the data of the element.
	 */
	private void appendElement(Element parent, String id, String data) {
		Element element = doc.createElement(id);
		element.appendChild(doc.createTextNode(data));
		parent.appendChild(element);		
	}
}
