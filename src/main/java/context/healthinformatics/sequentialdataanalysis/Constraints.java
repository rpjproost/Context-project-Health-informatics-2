package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * Class Constraints.
 */
public class Constraints extends Task {
	private String columnName;

	private ArrayList<Chunk> oldList;
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
	public Constraints() { }

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
	 * Checks current arraylist on constraint on data.
	 * @param whereClause sql clause over data.
	 * @return filtered arraylist.
	 * @throws SQLException iff sql could not be executed.
	 */
	@Override
	public ArrayList<Chunk> constraintOnData(String whereClause) 
			throws SQLException {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		ArrayList<Integer> ints = getLinesFromData(whereClause);
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			checkConstraintOnData(curChunk, ints, res);
		}
		return res;
	}

	/**
	 * Checks if chunk passes constraint.
	 * @param curChunk Chunk to be checked.
	 * @param ints list of ints of chunks that should pass.
	 * @param res arrayList to return.
	 */
	public void checkConstraintOnData(Chunk curChunk, ArrayList<Integer> ints, 
			ArrayList<Chunk> res) {
		if (ints.contains(curChunk.getLine())) {
			res.add(curChunk);
		}
		else {
			if (curChunk.hasChild()) {
				checkChildsOnData(curChunk, curChunk.getChunks(), ints, res);
			}	
		}
	}

	/**
	 * Remove childs from resul arraylist if they do not pass constraint.
	 * @param curChunk Chunk that has to be added to res if it passes constraint.
	 * @param childs childs of currentChunk that are being checked.
	 * @param ints list of ints of Chunks that should pass constraint.
	 * @param res result arrayList to be returned.
	 */
	public void checkChildsOnData(Chunk curChunk, ArrayList<Chunk> childs,
			ArrayList<Integer> ints, ArrayList<Chunk> res) {
		for (int i = 0; i < childs.size(); i++) {
			if (ints.contains(childs.get(i).getLine())) {
				if (!res.contains(curChunk)) {
					res.add(curChunk);
				}
			}
			else {
				if (childs.get(i).hasChild()) {
					checkChildsOnData(curChunk, childs.get(i).getChunks(), ints, res);
				}
				else {
					childs.remove(i);
				}
			}
		}
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
	protected void setChunks(ArrayList<Chunk> c) {
		super.setChunks(c);
		oldList = c;
	}

	@Override
	public ArrayList<Chunk> undo() {
		return oldList;
		
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		return hasCode(code, getChunks(), new ArrayList<Chunk>());
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		return equalsComment(comment, getChunks(), new ArrayList<Chunk>());
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		return containsComment(comment, getChunks(), new ArrayList<Chunk>());
	}
}
