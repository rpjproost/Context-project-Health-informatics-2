package context.healthinformatics.SequentialDataAnalysis;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import context.healthinformatics.Database.Db;
import context.healthinformatics.Database.SingletonDb;
import context.healthinformatics.Parser.Column;

public class ChunkTest {
	
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
		data = SingletonDb.getDb();

		tableName = "test";

		col = new String[2];
		types = new String[2];
		values = new String[2];

		colArr = new ArrayList<Column>();
		colArr.add(new Column(0, "Name", "String"));
		colArr.add(new Column(0, "Age", "Integer"));

		col[0] = "Name";
		col[1] = "Age";

		types[0] = "varchar(150)";
		types[1] = "INT";

		values[0] = "Rick";
		values[1] = "22";
	}

	/**
	 * Tests creating of chunks and adding Code.
	 * @throws SQLException database creation.
	 */
	@Test
	public void testCodeEquals() throws SQLException {
		data.createTable(tableName, col, types);
		data.insert(tableName, values, colArr);
		data.insert(tableName, values, colArr);
		int amountOfChunks = data.getMaxId(tableName);
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		for (int i = 0; i <= amountOfChunks; i++) {
			Chunk c = new Chunk(i);
			chunks.add(c);
		}
		Chunk c = chunks.get(2);
		c.setCode("C");
		assertEquals(chunks.get(2).getCode(), "C");
		data.dropTable(tableName);
	}

}
