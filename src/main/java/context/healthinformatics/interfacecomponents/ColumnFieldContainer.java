package context.healthinformatics.interfacecomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.healthinformatics.gui.InterfaceHelper;
import context.healthinformatics.parser.Column;

/**
 * Class which contains all field for a column form element.
 */
public class ColumnFieldContainer extends InterfaceHelper implements
		ActionListener {
	private static final long serialVersionUID = 1L;

	private JTextField columnID;
	private JTextField columnName;
	private JComboBox<String> columnType;
	private JTextField dateType;
	private JPanel panel;
	private String[] comboBoxValues = { "String", "Int", "Date" };
	private String comboValue;
	private JPanel dateTypePanel;

	private static final int THREE = 3;

	/**
	 * Constructor of the columnFieldContainer based on the input Column.
	 * 
	 * @param column
	 *            the column
	 */
	public ColumnFieldContainer(Column column) {
		this.comboValue = column.getColumnType();
		initTextFieldsWithValues(column);
		this.columnType = new JComboBox<>(comboBoxValues);
		this.columnType.setSelectedIndex(getComboBoxIndex(comboValue));
		this.columnType.addActionListener(this);
	}

	/**
	 * Initialize the TextField with the given values from the Column object.
	 * 
	 * @param column
	 *            the column object
	 */
	private void initTextFieldsWithValues(Column column) {
		this.columnID = new JTextField(Integer.toString(column
				.getColumnNumber()));
		this.columnName = new JTextField(column.getColumnName());
		if (hasDateType()) {
			this.dateType = new JTextField(column.getDateType());
		} else {
			this.dateType = new JTextField("dd-MM-yyyy");
		}
	}

	/**
	 * Check if a column has a date type.
	 * 
	 * @return true if has DateType else false
	 */
	public boolean hasDateType() {
		return comboValue.toLowerCase().equals("date");
	}

	/**
	 * Get the index of the combo box based on the given columnType.
	 * 
	 * @param columnType
	 *            the columnType of the column
	 * @return the index of the combo box
	 */
	private int getComboBoxIndex(String columnType) {
		if (columnType.equals("DATE")) {
			return 2;
		} else if (columnType.equals("Int")) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Build a column object from the TextField values.
	 * 
	 * @return a Column Object
	 */
	public Column getColumn() {
		Column col = new Column(getColumnIDValue(), getColumnNameValue(),
				getColumnTypeValue());
		if (hasDateType()) {
			col.setDateType(getColumnDateTypeValue());
		}
		return col;
	}

	/**
	 * Check if all column fields are filled in correctly.
	 * 
	 * @return true if they are else false
	 */
	public boolean checkIfHasEmptyFields() {
		if (hasDateType()) {
			return checkIfDateColumnHasEmptyFields();
		} else {
			return checkIfColumnHasEmptyFields();
		}
	}

	private boolean checkIfDateColumnHasEmptyFields() {
		if (isColumnIDEmpty()) {
			return true;
		} else if (isColumnNameEmpty()) {
			return true;
		} else {
			return isColumnDateTypeEmpty();
		}
	}

	private boolean checkIfColumnHasEmptyFields() {
		if (isColumnIDEmpty()) {
			return true;
		} else {
			return isColumnNameEmpty();
		}
	}

	/**
	 * Set selected column type.
	 * 
	 * @param index
	 *            the index of the type
	 */
	public void setColumnType(int index) {
		columnType.setSelectedIndex(index);
	}

	/**
	 * Get the inputed value of the columnID field.
	 * 
	 * @return the value
	 */
	public int getColumnIDValue() {
		if (columnID.getText().equals("") || !isInteger(columnID.getText())) {
			return -1;
		} else {
			return Integer.parseInt(columnID.getText());
		}
	}

	/**
	 * Get the inputed value of the columnName field.
	 * 
	 * @return the value
	 */
	public String getColumnNameValue() {
		return columnName.getText();
	}

	/**
	 * Get the inputed value of the columnType ComboBox.
	 * 
	 * @return the value
	 */
	public String getColumnTypeValue() {
		return columnType.getSelectedItem().toString();
	}

	/**
	 * Get the column date type value.
	 * 
	 * @return the value
	 */
	public String getColumnDateTypeValue() {
		return dateType.getText();
	}

	/**
	 * Get the column id TextField.
	 * 
	 * @return the TextField
	 */
	public JTextField getColumnID() {
		return columnID;
	}

	/**
	 * Get the columnName TextField.
	 * 
	 * @return the TextField
	 */
	public JTextField getColumnName() {
		return columnName;
	}

	/**
	 * Get the columnType ComboBox.
	 * 
	 * @return the ComboBox
	 */
	public JComboBox<String> getColumnType() {
		return columnType;
	}

	/**
	 * Get the date type TextField.
	 * 
	 * @return the TextField
	 */
	public JTextField getDateType() {
		return dateType;
	}

	/**
	 * Set the panel of this column fields.
	 * 
	 * @param panel
	 *            the panel
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * Check if column field has a panel.
	 * 
	 * @return true if panel is set
	 */
	public boolean hasPanel() {
		return this.panel != null;
	}

	/**
	 * Get the current panel.
	 * 
	 * @return the panel of this fields
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * @return the dateTypePanel
	 */
	public JPanel getDateTypePanel() {
		return dateTypePanel;
	}

	/**
	 * @param dateTypePanel
	 *            the dateTypePanel to set
	 */
	public void setDateTypePanel(JPanel dateTypePanel) {
		this.dateTypePanel = dateTypePanel;
	}

	/**
	 * Check if the column id field is empty.
	 * 
	 * @return true if it is.
	 */
	public boolean isColumnIDEmpty() {
		return getColumnIDValue() == -1;
	}

	/**
	 * Check if the column name field is empty.
	 * 
	 * @return true if it is.
	 */
	public boolean isColumnNameEmpty() {
		return getColumnNameValue().equals("");
	}

	/**
	 * Check if the date type field is empty.
	 * 
	 * @return true if it is.
	 */
	public boolean isColumnDateTypeEmpty() {
		if (hasDateType()) {
			return getColumnDateTypeValue().equals("");
		} else {
			return false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == columnType) {
			handleColumnTypeDrowDown();
		}
	}

	/**
	 * Handle the ActionListener of the type drop down menu.
	 */
	public void handleColumnTypeDrowDown() {
		String selectedItem = columnType.getSelectedItem().toString();
		if (selectedItem.equals("Date")
				&& !comboValue.toLowerCase().equals("date")) {
			addTheDateTypeElement(selectedItem);
		} else if ((selectedItem.equals("Int") || selectedItem.equals("String"))
				&& comboValue.toLowerCase().equals("date")) {
			removeTheDateTypeElement(selectedItem);
		}
	}

	/**
	 * Add the date type element to the column panel.
	 * 
	 * @param selectedItem
	 *            the string of the selected item
	 */
	private void addTheDateTypeElement(String selectedItem) {
		comboValue = selectedItem;
		dateTypePanel = makeFormRowWithTextField("Specified datetype: ",
				getDateType());
		panel.add(dateTypePanel, setGrids(0, THREE));
		panel.revalidate();
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	/**
	 * Remove the date type element from the column panel.
	 * 
	 * @param selectedItem
	 *            the string of the selected item
	 */
	private void removeTheDateTypeElement(String selectedItem) {
		comboValue = selectedItem;
		dateTypePanel.setVisible(false);
		panel.revalidate();
	}
}
