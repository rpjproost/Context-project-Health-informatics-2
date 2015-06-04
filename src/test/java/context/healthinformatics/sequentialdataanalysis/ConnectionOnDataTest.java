package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * Tests for Connection class.
 */
public class ConnectionOnDataTest {

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
	private ArrayList<Chunk> chunks;
	private Chunk c1;
	
	/**
	 * method preparing for environment for tests.
	 */
	@org.junit.Before
	public void before() {
		chunks = new ArrayList<Chunk>();
		c1 = new Chunk();
		c1.setComment("A");
		c1.setLine(1);
		chunks.add(c1);
		Chunk c2 = new Chunk();
		c2.setComment("A");
		chunks.add(c2);
		
		Set<String> tables = new TreeSet<String>();
		tables.addAll(data.getTables().keySet());
		try {
			for (String key : tables) {
				data.dropTable(key);
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong preparing db for tests.");
		}
	}
	
	/**
	 * Methods which checks if an connecction is correctly made after an query.
	 * @throws Exception e
	 */
	@Test
	public void testChunkingOnData() throws Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause);
		ArrayList<Chunk> chunks = test.getChunks();
		Connections tests = new Connections(chunks);
		
		assertEquals(0, c1.getPointer().size());
		tests.connectOnData(c1, "value = 209", "test");
		assertEquals(1, c1.getPointer().size());

		test.dropView("workspace");
		data.dropTable("result");
		data.dropTable("HospitalRecords");
		data.dropTable("StatSensor");
	}
}
