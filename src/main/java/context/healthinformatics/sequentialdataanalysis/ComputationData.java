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

	private ComputationData() {

	}


	/**
	 * Initializes class first time computation is run.
	 */
	public static void init() {
		setComputed(true);
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
		init(); setDays(xAs()); Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		HashMap<Integer, Double> res = new HashMap<Integer, Double>();
		Db database = SingletonDb.getDb();
		if (name.equals("times")) {
			createTimesMeasured(chunks, res);
			names.add(name);
		}
		else {
			for (Chunk c : chunks) {
				if (c.hasChild()) {
					computeDiffAndValueChild(column, database, c, res);
				}
				else {
					computeDiffAndValue(column, database, c, res);
				}
			}
			names.add(name + "* 10");
		}
		data.add(res);
	}

	private static void computeDiffAndValue(String column, Db database, Chunk c,
			HashMap<Integer, Double> res) throws SQLException {
		int difference = differsFromDate(new DateTime(database.selectDate(c.getLine())));
		double value = c.getValue(column);
		value = value / DIVIDE;
		res.put(difference, value);
	}
	
	private static void computeDiffAndValueChild(String column, Db database, Chunk c,
			HashMap<Integer, Double> res) throws SQLException {
		c.initializeComputations(column);
		double value = c.getAverageValue();
		int difference = differsFromDate(new DateTime(
				database.selectDate(c.getChildren().get(0).getLine())));
		res.put(difference, value);
	}

	/**
	 * Sets children size in a HashMap with difference in date measured by first child.
	 * @param chunks the list of chunks to be checked.
	 * @param res the result hashmap to be filled.
	 * @throws SQLException Date can not be found by chunk.
	 */
	public static void createTimesMeasured(ArrayList<Chunk> chunks, HashMap<Integer, Double> res)
			throws SQLException {
		Db database = SingletonDb.getDb();
		for (Chunk c : chunks) {
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
	public static int xAs() throws SQLException {
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		Db data = SingletonDb.getDb();
		Date first;
		Date last;
		if (chunks.get(0).hasChild()) {
			first = data.selectDate(chunks.get(0).getChildren().get(0).getLine());
			last = data.selectDate(chunks.get(chunks.size() - 1).getChildren().get(0).getLine());
		}
		else {
			first = data.selectDate(chunks.get(0).getLine());
			last = data.selectDate(chunks.get(chunks.size() - 1).getLine());
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
	public static int differsFromDate(DateTime actual) {
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
	public static void setComputed(boolean computed) {
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
