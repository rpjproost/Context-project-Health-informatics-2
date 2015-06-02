package context.healthinformatics.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	 * Init the textfield with the given values from the Column object.
	 * 
	 * @param column
	 *            the column object
	 */
	public void initTextFieldsWithValues(Column column) {
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
	 * @return true if has datetype else false
	 */
	public boolean hasDateType() {
		return comboValue.toLowerCase().equals("date");
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
		return columnName.getText();
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

	/**
	 * Set the panel of this columnfields.
	 * 
	 * @param panel
	 *            the panel
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * Check if columnfield has a panel.
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == columnType) {
			handleColumnTypeDrowDown();
		}
	}

	/**
	 * Handle the actionlistener of the type drop down menu.
	 */
	public void handleColumnTypeDrowDown() {
		String selectedItem = columnType.getSelectedItem().toString();
		if (selectedItem.equals("Date")
				&& !comboValue.toLowerCase().equals("date")) {
			addTheDateTypeElement(selectedItem);
		} else if (selectedItem.equals("Int") || selectedItem.equals("String")
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
	public void addTheDateTypeElement(String selectedItem) {
		comboValue = selectedItem;
		dateTypePanel = makeFormRowWithTextField("Specified datetype: ",
				getDateType());
		panel.add(dateTypePanel, setGrids(0, THREE));
		panel.revalidate();
	}

	/**
	 * Remove the date type element from the column panel.
	 * 
	 * @param selectedItem
	 *            the string of the selected item
	 */
	public void removeTheDateTypeElement(String selectedItem) {
		// TODO not sure if here is a bug but sometimes datetype field
		// doesnt properly appear.
		comboValue = selectedItem;
		dateTypePanel.setVisible(false);
		panel.revalidate();
	}
}
