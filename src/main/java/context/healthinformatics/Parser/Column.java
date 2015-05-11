package context.healthinformatics.Parser;

/**
 * class for giving information about the columns
 * to the parsers.
 */
public class Column {
	/**
	 * The number of the column this column object is for.
	 * Starts @ number 1.
	 */
	private int columnNumber;
	/**
	 * The name of the column.
	 */
	private String columnName;
	/**
	 * The type of this column.
	 * String/Integer.
	 */
	private String columnType;

	/**
	 * Constructor for column.
	 * @param id the columnNumber.
	 * @param cName the columnName.
	 * @param cType the columnType.
	 */
	public Column(int id, String cName, String cType) {
		setColumnNumber(id);
		setColumnName(cName);
		setColumnType(cType);
	}

	/**
	 * getter for the column number.
	 * @return columnNumber.
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * sets the columnNuber.
	 * @param columnNumber the number to set.
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * getter for the column name.
	 * @return columnName.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * setter for the column name.
	 * @param columnName the name to set.
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * getter for the column type.
	 * @return columnType.
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * setter for the column type.
	 * @param columnType the type to set.
	 */
	public void setColumnType(String columnType) {
		if (columnType != null) {
			if (columnType.toLowerCase().equals("string")) {
				this.columnType = "varchar(150)";
			}
			if (columnType.toLowerCase().equals("integer")) {
				this.columnType = "INT";
			}
		}
		else {
			this.columnType = columnType;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false; }
		if (o instanceof Column) {
			Column c = (Column) o;
			if (getColumnName().equals(c.getColumnName())
					&& getColumnNumber() == c.getColumnNumber()
					&& getColumnType().equals(c.getColumnType())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates a String representation of the column object.
	 * @return returns a String representing a column.
	 */
	@Override
	public String toString() {
		return "Column<" + columnNumber + " " + columnName + " " + columnType + ">";
	}
}
