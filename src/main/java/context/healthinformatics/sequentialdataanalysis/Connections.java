package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class Connections.
 */
public class Connections extends Task {
	
	private String noteForConnection;
	
	/**
	 * Constructor for Connections without an argument.
	 * @param info String that contains info about the connection.
	 */
	public Connections(String info) { 
		noteForConnection = info;
	}
	
	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Method which sets the pointers of a chunk and
	 * a line-chunk to point to each other with an note associated to the connection.
	 * @param chunk1 is one chunk of the connection.
	 * @param chunk2 is the other chunk of the connection.
	 * @throws Exception e
	 */
	public void connectToChunk(Chunk chunk1, Chunk chunk2) throws Exception {
		if (getChunks().indexOf(chunk1) < getChunks().indexOf(chunk2)) {
			HashMap<Chunk, String> pointer1 = chunk1.getPointer();
			pointer1.put(chunk2, noteForConnection);
			chunk1.setPointer(pointer1);
		}
	}
	
	/**
	 * Method which sets the pointers of a chunk and
	 * a line-chunk to point to each other with an note associated to the connection.
	 * @param chunk is one chunk of the connection.
	 * @param line line of the chunk at the other end of the connection.
	 * @throws Exception e
	 */
	public void connectToLine(Chunk chunk, int line) throws Exception {
		Chunk c = getChunkByLine(line, getChunks());
		connectToChunk(chunk, c);
	}
	
	/**
	 * Method which connects every chunk in a list of chunks to a certain chunk.
	 * @param chunkList list of chunks.
	 * @param c chunk
	 * @throws Exception e
	 */
	public void connectListOfChunksToChunk(ArrayList<Chunk> chunkList, Chunk c) throws Exception {
		for (Chunk chunk : chunkList) {
			connectToChunk(chunk, c);
		}
	}
	
	/**
	 * Method which connects a certain chunk to every chunk in a list of chunks.
	 * @param c chunk
	 * @param chunkList list of chunks.
	 * @throws Exception e
	 */
	public void connectChunkToListOfChunks(Chunk c, ArrayList<Chunk> chunkList) throws Exception {
		for (Chunk chunk : chunkList) {
			connectToChunk(c, chunk);
		}
	}
	
	/**
	 * Method which connects all chunks with code equal to c, to chunk.
	 * @param c1 code where the connection originates.
	 * @param c2 code where the connections is made to.
	 * @throws Exception 
	 */
	public void connectOnCode(String c1, String c2) throws Exception {
		Chunk curChunk1 = null;
		Chunk curChunk2 = null;
		for (int i = 0; i < getChunks().size(); i++) {
			curChunk1 = getChunks().get(i);
			if (curChunk1.getCode().equals(c1)) {
				for (int j = i + 1; j < getChunks().size(); j++) {
					curChunk2 = getChunks().get(j);
					if (curChunk2.getCode().equals(c2)) {
						connectToChunk(curChunk1, curChunk2);
					}
				}
			}
		}
	}
	
	/**
	 * Method which connects all chunks with comment equal to c, to chunk.
	 * @param c1 comment where the connection originates.
	 * @param c2 comment where the connections is made to.
	 * @throws Exception 
	 */
	public void connectOnComment(String c1, String c2) throws Exception {
		Chunk curChunk1 = null;
		Chunk curChunk2 = null;
		for (int i = 0; i < getChunks().size(); i++) {
			curChunk1 = getChunks().get(i);
			if (curChunk1.getComment().equals(c1)) {
				for (int j = i + 1; j < getChunks().size(); j++) {
					curChunk2 = getChunks().get(j);
					if (curChunk2.getComment().equals(c2)) {
						connectToChunk(curChunk1, curChunk2);
					}
				}
			}
		}
	}
	
	/**
	 * Method which connects on line numbers.
	 * @param c1 the line where the connection originates.
	 * @param c2 the line where the connections is made to.
	 * @throws Exception e
	 */
	public void connectOnLine(int c1, int c2) throws Exception {
		Chunk curChunk1 = getChunkByLine(c1, getChunks());
		Chunk curChunk2 = getChunkByLine(c2, getChunks());
		connectToChunk(curChunk1, curChunk2);
	}
	
	/**
	 * Method which connects a chunk to all chunks.
	 * @param chunk which m connections.
	 * @param whereClause condition to be met.
	 * @throws Exception e
	 */
	public void connectOnData(Chunk chunk, String whereClause) throws Exception {
		ArrayList<Integer> list = getLinesFromData(whereClause);
		for (Integer i : list) {
			connectToLine(chunk, i);
		}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}
}
