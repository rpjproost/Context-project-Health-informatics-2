package context.healthinformatics.SequentialDataAnalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * Tests for chunking on chunks class.
 *
 */
public class ChunkingTest {
	
	private ArrayList<Chunk> test = new ArrayList<Chunk>();
	
	/**
	 * Sets up a few chunks for testing purposes.
	 */
	@Before
	public void before() {
		Chunk a = new Chunk();
		Chunk b = new Chunk();
		Chunk c = new Chunk();
		Chunk d = new Chunk();
		Chunk e = new Chunk();
		
		a.setChunk(b);
		a.setChunk(c);
		a.setCode("A");
		b.setCode("B");
		c.setCode("C");
		d.setCode("A");
		d.setComment("Deze heeft code A");
		e.setCode("E");
		e.setComment("Deze heeft code E");
		test.add(a);
		test.add(d);
		test.add(e);
	}
	
	/**
	 * Tests chunking chunks on code constraint.
	 */
	@Test
	public void testChunkOnConstraint() {
		Chunking testChunking = new Chunking(test);
		ArrayList<Chunk> res = testChunking.constraintOnCode("A");
		assertEquals(res.size(), 2);
		assertEquals(res.get(0).getChunks().get(0).getCode(), "A");
		assertEquals(res.get(0).getChunks().get(0).getChunks().get(0).getCode(), "B");
		assertEquals(res.get(0).getChunks().get(0).getChunks().get(1).getCode(), "C");
		assertEquals(res.get(1).getChunks().get(0).getCode(), "A");
		assertEquals(res.get(1).getChunks().get(0).getCode(), "A");
	}
	
	/**
	 * Tests chunking chunks on comment constraint.
	 */
	@Test
	public void testChunkOnContainsComment() {
		Chunking testChunking = new Chunking(test);
		ArrayList<Chunk> res = testChunking.constraintOnContainsComment("E");
		assertEquals(res.get(0).getChunks().get(0).getCode(), "E");
	}
	
	/**
	 * Tests chunking chunks on comment constraint.
	 */
	@Test
	public void testChunkOnEqualsComment() {
		Chunking testChunking = new Chunking(test);
		ArrayList<Chunk> res = testChunking.constraintOnEqualsComment("Deze heeft code E");
		assertEquals(res.get(0).getChunks().get(0).getCode(), "E");
	}
	
	/**
	 * Tests getChunks method.
	 */
	@Test
	public void testGetChunks() {
		Chunking testChunking = new Chunking(test);
		assertEquals(testChunking.getChunks(), test);
	}

}
