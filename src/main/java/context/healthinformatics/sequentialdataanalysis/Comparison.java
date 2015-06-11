package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.SingletonDb;

/**
 * Class which determines the advice for patients in the ADMIRE project
 * according to there creatine values.
 */
public class Comparison extends Task {
	
	private ArrayList<Integer> values;
	private ArrayList<Date> dates;
	
	/**
	 * Constructor for Comparison.
	 */
	public Comparison() {
		values = new ArrayList<Integer>();
		dates = new ArrayList<Date>();
	}
	
	private void getCreaValuesAndDates() throws SQLException {
		ResultSet rs = SingletonDb.getDb().selectResultSet("workspace", "value, date", "beschrijving = 'Kreatinine (stat)'");
		values = new ArrayList<Integer>();
		dates = new ArrayList<Date>();
		while (rs.next()) {
			values.add(rs.getInt("value"));
			dates.add(rs.getDate("date"));
		}
	}
	
	/**
	 * Executes a  comparison task.
	 * @param query An array of query words.
	 * @throws Exception query input can be wrong.
	 */
	@Override
	public void run(Query query) throws Exception {////////////////////////////////////////////////////////////////////////
		ArrayList<Chunk> c = SingletonInterpreter.getInterpreter().getChunks();
		query.inc();
		setChunks(c);
		//handle second part
	}
	
	private ArrayList<String> getAdvice() throws SQLException {
		ArrayList<String> list = determineCreatineStatus();
		ArrayList<Date> uniqueDates = removeDuplicateDates();
	}
	
	private ArrayList<String> determineCreatineStatus() throws SQLException {
		ArrayList<String> result = new ArrayList<String>();//correspondeert met dates waar alle dubble uit zijn gehaald.
		ArrayList<String> boundaries =  calculateMeasurementBoundaries();
		int count = 4;// omdat je pas bij de vijfde meting iets kan zeggen.
		while (count < dates.size()) {
			if (!dates.get(count).equals(dates.get(count + 1))) {
				result.add(boundaries.get(count));
				count++;
			}
			else {
				while (!dates.get(count).equals(dates.get(count + 1))) {
					count++;
				}
				result.add(resolveBoundaries(boundaries.get(count), boundaries.get(count - 1)));
				count++;
			}
		}
		return result;
	}
	
	private ArrayList<Date> removeDuplicateDates() {
		ArrayList<Date> list = new ArrayList<Date>();
		Date d = dates.get(0);
		list.add(d);
		for (int i = 1; i < dates.size(); i++) {
			Date curDate = dates.get(i);
			if (!d.equals(curDate)) {
				list.add(curDate);
				d = curDate;
			}
		}
		return list;
	}
	
	private String resolveBoundaries(String s1, String s2) {
		if (s1.equals("Safe")) {
			return "Safe";
		}
		if (s1.equals("Reasonably Safe")) {
			return "Reasonably Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s1.equals("Safe")) {
			return "Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s1.equals("Reasonably Safe")) {
			return "Reasonably Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s1.equals("Somewhat Safe")) {
			return "Somewhat Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s1.equals("Concerned")) {
			return "Concerned";
		}
		if (s1.equals("Concerned") &&  s1.equals("Safe")) {
			return "Reasonably Safe";
		}
		if (s1.equals("Concerned") &&  s1.equals("Reasonably Safe")) {
			return "Somewhat Safe";
		}
		if (s1.equals("Concerned") &&  s1.equals("Somewhat Safe")) {
			return "Concerned";
		}
		if (s1.equals("Concerned") &&  s1.equals("Concerned")) {
			return "Concerned";
		}
		else {
			return null;
		}
	}

	private ArrayList<String> calculateMeasurementBoundaries() throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		getCreaValuesAndDates();
		ArrayList<Integer> averageValues = getValueAverages(values);
		ArrayList<Integer> boundaries = runAlgorithm(values, averageValues);
		for (int i = 0; i < boundaries.size(); i++) {
			if (boundaries.get(i) < averageValues.get(i)) {
				result.add("Safe");
			}
			if (boundaries.get(i) < averageValues.get(i)
					&& checkReasonablySafeUpperBound(boundaries, averageValues, i)) {
				result.add("Reasonably Safe");
			}
			if (!checkReasonablySafeUpperBound(boundaries, averageValues, i)
					&& checkSomewhatSafeUpperBound(boundaries, averageValues, i)) {
				result.add("Somewhat Safe");
			}
			if (!checkSomewhatSafeUpperBound(boundaries, averageValues, i)) {
				result.add("Concerned");
			}
		}
		return result;
	}

	private ArrayList<Integer> runAlgorithm(ArrayList<Integer> values, ArrayList<Integer> averageValues) throws SQLException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int count = 0;
		int sum = 0;
		for (int i = 4; i < values.size(); i++) {
			sum = 0;
			for (int j = i; j >= 0; j--) {
				sum += Math.sqrt(values.get(j) - averageValues.get(count));
			}
			count++;
			result.add(sum / 5);
		}
		return result;
	}
	
	private ArrayList<Integer> getValueAverages(ArrayList<Integer> list) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int sum = 0;
		for (int i = 4; i < list.size(); i++) {
			sum = 0;
			for (int j = i; j >= 0; j--) {
				sum += list.get(i);
			}
			result.add(sum / 5);
		}
		return result;
	}
	
	private Boolean checkReasonablySafeUpperBound(ArrayList<Integer> boundaries
			, ArrayList<Integer> averageValues, int index) {
		double a = averageValues.get(index) + boundaries.get(index);
		double b = averageValues.get(index) * 1.15;
		double max = Math.max(a, b);
		return boundaries.get(index) < max;
	}
	
	private boolean checkSomewhatSafeUpperBound(ArrayList<Integer> boundaries,
			ArrayList<Integer> averageValues, int index) {
		double a = averageValues.get(index) + ( 1.5 * boundaries.get(index));
		double b = averageValues.get(index) * 1.25;
		double max = Math.max(a, b);
		return boundaries.get(index) < max;
	}

	@Override
	public ArrayList<Chunk> undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnData(String whereClause)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
