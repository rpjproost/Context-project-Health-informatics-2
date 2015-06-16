package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.Query;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * Class to test cOmparison.
 */
public class ComparisonTest {

	private Interpreter ip;
	private XMLParser xmlp;
	private String path = "src/test/data/xml/";
	private Db data;
	private MergeTable test;

	/**
	 * Before each method
	 * @throws Exception 
	 */
	@Before
	public void before() throws Exception {
		data = SingletonDb.getDb();
		SingletonDb.dropAll(data);
		xmlp = new XMLParser(path + "ComparisonTest.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "meeting.createdby = 'admire2'";
		test = new MergeTable();
		test.merge(clause, "itemid");
		
		ip = SingletonInterpreter.getInterpreter();		
	}

	/**
	 * Test the set code on a line method.
	 * 
	 * @throws Exception
	 *             maybe the code couldn't be set.
	 */
	@Test
	public void testCompare() throws Exception {
		Comparison c = new Comparison();
		ip.interpret("filter data where beschrijving = 'Kreatinine (stat)' OR beschrijving = 'Kreatinine2 (stat)'");		
//		ArrayList<Chunk> chunks = ip.getChunks();
//		for (Chunk ch : chunks) {
//			System.out.println(ch.toArray().toString());
//		}
		
		Query q = new Query("test");//na het woord compare geen andere input.
		c.run(q);
//		int index = c.dates.indexOf
		assertEquals(c.getAdvices().get(1), "NULL");
		assertEquals(c.getAdvices().get(8), "nothing extra");
		assertEquals(c.getAdvices().get(60), "measure tomorrow");
		
		ip.interpret("undo");
	}
	
	@After
	public void after() {
		try {
			ip.interpret("undoAll");
			ip.setIntialChunks(null);
		} catch(Exception e) {
			Log.info(e.getMessage());
		}
	}
}