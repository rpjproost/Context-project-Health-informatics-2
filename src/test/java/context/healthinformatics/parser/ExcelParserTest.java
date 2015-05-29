package context.healthinformatics.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;

/**
 * Class to test the TXTParser.
 */
public class ExcelParserTest {

	private String path;
	private static final int THREE = 3;
	private static final int FOUR = 4;
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
		
	}

	/**
	 * Test correct parse. Should give no errors.
	 * 
	 * @throws IOException
	 *             throws IO Exception if file not found
	 * @throws SQLException 
	 */
	@Test
	public void testCorrectXLSX() throws IOException, SQLException {
		Db data = SingletonDb.getDb();
		Column date = new Column(THREE, "date", "Date");
		date.setDateType("dd/MM/yyyy");
		cols.add(date);
		cols.add(new Column(1, "value", "Integer"));
		cols.add(new Column(FOUR, "time", "String"));
		data.createTable("docname", cols);
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", 1, cols, 1,
				"docname");
		excelp.parse();
		data.dropTable("docname");
	}

	/**
	 * Test correct parse. Should give no errors.
	 * 
	 * @throws IOException
	 *             throws IO Exception if file not found
	 * @throws SQLException 
	 */
	@Test
	public void testCorrectXLS() throws IOException, SQLException {
		Db data = SingletonDb.getDb();
		Column date = new Column(1, "date", "Date");
		date.setDateType("dd/MM/yyyy");
		cols.add(date);
		cols.add(new Column(2, "time", "String"));
		cols.add(new Column(THREE, "value", "Integer"));
		data.createTable("docname", cols);
		ExcelParser excelp = new ExcelParser(path + "test.xls", FOUR, cols, 1,
				"docname");
		excelp.parse();
		data.dropTable("docname");
	}

	/**
	 * Test wrong input file.
	 * 
	 * @throws IOException
	 *             throws IO Exception for not found file
	 */
	@Test(expected = IOException.class)
	public void testWrongInputFile() throws IOException {
		ExcelParser excelp = new ExcelParser("not exitent file", FIVE, cols, 1,
				"docname");
		excelp.parse();
	}

	/**
	 * Test the getstartline method.
	 */
	@Test
	public void testGetStartLine() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1,
				"docname");
		assertEquals(excelp.getStartLine(), FIVE);
	}

	/**
	 * Test the set start line.
	 */
	@Test
	public void testSetStartLine() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1,
				"docname");
		assertEquals(excelp.getStartLine(), FIVE);
		excelp.setStartLine(SIX);
		assertEquals(excelp.getStartLine(), SIX);
	}

	/**
	 * Test the get columns method.
	 */
	@Test
	public void testGetColumns() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1,
				"docname");
		assertEquals(excelp.getColumns(), cols);
	}

	/**
	 * Test the set columns method.
	 */
	@Test
	public void testSetColumns() {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1,
				"docname");
		ArrayList<Column> setCols = new ArrayList<Column>();
		setCols.add(new Column(2, "value", "Integer"));
		excelp.setColumns(setCols);
		assertEquals(excelp.getColumns(), setCols);
	}

}