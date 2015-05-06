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

	/**
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

	/**
	 * Sets up connection.
	 * @throws SQLException 
	 */
	public void setupConn() {
		setDb(pad, dName);
		try {
			conn = DriverManager.getConnection(db);
			//Check connection
			if (conn != null) {
				System.out.println("Connected to database");
			}
			else {
				System.out.println("Could not connect to database");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param tableName name for new table.
	 * @param columns column names.
	 * @param types type specifications.
	 */
	public void createTable(String tableName, String[] columns, String[] types) {
		try {
			stmt = conn.createStatement();
			String sql = "CREATE TABLE " + tableName + createTableColumns(columns, types);
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param columns column names.
	 * @param types type specifications.
	 * @return string for sql building.
	 */
	public String createTableColumns(String[] columns, String[] types) {
		String res = "(";
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

	/**
	 * 
	 * @param tableName insert into this table.
	 * @param values variables to insert.
	 */
	public void insert(String tableName, String[] values) {
		try {
			stmt = conn.createStatement();
			String sql = "INSERT INTO " + tableName + " VALUES (";
			for (int i = 0; i < values.length; i++) {
				if (i == values.length - 1) {
					sql = sql + values[i] + ")";
				}
				else {
					sql = sql + values[i] + ",";
				}
			}
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	/**
	 * 
	 * @param tableName name of table to drop.
	 */
	public void dropTable(String tableName) {
		try {
			stmt = conn.createStatement();
			String sql = "DROP TABLE " + tableName;
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
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

	/**
	 * 
	 * @param path path where database is going to be stored.
	 * @param dbName name of database.
	 */
	public void setDb(String path, String dbName) {
		String prefix = "jdbc:derby:";
		String suffix = ";create=true";
		String temp = prefix + path + dbName + suffix;
		this.db = temp;
	}

	/**
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
