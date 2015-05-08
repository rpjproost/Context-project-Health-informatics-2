package context.healthinformatics.Database;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

/**Test for database class.
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
	 * @throws SQLException 
	 * @throws NullPointerException 
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
	 * Test for creating a table.
	 * @throws SQLException 
	 */
	@Test
	public void testCreateTable() throws SQLException {
		assertTrue(data.createTable(tableName, col, types));
	}
	

}
