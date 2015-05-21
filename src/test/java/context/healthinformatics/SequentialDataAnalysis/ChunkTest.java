package context.healthinformatics.SequentialDataAnalysis;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

/**
 * 
 * Test for chunk class.
 *
 */
public class ChunkTest {
	
	private Chunk chunk;
	
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
