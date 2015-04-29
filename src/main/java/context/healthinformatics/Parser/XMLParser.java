package context.healthinformatics.Parser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;

/**
 * Reading XML files with specifications for other files that must be read.
 */
public class XMLParser extends Parser {

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
	public void parse() {
		try {

			File xml = new File(getFileName());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			setDocName(doc.getDocumentElement().getAttribute("id"));
			setPath(doc.getElementsByTagName("path").item(0).getTextContent());
			setStartLine(Integer.parseInt(doc.getElementsByTagName("start")
					.item(0).getTextContent()));
			setDelimiter(doc.getElementsByTagName("delimiter").item(0)
					.getTextContent());
			NodeList columnList = doc.getElementsByTagName("column");
			parseColumns(columnList);

		} catch (Exception e) {
			System.out.println(e.toString());
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
				String cName = e.getElementsByTagName("name").item(0).getTextContent();
				String cType = e.getElementsByTagName("type").item(0).getTextContent();
				Column c = new Column(id, cName, cType);
				columns.add(c);
			}
		}
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
