package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * The Class Comments.
 */
public class Comments {

	private Logger log = Logger.getLogger(Comments.class.getName());
	private ArrayList<Chunk> chunks;

	/**
	 * Constructor for comments.
	 * 
	 * @param chunks
	 *            the chunks
	 */
	public Comments(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
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
	public void setComment(int line, String comment) {
		Chunk c = getChunk(line, this.chunks);
		if (c != null) {
			c.setComment(comment);
		} else {
			log.info("The line: " + line + "could not be found."
					+ "The comment '" + comment + "' was not set.");
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
	public Chunk getChunk(int line, ArrayList<Chunk> chunk) {
		Chunk curChunk = null;
		for (int i = 0; i < chunk.size(); i++) {
			curChunk = chunk.get(i);
			if (curChunk.getLine() == line) {
				return curChunk;
			} else if (curChunk.hasChild()) {
				return getChunk(line, curChunk.getChunks());
			}
		}
		return curChunk;
	}
}
