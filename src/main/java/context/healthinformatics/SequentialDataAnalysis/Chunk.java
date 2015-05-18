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
	
	public Chunk() {
		chunks = new ArrayList<Chunk>();
		pointer = new HashMap<Chunk, String>();
		comment = null;
		code = null;
		
	}

}
