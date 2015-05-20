package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class Constraints.
 */
public class Constraints {
	private ArrayList<Chunk> chunks;
	private String columnName;

	/**
	 * Constructor Constraints.
	 * 
	 * @param chunks
	 *            the list of chunks
	 */
	public Constraints(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}

	/**
	 * Constructor Constraints.
	 * 
	 * @param columnName
	 *            the columnName of the value
	 * @param chunks
	 *            the list of chunks
	 */
	public Constraints(ArrayList<Chunk> chunks, String columnName) {
		this.chunks = chunks;
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
	 * @return the chunks which containt the comment
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
	 * Return the chunks the constraint must apply to.
	 * 
	 * @return the chunks
	 */
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

	/**
	 * Get the column name.
	 * 
	 * @return the name of the column
	 */
	public String getColumnName() {
		// TODO
		return columnName;
	}

	/**
	 * Get all lines from the chunks and format it for sql.
	 * 
	 * @param chunk
	 *            the chunks
	 * @param res
	 *            the resulting sql string for where clause
	 * @return the sql string for the where clause
	 */
	public String getAllChunkLines(ArrayList<Chunk> chunk, String res) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			System.out.println(curChunk.getLine());
			if (curChunk.getLine() != 0) {
				res += "AND row = " + curChunk.getLine() + " ";
			}
			if (curChunk.hasChild()) {
				res += getAllChunkLines(curChunk.getChunks(), res);
			}
		}
		return res;
	}

	/**
	 * Constraint on a data value string.
	 * 
	 * @param value
	 *            the string value
	 * @param operator
	 *            the operator to specify the constraint
	 * @return return the ArrayList with remaining chuncks
	 */
	public ArrayList<Chunk> constraint(String value, String operator) {
		String rowClause = getAllChunkLines(chunks, "");
		String constraint = columnName + " " + operator + " ";
		if (isInteger(value)) {
			constraint += value + " ";
		} else {
			constraint += "'" + value + "' ";
		}
		System.out.println(constraint + rowClause);
		// return remaining chunks

		// SELECT rowNums FROM stat WHERE columnName (statement) AND row = i AND
		return null;
	}

	/**
	 * Check if a string is an interger.
	 * 
	 * @param s
	 *            the string to check
	 * @return return true if is interger and false if not
	 */
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		return true;
	}
}
