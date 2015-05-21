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

	public void setComment(int line, String comment) {
		getChunk(line, this.chunks, comment);

	}

	public Chunk getChunk(int line, ArrayList<Chunk> chunk, String comment) {
		for (int i = 0; i < chunk.size(); i++) {
			Chunk curChunk = chunk.get(i);
			if (curChunk.getLine() == line) {
				curChunk.setComment(comment);
				return curChunk;
			} else if (curChunk.hasChild()) {
				getChunk(line, curChunk.getChunks(), comment);
			}
		}
		return null;
	}
}
