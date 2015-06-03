package context.healthinformatics.sequentialdataanalysis;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The Class Comments.
 */
public class Comments extends Tasks {

	private Logger log = Logger.getLogger(Comments.class.getName());
	private ArrayList<Chunk> chunks;

	/**
	 * Constructor for comments without arguments.
	 */
	public Comments() {	}
	
	/**
	 * Constructor for comments.
	 * 
	 * @param chunks
	 *            the chunks
	 */
	public Comments(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
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
		Chunk c = getChunkByLine(line, this.chunks);
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
	 * @param comment the chunk has to have.
	 * @param code to be set, if the condition is met.
	 */
	public void setCommentOnCode(String comment, String code) {
		for (Chunk c : chunks) {
			if (c.getCode().equals(code)) {
				c.setComment(comment);
			}
		}
	}
	
	/**
	 * Method which replaces the comment for all chunks which have a certain comment.
	 * @param previousComment the chunk has to have.
	 * @param newComment to be set, if the condition is met.
	 */
	public void setCommentOnComment(String newComment, String previousComment) {
		for (Chunk c : chunks) {
			if (c.getCode().equals(previousComment)) {
				c.setComment(newComment);
			}
		}
	}
	
	/**
	 * Method that sets the comment for all chunks which
	 * Fulfill the conditions of the whereClause.
	 * @param comment to be set.
	 * @param whereClause is condition to be met.
	 * @throws Exception e.
	 */
	public void setCommentOnData(String comment, String whereClause) throws Exception {
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
//			else if (curChunk.hasChild()) {
//				return getChunkByLine(line, curChunk.getChunks());///////////// is recursief nodig?
//			}
		}
		return curChunk;
	}
}
