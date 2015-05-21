package context.healthinformatics.Database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;

import context.healthinformatics.Parser.XMLParser;

public class MergeTableTest {

	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/xml/";

	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();
	
	/**
	 * method preparing for environment for tests.
	 */
	@org.junit.Before
	public void before() {
		try {
			for (String key : data.getTables().keySet()) {
				data.dropTable(key);
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong preparing db for tests.");
		}
	}


}
