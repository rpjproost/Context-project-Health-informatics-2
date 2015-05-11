package context.healthinformatics.Parser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Database.SingletonDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
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
	private int sheet;
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
	 * @throws SQLException 
	 * @throws InvalidFormatException 
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
			NodeList nList = doc.getChildNodes().item(0).getChildNodes();
			for (int i = 0; i < nList.getLength(); i++) {
				if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					parseDocument(nList.item(i));
				}
			}
		} catch (ParserConfigurationException | SAXException 
				| InvalidFormatException | SQLException e) {
			throw new FileNotFoundException();
		}
	}

	/**
	 * Parse nodes to Column objects.
	 * 
	 * @param nList
	 *            list with Nodes.
	 */
	protected void parseColumns(NodeList nList) {
		columns = new ArrayList<Column>(); 
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
	 * @throws InvalidFormatException 
	 * @throws SQLException 
	 */
	private void parseDocument(Node n) throws IOException, InvalidFormatException, SQLException {
		Element e = (Element) n;
		setDocName(e.getAttribute("docname"));
		setPath(getString(e, "path"));
		setStartLine(getInt("start"));
		NodeList columnList = e.getElementsByTagName("column"); 
		parseColumns(columnList);
		setDelimiter(getString(e, "delimiter"));
		setSheet(getInt(getString(e, "sheet")));
		createTableDb();
		getParser(getString(e, "doctype")).parse();
		
	}
	
	/**
	 * Returns the Parser with the correct settings.
	 * @param label the String with the parser to use.
	 * @return The Parser with all settings.
	 */
	protected Parser getParser(String label) {
		switch (label.toLowerCase()) {
		case "text" : return new TXTParser(getPath(), getStartLine(), getDelimiter(), getColumns()
				, getDocName());
		case "excel" : return new ExcelParser(getPath(), getStartLine(), getColumns(), getSheet());
		case "csv" : return new TXTParser(getPath(), 1, ";" , getColumns(), getDocName());
		default : return null;
		}		
	}
	
	private void createTableDb() throws SQLException {
		int length = columns.size();
		Db data = SingletonDb.getDb();
		String[] col = new String[length];
		String[] t = new String[length];
		for (int i = 0; i < length; i++) {
			col[i] = columns.get(i).getColumnName();
			t[i] = columns.get(i).getColumnType();
		}
		data.createTable(docName, col, t);
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
		} catch (Exception e) {
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
		if (e != null) {
			NodeList nList = e.getElementsByTagName(s);
			if (nList.item(0) != null) {
				res = nList.item(0).getTextContent();
			}
			
		} else {
			res = doc.getElementsByTagName(s).item(0).getTextContent();
		}
		if (res.isEmpty()) {
			res = null;
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

	/**
	 * getter for sheetNumber.
	 * @return sheet number
	 */
	public int getSheet() {
		return sheet;
	}

	/**
	 * setter for sheetNumber.
	 * @param sheet sheetNumber to set.
	 */
	public void setSheet(int sheet) {
		this.sheet = sheet;
	}

}

/**
 * Class used for testing the XMLParser.
 * Stops the parsing after the document.
 */
class XMLTestParser extends XMLParser {

	/**
	 * list of parsers.
	 */
	private ArrayList<Parser> parsers;
	/**
	 * constructor for xmlTestParser.
	 * @param fileName path to file.
	 */
	public XMLTestParser(String fileName) {
		super(fileName);
		parsers = new ArrayList<Parser>();
	}
	
	/**
	 * Returns a dumy parser and saves the parser genrated
	 * by the XMLParser class to an arrayList.
	 * @param label the String with the parser to use.
	 * @return The Parser with all settings.
	 */
	@Override
	protected Parser getParser(String label) {
		parsers.add(super.getParser(label));
		Parser p = new TXTParser(null, 0, null, null, null) {
			@Override
			public void parse() { };
		};
		return p;
	}

	/**
	 * getter for the arrayList of parsers.
	 * @return list of parsers.
	 */
	public ArrayList<Parser> getParsers() {
		return parsers;
	}	
}
