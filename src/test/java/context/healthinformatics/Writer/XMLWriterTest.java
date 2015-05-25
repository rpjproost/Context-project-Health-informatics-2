package context.healthinformatics.Writer;

import java.util.ArrayList;

import org.junit.Test;

import context.healthinformatics.Parser.Column;
import context.healthinformatics.Parser.XMLParser;

/**
 * Test class for XMLWriter.
 */
public class XMLWriterTest {
	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/writerfiles/";

	private static final int THREE = 3;
	private static final int FOUR = 4;

	/**
	 * Test if writer writes a document to a xml file.
	 */
	@Test
	public void testWriter() {
		ArrayList<Column> cols = new ArrayList<Column>();
		Column date = new Column(THREE, "date", "Date");
		date.setDateType("dd/MM/yyyy");
		cols.add(date);
		cols.add(new Column(1, "value", "Integer"));
		cols.add(new Column(FOUR, "time", "String"));
		XMLDocument doc = new XMLDocument("excel", "exceldocname",
				"excelhasnodelimiter", "samplepath", 2, 1, cols);
		ArrayList<XMLDocument> docs = new ArrayList<XMLDocument>();
		docs.add(doc);
		XMLWriter xmlwriter = new XMLWriter(docs);
		xmlwriter.writeXML("C:\test.xml");
	}
}
