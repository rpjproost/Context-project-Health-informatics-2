package context.healthinformatics.sequentialdataanalysis;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class Connections.
 */
public class Connections extends Tasks {

	private ArrayList<Chunk> chunks;
	
	/**
	 * Constructor for Connections without an argument.
	 */
	public Connections() { }

	/**
	 * Constructor for connections.
	 * 
	 * @param chunks
	 *            the chunks
	 */
	public Connections(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}
	
	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Method which sets the pointers of a chunk and
	 * a line-chunk to point to each other with an note associated to the connection.
	 * @param chunk1 is one chunk of the connection.
	 * @param chunk2 is the other chunk of the connection.
	 * @param noteForConnection string which is associated with the connection.
	 * @throws Exception e
	 */
	public void connectToChunk(Chunk chunk1, Chunk chunk2
			, String noteForConnection) throws Exception {
		
		if (chunks.indexOf(chunk1) < chunks.indexOf(chunk2)) {
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
	 * @param noteForConnection string which is associated with the connection.
	 * @throws Exception e
	 */
	public void connectToLine(Chunk chunk, int line, String noteForConnection) throws Exception {
		Chunk c = getChunkByLine(line);
		if (chunks.indexOf(chunk) < chunks.indexOf(c)) {
			connectToChunk(chunk, c, noteForConnection);
		}
	}
	
	/**
	 * Method which connects every chunk in a list of chunks to a certain chunk.
	 * @param chunkList list of chunks.
	 * @param c chunk
	 * @param noteForConnection String associated with the connection.
	 * @throws Exception e
	 */
	public void connectListOfChunksToChunk(ArrayList<Chunk> chunkList, Chunk c
			, String noteForConnection) throws Exception {
		for (Chunk chunk : chunkList) {
			connectToChunk(chunk, c, noteForConnection);
		}
	}
	
	/**
	 * Method which connects a certain chunk to every chunk in a list of chunks.
	 * @param c chunk
	 * @param chunkList list of chunks.
	 * @param noteForConnection String associated with the connection.
	 * @throws Exception e
	 */
	public void connectChunkToListOfChunks(Chunk c, ArrayList<Chunk> chunkList
			, String noteForConnection) throws Exception {
		for (Chunk chunk : chunkList) {
			connectToChunk(c, chunk, noteForConnection);
		}
	}
	
	/**
	 * Method which connects all chunks with code equal to c, to chunk.
	 * @param c1 code where the connection originates.
	 * @param c2 code where the connections is made to.
	 * @param noteForConnection string along the connection
	 * @throws Exception 
	 */
	public void connectOnCode(String c1, String c2, String noteForConnection) throws Exception {
		Chunk curChunk1 = null;
		Chunk curChunk2 = null;
		for (int i = 0; i < chunks.size(); i++) {
			curChunk1 = chunks.get(i);
			if (curChunk1.getCode().equals(c1)) {
				for (int j = i + 1; j < chunks.size(); j++) {
					curChunk2 = chunks.get(j);
					if (curChunk2.getCode().equals(c2)) {
						connectToChunk(curChunk1, curChunk2, noteForConnection);
					}
				}
			}
		}
	}
	
	/**
	 * Method which connects all chunks with comment equal to c, to chunk.
	 * @param c1 comment where the connection originates.
	 * @param c2 comment where the connections is made to.
	 * @param noteForConnection string along the connection.
	 * @throws Exception 
	 */
	public void connectOnComment(String c1, String c2, String noteForConnection) throws Exception {
		Chunk curChunk1 = null;
		Chunk curChunk2 = null;
		for (int i = 0; i < chunks.size(); i++) {
			curChunk1 = chunks.get(i);
			if (curChunk1.getComment().equals(c1)) {
				for (int j = i + 1; j < chunks.size(); j++) {
					curChunk2 = chunks.get(j);
					if (curChunk2.getComment().equals(c2)) {
						connectToChunk(curChunk1, curChunk2, noteForConnection);
					}
				}
			}
		}
	}
	
	/**
	 * Method which connects on line numbers.
	 * @param c1 the line where the connection originates.
	 * @param c2 the line where the connections is made to.
	 * @param noteForConnection string along the connection.
	 * @throws Exception e
	 */
	public void connectOnLine(int c1, int c2, String noteForConnection) throws Exception {
		Chunk curChunk1 = getChunkByLine(c1);
		Chunk curChunk2 = getChunkByLine(c2);
		connectToChunk(curChunk1, curChunk2, noteForConnection);
	}
	
	/**
	 * Retrieves the Chunk out of the Arraylist with a specific line.
	 * 
	 * @param line
	 *            the line that is needed.
	 * @return the Chunk with the corresponding line.
	 * @throws Exception
	 *             thrown if there is no Chunk with this line.
	 */
	public Chunk getChunkByLine(int line) throws Exception {
		Chunk curChunk = null;
		for (int i = 0; i < chunks.size(); i++) {
			curChunk = chunks.get(i);
			if (curChunk.getLine() == line) {
				return curChunk;
			}
		}
		throw new Exception();
	}
}
