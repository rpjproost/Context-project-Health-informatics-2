package context.healthinformatics.interfacecomponents;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * Class to keep track of all fields of a document for the form.
 */
public class DocumentFieldsContainer extends InterfaceHelper implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int FORM_ELEMENT_HEIGHT = 25;
	private static final int XML_EDITOR_WIDTH = 800;

	private JTextField documentName;
	private JComboBox<String> documentType;
	private JTextField documentPath;
	private JTextField startLine;
	private JTextField sheet;
	private JTextField delimiter;
	private ArrayList<ColumnFieldContainer> columnFields;
	private String[] doctypes = { "Excel", "Text", "Csv" };

	private JPanel columnFormPanel;
	private JPanel documentFormPanel;
	private JPanel panelForDocTypeSpecificInput;

	private JButton addColumnButton = new JButton("Add new Column");
	private JButton removeColumnButton = new JButton("Remove column");
	private XMLEditor xmledit;

	/**
	 * Build a DocumentField container with a XMLDocument.
	 * 
	 * @param xmlDoc
	 *            the XML document
	 * @param xmledit
	 *            the XML editor
	 */
	public DocumentFieldsContainer(XMLDocument xmlDoc, XMLEditor xmledit) {
		this.xmledit = xmledit;
		initPanels();
		initTextFieldsWithValues(xmlDoc);
		initSheetOrDelimiterField(xmlDoc.getDocType(), xmlDoc);
		columnFields = new ArrayList<ColumnFieldContainer>();
		initColumns(xmlDoc.getColumns());
		addActionListeners();
	}

	/**
	 * Initialize the panels for the document fields.
	 */
	private void initPanels() {
		columnFormPanel = new JPanel();
		columnFormPanel.setLayout(new GridBagLayout());
		documentFormPanel = new JPanel();
		documentFormPanel.setLayout(new GridBagLayout());
	}

	/**
	 * Add all action listeners.
	 */
	private void addActionListeners() {
		addColumnButton.addActionListener(this);
		removeColumnButton.addActionListener(this);
		documentType.addActionListener(this);
	}

	/**
	 * Initialize the JTextFields with the given values.
	 * 
	 * @param xmlDoc
	 *            the document with the values
	 */
	private void initTextFieldsWithValues(XMLDocument xmlDoc) {
		this.documentName = new JTextField(xmlDoc.getDocName());
		this.documentType = new JComboBox<>(doctypes);
		this.documentType
				.setSelectedIndex(getComboBoxIndex(xmlDoc.getDocType()));
		this.documentPath = new JTextField(xmlDoc.getPath());
		this.startLine = new JTextField(Integer.toString(xmlDoc.getStartLine()));
	}

	/**
	 * Initialize one of the two TextFields bases on the document type.
	 * 
	 * @param docType
	 *            the type of the document
	 * @param xmlDoc
	 *            the XML document
	 */
	private void initSheetOrDelimiterField(String docType, XMLDocument xmlDoc) {
		if (docType.toLowerCase().equals("excel")) {
			this.sheet = new JTextField(Integer.toString(xmlDoc.getSheet()));
			this.delimiter = new JTextField(",");
		} else {
			this.delimiter = new JTextField(xmlDoc.getDelimiter());
			this.sheet = new JTextField("1");
		}
	}

	/**
	 * Initialize the ColumnField containers for all the columns in the
	 * XMLDocument.
	 * 
	 * @param cols
	 *            the columns
	 */
	private void initColumns(ArrayList<Column> cols) {
		for (int i = 0; i < cols.size(); i++) {
			columnFields.add(new ColumnFieldContainer(cols.get(i)));
		}
	}

	/**
	 * Get the index of the document type for the ComboBox.
	 * 
	 * @param doctype
	 *            the type of the document
	 * @return the index
	 */
	protected int getComboBoxIndex(String doctype) {
		if (doctype.toLowerCase().equals("excel")) {
			return 0;
		} else if (doctype.toLowerCase().equals("csv")) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * Build a new XMLDocument from the values of the input fields.
	 * 
	 * @return a XMLDocument
	 */
	public XMLDocument getXMLDocument() {
		ArrayList<Column> cols = new ArrayList<Column>();
		for (int i = 0; i < columnFields.size(); i++) {
			cols.add(columnFields.get(i).getColumn());
		}
		return getCorrect(cols);
	}

	/**
	 * Get the XMLDocument with the excel values or txt/csv values.
	 * 
	 * @param columns
	 *            the columns of the document
	 * @return the XMLDocument object
	 */
	private XMLDocument getCorrect(ArrayList<Column> columns) {
		if (getDocumentTypeValue().toLowerCase().equals("excel")) {
			return new XMLDocument(getDocumentTypeValue(),
					getDocumentNameValue(), "", getDocumentPathValue(),
					Integer.parseInt(getDocumentStartLineValue()),
					Integer.parseInt(getSheetValue()), columns);
		} else {
			return new XMLDocument(getDocumentTypeValue(),
					getDocumentNameValue(), getDelimiterValue(),
					getDocumentPathValue(),
					Integer.parseInt(getDocumentStartLineValue()), -1, columns);
		}
	}

	/**
	 * Get the panel where all the elements for this document are added on.
	 * 
	 * @return the document Panel.
	 */
	public JPanel getDocumentFormPanel() {
		return documentFormPanel;
	}

	/**
	 * Add a ColumnField to the DocumentContainer.
	 * 
	 * @param columnField
	 *            a field with the values of a column
	 */
	private void addColumnField(ColumnFieldContainer columnField) {
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
	public String getDocumentStartLineValue() {
		return startLine.getText();
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

	/**
	 * Get the panel for the delimiter or sheet.
	 * 
	 * @return the panel
	 */
	public JPanel getPanelForDocTypeSpecificInput() {
		return panelForDocTypeSpecificInput;
	}

	/**
	 * Set the panel for the delimiter or sheet.
	 * 
	 * @param panelForDocTypeSpecificInput
	 *            the panel of the delimiter or sheet
	 */
	public void setPanelForDocTypeSpecificInput(
			JPanel panelForDocTypeSpecificInput) {
		this.panelForDocTypeSpecificInput = panelForDocTypeSpecificInput;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addColumnButton) {
			handleAddColumnButton();
		} else if (e.getSource() == removeColumnButton) {
			handleRemoveColumnButton();
		} else if (e.getSource() == documentType) {
			handleDoctypeDropDown();
		}
	}

	/**
	 * Handle the ActionListener of the addColumnButton.
	 */
	private void handleAddColumnButton() {
		ColumnFieldContainer cfc = new ColumnFieldContainer(new Column(-1, "",
				""));
		addColumnField(cfc);
		columnFormPanel.add(xmledit.createColumnForm(cfc),
				setGrids(0, columnFields.size()));
		columnFormPanel.revalidate();
	}

	/**
	 * Handle the ActionListener of the removeColumnButton.
	 */
	private void handleRemoveColumnButton() {
		if (columnFields.size() > 0) {
			ColumnFieldContainer cfc = columnFields
					.remove(columnFields.size() - 1);
			cfc.getPanel().setVisible(false);
		}
	}

	/**
	 * Handle the ActionListener of the DoctypeButton DropDown.
	 */
	private void handleDoctypeDropDown() {
		String selectedItem = documentType.getSelectedItem().toString();
		if (selectedItem.equals("Excel")) {
			changeLastDocumentRow("Document sheet: ", getSheet());
		} else {
			changeLastDocumentRow("Document delimiter: ", getDelimiter());
		}
	}

	/**
	 * Changes the last row from delimiter to sheet.
	 * 
	 * @param labelName
	 *            the name of the label: sheet or delimiter
	 * @param theTextField
	 *            one of the two textfields
	 */
	private void changeLastDocumentRow(String labelName, JTextField theTextField) {
		panelForDocTypeSpecificInput.setVisible(false);
		JPanel testPanel = makeFormRowPanelWithTextField(labelName,
				theTextField, XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT);
		documentFormPanel.add(testPanel, setGrids(0, 1));
		setPanelForDocTypeSpecificInput(testPanel);
		documentFormPanel.revalidate();
	}
}
