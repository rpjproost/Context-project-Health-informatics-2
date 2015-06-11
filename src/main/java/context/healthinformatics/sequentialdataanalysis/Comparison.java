package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;

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
		setResult(getKreaStatData());
	}
	
	private ArrayList<Chunk> getKreaStatData() throws SQLException {
		Constraints c = new Constraints(getChunks());
		System.out.println("get krea");
		return c.constraintOnData("beschrijving = 'Kreatinine (stat)'");
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
