package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * Class chunk.
 *
 */
public class Chunk {
	private String code;
	private HashMap<Chunk, String> pointer;
	private String comment;
	private ArrayList<Chunk> chunks;
	private int line;

	/**
	 * 
	 */
	public Chunk() {
		chunks = new ArrayList<Chunk>();
		pointer = new HashMap<Chunk, String>();
		comment = "";
		code = "";
	}

	/**
	 * 
	 * @return code for this chunk.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @param code
	 *            set code of this chunk.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 
	 * @return pointer connection to other chunk.
	 */
	public HashMap<Chunk, String> getPointer() {
		return pointer;
	}

	/**
	 * set pointer.
	 * 
	 * @param pointer
	 *            pointer where this chunk has connection to.
	 */
	public void setPointer(HashMap<Chunk, String> pointer) {
		this.pointer = pointer;
	}

	/**
	 * 
	 * @return comment for this chunk.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Set comment.
	 * 
	 * @param comment
	 *            comment to be set for this chunk.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * List of chunks this chunk consists out of.
	 * 
	 * @return childs of this chunk.
	 */
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}
	
	/**
	 * Set completely new arraylist.
	 * @param chunks arraylist of chunks.
	 */
	public void setChunks(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}

	/**
	 * Set childs of this chunk.
	 * 
	 * @param c
	 *            chunk to be added to arraylist.
	 */
	public void setChunk(Chunk c) {
		chunks.add(c);
	}

	/**
	 * 
	 * @return true iff this chunk contains other chunks.
	 */
	public boolean hasChild() {
		if (chunks.size() == 0) {
			return false;
		}
		return true;
	}
	
	/** Set id for chunk.
	 * 
	 * @param i id for chunk.
	 */
	public void setLine(int i) {
		line = i;
	}
	
	/**
	 * get Id of this chunk.
	 * @return id.
	 */
	public int getLine() {
		return line;
	}

	@Override
	public String toString() {
		return "Chunk [code=" + code + ", pointer=" + pointer + ", comment="
				+ comment + ", chunks=" + chunks + ", line=" + line + "]";
	}

}
