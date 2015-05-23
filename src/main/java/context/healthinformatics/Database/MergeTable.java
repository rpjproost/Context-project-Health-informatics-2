package context.healthinformatics.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import context.healthinformatics.Parser.Column;
import context.healthinformatics.SequentialDataAnalysis.Chunk;

/**
 * 
 *
 */
public class MergeTable {
	
	private Db data;
	
	/**
	 * 
	 */
	public MergeTable() {
		data = SingletonDb.getDb();
	}
	
	/**
	 * @param clause clause the tables merge.
	 * @throws SQLException if tables are not able to merge.
	 */
	public void merge(String[] clause) throws SQLException {
		mergeTables(clause);
		mergeTablesView();
	}
	
	/**
	 * creates the view ordered by date of the workspace.
	 */
	public void mergeTablesView() {
		//TODO make sure this works with 1 date column.
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE VIEW workspace AS SELECT * FROM result ORDER BY ");
		sql.append("date");
		try {
			data.executeUpdate(sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Method for merging the different files into 1 workspace.
	 * @param clause String array of clauses per table.
	 * @throws SQLException Throws an SQLException if inserting goes wrong.
	 */
	public void mergeTables(String[] clause) throws SQLException {
		HashMap<String, ArrayList<Column>> tables = data.getTables();
		ArrayList<Column> columns = new ArrayList<Column>();
		Set<String> allTables = new TreeSet<String>();
		allTables.addAll(tables.keySet());
		
		for (Entry<String, ArrayList<Column>> entries : tables.entrySet()) {
			for (int i = 0; i < entries.getValue().size(); i++) {
				if (!(columns.contains(entries.getValue().get(i)))) {
					columns.add(entries.getValue().get(i));
				}
			}
		}
		data.createTable("result", columns);
		insertTables(allTables, clause);
	}
	
	/**
	 * method for inserting all tables into the workspace.
	 * @param tableNames set containing all tables to add.
	 * @param clause all clauses for these tables.
	 */
	public void insertTables(Set<String> tableNames, String[] clause) {
		for (String key : tableNames) {
			String tableClause = "";
			for (int i = 0; i < clause.length; i++) {
				if (clause[i].contains(key)) {
					tableClause = clause[i];
				}
			}
			insertTable(key, data.getTables().get(key), tableClause);
		}
	}
	
	/**
	 * Method that inserts a table into the workspace.
	 * @param key Table name to insert.
	 * @param cols	Array of columns belonging to the table to insert.
	 * @param clause filter for the records in sql. If an empty string or
	 * 				<code>null</code> is inserted no clause is used.
	 */
	public void insertTable(String key, ArrayList<Column> cols, String clause) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO result (");
		appendColumns(cols, sql);
		sql.append(") SELECT ");
		appendColumns(cols, sql);
		sql.append("FROM ").append(key);
		if (clause != null && clause.length() > 0) {
			sql.append(" WHERE ").append(clause);
		}
		try {
			data.executeUpdate(sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * method for appending Column names to an sql query.
	 * @param columns ArrayList of columns to append.
	 * @param sql StringBuilder with the preceding sql query.
	 */
	public void appendColumns(ArrayList<Column> columns, StringBuilder sql) {
		String prefix = "";
		for (int i = 0; i < columns.size(); i++) {
			sql.append(prefix);
			prefix = ", ";
			String name = columns.get(i).getColumnName();
			sql.append(name);
		}
		sql.append(" ");
	}
	
	/**
	 * method for appending table names to an sql query.
	 * @param tables tables to insert.
	 * @param sql StringBuilder with the preceding sql query.
	 */
	public void appendTables(Set<String> tables, StringBuilder sql) {
		String prefix = "";
		for (String key : tables) {
			sql.append(prefix);
			prefix = ", ";
			sql.append(key);
		}
	}
	
	/**
	 * method for dropping a view from the database.
	 * @param viewName The name of the view to drop.
	 */
	public void dropView(String viewName) {
		String sql = "DROP VIEW " + viewName;
		try {
			data.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the workspace in chunks.
	 * @return an arrayList of chunks.
	 * @throws SQLException iff workspace does not exist.
	 */
	public ArrayList<Chunk> getChunks() throws SQLException {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		ResultSet rs = data.selectResultSet("workspace", "resultid", "");
		while (rs.next()) {
			Chunk c = new Chunk();
			int line = rs.getInt("resultid");
			c.setLine(line);
			chunks.add(c);
		}
		return chunks;
	}
}
