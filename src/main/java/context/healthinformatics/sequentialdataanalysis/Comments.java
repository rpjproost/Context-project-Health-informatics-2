package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The Class Comments.
 */
public class Comments extends Task {

	private Logger log = Logger.getLogger(Comments.class.getName());
	private String comment;

	/**
	 * Constructor for comments without arguments.
	 * @param c Comment to be set.
	 */
	public Comments(String c) {
		comment = c;
	}
	
	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Set the comment on line line with the string comment.
	 * 
	 * @param line
	 *            the line of the comment
	 * @param comment
	 *            the comment to be set
	 * @throws Exception 
	 */
	public void setCommentByLine(int line, String comment) {
		Chunk c = getChunkByLine(line, getChunks());
		if (c != null) {
			c.setComment(comment);
		} else {
			log.info("The line: " + line + "could not be found."
					+ "The comment '" + comment + "' was not set.");
		}
	}
	
	/**
	 * Method which sets the comment of all chunks in a list.
	 * @param chunks list of chunks.
	 * @param comment to be set.
	 */
	public void setCommentOfListOfChunks(ArrayList<Chunk> chunks, String comment) {
		for (Chunk chunk : chunks) {
			chunk.setComment(comment);
		}
	}
	
	/**
	 * Method which sets the comment for all chunks which have a certain comment.
	 * @param code to be set, if the condition is met.
	 */
	public void setCommentOnCode(String code) {
		for (Chunk c : getChunks()) {
			if (c.getCode().equals(code)) {
				c.setComment(comment);
			}
		}
	}
	
	/**
	 * Method which replaces the comment for all chunks which have a certain comment.
	 * @param previousComment the chunk has to have.
	 */
	public void setCommentOnComment(String previousComment) {
		for (Chunk c : getChunks()) {
			if (c.getComment().equals(previousComment)) {
				c.setComment(comment);
			}
		}
	}
	
	/**
	 * Method that sets the comment for all chunks which
	 * Fulfill the conditions of the whereClause.
	 * @param whereClause is condition to be met.
	 * @throws SQLException e.
	 */
	public void setCommentOnData(String whereClause) throws SQLException {
		ArrayList<Integer> list = getLinesFromData(whereClause);
		for (Integer i : list) {
			setCommentByLine(i, comment);
		}
	}

	/**
	 * Get the chunk at line line and add the comment.
	 * 
	 * @param line
	 *            the line of the chunk
	 * @param chunk
	 *            the chunk
	 * @return the chunk at line line
	 * @throws Exception 
	 */
	public Chunk getChunkByLine(int line, ArrayList<Chunk> chunk) {
		Chunk curChunk = null;
		for (int i = 0; i < chunk.size(); i++) {
			curChunk = chunk.get(i);
			if (curChunk.getLine() == line) {
				return curChunk;
			}
			else if (curChunk.hasChild()) {
				return getChunkByLine(line, curChunk.getChunks());
			}
		}
		return curChunk;
	}

	@Override
	protected ArrayList<Chunk> constraintOnData(String whereClause)
			throws SQLException {
		setCommentOnData(whereClause);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		setCommentOnCode(code);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		setCommentOnComment(comment);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		return null;
	}
}
