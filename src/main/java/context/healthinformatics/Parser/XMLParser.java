package context.healthinformatics.Parser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLParser extends Parser {

	public XMLParser(String fileName) {
		super(fileName);
	}

	@Override
	public void parse() {
		try {

			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getAttribute("id"));
			System.out.println("Path document :"+ doc.getElementsByTagName("path").item(0).getTextContent());
			System.out.println("Starting line :"+ doc.getElementsByTagName("start").item(0).getTextContent());

			NodeList columnList = doc.getElementsByTagName("column");
			parseColumns(columnList);

			System.out.println("----------------------------");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseColumns(NodeList nList) {
		for (int i = 0; i < nList.getLength(); i++) {
			Node nColumn = nList.item(i);
			System.out.println("\nCurrent Element :" + nColumn.getNodeName());
			if (nColumn.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nColumn;
				System.out.println("Column id : " + e.getAttribute("id"));
				System.out.println("Name of the column : " + e.getElementsByTagName("name").item(0).getTextContent());
				System.out.println("Type of the column : " + e.getElementsByTagName("type").item(0).getTextContent());
			}
		}
	}

}
