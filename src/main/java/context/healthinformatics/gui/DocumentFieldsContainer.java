package context.healthinformatics.gui;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class to keep track of all fields of a document for the form.
 */
public class DocumentFieldsContainer {
	private JTextField documentName;
	private JComboBox<String> documentType;
	private JTextField documentPath;
	private JTextField startLine;
	private JTextField sheet;
	private JTextField delimiter;
	private ArrayList<ColumnFieldContainer> columnFields;
	private String[] doctypes = { "Excel", "txt/csv" };

	/**
	 * Build a DocumentField container with a XMLDocument.
	 * 
	 * @param xmlDoc
	 *            the xml document
	 */
	public DocumentFieldsContainer(XMLDocument xmlDoc) {
		this.documentName = new JTextField(xmlDoc.getDocName());
		this.documentType = new JComboBox<>(doctypes);
		this.documentType
				.setSelectedIndex(getComboBoxIndex(xmlDoc.getDocType()));
		this.documentPath = new JTextField(xmlDoc.getPath());
		this.startLine = new JTextField(Integer.toString(xmlDoc.getStartLine()));
		this.delimiter = new JTextField(Integer.toString(xmlDoc.getSheet()));
		columnFields = new ArrayList<ColumnFieldContainer>();
		initColumns(xmlDoc.getColumns());
	}

	/**
	 * Init the columnfield containers for all the columns in the xmldocument.
	 * 
	 * @param cols
	 *            the columns
	 */
	public void initColumns(ArrayList<Column> cols) {
		for (int i = 0; i < cols.size(); i++) {
			columnFields.add(new ColumnFieldContainer(cols.get(i)));
		}
	}

	/**
	 * Get the index of the document type for the combobox.
	 * 
	 * @param doctype
	 *            the type of the document
	 * @return the index
	 */
	public int getComboBoxIndex(String doctype) {
		if (doctype.toLowerCase().equals("excel")) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Constructor for a text document.
	 * 
	 * @param documentName
	 *            the document name
	 * @param documentTypes
	 *            the document type
	 * @param documentPath
	 *            the document path
	 * @param startLine
	 *            the start line
	 * @param delimiter
	 *            the delimiter of the textfile
	 * @param comboIndex
	 *            the index of the selected combobox value
	 */
	public DocumentFieldsContainer(String documentName, String[] documentTypes,
			String documentPath, int startLine, String delimiter, int comboIndex) {
		this.documentName = new JTextField(documentName);
		this.documentType = new JComboBox<>(documentTypes);
		this.documentType.setSelectedIndex(comboIndex);
		this.documentPath = new JTextField(documentPath);
		this.startLine = new JTextField(Integer.toString(startLine));
		this.delimiter = new JTextField(delimiter);
		columnFields = new ArrayList<ColumnFieldContainer>();
	}

	/**
	 * Constructor for an excel document.
	 * 
	 * @param documentName
	 *            the document name
	 * @param documentTypes
	 *            the document type
	 * @param documentPath
	 *            the document path
	 * @param startLine
	 *            the start line
	 * @param sheet
	 *            the number of the sheet
	 * @param comboIndex
	 *            the index of the selected combobox value
	 */
	public DocumentFieldsContainer(String documentName, String[] documentTypes,
			String documentPath, int startLine, int sheet, int comboIndex) {
		this.documentName = new JTextField(documentName);
		this.documentType = new JComboBox<>(documentTypes);
		this.documentType.setSelectedIndex(comboIndex);
		this.documentPath = new JTextField(documentPath);
		this.startLine = new JTextField(Integer.toString(startLine));
		this.delimiter = new JTextField(Integer.toString(sheet));
		columnFields = new ArrayList<ColumnFieldContainer>();
	}

	/**
	 * Build a new XMLDocument from the values of the input fields.
	 * 
	 * @return a xmldocument
	 */
	public XMLDocument getXMLDocument() {
		ArrayList<Column> cols = new ArrayList<Column>();
		for (int i = 0; i < cols.size(); i++) {
			cols.add(columnFields.get(i).getColumn());
		}
		return new XMLDocument(getDocumentTypeValue(), getDocumentNameValue(),
				getDelimiterValue(), getDocumentPathValue(),
				getDocumentStartLineValue(), getSheetValue(), cols);
	}

	/**
	 * Add a columnfield to the documentcontainer.
	 * 
	 * @param columnField
	 *            a field with the values of a column
	 */
	public void addColumnField(ColumnFieldContainer columnField) {
		columnFields.add(columnField);
	}

	/**
	 * Get the type of the document.
	 * 
	 * @return the selected type of the document
	 */
	public String getTypeOfDocument() {
		return documentType.getSelectedItem().toString();
	}

	/**
	 * The value of the document name field.
	 * 
	 * @return the document name value
	 */
	public String getDocumentNameValue() {
		return documentName.getText();
	}

	/**
	 * The value of the selected document type.
	 * 
	 * @return the document type
	 */
	public String getDocumentTypeValue() {
		return documentType.getSelectedItem().toString();
	}

	/**
	 * The value of the document path.
	 * 
	 * @return the document path value
	 */
	public String getDocumentPathValue() {
		return documentPath.getText();
	}

	/**
	 * Get the value of the start line.
	 * 
	 * @return the start line value
	 */
	public int getDocumentStartLineValue() {
		return Integer.parseInt(documentPath.getText());
	}

	/**
	 * Get the value of the sheet.
	 * 
	 * @return the sheet value
	 */
	public int getSheetValue() {
		return Integer.parseInt(sheet.getText());
	}

	/**
	 * Get the delimiter value.
	 * 
	 * @return the value of the delimiter
	 */
	public String getDelimiterValue() {
		return delimiter.getText();
	}

	/**
	 * @return the documentName
	 */
	public JTextField getDocumentName() {
		return documentName;
	}

	/**
	 * @return the documentType
	 */
	public JComboBox<String> getDocumentType() {
		return documentType;
	}

	/**
	 * @return the documentPath
	 */
	public JTextField getDocumentPath() {
		return documentPath;
	}

	/**
	 * @return the startLine
	 */
	public JTextField getStartLine() {
		return startLine;
	}

	/**
	 * @return the sheet
	 */
	public JTextField getSheet() {
		return sheet;
	}

	/**
	 * @return the delimiter
	 */
	public JTextField getDelimiter() {
		return delimiter;
	}

	/**
	 * @return the columnFields
	 */
	public ArrayList<ColumnFieldContainer> getColumnFields() {
		return columnFields;
	}

}
