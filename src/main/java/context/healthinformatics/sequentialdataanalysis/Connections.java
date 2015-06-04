package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * The Class Connections.
 */
public class Connections extends Task {
	
	private String noteForConnection;
	private ArrayList<Chunk> copyOfChunks;
	private Logger log = Logger.getLogger(Comments.class.getName());
	
	/**
	 * Constructor for Connections without an argument.
	 * @param info String that contains info about the connection.
	 */
	public Connections(String info) { 
		noteForConnection = info;
		copyOfChunks = copyChunks(getChunks());
	}
	
	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method which connects on line numbers.
	 * @param index1 the line where the connection originates.
	 * @param index2 the line where the connections is made to.
	 * @throws Exception e
	 */
	public void connectOnLine(int index1, int index2) {
		if (index1 < index2) {
			HashMap<Chunk, String> pointer1 = copyOfChunks.get(index1).getPointer();
			pointer1.put(getChunks().get(index2), noteForConnection);
			copyOfChunks.get(index1).setPointer(pointer1);
		}
	}	

	/**
	 * Method which connects every chunk in list one, to every chunk in list two.
	 * @param originChunkList one 
	 * @param destinationChunkList two
	 */
	public void connectListsOfChunkIndices(ArrayList<Integer> originChunkList
			, ArrayList<Integer> destinationChunkList) {
		for (Integer i : originChunkList) {
			for (Integer j : destinationChunkList) {
				connectOnLine(i, j);
			}
		}
	}
	
	/**
	 * Method which gives a list of indices of all chunks which have code : code.
	 * @param code the desired code.
	 * @return list
	 */
	public ArrayList<Integer> getListOnCode(String code) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < copyOfChunks.size(); i++) {
			if (copyOfChunks.get(i).getCode().equals(code)) {
				list.add(i);
			}
		}
		return list;
	}
	

	/**
	 * Method which gives a list of indices of all chunks which have comment : comment.
	 * @param comment to be had.
	 * @return list
	 */
	public ArrayList<Integer> getListOnComment(String comment) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < copyOfChunks.size(); i++) {
			if (copyOfChunks.get(i).getComment().equals(comment)) {
				list.add(i);
			}
		}
		return list;
	}
	
	/**
	 * Retrieves the Chunk out of the Arraylist with a specific line.
	 * 
	 * @param line
	 *            the line that is needed.
	 * @param chunk is the list of chunks.
	 * @return the Chunk with the corresponding line.
	 * @throws Exception
	 *             thrown if there is no Chunk with this line.
	 */
	public Chunk getChunkByLine(int line, ArrayList<Chunk> chunk) throws Exception {
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
		throw new Exception();
	}

	@Override
	protected ArrayList<Chunk> constraintOnData(String whereClause)
			throws SQLException {
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		return null;
	}
	
	/**
	 * Executes constraintOnData with query.
	 * @param query interpreter query.
	 * @throws SQLException iff sql query goes wrong.
	 */
	@Override
	protected void runData(String[] query) throws SQLException {
		StringBuilder q = new StringBuilder();
		ArrayList<Integer> destinationList = new ArrayList<Integer>();
		ArrayList<Integer> originList = new ArrayList<Integer>();
		increment(2);
		while (!isData(query) || !isComment(query) || !isCode(query)) {
			q.append(query[getQueryPart()]);
			q.append(" ");
			inc();
		}
		originList = getLinesFromData(q.toString());
		parseSecondCondition(query, originList, destinationList);
	}
	
	/**
	 * Executes constraintOnCode with query.
	 * @param query interpreter query.
	 */
	@Override
	protected void runCode(String[] query) {
		increment(2);
		ArrayList<Integer> originList = getListOnCode(query[getQueryPart()]);
		ArrayList<Integer> destinationList = new ArrayList<Integer>();
		parseSecondCondition(query, originList, destinationList);
	}
	
	/**
	 * Executes constraint on contains/equals comment.
	 * @param query interpreter query.
	 */
	@Override
	protected void runComment(String[] query) {
		increment(2);
		ArrayList<Integer> originList = getListOnComment(query[getQueryPart()]);
		ArrayList<Integer> destinationList = new ArrayList<Integer>();
		parseSecondCondition(query, originList, destinationList);
	}
	
	private void parseSecondCondition(String[] query, ArrayList<Integer> originList,
			ArrayList<Integer> destinationList) {
		if (isComment(query)) {
			increment(2);
			destinationList = getListOnComment(query[getQueryPart()]);
			connectListsOfChunkIndices(originList, destinationList);
		}
		if (isData(query)) {
			StringBuilder q = new StringBuilder();
			inc();
			q = new StringBuilder();
			for (int i = getQueryPart(); i < query.length; i++) {
				q.append(query[i]);
				q.append(" ");
			}
			try {
				destinationList = getLinesFromData(q.toString());
			} catch (SQLException e) {
				log.info("Could not retrieve lines from database");
			}
			connectListsOfChunkIndices(originList, destinationList);
		}
		if (isCode(query)) {
			increment(2);
			destinationList = getListOnCode(query[getQueryPart()]);
			connectListsOfChunkIndices(originList, destinationList);
		}
	}
}
