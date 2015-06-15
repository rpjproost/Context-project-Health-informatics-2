package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import context.healthinformatics.gui.InputPage;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.writer.XMLDocument;

/**
 * XMLEditor class makes a panel which is filled with a form to edit XML files.
 */
public class XMLEditor extends InterfaceHelper implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final int MARGINTOP = 10;

	private static final int THREE = 3;

	private static final int PARENTWIDTH = 900;
	private static final int PARENTHEIGHT = 650;
	private static final int FORM_ELEMENT_HEIGHT = 25;
	private static final int BUTTON_PANEL_HEIGHT = 35;
	private static final int XML_EDITOR_WIDTH = 800;

	private JPanel containerScrollPanel;
	private JScrollPane scrollPane;

	private JButton addDocument = new JButton("Create new Document");
	private JButton removeDocument = new JButton("Remove Document");
	private JButton saveXMLDocument = new JButton("Save XML file");

	private ArrayList<DocumentFieldsContainer> documentFieldsContainers;
	private ArrayList<JPanel> documentPanels = new ArrayList<JPanel>();
	private InputPage inputPage;

	/**
	 * Constructor of the XMLEditor.
	 * 
	 * @param inputPage
	 *            the input page with the XMLEditorController
	 */
	public XMLEditor(InputPage inputPage) {
		this.inputPage = inputPage;
		this.documentFieldsContainers = new ArrayList<DocumentFieldsContainer>();
		containerScrollPanel = createContainerWithGivenSizePanel(PARENTWIDTH,
				PARENTHEIGHT);
		scrollPane = makeScrollPaneForContainerPanel(containerScrollPanel,
				PARENTWIDTH, PARENTHEIGHT);
		addActionListeners();
	}

	/**
	 * Add the action listeners of the XMLEditor.
	 */
	public void addActionListeners() {
		addDocument.addActionListener(this);
		removeDocument.addActionListener(this);
		saveXMLDocument.addActionListener(this);
	}

	/**
	 * Load the parent panel.
	 * 
	 * @return the parent panel
	 */
	public JPanel loadPanel() {
		JPanel scrollPaneContainerPanel = new JPanel();
		scrollPaneContainerPanel.add(scrollPane);
		JPanel parentPanel = createEmptyWithGridBagLayoutPanel();
		parentPanel.add(createTitle("The XMLEditor:"), setGrids(0, 0));
		parentPanel.add(scrollPaneContainerPanel, setGrids(0, 1));
		parentPanel.add(
				makeFormRowPanelWithThreeButton(addDocument, saveXMLDocument,
						removeDocument, XML_EDITOR_WIDTH, BUTTON_PANEL_HEIGHT),
				setGrids(0, 2, new Insets(MARGINTOP, 0, 0, 0)));
		return parentPanel;
	}

	/**
	 * Add an XMLDocument object the container panel with the scroll bar.
	 * 
	 * @param xmlDocument
	 *            the XMLDocument which is read
	 */
	public void addXMLDocumentToContainerScrollPanel(XMLDocument xmlDocument) {
		documentFieldsContainers.add(new DocumentFieldsContainer(xmlDocument,
				this));
		JPanel documentContainerPanel = createDocumentPanel(documentFieldsContainers
				.get(documentFieldsContainers.size() - 1));
		documentPanels.add(documentContainerPanel);
		containerScrollPanel.add(
				documentContainerPanel,
				setGrids(0, documentFieldsContainers.size() - 1, new Insets(
						MARGINTOP, 0, 0, 0)));
		scrollPane.revalidate();
		scrollPane.getVerticalScrollBar().setValue(0);
	}

	/**
	 * Create a panel for a XML document.
	 * 
	 * @param documentFieldContainer
	 *            the container with the fields for the XML document
	 * @return the panel with the added components
	 */
	private JPanel createDocumentPanel(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel documentPanel = createEmptyWithGridBagLayoutPanel();
		documentPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		documentPanel.add(
				createStandardDocumentSettingFields(documentFieldContainer),
				setGrids(0, 0));
		documentPanel.add(createColumnFormPanel(documentFieldContainer),
				setGrids(0, 1));
		return documentPanel;
	}

	/**
	 * Create the document setting fields with a delimiter or sheet row.
	 * 
	 * @param documentFieldContainer
	 *            the container which contains all GUI elements for the XML;
	 *            containerPanel.add(dateTypePanel, setGrids(0, THREE));
	 * @return the panel with the components
	 */
	private JPanel createStandardDocumentSettingFields(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel documentSettingsPanel = documentFieldContainer
				.getDocumentFormPanel();
		documentSettingsPanel.add(
				createStandardSettingFields(documentFieldContainer),
				setGrids(0, 0));
		JPanel specificRowElement = createSpecificFormRow(documentFieldContainer);
		documentFieldContainer
				.setPanelForDocTypeSpecificInput(specificRowElement);
		documentSettingsPanel.add(specificRowElement, setGrids(0, 1));
		return documentSettingsPanel;
	}

	/**
	 * Create a panel for a form row with a delimiter row or a sheet row
	 * depending on document type.
	 * 
	 * @param documentFieldContainer
	 *            the container containing the document type
	 * @return the panel
	 */
	private JPanel createSpecificFormRow(
			DocumentFieldsContainer documentFieldContainer) {
		if (documentFieldContainer.getDocumentTypeValue().toLowerCase()
				.equals("excel")) {
			return makeFormRowPanelWithTextField("Document sheet: ",
					documentFieldContainer.getSheet(), XML_EDITOR_WIDTH,
					FORM_ELEMENT_HEIGHT);
		} else {
			return makeFormRowPanelWithTextField("Document delimiter: ",
					documentFieldContainer.getDelimiter(), XML_EDITOR_WIDTH,
					FORM_ELEMENT_HEIGHT);
		}
	}

	/**
	 * Create the setting fields which every document has.
	 * 
	 * @param documentFieldContainer
	 *            the container of the elements
	 * @return a panel with the fields
	 */
	private JPanel createStandardSettingFields(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel standardSettingsPanel = createEmptyWithGridBagLayoutPanel();
		standardSettingsPanel.add(
				makeFormRowPanelWithTextField("Document name: ",
						documentFieldContainer.getDocumentName(),
						XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT), setGrids(0, 0));
		standardSettingsPanel.add(
				makeFormRowPanelWithComboBox("Document type: ",
						documentFieldContainer.getDocumentType(),
						XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT), setGrids(0, 1));
		standardSettingsPanel.add(
				makeFormRowPanelWithTextField("Document path: ",
						documentFieldContainer.getDocumentPath(),
						XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT), setGrids(0, 2));
		standardSettingsPanel.add(
				makeFormRowPanelWithTextField("Document start line: ",
						documentFieldContainer.getStartLine(),
						XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT),
				setGrids(0, THREE));
		return standardSettingsPanel;
	}

	/**
	 * Add all columns to the panel for the columns.
	 * 
	 * @param documentFieldContainer
	 *            the container
	 * @return the panel with the columns
	 */
	private JPanel createColumnFormPanel(
			DocumentFieldsContainer documentFieldContainer) {
		JPanel columnFormPanel = documentFieldContainer.getColumnFormPanel();
		ArrayList<ColumnFieldContainer> columnsOfDocument = documentFieldContainer
				.getColumnFields();
		columnFormPanel.add(
				makeFormRowPanelWithTwoButton(
						documentFieldContainer.getAddColumnButton(),
						documentFieldContainer.getRemoveColumnButton(),
						XML_EDITOR_WIDTH, BUTTON_PANEL_HEIGHT), setGrids(0, 0));
		for (int i = 0; i < columnsOfDocument.size(); i++) {
			columnFormPanel.add(createColumnForm(columnsOfDocument.get(i)),
					setGrids(0, i + 1));
		}
		return columnFormPanel;
	}

	/**
	 * Create a panel for a column.
	 * 
	 * @param currentColumnFieldContainer
	 *            the container of all input elements for the current columns
	 * @return the panel with all field for a column
	 */
	protected JPanel createColumnForm(
			ColumnFieldContainer currentColumnFieldContainer) {
		JPanel containerPanel = createEmptyWithGridBagLayoutPanel();
		currentColumnFieldContainer.setPanel(containerPanel);
		containerPanel.add(
				createStandardColumnSettingFields(currentColumnFieldContainer),
				setGrids(0, 0));
		if (currentColumnFieldContainer.hasDateType()) {
			containerPanel.add(getDateTypePanel(currentColumnFieldContainer),
					setGrids(0, 1));
		}
		return containerPanel;
	}

	/**
	 * Create the date type panel and set it at the currentColumnFieldcontainer.
	 * 
	 * @param currentColumnFieldContainer
	 *            the container for the columns
	 * @return the panel with the date type panel
	 */
	private JPanel getDateTypePanel(
			ColumnFieldContainer currentColumnFieldContainer) {
		JPanel dateTypePanel = makeFormRowPanelWithTextField(
				"Specified datetype: ",
				currentColumnFieldContainer.getDateType(), XML_EDITOR_WIDTH,
				FORM_ELEMENT_HEIGHT);
		currentColumnFieldContainer.setDateTypePanel(dateTypePanel);
		return dateTypePanel;
	}

	/**
	 * Create the column settings fields which every column needs.
	 * 
	 * @param currentColumnFieldContainer
	 *            the column field container
	 * @return the panel with the fields
	 */
	private JPanel createStandardColumnSettingFields(
			ColumnFieldContainer currentColumnFieldContainer) {
		JPanel containerPanel = createEmptyWithGridBagLayoutPanel();
		containerPanel.add(
				makeFormRowPanelWithTextField("Column id: ",
						currentColumnFieldContainer.getColumnID(),
						XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT), setGrids(0, 0));
		containerPanel.add(
				makeFormRowPanelWithTextField("Column name: ",
						currentColumnFieldContainer.getColumnName(),
						XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT), setGrids(0, 1));
		containerPanel.add(
				makeFormRowPanelWithComboBox("Select type: ",
						currentColumnFieldContainer.getColumnType(),
						XML_EDITOR_WIDTH, FORM_ELEMENT_HEIGHT), setGrids(0, 2));
		return containerPanel;
	}

	/**
	 * Get all documents from the fields in the xml editor.
	 * 
	 * @return a list of XMLDocuments
	 */
	public ArrayList<XMLDocument> getAllXMLDocuments() {
		ArrayList<XMLDocument> xmlDocuments = new ArrayList<XMLDocument>();
		for (int i = 0; i < documentFieldsContainers.size(); i++) {
			xmlDocuments.add(documentFieldsContainers.get(i).getXMLDocument());
		}
		return xmlDocuments;
	}

	/**
	 * Check if there is wrong input in the XMLEditor.
	 * 
	 * @return true if there is
	 */
	public boolean checkAllXMLDocumentsOnError() {
		XMLDocumentFormController xdfc = new XMLDocumentFormController(
				documentFieldsContainers);
		boolean errors = false;
		try {
			errors = xdfc.checkIfHasEmptyFields();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Save Error",
					JOptionPane.WARNING_MESSAGE);
			return true;
		}
		return errors;

	}

	/**
	 * Saves an XML File.
	 */
	private void saveXMLFile() {
		if (!checkAllXMLDocumentsOnError()) {
			ArrayList<XMLDocument> xmlDocuments = getAllXMLDocuments();
			inputPage.getXMLController().updateDocuments(inputPage,
					xmlDocuments);
			inputPage.getXMLController().save();
		}
	}

	/**
	 * Empties the editor.
	 */
	protected void emptyEditor() {
		while (documentFieldsContainers.size() > 0) {
			documentFieldsContainers
					.remove(documentFieldsContainers.size() - 1);
			JPanel docContainerPanel = documentPanels.remove(documentPanels
					.size() - 1);
			docContainerPanel.setVisible(false);
		}
	}

	private void handleRemoveButton() {
		if (documentFieldsContainers.size() > 0) {
			removeDocument();
		} else {
			JOptionPane.showMessageDialog(null,
					"You can't remove a document, because there are none!",
					"Remove Document Error", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Remove a document from the document field.
	 */
	private void removeDocument() {
		documentFieldsContainers.remove(documentFieldsContainers.size() - 1);
		JPanel docContainerPanel = documentPanels
				.remove(documentPanels.size() - 1);
		docContainerPanel.setVisible(false);
		inputPage.getXMLController().updateDocuments(inputPage,
				getAllXMLDocuments());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addDocument) {
			addXMLDocumentToContainerScrollPanel(new XMLDocument());
			inputPage.getXMLController().updateDocuments(inputPage,
					getAllXMLDocuments());
		} else if (e.getSource() == removeDocument) {
			handleRemoveButton();
		} else if (e.getSource() == saveXMLDocument) {
			saveXMLFile();
		}
	}
}
