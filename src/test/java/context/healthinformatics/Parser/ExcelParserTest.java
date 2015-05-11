package context.healthinformatics.Parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the TXTParser.
 */
public class ExcelParserTest {

	private String path;
	private static final int FIVE = 5;
	private static final int SIX = 6;
	private ArrayList<Column> cols;

	/**
	 * Before setup a TXTParser object.
	 */
	@Before
	public void before() {
		path = "src/test/data/excelparsertestfiles/";
		cols = new ArrayList<Column>();
		cols.add(new Column(2, "value", "Integer"));
		cols.add(new Column(FIVE, "date", "String"));
		cols.add(new Column(SIX, "time", "String"));
	}

	/**
	 * Test correct parse. Should give no errors.
	 * 
	 * @throws IOException
	 *             throws IO Exception if file not found
	 */
	@Test
	public void testCorrectXLSX() throws IOException {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1);
		excelp.parse();
	}

	/**
	 * Test correct parse. Should give no errors.
	 * 
	 * @throws IOException
	 *             throws IO Exception if file not found
	 */
	@Test
	public void testCorrectXLS() throws IOException {
		ExcelParser excelp = new ExcelParser(path + "test.xls", FIVE, cols, 1);
		excelp.parse();

	}

	/**
	 * Test wrong input file.
	 * 
	 * @throws IOException
	 *             throws IO Exception for not found file
	 */
	@Test(expected = IOException.class)
	public void testWrongInputFile() throws IOException {
		ExcelParser excelp = new ExcelParser("not exitent file", FIVE, cols, 1);
		excelp.parse();
	}

	/**
	 * Test the getstartline method.
	 */
	@Test
	public void testGetStartLine() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1);
		assertEquals(excelp.getStartLine(), FIVE);
	}

	/**
	 * Test the set start line.
	 */
	@Test
	public void testSetStartLine() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1);
		assertEquals(excelp.getStartLine(), FIVE);
		excelp.setStartLine(SIX);
		assertEquals(excelp.getStartLine(), SIX);
	}

	/**
	 * Test the get columns method.
	 */
	@Test
	public void testGetColumns() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1);
		assertEquals(excelp.getColumns(), cols);
	}

	/**
	 * Test the set columns method.
	 */
	@Test
	public void testSetColumns() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1);
		ArrayList<Column> setCols = new ArrayList<Column>();
		setCols.add(new Column(2, "value", "Integer"));
		excelp.setColumns(setCols);
		assertEquals(excelp.getColumns(), setCols);
	}

}