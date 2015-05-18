package context.healthinformatics.SequentialDataAnalysis;

import java.sql.ResultSet;
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
	private ResultSet res;

	/**
	 * Class chunk.
	 */
	public Chunk() {
		chunks = new ArrayList<Chunk>();
		pointer = new HashMap<Chunk, String>();
		comment = null;
		code = null;

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
	 * @param code set code of this chunk.
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

	/** set pointer.
	 * 
	 * @param pointer pointer where this chunk has connection to.
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

	/** Set comment.
	 * 
	 * @param comment comment to be set for this chunk.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/** List of chunks this chunk consists out of.
	 * 
	 * @return childs of this chunk.
	 */
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

	/** Set childs of this chunk.
	 * 
	 * @param c chunk to be added to arraylist.
	 */
	public void setChunks(Chunk c) {
		chunks.add(c);
	}

	/** 
	 * 
	 * @return content of this chunk.
	 */
	public ResultSet getRes() {
		return res;
	}

	/** Set content of this chunk.
	 * 
	 * @param res resultset to be added to this chunk.
	 */
	public void setRes(ResultSet res) {
		this.res = res;
	}

}
