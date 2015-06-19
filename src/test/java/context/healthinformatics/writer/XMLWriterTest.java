package context.healthinformatics.writer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.parser.Column;
import context.healthinformatics.parser.XMLParser;

/**
 * Test class for XMLWriter.
 */
public class XMLWriterTest {

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/writerfiles/";

	private XMLWriter xmlwriter;
	private ArrayList<XMLDocument> docs;

	private static final int THREE = 3;
	private static final int FOUR = 4;

	/**
	 * Make some files to write.
	 * 
	 * @throws ParserConfigurationException
	 *             the ParserConfigurationException
	 * @throws TransformerConfigurationException
	 *             the TransformerConfigurationException
	 */
	@Before
	public void setup() throws TransformerConfigurationException,
			ParserConfigurationException {
		ArrayList<Column> cols = new ArrayList<Column>();
		Column date = new Column(THREE, "date", "Date");
		date.setDateType("dd/MM/yyyy");
		cols.add(date);
		cols.add(new Column(1, "value", "Integer"));
		cols.add(new Column(FOUR, "time", "String"));
		XMLDocument doc = new XMLDocument("excel", "exceldocname",
				"excelhasnodelimiter", "samplepath", 2, 1, cols);

		ArrayList<Column> cols2 = new ArrayList<Column>();
		Column date2 = new Column(FOUR, "date", "Date");
		date2.setDateType("dd/MM/yyyy");
		cols2.add(date2);
		cols2.add(new Column(1, "value", "Integer"));
		cols2.add(new Column(THREE, "omschrijving", "String"));
		XMLDocument doc2 = new XMLDocument("text", "textdocname",
				"textwithadelimiter", "path", 0, -1, cols);

		docs = new ArrayList<XMLDocument>();
		docs.add(doc);
		docs.add(doc2);
		xmlwriter = new XMLWriter(docs);
	}

	/**
	 * Test if writer writes a document to a xml file.
	 * 
	 * @throws IOException
	 *             thrown if the parse method fails.
	 * @throws TransformerException
	 *             the transformerException
	 */
	@Test
	public void testWriter() throws IOException, TransformerException {
		xmlwriter.writeXML(path + "test.xml");
		XMLParser parser = new XMLParser(path + "test.xml");
		parser.parse();
		ArrayList<XMLDocument> parsedDocs = parser.getDocuments();
		for (int i = 0; i < docs.size(); i++) {
			assertEquals(docs.get(i).getDocType(), parsedDocs.get(i)
					.getDocType());
			assertEquals(docs.get(i).getDelimiter(), parsedDocs.get(i)
					.getDelimiter());
			assertEquals(docs.get(i).getDocName(), parsedDocs.get(i)
					.getDocName());
			assertEquals(docs.get(i).getPath(), parsedDocs.get(i).getPath());
		}
	}
}
