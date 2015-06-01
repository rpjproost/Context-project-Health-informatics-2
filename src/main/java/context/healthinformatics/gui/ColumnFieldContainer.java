package context.healthinformatics.gui;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import context.healthinformatics.parser.Column;

/**
 * Class which contains all field for a column form element.
 */
public class ColumnFieldContainer {
	private JTextField columnID;
	private JTextField columnName;
	private JComboBox<String> columnType;
	private JTextField dateType;
	private String[] comboBoxValues = { "String", "Int", "Date" };

	/**
	 * Constructor of the columnFieldContainer based on the input Column.
	 * 
	 * @param column
	 *            the column
	 */
	public ColumnFieldContainer(Column column) {
		this.columnID = new JTextField(Integer.toString(column
				.getColumnNumber()));
		this.columnName = new JTextField(column.getColumnName());
		this.columnType = new JComboBox<>(comboBoxValues);
		this.columnType.setSelectedIndex(getComboBoxIndex(column
				.getColumnType()));
		if (hasDateType(column.getColumnType())) {
			this.dateType = new JTextField(column.getDateType());
		}
	}

	/**
	 * Check if a column has a date type.
	 * 
	 * @param columnType
	 *            the type of the column
	 * @return true if has datetype else false
	 */
	public boolean hasDateType(String columnType) {
		return columnType.equals("DATE");
	}

	/**
	 * Get the index of the combo box based on the given columntype.
	 * 
	 * @param columnType
	 *            the columntype of the column
	 * @return the index of the combo box
	 */
	public int getComboBoxIndex(String columnType) {
		if (columnType.equals("DATE")) {
			return 2;
		} else if (columnType.equals("INT")) {
			return 1;
		} else {
			return 0;
		}

	}

	/**
	 * Build a column object from the textfield values.
	 * 
	 * @return a Column Object
	 */
	public Column getColumn() {
		Column col = new Column(Integer.parseInt(getColumnIDValue()),
				getColumnNameValue(), getColumnTypeValue());
		if (hasDateType()) {
			col.setDateType(getColumnDateTypeValue());
		}
		return col;
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
