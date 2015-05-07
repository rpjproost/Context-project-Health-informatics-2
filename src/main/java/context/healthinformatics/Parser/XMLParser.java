package context.healthinformatics.Parser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reading XML files with specifications for other files that must be read.
 */
public class XMLParser extends Parser {
	
	private Document doc;
	private String docName;
	private String delimiter;
	private String path;
	private int startLine;
	private ArrayList<Column> columns;

	/**
	 * Creates a parser for the data in the file.
	 * 
	 * @param fileName
	 *            location where the file is stored.
	 */
	public XMLParser(String fileName) {
		super(fileName);
		columns = new ArrayList<Column>();
		startLine = 1;
	}

	/**
	 * Parse the XML file to objects.
	 */
	@Override
	public void parse() throws IOException {
		try {

			File xml = new File(getFileName());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xml);

			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getChildNodes();
			for(int i = 0; i < nList.getLength(); i++){
				parseDocument(nList.item(i));
			}
		} catch (ParserConfigurationException | SAXException e) {
			throw new FileNotFoundException();
		}
	}

	/**
	 * Parse nodes to Column objects.
	 * 
	 * @param nList
	 *            list with Nodes.
	 */
	private void parseColumns(NodeList nList) {
		for (int i = 0; i < nList.getLength(); i++) {
			Node nColumn = nList.item(i);
			if (nColumn.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) nColumn;
				int id = Integer.parseInt(e.getAttribute("id"));
				String cName = getString(e, "name");
				String cType = getString(e, "type");
				Column c = new Column(id, cName, cType);
				columns.add(c);
			}
		}
	}
	
	/**
	 * Method that handles parsing for a single document.
	 * @param n the document node.
	 * @throws IOException
	 */
	private void parseDocument(Node n) throws IOException {
		Element e = (Element)n;
		setDocName(e.getAttribute("docname"));
		setPath(getString(e, "path"));
		setStartLine(getInt("start"));
		Parser p;
		NodeList columnList = e.getElementsByTagName("column");
		parseColumns(columnList);
		String type = getString(e,"doctype").toLowerCase();
		if (type.equals("text")) {
			setDelimiter(getString(e, "delimiter"));
			p = new TXTParser(getPath(),getStartLine(),getDelimiter(),getColumns());
		}
		else if (type.equals("excel")) {
			p = new ExcelParser(getPath(),getStartLine(),getColumns());
		}
		else {
			p = null;
		}
		p.parse();
	}

	/**
	 * Get a Integer at the place of the tag in the xml.
	 * When there isn't a Integer, it will be set on a default 1.
	 * @param s the tag in the xml
	 * @return int or default 1
	 */
	private int getInt(String s) {
		try {
			return Integer.parseInt(getString(null, s));
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	/**
	 * Get a String at the place of the tag in the xml.
	 * @param e a specific element in the xml, 
	 * 		when you want to search the whole document fill in null.
	 * @param s the tag you are looking for.
	 * @return the String in the tag.
	 */
	private String getString(Element e, String s) {
		String res = "";
		if(e != null) {
			res = e.getElementsByTagName(s).item(0).getTextContent();
		} else {
			res = doc.getElementsByTagName(s).item(0).getTextContent();
		}
		if(res.isEmpty()) {
			throw new NullPointerException();
		}
		return res;
	}

	/**
	 * @return document name.
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * Set the current document name to a new one.
	 * 
	 * @param docName
	 *            the new name.
	 */
	void setDocName(String docName) {
		this.docName = docName;
	}

	/**
	 * Get the delimiter which is located in the XML file.
	 * 
	 * @return the delimiter.
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * Set the current delimiter to a new delimiter.
	 * 
	 * @param delimiter
	 *            the new delimiter.
	 */
	void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Get the source path to the document which the XML is for.
	 * 
	 * @return a source path as String.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the current path to a new one.
	 * 
	 * @param path
	 *            the new source path.
	 */
	void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get the number where another document must start to read.
	 * 
	 * @return the start line.
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Set the starting line to a new int.
	 * 
	 * @param startLine
	 *            the new starting line.
	 */
	void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	/**
	 * @return ArrayList with created Columns.
	 */
	public ArrayList<Column> getColumns() {
		return columns;
	}

}
