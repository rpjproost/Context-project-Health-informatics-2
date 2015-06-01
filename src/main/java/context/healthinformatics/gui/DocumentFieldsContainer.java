package context.healthinformatics.gui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class to keep track of all fields of a document for the form.
 */
public class DocumentFieldsContainer extends InterfaceHelper implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField documentName;
	private JComboBox<String> documentType;
	private JTextField documentPath;
	private JTextField startLine;
	private JTextField sheet;
	private JTextField delimiter;
	private ArrayList<ColumnFieldContainer> columnFields;
	private String[] doctypes = { "Excel", "txt/csv" };

	private JPanel columnFormPanel;
	private JButton addColumnButton = new JButton("Add new Column");
	private JButton removeColumnButton = new JButton("Remove column");
	private XMLEditor xmledit;

	/**
	 * Build a DocumentField container with a XMLDocument.
	 * 
	 * @param xmlDoc
	 *            the xml document
	 * @param xmledit
	 *            the xml editor
	 */
	public DocumentFieldsContainer(XMLDocument xmlDoc, XMLEditor xmledit) {
		this.xmledit = xmledit;
		columnFormPanel = new JPanel();
		addColumnButton.addActionListener(this);
		removeColumnButton.addActionListener(this);
		columnFormPanel.setLayout(new GridBagLayout());
		this.documentName = new JTextField(xmlDoc.getDocName());
		this.documentType = new JComboBox<>(doctypes);
		this.documentType
				.setSelectedIndex(getComboBoxIndex(xmlDoc.getDocType()));
		this.documentPath = new JTextField(xmlDoc.getPath());
		this.startLine = new JTextField(Integer.toString(xmlDoc.getStartLine()));
		initSheetOrDelimiterField(xmlDoc.getDocType(), xmlDoc);
		columnFields = new ArrayList<ColumnFieldContainer>();
		initColumns(xmlDoc.getColumns());
	}

	/**
	 * Init one of the two textfields bases on the document type.
	 * 
	 * @param docType
	 *            the type of the document
	 * @param xmlDoc
	 *            the xml document
	 */
	public void initSheetOrDelimiterField(String docType, XMLDocument xmlDoc) {
		if (docType.toLowerCase().equals("excel")) {
			this.sheet = new JTextField(Integer.toString(xmlDoc.getSheet()));
		} else {
			this.delimiter = new JTextField(xmlDoc.getDelimiter());
		}
	}

	/**
	 * Init the columnfield containers for all the columns in the xmldocument.
	 * 
	 * @param cols
	 *            the columns
	 */
	public void initColumns(ArrayList<Column> cols) {
		for (int i = 0; i < cols.size(); i++) {
			columnFields.add(new ColumnFieldContainer(cols.get(i), xmledit));
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

	/**
	 * Get the Panel for the form.
	 * 
	 * @return the panel for the columns.
	 */
	public JPanel getColumnFormPanel() {
		return columnFormPanel;
	}

	/**
	 * Get the button to add a column.
	 * 
	 * @return the button
	 */
	public JButton getAddColumnButton() {
		return addColumnButton;
	}

	/**
	 * Get the button to remove a column.
	 * 
	 * @return the button
	 */
	public JButton getRemoveColumnButton() {
		return removeColumnButton;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addColumnButton) {
			columnFields.size();
			ColumnFieldContainer cfc = new ColumnFieldContainer(new Column(-1,
					"", ""), xmledit);
			columnFields.add(cfc);
			columnFormPanel.add(xmledit.createColumnForm(cfc),
					setGrids(0, columnFields.size()));
			columnFormPanel.revalidate();
		} else if (e.getSource() == removeColumnButton) {
			if (columnFields.size() > 0) {
				ColumnFieldContainer cfc = columnFields.remove(columnFields
						.size() - 1);
				cfc.getPanel().setVisible(false);
			}
		}

	}

}
