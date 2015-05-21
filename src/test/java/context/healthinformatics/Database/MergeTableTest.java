package context.healthinformatics.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import context.healthinformatics.Parser.XMLParser;

/**
 * 
 * Tests for mergeTable class.
 *
 */
public class MergeTableTest {

	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/mergeTableFiles/";

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
	 * Test for merging two tables with condition.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 */
	@Test
	public void mergeTableTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		final int res = 209;
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		MergeTable test = new MergeTable();
		test.mergeTables(clause);
		ResultSet rs = data.selectResultSet("result", "value");
		while (rs.next()) {
			if (rs.getInt("value") == 0) {
				assertEquals((rs.getInt("value")), 0);
			}
			else {
				assertEquals((rs.getInt("value")), res);
			}
		}
		rs.close();
		data.dropTable("result");
		data.dropTable("HospitalRecords");
		data.dropTable("StatSensor");
	}
	
	/**
	 * Test for merging two tables with one date column.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 */
	@Test
	public void checkDateTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		MergeTable test = new MergeTable();
		test.mergeTables(clause);
		ResultSet rs = data.selectResultSet("result", "date");
		while (rs.next()) {
			assertNotNull(rs.getDate("date"));
		}
		rs.close();
		data.dropTable("result");
		data.dropTable("HospitalRecords");
		data.dropTable("StatSensor");
	}
	
	@Test
	public void mergeViewTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		MergeTable test = new MergeTable();
		test.mergeTables(clause);
		test.mergeTablesView();
		ResultSet rs = data.selectResultSet("workspace", "date");
		while (rs.next()) {
			assertNotNull(rs.getDate("date"));
		}
		rs.close();
		test.dropView("workspace");
		data.dropTable("result");
		data.dropTable("HospitalRecords");
		data.dropTable("StatSensor");
	}


}