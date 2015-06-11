package context.healthinformatics.sequentialdataanalysis;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.SingletonDb;

/**
 * Class which determines the advice for patients in the ADMIRE project
 * according to there creatine values.
 */
public class Comparison extends Task {
	
	/**
	 * Executes a  comparison task.
	 * @param query An array of query words.
	 * @throws Exception query input can be wrong.
	 */
	@Override
	public void run(Query query) throws Exception {
		ArrayList<Chunk> c = SingletonInterpreter.getInterpreter().getChunks();
		query.inc();
		setChunks(c);
		//handle second part
	}
	
	private ArrayList<Integer> getAdvice() {
		
	}
	
	private ArrayList<String> determineCreatineStatus() {
		
	}
	
	private ArrayList<String> calculateMeasurementBoundaries() throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Integer> values = getCreaValues();
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
		for (int i = 5; i < values.size(); i++) {
			sum = 0;
			for (int j = i; j >= 0; j--) {
				sum += Math.sqrt(values.get(j) - averageValues.get(count));
			}
			count++;
			result.add(sum / 5);
		}
		return result;
	}
	
	private ArrayList<Integer> getCreaValues() throws SQLException {
		ResultSet rs = SingletonDb.getDb().selectResultSet("workspace", "value, date", "beschrijving = 'Kreatinine (stat)'");
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		while (rs.next()) {
			values.add(rs.getInt("value"));
			
		}
		return values;
	}
	
	private ArrayList<Integer> getValueAverages(ArrayList<Integer> list) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int sum = 0;
		for (int i = 5; i < list.size(); i++) {
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
