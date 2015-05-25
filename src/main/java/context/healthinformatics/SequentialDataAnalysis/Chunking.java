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
			addChunkOnEqualsCode(curChunk, temp, code, res);
		}
		if (temp.hasChild()) {
			res.add(temp);
		}
		return res;
	}

	/**
	 * Chunking chunks on a comment contains constraint.
	 * @param comment String for comment.
	 * @return new ArrayList<Chunk> chunked on constraint.
	 */
	public ArrayList<Chunk> constraintOnContainsComment(String comment) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		Chunk temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnContainsComment(curChunk, temp, comment, res);
		}
		if (temp.hasChild()) {
			res.add(temp);
		}
		return res;
	}
	
	/**
	 * Chunking chunks on a comment equals constraint.
	 * @param comment String for comment.
	 * @return new ArrayList<Chunk> chunked on constraint.
	 */
	public ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		Chunk temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnEqualsComment(curChunk, temp, comment, res);
		}
		if (temp.hasChild()) {
			res.add(temp);
		}
		return res;
	}
	
	/**
	 * Add chunk to new Chunk in new ArrayList if it contains comment.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for comment constraint.
	 * @param res ArrayList<Chunk> chunked.
	 */
	public void addChunkOnContainsComment(Chunk curChunk, Chunk temp, String c, 
			ArrayList<Chunk> res) {
		if (curChunk.getComment().contains(c)) {
			temp.setChunk(curChunk);
		}
		else {
			if (temp.hasChild()) {
				res.add(temp);
			}
			temp  = new Chunk();
		}
	}
	
	/**
	 * Add chunk to new Chunk in new ArrayList if it equals comment.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for comment constraint.
	 * @param res ArrayList<Chunk> chunked.
	 */
	public void addChunkOnEqualsComment(Chunk curChunk, Chunk temp, String c, 
			ArrayList<Chunk> res) {
		if (curChunk.getComment().equals(c)) {
			temp.setChunk(curChunk);
		}
		else {
			if (temp.hasChild()) {
				res.add(temp);
			}
			temp  = new Chunk();
		}
	}
	
	/**
	 * Add chunk to new Chunk in new ArrayList if it equals code.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for code constraint.
	 * @param res ArrayList<Chunk> chunked.
	 */
	public void addChunkOnEqualsCode(Chunk curChunk, Chunk temp, String c, ArrayList<Chunk> res) {
		if (curChunk.getCode().equals(c)) {
			temp.setChunk(curChunk);
		}
		else {
			if (temp.hasChild()) {
				res.add(temp);
			}
			temp  = new Chunk();
		}
	}
	
	/**
	 * Return the chunks from the constructor.
	 * 
	 * @return the chunks
	 */
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

}
