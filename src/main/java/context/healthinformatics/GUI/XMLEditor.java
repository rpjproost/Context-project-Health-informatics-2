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

import context.healthinformatics.Writer.XMLDocument;

/**
 * XMLEditor class makes a panel which is filled with a form to edit xml files.
 */
public class XMLEditor extends InterfaceHelper {

	private static final long serialVersionUID = 1L;

	private static final int PARENTWIDTH = 900;
	private static final int PARENTHEIGHT = 700;

	private static final int FORMELEMENTWITH = 800;
	private static final int FORMELEMNTHEIGHT = 25;
	
	private static final int MARGINTOP = 10;

	private ArrayList<XMLDocument> xmlDocumentList;

	/**
	 * Empty Constructor of the XMLEditor.
	 */
	public XMLEditor() {
		xmlDocumentList = new ArrayList<XMLDocument>();
	}

	/**
	 * Load the parent panel.
	 * 
	 * @return the parent panel
	 */
	public JPanel loadPanel() {
		JPanel parentPanel = new JPanel();
		JPanel containerPanel = createContainerPanel();

		// hardcoded added two documentpanels
		containerPanel.add(createDocumentPanel(), setGrids(0, 0));
		GridBagConstraints c = setGrids(0, 1);
		// margin top
		c.insets = new Insets(MARGINTOP, 0, 0, 0);
		containerPanel.add(createDocumentPanel(), c);

		JScrollPane scrollPane = makeScrollPaneForContainerPanel(containerPanel);
		parentPanel.add(scrollPane);
		return parentPanel;
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
	 * @return the panel with the added components
	 */
	public JPanel createDocumentPanel() {
		JPanel documentPanel = new JPanel();
		documentPanel.setLayout(new GridBagLayout());
		documentPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		documentPanel
				.add(createStandardDocumentSettingFields(), setGrids(0, 0));

		int counter = 1;

		for (int i = 0; i < 4; i++) {
			documentPanel.add(createColumnForm(i), setGrids(0, counter));
			counter++;
		}
		return documentPanel;
	}

	/**
	 * Create the document setting fields which are required for all documents.
	 * 
	 * @return the panel with de components
	 */
	public JPanel createStandardDocumentSettingFields() {
		JPanel documentSettingsPanel = new JPanel();
		documentSettingsPanel.setLayout(new GridBagLayout());
		documentSettingsPanel
				.add(makeFormRowWithTextField("Document name: ", ""),
						setGrids(0, 0));
		String[] doctypes = { "Excel", "txt/csv" };
		documentSettingsPanel.add(
				makeFormRowWithComboBox("Document type: ", doctypes),
				setGrids(0, 1));
		documentSettingsPanel
				.add(makeFormRowWithTextField("Document path: ", ""),
						setGrids(0, 2));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document start line: ", ""),
				setGrids(0, 3));
		documentSettingsPanel.add(
				makeFormRowWithTextField("Document delimiter: ", ""),
				setGrids(0, 4));
		return documentSettingsPanel;
	}

	/**
	 * Create a Java Combo Box with given array of strings.
	 * 
	 * @param comboBoxStrings
	 *            the array of strings to display in the dropdown.
	 * @return the Java ComboBox
	 */
	public JComboBox<String> createTypeDropDown(String[] comboBoxStrings) {
		JComboBox<String> comboBox = new JComboBox<>(comboBoxStrings);
		comboBox.setSelectedIndex(0);
		return comboBox;
	}

	/**
	 * Create a panel for a column.
	 * 
	 * @param number
	 *            the number of the current column
	 * @return the panel with all field for a column
	 */
	public JPanel createColumnForm(int number) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWITH, 75);
		String[] typeStrings = { "String", "Int", "Date" };
		containerPanel.add(makeFormRowWithTextField("Column id: ", ""),
				setGrids(0, 0));
		containerPanel.add(makeFormRowWithTextField("Column name: ", ""),
				setGrids(0, 1));
		containerPanel.add(
				makeFormRowWithComboBox("Select type: ", typeStrings),
				setGrids(0, 2));
		return containerPanel;
	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param name
	 *            the name of the label.
	 * @param comboBoxStrings
	 *            the array of string for the dropdown element
	 * @return panel with the two textfields
	 */
	public JPanel makeFormRowWithComboBox(String name, String[] comboBoxStrings) {
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWITH,
				FORMELEMNTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		containerPanel.add(new JLabel(name));
		containerPanel.add(createTypeDropDown(comboBoxStrings));
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
		JPanel containerPanel = createPanel(Color.WHITE, FORMELEMENTWITH,
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
