package context.healthinformatics.Writer;

import java.util.ArrayList;

import org.w3c.dom.Document;

import context.healthinformatics.Parser.Column;

/**
 * Class document to store the info of a document in the XML file.
 */
public class XMLDocument {
	private Document doc;
	private String docName;
	private String delimiter;
	private String path;
	private int startLine;
	private int sheet;
	private ArrayList<Column> columns;

	/**
	 * Constructor for the XML document.
	 * @param doc the document
	 * @param docName the name of the document
	 * @param delimiter the delimiter
	 * @param path the path to the document
	 * @param start the start  variable
	 * @param columns the list of columns
	 */
	public XMLDocument(Document doc, String docName, String delimiter,
			String path, int start, ArrayList<Column> columns) {
		if (doc.equals("excel")) {
			this.sheet = start;
			this.startLine = -1;
		} else {
			this.startLine = start;
			this.sheet = -1;
		}
		this.doc = doc;
		this.docName = docName;
		this.delimiter = delimiter;
		this.columns = columns;
	}

}
