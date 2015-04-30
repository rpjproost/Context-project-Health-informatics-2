package context.healthinformatics.Parser;

import static org.junit.Assert.*;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ParserTest {
	Parser p;
	
	@DataPoints
	public static Parser[] parsers() {
		XMLParser xmlp = new XMLParser("test");
		TXTParser txtp = new TXTParser("test", 0, null);
		return new Parser[] { xmlp, txtp};
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
