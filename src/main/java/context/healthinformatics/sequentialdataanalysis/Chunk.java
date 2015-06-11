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
	private boolean compute = false;
	private double[] computations;

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
		if (isCompute()) {
			res.add("sum of values = " + computations[0]);
			res.add("Childs sum = " + sum);
		} else if (sum != Integer.MIN_VALUE) {
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
		}
		return res;
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
	 * Prepare the chunk for the writer.
	 * 
	 * @return return string list of the chunk data
	 */
	public ArrayList<String> toWriter() {
		ArrayList<String> valuesOfChunk = new ArrayList<String>();
		if (!hasChild()) {
			Db data = SingletonDb.getDb();
			try {
				this.rs = data.selectResultSet("result", "*", "resultid = "
						+ line);
				ResultSetMetaData rsmd = this.rs.getMetaData();
				processResultSet(rsmd.getColumnCount(), valuesOfChunk);
				rs.close();
			} catch (SQLException e) {
				System.out.println(e);
				return valuesOfChunk;
			}
			return valuesOfChunk;
		} else {
			return valuesOfChunk;
		}
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

	/**
	 * Returns boolean if chunk is computed.
	 * @return true iff data is computed.
	 */
	public boolean isCompute() {
		return compute;
	}

	/**
	 * Sets boolean compute if data is being computed.
	 * @param compute true iff data is being computed, false if normal data should be shown.
	 */
	public void setCompute(boolean compute) {
		this.compute = compute;
	}
	
	public void initializeComputations(String column) throws SQLException {
		if (hasChild()) {
			computations = new double[4];
			Db data = SingletonDb.getDb();
			computeSum(column, data);
			setCompute(true);
		}
	}
	
	public void computeSum(String column, Db data) throws SQLException {
		double sum = 0;
		StringBuilder query = new StringBuilder();
		String prefix = "";
		for (int i = 0; i < getChildren().size(); i++) {
			query.append(prefix); query.append(data.getMergeTable()); query.append("id = "); 
			int line = getChildren().get(i).getLine();
			query.append(line);
			prefix = " OR "; 
		}
		ResultSet rs = data.selectResultSet(data.getMergeTable(), column, query.toString());
		while (rs.next()) {
			double value = rs.getDouble(column);
			if (value != Integer.MIN_VALUE) {
				sum += value;
			}
		}
		rs.close();
		computations[0] = sum;
		System.out.println(computations[0]);
	}

}
