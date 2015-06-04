package context.healthinformatics.sequentialdataanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
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
	public void run(String[] args) {
		ArrayList<Chunk> list = new ArrayList<Chunk>();
		for (Chunk c : getChunks()) {
			if (c.hasChild()) {
				Chunk res = new Chunk();
				int i = c.getChunks().size();
				res.setSum(i);
			}
			list.add(c);
		}
	}

}
