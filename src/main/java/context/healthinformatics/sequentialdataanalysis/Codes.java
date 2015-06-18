package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * The Class Codes.
 */
public class Codes extends Task {

	private String code;
	private ArrayList<Chunk> changedChunks;
	private ArrayList<String> oldCodes;
	private int index = 0;
	/**
	 * Constructor for codes without an argument.
	 * @param c Code what is going to be set.
	 */
	public Codes(String c) {
		code = c;
		changedChunks = new ArrayList<Chunk>();
		oldCodes = new ArrayList<String>();
	}
	
	@Override
	public ArrayList<Chunk> undo() {
		for (int i = 0; i < changedChunks.size(); i++) {
			Chunk chunk = changedChunks.get(i);
			chunk.setCode(oldCodes.get(i));
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
					changedChunks.add(chunk);
					chunk.setCode(code);
					
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Method which sets the code of every chunk with the comment : comment, to code.
	 * @param comment comment to set the code on.
	 */
	public void setCodeOnContainsComment(String comment) {
		try {
			for (int i = 0; i < getChunks().size(); i++) {
				Chunk chunk = getChunks().get(i);
				if (chunk.getComment().contains(comment)) {
					oldCodes.add(chunk.getCode());
					changedChunks.add(chunk);
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
				changedChunks.add(chunk);
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
		setCodesOnChild(getChunks(), list);
		index = 0;
	}
	
	private void setCodesOnChild(ArrayList<Chunk> childs, ArrayList<Integer> list) {
		for (int i = 0; i < childs.size(); i++) {
			Chunk temp = childs.get(i);
			if (index < list.size() && temp.getLine() == list.get(index)) {
				oldCodes.add(temp.getCode());
				changedChunks.add(temp);
				temp.setCode(code);
				index++;
			}
			else if (temp.hasChild()) {
				setCodesOnChild(temp.getChildren(), list);
			}

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
		setCodeOnContainsComment(comment);
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		int i = Integer.parseInt(line);
		changedChunks.add(getChunks().get(i - 1));
		oldCodes.add(getChunks().get(i - 1).getCode());
		getChunks().get(i - 1).setCode(code);
		return getChunks();
	}
	
	@Override
	public void run(Query query) throws Exception {
		try {
			super.run(query);
		} catch (Exception e) {
			if (query.part().equals("date")) {
				setCodeDate();
			}
			else if (query.part().equals("difference")) {
				setCodeOndifference(query.next());
			}
			else {
				throw new Exception(e.getMessage() + "/date");
			}
		}
	}
	
	private void setCodeOndifference(String number) {
		for (Chunk c : getChunks()) {
			if (c.getDifference() != Integer.MIN_VALUE) {
				double smallerThan = Integer.parseInt(number);
				if (c.getDifference() < smallerThan) {
					changedChunks.add(c);
					oldCodes.add(c.getCode());
					c.setCode(code);
				}
			}
		}
		setResult(getChunks());
		
	}

	private void setCodeDate() throws SQLException {
		Db data = SingletonDb.getDb();
		for (Chunk c : getChunks()) {
			if (c.hasChild()) {
				for (Chunk child : c.getChildren()) {
					Date date = data.selectDate(child.getLine());
					String day = new SimpleDateFormat("EEEE").format(date);
					changedChunks.add(child);
					oldCodes.add(child.getCode());
					child.setCode(day);
				}
			}
			else {
				Date date = data.selectDate(c.getLine());
				String day = new SimpleDateFormat("EEEE").format(date);
				changedChunks.add(c);
				oldCodes.add(c.getCode());
				c.setCode(day);
			}
		}
		setResult(getChunks());
	}
}
