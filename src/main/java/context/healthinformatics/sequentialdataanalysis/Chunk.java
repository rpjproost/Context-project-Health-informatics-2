package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

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
	private int sum = Integer.MIN_VALUE;
	private ResultSet rs;

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
		String res = "Chunk [code=" + code + ", pointer=" + pointer + ", comment="
				+ comment + ", chunks=" + chunks + ", line=" + line + "]";
		if (sum != Integer.MIN_VALUE) {
			return sum + "";
		}
		else if (hasChild()) {
			return res;
		}
		else {
			Db data = SingletonDb.getDb();
			try {
				this.rs = data.selectResultSet("result", "*", "resultid = " + line);
				ResultSetMetaData rsmd = this.rs.getMetaData();
				res = processResultSet(rsmd.getColumnCount()).toString();
				rs.close();
			} catch (SQLException e) {
				return res;
			}
			return res;
		}
	}

	/**
	 * returns a deep copy of the current chunk.
	 * @return copy of the chunk.
	 */
	public Chunk copy() {
		Chunk c = new Chunk();
		c.setCode(this.getCode());
		c.setChunks(SingletonInterpreter.getInterpreter().copyChunks(getChunks()));
		c.setComment(getComment());
		c.setLine(c.getLine());
		Object o = this.getPointer().clone();
		if (o instanceof HashMap<?, ?>) {
			HashMap<Chunk, String> map = (HashMap<Chunk, String>) o;
			c.setPointer(map);
		}
		return c;
	}

	/**
	 * Process the result set.
	 * 
	 * @param numColumns
	 *            the number of columns in the resultset
	 * @return a stringbuffer with the right formatted resultset
	 * @throws SQLException
	 *             the sql exception of the resultset
	 */
	public StringBuffer processResultSet(int numColumns) throws SQLException {
		StringBuffer str = new StringBuffer();

		for (int i = 1; i < numColumns; i++) {
			if (i == numColumns - 1) {
				str.append(rs.getObject(i + 1));
			} else {
				str.append(rs.getObject(i + 1) + " ");
			}
		}
		str.append(System.lineSeparator());

		if (str.length() > 2) {
			str.setLength(str.length() - 2);
		}
		rs.close();
		return str;
	}

}
