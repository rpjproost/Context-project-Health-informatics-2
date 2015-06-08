package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

/**
 * 
 * Test for chunk class.
 *
 */
public class ChunkTest {

	private Chunk chunk = new Chunk();

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

	/**
	 * Test if the setPointer method works.
	 */
	@Test
	public void testSetPointer() {
		HashMap<Chunk, String> aPointer = new HashMap<Chunk, String>();
		aPointer.put(chunk, "testPointer");
		chunk.setPointer(aPointer);
		assertEquals(chunk.getPointer(), aPointer);
	}

	/**
	 * Test if the addPointer method works.
	 */
	@Test
	public void testAddPointer() {
		chunk.addPointer(chunk, "testPointer");
		assertTrue(chunk.getPointer().containsKey(chunk));
	}

}
