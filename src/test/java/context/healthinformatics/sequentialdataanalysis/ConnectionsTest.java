package context.healthinformatics.sequentialdataanalysis;

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
	private Chunk c3;
	
	/**
	 * Before each method create a arraylist with chunks already.
	 */
	@Before
	public void before() {
		note = "test";
		chunks = new ArrayList<Chunk>();
		c1 = new Chunk();
		c1.setLine(1);
		c1.setCode("A");
		chunks.add(c1);
		c2 = new Chunk();
		chunks.add(c2);
		c3 = new Chunk();
		c3.setCode("B");
		c3.setLine(2);
		chunks.add(c3);
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
		c.connectToLine(c1, 2, note);
		assertEquals(c1.getPointer().get(c3), "test");
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
		assertEquals(c.getChunkByLine(2), c3);
	}
	
	/**
	 * Method which tests if an exception is thrown with incorrect input for the method.
	 * @throws Exception e
	 */
	@Test(expected = Exception.class)
	public void testGetChunkByLineException() throws Exception {
		Connections c = new Connections(chunks);
		c.getChunkByLine(-1);
	}
	
	/**
	 * Method which tests if a connection is made from chunks with code "A" to chunks with code "B".
	 * @throws Exception e
	 */
	@Test
	public void testConnectOnCode() throws Exception {
		Connections c = new Connections(chunks);
		c.connectOnCode("A", "B", note);
		assertEquals(c1.getPointer().get(c3), "test");
	}
}
