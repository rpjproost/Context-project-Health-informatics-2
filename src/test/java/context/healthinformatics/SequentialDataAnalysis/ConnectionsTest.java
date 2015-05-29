package context.healthinformatics.SequentialDataAnalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for class Connections.
 */
public class ConnectionsTest {

	private ArrayList<Chunk> chunks;
	private String note;
	private Chunk c1;
	private Chunk c2;
	
	/**
	 * Before each method create a arraylist with chunks already.
	 */
	@Before
	public void before() {
		note = "test";
		chunks = new ArrayList<Chunk>();
		c1 = new Chunk();
		c1.setLine(1);
		chunks.add(c1);
		c2 = new Chunk();
		chunks.add(c2);
	}
	
	/**
	 * Test the set connection between a chunk and a line method.
	 * 
	 * @throws Exception
	 *             maybe the connection couldn't be set.
	 */
	@Test
	public void testConnectToLine() throws Exception {
		Connections c = new Connections(chunks);
		c.connectToLine(c2, 1, note);
		assertEquals(c2.getPointer().get(c1), "test");
	}
	
	/**
	 * Test the set connection between a chunk and another chunk method.
	 * 
	 * @throws Exception
	 *             maybe the connection couldn't be set.
	 */
	@Test
	public void testConnectToChunk() throws Exception {
		Connections c = new Connections(chunks);
		c.connectToChunk(c1, c2, note);
		assertEquals(c1.getPointer().get(c2), "test");
	}
	
	/**
	 * Checks if a chunk at a line is fetched correctly.
	 * @throws Exception e
	 */
	@Test
	public void testGetChunks() throws Exception {
		Connections c = new Connections(chunks);
		assertEquals(c.getChunkByLine(1), c1);
	}
}
