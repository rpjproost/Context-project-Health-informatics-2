package context.healthinformatics.ParserToDB;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import java.sql.ResultSet;
import org.junit.After;
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
	
	//used for cleaning up;
	private Set<ResultSet> results;
	private Set<String> tables;
	
	/**
	 * method preparing for environment for tests.
	 */
	@org.junit.Before
	public void before() {
		tables = new TreeSet<String>();
		results = new HashSet<ResultSet>();
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
		String table = "stat";
		tables.add(table);
		assertNotNull(getResult(table, "date"));
		assertNotNull(getResult(table, "value"));
		assertNotNull(getResult(table, "time"));
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
		String table = "exceltest";
		tables.add(table);
		assertNotNull(getResult(table, "date"));
		assertNotNull(getResult(table, "groep"));
		assertNotNull(getResult(table, "admnr"));
		assertNotNull(getResult(table, "agendaomschrijving"));
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
		String table = "exceltest";
		tables.add(table);
		assertNotNull(getResult(table, "admnr"));
		assertNotNull(getResult(table, "monitoring"));
		assertNotNull(getResult(table, "date"));
		assertNotNull(getResult(table, "datelogin"));
		assertNotNull(getResult(table, "remarks"));
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
		String table = "csvtest";
		assertNotNull(getResult(table, "num1"));
		assertNotNull(getResult(table, "num2"));
		assertNotNull(getResult(table, "num3"));
	}
	
	/**
	 * method that cleans up after a test.
	 * @throws SQLException if something goes wrong closing resultsets of dropping tables.
	 */
	@After
	public void after() throws SQLException {
		for (ResultSet r : results) {
			r.close();
		}
		for (String table : tables) {
			data.dropTable(table);
		}
	}
	
	/**
	 * Retrieves the correct result set from the database.
	 * @param table table to get the result set from.
	 * @param column column to get the result set for
	 * @return the retrieved result set.
	 * @throws SQLException might throw an SQL exception if table does not exist.
	 */
	private ResultSet getResult(String table, String column) throws SQLException {
		ResultSet res = data.selectResultSet(table, column, "");
		results.add(res);
		return res;
	}
}
