package context.healthinformatics.graphs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.graphs.graphspanel.BoxPlotPanel;
import context.healthinformatics.graphs.graphspanel.FrequencyBarPanel;
import context.healthinformatics.graphs.graphspanel.GraphPanel;
import context.healthinformatics.graphs.graphspanel.TransitionaMatrixPanel;
import context.healthinformatics.parser.XMLParser;

/**
 * Tests all possible graphs.
 */
@RunWith(Theories.class)
public class GraphsTest {

	private static final int WIDTH = 500;
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
	 * @return array list of panels that must be tested.
	 */
	@DataPoints
	public static GraphPanel[] panels() {
		BoxPlotPanel bpp = new BoxPlotPanel(WIDTH);
		FrequencyBarPanel fbp = new FrequencyBarPanel(WIDTH);
		TransitionaMatrixPanel tmp = new TransitionaMatrixPanel(WIDTH);
		return new GraphPanel[] { bpp, fbp, tmp};
	}

	/**
	 * Tests if the check-box for the panel is selected or not.
	 * @param gp the panel to be tested.
	 */
	@Theory
	public void testIsSelected(GraphPanel gp) {
		assertFalse(gp.isSelected());
	}
	
	/**
	 * try to plot a graph without data.
	 * @param gp the panel to be tested.
	 */
	@Theory
	public void testPlotNoData(GraphPanel gp) {
		assertFalse(gp.getGraphPanel().isVisible());
		gp.plot();
		assertTrue(gp.getGraphPanel().isVisible());
	}
	
	/**
	 * try to plot with data.
	 * @param gp the panel to be tested.
	 * @throws IOException exception that could be thrown.
	 * @throws SQLException exception that could be thrown.
	 * @throws Exception exception that could be thrown.
	 */
	@Theory
	public void testPlotWithData(GraphPanel gp) throws IOException, SQLException, Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 0";
		MergeTable test = new MergeTable();
		test.merge(clause);
		SingletonInterpreter.getInterpreter().interpret("code (low) line = 4");
		SingletonInterpreter.getInterpreter().interpret("code (low) line = 5");
		SingletonInterpreter.getInterpreter().interpret("code (mid) line = 1");
		SingletonInterpreter.getInterpreter().interpret("code (high) line = 2");
		SingletonInterpreter.getInterpreter().interpret("code (high) line = 3");
		SingletonInterpreter.getInterpreter().interpret("chunk code = mid");
		SingletonInterpreter.getInterpreter().interpret(
				"connect (low-mid) code = low to code = mid");
		SingletonInterpreter.getInterpreter().interpret(
				"connect (mid-high) code = mid to code = high");
		SingletonInterpreter.getInterpreter().interpret(
				"connect (low-high) code = low to code = high");
		SingletonInterpreter.getInterpreter().interpret(
				"connect (high-low) code = high to code = low");
		SingletonInterpreter.getInterpreter().interpret(
				"connect (mid-low) code = mid to code = low");
		assertFalse(gp.getGraphPanel().isVisible());
		gp.updateContainer();
		gp.plot();
		assertTrue(gp.getGraphPanel().isVisible());
	}

}
