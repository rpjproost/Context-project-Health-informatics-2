package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
	private int sum;
	private ResultSet rs;

	/**
	 * 
	 */
	public Chunk() {
		chunks = new ArrayList<Chunk>();
		pointer = new HashMap<Chunk, String>();
		comment = "";
		code = "";
		sum = Integer.MIN_VALUE;
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
	 * Add a chunk to the pointer map.
	 * 
	 * @param chunk
	 *            the chunk which it points too
	 * @param note
	 *            the note of the connection
	 */
	public void addPointer(Chunk chunk, String note) {
		pointer.put(chunk, note);
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
	 * List of children this chunk contains.
	 * 
	 * @return children of this chunk.
	 */
	public ArrayList<Chunk> getChildren() {
		return chunks;
	}

	/**
	 * Set completely new arraylist.
	 * 
	 * @param chunks
	 *            arraylist of chunks.
	 */
	public void setChunks(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}

	/**
	 * setter for sum.
	 * 
	 * @param i
	 *            sum to set.
	 */
	public void setSum(int i) {
		sum = i;
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

	/**
	 * Set id for chunk.
	 * 
	 * @param i
	 *            id for chunk.
	 */
	public void setLine(int i) {
		line = i;
	}

	/**
	 * get Id of this chunk.
	 * 
	 * @return id.
	 */
	public int getLine() {
		return line;
	}

	@Override
	public String toString() {
		String res = "Chunk [code=" + code + ", pointer=" + pointer
				+ ", comment=" + comment + ", chunks=" + chunks + ", line="
				+ line + "]";
		return res;
	}

	/**
	 * Returns array of chunk object.
	 * 
	 * @return Arraylist of strings.
	 */
	public ArrayList<String> toArray() {
		ArrayList<String> res = new ArrayList<String>();
		if (sum != Integer.MIN_VALUE) {
			res.add(sum + "");
			return res;
		} else if (hasChild()) {
			res.add("Chunk contains childs, code = " + code + " comment = "
					+ comment);
			return res;

		} else {
			Db data = SingletonDb.getDb();
			try {
				this.rs = data.selectResultSet("result", "*", "resultid = "
						+ line);
				ResultSetMetaData rsmd = this.rs.getMetaData();
				processResultSet(rsmd.getColumnCount(), res);
				rs.close();
			} catch (SQLException e) {
				System.out.println(e);
				return res;
			}
			return res;
		}
	}

	/**
	 * returns a deep copy of the current chunk.
	 * 
	 * @return copy of the chunk.
	 */
	public Chunk copy() {
		Chunk c = new Chunk();
		c.setCode(this.getCode());
		c.setChunks(SingletonInterpreter.getInterpreter().copyChunks(
				getChildren()));
		c.setComment(getComment());
		c.setLine(getLine());
		HashMap<Chunk, String> map = new HashMap<Chunk, String>();
		for (Entry<Chunk, String> e : pointer.entrySet()) {
			map.put(e.getKey(), e.getValue());
		}
		c.setPointer(map);
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
	private void processResultSet(int numColumns, ArrayList<String> res)
			throws SQLException {
		while (rs.next()) {
			for (int i = 1; i < numColumns; i++) {
				if (rs.getObject(i + 1) != null) {
					String temp = rs.getObject(i + 1).toString();
					int compare = Integer.MIN_VALUE;
					String comp = Integer.toString(compare);
					if (temp.equals(comp)) {
						res.add("");
					} else {
						res.add(temp);
					}
				} else {
					res.add("");
				}
			}
		}
		rs.close();
	}

}
