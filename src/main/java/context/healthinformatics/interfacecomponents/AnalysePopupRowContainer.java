package context.healthinformatics.interfacecomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.healthinformatics.gui.InterfaceHelper;

/**
 * Class to contain a row of the analyse popup.
 */
public class AnalysePopupRowContainer extends InterfaceHelper implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int TEXT_FIELD_LENGTH = 10;
	private JComboBox<String> docNameDropDown;
	private JComboBox<String> docDropDown;
	private JTextField filterValue;
	private String[] docNames;
	private HashMap<String, String[]> columnNames;
	private JPanel filterRow;

	/**
	 * The constructor of the row container which inits all values.
	 * 
	 * @param docNames
	 *            the names of the documents for the combobox
	 * @param columnNames
	 *            the names of the columns per document in a hashmap
	 */
	public AnalysePopupRowContainer(String[] docNames,
			HashMap<String, String[]> columnNames) {
		docNames = new String[docNames.length];
		for (int i = 0; i < docNames.length; i++) {
			this.docNames[i] = docNames[i];
		}
		this.columnNames = columnNames;
		initAnalysePopupRow();
	}

	private void initAnalysePopupRow() {
		filterRow = createEmptyWithGridBagLayoutPanel();
		docNameDropDown = new JComboBox<>(docNames);
		docNameDropDown.addActionListener(this);
		docDropDown = new JComboBox<>(columnNames.get(docNames[0]));
		filterValue = new JTextField(TEXT_FIELD_LENGTH);
		filterRow.add(docNameDropDown, setGrids(0, 0));
		filterRow.add(docDropDown, setGrids(1, 0));
		filterRow.add(filterValue, setGrids(2, 0));
	}

	/**
	 * Set the values of the analyse filter row to the specified values.
	 * 
	 * @param docName
	 *            the document name
	 * @param columnName
	 *            the column name
	 * @param filterValue
	 *            the filter value
	 */
	public void setValues(String docName, String columnName, String filterValue) {
		this.docNameDropDown.setSelectedItem(docName);
		this.filterValue.setText(filterValue);
		docDropDown.removeAllItems();
		for (String s : columnNames.get(docNameDropDown.getSelectedItem()
				.toString())) {
			docDropDown.addItem(s);
		}
		docDropDown.setSelectedItem(columnName);
	}

	/**
	 * Get the values of the checkboxes and the textfield in a string.
	 * 
	 * @return the string of values
	 */
	public String getValues() {
		StringBuilder buildResult = new StringBuilder();
		buildResult.append(docNameDropDown.getSelectedItem().toString())
				.append(".");
		buildResult.append(docDropDown.getSelectedItem().toString()).append(
				" = '");
		buildResult.append(filterValue.getText()).append("'");
		return buildResult.toString();
	}

	/**
	 * Get the Panel with components.
	 * 
	 * @return the panel
	 */
	public JPanel getPanelOfRow() {
		return filterRow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		docDropDown.removeAllItems();
		for (String s : columnNames.get(docNameDropDown.getSelectedItem()
				.toString())) {
			docDropDown.addItem(s);
		}
		filterRow.revalidate();
	}
}
