package context.healthinformatics.interfacecomponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.healthinformatics.gui.InterfaceHelper;

/**
 * Class to contain a row of the Analyze PopUp.
 */
public class AnalyzePopupRowContainer extends InterfaceHelper implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int ROW_WIDTH = 350;
	private static final int ROW_HEIGHT = 30;
	private static final int TEXT_FIELD_LENGTH = 10;
	
	private JPanel filterRow;
	private JComboBox<String> docNameDropDown;
	private JComboBox<String> docDropDown;
	private JTextField filterValue;
	private String[] docNames;
	private HashMap<String, String[]> columnNames;
	

	/**
	 * The constructor of the row container which initializes all values.
	 * 
	 * @param docNamesIn
	 *            the names of the documents for the ComboBox
	 * @param columnNamesIn
	 *            the names of the columns per document in a HashMap
	 */
	public AnalyzePopupRowContainer(String[] docNamesIn,
			HashMap<String, String[]> columnNamesIn) {
		this.docNames = docNamesIn.clone();
		this.columnNames = columnNamesIn;
		initAnalysePopupRow();
	}

	private void initAnalysePopupRow() {
		filterRow = createEmptyWithGridBagLayoutPanel();
		filterRow.setPreferredSize(new Dimension(ROW_WIDTH, ROW_HEIGHT));
		docNameDropDown = new JComboBox<>(docNames);
		docNameDropDown.addActionListener(this);
		docDropDown = new JComboBox<>(columnNames.get(docNames[0]));
		filterValue = new JTextField(TEXT_FIELD_LENGTH);
		filterRow.add(docNameDropDown, setGrids(0, 0));
		filterRow.add(docDropDown, setGrids(1, 0));
		filterRow.add(filterValue, setGrids(2, 0));
	}

	/**
	 * Set the values of the analyze filter row to the specified values.
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
	 * Get the values of the CheckBoxes and the TextField in a string.
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
