package context.healthinformatics.Parser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Database.SingletonDb;

/**
 * class that is used to test the xml parser.
 */
public class XMLParserTest {

	/**
	 * variable used to save the newly created parser.
	 */
	private XMLTestParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/xml/";

	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();

	/**
	 * this method is called before each test and
	 * creates a new XMLParser.
	 */
	@Before
	public void before() {
		xmlp = new XMLTestParser("");
	}

	/**
	 * Test the xmlparser with a correct file.
	 */
	@Test
	public void correctTest() {
		final int startLine = 7;
		final int col1 = 2;
		final int col2 = 5;
		final int col3 = 6;
		try {
			xmlp.setFileName(path + "goodXML.xml");
			xmlp.parse();
			TXTParser txtp = (TXTParser) xmlp.getParsers().get(0);
			assertEquals("," , txtp.getDelimiter());
			assertEquals("stat", txtp.getDocName());
			assertEquals("src/test/data/xml/inputTXT.txt", txtp.getFileName());
			assertEquals(startLine, txtp.getStartLine());

			ArrayList<Column> cols = new ArrayList<Column>();
			cols.add(new Column(col1, "value", "Integer"));
			cols.add(new Column(col2, "date", "String"));
			cols.add(new Column(col3, "time", "String"));
			assertTrue(compare(txtp.getColumns(), cols));

			data.dropTable("stat");
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("an exception occurred: " + e.toString());
			System.out.println(e.toString());
		}
	}

	/**
	 * Test with an xmlfile with empty fields. This test should
	 * throw a nullpointer.
	 * @throws IOException Should not throw this exception.
	 */
	@Test
	public void emptyFieldTest() throws IOException {
		xmlp.setFileName(path + "emptyField.xml");
		try {
			xmlp.parse(); }
		catch (FileNotFoundException e) {
			assertEquals("The Table could not be created.", e.getMessage());
		}
	}

	/**
	 * Test with an xmlfile without a delimiter. This test should
	 * throw a nullPointer.
	 * @throws IOException Should not throw this exception
	 */
	@Test(expected = FileNotFoundException.class)
	public void noDelimiterTest() throws IOException {
		xmlp.setFileName(path + "noDelimiter.xml");
		xmlp.parse();
		try {
			data.dropTable("StatSensor");
		} catch (Exception e) {
			fail("something went wrong, the table was not created: "  + e.getMessage());
		}
	}

	/**
	 * Test with an xmlfile without an ID. This test should
	 * throw a numberFormatException.
	 * @throws IOException Should not throw this exception
	 */
	@Test(expected = NumberFormatException.class)
	public void noIDsTest() throws IOException {
		xmlp.setFileName(path + "noIDs.xml");
		xmlp.parse();
	}

	/**
	 *Test with an xmlfile without a path. This test should
	 * throw a nullPointer.
	 * @throws IOException Should not throw this exception
	 */
	@Test
	public void noPathTest() throws IOException {
		xmlp.setFileName(path + "noPath.xml");
		xmlp.parse();
		assertEquals(null, xmlp.getParsers().get(0).getFileName());
		try {
			data.dropTable("StatSensor");
		} catch (Exception e) {
			fail("something went wrong, the table was not created: "  + e.getMessage());
		}
	}

	/**
	 * Test that checks it the startline is correct if it is
	 * not specified.
	 * @throws IOException should not throw this.
	 */
	@Test
	public void noStartTest() throws IOException {
		xmlp.setFileName(path + "wrongStart.xml");
		xmlp.parse();
	}

	/**
	 * Test if the FileNotFoundException is thrown when
	 * the file does not exist.
	 * @throws IOException This should be thrown.
	 */
	@Test(expected = FileNotFoundException.class)
	public void fileNotFoundTest() throws IOException {
		xmlp.setFileName("nonexistent.xml");
		xmlp.parse();
	}
	/**
	 * tests parsing the settings for 2 documents
	 * and assigning the different parsers.
	 * @throws IOException shouldn't throw this.
	 */
	@Test
	public void twoDocsTest() throws IOException {
		xmlp.setFileName(path + "twoDocs.xml");
		xmlp.parse();
		ArrayList<Parser> parsers = xmlp.getParsers();
		assertEquals(2, parsers.size());
		assertEquals("src/test/data/xml/inputTXT.txt", parsers.get(0).getFileName());
		assertEquals("src/test/data/xml/inputExcel.xlsx", parsers.get(1).getFileName());
		ExcelParser exlp = (ExcelParser) parsers.get(1);
		assertEquals(1, exlp.getStartLine());
		try {
			data.dropTable("StatSensor");
			data.dropTable("HospitalRecords"); }
		catch (Exception e) {
			fail("something went wrong, the tables where not created: "  + e.getMessage());
		}
	}

	/**
	 * tests parsing for a csv document.
	 * @throws IOException throws if the document is wrong.
	 */
	@Test
	public void csvTest() throws IOException {
		xmlp.setFileName(path + "csv.xml");
		xmlp.parse();
		TXTParser txtp = (TXTParser) xmlp.getParsers().get(0);
		assertEquals("csv", txtp.getDocName());
		assertEquals("src/test/data/xml/inputCSV.csv", txtp.getFileName());
		assertEquals(";", txtp.getDelimiter());
		try {
			data.dropTable("csv");
		}
		catch (Exception e) {
			fail("something went wrong, the table was not created: "  + e.getMessage());
		}
	}

	/**
	 * tests the parsing of a badly formatted xml file.
	 * @throws IOException shouldn't throw this.
	 */
	@Test
	public void noEnd() throws IOException {
		xmlp.setFileName(path + "noEnd.xml");
		try {
			xmlp.parse();
		}
		catch (FileNotFoundException e) {
			assertEquals("The XML was not formatted correctly", e.getMessage());
		}
	}
	/**
	 * Method used to compare 2 arraylists of collumns.
	 * @param l1 The first list to be compared.
	 * @param l2 The Second list to be compared.
	 * @return <code>true</code> when both lists and all
	 * its elements are equals, otherwise return <code>false</code>
	 */
	public boolean compare(ArrayList<Column> l1, ArrayList<Column> l2) {
		if (l1.size() == l2.size()) {
			for (int i = 0; i < l1.size(); i++) {
				if (!l1.get(i).equals(l2.get(i))) {
					return false; }
			}
		}
		return true;
	}
}
