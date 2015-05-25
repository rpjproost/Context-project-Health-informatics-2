package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;

/**
 * Class for chunking a list of chunks.
 *
 */
public class Chunking {

	private ArrayList<Chunk> chunks;

	/**
	 * Constructor Chunking.
	 * 
	 * @param chunks
	 *            the list of chunks
	 */
	public Chunking(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}

	/**
	 * Chunking chunks on a code constraint.
	 * @param code Code constraint for a chunk.
	 * @return New ArrayList<Chunk> chunked on code.
	 */
	public ArrayList<Chunk> constraintOnCode(String code) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		Chunk temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			if (curChunk.getCode().equals(code)) {
				temp.setChunk(curChunk);
			}
			else {
				if (temp.hasChild()) {
					res.add(temp);
				}
				temp  = new Chunk();
			}	
		}
		return res;
	}

	/**
	 * Chunking chunks on a comment constraint.
	 * @param comment String for comment.
	 * @return new ArrayList<Chunk> chunked on constraint.
	 */
	public ArrayList<Chunk> constraintOnComment(String comment) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		Chunk temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			if (curChunk.getComment().contains(comment)) {
				temp.setChunk(curChunk);
			}
			else {
				if (temp.hasChild()) {
					res.add(temp);
				}
				temp  = new Chunk();
			}
		}
		return res;
	}

}
