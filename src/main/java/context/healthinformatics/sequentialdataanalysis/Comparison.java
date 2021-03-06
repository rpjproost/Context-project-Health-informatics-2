package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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

	private static final int FIVE = 5;
	
	private ArrayList<Double> values;
	private ArrayList<Date> dates;
	private ArrayList<String> creatine;
	private ArrayList<String> remeasures;
	
	private ArrayList<KreatinineStatus> status;
	private ArrayList<KreatinineStatus> borderAreas;
	private ArrayList<String> advices;

	private Date currentDate;
	private KreatinineStatus yesterdayStatus;
	private String runOn;
	
	private ArrayList<String> oldComments;

	/**
	 * Constructor for Comparison.
	 */
	public Comparison() {
		values = new ArrayList<Double>();
		dates = new ArrayList<Date>();
		creatine = new ArrayList<String>();
		remeasures = new ArrayList<String>();
		oldComments = new ArrayList<String>();
	}

	private void getCreaValuesAndDates() throws SQLException {
		for (Chunk c : getChunks()) {
			ResultSet rs = SingletonDb.getDb().selectResultSet("workspace",
					runOn + ", date, beschrijving", "resultid =" + c.getLine());
			while (rs.next()) {
				values.add(rs.getDouble(runOn));
				dates.add(rs.getDate("date"));
				creatine.add(rs.getString("beschrijving"));
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
	public void run(Query query) throws Exception {
		query.inc(2);
		String parameter = query.next();
		Task filter = new Constraints();
		filter.setChunks(SingletonInterpreter.getInterpreter().getChunks());
		ArrayList<Chunk> filterResult = filter.constraintOnCode(parameter);
		setChunks(filterResult);
		query.inc();
		runOn = query.next();
		getCreaValuesAndDates();
		getAdvice();
		setAdviceAsComment();
		setResult(SingletonInterpreter.getInterpreter().getChunks());
	}

	private void setAdviceAsComment() {
		ArrayList<Chunk> chunks = getChunks();
		String splitter = "; ";
		for (int i = 0; i < values.size(); i++) {
			oldComments.add(chunks.get(i).getComment());
			StringBuilder builder = new StringBuilder();
			builder.append(borderAreas.get(i) + splitter);
			builder.append(remeasures.get(i) + splitter);
			builder.append(status.get(i) + splitter);
			builder.append(advices.get(i));
			chunks.get(i).setComment(builder.toString());
		} 
	}

	/**
	 * STAP 3 Advies en eventuele te nemen actie!
	 * */
	private void getAdvice() {
		determineCreatineStatus();
		currentDate = dates.get(FIVE);
		yesterdayStatus = new SafeStatus();
		advices = new ArrayList<String>();
		for (int i = 0; i < status.size(); i++) {
			if (i < FIVE) {
				advices.add(status.get(i).toString());
			} else {
				if (!dates.get(i).equals(currentDate)) {
					yesterdayStatus = getNextStatus(i - 1);
					currentDate = dates.get(i);
				}
				advices.add(status.get(i).getAdvice(yesterdayStatus));
			}
		}
	}
	
	private KreatinineStatus getNextStatus(int index) {
		KreatinineStatus nextStatus = status.get(index);
		if (nextStatus.toString().equals("NULL")) {
			if (currentDate.equals(dates.get(index))) {
				return getNextStatus(index - 1);
			} else {
				return yesterdayStatus;
			}
		}
		return nextStatus;
	}

	/**
	 * EINDE STAP 3!
	 * */

	/**
	 * STAP 2 KREATININE STATUS!
	 * */

	private void determineCreatineStatus() {
		status = new ArrayList<KreatinineStatus>();
		calculateBorderAreaForCreatine();
		generateCreatineDescription();
		for (int i = 0; i < borderAreas.size() - 1; i++) {
			if (!dates.get(i).equals(dates.get(i + 1))
					|| !creatine.get(i + 1).equals("Kreatinine2 (stat)")) {
				status.add(borderAreas.get(i).getStatus(new NullStatus()));
			} else {
				status.add(borderAreas.get(i).getStatus(borderAreas.get(i + 1)));
			}
			addReMeasurementsNeeded(i);
		}
		status.add(borderAreas.get(borderAreas.size() - 1).getStatus(new NullStatus()));
		addReMeasurementsNeeded(borderAreas.size() - 1);
	}
	
	private void addReMeasurementsNeeded(int index) {
		if (creatine.get(index).equals("Kreatinine2 (stat)")) {
			remeasures.add(new NullStatus().needToReMeasure());
		} else {
			remeasures.add(borderAreas.get(index).needToReMeasure());
		}
	}
	
	private void generateCreatineDescription() {
		for (int i = 0; i < creatine.size() - 1; i++) {
			KreatinineStatus currentStatus = borderAreas.get(i);
			if ((currentStatus instanceof MildConcernStatus 
					|| currentStatus instanceof ConcernStatus) 
					&& dates.get(i).equals(dates.get(i + 1))
					&& !creatine.get(i + 1).contains("(stat)")) {
				creatine.set(i + 1, "Kreatinine2 (stat)");
				i++;
			}
		}
	}

	/**
	 * EINDE STAP 2!
	 * */


	/**
	 * STAP 1 GRENSGEBIEDEN BEREKENINGEN!
	 * */

	private void calculateBorderAreaForCreatine() {
		borderAreas = new ArrayList<KreatinineStatus>();
		ArrayList<Double> averageValues = getValueAverages();
		ArrayList<Double> boundaries = runAlgorithm(averageValues);
		for (int i = 0; i < values.size(); i++) {
			if (i < FIVE) {
				this.borderAreas.add(new NullStatus());
			} else {
				int boundaryIndex = i - FIVE;
				double boundaryValue = boundaries.get(boundaryIndex);
				double averageValue = averageValues.get(boundaryIndex);
				double currentValue = values.get(i);
				setBorderArea(currentValue, averageValue, boundaryValue);
			}
		}
	}

	private void setBorderArea(double currentValue, double averageValue, double boundaryValue) {
		if (currentValue > 0 && currentValue <= averageValue) {
			borderAreas.add(new SafeStatus());
		}
		if (currentValue > averageValue
				&& currentValue <= checkReasonablySafeUpperBound(
						averageValue, boundaryValue)) {
			borderAreas.add(new ReasonablySafeStatus());
		}
		if (currentValue > checkReasonablySafeUpperBound(averageValue,
				boundaryValue)
				&& currentValue <= checkMildConcernUpperBound(
						averageValue, boundaryValue)) {
			borderAreas.add(new MildConcernStatus());
		}
		if (currentValue > checkMildConcernUpperBound(averageValue,
				boundaryValue)) {
			borderAreas.add(new ConcernStatus());
		}
	}

	private ArrayList<Double> runAlgorithm(ArrayList<Double> averageValues) {

		ArrayList<Double> result = new ArrayList<Double>();
		int count = 0;
		double sum = 0;
		for (int i = FIVE; i < values.size(); i++) {
			sum = 0;
			for (int j = i - 1; j >= i - FIVE; j--) {
				sum += Math.pow(values.get(j) - averageValues.get(count), 2);
			}
			count++;
			result.add(Math.sqrt(sum / FIVE));
		}
		return result;
	}

	private ArrayList<Double> getValueAverages() {
		ArrayList<Double> result = new ArrayList<Double>();
		double sum = 0;
		for (int i = FIVE; i < values.size(); i++) {
			sum = 0;
			for (int j = i - 1; j >= i - FIVE; j--) {
				sum += values.get(j);
			}
			result.add(sum / FIVE);
		}
		return result;
	}

	private double checkReasonablySafeUpperBound(double averageValue, double boundaryValue) {
		final double maxUpperPercentage = 1.15;
		double a = averageValue + boundaryValue;
		double b = averageValue * maxUpperPercentage;
		return Math.max(a, b);
	}

	private double checkMildConcernUpperBound(double averageValue,	double boundaryValue) {
		final double maxUpperBoundary = 1.5;
		final double maxUpperPercentage = 1.25;
		double a = averageValue + (maxUpperBoundary * boundaryValue);
		double b = averageValue * maxUpperPercentage;
		return Math.max(a, b);
	}
	
	/**
	 * @return the list of all advices.
	 */
	public ArrayList<String> getAdvices() {
		return advices;
	}

	/**
	 * EINDE STAP 1!
	 * */

	@Override
	public ArrayList<Chunk> undo() {
		for (int i = 0; i < oldComments.size(); i++) {
			getChunks().get(i).setComment(oldComments.get(i));
		}
		return null;
	}

	@Override
	protected ArrayList<Chunk> constraintOnData(String whereClause)
			throws SQLException {
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnCode(String code) {
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnEqualsComment(String comment) {
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnContainsComment(String comment) {
		return getChunks();
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		return getChunks();
	}

}
