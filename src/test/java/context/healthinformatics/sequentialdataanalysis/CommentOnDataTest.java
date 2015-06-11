package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Test;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * Class to test CommentOnData.
 */
public class CommentOnDataTest {

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
	private Db data = SingletonDb.setDb();
	private ArrayList<Chunk> chunks;

	/**
	 * method preparing for environment for tests.
	 */
	@org.junit.Before
	public void before() {
		SingletonDb.dropAll(data);
		chunks = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setComment("A");
		c1.setLine(1);
		chunks.add(c1);
		Chunk c2 = new Chunk();
		c2.setComment("A");
		chunks.add(c2);
		
		Set<String> tables = new TreeSet<String>();
		tables.addAll(data.getTables().keySet());
	}
	
	/**
	 * Removes chunks from interpreter.
	 */
	@After
	public void after() {
		SingletonDb.dropAll(data);
		SingletonInterpreter.getInterpreter().setIntialChunks(null);
	}

	/**
	 * Test for creating chunked list on data constraint.
	 * @throws Exception 
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
		Comments tests = new Comments("testComment");
		tests.setChunks(chunks);

		tests.setCommentOnData("value = 209");
		assertEquals("testComment", chunks.get(1).getComment());
	}
}
