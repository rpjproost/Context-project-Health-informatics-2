package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;
/**
 * class for computing.
 */
public class Computations extends Task {
	private boolean comp = false;
	private boolean diff = false;

	@Override
	public ArrayList<Chunk> undo() {
		if (comp) {
			for (Chunk c : getChunks()) {
				c.setCompute(false);
			}
		} else if (diff) {
			for (Chunk c : getChunks()) {
				c.undoDifference();
			}
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
		args.inc();
		ArrayList<Chunk> list = parseSecondArg(args);
		setResult(list);
	}

	private ArrayList<Chunk> parseSecondArg(Query args) throws Exception {
		String arg = args.part().toLowerCase();
		args.inc();
		if ("all".equals(arg)) {
			prepareForComputeAll();
		} else if ("difference".equals(arg)) {
			return computeDif(args.part());
		}
		else if ("data".equals(arg)) {
			return computeData(args.part(), args.next());
		}
		else if (!"chunk".equals(arg)) {
			throw new Exception("expected : chunk/all/difference, but was: " + arg);
		}
		return  compute(args.part());

	}

	private ArrayList<Chunk> computeData(String column, String name) throws SQLException {
		ComputationData.createHashMap(column, name);
		return getChunks();
	}

	private void prepareForComputeAll() {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk temp = new Chunk();
		temp.setChunks(getChunks());
		chunks.add(temp);
		setChunks(chunks);
	}

	private ArrayList<Chunk> compute(String column) throws SQLException {
		comp = true;
		if (column.equals("times")) {
			ComputationData.createHashMap(column, "times");
			return getChunks();
		}
		else {
			for (Chunk c : getChunks()) {
				c.initializeComputations(column);
			}
			return getChunks();
		}
	}

	private ArrayList<Chunk> computeDif(String column) throws SQLException {
		diff = true;
		for (Chunk c : getChunks()) {
			c.initializeDifference(column);
		}
		return getChunks();
	}
}
