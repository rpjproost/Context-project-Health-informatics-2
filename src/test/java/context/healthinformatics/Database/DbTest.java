package context.healthinformatics.Database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import context.healthinformatics.Parser.Column;

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
	private ArrayList<Column> colArr;

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

		colArr = new ArrayList<Column>();
		colArr.add(new Column(0, "Name", "Type"));
		colArr.add(new Column(0, "Age", "Type"));

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
		assertTrue(data.dropTable(tableName));
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
		assertTrue(data.dropTable(tableName));
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
		assertTrue(data.insert(tableName, values, colArr));
		assertTrue(data.dropTable(tableName));
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
		assertTrue(data.insert(tableName, values, colArr));
		assertTrue(data.insert(tableName, values, colArr));
		assertEquals(data.getMaxId("test"), 2);
		assertTrue(data.dropTable(tableName));
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
		assertTrue(data.insert(tableName, values, colArr));
		assertEquals(data.select(tableName, "Name"), "Rick");
		assertTrue(data.dropTable(tableName));
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
		assertTrue(data.insert(tableName, values, colArr));
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
		data.setDbPath(path);
	}

	/**
	 * Test setDB.
	 */
	@Test
	public void setDB() {
		assertEquals(data.setDb("path", "DBName"),
				"jdbc:derby:pathDBName;create=true");
		assertEquals(data.setDb(path, dbName),
				"jdbc:derby:C:/db/testDB;create=true");
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
		data.insert("nonexistent", values, colArr);
	}

	/**
	 * Try to select from nonexistent table and col.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	@Test(expected = SQLException.class)
	public void selectNonExistent() throws SQLException {
		data.select("nonexistent", "nonexistent");
	}

	/**
	 * Try to select non existent col from existent table.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	@Test
	public void selectNonExistentCol() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		ExpectedException thrown = ExpectedException.none();
		try {
			data.select(tableName, "nonexistentcol");
		} catch (SQLException e) {
			thrown.expect(SQLException.class);

		}
		assertTrue(data.dropTable(tableName));
	}

	/**
	 * Try to insert null into table.
	 * 
	 * @throws SQLException
	 *             the sql exception
	 */
	@Test
	public void insertNull() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
		ExpectedException thrown = ExpectedException.none();
		try {
			assertTrue(data.insert(tableName, null, null));
		} catch (SQLException e) {
			thrown.expect(SQLException.class);

		}
		assertTrue(data.dropTable(tableName));
	}

	/**
	 * Try to remove directory null.
	 */
	@Test
	public void removeNull() {
		assertFalse(data.removeDirectory(null));
	}

}
