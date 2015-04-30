package context.healthinformatics.Database;

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
	private String pad = "C:/db/analyze"; //default path for testing.
	private Connection conn;
	private Statement stmt = null;

	/**
	 * Sets up connection.
	 * @throws SQLException 
	 */
	public void setupConn() {
		setDb(pad);
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
		if (conn != null) {
			try {
				stmt = conn.createStatement();
				String sql = "CREATE TABLE " + tableName + createTableColumns(columns, types);
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Connection is not set.");
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
		if (conn != null) {
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
		else {
			System.out.println("Connection is not set.");
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
		if (conn != null) {
			try {
				stmt = conn.createStatement();
				String sql = "SELECT " + variable + " FROM " + tableName;
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					res = rs.getString("test2");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Connection is not set.");
		}
		return res;
	}

	/**
	 * 
	 * @param tableName name of table to drop.
	 */
	public void dropTable(String tableName) {
		if (conn != null) {
			try {
				stmt = conn.createStatement();
				String sql = "DROP TABLE " + tableName;
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Connection is not set.");
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
	 * @param path specifies path to db.
	 */
	public void setDb(String path) {
		String prefix = "jdbc:derby:";
		String suffix = ";create=true";
		String temp = prefix + path + suffix;
		this.db = temp;
	}

}
