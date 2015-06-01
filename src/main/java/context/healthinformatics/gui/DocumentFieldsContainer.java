package context.healthinformatics.gui;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

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
	public String getDocumentStartLineValue() {
		return documentPath.getText();
	}

	/**
	 * Get the value of the sheet.
	 * 
	 * @return the sheet value
	 */
	public String getSheetValue() {
		return sheet.getText();
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
