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
	private Chunk chunk;

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
		chunk = new Chunk();
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
	 * Tests creating of chunks with database max rows.
	 * @throws SQLException database creation.
	 */
	@Test
	public void testCreatingChunksWithDbMaxRows() throws SQLException {
		assertTrue(data.createTable(tableName, colArr));
		data.insert(tableName, values, colArr);
		data.insert(tableName, values, colArr);
		int amountOfChunks = data.getMaxId(tableName);
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		for (int i = 0; i <= amountOfChunks; i++) {
			Chunk c = new Chunk();
			chunks.add(c);
		}
		Chunk c = chunks.get(2);
		c.setCode("C");
		assertEquals(chunks.get(2).getCode(), "C");
		data.dropTable(tableName);
	}
	
	/**
	 * Test creation of chunk.
	 */
	@Test
	public void testChunkCreation() {
		Chunk c = new Chunk();
		assertEquals(c.getLine(), 0);
	}
	/**
	 * Tests chunk code get/setters.
	 */
	@Test
	public void testChunkCode() {
		assertEquals(chunk.getCode(), "");
		chunk.setCode("C");
		assertEquals(chunk.getCode(), "C");
	}
	
	/**
	 * Test chunk comment get/setters.
	 */
	@Test
	public void testChunkComment() {
		assertEquals(chunk.getComment(), "");
		chunk.setComment("Test");
		assertEquals(chunk.getComment(), "Test");
	}
	
	/**
	 * Test chunk id get/setters.
	 */
	@Test
	public void testChunkId() {
		assertEquals(chunk.getLine(), 0);
		chunk.setLine(2);
		assertEquals(chunk.getLine(), 2);
	}
	
	/**
	 * Test method hadChild.
	 */
	@Test
	public void testChunkChilds() {
		assertEquals(chunk.hasChild(), false);
		Chunk test = new Chunk();
		chunk.setChunk(test);
		assertEquals(chunk.hasChild(), true);
	}

}
