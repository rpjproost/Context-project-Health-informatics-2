package context.healthinformatics.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Rick, 30 apr 2015.
 *
 */
public class Db {

	private String db; //db query.
	private String dName;
	private String pad = "C:/db/"; //default path for testing.
	private Connection conn;
	private Statement stmt = null;

	/**Constructor, sets variables and calls setupConn.
	 * 
	 * @param databaseName name of database you connect to.
	 * @param p directory for the database to be stored.
	 * @throws NullPointerException when input is null.
	 * @throws SQLException 
	 */
	protected Db(String databaseName, String p) throws NullPointerException, SQLException {
		if (p == null || databaseName == null) {
			throw new NullPointerException();
		}
		boolean isWhitespace = databaseName.matches("^\\s*$");
		boolean isWhitespace2 = p.matches("^\\s*$");

		if (!isWhitespace) {
			dName = databaseName;
		}
		else {
			dName = "default";
		}
		if (!isWhitespace2) {
			pad = p;
		}
		File delDb = new File(pad + dName);
		removeDirectory(delDb);
		setupConn();
	}

	/**Sets up connection.
	 * 
	 * @throws SQLException if database query is incorrect.
	 * @return true iff connection is set.
	 */
	private boolean setupConn() throws SQLException {
		boolean res = false;
		setDb(pad, dName);
		try {
			conn = DriverManager.getConnection(db);
			//Check connection
			if (conn != null) {
				res = true;
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/** Creates new table.
	 * 
	 * @param tableName name for new table.
	 * @param columns column names.
	 * @param types type specifications.
	 * @return true iff new table is created.
	 * @throws SQLException if table could not be created.
	 */
	public boolean createTable(String tableName, String[] columns, String[] types) 
			throws SQLException {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = "CREATE TABLE " + tableName + createTableColumns(columns, types);
			stmt.executeUpdate(sql);
			res = true;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/** returns highest Identifier from table, otherwise 1.
	 * 
	 * @param tableName table to return max identifier from.
	 * @return 1 or max id.
	 * @throws SQLException if table name is incorrect.
	 */
	public int getMaxId(String tableName) throws SQLException {
		int res = 1;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT MAX(ID) FROM " + tableName;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int max = rs.getInt(1);
				if (max > res) {
					res = max;
				}
			}
			rs.close();
		} catch (SQLException e) {
			res = 1;
			throw new SQLException(e);
		}
		return res;
	}

	/** Part of the method CreateTable, creates columns with specified types.
	 * 
	 * @param columns column names.
	 * @param types type specifications.
	 * @return string for sql building.
	 */
	public String createTableColumns(String[] columns, String[] types) {
		StringBuffer res = new StringBuffer();
		res.append("(ID int not null "
				+ "primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
		for (int i = 0; i < columns.length; i++) {
			if (i == columns.length - 1) {
				res.append(columns[i]); res.append(" ");
				res.append(types[i]); res.append(")");
			}
			else {
				res.append(columns[i]); res.append(" ");
				res.append(types[i]); res.append(",");
			}
		}
		return res.toString();
	}

	/**Insert values in to table.
	 * 
	 * @param tableName name of table.
	 * @param values values to be inserted.
	 * @param columns columns where values be inserted.
	 * @return true iff values are inserted.
	 * @throws SQLException if values could not be inserted.
	 */
	public boolean insert(String tableName, String[] values, String[] columns) throws SQLException {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO " + tableName + "(");
			for (int i = 0; i < columns.length; i++) {
				if (i == values.length - 1) {
					sql.append(columns[i]);	sql.append(")");
				}
				else {
					sql.append(columns[i]);	sql.append(",");
				}
			}
			sql.append(" VALUES (");
			for (int i = 0; i < values.length; i++) {
				if (i == values.length - 1) {
					sql.append(values[i]); sql.append(")");
				}
				else {
					sql.append(values[i]); sql.append(",");
				}
			}
			stmt.executeUpdate(sql.toString());
			res = true;
		} catch (SQLException | NullPointerException e) {
			throw new SQLException(e);
		}
		return res;
	}
	


	/**NEEDS WORK!
	 * 
	 * @param tableName name of table to select variables from.
	 * @param variable variables to select.
	 * @return String with results.
	 * @throws SQLException if data is not found.
	 */
	public String select(String tableName, String variable) throws SQLException {
		String res = "";
		try {
			stmt = conn.createStatement();
			String sql = "SELECT " + variable + " FROM " + tableName;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				res = rs.getString(variable);
				//hier moet nog iets beters gereturned worden.
				System.out.println(res);
			}
			rs.close();
		} catch (SQLException e) {
			res = "Data not found";
			throw new SQLException(e);
		}
		return res;
	}

	/**Drops a table from database.
	 * 
	 * @param tableName name of table to drop.
	 * @return true iff table is dropped.
	 * @throws SQLException if table does not exist.
	 */
	public boolean dropTable(String tableName) throws SQLException {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = "DROP TABLE " + tableName;
			stmt.executeUpdate(sql);
			res = true;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**Returns specified path to the database on your computer.
	 * 
	 * @return db path string.
	 */
	public String getDbPath() {
		return pad;
	}

	/**
	 * 
	 * @param path sets db path
	 */
	public void setDbPath(String path) {
		pad = path; 
	}

	/**Sets the databasedriver query.
	 * 
	 * @param path path where database is going to be stored.
	 * @param dbName name of database.
	 * @return query as a String.
	 */
	public String setDb(String path, String dbName) {
		String prefix = "jdbc:derby:";
		String suffix = ";create=true";
		String temp = prefix + path + dbName + suffix;
		this.db = temp;
		return db;
	}

	/**Removes old database at specified path if exists.
	 * 
	 * @param dir database directory to delete.
	 * @return true if directory deleted.
	 */
	public boolean removeDirectory(File dir) {
		boolean res = false;
		if (dir != null) { 

			String[] list = dir.list();

			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					File file = new File(dir, list[i]);


					if (file.isDirectory()) {
						if (!removeDirectory(file)) {
							return false;
						}
					}
					else {
						if (!file.delete()) {
							return false;
						}
					}
				}
			}
			 res = dir.delete();
		}
		return res;
	}

}
