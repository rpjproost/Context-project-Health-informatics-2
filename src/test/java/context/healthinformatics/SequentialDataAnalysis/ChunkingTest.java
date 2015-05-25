package context.healthinformatics.SequentialDataAnalysis;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class ChunkingTest {
	
	ArrayList<Chunk> test = new ArrayList<Chunk>();
	
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
		test.add(b);
		test.add(c);
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

}
