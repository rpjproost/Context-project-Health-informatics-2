package context.healthinformatics.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * XMLEditor class makes a panel which is filled with a form to edit xml files.
 */
public class XMLEditor extends InterfaceHelper {

	private static final long serialVersionUID = 1L;
	
	private static final int PARENTWIDTH = 900;
	private static final int PARENTHEIGHT = 700;

	private static final int FORMELEMENTWITH = 800;
	private static final int FORMELEMNTHEIGHT = 25;

	/**
	 * Empty Constructor of the XMLEditor.
	 */
	public XMLEditor() {

	}

	/**
	 * Load the parent panel.
	 * 
	 * @return the parent panel
	 */
	public JPanel loadPanel() {
		JPanel jp = new JPanel();
		JPanel testPanel = new JPanel();
		testPanel.setMinimumSize(new Dimension(PARENTWIDTH, PARENTHEIGHT));
		testPanel.setLayout(new GridBagLayout());
		testPanel.add(makeFormRowWithTextField("Document name: "),
				setGrids(0, 0));
		String[] doctypes = { "Excel", "txt/csv" };
		testPanel.add(makeFormRowWithComboBox("Document type: ", doctypes),
				setGrids(0, 1));
		testPanel.add(makeFormRowWithTextField("Document path: "),
				setGrids(0, 2));
		testPanel.add(makeFormRowWithTextField("Document start line: "),
				setGrids(0, 3));
		testPanel.add(makeFormRowWithTextField("Document delimiter: "),
				setGrids(0, 4));

		int counter = 5;

		for (int i = 0; i < 3; i++) {
			testPanel.add(createColumnForm(i), setGrids(0, counter));
			counter++;
		}
		JScrollPane scrollPane = new JScrollPane(testPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(PARENTWIDTH, PARENTHEIGHT));
		jp.add(scrollPane);
		return jp;
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
		JPanel containerPanel = createPanel(Color.BLUE, FORMELEMENTWITH, 75);
		String[] typeStrings = { "String", "Int", "Date" };
		GridBagConstraints c = setGrids(0, 0);
		// margin top
		c.insets = new Insets(10, 0, 0, 0);
		containerPanel.add(makeFormRowWithTextField("Column id: "), c);
		containerPanel.add(makeFormRowWithTextField("Column name: "),
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
		JPanel containerPanel = createPanel(Color.BLUE, FORMELEMENTWITH,
				FORMELEMNTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		JTextField label = new JTextField(name);
		label.setEditable(false);
		containerPanel.add(label);
		containerPanel.add(createTypeDropDown(comboBoxStrings));
		return containerPanel;
	}

	/**
	 * Make a row with display text field and field to fill in value.
	 * 
	 * @param name
	 *            the name of the label.
	 * @return panel with the two textfields
	 */
	public JPanel makeFormRowWithTextField(String name) {
		JPanel containerPanel = createPanel(Color.BLUE, FORMELEMENTWITH,
				FORMELEMNTHEIGHT);
		containerPanel.setLayout(new GridLayout(1, 2));
		JTextField label = new JTextField(name);
		label.setEditable(false);
		containerPanel.add(label);
		containerPanel.add(new JTextField());
		return containerPanel;
	}

	/**
	 * Make a scrollPanefor the parent.
	 * 
	 * @return the scrollpane
	 */
	public JScrollPane makeScrollPaneForParentPanel() {
		JPanel panel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(50, 30, 300, 50);
		return scrollPane;
	}

}
