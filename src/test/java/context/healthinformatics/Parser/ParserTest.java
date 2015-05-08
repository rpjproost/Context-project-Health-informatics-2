package context.healthinformatics.Parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * General test for parsers.
 */
@RunWith(Theories.class)
public class ParserTest {
	
	/**
	 * method that creates each parser to be tested.
	 * @return an array of the parsers to test.
	 */
	@DataPoints
	public static Parser[] parsers() {
		XMLParser xmlp = new XMLParser("test");
		TXTParser txtp = new TXTParser("test", 0, null, new ArrayList<Column>());
		ExcelParser exlp = new ExcelParser("test", 0, null);
		return new Parser[] { xmlp, txtp, exlp};
	}
	
	/**
	 * tests the getFileName method.
	 * @param p the parser to test it for.
	 */
	@Theory
	public void getTest(Parser p) {
		assertEquals(p.getFileName(), "test");
	}
	/**
	 * tests the setFileName method.
	 * @param p the parser to test it for.
	 */
	@Theory
	public void setTest(Parser p) {
		p.setFileName("foo");
		assertEquals(p.getFileName(), "foo");
	}

}
