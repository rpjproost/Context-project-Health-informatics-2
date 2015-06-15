package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;//////////////////////////////////////////////////////////

import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.SingletonDb;

/**
 * Class which determines the advice for patients in the ADMIRE project
 * according to there creatine values.
 */
public class Comparison extends Task {
	
	private ArrayList<Integer> values;
	public ArrayList<Date> dates;
	
	public ArrayList<String> advices;////////////////////
	
	/**
	 * Constructor for Comparison.
	 */
	public Comparison() {
		values = new ArrayList<Integer>();
		dates = new ArrayList<Date>();
		
		advices = new ArrayList<String>();//////////////////
	}
	
	private void getCreaValuesAndDates() throws SQLException {
		values = new ArrayList<Integer>();
		dates = new ArrayList<Date>();
		for (Chunk c : getChunks()) {
			ResultSet rs = SingletonDb.getDb().selectResultSet("workspace"
					, "value, date", "resultid =" + c.getLine());
			while (rs.next()) {
				values.add(rs.getInt("value"));
				dates.add(rs.getDate("date"));
			}
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
		//query.inc();
		setChunks(c);
		
		//handle second part
		getCreaValuesAndDates();
		
		System.out.println(values.toString());//////////////////////////////////////////////////////
		
		ArrayList<String> advices = getAdvice();
		
		assertEquals(dates.size(), advices.size());//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// dates is now filled with unique dates.
		// advices is as long as dates, and it has corresponding advices for that day.
		
		//output
		System.out.println(advices.toString());/////////////////////////////////////////////////////
	}
	
	private ArrayList<String> getAdvice() throws SQLException {
		ArrayList<String> statusList = determineCreatineStatus();
		removeDuplicateAndIrrelevantDates();
		
		assertEquals(dates.size(), statusList.size());//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		ArrayList<String> result = new ArrayList<String>();
		result.add("no advice");
		for (int i = 1; i < statusList.size(); i++) {
			String status = resolveStatus(statusList.get(i - 1), statusList.get(i));
			result.add(status);
		}
		return result;//per day
	}
	
	private String resolveStatus(String s1, String s2) {
		if (s1.equals("Reasonably Safe") &&  s2.equals("Reasonably Safe")) {
			return "Do Nothing";
		}
		if (s1.equals("Reasonably Safe") &&  s2.equals("Somewhat Safe")) {
			return "Repeat measurement tommorow";
		}
		if (s1.equals("Reasonably Safe") &&  s2.equals("Concerned")) {
			return "Contact Hospital";
		}
		if (s1.equals("Somewhat Safe") &&  s2.equals("Safe")) {
			return "Do Nothing";
		}
		if (s1.equals("Somewhat Safe") &&  s2.equals("Reasonably Safe")) {
			return "Contact Hospital";
		}
		if (s1.equals("Concerned")) {
			return "Follow doctors advice";
		}
		else {
			return null;
		}
	}
	
	private ArrayList<String> determineCreatineStatus() throws SQLException {
		ArrayList<String> result = new ArrayList<String>();//correspondeert met dates waar alle dubble uit zijn gehaald.
		ArrayList<String> boundaries =  calculateMeasurementBoundaries();
		result.add(boundaries.get(0));// de eerste heeft geen voorganger dus die gaat er gewoon in.
		int count = 6;// de eerste zit er in dus je begint met terugkijken bij de tweede.
		while (count < dates.size()) {
			if (!dates.get(count - 1).equals(dates.get(count))) {
				result.add(boundaries.get(count));
				count++;
			}
			else {
				while (dates.get(count - 1).equals(dates.get(count))) {
					count++;
				}
				result.add(resolveBoundaries(boundaries.get(count - 1), boundaries.get(count)));
				count++;
			}
		}
		return result;
	}
	
	private void removeDuplicateAndIrrelevantDates() {
		ArrayList<Date> list = new ArrayList<Date>();
		Date d = dates.get(5);// begin mij 5de meting
		list.add(d);
		for (int i = 6; i < dates.size(); i++) {
			Date curDate = dates.get(i);
			if (!d.equals(curDate)) {
				list.add(curDate);
				d = curDate;
			}
		}
		dates = list;
	}
	
	public String resolveBoundaries(String s1, String s2) {
		if (s1.equals("Safe")) {
			return "Safe";
		}
		if (s1.equals("Reasonably Safe")) {
			return "Reasonably Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s2.equals("Safe")) {
			return "Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s2.equals("Reasonably Safe")) {
			return "Reasonably Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s2.equals("Somewhat Safe")) {
			return "Somewhat Safe";
		}
		if (s1.equals("Somewhat Safe") &&  s2.equals("Concerned")) {
			return "Concerned";
		}
		if (s1.equals("Concerned") &&  s2.equals("Safe")) {
			return "Reasonably Safe";
		}
		if (s1.equals("Concerned") &&  s2.equals("Reasonably Safe")) {
			return "Somewhat Safe";
		}
		if (s1.equals("Concerned") &&  s2.equals("Somewhat Safe")) {
			return "Concerned";
		}
		if (s1.equals("Concerned") &&  s2.equals("Concerned")) {
			return "Concerned";
		}
		else {
			return null;
		}
	}

	private ArrayList<String> calculateMeasurementBoundaries() throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		//getCreaValuesAndDates();
		ArrayList<Integer> averageValues = getValueAverages();
		ArrayList<Integer> boundaries = runAlgorithm(averageValues);
		
		assertEquals(averageValues.size(), boundaries.size());////////////////////////////////////////////////////////////////////////////////////////////
		
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

	private ArrayList<Integer> runAlgorithm( ArrayList<Integer> averageValues) throws SQLException {
		
		//assertEquals(values.size() - 5, averageValues.size());////////////////////////////////////////////////////////////////////////////////////////////
		ArrayList<Integer> result = new ArrayList<Integer>();
		int count = 0;
		int sum = 0;
		for (int i = 4; i < values.size(); i++) {
			sum = 0;
			for (int j = i; j >= i - 4; j--) {
				sum += Math.sqrt(values.get(j) - averageValues.get(count));
			}
			count++;
			result.add(sum / 5);
		}
		return result;
	}
	
	private ArrayList<Integer> getValueAverages() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int sum = 0;
		for (int i = 4; i < values.size(); i++) {
			sum = 0;
			for (int j = i; j >= i - 4; j--) {
				sum += values.get(j);
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
