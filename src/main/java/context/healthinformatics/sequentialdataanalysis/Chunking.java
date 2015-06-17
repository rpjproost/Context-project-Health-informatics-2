package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * Class for chunking a list of chunks.
 *
 */
public class Chunking extends Task {

	private Chunk temp;
	private String code = "";
	private int indexcheck = 0;
	private ArrayList<Chunk> newChunks = new ArrayList<Chunk>();
	/**
	 * Constructor for chunking.
	 */
	public Chunking() {	}
	
	/**
	 * Chunking with parameter.
	 * @param parameter code for new chunks.
	 */
	public Chunking(String parameter) {
		code = parameter;
	}
	
	/**
	 * Create chunks on constraint data.
	 * @param whereClause for sql query.
	 * @return new Arraylist with chunked chunks.
	 * @throws SQLException if whereclause was not correct.
	 */
	@Override
	public ArrayList<Chunk> constraintOnData(String whereClause) throws SQLException {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp = new Chunk();
		ArrayList<Integer> ints = getLinesFromData(whereClause);
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnEqualsData(ints, curChunk, res, indexcheck);
		}
		addLastElementToChunks(res);
		return res;
	}

	/**
	 * Add chunks with data to new chunks if constraint on data passes.
	 * @param ints the arraylist with integers with correct data.
	 * @param curChunk Chunk that currently is being checked.
	 * @param res arraylist result.
	 * @param index for list to check
	 */
	public void addChunkOnEqualsData(ArrayList<Integer> ints, Chunk curChunk,
			ArrayList<Chunk> res, int index) {
		if (indexcheck < ints.size() && curChunk.getLine() == ints.get(indexcheck)) {
			temp.setChunk(curChunk);
			indexcheck++;
		}
		else {
			addChunkToRes(res, curChunk);
			this.temp = new Chunk();
		}
	}

	/**
	 * Chunking chunks on a code constraint.
	 * @param code Code constraint for a chunk.
	 * @return New ArrayList of chunks chunked on code.
	 */
	@Override
	public ArrayList<Chunk> constraintOnCode(String code) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnEqualsCode(curChunk, code, res);
		}
		addLastElementToChunks(res);
		return res;
	}

	/**
	 * Add chunk to new Chunk in new ArrayList if it equals code.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for code constraint.
	 * @param res ArrayList of chunks chunked.
	 */
	private void addChunkOnEqualsCode(Chunk curChunk, String c, ArrayList<Chunk> res) {
		if (curChunk.getCode().equals(c)) {
			temp.setChunk(curChunk);
		}
		else {
			addChunkToRes(res, curChunk);
			this.temp  = new Chunk();
		}
	}

	/**
	 * Chunking chunks on a comment contains constraint.
	 * @param comment String for comment.
	 * @return new ArrayList of chunks chunked on constraint.
	 */
	@Override
	public ArrayList<Chunk> constraintOnContainsComment(String comment) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnContainsComment(curChunk, comment, res);
		}
		addLastElementToChunks(res);
		return res;
	}
	
	/**
	 * Add chunk to new Chunk in new ArrayList if it contains comment.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList<Chunk>
	 * @param c String for comment constraint.
	 * @param res ArrayList of chunks chunked.
	 */
	private void addChunkOnContainsComment(Chunk curChunk, String c, 
			ArrayList<Chunk> res) {
		if (curChunk.getComment().contains(c)) {
			temp.setChunk(curChunk);
		}
		else {
			addChunkToRes(res, curChunk);
			temp  = new Chunk();
		}
	}

	/**
	 * Chunking chunks on a comment equals constraint.
	 * @param comment String for comment.
	 * @return new ArrayList of chunks chunked on constraint.
	 */
	@Override
	public ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		ArrayList<Chunk> chunks = getChunks();
		temp  = new Chunk();
		for (int i = 0; i < chunks.size(); i++) {
			Chunk curChunk = chunks.get(i);
			addChunkOnEqualsComment(curChunk, comment, res);
		}
		addLastElementToChunks(res);
		return res;
	}

	/**
	 * Add chunk to new Chunk in new ArrayList if it equals comment.
	 * @param curChunk Chunk to be checked on constraint.
	 * @param temp New chunk that will be returned in new ArrayList of chunks
	 * @param c String for comment constraint.
	 * @param res ArrayList of chunks chunked.
	 */
	private void addChunkOnEqualsComment(Chunk curChunk, String c, 
			ArrayList<Chunk> res) {
		if (curChunk.getComment().equals(c)) {
			temp.setChunk(curChunk);
		}
		else {
			addChunkToRes(res, curChunk);
			temp  = new Chunk();
		}
	}

	/**
	 * Adds a Chunk to a new chunk if it is part of the Chunk.
	 * @param temp The new Chunk.
	 * @param res The new ArrayList of chunks to be returned after chunking.
	 */
	private void addChunkToRes(ArrayList<Chunk> res, Chunk curChunk) {
		if (temp.hasChild()) {
			res.add(temp);
			temp.setCode(code);
			res.add(curChunk);
		}
		else {
			res.add(curChunk);
		}
	}

	/**
	 * Adds last chunked chunk to result ArrayList.
	 * @param res ArrayList to be returned.
	 */
	private void addLastElementToChunks(ArrayList<Chunk> res) {
		if (temp.hasChild()) {
			res.add(temp);
		}
	}

	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void run(Query query) throws Exception {
		try {
			super.run(query);
		} catch (Exception e) {
			boolean check = isDate(query.part());
			if (check) {
				//setResult(chunkOnDate(query));
				chunkOnDate(query);
				setResult(newChunks);
			}
			else {
				throw new Exception(e.getMessage() + "/date");
			}
		}
	}
	
	private boolean isDate(String s) {
		return "date".equals(s);
	}
	
	/**
	 * Chunks the data repeatedly on dates.
	 * @param q Query to run.
	 * @return The output of the query.
	 * @throws Exception throws an exception if chunking on date goes wrong.
	 */
	protected ArrayList<Chunk> chunkOnDate(Query q) throws Exception {
		ArrayList<Chunk> res = new ArrayList<Chunk>();
		int days = Integer.parseInt(q.next());
		ArrayList<Integer> sizes = intsOnDate(getStartDate(), days - 1);
		int chunkIndex = 0;
		for (int i = 0; i < sizes.size(); i++) {
			Chunk temp = new Chunk();
			for (int k = 0; k < sizes.get(i); k++) {
				temp.setChunk(getChunks().get(chunkIndex));
				chunkIndex++;
			}
			res.add(temp);
		}
		return res;
	}
	
	private Date getStartDate() throws SQLException {
		Chunk chunk = getChunks().get(0);
		while (chunk.hasChild()) {
			chunk = chunk.getChildren().get(0);
		}
		return SingletonDb.getDb().selectDate(chunk.getLine());
	}

	/**
	 * gives a list of integers with the number of chunks between the dates.
	 * @param start Start date of the method
	 * @param days number of days to chunk.
	 * @return list with the sizes of the chunks.
	 */
	protected ArrayList<Integer> intsOnDate(Date start, int days) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		int size = 0;
		Calendar c = Calendar.getInstance();
		Date startDate = start;
		Date endDate = null;
		int numChunks;
		c.setTime(start);
		while (size < getChunks().size()) {
			c.add(Calendar.DAY_OF_MONTH, days);
			endDate = c.getTime();
			numChunks  = getPeriod(startDate, endDate);
			if (numChunks > 0) {
				res.add(numChunks);
			}
			c.add(Calendar.DAY_OF_MONTH, 1);
			startDate = c.getTime();
			size += numChunks;
		}
		return res;
	}
	
	/**
	 * returns the number of chunks between the given dates.
	 * @param start start date;
	 * @param end end date;
	 * @return the number of chunks between that.
	 */
	protected int getPeriod(Date start, Date end) {
		Db data = SingletonDb.getDb();
		for (Chunk c : getChunks()) {
			if (c.hasChild()) {
				for (Chunk child : c.getChildren()) {
					try {
						Date childDate = data.selectDate(child.getLine());
						if (childDate.before(end) && childDate.after(start)) {
							newChunks.add(child);
						}
						else {
							break;
						}
					} catch (SQLException e) {
						break;
					}
					
				}
			}
			else {
				Date cDate;
				try {
					cDate = data.selectDate(c.getLine());
					if (cDate.before(end) && cDate.after(start)) {
						newChunks.add(c);
					}
					else {
						break;
					}
				} catch (SQLException e) {
						break;
				}
			}
		}
		String s = "date BETWEEN '" + convertDate(start) + "' AND '" + convertDate(end) + "'";
		try {
			return getLinesFromData(s).size();
		} catch (SQLException e) {
			return 0;
		}
	}
	
	private String convertDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
}
