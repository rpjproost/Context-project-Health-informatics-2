package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import context.healthinformatics.Parser.Column;
import context.healthinformatics.Writer.XMLDocument;

/**
 * XMLEditor class makes a panel which is filled with a form to edit xml files.
 */
public class XMLEditor extends InterfaceHelper {

	private static final long serialVersionUID = 1L;

	private static final int PARENTWIDTH = 900;
	private static final int PARENTHEIGHT = 700;

	private static final int FORMELEMENTWIDTH = 800;
	private static final int FORMELEMNTHEIGHT = 25;

	private static final int COLUMNPANELHEIGHT = 75;

	private static final int MARGINTOP = 10;

	private static final int THREE = 3;
	private static final int FOUR = 4;

	private JPanel containerScrollPanel;
	private int numberOfXMLDocuments;
	private JScrollPane scrollPane;

	/**
	 * Empty Constructor of the XMLEditor.
	 */
	public XMLEditor() {
		containerScrollPanel = createContainerPanel();
		numberOfXMLDocuments = 0;
		scrollPane = makeScrollPaneForContainerPanel(containerScrollPanel);
	}

	/**
	 * Load the parent panel.
	 * 
	 * @return the parent panel
	 */
	public JPanel loadPanel() {
		JPanel parentPanel = new JPanel();
		parentPanel.add(scrollPane);
		return parentPanel;
	}

	/**
	 * Add an XMLDocument object the containerpanel with the scrollbar.
	 * 
	 * @param xmlDocument
	 *            the xmldocument which is read
	 */
	public void addXMLDocumentToContainerScrollPanel(XMLDocument xmlDocument) {
		GridBagConstraints c = setGrids(0, numberOfXMLDocuments);
		// margin top
		c.insets = new Insets(MARGINTOP, 0, 0, 0);
		containerScrollPanel.add(createDocumentPanel(xmlDocument), c);
		numberOfXMLDocuments++;
		scrollPane.revalidate();
	}

	/**
	 * Create a container panel for the document of xml files to container.
	 * 
	 * @return the panel to container the documents
	 */
	public JPanel createContainerPanel() {
		JPanel parentPanel = new JPanel();
		parentPanel.setMinimumSize(new Dimension(PARENTWIDTH, PARENTHEIGHT));
		parentPanel.setLayout(new GridBagLayout());
		return parentPanel;
	}

	/**
	 * Create a panel for a xml document.
	 * 
	 * @param xmlDocument
	 *            the xml document which is loaded in
	 * @return the panel with the added components
	 */
	public JPanel createDocumentPanel(XMLDocument xmlDocument) {
		JPanel documentPanel = new JPanel();
		documentPanel.setLayout(new GridBagLayout());
		documentPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		// other setting fields
		if (xmlDocument.getDocType().toLowerCase().equals("excel")) {
			documentPanel.add(
					createStandardExcelDocumentSettingFields(xmlDocument),
					setGrids(0, 0));
		} else {
			documentPanel.add(
					createStandardTXTDocumentSettingFields(xmlDocument),
					setGrids(0, 0));
		}
		// add all column fields
		ArrayList<Column> columnsOfDocument = xmlDocument.getColumns();
		for (int i = 0; i < columnsOfDocument.size(); i++) {
			documentPanel.add(createColumnForm(columnsOfDocument.get(i)),
					setGrids(0, i + 1));
		}
		return documentPanel;
	}

	/**
	 * Create the document setting fields which are required for all documents.
	 * 
	 * @param xmlDocument
	 *            the xmlDocument which is read.
	 * 
	 * @return the panel with de components
	 */
	public JPanel createStandardTXTDocumentSettingFields(XMLDocument xmlDocument) {
		JPanel documentSettingsPanel = new JPanel();
		documentSettingsPanel.setLayout(new GridBagLayout());
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document name: ",
						xmlDocument.getDocName()), setGrids(0, 0));
		String[] doctypes = { "Excel", "txt/csv" };
		documentSettingsPanel.add(
				makeFormRowWithComboBox("Document type: ", doctypes, 1),
				setGrids(0, 1));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document path: ",
						xmlDocument.getPath()), setGrids(0, 2));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document start line: ",
						Integer.toString(xmlDocument.getStartLine())),
				setGrids(0, THREE));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document delimiter: ",
						xmlDocument.getDelimiter()), setGrids(0, FOUR));
		return documentSettingsPanel;
	}

	/**
	 * Create the document setting fields for excel which are required for all
	 * documents.
	 * 
	 * @param xmlDocument
	 *            the xmldocument which is read
	 * 
	 * @return the panel with de components
	 */
	public JPanel createStandardExcelDocumentSettingFields(
			XMLDocument xmlDocument) {
		JPanel documentSettingsPanel = new JPanel();
		documentSettingsPanel.setLayout(new GridBagLayout());
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document name: ",
						xmlDocument.getDocName()), setGrids(0, 0));
		String[] doctypes = { "Excel", "txt/csv" };
		documentSettingsPanel.add(
				makeFormRowWithComboBox("Document type: ", doctypes, 0),
				setGrids(0, 1));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document path: ",
						xmlDocument.getPath()), setGrids(0, 2));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document start line: ",
						Integer.toString(xmlDocument.getStartLine())),
				setGrids(0, THREE));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document sheet: ",
						Integer.toString(xmlDocument.getSheet())),
				setGrids(0, FOUR));
		return documentSettingsPanel;
	}

	/**
	 * Create a Java Combo Box with given array of strings.
	 * 
	 * @param comboBoxStrings
	 *            the array of strings to display in the dropdown.
	 * @param selectedValue
	 *            the int which indicates which elemtent is selected
	 * @return the Java ComboBox
	 */
	public JComboBox<String> createTypeDropDown(String[] comboBoxStrings,
			int selectedValue) {
		JComboBox<String> comboBox = new JComboBox<>(comboBoxStrings);
		comboBox.setSelectedIndex(selectedValue);
		return comboBox;
	}

	/**
	 * Create a panel for a column.
	 * 
	 * @param currentColumn
	 *            the column which is read
	 * @return the panel with all field for a column
	 */
	public JPanel createColumnForm(Column currentColumn) {
		int width = COLUMNPANELHEIGHT;
		if (currentColumn.getColumnType().equals("DATE")) {
			width += FORMELEMNTHEIGHT;
		}
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				width);
		String[] typeStrings = { "String", "Int", "Date" };
		containerPanel.add(
				makeFormRowWithTextField("Column id: ",
						Integer.toString(currentColumn.getColumnNumber())),
				setGrids(0, 0));
		containerPanel.add(
				makeFormRowWithTextField("Column name: ",
						currentColumn.getColumnName()), setGrids(0, 1));
		containerPanel.add(
				makeFormRowWithComboBox("Select type: ", typeStrings,
						getComboBoxIndex(currentColumn.getColumnType())),
				setGrids(0, 2));
		if (currentColumn.getColumnType().equals("DATE")) {

			containerPanel.add(
					makeFormRowWithTextField("Specified datetype: ",
							currentColumn.getDateType()), setGrids(0, THREE));
		}
		return containerPanel;
	}

	/**
	 * Get the index of the combo box for different columntypes.
	 * 
	 * @param columnType
	 *            the column type as a string
	 * @return the index of the selected combo box
	 */
	public int getComboBoxIndex(String columnType) {
		if (columnType.equals("DATE")) {
			return 2;
		} else if (columnType.equals("INT")) {
			return 1;
		} else {
			return 0;
		}

	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param name
	 *            the name of the label.
	 * @param comboBoxStrings
	 *            the array of string for the dropdown element
	 * @param comboIndex
	 *            the index of the selected dropdown element
	 * @return panel with the two textfields
	 */
	public JPanel makeFormRowWithComboBox(String name,
			String[] comboBoxStrings, int comboIndex) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				FORMELEMNTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		containerPanel.add(new JLabel(name));
		containerPanel.add(createTypeDropDown(comboBoxStrings, comboIndex));
		return containerPanel;
	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param labelName
	 *            the name of the label.
	 * @param textFieldInput
	 *            the input for the textfield if it is set
	 * @return panel with the two textfields
	 */
	public JPanel makeFormRowWithTextField(String labelName,
			String textFieldInput) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				FORMELEMNTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		containerPanel.add(new JLabel(labelName));
		containerPanel.add(new JTextField(textFieldInput));
		return containerPanel;
	}

	/**
	 * Make a scrollPanefor the container.
	 * 
	 * @param containerPanel
	 *            the panel for which the scrollpane is made
	 * @return the scrollPane
	 */
	public JScrollPane makeScrollPaneForContainerPanel(JPanel containerPanel) {
		JScrollPane scrollPane = new JScrollPane(containerPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(PARENTWIDTH, PARENTHEIGHT));
		return scrollPane;
	}

}
