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
public class ComputationData {
	private static ArrayList<HashMap<Integer, Double>> data;
	private static DateTime f;
	private static Date firstDate;
	public static int days;
	private static ArrayList<String> names;

	/**
	 * Initializes class first time computation is run.
	 */
	public static void init() {
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
		init();
		setDays(xAs());
		Interpreter interpreter = SingletonInterpreter.getInterpreter();
		ArrayList<Chunk> chunks = interpreter.getChunks();
		HashMap<Integer, Double> res = new HashMap<Integer, Double>();
		Db database = SingletonDb.getDb();
		if (name.equals("times")) {
			createTimesMeasured(chunks, res);
		}
		else {
			for (Chunk c : chunks) {
				int difference = differsFromDate(new DateTime(database.selectDate(c.getLine())));
				double value = c.getValue(column);
				value = value / 10;
				res.put(difference, value);
			}
		}
		data.add(res);
		names.add(name + "* 10");
	}

	/**
	 * Sets children size in a HashMap with difference in date measured by first child.
	 * @param chunks the list of chunks to be checked.
	 * @param res the result hashmap to be filled.
	 * @throws SQLException Date can not be found by chunk.
	 */
	public static void createTimesMeasured(ArrayList<Chunk> chunks, HashMap<Integer, Double> res) throws SQLException {
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
			if (days != 0) {
				System.out.println(d - days);
			}
			days = d;
		}
	}
}
