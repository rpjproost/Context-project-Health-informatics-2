package context.healthinformatics.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.joda.time.DateTime;

import context.healthinformatics.parser.Column;

/**
 * 
 * SqlBuilder class.
 *
 */
public final class SqlBuilder {

	private SqlBuilder() {
	}

	/**
	 * Creates query for new table in database.
	 * @param tableName Name of table.
	 * @param columns ArrayList of Columns with names of columns and datatypes.
	 * @return the sql query to be executed.
	 */
	public static String createSqlTable(String tableName, ArrayList<Column> columns) {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE ").append(tableName);
		createTableColumns(tableName, columns, sql);
		return sql.toString();
	}

	/**
	 * Appends query with tableColumn names and types of createTable.
	 * @param tableName Name of table to create.
	 * @param columns ArrayList of Columns for column names and types.
	 * @param res Query to be appended.
	 */
	private static void createTableColumns(String tableName, ArrayList<Column> columns, 
			StringBuffer res) {
		createTableWithIndexedColumn(tableName, res);
		createTableWithColumnsFromArrayList(columns, res);
	}

	/**
	 * Appends an indexed column to createTable query.
	 * @param tableName Name of table to create.
	 * @param res res Query to be appended.
	 */
	private static void createTableWithIndexedColumn(String tableName, StringBuffer res) {
		res.append("(").append(tableName);
		res.append("ID int not null "
				+ "primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ");
	}

	/**
	 * Appends every column in Arraylist to the query of createTable.
	 * @param columns ArrayList of Columns for column names and types.
	 * @param res res Query to be appended.
	 */
	private static void createTableWithColumnsFromArrayList(ArrayList<Column> columns, 
			StringBuffer res) {
		for (int i = 0; i < columns.size(); i++) {
			res.append(columns.get(i).getColumnName());
			res.append(" ");
			if (columns.get(i).isTime()) {
				res.append("INT");
			}
			else {
				res.append(columns.get(i).getColumnType());
			}
			if (i == columns.size() - 1) {
				res.append(")");
			} else {
				res.append(",");
			}
		}
	}

	/**
	 * @param tableName Name of table to be queried.
	 * @return query for executing.
	 */
	public static String queryForMaxId(String tableName) {
		return "SELECT MAX(" + tableName + "ID) FROM " + tableName;
	}

	/**
	 * Gets highest identifier out of table.
	 * @param rs Resultset to be searched for max ID.
	 * @return max ID as integer or 0.
	 * @throws SQLException if resultset is empty. Cannot happen here.
	 */
	public static int calculateMaxId(ResultSet rs) throws SQLException {
		int res = 0;
		while (rs.next()) {
			int max = rs.getInt(1);
			if (max > res) {
				res = max;
			}
		}
		rs.close();
		return res;
	}

	/**
	 * Returns a preparedStatement to be executed for inserting variables in a table.
	 * @param tableName Name of table data to be inserted.
	 * @param columns ArrayList of column names and types.
	 * @param values String[] of values to be inserted.
	 * @param conn Connection to database to prepare statement.
	 * @return PreparedStatement to be executed.
	 * @throws SQLException iff data could not be inserted.
	 */
	public static PreparedStatement createSqlInsert(String tableName, ArrayList<Column> columns, 
			String[] values, Connection conn) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + tableName + "(");
		appendQueryColumns(sql, columns);
		appendQueryValues(sql, values);
		return appendValuesInsert(sql.toString(), values,
				columns, conn);
	}

	/**
	 * Appends columns to query.
	 * @param sql SQL query to be appended.
	 * @param columns ArrayList of column names.
	 */
	private static void appendQueryColumns(StringBuilder sql, ArrayList<Column> columns) {
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i).getColumnName());
			if (i == columns.size() - 1) {
				sql.append(")");
			} else {
				sql.append(",");
			}
		}
	}

	/**
	 * Appends values to query as questionmarks for prepared statement.
	 * @param sql SQL query to be appended.
	 * @param values String[] of values to be inserted. (for length).
	 */
	private static void appendQueryValues(StringBuilder sql, String[] values) {
		sql.append(" VALUES (");
		for (int i = 0; i < values.length; i++) {
			if (i == values.length - 1) {
				sql.append("?)");
			} else {
				sql.append("?,");
			}
		}
	}


	/**
	 * Creates Prepared statement for inserting values.
	 * @param sql SQL query to convert into prepared statement.
	 * @param values values to be inserted in prepared statement.
	 * @param columns ArrayList of column names and types.
	 * @param conn Connection to database to create preparedstatement.
	 * @return preparedStatement to be executed.
	 * @throws SQLException iff values could not be inserted.
	 */
	private static PreparedStatement appendValuesInsert(String sql, String[] values,
			ArrayList<Column> columns, Connection conn) throws SQLException {
		PreparedStatement preparedStmt = conn.prepareStatement(sql);
		for (int i = 0; i < values.length; i++) {
			String type = columns.get(i).getColumnType().toLowerCase();
			if (type.startsWith("varchar")) {
				preparedStmt.setString(i + 1, values[i]);
			} else if (type.equals("date")) {
				appendDateIntoPreparedStatement(preparedStmt, values, columns, i);
			} else if (type.equals("int")) {
				appendIntIntoPreparedStatement(preparedStmt, values, i);
			}
			else {
				preparedStmt.close();
				throw new SQLException("type of insert not recognized.");
			}
		}
		return preparedStmt;
	}

	/**
	 * Inserts date into preparedStatement.
	 * @param p PreparedStatement
	 * @param values String of values where one is the date.
	 * @param columns ArrayList of columns, where dateType is specified.
	 * @param i Index of value String and preparedStatement.
	 * @throws SQLException Iff date could not be inserted.
	 */
	private static void appendDateIntoPreparedStatement(PreparedStatement p, String[] values, 
			ArrayList<Column> columns, int i) throws SQLException {
		String dateType = columns.get(i).getDateType();
		if (dateType.contains("H")) {
			int time = convertTime(values[i], dateType);
			p.setInt(i + 1, time);
		}
		else {
			java.sql.Date date = convertDate(values[i], dateType);
			p.setDate(i + 1, date);
		}
	}

	/**
	 * Converts the time.
	 * @param time the time to be converted.
	 * @param dateT input string.
	 * @return the parsed time.
	 */
	private static int convertTime(String time, String dateT) {
		SimpleDateFormat input = new SimpleDateFormat(dateT);
		int res = 0;
		final int hundred = 100;
		try {
			java.util.Date date = input.parse(time);
			DateTime dateTime = new DateTime(date);
			int h = dateTime.getHourOfDay();
			int m = dateTime.getMinuteOfHour();
			int timeToString = h * hundred + m;
			String temp = timeToString + "";
			res = Integer.parseInt(temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Inserts an integer into an preparedStatement.
	 * @param p Prepared statement.
	 * @param values String of values where one is a integer.
	 * @param i index of value String[] and preparedStatement.
	 * @throws SQLException Iff integer could not be inserted.
	 */
	private static void appendIntIntoPreparedStatement(PreparedStatement p, 
			String[] values, int i) throws SQLException {
		double value = 0;
		try {
			value = Double.parseDouble(values[i]);
		} catch (Exception e) {
			value = Integer.MIN_VALUE;
		}
		p.setDouble(i + 1, value);
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
	private static java.sql.Date convertDate(String s, String dateT) {
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

	/**
	 * Returns SQL query to select all data with a whereClause.
	 * @param tableName Name of table where data to be selected from.
	 * @param whereClause The constraint for this query.
	 * @return String of query to be executed.
	 */
	public static String createSelectSqlWithWhereClause(String tableName, String whereClause) {
		return "SELECT * FROM " + tableName + " WHERE " + whereClause;
	}

	/**
	 * Returns SQL query with or without whereClause but only selects one column of
	 * data from a table.
	 * @param tableName Name of table where data to be selected from.
	 * @param variable Column name to select.
	 * @param whereClause The constraint IF IT IS GIVEN.
	 * @return SQL query String to be executed.
	 */
	public static String createSelectWithOneColumn(String tableName, String variable
			, String whereClause) {
		String sql = "SELECT " + variable + " FROM " + tableName;
		if (!(whereClause.equals(""))) {
			sql += " WHERE " + whereClause;
		}
		return sql;
	}

	/**
	 * Returns SQL query for dropping a table.
	 * @param tableName Name of table to be dropped.
	 * @return SQL query String.
	 */
	public static String createSqlDropTable(String tableName) {
		return "DROP TABLE " + tableName;
	}

}
