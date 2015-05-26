package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class Connections.
 */
public class Connections {

	private ArrayList<Chunk> chunks;

	/**
	 * Constructor for connections.
	 * 
	 * @param chunks
	 *            the chunks
	 */
	public Connections(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
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
		HashMap<Chunk, String> pointer2 = chunk.getPointer();
		pointer2.put(c, noteForConnection);
		chunk.setPointer(pointer2);
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
		
		HashMap<Chunk, String> pointer1 = chunk1.getPointer();
		pointer1.put(chunk2, noteForConnection);
		chunk1.setPointer(pointer1);
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
