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
	 * Creates an empty XMLDocument.
	 * Integers are set to -1, String to "" and other objects null.
	 */
	public XMLDocument() {
		this.startLine = -1;
		this.sheet = -1;
		this.docType = "";
		this.docName = "";
		this.path = "";
		this.delimiter = "";
		this.columns = null;
	}

	/**
	 * @return the docType
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * @param docType the docType to set
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * @return the docName
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * @param docName the docName to set
	 */
	public void setDocName(String docName) {
		this.docName = docName;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
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
	 * @param startLine the startLine to set
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	/**
	 * @return the sheet
	 */
	public int getSheet() {
		return sheet;
	}

	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(int sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return the columns
	 */
	public ArrayList<Column> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}
}
