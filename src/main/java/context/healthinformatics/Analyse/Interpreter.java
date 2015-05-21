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
	
	/**
	 * method for interpreting the things to do.
	 * @param code code to interpret.
	 */
	public void interpret(String code) {
		//TODO auto-generated method stub.
	}

	/**
	 * getter for the hashmap containing all the tables.
	 * @return al the hahsmap with tables.
	 */
	public HashMap<String, ArrayList<Column>> getTables() {
		return tables;
	}

	/**
	 * setter for the hashmap with tables.
	 * @param tables hashmap to set.
	 */
	public void setTables(HashMap<String, ArrayList<Column>> tables) {
		this.tables = tables;
	}
}
