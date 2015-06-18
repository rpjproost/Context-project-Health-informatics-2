package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * Test for computationData class.
 *
 */
public class ComputationDataTest {

	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/onDataTestFiles/";

	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();

	/**
	 * method preparing for environment for tests.
	 */
	@org.junit.Before
	public void before() {
		SingletonDb.dropAll(data);
	}
	
	/**
	 * Empty the interpreter.
	 * @throws Exception 
	 */
	@After
	public void after() throws Exception {
		SingletonDb.dropAll(data);
		SingletonInterpreter.getInterpreter().interpret("undoall");
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}

	/**
	 * Test for computing on value and time.
	 * Also computes how many childs a chunk has.
	 * Also computes average values for chunks
	 * Every branch tested.
	 * @throws Exception Sql Exception.
	 */
	@Test
	public void testComputationValueAndUndo() throws Exception {
		xmlp = new XMLParser(path + "demo.xml");
		xmlp.parse();
		xmlp.createDatabase();
		final int res = 321;
		String[] clause = new String[1];
		clause[0] = "meeting.createdby = 'admire2'";
		MergeTable test = new MergeTable();
		test.merge(clause);
		ArrayList<Chunk> chunks = test.getChunks();
		Interpreter i = SingletonInterpreter.getInterpreter();
		i.setIntialChunks(chunks);
		i.interpret("compute data value stat");
		assertTrue(ComputationData.getData().get(0).get(0) != null);
		i.interpret("chunk date 1");
		i.interpret("compute data time time");
		assertTrue(ComputationData.getData().get(1).get(0) != null);
		i.interpret("compute chunk times");
		assertTrue(ComputationData.getData().get(2).get(0) != null);
		assertEquals(ComputationData.getDays(), res);
		assertEquals(ComputationData.getNames().get(0), "stat* 10");
		i.interpret("undocomputation");
		assertEquals(ComputationData.isComputed(), false);
		assertEquals(ComputationData.getData().size(), 0);
	}

}
