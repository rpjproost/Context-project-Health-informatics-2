package context.healthinformatics.graphs;

import static org.junit.Assert.assertNotNull;

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
 * Tests the line chart with good and bad data.
 */
public class LineChartTest {
	
	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/xml/";

	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();
	
	/**
	 * preparing db for test.
	 */
	@Before
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
	 * Tests the methods in line chart.
	 * @throws Exception could be thrown by the interpreter.
	 */
	@Test
	public void testCreateLineChart() throws Exception {
		LineChart chart = new LineChart();
		chart.initDataset();
		writeInterpret();
		chart.initDataset();
		chart.createLineChart("TestChart");
		assertNotNull(chart.getPanel());
	}
	
	/**
	 * Writes all lines for the interpreter.
	 * @throws Exception could be thrown.
	 */
	private void writeInterpret() throws Exception {
		xmlp = new XMLParser(path + "ComparisonTest.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "meeting.createdby = 'admire2'";
		MergeTable test = new MergeTable();
		test.merge(clause);
		SingletonInterpreter.getInterpreter().interpret("chunk date 7");
		SingletonInterpreter.getInterpreter().interpret("compute chunk times");
		SingletonInterpreter.getInterpreter().interpret("filter data where beschrijving = 'Crea'");
		SingletonInterpreter.getInterpreter().interpret("compute data value Stat");
		SingletonInterpreter.getInterpreter().interpret("undo");
		SingletonInterpreter.getInterpreter().interpret("undo");
		SingletonInterpreter.getInterpreter().interpret(
				"filter data where beschrijving = 'Kreatinine (stat)'");
		SingletonInterpreter.getInterpreter().interpret("compute data value Website");
		SingletonInterpreter.getInterpreter().interpret("undo");
		SingletonInterpreter.getInterpreter().interpret("undo");
		SingletonInterpreter.getInterpreter().interpret(
				"filter data where beschrijving = 'Bloeddruk'");
		SingletonInterpreter.getInterpreter().interpret("compute data value Bloed");
	}

}