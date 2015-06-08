package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
	public void testChunkChildren() {
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

	/**
	 * Test the get children method.
	 */
	@Test
	public void testGetChildren() {
		ArrayList<Chunk> children = new ArrayList<Chunk>();
		children.add(new Chunk());
		chunk.setChunks(children);
		assertEquals(chunk.getChildren().size(), 1);
	}

	/**
	 * Tets the set sum method.
	 */
	@Test
	public void testSetSum() {
		chunk.setSum(2);
		assertEquals(chunk.toArray().get(0), "2");
	}

	/**
	 * Test the toString method.
	 */
	@Test
	public void testToString() {
		assertEquals(chunk.toString(),
				"Chunk [code=, pointer={}, comment=, chunks=[], line=0]");
	}

	/**
	 * Tets the toarray method with children.
	 */
	@Test
	public void testToArrayWithChild() {
		ArrayList<Chunk> children = new ArrayList<Chunk>();
		children.add(new Chunk());
		chunk.setChunks(children);
		assertEquals(chunk.toArray().get(0),
				"Chunk contains childs, code =  comment = ");
	}

	/**
	 * Test the copy chunk method.
	 */
	@Test
	public void testCopyChunk() {
		assertEquals(chunk.toString(), chunk.copy().toString());
	}

	/**
	 * Test the copy chunk method which are not equal.
	 */
	@Test
	public void testCopyChunkFalse() {
		Chunk c1 = chunk.copy();
		c1.setComment("testComment");

		assertFalse(c1.toString().equals(chunk.toString()));
	}

	/**
	 * Test the copy chunk with pointer.
	 */
	@Test
	public void testCopyChunkWithConnection() {
		HashMap<Chunk, String> point = new HashMap<Chunk, String>();
		point.put(new Chunk(), "connectionNote");
		chunk.setPointer(point);
		assertEquals(chunk.toString(), chunk.copy().toString());
	}

}
