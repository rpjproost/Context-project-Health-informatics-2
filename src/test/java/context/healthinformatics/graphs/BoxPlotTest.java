package context.healthinformatics.graphs;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * Specific tests for the boxplot.
 */
public class BoxPlotTest {

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

	private BoxPlot boxplot;
	
	/**
	 * preparing db for test.
	 */
	@Before
	public void before() {
		SingletonDb.dropAll(data);
		boxplot = new BoxPlot();
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
	 * Tests set data with column for box plot.
	 * @throws Exception possible error of interpreter.
	 */
	@Test
	public void testSetDataPerColumn() throws Exception {
		ArrayList<String> data = new ArrayList<String>();
		boxplot.setDataPerColumn(data);
		writeInterpret();
		data.add("value");
		boxplot.setDataPerColumn(data);
		assertNotNull(boxplot.getPanel());
	}

	/**
	 * Tests set data with per chunk for box plot.
	 * @throws Exception possible error of interpreter.
	 */
	@Test
	public void testSetDataPerChunk() throws Exception {
		ArrayList<String> data = new ArrayList<String>();
		boxplot.setDataPerChunk(data);
		writeInterpret();
		data.add("value");
		boxplot.setDataPerChunk(data);
		boxplot.createBoxPlot("test");
		assertNotNull(boxplot.getPanel());
	}
	
	/**
	 * Writes all lines for the interpreter.
	 * @throws Exception could be thrown.
	 */
	private void writeInterpret() throws Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 0";
		MergeTable test = new MergeTable();
		test.merge(clause);
		SingletonInterpreter.getInterpreter().interpret("");
	}

}
