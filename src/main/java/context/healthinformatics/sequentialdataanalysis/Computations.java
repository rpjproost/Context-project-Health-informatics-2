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
	public void run(Query args) {
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		setChunks(chunks);
		ArrayList<Chunk> list = new ArrayList<Chunk>();
		for (Chunk c : getChunks()) {
			if (c.hasChild()) {
				Chunk res = new Chunk();
				int i = c.getChildren().size();
				res.setSum(i);
				list.add(res);
			}
			else {
				list.add(c);
			}
		}
		setResult(list);
	}

	@Override
	protected ArrayList<Chunk> constraintOnLine(String line) {
		// TODO Auto-generated method stub
		return null;
	}

}
