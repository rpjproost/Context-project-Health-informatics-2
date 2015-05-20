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
	 * @param comment the comment 
	 * @param chunk the list of chunks
	 * @param res the resulting chunks 
	 * @return the chunks which containt the comment
	 */
	public ArrayList<Chunk> containsComment(String comment, ArrayList<Chunk> chunk,
			ArrayList<Chunk> res) {
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
	 * @param comment the comment
	 * @param chunk the chunks
	 * @param res the result
	 * @return the resulting Chunks which have the comment
	 */
	public ArrayList<Chunk> equalsComment(String comment, ArrayList<Chunk> chunk,
			ArrayList<Chunk> res) {
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

	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

	public String getColumnName() {
		return columnName;
	}

	/**
	 * Put constraint on comment.
	 * 
	 * @param comment
	 *            the comment to constraint
	 * @return return the ArrayList with remaining chuncks
	 */
	public ArrayList<Chunk> constraintComment(String comment) {
		// iterate through chunks

		// return remaining chunks
		// TODO
		return null;
	}

	/**
	 * Constraint on a data value string.
	 * 
	 * @param value
	 *            the string value
	 * @return return the ArrayList with remaining chuncks
	 */
	public ArrayList<Chunk> constraint(String value) {
		// iterate through chunks

		// return remaining chunks
		// TODO
		return null;
	}

	/**
	 * Constraint on a data value int.
	 * 
	 * @param value
	 *            the int value
	 * @return return the ArrayList with remaining chuncks
	 */
	public ArrayList<Chunk> constraint(int value) {
		// iterate through chunks

		// return remaining chunks
		// TODO
		return null;
	}

	/**
	 * Constraint on a data value Date.
	 * 
	 * @param value
	 *            the Date value
	 * @return return the ArrayList with remaining chuncks
	 */
	public ArrayList<Chunk> constraint(Date value) {
		// iterate through chunks

		// return remaining chunks
		// TODO
		return null;
	}

}
