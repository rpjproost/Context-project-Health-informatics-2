package context.healthinformatics.sequentialdataanalysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the Codes class.
 */
public class CodesTest {

	private ArrayList<Chunk> chunks;
	private String code = "A";

	/**
	 * Before each method create a arraylist with chunks already.
	 */
	@Before
	public void before() {
		chunks = new ArrayList<Chunk>();
		Chunk c1 = new Chunk();
		c1.setLine(1);
		chunks.add(c1);
		Chunk c2 = new Chunk();
		chunks.add(c2);
	}

	/**
	 * Test the set code on a line method.
	 * 
	 * @throws Exception
	 *             maybe the code couldn't be set.
	 */
	@Test
	public void testSetCodeOfLine() throws Exception {
		Codes codes = new Codes(chunks);
		codes.setCodeOfLine(1, code);
		assertEquals(code, chunks.get(0).getCode());
	}

	/**
	 * Test if the exception will be thrown if the line doesn't exists.
	 * 
	 * @throws Exception
	 *             the one that must be thrown.
	 */
	@Test(expected = Exception.class)
	public void testSetCodeOfLineException() throws Exception {
		Codes codes = new Codes(chunks);
		codes.setCodeOfLine(-1, code);
	}

	/**
	 * Test if you can set code on a chunk of chunks.
	 */
	@Test
	public void testSetCodeOfChunks() {
		ArrayList<Chunk> combined = new ArrayList<Chunk>();
		Chunk combi = new Chunk();
		combi.setChunks(chunks);
		combined.add(combi);

		Codes codes = new Codes(combined);
		codes.setCodeOfChunks(0, code);
		assertEquals(code, combined.get(0).getCode());
	}

	/**
	 * Test the exception handling of coding the chunk of chunks.
	 */
	@Test(expected = Exception.class)
	public void testSetCodeOfChunksException() {
		ArrayList<Chunk> combined = new ArrayList<Chunk>();
		Codes codes = new Codes(combined);
		codes.setCodeOfChunks(0, code);
	}
}
