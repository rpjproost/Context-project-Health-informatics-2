package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;

/**
 * The Class Comments.
 */
public class Comments {

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
	 */
	public void setComment(int line, String comment) {
		getChunk(line, this.chunks, comment).setComment(comment);

	}

	/**
	 * Get the chunk at line line and add the comment.
	 * 
	 * @param line
	 *            the line of the chunk
	 * @param chunk
	 *            the chunk
	 * @param comment
	 *            the comment
	 * @return the chunk at line line
	 */
	public Chunk getChunk(int line, ArrayList<Chunk> chunk, String comment) {
		Chunk curChunk = null;
		for (int i = 0; i < chunk.size(); i++) {
			curChunk = chunk.get(i);
			if (curChunk.getLine() == line) {
				return curChunk;
			} else if (curChunk.hasChild()) {
				return getChunk(line, curChunk.getChunks(), comment);
			}
		}
		return curChunk;
	}
}
