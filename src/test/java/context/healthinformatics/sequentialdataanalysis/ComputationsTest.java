package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Test;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

public class ComputationsTest {

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
	
	private final int TWO = 2;
	private final int THREE = 3;
	
	@org.junit.Before
	public void before() {
		SingletonDb.dropAll(data);
	}
	
	/**
	 * Empty the interpreter.
	 */
	@After
	public void after() {
		SingletonDb.dropAll(data);
		try {
			SingletonInterpreter.getInterpreter().interpret("undoAll");
		} catch (Exception e) {
			Log.info("undoAll failed.");
		}
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}

	/**
	 * Test for creating chunked list on data constraint.
	 * @throws IOException if files could not be read.
	 * @throws SQLException if tables could not be created.
	 * @throws Exception if the interpreting goes wrong.
	 */
	@Test
	public void testChunkingOnData() throws IOException, SQLException, Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause);
		SingletonInterpreter.getInterpreter().interpret("filter data where not time is null");
		SingletonInterpreter.getInterpreter().interpret("code(a) data where not time is null");
		SingletonInterpreter.getInterpreter().
		interpret("connect(connectionNote) code = a to code = a");
		SingletonInterpreter.getInterpreter().interpret("compute difference time");
		ArrayList<Chunk> chunks = SingletonInterpreter.getInterpreter().getChunks();
		final int firstRes = 27;
		final int seccondRes = 1740;
		final int thirdRes = 1603;
		final int fourthRes = 3066;
		assertEquals(isDif(firstRes), chunks.get(0).toArray().get(0));
		assertEquals(isDif(seccondRes), chunks.get(1).toArray().get(0));
		assertEquals(isDif(thirdRes), chunks.get(TWO).toArray().get(0));
		assertEquals(isDif(fourthRes), chunks.get(THREE).toArray().get(0));
	}
	
	private String isDif(int num) {
		String test = "difference to connection " + num + ".0";
		return test;
	}

}
