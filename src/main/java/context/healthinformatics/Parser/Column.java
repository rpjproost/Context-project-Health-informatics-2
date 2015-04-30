package context.healthinformatics.Parser;

public class Column {
	
	private int columnNumber;
	private String columnName;
	private String columnType;
	
	public Column(int id, String cName, String cType) {
		setColumnNumber(id);
		setColumnName(cName);
		setColumnType(cType);
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;
		if(o instanceof Column){
			Column c = (Column) o;
			if(getColumnName().equals(c.getColumnName()) &&
					getColumnNumber() == c.getColumnNumber() &&
					getColumnType().equals(c.getColumnType())){
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		return "<" + columnNumber + " " + columnName + " " + columnType + ">";
	}
}
