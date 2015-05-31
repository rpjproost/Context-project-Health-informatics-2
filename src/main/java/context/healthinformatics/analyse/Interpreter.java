package context.healthinformatics.analyse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.sequentialdataanalysis.Chunk;
import context.healthinformatics.sequentialdataanalysis.Constraints;

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
				try {
					ArrayList<Chunk> list = c.constraint(split[THREE],
							split[FOUR], split[FIVE]);
					Db data = SingletonDb.getDb();
					for (Chunk chunk : list) {
						ResultSet rs = data.selectAllWithWhereClause(
								"result", "resultid = " + chunk.getLine());
						while (rs.next()) {
							System.out.print("Hospital: ");
							System.out.print(rs.getString("omschrijving"));
							System.out.print("; Date: ");
							System.out.print(rs.getDate("date"));
							System.out.print("; Creatine: ");
							System.out.print(rs.getInt("value"));
							System.out.println();
						}
					}
				} catch (Exception e) {
					System.out.println(e); // TODO catch this exception.
				}
			}
		}
	}

}
