package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.Days;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * Class for computing and storing computations.
 *
 */
public final class ComputationData {
	private static boolean computed = false;
	private static ArrayList<HashMap<Integer, Double>> data;
	private static DateTime f;
	private static Date firstDate;
	private static int days;
	private static ArrayList<String> names;
	public static final int DIVIDE = 10;
	private static Interpreter interpreter;
	private static Db database;

	private ComputationData() {

	}


	/**
	 * Initializes class first time computation is run.
	 */
	public static void init() {
		setComputed(true);
		interpreter = SingletonInterpreter.getInterpreter();
		database = SingletonDb.getDb();
		if (data == null) {
			data = new ArrayList<HashMap<Integer, Double>>();
			names = new ArrayList<String>();
		}
	}

	/**
	 * Creates a hashMap with computed value and difference in date.
	 * @param column Value to be checked
	 * @param name name of the computation
	 * @throws SQLException iff data can not be found at a chunk.
	 */
	public static void createHashMap(String column, String name) throws SQLException {
		init(); setDays(xAs()); 
		HashMap<Integer, Double> res = new HashMap<Integer, Double>();
		if (name.equals("times")) {
			createTimesMeasured(res);
			names.add(name);
		}
		else {
			computeDiffValue(column, res, name);
		}
		data.add(res);
	}

	/**
	 * Computes difference in value.
	 * @param chunks Arraylist of chunks
	 * @param column column of chunk to be checked in difference.
	 * @param res Hashmap with computations.
	 * @param name name of this computation.
	 * @throws SQLException if column could not be found in this chunk.
	 */
	private static void computeDiffValue(String column, HashMap<Integer, Double> res, 
			String name) throws SQLException {
		for (Chunk c : interpreter.getChunks()) {
			if (c.hasChild()) {
				computeDiffAndValueChild(column, c, res);
			}
			else {
				computeDiffAndValue(column, c, res);
			}
		}
		names.add(name + "* 10");
	}


	/**
	 * Compute difference on this parent chunk.
	 * @param column column to be checked.
	 * @param c chunk to be checked.
	 * @param res result hashmap with computations.
	 * @throws SQLException if column could not be found in this chunk.
	 */
	private static void computeDiffAndValue(String column, Chunk c,
			HashMap<Integer, Double> res) throws SQLException {
		int difference = differsFromDate(new DateTime(database.selectDate(c.getLine())));
		double value = c.getValue(column);
		value = value / DIVIDE;
		res.put(difference, value);
	}
	
	/**
	 * Compute difference on this child chunk.
	 * @param column column to be checked.
	 * @param c chunk to be checked.
	 * @param res result hashmap with computations.
	 * @throws SQLException if column could not be found in this chunk.
	 */
	private static void computeDiffAndValueChild(String column, Chunk c,
			HashMap<Integer, Double> res) throws SQLException {
		c.initializeComputations(column);
		double value = c.getAverageValue();
		int difference = differsFromDate(new DateTime(
				database.selectDate(c.getChildren().get(0).getLine())));
		res.put(difference, value);
	}

	/**
	 * Sets children size in a HashMap with difference in date measured by first child.
	 * @param res the result hashmap to be filled.
	 * @throws SQLException Date can not be found by chunk.
	 */
	private static void createTimesMeasured(HashMap<Integer, Double> res)
			throws SQLException {
		Db database = SingletonDb.getDb();
		for (Chunk c : interpreter.getChunks()) {
			double value = c.getChildren().size();
			int difference = differsFromDate(new DateTime(
					database.selectDate(c.getChildren().get(0).getLine())));
			res.put(difference, value);
		}
	}

	/**
	 * Create data for the x-as.
	 * 
	 * @return number of days between first and last day.
	 * @throws SQLException
	 *             if data is not found
	 */
	private static int xAs() throws SQLException {
		ArrayList<Chunk> chunks = interpreter.getChunks();
		Date first;
		Date last;
		if (chunks.get(0).hasChild()) {
			first = database.selectDate(chunks.get(0).getChildren().get(0).getLine());
			last = database.selectDate(chunks.get(chunks.size() - 1).
					getChildren().get(0).getLine());
		}
		else {
			first = database.selectDate(chunks.get(0).getLine());
			last = database.selectDate(chunks.get(chunks.size() - 1).getLine());
		}
		if (f == null) {
			f = new DateTime(first);
			firstDate = first;
		}
		else if (first.before(firstDate)) {
			f = new DateTime(first);
			firstDate = first;
		}
		DateTime l = new DateTime(last);
		return Days.daysBetween(f, l).getDays();
	}

	/**
	 * Returns integer of how many days it differs from first.
	 * @param actual date to be checked.
	 * @return difference in integer.
	 */
	private static int differsFromDate(DateTime actual) {
		return Days.daysBetween(f, actual).getDays();
	}

	/**
	 * Returns the data of the computations.
	 * @return HashMap with integer and double.
	 */
	public static ArrayList<HashMap<Integer, Double>> getData() {
		return data;
	}

	/**
	 * Get names of the computation.
	 * @return Names of the computations in ArrayList string.
	 */
	public static ArrayList<String> getNames() {
		return names;
	}

	/**
	 * Returns biggest difference in days.
	 * @return Integer how many days there should be on the x-as.
	 */
	public static int getDays() {
		return days;
	}

	private static void setDays(int d) {
		if (d > days) {
			days = d;
		}
	}

	/**
	 * Returns if there are computations.
	 * @return true iff there are computations.
	 */
	public static boolean isComputed() {
		return computed;
	}

	/**
	 * Sets computed on true.
	 * @param computed true.
	 */
	private static void setComputed(boolean computed) {
		ComputationData.computed = computed;
	}


	/**
	 * Undo computations.
	 */
	public static void undo() {
		setComputed(false);
		data = new ArrayList<HashMap<Integer, Double>>();
		names = new ArrayList<String>();
		days = 0;
		f = new DateTime();
		firstDate = new Date();
	}
}
