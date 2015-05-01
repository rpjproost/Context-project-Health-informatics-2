package context.healthinformatics.Parser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * class that is used to test the xml parser.
 */
public class XMLParserTest {
	/**
	 * variable used to save the newly created parser.
	 */
	XMLParser xmlp;
	
	/**
	 * this method is called before each test and
	 * creates a new XMLParser.
	 */
	@Before
	public void before() {
		xmlp = new XMLParser("");
	}
	
	/**
	 * Test the xmlparser with a correct file.
	 */
	@Test
	public void correctTest() {
		try {
		xmlp.setFileName("src/test/data/xml/goodXML.xml");
		xmlp.parse();
		assertEquals(xmlp.getDelimiter(), ",");
		assertEquals(xmlp.getDocName(), "StatSensor");
		assertEquals(xmlp.getPath(), "data/xxx.txt");
		assertEquals(xmlp.getStartLine(), 7);
		
		ArrayList<Column> cols = new ArrayList<Column>();
		cols.add(new Column(2,"value","Integer"));
		cols.add(new Column(5,"date","String"));
		cols.add(new Column(6,"time","String"));
		assertTrue(compare(xmlp.getColumns(), cols));
		}
		catch (Exception e) {
			fail("an exception occurred: " + e.toString());
		}
	}
	
	/**
	 * Test with an xmlfile with empty fields. This test should
	 * throw a nullpointer.
	 * @throws IOException Should not throw this exception.
	 */
	@Test(expected = NullPointerException.class)
	public void emptyFieldTest() throws IOException {
		xmlp.setFileName("src/test/data/xml/emptyField.xml");
		xmlp.parse();
	}

	/**
	 * Test with an xmlfile without a delimiter. This test should
	 * throw a nullPointer.
	 * @throws IOException Should not throw this exception
	 */
	@Test(expected = NullPointerException.class)
	public void noDelimiterTest() throws IOException {
		xmlp.setFileName("src/test/data/xml/noDelimiter.xml");
		xmlp.parse();
	}
	
	/**
	 * Test with an xmlfile without an ID. This test should
	 * throw a numberFormatException.
	 * @throws IOException Should not throw this exception
	 */
	@Test(expected = NumberFormatException.class)
	public void noIDsTest() throws IOException {
		xmlp.setFileName("src/test/data/xml/noIDs.xml");
		xmlp.parse();
	}
	
	/**
	 *Test with an xmlfile without a path. This test should
	 * throw a nullPointer.
	 * @throws IOException Should not throw this exception
	 */
	@Test(expected  =NullPointerException.class)
	public void noPathTest() throws IOException {
		xmlp.setFileName("src/test/data/xml/noPath.xml");
		xmlp.parse();
	}

	/**
	 * Test that checks it the startline is correct if it is
	 * not specified.
	 * @throws IOException should not throw this.
	 */
	@Test
	public void noStartTest() throws IOException {
		xmlp.setFileName("src/test/data/xml/wrongStart.xml");
		xmlp.parse();
		assertTrue((xmlp.getStartLine() == 1));
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
	 * Method used to compare 2 arraylists of collumns.
	 * @param l1 The first list to be compared.
	 * @param l2 The Second list to be compared.
	 * @return <code>true</code> when both lists and all
	 * its elements are equals, otherwise return <code>false</code>
	 */
	public boolean compare(ArrayList<Column> l1, ArrayList<Column> l2){
		if (l1.size() == l2.size()) {
			for (int i = 0; i < l1.size(); i++) {
				if (!l1.get(i).equals(l2.get(i)))
					return false;
			}
		}
		return true;
	}
}
