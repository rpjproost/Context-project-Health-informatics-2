package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * class to test Codes class.
 */
public class CodeOnDataTest {

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

	/**
	 * method preparing for environment for tests.
	 */
	@org.junit.Before
	public void before() {
		chunks = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setComment("A");
		c1.setLine(1);
		chunks.add(c1);
		Chunk c2 = new Chunk();
		c2.setComment("A");
		chunks.add(c2);
		SingletonDb.dropAll(data);
	}
	
	/**
	 * Removes chunks from interpreter.
	 */
	@After
	public void after() {
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}
	
	/**
	 * method to test if the correct chunks are given a code according to a query.
	 * @throws Exception e
	 */
	@Test
	public void testCodesOnData() throws Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause);
		ArrayList<Chunk> chunks = test.getChunks();
		Codes tests = new Codes("test");
		tests.setChunks(chunks);
		
		tests.setCodeOnData("value = 209");
		assertEquals("test", chunks.get(1).getCode());
	}
}
