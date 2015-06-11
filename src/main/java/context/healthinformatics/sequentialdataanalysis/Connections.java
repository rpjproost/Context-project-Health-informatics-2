
package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;

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
		ArrayList<Chunk> c = SingletonInterpreter.getInterpreter().getChunks();
		setChunks(c);
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
	 */
	private void connectOnLine(int index1, int index2) {
		if (index1 < index2) {
			copyOfChunks.get(index1).addPointer(getChunks().get(index2), noteForConnection);
		}
	}	

	/**
	 * Method which connects a chunk in origin list, to the first chunk in list two with
	 * a lower index in the copyOfChunk list.
	 * @param originChunkList one.
	 * @param destinationChunkList two.
	 */
	private void connectListsOfChunkIndices(ArrayList<Integer> originChunkList
			, ArrayList<Integer> destinationChunkList) {
		int count = 0;
		for (Integer i : destinationChunkList) {
			while (count < originChunkList.size() && originChunkList.get(count) < i) {
				connectOnLine(originChunkList.get(count), i);
				count++;
			}
		}
	}
	
	/**
	 * Method which gives a list of indices of all chunks which have code : code.
	 * @param code the desired code.
	 * @return the asked list.
	 */
	private ArrayList<Integer> getListOnCode(String code) {
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
	 * @return the asked list.
	 */
	private ArrayList<Integer> getListOnComment(String comment) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < copyOfChunks.size(); i++) {
			if (copyOfChunks.get(i).getComment().equals(comment)) {
				list.add(i);
			}
		}
		return list;
	}
	
	/**
	 * returns the list of chunks with pointers added.
	 * @return list of chunks.
	 */
	public ArrayList<Chunk> getResult() {
		return copyOfChunks;
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
	
	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		return null;
	}
	
	/**
	 * Executes constraintOnData with query.
	 * @param query interpreter query.
	 * @throws SQLException iff sql query goes wrong.
	 */
	@Override
	protected void runData(Query query) throws SQLException {
		StringBuilder q = new StringBuilder();
		ArrayList<Integer> destinationList = new ArrayList<Integer>();
		query.inc(2);
		while (!isData(query.part()) || !isComment(query.part()) || !isCode(query.part())) {
			q.append(query.part());
			q.append(" ");
			query.inc();
		}
		ArrayList<Integer> originList = getLinesFromData(q.toString());
		parseSecondCondition(query, originList, destinationList);
	}
	
	/**
	 * Executes constraintOnCode with query.
	 * @param query interpreter query.
	 */
	@Override
	protected void runCode(Query query) {
		query.inc();
		ArrayList<Integer> originList = getListOnCode(query.next());
		ArrayList<Integer> destinationList = new ArrayList<Integer>();
		parseSecondCondition(query, originList, destinationList);
	}
	
	/**
	 * Executes constraint on contains/equals comment.
	 * @param query interpreter query.
	 */
	@Override
	protected void runComment(Query query) {
		query.inc();
		ArrayList<Integer> originList = getListOnComment(query.next());
		ArrayList<Integer> destinationList = new ArrayList<Integer>();
		parseSecondCondition(query, originList, destinationList);
	}
	
	/**
	 * Executes constraint on equals line number.
	 * -1 because the list starts at 1 when we show it to the user.
	 * @param query interpreter query.
	 */
	@Override
	protected void runLine(Query query) {
		query.inc();
		int i = Integer.parseInt(query.next());
		ArrayList<Integer> originList = new ArrayList<Integer>();
		originList.add(i - 1);
		ArrayList<Integer> destinationList = new ArrayList<Integer>();
		parseSecondCondition(query, originList, destinationList);
	}
	
	/**
	 * Method which handles the second part of the query.
	 * @param query
	 * @param originList
	 * @param destinationList
	 */
	private void parseSecondCondition(Query query, ArrayList<Integer> originList,
			ArrayList<Integer> destinationList) {
		query.inc(2);
		handleSecondComment(query, originList, destinationList);
		handleSecondData(query, originList, destinationList);
		handleSecondCode(query, originList, destinationList);
		if (isLine(query.part())) {
			query.inc();
			int i = Integer.parseInt(query.next());
			destinationList.add(i - 1);
			connectListsOfChunkIndices(originList, destinationList);
		}
	}
	
	/**
	 * Method which handles the second half of the query if it starts with comment.
	 * @param query
	 * @param originList
	 * @param destinationList
	 */
	private void handleSecondComment(Query query, ArrayList<Integer> originList,
			ArrayList<Integer> destinationList) {
		if (isComment(query.part())) {
			query.inc();
			destinationList = getListOnComment(query.next());
			connectListsOfChunkIndices(originList, destinationList);
		}
	}
	
	/**
	 * Method which handles the second half of the query if it starts with code.
	 * @param query
	 * @param originList
	 * @param destinationList
	 */
	private void handleSecondCode(Query query, ArrayList<Integer> originList,
			ArrayList<Integer> destinationList) {
		if (isCode(query.part())) {
			query.inc();
			destinationList = getListOnCode(query.next());
			connectListsOfChunkIndices(originList, destinationList);
		}
	}
	
	/**
	 * Method which handles the second half of the query if it starts with data.
	 * @param query
	 * @param originList
	 * @param destinationList
	 */
	private void handleSecondData(Query query, ArrayList<Integer> originList,
			ArrayList<Integer> destinationList) {
		if (isData(query.part())) {
			StringBuilder q = new StringBuilder();
			while (query.hasNext()) {
				q.append(query.next());
				q.append(" ");
			}
			try {
				destinationList = getLinesFromData(q.toString());
			} catch (SQLException e) {
				log.info("Could not retrieve lines from database");
			}
			connectListsOfChunkIndices(originList, destinationList);
		}
	}
}
