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
	 * Put constraint on code.
	 * 
	 * @param code
	 *            the code to constraint
	 * @return return the ArrayList with remaining chuncks
	 */
	public ArrayList<Chunk> constraintCode(String code) {
		// iterate through chunks

		// return remaining chunks
		// TODO
		return null;
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
