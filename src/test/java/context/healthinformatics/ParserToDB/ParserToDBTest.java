package context.healthinformatics.ParserToDB;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Database.SingletonDb;
import context.healthinformatics.Parser.XMLParser;

/**
 * Test that parsers put everything correctly into the database.
 */
public class ParserToDBTest {
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

	@Test
	public void textParsertoDBTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "textxml.xml");
		xmlp.parse();
		assertNotNull(data.select("stat", "date"));
		assertNotNull(data.select("stat", "value"));
		assertNotNull(data.select("stat", "time"));		
		data.dropTable("stat");
	}
	
	@Test
	public void excelParsertoDBTest() throws IOException, SQLException{
		xmlp = new XMLParser(path + "excelxml.xml");
		xmlp.parse();
		assertNotNull(data.select("exceltest", "date"));
		assertNotNull(data.select("exceltest", "groep"));
		assertNotNull(data.select("exceltest", "admnr"));
		assertNotNull(data.select("exceltest", "agendaomschrijving"));
	}
}
