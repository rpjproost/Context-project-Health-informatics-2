package context.healthinformatics.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import context.healthinformatics.interfacecomponents.Observable;
import context.healthinformatics.interfacecomponents.Observer;
import context.healthinformatics.parser.Column;

/**
 * The database which stores all data out of the files.
 */
public class Db implements Observer {

	private String db;
	private String dName;
	private String pad;
	private Connection conn;
	private Statement stmt = null;
	private HashMap<String, ArrayList<Column>> tables;
	private String mergeTable = "result";
	private ArrayList<Column> columns;
	private boolean merged = false;

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
		File f = new File(pad + dName);
		removeDirectory(f);
		tables = new HashMap<String, ArrayList<Column>>();
		setupConn();
	}

	/**
	 * Sets up connection.
	 * @throws SQLException
	 *             if database query is incorrect.
	 * @return true iff connection is set.
	 */
	private boolean setupConn() throws SQLException {
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
	 * Creates new table with checks if table already exists.
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
		if (tables.get(tableName) != null) {
			if (createTableAdjustedTable(tableName, columns)) {
				dropView("workspace");
				dropTable(mergeTable);
				dropTable(tableName);
				createTableInDb(tableName, columns);
			}
			else {
				return false;
			}
		}
		else {
			createTableInDb(tableName, columns);
		}
		return true;
	}
	
	/**
	 * Creates table.
	 * @param tableName name of table to be created.
	 * @param columns arraylist of columns to be added in table.
	 * @return true if created without errors.
	 * @throws SQLException iff table already exists or database does not exist.
	 */
	public boolean createTableInDb(String tableName, ArrayList<Column> columns)
			throws SQLException {
		stmt = conn.createStatement();
		stmt.executeUpdate(SqlBuilder.createSqlTable(tableName, columns));
		tables.put(tableName, columns);
		return true;
	}

	/**
	 * Checks if an existing table has changed.
	 * @param tableName Name of table to be checked.
	 * @param columns ArrayList of new columns.
	 * @return true iff changed, false if still the same as previous table.
	 */
	public boolean createTableAdjustedTable(String tableName, ArrayList<Column> columns) {
		ArrayList<Column> c = tables.get(tableName);
		if (c != null && !(c.equals(columns))) {
			return true;
		}
		return false;
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
	 * Selects date with resultid.
	 * @param id result id integer.
	 * @return Date in date column.
	 * @throws SQLException id could not be found.
	 */
	public Date selectDate(int id) throws SQLException {
		ResultSet rs = null;
		Date datum = null;
		try {
			stmt = conn.createStatement();
			String sql = SqlBuilder.createSelectWithOneColumn(mergeTable,
					"date", mergeTable + "id = " + id);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				datum = rs.getDate("date");
			}
		} catch (SQLException e) {
			throw new SQLException("Error with recovering date with resultid.");
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return datum;
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
	 * method for dropping a view from the database.
	 * 
	 * @param viewName
	 *            The name of the view to drop.
	 * @throws SQLException if the view can't be dropped.
	 */
	public void dropView(String viewName) throws SQLException {
		String sql = "DROP VIEW " + viewName;
		executeUpdate(sql);
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

	/**
	 * if database has a merged table true.
	 * @return true/false.
	 */
	public boolean isMerged() {
		return merged;
	}

	/**
	 * Sets boolean to true if database gets mergetable.
	 * @param merged boolean.
	 */
	public void setMerged(boolean merged) {
		this.merged = merged;
	}

	/**
	 * updates the singleton of the database.
	 */
	@Override
	public void update(String param) {
		SingletonDb.update(param);
	}

	/**
	 * observers if the project is updated.
	 */
	@Override
	public void observe(Observable o) {
	 o.subscribe(this);
	}
}
