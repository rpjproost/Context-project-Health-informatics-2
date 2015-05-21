package context.healthinformatics.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import context.healthinformatics.Parser.XMLParser;
import context.healthinformatics.SequentialDataAnalysis.Chunk;
import context.healthinformatics.SequentialDataAnalysis.Constraints;

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

	/**
	 * Tests if view is being created sorted by date.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 */
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
		
		orderedByDate(rs);

		test.dropView("workspace");
		data.dropTable("result");
		data.dropTable("HospitalRecords");
		data.dropTable("StatSensor");
	}
	
	/**
	 * Tests if view is ordered by date.
	 * @param rs resultset of view.
	 * @throws SQLException if resultset is empty.
	 */
	public void orderedByDate(ResultSet rs) throws SQLException {
		Date date = new Date(1, 1, 1);
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
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 */
	@Test
	public void mergeTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		String[] clause = new String[1];
		clause[0] = "StatSensor.value = 209";
		MergeTable test = new MergeTable();
		test.merge(clause);
		
		ResultSet rs = data.selectResultSet("workspace", "date");
		
		orderedByDate(rs);
		
		test.dropView("workspace");
		data.dropTable("result");
		data.dropTable("HospitalRecords");
		data.dropTable("StatSensor");
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
		xmlp.parse();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause);
		ArrayList<Chunk> chunks = test.getChunks();
		for (Chunk c : chunks) {
			assertTrue(c.getLine() > 0);
		}

		test.dropView("workspace");
		data.dropTable("result");
		data.dropTable("HospitalRecords");
		data.dropTable("StatSensor");
	}


}
