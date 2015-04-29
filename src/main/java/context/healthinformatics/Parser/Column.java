package context.healthinformatics.Parser;

/**
 * Column Object keeps track of the specifics of a column.
 * @author Wim Spaargaren, April 2014
 *
 */
public class Column {

	/**
	 * The column attributes.
	 */
	private int id;
	private String cName;
	private String cTYpe;
	
	/**
	 * Constructor.
	 * @param id the column id
	 * @param cName the column name
	 * @param cType the column type
	 */
	public Column(int id, String cName, String cType) {
		this.id = id;
		this.cName = cName;
		this.cTYpe = cType;
	}

	/**\
	 * Get the id.
	 * @return the column id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id.
	 * @param id the column id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get the column name.
	 * @return the column name
	 */
	public String getcName() {
		return cName;
	}

	/**
	 * Set the column name.
	 * @param cName the column name
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}

	/**
	 * Get the column type.
	 * @return the column type
	 */
	public String getcTYpe() {
		return cTYpe;
	}

	/**
	 * Set the column type.
	 * @param cTYpe the column type
	 */
	public void setcTYpe(String cTYpe) {
		this.cTYpe = cTYpe;
	}

}
