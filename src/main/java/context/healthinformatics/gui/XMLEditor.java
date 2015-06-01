package context.healthinformatics.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import context.healthinformatics.writer.XMLDocument;

/**
 * XMLEditor class makes a panel which is filled with a form to edit xml files.
 */
public class XMLEditor extends InterfaceHelper implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int PARENTWIDTH = 900;
	private static final int PARENTHEIGHT = 700;

	private static final int FORMELEMENTWIDTH = 800;
	private static final int FORMELEMENTHEIGHT = 25;

	private static final int COLUMNPANELHEIGHT = 75;

	private static final int BUTTONHEIGHT = 35;

	private static final int MARGINTOP = 10;

	private static final int THREE = 3;

	private JPanel containerScrollPanel;
	private JScrollPane scrollPane;

	private JButton addDocument = new JButton("Create new Document");
	private JButton removeDocument = new JButton("Remove Document");

	private ArrayList<DocumentFieldsContainer> documentFieldsContainers;
	private ArrayList<JPanel> documentPanels = new ArrayList<JPanel>();

	/**
	 * Empty Constructor of the XMLEditor.
	 */
	public XMLEditor() {
		documentFieldsContainers = new ArrayList<DocumentFieldsContainer>();
		JPanel extraContainer = createContainerPanel();
		containerScrollPanel = createContainerPanel();
		extraContainer.add(containerScrollPanel, setGrids(0, 0));
		extraContainer.add(makeFormRowWithButton(addDocument, removeDocument),
				setGrids(0, 1, MARGINTOP));
		addDocument.addActionListener(this);
		removeDocument.addActionListener(this);
		scrollPane = makeScrollPaneForContainerPanel(extraContainer);
	}

	/**
	 * Create a row with a single button to add columns.
	 * 
	 * @param buttonLeft
	 *            the button on the left side of the panel
	 * @param buttonRight
	 *            the button on the right side of the panel
	 * @return the panel with white space and the button
	 */
	public JPanel makeFormRowWithButton(JButton buttonLeft, JButton buttonRight) {
		JPanel buttonPanel = createPanel(Color.WHITE, FORMELEMENTWIDTH,
				BUTTONHEIGHT);
		buttonPanel.setLayout(new GridLayout(1, THREE));
		buttonPanel.add(buttonLeft);
		buttonPanel.add(new JPanel());
		buttonPanel.add(buttonRight);
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
		documentFieldsContainers.add(new DocumentFieldsContainer(xmlDocument,
				this));
		JPanel documentContainerPanel = createDocumentPanel(documentFieldsContainers
				.get(documentFieldsContainers.size() - 1));
		documentPanels.add(documentContainerPanel);
		containerScrollPanel.add(documentContainerPanel,
				setGrids(0, documentFieldsContainers.size() - 1, MARGINTOP));
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
	 * @param documentFieldContainer
	 *            the container with the fields for the xml document
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
		documentPanel.add(createColumnFormPanel(documentFieldContainer),
				setGrids(0, 1));
		return documentPanel;
	}

	/**
	 * Add al columns to the panel for the columns.
	 * 
	 * @param documentFieldContainer
	 *            the container
	 * @return the panel with the columns
	 */
	public JPanel createColumnFormPanel(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel columnFormPanel = documentFieldContainer.getColumnFormPanel();
		ArrayList<ColumnFieldContainer> columnsOfDocument = documentFieldContainer
				.getColumnFields();
		columnFormPanel.add(
				makeFormRowWithButton(
						documentFieldContainer.getAddColumnButton(),
						documentFieldContainer.getRemoveColumnButton()),
				setGrids(0, 0));
		for (int i = 0; i < columnsOfDocument.size(); i++) {
			columnFormPanel.add(createColumnForm(columnsOfDocument.get(i)),
					setGrids(0, i + 1));
		}
		return columnFormPanel;
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
				createStandardSettingFields(documentFieldContainer),
				setGrids(0, 0));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document delimiter: ",
						documentFieldContainer.getDelimiter()), setGrids(0, 1));
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
				createStandardSettingFields(documentFieldContainer),
				setGrids(0, 0));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document sheet: ",
						documentFieldContainer.getSheet()), setGrids(0, 1));
		return documentSettingsPanel;
	}

	/**
	 * Create the setting fields which every document has.
	 * 
	 * @param documentFieldContainer
	 *            the container of the elements
	 * @return a panel with the fields
	 */
	public JPanel createStandardSettingFields(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel standardSettingsPanel = new JPanel();
		standardSettingsPanel.setLayout(new GridBagLayout());
		standardSettingsPanel.add(
				makeFormRowWithTextField("Document name: ",
						documentFieldContainer.getDocumentName()),
				setGrids(0, 0));
		standardSettingsPanel.add(
				makeFormRowWithComboBox("Document type: ",
						documentFieldContainer.getDocumentType()),
				setGrids(0, 1));
		standardSettingsPanel.add(
				makeFormRowWithTextField("Document path: ",
						documentFieldContainer.getDocumentPath()),
				setGrids(0, 2));
		standardSettingsPanel.add(
				makeFormRowWithTextField("Document start line: ",
						documentFieldContainer.getStartLine()),
				setGrids(0, THREE));
		return standardSettingsPanel;
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
		currentColumnFieldContainer.setPanel(containerPanel);
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
			JPanel dateTypePanel = makeFormRowWithTextField(
					"Specified datetype: ",
					currentColumnFieldContainer.getDateType());
			currentColumnFieldContainer.setDateTypePanel(dateTypePanel);
			containerPanel.add(dateTypePanel, setGrids(0, THREE));
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addDocument) {
			addXMLDocumentToContainerScrollPanel(new XMLDocument());
		} else if (e.getSource() == removeDocument) {
			if (documentFieldsContainers.size() > 0) {
				documentFieldsContainers
						.remove(documentFieldsContainers.size() - 1);
				JPanel docContainerPanel = documentPanels.remove(documentPanels
						.size() - 1);
				docContainerPanel.setVisible(false);
			}

		} else if (e.getSource() == "SAVEXMLEDITBUTTON") {
			//TODO Couple this with writer and a real save button
			ArrayList<XMLDocument> xmlDocuments = new ArrayList<XMLDocument>();
			for (int i = 0; i < documentFieldsContainers.size(); i++) {
				xmlDocuments.add(documentFieldsContainers.get(i)
						.getXMLDocument());
			}

		}

	}
}
