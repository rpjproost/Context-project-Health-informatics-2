package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
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
 * 
 * Class for testing chunking on data.
 *
 */
public class ChunkingOnDataTest {
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
		SingletonDb.dropAll(data);
	}
	
	/**
	 * Empty the interpreter.
	 */
	@After
	public void after() {
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}

	/**
	 * Test for creating chunked list on data constraint.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 */
	@Test
	public void testChunkingOnData() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		final int res = 6;
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause);
		ArrayList<Chunk> chunks = test.getChunks();
		Chunking tests = new Chunking();
		tests.setChunks(chunks);

		ArrayList<Chunk> x = tests.constraintOnData("groep = 2");
		assertTrue(x.size() == res);
	}
	
	/**
	 * Test for chunking with interpretor.
	 * @throws Exception if query is wrong.
	 */
	@Test
	public void testChunkingWithInterpreter() throws Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		final int res = 6;
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause);
		ArrayList<Chunk> chunks = test.getChunks();
		
		Interpreter i = SingletonInterpreter.getInterpreter();
		i.setIntialChunks(chunks);
		i.interpret("chunk data where groep = 2");
		ArrayList<Chunk> c = i.getChunks();

		assertTrue(c.size() == res);
		i.interpret("undo");
	}
}
