package context.healthinformatics.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import context.healthinformatics.Parser.Column;

/**
 * 
 * @author Rick, 30 apr 2015.
 *
 */
public class Db {

	private String db; // db query.
	private String dName;
	private String pad = "C:/db/"; // default path for testing.
	private Connection conn;
	private Statement stmt = null;
	private HashMap<String, ArrayList<Column>> tables;

	/**
	 * Constructor, sets variables and calls setupConn.
	 * 
	 * @param databaseName
	 *            name of database you connect to.
	 * @param p
	 *            directory for the database to be stored.
	 * @throws NullPointerException
	 *             when input is null.
	 * @throws SQLException
	 *             the sql exception
	 */
	protected Db(String databaseName, String p) throws NullPointerException,
	SQLException {
		if (p == null || databaseName == null) {
			throw new NullPointerException();
		}
		boolean isWhitespace = databaseName.matches("^\\s*$");
		boolean isWhitespace2 = p.matches("^\\s*$");

		if (!isWhitespace) {
			dName = databaseName;
		} else {
			dName = "default";
		}
		if (!isWhitespace2) {
			pad = p;
		}
		tables = new HashMap<String, ArrayList<Column>>();
		File delDb = new File(pad + dName);
		removeDirectory(delDb);
		setupConn();
	}

	/**
	 * Sets up connection.
	 * 
	 * @throws SQLException
	 *             if database query is incorrect.
	 * @return true iff connection is set.
	 */
	private boolean setupConn() throws SQLException {
		boolean res = false;
		setDb(pad, dName);
		try {
			conn = DriverManager.getConnection(db);
			// Check connection
			if (conn != null) {
				res = true;
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * Creates new table.
	 * 
	 * @param tableName
	 *            name for new table.
	 * @param columns
	 *            column names and types.
	 * @return true iff new table is created.
	 * @throws SQLException
	 *             if table could not be created.
	 */
	public boolean createTable(String tableName, ArrayList<Column> columns) throws SQLException {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = "CREATE TABLE " + tableName
					+ createTableColumns(tableName, columns);
			stmt.executeUpdate(sql);
			res = true;
			tables.put(tableName, columns);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * returns highest Identifier from table, otherwise 1.
	 * 
	 * @param tableName
	 *            table to return max identifier from.
	 * @return 1 or max id.
	 * @throws SQLException
	 *             if table name is incorrect.
	 */
	public int getMaxId(String tableName) throws SQLException {
		int res = 1;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT MAX(" + tableName + "ID) FROM " + tableName;
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

	/**
	 * Part of the method CreateTable, creates columns with specified types.
	 * 
	 * @param tableName 
	 * 				tablename.
	 * @param columns
	 *            column names and types.
	 * @return string for sql building.
	 */
	public String createTableColumns(String tableName, ArrayList<Column> columns) {
		StringBuffer res = new StringBuffer();
		res.append("(").append(tableName);
		res.append("ID int not null "
				+ "primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
		for (int i = 0; i < columns.size(); i++) {
			if (i == columns.size() - 1) {
				res.append(columns.get(i).getColumnName());
				res.append(" ");
				res.append(columns.get(i).getColumnType());
				res.append(")");
			} else {
				res.append(columns.get(i).getColumnName());
				res.append(" ");
				res.append(columns.get(i).getColumnType());
				res.append(",");
			}
		}
		return res.toString();
	}

	/**
	 * Insert values in to table.
	 * 
	 * @param tableName
	 *            name of table.
	 * @param values
	 *            values to be inserted.
	 * @param columns
	 *            columns where values be inserted.
	 * @return true iff values are inserted.
	 * @throws SQLException
	 *             if values could not be inserted.
	 */
	public boolean insert(String tableName, String[] values,
			ArrayList<Column> columns) throws SQLException {
		boolean res = false;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO " + tableName + "(");
			for (int i = 0; i < columns.size(); i++) {
				if (i == values.length - 1) {
					sql.append(columns.get(i).getColumnName()); sql.append(")");
				} else {
					sql.append(columns.get(i).getColumnName()); sql.append(",");
				}
			}
			sql.append(" VALUES (");
			for (int i = 0; i < values.length; i++) {
				if (i == values.length - 1) {
					sql.append("?)");
				}
				else {
					sql.append("?,");
				}
			}
			PreparedStatement stm = appendValuesInsert(sql.toString(), values, columns);
			stm.execute();
			res = true;
		} catch (SQLException | NullPointerException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * 
	 * @param s sql query to be turned into preparedStatement.
	 * @param values to be inserted into table.
	 * @param columns ArrayList of columns, types and dateTypes are used.
	 * @return preparedstatement to be executed.
	 * @throws SQLException 
	 */
	public PreparedStatement appendValuesInsert(String s, String[] values,
			ArrayList<Column> columns) throws SQLException {
		PreparedStatement preparedStmt = conn.prepareStatement(s);
		for (int i = 0; i < values.length; i++) {
			String type = columns.get(i).getColumnType().toLowerCase();
			if (type.startsWith("varchar")) {
				preparedStmt.setString(i + 1, values[i]);
			}
			else if (type.equals("date")) {
				String dateType = columns.get(i).getDateType();
				java.sql.Date date = convertDate(values[i], dateType);
				preparedStmt.setDate(i + 1, date);
			}
			else if (type.equals("int")) {
				double value = 0;
				try {
					value = Double.parseDouble(values[i]);
				} catch (Exception e) {
					throw new SQLException(e);
				}
				preparedStmt.setDouble(i + 1, value);
			}
			else {
				throw new SQLException("type of insert not recognized.");
			}
		}
		return preparedStmt;
	}

	/**
	 * Select variable from tablename.
	 * 
	 * @param tableName
	 *            the table name
	 * @param variable
	 *            the variable
	 * @return the resultset
	 * @throws SQLException
	 *             the sql exception
	 */
	public ResultSet selectResultSet(String tableName, String variable)
			throws SQLException {
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT " + variable + " FROM " + tableName;
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			throw new SQLException("ResultSet not created: Data not found");
		}
		return rs;
	}

	/**
	 * NEEDS WORK!
	 * 
	 * @param tableName
	 *            name of table to select variables from.
	 * @param variable
	 *            variables to select.
	 * @return String with results.
	 * @throws SQLException
	 *             if data is not found.
	 */
	public String select(String tableName, String variable) throws SQLException {
		String res = "";
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT " + variable + " FROM " + tableName;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				res = rs.getString(variable);
				System.out.println(res);
				// hier moet nog iets beters gereturned worden.
				// This method is only for testing purposes now.
			}
		} catch (SQLException e) {
			res = "Data not found";
			throw new SQLException(e);
		}
		rs.close();
		return res;
	}

	/**
	 * Drops a table from database.
	 * 
	 * @param tableName
	 *            name of table to drop.
	 * @return true iff table is dropped.
	 * @throws SQLException
	 *             if table does not exist.
	 */
	public boolean dropTable(String tableName) throws SQLException {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = "DROP TABLE " + tableName;
			stmt.executeUpdate(sql);
			tables.remove(tableName);
			res = true;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}
	
	/**
	 * 
	 * @param sql query.
	 * @return true iff sql query is executed.
	 */
	public boolean executeUpdate(String sql) {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			res = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Returns specified path to the database on your computer.
	 * 
	 * @return db path string.
	 */
	public String getDbPath() {
		return pad;
	}

	/**
	 * 
	 * @param path
	 *            sets db path
	 */
	public void setDbPath(String path) {
		pad = path;
	}

	/**
	 * Sets the databasedriver query.
	 * 
	 * @param path
	 *            path where database is going to be stored.
	 * @param dbName
	 *            name of database.
	 * @return query as a String.
	 */
	public String setDb(String path, String dbName) {
		String prefix = "jdbc:derby:";
		String suffix = ";create=true";
		String temp = prefix + path + dbName + suffix;
		this.db = temp;
		return db;
	}
	
	/**
	 * 
	 * @return tablenames with columns.
	 */
	public HashMap<String, ArrayList<Column>> getTables() {
		return tables;
	}

	/**
	 * Removes old database at specified path if exists.
	 * 
	 * @param dir
	 *            database directory to delete.
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
					} else {
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

	/**
	 * Converts date to sql date format.
	 * 
	 * @param s
	 *            date value.
	 * @param dateT
	 *            type of date input.
	 * @return date value in sql format.
	 */
	public java.sql.Date convertDate(String s, String dateT) {
		SimpleDateFormat input = new SimpleDateFormat(dateT);
		java.sql.Date sqlDate = null;
		try {
			java.util.Date date = input.parse(s);
			sqlDate = new java.sql.Date(date.getTime());

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sqlDate;
	}

}
