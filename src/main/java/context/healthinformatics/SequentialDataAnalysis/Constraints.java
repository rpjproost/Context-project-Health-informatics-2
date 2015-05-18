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

	private ArrayList<Chunk> res = new ArrayList<Chunk>();

	/**
	 * Put constraint on code.
	 * 
	 * @param code
	 *            the code to constraint
	 * @return return the ArrayList with remaining chuncks
	 */
	public ArrayList<Chunk> containsCode(String code, ArrayList<Chunk> chunk) {
		System.out.println(chunks.toString());
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			System.out.println(curChunk.toString());
			if (curChunk.getCode().equals(code)) {
				res.add(curChunk);
			} else {
				if (curChunk != null) {
					res.addAll(containsCode(code, curChunk.getChunks()));
				}
			}

		}
		// TODO
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
