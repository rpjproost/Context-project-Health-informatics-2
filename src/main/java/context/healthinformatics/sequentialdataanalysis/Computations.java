package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;
/**
 * class for computing.
 */
public class Computations extends Task {

	@Override
	public ArrayList<Chunk> undo() {
		for (Chunk c : getChunks()) {
			c.setCompute(false);
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
	
	@Override
	public void run(Query args) throws Exception {
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		setChunks(chunks);
		parseSecondArg(args.next());
		ArrayList<Chunk> list = compute(args.next());
		setResult(list);
	}

	private void parseSecondArg(String next) throws Exception {
		String arg = next.toLowerCase();
		if ("all".equals(arg)) {
			prepareForComputeAll();
		} else if (!"chunk".equals(arg)) {
			throw new Exception("expected : chunk/all, but was: " + next);
		}
		
	}

	private void prepareForComputeAll() {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk temp = new Chunk();
		temp.setChunks(getChunks());
		chunks.add(temp);
		setChunks(chunks);
	}
	
	private ArrayList<Chunk> compute(String column) throws SQLException {
		for (Chunk c : getChunks()) {
			c.initializeDifference(column);
			//c.initializeComputations(column);
		}
		return getChunks();
	}
}
