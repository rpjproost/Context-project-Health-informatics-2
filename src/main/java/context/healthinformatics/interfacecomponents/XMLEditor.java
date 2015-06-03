package context.healthinformatics.interfacecomponents;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import context.healthinformatics.gui.InputPage;
import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.writer.XMLDocument;

/**
 * XMLEditor class makes a panel which is filled with a form to edit xml files.
 */
public class XMLEditor extends InterfaceHelper implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int MARGINTOP = 10;

	private static final int THREE = 3;

	private JPanel containerScrollPanel;
	private JScrollPane scrollPane;

	private JButton addDocument = new JButton("Create new Document");
	private JButton removeDocument = new JButton("Remove Document");
	private JButton saveXMLDocument = new JButton("Save XML file");

	private ArrayList<DocumentFieldsContainer> documentFieldsContainers;
	private ArrayList<JPanel> documentPanels = new ArrayList<JPanel>();
	private InputPage inputPage;

	/**
	 * Empty Constructor of the XMLEditor.
	 * 
	 * @param inputPage
	 *            the input page with the xmleditorcontroller
	 */
	public XMLEditor(InputPage inputPage) {
		this.inputPage = inputPage;
		documentFieldsContainers = new ArrayList<DocumentFieldsContainer>();
		JPanel extraContainer = createContainerPanel();
		containerScrollPanel = createContainerPanel();
		extraContainer.add(containerScrollPanel, setGrids(0, 0));
		addActionListeners();
		scrollPane = makeScrollPaneForContainerPanel(extraContainer);
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
				makeFormRowWithThreeButton(addDocument, saveXMLDocument,
						removeDocument), setGrids(0, 2, MARGINTOP));
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
	 * Create a panel for a xml document.
	 * 
	 * @param documentFieldContainer
	 *            the container with the fields for the xml document
	 * @return the panel with the added components
	 */
	public JPanel createDocumentPanel(
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
	 *            the container which contains all gui elements for the xml;
	 *            containerPanel.add(dateTypePanel, setGrids(0, THREE));
	 * @return the panel with de components
	 */
	public JPanel createStandardDocumentSettingFields(
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
	 * depending on doctype.
	 * 
	 * @param documentFieldContainer
	 *            the container containg the doctype
	 * @return the panel
	 */
	public JPanel createSpecificFormRow(
			DocumentFieldsContainer documentFieldContainer) {
		if (documentFieldContainer.getDocumentTypeValue().toLowerCase()
				.equals("excel")) {
			return makeFormRowWithTextField("Document sheet: ",
					documentFieldContainer.getSheet());
		} else {
			return makeFormRowWithTextField("Document delimiter: ",
					documentFieldContainer.getDelimiter());
		}
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
		JPanel standardSettingsPanel = createEmptyWithGridBagLayoutPanel();
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
	 * Create a panel for a column.
	 * 
	 * @param currentColumnFieldContainer
	 *            the container of all input elements for the current columns
	 * @return the panel with all field for a column
	 */

	public JPanel createColumnForm(
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
		JPanel dateTypePanel = makeFormRowWithTextField("Specified datetype: ",
				currentColumnFieldContainer.getDateType());
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
	public JPanel createStandardColumnSettingFields(
			ColumnFieldContainer currentColumnFieldContainer) {
		JPanel containerPanel = createEmptyWithGridBagLayoutPanel();
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
	 * Saves an XML File.
	 */
	public void saveXMLFile() {
		ArrayList<XMLDocument> xmlDocuments = getAllXMLDocuments();
		inputPage.getXMLController().updateDocuments(inputPage, xmlDocuments);
		inputPage.getXMLController().save();
	}

	/**
	 * Empties the editor.
	 */
	public void emptyEditor() {
		while (documentFieldsContainers.size() > 0) {
			documentFieldsContainers
					.remove(documentFieldsContainers.size() - 1);
			JPanel docContainerPanel = documentPanels.remove(documentPanels
					.size() - 1);
			docContainerPanel.setVisible(false);
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
			if (documentFieldsContainers.size() > 0) {
				removeDocument();
			}
		} else if (e.getSource() == saveXMLDocument) {
			saveXMLFile();
		}
	}
}
