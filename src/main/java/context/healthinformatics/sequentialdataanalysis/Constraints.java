package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * Class Constraints.
 */
public class Constraints extends Task {
	private String columnName;

	/**
	 * Constructor Constraints.
	 * @param chunks
	 *            the list of chunks
	 */
	public Constraints(ArrayList<Chunk> chunks) {
		setChunks(chunks);
	}
	
	/**
	 * Constructor Constraints without workspace previously set.
	 */
	public Constraints() {}

	/**
	 * Constructor Constraints.
	 * 
	 * @param columnName
	 *            the columnName of the value
	 * @param chunks
	 *            the list of chunks
	 */
	public Constraints(ArrayList<Chunk> chunks, String columnName) {
		setChunks(chunks);
		this.columnName = columnName;
	}

	/**
	 * Recursive method which find all chunks with code code.
	 * 
	 * @param code
	 *            the code we're looking for
	 * @param chunk
	 *            the list of chunks
	 * @param res
	 *            the result list of chunks
	 * @return the result of chunks.
	 */
	public ArrayList<Chunk> hasCode(String code, ArrayList<Chunk> chunk,
			ArrayList<Chunk> res) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getCode().equals(code)) {
				res.add(curChunk);
			}
			if (curChunk.hasChild()) {
				hasCode(code, curChunk.getChunks(), res);
			}
		}
		return res;
	}

	/**
	 * Check if a chunk has a substring of the specified comment.
	 * 
	 * @param comment
	 *            the comment
	 * @param chunk
	 *            the list of chunks
	 * @param res
	 *            the resulting chunks
	 * @return the chunks which contains the comment
	 */
	public ArrayList<Chunk> containsComment(String comment,
			ArrayList<Chunk> chunk, ArrayList<Chunk> res) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getComment().contains(comment)) {
				res.add(curChunk);
			}
			if (curChunk.hasChild()) {
				containsComment(comment, curChunk.getChunks(), res);
			}
		}
		return res;
	}

	/**
	 * Check if a chunk has a comment which equals the specified comment.
	 * 
	 * @param comment
	 *            the comment
	 * @param chunk
	 *            the chunks
	 * @param res
	 *            the result
	 * @return the resulting Chunks which have the comment
	 */
	public ArrayList<Chunk> equalsComment(String comment,
			ArrayList<Chunk> chunk, ArrayList<Chunk> res) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getComment().equals(comment)) {
				res.add(curChunk);
			}
			if (curChunk.hasChild()) {
				equalsComment(comment, curChunk.getChunks(), res);
			}
		}
		return res;
	}

	/**
	 * Remove chunks which do not pass the constraint.
	 * 
	 * @param arrList
	 *            the list of lines remaining
	 * @param chunk
	 *            the current chunk
	 */
	public void removeChunks(ArrayList<Integer> arrList, ArrayList<Chunk> chunk) {
		Chunk curChunk;
		for (int i = 0; i < chunk.size(); i++) {
			curChunk = chunk.get(i);
			if (!arrList.contains(curChunk.getLine()) && !curChunk.hasChild()) {
				chunk.remove(i);
				removeChunks(arrList, chunk);
			} else if (curChunk.hasChild()) {
				removeChunks(arrList, curChunk.getChunks());
			}
		}
	}

	/**
	 * Get the column name.
	 * 
	 * @return the name of the column
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Append string for the sql where clause.
	 * 
	 * @param tableName
	 *            the name of the table
	 * @param line
	 *            the line of the chunk
	 * @return the resulting where string
	 */
	public String appendOrClauseForQuery(String tableName, int line) {
		StringBuilder res = new StringBuilder();
		res.append("OR ");
		res.append(tableName);
		res.append("id = ");
		res.append(line);
		res.append(" ");
		return res.toString();
	}

	/**
	 * Get all lines from the chunks and format it for sql.
	 * 
	 * @param chunk
	 *            the chunks
	 * @param res
	 *            the resulting sql string for where clause
	 * @param tableName
	 *            the name of the table
	 * @return the sql string for the where clause
	 */
	public String getAllChunkLines(ArrayList<Chunk> chunk, StringBuilder res,
			String tableName) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getLine() != 0) {
				res.append(appendOrClauseForQuery(tableName, curChunk.getLine()));
			}
			if (curChunk.hasChild()) {
				res.append(getAllChunkLines(curChunk.getChunks(), res,
						tableName));
			}
		}
		return res.toString();
	}

	/**
	 * Constraint on a data value string.
	 * 
	 * @param value
	 *            the string value
	 * @param operator
	 *            the operator to specify the constraint
	 * @param tableName
	 *            the name of the table
	 * @return return the ArrayList with remaining chunks
	 * @throws SQLException
	 *             the sql exception
	 */
	public ArrayList<Chunk> constraint(String value, String operator,
			String tableName) throws SQLException {
		String rowClause = getAllChunkLines(getChunks(), new StringBuilder(),
				tableName);
		rowClause = "(" + rowClause.substring(2, rowClause.length()) + ")";
		String constraint = columnName + " " + operator + " ";
		if (isInteger(value)) {
			constraint += value + " ";
		} else {
			constraint += "'" + value + "' ";
		}
		String whereClause = constraint + " AND " + rowClause;
		removeChunks(getRemainingIDs(tableName, whereClause), getChunks());
		return getChunks();
	}

	/**
	 * Get the id's which pass the constraint from the database.
	 * 
	 * @param tableName
	 *            the name of the table
	 * @param whereClause
	 *            the where clause
	 * @return the id's which pass
	 * @throws SQLException
	 *             sql exception
	 */
	public ArrayList<Integer> getRemainingIDs(String tableName,
			String whereClause) throws SQLException {
		Db data = SingletonDb.getDb();
		ResultSet rs = data.selectAllWithWhereClause(tableName, whereClause);
		ArrayList<Integer> intArr = new ArrayList<Integer>();
		while (rs.next()) {
			intArr.add(rs.getInt(tableName + "id"));
		}
		rs.close();
		return intArr;
	}

	/**
	 * Check if a string is an integer.
	 * 
	 * @param s
	 *            the string to check
	 * @return return true if is integer and false if not
	 */
	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
