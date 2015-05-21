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
	
	private ArrayList<Chunk> chunks;
	
	/**
	 * constructor for the Interpreter.
	 */
	public Interpreter() {
		MergeTable mt = new MergeTable();
		try {
			String[] clause = new String[1];
			clause[0] = "Hospital.admire = 2";
			mt.merge(clause);
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
				String[] split = methods[i].split(" ");
				Constraints c = new Constraints(chunks);
				try {
					ArrayList<Chunk> list = c.constraint(split[5], split[4], split[3]);
					for (Chunk chunk : list) {
						System.out.println(chunk);
					}
				}
				catch(Exception e) {
					System.out.println("TODO catch this"); //TODO catch this exception.
				}
			}
		}
	}

}
