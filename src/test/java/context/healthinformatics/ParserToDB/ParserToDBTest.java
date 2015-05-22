package context.healthinformatics.ParserToDB;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

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
	
	/**
	 * method preparing for environment for tests.
	 */
	@org.junit.Before
	public void before() {
		Set<String> tables = new TreeSet<String>();
		tables.addAll(data.getTables().keySet());
		try {
			for (String key : tables) {
				data.dropTable(key);
			}
		} catch (SQLException e) {
			System.out.println("Something went wrong preparing db for tests.");
		}
	}

	/**
	 * Test if the xml parser correctly insert the text file with text parser
	 * into the database.
	 * 
	 * @throws IOException
	 *             the IOException
	 * @throws SQLException
	 *             the SQLException
	 */
	@Test
	public void textParsertoDBTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "textxml.xml");
		xmlp.parse();
		assertNotNull(data.select("stat", "date"));
		assertNotNull(data.select("stat", "value"));
		assertNotNull(data.select("stat", "time"));
		data.dropTable("stat");
	}

	/**
	 * Test if the xml parser correctly insert the excel xlsx file with excel
	 * parser into the database.
	 * 
	 * @throws IOException
	 *             the IOException
	 * @throws SQLException
	 *             the SQLException
	 */
	@Test
	public void excelParsertoDBXLSXTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "excelxml.xml");
		xmlp.parse();
		assertNotNull(data.select("exceltest", "date"));
		assertNotNull(data.select("exceltest", "groep"));
		assertNotNull(data.select("exceltest", "admnr"));
		assertNotNull(data.select("exceltest", "agendaomschrijving"));
		data.dropTable("exceltest");
	}

	/**
	 * Test if the xml parser correctly insert the excel xls file with excel
	 * parser into the database.
	 * 
	 * @throws IOException
	 *             the IOException
	 * @throws SQLException
	 *             the SQLException
	 */
	@Test
	public void excelParsertoDBXLSTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "excelxlsxml.xml");
		xmlp.parse();
		assertNotNull(data.select("exceltest", "admnr"));
		assertNotNull(data.select("exceltest", "monitoring"));
		assertNotNull(data.select("exceltest", "date"));
		assertNotNull(data.select("exceltest", "datelogin"));
		assertNotNull(data.select("exceltest", "remarks"));
		data.dropTable("exceltest");
	}
	
	/**
	 * Test if the xml parser correctly insert the csv file with txt
	 * parser into the database.
	 * 
	 * @throws IOException
	 *             the IOException
	 * @throws SQLException
	 *             the SQLException
	 */
	@Test
	public void csvParsertoDBTest() throws IOException, SQLException {
		xmlp = new XMLParser(path + "csvxml.xml");
		xmlp.parse();
		assertNotNull(data.select("csvtest", "num1"));
		assertNotNull(data.select("csvtest", "num2"));
		assertNotNull(data.select("csvtest", "num3"));
		data.dropTable("csvtest");
	}
}
