package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The Class Codes.
 */
public class Codes extends Task {

	private String code;
	private ArrayList<Integer> changedLines;
	private ArrayList<String> oldCodes;
	/**
	 * Constructor for codes without an argument.
	 * @param c Code what is going to be set.
	 */
	public Codes(String c) {
		code = c;
		changedLines = new ArrayList<Integer>();
		oldCodes = new ArrayList<String>();
	}
	
	@Override
	public ArrayList<Chunk> undo() {
		for (int i = 0; i < changedLines.size(); i++) {
			int index = changedLines.get(i);
			getChunks().get(index).setCode(oldCodes.get(i));
		}
		return getChunks();
	}

	/**
	 * Set the code on line line with the string code.
	 * 
	 * @param line
	 *            the line of the code.
	 * @param code
	 *            the code to be set.
	 */
	public void setCodeOfLine(int line, String code) {
		Chunk temp = getChunkByLine(line, getChunks());
		if (temp != null) {
			temp.setCode(code);
		}
	}

	/**
	 * Set the code on the chunk at index in the chunks ArrayList with the
	 * string code.
	 * 
	 * @param indexInChunks
	 *            the index of the chunk in the ArrayList chunks.
	 * @param code
	 *            the code to be set.
	 */
	public void setCodeOfChunks(int indexInChunks, String code) {
		try {
			Chunk c = getChunks().get(indexInChunks);
			c.setCode(code);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Method which sets the code for every chunk in a list.
	 * @param list of chunks.
	 * @param code code to be set.
	 */
	public void setCodeOfListOfChunks(ArrayList<Chunk> list, String code) {
		for (Chunk c : list) {
			c.setCode(code);
		}
	}

	/**
	 * Method which sets the code of every chunk with the comment : comment, to code.
	 * @param comment comment to set the code on.
	 */
	public void setCodeOnComment(String comment) {
		try {
			for (int i = 0; i < getChunks().size(); i++) {
				Chunk chunk = getChunks().get(i);
				if (chunk.getComment().equals(comment)) {
					oldCodes.add(chunk.getCode());
					changedLines.add(i);
					chunk.setCode(code);
					
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Method which replaces the code of every chunk with the code : previousCode, to code.
	 * @param previousCode old code.
	 */
	public void setCodeOnCode(String previousCode) {
		for (int i = 0; i < getChunks().size(); i++) {
			Chunk chunk = getChunks().get(i);
			if (chunk.getCode().equals(previousCode)) {
				oldCodes.add(chunk.getCode());
				changedLines.add(i);
				chunk.setCode(code);
			}
		}
	}

	/**
	 * Method which sets the code for all.
	 * @param whereClause to be met.
	 * @throws SQLException If sql query is incorrect.
	 */
	public void setCodeOnData(String whereClause) throws SQLException {
		ArrayList<Integer> list = getLinesFromData(whereClause);
		for (Integer i : list) {
			Chunk c = getChunkByLine(i, getChunks());
			changedLines.add(getChunks().indexOf(c));
			oldCodes.add(c.getCode());
			setCodeOfLine(i, code);
		}
	}

	@Override
	protected ArrayList<Chunk> constraintOnData(String whereClause) throws SQLException {
		setCodeOnData(whereClause);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		setCodeOnCode(code);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		setCodeOnComment(comment);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		int i = Integer.parseInt(line);
		changedLines.add(i - 1);
		oldCodes.add(getChunks().get(i - 1).getCode());
		getChunks().get(i - 1).setCode(code);
		return getChunks();
	}
}
