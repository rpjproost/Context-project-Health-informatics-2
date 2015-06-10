package context.healthinformatics.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import context.healthinformatics.parser.Column;

/**
 * 
 * @author Rick, 30 apr 2015.
 *
 */
public class Db {

	private String db; // db query.
	private String dName;
	private String pad;
	private Connection conn;
	private Statement stmt = null;
	private HashMap<String, ArrayList<Column>> tables;
	private String mergeTable = "result";
	private ArrayList<Column> columns;

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
		dName = databaseName;
		pad = p;
		//File delDb = new File(pad + dName);
		tables = new HashMap<String, ArrayList<Column>>();
		//removeDirectory(delDb);
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
		System.out.println(dName);
		boolean res = false;
		setDb(pad, dName);
		try {
			conn = DriverManager.getConnection(db);
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
	public boolean createTable(String tableName, ArrayList<Column> columns)
			throws SQLException {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			String sql = SqlBuilder.createSqlTable(tableName, columns);
			stmt.executeUpdate(sql);
			res = true;
			tables.put(tableName, columns);
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * returns highest Identifier from table, otherwise 0.
	 * 
	 * @param tableName
	 *            table to return max identifier from.
	 * @return 0 or max id.
	 * @throws SQLException
	 *             if table name is incorrect.
	 */
	public int getMaxId(String tableName) throws SQLException {
		int res = 0;
		try {
			stmt = conn.createStatement();
			String sql = SqlBuilder.queryForMaxId(tableName);
			ResultSet rs = stmt.executeQuery(sql);
			res = SqlBuilder.calculateMaxId(rs);
			rs.close();
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * Inserts data into table.
	 * 
	 * @param tableName
	 *            Name of table values to be inserted.
	 * @param values
	 *            String array of values.
	 * @param columns
	 *            ArrayList of column names and types.
	 * @return true iff data is inserted.
	 * @throws SQLException
	 *             Exception occurs when data is not delivered as type to be
	 *             inserted.
	 */
	public boolean insert(String tableName, String[] values,
			ArrayList<Column> columns) throws SQLException {
		boolean res = false;
		try {
			PreparedStatement stm = SqlBuilder.createSqlInsert(tableName,
					columns, values, conn);
			stm.execute();
			stm.close();
			res = true;
		} catch (SQLException | NullPointerException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * Selects all columns from tableName with Where clause.
	 * 
	 * @param tableName
	 *            Name of table data to be selected from.
	 * @param whereClause
	 *            The constraint.
	 * @return Resultset with data.
	 * @throws SQLException
	 *             iff data is not found in table or clause was not formatted
	 *             correctly.
	 */
	public ResultSet selectAllWithWhereClause(String tableName,
			String whereClause) throws SQLException {
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = SqlBuilder.createSelectSqlWithWhereClause(tableName,
					whereClause);
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			throw new SQLException(whereClause);
		}
		return rs;
	}

	/**
	 * Select one column from tablename.
	 * 
	 * @param tableName
	 *            the table name
	 * @param variable
	 *            the variable
	 * @param whereClause
	 *            the constraint.
	 * @return the ResultSet(data from one column)
	 * @throws SQLException
	 *             the SQL exception
	 */
	public ResultSet selectResultSet(String tableName, String variable,
			String whereClause) throws SQLException {
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String sql = SqlBuilder.createSelectWithOneColumn(tableName,
					variable, whereClause);
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			throw new SQLException(whereClause);
		}
		return rs;
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
			String sql = SqlBuilder.createSqlDropTable(tableName);
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
	 * @param sql
	 *            query.
	 * @return true iff sql query is executed.
	 * @throws SQLException
	 *             iff update could not be executed on database.
	 */
	public boolean executeUpdate(String sql) throws SQLException {
		boolean res = false;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			res = true;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * Closes connection for new project.
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	 * Returns name of database.
	 * @return String name of database.
	 */
	public String getDbName() {
		return dName;
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
			res = removeDirList(dir.list(), dir);
		}
		return res;
	}

	/**
	 * Remove a list of files in a directory.
	 * 
	 * @param list
	 *            the list of files
	 * @param dir
	 *            the directory
	 * @return true if deleted directory
	 */
	public boolean removeDirList(String[] list, File dir) {
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
		return dir.delete();
	}
	
	/**
	 * Gets mergeTable name.
	 * @return String of mergeTable name.
	 */
	public String getMergeTable() {
		return mergeTable;
	}

	/**
	 * Sets mergeTable name.
	 * @param mt String of mergeTable name.
	 */
	public void setMergeTable(String mt) {
		mergeTable = mt;
	}
	
	/**
	 * Sets all columns in result table.
	 * @param col column list.
	 */
	public void setColumns(ArrayList<Column> col) {
		columns = col;
	}
	
	/**
	 * Get all columns in result table.
	 * @return list with columns.
	 */
	public ArrayList<Column> getColumns() {
		if (columns != null) {
			return columns;
		}
		return new ArrayList<Column>();
	}
}
