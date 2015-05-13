package context.healthinformatics.Analyse;

import java.util.HashMap;
import java.util.ArrayList;

import context.healthinformatics.Parser.Column;

/**
 * class handeling the interpreting of the user input.
 */
public class Interpreter {
	/**
	 * map with table names linked to collumnArrays.
	 */
	private HashMap<String, ArrayList<Column>> tables;
	
	/**
	 * constructor for the Interpreter.
	 */
	protected Interpreter() {
		tables = new HashMap<String, ArrayList<Column>>();
	}

	public HashMap<String, ArrayList<Column>> getTables() {
		return tables;
	}

	public void setTables(HashMap<String, ArrayList<Column>> tables) {
		this.tables = tables;
	}
}
