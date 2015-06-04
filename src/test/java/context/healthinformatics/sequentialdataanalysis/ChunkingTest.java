package context.healthinformatics.sequentialdataanalysis;

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
	private Chunking testChunking;
	
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
		Chunk f = new Chunk();
		
		a.setChunk(b);
		a.setChunk(c);
		a.setCode("A");									//	B	code B 
		b.setCode("B");									//A		code A
		c.setCode("C");									//	C	code C
		d.setCode("A");									//D		code A	comment= Deze heeft code A
		d.setComment("Deze heeft code A");				//E		code E  comment= Deze heeft code E
		e.setCode("E");									//F		code A 	comment= Dit is F
		e.setComment("Deze heeft code E");
		f.setCode("A");
		f.setComment("Dit is F");
		test.add(a);
		test.add(d);
		test.add(e);
		test.add(f);
		testChunking = new Chunking();
		testChunking.setChunks(test);
	}
	
	/**
	 * Tests chunking chunks on code constraint.
	 */
	@Test
	public void testChunkOnConstraintCode() {
		ArrayList<Chunk> res = testChunking.constraintOnCode("A");
		final int three = 3;
		assertEquals(three, res.size());
		assertEquals(res.get(0).getChunks().get(0).getCode(), "A");
		assertEquals(res.get(0).getChunks().get(0).getChunks().get(0).getCode(), "B");
		assertEquals(res.get(0).getChunks().get(0).getChunks().get(1).getCode(), "C");
		assertEquals(res.get(1).getCode(), "E");
	}
	
	/**
	 * Tests chunking chunks on comment constraint.
	 */
	@Test
	public void testChunkOnContainsComment() {
		ArrayList<Chunk> res = testChunking.constraintOnContainsComment("E");
		final int size = 4;
		assertEquals(size, res.size());
		assertEquals(res.get(2).getChunks().get(0).getCode(), "E");
	}
	
	/**
	 * Tests chunking chunks on comment constraint.
	 */
	@Test
	public void testChunkOnEqualsComment() {
		ArrayList<Chunk> res = testChunking.constraintOnEqualsComment("Deze heeft code E");
		final int size = 4;
		assertEquals(size, res.size());
		assertEquals(res.get(2).getChunks().get(0).getCode(), "E");
	}
	
	/**
	 * Tests getChunks method.
	 */
	@Test
	public void testGetChunks() {
		assertEquals(testChunking.getChunks(), test);
	}

}
