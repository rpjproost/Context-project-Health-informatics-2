package context.healthinformatics.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertArrayEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the TXTParser.
 */
public class ExcelParserTest {

	private TXTParser txtp;
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
	 * @throws IOException 
	 */
	@Test
	public void testCorrectXLSX() throws IOException {
		ExcelParser excelp = new ExcelParser(path + "test.xlsx", FIVE, cols, 1);
		excelp.parse();
	}
	
	/**
	 * Test correct parse. Should give no errors.
	 * @throws IOException 
	 */
	@Test
	public void testCorrectXLS() throws IOException {
		ExcelParser excelp = new ExcelParser(path + "test.xls", FIVE, cols, 1);
		excelp.parse();

	}

}
