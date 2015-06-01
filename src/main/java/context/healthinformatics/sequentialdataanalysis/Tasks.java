package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * 
 * Abstract class that every C class uses.
 *
 */
public abstract class Tasks {
	
	/**
	 * List of chunks that every Class. 
	 */
	private ArrayList<Chunk> chunks;
	
	/**
	 * 
	 * @return previous state of ArrayList<Chunk>.
	 */
	public abstract ArrayList<Chunk> undo();
	
	/**
	 * Runs the C method from parser.
	 */
	public abstract void run();
	
	/**
	 * Return the chunks from the constructor.
	 * 
	 * @return the chunks
	 */
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}
	
	/**
	 * 
	 * @param c ArrayList of chunks.
	 */
	protected void setChunks(ArrayList<Chunk> c) {
		chunks = c;
	}
	
	/**
	 * Get line numbers of data which correspond to sql query.
	 * @param whereClause clause for sql query.
	 * @return lineNumbers of data in arrayList.
	 * @throws SQLException if data is not found
	 */
	public ArrayList<Integer> getLinesFromData(String whereClause) throws SQLException {
		ArrayList<Integer> res = new ArrayList<Integer>();
		Db data = SingletonDb.getDb();
		try {
			ResultSet rs = data.selectResultSet("result", "resultid", whereClause);
			while (rs.next()) {
				res.add(rs.getInt("resultid"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

}
