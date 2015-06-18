package context.healthinformatics.graphs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JFileChooser;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * Tests the TransitionMatrix incl screenimage save system.
 */
public class StateTransitionMatrixTest {

	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/mergeTableFiles/";
	private String savePath = "src/test/data/writerfiles/testPNG";

	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();

	private JFileChooser chooser;
	private File file;

	private File expected;
	
	/**
	 * preparing db for test.
	 */
	@Before
	public void before() {
		SingletonDb.dropAll(data);
		chooser = mock(JFileChooser.class);
		file = mock(File.class);
		when(chooser.getSelectedFile()).thenReturn(file);
		when(file.getAbsolutePath()).thenReturn(savePath);
		
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
		if (expected != null) {
			expected.delete();
		}
	}

	/**
	 * Tests save image method.
	 * @throws Exception possible throw of the interpreters.
	 */
	@Test
	public void testSaveImage() throws Exception {
		StateTransitionMatrix matrix = new StateTransitionMatrix();
		matrix.fillTransitionMatrix(SingletonInterpreter.getInterpreter().getChunks());
		writeInterpret();
		matrix.fillTransitionMatrix(SingletonInterpreter.getInterpreter().getChunks());
		matrix.createStateTransitionMatrix("Test Title");
		assertTrue(matrix.getStateTransitionMatrix().isVisible());
		matrix.saveImage(chooser, 1);
		matrix.saveImage(chooser, 0);
		verify(chooser).getSelectedFile();
		verify(file).getAbsolutePath();
		expected = new File(savePath + ".png");
		assertTrue(expected.exists());
	}
	
	/**
	 * Tests the connection sets which are used by the matrix.
	 */
	@Test
	public void testConnectionSets() {
		String a = "a";
		String b = "b";
		ConnectionSet set = new ConnectionSet(a, b);
		assertTrue(set.equals(new ConnectionSet(a, b)));
		assertFalse(set.equals(new ConnectionSet(a, a)));
		assertFalse(set.equals(new ConnectionSet(b, b)));
		assertFalse(set.equals(new ConnectionSet(b, a)));
		assertFalse(set.equals(new StateTransitionMatrixTest()));
		String expected = "Code from: " + a + " code to: " + b;
		assertEquals(expected, set.toString());
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
		SingletonInterpreter.getInterpreter().interpret("code (low) line = 4");
		SingletonInterpreter.getInterpreter().interpret("code (low) line = 5");
		SingletonInterpreter.getInterpreter().interpret("code (mid) line = 2");
		SingletonInterpreter.getInterpreter().interpret("code (high) line = 1");
		SingletonInterpreter.getInterpreter().interpret("code (high) line = 3");
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
	}

}
