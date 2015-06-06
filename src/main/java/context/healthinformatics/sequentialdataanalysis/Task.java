package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * 
 * Abstract class that every C class uses.
 *
 */
public abstract class Task {

	private String mergeTable = SingletonDb.getDb().getMergeTable();
	private int querypart = 1;

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
	public void run(String[] query) throws Exception {
		ArrayList<Chunk> c = SingletonInterpreter.getInterpreter().getChunks();
		setChunks(c);
		if (isData(query)) {
			runData(query);
		}
		else if (isCode(query)) {
			runCode(query);
		}
		else if (isComment(query)) {
			runComment(query);
		}
		else {
			throw new Exception("query input is wrong at: " + query[getQueryPart()]
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
				return getChunkByLine(line, curChunk.getChunks());
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
	protected boolean isData(String[] query) {
		return query[querypart].equals("data");
	}

	/**
	 * Returns if query constraints on code.
	 * @param query The query.
	 * @return true/false.
	 */
	protected boolean isCode(String[] query) {
		return query[querypart].equals("code");
	}

	/**
	 * Returns if query constraints on comment.
	 * @param query The query.
	 * @return true/false.
	 */
	protected boolean isComment(String[] query) {
		return query[querypart].equals("comment");
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
	 * Increment the querypart (word you are interpreting).
	 * @param i Integer for how many words you skip.
	 */
	protected void increment(int i) {
		querypart += i;
	}
	
	/**
	 * Increment querypart counter with 1.
	 */
	protected void inc() {
		querypart++;
	}
	
	/**
	 * Returns actual query counter.
	 * @return Integer of query counter.
	 */
	protected int getQueryPart() {
		return querypart;
	}
	
	/**
	 * Executes constraintOnData with query.
	 * @param query interpreter query.
	 * @throws SQLException iff sql query goes wrong.
	 */
	protected void runData(String[] query) throws SQLException {
		StringBuilder q = new StringBuilder();
		increment(2);
		for (int i = getQueryPart(); i < query.length; i++) {
			q.append(query[i]);
			q.append(" ");
		}
		setResult(constraintOnData(q.toString()));
	}
	
	/**
	 * Executes constraintOnCode with query.
	 * @param query interpreter query.
	 */
	protected void runCode(String[] query) {
		increment(2);
		if (isEquals(query[getQueryPart()])) {
			inc();
			setResult(constraintOnCode(query[getQueryPart()]));
		}
	}
	
	/**
	 * Executes constraint on contains/equals comment.
	 * @param query interpreter query.
	 */
	protected void runComment(String[] query) {
		increment(2);
		if (isEquals(query[getQueryPart()])) {
			inc();
			constraintOnEqualsComment(query[getQueryPart()]);
		}
		if (isContains(query[getQueryPart()])) {
			inc();
			constraintOnContainsComment(query[getQueryPart()]);
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
