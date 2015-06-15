package context.healthinformatics.parser;

/**
 * class for giving information about the columns to the parsers.
 */
public class Column  implements Comparable<Column> {
	/**
	 * The number of the column this column object is for. Starts @ number 1.
	 */
	private int columnNumber;
	/**
	 * The name of the column.
	 */
	private String columnName;
	/**
	 * The type of this column. String/Integer.
	 */
	private String columnType;

	/**
	 * The type of the date specified.
	 */
	private String dateType;

	/**
	 * Constructor for column.
	 * 
	 * @param id
	 *            the columnNumber.
	 * @param cName
	 *            the columnName.
	 * @param cType
	 *            the columnType.
	 */
	public Column(int id, String cName, String cType) {
		setColumnNumber(id);
		setColumnName(cName);
		setColumnType(cType);
		dateType = null;
	}

	/**
	 * getter for the column number.
	 * 
	 * @return columnNumber.
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * sets the columnNuber.
	 * 
	 * @param columnNumber
	 *            the number to set.
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * getter for the column name.
	 * 
	 * @return columnName.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * setter for the column name.
	 * 
	 * @param columnName
	 *            the name to set.
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * getter for the column type.
	 * 
	 * @return columnType.
	 */
	public String getColumnType() {
		return columnType;
	}

	/**
	 * Sets date type.
	 * 
	 * @param s
	 *            specified date.
	 */
	public void setDateType(String s) {
		this.dateType = s;
	}

	/**
	 * Returns date type.
	 * 
	 * @return date type as string.
	 */
	public String getDateType() {
		return this.dateType;
	}

	/**
	 * Returns if this column is a time column.
	 * @return true iff datetype contains Time constraint.
	 */
	public boolean isTime() {
		if (dateType != null) {
			return dateType.contains("H");	
		}
		return false;
	}

	/**
	 * setter for the column type.
	 * 
	 * @param columnType
	 *            the type to set.
	 */
	public void setColumnType(String columnType) {
		if (columnType != null) {
			if (columnType.toLowerCase().equals("string")) {
				this.columnType = "varchar(150)";
			} else if (columnType.toLowerCase().equals("integer")) {
				this.columnType = "INT";
			} else if (columnType.toLowerCase().equals("date")) {
				this.columnType = "DATE";
			} else {
				this.columnType = columnType;
			}
		}
	}

	/**
	 * Generates hashcode for object column.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (columnName != null) {
			result += columnName.hashCode();
		}
		result = prime * result + columnNumber;
		result = prime * result;
		if (columnName != null) {
			result += columnType.hashCode();
		}
		result = prime * result;
		if (columnName != null && dateType != null) {
			result += dateType.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Column) {
			Column c = (Column) o;
			if (getColumnName().equals(c.getColumnName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param o column object.
	 * @return true or false.
	 */
	public int compareTo(Column o) {
		return columnName.hashCode() - columnName.hashCode();
	}

	/**
	 * Creates a String representation of the column object.
	 * 
	 * @return returns a String representing a column.
	 */
	@Override
	public String toString() {
		return "Column<" + columnNumber + " " + columnName + " " + columnType
				+ ">";
	}
}
