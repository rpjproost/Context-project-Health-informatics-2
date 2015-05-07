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
	 */
	public Db(String databaseName, String p) throws NullPointerException {
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
	 * @throws SQLException 
	 * @return true iff connection is set.
	 */
	public boolean setupConn() {
		boolean res = false;
		setDb(pad, dName);
		try {
			conn = DriverManager.getConnection(db);
			//Check connection
			if (conn != null) {
				res = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/** Creates new table.
	 * 
	 * @param tableName name for new table.
	 * @param columns column names.
	 * @param types type specifications.
	 * @return true iff new table is created.
	 */
	public boolean createTable(String tableName, String[] columns, String[] types) {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = "CREATE TABLE " + tableName + createTableColumns(columns, types);
			stmt.executeUpdate(sql);
			res = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/** returns highest Identifier from table, otherwise 1.
	 * 
	 * @param tableName table to return max indentifier from.
	 * @return 1 or max id.
	 */
	public int getMaxId(String tableName) {
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
		} catch (SQLException e) {
			res = 1;
			e.printStackTrace();
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
		String res = "(ID int not null "
				+ "primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ";
		for (int i = 0; i < columns.length; i++) {
			if (i == columns.length - 1) {
				res = res + columns[i] + " ";
				res = res + types[i] + ")";
			}
			else {
				res = res + columns[i] + " ";
				res = res + types[i] + ",";
			}
		}
		return res;
	}

	/** Inserts values into table.
	 * 
	 * @param tableName table name the values go into.
	 * @param values the values to be added.
	 * @param columns the columns specified for the values.
	 * @return true iff successfully inserted values.
	 */
	public boolean insert(String tableName, String[] values, String[] columns) {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = "INSERT INTO " + tableName + "(";
			for (int i = 0; i < columns.length; i++) {
				if (i == values.length - 1) {
					sql = sql + columns[i] + ")";
				}
				else {
					sql = sql + columns[i] + ",";
				}
			}
			sql = sql + " VALUES (";
			for (int i = 0; i < values.length; i++) {
				if (i == values.length - 1) {
					sql = sql + values[i] + ")";
				}
				else {
					sql = sql + values[i] + ",";
				}
			}
			stmt.executeUpdate(sql);
			res = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**NEEDS WORK!
	 * 
	 * @param tableName name of table to select variables from.
	 * @param variable variables to select.
	 * @return String with results.
	 */
	public String select(String tableName, String variable) {
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
		} catch (SQLException e) {
			res = "Data not found";
			e.printStackTrace();
		}
		return res;
	}

	/**Drops a table from database.
	 * 
	 * @param tableName name of table to drop.
	 * @return true iff table is dropped.
	 */
	public boolean dropTable(String tableName) {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = "DROP TABLE " + tableName;
			stmt.executeUpdate(sql);
			res = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**Returns specified path to the database on your computer.
	 * 
	 * @return db path string.
	 */
	public String getDbPath() {
		return db;
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
		}
		return dir.delete();
	}

}
