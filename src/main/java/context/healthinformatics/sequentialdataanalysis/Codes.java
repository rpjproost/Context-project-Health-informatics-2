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
	protected void setCodeOfLine(int line, String code) {
		Chunk temp = getChunkByLine(line, getChunks());
		if (temp != null) {
			temp.setCode(code);
		}
	}

	/**
	 * Method which sets the code of every chunk with the comment : comment, to code.
	 * @param comment comment to set the code on.
	 */
	protected void setCodeOnComment(String comment) {
		try {
			for (int i = 0; i < getChunks().size(); i++) {
				Chunk chunk = getChunks().get(i);
				if (chunk.hasChild()) {
					codeOnChildOnEqualsComment(chunk.getChildren(), comment);
				}
				else if (chunk.getComment().equals(comment)) {
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
	 * Sets new code on child chunk if equals prevCode.
	 * @param childs Arraylist of chunks from parent chunk.
	 * @param prevCode String of previous code.
	 */
	private void codeOnChildOnOldCode(ArrayList<Chunk> childs, String prevCode) {
		for (Chunk c : childs) {
			if (c.getCode().equals(prevCode)) {
				c.setCode(code);
			}
		}
	}
	
	private void codeOnChildOnEqualsComment(ArrayList<Chunk> childs, String comment) {
		for (Chunk c : childs) {
			if (c.getComment().equals(comment)) {
				c.setCode(code);
			}
		}
	}
	
	private void codeOnChildOnContainsComment(ArrayList<Chunk> childs, String comment) {
		for (Chunk c : childs) {
			if (c.getComment().contains(comment)) {
				c.setCode(code);
			}
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
				if (chunk.hasChild()) {
					codeOnChildOnContainsComment(chunk.getChildren(), comment);
				}
				else if (chunk.getComment().contains(comment)) {
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
			if (chunk.hasChild()) {
				codeOnChildOnOldCode(chunk.getChildren(), previousCode);
			}
			else if (chunk.getCode().equals(previousCode)) {
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
	
	/**
	 * Set code on child of current chunk.
	 * @param childs Arraylist of childs.
	 * @param list result list.
	 */
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
				setCodeOnDifference(query.next());
			}
			else {
				throw new Exception(e.getMessage() + "/date");
			}
		}
	}
	
	/**
	 * Sets code when difference smaller than number.
	 * @param number integer of difference constraint.
	 */
	private void setCodeOnDifference(String number) {
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

	/**
	 * Sets code (monday/tuesday/..) on date of the chunk.
	 * @throws SQLException If chunk has no date.
	 */
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
