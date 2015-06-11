package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.analyse.Query;

/**
 * 
 * Abstract class that every C class uses.
 *
 */
public abstract class Task {

	private String mergeTable = SingletonDb.getDb().getMergeTable();

	/**
	 * List of chunks that every Class. 
	 */
	private ArrayList<Chunk> chunks;

	private ArrayList<Chunk> result;

	/**
	 * Undoes the changes made by this Task.
	 * @return previous state of ArrayList of chunks.
	 */
	public abstract ArrayList<Chunk> undo();

	/**
	 * Runs the C method from parser.
	 * @param query An array of query words.
	 * @throws Exception query input can be wrong.
	 */
	public void run(Query query) throws Exception {
		ArrayList<Chunk> c = SingletonInterpreter.getInterpreter().getChunks();
		query.inc();
		setChunks(c);
		if (isData(query.part())) {
			runData(query);
		}
		else if (isCode(query.part())) {
			runCode(query);
		}
		else if (isComment(query.part())) {
			runComment(query);
		}
		else if (isLine(query.part())) {
			runLine(query);
		}
		else {
			throw new Exception("query input is wrong at: " + query.part()
					+ "Expected: (data/comment/code");
		}
	}

	/**
	 * Return the chunks from the constructor.
	 * 
	 * @return the chunks.
	 */
	public ArrayList<Chunk> getChunks() {
		return chunks;
	}

	/**
	 * Setter for the ArrayList of chunks.
	 * @param c ArrayList of chunks.
	 */
	protected void setChunks(ArrayList<Chunk> c) {
		chunks = c;
	}
	
	/**
	 * Runs constraintOnData in C classes.
	 * @param whereClause Constrains String.
	 * @return Result list of chunks.
	 * @throws SQLException if sql query is incorrect.
	 */
	protected abstract ArrayList<Chunk> constraintOnData(String whereClause) throws SQLException;
	
	/**
	 * Runs constraintOnCode in C classes.
	 * @param code Constraint String.
	 * @return Result list of chunks.
	 */
	protected abstract ArrayList<Chunk> constraintOnCode(String code);
	
	/**
	 * Returns constraintOnEquals comment in C classes.
	 * @param comment Constraint String.
	 * @return Result list of chunks.
	 */
	protected abstract ArrayList<Chunk> constraintOnEqualsComment(String comment);
	
	/**
	 * Returns constraintOnContains comment.
	 * @param comment Constraint String.
	 * @return Result list of chunks.
	 */
	protected abstract ArrayList<Chunk> constraintOnContainsComment(String comment);
	
	/**
	 * Returns constraintOnLine line.
	 * @param line Constraint String.
	 * @return Result list of chunks.
	 */
	protected abstract ArrayList<Chunk> constraintOnLine(String line);
	
	/**
	 * Get the chunk at line line and add the comment.
	 * 
	 * @param line
	 *            the line of the chunk.
	 * @param chunk
	 *            the chunk.
	 * @return the chunk at line line.
	 */
	public Chunk getChunkByLine(int line, ArrayList<Chunk> chunk) {
		Chunk curChunk = null;
		for (int i = 0; i < chunk.size(); i++) {
			curChunk = chunk.get(i);
			if (curChunk.getLine() == line) {
				return curChunk;
			}
			else if (curChunk.hasChild()) {
				return getChunkByLine(line, curChunk.getChildren());
			}
		}
		return curChunk;
	}

	/**
	 * Get line numbers of data which correspond to sql query.
	 * @param whereClause clause for sql query.
	 * @return lineNumbers of data in arrayList.
	 * @throws SQLException if data is not found.
	 */
	protected ArrayList<Integer> getLinesFromData(String whereClause) throws SQLException {
		ArrayList<Integer> res = new ArrayList<Integer>();
		Db data = SingletonDb.getDb();
		try {
			ResultSet rs = data.selectResultSet(mergeTable, mergeTable + "id", whereClause);
			while (rs.next()) {
				res.add(rs.getInt(mergeTable + "id"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		}
		return res;
	}

	/**
	 * Returns if query constraints on data.
	 * @param query The query.
	 * @return true/false.
	 */
	protected boolean isData(String query) {
		return query.equals("data");
	}

	/**
	 * Returns if query constraints on code.
	 * @param query The query.
	 * @return true/false.
	 */
	protected boolean isCode(String query) {
		return query.equals("code");
	}
	
	/**
	 * Returns if query constraints on line.
	 * @param query The query.
	 * @return true/false.
	 */
	protected boolean isLine(String query) {
		return query.equals("line");
	}

	/**
	 * Returns if query constraints on comment.
	 * @param query The query.
	 * @return true/false.
	 */
	protected boolean isComment(String query) {
		return query.equals("comment");
	}

	/**
	 * Returns if query constraints on contains.
	 * @param query The word in the query.
	 * @return true/false.
	 */
	protected boolean isContains(String query) {
		return query.equals("contains");
	}

	/**
	 * Returns if query constraints on equals.
	 * @param query The word in the query.
	 * @return true/false.
	 */
	protected boolean isEquals(String query) {
		return query.equals("equals") || query.equals("=");
	}

	/**
	 * Sets result after list is analyzed.
	 * @param list Analyzed list of chunks.
	 */
	protected void setResult(ArrayList<Chunk> list) {
		result = list;
	}

	/**
	 * Gets analyzed list of chunks.
	 * @return List of analyzed chunks.
	 */
	public ArrayList<Chunk> getResult() {
		return result;
	}

	/**
	 * Executes constraintOnData with query.
	 * @param query interpreter query.
	 * @throws SQLException iff sql query goes wrong.
	 */
	protected void runData(Query query) throws SQLException {
		StringBuilder q = new StringBuilder();
		query.inc();
		while (query.hasNext()) {
			q.append(query.next());
			q.append(" ");
		}
		setResult(constraintOnData(q.toString()));
	}
	
	/**
	 * Executes constraintOnCode with query.
	 * @param query interpreter query.
	 */
	protected void runCode(Query query) {
		query.inc();
		if (isEquals(query.next())) {
			setResult(constraintOnCode(query.next()));
		}
	}
	
	/**
	 * Executes constraintOnLine with query.
	 * @param query interpreter query.
	 */
	protected void runLine(Query query) {
		query.inc();
		if (isEquals(query.next())) {
			setResult(constraintOnLine(query.next()));
		}
	}
	
	/**
	 * Executes constraint on contains/equals comment.
	 * @param query interpreter query.
	 */
	protected void runComment(Query query) {
		query.inc();
		if (isEquals(query.next())) {
			StringBuilder q = new StringBuilder();
			String prefix = "";
			while (query.hasNext()) {
				q.append(prefix);
				q.append(query.next());
				prefix = " ";
			}
			setResult(constraintOnEqualsComment(q.toString()));
		}
		if (isContains(query.next())) {
			setResult(constraintOnContainsComment(query.next()));
		}
	}
	
	/**
	 * deepcopys the list of chunks.
	 * @param list list to copy.
	 * @return returns the copy.
	 */
	protected ArrayList<Chunk> copyChunks(ArrayList<Chunk> list) {
		return SingletonInterpreter.getInterpreter().copyChunks(list);
	}
}
