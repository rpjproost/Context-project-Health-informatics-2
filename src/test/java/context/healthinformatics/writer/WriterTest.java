package context.healthinformatics.writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.jfree.util.Log;
import org.junit.After;
import org.junit.Test;

import context.healthinformatics.analyse.Interpreter;
import context.healthinformatics.analyse.SingletonInterpreter;
import context.healthinformatics.database.Db;
import context.healthinformatics.database.MergeTable;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;

/**
 * Test for WriteToTXT.
 */
public class WriterTest {
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
	@org.junit.Before
	public void before() {
		File file = new File("src/test/data/writerfiles/testwriter.txt");
		if (file.exists()) {
			file.delete();
		}
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
	 * Test the file not found exception if a bad path is specified.
	 * 
	 * @throws Exception
	 *             FileNotFoundException
	 */
	@Test(expected = Exception.class)
	public void testWrongPath() throws Exception {
		WriteToTXT writer = new WriteToTXT(
				"X:/this/path/does/not/exist/fail.txt");
		try {
			writer.writeOldCodeArea("fail");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			assertEquals(e.getMessage(), "The path to write to is not found!");
			throw new Exception("Test passes!");
		}
	}

	/**
	 * Test the file not found exception if a bad path is specified.
	 * 
	 * @throws Exception
	 *             FileNotFoundException
	 */
	@Test
	public void testWriteStringToTextArea() throws Exception {
		WriteToTXT writer = new WriteToTXT(
				"src/test/data/writerfiles/testwriter.txt");
		writer.writeOldCodeArea("Some string which should be printed");
		File file = new File("src/test/data/writerfiles/testwriter.txt");
		assertTrue(file.exists());
	}

	/**
	 * Test write chunks to TXT file.
	 * 
	 * @throws SQLException
	 *             the SQLException of the database
	 * @throws IOException
	 *             the ioException of the WriteToText and XML parser
	 */
	@Test
	public void testWriteChunks() throws IOException, SQLException {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause, "time");
		WriteToTXT writer = new WriteToTXT(
				"src/test/data/writerfiles/testwriter.txt");
		writer.writeSPSSDataToFile(SingletonInterpreter.getInterpreter()
				.getChunks(), data);
		File file = new File("src/test/data/writerfiles/testwriter.txt");
		assertTrue(file.exists());
	}

	/**
	 * Test write chunks with children to TXT file.
	 * 
	 * @throws Exception
	 *             the exception the interpreter could throw
	 */
	@Test
	public void testWriteChunksWithChildren() throws Exception {
		xmlp = new XMLParser(path + "twoDocs.xml");
		xmlp.parse();
		xmlp.createDatabase();
		String[] clause = new String[1];
		clause[0] = "HospitalRecords.Groep = 2";
		MergeTable test = new MergeTable();
		test.merge(clause, "time");
		WriteToTXT writer = new WriteToTXT(
				"src/test/data/writerfiles/testwriter");
		Interpreter i = SingletonInterpreter.getInterpreter();
		i.interpret("chunk date 30");
		writer.writeSPSSDataToFile(i.getChunks(), data);
		File file = new File("src/test/data/writerfiles/testwriter.txt");
		assertTrue(file.exists());
	}
}
