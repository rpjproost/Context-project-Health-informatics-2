package context.healthinformatics.Database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	/**
	 * Test if the xml parser correctly insert the text file with text parser
	 * into the database.
	 * 
	 * @throws IOException
	 *             the IOException
	 * @throws SQLException
	 *             the SQLException
	 */
	@Test
	public void createViewTest() throws IOException, SQLException {
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		MergeTable test = new MergeTable();
		test.mergeTables(clause);
		//data.select("result", "value");
		data.select("HospitalRecords", "groep");
		System.out.println(data.getMaxId("HospitalRecords"));
		data.dropTable("RESULT");
		data.dropTable("StatSensor");
	}

}
