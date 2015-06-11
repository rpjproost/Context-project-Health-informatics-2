package context.healthinformatics.writer;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

import context.healthinformatics.database.Db;
import context.healthinformatics.database.SingletonDb;
import context.healthinformatics.parser.XMLParser;
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
		//TODO IMPLEMENT TEST FOR WRITER!!!!!
		xmlp.parse();
		xmlp.createDatabase();
	//	ResultSet rs = data.selectResultSet("stat", "*", "");
		//WriteToTXT wtxt = new WriteToTXT("test.txt", "src/test/data/writerfiles/");
		//wtxt.writeToFile(rs);
		data.dropTable("stat");
	}
}
