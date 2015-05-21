package context.healthinformatics.Analyse;

import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.Database.MergeTable;
import context.healthinformatics.SequentialDataAnalysis.Chunk;
import context.healthinformatics.SequentialDataAnalysis.Constraints;

/**
 * class handeling the interpreting of the user input.
 */
//TODO needs to be cleaned!
public class Interpreter {
	ArrayList<Chunk> chunks;
	
	/**
	 * constructor for the Interpreter.
	 */
	protected Interpreter() {
		MergeTable mt = new MergeTable();
		try {
			mt.merge(" , ".split(","));
		} catch (SQLException e) {
			e.printStackTrace();
		} //TODO add clause
	}
	
	/**
	 * method for interpreting the things to do.
	 * @param code code to interpret.
	 */
	public void interpret(String code) {
		String[] methods = code.split(";");
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].contains("filter")) {
				Constraints c = new Constraints(chunks);
			}
		}
	}

}
