package context.healthinformatics.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for database class.
 * 
 * @author Rick
 *
 */
public class DbTest {

	private Db data;
	private String path;
	private String dbName;
	private String tableName;
	private String[] col;
	private String[] types;
	private String[] values;

	/**
	 * Sets up new database for test usage.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 * @throws NullPointerException
	 *             the nullpointer exception
	 */
	@Before
	public void before() throws NullPointerException, SQLException {
		path = "C:/db/";
		dbName = "testDB";
		data = new Db(dbName, path);

		tableName = "test";

		col = new String[2];
		types = new String[2];
		values = new String[2];

		col[0] = "Name";
		col[1] = "Age";

		types[0] = "varchar(150)";
		types[1] = "INT";

		values[0] = "'Rick'";
		values[1] = "22";
	}

	/**
	 * After test is run.
	 * 
	 * @throws SQLException
	 */
	@After
	public void after() throws SQLException {
		data.dropTable("test");
	}
	
	/**
	 * Test for creating a table.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	@Test
	public void testCreateTable() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
	}

	/**
	 * Get max id from table.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	@Test
	public void testGetMaxIDDefault() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		assertEquals(data.getMaxId("test"), 1);
	}

	/**
	 * @throws SQLException
	 * 
	 */
	@Test
	public void testInsert() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		assertTrue(data.insert(tableName, values, col));

	}

	/**
	 * @throws SQLException
	 * 
	 */
	@Test
	public void testInsertMaxId() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		assertTrue(data.insert(tableName, values, col));
		assertTrue(data.insert(tableName, values, col));
		assertEquals(data.getMaxId("test"), 2);
	}
	
	@Test
	public void testSelect() throws SQLException{
		assertTrue(data.createTable(tableName, col, types));
		assertTrue(data.insert(tableName, values, col));
		data.select(tableName, "Name");
	}

}
