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
	
	private ArrayList<Double> values;
	public ArrayList<Date> dates;
	
	public ArrayList<String> advices;////////////////////
	
	private String safe = "Safe";
	private String reasonable = "Reasonable Safe";
	private String mild = "Mild Concern";
	private String concern = "Concern";
	
	/**
	 * Constructor for Comparison.
	 */
	public Comparison() {
		values = new ArrayList<Double>();
		dates = new ArrayList<Date>();
		
		advices = new ArrayList<String>();//////////////////
	}
	
	private void getCreaValuesAndDates() throws SQLException {
		for (Chunk c : getChunks()) {
			ResultSet rs = SingletonDb.getDb().selectResultSet("workspace"
					, "value, date", "resultid =" + c.getLine());
			while (rs.next()) {
				values.add(rs.getDouble("value"));
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
		
//		assertEquals(dates.size(), advices.size());//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// dates is now filled with unique dates.
		// advices is as long as dates, and it has corresponding advices for that day.
		
		//output
		System.out.println(advices.toString());/////////////////////////////////////////////////////
	}
	
	
	/**
	 * STAP 3 Advies en eventuele te nemen actie!
	 * */
	private ArrayList<String> getAdvice() throws SQLException {
		ArrayList<String> statusList = determineCreatineStatus();
		removeDuplicateAndIrrelevantDates();
		
//		assertEquals(dates.size(), statusList.size());//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		ArrayList<String> result = new ArrayList<String>();
		result.add("no advice");
		for (int i = 1; i < statusList.size(); i++) {
			String status = resolveStatus(statusList.get(i - 1), statusList.get(i));
			result.add(status);
		}
		return result;//per day
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
	
	private String resolveStatus(String s1, String s2) {
		if (s1.equals(reasonable) &&  s2.equals(reasonable)) {
			return "Do Nothing";
		}
		if (s1.equals(reasonable) &&  s2.equals(mild)) {
			return "Repeat measurement tommorow";
		}
		if (s1.equals(reasonable) &&  s2.equals(concern)) {
			return "Contact Hospital";
		}
		if (s1.equals(mild) &&  s2.equals(safe)) {
			return "Do Nothing";
		}
		if (s1.equals(mild) &&  s2.equals(reasonable)) {
			return "Contact Hospital";
		}
		if (s1.equals(concern)) {
			return "Follow doctors advice";
		}
		else {
			return null;
		}
	}
	
	/**
	 * EINDE STAP 3!
	 * */
	
	/**
	 * STAP 2 KREATININE STATUS!
	 * */
	
	private ArrayList<String> determineCreatineStatus() throws SQLException { //TODO hier ben ik gebleven!!!
		ArrayList<String> result = new ArrayList<String>();//correspondeert met dates waar alle dubble uit zijn gehaald.
		ArrayList<String> boundaries =  calculateMeasurementBoundaries();
//		result.add(boundaries.get(0));// de eerste heeft geen voorganger dus die gaat er gewoon in.
		int count = 1;// de eerste zit er in dus je begint met terugkijken bij de tweede.
		Date currentDate = dates.get(5);
		String currentBoundary = boundaries.get(0);
		System.out.println("boundaries size: " + boundaries.size());
		System.out.println("boudaries: " + boundaries);
		System.out.println("Dates size: " + dates.size());
		System.out.println("Dates: " + dates);
		for (int i = 6; i < dates.size(); i++) {
			if (!currentDate.equals(dates.get(i))) {
				result.add(currentBoundary);
				currentBoundary = boundaries.get(count);
				currentDate = dates.get(i);
				count++;
			}
			else {
				while (currentDate.equals(dates.get(i))) {
					i++;
					currentBoundary = resolveBoundaries(boundaries.get(count - 1), boundaries.get(count));
					count++;
				}
			}
			System.out.println("Current date: " + dates.get(i) + "; Last Date: " + dates.get(i - 1) + "; Value: " + values.get(i) + "; Boundary: " + boundaries.get(count));
		}
		System.out.println("result size: " + result.size());
		System.out.println("result: " + result);
		return result;
	}
	
	public String resolveBoundaries(String s1, String s2) {
		if (s1.equals(safe)) {
			return safe;
		}
		if (s1.equals(reasonable)) {
			return reasonable;
		}
		if (s1.equals(mild) &&  s2.equals(safe)) {
			return safe;
		}
		if (s1.equals(mild) &&  s2.equals(reasonable)) {
			return reasonable;
		}
		if (s1.equals(mild) &&  s2.equals(mild)) {
			return mild;
		}
		if (s1.equals(mild) &&  s2.equals(concern)) {
			return concern;
		}
		if (s1.equals(concern) &&  s2.equals(safe)) {
			return reasonable;
		}
		if (s1.equals(concern) &&  s2.equals(reasonable)) {
			return mild;
		}
		if (s1.equals(concern) &&  s2.equals(reasonable)) {
			return concern;
		}
		if (s1.equals(concern) &&  s2.equals(concern)) {
			return concern;
		}
		else {
			return null;
		}
	}
	
	/**
	 * EINDE STAP 2!
	 * */
	
	/**
	 * STAP 1 GRENSGEBIEDEN BEREKENINGEN!
	 * */

	private ArrayList<String> calculateMeasurementBoundaries() throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		//getCreaValuesAndDates();
		ArrayList<Double> averageValues = getValueAverages();
		ArrayList<Double> boundaries = runAlgorithm(averageValues);
//		System.out.println("should be size: " + boundaries.size());
//		System.out.println("values size: " + values.size());
//		assertEquals(averageValues.size(), boundaries.size());////////////////////////////////////////////////////////////////////////////////////////////
		
		for (int i = 5; i < values.size(); i++) {
			int boundaryIndex = i - 5;
			double boundaryValue = boundaries.get(boundaryIndex);
			double averageValue = averageValues.get(boundaryIndex);
			double currentValue = values.get(i);
			if (currentValue > 0 && currentValue <= averageValue) {
//				System.out.println("value: " + i + "; currentValue: " + currentValue + "; averageValue: " + averageValue + "; boundaryValue: " + boundaryValue + "; Result: " + safe);
				result.add(safe);
			}
			if (currentValue > averageValue 
					&& currentValue <= checkReasonablySafeUpperBound(averageValue, boundaryValue)) {
//				System.out.println("value: " + i + "; currentValue: " + currentValue + "; averageValue: " + averageValue + "; boundaryValue: " + boundaryValue + "; Upperbound: " + checkReasonablySafeUpperBound(averageValue, boundaryValue) + "; Result: " + reasonable);
				result.add(reasonable);
			}
			if (currentValue > checkReasonablySafeUpperBound(averageValue, boundaryValue)
					&& currentValue <= checkSomewhatSafeUpperBound(averageValue, boundaryValue)) {
//				System.out.println("value: " + i + "; currentValue: " + currentValue + "; averageValue: " + averageValue + "; boundaryValue: " + boundaryValue + "; Upperbound1: " + checkReasonablySafeUpperBound(averageValue, boundaryValue) + "; Upperbound2: " + checkSomewhatSafeUpperBound(averageValue, boundaryValue) + "; Result: " + mild);
				result.add(mild);
			}
			if (currentValue > checkSomewhatSafeUpperBound(averageValue, boundaryValue)) {
//				System.out.println("value: " + i + "; currentValue: " + currentValue + "; averageValue: " + averageValue + "; boundaryValue: " + boundaryValue + "; Upperbound: " + checkSomewhatSafeUpperBound(averageValue, boundaryValue) + "; Result: " + concern);
				result.add(concern);
			}
		}
//		System.out.println("size of measurements: " + result.size());
//		System.out.println("measurements: " + result);
		return result;
	} //TODO THIS IS CORRECT AND HANDTESTED!!

	private ArrayList<Double> runAlgorithm(ArrayList<Double> averageValues) throws SQLException {
		
		//assertEquals(values.size() - 5, averageValues.size());////////////////////////////////////////////////////////////////////////////////////////////
		ArrayList<Double> result = new ArrayList<Double>();
		int count = 0;
		double sum = 0;
		for (int i = 5; i < values.size(); i++) {
			sum = 0;
			for (int j = i - 1; j >= i - 5; j--) {
//				System.out.println("value: " + values.get(j) + " average: " + averageValues.get(count));
				sum += Math.pow(values.get(j) - averageValues.get(count), 2);
			}
			count++;
			result.add(Math.sqrt(sum / 5));
		}
//		System.out.println("algorithm: " + result);
		return result;
	} //TODO THIS IS CORRECT AND HANDTESTED!!
	
	private ArrayList<Double> getValueAverages() {
		ArrayList<Double> result = new ArrayList<Double>();
		double sum = 0;
		for (int i = 5; i < values.size(); i++) {
			sum = 0;
			for (int j = i - 1; j >= i - 5; j--) {
				sum += values.get(j);
			}
			result.add(sum / 5);
		}
		return result;
	} // TODO THIS IS CORRECT AND HANDTESTED!!
	
	private double checkReasonablySafeUpperBound(double averageValue, double boundaryValue) {
		double a = averageValue + boundaryValue;
		double b = averageValue * 1.15;
		double max = Math.max(a, b);
		return max;
	}
	
	private double checkSomewhatSafeUpperBound(double averageValue, double boundaryValue) {
		double a = averageValue + ( 1.5 * boundaryValue);
		double b = averageValue * 1.25;
		double max = Math.max(a, b);
		return max;
	}
	
	/**
	 * EINDE STAP 1!
	 * */
	 

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
