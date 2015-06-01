package context.healthinformatics.gui;

import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * Class which contains all field for a column form element.
 */
public class ColumnFieldContainer {
	private JTextField columnID;
	private JTextField columnName;
	private JComboBox<String> columnType;
	private JTextField dateType;

	/**
	 * Constructor for column without a datetype.
	 * 
	 * @param columnID
	 *            the id of the column
	 * @param columnName
	 *            the name of the column
	 * @param comboBoxStrings
	 *            the strings for the combox
	 */
	public ColumnFieldContainer(int columnID, String columnName,
			String[] comboBoxStrings) {
		this.columnID = new JTextField(columnID);
		this.columnName = new JTextField(columnName);
		this.columnType = new JComboBox<>(comboBoxStrings);
		this.dateType = null;
	}

	/**
	 * Constructor for a columnField whit all needed TextFields and combobox.
	 * 
	 * @param columnID
	 *            the id value of the column
	 * @param columnName
	 *            the name of the column
	 * @param comboBoxStrings
	 *            the column type dropdown
	 * @param dateType
	 *            the datetype
	 */
	public ColumnFieldContainer(int columnID, String columnName,
			String[] comboBoxStrings, String dateType) {
		this.columnID = new JTextField(columnID);
		this.columnName = new JTextField(columnName);
		this.columnType = new JComboBox<>(comboBoxStrings);
		this.dateType = new JTextField(dateType);
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
	 * Check if has date type field.
	 * 
	 * @return true if has date type
	 */
	public boolean hasDateType() {
		return dateType != null;
	}

	/**
	 * Get the inputed value of the columnID field.
	 * 
	 * @return the value
	 */
	public String getColumnIDValue() {
		return columnID.getText();
	}

	/**
	 * Get the inputed value of the columnName field.
	 * 
	 * @return the value
	 */
	public String getColumnNameValue() {
		return columnID.getText();
	}

	/**
	 * Get the inputed value of the columnType combobox.
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
		if (hasDateType()) {
			return dateType.getText();
		} else {
			return null;
		}
	}

	/**
	 * Get the column id textfield.
	 * 
	 * @return the textfield
	 */
	public JTextField getColumnID() {
		return columnID;
	}

	/**
	 * Get the columnName textfield.
	 * 
	 * @return the textfield
	 */
	public JTextField getColumnName() {
		return columnName;
	}

	/**
	 * Get the columnType combobox.
	 * 
	 * @return the combobox
	 */
	public JComboBox<String> getColumnType() {
		return columnType;
	}

	/**
	 * Get the date type textfield.
	 * 
	 * @return the textfield
	 */
	public JTextField getDateType() {
		return dateType;
	}
}
