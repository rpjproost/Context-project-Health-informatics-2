package context.healthinformatics.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Test;

import context.healthinformatics.parser.XMLParser;
import context.healthinformatics.sequentialdataanalysis.Chunk;

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

	//used for cleaning up;
		private Set<ResultSet> results;
		private Set<String> tables;
		
		/**
		 * method preparing for environment for tests.
		 */
		@org.junit.Before
		public void before() {
			tables = new TreeSet<String>();
			results = new HashSet<ResultSet>();
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
		ResultSet rs = getResult("result", "value");
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
		tables.add("HospitalRecords");
		tables.add("StatSensor");
		xmlp.parse();
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		MergeTable test = new MergeTable();
		tables.add("result");
		test.mergeTables(clause);
		ResultSet rs = getResult("result", "date");
		while (rs.next()) {
			assertNotNull(rs.getDate("date"));
		}
		test.dropView("workspace");
	}

	/**
	 * Tests if view is being created sorted by date.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 */
	@Test
	public void mergeViewTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		tables.add("HospitalRecords");
		tables.add("StatSensor");
		xmlp.parse();
		
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		
		MergeTable test = new MergeTable();
		data.dropTable("result");
		test.mergeTables(clause);
		test.mergeTablesView();
		
		ResultSet rs = getResult("workspace", "date");
		
		orderedByDate(rs);

		test.dropView("workspace");
	}
	
	/**
	 * Tests if view is ordered by date.
	 * @param rs resultset of view.
	 * @throws SQLException if resultset is empty.
	 */
	public void orderedByDate(ResultSet rs) throws SQLException {
		Date date = null;
		int counter = 0;
		while (rs.next()) {
			if (counter == 0) {
				assertNotNull(rs.getDate("date"));
				date = rs.getDate("date");
				counter++;
			}
			else {
				assertNotNull(rs.getDate("date"));
				Date temp = rs.getDate("date");
				if (date.equals(temp)) {
					assertEquals(date, temp);
				}
				else {
					assertTrue(temp.after(date));
				}
				date = temp;
				counter++;
			}
		}
		final int res = 10;
		assertEquals(counter, res);
		rs.close();
	}
	
	/**
	 * Tests the full merge method.
	 * @throws Exception 
	 */
	@Test
	public void mergeTest() throws Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		tables.add("HospitalRecords");
		tables.add("StatSensor");
		xmlp.parse();
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		MergeTable test = new MergeTable();
		tables.add("result");
		test.merge(clause);
		
		ResultSet rs = getResult("workspace", "date");
		
		orderedByDate(rs);
		
		test.dropView("workspace");
	}
	
	/**
	 * Tests for appendTables.
	 */
	@Test 
	public void appendTest() {
		MergeTable test = new MergeTable();
		Set<String> tables = new TreeSet<String>();
		StringBuilder sql = new StringBuilder();
		sql.append("tables = ");
		tables.add("test1");
		tables.add("test2");
		test.appendTables(tables, sql);
		assertEquals(sql.toString(), "tables = test1, test2");
	}
	
	/**
	 * Test for creating chunks from ordered view.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 */
	@Test
	public void testChunkArray() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		tables.add("HospitalRecords");
		tables.add("StatSensor");
		xmlp.parse();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		tables.add("result");
		test.merge(clause);
		ArrayList<Chunk> chunks = test.getChunks();
		for (Chunk c : chunks) {
			assertTrue(c.getLine() > 0);
		}
		test.dropView("workspace");
	}
	
	/**
	 * Retrieves the correct result set from the database.
	 * @param table table to get the result set from.
	 * @param column column to get the result set for
	 * @return the retrieved result set.
	 * @throws SQLException might throw an SQL exception if table does not exist.
	 */
	private ResultSet getResult(String table, String column) throws SQLException {
		ResultSet res = data.selectResultSet(table, column, "");
		results.add(res);
		return res;
	}
	
	/**
	 * method that cleans up after a test.
	 * @throws SQLException if something goes wrong closing resultsets of dropping tables.
	 */
	@After
	public void after() throws SQLException {
		for (ResultSet r : results) {
			r.close();
		}
		for (String table : tables) {
			data.dropTable(table);
		}
	}


}
