package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import org.joda.time.DateTime;
import org.joda.time.Days;

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
	private int amountOfChilds;
	private ResultSet rs;
	private boolean compute;
	private double[] computations;
	private double difference = Integer.MIN_VALUE;

	/**
	 * 
	 */
	public Chunk() {
		chunks = new ArrayList<Chunk>();
		pointer = new HashMap<Chunk, String>();
		comment = "";
		code = "";
		amountOfChilds = Integer.MIN_VALUE;
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
	 * Returns if this chunk has connections to others.
	 * @return true iff chunk has connection.
	 */
	public boolean hasConnection() {
		return pointer.size() > 0;
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
	public void setAmountOfChilds(int i) {
		amountOfChilds = i;
	}
	
	/**
	 * Returns amount of children.
	 * @return integer of amount of children.
	 */
	public int getAmountOfChilds() {
		return amountOfChilds;
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
		if (difference != Integer.MIN_VALUE) {
			res.add("difference to connection " + difference);
		}
		else if (isCompute()) {
			toArrayComputed(res);
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

	private void toArrayComputed(ArrayList<String> res) {
		final int comp = 3;
		res.add("sum of values = " + computations[0]);
		if (computations[1] != Integer.MIN_VALUE) {
			res.add("max of values = " + computations[1]);
		}
		if (computations[2] != Integer.MAX_VALUE) {
			res.add("min of values = " + computations[2]);
		}
		res.add("average of values = " + computations[comp]);
		res.add("Childs sum = " + amountOfChilds);
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
	 * undo for difference.
	 */
	public void undoDifference() {
		difference = Integer.MIN_VALUE;
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

	/**
	 * Initialize computing for a parent chunk.
	 * @param column column to be computed.
	 * @throws SQLException if column is not in table.
	 */
	public void initializeComputations(String column) throws SQLException {
		final int comp = 4;
		if (hasChild()) {
			computations = new double[comp];
			setCompute(true);
			this.amountOfChilds = getChildren().size();
			computeChunkValues(column);
		}
	}

	/**
	 * Compute the values sum/max/min/average for column.
	 * @param column the column to be computed.
	 * @throws SQLException if column is not in table.
	 */
	public void computeChunkValues(String column) throws SQLException {
		double sum = 0;
		double min = Integer.MAX_VALUE;
		double max = Integer.MIN_VALUE;
		int counter = 0;
		for (int i = 0; i < getChildren().size(); i++) {
			double value = getChildren().get(i).getValue(column);
			if (value != Integer.MIN_VALUE) {
				if (value > max) {
					max = value;
				}
				if (value < min) {
					min = value;
				}
				counter++;
				sum += value;
			}
		}
		final int comp = 3;
		computations[0] = sum; computations[1] = max; computations[2] = min; 
		computations[comp] = sum / counter;
	}

	/**
	 * Computes difference on column for this chunk and its connection.
	 * @param column value to be compared.
	 * @throws SQLException if column is not an integer or does not exist.
	 */
	public void initializeDifference(String column) throws SQLException {
		if (hasConnection()) {
			if (column.toLowerCase().equals("time")) {
				timeDifference(column);
			}
			else {
				integerDifference(column);
			}
		}
	}

	private void timeDifference(String column) throws SQLException {
		Db data = SingletonDb.getDb();
		int days = 0; double first = 0.0; double second = 0.0;
		final int twentyfour = 24; final int sixty = 60;
		first = getValue(column);
		Date firstDate = data.selectDate(getLine());
		Date secondDate = null;
		for (Chunk c : pointer.keySet()) {
			second = c.getValue(column);
			secondDate = data.selectDate(c.getLine());
		}
		if (first != Integer.MIN_VALUE && second != Integer.MIN_VALUE) {
			days = daysBetween(firstDate, secondDate);
			//TODO: time should be sorted in database.
			if (first > second) {
				double[] res = convertTime(first, second);
				difference = res[0] - res[1] + days * twentyfour * sixty;
			}
			else {
				double[] res = convertTime(first, second);
				difference = res[1] - res[0] + days * twentyfour * sixty;
			}
		}
	}
	
	private int daysBetween(Date first, Date second) {
		return Days.daysBetween(new DateTime(first), new DateTime(second)).getDays();
	}

	private double[] convertTime(double first, double second) {
		final double hundred = 100;
		final double sixty = 60;
		first = Math.floor(first / hundred) * sixty + first % hundred;
		second = Math.floor(second / hundred) * sixty + second % hundred;
		double[] res = new double[2];
		res[0] = first; res[1] = second;
		return res;
	}

	private void integerDifference(String column) throws SQLException {
		double first = 0.0;
		double second = 0.0;
		first = getValue(column);
		for (Chunk c : pointer.keySet()) {
			second = c.getValue(column);
		}
		if (first > second && second != Integer.MIN_VALUE) {
			difference = first - second;
		}
		else {
			if (first != Integer.MIN_VALUE) {
				difference = second - first;
			}
		}
		if (difference == 0.0) {
			setComment("No difference with connection");
		}
	}

	/**
	 * Gets column value of this chunk.
	 * @param column column to select.
	 * @return value of the column.
	 * @throws SQLException if column not found or is not a double.
	 */
	public double getValue(String column) throws SQLException {
		Db data = SingletonDb.getDb(); double value = 0.0;
		ResultSet rs = data.selectResultSet(data.getMergeTable(), column, data.getMergeTable() 
				+ "id = " + getLine());
		while (rs.next()) {
			value = rs.getDouble(column);
		}
		rs.close();
		return value;
	}
	
	/**
	 * gets the average value computed of this chunk.
	 * @return double of average value computed.
	 */
	public double getAverageValue() {
		final int comp = 3;
		return computations[comp];
	}
}
