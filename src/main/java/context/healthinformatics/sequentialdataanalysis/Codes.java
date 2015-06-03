package context.healthinformatics.sequentialdataanalysis;

import java.util.ArrayList;

/**
 * The Class Codes.
 */
public class Codes extends Tasks {

	private ArrayList<Chunk> chunks;
	
	/**
	 * Constructor for codes without an argument.
	 */
	public Codes() { }

	/**
	 * Constructor for codes.
	 * 
	 * @param chunks
	 *            the chunks
	 */
	public Codes(ArrayList<Chunk> chunks) {
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
	 * Set the code on line line with the string code.
	 * 
	 * @param line
	 *            the line of the code
	 * @param code
	 *            the code to be set
	 * @throws Exception
	 *             thrown if you couldn't set the code.
	 */
	public void setCodeOfLine(int line, String code) throws Exception {
		try {
			getChunk(line).setCode(code);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Set the code on the chunk at index in the chunks ArrayList with the
	 * string code.
	 * 
	 * @param index
	 *            the index of the chunk in the ArrayList chunks
	 * @param code
	 *            the code to be set
	 * @throws Exception
	 */
	public void setCodeOfChunks(int index, String code) {
		try {
			Chunk c = chunks.get(index);
			c.setCode(code);
		} catch (Exception e) {
			throw e;
		}
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
	public Chunk getChunk(int line) throws Exception {
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
