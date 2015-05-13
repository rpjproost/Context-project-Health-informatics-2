package context.healthinformatics.Writer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Database.SingletonDb;
import context.healthinformatics.Parser.XMLParser;

/**
 * Test for WriteToTXT.
 */
public class WriterTest {
	/**
	 * variable used to save the newly created parser.
	 */
	private XMLParser xmlp;

	/**
	 * path leading to the place of all test files.
	 */
	private String path = "src/test/data/parsertodbfiles/";

	/**
	 * object calling the database.
	 */
	private Db data = SingletonDb.getDb();

	/**
	 * Setup test.
	 * 
	 * @throws SQLException
	 *             the sqlexception
	 * @throws IOException the ioException of the writetotxt
	 */
	@Test
	public void testFirst() throws SQLException, IOException {
		xmlp = new XMLParser(path + "textxml.xml");
		xmlp.parse();

		ResultSet rs = data.selectResultSet("stat", "*");
		WriteToTXT wtxt = new WriteToTXT("test.txt", "C:/Users/wim/Desktop/",
				rs);
		wtxt.writeToFile();
	}
}
