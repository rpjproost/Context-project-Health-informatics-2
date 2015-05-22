package context.healthinformatics.Analyse;

import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.Database.MergeTable;
import context.healthinformatics.Database.SingletonDb;
import context.healthinformatics.SequentialDataAnalysis.Chunk;
import context.healthinformatics.SequentialDataAnalysis.Constraints;

/**
 * class handeling the interpreting of the user input.
 */
// TODO needs to be cleaned!
public class Interpreter {

	private ArrayList<Chunk> chunks;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;

	/**
	 * constructor for the Interpreter.
	 */
	public Interpreter() {
		MergeTable mt = new MergeTable();
		try {
			String[] clause = new String[1];
			clause[0] = "Hospital.admire = 2";
			mt.merge(clause);
			chunks = mt.getChunks();
		} catch (SQLException e) {
			e.printStackTrace();
		} // TODO add clause
	}

	/**
	 * method for interpreting the things to do.
	 * 
	 * @param code
	 *            code to interpret.
	 */
	public void interpret(String code) {
		String[] methods = code.split(";");
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].contains("filter")) {
				String[] split = methods[i].split(" ");

				Constraints c = new Constraints(chunks, "date");
				System.out.println("value: " + split[THREE]);
				System.out.println("operator: " + split[FOUR]);
				System.out.println("table: " + split[FIVE]);
				System.out.println(SingletonDb.getDb().getTables());
				try {
					ArrayList<Chunk> list = c.constraint(split[THREE],
							split[FOUR], split[FIVE]);
					for (Chunk chunk : list) {
						System.out.println(chunk);
					}
				} catch (Exception e) {
					System.out.println(e); // TODO catch this exception.
				}
			}
		}
	}

}
