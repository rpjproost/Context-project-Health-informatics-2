package context.healthinformatics.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for database class.
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
	 *             the sql exception
	 */
	@After
	public void after() throws SQLException {

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
		assertTrue(data.dropTable("test"));
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
		assertTrue(data.dropTable("test"));
	}

	/**
	 * Test insert method.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 * 
	 */
	@Test
	public void testInsert() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		assertTrue(data.insert(tableName, values, col));
		assertTrue(data.dropTable("test"));
	}

	/**
	 * Test insert max id.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 * 
	 */
	@Test
	public void testInsertMaxId() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		assertTrue(data.insert(tableName, values, col));
		assertTrue(data.insert(tableName, values, col));
		assertEquals(data.getMaxId("test"), 2);
		assertTrue(data.dropTable("test"));
	}

	/**
	 * Test select name.
	 * 
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	public void testSelectName() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		assertTrue(data.insert(tableName, values, col));
		assertEquals(data.select(tableName, "Name"), "Rick");
		assertTrue(data.dropTable("test"));
	}

	/**
	 * Test select age column.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	@Test
	public void testSelectAge() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		assertTrue(data.insert(tableName, values, col));
		assertEquals(data.select(tableName, "Age"), "22");
		assertTrue(data.dropTable("test"));
	}

	/**
	 * Test get database path.
	 */
	@Test
	public void testDBPath() {
		assertEquals(data.getDbPath(), "C:/db/");
	}

	/**
	 * Test set database path.
	 */
	@Test
	public void testSetDBPath() {
		data.setDbPath("test");
		assertEquals(data.getDbPath(), "test");
	}

	/**
	 * Test setDB.
	 */
	@Test
	public void setDB() {
		assertEquals(data.setDb("path", "DBName"),
				"jdbc:derby:pathDBName;create=true");
	}

	/**
	 * Drop a non existent sql table.
	 * 
	 * @throws SQLException
	 *             the exception
	 */
	@Test(expected = SQLException.class)
	public void dropNonExistentTable() throws SQLException {
		data.dropTable("nonexistent");
	}

	/**
	 * Insert in a nonexistent table.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	@Test(expected = SQLException.class)
	public void insertNonExistentTable() throws SQLException {
		data.insert("nonexistent", values, col);
	}

}
