package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import context.healthinformatics.parser.Column;
import context.healthinformatics.writer.XMLDocument;

/**
 * XMLEditor class makes a panel which is filled with a form to edit xml files.
 */
public class XMLEditor extends InterfaceHelper {

	private static final long serialVersionUID = 1L;

	private static final int PARENTWIDTH = 900;
	private static final int PARENTHEIGHT = 700;

	private static final int FORMELEMENTWIDTH = 800;
	private static final int FORMELEMENTHEIGHT = 25;

	private static final int COLUMNPANELHEIGHT = 75;

	private static final int BUTTONHEIGHT = 35;

	private static final int MARGINTOP = 10;

	private static final int THREE = 3;
	private static final int FOUR = 4;

	private JPanel containerScrollPanel;
	private int numberOfXMLDocuments;
	private JScrollPane scrollPane;

	private ArrayList<DocumentFieldsContainer> documentFieldsContainers;

	/**
	 * Empty Constructor of the XMLEditor.
	 */
	public XMLEditor() {
		documentFieldsContainers = new ArrayList<DocumentFieldsContainer>();
		JPanel extraContainer = createContainerPanel();
		containerScrollPanel = createContainerPanel();
		extraContainer.add(containerScrollPanel, setGrids(0, 0));
		GridBagConstraints c = setGrids(0, 1);
		// margin top
		c.insets = new Insets(MARGINTOP, 0, 0, 0);
		extraContainer.add(makeFormRowWithButton("Create new Document", null),
				c);
		numberOfXMLDocuments = 0;
		scrollPane = makeScrollPaneForContainerPanel(extraContainer);
	}

	/**
	 * Create a row with a single button to add columns.
	 * 
	 * @param nameButton1
	 *            the name of the button
	 * @param color
	 *            the background color
	 * @return the panel with white space and the button
	 */
	public JPanel makeFormRowWithButton(String nameButton1, Color color) {
		JPanel buttonPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				BUTTONHEIGHT);
		buttonPanel.setLayout(new GridLayout(1, THREE));
		JPanel dummyPanel = new JPanel();
		JPanel dummyPanel2 = new JPanel();
		dummyPanel.setBackground(color);
		dummyPanel2.setBackground(color);
		buttonPanel.add(dummyPanel);
		buttonPanel.add(dummyPanel2);
		buttonPanel.add(new JButton(nameButton1));
		return buttonPanel;
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
		documentFieldsContainers.add(new DocumentFieldsContainer(xmlDocument));
		containerScrollPanel.add(createDocumentPanel(documentFieldsContainers
				.get(numberOfXMLDocuments)),
				setGrids(0, numberOfXMLDocuments, MARGINTOP));
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
	public JPanel createDocumentPanel(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel documentPanel = new JPanel();
		documentPanel.setLayout(new GridBagLayout());
		documentPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		// other setting fields
		if (documentFieldContainer.getDocumentTypeValue().toLowerCase()
				.equals("excel")) {
			documentPanel
					.add(createStandardExcelDocumentSettingFields(documentFieldContainer),
							setGrids(0, 0));
		} else {
			documentPanel
					.add(createStandardTXTDocumentSettingFields(documentFieldContainer),
							setGrids(0, 0));
		}
		// add all column fields
		ArrayList<ColumnFieldContainer> columnsOfDocument = documentFieldContainer
				.getColumnFields();
		documentPanel.add(
				makeFormRowWithButton("Add extra Column", Color.WHITE),
				setGrids(0, 1));
		for (int i = 0; i < columnsOfDocument.size(); i++) {
			documentPanel.add(createColumnForm(columnsOfDocument.get(i)),
					setGrids(0, i + 2));
		}
		return documentPanel;
	}

	/**
	 * Create the document setting fields which are required for all documents.
	 * 
	 * @param documentFieldContainer
	 *            the container which contains all gui elements for the xml
	 *            editor form
	 * 
	 * @return the panel with the components
	 */
	public JPanel createStandardTXTDocumentSettingFields(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel documentSettingsPanel = new JPanel();
		documentSettingsPanel.setLayout(new GridBagLayout());
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document name: ",
						documentFieldContainer.getDocumentName()),
				setGrids(0, 0));
		documentSettingsPanel.add(
				makeFormRowWithComboBox("Document type: ",
						documentFieldContainer.getDocumentType()),
				setGrids(0, 1));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document path: ",
						documentFieldContainer.getDocumentPath()),
				setGrids(0, 2));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document start line: ",
						documentFieldContainer.getStartLine()),
				setGrids(0, THREE));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document delimiter: ",
						documentFieldContainer.getDelimiter()),
				setGrids(0, FOUR));
		return documentSettingsPanel;
	}

	/**
	 * Create the document setting fields for excel which are required for all
	 * documents.
	 * 
	 * @param documentFieldContainer
	 *            the container which contains all gui elements for the xml
	 *            editor form
	 * 
	 * @return the panel with de components
	 */
	public JPanel createStandardExcelDocumentSettingFields(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel documentSettingsPanel = new JPanel();
		documentSettingsPanel.setLayout(new GridBagLayout());
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document name: ",
						documentFieldContainer.getDocumentName()),
				setGrids(0, 0));
		documentSettingsPanel.add(
				makeFormRowWithComboBox("Document type: ",
						documentFieldContainer.getDocumentType()),
				setGrids(0, 1));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document path: ",
						documentFieldContainer.getDocumentPath()),
				setGrids(0, 2));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document start line: ",
						documentFieldContainer.getStartLine()),
				setGrids(0, THREE));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document sheet: ",
						documentFieldContainer.getSheet()), setGrids(0, FOUR));
		return documentSettingsPanel;
	}

	/**
	 * Create a panel for a column.
	 * 
	 * @param currentColumnFieldContainer
	 *            the container of all input elements for the current columns
	 * @return the panel with all field for a column
	 */
	public JPanel createColumnForm(
			ColumnFieldContainer currentColumnFieldContainer) {
		int width = COLUMNPANELHEIGHT;
		if (currentColumnFieldContainer.hasDateType()) {
			width += FORMELEMENTHEIGHT;
		}
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				width);
		containerPanel.add(
				makeFormRowWithTextField("Column id: ",
						currentColumnFieldContainer.getColumnID()),
				setGrids(0, 0));
		containerPanel.add(
				makeFormRowWithTextField("Column name: ",
						currentColumnFieldContainer.getColumnName()),
				setGrids(0, 1));
		containerPanel.add(
				makeFormRowWithComboBox("Select type: ",
						currentColumnFieldContainer.getColumnType()),
				setGrids(0, 2));
		if (currentColumnFieldContainer.hasDateType()) {
			containerPanel.add(
					makeFormRowWithTextField("Specified datetype: ",
							currentColumnFieldContainer.getDateType()),
					setGrids(0, THREE));
		}
		return containerPanel;
	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param name
	 *            the name of the label.
	 * @param comboBox
	 *            the given comboBox
	 * @return panel with the two textfields
	 */
	public JPanel makeFormRowWithComboBox(String name,
			JComboBox<String> comboBox) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				FORMELEMENTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		containerPanel.add(new JLabel(name));
		containerPanel.add(comboBox);
		return containerPanel;
	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param labelName
	 *            the name of the label.
	 * @param textField
	 *            the textfield of the row
	 * @return panel with the the textfield and label
	 */
	public JPanel makeFormRowWithTextField(String labelName,
			JTextField textField) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				FORMELEMENTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		containerPanel.add(new JLabel(labelName));
		containerPanel.add(textField);
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
