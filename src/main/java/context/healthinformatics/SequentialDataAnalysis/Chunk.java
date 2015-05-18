package context.healthinformatics.SequentialDataAnalysis;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Chunk {
	public String code;
	public HashMap<Chunk, String> pointer;
	public String comment;
	public ArrayList<Chunk> chunks;
	public ResultSet res;
	
	public Chunk(String code) {
		chunks = new ArrayList<Chunk>();
		pointer = new HashMap<Chunk, String>();
		comment = null;
		this.code = code;
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public HashMap<Chunk, String> getPointer() {
		return pointer;
	}

	public void setPointer(HashMap<Chunk, String> pointer) {
		this.pointer = pointer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

	public void setChunks(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}

	public ResultSet getRes() {
		return res;
	}

	public void setRes(ResultSet res) {
		this.res = res;
	}

}
