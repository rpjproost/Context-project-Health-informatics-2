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
	 * Get the type of the document.
	 * 
	 * @return the document type
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * get the name of the document.
	 * 
	 * @return the name of the document
	 */
	public String getDocName() {
		return docName;
	}

	/**
	 * Get the delimiter of the document.
	 * 
	 * @return the delimiter of the document
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * Get the path of where the file is stored.
	 * 
	 * @return the path of the file
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Get the starting line of the document.
	 * 
	 * @return the start line
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Get the start sheet of the document.
	 * 
	 * @return the sheet of the document
	 */
	public int getSheet() {
		return sheet;
	}

	/**
	 * Get the list of columns.
	 * 
	 * @return the list of columns
	 */
	public ArrayList<Column> getColumns() {
		return columns;
	}

}
