package context.healthinformatics.SequentialDataAnalysis;

import java.util.ArrayList;

/**
 * The Class Codes.
 */
public class Codes {
	
	private ArrayList<Chunk> chunks;

	/**
	 * Constructor for codes.
	 * 
	 * @param chunks the chunks
	 */
	public Codes(ArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}
	
	/**
	 * Set the code on line line with the string code.
	 * 
	 * @param line
	 *            the line of the code
	 * @param code
	 *            the code to be set
	 * @throws Exception 
	 */
	public void setCodeOfLine(int line, String code) throws Exception {
		try {
			Chunk curChunk = null;
			for (int i = 0; i < chunks.size(); i++) {
				curChunk = chunks.get(i);
				if (curChunk.getLine() == line) {
					curChunk.setCode(code);
					break;
				}
			}
		} catch (Exception e) {
			throw new Exception("Code could not be set.");
		}
	}
	
	/**
	 * Set the code on the chunk at index in the chunks ArrayList with the string code.
	 * 
	 * @param index
	 *            the index of the chunk in the ArrayList chunks
	 * @param code
	 *            the code to be set
	 * @throws Exception 
	 */
	public void setCodeOfChunks(int index, String code) throws Exception {
		try {
			Chunk c = chunks.get(index);
			c.setCode(code);
		} catch (Exception e) {
			throw new Exception("Code could not be set.");
		}
	}

}
