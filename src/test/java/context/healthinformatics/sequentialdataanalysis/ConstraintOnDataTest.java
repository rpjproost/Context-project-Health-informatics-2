package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * 
 * Test class for constraint on data method.
 *
 */
public class ConstraintOnDataTest {
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
	private Db data;

	private ArrayList<Chunk> chunks;
	
	private MergeTable test;

	/**
	 * Set up parsers and database for constraint over data.
	 * @throws SQLException Database exceptions that should not happen here.
	 * @throws IOException Parser exceptions that should not happen here.
	 */
	@Before
	public void before() throws SQLException, IOException {
		data = SingletonDb.setDb();
		xmlp = new XMLParser(path + "demo.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "meeting.createdby = 'admire2'";
		test = new MergeTable();
		test.merge(clause);
		chunks = test.getChunks();
	}
	
	/**
	 * Removes chunks from interpreter.
	 */
	@After
	public void after() {
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}
	
	/**
	 * Test for constraintOnData without chunks.
	 * @throws Exception interpreter query.
	 */
	@Test
	public void testConstraintOnData() throws Exception {
		final int res = 10;
		Interpreter i = SingletonInterpreter.getInterpreter();
		i.setIntialChunks(chunks);
		i.interpret("filter data where value = 171");
		ArrayList<Chunk> c = i.getChunks();
		assertEquals(res, c.size());
		
		test.dropView("workspace");
		data.dropTable("result");
		data.dropTable("meeting");
		data.dropTable("ADMIRE2");
		i.interpret("undo");
	}
	
	/**
	 * Test for constraintOnData.
	 * @throws Exception interpreter query.
	 */
	@Test
	public void testConstraintOnDataWithChunks() throws Exception {
		final int res = 5;
		Interpreter i = SingletonInterpreter.getInterpreter();
		i.setIntialChunks(chunks);
		i.interpret("chunk data where value < 172");
		i.interpret("filter data where value = 170");

		assertEquals(res, i.getChunks().size());
		
		test.dropView("workspace");
		data.dropTable("result");
		data.dropTable("meeting");
		data.dropTable("ADMIRE2");
		i.interpret("undo");
		i.interpret("undo");
	}

}
