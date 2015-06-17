package context.healthinformatics.writer;

import java.util.ArrayList;

import context.healthinformatics.parser.Column;

/**
 * Class document to store the info of a document in the XML file.
 */
public class XMLDocument {
	private String docType;
	private String docName;
	private String delimiter;
	private String path;
	private int startLine;
	private int sheet;
	private ArrayList<Column> columns;

	/**
	 * Constructor for the XML document.
	 * 
	 * @param docType
	 *            the type of the document
	 * @param docName
	 *            the name of the document
	 * @param delimiter
	 *            the delimiter
	 * @param path
	 *            the path to the document
	 * @param startLine
	 *            the start line variable
	 * @param sheet
	 *            the current sheet
	 * @param columns
	 *            the list of columns
	 */
	public XMLDocument(String docType, String docName, String delimiter,
			String path, int startLine, int sheet, ArrayList<Column> columns) {
		this.startLine = startLine;
		this.sheet = sheet;
		this.docType = docType;
		this.docName = docName;
		this.path = path;
		this.delimiter = delimiter;
		this.columns = columns;
	}

	/**
	 * Creates an empty XMLDocument. Integers are set to 1, String to "" and
	 * column is auto-generated with one date column.
	 */
	public XMLDocument() {
		this.startLine = 1;
		this.sheet = 1;
		this.docType = "";
		this.docName = "";
		this.path = "path/file.txt";
		this.delimiter = "";
		Column c = new Column(0, "date", "Date");
		c.setDateType("yyMMdd");
		this.columns = new ArrayList<Column>();
		columns.add(c);
	}

	/**
	 * @return the docType
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Set the path of the document.
	 * 
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * @return the sheet
	 */
	public int getSheet() {
		return sheet;
	}

	/**
	 * @return the columns
	 */
	public ArrayList<Column> getColumns() {
		return columns;
	}
}
