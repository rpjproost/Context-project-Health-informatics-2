package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;//////////////////////////////////////////////////////////
import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.kreatininestatus.ConcernStatus;
import context.healthinformatics.kreatininestatus.KreatinineStatus;
import context.healthinformatics.kreatininestatus.MildConcernStatus;
import context.healthinformatics.kreatininestatus.NullStatus;
import context.healthinformatics.kreatininestatus.ReasonablySafeStatus;
import context.healthinformatics.kreatininestatus.SafeStatus;

/**
 * Class which determines the advice for patients in the ADMIRE project
 * according to there creatine values.
 */
public class Comparison extends Task {

	private ArrayList<Double> values;
	private ArrayList<Date> dates;
	private ArrayList<String> kreatinine;
	
	private ArrayList<KreatinineStatus> status;
	private ArrayList<KreatinineStatus> boundaries;
	private ArrayList<String> advices;

	/**
	 * Constructor for Comparison.
	 */
	public Comparison() {
		values = new ArrayList<Double>();
		dates = new ArrayList<Date>();
		kreatinine = new ArrayList<String>();
	}

	private void getCreaValuesAndDates() throws SQLException {
		for (Chunk c : getChunks()) {
			ResultSet rs = SingletonDb.getDb().selectResultSet("workspace",
					"value, date, beschrijving", "resultid =" + c.getLine());
			while (rs.next()) {
				values.add(rs.getDouble("value"));
				dates.add(rs.getDate("date"));
				kreatinine.add(rs.getString("beschrijving"));
			}
		}
	}

	/**
	 * Executes a comparison task.
	 * 
	 * @param query
	 *            An array of query words.
	 * @throws Exception
	 *             query input can be wrong.
	 */
	@Override
	public void run(Query query) throws Exception {// //////////////////////////////////////////////////////////////////////
		ArrayList<Chunk> c = SingletonInterpreter.getInterpreter().getChunks();
		// query.inc();
		setChunks(c);

		// handle second part
		getCreaValuesAndDates();

		System.out.println(values.toString());// ////////////////////////////////////////////////////

		ArrayList<String> advices = getAdvice();
		for (int i = 0; i < values.size(); i++) {
			System.out.println(i + "     " + values.get(i) + "     " + boundaries.get(i) + "     " + dates.get(i) + "     " + status.get(i) + "     " + kreatinine.get(i) + "     " + advices.get(i));
		}

		// dates is now filled with unique dates.
		// advices is as long as dates, and it has corresponding advices for
		// that day.

		// output
//		System.out.println(advices.toString());// ///////////////////////////////////////////////////
	}

	/**
	 * STAP 3 Advies en eventuele te nemen actie!
	 * */
	private ArrayList<String> getAdvice() throws SQLException {
		determineCreatineStatus();
		Date currentDate = dates.get(5);
		KreatinineStatus yesterdayStatus = new SafeStatus();
		advices = new ArrayList<String>();
		for (int i = 0; i < status.size(); i++) {
			if (i < 5) {
				advices.add(status.get(i).toString());
			} else {
				if (!dates.get(i).equals(currentDate)) {
					currentDate = dates.get(i);
					yesterdayStatus = getNextStatus(i - 1);
				}
				advices.add(status.get(i).getAdvice(yesterdayStatus));
			}
		}
		System.out.println("advices size:" + advices.size());
		System.out.println("advices: " + advices);
		return advices;// per day
	}
	
	private KreatinineStatus getNextStatus(int index) {
		KreatinineStatus nextStatus = status.get(index);
		if (nextStatus instanceof NullStatus) {
			return getNextStatus(index - 1);
		}
		return nextStatus;
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

	/**
	 * EINDE STAP 3!
	 * */

	/**
	 * STAP 2 KREATININE STATUS!
	 * */

	private ArrayList<KreatinineStatus> determineCreatineStatus() throws SQLException {
		status = new ArrayList<KreatinineStatus>();
		ArrayList<KreatinineStatus> boundaries = calculateMeasurementBoundaries();
		System.out.println("boundaries size: " + boundaries.size());
		System.out.println("boudaries: " + boundaries);
//		System.out.println("Dates size: " + dates.size());
//		System.out.println("Dates: " + dates);
//		System.out.println("Kreatinine size: " + kreatinine.size());
//		System.out.println("Kreatinine: " + kreatinine);
		for (int i = 0; i < boundaries.size() - 1; i++) {
//				System.out.println("Current date: " + dates.get(i) + "; Next Date: " + dates.get(i + 1) 
//						+ "; Boundary: " + boundaries.get(i) + "; Next Boundary: " + boundaries.get(i + 1)
//						+ "; Kreatinine: " + kreatinine.get(i) + "; Next Kreatinine: " + kreatinine.get(i + 1));
//				System.out.println("Dates equals: " + !dates.get(i).equals(dates.get(i + 1))
//						+ "; Kreatinine equals: " + !kreatinine.get(i + 1).equals("Kreatinine2 (stat)"));
			if (!dates.get(i).equals(dates.get(i + 1))
					|| !kreatinine.get(i + 1).equals("Kreatinine2 (stat)")) {
				status.add(boundaries.get(i).getStatus(new NullStatus()));
			} else {
				status.add(boundaries.get(i).getStatus(boundaries.get(i + 1)));
			}
		}
		status.add(boundaries.get(boundaries.size() - 1).getStatus(new NullStatus()));
		System.out.println("status size: " + status.size());
		System.out.println("status: " + status);
		return status;
	}

	/**
	 * EINDE STAP 2!
	 * */

	/**
	 * STAP 1 GRENSGEBIEDEN BEREKENINGEN!
	 * */

	private ArrayList<KreatinineStatus> calculateMeasurementBoundaries()
			throws SQLException {
		boundaries = new ArrayList<KreatinineStatus>();
		ArrayList<Double> averageValues = getValueAverages();
		ArrayList<Double> boundaries = runAlgorithm(averageValues);
		for (int i = 0; i < values.size(); i++) {
			if (i < 5) {
				this.boundaries.add(new NullStatus());
			} else {
				int boundaryIndex = i - 5;
				double boundaryValue = boundaries.get(boundaryIndex);
				double averageValue = averageValues.get(boundaryIndex);
				double currentValue = values.get(i);
				if (currentValue > 0 && currentValue <= averageValue) {
					this.boundaries.add(new SafeStatus());
				}
				if (currentValue > averageValue
						&& currentValue <= checkReasonablySafeUpperBound(
								averageValue, boundaryValue)) {
					this.boundaries.add(new ReasonablySafeStatus());
				}
				if (currentValue > checkReasonablySafeUpperBound(averageValue,
						boundaryValue)
						&& currentValue <= checkSomewhatSafeUpperBound(
								averageValue, boundaryValue)) {
					this.boundaries.add(new MildConcernStatus());
				}
				if (currentValue > checkSomewhatSafeUpperBound(averageValue,
						boundaryValue)) {
					this.boundaries.add(new ConcernStatus());
				}
			}
		}
		// System.out.println("measurements: " + result);
		return this.boundaries;
	} // TODO THIS IS CORRECT AND HANDTESTED!!

	private ArrayList<Double> runAlgorithm(ArrayList<Double> averageValues)
			throws SQLException {

		ArrayList<Double> result = new ArrayList<Double>();
		int count = 0;
		double sum = 0;
		for (int i = 5; i < values.size(); i++) {
			sum = 0;
			for (int j = i - 1; j >= i - 5; j--) {
				// System.out.println("value: " + values.get(j) + " average: " +
				// averageValues.get(count));
				sum += Math.pow(values.get(j) - averageValues.get(count), 2);
			}
			count++;
			result.add(Math.sqrt(sum / 5));
		}
		// System.out.println("algorithm: " + result);
		return result;
	} // TODO THIS IS CORRECT AND HANDTESTED!!

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

	private double checkReasonablySafeUpperBound(double averageValue,
			double boundaryValue) {
		double a = averageValue + boundaryValue;
		double b = averageValue * 1.15;
		double max = Math.max(a, b);
		return max;
	}

	private double checkSomewhatSafeUpperBound(double averageValue,
			double boundaryValue) {
		double a = averageValue + (1.5 * boundaryValue);
		double b = averageValue * 1.25;
		double max = Math.max(a, b);
		return max;
	}
	
	public ArrayList<String> getAdvices() {
		return advices;
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
