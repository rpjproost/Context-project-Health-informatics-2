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

	/**
	 * Extra interpreter for computations.
	 * @param args Query from interpreter.
	 * @return Result of computations.
	 * @throws Exception if query is not formatted right.
	 */
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

	/**
	 * Computes value of a column of a chunk.
	 * Average of child chunks or just value of chunk.
	 * @param column column to be computed.
	 * @param name name of computation.
	 * @return list of chunks.
	 * @throws SQLException Column does not exist.
	 */
	private ArrayList<Chunk> computeData(String column, String name) throws SQLException {
		ComputationData.createHashMap(column, name);
		return getChunks();
	}

	/**
	 * Sets all chunks in one chunk to compute average/sum/etc.
	 */
	private void prepareForComputeAll() {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		Chunk temp = new Chunk();
		temp.setChunks(getChunks());
		chunks.add(temp);
		setChunks(chunks);
	}

	/**
	 * Computes average/sum/etc per chunk.
	 * @param column column to be computed.
	 * @return list of computed chunks.
	 * @throws SQLException Column does not exist in database.
	 */
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

	/**
	 * Compute difference per chunk.
	 * @param column column to be computed
	 * @return list of chunks computed.
	 * @throws SQLException Column does not exist in database.
	 */
	private ArrayList<Chunk> computeDif(String column) throws SQLException {
		diff = true;
		for (Chunk c : getChunks()) {
			c.initializeDifference(column);
		}
		return getChunks();
	}
}
