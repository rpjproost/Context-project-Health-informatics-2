package context.healthinformatics.Parser;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertArrayEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the TXTParser.
 */
public class TXTParserTest {

	private TXTParser txtp;
	private String path;
	private static final int FIVE = 5;
	private static final int SIX = 6;
	private static final int NEGATIVEINT = -2;

	/**
	 * Before setup a TXTParser object.
	 */
	@Before
	public void before() {
		path = "src/test/data/txtparsertestfiles/";
		ArrayList<Column> cols = new ArrayList<Column>();
		cols.add(new Column(2, "value", "Integer"));
		cols.add(new Column(FIVE, "date", "String"));
		cols.add(new Column(SIX, "time", "String"));
		txtp = new TXTParser(path + "correcttest.txt", FIVE, ",", cols);
	}

	/**
	 * Test correct parse. Should give no errors.
	 */
	@Test
	public void testCorrect() {
		try {
			txtp.parse();
		} catch (FileNotFoundException e) {
			fail("Correct parse failed");
		}
	}

	/**
	 * Test start line.
	 */
	@Test
	public void testStartLine() {
		assertEquals(txtp.getStartLine(), FIVE);
		txtp.setStartLine(0);
		assertEquals(txtp.getStartLine(), 0);
		txtp.setStartLine(FIVE);
		assertEquals(txtp.getStartLine(), FIVE);
	}

	/**
	 * Make sure a negative startline cannot be set.
	 */
	@Test(expected = AssertionError.class)
	public void testNegativeStartLine() {
		txtp.setStartLine(NEGATIVEINT);
		assertEquals(txtp.getStartLine(), FIVE);
	}

	/**
	 * Test delimiter.
	 */
	@Test
	public void testDelimiter() {
		assertEquals(txtp.getDelimiter(), ",");
		txtp.setDelimiter(";");
		assertEquals(txtp.getDelimiter(), ";");
		txtp.setDelimiter(",");
		assertEquals(txtp.getDelimiter(), ",");
	}

	/**
	 * Test not existent file.
	 * 
	 * @throws FileNotFoundException
	 *             if file not found throw filenotfoundexception.
	 */
	@Test(expected = FileNotFoundException.class)
	public void fileNotFoundTest() throws FileNotFoundException {
		txtp.setFileName(path + "nonexistent.txt");
		txtp.parse();
	}
	
	/**
	 * Tests splitter on empty space.
	 */
	@Test
	public void testDelimiterSpace() {
		String[] res = new String[2];
		res[0] = "a";
		res[1] = "b";
		
		ArrayList<Column> columns = new ArrayList<Column>();
		Column c1 = new Column(1, "test", "String");
		Column c2 = new Column(2, "test2", "String");
		columns.add(c1);
		columns.add(c2);
		
		txtp.setDelimiter(" ");
		txtp.setColumns(columns);
		String[] test = txtp.splitLine("a b");
		
		assertArrayEquals(res, test);
	}
	
	/**
	 * tests splitter on comma.
	 */
	@Test
	public void testDelimiterComma() {
		String[] res = new String[2];
		res[0] = "a";
		res[1] = "b";
		
		ArrayList<Column> columns = new ArrayList<Column>();
		Column c1 = new Column(1, "test", "String");
		Column c2 = new Column(2, "test2", "String");
		columns.add(c1);
		columns.add(c2);
		
		txtp.setDelimiter(",");
		txtp.setColumns(columns);
		String[] test = txtp.splitLine("a,b");
		
		assertArrayEquals(res, test);
	}
	
	/**
	 * tests splitter on comma plus space.
	 */
	@Test
	public void testDelimiterCommaPlusSpace() {
		String[] res = new String[2];
		res[0] = "a";
		res[1] = "b";
		
		ArrayList<Column> columns = new ArrayList<Column>();
		Column c1 = new Column(1, "test", "String");
		Column c2 = new Column(2, "test2", "String");
		columns.add(c1);
		columns.add(c2);
		
		txtp.setDelimiter(", ");
		txtp.setColumns(columns);
		String[] test = txtp.splitLine("a, b");
		
		assertArrayEquals(res, test);
	}
	
	/**
	 * tests splitter without column list.
	 * @throws NullPointerException because of missing column class what should never happen.
	 */
	@Test(expected = NullPointerException.class)
	public void testSplitterNullPointer() throws NullPointerException {
		txtp = new TXTParser(path + "correcttest.txt", FIVE, ",", null);
		txtp.setDelimiter(", ");
		txtp.splitLine("a, b");
	}

}
